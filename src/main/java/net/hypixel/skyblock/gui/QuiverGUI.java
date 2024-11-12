/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.Sound
 *  org.bukkit.entity.Player
 *  org.bukkit.event.inventory.InventoryClickEvent
 *  org.bukkit.event.inventory.InventoryCloseEvent
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.ItemStack
 */
package net.hypixel.skyblock.gui;

import java.util.Map;
import net.hypixel.skyblock.SkyBlock;
import net.hypixel.skyblock.gui.GUI;
import net.hypixel.skyblock.gui.GUIClickableItem;
import net.hypixel.skyblock.gui.GUIOpenEvent;
import net.hypixel.skyblock.gui.GUIType;
import net.hypixel.skyblock.item.SItem;
import net.hypixel.skyblock.item.SMaterial;
import net.hypixel.skyblock.user.PlayerUtils;
import net.hypixel.skyblock.user.User;
import net.hypixel.skyblock.util.SUtil;
import net.hypixel.skyblock.util.Sputnik;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class QuiverGUI
extends GUI {
    public QuiverGUI() {
        super("Quiver", 36);
        this.fill(BLACK_STAINED_GLASS_PANE, 27, 35);
        this.set(GUIClickableItem.getCloseItem(31));
    }

    @Override
    public void onOpen(GUIOpenEvent e2) {
        final Player player = e2.getPlayer();
        this.set(GUIClickableItem.createGUIOpenerItem(GUIType.SKYBLOCK_MENU, e2.getPlayer(), ChatColor.GREEN + "Go Back", 30, Material.ARROW, new String[0]));
        User user = User.getUser(e2.getPlayer().getUniqueId());
        Inventory inventory = e2.getInventory();
        for (Map.Entry<SMaterial, Integer> entry : user.getQuiver().entrySet()) {
            inventory.addItem(new ItemStack[]{SUtil.setStackAmount(SItem.of(entry.getKey()).getStack(), entry.getValue())});
        }
        this.set(new GUIClickableItem(){

            @Override
            public void run(InventoryClickEvent e2) {
                Player p2 = (Player)e2.getWhoClicked();
                if (p2 == null) {
                    return;
                }
                if (PlayerUtils.getCookieDurationTicks(p2) <= 0L) {
                    p2.sendMessage(Sputnik.trans("&cYou need the Cookie Buff active to use this feature!"));
                    p2.playSound(p2.getLocation(), Sound.VILLAGER_NO, 1.0f, 1.0f);
                    return;
                }
                for (int a2 = 0; a2 < 27; ++a2) {
                    e2.getInventory().setItem(a2, null);
                }
                p2.closeInventory();
                p2.sendMessage(Sputnik.trans("&aSuccessfully cleared your Quiver!"));
                p2.playSound(p2.getLocation(), Sound.LEVEL_UP, 1.0f, 1.0f);
            }

            @Override
            public int getSlot() {
                return 34;
            }

            @Override
            public ItemStack getItem() {
                ItemStack isBuilder = new ItemStack(Material.BEDROCK, 1);
                String a2 = ChatColor.YELLOW + "Click to proceed";
                if (PlayerUtils.getCookieDurationTicks(player) <= 0L) {
                    a2 = ChatColor.RED + "Requires Cookie Buff!";
                }
                isBuilder = SUtil.getStack(ChatColor.RED + "Clear Quiver", Material.LAVA_BUCKET, (short)0, 1, ChatColor.GRAY + "Click to clear your Quiver", ChatColor.GRAY + "instantly.", " ", a2);
                return isBuilder;
            }
        });
        this.set(new GUIClickableItem(){

            @Override
            public void run(InventoryClickEvent e2) {
                Player p2 = (Player)e2.getWhoClicked();
                if (p2 == null) {
                    return;
                }
                if (PlayerUtils.getCookieDurationTicks(p2) <= 0L) {
                    p2.sendMessage(Sputnik.trans("&cYou need the Cookie Buff active to use this feature!"));
                    p2.playSound(p2.getLocation(), Sound.VILLAGER_NO, 1.0f, 1.0f);
                    return;
                }
                for (int a2 = 0; a2 < 27; ++a2) {
                    ItemStack arrow = SItem.of(SMaterial.ARROW).getStack();
                    arrow.setAmount(64);
                    e2.getInventory().setItem(a2, arrow);
                }
                p2.closeInventory();
                p2.sendMessage(Sputnik.trans("&aSuccessfully filled your Quiver!"));
                p2.playSound(p2.getLocation(), Sound.LEVEL_UP, 1.0f, 1.0f);
            }

            @Override
            public int getSlot() {
                return 35;
            }

            @Override
            public ItemStack getItem() {
                ItemStack isBuilder = new ItemStack(Material.BEDROCK, 1);
                String a2 = ChatColor.YELLOW + "Click to proceed";
                if (PlayerUtils.getCookieDurationTicks(player) <= 0L) {
                    a2 = ChatColor.RED + "Requires Cookie Buff!";
                }
                isBuilder = SUtil.getStack(ChatColor.GREEN + "Fill Quiver", Material.CHEST, (short)0, 1, ChatColor.GRAY + "Click to fill your Quiver", ChatColor.GRAY + "instantly.", " ", a2);
                return isBuilder;
            }
        });
    }

    @Override
    public void onClose(InventoryCloseEvent e2) {
        User user = User.getUser(e2.getPlayer().getUniqueId());
        Inventory inventory = e2.getInventory();
        user.clearQuiver();
        for (int i2 = 0; i2 < 27; ++i2) {
            ItemStack stack = inventory.getItem(i2);
            SItem sItem = SItem.find(stack);
            if (sItem == null && (sItem = SItem.of(stack)) == null) continue;
            user.addToQuiver(sItem.getType(), stack.getAmount());
        }
        if (SkyBlock.getPlugin().config.getBoolean("Config")) {
            user.configsave();
        } else {
            user.save();
        }
    }
}

