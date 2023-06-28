package kelvin.slendermod.util;

public class MiscUtil {
    public static float staticIntensity = 0.0F;
    public static float staticSpeed = 1.0F;

    // Range of 0.0 to 1.0 anything above 1.0 acts like 1.0 and vice versa
    // 0 is no static 1 is max static
    public static void setStaticIntensity(float val) {
        staticIntensity = val;
    }

    // Acts like a multiplier
    public static void setStaticSpeed(float val) {
        staticSpeed = val;
    }
}
