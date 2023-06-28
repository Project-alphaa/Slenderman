package kelvin.slendermod.client.entity.renderers;

import kelvin.slendermod.client.entity.models.SafeBlockItemModel;
import kelvin.slendermod.item.ItemSafeBlock;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class SafeBlockItemRenderer extends GeoItemRenderer<ItemSafeBlock> {
    public SafeBlockItemRenderer() {
        super(new SafeBlockItemModel());
    }
}
