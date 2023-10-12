package kelvin.slendermod.mixin;

import com.mojang.authlib.GameProfile;
import kelvin.slendermod.registry.ItemRegistry;
import kelvin.slendermod.util.IForceCrawlingPlayer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.OpenWrittenBookS2CPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity {

    public ServerPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
        super(world, pos, yaw, gameProfile);
    }

    @Override
    public boolean isCrawling() {
        return super.isCrawling() || ((IForceCrawlingPlayer) this).isForcedCrawling();
    }

    @Redirect(method = "useBook", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"))
    public boolean useBook(ItemStack instance, Item item) {
        if (instance.isOf(ItemRegistry.SLENDER_GRIMOIRE) || instance.isOf(ItemRegistry.NOTE)) {
            return true;
        }
        return instance.isOf(item);
    }
}
