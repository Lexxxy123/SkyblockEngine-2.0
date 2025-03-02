/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraft.server.v1_8_R3.NBTTagCompound
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.entity.Player
 */
package net.hypixel.skyblock.item.weapon;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import net.hypixel.skyblock.item.GenericItemType;
import net.hypixel.skyblock.item.MaterialFunction;
import net.hypixel.skyblock.item.Ownable;
import net.hypixel.skyblock.item.Rarity;
import net.hypixel.skyblock.item.SpecificItemType;
import net.hypixel.skyblock.item.ToolStatistics;
import net.hypixel.skyblock.user.User;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class EmeraldBlade
implements ToolStatistics,
MaterialFunction,
Ownable {
    @Override
    public String getDisplayName() {
        return "Emerald Blade";
    }

    @Override
    public Rarity getRarity() {
        return Rarity.EPIC;
    }

    @Override
    public GenericItemType getType() {
        return GenericItemType.WEAPON;
    }

    @Override
    public SpecificItemType getSpecificType() {
        return SpecificItemType.SWORD;
    }

    @Override
    public int getBaseDamage() {
        return 130;
    }

    @Override
    public List<String> getListLore() {
        return Arrays.asList("A powerful blade made from pure", ChatColor.DARK_GREEN + "Emeralds" + ChatColor.GRAY + ". This blade becomes", "stronger as you carry more", ChatColor.GOLD + "coins" + ChatColor.GRAY + " in your purse.");
    }

    @Override
    public List<String> getDataLore(String key, Object value) {
        if (!key.equals("owner")) {
            return null;
        }
        Player player = Bukkit.getPlayer((UUID)UUID.fromString(String.valueOf(value)));
        if (player == null) {
            return null;
        }
        User user = User.getUser(player.getUniqueId());
        long cap = 35000000000L;
        double d1 = Math.pow(Math.min(35000000000L, User.getUser(player.getUniqueId()).getCoins()), 0.25);
        double finald = 2.5 * d1;
        double dmgfin = (double)Math.round(finald / 10.0) * 10.0;
        return Collections.singletonList(ChatColor.GRAY + "Current Damage Bonus: " + ChatColor.GREEN + dmgfin);
    }

    @Override
    public NBTTagCompound getData() {
        return new NBTTagCompound();
    }
}

