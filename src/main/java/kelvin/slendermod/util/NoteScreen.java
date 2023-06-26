package kelvin.slendermod.util;

import com.mojang.blaze3d.systems.RenderSystem;
import kelvin.slendermod.SlenderMod;
import net.minecraft.util.Identifier;

public interface NoteScreen {

    Identifier NOTE_TEXTURE = SlenderMod.id("textures/gui/note.png");

    void isNote();

    default void renderAsNote(boolean isNote, Identifier defaultTexture) {
        if (isNote) {
            RenderSystem.setShaderTexture(0, NOTE_TEXTURE);
        }
        else {
            RenderSystem.setShaderTexture(0, defaultTexture);
        }
    }
}
