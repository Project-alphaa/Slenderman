package kelvin.slendermod.mixin;

import kelvin.slendermod.util.CustomBookScreen;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.ingame.BookEditScreen;
import net.minecraft.client.gui.screen.ingame.BookScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({BookScreen.class, BookEditScreen.class})
public class SharedBookScreenMixin implements CustomBookScreen {

    @Unique
    private CustomScreenType screenType = CustomScreenType.DEFAULT;

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderTexture(ILnet/minecraft/util/Identifier;)V"))
    private void render(int id, Identifier texture) {
        renderAsCustomScreen(screenType, texture);
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/font/TextRenderer;draw(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/text/Text;FFI)I"))
    private int drawPageNumber(TextRenderer textRenderer, MatrixStack matrices, Text text, float x, float y, int color) {
        return !screenType.isDefault() ? textRenderer.draw(matrices, text, x, y, screenType.getTextColor()) : textRenderer.draw(matrices, text, x, y, color);
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/font/TextRenderer;draw(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/text/OrderedText;FFI)I"))
    private int drawPageLine(TextRenderer textRenderer, MatrixStack matrices, OrderedText text, float x, float y, int color) {
        return !screenType.isDefault() ? textRenderer.draw(matrices, text, x, y, screenType.getTextColor()) : textRenderer.draw(matrices, text, x, y, color);
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
