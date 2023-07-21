package kelvin.slendermod.item;

import kelvin.slendermod.SlenderMod;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

public class MedicalKitItem extends Item {

    public MedicalKitItem(Settings settings) {
        super(settings);
    }


    //Not sure what this needs to be
    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 40;
    }

    @Override
    public boolean isUsedOnRelease(ItemStack stack) {
        return true;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {


        ItemStack stack = player.getStackInHand(hand);


        //You can't take a med kit when max HP (subject to Alpha's design)
        if (!player.isCreative() &&  player.getHealth() >= player.getMaxHealth()) {
            return TypedActionResult.fail(stack);
        }


        player.setCurrentHand(hand);





        if (!player.isCreative()) player.getItemCooldownManager().set(stack.getItem(), 40);

        return TypedActionResult.consume(stack);
//        return TypedActionResult.success(stack);
    }


    //Don't set a cooldown if they did not use it.
    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if( remainingUseTicks > 0 && user instanceof PlayerEntity player && !player.isCreative()) {
            player.getItemCooldownManager().set(stack.getItem(), 0);

        }
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {

        if (remainingUseTicks <=0 && user instanceof PlayerEntity player) {

            player.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 300, 1));

            if (!player.isCreative()) {
                player.getItemCooldownManager().set(stack.getItem(), 15);
                stack.decrement(1);
            }

        }

    }
}