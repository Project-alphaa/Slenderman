package kelvin.slendermod.block;


import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

public class BonesBlock extends HorizontalFacingBlock {

    public static final VoxelShape NORTH_SHAPE = Block.createCuboidShape(1.0, 0.0, 0.0, 14.0, 6.0, 16.0);
    public static final VoxelShape SOUTH_SHAPE = Block.createCuboidShape(2.0, 0.0, 0.0, 15.0, 6.0, 16.0);
    public static final VoxelShape WEST_SHAPE = Block.createCuboidShape(0.0, 0.0, 2.0, 16.0, 6.0, 15.0);
    public static final VoxelShape EAST_SHAPE = Block.createCuboidShape(0.0, 0.0, 1.0, 16.0, 6.0, 14.0);

    public BonesBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH));
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        Direction direction = state.get(FACING);
        return switch (direction) {
            case SOUTH -> SOUTH_SHAPE;
            case WEST -> WEST_SHAPE;
            case EAST -> EAST_SHAPE;
            default -> NORTH_SHAPE;
        };
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getPlayerFacing().getOpposite());
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        return world.getBlockState(pos.down()).isSideSolidFullSquare(world,pos,Direction.UP);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        return this.canPlaceAt(state,world,pos) ? state :  Blocks.AIR.getDefaultState();
    }
}
