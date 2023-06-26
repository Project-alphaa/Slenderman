package kelvin.slendermod.mixin;

import dev.lambdaurora.lambdynlights.LambDynLights;
import kelvin.slendermod.item.ItemFlashlight;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LambDynLights.class)
public class LambDynLightsMixin {

    @Inject(method = "getLuminanceFromItemStack", at = @At("RETURN"), cancellable = true)
    private static void getLuminanceFromItemStack(ItemStack stack, boolean submergedInWater, CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(ItemFlashlight.isFlashlightPowered(stack) ? 15 : 0);
    }
}
