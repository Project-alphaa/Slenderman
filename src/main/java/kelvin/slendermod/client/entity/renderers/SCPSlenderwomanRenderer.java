package kelvin.slendermod.client.entity.renderers;

import kelvin.slendermod.client.entity.models.SCPSlenderwomanModel;
import kelvin.slendermod.entity.AdultSCPSlenderEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class SCPSlenderwomanRenderer extends GeoEntityRenderer<AdultSCPSlenderEntity> {

    public SCPSlenderwomanRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new SCPSlenderwomanModel());
    }
}
