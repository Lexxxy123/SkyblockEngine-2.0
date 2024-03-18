package net.hypixel.skyblock.gui;

import net.hypixel.skyblock.item.SItem;
import net.hypixel.skyblock.item.SMaterial;
import net.hypixel.skyblock.util.PaginationList;
import net.hypixel.skyblock.util.SUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class AdminItemBrowser extends GUI{
    private static final int[] INTERIOR = new int[]{10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34, 37, 38, 39, 40, 41, 42, 43};

    public AdminItemBrowser(String query, int page) {
        super("Admin Item Browser", 54);
        this.border(BLACK_STAINED_GLASS_PANE);
        PaginationList<SMaterial> pagedMaterials = new PaginationList(28);
        for (SMaterial mat : SMaterial.values()) {
            if (mat.name().toLowerCase().contains("dwarven_") || mat.name().toLowerCase().contains("hidden_") || mat.name().toLowerCase().contains("bzb") || mat.name().toLowerCase().contains("god_pot")) {
                pagedMaterials.add(mat);
            }
        }


        if (!query.isEmpty()) {
            pagedMaterials.removeIf((material) -> {
                return !material.name().toLowerCase().contains(query.replaceAll(" ", "_").toLowerCase());
            });
        }

        if (pagedMaterials.isEmpty()) {
            page = 0;
        }

        this.title = "Admin Item Browser (" + page + "/" + pagedMaterials.getPageCount() + ")";
        if (page > 1) {
            int finalPage = page;
            this.set(new GUIClickableItem() {
                public void run(InventoryClickEvent e) {
                    (new AdminItemBrowser(finalPage - 1)).open((Player) e.getWhoClicked());
                    ((Player) e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), Sound.NOTE_PIANO, 1.0F, 1.0F);
                }

                public int getSlot() {
                    return 45;
                }

                public ItemStack getItem() {
                    return SUtil.createNamedItemStack(Material.ARROW, ChatColor.GRAY + "Pervious Page");
                }
            });
        }

        if (page != pagedMaterials.getPageCount()) {
            int finalPage1 = page;
            this.set(new GUIClickableItem() {
                public void run(InventoryClickEvent e) {
                    (new AdminItemBrowser(finalPage1 + 1)).open((Player) e.getWhoClicked());
                    ((Player) e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), Sound.NOTE_PIANO, 1.0F, 1.0F);
                }

                public int getSlot() {
                    return 53;
                }

                public ItemStack getItem() {
                    return SUtil.createNamedItemStack(Material.ARROW, ChatColor.GRAY + "Next Page");
                }
            });
        }

        this.set(new GUIQueryItem() {
            public GUI onQueryFinish(String query) {
                return new AdminItemBrowser(query);
            }

            public void run(InventoryClickEvent e) {
            }

            public int getSlot() {
                return 48;
            }

            public ItemStack getItem() {
                return SUtil.createNamedItemStack(Material.SIGN, ChatColor.GREEN + "Search");
            }
        });
        this.set(GUIClickableItem.getCloseItem(50));
        List<SMaterial> p = pagedMaterials.getPage(page);
        if (p != null) {
            for (int i = 0; i < p.size(); ++i) {
                final int slot = INTERIOR[i];
                SItem sItem = SItem.of(p.get(i), p.get(i).getData());

                this.set(new GUIClickableItem() {
                    public void run(InventoryClickEvent e) {
                        Player player = (Player) e.getWhoClicked();
                        player.playSound(player.getLocation(), Sound.NOTE_PLING, 1.0F, 2.0F);
                        player.sendMessage(ChatColor.GOLD + sItem.getType().getDisplayName(sItem.getVariant()) + ChatColor.GREEN + " has been added to your inventory.");
                        player.getInventory().addItem(SItem.of(sItem.getType(), sItem.getVariant()).getStack());
                    }

                    public int getSlot() {
                        return slot;
                    }

                    public ItemStack getItem() {
                        return sItem.getStack();
                    }
                });
            }

        }
    }

    public AdminItemBrowser(String query) {
        this(query, 1);
    }

    public AdminItemBrowser(int page) {
        this("", page);
    }

    public AdminItemBrowser() {
        this(1);
    }
}
