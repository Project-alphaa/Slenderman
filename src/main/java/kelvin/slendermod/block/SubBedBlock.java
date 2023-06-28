package kelvin.slendermod.block;

import net.minecraft.block.*;
import net.minecraft.block.enums.BedPart;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

public class SubBedBlock extends BedBlock {

    public static final VoxelShape HEAD_SOUTH_SHAPE = VoxelShapes.union(Block.createCuboidShape(2, 1, 0, 15, 3, 13), Block.createCuboidShape(2.5, 3, 0, 14.5, 5, 13), Block.createCuboidShape(2, 3, 4.5, 15, 5.5, 8.5), Block.createCuboidShape(1, 6, 13, 16, 8, 16), Block.createCuboidShape(2, 0, 13, 5, 6, 15), Block.createCuboidShape(12, 0, 13, 15, 6, 15));

    public static final VoxelShape HEAD_EAST_SHAPE = VoxelShapes.union(Block.createCuboidShape(0, 1, 1, 13, 3, 14), Block.createCuboidShape(0, 3, 1.5, 13, 5, 13.5), Block.createCuboidShape(4.5, 3, 1, 8.5, 5.5, 14), Block.createCuboidShape(13, 6, 0, 16, 8, 15), Block.createCuboidShape(13, 0, 11, 15, 6, 14), Block.createCuboidShape(13, 0, 1, 15, 6, 4));

    public static final VoxelShape HEAD_NORTH_SHAPE = VoxelShapes.union(Block.createCuboidShape(1, 1, 3, 14, 3, 16), Block.createCuboidShape(1.5, 3, 3, 13.5, 5, 16), Block.createCuboidShape(1, 3, 7.5, 14, 5.5, 11.5), Block.createCuboidShape(3.25, 1.25, 2.75, 11.75, 3.5, 6.75), Block.createCuboidShape(0, 6, 0, 15, 8, 3), Block.createCuboidShape(11, 0, 1, 14, 6, 3), Block.createCuboidShape(1, 0, 1, 4, 6, 3));

    public static final VoxelShape HEAD_WEST_SHAPE = VoxelShapes.union(Block.createCuboidShape(3, 1, 2, 16, 3, 15), Block.createCuboidShape(3, 3, 2.5, 16, 5, 14.5), Block.createCuboidShape(7.5, 3, 2, 11.5, 5.5, 15), Block.createCuboidShape(0, 6, 1, 3, 8, 16), Block.createCuboidShape(1, 0, 2, 3, 6, 5), Block.createCuboidShape(1, 0, 12, 3, 6, 15));

    public static final VoxelShape FOOT_NORTH_SHAPE = VoxelShapes.union(Block.createCuboidShape(0, 6, 13, 15, 8, 16), Block.createCuboidShape(11, 0, 13, 14, 6, 15), Block.createCuboidShape(1, 0, 13, 4, 6, 15), Block.createCuboidShape(1.5, 3, 0, 13.5, 5, 13), Block.createCuboidShape(1, 1, 0, 14, 3, 13));

    public static final VoxelShape FOOT_WEST_SHAPE = VoxelShapes.union(Block.createCuboidShape(13, 6, 1, 16, 8, 16), Block.createCuboidShape(13, 0, 2, 15, 6, 5), Block.createCuboidShape(13, 0, 12, 15, 6, 15), Block.createCuboidShape(0, 3, 2.5, 13, 5, 14.5), Block.createCuboidShape(0, 1, 2, 13, 3, 15));

    public static final VoxelShape FOOT_SOUTH_SHAPE = VoxelShapes.union(Block.createCuboidShape(1, 6, 0, 16, 8, 3), Block.createCuboidShape(2, 0, 1, 5, 6, 3), Block.createCuboidShape(12, 0, 1, 15, 6, 3), Block.createCuboidShape(2.5, 3, 3, 14.5, 5, 16), Block.createCuboidShape(2, 1, 3, 15, 3, 16));

    public static final VoxelShape FOOT_EAST_SHAPE = VoxelShapes.union(Block.createCuboidShape(0, 6, 0, 3, 8, 15), Block.createCuboidShape(1, 0, 11, 3, 6, 14), Block.createCuboidShape(1, 0, 1, 3, 6, 4), Block.createCuboidShape(3, 3, 1.5, 16, 5, 13.5), Block.createCuboidShape(3, 1, 1, 16, 3, 14));

    public SubBedBlock(DyeColor color, Settings settings) {
        super(color, settings);
    }

    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        Direction direction = state.get(FACING);
        BedPart part = state.get(PART);
        if (part == BedPart.HEAD) {
            return switch (direction) {
                case WEST -> HEAD_WEST_SHAPE;
                case SOUTH -> HEAD_SOUTH_SHAPE;
                case EAST -> HEAD_EAST_SHAPE;
                default -> HEAD_NORTH_SHAPE;
            };
        } else {
            return switch (direction) {
                case WEST -> FOOT_WEST_SHAPE;
                case SOUTH -> FOOT_SOUTH_SHAPE;
                case EAST -> FOOT_EAST_SHAPE;
                default -> FOOT_NORTH_SHAPE;
            };
        }
    }
}
