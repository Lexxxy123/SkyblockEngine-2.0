package in.godspunky.skyblock.item.accessory;

import com.google.common.util.concurrent.AtomicDouble;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import in.godspunky.skyblock.item.GenericItemType;
import in.godspunky.skyblock.item.Rarity;
import in.godspunky.skyblock.item.SItem;
import in.godspunky.skyblock.item.SpecificItemType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TarantulaTalisman implements AccessoryStatistics, AccessoryFunction {
    private static final Map<UUID, Integer> HITS;

    @Override
    public String getDisplayName() {
        return "Tarantula Talisman";
    }

    @Override
    public Rarity getRarity() {
        return Rarity.EPIC;
    }

    @Override
    public GenericItemType getType() {
        return GenericItemType.ACCESSORY;
    }

    @Override
    public SpecificItemType getSpecificType() {
        return SpecificItemType.ACCESSORY;
    }

    @Override
    public String getURL() {
        return "442cf8ce487b78fa203d56cf01491434b4c33e5d236802c6d69146a51435b03d";
    }

    @Override
    public void onDamageInInventory(final SItem weapon, final Player damager, final Entity damaged, final SItem accessory, final AtomicDouble damage) {
        TarantulaTalisman.HITS.put(damager.getUniqueId(), TarantulaTalisman.HITS.getOrDefault(damager.getUniqueId(), 0) + 1);
        if (TarantulaTalisman.HITS.get(damager.getUniqueId()) >= 10) {
            damage.addAndGet(damage.get() * 0.1);
            TarantulaTalisman.HITS.remove(damager.getUniqueId());
        }
    }

    static {
        HITS = new HashMap<UUID, Integer>();
    }
}
