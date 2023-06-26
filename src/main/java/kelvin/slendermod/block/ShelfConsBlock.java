package kelvin.slendermod.block;

import net.minecraft.block.*;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

public class ShelfConsBlock extends CustomFacingBlock {

    public static final VoxelShape UPPER_NS_SHAPE = Block.createCuboidShape(0, 0, 2, 16, 14, 14);
    public static final VoxelShape UPPER_EW_SHAPE = Block.createCuboidShape(2, 0, 0, 14, 14, 16);
    public static final VoxelShape LOWER_NS_SHAPE = Block.createCuboidShape(0, 0, 2, 16, 16, 14);
    public static final VoxelShape LOWER_EW_SHAPE = Block.createCuboidShape(2, 0, 0, 14, 16, 16);

    public static final EnumProperty<DoubleBlockHalf> HALF = Properties.DOUBLE_BLOCK_HALF;

    public ShelfConsBlock(Settings settings) {
        super(settings);
        setDefaultState(stateManager.getDefaultState().with(HALF, DoubleBlockHalf.LOWER));
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        Direction direction = state.get(FACING);
        DoubleBlockHalf half = state.get(HALF);
        if (half == DoubleBlockHalf.LOWER) {
            return switch (direction) {
                case EAST, WEST -> LOWER_EW_SHAPE;
                default -> LOWER_NS_SHAPE;
            };
        } 
        else {
            return switch (direction) {
                case EAST, WEST -> UPPER_EW_SHAPE;
                default -> UPPER_NS_SHAPE;
            };
        }
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (direction == getDirectionTowardsOtherHalf(state.get(HALF))) {
            return neighborState.isOf(this) && neighborState.get(HALF) != state.get(HALF) ? state : Blocks.AIR.getDefaultState();
        }
        else {
            return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
        }
    }

    private static Direction getDirectionTowardsOtherHalf(DoubleBlockHalf half) {
        return half == DoubleBlockHalf.LOWER ? Direction.UP : Direction.DOWN;
    }

    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!world.isClient && player.isCreative()) {
            DoubleBlockHalf doubleBlockHalf = state.get(HALF);
            if (doubleBlockHalf == DoubleBlockHalf.LOWER) {
                BlockPos blockPos = pos.offset(getDirectionTowardsOtherHalf(doubleBlockHalf));
                BlockState blockState = world.getBlockState(blockPos);
                if (blockState.isOf(this) && blockState.get(HALF) == DoubleBlockHalf.UPPER) {
                    world.setBlockState(blockPos, Blocks.AIR.getDefaultState(), 35);
                    world.syncWorldEvent(player, 2001, blockPos, Block.getRawIdFromState(blockState));
                }
            }
        }

        super.onBreak(world, pos, state, player);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        Direction direction = ctx.getPlayerFacing();
        BlockPos blockPos = ctx.getBlockPos();
        BlockPos blockPos2 = blockPos.offset(Direction.UP);
        World world = ctx.getWorld();
        return world.getBlockState(blockPos2).canReplace(ctx) && world.getWorldBorder().contains(blockPos2) ? getDefaultState().with(FACING, direction) : null;
    }

    @Override
    public PistonBehavior getPistonBehavior(BlockState state) {
        return PistonBehavior.DESTROY;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, HALF);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);
        if (!world.isClient) {
            BlockPos blockPos = pos.offset(Direction.UP);
            world.setBlockState(blockPos, state.with(HALF, DoubleBlockHalf.UPPER), 3);
            world.updateNeighbors(pos, Blocks.AIR);
            state.updateNeighbors(world, pos, 3);
        }
    }
}
