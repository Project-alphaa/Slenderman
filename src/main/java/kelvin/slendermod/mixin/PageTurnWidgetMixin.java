package kelvin.slendermod.mixin;

import kelvin.slendermod.util.NoteScreen;
import net.minecraft.client.gui.widget.PageTurnWidget;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PageTurnWidget.class)
public class PageTurnWidgetMixin implements NoteScreen {

    private boolean isNote = false;

    @Redirect(method = "renderButton", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderTexture(ILnet/minecraft/util/Identifier;)V"))
    private void render(int texture, Identifier id) {
        this.renderAsNote(this.isNote, id);
    }

    @Override
    public void isNote() {
        this.isNote = true;
    }
}
