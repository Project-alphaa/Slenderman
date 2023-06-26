package kelvin.slendermod.entity;

import kelvin.slendermod.registry.SoundRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;

public class EntitySlenderBoss extends PathAwareEntity implements GeoEntity {

    private static final TrackedData<Integer> CURRENT_ANIMATION = DataTracker.registerData(EntitySlenderBoss.class, TrackedDataHandlerRegistry.INTEGER);

    private static final RawAnimation ANIM_IDLE = RawAnimation.begin().then("animation.slender_boss.idle", Animation.LoopType.LOOP);
    private static final RawAnimation ANIM_WALK = RawAnimation.begin().then("animation.slender_boss.crawl", Animation.LoopType.LOOP);
    private static final RawAnimation ANIM_ATTACK = RawAnimation.begin().then("animation.slender_boss.attack", Animation.LoopType.PLAY_ONCE);
    private static final RawAnimation ANIM_DASH = RawAnimation.begin().then("animation.slender_boss.dash", Animation.LoopType.PLAY_ONCE);
    private static final List<RawAnimation> ANIMATIONS = List.of(ANIM_IDLE, ANIM_WALK, ANIM_ATTACK, ANIM_DASH);

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private final Vec3d startPos;
    private State currentState = State.DEFAULT;
    private int timeInState = 0;
    private int moveTimer = 0;
    private final ServerBossBar bossBar = new ServerBossBar(Text.translatable("entity.slendermod.slender_boss"), BossBar.Color.RED, BossBar.Style.NOTCHED_6);

    public EntitySlenderBoss(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
        stepHeight = 1.0f;
        startPos = new Vec3d(getPos().x, getPos().y, getPos().z);
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 300.0).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25).add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 1.0).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 15.0);
    }

    protected void initDataTracker() {
        super.initDataTracker();
        dataTracker.startTracking(CURRENT_ANIMATION, 0);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putInt("currentAnimation", dataTracker.get(CURRENT_ANIMATION));
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        dataTracker.set(CURRENT_ANIMATION, nbt.getInt("currentAnimation"));
        if (hasCustomName()) {
            bossBar.setName(getDisplayName());
        }
    }

    @Override
    public void setCustomName(@Nullable Text name) {
        super.setCustomName(name);
        bossBar.setName(name);
    }

    @Override
    public void pushAway(Entity entity) {
    }

    @Override
    public void pushAwayFrom(Entity entity) {
    }

    @Override
    public void tick() {
        super.tick();

        if (!world.isClient()) {
            if (world.getTime() % (100) == 0) {
                if (getCurrentState() != State.ATTACK || getCurrentState() != State.DASH) {
                    playSound(SoundRegistry.BOSS_IDLE, 1, 1);
                }
            }

            RawAnimation animation = ANIM_IDLE;
            float yaw = -(float) Math.toRadians(getYaw());
            switch (getCurrentState()) {
                case DEFAULT -> {
                    Entity first = getFirstPassenger();
                    if (first instanceof ServerPlayerEntity player) {

                        yaw = -(float) Math.toRadians(player.getYaw());

                        float lookX = (float) Math.sin(yaw);
                        float lookZ = (float) Math.cos(yaw);

                        float motionX = lookX * player.forwardSpeed * 0.2f;
                        float motionZ = lookZ * player.forwardSpeed * 0.2f;
                        ;

                        if (player.forwardSpeed != 0) {
                            animation = ANIM_WALK;
                            move(MovementType.SELF, new Vec3d(motionX, 0, motionZ));
                        }
                        setYaw(player.getYaw());
                        setBodyYaw(player.getBodyYaw());
                        setHeadYaw(player.getHeadYaw());
                    }
                    else {
                        if (moveTimer == 0) {
                            if (random.nextInt(100) == 0) {
                                int radius = 15;
                                navigation.startMovingTo((int) (startPos.x + random.nextInt(radius * 2) - radius), (int) startPos.y, (int) (startPos.z + random.nextInt(radius * 2) - radius), 1.0f);
                                moveTimer = 20 * 5;
                            }
                        }
                        else {
                            animation = ANIM_WALK;
                            moveTimer--;
                        }
                    }
                }
                case ATTACK -> {
                    animation = ANIM_ATTACK;
                    if (timeInState == 0) {
                        playSound(SoundRegistry.BOSS_ATTACK, 1, 1);
                    }
                    else if (timeInState == 10) {
                        float look_x = (float) Math.sin(yaw);
                        float look_z = (float) Math.cos(yaw);

                        setVelocity(new Vec3d(look_x, 0.1, look_z));

                        Vec3d forward = getForward(3);

                        double range = 2;

                        List<Entity> entities = world.getOtherEntities(this, new Box(forward.subtract(range, range, range), forward.add(range, range, range)));
                        for (Entity entity : entities) {
                            entity.damage(DamageSource.mob(this), 10);
                        }

                    }
                    else if (timeInState > 30) {
                        changeState(State.DEFAULT);
                    }
                }
                case DASH -> {
                    animation = ANIM_DASH;
                    if (timeInState == 0) {
                        float lookX = (float) Math.sin(yaw);
                        float lookZ = (float) Math.cos(yaw);

                        float motionX = lookX * 3;
                        float motionZ = lookZ * 3;

                        setVelocity(new Vec3d(motionX, 0.25, motionZ));
                        playSound(SoundRegistry.BOSS_DASH, 1, 1);
                    }
                    else if (timeInState > 5) {
                        changeState(State.DEFAULT);
                    }
                    else {

                        Vec3d forward = getForward(3);

                        double range = 2;

                        List<Entity> entities = world.getOtherEntities(this, new Box(forward.subtract(range, range, range), forward.add(range, range, range)));
                        for (Entity entity : entities) {
                            entity.damage(DamageSource.mob(this), 10);
                        }
                    }
                }
            }
            timeInState++;
            setCurrentAnimation(animation);
        }
        bossBar.setPercent(getHealth() / getMaxHealth());
    }

    public Vec3d getForward(double distance) {
        double yaw = -Math.toRadians(getYaw());
        return getPos().add(new Vec3d(Math.sin(yaw), 0, Math.cos(yaw)).multiply(distance));
    }

    @Override
    protected ActionResult interactMob(PlayerEntity player, Hand hand) {
        if (hand == Hand.OFF_HAND) {
            if (!hasPassengers()) {
                if (player.isCreative()) {
                    player.startRiding(this);
                }
            }
        }
        return super.interactMob(player, hand);
    }

    @Override
    public void onStartedTrackingBy(ServerPlayerEntity player) {
        super.onStartedTrackingBy(player);
        bossBar.addPlayer(player);
    }

    @Override
    public void onStoppedTrackingBy(ServerPlayerEntity player) {
        super.onStoppedTrackingBy(player);
        bossBar.removePlayer(player);
    }

    public void changeState(State newState) {
        if (currentState != newState) {
            currentState = newState;
            timeInState = 0;
        }
    }

    private void setCurrentAnimation(RawAnimation animation) {
        int index = ANIMATIONS.indexOf(animation);
        if (dataTracker.get(CURRENT_ANIMATION) != index) {
            dataTracker.set(CURRENT_ANIMATION, index);
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar data) {
        data.add(new AnimationController<>(this, "controller", 0, animation -> {
            var controller = animation.getController();
            controller.setAnimation(ANIMATIONS.get(dataTracker.get(CURRENT_ANIMATION)));
            controller.setTransitionLength(5);
            return PlayState.CONTINUE;
        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    public State getCurrentState() {
        return currentState;
    }

    public enum State {
        DEFAULT,
        ATTACK,
        DASH
    }
}
