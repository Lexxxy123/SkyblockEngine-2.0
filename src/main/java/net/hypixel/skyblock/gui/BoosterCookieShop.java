/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.event.inventory.InventoryClickEvent
 *  org.bukkit.inventory.ItemStack
 */
package net.hypixel.skyblock.gui;

import net.hypixel.skyblock.gui.GUI;
import net.hypixel.skyblock.gui.GUIClickableItem;
import net.hypixel.skyblock.gui.GUIOpenEvent;
import net.hypixel.skyblock.gui.GUIType;
import net.hypixel.skyblock.user.User;
import net.hypixel.skyblock.util.SUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class BoosterCookieShop
extends GUI {
    public BoosterCookieShop() {
        super("Community Shop", 54);
    }

    @Override
    public void onOpen(GUIOpenEvent e) {
        this.fill(BLACK_STAINED_GLASS_PANE);
        final Player player = e.getPlayer();
        User user = User.getUser(player.getUniqueId());
        this.set(new GUIClickableItem(){

            @Override
            public void run(InventoryClickEvent e) {
            }

            @Override
            public int getSlot() {
                return 1;
            }

            @Override
            public ItemStack getItem() {
                return SUtil.getStack(ChatColor.GREEN + "City Project", Material.GOLD_BARDING, (short)0, 1, ChatColor.GRAY + "Participate with the whole", ChatColor.GRAY + "SkyBlock community to upgrade", ChatColor.GRAY + "the village and more.", " ", ChatColor.AQUA + "Contribute " + ChatColor.GRAY + "to various", ChatColor.GRAY + "project to obtain unique", ChatColor.GRAY + "perks.", "", ChatColor.YELLOW + "Click to view!");
            }
        });
        this.set(new GUIClickableItem(){

            @Override
            public void run(InventoryClickEvent e) {
            }

            @Override
            public int getSlot() {
                return 2;
            }

            @Override
            public ItemStack getItem() {
                return SUtil.getStack(ChatColor.LIGHT_PURPLE + "Account & Profile Upgrades", Material.HOPPER, (short)0, 1, ChatColor.GRAY + "Upgrade your current profile and", ChatColor.GRAY + "your SkyBlock account with", ChatColor.GRAY + "permanent upgrades.", " ", ChatColor.GRAY + "Profile: " + ChatColor.DARK_GRAY + "Nothing going on!", ChatColor.GRAY + "Account:" + ChatColor.DARK_GRAY + "None underway!", "", ChatColor.YELLOW + "Click to view!");
            }
        });
        this.set(new GUIClickableItem(){

            @Override
            public void run(InventoryClickEvent e) {
            }

            @Override
            public int getSlot() {
                return 3;
            }

            @Override
            public ItemStack getItem() {
                return SUtil.getStack(ChatColor.GOLD + "Booster Cookie", Material.COOKIE, (short)0, 1, ChatColor.GRAY + "Obtain a temporary buff", ChatColor.GRAY + "letting you earn " + ChatColor.AQUA + "bits", ChatColor.GRAY + "as well as " + ChatColor.LIGHT_PURPLE + "tons of perks.", " ", ChatColor.GREEN + "Currently selected!");
            }
        });
        this.set(new GUIClickableItem(){

            @Override
            public void run(InventoryClickEvent e) {
                GUIType.BIT_SHOP.getGUI().open(player);
            }

            @Override
            public int getSlot() {
                return 4;
            }

            @Override
            public ItemStack getItem() {
                return SUtil.getStack(ChatColor.AQUA + "Bits Shop", Material.DIAMOND, (short)0, 1, ChatColor.GRAY + "Spend " + ChatColor.AQUA + "bits" + ChatColor.GRAY + " on a variety of", ChatColor.GRAY + "powerful items.", "", ChatColor.GRAY + "Earn bits from " + ChatColor.GOLD + "Booster Cookie.", " ", ChatColor.YELLOW + "Click to view!");
            }
        });
        this.set(new GUIClickableItem(){

            @Override
            public void run(InventoryClickEvent e) {
                GUIType.FIRE_SALE.getGUI().open(player);
            }

            @Override
            public int getSlot() {
                return 5;
            }

            @Override
            public ItemStack getItem() {
                return SUtil.getStack(ChatColor.translateAlternateColorCodes((char)'&', (String)"&6\u2668 &c&lFIRE &cSales &6\u2668"), Material.BLAZE_POWDER, (short)0, 1, ChatColor.GRAY + "Acquire " + ChatColor.GOLD + "exclusive" + ChatColor.GRAY + "cosmetics", ChatColor.GRAY + "which are only available in", ChatColor.RED + "limited quantity" + ChatColor.GRAY + " across all", ChatColor.GRAY + "of SkyBlock", " ", ChatColor.DARK_GRAY + "No ongoing sale!", "", ChatColor.YELLOW + "Click to view!");
            }
        });
        this.set(new GUIClickableItem(){

            @Override
            public void run(InventoryClickEvent e) {
            }

            @Override
            public int getSlot() {
                return 7;
            }

            @Override
            public ItemStack getItem() {
                return SUtil.getStack(ChatColor.YELLOW + "Funpixel Ranks", Material.EMERALD, (short)0, 1, ChatColor.GRAY + "Browse the SkyBlock perks of", ChatColor.GRAY + "our " + ChatColor.YELLOW + "server-wide" + ChatColor.GRAY + "ranks such", ChatColor.GRAY + "as the " + ChatColor.GOLD + "[MVP" + ChatColor.RED + "++" + ChatColor.GOLD + "]" + ChatColor.GRAY + " rank.", ChatColor.GRAY + "of SkyBlock", " ", ChatColor.YELLOW + "Click to view!");
            }
        });
        this.set(new GUIClickableItem(){

            @Override
            public void run(InventoryClickEvent e) {
            }

            @Override
            public int getSlot() {
                return 10;
            }

            @Override
            public ItemStack getItem() {
                return SUtil.getStack(ChatColor.DARK_GRAY + "\u25b2 " + ChatColor.GRAY + "Categories", Material.STAINED_GLASS_PANE, (short)7, 1, ChatColor.DARK_GRAY + "\u25bc " + ChatColor.GRAY + "Items");
            }
        });
        this.set(new GUIClickableItem(){

            @Override
            public void run(InventoryClickEvent e) {
            }

            @Override
            public int getSlot() {
                return 11;
            }

            @Override
            public ItemStack getItem() {
                return SUtil.getStack(ChatColor.DARK_GRAY + "\u25b2 " + ChatColor.GRAY + "Categories", Material.STAINED_GLASS_PANE, (short)7, 1, ChatColor.DARK_GRAY + "\u25bc " + ChatColor.GRAY + "Items");
            }
        });
        this.set(new GUIClickableItem(){

            @Override
            public void run(InventoryClickEvent e) {
            }

            @Override
            public int getSlot() {
                return 13;
            }

            @Override
            public ItemStack getItem() {
                return SUtil.getStack(ChatColor.DARK_GRAY + "\u25b2 " + ChatColor.GRAY + "Categories", Material.STAINED_GLASS_PANE, (short)7, 1, ChatColor.DARK_GRAY + "\u25bc " + ChatColor.GRAY + "Items");
            }
        });
        this.set(new GUIClickableItem(){

            @Override
            public void run(InventoryClickEvent e) {
            }

            @Override
            public int getSlot() {
                return 14;
            }

            @Override
            public ItemStack getItem() {
                return SUtil.getStack(ChatColor.DARK_GRAY + "\u25b2 " + ChatColor.GRAY + "Categories", Material.STAINED_GLASS_PANE, (short)7, 1, ChatColor.DARK_GRAY + "\u25bc " + ChatColor.GRAY + "Items");
            }
        });
        this.set(new GUIClickableItem(){

            @Override
            public void run(InventoryClickEvent e) {
            }

            @Override
            public int getSlot() {
                return 16;
            }

            @Override
            public ItemStack getItem() {
                return SUtil.getStack(ChatColor.DARK_GRAY + "\u25b2 " + ChatColor.GRAY + "Categories", Material.STAINED_GLASS_PANE, (short)7, 1, ChatColor.DARK_GRAY + "\u25bc " + ChatColor.GRAY + "Items");
            }
        });
        this.set(new GUIClickableItem(){

            @Override
            public void run(InventoryClickEvent e) {
            }

            @Override
            public int getSlot() {
                return 12;
            }

            @Override
            public ItemStack getItem() {
                return SUtil.getStack(ChatColor.DARK_GRAY + "\u25b2 " + ChatColor.GRAY + "Categories", Material.STAINED_GLASS_PANE, (short)5, 1, ChatColor.DARK_GRAY + "\u25bc " + ChatColor.GRAY + "Items");
            }
        });
        this.set(new GUIClickableItem(){

            @Override
            public void run(InventoryClickEvent e) {
            }

            @Override
            public int getSlot() {
                return 29;
            }

            @Override
            public ItemStack getItem() {
                return SUtil.getStack(ChatColor.GOLD + "Single Cookie", Material.COOKIE, (short)0, 1, "", ChatColor.GOLD + "Booster Cookie " + ChatColor.DARK_GRAY + "x1", ChatColor.translateAlternateColorCodes((char)'&', (String)"&7Consume to gain the &dCookie Buff"), ChatColor.translateAlternateColorCodes((char)'&', (String)"&7for &b5 &7days:"), " ", ChatColor.translateAlternateColorCodes((char)'&', (String)"&8\u25ba &7Ability to gain &bBis&7!"), ChatColor.translateAlternateColorCodes((char)'&', (String)"&8\u25ba &b+20% &7 Skill XP"), ChatColor.translateAlternateColorCodes((char)'&', (String)"&8\u25ba &b+15 &7Magic Find"), ChatColor.translateAlternateColorCodes((char)'&', (String)"&8\u25ba &7Keep &6coins &7and &beffects &7on death"), ChatColor.translateAlternateColorCodes((char)'&', (String)"&8\u25ba &ePermafly &7 on private islands"), ChatColor.translateAlternateColorCodes((char)'&', (String)"&8\u25ba &7Access &6/ah &7and &6/bazaar &7 anywhere"), ChatColor.translateAlternateColorCodes((char)'&', (String)"&8\u25ba &7Sell items directly to the trades menu"), ChatColor.translateAlternateColorCodes((char)'&', (String)"&8\u25ba &7AFK &aimmunity &7on your island"), ChatColor.translateAlternateColorCodes((char)'&', (String)"&8\u25ba &7Toggle specific &dpotion effects"), ChatColor.translateAlternateColorCodes((char)'&', (String)"&8\u25ba &7Access to &6/anvil &7and &6/etable"), "", ChatColor.GOLD + "" + ChatColor.BOLD + "LEGENDARY", "", ChatColor.GRAY + "Cost", ChatColor.GREEN + "325 Skyblock Gems", "", ChatColor.GRAY + "You have: 0");
            }
        });
        this.set(new GUIClickableItem(){

            @Override
            public void run(InventoryClickEvent e) {
            }

            @Override
            public int getSlot() {
                return 31;
            }

            @Override
            public ItemStack getItem() {
                return SUtil.getStack(ChatColor.GOLD + "Hafl-Dozen Cookie", Material.COOKIE, (short)0, 1, "", ChatColor.GOLD + "Booster Cookie " + ChatColor.DARK_GRAY + "x6", ChatColor.translateAlternateColorCodes((char)'&', (String)"&7Consume to gain the &dCookie Buff"), ChatColor.translateAlternateColorCodes((char)'&', (String)"&7for &b5 &7days:"), " ", ChatColor.translateAlternateColorCodes((char)'&', (String)"&8\u25ba &7Ability to gain &bBis&7!"), ChatColor.translateAlternateColorCodes((char)'&', (String)"&8\u25ba &b+20% &7 Skill XP"), ChatColor.translateAlternateColorCodes((char)'&', (String)"&8\u25ba &b+15 &7Magic Find"), ChatColor.translateAlternateColorCodes((char)'&', (String)"&8\u25ba &7Keep &6coins &7and &beffects &7on death"), ChatColor.translateAlternateColorCodes((char)'&', (String)"&8\u25ba &ePermafly &7 on private islands"), ChatColor.translateAlternateColorCodes((char)'&', (String)"&8\u25ba &7Access &6/ah &7and &6/bazaar &7 anywhere"), ChatColor.translateAlternateColorCodes((char)'&', (String)"&8\u25ba &7Sell items directly to the trades menu"), ChatColor.translateAlternateColorCodes((char)'&', (String)"&8\u25ba &7AFK &aimmunity &7on your island"), ChatColor.translateAlternateColorCodes((char)'&', (String)"&8\u25ba &7Toggle specific &dpotion effects"), ChatColor.translateAlternateColorCodes((char)'&', (String)"&8\u25ba &7Access to &6/anvil &7and &6/etable"), "", ChatColor.GOLD + "" + ChatColor.BOLD + "LEGENDARY", "", ChatColor.GRAY + "Cost", ChatColor.GREEN + "1,375 Skyblock Gems", "", ChatColor.GRAY + "You have: 0");
            }
        });
        this.set(new GUIClickableItem(){

            @Override
            public void run(InventoryClickEvent e) {
            }

            @Override
            public int getSlot() {
                return 33;
            }

            @Override
            public ItemStack getItem() {
                return SUtil.getStack(ChatColor.GOLD + "A Dozen Cookie", Material.COOKIE, (short)0, 1, "", ChatColor.GOLD + "Booster Cookie " + ChatColor.DARK_GRAY + "x12", ChatColor.translateAlternateColorCodes((char)'&', (String)"&7Consume to gain the &dCookie Buff"), ChatColor.translateAlternateColorCodes((char)'&', (String)"&7for &b5 &7days:"), " ", ChatColor.translateAlternateColorCodes((char)'&', (String)"&8\u25ba &7Ability to gain &bBis&7!"), ChatColor.translateAlternateColorCodes((char)'&', (String)"&8\u25ba &b+20% &7 Skill XP"), ChatColor.translateAlternateColorCodes((char)'&', (String)"&8\u25ba &b+15 &7Magic Find"), ChatColor.translateAlternateColorCodes((char)'&', (String)"&8\u25ba &7Keep &6coins &7and &beffects &7on death"), ChatColor.translateAlternateColorCodes((char)'&', (String)"&8\u25ba &ePermafly &7 on private islands"), ChatColor.translateAlternateColorCodes((char)'&', (String)"&8\u25ba &7Access &6/ah &7and &6/bazaar &7 anywhere"), ChatColor.translateAlternateColorCodes((char)'&', (String)"&8\u25ba &7Sell items directly to the trades menu"), ChatColor.translateAlternateColorCodes((char)'&', (String)"&8\u25ba &7AFK &aimmunity &7on your island"), ChatColor.translateAlternateColorCodes((char)'&', (String)"&8\u25ba &7Toggle specific &dpotion effects"), ChatColor.translateAlternateColorCodes((char)'&', (String)"&8\u25ba &7Access to &6/anvil &7and &6/etable"), "", ChatColor.GOLD + "" + ChatColor.BOLD + "LEGENDARY", "", ChatColor.GRAY + "Cost", ChatColor.GREEN + "3,900 SkyBlock Gems", "", ChatColor.GRAY + "You have: 0");
            }
        });
        this.set(new GUIClickableItem(){

            @Override
            public void run(InventoryClickEvent e) {
                player.sendMessage(ChatColor.AQUA + "https://store.funpixel.xyz/");
            }

            @Override
            public int getSlot() {
                return 49;
            }

            @Override
            public ItemStack getItem() {
                return SUtil.getStack(ChatColor.GREEN + "Community Shop", Material.EMERALD, (short)0, 1, "", ChatColor.DARK_GRAY + "Elizabeth", "", ChatColor.translateAlternateColorCodes((char)'&', (String)"&7Gems: "), ChatColor.translateAlternateColorCodes((char)'&', (String)"&8Purchase on store.funpixel.xyz"), " ", ChatColor.translateAlternateColorCodes((char)'&', (String)"&7Bits: &bN/A"), ChatColor.translateAlternateColorCodes((char)'&', (String)"&8Earn from Booster Cookies!"), "", ChatColor.translateAlternateColorCodes((char)'&', (String)"&7Fame Rank: &eNew player"), ChatColor.translateAlternateColorCodes((char)'&', (String)"&8rank up by spending gems &"), ChatColor.translateAlternateColorCodes((char)'&', (String)"&8bits!"), "", ChatColor.GRAY + "Store: " + ChatColor.AQUA + "store.funpixel.xyz", ChatColor.YELLOW + "Click to get link!");
            }
        });
    }
}

