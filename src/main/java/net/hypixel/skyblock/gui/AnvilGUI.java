/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.Sound
 *  org.bukkit.entity.HumanEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.inventory.InventoryClickEvent
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 */
package net.hypixel.skyblock.gui;

import java.util.Arrays;
import java.util.List;
import net.hypixel.skyblock.SkyBlock;
import net.hypixel.skyblock.features.enchantment.Enchantment;
import net.hypixel.skyblock.features.enchantment.EnchantmentType;
import net.hypixel.skyblock.features.skill.EnchantingSkill;
import net.hypixel.skyblock.features.skill.Skill;
import net.hypixel.skyblock.gui.BlockBasedGUI;
import net.hypixel.skyblock.gui.GUI;
import net.hypixel.skyblock.gui.GUIClickableItem;
import net.hypixel.skyblock.gui.GUIOpenEvent;
import net.hypixel.skyblock.item.GenericItemType;
import net.hypixel.skyblock.item.SItem;
import net.hypixel.skyblock.item.SMaterial;
import net.hypixel.skyblock.util.SUtil;
import net.hypixel.skyblock.util.Sputnik;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class AnvilGUI
extends GUI
implements BlockBasedGUI {
    private static final ItemStack ANVIL_BARRIER = SUtil.getSingleLoreStack(ChatColor.RED + "Anvil", Material.BARRIER, (short)0, 1, "Place a target item in the left slot and a sacrifice item in the right slot to combine Enchantments!");
    private static final ItemStack DEFAULT_COMBINE_ITEMS = SUtil.getStack(ChatColor.GREEN + "Combine Items", Material.ANVIL, (short)0, 1, ChatColor.GRAY + "Combine the items in the slots", ChatColor.GRAY + "to the left and right below.");
    private static final String CANNOT_COMBINE = ChatColor.RED + "These items cannot be combined!";
    private boolean isApplied = false;

    public AnvilGUI() {
        super("Anvil", 54);
        this.fill(BLACK_STAINED_GLASS_PANE);
        this.fill(RED_STAINED_GLASS_PANE, 45, 53);
        this.set(GUIClickableItem.getCloseItem(49));
        for (int i2 : Arrays.asList(11, 12, 20)) {
            this.set(i2, SUtil.getSingleLoreStack(ChatColor.GOLD + "Item to Upgrade", Material.STAINED_GLASS_PANE, (short)14, 1, "The item you want to upgrade should be placed in the slot on this side."));
        }
        for (int i2 : Arrays.asList(14, 15, 24)) {
            this.set(i2, SUtil.getSingleLoreStack(ChatColor.GOLD + "Item to Sacrifice", Material.STAINED_GLASS_PANE, (short)14, 1, "The item you are sacrificing in order to upgrade the item on the left should be placed in the slot on this side."));
        }
        this.set(29, null);
        this.set(33, null);
        this.set(new GUIClickableItem(){

            @Override
            public void run(final InventoryClickEvent e2) {
                ItemStack current = e2.getCurrentItem();
                if (null == current) {
                    return;
                }
                if (Material.BARRIER == e2.getCurrentItem().getType()) {
                    e2.setCancelled(true);
                    return;
                }
                final Inventory inventory = e2.getClickedInventory();
                if (!SUtil.isAir(inventory.getItem(29)) || !SUtil.isAir(inventory.getItem(33))) {
                    e2.setCancelled(true);
                    return;
                }
                new BukkitRunnable(){

                    public void run() {
                        inventory.setItem(e2.getSlot(), ANVIL_BARRIER);
                    }
                }.runTaskLater((Plugin)SkyBlock.getPlugin(), 1L);
            }

            @Override
            public int getSlot() {
                return 13;
            }

            @Override
            public boolean canPickup() {
                return true;
            }

            @Override
            public ItemStack getItem() {
                return ANVIL_BARRIER;
            }
        });
        this.set(new GUIClickableItem(){

            @Override
            public void run(InventoryClickEvent e2) {
                HumanEntity entity = e2.getWhoClicked();
                Inventory inventory = e2.getClickedInventory();
                ItemStack upgrade = inventory.getItem(29);
                ItemStack sacrifice = inventory.getItem(33);
                if (null == upgrade || null == sacrifice) {
                    entity.sendMessage(CANNOT_COMBINE);
                    return;
                }
                SItem specUpgrade = SItem.find(upgrade);
                SItem specSacrifice = SItem.find(sacrifice);
                if (14 == inventory.getItem(11).getData().getData() || 14 == inventory.getItem(14).getData().getData()) {
                    entity.sendMessage(CANNOT_COMBINE);
                    return;
                }
                if (SMaterial.HOT_POTATO_BOOK == specSacrifice.getType() && 9 == specUpgrade.getDataInt("hpb")) {
                    entity.sendMessage(Sputnik.trans("&7You have already applied the maximum number of Hot Potato books to this item! &eFuming Hot Potato Book coming soon!"));
                }
                if (SMaterial.ENCHANTED_BOOK == specSacrifice.getType()) {
                    for (Enchantment enchantment : specSacrifice.getEnchantments()) {
                        Skill.reward(EnchantingSkill.INSTANCE, enchantment.getLevel() * 2, (Player)e2.getWhoClicked());
                    }
                }
                inventory.setItem(22, DEFAULT_COMBINE_ITEMS);
                AnvilGUI.this.isApplied = true;
                AnvilGUI.setItemTo(true, false, inventory);
                AnvilGUI.setItemTo(false, false, inventory);
                inventory.setItem(29, null);
                inventory.setItem(33, null);
                entity.getWorld().playSound(entity.getLocation(), Sound.ANVIL_USE, 1.0f, 1.0f);
            }

            @Override
            public int getSlot() {
                return 22;
            }

            @Override
            public ItemStack getItem() {
                return DEFAULT_COMBINE_ITEMS;
            }
        });
    }

    @Override
    public void onOpen(final GUIOpenEvent e2) {
        new BukkitRunnable(){

            public void run() {
                Player player = e2.getPlayer();
                if (AnvilGUI.this != GUI.GUI_MAP.get(player.getUniqueId())) {
                    this.cancel();
                    return;
                }
                Inventory inventory = e2.getInventory();
                AnvilGUI.this.update(inventory);
                if (14 == inventory.getItem(11).getData().getData() || 14 == inventory.getItem(14).getData().getData()) {
                    SUtil.border(inventory, AnvilGUI.this, SUtil.createColoredStainedGlassPane((short)14, ChatColor.RESET + " "), 45, 48, true, false);
                    SUtil.border(inventory, AnvilGUI.this, SUtil.createColoredStainedGlassPane((short)14, ChatColor.RESET + " "), 50, 53, true, false);
                } else {
                    SUtil.border(inventory, AnvilGUI.this, SUtil.createColoredStainedGlassPane((short)5, ChatColor.RESET + " "), 45, 48, true, false);
                    SUtil.border(inventory, AnvilGUI.this, SUtil.createColoredStainedGlassPane((short)5, ChatColor.RESET + " "), 50, 53, true, false);
                }
            }
        }.runTaskTimer((Plugin)SkyBlock.getPlugin(), 0L, 5L);
    }

    @Override
    public void update(final Inventory inventory) {
        new BukkitRunnable(){

            public void run() {
                ItemStack select;
                if (null == inventory.getItem(13)) {
                    inventory.setItem(13, SUtil.getStack(ChatColor.RED + "Loading...", Material.BARRIER, (short)0, 1, ChatColor.GRAY + "If this appear for too long", ChatColor.GRAY + "contact admins!"));
                }
                if (null == inventory.getItem(29) && null == inventory.getItem(33) && Material.BARRIER == inventory.getItem(13).getType() && AnvilGUI.this.isApplied) {
                    AnvilGUI.this.isApplied = false;
                }
                if ((null != inventory.getItem(29) || null != inventory.getItem(33)) && AnvilGUI.this.isApplied) {
                    Sputnik.smartGiveItem(inventory.getItem(13), (Player)inventory.getViewers().get(0));
                    inventory.setItem(13, SUtil.getStack(ChatColor.RED + "Loading...", Material.BARRIER, (short)0, 1, ChatColor.GRAY + "If this appear for too long", ChatColor.GRAY + "contact admins!"));
                    AnvilGUI.this.isApplied = false;
                }
                if (null != (select = inventory.getItem(13)) && Material.BARRIER != select.getType() && SUtil.isAir(inventory.getItem(29)) && SUtil.isAir(inventory.getItem(33))) {
                    return;
                }
                SItem specUpgrade = SItem.find(inventory.getItem(29));
                SItem specSacrifice = SItem.find(inventory.getItem(33));
                boolean upgradeGreen = false;
                boolean sacrificeGreen = false;
                if (null == specSacrifice && null != specUpgrade) {
                    upgradeGreen = true;
                }
                if (null != specUpgrade) {
                    if (SUtil.isEnchantable(specUpgrade) || SUtil.isHotPotatoAble(specUpgrade)) {
                        upgradeGreen = true;
                    }
                    if (SMaterial.HIDDEN_ETHERWARP_CONDUIT == specUpgrade.getType()) {
                        upgradeGreen = true;
                    }
                    if (SMaterial.HOT_POTATO_BOOK == specUpgrade.getType()) {
                        upgradeGreen = false;
                    }
                }
                if (null != specSacrifice) {
                    if (SUtil.isEnchantable(specSacrifice)) {
                        sacrificeGreen = true;
                    } else if (SMaterial.HOT_POTATO_BOOK == specSacrifice.getType()) {
                        sacrificeGreen = true;
                    } else if (specSacrifice.getType().toString().contains("HIDDEN_ETHERWARP")) {
                        sacrificeGreen = true;
                    }
                }
                if (null != specUpgrade && null != specSacrifice) {
                    if (!SUtil.isHotPotatoAble(specUpgrade) && SMaterial.HOT_POTATO_BOOK == specSacrifice.getType()) {
                        upgradeGreen = false;
                        sacrificeGreen = false;
                    }
                    if (!SUtil.isEnchantable(specUpgrade) && SMaterial.ENCHANTED_BOOK == specSacrifice.getType()) {
                        upgradeGreen = false;
                        sacrificeGreen = false;
                    }
                    if (SMaterial.HIDDEN_ETHERWARP_CONDUIT == specUpgrade.getType() && SMaterial.HIDDEN_ETHERWARP_MERGER == specSacrifice.getType()) {
                        upgradeGreen = true;
                        sacrificeGreen = true;
                    }
                    if ((SMaterial.ASPECT_OF_THE_VOID == specUpgrade.getType() || SMaterial.ASPECT_OF_THE_END == specUpgrade.getType()) && SMaterial.HIDDEN_ETHERWARP_TRANSCODER == specSacrifice.getType()) {
                        upgradeGreen = true;
                        sacrificeGreen = true;
                    }
                }
                AnvilGUI.setItemTo(true, upgradeGreen, inventory);
                AnvilGUI.setItemTo(false, sacrificeGreen, inventory);
                if (!upgradeGreen || !sacrificeGreen) {
                    inventory.setItem(13, ANVIL_BARRIER);
                    inventory.setItem(22, DEFAULT_COMBINE_ITEMS);
                    return;
                }
                if (SMaterial.HIDDEN_ETHERWARP_CONDUIT == specUpgrade.getType() && SMaterial.HIDDEN_ETHERWARP_MERGER != specSacrifice.getType()) {
                    AnvilGUI.setItemTo(false, false, inventory);
                    inventory.setItem(13, ANVIL_BARRIER);
                    inventory.setItem(22, DEFAULT_COMBINE_ITEMS);
                    return;
                }
                if ((SMaterial.ASPECT_OF_THE_END == specUpgrade.getType() || SMaterial.ASPECT_OF_THE_VOID == specUpgrade.getType()) && SMaterial.HIDDEN_ETHERWARP_TRANSCODER != specSacrifice.getType() && SMaterial.ENCHANTED_BOOK != specSacrifice.getType() && SMaterial.HOT_POTATO_BOOK != specSacrifice.getType()) {
                    AnvilGUI.setItemTo(false, false, inventory);
                    inventory.setItem(13, ANVIL_BARRIER);
                    inventory.setItem(22, DEFAULT_COMBINE_ITEMS);
                    return;
                }
                if (SMaterial.ASPECT_OF_THE_END != specUpgrade.getType() && SMaterial.ASPECT_OF_THE_VOID != specUpgrade.getType() && SMaterial.HIDDEN_ETHERWARP_TRANSCODER == specSacrifice.getType()) {
                    AnvilGUI.setItemTo(false, false, inventory);
                    inventory.setItem(13, ANVIL_BARRIER);
                    inventory.setItem(22, DEFAULT_COMBINE_ITEMS);
                    return;
                }
                if (specUpgrade.getType() != specSacrifice.getType() && SMaterial.ENCHANTED_BOOK != specSacrifice.getType() && SMaterial.HOT_POTATO_BOOK != specSacrifice.getType() && SMaterial.HIDDEN_ETHERWARP_MERGER != specSacrifice.getType() && SMaterial.HIDDEN_ETHERWARP_TRANSCODER != specSacrifice.getType()) {
                    AnvilGUI.setItemTo(false, false, inventory);
                    inventory.setItem(13, ANVIL_BARRIER);
                    inventory.setItem(22, DEFAULT_COMBINE_ITEMS);
                    return;
                }
                if ((SMaterial.ASPECT_OF_THE_END == specUpgrade.getType() || SMaterial.ASPECT_OF_THE_VOID == specUpgrade.getType()) && SMaterial.HIDDEN_ETHERWARP_TRANSCODER == specSacrifice.getType() && "true" == specUpgrade.getDataString("etherwarp_trans")) {
                    AnvilGUI.setItemTo(false, false, inventory);
                    inventory.setItem(13, ANVIL_BARRIER);
                    inventory.setItem(22, DEFAULT_COMBINE_ITEMS);
                    return;
                }
                if (SMaterial.ENCHANTED_BOOK != specUpgrade.getType() && SMaterial.HOT_POTATO_BOOK != specSacrifice.getType() && !specSacrifice.getType().toString().contains("ETHER")) {
                    for (Enchantment enchantment : specSacrifice.getEnchantments()) {
                        if (enchantment.getType().getCompatibleTypes().contains((Object)specUpgrade.getType().getStatistics().getSpecificType())) continue;
                        AnvilGUI.setItemTo(true, false, inventory);
                        inventory.setItem(13, ANVIL_BARRIER);
                        inventory.setItem(22, DEFAULT_COMBINE_ITEMS);
                        return;
                    }
                }
                SItem display = specUpgrade.clone();
                if (SMaterial.HOT_POTATO_BOOK != specSacrifice.getType() && !specSacrifice.getType().toString().contains("ETHER")) {
                    List<Enchantment> list;
                    for (Enchantment enchantment2 : specSacrifice.getEnchantments()) {
                        Enchantment equiv = display.getEnchantment(enchantment2.getType());
                        if (null != equiv && equiv.getLevel() == enchantment2.getLevel()) {
                            display.removeEnchantment(enchantment2.getType());
                            if (enchantment2.getType() == EnchantmentType.ONE_FOR_ALL || enchantment2.getType() == EnchantmentType.TELEKINESIS) {
                                display.addEnchantment(enchantment2.getType(), enchantment2.getLevel());
                                continue;
                            }
                            if (5 > enchantment2.getLevel()) {
                                display.addEnchantment(enchantment2.getType(), enchantment2.getLevel() + 1);
                                continue;
                            }
                            display.addEnchantment(enchantment2.getType(), enchantment2.getLevel());
                            continue;
                        }
                        display.addEnchantment(enchantment2.getType(), enchantment2.getLevel());
                    }
                    if (null != specSacrifice.getEnchantment(EnchantmentType.ONE_FOR_ALL) && SMaterial.ENCHANTED_BOOK != specUpgrade.getType()) {
                        for (Enchantment enchantment2 : specUpgrade.getEnchantments()) {
                            if (enchantment2.getType() == EnchantmentType.TELEKINESIS) continue;
                            display.removeEnchantment(enchantment2.getType());
                            display.addEnchantment(EnchantmentType.ONE_FOR_ALL, specSacrifice.getEnchantment(EnchantmentType.ONE_FOR_ALL).getLevel());
                        }
                    }
                    if (null != display.getEnchantment(EnchantmentType.ONE_FOR_ALL) && SMaterial.ENCHANTED_BOOK != display.getType() && GenericItemType.WEAPON == display.getType().getStatistics().getType()) {
                        for (Enchantment enchantment2 : display.getEnchantments()) {
                            if (enchantment2.getType() == EnchantmentType.TELEKINESIS || enchantment2.getType() == EnchantmentType.ONE_FOR_ALL) continue;
                            display.removeEnchantment(enchantment2.getType());
                        }
                    }
                    if (SMaterial.ENCHANTED_BOOK != display.getType() && null != display.getEnchantments() && 1 < (list = Enchantment.ultimateEnchantsListFromList(display.getEnchantments())).size()) {
                        for (int i2 = 1; i2 < list.size(); ++i2) {
                            display.removeEnchantment(list.get(i2).getType());
                        }
                    }
                } else if (SMaterial.HOT_POTATO_BOOK == specSacrifice.getType()) {
                    display.setDataInt("hpb", display.getDataInt("hpb") + 1);
                } else if (SMaterial.HIDDEN_ETHERWARP_CONDUIT == specUpgrade.getType() && SMaterial.HIDDEN_ETHERWARP_MERGER == specSacrifice.getType()) {
                    display = SItem.of(SMaterial.HIDDEN_ETHERWARP_TRANSCODER);
                } else if ((SMaterial.ASPECT_OF_THE_VOID == specUpgrade.getType() || SMaterial.ASPECT_OF_THE_END == specUpgrade.getType()) && SMaterial.HIDDEN_ETHERWARP_TRANSCODER == specSacrifice.getType()) {
                    display.setDataString("etherwarp_trans", "true");
                }
                inventory.setItem(13, display.getStack());
                inventory.setItem(22, AnvilGUI.getCombineItemsForXP(0));
            }
        }.runTaskLater((Plugin)SkyBlock.getPlugin(), 0L);
    }

    @Override
    public Material getBlock() {
        return Material.ANVIL;
    }

    private static void setItemTo(boolean upgrade, boolean green, Inventory inventory) {
        for (int i2 : upgrade ? Arrays.asList(11, 12, 20) : Arrays.asList(14, 15, 24)) {
            inventory.setItem(i2, SUtil.getSingleLoreStack(ChatColor.GOLD + "Item to " + (upgrade ? "Upgrade" : "Sacrifice"), Material.STAINED_GLASS_PANE, (short)(green ? 5 : 14), 1, upgrade ? "The item you want to upgrade should be placed in the slot on this side." : "The item you are sacrificing in order to upgrade the item on the left should be placed in the slot on this side."));
        }
    }

    private static ItemStack getCombineItemsForXP(int levels) {
        return SUtil.getStack(ChatColor.GREEN + "Combine Items", Material.ANVIL, (short)0, 1, ChatColor.GRAY + "Combine the items in the slots", ChatColor.GRAY + "to the left and right below.", "", ChatColor.GRAY + "Cost", ChatColor.DARK_AQUA + "" + levels + " Exp Level" + (1 != levels ? "s" : ""), "", ChatColor.YELLOW + "Click to combine!");
    }
}

