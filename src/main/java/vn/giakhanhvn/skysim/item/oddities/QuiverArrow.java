package vn.giakhanhvn.skysim.item.oddities;

import vn.giakhanhvn.skysim.item.GenericItemType;
import vn.giakhanhvn.skysim.item.Rarity;
import org.bukkit.ChatColor;
import vn.giakhanhvn.skysim.item.Untradeable;
import vn.giakhanhvn.skysim.item.MaterialFunction;
import vn.giakhanhvn.skysim.item.MaterialStatistics;

public class QuiverArrow implements MaterialStatistics, MaterialFunction, Untradeable {
    @Override
    public String getDisplayName() {
        return ChatColor.DARK_GRAY + "Quiver Arrow";
    }

    @Override
    public Rarity getRarity() {
        return Rarity.COMMON;
    }

    @Override
    public GenericItemType getType() {
        return GenericItemType.ITEM;
    }

    @Override
    public String getLore() {
        return "This item is in your inventory because you are holding your bow currently. Switch your held item to see the item that was here before.";
    }

    @Override
    public boolean displayRarity() {
        return false;
    }
}
