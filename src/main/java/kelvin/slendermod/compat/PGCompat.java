package kelvin.slendermod.compat;

import com.ultreon.mods.pixelguns.event.GunEvents;
import kelvin.slendermod.entity.AbstractSCPSlenderEntity;
import kelvin.slendermod.network.server.ServerPacketHandler;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.hit.EntityHitResult;

public class PGCompat {

    public static void onGunshotHit() {
        GunEvents.GUN_HIT.registerListener((result, serverWorld, serverPlayer) -> {
            if (result instanceof EntityHitResult hitResult) {
                if (hitResult.getEntity() instanceof AbstractSCPSlenderEntity slender) {
                    PacketByteBuf buf = PacketByteBufs.create();
                    buf.writeUuid(slender.getUuid());
                    buf.writeBlockPos(slender.getBlockPos());
                    ClientPlayNetworking.send(ServerPacketHandler.SLENDER_SHOT_ID, buf);
                }
            }
        });
    }
}
