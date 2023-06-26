package kelvin.slendermod.mixin;

import kelvin.slendermod.entity.EntitySlenderman;
import kelvin.slendermod.registry.ParticleRegistry;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EndermanEntity.class)
public class EndermanEntityMixin {

    @Redirect(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;addParticle(Lnet/minecraft/particle/ParticleEffect;DDDDDD)V"))
    private void addParticle(World world, ParticleEffect particle, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
        if (((Object) this) instanceof EntitySlenderman) {
            world.addParticle(ParticleRegistry.SLENDERMAN_MAGIC, x, y, z, velocityX, velocityY, velocityZ);
        }
        else {
            world.addParticle(ParticleTypes.PORTAL, x, y, z, velocityX, velocityY, velocityZ);
        }
    }
}
