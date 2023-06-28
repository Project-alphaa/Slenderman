package kelvin.slendermod.client.entity.renderers;

import kelvin.slendermod.client.entity.models.SlenderBossModel;
import kelvin.slendermod.entity.SlenderBossEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class SlenderBossRenderer extends GeoEntityRenderer<SlenderBossEntity> {

    public SlenderBossRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new SlenderBossModel());
    }

    @Override
    public void render(SlenderBossEntity entity, float entityYaw, float partialTicks, MatrixStack stack, VertexConsumerProvider bufferIn, int packedLightIn) {
        stack.push();
        stack.scale(0.5f, 0.5f, 0.5f);
        super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
        stack.pop();
    }
}
