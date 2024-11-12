/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 *  com.mojang.authlib.properties.Property
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.Sound
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.Player
 *  org.bukkit.event.inventory.InventoryClickEvent
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.inventory.meta.SkullMeta
 */
package net.hypixel.skyblock.gui;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.hypixel.skyblock.entity.SEntity;
import net.hypixel.skyblock.entity.SEntityType;
import net.hypixel.skyblock.gui.GUI;
import net.hypixel.skyblock.gui.GUIClickableItem;
import net.hypixel.skyblock.gui.GUIQueryItem;
import net.hypixel.skyblock.util.PaginationList;
import net.hypixel.skyblock.util.SUtil;
import net.hypixel.skyblock.util.Sputnik;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class MobSummonGUI
extends GUI {
    private static final int[] INTERIOR = new int[]{10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34, 37, 38, 39, 40, 41, 42, 43};

    public MobSummonGUI(String query, int page) {
        super("Mob Browser", 54);
        this.border(BLACK_STAINED_GLASS_PANE);
        PaginationList pagedMaterials = new PaginationList(28);
        for (SEntityType type2 : SEntityType.values()) {
            pagedMaterials.add(this.createIcon(type2));
        }
        if (!query.equals("")) {
            pagedMaterials.removeIf(type -> !type.getItemMeta().getDisplayName().toLowerCase().contains(query));
        }
        if (pagedMaterials.size() == 0) {
            page = 0;
        }
        this.set(GUIClickableItem.getCloseItem(50));
        this.title = "Mob Browser (" + page + "/" + pagedMaterials.getPageCount() + ")";
        if (page > 1) {
            final int finalPage = page;
            this.set(new GUIClickableItem(){

                @Override
                public void run(InventoryClickEvent e2) {
                    new MobSummonGUI(finalPage - 1).open((Player)e2.getWhoClicked());
                    ((Player)e2.getWhoClicked()).playSound(e2.getWhoClicked().getLocation(), Sound.NOTE_PIANO, 1.0f, 1.0f);
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
            final int finalPage1 = page;
            this.set(new GUIClickableItem(){

                @Override
                public void run(InventoryClickEvent e2) {
                    new MobSummonGUI(finalPage1 + 1).open((Player)e2.getWhoClicked());
                    ((Player)e2.getWhoClicked()).playSound(e2.getWhoClicked().getLocation(), Sound.NOTE_PIANO, 1.0f, 1.0f);
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
            this.set(new GUIQueryItem(){

                @Override
                public GUI onQueryFinish(String query) {
                    return new MobSummonGUI(query);
                }

                @Override
                public void run(InventoryClickEvent e2) {
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
            List p2 = pagedMaterials.getPage(page);
            if (p2 == null) {
                return;
            }
            for (int i2 = 0; i2 < p2.size(); ++i2) {
                final int slot = INTERIOR[i2];
                final ItemStack stack = (ItemStack)p2.get(i2);
                this.set(new GUIClickableItem(){

                    @Override
                    public void run(InventoryClickEvent e2) {
                        ItemStack stack2 = e2.getCurrentItem();
                        String name = MobSummonGUI.this.deserilize(stack2.getItemMeta().getDisplayName());
                        SEntityType type = SEntityType.getEntityType(name);
                        SEntity entity = new SEntity((Entity)e2.getWhoClicked(), type, new Object[0]);
                        e2.getWhoClicked().closeInventory();
                    }

                    @Override
                    public int getSlot() {
                        return slot;
                    }

                    @Override
                    public ItemStack getItem() {
                        return stack;
                    }
                });
            }
        }
    }

    public MobSummonGUI(String query) {
        this(query, 1);
    }

    public MobSummonGUI(int page) {
        this("", page);
    }

    public MobSummonGUI() {
        this(1);
    }

    private ItemStack createIcon(SEntityType type) {
        ItemStack stack = MobSummonGUI.getEntitySkull(type.getCraftType());
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(this.serilize(type.name()));
        ArrayList<String> lore = new ArrayList<String>();
        try {
            lore.add(ChatColor.GRAY + "Health : " + ChatColor.RED + Sputnik.formatFull((float)type.getStatistics().getEntityMaxHealth()) + " \u2764");
            lore.add(ChatColor.GRAY + "Damage : " + ChatColor.RED + Sputnik.formatFull((float)type.getStatistics().getDamageDealt()));
            if (type.getStatistics().getMovementSpeed() > 0.0) {
                type.getStatistics().getMovementSpeed();
            }
            lore.add(ChatColor.GRAY + "Level : " + ChatColor.BLUE + type.getStatistics().mobLevel());
        } catch (NullPointerException nullPointerException) {
            // empty catch block
        }
        lore.add(" ");
        lore.add(ChatColor.GOLD + "Click to summon");
        meta.setLore(lore);
        stack.setItemMeta(meta);
        return stack;
    }

    private String serilize(String name) {
        return name.replace("_", " ");
    }

    private String deserilize(String name) {
        return name.replace(" ", "_");
    }

    public static ItemStack getEntitySkull(EntityType entityType) {
        ItemStack skullItem = new ItemStack(Material.SKULL_ITEM, 1, 3);
        SkullMeta skullMeta = (SkullMeta)skullItem.getItemMeta();
        skullMeta.setOwner(entityType.getName());
        skullItem.setItemMeta((ItemMeta)skullMeta);
        return skullItem;
    }

    public static ItemStack getSkull(String texture, String signature) {
        ItemStack skullItem = new ItemStack(Material.SKULL_ITEM, 1, 3);
        SkullMeta skullMeta = (SkullMeta)skullItem.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        profile.getProperties().put((Object)"textures", (Object)new Property("textures", texture, signature));
        try {
            Field profileField = skullMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(skullMeta, profile);
        } catch (IllegalAccessException | NoSuchFieldException e2) {
            e2.printStackTrace();
        }
        skullItem.setItemMeta((ItemMeta)skullMeta);
        return skullItem;
    }
}

