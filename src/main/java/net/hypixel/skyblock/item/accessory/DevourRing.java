/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 */
package net.hypixel.skyblock.item.accessory;

import com.google.common.util.concurrent.AtomicDouble;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.hypixel.skyblock.item.Rarity;
import net.hypixel.skyblock.item.SItem;
import net.hypixel.skyblock.item.accessory.AccessoryFunction;
import net.hypixel.skyblock.item.accessory.AccessoryStatistics;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class DevourRing
implements AccessoryStatistics,
AccessoryFunction {
    private static final Map<UUID, Integer> HITS = new HashMap<UUID, Integer>();

    @Override
    public String getURL() {
        return "f06706eecb2d558ace27abda0b0b7b801d36d17dd7a890a9520dbe522374f8a6";
    }

    @Override
    public List<String> getListLore() {
        ArrayList<String> lore = new ArrayList<String>();
        lore.add(ChatColor.GRAY + "Heal 5\u2764 when killing a");
        lore.add(ChatColor.GRAY + "monster.");
        return lore;
    }

    @Override
    public String getDisplayName() {
        return "Devour Ring";
    }

    @Override
    public Rarity getRarity() {
        return Rarity.RARE;
    }

    @Override
    public void onDamageInInventory(SItem weapon, Player player, Entity damaged, SItem accessory, AtomicDouble damage) {
        player.setMaxHealth(Math.min(player.getMaxHealth(), player.getHealth() + player.getMaxHealth() * 0.02));
    }
}

