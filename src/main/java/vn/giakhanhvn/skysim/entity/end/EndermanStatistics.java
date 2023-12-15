package vn.giakhanhvn.skysim.entity.end;

import org.bukkit.material.MaterialData;
import vn.giakhanhvn.skysim.entity.EntityStatistics;

public interface EndermanStatistics extends EntityStatistics {
    default MaterialData getCarriedMaterial() {
        return null;
    }
}
