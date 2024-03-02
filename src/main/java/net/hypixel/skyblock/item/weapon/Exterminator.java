package net.hypixel.skyblock.item.weapon;

import net.hypixel.skyblock.item.*;
import org.bukkit.ChatColor;
import net.hypixel.skyblock.item.*;

public class Exterminator implements ToolStatistics, MaterialFunction {
    @Override
    public int getBaseDamage() {
        return 10;
    }

    @Override
    public double getBaseStrength() {
        return 1.0;
    }

    @Override
    public double getBaseCritDamage() {
        return 0.0;
    }

    @Override
    public String getDisplayName() {
        return "Remnant of The Exterminator";
    }

    @Override
    public Rarity getRarity() {
        return Rarity.EPIC;
    }

    @Override
    public GenericItemType getType() {
        return GenericItemType.WEAPON;
    }

    @Override
    public SpecificItemType getSpecificType() {
        return SpecificItemType.SWORD;
    }

    @Override
    public String getLore() {
        return ChatColor.RED + "Sky" + ChatColor.GREEN + "Sim" + ChatColor.GRAY + "'s Origin Weapon remnant. Rest In Peace, " + ChatColor.RED + "Exterminator" + ChatColor.GRAY + "!";
    }
}
