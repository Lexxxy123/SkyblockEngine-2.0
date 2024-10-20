/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 */
package net.hypixel.skyblock.item.armor;

import java.util.ArrayList;
import java.util.List;
import net.hypixel.skyblock.item.GenericItemType;
import net.hypixel.skyblock.item.MaterialFunction;
import net.hypixel.skyblock.item.Rarity;
import net.hypixel.skyblock.item.SpecificItemType;
import net.hypixel.skyblock.item.ToolStatistics;
import org.bukkit.ChatColor;

public class PotatoCrown
implements MaterialFunction,
ToolStatistics {
    @Override
    public double getBaseSpeed() {
        return 1.0;
    }

    @Override
    public String getDisplayName() {
        return "Potato Crown";
    }

    @Override
    public Rarity getRarity() {
        return Rarity.LEGENDARY;
    }

    @Override
    public GenericItemType getType() {
        return GenericItemType.ARMOR;
    }

    @Override
    public SpecificItemType getSpecificType() {
        return SpecificItemType.HELMET;
    }

    @Override
    public List<String> getListLore() {
        ArrayList<String> lore = new ArrayList<String>();
        lore.add(this.formatLore("&6Ability: Potato Crown"));
        lore.add(this.formatLore("Bestowed by Hypixel to the winner of"));
        lore.add(this.formatLore("the Great Potato War."));
        lore.add(this.formatLore(" "));
        lore.add(this.formatLore("&dAll men can see the tactics whereby I"));
        lore.add(this.formatLore("&dconquer, but what none can see is the"));
        lore.add(this.formatLore("&dstrategy out of which victory is"));
        lore.add(this.formatLore("&devolved."));
        lore.add(this.formatLore("&8- Sun Tzu, The Art of War"));
        return lore;
    }

    private String formatLore(String lore) {
        return ChatColor.translateAlternateColorCodes((char)'&', (String)lore);
    }
}

