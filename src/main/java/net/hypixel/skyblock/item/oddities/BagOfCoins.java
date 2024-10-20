/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraft.server.v1_8_R3.NBTTagCompound
 *  org.bukkit.ChatColor
 */
package net.hypixel.skyblock.item.oddities;

import java.util.Collections;
import java.util.List;
import net.hypixel.skyblock.item.GenericItemType;
import net.hypixel.skyblock.item.ItemData;
import net.hypixel.skyblock.item.MaterialFunction;
import net.hypixel.skyblock.item.Rarity;
import net.hypixel.skyblock.item.SItem;
import net.hypixel.skyblock.item.SkullStatistics;
import net.hypixel.skyblock.util.SUtil;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.ChatColor;

public class BagOfCoins
implements SkullStatistics,
MaterialFunction,
ItemData {
    @Override
    public String getDisplayName() {
        return "Bag of Coins";
    }

    @Override
    public Rarity getRarity() {
        return Rarity.COMMON;
    }

    @Override
    public GenericItemType getType() {
        return GenericItemType.ITEM;
    }

    @Override
    public void onInstanceUpdate(SItem instance) {
        long coins = instance.getDataLong("coins");
        if (10000L > coins) {
            instance.setRarity(Rarity.COMMON, false);
        } else if (100000L > coins) {
            instance.setRarity(Rarity.UNCOMMON, false);
        } else if (250000L > coins) {
            instance.setRarity(Rarity.RARE, false);
        } else if (4000000L > coins) {
            instance.setRarity(Rarity.EPIC, false);
        } else if (10000000L > coins) {
            instance.setRarity(Rarity.LEGENDARY, false);
        } else if (25000000L > coins) {
            instance.setRarity(Rarity.MYTHIC, false);
        } else if (100000000L > coins) {
            instance.setRarity(Rarity.SUPREME, false);
        } else if (500000000L > coins) {
            instance.setRarity(Rarity.SPECIAL, false);
        } else {
            instance.setRarity(Rarity.VERY_SPECIAL, false);
        }
    }

    @Override
    public NBTTagCompound getData() {
        NBTTagCompound compound = new NBTTagCompound();
        compound.setLong("coins", 1L);
        return compound;
    }

    @Override
    public List<String> getDataLore(String key, Object value) {
        if (!key.equals("coins")) {
            return null;
        }
        return Collections.singletonList(ChatColor.GOLD + "Contents: " + ChatColor.YELLOW + SUtil.commaify((Long)value) + " coins");
    }

    @Override
    public String getURL() {
        return "8381c529d52e03cd74c3bf38bb6ba3fde1337ae9bf50332faa889e0a28e8081f";
    }
}

