package kelvin.slendermod.client.entity.models;

import kelvin.slendermod.SlenderMod;
import kelvin.slendermod.blockentity.SafeBlockEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

public class ModelSafeBlock extends GeoModel<SafeBlockEntity> {
    @Override
    public Identifier getModelResource(SafeBlockEntity animatable) {
        return new Identifier(SlenderMod.MODID, "geo/safe.geo.json");
    }

    @Override
    public Identifier getTextureResource(SafeBlockEntity animatable) {
        return new Identifier(SlenderMod.MODID, "textures/block/safe.png");
    }

    @Override
    public Identifier getAnimationResource(SafeBlockEntity animatable) {
        return new Identifier(SlenderMod.MODID, "animations/safe.animation.json");
    }
}
