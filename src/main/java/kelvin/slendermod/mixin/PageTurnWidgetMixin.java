package kelvin.slendermod.mixin;

import kelvin.slendermod.util.CustomBookScreen;
import net.minecraft.client.gui.widget.PageTurnWidget;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PageTurnWidget.class)
public class PageTurnWidgetMixin implements CustomBookScreen {

    @Unique
    private CustomScreenType screenType = CustomScreenType.DEFAULT;

    @Redirect(method = "renderButton", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderTexture(ILnet/minecraft/util/Identifier;)V"))
    private void render(int texture, Identifier id) {
        renderAsCustomScreen(screenType, id);
    }

    @Override
    public void setScreenType(CustomScreenType type) {
        screenType = type;
    }

    @Override
    public CustomScreenType getScreenType() {
        return screenType;
    }
}
