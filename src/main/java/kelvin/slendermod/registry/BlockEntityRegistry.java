package kelvin.slendermod.registry;

import kelvin.slendermod.blockentity.*;
import kelvin.slendermod.multiblock.DumpsterMultiblockEntity;
import kelvin.slendermod.multiblock.MultiBlockDoorEntity;
import kelvin.slendermod.multiblock.MultiBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

import static kelvin.slendermod.SlenderMod.id;

public class BlockEntityRegistry {

    public static final BlockEntityType<RotatableBlockEntity> ROTATABLE_BLOCK_ENTITY = register("rotatable_block_entity", BlockEntityType.Builder.create(RotatableBlockEntity::new, BlockRegistry.CAR_BODY, BlockRegistry.JUNK_PILE, BlockRegistry.DEBRIS, BlockRegistry.DEAD_TREE, BlockRegistry.SCRATCHED_DEAD_TREE, BlockRegistry.UFO_INTERIOR).build(null));

    public static final BlockEntityType<SafeBlockEntity> ACCESS_READER_ENTITY = register("access_reader", FabricBlockEntityTypeBuilder.create(SafeBlockEntity::new, BlockRegistry.ACCESS_READER).build());

    public static final BlockEntityType<RadioBlockEntity> RADIO_BLOCK_ENTITY = register("radio", BlockEntityType.Builder.create(RadioBlockEntity::new, BlockRegistry.RADIO).build(null));


    public static final BlockEntityType<SafeBlockEntity> SAFE_BLOCK_ENTITY = register("safe", BlockEntityType.Builder.create(SafeBlockEntity::new, BlockRegistry.SAFE).build(null));

    public static final BlockEntityType<TrashBinBlockEntity> TRASH_BIN_BLOCK_ENTITY = register("trash_bin", BlockEntityType.Builder.create(TrashBinBlockEntity::new, BlockRegistry.TRASH_BIN).build(null));

    public static final BlockEntityType<WalkmanBlockEntity> WALKMAN_BLOCK_ENTITY = register("walkman_block", BlockEntityType.Builder.create(WalkmanBlockEntity::new, BlockRegistry.WALKMAN).build(null));

    public static final BlockEntityType<DumpsterMultiblockEntity> DUMPSTER_MULTI_BLOCK_ENTITY = register("multi_block", BlockEntityType.Builder.create(DumpsterMultiblockEntity::new, BlockRegistry.DUMPSTER_MULTI_BLOCK).build(null));

    public static final BlockEntityType<MultiBlockDoorEntity> DOOR_MULTI_BLOCK_ENTITY = register("multi_block_door", BlockEntityType.Builder.create(MultiBlockDoorEntity::new, BlockRegistry.MULTI_BLOCK_DOOR).build(null));
    public static void register() {
    }

    private static <T extends BlockEntityType<?>> T register(String name, T blockEntityType) {
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, id(name), blockEntityType);
    }
}
