package kelvin.slendermod.registry;

import kelvin.slendermod.blockentity.*;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

import static kelvin.slendermod.SlenderMod.id;

public class BlockEntityRegistry {

    public static final BlockEntityType<RotatableBlockEntity> ROTATABLE_BLOCK_ENTITY = register("rotatable_block_entity", BlockEntityType.Builder.create(RotatableBlockEntity::new, BlockRegistry.JUNK_PILE, BlockRegistry.DEBRIS, BlockRegistry.DEAD_TREE, BlockRegistry.SCRATCHED_DEAD_TREE, BlockRegistry.UFO_INTERIOR).build(null));

    public static final BlockEntityType<SafeBlockEntity> ACCESS_READER_ENTITY = register("access_reader", FabricBlockEntityTypeBuilder.create(SafeBlockEntity::new, BlockRegistry.ACCESS_READER).build());

    public static final BlockEntityType<RadioBlockEntity> RADIO_BLOCK_ENTITY = register("radio", BlockEntityType.Builder.create(RadioBlockEntity::new, BlockRegistry.RADIO).build(null));


    public static final BlockEntityType<SafeBlockEntity> SAFE_BLOCK_ENTITY = register("safe", BlockEntityType.Builder.create(SafeBlockEntity::new, BlockRegistry.SAFE).build(null));

    public static final BlockEntityType<TrashBinBlockEntity> TRASH_BIN_BLOCK_ENTITY = register("trash_bin", BlockEntityType.Builder.create(TrashBinBlockEntity::new, BlockRegistry.TRASH_BIN).build(null));

    public static final BlockEntityType<WalkmanBlockEntity> WALKMAN_BLOCK_ENTITY = register("walkman_block", BlockEntityType.Builder.create(WalkmanBlockEntity::new, BlockRegistry.WALKMAN).build(null));

    public static void register() {
    }

    private static <T extends BlockEntityType<?>> T register(String name, T blockEntityType) {
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, id(name), blockEntityType);
    }
}
