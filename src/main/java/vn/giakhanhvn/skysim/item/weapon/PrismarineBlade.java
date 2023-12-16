package vn.giakhanhvn.skysim.item.weapon;

import org.bukkit.ChatColor;
import vn.giakhanhvn.skysim.item.*;

public class PrismarineBlade implements ToolStatistics, MaterialFunction {
    @Override
    public int getBaseDamage() {
        return 50;
    }

    @Override
    public double getBaseStrength() {
        return 25.0;
    }

    @Override
    public String getDisplayName() {
        return "Prismarine Blade";
    }

    @Override
    public Rarity getRarity() {
        return Rarity.UNCOMMON;
    }

    @Override
    public GenericItemType getType() {
        return GenericItemType.WEAPON;
    }

    @Override
    public String getLore() {
        return "Deals " + ChatColor.GREEN + "+200% " + ChatColor.GRAY + "damage while in water.";
    }

    @Override
    public SpecificItemType getSpecificType() {
        return SpecificItemType.SWORD;
    }
}
