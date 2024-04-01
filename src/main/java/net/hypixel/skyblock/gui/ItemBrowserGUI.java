package net.hypixel.skyblock.gui;

import net.hypixel.skyblock.item.SItem;
import net.hypixel.skyblock.util.PaginationList;
import net.hypixel.skyblock.util.SUtil;
import net.minecraft.server.v1_8_R3.Item;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import net.hypixel.skyblock.item.SMaterial;

import java.util.List;

public class ItemBrowserGUI extends GUI {
    private static final int[] INTERIOR;

    public ItemBrowserGUI(final String query, int page) {
        super("Item Browser", 54);
        this.border(BLACK_STAINED_GLASS_PANE);
        final PaginationList<SMaterial> pagedMaterials = new PaginationList<SMaterial>(28);
        pagedMaterials.addAll(SMaterial.values());
        pagedMaterials.removeIf((mat) -> mat.name().toLowerCase().contains("dwarven_"));
        pagedMaterials.removeIf((mat) -> mat.name().toLowerCase().contains("hidden_"));
        pagedMaterials.removeIf(mat -> mat.name().toLowerCase().contains("spawn_egg"));
        pagedMaterials.removeIf(mat -> mat.name().toLowerCase().contains("gunga"));
        pagedMaterials.removeIf(mat -> mat.name().toLowerCase().contains("menu"));
        pagedMaterials.removeIf(mat -> mat.name().toLowerCase().contains("quiver"));
        pagedMaterials.removeIf(mat -> mat.name().toLowerCase().contains("exterminator"));
        pagedMaterials.removeIf(mat -> mat.name().toLowerCase().contains("voidbane"));
        pagedMaterials.removeIf(mat -> Item.getById(mat.getCraftMaterial().getId()) == null);
        if (!query.isEmpty()) {
            pagedMaterials.removeIf(material -> !material.name().toLowerCase().contains(query.replaceAll(" ", "_").toLowerCase()));
        }
        if (pagedMaterials.size() == 0) {
            page = 0;
        }
        this.title = "Item Browser (" + page + "/" + pagedMaterials.getPageCount() + ")";
        final int finalPage;
        if ((finalPage = page) > 1) {
            this.set(new GUIClickableItem() {
                @Override
                public void run(final InventoryClickEvent e) {
                    new ItemBrowserGUI(finalPage - 1).open((Player) e.getWhoClicked());
                    ((Player) e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), Sound.NOTE_PIANO, 1.0f, 1.0f);
                }

                @Override
                public int getSlot() {
                    return 45;
                }

                @Override
                public ItemStack getItem() {
                    return SUtil.createNamedItemStack(Material.ARROW, ChatColor.GRAY + "Pervious Page");
                }
            });
        }
        if (page != pagedMaterials.getPageCount()) {
            this.set(new GUIClickableItem() {
                @Override
                public void run(final InventoryClickEvent e) {
                    new ItemBrowserGUI(finalPage + 1).open((Player) e.getWhoClicked());
                    ((Player) e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), Sound.NOTE_PIANO, 1.0f, 1.0f);
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
        this.set(new GUIQueryItem() {
            @Override
            public GUI onQueryFinish(final String query) {
                return new ItemBrowserGUI(query);
            }

            @Override
            public void run(final InventoryClickEvent e) {
            }

            @Override
            public int getSlot() {
                return 48;
            }

            @Override
            public ItemStack getItem() {
                return SUtil.createNamedItemStack(Material.SIGN, ChatColor.GREEN + "Search");
            }
        });
        this.set(GUIClickableItem.getCloseItem(50));
        final List<SMaterial> p = pagedMaterials.getPage(page);
        if (p == null) {
            return;
        }
        for (int i = 0; i < p.size(); ++i) {
            final int slot = ItemBrowserGUI.INTERIOR[i];
            final SItem sItem = SItem.of(p.get(i), (byte) p.get(i).getData());
            this.set(new GUIClickableItem() {
                @Override
                public void run(final InventoryClickEvent e) {
                    final Player player = (Player) e.getWhoClicked();
                    player.playSound(player.getLocation(), Sound.NOTE_PLING, 1.0f, 2.0f);
                    player.sendMessage(ChatColor.GOLD + sItem.getType().getDisplayName(sItem.getVariant()) + ChatColor.GREEN + " has been added to your inventory.");
                    player.getInventory().addItem(SItem.of(sItem.getType(), sItem.getVariant()).getStack());
                }

                @Override
                public int getSlot() {
                    return slot;
                }

                @Override
                public ItemStack getItem() {
                    return sItem.getStack();
                }
            });
        }
    }

    public ItemBrowserGUI(final String query) {
        this(query, 1);
    }

    public ItemBrowserGUI(final int page) {
        this("", page);
    }

    public ItemBrowserGUI() {
        this(1);
    }

    static {
        INTERIOR = new int[]{10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34, 37, 38, 39, 40, 41, 42, 43};
    }
}
