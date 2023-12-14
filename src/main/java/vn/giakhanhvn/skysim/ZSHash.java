package vn.giakhanhvn.skysim;

import java.util.HashMap;
import java.util.UUID;
import java.util.Map;

public class ZSHash
{
    public static final Map<UUID, Integer> Charges;
    public static final Map<UUID, Integer> Cooldown;
    
    static {
        Charges = new HashMap<UUID, Integer>();
        Cooldown = new HashMap<UUID, Integer>();
    }
}
