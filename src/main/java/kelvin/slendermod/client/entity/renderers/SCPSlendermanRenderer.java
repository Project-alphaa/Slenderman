package kelvin.slendermod.client.entity.renderers;

import kelvin.slendermod.client.entity.models.SCPSlendermanModel;
import kelvin.slendermod.entity.AdultSCPSlenderEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class SCPSlendermanRenderer extends GeoEntityRenderer<AdultSCPSlenderEntity> {

    public SCPSlendermanRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new SCPSlendermanModel());
    }
}
