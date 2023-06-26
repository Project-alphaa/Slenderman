package kelvin.slendermod.client.entity.renderers;

import kelvin.slendermod.client.entity.models.ModelSmallSCPSlender;
import kelvin.slendermod.entity.EntitySmallSCPSlender;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class RendererSmallSCPSlender extends GeoEntityRenderer<EntitySmallSCPSlender> {

    public RendererSmallSCPSlender(EntityRendererFactory.Context renderManager) {
        super(renderManager, new ModelSmallSCPSlender());
    }

    @Override
    public void render(EntitySmallSCPSlender entity, float entityYaw, float partialTicks, MatrixStack stack, VertexConsumerProvider bufferIn, int packedLightIn) {
        stack.push();
        stack.scale(0.5f, 0.5f, 0.5f);
        super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
        stack.pop();
    }
}
