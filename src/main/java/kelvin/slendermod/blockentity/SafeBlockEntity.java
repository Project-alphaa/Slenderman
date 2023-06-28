package kelvin.slendermod.blockentity;

import kelvin.slendermod.registry.BlockEntityRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class SafeBlockEntity extends BlockEntity implements GeoBlockEntity {

    private static final RawAnimation OPEN_ANIM = RawAnimation.begin().then("open", Animation.LoopType.PLAY_ONCE);
    private static final RawAnimation OPEN_IDLE_ANIM = RawAnimation.begin().then("open_idle", Animation.LoopType.LOOP);
    private static final RawAnimation CLOSE_ANIM = RawAnimation.begin().then("close", Animation.LoopType.PLAY_ONCE);
    private static final RawAnimation IDLE_ANIM = RawAnimation.begin().then("idle", Animation.LoopType.LOOP);

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private boolean isOpen = false;

    public SafeBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.SAFE_BLOCK_ENTITY, pos, state);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        isOpen = nbt.getBoolean("IsOpen");
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putBoolean("IsOpen", isOpen);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller", 5, this::predicate));
        controllerRegistrar.add(new AnimationController<>(this, "idle_controller", 0, state -> {
            state.getController().setAnimation(IDLE_ANIM);
            return PlayState.CONTINUE;
        }));
    }

    private PlayState predicate(AnimationState<GeoAnimatable> state) {
        AnimationController<GeoAnimatable> controller = state.getController();
        controller.triggerableAnim("open", OPEN_ANIM);
        controller.triggerableAnim("close", CLOSE_ANIM);

        if (controller.hasAnimationFinished()) {
            if (controller.getCurrentRawAnimation().equals(OPEN_ANIM)) {
                controller.setAnimation(OPEN_IDLE_ANIM);
            }
            else if (controller.getCurrentRawAnimation().equals(CLOSE_ANIM)) {
                controller.setAnimation(IDLE_ANIM);
            }
        }
        return PlayState.CONTINUE;
    }

    public void setOpen(boolean open) {
        isOpen = open;
        triggerAnim("controller", isOpen ? "open" : "close");
    }

    public boolean isOpen() {
        return isOpen;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}
