package kelvin.slendermod.client.entity.models;

import kelvin.slendermod.entity.AdultSCPSlenderEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

import static kelvin.slendermod.SlenderMod.id;

public class SCPSlenderwomanModel extends GeoModel<AdultSCPSlenderEntity> {

    @Override
    public Identifier getModelResource(AdultSCPSlenderEntity object) {
        return id("geo/scp_slenderwoman.geo.json");
    }

    @Override
    public Identifier getTextureResource(AdultSCPSlenderEntity object) {
        return id("textures/entities/scp_slenderwoman.png");
    }

    @Override
    public Identifier getAnimationResource(AdultSCPSlenderEntity animatable) {
        return id("animations/scp_slenderwoman.animation.json");
    }
}