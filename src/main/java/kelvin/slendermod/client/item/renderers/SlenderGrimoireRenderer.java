package kelvin.slendermod.client.item.renderers;

import kelvin.slendermod.client.item.models.SlenderGrimoireModel;
import kelvin.slendermod.item.SlenderGrimoireItem;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class SlenderGrimoireRenderer extends GeoItemRenderer<SlenderGrimoireItem> {

    public SlenderGrimoireRenderer() {
        super(new SlenderGrimoireModel());
    }
}
