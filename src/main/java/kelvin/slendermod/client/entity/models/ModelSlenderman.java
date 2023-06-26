package kelvin.slendermod.client.entity.models;

import kelvin.slendermod.entity.EntitySlenderman;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

import static kelvin.slendermod.SlenderMod.id;

public class ModelSlenderman extends GeoModel<EntitySlenderman> {

    @Override
    public Identifier getModelResource(EntitySlenderman object) {
        return id("geo/slenderman.geo.json");
    }

    @Override
    public Identifier getTextureResource(EntitySlenderman object) {
        return id("textures/entities/slenderman.png");
    }

    @Override
    public Identifier getAnimationResource(EntitySlenderman animatable) {
        return id("animations/slenderman.animation.json");
    }

    @Override
    public RenderLayer getRenderType(EntitySlenderman animatable, Identifier texture) {
        return RenderLayer.getEntityCutoutNoCull(texture);
    }
}