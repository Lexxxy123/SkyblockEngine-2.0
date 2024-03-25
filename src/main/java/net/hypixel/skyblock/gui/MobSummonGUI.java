package net.hypixel.skyblock.gui;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.hypixel.skyblock.entity.SEntity;
import net.hypixel.skyblock.entity.SEntityType;
import net.hypixel.skyblock.util.PaginationList;
import net.hypixel.skyblock.util.SUtil;
import net.hypixel.skyblock.util.Sputnik;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MobSummonGUI extends GUI{

    private static final int[] INTERIOR = new int[]{10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34, 37, 38, 39, 40, 41, 42, 43};

    public MobSummonGUI(String query, int page) {
        super("Mob Browser", 54);
        this.border(BLACK_STAINED_GLASS_PANE);
        PaginationList<ItemStack> pagedMaterials = new PaginationList(28);
        for (SEntityType type : SEntityType.values()){
            pagedMaterials.add(createIcon(type));
        }
        if (!query.equals("")) {
            pagedMaterials.removeIf((type) -> !type.getItemMeta().getDisplayName().toLowerCase().contains(query));
        }
        if (pagedMaterials.size() == 0) {
            page = 0;
        }


        this.set(GUIClickableItem.getCloseItem(50));
        this.title = "Mob Browser (" + page + "/" + pagedMaterials.getPageCount() + ")";
        if (page > 1) {
            int finalPage = page;
            this.set(new GUIClickableItem() {
                public void run(InventoryClickEvent e) {
                    (new MobSummonGUI(finalPage - 1)).open((Player) e.getWhoClicked());
                    ((Player) e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), Sound.NOTE_PIANO, 1.0F, 1.0F);
                }

                public int getSlot() {
                    return 45;
                }

                public ItemStack getItem() {
                    return SUtil.createNamedItemStack(Material.ARROW, ChatColor.GRAY + "Pervious Page");
                }
            });
        }

        if (page != pagedMaterials.getPageCount()) {
            int finalPage1 = page;
            this.set(new GUIClickableItem() {
                public void run(InventoryClickEvent e) {
                    (new MobSummonGUI(finalPage1 + 1)).open((Player) e.getWhoClicked());
                    ((Player) e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), Sound.NOTE_PIANO, 1.0F, 1.0F);
                }

                public int getSlot() {
                    return 53;
                }

                public ItemStack getItem() {
                    return SUtil.createNamedItemStack(Material.ARROW, ChatColor.GRAY + "Next Page");
                }
            });
            set(new GUIQueryItem() {
                @Override
                public GUI onQueryFinish(String query) {
                    return new MobSummonGUI(query);
                }

                @Override
                public void run(InventoryClickEvent e) {
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
            List<ItemStack> p = pagedMaterials.getPage(page);
            if (p == null) {
                return;
            }
            for (int i = 0; i < p.size(); ++i) {
                final int slot = INTERIOR[i];
                ItemStack stack = p.get(i);
                this.set(new GUIClickableItem() {

                    @Override
                    public void run(InventoryClickEvent e) {
                        ItemStack stack = e.getCurrentItem();
                        String name = deserilize(stack.getItemMeta().getDisplayName());
                        SEntityType type = SEntityType.getEntityType(name);
                        SEntity entity = new SEntity(e.getWhoClicked() , type);
                        e.getWhoClicked().closeInventory();
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
        ItemStack stack = getEntitySkull(type.getCraftType());
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(serilize(type.name()));
        ArrayList<String> lore = new ArrayList<>();
        try {
            lore.add(ChatColor.GRAY + "Health : " + ChatColor.RED + Sputnik.formatFull((float) type.getStatistics().getEntityMaxHealth()) + " ❤");
            lore.add(ChatColor.GRAY + "Damage : " + ChatColor.RED + Sputnik.formatFull((float) type.getStatistics().getDamageDealt()));
            if (type.getStatistics().getMovementSpeed() > 0) {
                type.getStatistics().getMovementSpeed();
            }

            lore.add(ChatColor.GRAY + "Level : " + ChatColor.BLUE + type.getStatistics().mobLevel());
        }catch (NullPointerException ex){

        }
        lore.add(" ");
        lore.add(ChatColor.GOLD + "Click to summon");
        meta.setLore(lore);
        stack.setItemMeta(meta);
        return stack;
    }

    private String serilize(String name){
        return name.replace("_" , " ");
    }
    private String deserilize(String name){
        return name.replace(" " , "_");
    }

    public static ItemStack getEntitySkull(EntityType entityType) {
        ItemStack skullItem = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta skullMeta = (SkullMeta) skullItem.getItemMeta();

        skullMeta.setOwner(entityType.getName());

        skullItem.setItemMeta(skullMeta);
        return skullItem;
    }
    public static ItemStack getSkull(String texture, String signature) {
        ItemStack skullItem = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta skullMeta = (SkullMeta) skullItem.getItemMeta();

        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        profile.getProperties().put("textures", new Property("textures", texture, signature));

        try {
            Field profileField = skullMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(skullMeta, profile);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        skullItem.setItemMeta(skullMeta);
        return skullItem;
    }
}