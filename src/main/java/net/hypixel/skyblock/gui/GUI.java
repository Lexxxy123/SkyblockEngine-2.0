/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.inventory.InventoryClickEvent
 *  org.bukkit.event.inventory.InventoryCloseEvent
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.InventoryHolder
 *  org.bukkit.inventory.ItemStack
 */
package net.hypixel.skyblock.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.hypixel.skyblock.SkyBlock;
import net.hypixel.skyblock.gui.GUIItem;
import net.hypixel.skyblock.gui.GUIOpenEvent;
import net.hypixel.skyblock.item.SItem;
import net.hypixel.skyblock.item.SMaterial;
import net.hypixel.skyblock.util.SUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public abstract class GUI {
    public static final ItemStack BLACK_STAINED_GLASS_PANE = SUtil.createColoredStainedGlassPane((short)15, " ");
    public static final ItemStack RED_STAINED_GLASS_PANE = SUtil.createColoredStainedGlassPane((short)14, ChatColor.RESET + " ");
    public static final ItemStack LIME_STAINED_GLASS_PANE = SUtil.createColoredStainedGlassPane((short)5, ChatColor.RESET + " ");
    public static final ItemStack GRAY_STAINED_GLASS_PANE = SUtil.createColoredStainedGlassPane((short)7, ChatColor.RESET + " ");
    public static final Map<UUID, GUI> GUI_MAP = new HashMap<UUID, GUI>();
    protected String title;
    protected int size;
    protected List<GUIItem> items;

    public GUI(String title, int size) {
        this.title = title;
        this.size = size;
        this.items = new ArrayList<GUIItem>();
    }

    public GUI(String title) {
        this(title, 27);
    }

    public void set(GUIItem item) {
        this.items.removeIf(i2 -> i2.getSlot() == item.getSlot());
        this.items.add(item);
    }

    public void set(final int slot, final ItemStack stack, final boolean pickup) {
        if (stack == null) {
            this.items.removeIf(i2 -> i2.getSlot() == slot);
            return;
        }
        this.set(new GUIItem(){

            @Override
            public int getSlot() {
                return slot;
            }

            @Override
            public ItemStack getItem() {
                return stack;
            }

            @Override
            public boolean canPickup() {
                return pickup;
            }
        });
    }

    public void set(int slot, ItemStack stack) {
        this.set(slot, stack, false);
    }

    public GUIItem get(int slot) {
        for (GUIItem item : this.items) {
            if (item.getSlot() != slot) continue;
            return item;
        }
        return null;
    }

    public void fill(ItemStack stack, int cornerSlot, int cornerSlot2, boolean overwrite, boolean pickup) {
        int bottomLeft;
        int topRight;
        if (cornerSlot < 0 || cornerSlot > this.size) {
            throw new IllegalArgumentException("Corner 1 of the border described is out of bounds");
        }
        if (cornerSlot2 < 0 || cornerSlot2 > this.size) {
            throw new IllegalArgumentException("Corner 2 of the border described is out of bounds");
        }
        int topLeft = Math.min(cornerSlot, cornerSlot2);
        int bottomRight = topRight = Math.max(cornerSlot, cornerSlot2);
        while (topRight > topLeft) {
            topRight -= 9;
        }
        for (bottomLeft = topLeft; bottomLeft < bottomRight; bottomLeft += 9) {
        }
        topRight += 9;
        bottomLeft -= 9;
        for (int y2 = topLeft; y2 <= bottomLeft; y2 += 9) {
            for (int x2 = y2; x2 <= topRight - topLeft + y2; ++x2) {
                int f2 = x2;
                if (this.items.stream().filter(item -> item.getSlot() == f2).toArray().length != 0 && !overwrite) continue;
                this.set(x2, stack, pickup);
            }
        }
    }

    public void fill(ItemStack stack, int cornerSlot, int cornerSlot2, boolean pickup) {
        this.fill(stack, cornerSlot, cornerSlot2, true, pickup);
    }

    public void fill(ItemStack stack, int cornerSlot, int cornerSlot2) {
        this.fill(stack, cornerSlot, cornerSlot2, false);
    }

    public void fill(ItemStack stack) {
        this.fill(stack, 0, this.size - 1);
    }

    public void fill(Material material) {
        this.fill(new ItemStack(material));
    }

    public void border(ItemStack stack, int cornerSlot, int cornerSlot2, boolean overwrite, boolean pickup) {
        int bottomLeft;
        int topRight;
        if (cornerSlot < 0 || cornerSlot > this.size) {
            throw new IllegalArgumentException("Corner 1 of the border described is out of bounds");
        }
        if (cornerSlot2 < 0 || cornerSlot2 > this.size) {
            throw new IllegalArgumentException("Corner 2 of the border described is out of bounds");
        }
        int topLeft = Math.min(cornerSlot, cornerSlot2);
        int bottomRight = topRight = Math.max(cornerSlot, cornerSlot2);
        while (topRight > topLeft) {
            topRight -= 9;
        }
        for (bottomLeft = topLeft; bottomLeft < bottomRight; bottomLeft += 9) {
        }
        topRight += 9;
        bottomLeft -= 9;
        for (int y2 = topLeft; y2 <= bottomLeft; y2 += 9) {
            for (int x2 = y2; x2 <= topRight - topLeft + y2; ++x2) {
                int f2 = x2;
                if (this.items.stream().filter(item -> item.getSlot() == f2).toArray().length != 0 && !overwrite) continue;
                if (y2 == topLeft || y2 == bottomLeft) {
                    this.set(x2, stack, pickup);
                }
                if (x2 != y2 && x2 != topRight - topLeft + y2) continue;
                this.set(x2, stack, pickup);
            }
        }
    }

    public void border(ItemStack stack, int cornerSlot, int cornerSlot2, boolean pickup) {
        this.border(stack, cornerSlot, cornerSlot2, true, pickup);
    }

    public void border(ItemStack stack, int cornerSlot, int cornerSlot2) {
        this.border(stack, cornerSlot, cornerSlot2, false);
    }

    public void border(ItemStack stack) {
        this.border(stack, 0, this.size - 1);
    }

    public void add(SMaterial material, byte variant, int amount, boolean pickup) {
        for (int i2 = 0; i2 < amount / 64; ++i2) {
            int first = this.firstEmpty();
            if (first == -1) {
                return;
            }
            this.set(first, SUtil.setStackAmount(SItem.of(material, variant).getStack(), 64), pickup);
        }
        int first2 = this.firstEmpty();
        if (first2 == -1) {
            return;
        }
        this.set(first2, SUtil.setStackAmount(SItem.of(material, variant).getStack(), amount % 64), pickup);
    }

    public int firstEmpty() {
        int i2 = 0;
        while (i2 < this.size) {
            int finalI = i2++;
            long found = this.items.stream().filter(item -> item.getSlot() == finalI).count();
            if (found != 0L) continue;
            return i2;
        }
        return -1;
    }

    public void open(Player player) {
        this.early(player);
        Inventory inventory = Bukkit.createInventory((InventoryHolder)player, (int)this.size, (String)this.title);
        GUIOpenEvent openEvent = new GUIOpenEvent(player, this, inventory);
        SkyBlock.getPlugin().getServer().getPluginManager().callEvent((Event)openEvent);
        if (openEvent.isCancelled()) {
            return;
        }
        for (GUIItem item : this.items) {
            inventory.setItem(item.getSlot(), item.getItem());
        }
        player.openInventory(inventory);
        GUI_MAP.remove(player.getUniqueId());
        GUI_MAP.put(player.getUniqueId(), this);
    }

    public void update(Inventory inventory) {
    }

    public void onOpen(GUIOpenEvent e2) throws IOException {
    }

    public void onClose(InventoryCloseEvent e2) {
    }

    public void early(Player player) {
    }

    public void onBottomClick(InventoryClickEvent e2) throws IOException {
    }

    public void onTopClick(InventoryClickEvent e2) {
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getSize() {
        return this.size;
    }

    public List<GUIItem> getItems() {
        return this.items;
    }
}

