package kelvin.slendermod.multiblock;

import kelvin.slendermod.registry.BlockRegistry;
import kelvin.slendermod.util.MultiblockPositioner;
import kelvin.slendermod.util.Point;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.function.BiFunction;

import static kelvin.slendermod.SlenderMod.MOD_ID;

public class MultiBlock extends BlockWithEntity {
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;
    public static final BooleanProperty MAIN_BLOCK = BooleanProperty.of("main_block");
    public static final BooleanProperty MODEL_BLOCK = BooleanProperty.of("model_block");


    protected int WIDTH;
    protected int HEIGHT;
    protected int DEPTH;

    protected ArrayList<Point> POINTS;
    protected final int MAX_BLOCKS;

    protected int MODEL_OFFSET_X;
    protected int MODEL_OFFSET_Z;

    protected int BLOCK_OFFSET_X;
    protected int BLOCK_OFFSET_Z;

    private final boolean HOLLOW;
    private final boolean SHAPED;

    public MultiBlock(Settings settings, MultiblockPositioner positioner) {
        super(settings.nonOpaque());

        Vec3i size = positioner.getSize();
        Vec3i modelOffset = positioner.getModelOffset();
        Vec3i blockCenter = positioner.isCustomCenter() ? positioner.getCustomBlockCenter() : null;

        WIDTH = size.getX();
        HEIGHT = size.getY();
        DEPTH = size.getZ();

        MODEL_OFFSET_X = modelOffset != null ? modelOffset.getX() : 0;
        MODEL_OFFSET_Z = modelOffset != null ? modelOffset.getZ() : 0;

        BLOCK_OFFSET_X = (WIDTH - 1) / 2;
        BLOCK_OFFSET_X = positioner.isBlockCenterX() ? (WIDTH - 1) / 2 : BLOCK_OFFSET_X;
        BLOCK_OFFSET_X = blockCenter != null ? blockCenter.getX() : BLOCK_OFFSET_X;

        BLOCK_OFFSET_Z = DEPTH;
        BLOCK_OFFSET_Z = positioner.isBlockCenterX() ? (DEPTH + 1) / 2 : BLOCK_OFFSET_Z;
        BLOCK_OFFSET_Z = blockCenter != null ? blockCenter.getZ() : BLOCK_OFFSET_Z;

        HOLLOW = positioner.isHollow();
        SHAPED = positioner.isShaped();

        if (!SHAPED) {
            MAX_BLOCKS = WIDTH * HEIGHT * DEPTH;
        } else {
            POINTS = positioner.getPoints();
            MAX_BLOCKS = positioner.getPoints().size();
        }
        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH).with(MAIN_BLOCK, true).with(MODEL_BLOCK, false));
    }

    @Override
    public float getAmbientOcclusionLightLevel(BlockState state, BlockView world, BlockPos pos) {
        return 1.0F;
    }


    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockPos pos = ctx.getBlockPos();
        World world = ctx.getWorld();
        BlockState state = this.getDefaultState().with(FACING, ctx.getPlayerFacing().getOpposite());

        if (pos.getY() < world.getTopY() - 1 && this.preformOnAll(world, state, pos, (worldPos, relativePos) -> world.getBlockState(worldPos).canReplace(ctx), false)) {
            return state;
        }
        return null;
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        MultiBlockEntity mainEntity = (MultiBlockEntity) world.getBlockEntity(pos);
        BlockPos modelPos = new BlockPos((WIDTH - 1) - MODEL_OFFSET_X, 0, (DEPTH - 1) - MODEL_OFFSET_Z);

        this.preformOnAll(world, state, pos, (worldPos, relativePos) -> {
            if (!worldPos.equals(pos) || relativePos.equals(modelPos)) {
                world.setBlockState(worldPos, state.with(MAIN_BLOCK, false).with(MODEL_BLOCK, relativePos.equals(modelPos)));

                MultiBlockEntity dummyEntity = (MultiBlockEntity) world.getBlockEntity(worldPos);
                if (dummyEntity != null && mainEntity != null) {
                    dummyEntity.setMainBlock(mainEntity.getMainBlock());
                }
            }
            return true;
        }, false);

    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!world.isClient) {
            MultiBlockEntity blockEntity = (MultiBlockEntity) world.getBlockEntity(pos);
            if (blockEntity != null) {
                BlockPos mainPos = blockEntity.getMainBlock();
                this.preformOnAll(world, state, mainPos, (worldPos, relativePos) -> {
                    world.removeBlock(worldPos, false);
                    return true;
                }, false);
            }
        }
        super.onBreak(world, pos, state, player);
    }

    public final boolean preformOnAll(World world, BlockState state, BlockPos pos, BiFunction<BlockPos, Vec3i, Boolean> function, boolean swapAxis) {
        Direction facing = state.get(FACING);
        Direction clockwise = swapAxis ? facing : facing.rotateYClockwise();
        BlockPos startPos = pos.offset(facing.getOpposite(), swapAxis ? BLOCK_OFFSET_X : BLOCK_OFFSET_Z).offset(clockwise, swapAxis ? BLOCK_OFFSET_Z : BLOCK_OFFSET_X);

        int blocksSet = 0;
        for (int height = 0; height < HEIGHT; height++) {
            BlockPos offsetPos = startPos.up(height);
            for (int width = 0; width < WIDTH; width++) {
                BlockPos rowPos = offsetPos;
                offsetPos = offsetPos.offset(clockwise.getOpposite());
                for (int depth = 0; depth < DEPTH; depth++) {
                    rowPos = rowPos.offset(facing);

                    if (SHAPED) {
                        Vec3i relativePos = new Vec3i(width, height, depth);
                        BlockState possibleMain = world.getBlockState(rowPos);
                        for (Point i : POINTS) {
                            if (i.asVec().equals(relativePos) || (possibleMain.isOf(this) && possibleMain.get(MAIN_BLOCK))) {
                                if (function.apply(rowPos, relativePos)) {
                                    blocksSet++;
                                }
                                break;
                            }
                        }
                        continue;
                    }
                    if (HOLLOW) {
                        if (width != 0 && depth != 0) {
                            if (width != WIDTH - 1 && depth != DEPTH - 1) {
                                blocksSet++;
                                continue;
                            }
                        }
                    }
                    if (function.apply(rowPos, new Vec3i(width, height, depth))) {
                        blocksSet++;
                    }
                }
            }
        }
        return blocksSet == this.MAX_BLOCKS;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return state.get(MultiBlock.MODEL_BLOCK) ? BlockRenderType.MODEL : BlockRenderType.INVISIBLE;
        //return BlockRenderType.MODEL;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, MAIN_BLOCK, MODEL_BLOCK);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return null;
    }

}