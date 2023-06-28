package kelvin.slendermod.client.entity.renderers;

import kelvin.slendermod.client.entity.models.SlendermanModel;
import kelvin.slendermod.entity.SlendermanEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class SlendermanRenderer extends GeoEntityRenderer<SlendermanEntity> {

    public SlendermanRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new SlendermanModel());
    }
}
