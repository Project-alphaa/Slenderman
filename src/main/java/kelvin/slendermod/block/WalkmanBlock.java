package kelvin.slendermod.block;

import kelvin.slendermod.blockentity.WalkmanBlockEntity;
import kelvin.slendermod.item.CassetteTapeItem;
import kelvin.slendermod.registry.BlockEntityRegistry;
import kelvin.slendermod.registry.GameEventRegistry;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Property;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

public class WalkmanBlock extends BlockWithEntity {

    public static final VoxelShape NORTH_SHAPE = Block.createCuboidShape(0.0, 0.0, 11.0, 16.0, 14.0, 16.0);
    public static final VoxelShape SOUTH_SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 14.0, 5.0);
    public static final VoxelShape EAST_SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 5.0, 14.0, 16.0);
    public static final VoxelShape WEST_SHAPE = Block.createCuboidShape(11.0, 0.0, 0.0, 16.0, 14.0, 16.0);



    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;
    public static final BooleanProperty HAS_CASSETTE;
    public static final IntProperty PLAYING_STATE;

    public WalkmanBlock(AbstractBlock.Settings settings) {
        super(settings);
        //this.setDefaultState((BlockState)((BlockState)this.stateManager.getDefaultState()).with(PLAYING_STATE, 0));
        this.setDefaultState(this.stateManager.getDefaultState()
                .with(HAS_CASSETTE, false)
                .with(FACING, Direction.NORTH)
                .with(PLAYING_STATE, 0)
        );
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        Direction direction = state.get(FACING);
        return switch (direction) {
            case NORTH -> NORTH_SHAPE;
            case SOUTH -> SOUTH_SHAPE;
            case EAST -> EAST_SHAPE;
            case WEST -> WEST_SHAPE;
            default -> throw new IllegalStateException();
        };
    }

    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getPlayerFacing().getOpposite());
    }

    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);
        NbtCompound nbtCompound = BlockItem.getBlockEntityNbt(itemStack);
        if (nbtCompound != null && nbtCompound.contains("CassetteItem")) {
            world.setBlockState(pos, (BlockState)state.with(HAS_CASSETTE, false), 2);
        }
    }

    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (state.get(HAS_CASSETTE)) {
            if (state.get(PLAYING_STATE) == 0) {        // Occupied
                BlockEntity blockEntity = world.getBlockEntity(pos);
                Item item = null;
                if (blockEntity instanceof WalkmanBlockEntity) {
                    item = ((WalkmanBlockEntity) blockEntity).getCassette().getItem();
                    playCassette(world, (WalkmanBlockEntity)blockEntity, pos, item);
                    world.setBlockState(pos, state.with(PLAYING_STATE, 1));
                }
                return ActionResult.success(item != null);
            } else if (state.get(PLAYING_STATE) == 1 || state.get(PLAYING_STATE) == 2) { // Playing or Ended
                this.removeCassette(world, pos);
                state = (BlockState)state.with(HAS_CASSETTE, false);
                world.emitGameEvent(GameEventRegistry.WALKMAN_STOP_PLAY, pos, GameEvent.Emitter.of(state));
                world.setBlockState(pos, state, 2);
                world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Emitter.of(player, state));

                world.setBlockState(pos, state.with(PLAYING_STATE, 0));
                return ActionResult.success(world.isClient);
            }
            return ActionResult.success(world.isClient);
        } else {
            return ActionResult.PASS;
        }
    }

    public void playCassette(World world, WalkmanBlockEntity blockEntity, BlockPos pos, Item item) {
        blockEntity.startPlaying();
        world.syncWorldEvent((PlayerEntity) null, 1010, pos, Item.getRawId(item));
    }

    public void setCassette(@Nullable Entity user, WorldAccess world, BlockPos pos, BlockState state, ItemStack stack) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof WalkmanBlockEntity walkmanBlockEntity) {
            walkmanBlockEntity.setCassette(stack.copy());
            world.setBlockState(pos, (BlockState)state.with(HAS_CASSETTE, true), 2);
            world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Emitter.of(user, state));
        }
    }

    private void removeCassette(World world, BlockPos pos) {
        if (!world.isClient) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof WalkmanBlockEntity walkmanBlockEntity) {
                ItemStack itemStack = walkmanBlockEntity.getCassette();
                if (!itemStack.isEmpty()) {
                    world.syncWorldEvent(1010, pos, 0);
                    walkmanBlockEntity.clear();
                    float f = 0.7F;
                    double d = (double)(world.random.nextFloat() * 0.7F) + 0.15000000596046448;
                    double e = (double)(world.random.nextFloat() * 0.7F) + 0.06000000238418579 + 0.6;
                    double g = (double)(world.random.nextFloat() * 0.7F) + 0.15000000596046448;
                    ItemStack itemStack2 = itemStack.copy();
                    ItemEntity itemEntity = new ItemEntity(world, (double)pos.getX() + d, (double)pos.getY() + e, (double)pos.getZ() + g, itemStack2);
                    itemEntity.setToDefaultPickupDelay();
                    world.spawnEntity(itemEntity);
                }
            }
        }
    }

    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.isOf(newState.getBlock())) {
            this.removeCassette(world, pos);
            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }

    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new WalkmanBlockEntity(pos, state.with(HAS_CASSETTE, false));
    }

    public boolean hasComparatorOutput(BlockState state) {
        return true;
    }

    public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof WalkmanBlockEntity) {
            Item item = ((WalkmanBlockEntity)blockEntity).getCassette().getItem();
            if (item instanceof CassetteTapeItem) {
                return ((CassetteTapeItem)item).getComparatorOutput();
            }
        }

        return 0;
    }

    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{HAS_CASSETTE, PLAYING_STATE, FACING});
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return (Boolean)state.get(HAS_CASSETTE) ? checkType(type, BlockEntityRegistry.WALKMAN_BLOCK_ENTITY, WalkmanBlockEntity::tick) : null;
    }

    static {
        HAS_CASSETTE = BooleanProperty.of("has_cassette");
        PLAYING_STATE = IntProperty.of("playing_state", 0, 2);
    }
}
