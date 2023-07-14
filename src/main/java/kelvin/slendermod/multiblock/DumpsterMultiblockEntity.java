package kelvin.slendermod.multiblock;

import kelvin.slendermod.registry.BlockEntityRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

public class DumpsterMultiblockEntity extends MultiBlockEntity {
    public DumpsterMultiblockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.DUMPSTER_MULTI_BLOCK_ENTITY, pos, state);
    }
}
