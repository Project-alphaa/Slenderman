package kelvin.slendermod.util;

import net.minecraft.util.math.Vec3i;

public class Point {
    private final int X;
    private final int Y;
    private final int Z;


    public Point(int x, int y, int z) {
        this.X = x;
        this.Y = y;
        this.Z = z;
    }

    public Vec3i asVec() {
        return new Vec3i(this.X, this.Y, this.Z);
    }

    public int getX() {
        return this.X;
    }
    public int getY() {
        return this.Y;
    }
    public int getZ() {
        return this.Z;
    }
}
