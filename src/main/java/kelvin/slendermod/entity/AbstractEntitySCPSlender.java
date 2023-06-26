package kelvin.slendermod.entity;

import com.google.common.collect.ImmutableList;
import com.theendercore.npctrader.entity.TraderEntity;
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

public abstract class AbstractEntitySCPSlender extends PathAwareEntity implements GeoEntity {

    private static final TrackedData<Integer> CURRENT_ANIMATION = DataTracker.registerData(AbstractEntitySCPSlender.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Boolean> ROAR_TRACKER = DataTracker.registerData(AbstractEntitySCPSlender.class, TrackedDataHandlerRegistry.BOOLEAN);

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

    protected AbstractEntitySCPSlender(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
        PositionSource source = new EntityPositionSource(this, getStandingEyeHeight());
        slenderRoarEventHandler = new EntityGameEventHandler<>(new SlenderRoarEventListener(source, SlenderMod.GUN_SHOT.getRange()));
    }

    @Override
    protected EntityNavigation createNavigation(World world) {
        return new MMPathNavigateGround(this, world);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        dataTracker.startTracking(CURRENT_ANIMATION, 0);
        dataTracker.startTracking(ROAR_TRACKER, false);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putInt("currentAnimation", dataTracker.get(CURRENT_ANIMATION));
        nbt.putBoolean("roarTracker", dataTracker.get(ROAR_TRACKER));
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        dataTracker.set(CURRENT_ANIMATION, nbt.getInt("currentAnimation"));
        dataTracker.set(ROAR_TRACKER, nbt.getBoolean("roarTracker"));
    }

    @Override
    public void updateEventHandler(BiConsumer<EntityGameEventHandler<?>, ServerWorld> callback) {
        if (world instanceof ServerWorld serverWorld) {
            callback.accept(slenderRoarEventHandler, serverWorld);
        }
    }

    private void travelToGunShot(Entity entity) {
        if (target == null) {
            setAngryAt(entity);
        }
    }

    public void setAngryAt(Entity newTarget) {
        if (newTarget instanceof PlayerEntity player && (player.isCreative() || player.isSpectator())) {
            return;
        }

        target = newTarget;
        changeState(State.CHASING);
        timeInState = 80;
        anger = 2;
        angerTimer = 200;
        lastSeenPos = newTarget.getPos();
    }

    @Override
    public void tick() {
        super.tick();
        if (!world.isClient()) {
            double speed = Math.sqrt(Math.pow(getVelocity().x, 2) + Math.pow(getVelocity().z, 2));

            if (target == null) {
                tryToSpotTargets();
            }

            switch (getCurrentState()) {
                case IDLE -> {
                    setCurrentAnimation(getAnimationSet().idle());
                    changeState(State.WANDERING);
                    if (timeInState > 20 * 3) {
                        if (random.nextBoolean()) {
                            changeState(State.WANDERING);
                        }
                    }
                }
                case WANDERING -> {
                    if (speed > 0.01f) {
                        setCurrentAnimation(getAnimationSet().walk());
                    }
                    else {
                        setCurrentAnimation(getAnimationSet().idle());
                    }
                    if (navigation.isIdle()) {
                        var pos = getPos();

                        if (searchCount > 0 && lastSeenPos != null) {
                            searchCount--;
                            pos = lastSeenPos;
                        }
                        else {
                            PlayerEntity target = world.getClosestPlayer(getX(), getY(), getZ(), 1000, true);
                            if (target != null) {
                                pos = target.getPos();
                            }
                        }
                        Vec3d current_point = pos.add(random.nextInt(40) - 20, random.nextInt(30) - 15, random.nextInt(40) - 20);

                        if (getPos().distanceTo(current_point) <= 10) {
                            current_point = current_point.subtract(getPos()).normalize().multiply(10).add(getPos());
                        }

                        navigation.startMovingTo(current_point.x, current_point.y, current_point.z, 0.25f);
                    }
                    if (timeInState > 20 * 12) {
                        if (random.nextBoolean()) {
                            changeState(State.WANDERING);
                        }
                        else {
                            navigation.startMovingTo(getX(), getY(), getZ(), 0);
                            changeState(State.LOOKING);
                        }
                    }
                }
                case LOOKING -> {
                    navigation.stop();
                    setCurrentAnimation(getAnimationSet().look());
                    setVelocity(0, getVelocity().y, 0);
                    if (timeInState > 20 * 6) {
                        changeState(State.WANDERING);
                    }
                }
                case CHASING -> {
                    float yaw = -(float) Math.toRadians(getYaw());
                    float look_x = (float) Math.sin(yaw);
                    float look_z = (float) Math.cos(yaw);
                    if (doorPos == null) {
                        BlockPos findDoorPos = new BlockPos(getX() + look_x, getY() + 0.5f, getZ() + look_z);
                        if (canBreakDoors() && (world.getBlockState(findDoorPos).getBlock() instanceof DoorBlock || world.getBlockState(findDoorPos).getBlock() instanceof FenceBlock)) {
                            doorPos = findDoorPos;
                            changeState(State.ATTACKING);
                        }
                        else {
                            if (timeInState < 80 && angerTimer <= 0) {
                                setCurrentAnimation(getAnimationSet().roar());
                                if (target != null) {
                                    lookAtEntity(target, 180, 90);
                                }

                                if (timeInState == 10) {
                                    dataTracker.set(ROAR_TRACKER, true);
                                }
                            }
                            else {
                                angerTimer = 20 * 10;
                                if (!isInsideWaterOrBubbleColumn()) {
                                    setCurrentAnimation(getAnimationSet().run());
                                }
                                else {
                                    setCurrentAnimation(getAnimationSet().walk());
                                }

                                if (anger > 0) {
                                    if (target == null || !canSee(target)) {
                                        anger--;
                                    }

                                    if (target != null) {
                                        lastSeenPos = new Vec3d(target.getX(), target.getY(), target.getZ());

                                        navigation.startMovingTo(target, getFastMovementSpeed());

                                        if (target.distanceTo(this) <= 4 && timeInState > 20) {
                                            changeState(State.ATTACKING);
                                        }
                                    }
                                }
                                else {
                                    if (lastSeenPos != null) {
                                        navigation.startMovingTo(lastSeenPos.x, lastSeenPos.y, lastSeenPos.z, getSlowMovementSpeed());
                                        changeState(State.CONFUSED);
                                        target = null;
                                    }
                                    else {
                                        changeState(State.WANDERING);
                                    }
                                }
                            }
                        }
                    }
                    else {
                        if (timeInState > 40) {
                            changeState(State.ATTACKING);
                        }
                    }
                }
                case CONFUSED -> {
                    target = null;
                    if (navigation.isIdle() || timeInState > 20 * 3) {
                        changeState(State.WANDERING);
                        searchCount = 3;
                    }
                }
                case ATTACKING -> {
                    setCurrentAnimation(getAnimationSet().attack());
                    if (target != null) {
                        lookAtEntity(target, 180, 90);
                    }
                    if (timeInState > 20) {
                        if (anger > 0) {
                            changeState(State.CHASING);
                        }
                        else {
                            changeState(State.IDLE);
                        }
                    }
                    else if (timeInState == 4) {
                        if (canBreakDoors() && doorPos != null) {

                            if (doorHits < 2) {
                                world.playSound(getX(), getY(), getZ(), SoundEvents.ENTITY_ZOMBIE_ATTACK_WOODEN_DOOR, SoundCategory.HOSTILE, 1.0f, 1.0f + random.nextFloat() * 0.25f, true);

                                doorHits++;
                            }
                            else {
                                world.playSound(getX(), getY(), getZ(), SoundEvents.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR, SoundCategory.HOSTILE, 1.0f, 1.0f + random.nextFloat() * 0.25f, true);

                                world.breakBlock(doorPos, true, this);
                                doorPos = null;

                                doorHits = 0;
                            }
                            setCurrentAnimation(getAnimationSet().run());

                            changeState(State.CHASING);
                        }
                        else {
                            if (target != null) {
                                if (target.getPos().distanceTo(getPos().add(0, getDamageHeight(), 0)) <= getReach()) {
                                    target.damage(DamageSource.mob(this), getDamage());
                                }
                            }
                        }
                    }
                }
                default -> {
                    navigation.stop();
                    changeState(State.IDLE);
                }
            }
        }
        else {
            if (dataTracker.get(ROAR_TRACKER)) {
                world.playSound(getX(), getY(), getZ(), getAngrySound(), SoundCategory.HOSTILE, 2.0f, (float) (random.nextDouble() - 0.5f) / 5.0f + 1.0f, true);
                dataTracker.set(ROAR_TRACKER, false);
            }
        }

        timeInState++;
        angerTimer--;
    }

    protected void tryToSpotTargets() {
        Box box = new Box(getBlockPos()).expand(500);
        List<LivingEntity> targets = new ArrayList<>();
        targets.addAll(world.getEntitiesByClass(PlayerEntity.class, box, entity -> true));
        targets.addAll(world.getEntitiesByClass(TraderEntity.class, box, entity -> true));
        LivingEntity closest = world.getClosestEntity(targets, TargetPredicate.createAttackable().setBaseMaxDistance(500), this, getX(), getY(), getZ());
        DetectionRadius radius = getDetectionRadius(closest);

        float max_anger = 2;
        if (anger < 20 * max_anger) {
            if (closest != null) {

                if (canSee(closest)) {
                    int light = Math.max(world.getLightLevel(LightType.BLOCK, closest.getBlockPos().up()), world.getLightLevel(LightType.SKY, closest.getBlockPos().up()));

                    if (closest.distanceTo(this) <= radius.extremeAngerRange()) {
                        anger += max_anger;
                    }
                    else {
                        Vec3d look = new Vec3d(lookControl.getLookX(), lookControl.getLookY(), lookControl.getLookZ()).subtract(getPos()).normalize();
                        Vec3d direction = closest.getPos().subtract(getPos()).normalize();
                        double dot = look.dotProduct(direction);

                        if (closest.distanceTo(this) <= radius.majorAngerRange()) {
                            if (light > 7) {
                                if (dot > 0) {
                                    anger += max_anger;
                                }
                                else if (closest.isSprinting()) {
                                    anger += max_anger * (0.8f);
                                }
                                else {
                                    anger++;
                                }
                            }
                            else {
                                if (dot > 0) {
                                    if (closest.isSprinting()) {
                                        anger += max_anger * (0.8f);
                                    }
                                    else if (closest.isSneaking()) {
                                        anger += 0.25f;
                                    }
                                    else {
                                        anger += 0.75f;
                                    }
                                }
                                else if (closest.isSprinting()) {
                                    anger++;
                                }
                                else if (!closest.isSneaking()) {
                                    anger += 0.5f;
                                }
                            }
                        }
                        else if (closest.distanceTo(this) <= radius.minorAngerRange()) {
                            if (light > 7) {
                                if (dot > 0) {
                                    if (closest.isSprinting()) {
                                        anger++;
                                    }
                                    else if (!closest.isSneaking()) {
                                        anger += 0.25f;
                                    }
                                }
                            }
                            else {
                                if (closest.isSprinting()) {
                                    anger += 0.25f;
                                }
                            }
                        }
                    }
                }
            }
            else {
                if (anger > 0) {
                    anger -= 0.1f;
                }
                else {
                    anger = 0;
                }
            }
        }
        else {
            target = closest;
            if (closest != null && getCurrentState() != State.CHASING) {
                lastSeenPos = target.getPos();

                if (getCurrentState() == State.IDLE || getCurrentState() == State.WANDERING || getCurrentState() == State.LOOKING) {
                    changeState(State.CHASING);
                }
            }
        }
    }

    protected void changeState(State newState) {
        if (getCurrentState() != newState) {
            timeInState = 0;
            currentState = newState;
        }
    }

    protected void setCurrentAnimation(RawAnimation animation) {
        int index = getAnimationSet().getAnimations().indexOf(animation);
        if (dataTracker.get(CURRENT_ANIMATION) != index && random.nextInt(3) == 2) {
            dataTracker.set(CURRENT_ANIMATION, index);
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar registrar) {
        registrar.add(new AnimationController<>(this, "controller", 0, (animState) -> {
            var controller = animState.getController();
            controller.setAnimation(getAnimationSet().getAnimations().get(dataTracker.get(CURRENT_ANIMATION)));
            controller.setTransitionLength(0);
            return PlayState.CONTINUE;
        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        if (getCurrentState() == State.CHASING || getCurrentState() == State.ATTACKING) {
            return getAngrySound();
        }
        else if (getCurrentState() == State.LOOKING) {
            return getLookingSound();
        }
        return super.getAmbientSound();
    }

    public State getCurrentState() {
        return currentState;
    }

    protected final DetectionRadius getDetectionRadius(LivingEntity closest) {
        BlockPos eyePos = new BlockPos(getEyePos());
        DetectionRadius radius = getLightDetectionRadius();;

        if (closest instanceof DynamicLightSource lightSource) {
            if (world.getLightLevel(closest.getBlockPos()) <= 7 && lightSource.getLuminance() > 7) {
                radius = getLitTargetRadius();
            }
        }
        else if (world.getLightLevel(eyePos) <= 7 || (world.isSkyVisible(getBlockPos()) && world.isNight() && world.getLightLevel(eyePos) <= 9)) {
            radius = getDarkDetectionRadius();
        }

        return isWaringHead(closest) ? radius.divide(1.3) : radius;
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
        IDLE,
        WANDERING,
        LOOKING,
        CHASING,
        CONFUSED,
        ATTACKING;
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
            return source;
        }

        @Override
        public int getRange() {
            return range;
        }

        @Override
        public boolean listen(ServerWorld world, GameEvent event, GameEvent.Emitter emitter, Vec3d emitterPos) {
            if (emitter.sourceEntity() != null) {
                Entity sourceEntity = emitter.sourceEntity();

                if (event == SlenderMod.GUN_SHOT) {
                    AbstractEntitySCPSlender.this.travelToGunShot(sourceEntity);
                    return true;
                }
                else if (event == GameEvent.ENTITY_DIE && AbstractEntitySCPSlender.this.target != null) {
                    if (sourceEntity.getUuid() == target.getUuid()) {
                        AbstractEntitySCPSlender.this.tryToSpotTargets();
                        return true;
                    }
                }
            }
            return false;
        }
    }

    protected record DetectionRadius(double minorAngerRange, double majorAngerRange, double extremeAngerRange) {

        public DetectionRadius divide(double num) {
            return new DetectionRadius(minorAngerRange / num, majorAngerRange / num, extremeAngerRange / num);
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
            animations = ImmutableList.of(idle, walk, roar, run, look, attack);
            this.idle = idle;
            this.walk = walk;
            this.roar = roar;
            this.run = run;
            this.look = look;
            this.attack = attack;
        }

        private RawAnimation idle() {
            return idle;
        }

        private RawAnimation walk() {
            return walk;
        }

        private RawAnimation roar() {
            return roar;
        }

        private RawAnimation run() {
            return run;
        }

        private RawAnimation look() {
            return look;
        }

        private RawAnimation attack() {
            return attack;
        }

        public ImmutableList<RawAnimation> getAnimations() {
            return animations;
        }
    }
}
