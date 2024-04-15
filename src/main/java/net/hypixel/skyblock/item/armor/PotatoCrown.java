package net.hypixel.skyblock.item.armor;

import net.hypixel.skyblock.item.*;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import net.hypixel.skyblock.item.*;

import java.util.ArrayList;
import java.util.List;

public class PotatoCrown implements MaterialFunction, ToolStatistics {
    @Override
    public double getBaseSpeed() {
        return 1;
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
        final List<String> lore = new ArrayList<>();
        lore.add(formatLore("&6Ability: Potato Crown"));
        lore.add(formatLore("Bestowed by Hypixel to the winner of"));
        lore.add(formatLore("the Great Potato War."));
        lore.add(formatLore(" "));
        lore.add(formatLore("&dAll men can see the tactics whereby I"));
        lore.add(formatLore("&dconquer, but what none can see is the"));
        lore.add(formatLore("&dstrategy out of which victory is"));
        lore.add(formatLore("&devolved."));
        lore.add(formatLore("&8- Sun Tzu, The Art of War"));
        return lore;
    }

    private String formatLore(String lore) {
        return ChatColor.translateAlternateColorCodes('&', lore);
    }
}
