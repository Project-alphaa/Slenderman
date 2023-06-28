package kelvin.slendermod.client.entity.models;

import kelvin.slendermod.entity.SmallSCPSlenderEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

import static kelvin.slendermod.SlenderMod.id;

public class SmallSCPSlenderModel extends GeoModel<SmallSCPSlenderEntity> {

    @Override
    public Identifier getModelResource(SmallSCPSlenderEntity object) {
        return id("geo/small_scp_slender.geo.json");
    }

    @Override
    public Identifier getTextureResource(SmallSCPSlenderEntity object) {
        return id("textures/entities/small_scp_slender.png");
    }

    @Override
    public Identifier getAnimationResource(SmallSCPSlenderEntity animatable) {
        return id("animations/small_scp_slender.animation.json");
    }

}