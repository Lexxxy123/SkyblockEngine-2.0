package in.godspunky.skyblock.item.weapon;

import in.godspunky.skyblock.item.*;
import net.md_5.bungee.api.ChatColor;

public class VoidwalkerKatana implements ToolStatistics, MaterialFunction {
    @Override
    public int getBaseDamage() {
        return 80;
    }

    @Override
    public double getBaseStrength() {
        return 40.0;
    }

    @Override
    public double getBaseCritDamage() {
        return 0.1;
    }

    @Override
    public String getDisplayName() {
        return "Voidwalker Katana";
    }

    @Override
    public String getLore() {
        return "Deal " + ChatColor.GREEN + "+100% " + ChatColor.GRAY + "damage to Endermen.";
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
    public SpecificItemType getSpecificType() {
        return SpecificItemType.SWORD;
    }
}
