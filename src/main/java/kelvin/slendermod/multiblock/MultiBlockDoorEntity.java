package kelvin.slendermod.multiblock;

import kelvin.slendermod.registry.BlockEntityRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

public class MultiBlockDoorEntity extends MultiBlockEntity {
    public MultiBlockDoorEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.DOOR_MULTI_BLOCK_ENTITY, pos, state);
    }
}
