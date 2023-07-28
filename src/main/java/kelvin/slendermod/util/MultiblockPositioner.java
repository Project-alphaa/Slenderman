package kelvin.slendermod.util;

import net.minecraft.util.math.Vec3i;

import java.util.ArrayList;

public class MultiblockPositioner {
    private final Vec3i SIZE;
    private final ArrayList<Point> POINTS;
    private final Vec3i customModelCenter;
    private final Vec3i customBlockCenter;
    private final boolean isHollow;
    private final boolean isShaped;

    private boolean centerBlockX;
    private boolean centerBlockY;
    private boolean centerBlockZ;

    private boolean centerModelX;
    private boolean centerModelY;
    private boolean centerModelZ;

    private MultiblockPositioner(Vec3i size, Vec3i offset, boolean isHollow, boolean isShaped, boolean centerBlockX, boolean centerBlockY, boolean centerBlockZ, boolean centerModelX, boolean centerModelY, boolean centerModelZ, Vec3i customBlockCenter) {
        this.SIZE = size;
        this.POINTS = null;
        this.customModelCenter = offset;
        this.isHollow = isHollow;
        this.isShaped = isShaped;
        this.centerBlockX = centerBlockX;
        this.centerBlockY = centerBlockY;
        this.centerBlockZ = centerBlockZ;
        this.centerModelX = centerModelX;
        this.centerModelY = centerModelY;
        this.centerModelZ = centerModelZ;
        this.customBlockCenter = customBlockCenter;
    }

    private MultiblockPositioner(Vec3i size, ArrayList<Point> points, Vec3i offset, boolean isHollow, boolean isShaped, boolean centerBlockX, boolean centerBlockY, boolean centerBlockZ, boolean centerModelX, boolean centerModelY, boolean centerModelZ, Vec3i customBlockCenter) {
        this.SIZE = size;
        this.POINTS = points;
        this.customModelCenter = offset;
        this.isHollow = isHollow;
        this.isShaped = isShaped;
        this.centerBlockX = centerBlockX;
        this.centerBlockY = centerBlockY;
        this.centerBlockZ = centerBlockZ;
        this.centerModelX = centerModelX;
        this.centerModelY = centerModelY;
        this.centerModelZ = centerModelZ;
        this.customBlockCenter = customBlockCenter;
    }

    public Vec3i getSize() {
        return this.SIZE;
    }
    public ArrayList<Point> getPoints() {
        return this.POINTS;
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

    public boolean isModelCenterX() {
        return this.centerModelX;
    }
    public boolean isModelCenterY() {
        return this.centerModelY;
    }
    public boolean isModelCenterZ() {
        return this.centerModelZ;
    }
    public boolean hasCustomBlockCenter() {
        return !(this.customBlockCenter == null);
    }
    public Vec3i getCustomBlockCenter() {
        return this.customBlockCenter;
    }

    public boolean hasCustomModelCenter() {
        return !(this.customModelCenter == null);
    }
    public Vec3i getCustomModelCenter() {
        return this.customModelCenter;
    }


    public static class Builder {
        private final Vec3i SIZE;
        private final ArrayList<Point> POINTS;

        private Vec3i modelOffset;
        private boolean isHollow = false;
        private final boolean isShaped;

        private boolean centerBlockX;
        private boolean centerBlockY;
        private boolean centerBlockZ;

        private boolean centerModelX;
        private boolean centerModelY;
        private boolean centerModelZ;
        private Vec3i customBlockCenter;


        public Builder(Vec3i size) {
            this.SIZE = size;
            this.POINTS = null;
            this.isShaped = false;
        }

        public Builder(Vec3i size, ArrayList<Point> points) {
            this.SIZE = size;
            this.POINTS = points;
            this.isShaped = true;
        }

        public Builder positionModel(Vec3i offset) {
            this.modelOffset = offset;
            return this;
        }

        public Builder centerBlockX() {
            this.centerBlockX = true;
            return this;
        }
        public Builder centerBlockY() {
            this.centerBlockY = true;
            return this;
        }
        public Builder centerBlockZ() {
            this.centerBlockZ = true;
            return this;
        }

        public Builder centerModelX() {
            this.centerModelX = true;
            return this;
        }
        public Builder centerModelY() {
            this.centerModelY = true;
            return this;
        }
        public Builder centerModelZ() {
            this.centerModelZ = true;
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
            return this.isShaped ? new MultiblockPositioner(this.SIZE, this.POINTS, this.modelOffset, this.isHollow, this.isShaped, this.centerBlockX, this.centerBlockY, this.centerBlockZ, this.centerModelX, this.centerModelY, this.centerModelZ, this.customBlockCenter) :
                    new MultiblockPositioner(this.SIZE, this.modelOffset, this.isHollow, this.isShaped, this.centerBlockX, this.centerBlockY, this.centerBlockZ, this.centerModelX, this.centerModelY, this.centerModelZ, this.customBlockCenter);
        }

    }

}
