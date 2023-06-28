package kelvin.slendermod.mixin;

import kelvin.slendermod.entity.EntitySlenderBoss;
import kelvin.slendermod.registry.BlockRegistry;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin extends LivingEntityRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {

    public PlayerEntityRendererMixin(EntityRendererFactory.Context ctx, PlayerEntityModel<AbstractClientPlayerEntity> model, float shadowRadius) {
        super(ctx, model, shadowRadius);
    }

    @Inject(at = @At("HEAD"), method = "render", cancellable = true)
    public void render(AbstractClientPlayerEntity player, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo info) {
        if (player.getVehicle() instanceof EntitySlenderBoss) {
            info.cancel();
        }
    }

    @Inject(method = "setModelPose", at = @At("RETURN"))
    private void setModelPose(AbstractClientPlayerEntity player, CallbackInfo ci) {
        ItemStack helmet = player.getEquippedStack(EquipmentSlot.HEAD);
        if (helmet.isOf(BlockRegistry.SCP_SLENDERMAN_HEAD.asItem()) && !player.isSpectator()) {
            this.getModel().head.visible = false;
        }
    }
}
