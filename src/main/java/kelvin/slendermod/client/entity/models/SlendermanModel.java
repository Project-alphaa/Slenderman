package kelvin.slendermod.client.entity.models;

import kelvin.slendermod.entity.SlendermanEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

import static kelvin.slendermod.SlenderMod.id;

public class SlendermanModel extends GeoModel<SlendermanEntity> {

    @Override
    public Identifier getModelResource(SlendermanEntity object) {
        return id("geo/slenderman.geo.json");
    }

    @Override
    public Identifier getTextureResource(SlendermanEntity object) {
        return id("textures/entities/slenderman.png");
    }

    @Override
    public Identifier getAnimationResource(SlendermanEntity animatable) {
        return id("animations/slenderman.animation.json");
    }

}