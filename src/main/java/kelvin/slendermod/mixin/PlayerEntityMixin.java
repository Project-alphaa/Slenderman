package kelvin.slendermod.mixin;

import kelvin.slendermod.util.IForceCrawlingPlayer;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements IForceCrawlingPlayer {

    private boolean forcedCrawling = false;

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public void toggleForcedCrawling() {
        forcedCrawling = !forcedCrawling;
    }

    @Override
    public boolean isForcedCrawling() {
        return forcedCrawling;
    }

    @Inject(at = @At("HEAD"), method = "updatePose", cancellable = true)
    protected void updatePose(CallbackInfo info) {
        if (forcedCrawling) {
            setPose(EntityPose.SWIMMING);
            info.cancel();
        }
    }
}
