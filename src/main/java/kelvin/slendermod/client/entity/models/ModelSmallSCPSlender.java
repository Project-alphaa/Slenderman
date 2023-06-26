package kelvin.slendermod.client.entity.models;

import kelvin.slendermod.entity.EntitySmallSCPSlender;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

import static kelvin.slendermod.SlenderMod.id;

public class ModelSmallSCPSlender extends GeoModel<EntitySmallSCPSlender> {

    @Override
    public Identifier getModelResource(EntitySmallSCPSlender object) {
        return id("geo/small_scp_slender.geo.json");
    }

    @Override
    public Identifier getTextureResource(EntitySmallSCPSlender object) {
        return id("textures/entities/small_scp_slender.png");
    }

    @Override
    public Identifier getAnimationResource(EntitySmallSCPSlender animatable) {
        return id("animations/small_scp_slender.animation.json");
    }

    @Override
    public RenderLayer getRenderType(EntitySmallSCPSlender animatable, Identifier texture) {
        return RenderLayer.getEntityCutoutNoCull(texture);
    }
}