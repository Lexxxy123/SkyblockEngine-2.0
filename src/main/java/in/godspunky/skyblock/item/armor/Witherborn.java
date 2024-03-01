package in.godspunky.skyblock.item.armor;

import in.godspunky.skyblock.SkyBlock;
import in.godspunky.skyblock.features.skill.Skill;
import in.godspunky.skyblock.util.EntityManager;
import in.godspunky.skyblock.util.Groups;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import in.godspunky.skyblock.item.SItem;
import in.godspunky.skyblock.listener.PlayerListener;
import in.godspunky.skyblock.user.User;
import in.godspunky.skyblock.util.SUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Witherborn {
    private final Player p;
    private final int size;
    private Wither w;
    private boolean isTargetting;
    @Setter
    @Getter
    public Entity withersTarget;
    public static final Map<UUID, Witherborn> WITHER_MAP = new HashMap<>();
    public static final Map<UUID, Boolean> WITHER_COOLDOWN = new HashMap<>();

    public Witherborn(final Player p) {
        this.size = 790;
        this.isTargetting = false;
        this.withersTarget = null;
        this.p = p;
        this.isTargetting = false;
        WITHER_MAP.put(p.getUniqueId(), this);
    }

    public boolean checkCondition() {
        final SItem helm = SItem.find(this.p.getInventory().getHelmet());
        final SItem chest = SItem.find(this.p.getInventory().getChestplate());
        final SItem leg = SItem.find(this.p.getInventory().getLeggings());
        final SItem boots = SItem.find(this.p.getInventory().getBoots());
        if (helm == null || chest == null || leg == null || boots == null) {
            WITHER_MAP.remove(this.p.getUniqueId());
            this.w.remove();
            return false;
        }
        if (Groups.WITHER_HELMETS.contains(helm.getType()) && Groups.WITHER_CHESTPLATES.contains(chest.getType()) && Groups.WITHER_LEGGINGS.contains(leg.getType()) && Groups.WITHER_BOOTS.contains(boots.getType())) {
            return true;
        }
        WITHER_MAP.remove(this.p.getUniqueId());
        this.w.remove();
        return false;
    }

    public void spawnWither() {
        final Wither w = (Wither) this.p.getWorld().spawn(this.p.getLocation(), (Class) Wither.class);
        EntityManager.noAI(w);
        EntityManager.setNBTTag(w, "Invul", this.size);
        EntityManager.noHit(w);
        EntityManager.shutTheFuckUp(w);
        w.setMetadata("GiantSword", new FixedMetadataValue(SkyBlock.getPlugin(), 0));
        w.setMetadata("NoAffect", new FixedMetadataValue(SkyBlock.getPlugin(), 0));
        w.setMetadata("Ire", new FixedMetadataValue(SkyBlock.getPlugin(), 0));
        this.w = w;
        new BukkitRunnable() {
            public void run() {
                if (!p.isOnline() || w.isDead() || getWitherbornInstance(p) == null) {
                    this.cancel();
                    return;
                }
                if (withersTarget == null) {
                    final List<Entity> er = w.getNearbyEntities(10.0, 10.0, 10.0);
                    er.removeIf(en -> en.hasMetadata("GiantSword") || en.hasMetadata("NPC"));
                    er.removeIf(en -> !(en instanceof LivingEntity));
                    er.removeIf(en -> en instanceof Player);
                    er.removeIf(en -> en instanceof ArmorStand);
                    er.removeIf(en -> en instanceof Villager);
                    er.removeIf(Entity::isDead);
                    if (!er.isEmpty()) {
                        final LivingEntity le = (LivingEntity) er.get(SUtil.random(0, er.size() - 1));
                        setWithersTarget(le);
                    }
                }
            }
        }.runTaskTimer(SkyBlock.getPlugin(), 100L, 100L);
        new BukkitRunnable() {
            float cout = p.getLocation().getYaw();

            public void run() {
                if (!p.isOnline() || w.isDead() || getWitherbornInstance(p) == null) {
                    w.remove();
                    p.playSound(p.getLocation(), Sound.WITHER_DEATH, 0.5f, 1.0f);
                    this.cancel();
                    return;
                }
                checkCondition();
                if (withersTarget != null) {
                    if (withersTarget instanceof LivingEntity) {
                        if (!isTargetting) {
                            final LivingEntity r1 = (LivingEntity) withersTarget;
                            selfSacrificeHeroAction(w, r1);
                            isTargetting = true;
                        }
                    } else {
                        withersTarget = null;
                    }
                }
                final Location loc = p.getLocation();
                loc.setYaw(this.cout);
                loc.setPitch(0.0f);
                loc.add(loc.getDirection().normalize().multiply(1.45));
                if (withersTarget == null && !isTargetting) {
                    w.teleport(loc);
                }
                this.cout += 7.0f;
            }
        }.runTaskTimer(SkyBlock.getPlugin(), 2L, 2L);
    }

    public void selfSacrificeHeroAction(final Wither w, final Entity e) {
        new BukkitRunnable() {
            public void run() {
                if (!p.isOnline() || w.isDead()) {
                    w.remove();
                    w.getWorld().playEffect(w.getLocation(), Effect.EXPLOSION_LARGE, size);
                    w.getWorld().playEffect(w.getLocation(), Effect.EXPLOSION_LARGE, size);
                    w.getWorld().playSound(w.getLocation(), Sound.EXPLODE, 1.0f, 1.0f);
                    w.getWorld().playSound(w.getLocation(), Sound.WITHER_DEATH, 0.3f, 1.0f);
                    this.cancel();
                    return;
                }
                if (e.isDead()) {
                    isTargetting = false;
                    withersTarget = null;
                    this.cancel();
                    return;
                }
                if (w.getNearbyEntities(0.2, 0.2, 0.2).contains(e)) {
                    WITHER_COOLDOWN.put(p.getUniqueId(), true);
                    SUtil.delay(() -> {
                        if (checkCondition()) {
                            WITHER_MAP.remove(p.getUniqueId());
                            final Witherborn a = new Witherborn(p);
                            a.spawnWither();
                        }
                        WITHER_COOLDOWN.put(p.getUniqueId(), false);
                    }, 600L);
                    w.remove();
                    int j = 0;
                    double d = 0.0;
                    for (final Entity entity : w.getNearbyEntities(3.0, 3.0, 3.0)) {
                        if (entity.isDead()) {
                            continue;
                        }
                        if (!(entity instanceof LivingEntity)) {
                            continue;
                        }
                        if (entity.hasMetadata("GiantSword")) {
                            continue;
                        }
                        if (entity.hasMetadata("NoAffect")) {
                            continue;
                        }
                        if (entity instanceof Player || entity instanceof EnderDragonPart || entity instanceof Villager) {
                            continue;
                        }
                        if (entity instanceof ArmorStand) {
                            continue;
                        }
                        ++j;
                        final double damage = 1000000.0;
                        double find = 0.0;
                        final int combatLevel = Skill.getLevel(User.getUser(p.getUniqueId()).getCombatXP(), false);
                        final double damageMultiplier = 1.0 + combatLevel * 0.04;
                        find = damage * damageMultiplier;
                        if (EntityManager.DEFENSE_PERCENTAGE.containsKey(entity)) {
                            int defensepercent = EntityManager.DEFENSE_PERCENTAGE.get(entity);
                            if (defensepercent > 100) {
                                defensepercent = 100;
                            }
                            find -= find * defensepercent / 100.0;
                        }
                        PlayerListener.spawnDamageInd(entity, find, false);
                        User.getUser(p.getUniqueId()).damageEntityIgnoreShield((Damageable) entity, find);
                        d += find;
                    }
                    d = new BigDecimal(d).setScale(1, RoundingMode.HALF_EVEN).doubleValue();
                    if (j > 0) {
                        if (j == 1) {
                            p.sendMessage(ChatColor.GRAY + "Your Witherborn hit " + ChatColor.RED + j + ChatColor.GRAY + " enemy for " + ChatColor.RED + SUtil.commaify(d) + ChatColor.GRAY + " damage.");
                        } else {
                            p.sendMessage(ChatColor.GRAY + "Your Witherborn hit " + ChatColor.RED + j + ChatColor.GRAY + " enemies for " + ChatColor.RED + SUtil.commaify(d) + ChatColor.GRAY + " damage.");
                        }
                    }
                }
                withersTarget = e;
                final Location r = w.getLocation().setDirection(e.getLocation().toVector().subtract(w.getLocation().toVector()));
                w.teleport(r);
                w.teleport(w.getLocation().add(w.getLocation().getDirection().normalize().multiply(0.3)));
            }
        }.runTaskTimer(SkyBlock.getPlugin(), 2L, 1L);
    }

    public static Witherborn getWitherbornInstance(final Player p) {
        return WITHER_MAP.get(p.getUniqueId());
    }

}
