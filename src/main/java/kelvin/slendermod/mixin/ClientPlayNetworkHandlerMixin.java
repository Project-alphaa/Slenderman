package kelvin.slendermod.mixin;

import kelvin.slendermod.registry.ItemRegistry;
import kelvin.slendermod.util.CustomBookScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.BookScreen;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.OpenWrittenBookS2CPacket;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {

    @Final
    @Shadow
    private MinecraftClient client;

    @Inject(at = @At("RETURN"), method = "onOpenWrittenBook")
    public void onOpenWrittenBook(OpenWrittenBookS2CPacket packet, CallbackInfo info) {
        ItemStack stack = client.player.getStackInHand(packet.getHand());
        CustomBookScreen customScreen = (CustomBookScreen) new BookScreen(new BookScreen.WrittenBookContents(stack));
        if (stack.isOf(ItemRegistry.SLENDER_GRIMOIRE)) {
            customScreen.setScreenType(CustomBookScreen.CustomScreenType.SLENDER_GRIMOIRE);
        }
        else if (stack.isOf(ItemRegistry.NOTE)) {
            customScreen.setScreenType(CustomBookScreen.CustomScreenType.NOTE);
        }
        client.setScreen((Screen) customScreen);
    }
}
