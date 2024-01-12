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


import in.godspunky.skyblock.enchantment.Enchantment;
import in.godspunky.skyblock.enchantment.EnchantmentType;
import in.godspunky.skyblock.gui.GUI;
import in.godspunky.skyblock.gui.GUIClickableItem;
import in.godspunky.skyblock.item.SItem;
import in.godspunky.skyblock.item.SMaterial;
import in.godspunky.skyblock.util.PaginationList;
import in.godspunky.skyblock.util.SUtil;
import in.godspunky.skyblock.util.Sputnik;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;


import java.util.ArrayList;
import java.util.List;

public class HexEnchantments extends GUI {
    SItem upgradeableItem;
    List<EnchantmentType> selected = new ArrayList<>();
    List<EnchantmentType> exists = new ArrayList<>();

    private static final int[] INTERIOR = new int[]{
            12, 13, 14, 15, 16,
            21, 22, 23, 24, 25,
            30, 31, 32, 33, 34,
    };

    public HexEnchantments(SItem item) {
        this(item, 1);
    }

    public HexEnchantments(SItem item, int page) {
        super("The Hex -> Enchantments", 54);
        fill(BLACK_STAINED_GLASS_PANE);
        int finalPage = page;

        PaginationList<SItem> pagedEnchantBooks = new PaginationList<>(15);

        for (EnchantmentType type : EnchantmentType.getENCHANTMENT_TYPE_CACHE().values()) {
            if (type.isUltimate()) continue;
            if (!(type.getCompatibleTypes().contains(item.getType().getStatistics().getSpecificType()))) continue;
            //banned enchantments list :3

            SItem bookItem = SItem.of(SMaterial.ENCHANTED_BOOK);

            bookItem.addEnchantment(type, 5);

            pagedEnchantBooks.add(bookItem);
        }

        for (Enchantment enchantment : item.getEnchantments()) {
            exists.add(enchantment.getType());
        }

        List<SItem> items = pagedEnchantBooks.getPage(page);
        if (items == null) return;

        if (page > 1)
        {
            set(new GUIClickableItem()
            {
                @Override
                public void run(InventoryClickEvent e)
                {
                    new HexEnchantments(item,finalPage - 1).open((Player) e.getWhoClicked());
                }
                @Override
                public int getSlot()
                {
                    return 45;
                }
                @Override
                public ItemStack getItem()
                {
                    return SUtil.createNamedItemStack(Material.ARROW, ChatColor.GRAY + "<-");
                }
            });
        }
        if (page != pagedEnchantBooks.getPageCount())
        {
            set(new GUIClickableItem()
            {
                @Override
                public void run(InventoryClickEvent e)
                {
                    new HexEnchantments(item,finalPage + 1).open((Player) e.getWhoClicked());
                }
                @Override
                public int getSlot()
                {
                    return 53;
                }
                @Override
                public ItemStack getItem()
                {
                    return SUtil.createNamedItemStack(Material.ARROW, ChatColor.GRAY + "->");
                }
            });
        }

        for (int i = 0; i < items.size(); i++) {
            int slot = INTERIOR[i];
            int finalI = i;
            set(new GUIClickableItem()
            {
                @Override
                public void run(InventoryClickEvent e)
                {
                    Player player = (Player) e.getWhoClicked();
                    player.playSound(player.getLocation(), Sound.ORB_PICKUP, 10, 2);


                    for (Enchantment enchantment : items.get(finalI).getEnchantments()) {
                        EnchantmentType type = enchantment.getType();

                       if (exists.contains(type)) {
                           exists.remove(type);
                           item.removeEnchantment(type);
                           player.sendMessage(ChatColor.RED + "You removed the enchantment: " + type.getName() + " from your item!");
                           return;
                       } else {
                           if (selected.contains(type)) {
                               player.sendMessage(ChatColor.RED + "You removed the Enchantment " + type.getName() + " from your selection!");
                               selected.remove(type);
                               return;
                           }
                           if (!selected.contains(type)) {
                               if (item.hasEnchantment(EnchantmentType.ONE_FOR_ALL)) {
                                   player.sendMessage(ChatColor.GREEN + "You added the Enchantment " + type.getName() + " to your selection but One For All was removed!");
                                   selected.add(type);

                                   item.removeEnchantment(EnchantmentType.ONE_FOR_ALL);
                               } else {
                                   player.sendMessage(ChatColor.GREEN + "You added the Enchantment " + type.getName() + " to your selection!");
                                   selected.add(type);
                               }

                               return;
                           }
                       }
                    }
                }
                @Override
                public int getSlot()
                {
                    return slot;
                }
                @Override
                public ItemStack getItem()
                {
                    return items.get(finalI).getStack();
                }
            });
        }



        set(GUIClickableItem.getCloseItem(49));
        set(19, item.getStack());

        set(new GUIClickableItem() {
            @Override
            public void run(InventoryClickEvent e) {
                for (EnchantmentType type : selected) {
                    item.addEnchantment(type, 5);
                }

                e.getWhoClicked().sendMessage(ChatColor.translateAlternateColorCodes('&', "&d&lSUCCESS! &r&dYou enchanted your " + item.getFullName() + "!"));

                Player p = (Player) e.getWhoClicked();
                p.playSound(p.getLocation(), Sound.ANVIL_USE, 10, 1);

                new HexGUI(p.getPlayer(), item).open(p);
            }

            @Override
            public int getSlot() {
                return 28;
            }

            @Override
            public ItemStack getItem() {
                return SUtil.getStack("&aEnchant Item", Material.ENCHANTMENT_TABLE, (short) 0, 1,
                        Sputnik.trans("&7Add and remove enchantments from"),
                        Sputnik.trans("&7the item in the slot above!")
                );
            }
        });

    }

}
