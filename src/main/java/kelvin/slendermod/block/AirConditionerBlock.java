package kelvin.slendermod.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;

public class AirConditionerBlock extends HorizontalFacingBlock {

    private static final VoxelShape NORTH_SOUTH_SHAPE = Block.createCuboidShape(-1.5, 0, 4.5, 17.5, 12, 11.5);
    private static final VoxelShape EAST_WEST_SHAPE = Block.createCuboidShape(4.5, 0, -1.5, 11.5, 12, 17.5);

    public AirConditionerBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH));
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        Direction facing = state.get(FACING);
        if (facing == Direction.DOWN || facing == Direction.UP) {
            throw new IllegalStateException();
        }
        return facing == Direction.EAST || facing == Direction.WEST ? EAST_WEST_SHAPE : NORTH_SOUTH_SHAPE;
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return super.getPlacementState(ctx).with(FACING, ctx.getPlayerFacing().getOpposite());
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }
}
