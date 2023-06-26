package kelvin.slendermod.mixin;

import com.mojang.authlib.GameProfile;
import kelvin.slendermod.registry.ItemRegistry;
import kelvin.slendermod.util.IForceCrawlingPlayer;
import net.minecraft.entity.player.PlayerEntity;
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
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity {

    @Shadow
    public ServerPlayNetworkHandler networkHandler;

    public ServerPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
        super(world, pos, yaw, gameProfile);
    }

    @Override
    public boolean isCrawling() {
        return super.isCrawling() || ((IForceCrawlingPlayer) this).isForcedCrawling();
    }

    @Inject(at = @At("HEAD"), method = "useBook")
    public void useBook(ItemStack book, Hand hand, CallbackInfo info) {
        if (book.isOf(ItemRegistry.SLENDER_GRIMOIRE) || book.isOf(ItemRegistry.NOTE) || book.isOf(ItemRegistry.WRITABLE_NOTE)) {
            currentScreenHandler.sendContentUpdates();
            networkHandler.sendPacket(new OpenWrittenBookS2CPacket(hand));
        }
    }
}
