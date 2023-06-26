package kelvin.slendermod.network.server;

import kelvin.slendermod.SlenderMod;
import kelvin.slendermod.entity.AbstractEntitySCPSlender;
import kelvin.slendermod.util.IForceCrawlingPlayer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;

import java.util.UUID;

public class ServerPacketHandler {

    public static final Identifier TOGGLED_FORCED_CRAWLING_ID = SlenderMod.id("toggled_forced_crawling");
    public static final Identifier SLENDER_SHOT_ID = SlenderMod.id("slender_shot");

    public static void start() {
        ServerPlayNetworking.registerGlobalReceiver(TOGGLED_FORCED_CRAWLING_ID, (server, player, handler, buf, responseSender) ->
                server.execute(() -> ((IForceCrawlingPlayer)player).toggleForcedCrawling()));

        ServerPlayNetworking.registerGlobalReceiver(SLENDER_SHOT_ID, (server, player, handler, buf, responseSender) -> {
            ServerWorld world = player.getWorld();
            UUID uuid = buf.readUuid();
            Entity entity = world.getEntity(uuid);
            server.execute(() -> {
                if (entity instanceof AbstractEntitySCPSlender slender) {
                    slender.setAngryAt(player);
                }
            });
        });
    }
}
