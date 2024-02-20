package in.godspunky.skyblock.gui;

import in.godspunky.skyblock.item.SItem;
import in.godspunky.skyblock.user.PlayerUtils;
import in.godspunky.skyblock.user.User;
import in.godspunky.skyblock.util.SUtil;
import in.godspunky.skyblock.util.Sputnik;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import in.godspunky.skyblock.item.SMaterial;

import java.util.Map;

public class QuiverGUI extends GUI {
    public QuiverGUI() {
        super("Quiver", 36);
        this.fill(BLACK_STAINED_GLASS_PANE, 27, 35);
        this.set(GUIClickableItem.getCloseItem(31));
    }

    @Override
    public void onOpen(final GUIOpenEvent e) {
        final Player player = e.getPlayer();
        this.set(GUIClickableItem.createGUIOpenerItem(GUIType.SKYBLOCK_MENU, e.getPlayer(), ChatColor.GREEN + "Go Back", 30, Material.ARROW));
        final User user = User.getUser(e.getPlayer().getUniqueId());
        final Inventory inventory = e.getInventory();
        for (final Map.Entry<SMaterial, Integer> entry : user.getQuiver().entrySet()) {
            inventory.addItem(SUtil.setStackAmount(SItem.of(entry.getKey()).getStack(), entry.getValue()));
        }
        this.set(new GUIClickableItem() {
            @Override
            public void run(final InventoryClickEvent e) {
                final Player p = (Player) e.getWhoClicked();
                if (p == null) {
                    return;
                }
                if (PlayerUtils.getCookieDurationTicks(p) <= 0L) {
                    p.sendMessage(Sputnik.trans("&cYou need the Cookie Buff active to use this feature!"));
                    p.playSound(p.getLocation(), Sound.VILLAGER_NO, 1.0f, 1.0f);
                    return;
                }
                for (int a = 0; a < 27; ++a) {
                    e.getInventory().setItem(a, null);
                }
                p.closeInventory();
                p.sendMessage(Sputnik.trans("&aSuccessfully cleared your Quiver!"));
                p.playSound(p.getLocation(), Sound.LEVEL_UP, 1.0f, 1.0f);
            }

            @Override
            public int getSlot() {
                return 34;
            }

            @Override
            public ItemStack getItem() {
                ItemStack isBuilder = new ItemStack(Material.BEDROCK, 1);
                String a = ChatColor.YELLOW + "Click to proceed";
                if (PlayerUtils.getCookieDurationTicks(player) <= 0L) {
                    a = ChatColor.RED + "Requires Cookie Buff!";
                }
                isBuilder = SUtil.getStack(ChatColor.RED + "Clear Quiver", Material.LAVA_BUCKET, (short) 0, 1, ChatColor.GRAY + "Click to clear your Quiver", ChatColor.GRAY + "instantly.", " ", a);
                return isBuilder;
            }
        });
        this.set(new GUIClickableItem() {
            @Override
            public void run(final InventoryClickEvent e) {
                final Player p = (Player) e.getWhoClicked();
                if (p == null) {
                    return;
                }
                if (PlayerUtils.getCookieDurationTicks(p) <= 0L) {
                    p.sendMessage(Sputnik.trans("&cYou need the Cookie Buff active to use this feature!"));
                    p.playSound(p.getLocation(), Sound.VILLAGER_NO, 1.0f, 1.0f);
                    return;
                }
                for (int a = 0; a < 27; ++a) {
                    final ItemStack arrow = SItem.of(SMaterial.ARROW).getStack();
                    arrow.setAmount(64);
                    e.getInventory().setItem(a, arrow);
                }
                p.closeInventory();
                p.sendMessage(Sputnik.trans("&aSuccessfully filled your Quiver!"));
                p.playSound(p.getLocation(), Sound.LEVEL_UP, 1.0f, 1.0f);
            }

            @Override
            public int getSlot() {
                return 35;
            }

            @Override
            public ItemStack getItem() {
                ItemStack isBuilder = new ItemStack(Material.BEDROCK, 1);
                String a = ChatColor.YELLOW + "Click to proceed";
                if (PlayerUtils.getCookieDurationTicks(player) <= 0L) {
                    a = ChatColor.RED + "Requires Cookie Buff!";
                }
                isBuilder = SUtil.getStack(ChatColor.GREEN + "Fill Quiver", Material.CHEST, (short) 0, 1, ChatColor.GRAY + "Click to fill your Quiver", ChatColor.GRAY + "instantly.", " ", a);
                return isBuilder;
            }
        });
    }

    @Override
    public void onClose(final InventoryCloseEvent e) {
        final User user = User.getUser(e.getPlayer().getUniqueId());
        final Inventory inventory = e.getInventory();
        user.clearQuiver();
        for (int i = 0; i < 27; ++i) {
            final ItemStack stack = inventory.getItem(i);
            SItem sItem = SItem.find(stack);
            if (sItem == null) {
                sItem = SItem.of(stack);
                if (sItem == null) {
                    continue;
                }
            }
            user.addToQuiver(sItem.getType(), stack.getAmount());
        }
        user.save();
    }
}
