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
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.Animation;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;

public class SlenderBossEntity extends PathAwareEntity implements GeoEntity {

    private static final TrackedData<Integer> CURRENT_ANIMATION = DataTracker.registerData(SlenderBossEntity.class, TrackedDataHandlerRegistry.INTEGER);

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

    public SlenderBossEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
        this.stepHeight = 1.0f;
        this.startPos = new Vec3d(this.getPos().x, this.getPos().y, this.getPos().z);
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 300.0).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25).add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 1.0).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 15.0);
    }

    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(CURRENT_ANIMATION, 0);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putInt("currentAnimation", this.dataTracker.get(CURRENT_ANIMATION));
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.dataTracker.set(CURRENT_ANIMATION, nbt.getInt("currentAnimation"));
        if (this.hasCustomName()) {
            this.bossBar.setName(this.getDisplayName());
        }
    }

    @Override
    public void setCustomName(@Nullable Text name) {
        super.setCustomName(name);
        this.bossBar.setName(name);
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

        if (!this.world.isClient()) {
            if (this.world.getTime() % (100) == 0) {
                if (this.getCurrentState() != State.ATTACK || this.getCurrentState() != State.DASH) {
                    this.playSound(SoundRegistry.BOSS_IDLE, 1, 1);
                }
            }

            RawAnimation animation = ANIM_IDLE;
            float yaw = -(float) Math.toRadians(this.getYaw());
            switch (this.getCurrentState()) {
                case DEFAULT -> {
                    Entity first = this.getFirstPassenger();
                    if (first instanceof ServerPlayerEntity player) {

                        yaw = -(float) Math.toRadians(player.getYaw());

                        float lookX = (float) Math.sin(yaw);
                        float lookZ = (float) Math.cos(yaw);

                        float motionX = lookX * player.forwardSpeed * 0.2f;
                        float motionZ = lookZ * player.forwardSpeed * 0.2f;

                        if (player.forwardSpeed != 0) {
                            animation = ANIM_WALK;
                            this.move(MovementType.SELF, new Vec3d(motionX, 0, motionZ));
                        }
                        this.setYaw(player.getYaw());
                        this.setBodyYaw(player.getBodyYaw());
                        this.setHeadYaw(player.getHeadYaw());
                    } else {
                        if (this.moveTimer == 0) {
                            if (this.random.nextInt(100) == 0) {
                                int radius = 15;
                                this.navigation.startMovingTo((int) (this.startPos.x + this.random.nextInt(radius * 2) - radius), (int) this.startPos.y, (int) (this.startPos.z + this.random.nextInt(radius * 2) - radius), 1.0f);
                                this.moveTimer = 20 * 5;
                            }
                        } else {
                            animation = ANIM_WALK;
                            this.moveTimer--;
                        }
                    }
                }
                case ATTACK -> {
                    animation = ANIM_ATTACK;
                    if (this.timeInState == 0) {
                        this.playSound(SoundRegistry.BOSS_ATTACK, 1, 1);
                    } else if (this.timeInState == 10) {
                        float look_x = (float) Math.sin(yaw);
                        float look_z = (float) Math.cos(yaw);

                        this.setVelocity(new Vec3d(look_x, 0.1, look_z));

                        Vec3d forward = this.getForward(3);

                        double range = 2;

                        List<Entity> entities = this.world.getOtherEntities(this, new Box(forward.subtract(range, range, range), forward.add(range, range, range)));
                        for (Entity entity : entities) {
                            entity.damage(DamageSource.mob(this), 10);
                        }

                    } else if (this.timeInState > 30) {
                        this.changeState(State.DEFAULT);
                    }
                }
                case DASH -> {
                    animation = ANIM_DASH;
                    if (this.timeInState == 0) {
                        float lookX = (float) Math.sin(yaw);
                        float lookZ = (float) Math.cos(yaw);

                        float motionX = lookX * 3;
                        float motionZ = lookZ * 3;

                        this.setVelocity(new Vec3d(motionX, 0.25, motionZ));
                        this.playSound(SoundRegistry.BOSS_DASH, 1, 1);
                    } else if (this.timeInState > 5) {
                        this.changeState(State.DEFAULT);
                    } else {

                        Vec3d forward = this.getForward(3);

                        double range = 2;

                        List<Entity> entities = this.world.getOtherEntities(this, new Box(forward.subtract(range, range, range), forward.add(range, range, range)));
                        for (Entity entity : entities) {
                            entity.damage(DamageSource.mob(this), 10);
                        }
                    }
                }
            }
            this.timeInState++;
            this.setCurrentAnimation(animation);
        }
        this.bossBar.setPercent(this.getHealth() / this.getMaxHealth());
    }

    public Vec3d getForward(double distance) {
        double yaw = -Math.toRadians(this.getYaw());
        return this.getPos().add(new Vec3d(Math.sin(yaw), 0, Math.cos(yaw)).multiply(distance));
    }

    @Override
    protected ActionResult interactMob(PlayerEntity player, Hand hand) {
        if (hand == Hand.OFF_HAND) {
            if (!this.hasPassengers()) {
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
        this.bossBar.addPlayer(player);
    }

    @Override
    public void onStoppedTrackingBy(ServerPlayerEntity player) {
        super.onStoppedTrackingBy(player);
        this.bossBar.removePlayer(player);
    }

    public void changeState(State newState) {
        if (this.currentState != newState) {
            this.currentState = newState;
            this.timeInState = 0;
        }
    }

    private void setCurrentAnimation(RawAnimation animation) {
        int index = ANIMATIONS.indexOf(animation);
        if (this.dataTracker.get(CURRENT_ANIMATION) != index) {
            this.dataTracker.set(CURRENT_ANIMATION, index);
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar data) {
        data.add(new AnimationController<>(this, "controller", 0, animation -> {
            var controller = animation.getController();
            controller.setAnimation(ANIMATIONS.get(this.dataTracker.get(CURRENT_ANIMATION)));
            controller.setTransitionLength(5);
            return PlayState.CONTINUE;
        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    public State getCurrentState() {
        return this.currentState;
    }

    public enum State {
        DEFAULT, ATTACK, DASH
    }
}
