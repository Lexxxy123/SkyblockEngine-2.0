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


import lombok.NonNull;
import lombok.SneakyThrows;

import net.hypixel.skyblock.api.serializer.BukkitSerializeClass;
import net.hypixel.skyblock.config.Config;
import net.hypixel.skyblock.item.GenericItemType;
import net.hypixel.skyblock.item.ItemListener;
import net.hypixel.skyblock.item.SItem;
import net.hypixel.skyblock.user.PlayerUtils;
import net.hypixel.skyblock.user.User;
import net.hypixel.skyblock.util.SLog;
import net.hypixel.skyblock.util.SUtil;
import net.hypixel.skyblock.util.Sputnik;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class AccessoryBag extends GUI{
    private int page;
    private int maxSlot;
    private int closePos;
    private Player player;
    private Inventory inventory;

    public AccessoryBag(int page, int size, int maxSlot, int closePos) {
        super("Accessory Bag", size);
        this.page = page;
        this.maxSlot = maxSlot;

        for (int i = 0; i < size; i++) {
            if (i >= maxSlot) {
                set(i, BLACK_STAINED_GLASS_PANE);
            }
        }

        this.closePos = closePos;
    }

    @Override
    public void onOpen(GUIOpenEvent e) {
        inventory = e.getInventory();
        player = e.getPlayer();
        loadPage();
    }

    @SneakyThrows
    @Override
    public void onClose(InventoryCloseEvent e) {
        savePage();
        ItemListener.updateStatistics(player);
    }

    @Override
    public void update(Inventory inventory) {
        this.inventory = inventory;
        savePage();
        ItemListener.updateStatistics(player);
    }

    @SneakyThrows
    private void savePage() {
        if (inventory == null) {
            return;
        }

        User user = User.getUser(player);
        File home = User.getDataDirectory();

        File raw = new File(home, user.getUuid() + "_accessory-bag.yml");
        if (!raw.exists()) {
            raw.createNewFile();
            SLog.info("Creating new Accessory Bag for player " + user.getUuid());
        }

        Config file = new Config(home, user.getUuid() + "_accessory-bag.yml");

        List<ItemStack> items = new ArrayList<>();
        for (int i = 0; i < inventory.getSize(); i++) {
            if (i > maxSlot || i == closePos || i == closePos - 1 || i == closePos + 1) {
                continue;
            }

            items.add(inventory.getItem(i));
        }

        file.set("bag.page." + SUtil.numToStr[page], BukkitSerializeClass.itemStackArrayToBase64(items.toArray(new ItemStack[0])));
        file.save();
    }

    @SneakyThrows
    public static void setAccessories(@NonNull Player player, int page, List<ItemStack> accessories) {
        User user = User.getUser(player);
        File home = User.getDataDirectory();

        File raw = new File(home, user.getUuid() + "_accessory-bag.yml");
        if (!raw.exists()) {
            raw.createNewFile();
        }

        Config file = new Config(home, user.getUuid() + "_accessory-bag.yml");

        file.set("bag.page." + SUtil.numToStr[page], BukkitSerializeClass.itemStackArrayToBase64(accessories.toArray(new ItemStack[0])));
        file.save();

        ItemListener.updateStatistics(player);
    }

    public static @Nullable List<ItemStack> getAccessories(@NonNull Player player, int page) {
        User user = User.getUser(player);
        File home = User.getDataDirectory();

        File raw = new File(home, user.getUuid() + "_accessory-bag.yml");
        if (!raw.exists()) {
            return null;
        }

        Config file = new Config(home, user.getUuid() + "_accessory-bag.yml");

        List<ItemStack> items = null;
        try {
            items = Arrays.asList(BukkitSerializeClass.itemStackArrayFromBase64(file.getString("bag.page." + SUtil.numToStr[page])));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (items == null || items.isEmpty()) {
            return null;
        }

        List<ItemStack> filtered = new ArrayList<>();

        for (ItemStack item : items) {
            if (item == null) {
                continue;
            }

            if (item.getType().equals(Material.SKULL_ITEM)) {
                filtered.add(item);
            }
        }

        return filtered;
    }

    private void loadPage() {
        User user = User.getUser(player);
        File home = User.getDataDirectory();

        File raw = new File(home, user.getUuid() + "_accessory-bag.yml");
        if (!raw.exists()) {
            return;
        }

        Config file = new Config(home, user.getUuid() + "_accessory-bag.yml");
        List<ItemStack> items = null;
        try {
            items = Arrays.asList(BukkitSerializeClass.itemStackArrayFromBase64(file.getString("bag.page." + SUtil.numToStr[page])));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            List<ItemStack> finalItems = items;
            IntStream.range(0, maxSlot).forEach(slot -> {
                ItemStack vanilla = finalItems.get(slot);
                set(new GUIClickableItem() {

                    @Override
                    public void run(InventoryClickEvent e) {
                        update(inventory);
                    }

                    @Override
                    public ItemStack getItem() {
                        return vanilla;
                    }

                    @Override
                    public boolean canPickup() {
                        return true;
                    }

                    @Override
                    public int getSlot() {
                        return slot;
                    }
                });
            });
        } catch (ArrayIndexOutOfBoundsException e) {
            SLog.info(e);
        }

        set(GUIClickableItem.getCloseItem(closePos));
        set(new GUIClickableItem() {

            @Override
            public void run(InventoryClickEvent e) {
                e.getWhoClicked().closeInventory();
                SUtil.delay(() -> {
                    new AccessoryReforges().open(player);
                }, 5);
            }

            @Override
            public ItemStack getItem() {
                return SUtil.getStack(Sputnik.trans("&aReforge All"), Material.IRON_INGOT, (short) 0, 1,
                        Sputnik.trans4("&7Reforge all your &dAccessories&7 all",
                        "&7in one click!",
                        "&7",
                        "&eClick to Open!")
                );
            }

            @Override
            public int getSlot() {
                return closePos + 1;
            }
        });
        set(new GUIClickableItem() {
            @Override
            public void run(InventoryClickEvent e) {
                Player player = (Player) e.getWhoClicked();

                List<SItem> accessories = PlayerUtils.getAccessories(player);
                if (accessories == null) {
                    player.sendMessage(SUtil.color("&cYour accessory bag is empty!"));
                    player.playSound(player.getLocation(), Sound.VILLAGER_NO, 10, 1);
                    return;
                }

                List<ItemStack> stacks = new ArrayList<>();

                for (SItem accessory : accessories) {
                    accessory.setRecombobulated(true);
                    stacks.add(accessory.getStack());
                }

                player.sendMessage(SUtil.color("&aYou recombobulated all your accessories!"));
                player.playSound(player.getLocation(), Sound.ANVIL_USE, 10, 2);
                savePage();
                player.closeInventory();

                SUtil.delay(() -> setAccessories(player, page, stacks), 10);
                SUtil.delay(() -> new AccessoryBag(page, size, maxSlot, closePos).open(player), 15);
            }

            @Override
            public ItemStack getItem() {
                return SUtil.getSkullURLStack(Sputnik.trans("&aRecombobulate All"), "57ccd36dc8f72adcb1f8c8e61ee82cd96ead140cf2a16a1366be9b5a8e3cc3fc", 1,
                        Sputnik.trans4("&7Automatically recombobulates all",
                        "&7your &dAccessories&7 in one click!",
                        "&7",
                        "&eClick to Upgrade!")
                );
            }

            @Override
            public int getSlot() {
                return closePos - 1;
            }
        });
    }

    @Override
    public void onBottomClick(InventoryClickEvent e) {
       // update(inventory);

        ItemStack selected = e.getCurrentItem();
        if (selected == null) {
            return;
        }
        if (selected.getType() == Material.AIR) {
            return;
        }

        SItem item = SItem.find(selected);
        if (item == null) {
            item = SItem.convert(selected);
        }

        if (!SUtil.getItemType(item.getType()).equals(GenericItemType.ACCESSORY)) {
            e.setCancelled(true);
            player.sendMessage(SUtil.color("&cYou cannot put this item in the accessory bag!"));
            player.playSound(player.getLocation(), Sound.VILLAGER_NO, 10, 1);
        }

        List<ItemStack> accessories = getAccessories(player, page);
        if (accessories != null) {
            for (ItemStack stack : accessories) {
                if (stack == null) continue;
                if (stack.getType().equals(Material.AIR)) continue;

                SItem accessory = SItem.find(stack);

                if (item.getType().equals(accessory.getType())) {
                    e.setCancelled(true);
                    player.sendMessage(SUtil.color("&cAn accessory of this type is already in the bag!"));
                    player.playSound(player.getLocation(), Sound.VILLAGER_NO, 10, 1);
                }
                PlayerInventory inv = e.getWhoClicked().getInventory();
                inv.remove(item.getStack());
                inventory.addItem(item.getStack());
            }
        }
    }
}
