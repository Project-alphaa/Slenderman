package kelvin.slendermod.client.entity.models;

import kelvin.slendermod.SlenderMod;
import kelvin.slendermod.blockentity.SafeBlockEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

public class SafeBlockModel extends GeoModel<SafeBlockEntity> {
    @Override
    public Identifier getModelResource(SafeBlockEntity animatable) {
        return new Identifier(SlenderMod.MOD_ID, "geo/safe.geo.json");
    }

    @Override
    public Identifier getTextureResource(SafeBlockEntity animatable) {
        return new Identifier(SlenderMod.MOD_ID, "textures/block/safe.png");
    }

    @Override
    public Identifier getAnimationResource(SafeBlockEntity animatable) {
        return new Identifier(SlenderMod.MOD_ID, "animations/safe.animation.json");
    }
}
