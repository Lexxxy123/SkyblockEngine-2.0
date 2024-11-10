/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.Color
 *  org.bukkit.Material
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.inventory.ItemFlag
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.inventory.meta.LeatherArmorMeta
 */
package net.hypixel.skyblock.util;

import de.tr7zw.nbtapi.NBTItem;
import java.util.ArrayList;
import java.util.List;
import net.hypixel.skyblock.util.SUtil;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class ItemBuilder {
    private ItemStack item;
    private final ItemMeta meta;

    public ItemBuilder() {
        this.item = new ItemStack(Material.DIRT);
        this.meta = this.item.getItemMeta();
    }

    public ItemBuilder(ItemStack stack) {
        this.item = stack;
        this.meta = this.item.getItemMeta();
    }

    public ItemBuilder(String name) {
        this.item = new ItemStack(Material.DIRT);
        this.meta = this.item.getItemMeta();
        this.setDisplayName(name);
    }

    public ItemBuilder(Material material) {
        this.item = new ItemStack(material);
        this.meta = this.item.getItemMeta();
    }

    public ItemBuilder(String name, int amount) {
        this.item = new ItemStack(Material.DIRT, amount);
        this.meta = this.item.getItemMeta();
        this.setDisplayName(name);
    }

    public ItemBuilder(String name, Material material) {
        this.item = new ItemStack(material);
        this.meta = this.item.getItemMeta();
        this.setDisplayName(name);
    }

    public ItemBuilder(String name, Material material, short dmg) {
        this.item = new ItemStack(material, 1, dmg);
        this.meta = this.item.getItemMeta();
        this.setDisplayName(name);
    }

    public ItemBuilder(String name, Material material, int amount) {
        this.item = new ItemStack(material, amount);
        this.meta = this.item.getItemMeta();
        this.setDisplayName(name);
    }

    public ItemBuilder(String name, Material material, int amount, short damage) {
        this.item = new ItemStack(material, amount, damage);
        this.meta = this.item.getItemMeta();
        this.setDisplayName(name);
    }

    public ItemBuilder setDamage(int damage) {
        this.item.setDurability((short)damage);
        return this;
    }

    public ItemBuilder setDamage(short damage) {
        this.item.setDurability(damage);
        return this;
    }

    public ItemBuilder setAmount(int amount) {
        this.item.setAmount(amount);
        return this;
    }

    public ItemBuilder setDisplayName(String name) {
        this.meta.setDisplayName(ChatColor.translateAlternateColorCodes((char)'&', (String)name));
        this.item.setItemMeta(this.meta);
        return this;
    }

    public ItemBuilder setMaterial(Material material) {
        this.item.setType(material);
        return this;
    }

    public ItemBuilder setLore(List<String> lore) {
        this.meta.setLore(lore);
        this.item.setItemMeta(this.meta);
        return this;
    }

    public ItemBuilder setLore(String ... lore) {
        ArrayList<String> ls = new ArrayList<String>();
        for (String s : lore) {
            ls.add(ChatColor.translateAlternateColorCodes((char)'&', (String)s));
        }
        this.meta.setLore(ls);
        this.item.setItemMeta(this.meta);
        return this;
    }

    public ItemBuilder addLore(List<String> lore) {
        ArrayList<String> ls = this.meta.getLore() != null ? this.meta.getLore() : new ArrayList<String>();
        ls.addAll(lore);
        this.meta.setLore(ls);
        this.item.setItemMeta(this.meta);
        return this;
    }

    public ItemBuilder addLore(String lore) {
        ArrayList<String> ls = this.meta.getLore() != null ? this.meta.getLore() : new ArrayList<String>();
        ls.add(ChatColor.translateAlternateColorCodes((char)'&', (String)lore));
        this.meta.setLore(ls);
        this.item.setItemMeta(this.meta);
        return this;
    }

    public ItemBuilder addLore(String ... lore) {
        ArrayList<String> ls = this.meta.getLore() != null ? this.meta.getLore() : new ArrayList<String>();
        for (String s : lore) {
            ls.add(ChatColor.translateAlternateColorCodes((char)'&', (String)s));
        }
        this.meta.setLore(ls);
        this.item.setItemMeta(this.meta);
        return this;
    }

    public ItemBuilder addEnchantment(Enchantment enchantment) {
        this.meta.addEnchant(enchantment, 1, false);
        this.item.setItemMeta(this.meta);
        return this;
    }

    public ItemBuilder addEnchantment(Enchantment enchantment, int level) {
        this.meta.addEnchant(enchantment, level, false);
        this.item.setItemMeta(this.meta);
        return this;
    }

    public ItemBuilder addEnchantment(Enchantment enchantment, int level, boolean ignoreMaxLevel) {
        this.meta.addEnchant(enchantment, level, ignoreMaxLevel);
        this.item.setItemMeta(this.meta);
        return this;
    }

    public ItemBuilder removeEnchantment(Enchantment enchantment) {
        this.meta.removeEnchant(enchantment);
        this.item.setItemMeta(this.meta);
        return this;
    }

    public ItemBuilder addEnchantmentGlint() {
        this.meta.addEnchant(Enchantment.LUCK, 1, false);
        this.meta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ENCHANTS});
        this.item.setItemMeta(this.meta);
        return this;
    }

    public ItemBuilder removeEnchantmentGlint() {
        this.meta.removeEnchant(Enchantment.LUCK);
        this.item.setItemMeta(this.meta);
        return this;
    }

    public ItemBuilder addItemFlag(ItemFlag itemFlag) {
        this.meta.addItemFlags(new ItemFlag[]{itemFlag});
        this.item.setItemMeta(this.meta);
        return this;
    }

    public ItemBuilder addItemFlags(ItemFlag ... itemFlags) {
        this.meta.addItemFlags(itemFlags);
        this.item.setItemMeta(this.meta);
        return this;
    }

    public ItemBuilder removeItemFlag(ItemFlag itemFlag) {
        this.meta.removeItemFlags(new ItemFlag[]{itemFlag});
        this.item.setItemMeta(this.meta);
        return this;
    }

    public ItemBuilder removeItemFlags(ItemFlag ... itemFlags) {
        this.meta.removeItemFlags(itemFlags);
        this.item.setItemMeta(this.meta);
        return this;
    }

    public ItemBuilder dyeColor(Color color) {
        LeatherArmorMeta leather = (LeatherArmorMeta)this.meta;
        leather.setColor(color);
        this.item.setItemMeta((ItemMeta)leather);
        return this;
    }

    public ItemBuilder addNBT(String key, String value) {
        NBTItem nbt = new NBTItem(this.item);
        nbt.setString(key, value);
        this.item = nbt.getItem();
        return this;
    }

    public ItemBuilder addNBT(String key, int value) {
        NBTItem nbt = new NBTItem(this.item);
        nbt.setInteger(key, value);
        this.item = nbt.getItem();
        return this;
    }

    public ItemBuilder addNBT(String key, double value) {
        NBTItem nbt = new NBTItem(this.item);
        nbt.setDouble(key, value);
        this.item = nbt.getItem();
        return this;
    }

    public ItemBuilder addNBT(String key, boolean value) {
        NBTItem nbt = new NBTItem(this.item);
        nbt.setBoolean(key, value);
        this.item = nbt.getItem();
        return this;
    }

    public ItemBuilder setSkullID(String id) {
        this.item = SUtil.idToSkull(this.item, id);
        return this;
    }

    public ItemStack toItemStack() {
        return this.item;
    }
}

