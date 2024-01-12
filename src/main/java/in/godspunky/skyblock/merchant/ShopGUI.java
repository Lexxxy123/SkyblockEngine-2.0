package in.godspunky.skyblock.merchant;

import in.godspunky.skyblock.gui.GUI;
import in.godspunky.skyblock.gui.GUIClickableItem;
import in.godspunky.skyblock.gui.GUIOpenEvent;
import in.godspunky.skyblock.gui.ShopTradingOptionsGUI;
import in.godspunky.skyblock.item.SItem;
import in.godspunky.skyblock.item.SpecificItemType;
import in.godspunky.skyblock.user.User;
import in.godspunky.skyblock.util.PaginationList;
import in.godspunky.skyblock.util.SUtil;
import in.godspunky.skyblock.util.StackArrayList;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ShopGUI extends GUI {
    private static final Map<UUID, StackArrayList<SItem>> BUYBACK_HISTORY;
    private static final int[] INTERIOR;

    static {
        BUYBACK_HISTORY = new HashMap<UUID, StackArrayList<SItem>>();
        INTERIOR = new int[]{10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34, 37, 38, 39, 40, 41, 42, 43};
    }

    private final SItem[] items;
    private int page;

    protected ShopGUI(final String title, final int page, final SItem... items) {
        super(title, 54);
        this.page = page;
        this.items = items;
    }

    protected ShopGUI(final String name, final SItem... items) {
        this(name, 1, items);
    }

    @Override
    public void onOpen(final GUIOpenEvent e) {
        final Player player = e.getPlayer();
        final User user = User.getUser(player.getUniqueId());
        this.border(ShopGUI.BLACK_STAINED_GLASS_PANE);
        final PaginationList<SItem> paged = new PaginationList<SItem>(28);
        paged.addAll(this.items);
        if (paged.size() == 0) {
            this.page = 0;
        }
        final int finalPage = this.page;
        if (this.page > 1) {
            this.set(new GUIClickableItem() {
                @Override
                public void run(final InventoryClickEvent e) {
                    new ShopGUI(ShopGUI.this.title, finalPage - 1, ShopGUI.this.items).open((Player) e.getWhoClicked());
                }

                @Override
                public int getSlot() {
                    return 45;
                }

                @Override
                public ItemStack getItem() {
                    return SUtil.createNamedItemStack(Material.ARROW, ChatColor.GRAY + "<-");
                }
            });
        }
        if (this.page != paged.getPageCount()) {
            this.set(new GUIClickableItem() {
                @Override
                public void run(final InventoryClickEvent e) {
                    new ShopGUI(ShopGUI.this.title, finalPage + 1, ShopGUI.this.items).open((Player) e.getWhoClicked());
                }

                @Override
                public int getSlot() {
                    return 53;
                }

                @Override
                public ItemStack getItem() {
                    return SUtil.createNamedItemStack(Material.ARROW, ChatColor.GRAY + "->");
                }
            });
        }
        final UUID uuid = player.getUniqueId();
        final StackArrayList<SItem> buyback = ShopGUI.BUYBACK_HISTORY.get(uuid);
        this.set(new GUIClickableItem() {
            @Override
            public int getSlot() {
                return 49;
            }

            @Override
            public void run(final InventoryClickEvent e) {
                if (!ShopGUI.BUYBACK_HISTORY.containsKey(uuid) || ShopGUI.BUYBACK_HISTORY.get(player.getUniqueId()).size() == 0) {
                    return;
                }
                if (buyback.last() == null) return;
                if (buyback.last().getItemValue() == null) return;
                final long value = buyback.last().getItemValue() * buyback.last().getStack().getAmount();
                if (value > user.getCoins()) {
                    player.sendMessage(ChatColor.RED + "You don't have enough coins!");
                    return;
                }
                final Map<Integer, ItemStack> m = player.getInventory().addItem(buyback.pop().getStack());
                if (m.size() != 0) {
                    player.sendMessage(ChatColor.RED + "Free up inventory space to purchase this!");
                    return;
                }
                player.playSound(player.getLocation(), Sound.NOTE_PLING, 1.0f, 2.0f);
                user.subCoins(value);
                if (buyback.isEmpty()) {
                    ShopGUI.BUYBACK_HISTORY.remove(uuid);
                }
                new ShopGUI(ShopGUI.this.title, ShopGUI.this.page, ShopGUI.this.items).open(player);
            }

            @Override
            public ItemStack getItem() {
                if (!ShopGUI.BUYBACK_HISTORY.containsKey(uuid) || ShopGUI.BUYBACK_HISTORY.get(player.getUniqueId()).size() == 0) {
                    return SUtil.getStack(ChatColor.GREEN + "Sell Item", Material.HOPPER, (short) 0, 1, ChatColor.GRAY + "Click items in your inventory to", ChatColor.GRAY + "sell them to this Shop!");
                }
                final SItem last = buyback.last().clone();
                final ItemMeta meta = last.getStack().getItemMeta();
                final List<String> lore = meta.getLore();
                lore.add(" ");
                lore.add(ChatColor.GRAY + "Cost");
                if (last.getItemValue() == null) return null;
                final long price = last.getItemValue() * last.getStack().getAmount();
                lore.add(ChatColor.GOLD + SUtil.commaify(price) + " Coin" + ((price != 1L) ? "s" : ""));
                lore.add(" ");
                lore.add(ChatColor.YELLOW + "Click to buyback!");
                meta.setLore(lore);
                last.getStack().setItemMeta(meta);
                return last.getStack();
            }
        });
        final List<SItem> p = paged.getPage(this.page);
        if (p == null) {
            return;
        }
        for (int i = 0; i < p.size(); ++i) {
            final int slot = ShopGUI.INTERIOR[i];
            final SItem item = p.get(i).clone();
            final ItemMeta meta = item.getStack().getItemMeta();
            if (item.getStack().getAmount() != 1) {
                meta.setDisplayName(meta.getDisplayName() + ChatColor.DARK_GRAY + " x" + item.getStack().getAmount());
            }
            final List<String> lore = meta.getLore();
            lore.add(" ");
            lore.add(ChatColor.GRAY + "Cost");
            if (item.getPrice() == null) continue;
            final long price = item.getPrice() * item.getStack().getAmount();
            lore.add(ChatColor.GOLD + SUtil.commaify(price) + " Coin" + ((price != 1L) ? "s" : ""));
            lore.add(" ");
            lore.add(ChatColor.YELLOW + "Click to trade!");
            final SpecificItemType type = item.getType().getStatistics().getSpecificType();
            if (type == null || type.isStackable()) {
                lore.add(ChatColor.YELLOW + "Right-Click for more trading options!");
            }
            meta.setLore(lore);
            item.getStack().setItemMeta(meta);
            final int finalI = i;
            this.set(new GUIClickableItem() {
                @Override
                public void run(final InventoryClickEvent e) {
                    if ((type == null || type.isStackable()) && e.getClick() == ClickType.RIGHT) {
                        new ShopTradingOptionsGUI(p.get(finalI), ShopGUI.this).open(player);
                        return;
                    }
                    if (price > user.getCoins()) {
                        player.sendMessage(ChatColor.RED + "You don't have enough coins!");
                        return;
                    }
                    final Map<Integer, ItemStack> m = player.getInventory().addItem(p.get(finalI).clone().getStack());
                    if (m.size() != 0) {
                        player.sendMessage(ChatColor.RED + "Free up inventory space to purchase this!");
                        return;
                    }
                    player.playSound(player.getLocation(), Sound.NOTE_PLING, 1.0f, 2.0f);
                    user.subCoins(price);
                }

                @Override
                public int getSlot() {
                    return slot;
                }

                @Override
                public ItemStack getItem() {
                    return item.getStack();
                }
            });
        }
    }

    @Override
    public void onBottomClick(final InventoryClickEvent e) {
        final ItemStack current = e.getCurrentItem();
        if (current == null) {
            return;
        }
        if (current.getType() == Material.AIR) {
            return;
        }
        SItem item = SItem.find(current);
        if (item == null) {
            item = SItem.convert(current);
        }
        e.setCancelled(true);
        final Player player = (Player) e.getWhoClicked();
        final User user = User.getUser(player.getUniqueId());
        StackArrayList<SItem> buyback = ShopGUI.BUYBACK_HISTORY.get(player.getUniqueId());
        if (buyback == null) {
            ShopGUI.BUYBACK_HISTORY.put(player.getUniqueId(), new StackArrayList<SItem>());
            buyback = ShopGUI.BUYBACK_HISTORY.get(player.getUniqueId());
        }
        buyback.push(item.clone());
        if (item.getItemValue() == null) return;
        final long value = item.getItemValue() * item.getStack().getAmount();
        user.addCoins(value);
        player.playSound(player.getLocation(), Sound.NOTE_PLING, 1.0f, 2.0f);
        player.sendMessage(ChatColor.GREEN + "You sold " + item.getFullName() + ChatColor.DARK_GRAY + " x" + item.getStack().getAmount() + ChatColor.GREEN + " for " + ChatColor.GOLD + SUtil.commaify(value) + " Coin" + ((value != 1L) ? "s" : "") + ChatColor.GREEN + "!");
        player.getInventory().setItem(e.getSlot(), new ItemStack(Material.AIR));
        new ShopGUI(this.title, this.page, this.items).open(player);
    }
}
