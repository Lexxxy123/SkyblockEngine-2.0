package net.hypixel.skyblock.entity.dragon;

import net.hypixel.skyblock.SkyBlock;
import net.hypixel.skyblock.entity.*;
import net.hypixel.skyblock.entity.dungeons.watcher.GlobalBossBar;
import net.hypixel.skyblock.entity.nms.SNMSEntity;
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
import net.hypixel.skyblock.item.SItem;
import net.hypixel.skyblock.item.SMaterial;
import net.hypixel.skyblock.user.User;
import net.hypixel.skyblock.util.SUtil;

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

    protected Dragon(World world, double speedModifier, Range<Double> damageDegree, long attackCooldown) {
        super(world);
        this.frozen = false;
        this.yD = 1.0;
        this.speedModifier = speedModifier;
        this.damageDegree = damageDegree;
        this.attackCooldown = attackCooldown;
    }

    protected Dragon(double speedModifier, Range<Double> damageDegree, long attackCooldown) {
        this(((CraftWorld) Bukkit.getWorlds().get(0)).getHandle(), speedModifier, damageDegree, attackCooldown);
    }

    public double getXPDropped() {
        return 0.0;
    }

    public GlobalBossBar setBar(org.bukkit.World w) {
        GlobalBossBar bb = new GlobalBossBar(ChatColor.RED + this.getEntityName(), w);
        for (Player p : w.getPlayers()) {
            bb.addPlayer(p);
        }
        return bb;
    }

    public void updateBar(float percent, GlobalBossBar bb) {
        bb.setProgress(percent);
    }

    public boolean tick(LivingEntity entity) {
        this.target = null;
        if (this.frozen) {
            entity.setVelocity(new Vector(0, 0, 0));
            return true;
        }
        Location location = entity.getLocation();
        if (location.getY() < SUtil.random(7.0, 13.0)) {
            this.yD = SUtil.random(0.6, 1.2);
        }
        if (location.getY() > SUtil.random(57.0, 63.0)) {
            this.yD = SUtil.random(-1.2, -0.6);
        }
        if (-718.0 > location.getX() || -623.0 < location.getX() || -328.0 > location.getZ() || -224.0 < location.getZ()) {
            Vector vector = entity.getLocation().clone().subtract(new Vector(-670.5, 58.0, -275.5)).toVector();
            Location center = location.clone();
            center.setDirection(vector);
            entity.teleport(center);
            entity.setVelocity(vector.clone().normalize().multiply(-1.0).multiply(3.0));
            return true;
        }
        entity.setVelocity(entity.getLocation().getDirection().clone().multiply(-1.0).multiply(this.speedModifier).setY(this.yD));
        return true;
    }

    public void onSpawn(LivingEntity entity, SEntity sEntity) {
        GlobalBossBar bb = this.setBar(entity.getWorld());
        new BukkitRunnable() {
            public void run() {
                if (entity.isDead()) {
                    Dragon.this.updateBar(1.0E-4f, bb);
                    SUtil.delay(() -> {
                        ArrayList<Player> plist = new ArrayList<Player>();
                        Iterator<Player> iterator = bb.players.iterator();
                        while (iterator.hasNext()) {
                            Player p = iterator.next();
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
        }.runTaskTimerAsynchronously(SkyBlock.getPlugin(), 0L, 1L);
        new BukkitRunnable() {
            public void run() {
                if (entity.isDead()) {
                    this.cancel();
                    return;
                }
                switch (SUtil.random(0, 1)) {
                    case 0:
                        Dragon.this.frozen = true;
                        for (int i = 1; 4 >= i; ++i) {
                            SUtil.lightningLater(entity.getLocation(), true, 10 * i);
                        }
                        new BukkitRunnable() {
                            public void run() {
                                if (entity.isDead()) {
                                    return;
                                }
                                for (Entity e : entity.getNearbyEntities(200.0, 200.0, 200.0)) {
                                    e.getWorld().strikeLightningEffect(e.getLocation());
                                    double r = SUtil.random(Dragon.this.damageDegree.getMinimum(), Dragon.this.damageDegree.getMaximum());
                                    if (!(e instanceof LivingEntity)) {
                                        continue;
                                    }
                                    LivingEntity le = (LivingEntity) e;
                                    int damage = (int) (le.getMaxHealth() * r);
                                    if (le instanceof Player) {
                                        User.getUser(le.getUniqueId()).damage(damage, EntityDamageEvent.DamageCause.ENTITY_ATTACK, entity);
                                    } else {
                                        le.damage(damage);
                                    }
                                    e.sendMessage(ChatColor.DARK_PURPLE + "☬ " + ChatColor.RED + Dragon.this.getEntityName() + ChatColor.LIGHT_PURPLE + " used " + ChatColor.YELLOW + "Lightning Strike" + ChatColor.LIGHT_PURPLE + " on you for " + ChatColor.RED + damage + " damage.");
                                }
                                Dragon.this.frozen = false;
                            }
                        }.runTaskLater(SkyBlock.getPlugin(), 50L);
                        return;
                    case 1: {
                        Dragon.this.frozen = true;
                        Entity near = null;
                        for (Entity n : entity.getNearbyEntities(50.0, 50.0, 50.0)) {
                            if (n instanceof Player) {
                                near = n;
                            }
                        }
                        Entity finalNear;
                        if (null != (finalNear = near)) {
                            SUtil.runIntervalForTicks(() -> {
                                Object val$entity = entity;
                                if (entity.isDead()) {
                                } else {
                                    for (int j = 0; 15 > j; ++j) {
                                        entity.getWorld().spigot().playEffect(entity.getEyeLocation().subtract(0.0, 8.0, 0.0).add(entity.getLocation().getDirection().multiply(-8.0)).add(SUtil.random(-0.5, 0.5), SUtil.random(-0.5, 0.5), SUtil.random(-0.5, 0.5)), Effect.FLAME, 0, 1, 0.0f, 0.0f, 0.0f, 0.0f, 1, 50);
                                    }
                                }
                            }, 5L, 140L);
                            float fn = SUtil.getLookAtYaw(near.getLocation().toVector());
                            new BukkitRunnable() {
                                public void run() {
                                    SUtil.runIntervalForTicks(() -> {
                                        Object val$entity = entity;
                                        Object val$fn = fn;
                                        if (!entity.isDead()) {
                                            if ((int) fn != (int) entity.getLocation().getYaw()) {
                                                Location location = entity.getLocation().clone();
                                                location.setYaw(entity.getLocation().clone().getYaw() + 1.0f);
                                                entity.teleport(location);
                                            }
                                        }
                                    }, 1L, 120L);
                                }
                            }.runTaskLater(SkyBlock.getPlugin(), 20L);
                            new BukkitRunnable() {
                                public void run() {
                                    SUtil.runIntervalForTicks(() -> {
                                        Object val$entity = entity;
                                        Object val$sEntity = sEntity;
                                        Object val$finalNear = finalNear;
                                        if (!entity.isDead()) {
                                            Fireball fireball = (Fireball) entity.getWorld().spawn(entity.getEyeLocation().subtract(0.0, 8.0, 0.0).add(entity.getLocation().getDirection().multiply(-10.0)), (Class) Fireball.class);
                                            fireball.setMetadata("dragon", new FixedMetadataValue(SkyBlock.getPlugin(), sEntity));
                                            fireball.setDirection(finalNear.getLocation().getDirection().multiply(-1.0).normalize());
                                        }
                                    }, 5L, 60L);
                                }
                            }.runTaskLater(SkyBlock.getPlugin(), 80L);
                        }
                        new BukkitRunnable() {
                            public void run() {
                                Dragon.this.frozen = false;
                            }
                        }.runTaskLater(SkyBlock.getPlugin(), 140L);
                    }
                    default:
                }
            }
        }.runTaskTimer(SkyBlock.getPlugin(), 100L, this.attackCooldown);
    }

    public void onDeath(SEntity sEntity, Entity killed, Entity damager) {
        Map<UUID, List<Location>> eyes = new HashMap<UUID, List<Location>>(StaticDragonManager.EYES);
        KillEnderCrystal.killEC(killed.getWorld());
        SUtil.delay(() -> StaticDragonManager.endFight(), 500L);
        StringBuilder message = new StringBuilder();
        message.append(ChatColor.GREEN).append(ChatColor.BOLD).append("▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬\n");
        message.append(ChatColor.GOLD).append(ChatColor.BOLD).append("                 ").append(sEntity.getStatistics().getEntityName().toUpperCase()).append(" DOWN!\n \n");
        List<Map.Entry<UUID, Double>> damageDealt = new ArrayList<Map.Entry<UUID, Double>>(sEntity.getDamageDealt().entrySet());
        damageDealt.sort((Comparator<? super Map.Entry<UUID, Double>>) Map.Entry.<Object, Comparable>comparingByValue());
        Collections.reverse(damageDealt);
        String name = null;
        if (damager instanceof Player) {
            name = damager.getName();
        }
        if (damager instanceof Arrow && ((Arrow) damager).getShooter() instanceof Player) {
            name = ((Player) ((Arrow) damager).getShooter()).getName();
        }
        if (null != name) {
            message.append("            ").append(ChatColor.GREEN).append(name).append(ChatColor.GRAY).append(" dealt the final blow.\n \n");
        }
        for (int i = 0; i < Math.min(3, damageDealt.size()); ++i) {
            message.append("\n");
            Map.Entry<UUID, Double> d = damageDealt.get(i);
            int place = i + 1;
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
        for (Player player : Bukkit.getOnlinePlayers()) {
            int place = -1;
            int damage = 0;
            for (int j = 0; j < damageDealt.size(); ++j) {
                Map.Entry<UUID, Double> d2 = damageDealt.get(j);
                if (d2.getKey().equals(player.getUniqueId())) {
                    place = j + 1;
                    damage = d2.getValue().intValue();
                }
            }
            if (player.getWorld().getName().equals("dragon")) {
                player.sendMessage(String.format(message.toString(), (-1 != place) ? (ChatColor.GREEN + SUtil.commaify(damage) + ChatColor.GRAY + " (Position #" + place + ")") : (ChatColor.RED + "N/A" + ChatColor.GRAY + " (Position #N/A)")));
            }
        }
        new BukkitRunnable() {
            public void run() {
                for (int i = 0; i < damageDealt.size(); ++i) {
                    Map.Entry<UUID, Double> d = damageDealt.get(i);
                    Player player = Bukkit.getPlayer(d.getKey());
                    if (null != player) {
                        int weight = 0;
                        if (eyes.containsKey(player.getUniqueId())) {
                            weight += Math.min(400, eyes.get(player.getUniqueId()).size() * 100);
                        }
                        if (0 == i) {
                            weight += 300;
                        }
                        if (1 == i) {
                            weight += 250;
                        }
                        if (2 == i) {
                            weight += 200;
                        }
                        if (3 <= i && 6 >= i) {
                            weight += 125;
                        }
                        if (7 <= i && 14 >= i) {
                            weight += 100;
                        }
                        if (15 <= i) {
                            weight += 75;
                        }
                        List<DragonDrop> possibleMajorDrops = new ArrayList<DragonDrop>();
                        SEntityType type = sEntity.getSpecType();
                        SUtil.addIf(new DragonDrop(SMaterial.ASPECT_OF_THE_DRAGONS, 450), possibleMajorDrops, 450 <= weight);
                        SUtil.addIf(new DragonDrop(SMaterial.ENDER_DRAGON_PET, 450), possibleMajorDrops, 450 <= weight);
                        SUtil.addIf(new DragonDrop(SMaterial.ENDER_DRAGON_PET2, 450), possibleMajorDrops, 450 <= weight);
                        SUtil.addIf(new DragonDrop(SMaterial.VagueEntityMaterial.HELMET, 400, type), possibleMajorDrops, 400 <= weight);
                        SUtil.addIf(new DragonDrop(SMaterial.VagueEntityMaterial.CHESTPLATE, 325, type), possibleMajorDrops, 325 <= weight);
                        SUtil.addIf(new DragonDrop(SMaterial.VagueEntityMaterial.LEGGINGS, 350, type), possibleMajorDrops, 350 <= weight);
                        SUtil.addIf(new DragonDrop(SMaterial.VagueEntityMaterial.BOOTS, 300, type), possibleMajorDrops, 300 <= weight);
                        int remainingWeight = weight;
                        if (0 < possibleMajorDrops.size()) {
                            DragonDrop drop = possibleMajorDrops.get(SUtil.random(0, possibleMajorDrops.size() - 1));
                            SMaterial majorDrop = drop.getMaterial();
                            remainingWeight -= drop.getWeight();
                            if (null != majorDrop) {
                                SItem sItem = SItem.of(majorDrop);
                                if (!sItem.getFullName().equals("§6Ender Dragon") && !sItem.getFullName().equals("§5Ender Dragon")) {
                                    Item item = SUtil.spawnPersonalItem(sItem.getStack(), killed.getLocation(), player);
                                    item.setMetadata("obtained", new FixedMetadataValue(SkyBlock.getPlugin(), true));
                                    item.setCustomNameVisible(true);
                                    item.setCustomName(item.getItemStack().getAmount() + "x " + sItem.getFullName());
                                } else {
                                    Item item = SUtil.spawnPersonalItem(sItem.getStack(), killed.getLocation(), player);
                                    item.setMetadata("obtained", new FixedMetadataValue(SkyBlock.getPlugin(), true));
                                    item.setCustomNameVisible(true);
                                    item.setCustomName(item.getItemStack().getAmount() + "x " + ChatColor.GRAY + "[Lvl 1] " + sItem.getFullName());
                                }
                            }
                        }
                        List<DragonDrop> minorDrops = new ArrayList<DragonDrop>(Arrays.asList(new DragonDrop(SMaterial.ENDER_PEARL, 0), new DragonDrop(SMaterial.ENCHANTED_ENDER_PEARL, 0)));
                        SUtil.addIf(new DragonDrop(SMaterial.VagueEntityMaterial.FRAGMENT, 22, type), minorDrops, 22 <= weight);
                        int frags;
                        for (frags = 0; 22 <= remainingWeight; remainingWeight -= 22, ++frags) {
                        }
                        for (DragonDrop minorDrop : minorDrops) {
                            SItem sItem2 = SItem.of(minorDrop.getMaterial());
                            if (null == minorDrop.getMaterial()) {
                                continue;
                            }
                            if (null != minorDrop.getVagueEntityMaterial() && 0 != frags) {
                                Item item2 = SUtil.spawnPersonalItem(SUtil.setStackAmount(sItem2.getStack(), frags), killed.getLocation(), player);
                                item2.setCustomNameVisible(true);
                                item2.setCustomName(item2.getItemStack().getAmount() + "x " + sItem2.getFullName());
                            } else {
                                Item item2 = SUtil.spawnPersonalItem(SUtil.setStackAmount(sItem2.getStack(), SUtil.random(5, 10)), killed.getLocation(), player);
                                item2.setCustomNameVisible(true);
                                item2.setCustomName(item2.getItemStack().getAmount() + "x " + sItem2.getFullName());
                            }
                        }
                    }
                }
            }
        }.runTaskLater(SkyBlock.getPlugin(), 200L);
    }

    public LivingEntity spawn(Location location) {
        this.world = ((CraftWorld) location.getWorld()).getHandle();
        this.setPosition(location.getX(), location.getY(), location.getZ());
        this.world.addEntity(this, CreatureSpawnEvent.SpawnReason.CUSTOM);
        return (LivingEntity) this.getBukkitEntity();
    }

    public void onAttack(EntityDamageByEntityEvent e) {
        int d = SUtil.random(354, 902);
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

    public void setSpeedModifier(double speedModifier) {
        this.speedModifier = speedModifier;
    }

    public Range<Double> getDamageDegree() {
        return this.damageDegree;
    }

    public void setDamageDegree(Range<Double> damageDegree) {
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

        public DragonDrop(SMaterial material, int weight) {
            this.material = material;
            this.vagueEntityMaterial = null;
            this.weight = weight;
        }

        public DragonDrop(SMaterial.VagueEntityMaterial material, int weight, SEntityType type) {
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
