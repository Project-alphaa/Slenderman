package kelvin.slendermod.item.medkit;

import kelvin.slendermod.SlenderMod;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

public class MedicalKitModel extends GeoModel<BaseMedicalKitItem> {
    @Override
    public Identifier getModelResource(BaseMedicalKitItem animatable) {
        return SlenderMod.id("geo/medical_kit.geo.json");
    }

    @Override
    public Identifier getTextureResource(BaseMedicalKitItem animatable) {
        return SlenderMod.id("textures/item/medical_kit.png");

    }

    @Override
    public Identifier getAnimationResource(BaseMedicalKitItem animatable) {
        return SlenderMod.id("animations/medical_kit.animation.json");

    }
}
