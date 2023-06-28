package kelvin.slendermod.client.entity.renderers;

import kelvin.slendermod.client.entity.models.SmallSCPSlenderModel;
import kelvin.slendermod.entity.SmallSCPSlenderEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class SmallSCPSlenderRenderer extends GeoEntityRenderer<SmallSCPSlenderEntity> {

    public SmallSCPSlenderRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new SmallSCPSlenderModel());
    }

    @Override
    public void render(SmallSCPSlenderEntity entity, float entityYaw, float partialTicks, MatrixStack stack, VertexConsumerProvider bufferIn, int packedLightIn) {
        stack.push();
        stack.scale(0.5f, 0.5f, 0.5f);
        super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
        stack.pop();
    }
}
