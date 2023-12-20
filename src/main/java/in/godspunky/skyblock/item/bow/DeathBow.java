package in.godspunky.skyblock.item.bow;

import in.godspunky.skyblock.item.GenericItemType;
import in.godspunky.skyblock.item.Rarity;
import in.godspunky.skyblock.item.SpecificItemType;
import in.godspunky.skyblock.item.ToolStatistics;
import net.md_5.bungee.api.ChatColor;

public class DeathBow implements ToolStatistics, BowFunction {
    @Override
    public String getDisplayName() {
        return "Death Bow";
    }

    @Override
    public Rarity getRarity() {
        return Rarity.EPIC;
    }

    @Override
    public int getBaseDamage() {
        return 300;
    }

    @Override
    public String getLore() {
        return "Deal " + ChatColor.GREEN + "+100% " + ChatColor.GRAY + "more damage to Undead Monsters, Your arrows have a" + ChatColor.YELLOW + " 50.0%" + ChatColor.GRAY + " chance to bounce to another target after hitting an enemy.";
    }

    @Override
    public GenericItemType getType() {
        return GenericItemType.RANGED_WEAPON;
    }

    @Override
    public SpecificItemType getSpecificType() {
        return SpecificItemType.BOW;
    }
}
