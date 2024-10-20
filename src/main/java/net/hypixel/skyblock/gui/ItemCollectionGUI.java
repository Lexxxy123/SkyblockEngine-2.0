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

import java.util.ArrayList;
import java.util.Arrays;
import net.hypixel.skyblock.features.collection.ItemCollection;
import net.hypixel.skyblock.features.collection.ItemCollectionReward;
import net.hypixel.skyblock.features.collection.ItemCollectionRewards;
import net.hypixel.skyblock.gui.CategoryCollectionGUI;
import net.hypixel.skyblock.gui.GUI;
import net.hypixel.skyblock.gui.GUIClickableItem;
import net.hypixel.skyblock.gui.GUIOpenEvent;
import net.hypixel.skyblock.user.User;
import net.hypixel.skyblock.util.SUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class ItemCollectionGUI
extends GUI {
    private final ItemCollection collection;

    public ItemCollectionGUI(ItemCollection collection) {
        super(collection.getName() + " Collection", 54);
        this.collection = collection;
    }

    @Override
    public void onOpen(GUIOpenEvent e2) {
        int t2;
        this.fill(BLACK_STAINED_GLASS_PANE);
        Player player = e2.getPlayer();
        User user = User.getUser(player.getUniqueId());
        int amount = user.getCollection(this.collection);
        int tier = this.collection.getTier(amount);
        this.set(4, SUtil.getStack(ChatColor.YELLOW + this.collection.getName() + " " + SUtil.toRomanNumeral(tier), this.collection.getMaterial().getCraftMaterial(), this.collection.getData(), 1, ChatColor.GRAY + "View all your " + this.collection.getName() + " Collection", ChatColor.GRAY + "progress and rewards!", " ", ChatColor.GRAY + "Total Collected: " + ChatColor.YELLOW + SUtil.commaify(amount)));
        this.set(GUIClickableItem.getCloseItem(49));
        this.set(GUIClickableItem.createGUIOpenerItem(new CategoryCollectionGUI(this.collection.getCategory()), player, ChatColor.GREEN + "Go Back", 48, Material.ARROW, (short)0, ChatColor.GRAY + "To " + this.collection.getCategory().getName() + " Collection"));
        int i2 = 0;
        int slot = 18;
        while (i2 < this.collection.getRewards().size() && (t2 = i2 + 1) != 28) {
            ItemCollectionRewards rewards = this.collection.getRewards().get(i2);
            if (rewards != null) {
                final int finalSlot = slot;
                ChatColor color = ChatColor.RED;
                short data = 14;
                if (amount >= rewards.getRequirement()) {
                    color = ChatColor.GREEN;
                    data = 5;
                }
                if (tier + 1 == t2) {
                    color = ChatColor.YELLOW;
                    data = 4;
                }
                final ChatColor finalColor = color;
                final short finalData = data;
                final ArrayList<String> lore = new ArrayList<String>(Arrays.asList(" ", SUtil.createProgressText("Progress", amount, rewards.getRequirement()), SUtil.createLineProgressBar(20, ChatColor.DARK_GREEN, amount, rewards.getRequirement()), " "));
                if (rewards.size() != 0) {
                    lore.add(ChatColor.GRAY + "Reward" + (rewards.size() != 1 ? "s" : "") + ":");
                    for (ItemCollectionReward reward : rewards) {
                        lore.add(ChatColor.GRAY + " " + reward.toRewardString());
                    }
                    lore.add(" ");
                }
                lore.add(ChatColor.YELLOW + "Click to view rewards!");
                this.set(new GUIClickableItem(){

                    @Override
                    public void run(InventoryClickEvent e2) {
                    }

                    @Override
                    public int getSlot() {
                        return finalSlot;
                    }

                    @Override
                    public ItemStack getItem() {
                        return SUtil.getStack(finalColor + ItemCollectionGUI.this.collection.getName() + " " + SUtil.toRomanNumeral(t2), Material.STAINED_GLASS_PANE, finalData, t2, lore);
                    }
                });
            }
            ++i2;
            ++slot;
        }
    }
}

