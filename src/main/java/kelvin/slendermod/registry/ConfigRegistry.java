package kelvin.slendermod.registry;

import dev.isxander.yacl.api.ConfigCategory;
import dev.isxander.yacl.api.Option;
import dev.isxander.yacl.api.YetAnotherConfigLib;
import dev.isxander.yacl.config.ConfigEntry;
import dev.isxander.yacl.config.GsonConfigInstance;
import dev.isxander.yacl.gui.controllers.BooleanController;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class ConfigRegistry {

    public static final GsonConfigInstance<ConfigRegistry> INSTANCE = new GsonConfigInstance<>(ConfigRegistry.class, FabricLoader.getInstance().getConfigDir().resolve("slendermod.json"));

    @ConfigEntry
    public boolean enableSlenderEffects = true;

    public static Screen createScreen(Screen prevScreen) {
        return YetAnotherConfigLib.create(INSTANCE, (defaults, config, builder) -> builder.title(Text.translatable("config.slendermod.title")).category(ConfigCategory.createBuilder().name(Text.translatable("config.category.slendermod.main.name")).option(Option.createBuilder(Boolean.class).name(Text.translatable("config.option.slendermod.enable_slender_effects.name")).tooltip(Text.translatable("config.option.slendermod.enable_slender_effects.tooltip")).binding(defaults.enableSlenderEffects, () -> config.enableSlenderEffects, value -> config.enableSlenderEffects = value).controller(BooleanController::new).build()).build())).generateScreen(prevScreen);
    }
}
