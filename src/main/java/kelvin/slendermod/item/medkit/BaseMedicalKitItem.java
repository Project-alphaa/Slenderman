package kelvin.slendermod.item.medkit;

import kelvin.slendermod.SlenderMod;
import kelvin.slendermod.registry.SoundRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.network.packet.s2c.play.StopSoundS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class BaseMedicalKitItem extends Item implements GeoItem {

    private static final RawAnimation USE_ANIM = RawAnimation.begin().thenPlay("animation.medkit.use_heal");
    private static final RawAnimation STOP_USE = RawAnimation.begin().thenPlay("animation.medkit.stop");

    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    private final Supplier<Object> renderProvider = GeoItem.makeRenderer(this);


    public BaseMedicalKitItem(Settings settings) {
        super(settings);


        SingletonGeoAnimatable.registerSyncedAnimatable(this);

    }


    //API for starting and stopping animation

    public void animUse(World world, PlayerEntity player, Hand hand) {
        if (world instanceof ServerWorld serverWorld) {
            triggerAnim(player, GeoItem.getOrAssignId(player.getStackInHand(hand), serverWorld),
                    "controller", "animation.medkit.use_heal");
        }
        if (player instanceof ServerPlayerEntity serverPlayerEntity) {
            this.startMedSound(serverPlayerEntity);
        }

    }


    public void animStopUse(World world, PlayerEntity player, Hand hand) {
        if (world instanceof ServerWorld serverWorld) {

            triggerAnim(player, GeoItem.getOrAssignId(player.getStackInHand(hand), serverWorld),
                    "controller", "animation.medkit.stop");


            if (player instanceof ServerPlayerEntity serverPlayerEntity) {
                this.stopMedSound(serverPlayerEntity);
            }
        }
    }



    //END of API





    @Override
    public void createRenderer(Consumer<Object> consumer) {
        consumer.accept(new MedicalKitRenderProvider());
    }




    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
//        controllerRegistrar.add(new AnimationController<>(this,"idle_controller", 0,this::predicate));

        controllerRegistrar.add(new AnimationController<>(this, "controller", 0, BaseMedicalKitItem::predicate)
                .triggerableAnim("animation.medkit.use_heal", USE_ANIM)
                .triggerableAnim("animation.medkit.stop",STOP_USE)
        );
    }





    private static <T extends GeoAnimatable> PlayState predicate(AnimationState<T> tAnimationState) {

        //Cancel and return: PlayState.STOP

        if (tAnimationState.getController().getCurrentRawAnimation() !=null
                && tAnimationState.getController().getCurrentRawAnimation().equals(STOP_USE)) {
            tAnimationState.getController().stop();
//            return PlayState.STOP;
        }

        tAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.medkit.idle", Animation.LoopType.LOOP));
        return PlayState.CONTINUE;
    }




    @Override
    public Supplier<Object> getRenderProvider() {
        return renderProvider;
    }


    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }




    public void stopMedSound(Collection<ServerPlayerEntity> targets) {
        StopSoundS2CPacket stopSoundS2CPacket = new StopSoundS2CPacket(SoundRegistry.MEDICAL_KIT_USE.getId(), SoundCategory.MASTER);
        for (ServerPlayerEntity serverPlayerEntity : targets) {
            serverPlayerEntity.networkHandler.sendPacket(stopSoundS2CPacket);
        }
    }

    public void stopMedSound(ServerPlayerEntity player) {
        StopSoundS2CPacket stopSoundS2CPacket = new StopSoundS2CPacket(SoundRegistry.MEDICAL_KIT_USE.getId(), SoundCategory.MASTER);
        player.networkHandler.sendPacket(stopSoundS2CPacket);
    }


    public void startMedSound(Collection<ServerPlayerEntity> targets) {
        for (ServerPlayerEntity serverPlayerEntity : targets) {
            serverPlayerEntity.playSound(SoundRegistry.MEDICAL_KIT_USE, SoundCategory.MASTER,1,1);
        }
    }

    public void startMedSound(ServerPlayerEntity player) {
        player.playSound(SoundRegistry.MEDICAL_KIT_USE, SoundCategory.MASTER,1,1);
    }

}
