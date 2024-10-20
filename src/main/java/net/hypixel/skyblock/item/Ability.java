/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 */
package net.hypixel.skyblock.item;

import net.hypixel.skyblock.features.requirement.AbstractRequirement;
import net.hypixel.skyblock.item.AbilityActivation;
import net.hypixel.skyblock.item.SItem;
import org.bukkit.entity.Player;

public interface Ability {
    public String getAbilityName();

    public String getAbilityDescription();

    default public void onAbilityUse(Player player, SItem sItem) {
    }

    public int getAbilityCooldownTicks();

    public int getManaCost();

    default public AbstractRequirement getRequirement() {
        return null;
    }

    default public AbilityActivation getAbilityActivation() {
        return AbilityActivation.RIGHT_CLICK;
    }

    default public boolean displayUsage() {
        return true;
    }

    default public boolean requirementsUse(Player player, SItem sItem) {
        return false;
    }

    default public String getAbilityReq() {
        return "";
    }

    default public boolean displayCooldown() {
        return true;
    }
}

