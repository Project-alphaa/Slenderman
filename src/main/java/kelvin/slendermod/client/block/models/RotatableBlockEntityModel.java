package kelvin.slendermod.client.block.models;

import kelvin.slendermod.blockentity.RotatableBlockEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

import static kelvin.slendermod.SlenderMod.id;

public class RotatableBlockEntityModel<T extends RotatableBlockEntity> extends GeoModel<T> {

    public RotatableBlockEntityModel() {
    }

    @Override
    public Identifier getModelResource(T animatable) {
        return id("geo/" + animatable.getResourceId() + ".geo.json");
    }

    @Override
    public Identifier getTextureResource(T animatable) {
        return id("textures/block/" + animatable.getResourceId() + ".png");
    }

    @Override
    public Identifier getAnimationResource(T animatable) {
        return id("animations/" + animatable.getResourceId() + ".animation.json");
    }
}
