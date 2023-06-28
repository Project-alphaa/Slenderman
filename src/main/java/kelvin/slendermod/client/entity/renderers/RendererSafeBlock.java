package kelvin.slendermod.client.entity.renderers;

import kelvin.slendermod.blockentity.SafeBlockEntity;
import kelvin.slendermod.client.entity.models.ModelSafeBlock;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class RendererSafeBlock extends GeoBlockRenderer<SafeBlockEntity> {
    public RendererSafeBlock(BlockEntityRendererFactory.Context ignoredContext) {
        super(new ModelSafeBlock());
    }
}
