package in.godspunky.skyblock.entity;

public final class StaticGiantManager {
    public static boolean ACTIVE;
    public static SEntity GIANT;

    static {
        StaticGiantManager.ACTIVE = false;
        StaticGiantManager.GIANT = null;
    }

    public static void endFight() {
        if (StaticGiantManager.GIANT == null) {
            return;
        }
        StaticGiantManager.ACTIVE = false;
        StaticGiantManager.GIANT = null;
    }
}
