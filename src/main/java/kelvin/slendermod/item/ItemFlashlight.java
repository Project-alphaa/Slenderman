package kelvin.slendermod.item;

import kelvin.slendermod.registry.SoundRegistry;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemFlashlight extends Item {

    public ItemFlashlight(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        user.playSound(SoundRegistry.FLASHLIGHT_SWITCH, 1, 1);
        if (!world.isClient()) {
            ItemStack heldStack = user.getStackInHand(hand);
            if (heldStack.getItem() instanceof ItemFlashlight) {
                boolean powered = false;
                NbtCompound nbt = heldStack.getOrCreateSubNbt("Flashlight");
                if (nbt.contains("Powered")) {
                    powered = nbt.getBoolean("Powered");
                }
                nbt.putBoolean("Powered", !powered);
            }
            return TypedActionResult.success(user.getStackInHand(hand));
        }
        return TypedActionResult.consume(user.getStackInHand(hand));
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (isFlashlightPowered(stack)) {
            tooltip.add(Text.translatable(this.getTranslationKey() + ".powered_on").formatted(Formatting.GRAY));
        } else {
            tooltip.add(Text.translatable(this.getTranslationKey() + ".powered_off").formatted(Formatting.GRAY));
        }
        super.appendTooltip(stack, world, tooltip, context);
    }

    public static boolean isFlashlightPowered(ItemStack stack) {
        if (stack.getItem() instanceof ItemFlashlight) {
            boolean powered = false;
            NbtCompound nbt = stack.getOrCreateSubNbt("Flashlight");
            if (nbt.contains("Powered")) {
                powered = nbt.getBoolean("Powered");
            }
            return powered;
        }
        return false;
    }
}
