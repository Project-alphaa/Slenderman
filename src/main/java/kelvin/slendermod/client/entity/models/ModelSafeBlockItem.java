package kelvin.slendermod.client.entity.models;

import kelvin.slendermod.SlenderMod;
import kelvin.slendermod.item.ItemSafeBlock;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

public class ModelSafeBlockItem extends GeoModel<ItemSafeBlock> {
    @Override
    public Identifier getModelResource(ItemSafeBlock animatable) {
        return new Identifier(SlenderMod.MODID, "geo/safe.geo.json");
    }

    @Override
    public Identifier getTextureResource(ItemSafeBlock animatable) {
        return new Identifier(SlenderMod.MODID, "textures/block/safe.png");
    }

    @Override
    public Identifier getAnimationResource(ItemSafeBlock animatable) {
        return new Identifier(SlenderMod.MODID, "animations/safe.animation.json");
    }
}
