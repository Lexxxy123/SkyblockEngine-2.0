package in.godspunky.skyblock.item.oddities;

import in.godspunky.skyblock.item.*;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.ChatColor;
import in.godspunky.skyblock.item.*;
import in.godspunky.skyblock.util.SUtil;

import java.util.Collections;
import java.util.List;

public class BagOfCoins implements SkullStatistics, MaterialFunction, ItemData {
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
    public void onInstanceUpdate(final SItem instance) {
        final long coins = instance.getDataLong("coins");
        if (coins < 10000L) {
            instance.setRarity(Rarity.COMMON, false);
        } else if (coins < 100000L) {
            instance.setRarity(Rarity.UNCOMMON, false);
        } else if (coins < 250000L) {
            instance.setRarity(Rarity.RARE, false);
        } else if (coins < 4000000L) {
            instance.setRarity(Rarity.EPIC, false);
        } else if (coins < 10000000L) {
            instance.setRarity(Rarity.LEGENDARY, false);
        } else if (coins < 25000000L) {
            instance.setRarity(Rarity.MYTHIC, false);
        } else if (coins < 100000000L) {
            instance.setRarity(Rarity.SUPREME, false);
        } else if (coins < 500000000L) {
            instance.setRarity(Rarity.SPECIAL, false);
        } else {
            instance.setRarity(Rarity.VERY_SPECIAL, false);
        }
    }

    @Override
    public NBTTagCompound getData() {
        final NBTTagCompound compound = new NBTTagCompound();
        compound.setLong("coins", 1L);
        return compound;
    }

    @Override
    public List<String> getDataLore(final String key, final Object value) {
        if (!key.equals("coins")) {
            return null;
        }
        return Collections.singletonList(ChatColor.GOLD + "Contents: " + ChatColor.YELLOW + SUtil.commaify((long) value) + " coins");
    }

    @Override
    public String getURL() {
        return "8381c529d52e03cd74c3bf38bb6ba3fde1337ae9bf50332faa889e0a28e8081f";
    }
}
