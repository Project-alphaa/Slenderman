package kelvin.slendermod.item.medkit;

import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;

public class MedicalKitItem extends BaseMedicalKitItem {

    private static final int COOLDOWN = 100;
    private static final int USE_TIME = 60;


    public MedicalKitItem(Settings settings) {
        super(settings);
    }



    @Override
    public int getRequiredUseTime() {
        return USE_TIME;
    }

    @Override
    public boolean canUse(World world, Entity entity, ItemStack stack) {
        //You can't take a med kit when max HP (subject to Alpha's design)
        return (entity instanceof ServerPlayerEntity player)
                && (player.isCreative() || player.getHealth() < player.getMaxHealth());
    }

    @Override
    public void onStartUse(World world, Entity entity, ItemStack stack) {

        if ((entity instanceof PlayerEntity player)) {
            super.animUse(world, player, stack);
        }
    }

    @Override
    public void onCompleteUse(World world, Entity user, ItemStack stack) {

        if (user instanceof PlayerEntity player) {
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.INSTANT_HEALTH, 1, 0));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 150, 1));
            if (!player.isCreative()) {
                player.getItemCooldownManager().set(stack.getItem(), COOLDOWN);
                stack.decrement(1);
            }

            super.animStopUse(world,player,stack);
        }
    }

    @Override
    public void onCancelUse(World world, Entity user, ItemStack stack) {

        if (user instanceof PlayerEntity player) {
            super.animStopUse(world,player, stack);
        }
    }
}
