package kelvin.slendermod.mixin;

import kelvin.slendermod.block.SCPSlendermanHeadBlock;
import kelvin.slendermod.entity.EntitySlenderman;
import kelvin.slendermod.registry.ParticleRegistry;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Inject(at = @At("HEAD"), method = "getPreferredEquipmentSlot", cancellable = true)
    private static void getPreferredEquipmentSlot(ItemStack stack, CallbackInfoReturnable<EquipmentSlot> info) {
        if (stack.getItem() != null && (stack.getItem().asItem() instanceof BlockItem && ((BlockItem) stack.getItem().asItem()).getBlock() instanceof SCPSlendermanHeadBlock)) {
            info.setReturnValue(EquipmentSlot.HEAD);
        }
    }

    @Redirect(method = "handleStatus", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;addParticle(Lnet/minecraft/particle/ParticleEffect;DDDDDD)V"))
    private void addParticles(World world, ParticleEffect particleEffect, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
        if (((Object) this) instanceof EntitySlenderman) {
            world.addParticle(ParticleRegistry.SLENDERMAN_MAGIC, x, y, z, velocityX, velocityY, velocityZ);
        }
        else {
            world.addParticle(ParticleTypes.PORTAL, x, y, z, velocityX, velocityY, velocityZ);
        }
    }
}
