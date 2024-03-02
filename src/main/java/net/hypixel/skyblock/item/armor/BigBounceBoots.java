package net.hypixel.skyblock.item.armor;

import net.hypixel.skyblock.item.*;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import net.hypixel.skyblock.item.*;

public class BigBounceBoots implements LeatherArmorStatistics, TickingMaterial, FlightStatistics, Ability {
    @Override
    public String getAbilityName() {
        return "Larger Double Jump";
    }

    @Override
    public String getAbilityDescription() {
        return "Allows you to Double Jump higher! " + ChatColor.RED + "Disabled!";
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
        return 80;
    }

    @Override
    public String getDisplayName() {
        return "Doubling Boots";
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
        return SpecificItemType.BOOTS;
    }

    @Override
    public double getBaseDefense() {
        return 90.0;
    }

    @Override
    public double getBaseSpeed() {
        return 0.4;
    }

    @Override
    public double getBaseIntelligence() {
        return 0.0;
    }

    @Override
    public void tick(final SItem item, final Player owner) {
    }

    @Override
    public boolean enableFlight() {
        return false;
    }

    @Override
    public int getColor() {
        return 9548515;
    }
}
