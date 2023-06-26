package kelvin.slendermod.mixin;

import kelvin.slendermod.registry.ItemRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerMixin {

    @Shadow
    public ServerPlayerEntity player;
    private boolean isNote;

    @Redirect(method = {"addBook", "updateBookContent"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"))
    private boolean book(ItemStack instance, Item item) {
        if (instance.isOf(ItemRegistry.WRITABLE_NOTE)) {
            isNote = true;
            return true;
        }
        return instance.isOf(Items.WRITABLE_BOOK);
    }

    @Redirect(method = "addBook", at = @At(value = "NEW", target = "(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/item/ItemStack;"))
    private ItemStack addBook(ItemConvertible item) {
        if (isNote) {
            return new ItemStack(ItemRegistry.NOTE);
        }
        else {
            return new ItemStack(item);
        }
    }
}
