package net.hypixel.skyblock.gui;

import net.hypixel.skyblock.Repeater;
import net.hypixel.skyblock.SkyBlock;
import net.hypixel.skyblock.features.calendar.SkyBlockCalendar;
import net.hypixel.skyblock.features.collection.ItemCollection;
import net.hypixel.skyblock.item.pet.Pet;
import net.hypixel.skyblock.user.PlayerStatistics;
import net.hypixel.skyblock.user.PlayerUtils;
import net.hypixel.skyblock.user.User;
import net.hypixel.skyblock.util.SUtil;
import net.hypixel.skyblock.util.Sputnik;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;

public class SkyBlockMenuGUI extends GUI {
    public SkyBlockMenuGUI() {
        super("SkyBlock Menu", 54);
    }

    @Override
    public void onOpen(final GUIOpenEvent e) {
        this.fill(BLACK_STAINED_GLASS_PANE);
        final Player player = e.getPlayer();
        final User user = User.getUser(player.getUniqueId());
        final PlayerStatistics statistics = PlayerUtils.STATISTICS_CACHE.get(player.getUniqueId());
        this.set(new GUIClickableItem() {
            @Override
            public void run(final InventoryClickEvent e) {
                GUIType.SKYBLOCK_PROFILE.getGUI().open((Player) e.getWhoClicked());
            }

            @Override
            public int getSlot() {
                return 13;
            }

            @Override
            public ItemStack getItem() {
                ItemStack itemstack = null;
                Double visualcap = statistics.getCritChance().addAll() * 100.0;
                if (visualcap > 100.0) {
                    visualcap = 100.0;
                }
                String feroDisplay = "";
                itemstack = statistics.getFerocity().addAll() > 0.0 ? SUtil.getSkullStack(ChatColor.GREEN + "Your SkySim Profile", player.getName(), 1, ChatColor.RED + "  \u2764 Health " + ChatColor.WHITE + SUtil.commaify(statistics.getMaxHealth().addAll().intValue()) + " HP", ChatColor.GREEN + "  \u2748 Defense " + ChatColor.WHITE + SUtil.commaify(statistics.getDefense().addAll().intValue()), ChatColor.RED + "  \u2741 Strength " + ChatColor.WHITE + SUtil.commaify(statistics.getStrength().addAll().intValue()), ChatColor.WHITE + "  \u2726 Speed " + SUtil.commaify(Double.valueOf(statistics.getSpeed().addAll() * 100.0).intValue()), ChatColor.BLUE + "  \u2623 Crit Chance " + ChatColor.WHITE + SUtil.commaify(visualcap.intValue()) + "%", ChatColor.BLUE + "  \u2620 Crit Damage " + ChatColor.WHITE + SUtil.commaify(Double.valueOf(statistics.getCritDamage().addAll() * 100.0).intValue()) + "%", ChatColor.AQUA + "  \u270e Intelligence " + ChatColor.WHITE + SUtil.commaify(statistics.getIntelligence().addAll().intValue()), ChatColor.YELLOW + "  \u2694 Bonus Attack Speed " + ChatColor.WHITE + SUtil.commaify(Double.valueOf(Math.min(100.0, statistics.getAttackSpeed().addAll())).intValue()) + "%", ChatColor.DARK_AQUA + "  \u03b1 Sea Creature Chance " + ChatColor.RED + "\u2717", ChatColor.LIGHT_PURPLE + "  \u2663 Pet Luck " + ChatColor.RED + "\u2717", ChatColor.AQUA + "  \u272f Magic Find " + ChatColor.WHITE + SUtil.commaify(Double.valueOf(statistics.getMagicFind().addAll() * 100.0).intValue()), ChatColor.RED + "  \u2afd Ferocity " + ChatColor.WHITE + SUtil.commaify(statistics.getFerocity().addAll().intValue()), ChatColor.RED + "  \u0e51 Ability Damage " + ChatColor.WHITE + SUtil.commaify(statistics.getAbilityDamage().addAll().intValue()) + "%", " ", ChatColor.YELLOW + "Click to view your profile!") : SUtil.getSkullStack(ChatColor.GREEN + "Your SkySim Profile", player.getName(), 1, ChatColor.RED + "  \u2764 Health " + ChatColor.WHITE + SUtil.commaify(statistics.getMaxHealth().addAll().intValue()) + " HP", ChatColor.GREEN + "  \u2748 Defense " + ChatColor.WHITE + SUtil.commaify(statistics.getDefense().addAll().intValue()), ChatColor.RED + "  \u2741 Strength " + ChatColor.WHITE + SUtil.commaify(statistics.getStrength().addAll().intValue()), ChatColor.WHITE + "  \u2726 Speed " + SUtil.commaify(Double.valueOf(statistics.getSpeed().addAll() * 100.0).intValue()), ChatColor.BLUE + "  \u2623 Crit Chance " + ChatColor.WHITE + SUtil.commaify(visualcap.intValue()) + "%", ChatColor.BLUE + "  \u2620 Crit Damage " + ChatColor.WHITE + SUtil.commaify(Double.valueOf(statistics.getCritDamage().addAll() * 100.0).intValue()) + "%", ChatColor.AQUA + "  \u270e Intelligence " + ChatColor.WHITE + SUtil.commaify(statistics.getIntelligence().addAll().intValue()), ChatColor.YELLOW + "  \u2694 Bonus Attack Speed " + ChatColor.WHITE + SUtil.commaify(Double.valueOf(Math.min(100.0, statistics.getAttackSpeed().addAll())).intValue()) + "%", ChatColor.DARK_AQUA + "  \u03b1 Sea Creature Chance " + ChatColor.RED + "\u2717", ChatColor.LIGHT_PURPLE + "  \u2663 Pet Luck " + ChatColor.RED + "\u2717", ChatColor.AQUA + "  \u272f Magic Find " + ChatColor.WHITE + SUtil.commaify(Double.valueOf(statistics.getMagicFind().addAll() * 100.0).intValue()), ChatColor.RED + "  \u0e51 Ability Damage " + ChatColor.WHITE + SUtil.commaify(statistics.getAbilityDamage().addAll().intValue()) + "%", " ", ChatColor.YELLOW + "Click to view your profile!");
                return itemstack;
            }
        });
        this.set(new GUIClickableItem() {
            @Override
            public void run(final InventoryClickEvent e) {
                GUIType.SKILL_MENU.getGUI().open(player);
            }

            @Override
            public int getSlot() {
                return 19;
            }

            @Override
            public ItemStack getItem() {
                return SUtil.getStack(ChatColor.GREEN + "Your Skills", Material.DIAMOND_SWORD, (short) 0, 1, ChatColor.GRAY + "View your Skill progression and", ChatColor.GRAY + "rewards.", " ", ChatColor.YELLOW + "Click to view!");
            }
        });

        final String[] progress = ItemCollection.getProgress(player, null);
        this.set(new GUIClickableItem() {
            @Override
            public void run(final InventoryClickEvent e) {
                GUIType.COLLECTION_MENU.getGUI().open(player);
            }

            @Override
            public int getSlot() {
                return 20;
            }

            @Override
            public ItemStack getItem() {
                return SUtil.getStack(ChatColor.GREEN + "Collection", Material.PAINTING, (short) 0, 1, ChatColor.GRAY + "View all of the items available", ChatColor.GRAY + "in SkyBlock. Collect more of an", ChatColor.GRAY + "item to unlock rewards on your", ChatColor.GRAY + "way to becoming a master of", ChatColor.GRAY + "SkyBlock!", " ", ChatColor.YELLOW + "Click to view!");
            }
        });
        this.set(new GUIClickableItem() {
            @Override
            public void run(final InventoryClickEvent e) {
                player.playSound(player.getLocation(), Sound.CHEST_OPEN, 1.0f, 0.0f);
                player.openInventory(player.getEnderChest());
            }

            @Override
            public int getSlot() {
                return 25;
            }

            @Override
            public ItemStack getItem() {
                return SUtil.getStack(ChatColor.GREEN + "Ender Chest", Material.ENDER_CHEST, (short) 0, 1, ChatColor.GRAY + "Store global items that you want", ChatColor.GRAY + "to access at any time from", ChatColor.GRAY + "anywhere here.", " ", ChatColor.YELLOW + "Click to open!");
            }
        });
        this.set(new GUIClickableItem() {
            @Override
            public void run(final InventoryClickEvent e) {
                player.sendMessage(ChatColor.RED + "Calender coming soon!");
            }

            @Override
            public int getSlot() {
                return 24;
            }

            @Override
            public ItemStack getItem() {
                return SUtil.getStack(ChatColor.GREEN + "Calendar and Events", Material.WATCH, (short) 0, 1, ChatColor.GRAY + "In-game Calendar", ChatColor.GRAY + SkyBlockCalendar.getMonthName() + " " + SUtil.ntify(SkyBlockCalendar.getDay()), " ", ChatColor.YELLOW + "Coming Soon!");
            }
        });
        this.set(new GUIClickableItem() {
            @Override
            public void run(final InventoryClickEvent e) {
                player.sendMessage(ChatColor.RED + "Quests coming soon!");
            }

            @Override
            public int getSlot() {
                return 23;
            }

            @Override
            public ItemStack getItem() {
                return SUtil.getStack(ChatColor.GREEN + "Quest Log", Material.BOOK_AND_QUILL, (short) 0, 1, ChatColor.GRAY + "View your active quests.", " ", ChatColor.YELLOW + "Coming Soon!");
            }
        });
        this.set(new GUIClickableItem() {
            @Override
            public void run(final InventoryClickEvent e) {
                player.sendMessage(ChatColor.RED + "Coming Soon!");
            }

            @Override
            public int getSlot() {
                return 22;
            }

            @Override
            public ItemStack getItem() {
                return SUtil.getStack(ChatColor.GREEN + "Trades", Material.EMERALD, (short) 0, 1, ChatColor.GRAY + "View your available Trades.", " ", ChatColor.YELLOW + "Coming Soon!");
            }
        });
        this.set(new GUIClickableItem() {
            @Override
            public void run(final InventoryClickEvent e) {
                new RecipeBookListGUI(player).open(player);
            }

            @Override
            public int getSlot() {
                return 21;
            }

            @Override
            public ItemStack getItem() {
                return SUtil.getStack(ChatColor.GREEN + "Recipe Book", Material.BOOK, (short) 0, 1, ChatColor.GRAY + "View your available crafting.", ChatColor.GRAY + "recipes", " ", ChatColor.YELLOW + "Click to open");
            }
        });
        if (user.getEffects().size() > 0) {
            this.set(new GUIClickableItem() {
                @Override
                public void run(final InventoryClickEvent e) {
                    GUIType.ACTIVE_EFFECTS.getGUI().open(player);
                }

                @Override
                public int getSlot() {
                    return 29;
                }

                @Override
                public ItemStack getItem() {
                    return SUtil.getStack(ChatColor.GREEN + "Active Effects", Material.POTION, (short) 0, 1, ChatColor.GRAY + "View and manage all of your", ChatColor.GRAY + "active potion effects.", " ", ChatColor.GRAY + "Drink Potions or splash them", ChatColor.GRAY + "on the ground to buff yourself!", " ", ChatColor.GRAY + "Currently Active: " + ChatColor.YELLOW + user.getEffects().size());
                }
            });
        }
        if (user.getPets().size() > 0) {
            final Pet.PetItem active = user.getActivePet();
            String name;
            if (active == null) {
                name = ChatColor.RED + "None";
            } else {
                name = active.getRarity().getColor() + active.getType().getDisplayName(active.getType().getData());
            }
            this.set(new GUIClickableItem() {
                @Override
                public void run(final InventoryClickEvent e) {
                    GUIType.PETS.getGUI().open(player);
                }

                @Override
                public int getSlot() {
                    return 30;
                }

                @Override
                public ItemStack getItem() {
                    return SUtil.getStack(ChatColor.GREEN + "Pets", Material.BONE, (short) 0, 1, ChatColor.GRAY + "View and manage all of your", ChatColor.GRAY + "Pets.", " ", ChatColor.GRAY + "Level up your pets faster by", ChatColor.GRAY + "gaining XP in their favorite", ChatColor.GRAY + "skill!", " ", ChatColor.GRAY + "Selected pet: " + name, " ", ChatColor.YELLOW + "Click to view!");
                }
            });
        }
        this.set(new GUIClickableItem() {
            @Override
            public void run(final InventoryClickEvent e) {
                GUIType.CRAFTING_TABLE.getGUI().open(player);
            }

            @Override
            public int getSlot() {
                return 31;
            }

            @Override
            public ItemStack getItem() {
                return SUtil.getStack(ChatColor.GREEN + "Crafting Table", Material.WORKBENCH, (short) 0, 1, ChatColor.GRAY + "Opens the crafting grid.", " ", ChatColor.YELLOW + "Click to open!");
            }
        });
        this.set(new GUIClickableItem() {
            @Override
            public void run(final InventoryClickEvent e) {
                player.sendMessage(ChatColor.RED + "Wardrobe is being reworked due to serve performance issues! If you lost your armors set stored inside, we're sorry, you can ask staff members to re-make it for you.");
            }

            @Override
            public int getSlot() {
                return 32;
            }

            @Override
            public ItemStack getItem() {
                final ItemStack i = new ItemStack(Material.LEATHER_CHESTPLATE);
                final LeatherArmorMeta im1 = (LeatherArmorMeta) i.getItemMeta();
                im1.setDisplayName(ChatColor.GREEN + "Wardrobe");
                im1.setColor(Color.fromRGB(127, 63, 178));
                final ArrayList<String> lore = new ArrayList<String>();
                lore.add(ChatColor.GRAY + "Store armors and quickly");
                lore.add(ChatColor.GRAY + "swap between them!");
                lore.add(ChatColor.GRAY + "");
                lore.add(ChatColor.RED + "Disabled!");
                im1.setLore(lore);
                i.setItemMeta(im1);
                return i;
            }
        });
        this.set(new GUIClickableItem() {
            @Override
            public void run(final InventoryClickEvent e) {
                GUIType.BANKER.getGUI().open(player);
            }

            @Override
            public int getSlot() {
                return 33;
            }

            @Override
            public ItemStack getItem() {
                return SUtil.getSkullURLStack(ChatColor.GREEN + "Personal Bank", "bf75d1b785d18d47b3ea8f0a7e0fd4a1fae9e7d323cf3b138c8c78cfe24ee59", 1, ChatColor.GRAY + "Contact your Banker from", ChatColor.GRAY + "anywhere", ChatColor.GRAY + "Cooldown: " + ChatColor.GREEN + "No Cooldown", ChatColor.GRAY + " ", ChatColor.GRAY + "Banker Status:", ChatColor.GREEN + "Avalible", " ", ChatColor.YELLOW + "Click to open!");
            }
        });
        this.set(new GUIClickableItem() {
            @Override
            public void run(final InventoryClickEvent e) {
                GUIType.QUIVER.getGUI().open(player);
            }

            @Override
            public int getSlot() {
                return 53;
            }

            @Override
            public ItemStack getItem() {
                return SUtil.getSkullURLStack(ChatColor.GREEN + "Quiver", "4cb3acdc11ca747bf710e59f4c8e9b3d949fdd364c6869831ca878f0763d1787", 1, ChatColor.GRAY + "A masterfully crafted Quiver", ChatColor.GRAY + "which holds any kind of", ChatColor.GRAY + "projectile you can think of!", " ", ChatColor.YELLOW + "Click to open!");
            }
        });
        this.set(new GUIClickableItem() {
            @Override
            public void run(final InventoryClickEvent e) {
                player.chat("/warp");
            }

            @Override
            public int getSlot() {
                return 48;
            }

            @Override
            public ItemStack getItem() {
                return SUtil.getSkullURLStack(ChatColor.AQUA + "Fast Travel", "c9c8881e42915a9d29bb61a16fb26d059913204d265df5b439b3d792acd56", 1, ChatColor.GRAY + "Teleport to islands that are", ChatColor.GRAY + "available to the public.", " ", ChatColor.YELLOW + "Click to pick location!");
            }
        });
        this.set(new GUIClickableItem() {
            @Override
            public void run(final InventoryClickEvent e) {
                e.getWhoClicked().sendMessage(ChatColor.RED + "This feature is under development!");
            }

            @Override
            public int getSlot() {
                return 50;
            }

            @Override
            public ItemStack getItem() {
                return SUtil.getStack(ChatColor.GREEN + "Settings", Material.REDSTONE_TORCH_ON, (short) 0, 1, Sputnik.trans("&c&lCOMING SOON!"));
            }
        });
        this.set(new GUIClickableItem() {
            @Override
            public void run(final InventoryClickEvent e) {
                GUIType.COOKIE_GUI.getGUI().open(player);
            }

            @Override
            public int getSlot() {
                return 49;
            }

            @Override
            public ItemStack getItem() {
                String text_ = "&7Status";
                if (PlayerUtils.getCookieDurationTicks(player) > 0L) {
                    text_ = "&7Duration";
                }
                return SUtil.enchant(SUtil.getStack(ChatColor.GOLD + "Booster Cookie", Material.COOKIE, (short) 0, 1, Sputnik.trans("&7Obtain the &dCookie Buff"), Sputnik.trans("&7from booster cookies in the"), Sputnik.trans("&7hub's community shop."), " ", Sputnik.trans(text_ + "&7: " + PlayerUtils.getCookieDurationDisplayGUI(player)), " ", ChatColor.YELLOW + "Click to get all the info!"));
            }
        });
        new BukkitRunnable() {
            public void run() {
                String text_ = "&7Status";
                if (PlayerUtils.getCookieDurationTicks(player) > 0L) {
                    text_ = "&7Duration";
                }
                if (SkyBlockMenuGUI.this != GUI_MAP.get(player.getUniqueId())) {
                    this.cancel();
                    return;
                }
                final InventoryView stackInventory = player.getOpenInventory();
                final ItemStack craftStack = stackInventory.getItem(49);
                final ItemMeta meta = craftStack.getItemMeta();
                meta.setLore(Arrays.asList(Sputnik.trans("&7Obtain the &dCookie Buff"), Sputnik.trans("&7from booster cookies in the"), Sputnik.trans("&7hub's community shop."), " ", Sputnik.trans(text_ + "&7: " + PlayerUtils.getCookieDurationDisplayGUI(player)), " ", ChatColor.YELLOW + "Click to get all the info!"));
                craftStack.setItemMeta(meta);
            }
        }.runTaskTimer(SkyBlock.getPlugin(), 0L, 20L);
    }
}
