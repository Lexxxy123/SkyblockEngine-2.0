package in.godspunky.skyblock.dungeons;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import in.godspunky.skyblock.SkySimEngine;
import in.godspunky.skyblock.entity.SEntity;
import in.godspunky.skyblock.entity.SEntityType;
import in.godspunky.skyblock.user.PlayerStatistics;
import in.godspunky.skyblock.user.PlayerUtils;
import in.godspunky.skyblock.user.TemporaryStats;
import in.godspunky.skyblock.user.User;
import in.godspunky.skyblock.util.SUtil;
import in.godspunky.skyblock.util.Sputnik;

import java.util.*;

public class Blessings {
    private final BlessingType type;
    private int level;
    private double buffPercent;
    private final World world;
    public static final SkySimEngine sse;
    public static final Map<World, List<Blessings>> BLESSINGS_MAP;
    public static final Map<UUID, float[]> STAT_MAP;

    public Blessings(final BlessingType type, final int level, final World operatedWorld) {
        this.level = 1;
        this.type = type;
        this.world = operatedWorld;
        this.level = level;
    }

    public void active() {
        final World operatedWorld = this.world;
        if (Blessings.BLESSINGS_MAP.containsKey(operatedWorld)) {
            for (final Blessings bs : Blessings.BLESSINGS_MAP.get(operatedWorld)) {
                if (bs.getType() == this.type) {
                    bs.setLevel(bs.getLevel() + this.level);
                    return;
                }
            }
            Blessings.BLESSINGS_MAP.get(operatedWorld).add(this);
        } else {
            Blessings.BLESSINGS_MAP.put(operatedWorld, new ArrayList<Blessings>());
            Blessings.BLESSINGS_MAP.get(operatedWorld).add(this);
        }
    }

    public static List<Blessings> getFrom(final World w) {
        if (Blessings.BLESSINGS_MAP.containsKey(w)) {
            return Blessings.BLESSINGS_MAP.get(w);
        }
        return null;
    }

    public void remove() {
        Blessings.BLESSINGS_MAP.get(this.world).remove(this);
        for (final Player p : this.world.getPlayers()) {
            TemporaryStats ts = null;
            final User u = User.getUser(p.getUniqueId());
            if (u == null) {
                continue;
            }
            if (TemporaryStats.getFromPlayer(u.toBukkitPlayer()) != null) {
                ts = TemporaryStats.getFromPlayer(u.toBukkitPlayer());
            } else {
                ts = new TemporaryStats(User.getUser(u.toBukkitPlayer().getUniqueId()));
            }
            ts.cleanStats();
        }
    }

    public static void resetForWorld(final World w) {
        Blessings.BLESSINGS_MAP.remove(w);
        for (final Player p : w.getPlayers()) {
            TemporaryStats ts = null;
            final User u = User.getUser(p.getUniqueId());
            if (u == null) {
                continue;
            }
            if (TemporaryStats.getFromPlayer(u.toBukkitPlayer()) != null) {
                ts = TemporaryStats.getFromPlayer(u.toBukkitPlayer());
            } else {
                ts = new TemporaryStats(User.getUser(u.toBukkitPlayer().getUniqueId()));
            }
            ts.cleanStats();
        }
    }

    public WrappedStats getBlessingStats(final User u) {
        TemporaryStats ts = null;
        if (TemporaryStats.getFromPlayer(u.toBukkitPlayer()) != null) {
            ts = TemporaryStats.getFromPlayer(u.toBukkitPlayer());
        } else {
            ts = new TemporaryStats(User.getUser(u.toBukkitPlayer().getUniqueId()));
        }
        ts.cleanStats();
        final float[] statsarray = this.calculate(User.getUser(u.getUuid()));
        return new WrappedStats(statsarray);
    }

    private float[] calculate(final User u) {
        final float[] stats = new float[7];
        if (u == null) {
            return stats;
        }
        final PlayerStatistics statistics = PlayerUtils.STATISTICS_CACHE.get(u.getUuid());
        final double pci = 1.0 + this.level * 2.0 * 1.2 / 100.0;
        final double fic = this.level * 2.0 * 1.2;
        if (this.type == BlessingType.LIFE_BLESSINGS) {
            final double stat = statistics.getMaxHealth().addAll();
            stats[0] = (float) (1.2 * (stat * pci + fic - stat));
            stats[1] = (float) (this.level / 100);
        } else if (this.type == BlessingType.POWER_BLESSINGS) {
            final double stat_1 = statistics.getStrength().addAll();
            stats[2] = (float) (1.2 * (stat_1 * pci + fic - stat_1));
            final double stat_2 = statistics.getCritDamage().addAll() * 100.0;
            stats[3] = (float) (1.2 * (stat_2 * pci + fic - stat_2)) / 100.0f;
        } else if (this.type == BlessingType.WISDOM_BLESSINGS) {
            final double stat_1 = statistics.getIntelligence().addAll();
            stats[4] = (float) (1.2 * (stat_1 * pci + fic - stat_1));
            final double stat_2 = statistics.getSpeed().addAll() * 100.0;
            stats[5] = (float) (1.2 * (stat_2 * pci + fic - stat_2)) / 100.0f;
        } else if (this.type == BlessingType.STONE_BLESSINGS) {
            final double stat_1 = statistics.getDefense().addAll();
            stats[6] = (float) (1.2 * (stat_1 * pci + fic - stat_1));
            final double stat_2 = statistics.getStrength().addAll();
            stats[2] = (float) (1.2 * (stat_2 * pci + fic - stat_2));
        } else {
            final double stat_1 = statistics.getDefense().addAll();
            stats[6] = (float) (stat_1 + 1.2 * (stat_1 * pci + fic - stat_1));
            final double stat_2 = statistics.getStrength().addAll();
            stats[2] = (float) (stat_2 + 1.2 * (stat_2 * pci + fic - stat_2));
            final double stat2 = statistics.getMaxHealth().addAll();
            stats[0] = (float) (stat2 + 1.2 * (stat2 * pci + fic - stat2));
            final double stat_3 = statistics.getIntelligence().addAll();
            stats[4] = (float) (stat_3 + 1.2 * (stat_3 * pci + fic - stat_3));
        }
        return stats;
    }

    public String toText() {
        final String n = SUtil.toRomanNumeral(this.level);
        switch (this.type) {
            case LIFE_BLESSINGS:
                return "Blessing of Life " + n;
            case POWER_BLESSINGS:
                return "Blessing of Power " + n;
            case STONE_BLESSINGS:
                return "Blessing of Stone " + n;
            case TIME_BLESSINGS:
                return "Blessing of Time " + n;
            case WISDOM_BLESSINGS:
                return "Blessing of Wisdom " + n;
            default:
                return "Blessing " + n;
        }
    }

    public static void update() {
        sse.async(() -> {
            for (Player p : Bukkit.getOnlinePlayers()) {
                World w = p.getWorld();
                if (User.getUser(p.getUniqueId()) == null || !BLESSINGS_MAP.containsKey(w)) continue;
                List<Blessings> bls = BLESSINGS_MAP.get(w);
                TemporaryStats ts = null;
                ts = TemporaryStats.getFromPlayer(p) != null ? TemporaryStats.getFromPlayer(p) : new TemporaryStats(User.getUser(p.getUniqueId()));
                float def = 0.0f;
                float spd = 0.0f;
                float intel = 0.0f;
                float cd = 0.0f;
                float str = 0.0f;
                float hpg = 0.0f;
                float hp = 0.0f;
                for (Blessings bl : bls) {
                    ts.cleanStats();
                    float[] statsarray = bl.calculate(User.getUser(p.getUniqueId()));
                    hp += statsarray[0];
                    hpg += statsarray[1];
                    str += statsarray[2];
                    cd += statsarray[3];
                    intel += statsarray[4];
                    spd += statsarray[5];
                    def += statsarray[6];
                    STAT_MAP.put(p.getUniqueId(), statsarray);
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
        });
    }

    public String buildPickupMessage(final User targetUser, final User picker) {
        final Blessings type = this;
        final StringBuilder sb = new StringBuilder();
        if (picker == null) {
            sb.append(Sputnik.trans("&6&lDUNGEON BUFF! &fA &d" + type.toText() + "&f have been picked up!\n"));
        } else if (targetUser.getUuid() == picker.getUuid()) {
            sb.append(Sputnik.trans("&6&lDUNGEON BUFF! &fYou found a &d" + type.toText() + "&f!\n"));
        } else {
            sb.append(Sputnik.trans("&6&lDUNGEON BUFF! &b" + picker.toBukkitPlayer().getName() + " &ffound a &d" + type.toText() + "&f!\n"));
        }
        sb.append(Sputnik.trans("   &7Granted you "));
        switch (type.getType()) {
            case LIFE_BLESSINGS:
                sb.append(Sputnik.trans("&a+" + Sputnik.roundComma(type.getBlessingStats(targetUser).getHealth()) + " HP &7and &a+0% &7health generation."));
                break;
            case POWER_BLESSINGS:
                sb.append(Sputnik.trans("&a+" + Sputnik.roundComma(type.getBlessingStats(targetUser).getStrength()) + " &c❁ Strength &7and &a+" + Sputnik.roundComma(type.getBlessingStats(targetUser).getCritDamage() * 100.0f) + " &9☠ Crit Damage&7."));
                break;
            case STONE_BLESSINGS:
                sb.append(Sputnik.trans("&a" + Sputnik.roundComma(type.getBlessingStats(targetUser).getDefense()) + " ❈ Defense &7and &a+" + Sputnik.roundComma(type.getBlessingStats(targetUser).getStrength()) + " &c❁ Strength&7."));
                break;
            case TIME_BLESSINGS:
                sb.append(Sputnik.trans("&a+" + Sputnik.roundComma(type.getBlessingStats(targetUser).getHealth()) + " HP&7, &a" + Sputnik.roundComma(type.getBlessingStats(targetUser).getDefense()) + " ❈ Defense&7, &a" + Sputnik.roundComma(type.getBlessingStats(targetUser).getIntelligence()) + " &b✎ Intelligence &7and &a+" + Sputnik.roundComma(type.getBlessingStats(targetUser).getStrength()) + " &c❁ Strength&7."));
                break;
            case WISDOM_BLESSINGS:
                sb.append(Sputnik.trans("&a" + Sputnik.roundComma(type.getBlessingStats(targetUser).getIntelligence()) + " &b✎ Intelligence &7and &a" + Sputnik.roundComma(type.getBlessingStats(targetUser).getSpeed() * 100.0f) + " &f✦ Speed&7."));
                break;
        }
        return sb.toString();
    }

    public static void openBlessingChest(final Block chest, final Blessings bless, final Player e) {
        final Location loc = chest.getLocation().add(0.5, 0.0, 0.5);
        final SEntity sEntity = new SEntity(loc.clone().add(0.0, -1.0, 0.0), SEntityType.VELOCITY_ARMOR_STAND);
        final ArmorStand drop = (ArmorStand) sEntity.getEntity();
        drop.setVisible(false);
        drop.setCustomNameVisible(false);
        drop.setMetadata("ss_drop", new FixedMetadataValue(SkySimEngine.getPlugin(), true));
        drop.getEquipment().setHelmet(SUtil.getSkullURLStack("asadas", "e93e2068617872c542ecda1d27df4ece91c699907bf327c4ddb85309412d3939", 1));
        final ArmorStand as = Sputnik.spawnStaticDialougeBox(drop, loc.clone().add(0.0, 1.65, 0.0));
        as.setCustomName(Sputnik.trans("&d" + bless.toText()));
        as.setCustomNameVisible(false);
        as.setMetadata("ss_drop", new FixedMetadataValue(SkySimEngine.getPlugin(), true));
        drop.setVelocity(new Vector(0.0, 0.058, 0.0));
        SUtil.delay(() -> drop.remove(), 150L);
        SUtil.delay(() -> {
            bless.active();
            final User u = User.getUser(e.getUniqueId());
            if (u == null) {
                return;
            }
            for (Player p : loc.getWorld().getPlayers()) {
                if (User.getUser(p.getUniqueId()) == null) continue;
                p.sendMessage(bless.buildPickupMessage(User.getUser(p.getUniqueId()), u));
            }
        }, 20L);
        new BukkitRunnable() {
            public void run() {
                if (drop.isDead()) {
                    as.remove();
                    this.cancel();
                    return;
                }
                as.setCustomNameVisible(true);
                final Vector velClone = drop.getVelocity().clone();
                drop.setVelocity(new Vector(0.0, (velClone.getY() < 0.0) ? 0.045 : -0.045, 0.0));
            }
        }.runTaskTimer(SkySimEngine.getPlugin(), 20L, 20L);
        new BukkitRunnable() {
            public void run() {
                if (drop.isDead()) {
                    as.remove();
                    this.cancel();
                    return;
                }
                final Location l = drop.getLocation();
                l.setYaw(l.getYaw() + 5.0f);
                drop.teleport(l);
            }
        }.runTaskTimer(SkySimEngine.getPlugin(), 0L, 1L);
        new BukkitRunnable() {
            public void run() {
                if (drop.isDead()) {
                    as.remove();
                    this.cancel();
                    return;
                }
                drop.getWorld().spigot().playEffect(drop.getLocation().clone().add(0.0, 1.5, 0.0), Effect.CLOUD, 21, 0, 0.3f, 0.0f, 0.3f, 0.01f, 1, 30);
            }
        }.runTaskTimer(SkySimEngine.getPlugin(), 0L, 2L);
    }

    public static void dropBlessingPickable(final Location loc, final Blessings bless) {
        final SEntity sEntity = new SEntity(loc.clone().add(0.0, -0.8, 0.0), SEntityType.VELOCITY_ARMOR_STAND);
        final ArmorStand drop = (ArmorStand) sEntity.getEntity();
        drop.setVisible(false);
        drop.setCustomNameVisible(false);
        drop.setMetadata("ss_drop", new FixedMetadataValue(SkySimEngine.getPlugin(), true));
        drop.getEquipment().setHelmet(SUtil.getSkullURLStack("asadas", "e93e2068617872c542ecda1d27df4ece91c699907bf327c4ddb85309412d3939", 1));
        final ArmorStand as = Sputnik.spawnStaticDialougeBox(drop, 2.35);
        as.setCustomName(Sputnik.trans("&d" + bless.toText()));
        as.setCustomNameVisible(true);
        as.setMetadata("ss_drop", new FixedMetadataValue(SkySimEngine.getPlugin(), true));
        drop.setVelocity(new Vector(0.0, 0.03, 0.0));
        SUtil.delay(() -> {
            if (!drop.isDead()) {
                for (Player p : loc.getWorld().getPlayers()) {
                    if (User.getUser(p.getUniqueId()) == null) continue;
                    p.sendMessage(bless.buildPickupMessage(User.getUser(p.getUniqueId()), null));
                    bless.active();
                }
                drop.remove();
            }
        }, 2000L);
        new BukkitRunnable() {
            public void run() {
                if (drop.isDead()) {
                    as.remove();
                    this.cancel();
                    return;
                }
                final Vector velClone = drop.getVelocity().clone();
                drop.setVelocity(new Vector(0.0, (velClone.getY() < 0.0) ? 0.035 : -0.035, 0.0));
            }
        }.runTaskTimer(SkySimEngine.getPlugin(), 25L, 25L);
        new BukkitRunnable() {
            public void run() {
                if (drop.isDead()) {
                    as.remove();
                    this.cancel();
                    return;
                }
                if (!loc.getWorld().getEntities().contains(drop)) {
                    this.cancel();
                    return;
                }
                final Location l = drop.getLocation();
                l.setYaw(l.getYaw() + 2.5f);
                drop.teleport(l);
                l.getWorld().spigot().playEffect(drop.getLocation().add(0.0, 1.0, 0.0), Effect.FIREWORKS_SPARK, 0, 1, (float) SUtil.random(-1.0, 1.0), 1.0f, (float) SUtil.random(-1.0, 1.0), 0.0f, 1, 100);
                for (final Entity e : drop.getNearbyEntities(0.07, 0.07, 0.07)) {
                    if (e instanceof Player) {
                        final User u = User.getUser(e.getUniqueId());
                        if (u == null) {
                            continue;
                        }
                        for (final Player p : loc.getWorld().getPlayers()) {
                            if (User.getUser(p.getUniqueId()) == null) {
                                continue;
                            }
                            p.sendMessage(bless.buildPickupMessage(User.getUser(p.getUniqueId()), u));
                        }
                        bless.active();
                        drop.getWorld().playEffect(loc, Effect.LAVA_POP, 0);
                        drop.getWorld().playEffect(loc, Effect.LAVA_POP, 0);
                        drop.getWorld().playSound(drop.getLocation(), Sound.ITEM_PICKUP, 1.0f, 1.0f);
                        drop.remove();
                    }
                }
            }
        }.runTaskTimer(SkySimEngine.getPlugin(), 0L, 1L);
    }

    public BlessingType getType() {
        return this.type;
    }

    public int getLevel() {
        return this.level;
    }

    public void setLevel(final int level) {
        this.level = level;
    }

    static {
        sse = SkySimEngine.getPlugin();
        BLESSINGS_MAP = new HashMap<World, List<Blessings>>();
        STAT_MAP = new HashMap<UUID, float[]>();
    }
}
