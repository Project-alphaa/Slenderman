package kelvin.slendermod.registry;

import dev.isxander.yacl.api.ConfigCategory;
import dev.isxander.yacl.api.Option;
import dev.isxander.yacl.api.YetAnotherConfigLib;
import dev.isxander.yacl.config.ConfigEntry;
import dev.isxander.yacl.config.GsonConfigInstance;
import dev.isxander.yacl.gui.controllers.BooleanController;
import dev.isxander.yacl.gui.controllers.slider.FloatSliderController;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class ConfigRegistry {

    public static final GsonConfigInstance<ConfigRegistry> INSTANCE = new GsonConfigInstance<>(ConfigRegistry.class, FabricLoader.getInstance().getConfigDir().resolve("slendermod.json"));

    @ConfigEntry
    public boolean enableSlenderEffects = true;

    @ConfigEntry
    public float staticIntensity = 1.0F;

    @ConfigEntry
    public float staticSpeed = 1.0F;

    public static Screen createScreen(Screen prevScreen) {
        return YetAnotherConfigLib.create(INSTANCE, (defaults, config, builder) ->
                        builder.title(Text.translatable("config.slendermod.title"))
                                .category(ConfigCategory.createBuilder().name(Text.translatable("config.category.slendermod.main.name"))
                                        .option(Option.createBuilder(Boolean.class).name(Text.translatable("config.option.slendermod.enable_slender_effects.name"))
                                                .tooltip(Text.translatable("config.option.slendermod.enable_slender_effects.tooltip"))
                                                .binding(defaults.enableSlenderEffects, () -> config.enableSlenderEffects, value -> config.enableSlenderEffects = value)
                                                .controller(BooleanController::new)
                                                .build()
                                        )
                                        .option(Option.createBuilder(Float.class).name(Text.translatable("config.option.slendermod.slender_effect_intensity.name"))
                                                .tooltip(Text.translatable("config.option.slendermod.slender_effect_intensity.tooltip"))
                                                .binding(defaults.staticIntensity, () -> config.staticIntensity, value -> config.staticIntensity = value)
                                                .controller(floatOption ->
                                                        new FloatSliderController(floatOption, 0, 1, 0.1F))
                                                .build()
                                        )
                                        .option(Option.createBuilder(Float.class).name(Text.translatable("config.option.slendermod.slender_effect_speed.name"))
                                                .tooltip(Text.translatable("config.option.slendermod.slender_effect_speed.tooltip"))
                                                .binding(defaults.staticSpeed, () -> config.staticSpeed, value -> config.staticSpeed = value)
                                                .controller(floatOption ->
                                                        new FloatSliderController(floatOption, 0, 1, 0.1F))
                                                .build()
                                        ).build()
                                )
                ).generateScreen(prevScreen);
    }
}
