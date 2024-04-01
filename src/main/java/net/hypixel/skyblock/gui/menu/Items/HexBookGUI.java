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
package net.hypixel.skyblock.gui.menu.Items;


import net.hypixel.skyblock.features.dungeons.stats.ItemSerial;
import net.hypixel.skyblock.gui.GUI;
import net.hypixel.skyblock.gui.GUIClickableItem;
import net.hypixel.skyblock.item.GenericItemType;
import net.hypixel.skyblock.item.MaterialStatistics;
import net.hypixel.skyblock.item.SItem;
import net.hypixel.skyblock.util.SUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

public class HexBookGUI extends GUI {
    private SItem item;

    public boolean forceclose = false;

    public HexBookGUI(SItem item) {
        super("The Hex -> Books", 54);
        fill(BLACK_STAINED_GLASS_PANE);

        this.item = item;

        set(19, item.getStack());
        set(new GUIClickableItem() {

            @Override
            public void run(InventoryClickEvent e) {
                e.getWhoClicked().sendMessage(SUtil.color("&d&lSUCCESS! &r&aYou applied all selected modifications to your " + item.getFullName() + "!"));

                forceclose = true;
                Player p = (Player) e.getWhoClicked();
                p.playSound(p.getLocation(), Sound.ORB_PICKUP, 10f, 10f);

                new HexGUI(p.getPlayer(), item).open(p);
            }

            @Override
            public ItemStack getItem() {
                return SUtil.getStack(
                        "&aApply Books And Stars",
                        Material.ANVIL,
                        (short) 0,
                        1,
                        "&7Knowledge is &6power&7! Apply",
                        "&7special books to your item to",
                        "&7upgrade it!"
                );
            }

            @Override
            public int getSlot() {
                return 28;
            }
        });

        MaterialStatistics statistics = item.getType().getStatistics();
        if (statistics.getType().equals(GenericItemType.WEAPON) || statistics.getType().equals(GenericItemType.ARMOR)) {
            set(new GUIClickableItem() {
                @Override
                public void run(InventoryClickEvent e) {
                    int current = item.getHPBs();
                    if (current < 10) {
                        item.setHPBs(current + 1);
                        ((Player) e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), Sound.ANVIL_USE, 10, 2);
                        e.getWhoClicked().sendMessage(SUtil.color("&aYou added 1 Hot Potato book to your item!"));
                    } else {
                        e.getWhoClicked().sendMessage(SUtil.color("&cYou have already applied the maximum amount of Hot Potato Books to this item!"));
                        ((Player) e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), Sound.VILLAGER_NO, 10, 1);
                    }
                }

                @Override
                public ItemStack getItem() {
                    return SUtil.getStack(
                            "&5Hot Potato Book",
                            Material.BOOK,
                            (short) 0,
                            1,
                            "&7When applied to armor, grants",
                            "&a+2❈ Defense&7 and &c+4❤",
                            "&cHealth&7.",
                            "&7",
                            "&7When applied to weapons, grants",
                            "&c+2❁ Strength&7 and &c+❁2",
                            "&cDamage&7.",
                            "&7",
                            "&7This can be applied to an item",
                            "&7up to &a10&7 times!"
                    );
                }

                @Override
                public int getSlot() {
                    return 22;
                }
            });

            set(new GUIClickableItem() {

                @Override
                public void run(InventoryClickEvent e) {
                    int amount = item.getStar();
                    if(item.getStar() == 5) return;
                    ItemSerial is = ItemSerial.createBlank();
                    is.saveTo(item);
                    item.setStarAmount(item.getStar() + amount);
                        ((Player) e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), Sound.ANVIL_USE, 10, 2);
                        e.getWhoClicked().sendMessage(SUtil.color("&aYou added 1 Star to your item!"));
                }

                @Override
                public ItemStack getItem() {
                    return SUtil.getSkullURLStack(ChatColor.RED+"Add Star", "8216ee40593c0981ed28f5bd674879781c425ce0841b687481c4f7118bb5c3b1", 1, ChatColor.GRAY+"Add or remove stars", ChatColor.GRAY+"from your items.");
                }

                @Override
                public int getSlot() {
                    return 23;
                }
            });
        }


    }

    @Override
    public void onClose(InventoryCloseEvent e){
        if(!forceclose) {
            if (item != null) {
                e.getPlayer().getInventory().addItem(item.getStack());
            }
        }
    }

}
