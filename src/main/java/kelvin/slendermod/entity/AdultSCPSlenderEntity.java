package kelvin.slendermod.entity;

import kelvin.slendermod.entity.ai.mm.MMEntityMoveHelper;
import kelvin.slendermod.registry.SoundRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.world.World;
import software.bernie.geckolib.core.animation.Animation;
import software.bernie.geckolib.core.animation.RawAnimation;

public class AdultSCPSlenderEntity extends AbstractSCPSlenderEntity {

    private static final TrackedData<Integer> ANIMATION_SET = DataTracker.registerData(AdultSCPSlenderEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final RawAnimation ANIM_IDLE = RawAnimation.begin().then("idle", Animation.LoopType.LOOP);
    private static final RawAnimation ANIM_WALK = RawAnimation.begin().then("walking", Animation.LoopType.LOOP);
    private static final RawAnimation ANIM_ROAR = RawAnimation.begin().then("roar", Animation.LoopType.PLAY_ONCE);
    private static final RawAnimation ANIM_RUN = RawAnimation.begin().then("chasing", Animation.LoopType.LOOP);
    private static final RawAnimation ANIM_LOOK = RawAnimation.begin().then("looking", Animation.LoopType.LOOP);
    private static final RawAnimation ANIM_ATTACK = RawAnimation.begin().then("attack_one_hand", Animation.LoopType.PLAY_ONCE);
    private static final RawAnimation ANIM_ATTACK2 = RawAnimation.begin().then("attack_both_hands", Animation.LoopType.PLAY_ONCE);
    private static final RawAnimation ANIM_CRYING = RawAnimation.begin().then("crying", Animation.LoopType.LOOP);

    private static final AnimationSet ANIMATIONS = new AnimationSet(ANIM_IDLE, ANIM_WALK, ANIM_CRYING, ANIM_RUN, ANIM_LOOK, ANIM_ATTACK);
    private static final AnimationSet ANIMATIONS2 = new AnimationSet(ANIM_IDLE, ANIM_WALK, ANIM_CRYING, ANIM_RUN, ANIM_LOOK, ANIM_ATTACK2);

    public AdultSCPSlenderEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
        this.stepHeight = 1.0f;
        this.moveControl = new MMEntityMoveHelper(this, 15);
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 40);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(ANIMATION_SET, this.random.nextInt(2));
    }

    @Override
    protected boolean canBreakDoors() {
        return false;
    }

    @Override
    protected float getFastMovementSpeed() {
        return 0.65f;
    }

    @Override
    protected float getSlowMovementSpeed() {
        return 0.45f;
    }

    @Override
    protected int getDamage() {
        return 15;
    }

    @Override
    protected int getReach() {
        return 4;
    }

    @Override
    protected int getDamageHeight() {
        return 2;
    }

    @Override
    protected DetectionRadius getLightDetectionRadius() {
        return new DetectionRadius(30, 20, 10);
    }

    @Override
    protected DetectionRadius getDarkDetectionRadius() {
        return new DetectionRadius(40, 30, 20);
    }

    @Override
    protected DetectionRadius getLitTargetRadius() {
        return new DetectionRadius(50, 40, 30);
    }

    @Override
    protected SoundEvent getAngrySound() {
        return SoundRegistry.HORROR_ROAR;
    }

    @Override
    protected SoundEvent getLookingSound() {
        return SoundRegistry.HORROR_GROWL;
    }

    protected AnimationSet getAnimationSet() {
        return this.dataTracker.get(ANIMATION_SET) == 0 ? ANIMATIONS : ANIMATIONS2;
    }
}
