package net.hypixel.skyblock.item.oddities;

import net.hypixel.skyblock.item.*;
import net.hypixel.skyblock.features.potion.PotionEffect;
import net.hypixel.skyblock.features.potion.PotionEffectType;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.ChatColor;
import net.hypixel.skyblock.util.SUtil;

import java.util.ArrayList;
import java.util.List;

public class WaterBottle implements MaterialStatistics, MaterialFunction, ItemData {
    @Override
    public NBTTagCompound getData() {
        final NBTTagCompound compound = new NBTTagCompound();
        compound.setBoolean("splash", false);
        return compound;
    }

    @Override
    public String getDisplayName() {
        return "Water Bottle";
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
    public List<String> getDataLore(final String key, final Object value) {
        if (!key.equals("effects")) {
            return null;
        }
        final NBTTagCompound compound = (NBTTagCompound) value;
        final List<String> lore = new ArrayList<String>();
        for (final String k : compound.c()) {
            lore.add(" ");
            final NBTTagCompound effectData = compound.getCompound(k);
            final PotionEffectType type = PotionEffectType.getByNamespace(k);
            final int level = effectData.getInt("level");
            final long duration = effectData.getLong("duration");
            final PotionEffect effect = new PotionEffect(type, level, duration);
            lore.add(type.getName() + " " + SUtil.toRomanNumeral(effect.getLevel()) + (effect.getType().isInstant() ? "" : (ChatColor.WHITE + " (" + effect.getDurationDisplay() + ")")));
            for (final String line : SUtil.splitByWordAndLength(effect.getDescription(), 30, "\\s")) {
                lore.add(ChatColor.GRAY + line);
            }
        }
        return lore;
    }

    @Override
    public void onInstanceUpdate(final SItem instance) {
        int max = 0;
        for (final PotionEffect effect : instance.getPotionEffects()) {
            if (effect.getLevel() > max) {
                max = effect.getLevel();
            }
        }
        instance.setRarity(SUtil.findPotionRarity(max), false);
        if (instance.getPotionEffects().size() == 1) {
            instance.setDisplayName(ChatColor.stripColor(instance.getPotionEffects().get(0).getType().getName() + " " + SUtil.toRomanNumeral(instance.getPotionEffects().get(0).getLevel())) + (instance.isSplashPotion() ? " Splash" : "") + " Potion");
        }
        if (instance.getPotionEffects().size() > 1) {
            instance.setDisplayName((instance.isSplashPotion() ? "Splash " : "") + "Potion");
        }
        if (instance.getPotionEffects().size() == 0) {
            instance.setDisplayName("Water Bottle");
        }
    }
}
