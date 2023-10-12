package kelvin.slendermod.util;

import com.mojang.blaze3d.systems.RenderSystem;
import kelvin.slendermod.SlenderMod;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.PageTurnWidget;
import net.minecraft.util.Identifier;

public interface CustomBookScreen {

    void setScreenType(CustomScreenType type);

    CustomScreenType getScreenType();

    default void renderAsCustomScreen(CustomScreenType type, Identifier defaultTexture) {
        if (type != CustomScreenType.DEFAULT) {
            RenderSystem.setShaderTexture(0, type.getTexture());
        }
        else {
            RenderSystem.setShaderTexture(0, defaultTexture);
        }
    }

    default PageTurnWidget setPageButtons(int x, int y, boolean isNextPageButton, ButtonWidget.PressAction action, boolean playPageTurnSound) {
        PageTurnWidget widget = new PageTurnWidget(x, y, isNextPageButton, action, playPageTurnSound);
        CustomBookScreen.CustomScreenType screenType = getScreenType();
        if (!screenType.isDefault()) {
            ((CustomBookScreen) widget).setScreenType(screenType);
        }
        return widget;
    }

    enum CustomScreenType {
        NOTE(SlenderMod.id("note.png"), 0),
        SLENDER_GRIMOIRE(SlenderMod.id("slender_grimoire.png"), 0x8a6a55),
        DEFAULT;

        private Identifier texture;
        private int textColor;

        CustomScreenType() {
        }

        CustomScreenType(Identifier texture, int textColor) {
            this.texture = texture.withPrefixedPath("textures/gui/");
            this.textColor = textColor;
        }

        public boolean isDefault() {
            return this == DEFAULT;
        }

        public Identifier getTexture() {
            return texture;
        }

        public int getTextColor() {
            return textColor;
        }
    }
}
