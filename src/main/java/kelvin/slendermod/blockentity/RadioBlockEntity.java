package kelvin.slendermod.blockentity;

import kelvin.slendermod.block.RadioBlock;
import kelvin.slendermod.registry.BlockEntityRegistry;
import kelvin.slendermod.registry.SoundRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RadioBlockEntity extends BlockEntity {

    private static final int SOUND_LENGTH = 140; // Length of sound clip in ticks
    private int tick;
    private PositionedSoundInstance soundInstance;
    private final MinecraftClient client;

    public RadioBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.RADIO_BLOCK_ENTITY, pos, state);
        this.client = MinecraftClient.getInstance();
    }

    public static void serverTick(World world, BlockPos pos, BlockState state, RadioBlockEntity blockEntity) {
        if (state.get(RadioBlock.POWERED)) {
            if (blockEntity.tick == 0 || blockEntity.tick % SOUND_LENGTH == 0) {
                blockEntity.soundInstance = new PositionedSoundInstance(SoundRegistry.RADIO_STATIC, SoundCategory.BLOCKS, 1, 1, world.getRandom(), pos.getX(), pos.getY(), pos.getZ());
                blockEntity.client.getSoundManager().play(blockEntity.soundInstance);
            }
            blockEntity.tick++;
        }
    }

    public void resetTicks() {
        this.tick = 0;
    }

    public void stopSound() {
        SoundManager manager = this.client.getSoundManager();
        if (manager.isPlaying(this.soundInstance)) {
            manager.stop(this.soundInstance);
        }
    }
}
