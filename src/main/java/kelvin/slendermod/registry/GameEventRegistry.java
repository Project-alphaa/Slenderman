package kelvin.slendermod.registry;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.world.event.GameEvent;

public class GameEventRegistry {
    public static final GameEvent WALKMAN_PLAY = register("walkman_play", 10);
    public static final GameEvent WALKMAN_STOP_PLAY = register("walkman_stop_play", 10);

    public static void register() {
    }
    private static GameEvent register(String id, int range) {
        return (GameEvent) Registry.register(Registries.GAME_EVENT, id, new GameEvent(id, range));
    }
}
