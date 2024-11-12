/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.World
 *  org.bukkit.entity.Player
 */
package net.hypixel.skyblock.item.weapon;

import net.hypixel.skyblock.item.Ability;
import net.hypixel.skyblock.item.GenericItemType;
import net.hypixel.skyblock.item.MaterialFunction;
import net.hypixel.skyblock.item.Rarity;
import net.hypixel.skyblock.item.SItem;
import net.hypixel.skyblock.item.SpecificItemType;
import net.hypixel.skyblock.item.ToolStatistics;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class TestItemAbi
implements ToolStatistics,
MaterialFunction,
Ability {
    @Override
    public String getAbilityName() {
        return "Admin Item!";
    }

    @Override
    public String getAbilityDescription() {
        return "Active";
    }

    @Override
    public int getAbilityCooldownTicks() {
        return 0;
    }

    @Override
    public void onAbilityUse(Player player, SItem sItem) {
        SItem.etherWarpTeleportation(player, sItem);
    }

    public void cylinder(Location loc, int r2) {
        int cx = loc.getBlockX();
        int cy = loc.getBlockY();
        int cz = loc.getBlockZ();
        World w2 = loc.getWorld();
        int rSquared = r2 * r2;
        for (int x2 = cx - r2; x2 <= cx + r2; ++x2) {
            for (int z2 = cz - r2; z2 <= cz + r2; ++z2) {
                if ((cx - x2) * (cx - x2) + (cz - z2) * (cz - z2) > rSquared) continue;
                Location l2 = new Location(w2, (double)x2, (double)cy, (double)z2);
                this.sendPacket(w2, l2);
            }
        }
    }

    public void sendPacket(World w2, Location l2) {
        for (Player p2 : w2.getPlayers()) {
            p2.sendBlockChange(l2, Material.BEDROCK, (byte)0);
        }
    }

    @Override
    public int getManaCost() {
        return 0;
    }

    @Override
    public String getDisplayName() {
        return "Test Item";
    }

    @Override
    public int getBaseDamage() {
        return 1;
    }

    @Override
    public Rarity getRarity() {
        return Rarity.EXCLUSIVE;
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

