package kelvin.slendermod.client.entity.renderers;

import kelvin.slendermod.blockentity.SafeBlockEntity;
import kelvin.slendermod.client.entity.models.SafeBlockModel;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class SafeBlockRenderer extends GeoBlockRenderer<SafeBlockEntity> {
    public SafeBlockRenderer(BlockEntityRendererFactory.Context ignoredContext) {
        super(new SafeBlockModel());
    }
}
