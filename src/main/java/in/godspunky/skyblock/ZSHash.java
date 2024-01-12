package in.godspunky.skyblock;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ZSHash {
    public static final Map<UUID, Integer> Charges;
    public static final Map<UUID, Integer> Cooldown;

    static {
        Charges = new HashMap<>();
        Cooldown = new HashMap<>();
    }
}
