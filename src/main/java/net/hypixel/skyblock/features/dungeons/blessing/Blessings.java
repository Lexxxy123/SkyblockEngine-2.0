/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Effect
 *  org.bukkit.Location
 *  org.bukkit.Sound
 *  org.bukkit.World
 *  org.bukkit.block.Block
 *  org.bukkit.entity.ArmorStand
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 *  org.bukkit.util.Vector
 */
package net.hypixel.skyblock.features.dungeons.blessing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.hypixel.skyblock.SkyBlock;
import net.hypixel.skyblock.entity.SEntity;
import net.hypixel.skyblock.entity.SEntityType;
import net.hypixel.skyblock.features.dungeons.blessing.BlessingType;
import net.hypixel.skyblock.features.dungeons.stats.WrappedStats;
import net.hypixel.skyblock.user.PlayerStatistics;
import net.hypixel.skyblock.user.PlayerUtils;
import net.hypixel.skyblock.user.TemporaryStats;
import net.hypixel.skyblock.user.User;
import net.hypixel.skyblock.util.SUtil;
import net.hypixel.skyblock.util.Sputnik;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class Blessings {
    private final BlessingType type;
    private int level = 1;
    private double buffPercent;
    private final World world;
    public static final SkyBlock sse = SkyBlock.getPlugin();
    public static final Map<World, List<Blessings>> BLESSINGS_MAP = new HashMap<World, List<Blessings>>();
    public static final Map<UUID, float[]> STAT_MAP = new HashMap<UUID, float[]>();

    public Blessings(BlessingType type, int level, World operatedWorld) {
        this.type = type;
        this.world = operatedWorld;
        this.level = level;
    }

    public void active() {
        World operatedWorld = this.world;
        if (BLESSINGS_MAP.containsKey(operatedWorld)) {
            for (Blessings bs : BLESSINGS_MAP.get(operatedWorld)) {
                if (bs.getType() != this.type) continue;
                bs.setLevel(bs.getLevel() + this.level);
                return;
            }
            BLESSINGS_MAP.get(operatedWorld).add(this);
        } else {
            BLESSINGS_MAP.put(operatedWorld, new ArrayList());
            BLESSINGS_MAP.get(operatedWorld).add(this);
        }
    }

    public static List<Blessings> getFrom(World w2) {
        if (BLESSINGS_MAP.containsKey(w2)) {
            return BLESSINGS_MAP.get(w2);
        }
        return null;
    }

    public void remove() {
        BLESSINGS_MAP.get(this.world).remove(this);
        for (Player p2 : this.world.getPlayers()) {
            TemporaryStats ts = null;
            User u2 = User.getUser(p2.getUniqueId());
            if (null == u2) continue;
            ts = null != TemporaryStats.getFromPlayer(u2.toBukkitPlayer()) ? TemporaryStats.getFromPlayer(u2.toBukkitPlayer()) : new TemporaryStats(User.getUser(u2.toBukkitPlayer().getUniqueId()));
            ts.cleanStats();
        }
    }

    public static void resetForWorld(World w2) {
        BLESSINGS_MAP.remove(w2);
        for (Player p2 : w2.getPlayers()) {
            TemporaryStats ts = null;
            User u2 = User.getUser(p2.getUniqueId());
            if (null == u2) continue;
            ts = null != TemporaryStats.getFromPlayer(u2.toBukkitPlayer()) ? TemporaryStats.getFromPlayer(u2.toBukkitPlayer()) : new TemporaryStats(User.getUser(u2.toBukkitPlayer().getUniqueId()));
            ts.cleanStats();
        }
    }

    public WrappedStats getBlessingStats(User u2) {
        TemporaryStats ts = null;
        ts = null != TemporaryStats.getFromPlayer(u2.toBukkitPlayer()) ? TemporaryStats.getFromPlayer(u2.toBukkitPlayer()) : new TemporaryStats(User.getUser(u2.toBukkitPlayer().getUniqueId()));
        ts.cleanStats();
        float[] statsarray = this.calculate(User.getUser(u2.getUuid()));
        return new WrappedStats(statsarray);
    }

    private float[] calculate(User u2) {
        float[] stats = new float[7];
        if (null == u2) {
            return stats;
        }
        PlayerStatistics statistics = PlayerUtils.STATISTICS_CACHE.get(u2.getUuid());
        double pci = 1.0 + (double)this.level * 2.0 * 1.2 / 100.0;
        double fic = (double)this.level * 2.0 * 1.2;
        if (BlessingType.LIFE_BLESSINGS == this.type) {
            double stat = statistics.getMaxHealth().addAll();
            stats[0] = (float)(1.2 * (stat * pci + fic - stat));
            stats[1] = this.level / 100;
        } else if (BlessingType.POWER_BLESSINGS == this.type) {
            double stat_1 = statistics.getStrength().addAll();
            stats[2] = (float)(1.2 * (stat_1 * pci + fic - stat_1));
            double stat_2 = statistics.getCritDamage().addAll() * 100.0;
            stats[3] = (float)(1.2 * (stat_2 * pci + fic - stat_2)) / 100.0f;
        } else if (BlessingType.WISDOM_BLESSINGS == this.type) {
            double stat_1 = statistics.getIntelligence().addAll();
            stats[4] = (float)(1.2 * (stat_1 * pci + fic - stat_1));
            double stat_2 = statistics.getSpeed().addAll() * 100.0;
            stats[5] = (float)(1.2 * (stat_2 * pci + fic - stat_2)) / 100.0f;
        } else if (BlessingType.STONE_BLESSINGS == this.type) {
            double stat_1 = statistics.getDefense().addAll();
            stats[6] = (float)(1.2 * (stat_1 * pci + fic - stat_1));
            double stat_2 = statistics.getStrength().addAll();
            stats[2] = (float)(1.2 * (stat_2 * pci + fic - stat_2));
        } else {
            double stat_1 = statistics.getDefense().addAll();
            stats[6] = (float)(stat_1 + 1.2 * (stat_1 * pci + fic - stat_1));
            double stat_2 = statistics.getStrength().addAll();
            stats[2] = (float)(stat_2 + 1.2 * (stat_2 * pci + fic - stat_2));
            double stat2 = statistics.getMaxHealth().addAll();
            stats[0] = (float)(stat2 + 1.2 * (stat2 * pci + fic - stat2));
            double stat_3 = statistics.getIntelligence().addAll();
            stats[4] = (float)(stat_3 + 1.2 * (stat_3 * pci + fic - stat_3));
        }
        return stats;
    }

    public String toText() {
        String n2 = SUtil.toRomanNumeral(this.level);
        switch (this.type) {
            case LIFE_BLESSINGS: {
                return "Blessing of Life " + n2;
            }
            case POWER_BLESSINGS: {
                return "Blessing of Power " + n2;
            }
            case STONE_BLESSINGS: {
                return "Blessing of Stone " + n2;
            }
            case TIME_BLESSINGS: {
                return "Blessing of Time " + n2;
            }
            case WISDOM_BLESSINGS: {
                return "Blessing of Wisdom " + n2;
            }
        }
        return "Blessing " + n2;
    }

    public static void update() {
        if (BLESSINGS_MAP.isEmpty()) {
            return;
        }
        for (Player p2 : Bukkit.getOnlinePlayers()) {
            World w2 = p2.getWorld();
            if (null == User.getUser(p2.getUniqueId()) || !BLESSINGS_MAP.containsKey(w2)) continue;
            List<Blessings> bls = BLESSINGS_MAP.get(w2);
            TemporaryStats ts = null != TemporaryStats.getFromPlayer(p2) ? TemporaryStats.getFromPlayer(p2) : new TemporaryStats(User.getUser(p2.getUniqueId()));
            float def = 0.0f;
            float spd = 0.0f;
            float intel = 0.0f;
            float cd = 0.0f;
            float str = 0.0f;
            float hpg = 0.0f;
            float hp = 0.0f;
            for (Blessings bl2 : bls) {
                ts.cleanStats();
                float[] statsarray = bl2.calculate(User.getUser(p2.getUniqueId()));
                hp += statsarray[0];
                hpg += statsarray[1];
                str += statsarray[2];
                cd += statsarray[3];
                intel += statsarray[4];
                spd += statsarray[5];
                def += statsarray[6];
                STAT_MAP.put(p2.getUniqueId(), statsarray);
            }
            ts.cleanStats();
            ts.setHealth(Math.max(0.0f, hp));
            ts.setDefense(def);
            ts.setStrength(str);
            ts.setSpeed(spd);
            ts.setCritDamage(cd);
            ts.setIntelligence(intel);
            ts.update();
        }
    }

    public String buildPickupMessage(User targetUser, User picker) {
        Blessings type = this;
        StringBuilder sb = new StringBuilder();
        if (null == picker) {
            sb.append(Sputnik.trans("&6&lDUNGEON BUFF! &fA &d" + type.toText() + "&f have been picked up!\n"));
        } else if (targetUser.getUuid() == picker.getUuid()) {
            sb.append(Sputnik.trans("&6&lDUNGEON BUFF! &fYou found a &d" + type.toText() + "&f!\n"));
        } else {
            sb.append(Sputnik.trans("&6&lDUNGEON BUFF! &b" + picker.toBukkitPlayer().getName() + " &ffound a &d" + type.toText() + "&f!\n"));
        }
        sb.append(Sputnik.trans("   &7Granted you "));
        switch (type.getType()) {
            case LIFE_BLESSINGS: {
                sb.append(Sputnik.trans("&a+" + Sputnik.roundComma(type.getBlessingStats(targetUser).getHealth()) + " HP &7and &a+0% &7health generation."));
                break;
            }
            case POWER_BLESSINGS: {
                sb.append(Sputnik.trans("&a+" + Sputnik.roundComma(type.getBlessingStats(targetUser).getStrength()) + " &c\u2741 Strength &7and &a+" + Sputnik.roundComma(type.getBlessingStats(targetUser).getCritDamage() * 100.0f) + " &9\u2620 Crit Damage&7."));
                break;
            }
            case STONE_BLESSINGS: {
                sb.append(Sputnik.trans("&a" + Sputnik.roundComma(type.getBlessingStats(targetUser).getDefense()) + " \u2748 Defense &7and &a+" + Sputnik.roundComma(type.getBlessingStats(targetUser).getStrength()) + " &c\u2741 Strength&7."));
                break;
            }
            case TIME_BLESSINGS: {
                sb.append(Sputnik.trans("&a+" + Sputnik.roundComma(type.getBlessingStats(targetUser).getHealth()) + " HP&7, &a" + Sputnik.roundComma(type.getBlessingStats(targetUser).getDefense()) + " \u2748 Defense&7, &a" + Sputnik.roundComma(type.getBlessingStats(targetUser).getIntelligence()) + " &b\u270e Intelligence &7and &a+" + Sputnik.roundComma(type.getBlessingStats(targetUser).getStrength()) + " &c\u2741 Strength&7."));
                break;
            }
            case WISDOM_BLESSINGS: {
                sb.append(Sputnik.trans("&a" + Sputnik.roundComma(type.getBlessingStats(targetUser).getIntelligence()) + " &b\u270e Intelligence &7and &a" + Sputnik.roundComma(type.getBlessingStats(targetUser).getSpeed() * 100.0f) + " &f\u2726 Speed&7."));
            }
        }
        return sb.toString();
    }

    public static void openBlessingChest(Block chest, Blessings bless, Player e2) {
        Location loc = chest.getLocation().add(0.5, 0.0, 0.5);
        SEntity sEntity = new SEntity(loc.clone().add(0.0, -1.0, 0.0), SEntityType.VELOCITY_ARMOR_STAND, new Object[0]);
        final ArmorStand drop = (ArmorStand)sEntity.getEntity();
        drop.setVisible(false);
        drop.setCustomNameVisible(false);
        drop.setMetadata("ss_drop", (MetadataValue)new FixedMetadataValue((Plugin)SkyBlock.getPlugin(), (Object)true));
        drop.getEquipment().setHelmet(SUtil.getSkullURLStack("asadas", "e93e2068617872c542ecda1d27df4ece91c699907bf327c4ddb85309412d3939", 1, new String[0]));
        final ArmorStand as2 = Sputnik.spawnStaticDialougeBox((Entity)drop, loc.clone().add(0.0, 1.65, 0.0));
        as2.setCustomName(Sputnik.trans("&d" + bless.toText()));
        as2.setCustomNameVisible(false);
        as2.setMetadata("ss_drop", (MetadataValue)new FixedMetadataValue((Plugin)SkyBlock.getPlugin(), (Object)true));
        drop.setVelocity(new Vector(0.0, 0.058, 0.0));
        SUtil.delay(() -> drop.remove(), 150L);
        SUtil.delay(() -> {
            bless.active();
            User u2 = User.getUser(e2.getUniqueId());
            if (null == u2) {
                return;
            }
            for (Player p2 : loc.getWorld().getPlayers()) {
                if (null == User.getUser(p2.getUniqueId())) continue;
                p2.sendMessage(bless.buildPickupMessage(User.getUser(p2.getUniqueId()), u2));
            }
        }, 20L);
        new BukkitRunnable(){

            public void run() {
                if (drop.isDead()) {
                    as2.remove();
                    this.cancel();
                    return;
                }
                as2.setCustomNameVisible(true);
                Vector velClone = drop.getVelocity().clone();
                drop.setVelocity(new Vector(0.0, 0.0 > velClone.getY() ? 0.045 : -0.045, 0.0));
            }
        }.runTaskTimer((Plugin)SkyBlock.getPlugin(), 20L, 20L);
        new BukkitRunnable(){

            public void run() {
                if (drop.isDead()) {
                    as2.remove();
                    this.cancel();
                    return;
                }
                Location l2 = drop.getLocation();
                l2.setYaw(l2.getYaw() + 5.0f);
                drop.teleport(l2);
            }
        }.runTaskTimer((Plugin)SkyBlock.getPlugin(), 0L, 1L);
        new BukkitRunnable(){

            public void run() {
                if (drop.isDead()) {
                    as2.remove();
                    this.cancel();
                    return;
                }
                drop.getWorld().spigot().playEffect(drop.getLocation().clone().add(0.0, 1.5, 0.0), Effect.CLOUD, 21, 0, 0.3f, 0.0f, 0.3f, 0.01f, 1, 30);
            }
        }.runTaskTimer((Plugin)SkyBlock.getPlugin(), 0L, 2L);
    }

    public static void dropBlessingPickable(final Location loc, final Blessings bless) {
        SEntity sEntity = new SEntity(loc.clone().add(0.0, -0.8, 0.0), SEntityType.VELOCITY_ARMOR_STAND, new Object[0]);
        final ArmorStand drop = (ArmorStand)sEntity.getEntity();
        drop.setVisible(false);
        drop.setCustomNameVisible(false);
        drop.setMetadata("ss_drop", (MetadataValue)new FixedMetadataValue((Plugin)SkyBlock.getPlugin(), (Object)true));
        drop.getEquipment().setHelmet(SUtil.getSkullURLStack("asadas", "e93e2068617872c542ecda1d27df4ece91c699907bf327c4ddb85309412d3939", 1, new String[0]));
        final ArmorStand as2 = Sputnik.spawnStaticDialougeBox((Entity)drop, 2.35);
        as2.setCustomName(Sputnik.trans("&d" + bless.toText()));
        as2.setCustomNameVisible(true);
        as2.setMetadata("ss_drop", (MetadataValue)new FixedMetadataValue((Plugin)SkyBlock.getPlugin(), (Object)true));
        drop.setVelocity(new Vector(0.0, 0.03, 0.0));
        SUtil.delay(() -> {
            if (!drop.isDead()) {
                for (Player p2 : loc.getWorld().getPlayers()) {
                    if (null == User.getUser(p2.getUniqueId())) continue;
                    p2.sendMessage(bless.buildPickupMessage(User.getUser(p2.getUniqueId()), null));
                    bless.active();
                }
                drop.remove();
            }
        }, 2000L);
        new BukkitRunnable(){

            public void run() {
                if (drop.isDead()) {
                    as2.remove();
                    this.cancel();
                    return;
                }
                Vector velClone = drop.getVelocity().clone();
                drop.setVelocity(new Vector(0.0, 0.0 > velClone.getY() ? 0.035 : -0.035, 0.0));
            }
        }.runTaskTimer((Plugin)SkyBlock.getPlugin(), 25L, 25L);
        new BukkitRunnable(){

            public void run() {
                if (drop.isDead()) {
                    as2.remove();
                    this.cancel();
                    return;
                }
                if (!loc.getWorld().getEntities().contains(drop)) {
                    this.cancel();
                    return;
                }
                Location l2 = drop.getLocation();
                l2.setYaw(l2.getYaw() + 2.5f);
                drop.teleport(l2);
                l2.getWorld().spigot().playEffect(drop.getLocation().add(0.0, 1.0, 0.0), Effect.FIREWORKS_SPARK, 0, 1, (float)SUtil.random(-1.0, 1.0), 1.0f, (float)SUtil.random(-1.0, 1.0), 0.0f, 1, 100);
                for (Entity e2 : drop.getNearbyEntities(0.07, 0.07, 0.07)) {
                    User u2;
                    if (!(e2 instanceof Player) || null == (u2 = User.getUser(e2.getUniqueId()))) continue;
                    for (Player p2 : loc.getWorld().getPlayers()) {
                        if (null == User.getUser(p2.getUniqueId())) continue;
                        p2.sendMessage(bless.buildPickupMessage(User.getUser(p2.getUniqueId()), u2));
                    }
                    bless.active();
                    drop.getWorld().playEffect(loc, Effect.LAVA_POP, 0);
                    drop.getWorld().playEffect(loc, Effect.LAVA_POP, 0);
                    drop.getWorld().playSound(drop.getLocation(), Sound.ITEM_PICKUP, 1.0f, 1.0f);
                    drop.remove();
                }
            }
        }.runTaskTimer((Plugin)SkyBlock.getPlugin(), 0L, 1L);
    }

    public BlessingType getType() {
        return this.type;
    }

    public int getLevel() {
        return this.level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}

