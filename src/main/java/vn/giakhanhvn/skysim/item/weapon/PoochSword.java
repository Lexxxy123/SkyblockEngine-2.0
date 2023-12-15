package vn.giakhanhvn.skysim.item.weapon;

import vn.giakhanhvn.skysim.item.SpecificItemType;
import org.bukkit.ChatColor;
import vn.giakhanhvn.skysim.item.GenericItemType;
import vn.giakhanhvn.skysim.item.Rarity;
import vn.giakhanhvn.skysim.item.MaterialFunction;
import vn.giakhanhvn.skysim.item.ToolStatistics;

public class PoochSword implements ToolStatistics, MaterialFunction {
    @Override
    public int getBaseDamage() {
        return 120;
    }

    @Override
    public double getBaseStrength() {
        return 20.0;
    }

    @Override
    public double getBaseCritDamage() {
        return 0.5;
    }

    @Override
    public double getBaseSpeed() {
        return 0.05;
    }

    @Override
    public String getDisplayName() {
        return "Pooch Sword";
    }

    @Override
    public Rarity getRarity() {
        return Rarity.LEGENDARY;
    }

    @Override
    public GenericItemType getType() {
        return GenericItemType.WEAPON;
    }

    @Override
    public String getLore() {
        return ChatColor.translateAlternateColorCodes('&', "Deal &c+1 Damage &7per &c50 &cmax Health ❤&7. Receive &a-20% &7damage from wolves. &7Gain &c+150❁ Strength &7against wolves.");
    }

    @Override
    public SpecificItemType getSpecificType() {
        return SpecificItemType.SWORD;
    }
}
