/*
 * Copyright (C) 2023 by Ruby Game Studios
 * Skyblock is licensed under the Creative Commons Non-Commercial 4.0 International License.
 *
 * You may not use this software for commercial use, however you are free
 * to modify, copy, redistribute, or build upon our codebase. You must give
 * appropriate credit, provide a link to the license, and indicate
 * if changes were made.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE
 * OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 * For more information, visit https://creativecommons.org/licenses/by-nc/4.0/legalcode
 */
package in.godspunky.skyblock.gui.menu.Items;


import in.godspunky.skyblock.Skyblock;
import in.godspunky.skyblock.gui.GUI;
import in.godspunky.skyblock.gui.GUIClickableItem;
import in.godspunky.skyblock.gui.GUIItem;
import in.godspunky.skyblock.item.GenericItemType;
import in.godspunky.skyblock.item.SItem;
import in.godspunky.skyblock.item.SpecificItemType;
import in.godspunky.skyblock.util.SUtil;
import in.godspunky.skyblock.util.Sputnik;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class HexGUI extends GUI {
    SItem upgradeableItem;

    public HexGUI(Player player, SItem item) {
        super("The Hex", 54);
        fill(BLACK_STAINED_GLASS_PANE);

        //Slot Handle
        //**Reforges
        set(new GUIClickableItem() {
            @Override
            public void run(InventoryClickEvent e) {
                if (upgradeableItem == null) {
                    player.sendMessage(ChatColor.RED + "You must put an item into the hex to use this menu!");
                    player.playSound(player.getLocation(), Sound.VILLAGER_NO, 10, 1);
                } else {
                    new HexReforgesGUI(upgradeableItem).open(player);
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
                    new HexModifiersGUI(upgradeableItem).open(player);
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
                    new HexEnchantments(upgradeableItem).open(player);
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
                    new HexUltimateEnchantments(upgradeableItem).open(player);
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
                player.sendMessage(ChatColor.RED + "Comming Soon!");
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

        if (item != null) {
            set(22, item.getStack(), true);
            this.upgradeableItem = item;
        } else {
            set(22, null);
        }

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

    public HexGUI(Player player) {
        this(player, null);
    }

    @Override
    public void update(Inventory inventory) {
        new BukkitRunnable() {
            @Override
            public void run() {
                SItem hexItem = SItem.find(inventory.getItem(22));
                if (hexItem == null) {
                    upgradeableItem = null;
                    return;
                }

                if (!(hexItem.getType().getStatistics().getSpecificType() == SpecificItemType.SWORD || hexItem.getType().getStatistics().getSpecificType() == SpecificItemType.AXE || hexItem.getType().getStatistics().getSpecificType() == SpecificItemType.BOW || hexItem.getType().getStatistics().getType().equals(GenericItemType.ARMOR) || hexItem.getType().getStatistics().getType().equals(GenericItemType.ACCESSORY))) return;

                upgradeableItem = hexItem;
            }
        }.runTaskLater(Skyblock.getPlugin(), 1);
    }

}
