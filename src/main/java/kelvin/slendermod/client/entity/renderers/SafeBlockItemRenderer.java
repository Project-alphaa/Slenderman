package kelvin.slendermod.client.entity.renderers;

import kelvin.slendermod.client.entity.models.SafeBlockItemModel;
import kelvin.slendermod.item.SafeBlockItem;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class SafeBlockItemRenderer extends GeoItemRenderer<SafeBlockItem> {

    public SafeBlockItemRenderer() {
        super(new SafeBlockItemModel());
    }
}
