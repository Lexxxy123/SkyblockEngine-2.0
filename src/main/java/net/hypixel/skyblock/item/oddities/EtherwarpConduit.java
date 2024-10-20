/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 */
package net.hypixel.skyblock.item.oddities;

import net.hypixel.skyblock.item.Ability;
import net.hypixel.skyblock.item.GenericItemType;
import net.hypixel.skyblock.item.MaterialFunction;
import net.hypixel.skyblock.item.Rarity;
import net.hypixel.skyblock.item.SItem;
import net.hypixel.skyblock.item.SkullStatistics;
import net.hypixel.skyblock.util.Sputnik;
import org.bukkit.entity.Player;

public class EtherwarpConduit
implements SkullStatistics,
MaterialFunction,
Ability {
    @Override
    public String getURL() {
        return "ca70e0206f6166048441dfe08e053a6017d914f35c6b1fb0f558c50574f970d0";
    }

    @Override
    public String getDisplayName() {
        return "Etherwarp Conduit";
    }

    @Override
    public Rarity getRarity() {
        return Rarity.EPIC;
    }

    @Override
    public GenericItemType getType() {
        return GenericItemType.ITEM;
    }

    @Override
    public String getAbilityName() {
        return "Ether Transmission";
    }

    @Override
    public String getAbilityDescription() {
        return Sputnik.trans("&7Teleport to your targetted block up to &a57 blocks &7away.");
    }

    @Override
    public int getAbilityCooldownTicks() {
        return 0;
    }

    @Override
    public int getManaCost() {
        return 250;
    }

    @Override
    public boolean displayUsage() {
        return true;
    }

    @Override
    public boolean isStackable() {
        return false;
    }

    @Override
    public boolean requirementsUse(Player player, SItem sItem) {
        return !SItem.isAbleToDoEtherWarpTeleportation(player, sItem);
    }

    @Override
    public String getAbilityReq() {
        return "&cNo block in range!";
    }

    @Override
    public void onAbilityUse(Player player, SItem sItem) {
        SItem.etherWarpTeleportation(player, sItem);
    }
}

