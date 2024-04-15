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
package net.hypixel.skyblock.gui;


import net.hypixel.skyblock.features.reforge.Reforge;
import net.hypixel.skyblock.features.reforge.ReforgeType;
import net.hypixel.skyblock.item.GenericItemType;
import net.hypixel.skyblock.item.SItem;
import net.hypixel.skyblock.item.SMaterial;
import net.hypixel.skyblock.user.PlayerUtils;
import net.hypixel.skyblock.util.SUtil;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AccessoryReforges extends GUI {
    private static final int[] INTERIOR = new int[]{
            10, 11, 12, 13, 14, 15, 16,
            19, 20, 21, 22, 23, 24, 25,
            28, 29, 30, 31, 32, 33, 34,
            37, 38, 39, 40, 41, 42, 43
    };

    public AccessoryReforges() {
        super("Accessory Reforges", 54);
        border(BLACK_STAINED_GLASS_PANE);

        List<SItem> stones = new ArrayList<>();

        for (ReforgeType type : ReforgeType.values()) {
            if (type.getReforge().getCompatibleTypes().contains(GenericItemType.ACCESSORY) && type.isAccessible()) {
                SItem stone = SItem.of(SMaterial.REFORGE_STONE);

                stone.setReforge(type.getReforge());
                stone.setDisplayName("Reforge");

                stones.add(stone);
            }
        }

        for (int i = 0; i < stones.size(); i++) {
            int slot = INTERIOR[i];
            SItem sItem = stones.get(i);
            ItemStack item = stones.get(i).getStack();
            set(new GUIClickableItem() {

                @Override
                public void run(InventoryClickEvent e) throws IOException {
                    Player player = (Player) e.getWhoClicked();

                    List<SItem> accessories = PlayerUtils.getAccessories(player);
                    if (accessories == null) {
                        player.sendMessage(SUtil.color("&cYou do not have any accessories in your bag!"));
                        return;
                    }
                    player.sendMessage(SUtil.color("&aYou applied the " + sItem.getReforge().getName() + " reforge to all your accessories!"));
                    player.playSound(player.getLocation(), Sound.ANVIL_USE, 10, 2);

                    List<ItemStack> stacks = new ArrayList<>();

                    for (SItem accessory : accessories) {
                        if (accessory == null) continue;
                        accessory.setReforge(sItem.getReforge());
                        stacks.add(accessory.getStack());
                    }
                }

                @Override
                public ItemStack getItem() {
                    return item;
                }

                @Override
                public int getSlot() {
                    return slot;
                }
            });
        }

        set(GUIClickableItem.getCloseItem(49));
    }
}
