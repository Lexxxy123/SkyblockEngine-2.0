package vn.giakhanhvn.skysim.gui;

import org.bukkit.plugin.Plugin;
import vn.giakhanhvn.skysim.SkySimEngine;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.event.inventory.InventoryClickEvent;
import vn.giakhanhvn.skysim.item.pet.Pet;
import vn.giakhanhvn.skysim.item.SItem;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.List;
import org.bukkit.ChatColor;
import java.util.ArrayList;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import vn.giakhanhvn.skysim.util.Sputnik;
import vn.giakhanhvn.skysim.util.SUtil;
import vn.giakhanhvn.skysim.placeholding;
import org.bukkit.inventory.ItemStack;
import vn.giakhanhvn.skysim.user.User;
import org.bukkit.entity.Player;

public class ProfileViewerGUI extends GUI
{
    public Player p;
    
    public ProfileViewerGUI(final Player player) {
        super(player.getName() + "'s Profile", 54);
        this.p = player;
    }
    
    @Override
    public void onOpen(final GUIOpenEvent e) {
        if (this.p == null) {
            return;
        }
        this.fill(ProfileViewerGUI.BLACK_STAINED_GLASS_PANE);
        final Player player = e.getPlayer();
        final User user = User.getUser(this.p.getUniqueId());
        this.set(GUIClickableItem.getCloseItem(49));
        this.set(new GUIItem() {
            @Override
            public int getSlot() {
                return 22;
            }
            
            @Override
            public ItemStack getItem() {
                final placeholding pl = new placeholding();
                final ItemStack itemstack = SUtil.getSkullStack(ProfileViewerGUI.this.p.getDisplayName(), ProfileViewerGUI.this.p.getName(), 1, Sputnik.trans("  &c❤ Health &f" + SUtil.commaify(Math.round(ProfileViewerGUI.this.p.getHealth())) + " HP"), Sputnik.trans("  &a❈ Defense&f " + pl.onRequest((OfflinePlayer)ProfileViewerGUI.this.p, "defense")), Sputnik.trans("  &c❁ Strength&f " + pl.onRequest((OfflinePlayer)ProfileViewerGUI.this.p, "strength")), Sputnik.trans("  &f✦ Speed " + pl.onRequest((OfflinePlayer)ProfileViewerGUI.this.p, "speed")), Sputnik.trans("  &9☣ Crit Chance&f " + pl.onRequest((OfflinePlayer)ProfileViewerGUI.this.p, "critchance") + "%"), Sputnik.trans("  &9☠ Crit Damage&f " + pl.onRequest((OfflinePlayer)ProfileViewerGUI.this.p, "critdamage") + "%"), Sputnik.trans("  &b✎ Intelligence&f " + pl.onRequest((OfflinePlayer)ProfileViewerGUI.this.p, "int")), Sputnik.trans("  &e⚔ Bonus Attack Speed&f " + pl.onRequest((OfflinePlayer)ProfileViewerGUI.this.p, "atkSpeed") + "%"), Sputnik.trans("  &c⫽ Ferocity&f " + pl.onRequest((OfflinePlayer)ProfileViewerGUI.this.p, "ferocity")), Sputnik.trans("  &c๑ Ability Damage&f " + pl.onRequest((OfflinePlayer)ProfileViewerGUI.this.p, "abilityDamage") + "%"), " ", Sputnik.trans("&8Skill Average: &6N/A &7(non-cosmetic)"), " ");
                return itemstack;
            }
        });
        this.set(new GUIItem() {
            @Override
            public int getSlot() {
                return 1;
            }
            
            @Override
            public ItemStack getItem() {
                if (ProfileViewerGUI.this.p.getItemInHand() != null && ProfileViewerGUI.this.p.getItemInHand().getType() != Material.AIR) {
                    return ProfileViewerGUI.this.p.getItemInHand();
                }
                final List<String> lore = new ArrayList<String>();
                final ItemStack gst = SUtil.createColoredStainedGlassPane((short)0, Sputnik.trans("&eHeld Item"));
                lore.add(ChatColor.RED + "Empty");
                final ItemMeta met = gst.getItemMeta();
                met.setLore((List)lore);
                gst.setItemMeta(met);
                return gst;
            }
        });
        this.set(new GUIItem() {
            @Override
            public int getSlot() {
                return 10;
            }
            
            @Override
            public ItemStack getItem() {
                if (ProfileViewerGUI.this.p.getInventory().getHelmet() != null) {
                    return ProfileViewerGUI.this.p.getInventory().getHelmet();
                }
                final List<String> lore = new ArrayList<String>();
                lore.add(ChatColor.RED + "Empty");
                final ItemStack gst = SUtil.createColoredStainedGlassPane((short)0, Sputnik.trans("&eHelmet"));
                final ItemMeta met = gst.getItemMeta();
                met.setLore((List)lore);
                gst.setItemMeta(met);
                return gst;
            }
        });
        this.set(new GUIItem() {
            @Override
            public int getSlot() {
                return 19;
            }
            
            @Override
            public ItemStack getItem() {
                if (ProfileViewerGUI.this.p.getInventory().getChestplate() != null) {
                    return ProfileViewerGUI.this.p.getInventory().getChestplate();
                }
                final List<String> lore = new ArrayList<String>();
                lore.add(ChatColor.RED + "Empty");
                final ItemStack gst = SUtil.createColoredStainedGlassPane((short)0, Sputnik.trans("&eChestplate"));
                final ItemMeta met = gst.getItemMeta();
                met.setLore((List)lore);
                gst.setItemMeta(met);
                return gst;
            }
        });
        this.set(new GUIItem() {
            @Override
            public int getSlot() {
                return 28;
            }
            
            @Override
            public ItemStack getItem() {
                if (ProfileViewerGUI.this.p.getInventory().getLeggings() != null) {
                    return ProfileViewerGUI.this.p.getInventory().getLeggings();
                }
                final List<String> lore = new ArrayList<String>();
                lore.add(ChatColor.RED + "Empty");
                final ItemStack gst = SUtil.createColoredStainedGlassPane((short)0, Sputnik.trans("&eLeggings"));
                final ItemMeta met = gst.getItemMeta();
                met.setLore((List)lore);
                gst.setItemMeta(met);
                return gst;
            }
        });
        this.set(new GUIItem() {
            @Override
            public int getSlot() {
                return 37;
            }
            
            @Override
            public ItemStack getItem() {
                if (ProfileViewerGUI.this.p.getInventory().getBoots() != null) {
                    return ProfileViewerGUI.this.p.getInventory().getBoots();
                }
                final List<String> lore = new ArrayList<String>();
                lore.add(ChatColor.RED + "Empty");
                final ItemStack gst = SUtil.createColoredStainedGlassPane((short)0, Sputnik.trans("&eBoots"));
                final ItemMeta met = gst.getItemMeta();
                met.setLore((List)lore);
                gst.setItemMeta(met);
                return gst;
            }
        });
        this.set(new GUIItem() {
            @Override
            public int getSlot() {
                return 46;
            }
            
            @Override
            public ItemStack getItem() {
                if (user.getActivePet() != null) {
                    final Pet.PetItem pet = user.getActivePet();
                    final SItem item = SItem.of(pet.getType());
                    item.setRarity(pet.getRarity());
                    item.setDataDouble("xp", pet.getXp());
                    item.getData().setBoolean("equipped", true);
                    item.update();
                    final ItemStack petstack = item.getStack();
                    final ItemMeta meta = petstack.getItemMeta();
                    final List<String> newlore = item.getStack().getItemMeta().getLore();
                    newlore.add(" ");
                    newlore.add(item.getRarity().getBoldedColor() + item.getRarity().getDisplay());
                    meta.setLore((List)newlore);
                    petstack.setItemMeta(meta);
                    return petstack;
                }
                final List<String> lore = new ArrayList<String>();
                lore.add(ChatColor.RED + "Empty");
                final ItemStack gst = SUtil.createColoredStainedGlassPane((short)0, Sputnik.trans("&ePets"));
                final ItemMeta met = gst.getItemMeta();
                met.setLore((List)lore);
                gst.setItemMeta(met);
                return gst;
            }
        });
        this.set(new GUIClickableItem() {
            @Override
            public void run(final InventoryClickEvent e) {
                ((Player)e.getWhoClicked()).closeInventory();
                ((Player)e.getWhoClicked()).chat("/trade " + ProfileViewerGUI.this.p.getName());
            }
            
            @Override
            public int getSlot() {
                return 16;
            }
            
            @Override
            public ItemStack getItem() {
                return SUtil.getStack(ChatColor.GREEN + "Trade Request", Material.EMERALD, (short)0, 1, ChatColor.YELLOW + "Send a trade request");
            }
        });
        this.set(new GUIClickableItem() {
            @Override
            public void run(final InventoryClickEvent e) {
                ((Player)e.getWhoClicked()).sendMessage(ChatColor.RED + "Not available!");
            }
            
            @Override
            public int getSlot() {
                return 15;
            }
            
            @Override
            public ItemStack getItem() {
                return SUtil.getStack(ChatColor.GREEN + "Visit Island", Material.FEATHER, (short)0, 1, ChatColor.RED + "Not available!");
            }
        });
        this.set(new GUIClickableItem() {
            @Override
            public void run(final InventoryClickEvent e) {
                ((Player)e.getWhoClicked()).sendMessage(ChatColor.RED + "Coming at a later date.");
            }
            
            @Override
            public int getSlot() {
                return 25;
            }
            
            @Override
            public ItemStack getItem() {
                return SUtil.getStack(ChatColor.YELLOW + "Unfinished!", Material.DIAMOND, (short)0, 1, ChatColor.RED + "Not available!");
            }
        });
        if (player.hasPermission("system.viewinv")) {
            this.set(new GUIClickableItem() {
                @Override
                public void run(final InventoryClickEvent e) {
                    ((Player)e.getWhoClicked()).chat("/openinv " + ProfileViewerGUI.this.p.getName());
                }
                
                @Override
                public int getSlot() {
                    return 50;
                }
                
                @Override
                public ItemStack getItem() {
                    return SUtil.getStack(ChatColor.GREEN + "Open Player Inventory", Material.CHEST, (short)0, 1, ChatColor.YELLOW + "Click to view " + ProfileViewerGUI.this.p.getName() + "'s", ChatColor.YELLOW + "inventory.");
                }
            });
        }
        new BukkitRunnable() {
            public void run() {
                if (ProfileViewerGUI.this != GUI.GUI_MAP.get(player.getUniqueId())) {
                    return;
                }
                if (!ProfileViewerGUI.this.p.isOnline()) {
                    return;
                }
                new ProfileViewerGUI(ProfileViewerGUI.this.p).open(player);
            }
        }.runTaskLater((Plugin)SkySimEngine.getPlugin(), 40L);
    }
}
