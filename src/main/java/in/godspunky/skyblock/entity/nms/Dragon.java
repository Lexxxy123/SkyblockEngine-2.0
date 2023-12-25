package in.godspunky.skyblock.entity.nms;

import in.godspunky.skyblock.Skyblock;
import in.godspunky.skyblock.entity.*;
import in.godspunky.skyblock.entity.dungeons.watcher.GlobalBossBar;
import net.minecraft.server.v1_8_R3.EntityEnderDragon;
import net.minecraft.server.v1_8_R3.World;
import org.apache.commons.lang3.Range;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.*;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import in.godspunky.skyblock.item.SItem;
import in.godspunky.skyblock.item.SMaterial;
import in.godspunky.skyblock.user.User;
import in.godspunky.skyblock.util.SUtil;

import java.util.*;

public abstract class Dragon extends EntityEnderDragon implements SNMSEntity, EntityFunction, EntityStatistics {
    public static final long DEFAULT_ATTACK_COOLDOWN = 300L;
    public static final Range DEFAULT_DAMAGE_DEGREE_RANGE;
    public static final double DEFAULT_SPEED_MODIFIER = 1.4;
    private boolean frozen;
    private double yD;
    private double speedModifier;
    private Range<Double> damageDegree;
    private final long attackCooldown;

    protected Dragon(final World world, final double speedModifier, final Range<Double> damageDegree, final long attackCooldown) {
        super(world);
        this.frozen = false;
        this.yD = 1.0;
        this.speedModifier = speedModifier;
        this.damageDegree = damageDegree;
        this.attackCooldown = attackCooldown;
    }

    protected Dragon(final double speedModifier, final Range<Double> damageDegree, final long attackCooldown) {
        this(((CraftWorld) Bukkit.getWorlds().get(0)).getHandle(), speedModifier, damageDegree, attackCooldown);
    }

    public double getXPDropped() {
        return 0.0;
    }

    public GlobalBossBar setBar(final org.bukkit.World w) {
        final GlobalBossBar bb = new GlobalBossBar(ChatColor.RED + this.getEntityName(), w);
        for (final Player p : w.getPlayers()) {
            bb.addPlayer(p);
        }
        return bb;
    }

    public void updateBar(final float percent, final GlobalBossBar bb) {
        bb.setProgress(percent);
    }

    public boolean tick(final LivingEntity entity) {
        this.target = null;
        if (this.frozen) {
            entity.setVelocity(new Vector(0, 0, 0));
            return true;
        }
        final Location location = entity.getLocation();
        if (location.getY() < SUtil.random(7.0, 13.0)) {
            this.yD = SUtil.random(0.6, 1.2);
        }
        if (location.getY() > SUtil.random(57.0, 63.0)) {
            this.yD = SUtil.random(-1.2, -0.6);
        }
        if (location.getX() < -718.0 || location.getX() > -623.0 || location.getZ() < -328.0 || location.getZ() > -224.0) {
            final Vector vector = entity.getLocation().clone().subtract(new Vector(-670.5, 58.0, -275.5)).toVector();
            final Location center = location.clone();
            center.setDirection(vector);
            entity.teleport(center);
            entity.setVelocity(vector.clone().normalize().multiply(-1.0).multiply(3.0));
            return true;
        }
        entity.setVelocity(entity.getLocation().getDirection().clone().multiply(-1.0).multiply(this.speedModifier).setY(this.yD));
        return true;
    }

    public void onSpawn(final LivingEntity entity, final SEntity sEntity) {
        final GlobalBossBar bb = this.setBar(entity.getWorld());
        new BukkitRunnable() {
            public void run() {
                if (entity.isDead()) {
                    Dragon.this.updateBar(1.0E-4f, bb);
                    SUtil.delay(() -> {
                        final ArrayList<Player> plist = new ArrayList<Player>();
                        final Iterator<Player> iterator = bb.players.iterator();
                        while (iterator.hasNext()) {
                            final Player p = iterator.next();
                            plist.add(p);
                        }
                        plist.forEach(pl -> bb.removePlayer(pl));
                        bb.setProgress(0.0);
                        bb.cancel();
                    }, 400L);
                    this.cancel();
                    return;
                }
                Dragon.this.updateBar((float) (entity.getHealth() / entity.getMaxHealth()), bb);
            }
        }.runTaskTimerAsynchronously(Skyblock.getPlugin(), 0L, 1L);
        new BukkitRunnable() {
            public void run() {
                if (entity.isDead()) {
                    this.cancel();
                    return;
                }
                switch (SUtil.random(0, 1)) {
                    case 0:
                        Dragon.this.frozen = true;
                        for (int i = 1; i <= 4; ++i) {
                            SUtil.lightningLater(entity.getLocation(), true, 10 * i);
                        }
                        new BukkitRunnable() {
                            public void run() {
                                if (entity.isDead()) {
                                    return;
                                }
                                for (final Entity e : entity.getNearbyEntities(200.0, 200.0, 200.0)) {
                                    e.getWorld().strikeLightningEffect(e.getLocation());
                                    final double r = SUtil.random(Dragon.this.damageDegree.getMinimum(), Dragon.this.damageDegree.getMaximum());
                                    if (!(e instanceof LivingEntity)) {
                                        continue;
                                    }
                                    final LivingEntity le = (LivingEntity) e;
                                    final int damage = (int) (le.getMaxHealth() * r);
                                    if (le instanceof Player) {
                                        User.getUser(le.getUniqueId()).damage(damage, EntityDamageEvent.DamageCause.ENTITY_ATTACK, entity);
                                    } else {
                                        le.damage(damage);
                                    }
                                    e.sendMessage(ChatColor.DARK_PURPLE + "☬ " + ChatColor.RED + Dragon.this.getEntityName() + ChatColor.LIGHT_PURPLE + " used " + ChatColor.YELLOW + "Lightning Strike" + ChatColor.LIGHT_PURPLE + " on you for " + ChatColor.RED + damage + " damage.");
                                }
                                Dragon.this.frozen = false;
                            }
                        }.runTaskLater(Skyblock.getPlugin(), 50L);
                        return;
                    case 1: {
                        Dragon.this.frozen = true;
                        Entity near = null;
                        for (final Entity n : entity.getNearbyEntities(50.0, 50.0, 50.0)) {
                            if (n instanceof Player) {
                                near = n;
                            }
                        }
                        final Entity finalNear;
                        if ((finalNear = near) != null) {
                            SUtil.runIntervalForTicks(() -> {
                                final Object val$entity = entity;
                                if (entity.isDead()) {
                                } else {
                                    for (int j = 0; j < 15; ++j) {
                                        entity.getWorld().spigot().playEffect(entity.getEyeLocation().subtract(0.0, 8.0, 0.0).add(entity.getLocation().getDirection().multiply(-8.0)).add(SUtil.random(-0.5, 0.5), SUtil.random(-0.5, 0.5), SUtil.random(-0.5, 0.5)), Effect.FLAME, 0, 1, 0.0f, 0.0f, 0.0f, 0.0f, 1, 50);
                                    }
                                }
                            }, 5L, 140L);
                            final float fn = SUtil.getLookAtYaw(near.getLocation().toVector());
                            new BukkitRunnable() {
                                public void run() {
                                    SUtil.runIntervalForTicks(() -> {
                                        final Object val$entity = entity;
                                        final Object val$fn = fn;
                                        if (!entity.isDead()) {
                                            if ((int) fn != (int) entity.getLocation().getYaw()) {
                                                final Location location = entity.getLocation().clone();
                                                location.setYaw(entity.getLocation().clone().getYaw() + 1.0f);
                                                entity.teleport(location);
                                            }
                                        }
                                    }, 1L, 120L);
                                }
                            }.runTaskLater(Skyblock.getPlugin(), 20L);
                            new BukkitRunnable() {
                                public void run() {
                                    SUtil.runIntervalForTicks(() -> {
                                        final Object val$entity = entity;
                                        final Object val$sEntity = sEntity;
                                        final Object val$finalNear = finalNear;
                                        if (!entity.isDead()) {
                                            final Fireball fireball = (Fireball) entity.getWorld().spawn(entity.getEyeLocation().subtract(0.0, 8.0, 0.0).add(entity.getLocation().getDirection().multiply(-10.0)), (Class) Fireball.class);
                                            fireball.setMetadata("dragon", new FixedMetadataValue(Skyblock.getPlugin(), sEntity));
                                            fireball.setDirection(finalNear.getLocation().getDirection().multiply(-1.0).normalize());
                                        }
                                    }, 5L, 60L);
                                }
                            }.runTaskLater(Skyblock.getPlugin(), 80L);
                        }
                        new BukkitRunnable() {
                            public void run() {
                                Dragon.this.frozen = false;
                            }
                        }.runTaskLater(Skyblock.getPlugin(), 140L);
                    }
                    default:
                }
            }
        }.runTaskTimer(Skyblock.getPlugin(), 100L, this.attackCooldown);
    }

    public void onDeath(final SEntity sEntity, final Entity killed, final Entity damager) {
        final Map<UUID, List<Location>> eyes = new HashMap<UUID, List<Location>>(StaticDragonManager.EYES);
        KillEnderCrystal.killEC(killed.getWorld());
        SUtil.delay(() -> StaticDragonManager.endFight(), 500L);
        final StringBuilder message = new StringBuilder();
        message.append(ChatColor.GREEN).append(ChatColor.BOLD).append("▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬\n");
        message.append(ChatColor.GOLD).append(ChatColor.BOLD).append("                 ").append(sEntity.getStatistics().getEntityName().toUpperCase()).append(" DOWN!\n \n");
        final List<Map.Entry<UUID, Double>> damageDealt = new ArrayList<Map.Entry<UUID, Double>>(sEntity.getDamageDealt().entrySet());
        damageDealt.sort((Comparator<? super Map.Entry<UUID, Double>>) Map.Entry.<Object, Comparable>comparingByValue());
        Collections.reverse(damageDealt);
        String name = null;
        if (damager instanceof Player) {
            name = damager.getName();
        }
        if (damager instanceof Arrow && ((Arrow) damager).getShooter() instanceof Player) {
            name = ((Player) ((Arrow) damager).getShooter()).getName();
        }
        if (name != null) {
            message.append("            ").append(ChatColor.GREEN).append(name).append(ChatColor.GRAY).append(" dealt the final blow.\n \n");
        }
        for (int i = 0; i < Math.min(3, damageDealt.size()); ++i) {
            message.append("\n");
            final Map.Entry<UUID, Double> d = damageDealt.get(i);
            final int place = i + 1;
            switch (place) {
                case 1:
                    message.append("        ").append(ChatColor.YELLOW);
                    break;
                case 2:
                    message.append("        ").append(ChatColor.GOLD);
                    break;
                case 3:
                    message.append("        ").append(ChatColor.RED);
                    break;
            }
            message.append(ChatColor.BOLD).append(place);
            switch (place) {
                case 1:
                    message.append("st");
                    break;
                case 2:
                    message.append("nd");
                    break;
                case 3:
                    message.append("rd");
                    break;
            }
            message.append(" Damager").append(ChatColor.RESET).append(ChatColor.GRAY).append(" - ").append(ChatColor.GREEN).append(Bukkit.getOfflinePlayer(d.getKey()).getName()).append(ChatColor.GRAY).append(" - ").append(ChatColor.YELLOW).append(SUtil.commaify(d.getValue().intValue()));
        }
        message.append("\n \n").append("         ").append(ChatColor.RESET).append(ChatColor.YELLOW).append("Your Damage: ").append("%s").append(ChatColor.RESET).append("\n").append("             ").append(ChatColor.YELLOW).append("Runecrafting Experience: ").append(ChatColor.LIGHT_PURPLE).append("N/A").append(ChatColor.RESET).append("\n \n");
        message.append(ChatColor.GREEN).append(ChatColor.BOLD).append("▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
        for (final Player player : Bukkit.getOnlinePlayers()) {
            int place = -1;
            int damage = 0;
            for (int j = 0; j < damageDealt.size(); ++j) {
                final Map.Entry<UUID, Double> d2 = damageDealt.get(j);
                if (d2.getKey().equals(player.getUniqueId())) {
                    place = j + 1;
                    damage = d2.getValue().intValue();
                }
            }
            if (player.getWorld().getName().equals("dragon")) {
                player.sendMessage(String.format(message.toString(), (place != -1) ? (ChatColor.GREEN + SUtil.commaify(damage) + ChatColor.GRAY + " (Position #" + place + ")") : (ChatColor.RED + "N/A" + ChatColor.GRAY + " (Position #N/A)")));
            }
        }
        new BukkitRunnable() {
            public void run() {
                for (int i = 0; i < damageDealt.size(); ++i) {
                    final Map.Entry<UUID, Double> d = damageDealt.get(i);
                    final Player player = Bukkit.getPlayer(d.getKey());
                    if (player != null) {
                        int weight = 0;
                        if (eyes.containsKey(player.getUniqueId())) {
                            weight += Math.min(400, eyes.get(player.getUniqueId()).size() * 100);
                        }
                        if (i == 0) {
                            weight += 300;
                        }
                        if (i == 1) {
                            weight += 250;
                        }
                        if (i == 2) {
                            weight += 200;
                        }
                        if (i >= 3 && i <= 6) {
                            weight += 125;
                        }
                        if (i >= 7 && i <= 14) {
                            weight += 100;
                        }
                        if (i >= 15) {
                            weight += 75;
                        }
                        final List<DragonDrop> possibleMajorDrops = new ArrayList<DragonDrop>();
                        final SEntityType type = sEntity.getSpecType();
                        SUtil.addIf(new DragonDrop(SMaterial.ASPECT_OF_THE_DRAGONS, 450), possibleMajorDrops, weight >= 450);
                        SUtil.addIf(new DragonDrop(SMaterial.ENDER_DRAGON_PET, 450), possibleMajorDrops, weight >= 450);
                        SUtil.addIf(new DragonDrop(SMaterial.ENDER_DRAGON_PET2, 450), possibleMajorDrops, weight >= 450);
                        SUtil.addIf(new DragonDrop(SMaterial.VagueEntityMaterial.HELMET, 400, type), possibleMajorDrops, weight >= 400);
                        SUtil.addIf(new DragonDrop(SMaterial.VagueEntityMaterial.CHESTPLATE, 325, type), possibleMajorDrops, weight >= 325);
                        SUtil.addIf(new DragonDrop(SMaterial.VagueEntityMaterial.LEGGINGS, 350, type), possibleMajorDrops, weight >= 350);
                        SUtil.addIf(new DragonDrop(SMaterial.VagueEntityMaterial.BOOTS, 300, type), possibleMajorDrops, weight >= 300);
                        int remainingWeight = weight;
                        if (possibleMajorDrops.size() > 0) {
                            final DragonDrop drop = possibleMajorDrops.get(SUtil.random(0, possibleMajorDrops.size() - 1));
                            final SMaterial majorDrop = drop.getMaterial();
                            remainingWeight -= drop.getWeight();
                            if (majorDrop != null) {
                                final SItem sItem = SItem.of(majorDrop);
                                if (!sItem.getFullName().equals("§6Ender Dragon") && !sItem.getFullName().equals("§5Ender Dragon")) {
                                    final Item item = SUtil.spawnPersonalItem(sItem.getStack(), killed.getLocation(), player);
                                    item.setMetadata("obtained", new FixedMetadataValue(Skyblock.getPlugin(), true));
                                    item.setCustomNameVisible(true);
                                    item.setCustomName(item.getItemStack().getAmount() + "x " + sItem.getFullName());
                                } else {
                                    final Item item = SUtil.spawnPersonalItem(sItem.getStack(), killed.getLocation(), player);
                                    item.setMetadata("obtained", new FixedMetadataValue(Skyblock.getPlugin(), true));
                                    item.setCustomNameVisible(true);
                                    item.setCustomName(item.getItemStack().getAmount() + "x " + ChatColor.GRAY + "[Lvl 1] " + sItem.getFullName());
                                }
                            }
                        }
                        final List<DragonDrop> minorDrops = new ArrayList<DragonDrop>(Arrays.asList(new DragonDrop(SMaterial.ENDER_PEARL, 0), new DragonDrop(SMaterial.ENCHANTED_ENDER_PEARL, 0)));
                        SUtil.addIf(new DragonDrop(SMaterial.VagueEntityMaterial.FRAGMENT, 22, type), minorDrops, weight >= 22);
                        int frags;
                        for (frags = 0; remainingWeight >= 22; remainingWeight -= 22, ++frags) {
                        }
                        for (final DragonDrop minorDrop : minorDrops) {
                            final SItem sItem2 = SItem.of(minorDrop.getMaterial());
                            if (minorDrop.getMaterial() == null) {
                                continue;
                            }
                            if (minorDrop.getVagueEntityMaterial() != null && frags != 0) {
                                final Item item2 = SUtil.spawnPersonalItem(SUtil.setStackAmount(sItem2.getStack(), frags), killed.getLocation(), player);
                                item2.setCustomNameVisible(true);
                                item2.setCustomName(item2.getItemStack().getAmount() + "x " + sItem2.getFullName());
                            } else {
                                final Item item2 = SUtil.spawnPersonalItem(SUtil.setStackAmount(sItem2.getStack(), SUtil.random(5, 10)), killed.getLocation(), player);
                                item2.setCustomNameVisible(true);
                                item2.setCustomName(item2.getItemStack().getAmount() + "x " + sItem2.getFullName());
                            }
                        }
                    }
                }
            }
        }.runTaskLater(Skyblock.getPlugin(), 200L);
    }

    public LivingEntity spawn(final Location location) {
        this.world = ((CraftWorld) location.getWorld()).getHandle();
        this.setPosition(location.getX(), location.getY(), location.getZ());
        this.world.addEntity(this, CreatureSpawnEvent.SpawnReason.CUSTOM);
        return (LivingEntity) this.getBukkitEntity();
    }

    public void onAttack(final EntityDamageByEntityEvent e) {
        final int d = SUtil.random(354, 902);
        if (e.getEntity() instanceof Player) {
            User.getUser(e.getEntity().getUniqueId()).damage(d, e.getCause(), e.getDamager());
        } else if (e.getEntity() instanceof LivingEntity) {
            ((LivingEntity) e.getEntity()).damage(d);
        }
        e.getEntity().setVelocity(e.getEntity().getVelocity().multiply(8.0));
        e.getEntity().sendMessage(ChatColor.DARK_PURPLE + "☬ " + ChatColor.RED + this.getEntityName() + ChatColor.LIGHT_PURPLE + " used " + ChatColor.YELLOW + "Rush" + ChatColor.LIGHT_PURPLE + " on you for " + ChatColor.RED + d + " damage.");
    }

    public double getSpeedModifier() {
        return this.speedModifier;
    }

    public void setSpeedModifier(final double speedModifier) {
        this.speedModifier = speedModifier;
    }

    public Range<Double> getDamageDegree() {
        return this.damageDegree;
    }

    public void setDamageDegree(final Range<Double> damageDegree) {
        this.damageDegree = damageDegree;
    }

    public long getAttackCooldown() {
        return this.attackCooldown;
    }

    static {
        DEFAULT_DAMAGE_DEGREE_RANGE = Range.between((Comparable) 0.3, (Comparable) 0.7);
    }

    private static class DragonDrop {
        private final SMaterial material;
        private final SMaterial.VagueEntityMaterial vagueEntityMaterial;
        private final int weight;

        public DragonDrop(final SMaterial material, final int weight) {
            this.material = material;
            this.vagueEntityMaterial = null;
            this.weight = weight;
        }

        public DragonDrop(final SMaterial.VagueEntityMaterial material, final int weight, final SEntityType type) {
            this.material = material.getEntityArmorPiece(type);
            this.vagueEntityMaterial = material;
            this.weight = weight;
        }

        public SMaterial getMaterial() {
            return this.material;
        }

        public SMaterial.VagueEntityMaterial getVagueEntityMaterial() {
            return this.vagueEntityMaterial;
        }

        public int getWeight() {
            return this.weight;
        }
    }
}
