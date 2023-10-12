package kelvin.slendermod.client.entity.models;

import kelvin.slendermod.entity.AdultSCPSlenderEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

import static kelvin.slendermod.SlenderMod.id;

public class SCPSlendermanModel extends GeoModel<AdultSCPSlenderEntity> {

    @Override
    public Identifier getModelResource(AdultSCPSlenderEntity object) {
        return id("geo/scp_slenderman.geo.json");
    }

    @Override
    public Identifier getTextureResource(AdultSCPSlenderEntity object) {
        return id("textures/entities/scp_slenderman.png");
    }

    @Override
    public Identifier getAnimationResource(AdultSCPSlenderEntity animatable) {
        return id("animations/scp_slenderman.animation.json");
    }
}