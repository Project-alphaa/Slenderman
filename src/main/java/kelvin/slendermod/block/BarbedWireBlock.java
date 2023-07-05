package kelvin.slendermod.block;

import net.minecraft.block.*;
import net.minecraft.block.enums.DoorHinge;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

@SuppressWarnings("deprecation")
public class BarbedWireBlock extends HorizontalFacingBlock {

    private static final VoxelShape LEFT_SHAPE = VoxelShapes.union(Block.createCuboidShape(15, 0, 0, 17, 16, 16), Block.createCuboidShape(0, 6.25, 7, 15, 8.25, 9));
    private static final VoxelShape RIGHT_SHAPE = VoxelShapes.union(Block.createCuboidShape(1, 0, 0, 3, 16, 16), Block.createCuboidShape(3, 6.25, 7, 16, 8.25, 9));

    public static final EnumProperty<DoorHinge> HALF = EnumProperty.of("half", DoorHinge.class);

    public BarbedWireBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH).with(HALF, DoorHinge.LEFT));
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        DoorHinge half = state.get(HALF);
        Direction facing = state.get(FACING);
        VoxelShape shape = switch (half) {
            case RIGHT -> RIGHT_SHAPE;
            case LEFT -> LEFT_SHAPE;
        };

        return switch (facing) {
            case EAST -> rotateShape(1, shape);
            case SOUTH -> rotateShape(2, shape);
            case WEST -> rotateShape(3, shape);
            default -> rotateShape(0, shape);
        };
    }

    public static VoxelShape rotateShape(int times, VoxelShape shape) {
        VoxelShape[] shapes = new VoxelShape[] {shape, VoxelShapes.empty()};

        for (int i = 0; i < times; i++) {
            shapes[0].forEachBox((minX, minY, minZ, maxX, maxY, maxZ) -> shapes[1] = VoxelShapes.union(shapes[1], VoxelShapes.cuboid(1 - maxZ, minY, minX, 1 - minZ, maxY, maxX)));
            shapes[0] = shapes[1];
            shapes[1] = VoxelShapes.empty();
        }

        return shapes[0];
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {

        if (!direction.equals(getDirectionToOpposite(state)) || !(state.isOf(this)))
            return state;

        BlockState otherState = world.getBlockState(getOppositePos(state, pos));

        if (otherState.isOf(this) && getDirectionToOpposite(otherState).rotateYClockwise().rotateYClockwise().equals(getDirectionToOpposite(state))) {
            return state;
        }

        return Blocks.AIR.getDefaultState();


    }


    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!world.isClient && player.isCreative()) {
            DoorHinge half = state.get(HALF);
            if (half == DoorHinge.RIGHT) {
                BlockPos leftPos = pos.offset(state.get(FACING).rotateYClockwise());
                BlockState leftState = world.getBlockState(leftPos);
                if (leftState.isOf(state.getBlock()) && leftState.get(HALF) == DoorHinge.LEFT) {
                    BlockState replacedWithState = leftState.getFluidState().isOf(Fluids.WATER) ? Blocks.WATER.getDefaultState() : Blocks.AIR.getDefaultState();
                    world.setBlockState(leftPos, replacedWithState, 35);
                    world.syncWorldEvent(player, 2001, leftPos, Block.getRawIdFromState(leftState));
                }
            }
        }

        super.onBreak(world, pos, state, player);
    }

    @Override
    public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
        return switch (type) {
            case LAND, AIR -> true;
            default -> false;
        };
    }


    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);
        if (!world.isClient) {
            BlockPos blockPos = pos.offset(state.get(FACING).rotateYCounterclockwise());
            world.setBlockState(blockPos, state.with(HALF, DoorHinge.RIGHT), 3);


            world.updateNeighbors(pos, Blocks.AIR);
            state.updateNeighbors(world, pos, 3);
        }
    }


    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockPos pos = ctx.getBlockPos();
        World world = ctx.getWorld();
        Direction facing = ctx.getPlayerFacing().getOpposite();
        BlockPos pos2 = pos.offset(facing.rotateYCounterclockwise());
        if (pos.getY() < world.getTopY() && world.getBlockState(pos2).canReplace(ctx) && world.getWorldBorder().contains(pos2)) {
            return this.getDefaultState().with(FACING, facing).with(HALF, DoorHinge.LEFT);
        } else {
            return null;
        }
    }


    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {


        BlockPos downPos = pos.down();
        BlockState downState = world.getBlockState(downPos);
        BlockState oppositeState = world.getBlockState(getOppositePos(state, pos));
        return downState.isSideSolidFullSquare(world, downPos, Direction.UP) && (state.get(HALF) == DoorHinge.LEFT || oppositeState.isOf(this));


    }


    @Override
    public PistonBehavior getPistonBehavior(BlockState state) {
        return PistonBehavior.DESTROY;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, HALF);
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        entity.damage(DamageSource.CACTUS, 1);
    }

    private static BlockPos getOppositePos(BlockState state, BlockPos pos) {
        DoorHinge half = state.get(HALF);
        Direction facing = state.get(FACING);
        return pos.offset(half == DoorHinge.LEFT ? facing.rotateYCounterclockwise() : facing.rotateYClockwise());
    }

    private static Direction getDirectionToOpposite(BlockState state) {
        DoorHinge half = state.get(HALF);
        Direction facing = state.get(FACING);
        return half == DoorHinge.LEFT ? facing.rotateYCounterclockwise() : facing.rotateYClockwise();
    }


}
