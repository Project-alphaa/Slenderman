package kelvin.slendermod.mixin;

import kelvin.slendermod.registry.ItemRegistry;
import kelvin.slendermod.util.NoteScreen;
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
        Screen screen = new BookScreen(new BookScreen.WrittenBookContents(stack));
        if (stack.isOf(ItemRegistry.SLENDER_GRIMOIRE)) {
            client.setScreen(screen);
        }
        else if (stack.isOf(ItemRegistry.NOTE)) {
            NoteScreen noteScreen = (NoteScreen) new BookScreen(new BookScreen.WrittenBookContents(stack));
            noteScreen.isNote();
            client.setScreen((Screen) noteScreen);
        }
    }
}
