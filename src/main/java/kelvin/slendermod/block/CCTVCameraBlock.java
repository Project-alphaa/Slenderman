package kelvin.slendermod.block;

import net.minecraft.block.*;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;

public class CCTVCameraBlock extends HorizontalFacingBlock {

    private static final VoxelShape NORTH_SOUTH_SHAPE = Block.createCuboidShape(5.5, 0, 0, 10.5, 16, 16);
    private static final VoxelShape EAST_WEST_SHAPE = Block.createCuboidShape(0, 0, 5.5, 16, 16, 10.5);

    public CCTVCameraBlock(AbstractBlock.Settings settings) {
        super(settings);
        setDefaultState(stateManager.getDefaultState().with(FACING, Direction.NORTH));
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        Direction direction = state.get(FACING);
        return switch (direction) {
            case EAST, WEST -> EAST_WEST_SHAPE;
            case NORTH, SOUTH -> NORTH_SOUTH_SHAPE;
            default -> throw new IllegalStateException();
        };
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
