
package net.hypixel.skyblock.gui.menu.Items;



import net.hypixel.skyblock.SkyBlock;
import net.hypixel.skyblock.gui.*;
import net.hypixel.skyblock.item.GenericItemType;
import net.hypixel.skyblock.item.SItem;
import net.hypixel.skyblock.item.SpecificItemType;
import net.hypixel.skyblock.util.SUtil;
import net.hypixel.skyblock.util.Sputnik;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class HexGUI extends GUI {


    public boolean forceclose = false;
    private Inventory inventory;
    SItem upgradeableItem;

    public HexGUI(Player player, SItem item) {
        super("The Hex", 54);
        fill(BLACK_STAINED_GLASS_PANE);
        this.upgradeableItem = item;

        //Slot Handle
        //**Reforges
        set(new GUIClickableItem() {
            @Override
            public void run(InventoryClickEvent e) {
                if (upgradeableItem == null) {
                    player.sendMessage(ChatColor.RED + "You must put an item into the hex to use this menu!");
                    player.playSound(player.getLocation(), Sound.VILLAGER_NO, 10, 1);
                } else {
                    forceclose = true;
                   // GUIType.REFORGE_ANVIL.getGUI().open(player);
                    new HexReforgesGUI(upgradeableItem).open(player);
                    upgradeableItem = null;
                }
            }

            @Override
            public int getSlot() {
                return 33;
            }

            @Override
            public ItemStack getItem() {
                return SUtil.getSkullURLStack(Sputnik.trans("&aReforges"), "94af5f3b50e2dea2687d292cc3a5e42c028b8626e638842fb4f78671debe276c", 1,
                        Sputnik.trans3("&7Apply &aReforges&7 to your item",
                                "&7with &aReforge Stones&7 or by",
                                "&7rolling a &brandom&7 reforge.")
                );
            }
        });
        //**Modifiers
        set(new GUIClickableItem() {
            @Override
            public void run(InventoryClickEvent e) {
                if (upgradeableItem == null) {
                    player.sendMessage(ChatColor.RED + "You must put an item into the hex to use this menu!");
                    player.playSound(player.getLocation(), Sound.VILLAGER_NO, 10, 1);
                } else {
                    forceclose = true;
                    new HexModifiersGUI(upgradeableItem).open(player);
                    upgradeableItem = null;
                }
            }

            @Override
            public int getSlot() {
                return 25;
            }

            @Override
            public ItemStack getItem() {
                return SUtil.getSkullURLStack(Sputnik.trans("&aModifiers"), "57ccd36dc8f72adcb1f8c8e61ee82cd96ead140cf2a16a1366be9b5a8e3cc3fc", 1,
                        Sputnik.trans5("&7Apply miscellaneous item",
                                "&7modifiers like the",
                                "&6Recombobulator 3000&7,",
                                "&5Wither Scrolls&7, and &cMaster",
                                "&cStars&7!")
                );
            }
        });
        //**Enchantments
        set(new GUIClickableItem() {
            @Override
            public void run(InventoryClickEvent e) {
                if (upgradeableItem == null) {
                    player.sendMessage(ChatColor.RED + "You must put an item into the hex to use this menu!");
                    player.playSound(player.getLocation(), Sound.VILLAGER_NO, 10, 1);
                } else {
                    forceclose = true;
                    new HexEnchantments(upgradeableItem).open(player);
                    upgradeableItem = null;
                }
            }

            @Override
            public int getSlot() {
                return 15;
            }

            @Override
            public ItemStack getItem() {
                return SUtil.getStack(Sputnik.trans("&aEnchantments"), Material.ENCHANTMENT_TABLE, (short) 0, 1,
                        Sputnik.trans5("&7This special &aEnchantment Table",
                                "&7allows you to access way more",
                                "&7Enchantments and gives you the",
                                "&7option to consume &aBottles of",
                                "&aEnchanting&7 directly!")
                );
            }
        });

        //*Ultimate Enchantments
        set(new GUIClickableItem() {
            @Override
            public void run(InventoryClickEvent e) {
                if (upgradeableItem == null) {
                    player.sendMessage(ChatColor.RED + "You must put an item into the hex to use this menu!");
                    player.playSound(player.getLocation(), Sound.VILLAGER_NO, 10, 1);
                } else {
                    forceclose = true;
                    new HexUltimateEnchantments(upgradeableItem).open(player);
                    upgradeableItem = null;
                }
            }

            @Override
            public int getSlot() {
                return 16;
            }

            @Override
            public ItemStack getItem() {
                return SUtil.getStack(Sputnik.trans("&aUltimate Enchantments"), Material.BOOK_AND_QUILL, (short) 0, 1,
                        Sputnik.trans4("&7Allows you to apply &d&lUltimate",
                                "&d&lEnchantments&r&7 and gives you the",
                                "&7option to consume &aBottles of",
                                "&aEnchanting&7 directly!")
                );
            }
        });

        //*Book Modifiers
        set(new GUIClickableItem() {
            @Override
            public void run(InventoryClickEvent e) {
                if (upgradeableItem == null) {
                    player.sendMessage(ChatColor.RED + "You must put an item into the hex to use this menu!");
                    player.playSound(player.getLocation(), Sound.VILLAGER_NO, 10, 1);
                } else {
                    forceclose = true;
                    new HexBookGUI(upgradeableItem).open(player);
                    upgradeableItem = null;
                }

            }

            @Override
            public int getSlot() {
                return 24;
            }

            @Override
            public ItemStack getItem() {
                return SUtil.getStack(Sputnik.trans("&aBooks"), Material.BOOK, (short) 0, 1,
                        Sputnik.trans3("&7Knowledge is &6power!&7 Apply",
                                "&7special books to your item to",
                                "&7upgrade it!")
                );
            }
        });

        set(new GUIClickableItem() {
            @Override
            public void run(InventoryClickEvent p0) {
            }

            @Override
            public ItemStack getItem() {
                if (upgradeableItem != null) {
                    return upgradeableItem.getStack();
                } else {
                    // Handle the situation when upgradeableItem is null
                    return new ItemStack(Material.AIR); // Return a default item or null item
                }
            }

            @Override
            public int getSlot() {
                return 22;
            }
        });

        //Borders
        set(new GUIItem() {
            @Override
            public int getSlot() {
                return 12;
            }

            @Override
            public ItemStack getItem() {
                return SUtil.createColoredStainedGlassPane((short) 10, " ");
            }
        });

        set(new GUIItem() {
            @Override
            public int getSlot() {
                return 13;
            }

            @Override
            public ItemStack getItem() {
                return SUtil.createColoredStainedGlassPane((short) 10, " ");
            }
        });

        set(new GUIItem() {
            @Override
            public int getSlot() {
                return 14;
            }

            @Override
            public ItemStack getItem() {
                return SUtil.createColoredStainedGlassPane((short) 10, " ");
            }
        });

        set(new GUIItem() {
            @Override
            public int getSlot() {
                return 21;
            }

            @Override
            public ItemStack getItem() {
                return SUtil.createColoredStainedGlassPane((short) 10, " ");
            }
        });



        set(GUIClickableItem.getCloseItem(49));

        set(new GUIItem() {
            @Override
            public int getSlot() {
                return 23;
            }

            @Override
            public ItemStack getItem() {
                return SUtil.createColoredStainedGlassPane((short) 10, " ");
            }
        });

        set(new GUIItem() {
            @Override
            public int getSlot() {
                return 30;
            }

            @Override
            public ItemStack getItem() {
                return SUtil.createColoredStainedGlassPane((short) 10, " ");
            }
        });

        set(new GUIItem() {
            @Override
            public int getSlot() {
                return 31;
            }

            @Override
            public ItemStack getItem() {
                return SUtil.createColoredStainedGlassPane((short) 10, " ");
            }
        });

        set(new GUIItem() {
            @Override
            public int getSlot() {
                return 32;
            }

            @Override
            public ItemStack getItem() {
                return SUtil.createColoredStainedGlassPane((short) 10, " ");
            }
        });

    }


    @Override
    public void onClose(InventoryCloseEvent e){
        if(!forceclose){
            if(upgradeableItem != null){
                e.getPlayer().getInventory().addItem(upgradeableItem.getStack());
            }
        }
    }

    @Override
    public void onOpen(GUIOpenEvent e) throws IOException {
        inventory = e.getInventory();
    }

    @Override
    public void onBottomClick(InventoryClickEvent e) {
        ItemStack selected = e.getCurrentItem();
        if (selected == null || selected.getType() == Material.AIR) {
            return;
        }

        SItem item = SItem.find(selected);
        if (item == null) {
            item = SItem.convert(selected);
        }
        PlayerInventory playerInventory = e.getWhoClicked().getInventory();
        upgradeableItem = item;
        playerInventory.remove(selected);
        inventory.setItem(22, upgradeableItem.getStack());
    }

    public HexGUI(Player player) {
        this(player, null);
    }

//    @Override
//    public void update(Inventory inventory) {
//        new BukkitRunnable() {
//            @Override
//            public void run() {
//
//                if (upgradeableItem == null) return;
//
//            }
//        }.runTaskLater(SkyBlock.getPlugin(), 1);
//    }

}