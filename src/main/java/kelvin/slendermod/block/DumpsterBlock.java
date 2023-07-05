package kelvin.slendermod.block;

import kelvin.slendermod.SlenderMod;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiFunction;
import java.util.function.Function;

@SuppressWarnings("deprecation")
public class DumpsterBlock extends HorizontalFacingBlock {

    private static final int BLOCK_COUNT = 12;
    private static final int HEIGHT = 2;
    private static final int WIDTH = 3;
    private static final int DEPTH = 2;
    public static final EnumProperty<Layer> LAYER = EnumProperty.of("layer", Layer.class);
    public static final EnumProperty<WidthRow> WIDTH_ROW = EnumProperty.of("width_row", WidthRow.class);
    public static final EnumProperty<DepthRow> DEPTH_ROW = EnumProperty.of("depth_row", DepthRow.class);

    public DumpsterBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH).with(LAYER, Layer.LOWER).with(WIDTH_ROW, WidthRow.MIDDLE).with(DEPTH_ROW, DepthRow.FRONT));
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!world.isClient && player.isCreative()) {
            //            TallPlantBlock.onBreakInCreative(world, pos, state, player);    // TODO rewrite for boolean property
        }
        super.onBreak(world, pos, state, player);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockPos pos = ctx.getBlockPos();
        World world = ctx.getWorld();
        BlockState state = this.getDefaultState().with(FACING, ctx.getPlayerFacing().getOpposite()).with(LAYER, Layer.LOWER).with(WIDTH_ROW, WidthRow.MIDDLE).with(DEPTH_ROW, DepthRow.FRONT);
        if (pos.getY() < world.getTopY() - 1 && this.preformOnAll(state, pos, (worldPos, relativePos) -> world.getBlockState(worldPos).canReplace(ctx))) {
            return state;
        }
        return null;
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        this.preformOnAll(state, pos, (worldPos, relativePos) -> {
            if (!worldPos.equals(pos)) {
                world.setBlockState(worldPos, state.with(LAYER, Layer.values()[relativePos.getY()]).with(WIDTH_ROW, WidthRow.values()[relativePos.getX()]).with(DEPTH_ROW, DepthRow.values()[relativePos.getZ()]), 3);
            }
            return true;
        });
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        Direction facing = state.get(FACING);
        Direction.Axis axis = direction.getAxis();
        //        EnumProperty<? extends DirectionCheckable> property = switch (axis) {
        //            case X -> WIDTH_ROW;
        //            case Y -> LAYER;
        //            case Z -> DEPTH_ROW;
        //        };

        //        List<Function<Direction, Direction>> list = new ArrayList<>();
        //        list.addAll(List.of(state.get(WIDTH_ROW).getDirectionsChecks()));
        //        list.addAll(List.of(state.get(LAYER).getDirectionsChecks()));
        //        list.addAll(List.of(state.get(DEPTH_ROW).getDirectionsChecks()));

        //        if (neighborState.contains(property) && neighborState.get(property) != state.get(property)) {
        //            return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
        //        }
        //        Direction direction1 = null;

        //        if (axis.equals(Direction.Axis.X)) {
        //            var a = state.get(WIDTH_ROW);
        //            if (a.equals(WidthRow.LEFT)) {
        //                direction1 = state.get(FACING).rotateYClockwise();
        //            }
        //            else if (a.equals(WidthRow.MIDDLE)) {
        //                direction1 = state.get(FACING);
        //            }
        //            else if (a.equals(WidthRow.RIGHT)) {
        //                direction1 = state.get(FACING).rotateYClockwise().getOpposite();
        //            }
        //        }
        //        else if (axis.equals(Direction.Axis.Y)) {
        //            var a = state.get(LAYER);
        //            if (a.equals(Layer.UPPER)) {
        //                direction1 = Direction.UP;
        //            }
        //            else if (a.equals(Layer.LOWER)) {
        //                direction1 = Direction.DOWN;
        //            }
        //        }
        //        else if (axis.equals(Direction.Axis.Z)) {
        //            var a = state.get(DEPTH_ROW);
        //            if (a.equals(DepthRow.BACK)) {
        //                direction1 = state.get(FACING).getOpposite();
        //            }
        //            else if (a.equals(DepthRow.FRONT)) {
        //                direction1 = state.get(FACING);
        //            }
        //        }
        //
        //        if (direction.equals(direction)) {
        //        }

        //        for (Function<Direction, Direction> function : list) {
        //            if (function.apply(facing).equals(direction)) {
        //                if (neighborState.isOf(this)) {
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
        //                }
        //                else {
        //                    return Blocks.AIR.getDefaultState();
        //                }
        //            }
        //        }
        //        return Blocks.AIR.getDefaultState();//super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    public PistonBehavior getPistonBehavior(BlockState state) {
        return PistonBehavior.BLOCK;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, LAYER, WIDTH_ROW, DEPTH_ROW);
    }

    @Override
    public boolean isTranslucent(BlockState state, BlockView world, BlockPos pos) {
        return true;
    }
    public VoxelShape getCullingShape(BlockState state, BlockView world, BlockPos pos) {
        return VoxelShapes.empty();
    }

    @Override
    public float getAmbientOcclusionLightLevel(BlockState state, BlockView world, BlockPos pos) {
        return 1.0F;
    }




    @Override
    public boolean isSideInvisible(BlockState state, BlockState stateFrom, Direction direction) {
        return stateFrom.isOf(this) || super.isSideInvisible(state, stateFrom, direction);
    }

    private boolean preformOnAll(BlockState state, BlockPos pos, BiFunction<BlockPos, Vec3i, Boolean> function) {
        Direction facing = state.get(FACING);
        Direction clockwise = facing.rotateYClockwise();
        BlockPos startPos = pos.offset(facing.getOpposite(), 2).offset(clockwise);
        int successful = 0;
        for (int height = 0; height < HEIGHT; height++) {
            BlockPos offsetPos = startPos.up(height);
            for (int width = 0; width < WIDTH; width++) {
                BlockPos rowPos = offsetPos;
                offsetPos = offsetPos.offset(clockwise.getOpposite());
                for (int depth = 0; depth < DEPTH; depth++) {
                    rowPos = rowPos.offset(facing);
                    if (function.apply(rowPos, new Vec3i(width, height, depth))) {
                        successful++;
                    }
                }
            }
        }
        return successful == BLOCK_COUNT;
    }

    private enum WidthRow implements DirectionCheckable {
        LEFT("left", Direction::rotateYCounterclockwise), MIDDLE("middle", Direction::rotateYClockwise, Direction::rotateYCounterclockwise), RIGHT("right", Direction::rotateYClockwise);

        private final String name;
        private final Function<Direction, Direction>[] directionChecks;

        @SafeVarargs
        WidthRow(String name, Function<Direction, Direction>... directionChecks) {
            this.name = name;
            this.directionChecks = directionChecks;
        }

        @Override
        public Function<Direction, Direction>[] getDirectionsChecks() {
            return this.directionChecks;
        }

        @Override
        public String asString() {
            return this.name;
        }

        @Override
        public String toString() {
            return this.asString();
        }
    }

    private enum DepthRow implements DirectionCheckable {
        BACK("back", direction -> direction), FRONT("front", Direction::getOpposite);

        private final String name;
        private final Function<Direction, Direction>[] directionChecks;

        @SafeVarargs
        DepthRow(String name, Function<Direction, Direction>... directionChecks) {
            this.name = name;
            this.directionChecks = directionChecks;
        }

        @Override
        public Function<Direction, Direction>[] getDirectionsChecks() {
            return this.directionChecks;
        }

        @Override
        public String asString() {
            return this.name;
        }

        @Override
        public String toString() {
            return this.asString();
        }
    }

    private enum Layer implements DirectionCheckable {
        LOWER("lower", direction -> Direction.UP), UPPER("upper", direction -> Direction.DOWN);

        private final String name;
        private final Function<Direction, Direction>[] directionChecks;

        @SafeVarargs
        Layer(String name, Function<Direction, Direction>... directionChecks) {
            this.name = name;
            this.directionChecks = directionChecks;
        }

        @Override
        public Function<Direction, Direction>[] getDirectionsChecks() {
            return this.directionChecks;
        }

        @Override
        public String asString() {
            return this.name;
        }

        @Override
        public String toString() {
            return this.asString();
        }
    }

    private interface DirectionCheckable extends StringIdentifiable {

        Function<Direction, Direction>[] getDirectionsChecks();
    }
}
