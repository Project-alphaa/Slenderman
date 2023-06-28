package kelvin.slendermod.mixin;

import com.ultreon.mods.pixelguns.network.packet.c2s.play.GunShootC2SPacket;
import kelvin.slendermod.SlenderMod;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GunShootC2SPacket.class)
public class GunShootC2SPacketMixin {

    @Inject(method = "receive", at = @At("HEAD"))
    private void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender, CallbackInfo ci) {
        ServerWorld world = player.getWorld();
        server.execute(() -> world.emitGameEvent(player, SlenderMod.GUN_SHOT, player.getPos()));
    }
}
