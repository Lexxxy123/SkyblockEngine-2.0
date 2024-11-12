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

import net.hypixel.skyblock.features.itemeditor.EditableItem;
import net.hypixel.skyblock.gui.GUI;
import net.hypixel.skyblock.gui.GUIClickableItem;
import net.hypixel.skyblock.gui.GUIOpenEvent;
import net.hypixel.skyblock.gui.GUIType;
import net.hypixel.skyblock.item.SItem;
import net.hypixel.skyblock.item.SMaterial;
import net.hypixel.skyblock.user.User;
import net.hypixel.skyblock.util.SUtil;
import net.hypixel.skyblock.util.Sputnik;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class ItemEditor
extends GUI {
    public ItemEditor() {
        super("Item Editor (incomplete)", 45);
    }

    @Override
    public void onOpen(GUIOpenEvent e2) {
        if (e2.getPlayer().getItemInHand() == null) {
            e2.getPlayer().sendMessage(ChatColor.RED + "Please hold a item!");
            e2.getPlayer().closeInventory();
            return;
        }
        this.border(BLACK_STAINED_GLASS_PANE);
        final Player player = e2.getPlayer();
        this.set(GUIClickableItem.getCloseItem(40));
        User user = User.getUser(player.getUniqueId());
        final EditableItem editableItem = new EditableItem(SItem.find(player.getItemInHand()));
        this.set(new GUIClickableItem(){

            @Override
            public void run(InventoryClickEvent e2) {
                GUIType.REFORGE_ANVIL.getGUI().open(player);
            }

            @Override
            public int getSlot() {
                return 13;
            }

            @Override
            public ItemStack getItem() {
                return SUtil.getStack(ChatColor.AQUA + "Reforge", Material.ANVIL, (short)0, 1, ChatColor.GRAY + "Reforge your items.");
            }
        });
        this.set(new GUIClickableItem(){

            @Override
            public void run(InventoryClickEvent e2) {
                player.sendMessage(ChatColor.RED + "/enc <enchant type> <level>");
            }

            @Override
            public int getSlot() {
                return 28;
            }

            @Override
            public ItemStack getItem() {
                return SUtil.getStack(ChatColor.YELLOW + "Enchant", Material.ENCHANTMENT_TABLE, (short)0, 1, ChatColor.GRAY + "Enchant your items.");
            }
        });
        this.set(new GUIClickableItem(){

            @Override
            public void run(InventoryClickEvent e2) {
                editableItem.addStar(1);
            }

            @Override
            public int getSlot() {
                return 21;
            }

            @Override
            public ItemStack getItem() {
                return SUtil.getSkullURLStack(ChatColor.RED + "Add Star", "8216ee40593c0981ed28f5bd674879781c425ce0841b687481c4f7118bb5c3b1", 1, ChatColor.GRAY + "Add or remove stars", ChatColor.GRAY + "from your items.");
            }
        });
        this.set(new GUIClickableItem(){

            @Override
            public void run(InventoryClickEvent e2) {
                GUIType.DUNGEON_CRAFTING.getGUI().open(player);
            }

            @Override
            public int getSlot() {
                return 23;
            }

            @Override
            public ItemStack getItem() {
                return SUtil.getSkullURLStack(ChatColor.GOLD + "Convert to Dungeon Item", "5a6314eac34416ce10ab22c2e1c4dcb472a3feb98d4e04d3fbbb85a9a471b18", 1, ChatColor.GRAY + "Convert your items ", ChatColor.GRAY + "to dungeon items.");
            }
        });
        this.set(new GUIClickableItem(){

            @Override
            public void run(InventoryClickEvent e2) {
                editableItem.recom(true);
            }

            @Override
            public int getSlot() {
                return 34;
            }

            @Override
            public ItemStack getItem() {
                return SItem.of(SMaterial.RECOMBOBULATOR_3000).getStack();
            }
        });
        this.set(new GUIClickableItem(){

            @Override
            public void run(InventoryClickEvent e2) {
            }

            @Override
            public int getSlot() {
                return 31;
            }

            @Override
            public ItemStack getItem() {
                return SUtil.getSkullURLStack(ChatColor.LIGHT_PURPLE + "Change Rarity", "1035c528036b384c53c9c8a1a125685e16bfb369c197cc9f03dfa3b835b1aa55", 1, ChatColor.GRAY + "Change rarity of your items.");
            }
        });
        this.set(new GUIClickableItem(){

            @Override
            public void run(InventoryClickEvent e2) {
                if (editableItem.getHandle().getHPBs() == 20) {
                    player.sendMessage(Sputnik.trans("&cYou have already reached the Maximum limit!"));
                }
                editableItem.addhpb();
            }

            @Override
            public int getSlot() {
                return 10;
            }

            @Override
            public ItemStack getItem() {
                return SUtil.getStack(ChatColor.GOLD + "Add Hot Potato Book", Material.BOOK, (short)0, 1, ChatColor.GRAY + "Add hot potato book to item.");
            }
        });
    }
}

