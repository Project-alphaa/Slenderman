package kelvin.slendermod.entity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.Animation;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class EntitySlenderman extends EndermanEntity implements GeoEntity {

    private static final RawAnimation ANIM_IDLE = RawAnimation.begin().then("idle", Animation.LoopType.LOOP);
    private static final RawAnimation ANIM_WALKING = RawAnimation.begin().then("walking", Animation.LoopType.LOOP);
    private static final RawAnimation ANIM_RUNNING = RawAnimation.begin().then("running", Animation.LoopType.LOOP);
    private static final RawAnimation ANIM_ATTACK_SLAM = RawAnimation.begin().then("slam_attack", Animation.LoopType.PLAY_ONCE);
    private static final RawAnimation ANIM_ATTACK_SWING = RawAnimation.begin().then("swing_attack", Animation.LoopType.PLAY_ONCE);
    private static final RawAnimation ANIM_CHOKING = RawAnimation.begin().then("choking", Animation.LoopType.PLAY_ONCE);

    private static final TrackedData<Boolean> ATTACKING = DataTracker.registerData(EntitySlenderman.class, TrackedDataHandlerRegistry.BOOLEAN);

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private int attackTimer = 0;

    public EntitySlenderman(EntityType<? extends EndermanEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(ATTACKING, false);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new ChasePlayerGoal(this));
        this.goalSelector.add(2, new SlendermanAttackGoal(this, 1.0, false));
        this.goalSelector.add(7, new WanderAroundFarGoal(this, 1.0, 0.0F));
        this.goalSelector.add(8, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(8, new LookAroundGoal(this));
        this.targetSelector.add(1, new TeleportTowardsPlayerGoal(this, this::shouldAngerAt));
        this.targetSelector.add(2, new RevengeGoal(this));
        this.targetSelector.add(4, new UniversalAngerGoal<>(this, false));
    }

    @Override
    public void tickMovement() {
        super.tickMovement();

        if (this.isAttacking()) {
            this.attackTimer--;
        }

        if (this.attackTimer <= 0) {
            this.setAttacking(false);
        }
    }

    @Nullable
    @Override
    public BlockState getCarriedBlock() {
        return null;
    }

    @Override
    public void setCarriedBlock(@Nullable BlockState state) {
    }

    @Override
    public void setAttacking(boolean attacking) {
        this.dataTracker.set(ATTACKING, attacking);
        this.attackTimer = attacking ? 7 : 0;
    }

    @Override
    public boolean isAttacking() {
        return this.dataTracker.get(ATTACKING);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller", 0, state -> {
            var controller = state.getController();
            if (this.isAttacking()) {
                //                if (random.nextBoolean()) {
                //                    controller.setAnimation(ANIM_ATTACK_SLAM);
                //                }
                //                else {
                controller.setAnimation(ANIM_ATTACK_SWING);
                //                }
            } else if (state.isMoving()) {
                if (this.getMoveControl().getSpeed() > 1) {
                    controller.setAnimation(ANIM_RUNNING);
                } else {
                    controller.setAnimation(ANIM_WALKING);
                }
            } else {
                controller.setAnimation(ANIM_IDLE);
            }

            return PlayState.CONTINUE;
        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    private static class SlendermanAttackGoal extends MeleeAttackGoal {

        private final EntitySlenderman slenderman;

        public SlendermanAttackGoal(EntitySlenderman mob, double speed, boolean pauseWhenMobIdle) {
            super(mob, speed, pauseWhenMobIdle);
            this.slenderman = mob;
        }

        @Override
        protected void attack(LivingEntity target, double squaredDistance) {
            if (this.slenderman.attackTimer <= 0) {
                super.attack(target, squaredDistance);
            }
        }

        @Override
        protected void resetCooldown() {
            super.resetCooldown();
            this.slenderman.setAttacking(true);
        }
    }
}
