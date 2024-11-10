/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.md_5.bungee.api.ChatColor
 *  org.bukkit.Effect
 *  org.bukkit.Location
 *  org.bukkit.Sound
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 */
package net.hypixel.skyblock.item.armor;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.hypixel.skyblock.Repeater;
import net.hypixel.skyblock.SkyBlock;
import net.hypixel.skyblock.item.Ability;
import net.hypixel.skyblock.item.AbilityActivation;
import net.hypixel.skyblock.item.GenericItemType;
import net.hypixel.skyblock.item.MaterialFunction;
import net.hypixel.skyblock.item.Rarity;
import net.hypixel.skyblock.item.SItem;
import net.hypixel.skyblock.item.SMaterial;
import net.hypixel.skyblock.item.SkullStatistics;
import net.hypixel.skyblock.item.SpecificItemType;
import net.hypixel.skyblock.item.ToolStatistics;
import net.hypixel.skyblock.user.PlayerUtils;
import net.hypixel.skyblock.user.User;
import net.hypixel.skyblock.util.DefenseReplacement;
import net.hypixel.skyblock.util.ManaReplacement;
import net.hypixel.skyblock.util.SUtil;
import net.hypixel.skyblock.util.Sputnik;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class VoidlingsWardenHelmet
implements MaterialFunction,
SkullStatistics,
ToolStatistics,
Ability {
    public static int serverIterator = 0;
    private static String[] texture;
    static Map<UUID, Boolean> COOLDOWN_VOIDLINGS;
    public static final Map<UUID, Integer> VOIDLING_SHIELD;
    public static final Map<UUID, Boolean> VOIDLING_WARDEN_BUFF;

    public static void startCounting() {
        new BukkitRunnable(){

            public void run() {
                serverIterator = serverIterator >= 12 ? 0 : ++serverIterator;
            }
        }.runTaskTimer((Plugin)SkyBlock.getPlugin(), 3L, 3L);
    }

    public static String getTexture() {
        return "bd86e123a0a3803bd4d629450ff066b509018ff1fe7b3db62047eca729771950";
    }

    @Override
    public double getBaseHealth() {
        return 1000.0;
    }

    @Override
    public double getBaseDefense() {
        return 500.0;
    }

    @Override
    public double getBaseStrength() {
        return 150.0;
    }

    @Override
    public double getBaseCritDamage() {
        return 0.35;
    }

    @Override
    public double getBaseCritChance() {
        return 0.15;
    }

    @Override
    public double getBaseFerocity() {
        return 30.0;
    }

    @Override
    public String getURL() {
        return "bd86e123a0a3803bd4d629450ff066b509018ff1fe7b3db62047eca729771950";
    }

    @Override
    public String getDisplayName() {
        return "Voidlings Warden Helmet";
    }

    @Override
    public Rarity getRarity() {
        return Rarity.MYTHIC;
    }

    @Override
    public GenericItemType getType() {
        return GenericItemType.ARMOR;
    }

    @Override
    public SpecificItemType getSpecificType() {
        return SpecificItemType.HELMET;
    }

    @Override
    public String getAbilityName() {
        return "Voidling's Stronghold";
    }

    @Override
    public String getAbilityDescription() {
        return Sputnik.trans("Combine the Power of the &dVoidlings &7and grant you &e100 &eHits &7of &bdamage immunity&7, during this period, you can still damage mobs normally. After the shield breaks, heal &c50% &7slower for &a15s &7but dealing &c+50% &7more damage for the next &a15s&7. The shield only last for &c60 seconds&7!");
    }

    @Override
    public int getAbilityCooldownTicks() {
        return 0;
    }

    @Override
    public AbilityActivation getAbilityActivation() {
        return AbilityActivation.SNEAK;
    }

    @Override
    public void onAbilityUse(Player player, SItem sItem) {
        int cost;
        boolean take;
        int manaPool;
        User user = User.getUser(player.getUniqueId());
        if (user.isVoidlingWardenActive()) {
            user.toBukkitPlayer().sendMessage(Sputnik.trans("&cThe ability is already active!"));
            return;
        }
        if (COOLDOWN_VOIDLINGS.containsKey(user.getUuid())) {
            user.toBukkitPlayer().sendMessage(Sputnik.trans("&cThe ability is on cooldown! Please wait!"));
            user.toBukkitPlayer().playSound(user.toBukkitPlayer().getLocation(), Sound.ENDERMAN_TELEPORT, 1.0f, -4.0f);
            return;
        }
        int manaCost = manaPool = SUtil.blackMagic(PlayerUtils.STATISTICS_CACHE.get(player.getUniqueId()).getIntelligence().addAll() + 100.0);
        if (Repeater.MANA_MAP.get(player.getUniqueId()) > manaCost) {
            manaCost += Repeater.MANA_MAP.get(player.getUniqueId()) - manaPool;
        }
        if (!(take = PlayerUtils.takeMana(player, cost = PlayerUtils.getFinalManaCost(player, sItem, manaCost)))) {
            player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1.0f, -4.0f);
            final long c = System.currentTimeMillis();
            Repeater.MANA_REPLACEMENT_MAP.put(player.getUniqueId(), new ManaReplacement(){

                @Override
                public String getReplacement() {
                    return "" + ChatColor.RED + ChatColor.BOLD + "NOT ENOUGH MANA";
                }

                @Override
                public long getEnd() {
                    return c + 1500L;
                }
            });
            return;
        }
        final long c = System.currentTimeMillis();
        Repeater.DEFENSE_REPLACEMENT_MAP.put(player.getUniqueId(), new DefenseReplacement(){

            @Override
            public String getReplacement() {
                return ChatColor.AQUA + "-" + cost + " Mana (" + ChatColor.GOLD + VoidlingsWardenHelmet.this.getAbilityName() + ChatColor.AQUA + ")";
            }

            @Override
            public long getEnd() {
                return c + 2000L;
            }
        });
        COOLDOWN_VOIDLINGS.put(user.getUuid(), true);
        user.setVoidlingWardenActive(true);
        VOIDLING_SHIELD.put(user.getUuid(), 100);
        this.startLoop(player);
        user.send("&6Voidgloom's Stronghold &ais now &a&lACTIVATED&a!");
        SUtil.delay(() -> player.playSound(player.getLocation(), Sound.ZOMBIE_UNFECT, 1.0f, 1.2f), 1L);
        SUtil.delay(() -> {
            COOLDOWN_VOIDLINGS.remove(user.getUuid());
            if (user != null && user.toBukkitPlayer().isOnline()) {
                user.send("&6Voidgloom's Stronghold &ais now available!");
            }
        }, 600L);
    }

    public void startLoop(final Player e) {
        new BukkitRunnable(){
            float cout;
            int i;
            {
                this.cout = e.getLocation().getYaw();
                this.i = 0;
            }

            public void run() {
                if (!(e.isOnline() && User.getUser(e.getUniqueId()).isVoidlingWardenActive() && VoidlingsWardenHelmet.this.checkHelmet(e))) {
                    if (e.isOnline() && VoidlingsWardenHelmet.this.checkHelmet(e)) {
                        VoidlingsWardenHelmet.activeVoidlingsBuff(e);
                    }
                    if (e.isOnline()) {
                        User.getUser(e.getUniqueId()).setVoidlingWardenActive(false);
                    }
                    VOIDLING_SHIELD.remove(e.getUniqueId());
                    this.cancel();
                    return;
                }
                ++this.i;
                User user = User.getUser(e.getUniqueId());
                if (this.i >= 1200 && user != null && user.toBukkitPlayer().isOnline()) {
                    user.setVoidlingWardenActive(false);
                }
                Location loc = e.getLocation();
                loc.setYaw(this.cout);
                loc.setPitch(0.0f);
                loc.add(loc.getDirection().normalize().multiply(0.6));
                int hitshield = VOIDLING_SHIELD.get(e.getUniqueId());
                int hitshieldmax = 100;
                int stage = 3;
                if (hitshield <= 50 && hitshield > 25) {
                    stage = 2;
                } else if (hitshield <= 25 && hitshield != 1) {
                    stage = 1;
                } else if (hitshield == 1) {
                    stage = 1;
                }
                if (VOIDLING_SHIELD.get(e.getUniqueId()) > 0) {
                    e.getWorld().spigot().playEffect(loc, Effect.WITCH_MAGIC, 0, 1, 1.0f, 1.0f, 1.0f, 0.0f, 0, 64);
                    e.getWorld().spigot().playEffect(loc.clone().add(0.0, 0.6, 0.0), Effect.WITCH_MAGIC, 0, 1, 1.0f, 1.0f, 1.0f, 0.0f, 0, 64);
                    if (stage >= 2) {
                        e.getWorld().spigot().playEffect(loc.clone().add(0.0, 1.2, 0.0), Effect.WITCH_MAGIC, 0, 1, 1.0f, 1.0f, 1.0f, 0.0f, 0, 64);
                    }
                    if (stage == 3) {
                        e.getWorld().spigot().playEffect(loc.clone().add(0.0, 1.8, 0.0), Effect.WITCH_MAGIC, 0, 1, 1.0f, 1.0f, 1.0f, 0.0f, 0, 64);
                    }
                }
                this.cout += 18.0f;
            }
        }.runTaskTimer((Plugin)SkyBlock.getPlugin(), 0L, 1L);
    }

    public boolean checkHelmet(Player p) {
        ItemStack is = p.getInventory().getHelmet();
        return is != null && SItem.find(is) != null && SItem.find(is).getType() == SMaterial.HIDDEN_VOIDLINGS_WARDEN_HELMET;
    }

    public static void activeVoidlingsBuff(Player p) {
        VOIDLING_WARDEN_BUFF.put(p.getUniqueId(), true);
        p.sendMessage(Sputnik.trans("&cYour Hitshield have broken! &6Your Voidling's Stronghold Buff is now active for the next &a20 seconds&6!"));
        p.playSound(p.getLocation(), Sound.ZOMBIE_REMEDY, 1.0f, 1.2f);
        SUtil.delay(() -> p.playSound(p.getLocation(), Sound.ZOMBIE_UNFECT, 1.0f, 1.2f), 5L);
        for (int i = 0; i < 30; ++i) {
            SUtil.delay(() -> {
                p.getWorld().playEffect(p.getLocation().add(0.0, 0.1, 0.0), Effect.FLYING_GLYPH, 0);
                p.getWorld().spigot().playEffect(p.getLocation().clone().add(0.0, 0.5, 0.0), Effect.WITCH_MAGIC, 0, 1, (float)SUtil.random(-0.5, 0.5), (float)SUtil.random(0.0, 0.5), (float)SUtil.random(-0.5, 0.5), 0.0f, 1, 20);
                p.getWorld().spigot().playEffect(p.getLocation().clone().add(0.0, 0.25, 0.0), Effect.MAGIC_CRIT, 0, 1, (float)SUtil.random(-0.5, 0.5), (float)SUtil.random(0.0, 0.5), (float)SUtil.random(-0.5, 0.5), 0.0f, 1, 20);
            }, i * 2);
        }
        SUtil.delay(() -> {
            if (p.isOnline()) {
                p.sendMessage(Sputnik.trans("&cYour Voidling's Stronghold Buff is now deactivated!"));
                p.playSound(p.getLocation(), Sound.ZOMBIE_REMEDY, 1.0f, 2.0f);
            }
            VOIDLING_WARDEN_BUFF.remove(p.getUniqueId());
        }, 400L);
    }

    public static String getDisplay(Player p) {
        if (VOIDLING_SHIELD.containsKey(p.getUniqueId())) {
            int hitshield = VOIDLING_SHIELD.get(p.getUniqueId());
            int hitshieldmax = 100;
            String defineHitShield = Sputnik.trans("&f&l" + hitshield + " Hits");
            if (hitshield <= 50 && hitshield > 25) {
                defineHitShield = Sputnik.trans("&d&l" + hitshield + " Hits");
            } else if (hitshield <= 25 && hitshield != 1) {
                defineHitShield = Sputnik.trans("&5&l" + hitshield + " Hits");
            } else if (hitshield == 1) {
                defineHitShield = Sputnik.trans("&5&l" + hitshield + " Hit");
            }
            return defineHitShield;
        }
        return "";
    }

    @Override
    public String getLore() {
        return Sputnik.trans(ChatColor.GRAY + "Halves your speed but grants " + ChatColor.RED + "+35% " + ChatColor.GRAY + "weapon damage for every " + ChatColor.GREEN + "25 " + ChatColor.GRAY + "speed.");
    }

    @Override
    public int getManaCost() {
        return 0;
    }

    @Override
    public boolean displayUsage() {
        return false;
    }

    static {
        COOLDOWN_VOIDLINGS = new HashMap<UUID, Boolean>();
        VOIDLING_SHIELD = new HashMap<UUID, Integer>();
        VOIDLING_WARDEN_BUFF = new HashMap<UUID, Boolean>();
    }
}

