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
        this.setCard(ItemStack.EMPTY);

        if (nbt.contains("Card", 10)) {
            this.setCard(ItemStack.fromNbt(nbt.getCompound("Card")));
            this.markDirty();
        }
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        this.saveCard(nbt);
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        NbtCompound nbt = new NbtCompound();
        this.saveCard(nbt);
        return nbt;
    }

    private void saveCard(NbtCompound nbt) {
        if (!this.getCard().isEmpty()) {
            nbt.put("Card", this.getCard().writeNbt(new NbtCompound()));
        }
    }

    public boolean setCard(PlayerEntity player, ItemStack stack) {
        if (this.getCard().isEmpty() && !stack.isEmpty()) {
            this.setCard(stack.split(1));
            this.world.emitGameEvent(GameEvent.BLOCK_DEACTIVATE, this.getPos(), GameEvent.Emitter.of(player, this.getCachedState()));
            this.setUpdated();
            return true;
        }
        return false;
    }

    public boolean removeCard(PlayerEntity player) {
        if (!this.getCard().isEmpty()) {
            if (!player.isCreative()) {
                Block.dropStack(this.world, this.getPos(), this.getCard());
            }

            this.world.emitGameEvent(GameEvent.BLOCK_ACTIVATE, this.getPos(), GameEvent.Emitter.of(player, this.getCachedState()));
            this.clear();
            return true;
        }
        return false;
    }

    @Override
    public void clear() {
        this.setCard(ItemStack.EMPTY);
        this.setUpdated();
    }

    public void setUpdated() {
        this.markDirty();
        this.world.updateListeners(this.getPos(), this.getCachedState(), this.getCachedState(), 3);
    }

    public ItemStack getCard() {
        return this.card;
    }

    private void setCard(ItemStack stack) {
        this.card = stack;
    }
}
