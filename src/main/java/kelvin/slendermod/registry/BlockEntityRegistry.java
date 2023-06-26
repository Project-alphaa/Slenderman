package kelvin.slendermod.registry;

import kelvin.slendermod.blockentity.AccessReaderBlockEntity;
import kelvin.slendermod.blockentity.RadioBlockEntity;
import kelvin.slendermod.blockentity.RotatableBlockEntity;
import kelvin.slendermod.blockentity.TrashBinBlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

import static kelvin.slendermod.SlenderMod.id;

public class BlockEntityRegistry {

    public static final BlockEntityType<RotatableBlockEntity> ROTATABLE_BLOCK_ENTITY = register("rotatable_block_entity",
            BlockEntityType.Builder.create(RotatableBlockEntity::new, BlockRegistry.CAR_BODY, BlockRegistry.TRASH, BlockRegistry.DEBRIS, BlockRegistry.DEAD_TREE, BlockRegistry.SCRATCHED_DEAD_TREE, BlockRegistry.UFO_INTERIOR).build(null)
    );

    public static final BlockEntityType<AccessReaderBlockEntity> ACCESS_READER_ENTITY = register("access_reader",
            BlockEntityType.Builder.create(AccessReaderBlockEntity::new, BlockRegistry.ACCESS_READER).build(null)
    );

    public static final BlockEntityType<RadioBlockEntity> RADIO_BLOCK_ENTITY = register("radio",
            BlockEntityType.Builder.create(RadioBlockEntity::new, BlockRegistry.RADIO).build(null)
    );

    public static final BlockEntityType<TrashBinBlockEntity> TRASH_BIN_BLOCK_ENTITY = register("trash_bin",
            BlockEntityType.Builder.create(TrashBinBlockEntity::new, BlockRegistry.TRASH_BIN).build(null)
    );

    public static void register() {
    }

    private static <T extends BlockEntityType<?>> T register(String name, T blockEntityType) {
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, id(name), blockEntityType);
    }
}
