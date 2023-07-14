package kelvin.slendermod.util;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;

public class MultiblockPositioner {
    private final BlockPos size;
    private final BlockPos modelOffset;

    private final boolean isHollow;

    private MultiblockPositioner(BlockPos size, BlockPos offset, boolean isHollow) {
        this.size = size;
        this.modelOffset = offset;
        this.isHollow = isHollow;
    }

    public BlockPos getSize() {
        return this.size;
    }
    public BlockPos getModelOffset() {
        return this.modelOffset;
    }
    public boolean getIsHollow() {
        return this.isHollow;
    }


    public static class Builder {
        private final BlockPos size;
        private BlockPos modelOffset;
        private boolean isHollow = false;

        public Builder(BlockPos size) {
            this.size = size;
        }

        public Builder offsetModel(BlockPos offset) {
            this.modelOffset = offset;
            return this;
        }

        public Builder hollow() {
            this.isHollow = true;
            return this;
        }

        public MultiblockPositioner build() {
            return new MultiblockPositioner(this.size, this.modelOffset, this.isHollow);
        }

    }

}
