package kelvin.slendermod.registry;

import kelvin.slendermod.entity.EntityAdultSCPSlender;
import kelvin.slendermod.entity.EntitySlenderBoss;
import kelvin.slendermod.entity.EntitySlenderman;
import kelvin.slendermod.entity.EntitySmallSCPSlender;
import net.fabricmc.fabric.api.biome.v1.BiomeModificationContext;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.world.Difficulty;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.SpawnSettings;

import static kelvin.slendermod.SlenderMod.id;

public class EntityRegistry {

    private static final EntityDimensions ADULT_SLENDER_SIZE = EntityDimensions.fixed(0.7f, 2.8f);

    public static final EntityType<EntityAdultSCPSlender> SCP_SLENDERMAN = register("scp_slenderman",
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, EntityAdultSCPSlender::new).dimensions(ADULT_SLENDER_SIZE).build()
    );

    public static final EntityType<EntityAdultSCPSlender> SCP_SLENDERWOMAN = register("scp_slenderwoman",
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, EntityAdultSCPSlender::new).dimensions(ADULT_SLENDER_SIZE).build()
    );

    public static final EntityType<EntitySmallSCPSlender> SMALL_SCP_SLENDER = register("small_scp_slender",
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, EntitySmallSCPSlender::new).dimensions(EntityDimensions.fixed(0.75f, 1.5f)).build()
    );

    public static final EntityType<EntitySlenderBoss> SLENDER_BOSS = register("slender_boss",
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, EntitySlenderBoss::new).dimensions(EntityDimensions.fixed(2, 4)).build()
    );

    public static final EntityType<EntitySlenderman> SLENDERMAN = register("slenderman",
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, EntitySlenderman::new).dimensions(ADULT_SLENDER_SIZE).build()
    );

    public static void register() {
        spawnSlender(SCP_SLENDERMAN, "scp_slenderman", 2);
        spawnSlender(SCP_SLENDERWOMAN, "scp_slenderwoman", 2);
        spawnSlender(SMALL_SCP_SLENDER, "small_scp_slender", 3);

        BiomeModifications.create(id("remove_all_monsters")).add(ModificationPhase.REMOVALS, selectionContext ->
                        selectionContext.hasTag(BiomeTags.IS_OVERWORLD) || selectionContext.hasTag(BiomeTags.IS_NETHER) || selectionContext.hasTag(BiomeTags.IS_END),
                (biomeSelectionContext, modificationContext) -> {
                    modificationContext.getSpawnSettings().removeSpawns((spawnGroup, spawnEntry) ->
                            spawnGroup == SpawnGroup.MONSTER && spawnEntry.type != SCP_SLENDERMAN && spawnEntry.type != SCP_SLENDERWOMAN && spawnEntry.type != SMALL_SCP_SLENDER);
                });

        FabricDefaultAttributeRegistry.register(SCP_SLENDERMAN, EntityAdultSCPSlender.createAttributes());
        FabricDefaultAttributeRegistry.register(SCP_SLENDERWOMAN, EntityAdultSCPSlender.createAttributes());
        FabricDefaultAttributeRegistry.register(SMALL_SCP_SLENDER, EntitySmallSCPSlender.createAttributes());
        FabricDefaultAttributeRegistry.register(SLENDER_BOSS, EntitySlenderBoss.createAttributes());
        FabricDefaultAttributeRegistry.register(SLENDERMAN, EndermanEntity.createEndermanAttributes());
    }

    private static <T extends MobEntity> void spawnSlender(EntityType<T> slender, String name, int maxGroupSize) {
        SpawnRestriction.register(slender, SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, (type, world, spawnReason, pos, random) ->
                world.getDifficulty() != Difficulty.PEACEFUL && MobEntity.canMobSpawn(type, world, spawnReason, pos, random));

        BiomeModifications.create(id(name)).add(ModificationPhase.ADDITIONS, selectionContext ->
                        selectionContext.hasTag(BiomeTags.IS_OVERWORLD) || selectionContext.hasTag(BiomeTags.IS_NETHER) || selectionContext.hasTag(BiomeTags.IS_END),
                (selectionContext, modificationContext) -> {
                    BiomeModificationContext.SpawnSettingsContext settings = modificationContext.getSpawnSettings();
                    settings.setSpawnCost(slender, 10, 10);
                    settings.addSpawn(SpawnGroup.MONSTER, new SpawnSettings.SpawnEntry(slender, 1, 1, maxGroupSize));
                });
    }

    private static <T extends EntityType<?>> T register(String name, T entityType) {
        return Registry.register(Registries.ENTITY_TYPE, id(name), entityType);
    }
}
