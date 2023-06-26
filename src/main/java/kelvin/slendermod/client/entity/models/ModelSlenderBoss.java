package kelvin.slendermod.client.entity.models;

import kelvin.slendermod.entity.EntitySlenderBoss;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

import static kelvin.slendermod.SlenderMod.id;

public class ModelSlenderBoss extends GeoModel<EntitySlenderBoss> {

    @Override
    public Identifier getModelResource(EntitySlenderBoss object) {
        return id("geo/slender_boss.geo.json");
    }

    @Override
    public Identifier getTextureResource(EntitySlenderBoss object) {
        return id("textures/entities/slender_boss.png");
    }

    @Override
    public Identifier getAnimationResource(EntitySlenderBoss animatable) {
        return id("animations/slender_boss.animation.json");
    }

    @Override
    public RenderLayer getRenderType(EntitySlenderBoss animatable, Identifier texture) {
        return RenderLayer.getEntityCutoutNoCull(texture);
    }
}