package in.godspunky.skyblock.user;

import java.util.HashMap;


public class ProfileDataHolder {
    private static final HashMap<String , ProfileDataHolder> DATA_HOLDER_CACHE = new HashMap<>();
    private final HashMap<String , Object> DATA_CACHE;

    private ProfileDataHolder(String uuid){
        this.DATA_CACHE = new HashMap<>();
        System.out.println("creating new profile holder!");
    }
    public void addData(String key, Object value){
        DATA_CACHE.put(key , value);
    }
    public static ProfileDataHolder getDataHolder(String uuid) {
        if (DATA_HOLDER_CACHE.containsKey(uuid)) return DATA_HOLDER_CACHE.get(uuid);
        DATA_HOLDER_CACHE.put(uuid , new ProfileDataHolder(uuid));
        return DATA_HOLDER_CACHE.get(uuid);

    }

    public HashMap<String, Object> getDATA_CACHE() {
        return DATA_CACHE;
    }
}
