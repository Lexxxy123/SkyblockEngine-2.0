package in.godspunky.skyblock.entity.end;

import org.bukkit.material.MaterialData;
import in.godspunky.skyblock.entity.EntityStatistics;

public interface EndermanStatistics extends EntityStatistics {
    default MaterialData getCarriedMaterial() {
        return null;
    }
}
