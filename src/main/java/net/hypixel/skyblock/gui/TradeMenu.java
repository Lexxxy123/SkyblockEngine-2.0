/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraft.server.v1_8_R3.ItemStack
 *  org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 */
package net.hypixel.skyblock.gui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.hypixel.skyblock.SkyBlock;
import net.hypixel.skyblock.features.sequence.SoundSequenceType;
import net.hypixel.skyblock.gui.TradeGUI;
import net.hypixel.skyblock.gui.TradeGUIInvert;
import net.hypixel.skyblock.user.User;
import net.hypixel.skyblock.util.SUtil;
import net.hypixel.skyblock.util.Sputnik;
import net.hypixel.skyblock.util.TradeUtil;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class TradeMenu {
    public static final Map<UUID, Boolean> tradeClose = new HashMap<UUID, Boolean>();
    public static final Map<UUID, Player> tradeClosePlayerName = new HashMap<UUID, Player>();
    public static final Map<UUID, Integer> tradeP1Countdown = new HashMap<UUID, Integer>();
    public static final Map<UUID, Integer> tradeP2Countdown = new HashMap<UUID, Integer>();
    public static final Map<UUID, Boolean> tradeP1Ready = new HashMap<UUID, Boolean>();
    public static final Map<UUID, Boolean> tradeP2Ready = new HashMap<UUID, Boolean>();
    public static final Map<UUID, Boolean> successTrade = new HashMap<UUID, Boolean>();
    public static final Map<UUID, Boolean> player1TradeUUID = new HashMap<UUID, Boolean>();
    public static final Map<UUID, Boolean> player2TradeUUID = new HashMap<UUID, Boolean>();
    private final Player p1;
    private final Player p2;
    private final UUID tradeUUID;

    public TradeMenu(Player player1, Player player2, UUID uuid) {
        this.p1 = player1;
        this.p2 = player2;
        this.tradeUUID = uuid;
    }

    public static void triggerCloseEvent(UUID tradeUUID, boolean isSuccess, Player player) {
        if (tradeClose.containsKey(tradeUUID)) {
            return;
        }
        if (!isSuccess) {
            tradeClose.put(tradeUUID, isSuccess);
            tradeClosePlayerName.put(tradeUUID, player);
        } else {
            tradeClose.put(tradeUUID, isSuccess);
            tradeClosePlayerName.put(tradeUUID, player);
        }
    }

    public void open() {
        if (this.p1.getUniqueId() == this.p2.getUniqueId()) {
            return;
        }
        TradeUtil.trading.put(this.p1.getUniqueId(), true);
        TradeUtil.trading.put(this.p2.getUniqueId(), true);
        TradeGUI.player1.put(this.tradeUUID, this.p1);
        TradeGUI.player2.put(this.tradeUUID, this.p2);
        new TradeGUI(this.tradeUUID).open(this.p1);
        new TradeGUIInvert(this.tradeUUID).open(this.p2);
        new BukkitRunnable(){

            public void run() {
                if (!TradeMenu.this.p1.isOnline() || !TradeMenu.this.p2.isOnline()) {
                    if (!TradeMenu.this.p1.isOnline()) {
                        TradeMenu.triggerCloseEvent(TradeMenu.this.tradeUUID, false, TradeMenu.this.p1);
                    } else if (!TradeMenu.this.p2.isOnline()) {
                        TradeMenu.triggerCloseEvent(TradeMenu.this.tradeUUID, false, TradeMenu.this.p2);
                    }
                }
                if (tradeClose.containsKey(TradeMenu.this.tradeUUID)) {
                    this.cancel();
                    if (!tradeClose.get(TradeMenu.this.tradeUUID).booleanValue()) {
                        if (tradeClosePlayerName.get(TradeMenu.this.tradeUUID) == TradeMenu.this.p1) {
                            TradeMenu.this.p1.sendMessage(Sputnik.trans("&cYou cancelled the trade!"));
                            TradeMenu.this.p2.sendMessage(Sputnik.trans("&b" + TradeMenu.this.p1.getName() + " &ccancelled the trade!"));
                            TradeMenu.this.p2.closeInventory();
                        } else {
                            TradeMenu.this.p2.sendMessage(Sputnik.trans("&cYou cancelled the trade!"));
                            TradeMenu.this.p1.sendMessage(Sputnik.trans("&b" + TradeMenu.this.p2.getName() + " &ccancelled the trade!"));
                            TradeMenu.this.p1.closeInventory();
                        }
                        TradeMenu.this.clean();
                    } else if (successTrade.containsKey(TradeMenu.this.tradeUUID)) {
                        List<ItemStack> itemlist2;
                        List<ItemStack> itemlist1;
                        if (successTrade.get(TradeMenu.this.tradeUUID).booleanValue()) {
                            itemlist1 = TradeGUI.itemOfferP1.get(TradeMenu.this.tradeUUID);
                            itemlist2 = TradeGUI.itemOfferP2.get(TradeMenu.this.tradeUUID);
                            TradeGUI.itemOfferP1.put(TradeMenu.this.tradeUUID, itemlist2);
                            TradeGUI.itemOfferP2.put(TradeMenu.this.tradeUUID, itemlist1);
                        }
                        itemlist1 = TradeGUI.itemOfferP1.get(TradeMenu.this.tradeUUID);
                        itemlist2 = TradeGUI.itemOfferP2.get(TradeMenu.this.tradeUUID);
                        StringBuilder sb1 = new StringBuilder();
                        sb1.append("&6Trade completed with &r" + TradeMenu.this.p2.getDisplayName() + "&6!");
                        for (ItemStack itemRece : itemlist1) {
                            if (!CraftItemStack.asNMSCopy((ItemStack)itemRece).getTag().hasKey("data_bits")) {
                                sb1.append("\n &a&l+ &8" + itemRece.getAmount() + "x &r" + itemRece.getItemMeta().getDisplayName());
                                continue;
                            }
                            sb1.append("\n &a&l+ &8" + itemRece.getItemMeta().getDisplayName());
                        }
                        for (ItemStack itemTaken : itemlist2) {
                            if (!CraftItemStack.asNMSCopy((ItemStack)itemTaken).getTag().hasKey("data_bits")) {
                                sb1.append("\n &c&l- &8" + itemTaken.getAmount() + "x &r" + itemTaken.getItemMeta().getDisplayName());
                                continue;
                            }
                            sb1.append("\n &c&l- &8" + itemTaken.getItemMeta().getDisplayName());
                        }
                        TradeMenu.this.p1.sendMessage(Sputnik.trans(sb1.toString()));
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("&6Trade completed with " + TradeMenu.this.p1.getDisplayName() + "&6!");
                        for (ItemStack itemRece2 : itemlist2) {
                            if (!CraftItemStack.asNMSCopy((ItemStack)itemRece2).getTag().hasKey("data_bits")) {
                                sb2.append("\n &a&l+ &8" + itemRece2.getAmount() + "x &r" + itemRece2.getItemMeta().getDisplayName());
                                continue;
                            }
                            sb2.append("\n &a&l+ &8" + itemRece2.getItemMeta().getDisplayName());
                        }
                        for (ItemStack itemTaken2 : itemlist1) {
                            if (!CraftItemStack.asNMSCopy((ItemStack)itemTaken2).getTag().hasKey("data_bits")) {
                                sb2.append("\n &c&l- &8" + itemTaken2.getAmount() + "x &r" + itemTaken2.getItemMeta().getDisplayName());
                                continue;
                            }
                            sb2.append("\n &c&l- &8" + itemTaken2.getItemMeta().getDisplayName());
                        }
                        TradeMenu.this.p2.sendMessage(Sputnik.trans(sb2.toString()));
                        SoundSequenceType.TRADE_COMPLETE.play(TradeMenu.this.p1);
                        SoundSequenceType.TRADE_COMPLETE.play(TradeMenu.this.p2);
                        TradeMenu.this.p1.closeInventory();
                        TradeMenu.this.p2.closeInventory();
                        TradeMenu.this.clean();
                        TradeGUI.itemOfferP1.remove(TradeMenu.this.p1.getUniqueId());
                        TradeGUI.itemOfferP2.remove(TradeMenu.this.p2.getUniqueId());
                    }
                    TradeMenu.this.returnToAllPlayers(TradeMenu.this.p1, TradeMenu.this.p2);
                }
            }
        }.runTaskTimer((Plugin)SkyBlock.getPlugin(), 0L, 1L);
    }

    public void clean() {
        SUtil.delay(() -> {
            player1TradeUUID.remove(this.p1.getUniqueId());
            player2TradeUUID.remove(this.p2.getUniqueId());
            tradeClose.remove(this.tradeUUID);
            tradeClosePlayerName.remove(this.tradeUUID);
            tradeP1Countdown.remove(this.tradeUUID);
            tradeP2Countdown.remove(this.tradeUUID);
            tradeP1Ready.remove(this.tradeUUID);
            tradeP2Ready.remove(this.tradeUUID);
            TradeGUI.itemOfferP1.remove(this.p1.getUniqueId());
            TradeGUI.itemOfferP2.remove(this.p2.getUniqueId());
            TradeUtil.resetTrade(this.p1);
            TradeUtil.resetTrade(this.p2);
            TradeUtil.trading.put(this.p1.getUniqueId(), false);
            TradeUtil.trading.put(this.p2.getUniqueId(), false);
        }, 2L);
    }

    public void returnToAllPlayers(Player player1, Player player2) {
        net.minecraft.server.v1_8_R3.ItemStack nmsStack;
        for (ItemStack i2 : TradeGUI.itemOfferP1.get(this.tradeUUID)) {
            nmsStack = CraftItemStack.asNMSCopy((ItemStack)i2);
            if (!nmsStack.getTag().hasKey("data_bits")) {
                Sputnik.smartGiveItem(i2, player1);
                continue;
            }
            User.getUser(player1.getUniqueId()).addBits(nmsStack.getTag().getLong("data_bits"));
        }
        for (ItemStack i2 : TradeGUI.itemOfferP2.get(this.tradeUUID)) {
            nmsStack = CraftItemStack.asNMSCopy((ItemStack)i2);
            if (!nmsStack.getTag().hasKey("data_bits")) {
                Sputnik.smartGiveItem(i2, player2);
                continue;
            }
            User.getUser(player2.getUniqueId()).addBits(nmsStack.getTag().getLong("data_bits"));
        }
    }

    public Player getP1() {
        return this.p1;
    }

    public Player getP2() {
        return this.p2;
    }
}

