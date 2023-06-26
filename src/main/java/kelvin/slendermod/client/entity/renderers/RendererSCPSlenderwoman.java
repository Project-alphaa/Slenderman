package kelvin.slendermod.client.entity.renderers;

import kelvin.slendermod.client.entity.models.ModelSCPSlenderwoman;
import kelvin.slendermod.entity.EntityAdultSCPSlender;
import net.minecraft.client.render.entity.EntityRendererFactory;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class RendererSCPSlenderwoman extends GeoEntityRenderer<EntityAdultSCPSlender> {

    public RendererSCPSlenderwoman(EntityRendererFactory.Context renderManager) {
        super(renderManager, new ModelSCPSlenderwoman());
    }
}
