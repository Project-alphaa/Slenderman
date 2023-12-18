package kelvin.slendermod.mixin;

import kelvin.slendermod.entity.SlendermanEntity;
import kelvin.slendermod.registry.ParticleRegistry;
import kelvin.slendermod.registry.SoundRegistry;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EndermanEntity.class)
public class EndermanEntityMixin {

    @Unique
    private final boolean slendermod$isSlenderman = ((Object) this) instanceof SlendermanEntity;

    @Redirect(method = "playAngrySound", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;playSound(DDDLnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;FFZ)V"))
    private void playAngrySound(World world, double x, double y, double z, SoundEvent sound, SoundCategory category, float volume, float pitch, boolean useDistance) {
        if (slendermod$isSlenderman) {
            world.playSound(x, y, z, SoundRegistry.SLENDERMAN_SCREAM, category, volume, pitch, useDistance);
        }
        else {
            world.playSound(x, y, z, sound, category, volume, pitch, useDistance);
        }
    }

    @Redirect(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;addParticle(Lnet/minecraft/particle/ParticleEffect;DDDDDD)V"))
    private void addParticle(World world, ParticleEffect particle, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
        if (slendermod$isSlenderman) {
            world.addParticle(ParticleRegistry.SLENDERMAN_MAGIC, x, y, z, velocityX, velocityY, velocityZ);
        }
        else {
            world.addParticle(ParticleTypes.PORTAL, x, y, z, velocityX, velocityY, velocityZ);
        }
    }
}
