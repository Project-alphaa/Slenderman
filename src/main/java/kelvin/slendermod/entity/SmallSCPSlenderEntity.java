package kelvin.slendermod.entity;

import kelvin.slendermod.entity.ai.mm.MMEntityMoveHelper;
import kelvin.slendermod.registry.SoundRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.pathing.MobNavigation;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.world.World;
import software.bernie.geckolib.core.animation.Animation;
import software.bernie.geckolib.core.animation.RawAnimation;

public class SmallSCPSlenderEntity extends AbstractSCPSlenderEntity {

    private static final RawAnimation ANIM_IDLE = RawAnimation.begin().then("animation.idle", Animation.LoopType.LOOP);
    private static final RawAnimation ANIM_WALK = RawAnimation.begin().then("animation.walk", Animation.LoopType.LOOP);
    private static final RawAnimation ANIM_ROAR = RawAnimation.begin().then("animation.roar", Animation.LoopType.PLAY_ONCE);
    private static final RawAnimation ANIM_RUN = RawAnimation.begin().then("animation.running", Animation.LoopType.LOOP);
    private static final RawAnimation ANIM_LOOK = RawAnimation.begin().then("animation.look", Animation.LoopType.LOOP);
    private static final RawAnimation ANIM_ATTACK = RawAnimation.begin().then("animation.attack", Animation.LoopType.PLAY_ONCE);
    private static final AnimationSet ANIMATIONS = new AnimationSet(ANIM_IDLE, ANIM_WALK, ANIM_ROAR, ANIM_RUN, ANIM_LOOK, ANIM_ATTACK);

    public SmallSCPSlenderEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
        this.stepHeight = 1.0f;
        this.moveControl = new MMEntityMoveHelper(this, 90);
        ((MobNavigation) this.getNavigation()).setCanPathThroughDoors(true);
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 22);
    }

    @Override
    protected boolean canBreakDoors() {
        return true;
    }

    @Override
    protected float getFastMovementSpeed() {
        return 0.75f;
    }

    @Override
    protected float getSlowMovementSpeed() {
        return 0.65f;
    }

    @Override
    protected int getDamage() {
        return 5;
    }

    @Override
    protected int getReach() {
        return 2;
    }

    @Override
    protected int getDamageHeight() {
        return 0;
    }

    @Override
    protected DetectionRadius getLightDetectionRadius() {
        return new DetectionRadius(20, 10, 3);
    }

    @Override
    protected DetectionRadius getDarkDetectionRadius() {
        return new DetectionRadius(30, 15, 10);
    }

    @Override
    protected DetectionRadius getLitTargetRadius() {
        return new DetectionRadius(40, 30, 25);
    }

    @Override
    protected SoundEvent getAngrySound() {
        return SoundRegistry.SMALL_SLENDER_CHASING;
    }

    @Override
    protected SoundEvent getLookingSound() {
        return SoundRegistry.SMALL_SLENDER_LOOKING;
    }

    @Override
    protected AnimationSet getAnimationSet() {
        return ANIMATIONS;
    }
}
