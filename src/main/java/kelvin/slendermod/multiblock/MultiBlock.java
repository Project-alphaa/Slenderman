package kelvin.slendermod.multiblock;

import kelvin.slendermod.util.MultiblockPositioner;
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
import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.function.BiFunction;

public class MultiBlock extends BlockWithEntity {
    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;
    public static final BooleanProperty MAIN_BLOCK = BooleanProperty.of("main_block");
    public static final BooleanProperty MODEL_BLOCK = BooleanProperty.of("model_block");


    protected int WIDTH;
    protected int HEIGHT;
    protected int DEPTH;
    protected final int MAX_BLOCKS;

    protected int MODEL_OFFSET_X;
    protected int MODEL_OFFSET_Z;

    protected int BLOCK_OFFSET_X;
    protected int BLOCK_OFFSET_Z;

    private final boolean HOLLOW;

    public MultiBlock(Settings settings, MultiblockPositioner positioner) {
        super(settings.nonOpaque());
        BlockPos size = positioner.getSize();
        BlockPos modelOffset = positioner.getModelOffset();

        WIDTH = size.getX();
        HEIGHT = size.getY();
        DEPTH = size.getZ();
        MAX_BLOCKS = WIDTH * HEIGHT * DEPTH;

        MODEL_OFFSET_X = modelOffset != null ? modelOffset.getX() : 0;
        MODEL_OFFSET_Z = modelOffset != null ? modelOffset.getZ() : 0;


        BLOCK_OFFSET_X = (WIDTH-1)/2;
        BLOCK_OFFSET_Z = DEPTH;

        HOLLOW = positioner.getIsHollow();

        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH).with(MAIN_BLOCK, true).with(MODEL_BLOCK, false));
    }

    public MultiBlock(Settings settings, MultiblockPositioner positioner, int blockOffsetX) {
        super(settings.nonOpaque());
        BlockPos size = positioner.getSize();
        BlockPos modelOffset = positioner.getModelOffset();

        WIDTH = size.getX();
        HEIGHT = size.getY();
        DEPTH = size.getZ();
        MAX_BLOCKS = WIDTH * HEIGHT * DEPTH;

        MODEL_OFFSET_X = modelOffset != null ? modelOffset.getX() : 0;
        MODEL_OFFSET_Z = modelOffset != null ? modelOffset.getZ() : 0;


        BLOCK_OFFSET_X = blockOffsetX;
        BLOCK_OFFSET_Z = DEPTH;

        HOLLOW = positioner.getIsHollow();

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

        if (pos.getY() < world.getTopY() - 1 && this.preformOnAll(world, state, pos, (worldPos, relativePos) -> world.getBlockState(worldPos).canReplace(ctx))) {
            return state;
        }
        return null;
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        MultiBlockEntity mainEntity = (MultiBlockEntity) world.getBlockEntity(pos);
        BlockPos modelPos = new BlockPos((WIDTH-1)-MODEL_OFFSET_X, 0, (DEPTH-1)-MODEL_OFFSET_Z);

        this.preformOnAll(world, state, pos, (worldPos, relativePos) -> {
            if (!worldPos.equals(pos) || relativePos.equals(modelPos)) {
                world.setBlockState(worldPos, state.with(MAIN_BLOCK, false).with(MODEL_BLOCK, relativePos.equals(modelPos)));
                MultiBlockEntity dummyEntity = (MultiBlockEntity) world.getBlockEntity(worldPos);
                if (dummyEntity != null && mainEntity != null) {
                    dummyEntity.setMainBlock(mainEntity.getMainBlock());
                }
            }
            return true;
        });
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
                });
            }
        }
        super.onBreak(world, pos, state, player);
    }

    public final boolean preformOnAll(World world, BlockState state, BlockPos pos, BiFunction<BlockPos, Vec3i, Boolean> function) {
        Direction facing = state.get(FACING);
        Direction clockwise = facing.rotateYClockwise();
        BlockPos startPos = pos.offset(facing.getOpposite(), BLOCK_OFFSET_Z).offset(clockwise, BLOCK_OFFSET_X);

        int blocksSet = 0;
        for (int height = 0; height < HEIGHT; height++) {
            BlockPos offsetPos = startPos.up(height);
            for (int width = 0; width < WIDTH; width++) {
                BlockPos rowPos = offsetPos;
                offsetPos = offsetPos.offset(clockwise.getOpposite());
                for (int depth = 0; depth < DEPTH; depth++) {
                    rowPos = rowPos.offset(facing);
                    if (HOLLOW) {
                        if (width != 0 && depth != 0) {
                            if (width != WIDTH-1 && depth != DEPTH-1) {
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