package kelvin.slendermod.block;

import net.minecraft.block.*;
import net.minecraft.block.enums.BedPart;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

public class HospitalBedBlock extends BedBlock {

    public static final VoxelShape FOOT_SOUTH_SHAPE = VoxelShapes.union(
            Block.createCuboidShape(15.25, 8, 1, 15.75, 10, 16),
            Block.createCuboidShape(0.75, 8, 1, 15.25, 9, 16),
            Block.createCuboidShape(0.25, 8, 1, 0.75, 10, 16),
            Block.createCuboidShape(15, 0, 0, 16, 14, 1),
            Block.createCuboidShape(1, 8, 0, 15, 11, 0.75),
            Block.createCuboidShape(0, 0, 0, 1, 14, 1),
            Block.createCuboidShape(1, 4.25, 0.25, 15, 4.75, 0.75));

    public static final VoxelShape FOOT_EAST_SHAPE = VoxelShapes.union(
            Block.createCuboidShape(1, 8, 0.25, 16, 10, 0.75),
            Block.createCuboidShape(1, 8, 0.75, 16, 9, 15.25),
            Block.createCuboidShape(1, 8, 15.25, 16, 10, 15.75),
            Block.createCuboidShape(0, 0, 0, 1, 14, 1),
            Block.createCuboidShape(0, 8, 1, 0.75, 11, 15),
            Block.createCuboidShape(0, 0, 15, 1, 14, 16),
            Block.createCuboidShape(0.25, 4.25, 1, 0.75, 4.75, 15));

    public static final VoxelShape FOOT_NORTH_SHAPE = VoxelShapes.union(
            Block.createCuboidShape(0.25, 8, 0, 0.75, 10, 15),
            Block.createCuboidShape(0.75, 8, 0, 15.25, 9, 15),
            Block.createCuboidShape(15.25, 8, 0, 15.75, 10, 15),
            Block.createCuboidShape(0, 0, 15, 1, 14, 16),
            Block.createCuboidShape(1, 8, 15.25, 15, 11, 16),
            Block.createCuboidShape(15, 0, 15, 16, 14, 16),
            Block.createCuboidShape(1, 4.25, 15.25, 15, 4.75, 15.75));

    public static final VoxelShape FOOT_WEST_SHAPE = VoxelShapes.union(
            Block.createCuboidShape(0, 8, 15.25, 15, 10, 15.75),
            Block.createCuboidShape(0, 8, 0.75, 15, 9, 15.25),
            Block.createCuboidShape(0, 8, 0.25, 15, 10, 0.75),
            Block.createCuboidShape(15, 0, 15, 16, 14, 16),
            Block.createCuboidShape(15.25, 8, 1, 16, 11, 15),
            Block.createCuboidShape(15, 0, 0, 16, 14, 1),
            Block.createCuboidShape(15.25, 4.25, 1, 15.75, 4.75, 15));

    public static final VoxelShape HEAD_NORTH_SHAPE = VoxelShapes.union(
            Block.createCuboidShape(0.25, 8, 1, 0.75, 10, 16),
            Block.createCuboidShape(0.75, 8, 1, 15.25, 9, 16),
            Block.createCuboidShape(15.25, 8, 1, 15.75, 10, 16),
            Block.createCuboidShape(0, 0, 0, 1, 12, 1),
            Block.createCuboidShape(1, 4.25, 0.25, 15, 4.75, 0.75),
            Block.createCuboidShape(15, 0, 0, 16, 12, 1),
            Block.createCuboidShape(1, 8, 0.25, 15, 11, 1));

    public static final VoxelShape HEAD_WEST_SHAPE = VoxelShapes.union(
            Block.createCuboidShape(1, 8, 15.25, 16, 10, 15.75),
            Block.createCuboidShape(1, 8, 0.75, 16, 9, 15.25),
            Block.createCuboidShape(1, 8, 0.25, 16, 10, 0.75),
            Block.createCuboidShape(0, 0, 15, 1, 12, 16),
            Block.createCuboidShape(0.25, 4.25, 1, 0.75, 4.75, 15),
            Block.createCuboidShape(0, 0, 0, 1, 12, 1),
            Block.createCuboidShape(0.25, 8, 1, 1, 11, 15));

    public static final VoxelShape HEAD_SOUTH_SHAPE = VoxelShapes.union(
            Block.createCuboidShape(15.25, 8, 0, 15.75, 10, 15),
            Block.createCuboidShape(0.75, 8, 0, 15.25, 9, 15),
            Block.createCuboidShape(0.25, 8, 0, 0.75, 10, 15),
            Block.createCuboidShape(15, 0, 15, 16, 12, 16),
            Block.createCuboidShape(1, 4.25, 15.25, 15, 4.75, 15.75),
            Block.createCuboidShape(0, 0, 15, 1, 12, 16),
            Block.createCuboidShape(1, 8, 15, 15, 11, 15.75));

    public static final VoxelShape HEAD_EAST_SHAPE = VoxelShapes.union(
            Block.createCuboidShape(0, 8, 0.25, 15, 10, 0.75),
            Block.createCuboidShape(0, 8, 0.75, 15, 9, 15.25),
            Block.createCuboidShape(0, 8, 15.25, 15, 10, 15.75),
            Block.createCuboidShape(15, 0, 0, 16, 12, 1),
            Block.createCuboidShape(15.25, 4.25, 1, 15.75, 4.75, 15),
            Block.createCuboidShape(15, 0, 15, 16, 12, 16),
            Block.createCuboidShape(15, 8, 1, 15.75, 11, 15));

    public HospitalBedBlock(DyeColor color, Settings settings) {
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
        }
        else {
            return switch (direction) {
                case WEST -> FOOT_WEST_SHAPE;
                case SOUTH -> FOOT_SOUTH_SHAPE;
                case EAST -> FOOT_EAST_SHAPE;
                default -> FOOT_NORTH_SHAPE;
            };
        }
    }
}
