/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 */
package net.hypixel.skyblock.item.accessory;

import com.google.common.util.concurrent.AtomicDouble;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.hypixel.skyblock.item.GenericItemType;
import net.hypixel.skyblock.item.Rarity;
import net.hypixel.skyblock.item.SItem;
import net.hypixel.skyblock.item.SpecificItemType;
import net.hypixel.skyblock.item.accessory.AccessoryFunction;
import net.hypixel.skyblock.item.accessory.AccessoryStatistics;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class TarantulaTalisman
implements AccessoryStatistics,
AccessoryFunction {
    private static final Map<UUID, Integer> HITS = new HashMap<UUID, Integer>();

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
    public void onDamageInInventory(SItem weapon, Player damager, Entity damaged, SItem accessory, AtomicDouble damage) {
        HITS.put(damager.getUniqueId(), HITS.getOrDefault(damager.getUniqueId(), 0) + 1);
        if (HITS.get(damager.getUniqueId()) >= 10) {
            damage.addAndGet(damage.get() * 0.1);
            HITS.remove(damager.getUniqueId());
        }
    }
}

