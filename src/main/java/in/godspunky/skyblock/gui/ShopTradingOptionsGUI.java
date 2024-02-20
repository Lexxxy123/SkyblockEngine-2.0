package in.godspunky.skyblock.gui;

import in.godspunky.skyblock.item.SItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import in.godspunky.skyblock.user.User;
import in.godspunky.skyblock.util.SUtil;

import java.util.List;
import java.util.Map;

public class ShopTradingOptionsGUI extends GUI {
    private final SItem item;
    private final GUI ret;

    public ShopTradingOptionsGUI(final SItem item, final GUI ret) {
        super("Shop Trading Options", 54);
        this.item = item;
        this.ret = ret;
    }

    @Override
    public void onOpen(final GUIOpenEvent e) {
        final Player player = e.getPlayer();
        this.fill(BLACK_STAINED_GLASS_PANE);
        this.set(createTrade(this.item, 20, 1, player));
        this.set(createTrade(this.item, 21, 5, player));
        this.set(createTrade(this.item, 22, 10, player));
        this.set(createTrade(this.item, 23, 32, player));
        this.set(createTrade(this.item, 24, 64, player));
        this.set(GUIClickableItem.createGUIOpenerItem(this.ret, player, ChatColor.GREEN + "Go Back", 48, Material.ARROW, (short) 0, ChatColor.GRAY + "To " + this.ret.getTitle()));
        this.set(GUIClickableItem.getCloseItem(49));
    }

    private static GUIClickableItem createTrade(final SItem item, final int slot, final int amount, final Player player) {
        final User user = User.getUser(player.getUniqueId());
        final SItem display = item.clone();
        display.getStack().setAmount(amount);
        final ItemMeta meta = display.getStack().getItemMeta();
        if (amount != 1) {
            meta.setDisplayName(meta.getDisplayName() + ChatColor.DARK_GRAY + " x" + amount);
        }
        final List<String> lore = meta.getLore();
        lore.add(" ");
        lore.add(ChatColor.GRAY + "Cost");
        if (item.getPrice() == null) return null;
        final long price = item.getPrice() * amount;
        lore.add(ChatColor.GOLD + SUtil.commaify(price) + " Coin" + ((price != 1L) ? "s" : ""));
        lore.add(" ");
        lore.add(ChatColor.YELLOW + "Click to purchase!");
        meta.setLore(lore);
        display.getStack().setItemMeta(meta);
        return new GUIClickableItem() {
            @Override
            public void run(final InventoryClickEvent e) {
                if (price > user.getCoins()) {
                    player.sendMessage(ChatColor.RED + "You don't have enough coins!");
                    return;
                }
                final Map<Integer, ItemStack> m = player.getInventory().addItem(SUtil.setSItemAmount(item.clone(), amount).getStack());
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
                return display.getStack();
            }
        };
    }
}
