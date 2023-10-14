package kelvin.slendermod.entity;

import com.google.common.collect.ImmutableList;
import dev.lambdaurora.lambdynlights.DynamicLightSource;
import kelvin.slendermod.SlenderMod;
import kelvin.slendermod.entity.ai.mm.MMPathNavigateGround;
import kelvin.slendermod.registry.BlockRegistry;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.FenceBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import net.minecraft.world.event.EntityPositionSource;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.event.PositionSource;
import net.minecraft.world.event.listener.EntityGameEventHandler;
import net.minecraft.world.event.listener.GameEventListener;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public abstract class AbstractSCPSlenderEntity extends PathAwareEntity implements GeoEntity {

    private static final TrackedData<Integer> CURRENT_ANIMATION = DataTracker.registerData(AbstractSCPSlenderEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Boolean> ROAR_TRACKER = DataTracker.registerData(AbstractSCPSlenderEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private final EntityGameEventHandler<SlenderRoarEventListener> slenderRoarEventHandler;
    private State currentState = State.IDLE;
    private int timeInState = 0;
    private int angerTimer;
    private int anger;
    private int searchCount;
    private Entity target;
    private Vec3d lastSeenPos;
    private BlockPos doorPos;
    private int doorHits;

    protected AbstractSCPSlenderEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
        PositionSource source = new EntityPositionSource(this, this.getStandingEyeHeight());
        this.slenderRoarEventHandler = new EntityGameEventHandler<>(new SlenderRoarEventListener(source, SlenderMod.GUN_SHOT.getRange()));
    }

    @Override
    protected EntityNavigation createNavigation(World world) {
        return new MMPathNavigateGround(this, world);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(CURRENT_ANIMATION, 0);
        this.dataTracker.startTracking(ROAR_TRACKER, false);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putInt("currentAnimation", this.dataTracker.get(CURRENT_ANIMATION));
        nbt.putBoolean("roarTracker", this.dataTracker.get(ROAR_TRACKER));
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.dataTracker.set(CURRENT_ANIMATION, nbt.getInt("currentAnimation"));
        this.dataTracker.set(ROAR_TRACKER, nbt.getBoolean("roarTracker"));
    }

    @Override
    public void updateEventHandler(BiConsumer<EntityGameEventHandler<?>, ServerWorld> callback) {
        if (this.world instanceof ServerWorld serverWorld) {
            callback.accept(this.slenderRoarEventHandler, serverWorld);
        }
    }

    private void travelToGunShot(Entity entity) {
        if (this.target == null) {
            this.setAngryAt(entity);
        }
    }

    public void setAngryAt(Entity newTarget) {
        if (newTarget instanceof PlayerEntity player && (player.isCreative() || player.isSpectator())) {
            return;
        }

        this.target = newTarget;
        this.changeState(State.CHASING);
        this.timeInState = 80;
        this.anger = 2;
        this.angerTimer = 200;
        this.lastSeenPos = newTarget.getPos();
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.world.isClient()) {
            double speed = Math.sqrt(Math.pow(this.getVelocity().x, 2) + Math.pow(this.getVelocity().z, 2));

            if (this.target == null) {
                this.tryToSpotTargets();
            }

            switch (this.getCurrentState()) {
                case IDLE -> {
                    this.setCurrentAnimation(this.getAnimationSet().idle());
                    this.changeState(State.WANDERING);
                    if (this.timeInState > 20 * 3) {
                        if (this.random.nextBoolean()) {
                            this.changeState(State.WANDERING);
                        }
                    }
                }
                case WANDERING -> {
                    if (speed > 0.01f) {
                        this.setCurrentAnimation(this.getAnimationSet().walk());
                    } else {
                        this.setCurrentAnimation(this.getAnimationSet().idle());
                    }
                    if (this.navigation.isIdle()) {
                        var pos = this.getPos();

                        if (this.searchCount > 0 && this.lastSeenPos != null) {
                            this.searchCount--;
                            pos = this.lastSeenPos;
                        } else {
                            PlayerEntity target = this.world.getClosestPlayer(this.getX(), this.getY(), this.getZ(), 1000, true);
                            if (target != null) {
                                pos = target.getPos();
                            }
                        }
                        Vec3d current_point = pos.add(this.random.nextInt(40) - 20, this.random.nextInt(30) - 15, this.random.nextInt(40) - 20);

                        if (this.getPos().distanceTo(current_point) <= 10) {
                            current_point = current_point.subtract(this.getPos()).normalize().multiply(10).add(this.getPos());
                        }

                        this.navigation.startMovingTo(current_point.x, current_point.y, current_point.z, 0.25f);
                    }
                    if (this.timeInState > 20 * 12) {
                        if (this.random.nextBoolean()) {
                            this.changeState(State.WANDERING);
                        } else {
                            this.navigation.startMovingTo(this.getX(), this.getY(), this.getZ(), 0);
                            this.changeState(State.LOOKING);
                        }
                    }
                }
                case LOOKING -> {
                    this.navigation.stop();
                    this.setCurrentAnimation(this.getAnimationSet().look());
                    this.setVelocity(0, this.getVelocity().y, 0);
                    if (this.timeInState > 20 * 6) {
                        this.changeState(State.WANDERING);
                    }
                }
                case CHASING -> {
                    float yaw = -(float) Math.toRadians(this.getYaw());
                    float look_x = (float) Math.sin(yaw);
                    float look_z = (float) Math.cos(yaw);
                    if (this.doorPos == null) {
                        BlockPos findDoorPos = new BlockPos(this.getX() + look_x, this.getY() + 0.5f, this.getZ() + look_z);
                        if (this.canBreakDoors() && (this.world.getBlockState(findDoorPos).getBlock() instanceof DoorBlock || this.world.getBlockState(findDoorPos).getBlock() instanceof FenceBlock)) {
                            this.doorPos = findDoorPos;
                            this.changeState(State.ATTACKING);
                        } else {
                            if (this.timeInState < 80 && this.angerTimer <= 0) {
                                this.setCurrentAnimation(this.getAnimationSet().roar());
                                if (this.target != null) {
                                    this.lookAtEntity(this.target, 180, 90);
                                }

                                if (this.timeInState == 10) {
                                    this.dataTracker.set(ROAR_TRACKER, true);
                                }
                            } else {
                                this.angerTimer = 20 * 10;
                                if (!this.isInsideWaterOrBubbleColumn()) {
                                    this.setCurrentAnimation(this.getAnimationSet().run());
                                } else {
                                    this.setCurrentAnimation(this.getAnimationSet().walk());
                                }

                                if (this.anger > 0) {
                                    if (this.target == null || !this.canSee(this.target)) {
                                        this.anger--;
                                    }

                                    if (this.target != null) {
                                        this.lastSeenPos = new Vec3d(this.target.getX(), this.target.getY(), this.target.getZ());

                                        this.navigation.startMovingTo(this.target, this.getFastMovementSpeed());

                                        if (this.target.distanceTo(this) <= 4 && this.timeInState > 20) {
                                            this.changeState(State.ATTACKING);
                                        }
                                    }
                                } else {
                                    if (this.lastSeenPos != null) {
                                        this.navigation.startMovingTo(this.lastSeenPos.x, this.lastSeenPos.y, this.lastSeenPos.z, this.getSlowMovementSpeed());
                                        this.changeState(State.CONFUSED);
                                        this.target = null;
                                    } else {
                                        this.changeState(State.WANDERING);
                                    }
                                }
                            }
                        }
                    } else {
                        if (this.timeInState > 40) {
                            this.changeState(State.ATTACKING);
                        }
                    }
                }
                case CONFUSED -> {
                    this.target = null;
                    if (this.navigation.isIdle() || this.timeInState > 20 * 3) {
                        this.changeState(State.WANDERING);
                        this.searchCount = 3;
                    }
                }
                case ATTACKING -> {
                    this.setCurrentAnimation(this.getAnimationSet().attack());
                    if (this.target != null) {
                        this.lookAtEntity(this.target, 180, 90);
                    }
                    if (this.timeInState > 20) {
                        if (this.anger > 0) {
                            this.changeState(State.CHASING);
                        } else {
                            this.changeState(State.IDLE);
                        }
                    } else if (this.timeInState == 4) {
                        if (this.canBreakDoors() && this.doorPos != null) {

                            if (this.doorHits < 2) {
                                this.world.playSound(this.getX(), this.getY(), this.getZ(), SoundEvents.ENTITY_ZOMBIE_ATTACK_WOODEN_DOOR, SoundCategory.HOSTILE, 1.0f, 1.0f + this.random.nextFloat() * 0.25f, true);

                                this.doorHits++;
                            } else {
                                this.world.playSound(this.getX(), this.getY(), this.getZ(), SoundEvents.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR, SoundCategory.HOSTILE, 1.0f, 1.0f + this.random.nextFloat() * 0.25f, true);

                                this.world.breakBlock(this.doorPos, true, this);
                                this.doorPos = null;

                                this.doorHits = 0;
                            }
                            this.setCurrentAnimation(this.getAnimationSet().run());

                            this.changeState(State.CHASING);
                        } else {
                            if (this.target != null) {
                                if (this.target.getPos().distanceTo(this.getPos().add(0, this.getDamageHeight(), 0)) <= this.getReach()) {
                                    this.target.damage(DamageSource.mob(this), this.getDamage());
                                }
                            }
                        }
                    }
                }
                default -> {
                    this.navigation.stop();
                    this.changeState(State.IDLE);
                }
            }
        } else {
            if (this.dataTracker.get(ROAR_TRACKER)) {
                this.world.playSound(this.getX(), this.getY(), this.getZ(), this.getAngrySound(), SoundCategory.HOSTILE, 2.0f, (float) (this.random.nextDouble() - 0.5f) / 5.0f + 1.0f, true);
                this.dataTracker.set(ROAR_TRACKER, false);
            }
        }

        this.timeInState++;
        this.angerTimer--;
    }

    protected void tryToSpotTargets() {
        Box box = new Box(this.getBlockPos()).expand(500);
        List<LivingEntity> targets = new ArrayList<>(this.world.getEntitiesByClass(PlayerEntity.class, box, entity -> true));
        LivingEntity closest = this.world.getClosestEntity(targets, TargetPredicate.createAttackable().setBaseMaxDistance(500), this, this.getX(), this.getY(), this.getZ());
        DetectionRadius radius = this.getDetectionRadius(closest);

        float max_anger = 2;
        if (this.anger < 20 * max_anger) {
            if (closest != null) {

                if (this.canSee(closest)) {
                    int light = Math.max(this.world.getLightLevel(LightType.BLOCK, closest.getBlockPos().up()), this.world.getLightLevel(LightType.SKY, closest.getBlockPos().up()));

                    if (closest.distanceTo(this) <= radius.extremeAngerRange()) {
                        this.anger += max_anger;
                    } else {
                        Vec3d look = new Vec3d(this.lookControl.getLookX(), this.lookControl.getLookY(), this.lookControl.getLookZ()).subtract(this.getPos()).normalize();
                        Vec3d direction = closest.getPos().subtract(this.getPos()).normalize();
                        double dot = look.dotProduct(direction);

                        if (closest.distanceTo(this) <= radius.majorAngerRange()) {
                            if (light > 7) {
                                if (dot > 0) {
                                    this.anger += max_anger;
                                } else if (closest.isSprinting()) {
                                    this.anger += max_anger * (0.8f);
                                } else {
                                    this.anger++;
                                }
                            } else {
                                if (dot > 0) {
                                    if (closest.isSprinting()) {
                                        this.anger += max_anger * (0.8f);
                                    } else if (closest.isSneaking()) {
                                        this.anger += 0.25f;
                                    } else {
                                        this.anger += 0.75f;
                                    }
                                } else if (closest.isSprinting()) {
                                    this.anger++;
                                } else if (!closest.isSneaking()) {
                                    this.anger += 0.5f;
                                }
                            }
                        } else if (closest.distanceTo(this) <= radius.minorAngerRange()) {
                            if (light > 7) {
                                if (dot > 0) {
                                    if (closest.isSprinting()) {
                                        this.anger++;
                                    } else if (!closest.isSneaking()) {
                                        this.anger += 0.25f;
                                    }
                                }
                            } else {
                                if (closest.isSprinting()) {
                                    this.anger += 0.25f;
                                }
                            }
                        }
                    }
                }
            } else {
                if (this.anger > 0) {
                    this.anger -= 0.1f;
                } else {
                    this.anger = 0;
                }
            }
        } else {
            this.target = closest;
            if (closest != null && this.getCurrentState() != State.CHASING) {
                this.lastSeenPos = this.target.getPos();

                if (this.getCurrentState() == State.IDLE || this.getCurrentState() == State.WANDERING || this.getCurrentState() == State.LOOKING) {
                    this.changeState(State.CHASING);
                }
            }
        }
    }

    protected void changeState(State newState) {
        if (this.getCurrentState() != newState) {
            this.timeInState = 0;
            this.currentState = newState;
        }
    }

    protected void setCurrentAnimation(RawAnimation animation) {
        int index = this.getAnimationSet().getAnimations().indexOf(animation);
        if (this.dataTracker.get(CURRENT_ANIMATION) != index && this.random.nextInt(3) == 2) {
            this.dataTracker.set(CURRENT_ANIMATION, index);
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar registrar) {
        registrar.add(new AnimationController<>(this, "controller", 0, (animState) -> {
            var controller = animState.getController();
            controller.setAnimation(this.getAnimationSet().getAnimations().get(this.dataTracker.get(CURRENT_ANIMATION)));
            controller.setTransitionLength(0);
            return PlayState.CONTINUE;
        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        if (this.getCurrentState() == State.CHASING || this.getCurrentState() == State.ATTACKING) {
            return this.getAngrySound();
        } else if (this.getCurrentState() == State.LOOKING) {
            return this.getLookingSound();
        }
        return super.getAmbientSound();
    }

    public State getCurrentState() {
        return this.currentState;
    }

    protected final DetectionRadius getDetectionRadius(LivingEntity closest) {
        BlockPos eyePos = new BlockPos(this.getEyePos());
        DetectionRadius radius = this.getLightDetectionRadius();

        if (closest instanceof DynamicLightSource lightSource) {
            if (this.world.getLightLevel(closest.getBlockPos()) <= 7 && lightSource.getLuminance() > 7) {
                radius = this.getLitTargetRadius();
            }
        } else if (this.world.getLightLevel(eyePos) <= 7 || (this.world.isSkyVisible(this.getBlockPos()) && this.world.isNight() && this.world.getLightLevel(eyePos) <= 9)) {
            radius = this.getDarkDetectionRadius();
        }

        return this.isWaringHead(closest) ? radius.divide(1.3) : radius;
    }

    private boolean isWaringHead(LivingEntity entity) {
        if (entity != null) {
            for (ItemStack stack : entity.getArmorItems()) {
                if (stack.isOf(BlockRegistry.SCP_SLENDERMAN_HEAD.asItem())) {
                    return true;
                }
            }
        }
        return false;
    }

    protected abstract boolean canBreakDoors();

    protected abstract float getFastMovementSpeed();

    protected abstract float getSlowMovementSpeed();

    protected abstract int getDamage();

    protected abstract int getReach();

    protected abstract int getDamageHeight();

    protected abstract DetectionRadius getLightDetectionRadius();

    protected abstract DetectionRadius getDarkDetectionRadius();

    protected abstract DetectionRadius getLitTargetRadius();

    protected abstract SoundEvent getAngrySound();

    protected abstract SoundEvent getLookingSound();

    protected abstract AnimationSet getAnimationSet();

    public enum State {
        IDLE, WANDERING, LOOKING, CHASING, CONFUSED, ATTACKING
    }

    private class SlenderRoarEventListener implements GameEventListener {

        private final PositionSource source;
        private final int range;

        public SlenderRoarEventListener(PositionSource source, int range) {
            this.source = source;
            this.range = range;
        }

        @Override
        public PositionSource getPositionSource() {
            return this.source;
        }

        @Override
        public int getRange() {
            return this.range;
        }

        @Override
        public boolean listen(ServerWorld world, GameEvent event, GameEvent.Emitter emitter, Vec3d emitterPos) {
            if (emitter.sourceEntity() != null) {
                Entity sourceEntity = emitter.sourceEntity();

                if (event == SlenderMod.GUN_SHOT) {
                    AbstractSCPSlenderEntity.this.travelToGunShot(sourceEntity);
                    return true;
                } else if (event == GameEvent.ENTITY_DIE && AbstractSCPSlenderEntity.this.target != null) {
                    if (sourceEntity.getUuid() == AbstractSCPSlenderEntity.this.target.getUuid()) {
                        AbstractSCPSlenderEntity.this.tryToSpotTargets();
                        return true;
                    }
                }
            }
            return false;
        }
    }

    protected record DetectionRadius(double minorAngerRange, double majorAngerRange, double extremeAngerRange) {

        public DetectionRadius divide(double num) {
            return new DetectionRadius(this.minorAngerRange / num, this.majorAngerRange / num, this.extremeAngerRange / num);
        }
    }

    protected static class AnimationSet {

        private final ImmutableList<RawAnimation> animations;
        private final RawAnimation idle;
        private final RawAnimation walk;
        private final RawAnimation roar;
        private final RawAnimation run;
        private final RawAnimation look;
        private final RawAnimation attack;

        public AnimationSet(RawAnimation idle, RawAnimation walk, RawAnimation roar, RawAnimation run, RawAnimation look, RawAnimation attack) {
            this.animations = ImmutableList.of(idle, walk, roar, run, look, attack);
            this.idle = idle;
            this.walk = walk;
            this.roar = roar;
            this.run = run;
            this.look = look;
            this.attack = attack;
        }

        private RawAnimation idle() {
            return this.idle;
        }

        private RawAnimation walk() {
            return this.walk;
        }

        private RawAnimation roar() {
            return this.roar;
        }

        private RawAnimation run() {
            return this.run;
        }

        private RawAnimation look() {
            return this.look;
        }

        private RawAnimation attack() {
            return this.attack;
        }

        public ImmutableList<RawAnimation> getAnimations() {
            return this.animations;
        }
    }
}
