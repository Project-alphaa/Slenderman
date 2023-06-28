package kelvin.slendermod.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.ItemStack;
import net.minecraft.item.WrittenBookItem;

public class NoteItem extends WrittenBookItem {

    public NoteItem() {
        super(new FabricItemSettings());
    }


    @Override
    public boolean hasGlint(ItemStack stack) {
        return false;
    }
}
