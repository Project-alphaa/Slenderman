package kelvin.slendermod.client.entity.models;

import kelvin.slendermod.entity.SlenderBossEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

import static kelvin.slendermod.SlenderMod.id;

public class SlenderBossModel extends GeoModel<SlenderBossEntity> {

    @Override
    public Identifier getModelResource(SlenderBossEntity object) {
        return id("geo/slender_boss.geo.json");
    }

    @Override
    public Identifier getTextureResource(SlenderBossEntity object) {
        return id("textures/entities/slender_boss.png");
    }

    @Override
    public Identifier getAnimationResource(SlenderBossEntity animatable) {
        return id("animations/slender_boss.animation.json");
    }

}