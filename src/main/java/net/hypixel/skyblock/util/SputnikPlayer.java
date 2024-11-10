/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.md_5.bungee.api.ChatColor
 *  net.minecraft.server.v1_8_R3.EntityHuman
 *  net.minecraft.server.v1_8_R3.ItemStack
 *  net.minecraft.server.v1_8_R3.NBTBase
 *  net.minecraft.server.v1_8_R3.NBTTagCompound
 *  net.minecraft.server.v1_8_R3.NBTTagInt
 *  org.bukkit.Material
 *  org.bukkit.craftbukkit.v1_8_R3.entity.CraftHumanEntity
 *  org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 */
package net.hypixel.skyblock.util;

import de.tr7zw.nbtapi.NBTItem;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.NBTBase;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTTagInt;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SputnikPlayer {
    public static final Map<UUID, Integer> AbsHP = new HashMap<UUID, Integer>();

    public static void sendTranslated(Player p, String content) {
        p.sendMessage(ChatColor.translateAlternateColorCodes((char)'&', (String)content));
    }

    public static void BonemerangFix(Player player) {
        for (int i = 0; i < player.getInventory().getSize(); ++i) {
            NBTItem nbti;
            ItemStack stack = player.getInventory().getItem(i);
            if (stack == null || !(nbti = new NBTItem(stack)).hasKey("ejectedBonemerang").booleanValue() || nbti.getInteger("ejectedBonemerang") != 1) continue;
            net.minecraft.server.v1_8_R3.ItemStack tagStack = CraftItemStack.asNMSCopy((ItemStack)stack);
            NBTTagCompound tagCompound = tagStack.hasTag() ? tagStack.getTag() : new NBTTagCompound();
            tagCompound.set("ejectedBonemerang", (NBTBase)new NBTTagInt(0));
            tagStack.setTag(tagCompound);
            ItemStack itemStack = CraftItemStack.asBukkitCopy((net.minecraft.server.v1_8_R3.ItemStack)tagStack);
            if (tagStack.getTag().getInt("ejectedBonemerang") != 0) continue;
            itemStack.setType(Material.BONE);
            player.getInventory().setItem(i, itemStack);
        }
    }

    public static void KatanasFix(Player player) {
        for (int i = 0; i < player.getInventory().getSize(); ++i) {
            NBTItem nbti;
            ItemStack stack = player.getInventory().getItem(i);
            if (stack == null || !(nbti = new NBTItem(stack)).hasKey("isGoldSword").booleanValue() || nbti.getInteger("isGoldSword") != 1) continue;
            net.minecraft.server.v1_8_R3.ItemStack tagStack = CraftItemStack.asNMSCopy((ItemStack)stack);
            NBTTagCompound tagCompound = tagStack.hasTag() ? tagStack.getTag() : new NBTTagCompound();
            tagCompound.set("isGoldSword", (NBTBase)new NBTTagInt(0));
            tagStack.setTag(tagCompound);
            ItemStack itemStack = CraftItemStack.asBukkitCopy((net.minecraft.server.v1_8_R3.ItemStack)tagStack);
            if (tagStack.getTag().getInt("isGoldSword") != 0) continue;
            itemStack.setType(Material.DIAMOND_SWORD);
            player.getInventory().setItem(i, itemStack);
        }
    }

    public static void setCustomAbsorptionHP(Player p, float amount) {
        if (!AbsHP.containsKey(p.getUniqueId())) {
            AbsHP.put(p.getUniqueId(), 0);
        }
        AbsHP.put(p.getUniqueId(), Math.round(amount));
    }

    public static void minusCustomAbsorptionHP(Player p, float amount) {
        if (!AbsHP.containsKey(p.getUniqueId())) {
            AbsHP.put(p.getUniqueId(), 0);
        }
        if (AbsHP.get(p.getUniqueId()) == 0) {
            return;
        }
        AbsHP.put(p.getUniqueId(), AbsHP.get(p.getUniqueId()) - Math.round(amount));
    }

    public static Integer getCustomAbsorptionHP(Player p) {
        if (!AbsHP.containsKey(p.getUniqueId())) {
            AbsHP.put(p.getUniqueId(), 0);
        }
        return AbsHP.get(p.getUniqueId());
    }

    public static void updateScaledAHP(Player p) {
        EntityHuman human = ((CraftHumanEntity)p).getHandle();
        if (!AbsHP.containsKey(p.getUniqueId())) {
            AbsHP.put(p.getUniqueId(), 0);
        }
        if (AbsHP.get(p.getUniqueId()) == 0) {
            human.setAbsorptionHearts(0.0f);
        }
        Integer absHP = AbsHP.get(p.getUniqueId());
        human.setAbsorptionHearts((float)Math.min(20.0, (double)((int)Math.round(0.05 * (double)absHP.intValue()))));
        if (AbsHP.get(p.getUniqueId()) == 0) {
            human.setAbsorptionHearts(0.0f);
        }
    }
}

