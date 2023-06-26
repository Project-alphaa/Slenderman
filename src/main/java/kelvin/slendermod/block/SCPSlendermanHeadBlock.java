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

public class SCPSlendermanHeadBlock extends HorizontalFacingBlock {

    public static final VoxelShape NORTH_SHAPE = Block.createCuboidShape(3.8, 0, 4.9, 12.2, 11.705, 12.46);
    public static final VoxelShape SOUTH_SHAPE = Block.createCuboidShape(3.8, 0, 3.54, 12.2, 11.705, 11.1);
    public static final VoxelShape EAST_SHAPE = Block.createCuboidShape(3.54, 0, 3.8, 11.1, 11.705, 12.2);
    public static final VoxelShape WEST_SHAPE = Block.createCuboidShape(4.9, 0, 3.8, 12.46, 11.705, 12.2);

    public SCPSlendermanHeadBlock(Settings settings) {
        super(settings);
        setDefaultState(stateManager.getDefaultState().with(FACING, Direction.NORTH));
    }

    @Override
    public boolean isShapeFullCube(BlockState state, BlockView world, BlockPos pos) {
        return false;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        Direction direction = state.get(FACING);
        return switch (direction) {
            case SOUTH -> SOUTH_SHAPE;
            case EAST -> EAST_SHAPE;
            case WEST -> WEST_SHAPE;
            default -> NORTH_SHAPE;
        };
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return getDefaultState().with(FACING, ctx.getPlayerFacing().getOpposite());
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }
}
