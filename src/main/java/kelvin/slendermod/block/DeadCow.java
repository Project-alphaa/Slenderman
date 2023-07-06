package kelvin.slendermod.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;

public class DeadCow extends DeadEntityBlock {
    public static final VoxelShape SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 10.0, 16.0);
    public DeadCow(AbstractBlock.Settings settings) {
        super(settings, 5);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        Direction direction = state.get(FACING);
        return switch (direction) {
            case NORTH, SOUTH, EAST, WEST -> SHAPE;
            default -> throw new IllegalStateException();
        };
    }
}

