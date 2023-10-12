package kelvin.slendermod.mixin;

import kelvin.slendermod.util.CustomBookScreen;
import net.minecraft.client.gui.screen.ingame.BookScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.PageTurnWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BookScreen.class)
public abstract class BookScreenMixin implements CustomBookScreen {

    @Redirect(method = "addPageButtons", at = @At(value = "NEW", target = "(IIZLnet/minecraft/client/gui/widget/ButtonWidget$PressAction;Z)Lnet/minecraft/client/gui/widget/PageTurnWidget;"))
    private PageTurnWidget addPageButtons(int x, int y, boolean isNextPageButton, ButtonWidget.PressAction action, boolean playPageTurnSound) {
        return setPageButtons(x, y, isNextPageButton, action, playPageTurnSound);
    }
}
