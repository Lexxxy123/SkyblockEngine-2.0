package in.godspunky.skyblock.item.pet;

import in.godspunky.skyblock.SkySimEngine;
import in.godspunky.skyblock.item.GenericItemType;
import in.godspunky.skyblock.item.Rarity;
import in.godspunky.skyblock.item.SItem;
import in.godspunky.skyblock.user.User;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import in.godspunky.skyblock.listener.PlayerListener;
import in.godspunky.skyblock.skill.CombatSkill;
import in.godspunky.skyblock.skill.Skill;
import in.godspunky.skyblock.util.EntityManager;
import in.godspunky.skyblock.util.SUtil;
import in.godspunky.skyblock.util.Sputnik;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class ArchivyPet extends Pet {
    public static final Map<Player, Boolean> COOLDOWN;

    @Override
    public List<PetAbility> getPetAbilities(SItem instance) {
        int level = Pet.getLevel(instance);
        BigDecimal flameArrow = new BigDecimal(level * 0.2).setScale(1, RoundingMode.HALF_EVEN);
        BigDecimal headshot = new BigDecimal(level * 0.75).setScale(1, RoundingMode.HALF_EVEN);
        BigDecimal blessed = new BigDecimal(level * 0.5).setScale(1, RoundingMode.HALF_EVEN);
        List<PetAbility> abilities = new ArrayList<PetAbility>(Collections.singletonList(new PetAbility() {
            @Override
            public String getName() {
                return "Flaming Arrow!";
            }

            @Override
            public List<String> getDescription(final SItem instance) {
                return Arrays.asList(Sputnik.trans("&7Grant &e" + flameArrow.toPlainString() + "% &7chance"), Sputnik.trans("&7to convert your &earrow &7into &cFlaming Arrow"), Sputnik.trans("&7that deal your last damage to all mobs"), Sputnik.trans("&7within &a6 &7blocks of the impact."), Sputnik.trans("&85s cooldown."));
            }

            @Override
            public void onDamage(final EntityDamageByEntityEvent e) {
                final double r = SUtil.random(0.0, 1.0);
                final double c = 0.2 * level / 100.0;
                if (r < c && e.getDamager() instanceof Arrow) {
                    ArchivyPet.this.spawnBullet((Player) ((Arrow) e.getDamager()).getShooter());
                }
            }
        }));
        if (instance.getRarity().isAtLeast(Rarity.EPIC)) {
            abilities.add(new PetAbility() {
                @Override
                public String getName() {
                    return "Headshot";
                }

                @Override
                public List<String> getDescription(final SItem instance) {
                    return Arrays.asList(Sputnik.trans("&7Upon shooting &cmob's head &7with an arrow,"), Sputnik.trans("&7the hit you land deal &a+" + headshot.toPlainString() + "%"), Sputnik.trans("more damage. Always &9crits&7."));
                }

                @Override
                public void onDamage(final EntityDamageByEntityEvent e) {
                    if (e.getDamager() instanceof Arrow) {
                        final Arrow arrow = (Arrow) e.getDamager();
                        if (arrow.getShooter() instanceof Player) {
                            final Player player = (Player) arrow.getShooter();
                            final Entity victim = e.getEntity();
                            final double verticalDist = arrow.getLocation().getY() - victim.getLocation().getY();
                            if (verticalDist > 1.4 && verticalDist < 2.0) {
                                User.getUser(player.getUniqueId()).setHeadShot(true);
                            }
                        }
                    }
                }
            });
        }
        if (instance.getRarity().isAtLeast(Rarity.EPIC)) {
            abilities.add(new PetAbility() {
                @Override
                public String getName() {
                    return "Blessed by the God";
                }

                @Override
                public List<String> getDescription(final SItem instance) {
                    return Arrays.asList(Sputnik.trans("&7All of your &branged-attack &7are &a" + blessed.toPlainString() + "%"), Sputnik.trans("stronger."));
                }
            });
        }
        if (instance.getRarity().isAtLeast(Rarity.LEGENDARY)) {
            abilities.add(new PetAbility() {
                @Override
                public String getName() {
                    return "Power of SkySim";
                }

                @Override
                public List<String> getDescription(final SItem instance) {
                    return Collections.singletonList("Immune to any type of knockback.");
                }

                @Override
                public void onHurt(final EntityDamageByEntityEvent e, final Entity damager) {
                    final Entity en = e.getEntity();
                    final Vector v = new Vector(0, 0, 0);
                    SUtil.delay(() -> en.setVelocity(v), 0L);
                }
            });
        }
        return abilities;
    }

    @Override
    public Skill getSkill() {
        return CombatSkill.INSTANCE;
    }

    @Override
    public String getURL() {
        return "c316eafa5a831b6a4b9de43b00649042f4fa8f0ae6265ac2515ad1dbdc151753";
    }

    @Override
    public String getDisplayName() {
        return "Archivy";
    }

    @Override
    public GenericItemType getType() {
        return GenericItemType.PET;
    }

    @Override
    public double getPerDefense() {
        return 1.0;
    }

    @Override
    public double getPerCritDamage() {
        return 0.005;
    }

    @Override
    public double getPerStrength() {
        return 1.0;
    }

    @Override
    public double getPerCritChance() {
        return 0.001;
    }

    @Override
    public Rarity getRarity() {
        return Rarity.LEGENDARY;
    }

    @Override
    public void particleBelowA(final Player p, final Location l) {
        p.spigot().playEffect(l, Effect.HAPPY_VILLAGER, 21, 0, 0.1f, 0.0f, 0.1f, 0.01f, 1, 30);
    }

    public void spawnBullet(final Player player) {
        if (ArchivyPet.COOLDOWN.containsKey(player) && ArchivyPet.COOLDOWN.get(player)) {
            return;
        }
        player.getWorld().playSound(player.getLocation(), Sound.GHAST_FIREBALL, 1.0f, 1.0f);
        final Location location = player.getLocation().add(0.0, -0.7, 0.0);
        final ArmorStand stand = (ArmorStand) player.getWorld().spawn(location.add(player.getLocation().getDirection().multiply(2)), (Class) ArmorStand.class);
        stand.setVisible(false);
        stand.setMarker(true);
        stand.setGravity(false);
        ArchivyPet.COOLDOWN.put(player, true);
        SUtil.delay(() -> {
            ArchivyPet.COOLDOWN.put(player, false);
            player.sendMessage(Sputnik.trans("&6Fire Arrow &ais now available."));
        }, 100L);
        double baseDMG = 100.0;
        if (PlayerListener.LAST_DAMAGE_DEALT.containsKey(player)) {
            baseDMG = PlayerListener.LAST_DAMAGE_DEALT.get(player);
        }
        final double base = baseDMG;
        new BukkitRunnable() {
            int c = 0;

            public void run() {
                if (stand.isDead()) {
                    stand.getWorld().playEffect(stand.getLocation(), Effect.EXPLOSION, 0);
                    this.cancel();
                    return;
                }
                if (stand.getLocation().add(0.0, 2.0, 0.0).getBlock().getType() != Material.AIR) {
                    stand.remove();
                    ArchivyPet.this.playParticleAndSound(stand);
                    ArchivyPet.this.dmgEntityInRange(stand, player, base);
                    this.cancel();
                    return;
                }
                if (ArchivyPet.scanEntityNear(stand, 1, 1, 1).size() > 0) {
                    stand.remove();
                    ArchivyPet.this.playParticleAndSound(stand);
                    ArchivyPet.this.dmgEntityInRange(stand, player, base);
                    this.cancel();
                    return;
                }
                if (this.c >= 50) {
                    stand.remove();
                    stand.getWorld().playEffect(stand.getLocation(), Effect.EXPLOSION, 0);
                    this.cancel();
                    return;
                }
                ++this.c;
                stand.getWorld().spigot().playEffect(stand.getLocation().add(0.0, 2.0, 0.0), Effect.FLAME, 21, 0, 0.1f, 0.0f, 0.1f, 0.01f, 1, 30);
                stand.getWorld().spigot().playEffect(stand.getLocation().add(0.0, 2.0, 0.0), Effect.FLAME, 21, 0, 0.1f, 0.0f, 0.1f, 0.01f, 1, 30);
                stand.getWorld().spigot().playEffect(stand.getLocation().add(0.0, 2.0, 0.0), Effect.FLAME, 21, 0, 0.1f, 0.0f, 0.1f, 0.01f, 1, 30);
                stand.getWorld().spigot().playEffect(stand.getLocation().add(0.0, 2.0, 0.0), Effect.FLAME, 21, 0, 0.1f, 0.0f, 0.1f, 0.01f, 1, 30);
                stand.getWorld().spigot().playEffect(stand.getLocation().add(0.0, 2.0, 0.0), Effect.FLAME, 21, 0, 0.1f, 0.0f, 0.1f, 0.01f, 1, 30);
                stand.getWorld().spigot().playEffect(stand.getLocation().add(0.0, 2.0, 0.0), Effect.FLAME, 21, 0, 0.1f, 0.0f, 0.1f, 0.01f, 1, 30);
                stand.getWorld().spigot().playEffect(stand.getLocation().add(0.0, 2.0, 0.0), Effect.FLAME, 21, 0, 0.1f, 0.0f, 0.1f, 0.01f, 1, 30);
                stand.getWorld().spigot().playEffect(stand.getLocation().add(0.0, 2.0, 0.0), Effect.FLAME, 21, 0, 0.1f, 0.0f, 0.1f, 0.01f, 1, 30);
                stand.getWorld().spigot().playEffect(stand.getLocation().add(0.0, 2.0, 0.0), Effect.FLAME, 21, 0, 0.1f, 0.0f, 0.1f, 0.01f, 1, 30);
                stand.getWorld().spigot().playEffect(stand.getLocation().add(0.0, 2.0, 0.0), Effect.FLAME, 21, 0, 0.1f, 0.0f, 0.1f, 0.01f, 1, 30);
                stand.getWorld().spigot().playEffect(stand.getLocation().add(0.0, 2.0, 0.0), Effect.FLAME, 21, 0, 0.1f, 0.0f, 0.1f, 0.01f, 1, 30);
                stand.getWorld().spigot().playEffect(stand.getLocation().add(0.0, 2.0, 0.0), Effect.FLAME, 21, 0, 0.1f, 0.0f, 0.1f, 0.01f, 1, 30);
                stand.teleport(stand.getLocation().add(stand.getLocation().getDirection().normalize().multiply(1)));
            }
        }.runTaskTimer(SkySimEngine.getPlugin(), 0L, 1L);
    }

    public void dmgEntityInRange(final Entity e, final Player owner, double damage) {
        final List<Entity> le = new ArrayList<Entity>();
        int td = 0;
        for (final Entity entity1 : e.getNearbyEntities(6.0, 6.0, 6.0)) {
            if (entity1.isDead()) {
                continue;
            }
            if (!(entity1 instanceof LivingEntity)) {
                continue;
            }
            if (entity1 instanceof Player || entity1 instanceof EnderDragonPart || entity1 instanceof Villager || entity1 instanceof ArmorStand || entity1 instanceof Item) {
                continue;
            }
            if (entity1 instanceof ItemFrame) {
                continue;
            }
            if (entity1.hasMetadata("GiantSword")) {
                continue;
            }
            if (EntityManager.DEFENSE_PERCENTAGE.containsKey(entity1)) {
                int defensepercent = EntityManager.DEFENSE_PERCENTAGE.get(entity1);
                if (defensepercent > 100) {
                    defensepercent = 100;
                }
                damage -= damage * defensepercent / 100.0;
            }
            User.getUser(owner.getUniqueId()).damageEntity((Damageable) entity1, damage);
            PlayerListener.customDMGIND(entity1, Sputnik.trans("&4" + Math.round(damage) + "✺"));
            le.add(entity1);
            td += (int) damage;
        }
        if (le.size() > 0) {
            owner.playSound(owner.getLocation(), Sound.SUCCESSFUL_HIT, 1.0f, 0.0f);
            if (le.size() > 1) {
                owner.sendMessage(Sputnik.trans("&7Your Fire Arrow hit &c" + le.size() + " &7enemies for &c" + SUtil.commaify(Math.round((float) td)) + " &7damage."));
            } else {
                owner.sendMessage(Sputnik.trans("&7Your Fire Arrow hit &c" + le.size() + " &7enemy for &c" + SUtil.commaify(Math.round((float) td)) + " &7damage."));
            }
        }
    }

    public static List<Entity> scanEntityNear(final Entity e, final int arg0, final int arg1, final int arg2) {
        final List<Entity> re = new ArrayList<Entity>();
        for (final Entity entity1 : e.getNearbyEntities(arg0, arg1, arg2)) {
            if (entity1.isDead()) {
                continue;
            }
            if (!(entity1 instanceof LivingEntity)) {
                continue;
            }
            if (entity1 instanceof Player || entity1 instanceof EnderDragonPart || entity1 instanceof Villager || entity1 instanceof ArmorStand || entity1 instanceof Item) {
                continue;
            }
            if (entity1 instanceof ItemFrame) {
                continue;
            }
            if (entity1.hasMetadata("GiantSword")) {
                continue;
            }
            if (entity1.hasMetadata("NoAffect")) {
                continue;
            }
            re.add(entity1);
        }
        return re;
    }

    public void playParticleAndSound(final Entity e) {
        e.getLocation().getWorld().playSound(e.getLocation(), Sound.GHAST_FIREBALL, 1.0f, 1.3f);
        e.getWorld().playEffect(e.getLocation(), Effect.EXPLOSION_HUGE, 0);
    }

    static {
        COOLDOWN = new HashMap<Player, Boolean>();
    }
}
