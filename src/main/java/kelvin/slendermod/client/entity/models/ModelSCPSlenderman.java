package kelvin.slendermod.client.entity.models;

import kelvin.slendermod.entity.EntityAdultSCPSlender;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

import static kelvin.slendermod.SlenderMod.id;

public class ModelSCPSlenderman extends GeoModel<EntityAdultSCPSlender> {

    @Override
    public Identifier getModelResource(EntityAdultSCPSlender object) {
        return id("geo/scp_slenderman.geo.json");
    }

    @Override
    public Identifier getTextureResource(EntityAdultSCPSlender object) {
        return id("textures/entities/scp_slenderman.png");
    }

    @Override
    public Identifier getAnimationResource(EntityAdultSCPSlender animatable) {
        return id("animations/scp_slenderman.animation.json");
    }

    @Override
    public RenderLayer getRenderType(EntityAdultSCPSlender animatable, Identifier texture) {
        return RenderLayer.getEntityCutoutNoCull(texture);
    }
}