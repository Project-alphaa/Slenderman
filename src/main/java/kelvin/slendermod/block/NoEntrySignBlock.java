package kelvin.slendermod.block;

import net.minecraft.block.*;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("deprecation")
public class NoEntrySignBlock extends HorizontalFacingBlock {
    private final VoxelShape SHAPE_N = VoxelShapes.combineAndSimplify(Block.createCuboidShape(4.5, 17, 7.125, 11.5, 24, 7.625), Block.createCuboidShape(7.5, 0, 7.5, 8.5, 23, 8.5), BooleanBiFunction.OR);

    private final VoxelShape SHAPE_E = VoxelShapes.combineAndSimplify(Block.createCuboidShape(8.5, 17, 4.625, 9, 24, 11.625), Block.createCuboidShape(7.625, 0, 7.625, 8.625, 23, 8.625), BooleanBiFunction.OR);

    private final VoxelShape SHAPE_S = VoxelShapes.combineAndSimplify(Block.createCuboidShape(4.5, 17, 8.625, 11.5, 24, 9.125), Block.createCuboidShape(7.5, 0, 7.75, 8.5, 23, 8.75), BooleanBiFunction.OR);

    private final VoxelShape SHAPE_W = VoxelShapes.combineAndSimplify(Block.createCuboidShape(7, 17, 4.625, 7.5, 24, 11.625), Block.createCuboidShape(7.375, 0, 7.625, 8.375, 23, 8.625), BooleanBiFunction.OR);

    private static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;

    public NoEntrySignBlock(Settings settings) {
        super(settings);
    }

    @Override
    public PistonBehavior getPistonBehavior(BlockState state) {
        return PistonBehavior.IGNORE;
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext context) {
        return this.getDefaultState().with(FACING, context.getPlayerFacing().getOpposite());
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return switch (state.get(FACING)) {
            case NORTH -> this.SHAPE_N;
            case EAST -> this.SHAPE_E;
            case SOUTH -> this.SHAPE_S;
            case WEST -> this.SHAPE_W;
            case UP, DOWN -> throw new RuntimeException("Unsupported block state types");
        };
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }
}
