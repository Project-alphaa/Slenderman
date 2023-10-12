package kelvin.slendermod.client.entity.models;

import kelvin.slendermod.SlenderMod;
import kelvin.slendermod.item.SafeBlockItem;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

public class SafeBlockItemModel extends GeoModel<SafeBlockItem> {

    @Override
    public Identifier getModelResource(SafeBlockItem animatable) {
        return new Identifier(SlenderMod.MOD_ID, "geo/safe.geo.json");
    }

    @Override
    public Identifier getTextureResource(SafeBlockItem animatable) {
        return new Identifier(SlenderMod.MOD_ID, "textures/block/safe.png");
    }

    @Override
    public Identifier getAnimationResource(SafeBlockItem animatable) {
        return new Identifier(SlenderMod.MOD_ID, "animations/safe.animation.json");
    }
}
