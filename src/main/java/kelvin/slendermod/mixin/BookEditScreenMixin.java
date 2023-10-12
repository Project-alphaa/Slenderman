package kelvin.slendermod.mixin;

import kelvin.slendermod.util.CustomBookScreen;
import net.minecraft.client.gui.screen.ingame.BookEditScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.PageTurnWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BookEditScreen.class)
public abstract class BookEditScreenMixin implements CustomBookScreen {

    @Redirect(method = "init", at = @At(value = "NEW", target = "(IIZLnet/minecraft/client/gui/widget/ButtonWidget$PressAction;Z)Lnet/minecraft/client/gui/widget/PageTurnWidget;"))
    private PageTurnWidget init(int x, int y, boolean isNextPageButton, ButtonWidget.PressAction action, boolean playPageTurnSound) {
        return setPageButtons(x, y, isNextPageButton, action, playPageTurnSound);
    }
}
