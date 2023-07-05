package kelvin.slendermod.blockentity;

import kelvin.slendermod.block.SafeBlock;
import kelvin.slendermod.client.screen.handler.SafeScreenHandler;
import kelvin.slendermod.registry.BlockEntityRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;

public class SafeBlockEntity extends BlockEntity implements GeoBlockEntity, NamedScreenHandlerFactory, Inventory {

    private static final RawAnimation OPEN_ANIM = RawAnimation.begin().then("open", Animation.LoopType.PLAY_ONCE);
    private static final RawAnimation OPEN_IDLE_ANIM = RawAnimation.begin().then("open_idle", Animation.LoopType.LOOP);
    private static final RawAnimation CLOSE_ANIM = RawAnimation.begin().then("close", Animation.LoopType.PLAY_ONCE);
    private static final RawAnimation IDLE_ANIM = RawAnimation.begin().then("idle", Animation.LoopType.LOOP);

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private boolean isOpen = false;
    private ItemStack item = ItemStack.EMPTY;

    public SafeBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.SAFE_BLOCK_ENTITY, pos, state);
    }

    @Override
    public Text getDisplayName() {
        return SafeBlock.TITLE;
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new SafeScreenHandler(syncId, inv, this, ScreenHandlerContext.create(this.world, this.pos));
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        this.isOpen = nbt.getBoolean("IsOpen");
        if (nbt.contains("Item", NbtElement.COMPOUND_TYPE)) {
            this.setItem(ItemStack.fromNbt(nbt.getCompound("Item")));
            this.markDirty();
        }
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putBoolean("IsOpen", this.isOpen);
        this.saveItem(nbt);
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        NbtCompound nbt = new NbtCompound();
        this.saveItem(nbt);
        return nbt;
    }

    private void saveItem(NbtCompound nbt) {
        if (!this.getItem().isEmpty()) {
            nbt.put("Item", this.getItem().writeNbt(new NbtCompound()));
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller", 5, this::predicate));
        controllerRegistrar.add(new AnimationController<>(this, "idle_controller", 0, state -> {
            state.getController().setAnimation(IDLE_ANIM);
            return PlayState.CONTINUE;
        }));
    }

    private PlayState predicate(AnimationState<GeoAnimatable> state) {
        AnimationController<GeoAnimatable> controller = state.getController();
        controller.triggerableAnim("open", OPEN_ANIM);
        controller.triggerableAnim("close", CLOSE_ANIM);

        if (controller.hasAnimationFinished()) {
            if (controller.getCurrentRawAnimation().equals(OPEN_ANIM)) {
                controller.setAnimation(OPEN_IDLE_ANIM);
            } else if (controller.getCurrentRawAnimation().equals(CLOSE_ANIM)) {
                controller.setAnimation(IDLE_ANIM);
            }
        }
        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public void clear() {
        this.setItem(ItemStack.EMPTY);
        this.setUpdated();
    }

    public void setOpen(boolean open) {
        this.isOpen = open;
        this.triggerAnim("controller", this.isOpen ? "open" : "close");
    }

    public boolean isOpen() {
        return this.isOpen;
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }

    public ItemStack getItem() {
        return this.item;
    }

    public void setUpdated() {
        this.markDirty();
        this.world.updateListeners(this.getPos(), this.getCachedState(), this.getCachedState(), Block.NOTIFY_ALL);
    }

    @Override
    public int size() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return this.getItem().isEmpty();
    }

    @Override
    public ItemStack getStack(int slot) {
        return this.item;
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        return Inventories.splitStack(List.of(this.item), slot, amount);
    }

    @Override
    public ItemStack removeStack(int slot) {
        return Inventories.removeStack(List.of(this.item), slot);
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        boolean flag = stack.isEmpty() && ItemStack.canCombine(stack, this.item);
        this.item = stack;

        if (stack.getCount() > this.getMaxCountPerStack()) {
            stack.setCount(this.getMaxCountPerStack());
        }

        if (!flag) {
            this.markDirty();
        }
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        if (this.world.getBlockEntity(this.pos) != this) {
            return false;
        } else {
            return player.squaredDistanceTo(this.pos.getX() + 0.5D, this.pos.getY() + 0.5D, this.pos.getZ() + 0.5D) <= 64;
        }
    }
}
