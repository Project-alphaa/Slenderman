package kelvin.slendermod.client.entity.renderers;

import kelvin.slendermod.client.entity.models.ModelSafeBlockItem;
import kelvin.slendermod.item.ItemSafeBlock;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class RendererSafeBlockItem extends GeoItemRenderer<ItemSafeBlock> {
    public RendererSafeBlockItem() {
        super(new ModelSafeBlockItem());
    }
}
