package kelvin.slendermod.blockentity;

import kelvin.slendermod.registry.BlockEntityRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.Clearable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

public class AccessReaderBlockEntity extends BlockEntity implements Clearable {

    private ItemStack card = ItemStack.EMPTY;

    public AccessReaderBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.ACCESS_READER_ENTITY, pos, state);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        setCard(ItemStack.EMPTY);

        if (nbt.contains("Card", 10)) {
            setCard(ItemStack.fromNbt(nbt.getCompound("Card")));
            markDirty();
        }
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        saveCard(nbt);
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        NbtCompound nbt = new NbtCompound();
        saveCard(nbt);
        return nbt;
    }

    private void saveCard(NbtCompound nbt) {
        if (!getCard().isEmpty()) {
            nbt.put("Card", getCard().writeNbt(new NbtCompound()));
        }
    }

    public boolean setCard(PlayerEntity player, ItemStack stack) {
        if (getCard().isEmpty() && !stack.isEmpty()) {
            setCard(stack.split(1));
            world.emitGameEvent(GameEvent.BLOCK_DEACTIVATE, getPos(), GameEvent.Emitter.of(player, getCachedState()));
            setUpdated();
            return true;
        }
        return false;
    }

    public boolean removeCard(PlayerEntity player) {
        if (!getCard().isEmpty()) {
            if (!player.isCreative()) {
                Block.dropStack(world, getPos(), getCard());
            }

            world.emitGameEvent(GameEvent.BLOCK_ACTIVATE, getPos(), GameEvent.Emitter.of(player, getCachedState()));
            clear();
            return true;
        }
        return false;
    }

    @Override
    public void clear() {
        setCard(ItemStack.EMPTY);
        setUpdated();
    }

    public void setUpdated() {
        markDirty();
        world.updateListeners(getPos(), getCachedState(), getCachedState(), 3);
    }

    public ItemStack getCard() {
        return card;
    }

    private void setCard(ItemStack stack) {
        card = stack;
    }
}
