package kelvin.slendermod.item;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class ItemMedicalKit extends Item {

    public ItemMedicalKit(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        user.getItemCooldownManager().set(stack.getItem(), 15);
        user.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 300, 1));
        if (!user.isCreative()) {
            stack.decrement(1);
        }
        return TypedActionResult.success(stack);
    }
}
