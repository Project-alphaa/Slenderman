package kelvin.slendermod.registry;

import kelvin.slendermod.client.screen.handler.SafeScreenHandler;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.HopperScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;

public class ScreenHandlerRegistry {

    public static final ScreenHandlerType<HopperScreenHandler> TRASH_BIN_SCREEN_HANDLER = register("trash_bin", HopperScreenHandler::new);
    public static final ScreenHandlerType<SafeScreenHandler> SAFE_SCREEN_HANDLER = register("safe", SafeScreenHandler::new);

    private static <T extends ScreenHandler> ScreenHandlerType<T> register(String name, ScreenHandlerType.Factory<T> factory) {
        return Registry.register(Registries.SCREEN_HANDLER, name, new ScreenHandlerType<>(factory));
    }
}
