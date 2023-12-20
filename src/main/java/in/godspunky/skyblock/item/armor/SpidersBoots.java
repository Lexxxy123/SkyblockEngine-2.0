package in.godspunky.skyblock.item.armor;

import in.godspunky.skyblock.item.*;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import in.godspunky.skyblock.item.*;

public class SpidersBoots implements ToolStatistics, TickingMaterial, FlightStatistics, Ability {
    @Override
    public String getAbilityName() {
        return "Double Jump";
    }

    @Override
    public String getAbilityDescription() {
        return "Allows you to Double Jump! " + ChatColor.RED + "Temporary Disabled!";
    }

    @Override
    public int getAbilityCooldownTicks() {
        return 0;
    }

    @Override
    public AbilityActivation getAbilityActivation() {
        return AbilityActivation.FLIGHT;
    }

    @Override
    public void onAbilityUse(final Player player, final SItem sItem) {
    }

    @Override
    public int getManaCost() {
        return 0;
    }

    @Override
    public String getDisplayName() {
        return "Spider's Boots";
    }

    @Override
    public Rarity getRarity() {
        return Rarity.RARE;
    }

    @Override
    public GenericItemType getType() {
        return GenericItemType.ARMOR;
    }

    @Override
    public SpecificItemType getSpecificType() {
        return SpecificItemType.BOOTS;
    }

    @Override
    public double getBaseDefense() {
        return 45.0;
    }

    @Override
    public double getBaseSpeed() {
        return 0.05;
    }

    @Override
    public double getBaseIntelligence() {
        return 50.0;
    }

    @Override
    public void tick(final SItem item, final Player owner) {
    }

    @Override
    public boolean enableFlight() {
        return false;
    }
}
