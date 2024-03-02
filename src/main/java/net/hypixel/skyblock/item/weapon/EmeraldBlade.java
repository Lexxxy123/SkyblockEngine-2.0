package net.hypixel.skyblock.item.weapon;

import net.hypixel.skyblock.item.*;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import net.hypixel.skyblock.item.*;
import net.hypixel.skyblock.user.User;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class EmeraldBlade implements ToolStatistics, MaterialFunction, Ownable {
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
    public List<String> getDataLore(final String key, final Object value) {
        if (!key.equals("owner")) {
            return null;
        }
        final Player player = Bukkit.getPlayer(UUID.fromString(String.valueOf(value)));
        if (player == null) {
            return null;
        }
        final User user = User.getUser(player.getUniqueId());
        final long cap = 35000000000L;
        final double d1 = Math.pow((double) Math.min(cap, User.getUser(player.getUniqueId()).getCoins()), 0.25);
        final double finald = 2.5 * d1;
        final double dmgfin = Math.round(finald / 10.0) * 10.0;
        return Collections.singletonList(ChatColor.GRAY + "Current Damage Bonus: " + ChatColor.GREEN + dmgfin);
    }

    @Override
    public NBTTagCompound getData() {
        return new NBTTagCompound();
    }
}
