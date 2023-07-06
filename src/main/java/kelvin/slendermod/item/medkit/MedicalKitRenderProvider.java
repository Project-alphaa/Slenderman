package kelvin.slendermod.item.medkit;

import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import software.bernie.geckolib.animatable.client.RenderProvider;

public class MedicalKitRenderProvider implements RenderProvider {


    private final MedicalKitRenderer renderer = new MedicalKitRenderer();



    @Override
    public BuiltinModelItemRenderer getCustomRenderer() {
        return renderer;
    }
}
