/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.Effect
 *  org.bukkit.Material
 *  org.bukkit.Sound
 *  org.bukkit.entity.Player
 *  org.bukkit.event.inventory.InventoryClickEvent
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 */
package net.hypixel.skyblock.gui;

import net.hypixel.skyblock.SkyBlock;
import net.hypixel.skyblock.gui.GUI;
import net.hypixel.skyblock.gui.GUIClickableItem;
import net.hypixel.skyblock.gui.GUIOpenEvent;
import net.hypixel.skyblock.user.PlayerUtils;
import net.hypixel.skyblock.user.User;
import net.hypixel.skyblock.util.SUtil;
import net.hypixel.skyblock.util.Sputnik;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class CookieConfirmGUI
extends GUI {
    String[] suffix = new String[]{"", "&dSweet!", "&dSavory!", "&dDelicious!", "", "&dYummy!", "&dSUPREME!", "", "&dScrumptious!", "", "&dBLYATIFUL!", "&dTasty!", ""};
    private final int cookieSlot;
    private final ItemStack stack;

    public CookieConfirmGUI(int cookieSlot, ItemStack stack) {
        super("Consume Booster Cookie?", 27);
        this.fill(BLACK_STAINED_GLASS_PANE);
        this.cookieSlot = cookieSlot;
        this.stack = stack;
    }

    public CookieConfirmGUI() {
        this(1, null);
    }

    @Override
    public void onOpen(GUIOpenEvent e2) {
        final Player player = e2.getPlayer();
        User user = User.getUser(e2.getPlayer().getUniqueId());
        this.set(new GUIClickableItem(){

            @Override
            public void run(InventoryClickEvent e2) {
                if (e2.getWhoClicked().getInventory().getItem(CookieConfirmGUI.this.cookieSlot) == null || CookieConfirmGUI.this.stack == null) {
                    ((Player)e2.getWhoClicked()).playSound(e2.getWhoClicked().getLocation(), Sound.VILLAGER_NO, 1.0f, 1.0f);
                    CookieConfirmGUI.this.sendError((Player)e2.getWhoClicked());
                    return;
                }
                if (!e2.getWhoClicked().getInventory().getItem(CookieConfirmGUI.this.cookieSlot).toString().equalsIgnoreCase(CookieConfirmGUI.this.stack.toString())) {
                    ((Player)e2.getWhoClicked()).playSound(e2.getWhoClicked().getLocation(), Sound.VILLAGER_NO, 1.0f, 1.0f);
                    CookieConfirmGUI.this.sendError((Player)e2.getWhoClicked());
                    return;
                }
                e2.getWhoClicked().getInventory().setItem(CookieConfirmGUI.this.cookieSlot, null);
                PlayerUtils.setCookieDurationTicks((Player)e2.getWhoClicked(), PlayerUtils.getCookieDurationTicks(player) + 3456000L);
                e2.getWhoClicked().sendMessage(Sputnik.trans("&eYou consumed a &6Booster Cookie&e! " + CookieConfirmGUI.this.suffix[SUtil.random(0, CookieConfirmGUI.this.suffix.length - 1)]));
                e2.getWhoClicked().closeInventory();
                ((Player)e2.getWhoClicked()).playSound(e2.getWhoClicked().getLocation(), Sound.CLICK, 1.0f, 1.0f);
                CookieConfirmGUI.this.playParticle((Player)e2.getWhoClicked());
            }

            @Override
            public ItemStack getItem() {
                if (PlayerUtils.getCookieDurationTicks(player) <= 0L) {
                    return SUtil.enchant(SUtil.getStack(ChatColor.YELLOW + "Consume Cookie", Material.COOKIE, (short)0, 1, Sputnik.trans("&7Gain the &dCookie Buff&7!"), "", Sputnik.trans("&7Duration: &b2 days&7!"), "", Sputnik.trans("&7Duration after: &b" + SUtil.getFormattedTimeToDay(3456000L))));
                }
                return SUtil.enchant(SUtil.getStack(ChatColor.YELLOW + "Consume Cookie", Material.COOKIE, (short)0, 1, Sputnik.trans("&7Adds &b2 &7days to your"), Sputnik.trans("&dCookie Buff&7!"), "", Sputnik.trans("&7Duration after: &b" + SUtil.getFormattedTimeToDay(PlayerUtils.getCookieDurationTicks(player) + 3456000L))));
            }

            @Override
            public int getSlot() {
                return 11;
            }
        });
        this.set(new GUIClickableItem(){

            @Override
            public void run(InventoryClickEvent e2) {
                e2.getWhoClicked().closeInventory();
                ((Player)e2.getWhoClicked()).playSound(e2.getWhoClicked().getLocation(), Sound.CLICK, 1.0f, 1.0f);
            }

            @Override
            public ItemStack getItem() {
                return SUtil.getStack(ChatColor.RED + "Cancel", Material.STAINED_CLAY, (short)14, 1, Sputnik.trans("&7I'm not hungry..."));
            }

            @Override
            public int getSlot() {
                return 15;
            }
        });
    }

    public void sendError(Player p2) {
        p2.closeInventory();
        p2.sendMessage(ChatColor.RED + "An unexpected error occured while consuming this Booster Cookie! Contact server staff if this happends again.");
    }

    public void playParticle(final Player p2) {
        p2.getWorld().playSound(p2.getLocation(), Sound.EAT, 1.0f, 1.6f);
        SUtil.delay(() -> p2.getWorld().playSound(p2.getLocation(), Sound.EAT, 1.0f, 1.7f), 3L);
        SUtil.delay(() -> p2.getWorld().playSound(p2.getLocation(), Sound.EAT, 1.0f, 1.8f), 6L);
        new BukkitRunnable(){
            int count = 0;

            public void run() {
                if (this.count >= 8) {
                    this.cancel();
                    return;
                }
                p2.getWorld().playEffect(p2.getLocation().add(0.0, 0.5, 0.0), Effect.POTION_SWIRL, 5);
                p2.getWorld().playEffect(p2.getLocation().add(0.0, 0.5, 0.0), Effect.POTION_SWIRL, 5);
                p2.getWorld().playEffect(p2.getLocation().add(0.0, 0.5, 0.0), Effect.POTION_SWIRL, 5);
                p2.getWorld().playEffect(p2.getLocation().add(0.0, 0.5, 0.0), Effect.POTION_SWIRL, 5);
                p2.getWorld().playEffect(p2.getLocation().add(0.0, 0.5, 0.0), Effect.POTION_SWIRL, 5);
                p2.getWorld().playEffect(p2.getLocation().add(0.0, 0.5, 0.0), Effect.POTION_SWIRL, 5);
                p2.getWorld().playEffect(p2.getLocation().add(0.0, 0.5, 0.0), Effect.POTION_SWIRL, 5);
                p2.getWorld().playEffect(p2.getLocation().add(0.0, 0.5, 0.0), Effect.POTION_SWIRL, 5);
                p2.getWorld().playEffect(p2.getLocation().add(0.0, 0.5, 0.0), Effect.POTION_SWIRL, 5);
                p2.getWorld().playEffect(p2.getLocation().add(0.0, 0.5, 0.0), Effect.POTION_SWIRL, 5);
                p2.getWorld().spigot().playEffect(p2.getLocation(), Effect.FIREWORKS_SPARK, 0, 1, (float)SUtil.random(-1.0, 1.0), 1.0f, (float)SUtil.random(-1.0, 1.0), 0.0f, 1, 100);
                p2.getWorld().spigot().playEffect(p2.getLocation(), Effect.SPELL, 0, 1, (float)SUtil.random(-1.0, 1.0), 1.0f, (float)SUtil.random(-1.0, 1.0), 0.0f, 1, 100);
                p2.getWorld().spigot().playEffect(p2.getLocation(), Effect.FIREWORKS_SPARK, 0, 1, (float)SUtil.random(-1.0, 1.0), 1.0f, (float)SUtil.random(-1.0, 1.0), 0.0f, 1, 100);
                p2.getWorld().spigot().playEffect(p2.getLocation(), Effect.SPELL, 0, 1, (float)SUtil.random(-1.0, 1.0), 1.0f, (float)SUtil.random(-1.0, 1.0), 0.0f, 1, 100);
                p2.getWorld().spigot().playEffect(p2.getLocation(), Effect.FIREWORKS_SPARK, 0, 1, (float)SUtil.random(-1.0, 1.0), 1.0f, (float)SUtil.random(-1.0, 1.0), 0.0f, 1, 100);
                p2.getWorld().spigot().playEffect(p2.getLocation(), Effect.SPELL, 0, 1, (float)SUtil.random(-1.0, 1.0), 1.0f, (float)SUtil.random(-1.0, 1.0), 0.0f, 1, 100);
                p2.getWorld().spigot().playEffect(p2.getLocation(), Effect.FIREWORKS_SPARK, 0, 1, (float)SUtil.random(-1.0, 1.0), 1.0f, (float)SUtil.random(-1.0, 1.0), 0.0f, 1, 100);
                p2.getWorld().spigot().playEffect(p2.getLocation(), Effect.SPELL, 0, 1, (float)SUtil.random(-1.0, 1.0), 1.0f, (float)SUtil.random(-1.0, 1.0), 0.0f, 1, 100);
                p2.getWorld().playEffect(p2.getLocation().add(0.0, 0.5, 0.0), Effect.LAVA_POP, 5);
                p2.getWorld().playEffect(p2.getLocation().add(0.0, 0.5, 0.0), Effect.LAVA_POP, 5);
                ++this.count;
            }
        }.runTaskTimer((Plugin)SkyBlock.getPlugin(), 0L, 1L);
    }
}

