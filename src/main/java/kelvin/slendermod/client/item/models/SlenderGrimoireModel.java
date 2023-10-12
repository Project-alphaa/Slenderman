package kelvin.slendermod.client.item.models;

import kelvin.slendermod.item.SlenderGrimoireItem;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

import static kelvin.slendermod.SlenderMod.id;

public class SlenderGrimoireModel extends GeoModel<SlenderGrimoireItem> {

    @Override
    public Identifier getModelResource(SlenderGrimoireItem animatable) {
        return id("geo/slender_grimoire.geo.json");
    }

    @Override
    public Identifier getTextureResource(SlenderGrimoireItem animatable) {
        return id("textures/item/slender_grimoire.png");
    }

    @Override
    public Identifier getAnimationResource(SlenderGrimoireItem animatable) {
        return id("animations/slender_grimoire.animation.json");
    }
}
