package in.godspunky.skyblock.entity.end;

import in.godspunky.skyblock.entity.EntityStatistics;
import org.bukkit.material.MaterialData;

public interface EndermanStatistics extends EntityStatistics {
    default MaterialData getCarriedMaterial() {
        return null;
    }
}
