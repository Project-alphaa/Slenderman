package kelvin.slendermod.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.ItemStack;
import net.minecraft.item.WrittenBookItem;

public class ItemNote extends WrittenBookItem {

    public ItemNote() {
        super(new FabricItemSettings());
    }


    @Override
    public boolean hasGlint(ItemStack stack) {
        return false;
    }
}
