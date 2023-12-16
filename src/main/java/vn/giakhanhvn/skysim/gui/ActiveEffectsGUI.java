package vn.giakhanhvn.skysim.gui;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import vn.giakhanhvn.skysim.SkySimEngine;
import vn.giakhanhvn.skysim.potion.ActivePotionEffect;
import vn.giakhanhvn.skysim.user.User;
import vn.giakhanhvn.skysim.util.PaginationList;
import vn.giakhanhvn.skysim.util.SUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ActiveEffectsGUI extends GUI {
    private static final int[] INTERIOR;
    private int page;

    public ActiveEffectsGUI(final int page) {
        super("Active Effects", 54);
        this.page = page;
    }

    public ActiveEffectsGUI() {
        this(1);
    }

    @Override
    public void onOpen(final GUIOpenEvent e) {
        final Player player = e.getPlayer();
        final User user = User.getUser(player.getUniqueId());
        this.border(ActiveEffectsGUI.BLACK_STAINED_GLASS_PANE);
        final PaginationList<ActivePotionEffect> paged = new PaginationList<ActivePotionEffect>(28);
        paged.addAll(user.getEffects());
        if (paged.size() == 0) {
            this.page = 0;
        }
        final int finalPage = this.page;
        this.title = "(" + this.page + "/" + paged.getPageCount() + ") Active Effects";
        if (this.page > 1) {
            this.set(new GUIClickableItem() {
                @Override
                public void run(final InventoryClickEvent e) {
                    new ActiveEffectsGUI(finalPage - 1).open((Player) e.getWhoClicked());
                }

                @Override
                public int getSlot() {
                    return 45;
                }

                @Override
                public ItemStack getItem() {
                    return SUtil.createNamedItemStack(Material.ARROW, ChatColor.GRAY + "Go Back");
                }
            });
        }
        if (this.page != paged.getPageCount()) {
            this.set(new GUIClickableItem() {
                @Override
                public void run(final InventoryClickEvent e) {
                    new ActiveEffectsGUI(finalPage + 1).open((Player) e.getWhoClicked());
                }

                @Override
                public int getSlot() {
                    return 53;
                }

                @Override
                public ItemStack getItem() {
                    return SUtil.createNamedItemStack(Material.ARROW, ChatColor.GRAY + "Next Page");
                }
            });
        }
        this.set(4, SUtil.getStack(ChatColor.GREEN + "Active Effects", Material.POTION, (short) 0, 1, ChatColor.GRAY + "View and manage all of your", ChatColor.GRAY + "active potion effects.", " ", ChatColor.GRAY + "Drink Potions or splash them", ChatColor.GRAY + "on the ground to buff yourself!", " ", ChatColor.GRAY + "Currently Active: " + ChatColor.YELLOW + user.getEffects().size()));
        this.set(GUIClickableItem.createGUIOpenerItem(GUIType.SKYBLOCK_MENU, player, ChatColor.GREEN + "Go Back", 48, Material.ARROW, ChatColor.GRAY + "To SkySim Menu"));
        this.set(GUIClickableItem.getCloseItem(49));
        final List<ActivePotionEffect> p = paged.getPage(this.page);
        if (p == null) {
            return;
        }
        for (int i = 0; i < p.size(); ++i) {
            final int slot = ActiveEffectsGUI.INTERIOR[i];
            final ActivePotionEffect effect = p.get(i);
            final List<String> lore = new ArrayList<String>(Collections.singletonList(" "));
            for (final String line : SUtil.splitByWordAndLength(effect.getEffect().getDescription(), 20, "\\s")) {
                lore.add(ChatColor.GRAY + line);
            }
            lore.add(" ");
            lore.add(ChatColor.GRAY + "Remaining: " + ChatColor.WHITE + effect.getRemainingDisplay());
            lore.add(" ");
            lore.add(SUtil.findPotionRarity(effect.getEffect().getLevel()).getDisplay());
            this.set(slot, SUtil.getStack(effect.getEffect().getType().getName() + " " + SUtil.toRomanNumeral(effect.getEffect().getLevel()), Material.POTION, effect.getEffect().getType().getColor().getData(), 1, lore));
        }
        new BukkitRunnable() {
            public void run() {
                if (ActiveEffectsGUI.this != GUI.GUI_MAP.get(player.getUniqueId())) {
                    return;
                }
                new ActiveEffectsGUI(ActiveEffectsGUI.this.page).open(player);
            }
        }.runTaskLater(SkySimEngine.getPlugin(), 20L);
    }

    static {
        INTERIOR = new int[]{10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34, 37, 38, 39, 40, 41, 42, 43};
    }
}
