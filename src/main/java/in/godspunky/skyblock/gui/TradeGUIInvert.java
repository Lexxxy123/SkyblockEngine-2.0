/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  net.milkbowl.vault.economy.Economy
 *  net.minecraft.server.v1_8_R3.ItemStack
 *  net.minecraft.server.v1_8_R3.NBTBase
 *  net.minecraft.server.v1_8_R3.NBTTagCompound
 *  net.minecraft.server.v1_8_R3.NBTTagLong
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.Sound
 *  org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack
 *  org.bukkit.entity.Player
 *  org.bukkit.event.inventory.InventoryClickEvent
 *  org.bukkit.event.inventory.InventoryCloseEvent
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 */
package in.godspunky.skyblock.gui;

import in.godspunky.skyblock.Skyblock;
import in.godspunky.skyblock.item.SItem;
import in.godspunky.skyblock.item.Untradeable;
import in.godspunky.skyblock.user.User;
import in.godspunky.skyblock.util.SUtil;
import in.godspunky.skyblock.util.Sputnik;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTTagLong;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static in.godspunky.skyblock.gui.TradeGUI.player2;

public class TradeGUIInvert
        extends GUI {
    private final int[] ls = new int[]{0, 1, 2, 3, 9, 10, 11, 12, 18, 19, 20, 21, 27, 28, 29, 30};
    private final int[] rs = new int[]{5, 6, 7, 8, 14, 15, 16, 17, 23, 24, 25, 26, 32, 33, 34, 35};
    private final UUID tradeUUID;

    public void fillFrom(Inventory i, int startFromSlot, int height, ItemStack stacc) {
        i.setItem(startFromSlot, stacc);
        i.setItem(startFromSlot + 9, stacc);
        i.setItem(startFromSlot + 9 + 9, stacc);
        i.setItem(startFromSlot + 9 + 9 + 9, stacc);
        i.setItem(startFromSlot + 9 + 9 + 9 + 9, stacc);
    }

    public TradeGUIInvert() {
        this(UUID.randomUUID());
    }

    public TradeGUIInvert(UUID uuid) {
        super("You                  " + TradeGUI.player1.get(uuid).getName(), 45);
        this.tradeUUID = uuid;
        if (!TradeGUI.itemOfferP1.containsKey(uuid) && TradeGUI.itemOfferP1.get(uuid) == null) {
            TradeGUI.itemOfferP1.put(uuid, new ArrayList());
        }
        if (!TradeGUI.itemOfferP2.containsKey(uuid) && TradeGUI.itemOfferP2.get(uuid) == null) {
            TradeGUI.itemOfferP2.put(uuid, new ArrayList());
        }
    }

    @Override
    public void onOpen(GUIOpenEvent e) {
        final Player player = e.getPlayer();
        final Inventory i = e.getInventory();
        ItemStack stk = SUtil.getSingleLoreStack(ChatColor.GRAY + "\u21e6 Your stuff", Material.STAINED_GLASS_PANE, (short) 0, 1, ChatColor.GRAY + "Their stuff \u21e8");
        stk.setDurability((short) 7);
        this.fillFrom(i, 4, 5, stk);
        TradeMenu.tradeP2Ready.put(this.tradeUUID, false);
        new BukkitRunnable() {

            public void run() {
                if (TradeGUIInvert.this != GUI_MAP.get(player.getUniqueId())) {
                    this.cancel();
                    return;
                }
                if (TradeMenu.tradeP2Countdown.containsKey(TradeGUIInvert.this.tradeUUID) && TradeMenu.tradeP2Countdown.get(TradeGUIInvert.this.tradeUUID) > 0) {
                    TradeMenu.tradeP2Countdown.put(TradeGUIInvert.this.tradeUUID, TradeMenu.tradeP2Countdown.get(TradeGUIInvert.this.tradeUUID) - 1);
                }
            }
        }.runTaskTimer(Skyblock.getPlugin(), 0L, 20L);
        this.set(new GUIClickableItem() {

            @Override
            public void run(InventoryClickEvent e) {
                if (TradeMenu.tradeP2Countdown.containsKey(TradeGUIInvert.this.tradeUUID) && TradeMenu.tradeP2Countdown.get(TradeGUIInvert.this.tradeUUID) <= 0 && (TradeGUI.itemOfferP1.get(TradeGUIInvert.this.tradeUUID).size() > 0 || TradeGUI.itemOfferP2.get(TradeGUIInvert.this.tradeUUID).size() > 0) && !TradeMenu.tradeP2Ready.get(TradeGUIInvert.this.tradeUUID).booleanValue()) {
                    TradeMenu.tradeP2Ready.put(TradeGUIInvert.this.tradeUUID, true);
                    player2.get(TradeGUIInvert.this.tradeUUID).playSound(player2.get(TradeGUIInvert.this.tradeUUID).getLocation(), Sound.VILLAGER_YES, 1.0f, 1.0f);
                    TradeGUI.player1.get(TradeGUIInvert.this.tradeUUID).playSound(TradeGUI.player1.get(TradeGUIInvert.this.tradeUUID).getLocation(), Sound.VILLAGER_YES, 1.0f, 1.0f);
                }
            }

            @Override
            public int getSlot() {
                return 39;
            }

            @Override
            public ItemStack getItem() {
                ItemStack stack = SUtil.getStack(Sputnik.trans("&aTrading!"), Material.STAINED_CLAY, (short) 13, 1, ChatColor.GRAY + "Click an item in your", ChatColor.GRAY + "inventory to offer it for", ChatColor.GRAY + "trade.");
                return stack;
            }
        });
        this.set(new GUIClickableItem() {

            @Override
            public void run(InventoryClickEvent e) {
            }

            @Override
            public int getSlot() {
                return 41;
            }

            @Override
            public ItemStack getItem() {
                ItemStack stack = SUtil.getStack(Sputnik.trans("&eNew deal"), Material.INK_SACK, (short) 8, 1, ChatColor.GRAY + "Trading with " + TradeGUI.player1.get(TradeGUIInvert.this.tradeUUID).getName());
                return stack;
            }
        });
        new BukkitRunnable() {

            public void run() {
                if (TradeGUIInvert.this != GUI_MAP.get(player.getUniqueId())) {
                    this.cancel();
                    return;
                }
                if (TradeMenu.tradeP1Ready.containsKey(TradeGUIInvert.this.tradeUUID) && TradeMenu.tradeP2Countdown.containsKey(TradeGUIInvert.this.tradeUUID)) {
                    if (TradeMenu.tradeP1Ready.get(TradeGUIInvert.this.tradeUUID).booleanValue()) {
                        i.setItem(41, SUtil.getStack(Sputnik.trans("&aOther player confirmed!"), Material.INK_SACK, (short) 10, 1, ChatColor.GRAY + "Trading with " + TradeGUI.player1.get(TradeGUIInvert.this.tradeUUID).getName() + ".", ChatColor.GRAY + "Waiting for you to confirm..."));
                    } else if (TradeMenu.tradeP2Countdown.get(TradeGUIInvert.this.tradeUUID) > 0 && !TradeMenu.tradeP2Ready.get(TradeGUIInvert.this.tradeUUID).booleanValue()) {
                        i.setItem(41, SUtil.getStack(Sputnik.trans("&eDeal timer..."), Material.INK_SACK, (short) 8, 1, ChatColor.GRAY + "Trading with " + TradeGUI.player1.get(TradeGUIInvert.this.tradeUUID).getName() + ".", ChatColor.GRAY + "The trade changed recently."));
                    } else if (TradeMenu.tradeP2Ready.get(TradeGUIInvert.this.tradeUUID).booleanValue()) {
                        i.setItem(41, SUtil.getStack(Sputnik.trans("&ePending their confirm."), Material.INK_SACK, (short) 8, 1, ChatColor.GRAY + "Trading with " + TradeGUI.player1.get(TradeGUIInvert.this.tradeUUID).getName() + ".", ChatColor.GRAY + "Waiting for them to confirm."));
                    } else {
                        i.setItem(41, SUtil.getStack(Sputnik.trans("&eNew deal"), Material.INK_SACK, (short) 8, 1, ChatColor.GRAY + "Trading with " + TradeGUI.player1.get(TradeGUIInvert.this.tradeUUID).getName() + "."));
                    }
                }
                if (TradeMenu.tradeP2Countdown.containsKey(TradeGUIInvert.this.tradeUUID)) {
                    if (TradeMenu.tradeP2Countdown.get(TradeGUIInvert.this.tradeUUID) > 0) {
                        i.setItem(39, SUtil.getStack(Sputnik.trans("&eDeal timer! &7(&e" + TradeMenu.tradeP2Countdown.get(TradeGUIInvert.this.tradeUUID) + "&7)"), Material.STAINED_CLAY, (short) 4, TradeMenu.tradeP2Countdown.get(TradeGUIInvert.this.tradeUUID), ChatColor.GRAY + "The trade recently changed.", ChatColor.GRAY + "Please review it before", ChatColor.GRAY + "accepting."));
                        TradeMenu.tradeP2Ready.put(TradeGUIInvert.this.tradeUUID, false);
                    } else if (TradeMenu.tradeP2Ready.get(TradeGUIInvert.this.tradeUUID)) {
                        i.setItem(39, SUtil.getStack(Sputnik.trans("&aDeal accepted!"), Material.STAINED_CLAY, (short) 13, 1, ChatColor.GRAY + "You accepted the trade.", ChatColor.GRAY + "wait for the other party to", ChatColor.GRAY + "accept."));
                    } else if (TradeGUI.itemOfferP1.get(TradeGUIInvert.this.tradeUUID).size() <= 0 && TradeGUI.itemOfferP2.get(TradeGUIInvert.this.tradeUUID).size() <= 0) {
                        i.setItem(39, SUtil.getStack(Sputnik.trans("&aTrading!"), Material.STAINED_CLAY, (short) 13, 1, ChatColor.GRAY + "Click an item in your", ChatColor.GRAY + "inventory to offer it for", ChatColor.GRAY + "trade."));
                    } else if (TradeGUI.itemOfferP1.get(TradeGUIInvert.this.tradeUUID).size() <= 0 && !TradeMenu.tradeP2Ready.get(TradeGUIInvert.this.tradeUUID).booleanValue()) {
                        i.setItem(39, SUtil.getStack(Sputnik.trans("&eWarning!"), Material.STAINED_CLAY, (short) 1, 1, ChatColor.GRAY + "You are offering items", ChatColor.GRAY + "without getting anything in", ChatColor.GRAY + "return.", " ", ChatColor.YELLOW + "Click to accept anyway!"));
                    } else if (TradeGUI.itemOfferP2.get(TradeGUIInvert.this.tradeUUID).size() <= 0) {
                        i.setItem(39, SUtil.getStack(Sputnik.trans("&bGift!"), Material.STAINED_CLAY, (short) 11, 1, ChatColor.GRAY + "You are receiving items", ChatColor.GRAY + "without offering anything in", ChatColor.GRAY + "return.", " ", ChatColor.YELLOW + "Click to accept!"));
                    } else if (!TradeGUI.itemOfferP1.get(TradeGUIInvert.this.tradeUUID).isEmpty() && TradeGUI.itemOfferP2.get(TradeGUIInvert.this.tradeUUID).size() > 0) {
                        i.setItem(39, SUtil.getStack(Sputnik.trans("&eDeal!"), Material.STAINED_CLAY, (short) 5, 1, ChatColor.GRAY + "All trades are final and", ChatColor.GRAY + "cannot be reverted.", " ", ChatColor.GREEN + "Make sure to review the", ChatColor.GREEN + "trade before accepting", " ", ChatColor.YELLOW + "Click to accept the trade!"));
                    }
                }
                List<ItemStack> stl1 = TradeGUI.itemOfferP1.get(TradeGUIInvert.this.tradeUUID);
                List<ItemStack> stl2 = TradeGUI.itemOfferP2.get(TradeGUIInvert.this.tradeUUID);
                ItemStack stk = SUtil.getSingleLoreStack(ChatColor.GRAY + "\u21e6 Your stuff", Material.STAINED_GLASS_PANE, (short) 0, 1, ChatColor.GRAY + "Their stuff \u21e8");
                stk.setDurability((short) 7);
                TradeGUIInvert.this.fillFrom(i, 4, 5, stk);
                int a = -1;
                for (int slot : TradeGUIInvert.this.rs) {
                    if (a < stl1.size() - 1) {
                        if (SItem.find(stl1.get(++a)) != null) {
                            i.setItem(slot, User.getUser(player2.get(TradeGUIInvert.this.tradeUUID).getUniqueId()).updateItemBoost(SItem.find(stl1.get(a))));
                            continue;
                        }
                        i.setItem(slot, stl1.get(a));
                        continue;
                    }
                    i.setItem(slot, null);
                }
                int b = -1;
                for (int slot : TradeGUIInvert.this.ls) {
                    if (b < stl2.size() - 1) {
                        if (SItem.find(stl2.get(++b)) != null) {
                            i.setItem(slot, User.getUser(player2.get(TradeGUIInvert.this.tradeUUID).getUniqueId()).updateItemBoost(SItem.find(stl2.get(b))));
                            continue;
                        }
                        i.setItem(slot, stl2.get(b));
                        continue;
                    }
                    i.setItem(slot, null);
                }
            }
        }.runTaskTimer(Skyblock.getPlugin(), 0L, 1L);
        this.set(new GUISignItem() {

            @Override
            public GUI onSignClose(String query, Player target) {
                User user = User.getUser(player2.get(TradeGUIInvert.this.tradeUUID).getUniqueId());
                if (target != player2.get(TradeGUIInvert.this.tradeUUID)) {
                    return null;
                }
                if (query == "$canc") {
                    return new TradeGUI(TradeGUIInvert.this.tradeUUID);
                }
                try {
                    long add = Long.parseLong(query);
                    double cur = user.getCoins();
                    if (add <= 0L) {
                        player2.get(TradeGUIInvert.this.tradeUUID).playSound(player2.get(TradeGUIInvert.this.tradeUUID).getLocation(), Sound.ENDERMAN_TELEPORT, 1.0f, -4.0f);
                        player.sendMessage(ChatColor.RED + "Couldn't validate this Coins amount!");
                        return new TradeGUIInvert(TradeGUIInvert.this.tradeUUID);
                    }
                    if ((double) add > cur) {
                        player2.get(TradeGUIInvert.this.tradeUUID).playSound(player2.get(TradeGUIInvert.this.tradeUUID).getLocation(), Sound.ENDERMAN_TELEPORT, 1.0f, -4.0f);
                        player.sendMessage(ChatColor.RED + "You don't have that much Coins for this.");
                        return new TradeGUIInvert(TradeGUIInvert.this.tradeUUID);
                    }
                    if (add > 640000L) {
                        player2.get(TradeGUIInvert.this.tradeUUID).playSound(player2.get(TradeGUIInvert.this.tradeUUID).getLocation(), Sound.ENDERMAN_TELEPORT, 1.0f, -4.0f);
                        player.sendMessage(ChatColor.RED + "You cannot transfer more than 640,000 Coins at once!");
                        return new TradeGUIInvert(TradeGUIInvert.this.tradeUUID);
                    }
                    if (TradeGUI.itemOfferP2.get(TradeGUIInvert.this.tradeUUID).size() < 16) {
                        user.subCoins(add);
                            player2.get(TradeGUIInvert.this.tradeUUID).playSound(player2.get(TradeGUIInvert.this.tradeUUID).getLocation(), Sound.VILLAGER_HAGGLE, 1.0f, 1.0f);
                            player2.get(TradeGUIInvert.this.tradeUUID).playSound(player2.get(TradeGUIInvert.this.tradeUUID).getLocation(), Sound.VILLAGER_HAGGLE, 1.0f, 1.0f);
                            long stackamount = Math.min(64L, Math.max(10000L, add) / 10000L);
                            ItemStack coinsStack = SUtil.getSkullURLStack(ChatColor.YELLOW + Sputnik.formatFull(add) + " Coins", "8dbe8b54fb9fddeb0f3fa75ee536b5c78360f100aecf5aaec134d35362d8bc57", (int) stackamount, ChatColor.GRAY + "Lump-sum amount");
                            net.minecraft.server.v1_8_R3.ItemStack tagStack = CraftItemStack.asNMSCopy(coinsStack);
                            NBTTagCompound tagCompound = tagStack.hasTag() ? tagStack.getTag() : new NBTTagCompound();
                            tagCompound.set("data_coins", new NBTTagLong(add));
                            tagStack.setTag(tagCompound);
                            coinsStack = CraftItemStack.asBukkitCopy(tagStack);
                            TradeGUI.itemOfferP2.get(TradeGUIInvert.this.tradeUUID).add(coinsStack);
                            TradeMenu.tradeP1Countdown.put(TradeGUIInvert.this.tradeUUID, 3);
                            TradeMenu.tradeP2Countdown.put(TradeGUIInvert.this.tradeUUID, 3);
                    }
                    return new TradeGUIInvert(TradeGUIInvert.this.tradeUUID);
                } catch (NumberFormatException ex) {
                    player.sendMessage(ChatColor.RED + "Couldn't parse this Coins amount!");
                    player2.get(TradeGUIInvert.this.tradeUUID).playSound(player2.get(TradeGUIInvert.this.tradeUUID).getLocation(), Sound.ENDERMAN_TELEPORT, 1.0f, -4.0f);
                    return new TradeGUIInvert(TradeGUIInvert.this.tradeUUID);
                }
            }

            @Override
            public void run(InventoryClickEvent e) {
                player.playSound(player.getLocation(), Sound.CLICK, 1.0f, 1.0f);
            }

            @Override
            public int getSlot() {
                return 36;
            }

            @Override
            public ItemStack getItem() {
                return SUtil.getSkullURLStack(ChatColor.YELLOW + "Coins transaction", "8dbe8b54fb9fddeb0f3fa75ee536b5c78360f100aecf5aaec134d35362d8bc57", 1, ChatColor.GRAY + " ", ChatColor.YELLOW + "Click to add coins!");
            }

            @Override
            public UUID inti() {
                return TradeGUIInvert.this.tradeUUID;
            }
        });
    }

    @Override
    public void onClose(InventoryCloseEvent e) {
        ((Player) e.getPlayer()).playSound(e.getPlayer().getLocation(), Sound.VILLAGER_HAGGLE, 1.0f, 1.0f);
        TradeMenu.triggerCloseEvent(this.tradeUUID, false, (Player) e.getPlayer());
        GUIListener.QUERY_MAP.remove(e.getPlayer().getUniqueId());
        GUIListener.QUERY_MAPPING.remove(e.getPlayer().getUniqueId());
    }

    @Override
    public void onBottomClick(InventoryClickEvent e) {
        if (e.getSlot() < 0) {
            e.setCancelled(true);
            return;
        }
        ItemStack cs = null;
        if (player2.get(this.tradeUUID).getInventory().getItem(e.getSlot()) != null) {
            cs = player2.get(this.tradeUUID).getInventory().getItem(e.getSlot());
        }
        if (cs != null) {
            SItem sItem = SItem.find(cs);
            if (sItem != null && SItem.isSpecItem(cs)) {
                if (!(sItem.getType().getGenericInstance() instanceof Untradeable)) {
                    if (TradeGUI.itemOfferP2.get(this.tradeUUID).size() < 16) {
                        TradeGUI.player1.get(this.tradeUUID).playSound(TradeGUI.player1.get(this.tradeUUID).getLocation(), Sound.VILLAGER_HAGGLE, 1.0f, 1.0f);
                        player2.get(this.tradeUUID).playSound(player2.get(this.tradeUUID).getLocation(), Sound.VILLAGER_HAGGLE, 1.0f, 1.0f);
                        TradeGUI.itemOfferP2.get(this.tradeUUID).add(cs);
                        player2.get(this.tradeUUID).getInventory().setItem(e.getSlot(), null);
                        TradeMenu.tradeP2Countdown.put(this.tradeUUID, 3);
                        TradeMenu.tradeP1Countdown.put(this.tradeUUID, 3);
                    } else {
                        player2.get(this.tradeUUID).sendMessage(Sputnik.trans("&c&lIT'S FULL! &7Your trade window is full!"));
                        player2.get(this.tradeUUID).playSound(player2.get(this.tradeUUID).getLocation(), Sound.VILLAGER_NO, 1.0f, 1.0f);
                        player2.get(this.tradeUUID).playSound(player2.get(this.tradeUUID).getLocation(), Sound.ENDERMAN_TELEPORT, 1.0f, -4.0f);
                    }
                } else {
                    player2.get(this.tradeUUID).sendMessage(Sputnik.trans("&cYou cannot trade this item!"));
                    player2.get(this.tradeUUID).playSound(player2.get(this.tradeUUID).getLocation(), Sound.ENDERMAN_TELEPORT, 1.0f, -4.0f);
                }
            } else {
                player2.get(this.tradeUUID).sendMessage(Sputnik.trans("&cYou cannot trade this item!"));
                player2.get(this.tradeUUID).playSound(player2.get(this.tradeUUID).getLocation(), Sound.ENDERMAN_TELEPORT, 1.0f, -4.0f);
            }
        }
    }

    @Override
    public void onTopClick(InventoryClickEvent e) {
        if (TradeGUI.itemOfferP2.get(this.tradeUUID).contains(e.getInventory().getItem(e.getSlot())) && TradeGUI.isContain(this.ls, e.getSlot())) {
            player2.get(this.tradeUUID).playSound(player2.get(this.tradeUUID).getLocation(), Sound.VILLAGER_IDLE, 1.0f, 1.0f);
            User user = User.getUser(player2.get(this.tradeUUID).getUniqueId());
            TradeGUI.itemOfferP2.get(this.tradeUUID).remove(e.getInventory().getItem(e.getSlot()));
            ItemStack stack = e.getInventory().getItem(e.getSlot());
            net.minecraft.server.v1_8_R3.ItemStack nmsStack = CraftItemStack.asNMSCopy(stack);
            if (!nmsStack.getTag().hasKey("data_coins")) {
                Sputnik.smartGiveItem(stack, player2.get(this.tradeUUID));
            } else {
                User.getUser(player2.get(this.tradeUUID).getUniqueId()).addCoins(nmsStack.getTag().getLong("data_coins"));
            }
            TradeMenu.tradeP2Countdown.put(this.tradeUUID, 3);
            TradeMenu.tradeP1Countdown.put(this.tradeUUID, 3);
        }
    }
}