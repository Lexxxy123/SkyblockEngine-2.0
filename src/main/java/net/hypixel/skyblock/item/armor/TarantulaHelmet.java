/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.md_5.bungee.api.ChatColor
 *  org.bukkit.entity.Player
 */
package net.hypixel.skyblock.item.armor;

import net.hypixel.skyblock.item.Ability;
import net.hypixel.skyblock.item.AbilityActivation;
import net.hypixel.skyblock.item.GenericItemType;
import net.hypixel.skyblock.item.Rarity;
import net.hypixel.skyblock.item.SItem;
import net.hypixel.skyblock.item.SpecificItemType;
import net.hypixel.skyblock.item.TickingMaterial;
import net.hypixel.skyblock.item.ToolStatistics;
import net.hypixel.skyblock.item.armor.LeatherArmorStatistics;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;

public class TarantulaHelmet
implements ToolStatistics,
TickingMaterial,
Ability,
LeatherArmorStatistics {
    @Override
    public String getAbilityName() {
        return ChatColor.GREEN + "Radioactive";
    }

    @Override
    public String getAbilityDescription() {
        return ChatColor.RED + "+1% Crit Damage " + ChatColor.GRAY + "per " + ChatColor.RED + "10 " + ChatColor.RED + "Strength";
    }

    @Override
    public int getAbilityCooldownTicks() {
        return 0;
    }

    @Override
    public AbilityActivation getAbilityActivation() {
        return AbilityActivation.NO_ACTIVATION;
    }

    @Override
    public void onAbilityUse(Player player, SItem sItem) {
    }

    @Override
    public int getManaCost() {
        return 0;
    }

    @Override
    public String getDisplayName() {
        return "Tarantula Helmet";
    }

    @Override
    public Rarity getRarity() {
        return Rarity.EPIC;
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
    public double getBaseDefense() {
        return 80.0;
    }

    @Override
    public double getBaseHealth() {
        return 100.0;
    }

    @Override
    public double getBaseSpeed() {
        return 0.0;
    }

    @Override
    public double getBaseIntelligence() {
        return 100.0;
    }

    @Override
    public int getColor() {
        return 0;
    }

    @Override
    public void tick(SItem item, Player owner) {
    }
}

