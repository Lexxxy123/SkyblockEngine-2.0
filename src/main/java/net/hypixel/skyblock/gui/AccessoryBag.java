/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  lombok.NonNull
 *  org.bukkit.Material
 *  org.bukkit.Sound
 *  org.bukkit.entity.Player
 *  org.bukkit.event.inventory.InventoryClickEvent
 *  org.bukkit.event.inventory.InventoryCloseEvent
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.PlayerInventory
 */
package net.hypixel.skyblock.gui;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import lombok.NonNull;
import net.hypixel.skyblock.api.serializer.BukkitSerializeClass;
import net.hypixel.skyblock.config.Config;
import net.hypixel.skyblock.gui.AccessoryReforges;
import net.hypixel.skyblock.gui.GUI;
import net.hypixel.skyblock.gui.GUIClickableItem;
import net.hypixel.skyblock.gui.GUIOpenEvent;
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

public class AccessoryBag
extends GUI {
    private int page;
    private int maxSlot;
    private int closePos;
    private Player player;
    private Inventory inventory;

    public AccessoryBag(int page, int size, int maxSlot, int closePos) {
        super("Accessory Bag", size);
        this.page = page;
        this.maxSlot = maxSlot;
        for (int i2 = 0; i2 < size; ++i2) {
            if (i2 < maxSlot) continue;
            this.set(i2, BLACK_STAINED_GLASS_PANE);
        }
        this.closePos = closePos;
    }

    @Override
    public void onOpen(GUIOpenEvent e2) {
        this.inventory = e2.getInventory();
        this.player = e2.getPlayer();
        this.loadPage();
    }

    @Override
    public void onClose(InventoryCloseEvent e2) {
        this.savePage();
        ItemListener.updateStatistics(this.player);
    }

    @Override
    public void update(Inventory inventory) {
        this.inventory = inventory;
        this.savePage();
        ItemListener.updateStatistics(this.player);
    }

    private void savePage() {
        if (this.inventory == null) {
            return;
        }
        User user = User.getUser(this.player);
        File home = User.getDataDirectory();
        File raw = new File(home, user.getUuid() + "_accessory-bag.yml");
        if (!raw.exists()) {
            raw.createNewFile();
            SLog.info("Creating new Accessory Bag for player " + user.getUuid());
        }
        Config file = new Config(home, user.getUuid() + "_accessory-bag.yml");
        ArrayList<ItemStack> items = new ArrayList<ItemStack>();
        for (int i2 = 0; i2 < this.inventory.getSize(); ++i2) {
            if (i2 > this.maxSlot || i2 == this.closePos || i2 == this.closePos - 1 || i2 == this.closePos + 1) continue;
            items.add(this.inventory.getItem(i2));
        }
        file.set("bag.page." + SUtil.numToStr[this.page], BukkitSerializeClass.itemStackArrayToBase64(items.toArray(new ItemStack[0])));
        file.save();
    }

    public static void setAccessories(@NonNull Player player, int page, List<ItemStack> accessories) {
        if (player == null) {
            throw new NullPointerException("player is marked non-null but is null");
        }
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

    @Nullable
    public static List<ItemStack> getAccessories(@NonNull Player player, int page) {
        if (player == null) {
            throw new NullPointerException("player is marked non-null but is null");
        }
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
        } catch (IOException e2) {
            throw new RuntimeException(e2);
        }
        if (items == null || items.isEmpty()) {
            return null;
        }
        ArrayList<ItemStack> filtered = new ArrayList<ItemStack>();
        for (ItemStack item : items) {
            if (item == null || !item.getType().equals((Object)Material.SKULL_ITEM)) continue;
            filtered.add(item);
        }
        return filtered;
    }

    private void loadPage() {
        User user = User.getUser(this.player);
        File home = User.getDataDirectory();
        File raw = new File(home, user.getUuid() + "_accessory-bag.yml");
        if (!raw.exists()) {
            return;
        }
        Config file = new Config(home, user.getUuid() + "_accessory-bag.yml");
        List<ItemStack> items = null;
        try {
            items = Arrays.asList(BukkitSerializeClass.itemStackArrayFromBase64(file.getString("bag.page." + SUtil.numToStr[this.page])));
        } catch (IOException e2) {
            throw new RuntimeException(e2);
        }
        try {
            List<ItemStack> finalItems = items;
            IntStream.range(0, this.maxSlot).forEach(slot -> {
                final ItemStack vanilla = (ItemStack)finalItems.get(slot);
                this.set(new GUIClickableItem(){

                    @Override
                    public void run(InventoryClickEvent e2) {
                        AccessoryBag.this.update(AccessoryBag.this.inventory);
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
        } catch (ArrayIndexOutOfBoundsException e3) {
            SLog.info(e3);
        }
        this.set(GUIClickableItem.getCloseItem(this.closePos));
        this.set(new GUIClickableItem(){

            @Override
            public void run(InventoryClickEvent e2) {
                e2.getWhoClicked().closeInventory();
                SUtil.delay(() -> new AccessoryReforges().open(AccessoryBag.this.player), 5L);
            }

            @Override
            public ItemStack getItem() {
                return SUtil.getStack(Sputnik.trans("&aReforge All"), Material.IRON_INGOT, (short)0, 1, Sputnik.trans4("&7Reforge all your &dAccessories&7 all", "&7in one click!", "&7", "&eClick to Open!"));
            }

            @Override
            public int getSlot() {
                return AccessoryBag.this.closePos + 1;
            }
        });
        this.set(new GUIClickableItem(){

            @Override
            public void run(InventoryClickEvent e2) {
                Player player = (Player)e2.getWhoClicked();
                List<SItem> accessories = PlayerUtils.getAccessories(player);
                if (accessories == null) {
                    player.sendMessage(SUtil.color("&cYour accessory bag is empty!"));
                    player.playSound(player.getLocation(), Sound.VILLAGER_NO, 10.0f, 1.0f);
                    return;
                }
                ArrayList<ItemStack> stacks = new ArrayList<ItemStack>();
                for (SItem accessory : accessories) {
                    accessory.setRecombobulated(true);
                    stacks.add(accessory.getStack());
                }
                player.sendMessage(SUtil.color("&aYou recombobulated all your accessories!"));
                player.playSound(player.getLocation(), Sound.ANVIL_USE, 10.0f, 2.0f);
                AccessoryBag.this.savePage();
                player.closeInventory();
                SUtil.delay(() -> AccessoryBag.setAccessories(player, AccessoryBag.this.page, stacks), 10L);
                SUtil.delay(() -> new AccessoryBag(AccessoryBag.this.page, AccessoryBag.this.size, AccessoryBag.this.maxSlot, AccessoryBag.this.closePos).open(player), 15L);
            }

            @Override
            public ItemStack getItem() {
                return SUtil.getSkullURLStack(Sputnik.trans("&aRecombobulate All"), "57ccd36dc8f72adcb1f8c8e61ee82cd96ead140cf2a16a1366be9b5a8e3cc3fc", 1, Sputnik.trans4("&7Automatically recombobulates all", "&7your &dAccessories&7 in one click!", "&7", "&eClick to Upgrade!"));
            }

            @Override
            public int getSlot() {
                return AccessoryBag.this.closePos - 1;
            }
        });
    }

    @Override
    public void onBottomClick(InventoryClickEvent e2) {
        List<ItemStack> accessories;
        ItemStack selected = e2.getCurrentItem();
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
        if (!SUtil.getItemType(item.getType()).equals((Object)GenericItemType.ACCESSORY)) {
            e2.setCancelled(true);
            this.player.sendMessage(SUtil.color("&cYou cannot put this item in the accessory bag!"));
            this.player.playSound(this.player.getLocation(), Sound.VILLAGER_NO, 10.0f, 1.0f);
        }
        if ((accessories = AccessoryBag.getAccessories(this.player, this.page)) != null) {
            for (ItemStack stack : accessories) {
                if (stack == null || stack.getType().equals((Object)Material.AIR)) continue;
                SItem accessory = SItem.find(stack);
                if (item.getType().equals((Object)accessory.getType())) {
                    e2.setCancelled(true);
                    this.player.sendMessage(SUtil.color("&cAn accessory of this type is already in the bag!"));
                    this.player.playSound(this.player.getLocation(), Sound.VILLAGER_NO, 10.0f, 1.0f);
                }
                PlayerInventory inv = e2.getWhoClicked().getInventory();
                inv.remove(item.getStack());
                this.inventory.addItem(new ItemStack[]{item.getStack()});
            }
        }
    }
}

