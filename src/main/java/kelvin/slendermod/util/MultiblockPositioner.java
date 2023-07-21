package kelvin.slendermod.util;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

import java.util.ArrayList;

public class MultiblockPositioner {
    private final Vec3i SIZE;
    private final ArrayList<Point> POINTS;
    private final Vec3i modelOffset;

    private final boolean isHollow;
    private final boolean isShaped;

    private boolean centerBlockX;
    private boolean centerBlockY;
    private boolean centerBlockZ;
    private Vec3i customBlockCenter;

    private MultiblockPositioner(Vec3i size, Vec3i offset, boolean isHollow, boolean isShaped, boolean centerBlockX, boolean centerBlockY, boolean centerBlockZ, Vec3i customBlockCenter) {
        this.SIZE = size;
        this.POINTS = null;
        this.modelOffset = offset;
        this.isHollow = isHollow;
        this.isShaped = isShaped;
        this.centerBlockX = centerBlockX;
        this.centerBlockY = centerBlockY;
        this.centerBlockZ = centerBlockZ;
        this.customBlockCenter = customBlockCenter;
    }

    private MultiblockPositioner(BlockPos size, ArrayList<Point> points, BlockPos offset, boolean isHollow, boolean isShaped, boolean centerBlockX, boolean centerBlockY, boolean centerBlockZ, Vec3i customBlockCenter) {
        this.SIZE = size;
        this.POINTS = points;
        this.modelOffset = offset;
        this.isHollow = isHollow;
        this.isShaped = isShaped;
        this.centerBlockX = centerBlockX;
        this.centerBlockY = centerBlockY;
        this.centerBlockZ = centerBlockZ;
        this.customBlockCenter = customBlockCenter;
    }

    public Vec3i getSize() {
        return this.SIZE;
    }
    public ArrayList<Point> getPoints() {
        return this.POINTS;
    }
    public Vec3i getModelOffset() {
        return this.modelOffset;
    }
    public boolean isHollow() {
        return this.isHollow;
    }
    public boolean isShaped() {
        return this.isShaped;
    }
    public boolean isBlockCenterX() {
        return this.centerBlockX;
    }
    public boolean isBlockCenterY() {
        return this.centerBlockY;
    }
    public boolean isBlockCenterZ() {
        return this.centerBlockZ;
    }
    public boolean isCustomCenter() {
        return !(this.customBlockCenter == null);
    }
    public Vec3i getCustomBlockCenter() {
        return this.customBlockCenter;
    }


    public static class Builder {
        private final BlockPos SIZE;
        private final ArrayList<Point> POINTS;

        private BlockPos modelOffset;
        private boolean isHollow = false;
        private final boolean isShaped;

        private boolean centerBLockX;
        private boolean centerBLockY;
        private boolean centerBLockZ;
        private Vec3i customBlockCenter;


        public Builder(BlockPos size) {
            this.SIZE = size;
            this.POINTS = null;
            this.isShaped = false;
        }

        public Builder(BlockPos size, ArrayList<Point> points) {
            this.SIZE = size;
            this.POINTS = points;
            this.isShaped = true;
        }

        public Builder offsetModel(BlockPos offset) {
            this.modelOffset = offset;
            return this;
        }

        public Builder centerBlockX() {
            this.centerBLockX = true;
            return this;
        }
        public Builder centerBlockY() {
            this.centerBLockY = true;
            return this;
        }
        public Builder centerBlockZ() {
            this.centerBLockZ = true;
            return this;
        }
        public Builder offsetBlock(Vec3i offset) {
            this.customBlockCenter = offset;
            return this;
        }

        public Builder hollow() {
            this.isHollow = true;
            return this;
        }

        public MultiblockPositioner build() {
            return this.isShaped ? new MultiblockPositioner(this.SIZE, this.POINTS, this.modelOffset, this.isHollow, this.isShaped, this.centerBLockX, this.centerBLockY, this.centerBLockZ, this.customBlockCenter) :
                    new MultiblockPositioner(this.SIZE, this.modelOffset, this.isHollow, this.isShaped, this.centerBLockX, this.centerBLockY, this.centerBLockZ, this.customBlockCenter);
        }

    }

}
