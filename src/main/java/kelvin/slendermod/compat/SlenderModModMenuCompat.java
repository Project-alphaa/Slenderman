package kelvin.slendermod.compat;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import kelvin.slendermod.registry.ConfigRegistry;

public class SlenderModModMenuCompat implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return ConfigRegistry::createScreen;
    }
}
