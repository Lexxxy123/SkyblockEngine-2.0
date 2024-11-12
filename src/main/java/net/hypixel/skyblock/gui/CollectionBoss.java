/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.Sound
 *  org.bukkit.entity.Player
 *  org.bukkit.event.inventory.InventoryClickEvent
 *  org.bukkit.inventory.ItemStack
 */
package net.hypixel.skyblock.gui;

import java.util.HashMap;
import java.util.Map;
import net.hypixel.skyblock.gui.GUI;
import net.hypixel.skyblock.gui.GUIClickableItem;
import net.hypixel.skyblock.gui.GUIOpenEvent;
import net.hypixel.skyblock.gui.GUIType;
import net.hypixel.skyblock.user.PlayerStatistics;
import net.hypixel.skyblock.user.PlayerUtils;
import net.hypixel.skyblock.user.User;
import net.hypixel.skyblock.util.SUtil;
import net.hypixel.skyblock.util.Sputnik;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class CollectionBoss
extends GUI {
    public static final Map<Player, Boolean> ableToJoin = new HashMap<Player, Boolean>();

    public CollectionBoss() {
        super("Boss Collections", 36);
    }

    @Override
    public void onOpen(GUIOpenEvent e2) {
        this.fill(BLACK_STAINED_GLASS_PANE);
        final Player player = e2.getPlayer();
        final User user = User.getUser(player.getUniqueId());
        PlayerStatistics statistics = PlayerUtils.STATISTICS_CACHE.get(player.getUniqueId());
        this.set(GUIClickableItem.getCloseItem(31));
        this.set(new GUIClickableItem(){

            @Override
            public void run(InventoryClickEvent e2) {
                GUIType.CATACOMBS_BOSS.getGUI().open((Player)e2.getWhoClicked());
                player.playSound(player.getLocation(), Sound.NOTE_STICKS, 1.0f, 1.0f);
            }

            @Override
            public int getSlot() {
                return 30;
            }

            @Override
            public ItemStack getItem() {
                Bukkit.getServer();
                return SUtil.getStack(ChatColor.GREEN + "Back", Material.ARROW, (short)0, 1, new String[0]);
            }
        });
        this.set(new GUIClickableItem(){

            @Override
            public void run(InventoryClickEvent e2) {
            }

            @Override
            public int getSlot() {
                return 13;
            }

            @Override
            public ItemStack getItem() {
                String rw = "&bAble to use &cGolden Sadan Trophy&b!";
                String a2 = "";
                String b2 = "";
                a2 = user.getBCollection() < 100L ? SUtil.createLineProgressBar(25, ChatColor.DARK_GREEN, user.getBCollection(), 100.0) : (user.getBCollection() >= 100L ? SUtil.createLineProgressBar(25, ChatColor.DARK_GREEN, user.getBCollection(), 1000.0) : Sputnik.trans("&c&lMAXED!"));
                if (user.getBCollection() >= 100L && user.getBCollection() < 1000L) {
                    rw = "&aAble to use &cDiamond Sadan Trophy&a!";
                } else if (user.getBCollection() >= 1000L) {
                    rw = "&6&lMAXED OUT! NICE";
                }
                b2 = user.getBCollection() > user.getBRun6() ? "&7Completion Rate: &cBruh, what the, how??" : (user.getBRun6() > 0L ? "&7Completion Rate: &a" + Math.round((double)user.getBCollection() / (double)user.getBRun6() * 100.0) + "%" : "&7Completion Rate: &cHaven't played.");
                ItemStack itemstack = null;
                itemstack = SUtil.getSkullURLStack(ChatColor.RED + "Sadan", "fa06cb0c471c1c9bc169af270cd466ea701946776056e472ecdaeb49f0f4a4dc", 1, Sputnik.trans("&7View all your Sadan Collection"), Sputnik.trans("&7Progress and rewards!"), "   ", Sputnik.trans("&7Total Boss Killed: &a" + SUtil.commaify(user.getBCollection())), Sputnik.trans("&7Total Runs: &e" + SUtil.commaify(user.getBRun6())), Sputnik.trans(b2), "   ", Sputnik.trans("&7Progress to next level"), Sputnik.trans(a2), "   ", Sputnik.trans("&7Rewards"), Sputnik.trans(rw), "   ", Sputnik.trans("&eClick to refresh"));
                return itemstack;
            }
        });
    }
}

