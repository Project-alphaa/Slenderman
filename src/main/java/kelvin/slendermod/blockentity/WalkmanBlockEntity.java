package kelvin.slendermod.blockentity;

import kelvin.slendermod.block.WalkmanBlock;
import kelvin.slendermod.item.CassetteTapeItem;
import kelvin.slendermod.registry.BlockEntityRegistry;
import kelvin.slendermod.registry.GameEventRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Clearable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

public class WalkmanBlockEntity extends BlockEntity implements Clearable {
    private ItemStack cassette;
    private int ticksThisSecond;
    private long tickCount;
    private long cassetteStartTick;
    private boolean isPlaying;

    public WalkmanBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.WALKMAN_BLOCK_ENTITY, pos, state);

        this.cassette = ItemStack.EMPTY;
    }

    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        if (nbt.contains("CassetteItem", 10)) {
            this.setCassette(ItemStack.fromNbt(nbt.getCompound("CassetteItem")));
        }

        this.isPlaying = nbt.getBoolean("IsPlaying");
        this.cassetteStartTick = nbt.getLong("CassetteStartTick");
        this.tickCount = nbt.getLong("TickCount");
    }

    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        if (!this.getCassette().isEmpty()) {
            nbt.put("CassetteItem", this.getCassette().writeNbt(new NbtCompound()));
        }

        nbt.putBoolean("IsPlaying", this.isPlaying);
        nbt.putLong("CassetteStartTick", this.cassetteStartTick);
        nbt.putLong("TickCount", this.tickCount);
    }

    public ItemStack getCassette() {
        return this.cassette;
    }

    public void setCassette(ItemStack stack) {
        this.cassette = stack;
        this.markDirty();
    }

    public void startPlaying() {
        this.cassetteStartTick = this.tickCount;
        this.isPlaying = true;
    }

    public void clear() {
        this.setCassette(ItemStack.EMPTY);
        this.isPlaying = false;
    }


    public static void tick(World world, BlockPos pos, BlockState state, kelvin.slendermod.blockentity.WalkmanBlockEntity blockEntity) {
        ++blockEntity.ticksThisSecond;
        if (isPlayingRecord(state, blockEntity)) {
            Item var5 = blockEntity.getCassette().getItem();
            if (var5 instanceof CassetteTapeItem) {
                CassetteTapeItem musicDiscItem = (CassetteTapeItem)var5;
                if (isSongFinished(blockEntity, musicDiscItem)) {
                    world.emitGameEvent(GameEventRegistry.WALKMAN_STOP_PLAY, pos, GameEvent.Emitter.of(state));
                    world.setBlockState(pos, state.with(WalkmanBlock.PLAYING_STATE, 2));
                    blockEntity.isPlaying = false;
                } else if (hasSecondPassed(blockEntity)) {
                    blockEntity.ticksThisSecond = 0;
                    world.emitGameEvent(GameEventRegistry.WALKMAN_PLAY, pos, GameEvent.Emitter.of(state));
                }
            }
        }

        ++blockEntity.tickCount;
    }

    private static boolean isPlayingRecord(BlockState state, kelvin.slendermod.blockentity.WalkmanBlockEntity blockEntity) {
        return (Boolean)state.get(WalkmanBlock.HAS_CASSETTE) && blockEntity.isPlaying;
    }

    private static boolean isSongFinished(kelvin.slendermod.blockentity.WalkmanBlockEntity blockEntity, CassetteTapeItem musicDisc) {
        return blockEntity.tickCount >= blockEntity.cassetteStartTick + (long)musicDisc.getSongLengthInTicks();
    }

    private static boolean hasSecondPassed(kelvin.slendermod.blockentity.WalkmanBlockEntity blockEntity) {
        return blockEntity.ticksThisSecond >= 20;
    }
}

