package kelvin.slendermod.client.entity.renderers;

import kelvin.slendermod.client.entity.models.ModelSlenderman;
import kelvin.slendermod.entity.EntitySlenderman;
import net.minecraft.client.render.entity.EntityRendererFactory;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class RendererSlenderman extends GeoEntityRenderer<EntitySlenderman> {

    public RendererSlenderman(EntityRendererFactory.Context renderManager) {
        super(renderManager, new ModelSlenderman());
    }
}
