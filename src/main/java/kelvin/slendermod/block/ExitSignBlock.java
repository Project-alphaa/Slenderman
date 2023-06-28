package kelvin.slendermod.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;

public class ExitSignBlock extends CustomFacingBlock {

    public static final VoxelShape NORTH_SOUTH_SHAPE = Block.createCuboidShape(0.05, 4, 5.35, 15.95, 11.95, 10.65);
    public static final VoxelShape EAST_WEST_SHAPE = Block.createCuboidShape(5.35, 4, 0.05, 10.65, 11.95, 15.95);

    public ExitSignBlock(Settings settings) {
        super(settings);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        Direction direction = state.get(FACING);
        return switch (direction) {
            case EAST, WEST -> EAST_WEST_SHAPE;
            default -> NORTH_SOUTH_SHAPE;
        };
    }
}
