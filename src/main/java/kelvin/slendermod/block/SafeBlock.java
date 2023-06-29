package kelvin.slendermod.block;

import kelvin.slendermod.blockentity.SafeBlockEntity;
import kelvin.slendermod.client.screen.handler.SafeScreenHandler;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

@SuppressWarnings("deprecation")
public class SafeBlock extends BlockWithEntity {
    private static final VoxelShape SHAPE_N = Block.createCuboidShape(1, 0, 4, 15, 16, 14);
    private static final VoxelShape SHAPE_E = Block.createCuboidShape(4, 0, 1, 14, 16, 15);
    private static final VoxelShape SHAPE_S = Block.createCuboidShape(1, 0, 2, 15, 16, 12);
    private static final VoxelShape SHAPE_W = Block.createCuboidShape(2, 0, 1, 12, 16, 15);
    private static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;

    public static final Text TITLE = Text.translatable("container.slendermod.safe");
    private static final VoxelShape SHAPE = Stream.of(Block.createCuboidShape(1, 0, 4, 3, 16, 14), Block.createCuboidShape(3, 14, 4, 13, 16, 14), Block.createCuboidShape(13, 0, 4, 15, 16, 14), Block.createCuboidShape(3, 0, 4, 13, 2, 14), Block.createCuboidShape(3, 2, 12, 13, 14, 14), Block.createCuboidShape(3, 2, 4, 13, 14, 6)).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR)).get();

    public SafeBlock(Settings settings) {
        super(settings);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new SafeBlockEntity(pos, state);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.getBlockEntity(pos) instanceof SafeBlockEntity safeBlockEntity) {
            if (!safeBlockEntity.isOpen()) {
                safeBlockEntity.setOpen(true);
                player.openHandledScreen(state.createScreenHandlerFactory(world, pos));
                return ActionResult.SUCCESS;
            }
        }

        return ActionResult.CONSUME;
    }
    
    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext context) {
        return this.getDefaultState().with(FACING, context.getPlayerFacing().getOpposite());
    }
    

    @Nullable
    @Override
    public NamedScreenHandlerFactory createScreenHandlerFactory(BlockState state, World world, BlockPos pos) {
        if (world.getBlockEntity(pos) instanceof SafeBlockEntity safeBlockEntity) {
            return new SimpleNamedScreenHandlerFactory((syncId, inv, player) -> new SafeScreenHandler(syncId, inv, safeBlockEntity, ScreenHandlerContext.create(world, pos)), TITLE);
        }
        return null;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return switch (state.get(FACING)) {
            case NORTH -> SHAPE_N;
            case EAST -> SHAPE_W;
            case SOUTH -> SHAPE_S;
            case WEST -> SHAPE_E;
            case UP, DOWN -> throw new RuntimeException("Unsupported block state type");
        };
    }
    
    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.isOf(newState.getBlock())) {
            if (world.getBlockEntity(pos) instanceof SafeBlockEntity safeBlockEntity) {
                if (world instanceof ServerWorld) {
                    ItemScatterer.spawn(world, pos, safeBlockEntity);

                }
                world.updateComparators(pos, this);
            }
            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }

    @Override
    public boolean hasComparatorOutput(BlockState state) {
        return true;
    }

    @Override
    public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
        if (world.getBlockEntity(pos) instanceof SafeBlockEntity safeBlockEntity) {
            return safeBlockEntity.getItem().isEmpty() ? 0 : 1;
        }
        return 0;
    }
}
