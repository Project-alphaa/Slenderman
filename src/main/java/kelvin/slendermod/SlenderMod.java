package kelvin.slendermod;

import kelvin.slendermod.compat.PGCompat;
import kelvin.slendermod.item.ItemSlenderGrimoire;
import kelvin.slendermod.network.server.ServerPacketHandler;
import kelvin.slendermod.registry.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.world.event.GameEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.bernie.geckolib.GeckoLib;

@SuppressWarnings("unused")
public class SlenderMod implements ModInitializer {

    public static final String MODID = "slendermod";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);
    public static final ItemGroup SLENDER_TAB = FabricItemGroup.builder(id("slender_tab")).icon(() -> new ItemStack(BlockRegistry.SCP_SLENDERMAN_HEAD)).entries((enabledFeatures, entries, operatorEnabled) -> Registries.ITEM.stream().filter(item -> Registries.ITEM.getId(item).getNamespace().equals(MODID)).forEach(item -> {
        if (item != ItemRegistry.NOTE) {
            if (item == ItemRegistry.SLENDER_GRIMOIRE) {
                ItemStack itemNbt = item.getDefaultStack();
                ItemSlenderGrimoire.writeCustomNBT(itemNbt);
                entries.add(itemNbt, ItemGroup.StackVisibility.PARENT_AND_SEARCH_TABS);
            } else {
                entries.add(item, ItemGroup.StackVisibility.PARENT_AND_SEARCH_TABS);
            }
        }
    })).build();
    public static final GameEvent GUN_SHOT = Registry.register(Registries.GAME_EVENT, SlenderMod.id("gun_shot"), new GameEvent("gun_shot", 48));

    @Override
    public void onInitialize() {
        EntityRegistry.register();
        BlockRegistry.register();
        BlockEntityRegistry.register();
        ItemRegistry.register();
        ParticleRegistry.register();
        GeckoLib.initialize();

        ServerPacketHandler.start();
        ConfigRegistry.INSTANCE.load();
        if (FabricLoader.getInstance().isModLoaded("pixel_guns")) {
            PGCompat.onGunshotHit();
        }
    }

    public static Identifier id(String path) {
        return new Identifier(MODID, path);
    }
}
