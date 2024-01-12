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


import in.godspunky.skyblock.gui.GUI;
import in.godspunky.skyblock.gui.GUIClickableItem;
import in.godspunky.skyblock.item.SItem;
import in.godspunky.skyblock.item.SMaterial;
import in.godspunky.skyblock.util.SUtil;
import in.godspunky.skyblock.util.Sputnik;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class HexModifiersGUI extends GUI {
    SItem upgradeableItem;

    public HexModifiersGUI(SItem item) {
        super("The Hex -> Modifiers", 54);
        fill(BLACK_STAINED_GLASS_PANE);

        //Recombobulator
        set(new GUIClickableItem() {
            @Override
            public void run(InventoryClickEvent e) {
                upgradeableItem = item;
                item.setRecombobulated(true);

                Player player = (Player) e.getWhoClicked();
                player.playSound(player.getLocation(), Sound.ORB_PICKUP, 10, 2);

                e.getWhoClicked().sendMessage(ChatColor.translateAlternateColorCodes('&', "&aYou recombobulated your " + item.getFullName() + "!"));
            }

            @Override
            public int getSlot() {
                return 23;
            }

            @Override
            public ItemStack getItem() {
                return SItem.of(SMaterial.RECOMBOBULATOR_3000).getStack();
            }
        });

        //Close ( with rizz )
        set(new GUIClickableItem() {
            @Override
            public void run(InventoryClickEvent e) {
                e.getWhoClicked().sendMessage(ChatColor.translateAlternateColorCodes('&', "&d&lSUCCESS! &r&dYou modified your " + item.getFullName() + "!"));

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
                return SUtil.getStack("&aApply Modifiers", Material.ANVIL, (short) 0, 1,
                        Sputnik.trans5("&7Apply miscellaneous item",
                        "&7modifiers like the",
                        "&6Recombobulator 3000&7,",
                        "&5Wither Scrolls&7, and &cMaster",
                        "&cStars&7!")
                );
            }
        });

        set(GUIClickableItem.getCloseItem(49));
        set(19, item.getStack());

    }

}
