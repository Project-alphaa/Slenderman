package kelvin.slendermod.mixin;

import kelvin.slendermod.registry.SoundRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.PillarBlock;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(PillarBlock.class)
public class PillarBlockMixin extends Block {

    public PillarBlockMixin(Settings settings) {
        super(settings.ticksRandomly());
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (state.getBlock() == Blocks.OCHRE_FROGLIGHT || state.getBlock() == Blocks.VERDANT_FROGLIGHT || state.getBlock() == Blocks.PEARLESCENT_FROGLIGHT) {
            world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundRegistry.BUZZING, SoundCategory.BLOCKS, 1, 1, false);
        }
    }
}
