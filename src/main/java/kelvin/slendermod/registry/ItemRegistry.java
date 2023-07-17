package kelvin.slendermod.registry;

import kelvin.slendermod.item.*;
import kelvin.slendermod.item.medkit.MedicalKitItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

import static kelvin.slendermod.SlenderMod.id;

@SuppressWarnings("unused")
public class ItemRegistry {

    public static final Item FLASHLIGHT = register("flashlight", new FlashlightItem(new Item.Settings().maxCount(1)));

    public static final Item BOSS_ATTACK = register("boss_hit", new BossAttackItem());

    public static final Item BOSS_DASH = register("boss_dash", new BossDashItem());

    public static final Item SLENDER_GRIMOIRE = register("slender_grimoire", new SlenderGrimoireItem());

    public static final Item NOTE = register("note", new NoteItem());

    public static final Item WRITABLE_NOTE = register("writable_note", new WritableNoteItem());

    public static final Item CASSETTE_TAPE = register("cassette_tape", new CassetteTapeItem(1, SoundRegistry.TAPE, new Item.Settings().maxCount(1), 5));

    public static final Item ACCESS_CARD = register("access_card", new Item(new Item.Settings()));

    public static final Item MEDICAL_KIT = register("medical_kit", new MedicalKitItem(new Item.Settings().maxCount(1)));

    public static final Item SAFE_ITEM = register("safe", new SafeBlockItem(BlockRegistry.SAFE, new FabricItemSettings()));

    public static final Item SCP_SLENDERMAN_SPAWN_EGG = register("scp_slenderman_spawn_egg", new SpawnEggItem(EntityRegistry.SCP_SLENDERMAN, 10658466, 6842472, new Item.Settings()));

    public static final Item SCP_SLENDERWOMAN_SPAWN_EGG = register("scp_slenderwoman_spawn_egg", new SpawnEggItem(EntityRegistry.SCP_SLENDERWOMAN, 10658466, 16777215, new Item.Settings()));

    public static final Item SMALL_SCP_SLENDER_SPAWN_EGG = register("small_scp_slender_spawn_egg", new SpawnEggItem(EntityRegistry.SMALL_SCP_SLENDER, 5722960, 9078144, new Item.Settings()));

    public static final Item SLENDER_BOSS_SPAWN_EGG = register("slender_boss_spawn_egg", new SpawnEggItem(EntityRegistry.SLENDER_BOSS, 11578536, 12688761, new Item.Settings()));

    public static final Item SLENDERMAN_SPAWN_EGG = register("slenderman_spawn_egg", new SpawnEggItem(EntityRegistry.SLENDERMAN, 0, 16777215, new Item.Settings()));

    public static void register() {
    }

    private static <T extends Item> T register(String name, T item) {
        return Registry.register(Registries.ITEM, id(name), item);
    }
}
