package kelvin.slendermod.item;

import kelvin.slendermod.registry.ItemRegistry;
import kelvin.slendermod.registry.SoundRegistry;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class ItemWalkman extends Item {

    public ItemWalkman() {
        super(new FabricItemSettings());
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (user.isCreative() || hasCassetteTape(user)) {
            user.getItemCooldownManager().set(this, 150);
            user.playSound(SoundRegistry.TAPE, SoundCategory.PLAYERS, 0.5F, 1);
            return TypedActionResult.success(stack);
        }
        return super.use(world, user, hand);
    }

    private boolean hasCassetteTape(PlayerEntity player) {
        Inventory inventory = player.getInventory();
        for (int i = 0; i < inventory.size(); i++) {
            ItemStack stack = inventory.getStack(i);
            if (stack.isOf(ItemRegistry.CASSETTE_TAPE)) {
                return true;
            }
        }
        return false;
    }
}
