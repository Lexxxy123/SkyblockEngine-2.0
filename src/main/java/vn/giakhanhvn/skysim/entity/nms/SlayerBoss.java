package vn.giakhanhvn.skysim.entity.nms;

import java.util.UUID;

public interface SlayerBoss
{
    UUID getSpawnerUUID();
    
    default int getTier() {
        return 1;
    }
}
