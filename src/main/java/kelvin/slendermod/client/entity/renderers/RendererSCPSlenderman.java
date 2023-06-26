package kelvin.slendermod.client.entity.renderers;

import kelvin.slendermod.client.entity.models.ModelSCPSlenderman;
import kelvin.slendermod.entity.EntityAdultSCPSlender;
import net.minecraft.client.render.entity.EntityRendererFactory;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class RendererSCPSlenderman extends GeoEntityRenderer<EntityAdultSCPSlender> {

    public RendererSCPSlenderman(EntityRendererFactory.Context renderManager) {
        super(renderManager, new ModelSCPSlenderman());
    }
}
