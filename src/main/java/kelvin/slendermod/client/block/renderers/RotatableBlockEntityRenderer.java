package kelvin.slendermod.client.block.renderers;

import kelvin.slendermod.blockentity.RotatableBlockEntity;
import kelvin.slendermod.client.block.models.RotatableBlockEntityModel;
import kelvin.slendermod.registry.BlockRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Quaternionf;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class RotatableBlockEntityRenderer extends GeoBlockRenderer<RotatableBlockEntity> {

    public RotatableBlockEntityRenderer() {
        super(new RotatableBlockEntityModel<>());
    }

    @Override
    public void actuallyRender(MatrixStack poseStack, RotatableBlockEntity animatable, BakedGeoModel model, RenderLayer renderType, VertexConsumerProvider bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        Block block = animatable.getCachedState().getBlock();
        if (block == BlockRegistry.UFO_INTERIOR) {
            poseStack.scale(1.5F, 1.5F, 1.5F);
            poseStack.translate(-0.18F, 0.1F, 0);
        }

        if (block == BlockRegistry.SCRATCHED_DEAD_TREE || block == BlockRegistry.UFO_INTERIOR || block == BlockRegistry.DEAD_TREE || block == BlockRegistry.TRASH) {
            poseStack.multiply(new Quaternionf().rotateY((float) Math.toRadians(180)));
            poseStack.translate(-1, 0, -1);
        }

        super.actuallyRender(poseStack, animatable, model, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
