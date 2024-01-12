package in.godspunky.skyblock.user;

import java.util.HashMap;

public class UserDataHolder {
    private static final HashMap<String, UserDataHolder> DATA_HOLDER_CACHE = new HashMap<>();
    private final HashMap<String, Object> DATA_CACHE;

    private UserDataHolder(String uuid) {
        this.DATA_CACHE = new HashMap<>();
    }

    public static UserDataHolder getDataHolder(String uuid) {
        if (DATA_HOLDER_CACHE.containsKey(uuid)) return DATA_HOLDER_CACHE.get(uuid);
        DATA_HOLDER_CACHE.put(uuid, new UserDataHolder(uuid));
        return DATA_HOLDER_CACHE.get(uuid);
    }

    public void addData(String key, Object value) {
        DATA_CACHE.put(key, value);
    }

    public HashMap<String, Object> getDATA_CACHE() {
        return DATA_CACHE;
    }
}
