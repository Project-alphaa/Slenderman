package kelvin.slendermod.block;

import kelvin.slendermod.blockentity.AccessReaderBlockEntity;
import kelvin.slendermod.registry.ItemRegistry;
import kelvin.slendermod.registry.SoundRegistry;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class AccessReaderBlock extends BlockWithEntity {

    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;

    public AccessReaderBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH));
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof AccessReaderBlockEntity accessReaderBlockEntity) {
            ItemStack heldItem = player.getStackInHand(hand);
            ItemStack card = accessReaderBlockEntity.getCard();

            if (card.isEmpty()) {
                if (heldItem.isOf(ItemRegistry.ACCESS_CARD)) {
                    if (world.isClient()) {
                        world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundRegistry.ACCESS_GRANTED, SoundCategory.BLOCKS, 1, 1, false);
                        return ActionResult.CONSUME;
                    }

                    if (accessReaderBlockEntity.setCard(player, player.isCreative() ? heldItem.copy() : heldItem)) {
                        this.updateNeighbors(state, world, pos);
                        return ActionResult.SUCCESS;
                    }
                }
            } else {
                if (world.isClient()) {
                    return ActionResult.CONSUME;
                }

                if (accessReaderBlockEntity.removeCard(player)) {
                    this.updateNeighbors(state, world, pos);
                    return ActionResult.SUCCESS;
                }
            }
            return ActionResult.CONSUME;
        }
        return ActionResult.PASS;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getPlayerFacing().getOpposite());
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

    @Override
    public boolean emitsRedstonePower(BlockState state) {
        return true;
    }

    @Override
    public int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof AccessReaderBlockEntity accessReaderBlockEntity) {
            if (!accessReaderBlockEntity.getCard().isEmpty()) {
                return 15;
            }
        }
        return 0;
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.isOf(newState.getBlock())) {
            this.dropCard(world, pos);
            this.updateNeighbors(state, world, pos);
            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }

    private void dropCard(World world, BlockPos pos) {
        if (!world.isClient()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof AccessReaderBlockEntity accessReaderBlockEntity) {
                ItemStack card = accessReaderBlockEntity.getCard();
                if (!card.isEmpty()) {
                    accessReaderBlockEntity.clear();
                    float f = 0.7F;
                    double d0 = (double) (world.random.nextFloat() * f) + (double) 0.15F;
                    double d1 = (double) (world.random.nextFloat() * f) + (double) 0.060000002F + 0.6D;
                    double d2 = (double) (world.random.nextFloat() * f) + (double) 0.15F;
                    ItemEntity itemEntity = new ItemEntity(world, pos.getX() + d0, pos.getY() + d1, pos.getZ() + d2, card);
                    itemEntity.setToDefaultPickupDelay();
                    world.spawnEntity(itemEntity);
                }
            }
        }
    }

    private void updateNeighbors(BlockState state, World world, BlockPos pos) {
        world.updateNeighborsAlways(pos, this);
        for (Direction direction : Direction.values()) {
            world.updateNeighborsAlways(pos.offset(direction), this);
        }
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new AccessReaderBlockEntity(pos, state);
    }
}
