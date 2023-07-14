package kelvin.slendermod.multiblock;

import kelvin.slendermod.util.MultiblockPositioner;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

public class DumpsterMultiblock extends MultiBlock {
    public DumpsterMultiblock(Settings settings, MultiblockPositioner positioner) {
        super(settings, positioner);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new DumpsterMultiblockEntity(pos, state);
    }
}
