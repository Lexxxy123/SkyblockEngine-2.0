package vn.giakhanhvn.skysim.entity.nms;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.block.Block;
import vn.giakhanhvn.skysim.util.BlockFallAPI;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.event.entity.EntityDamageEvent;
import vn.giakhanhvn.skysim.user.User;
import org.bukkit.entity.ArmorStand;
import org.bukkit.Sound;
import org.bukkit.Effect;
import org.bukkit.Bukkit;
import org.bukkit.entity.Arrow;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;
import java.util.Collection;
import java.util.UUID;
import java.util.Map;
import java.util.ArrayList;
import org.bukkit.ChatColor;
import vn.giakhanhvn.skysim.entity.SEntityEquipment;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.metadata.FixedMetadataValue;
import vn.giakhanhvn.skysim.SkySimEngine;
import vn.giakhanhvn.skysim.util.EntityManager;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import vn.giakhanhvn.skysim.util.SUtil;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftZombie;
import vn.giakhanhvn.skysim.entity.SEntity;
import java.util.Iterator;
import org.bukkit.entity.Player;
import org.bukkit.World;
import vn.giakhanhvn.skysim.util.Sputnik;
import vn.giakhanhvn.skysim.util.BossBar;
import org.bukkit.entity.LivingEntity;
import vn.giakhanhvn.skysim.entity.zombie.BaseZombie;

public class Giant extends BaseZombie
{
    private static LivingEntity e;
    private boolean laserActiveCD;
    private boolean laserActive;
    private boolean shockWave;
    private boolean shockWaveCD;
    private boolean terToss;
    private boolean terTossCD;
    private boolean swordActiv;
    private boolean swordSlamCD;
    private BossBar bb;
    
    public Giant() {
        this.laserActiveCD = true;
        this.laserActive = false;
        this.shockWave = false;
        this.shockWaveCD = true;
        this.terToss = false;
        this.terTossCD = true;
        this.swordActiv = false;
        this.swordSlamCD = true;
    }
    
    @Override
    public String getEntityName() {
        return Sputnik.trans("&4&lTerrorant");
    }
    
    @Override
    public double getEntityMaxHealth() {
        return 2.0E9;
    }
    
    @Override
    public double getDamageDealt() {
        return 1000000.0;
    }
    
    public BossBar setBar(final World w, final String s) {
        this.bb = new BossBar(Sputnik.trans(s));
        for (final Player p : w.getPlayers()) {
            this.bb.addPlayer(p);
        }
        return this.bb;
    }
    
    public void removeAllBar(final World w, final BossBar b) {
        for (final Player p : w.getPlayers()) {
            b.removePlayer(p);
        }
    }
    
    public void updateBar(final double percent) {
        this.bb.setProgress(percent);
    }
    
    @Override
    public void onSpawn(final LivingEntity entity, final SEntity sEntity) {
        Giant.e = entity;
        final BossBar boss = this.setBar(entity.getWorld(), "&4&lTerrorant");
        ((CraftZombie)entity).setBaby(false);
        SUtil.delay(() -> this.shockWaveCD = false, 400L);
        SUtil.delay(() -> this.terTossCD = false, 200L);
        SUtil.delay(() -> this.laserActiveCD = false, 300L);
        SUtil.delay(() -> this.swordSlamCD = false, 100L);
        entity.getEquipment().setItemInHand(SUtil.enchant(new ItemStack(Material.DIAMOND_SWORD)));
        Sputnik.applyPacketGiant((Entity)entity);
        EntityManager.DEFENSE_PERCENTAGE.put((Entity)entity, 60);
        entity.setMetadata("SlayerBoss", (MetadataValue)new FixedMetadataValue((Plugin)SkySimEngine.getPlugin(), (Object)true));
        entity.setMetadata("highername", (MetadataValue)new FixedMetadataValue((Plugin)SkySimEngine.getPlugin(), (Object)true));
        new BukkitRunnable() {
            public void run() {
                if (entity.getHealth() > 0.0) {
                    Giant.this.updateBar(entity.getHealth() / entity.getMaxHealth());
                }
                else {
                    Giant.this.updateBar(9.990009990009992E-4);
                }
                final LivingEntity target = (LivingEntity)((CraftZombie)entity).getTarget();
                if (entity.isDead()) {
                    SUtil.delay(() -> {
                        final Object val$entity = entity;
                        final Object val$boss = boss;
                        Giant.this.removeAllBar(entity.getWorld(), boss);
                        return;
                    }, 250L);
                }
                if (!Giant.this.laserActiveCD && !Giant.this.laserActive && SUtil.random(1, 120) <= 6 && target != null) {
                    Giant.this.laserActiveCD = true;
                    Giant.this.laserActive = true;
                    Giant.this.laser(entity);
                }
                if (!Giant.this.swordSlamCD && !Giant.this.swordActiv && !Giant.this.shockWave && SUtil.random(1, 140) <= 7 && target != null) {
                    Giant.this.swordActiv = true;
                    Giant.this.swordSlamCD = true;
                    Giant.this.swordSlamAC(entity, target);
                }
                if (!Giant.this.shockWave && !Giant.this.shockWaveCD && SUtil.random(1, 100) <= 5 && !Giant.this.swordActiv) {
                    Giant.this.shockWaveCD = true;
                    Giant.this.shockWave = true;
                    final Vector vec = new Vector(0, 0, 0);
                    vec.setY(2);
                    Giant.e.setVelocity(vec);
                    SUtil.delay(() -> {
                        final Object val$entity2 = entity;
                        Giant.this.jumpAni(entity);
                        return;
                    }, 10L);
                }
                if (!Giant.this.terToss && !Giant.this.terTossCD && SUtil.random(1, 150) <= 4) {
                    Giant.this.terTossCD = true;
                    Giant.this.terToss = true;
                    SUtil.delay(() -> Giant.this.terToss = false, 300L);
                    Giant.e.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 300, 0));
                    Giant.this.launchTerrain(entity);
                }
            }
        }.runTaskTimer((Plugin)SkySimEngine.getPlugin(), 0L, 1L);
    }
    
    @Override
    public SEntityEquipment getEntityEquipment() {
        return new SEntityEquipment(SUtil.enchant(new ItemStack(Material.DIAMOND_SWORD)), b(15249075, Material.LEATHER_HELMET), b(13565952, Material.LEATHER_CHESTPLATE), c(Material.DIAMOND_LEGGINGS), b(15132390, Material.LEATHER_BOOTS));
    }
    
    @Override
    public void onDeath(final SEntity sEntity, final Entity killed, final Entity damager) {
        final StringBuilder message = new StringBuilder();
        message.append(ChatColor.GREEN).append(ChatColor.BOLD).append("▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬\n");
        message.append(ChatColor.GOLD).append(ChatColor.BOLD).append("                 ").append(sEntity.getStatistics().getEntityName().toUpperCase()).append(" DOWN!\n \n");
        final List<Map.Entry<UUID, Double>> damageDealt = new ArrayList<Map.Entry<UUID, Double>>(sEntity.getDamageDealt().entrySet());
        damageDealt.sort((Comparator<? super Map.Entry<UUID, Double>>)Map.Entry.<Object, Comparable>comparingByValue());
        Collections.reverse(damageDealt);
        String name = null;
        if (damager instanceof Player) {
            name = damager.getName();
        }
        if (damager instanceof Arrow && ((Arrow)damager).getShooter() instanceof Player) {
            name = ((Player)((Arrow)damager).getShooter()).getName();
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
            message.append(" Damager").append(ChatColor.RESET).append(ChatColor.GRAY).append(" - ").append(ChatColor.GREEN).append(Bukkit.getOfflinePlayer((UUID)d.getKey()).getName()).append(ChatColor.GRAY).append(" - ").append(ChatColor.YELLOW).append(SUtil.commaify(d.getValue().intValue()));
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
            if (player.getWorld().getName().equals("gisland")) {
                player.sendMessage(String.format(message.toString(), (place != -1) ? (ChatColor.GREEN + SUtil.commaify(damage) + ChatColor.GRAY + " (Position #" + place + ")") : (ChatColor.RED + "N/A" + ChatColor.GRAY + " (Position #N/A)")));
            }
        }
    }
    
    @Override
    public boolean hasNameTag() {
        return false;
    }
    
    @Override
    public boolean isVillager() {
        return false;
    }
    
    @Override
    public boolean isBaby() {
        return false;
    }
    
    @Override
    public double getXPDropped() {
        return 0.0;
    }
    
    @Override
    public double getMovementSpeed() {
        return 0.3;
    }
    
    public void laser(final LivingEntity e) {
        final int[] array_colors = { 15249075, 15178658, 14907008, 14634331, 14563143 };
        applyEffect(PotionEffectType.SLOW, (Entity)e, 240, 1);
        SUtil.delay(() -> e.getEquipment().setHelmet(buildColorStack(array_colors[0])), 20L);
        SUtil.delay(() -> e.getEquipment().setHelmet(buildColorStack(array_colors[1])), 40L);
        SUtil.delay(() -> e.getEquipment().setHelmet(buildColorStack(array_colors[2])), 60L);
        SUtil.delay(() -> e.getEquipment().setHelmet(buildColorStack(array_colors[3])), 80L);
        SUtil.delay(() -> e.getEquipment().setHelmet(buildColorStack(array_colors[4])), 100L);
        SUtil.delay(() -> this.laserAni(e), 105L);
        SUtil.delay(() -> this.laserActive = false, 250L);
        SUtil.delay(() -> e.getEquipment().setHelmet(buildColorStack(array_colors[4])), 270L);
        SUtil.delay(() -> e.getEquipment().setHelmet(buildColorStack(array_colors[3])), 290L);
        SUtil.delay(() -> e.getEquipment().setHelmet(buildColorStack(array_colors[2])), 310L);
        SUtil.delay(() -> e.getEquipment().setHelmet(buildColorStack(array_colors[1])), 330L);
        SUtil.delay(() -> e.getEquipment().setHelmet(buildColorStack(array_colors[0])), 350L);
        SUtil.delay(() -> e.getEquipment().setHelmet(b(15249075, Material.LEATHER_HELMET)), 370L);
        SUtil.delay(() -> this.laserActiveCD = false, 950L);
    }
    
    public void jumpAni(final LivingEntity e) {
        new BukkitRunnable() {
            public void run() {
                if (e.isDead()) {
                    this.cancel();
                    return;
                }
                if (!Giant.this.shockWave) {
                    this.cancel();
                    return;
                }
                if (e.isOnGround()) {
                    Giant.this.shockWave = false;
                    SUtil.delay(() -> Giant.this.shockWaveCD = false, 500L);
                    e.getWorld().playEffect(e.getLocation().add(0.0, 0.5, 0.0), Effect.EXPLOSION_HUGE, 3);
                    e.getWorld().playEffect(e.getLocation(), Effect.EXPLOSION_HUGE, 3);
                    e.getWorld().playSound(e.getLocation(), Sound.EXPLODE, 3.0f, 0.0f);
                    SUtil.delay(() -> {
                        final Object val$e = e;
                        e.getWorld().playSound(e.getLocation(), Sound.EXPLODE, 10.0f, 0.0f);
                        return;
                    }, 5L);
                    SUtil.delay(() -> {
                        final Object val$e2 = e;
                        e.getWorld().playEffect(e.getLocation().add(0.0, 0.5, 0.0), Effect.EXPLOSION_HUGE, 3);
                        return;
                    }, 5L);
                    SUtil.delay(() -> {
                        final Object val$e3 = e;
                        e.getWorld().playEffect(e.getLocation().add(0.0, 0.5, 0.0), Effect.EXPLOSION_HUGE, 3);
                        return;
                    }, 7L);
                    Giant.createBlockTornado((Entity)e, e.getLocation().add(0.0, -1.0, 0.0).getBlock().getType(), e.getLocation().add(0.0, -1.0, 0.0).getBlock().getData());
                    new BukkitRunnable() {
                        public void run() {
                            Giant.createBlockTornado((Entity)e, e.getLocation().add(0.0, -1.0, 0.0).getBlock().getType(), e.getLocation().add(0.0, -1.0, 0.0).getBlock().getData());
                        }
                    }.runTaskLater((Plugin)SkySimEngine.getPlugin(), 5L);
                    new BukkitRunnable() {
                        public void run() {
                            Giant.createBlockTornado((Entity)e, e.getLocation().add(0.0, -2.0, 0.0).getBlock().getType(), e.getLocation().add(0.0, -2.0, 0.0).getBlock().getData());
                        }
                    }.runTaskLater((Plugin)SkySimEngine.getPlugin(), 6L);
                    new BukkitRunnable() {
                        public void run() {
                            Giant.createBlockTornado((Entity)e, e.getLocation().add(0.0, -2.0, 0.0).getBlock().getType(), e.getLocation().add(0.0, -2.0, 0.0).getBlock().getData());
                        }
                    }.runTaskLater((Plugin)SkySimEngine.getPlugin(), 7L);
                    new BukkitRunnable() {
                        public void run() {
                            Giant.createBlockTornado((Entity)e, e.getLocation().add(0.0, -1.0, 0.0).getBlock().getType(), e.getLocation().add(0.0, -1.0, 0.0).getBlock().getData());
                        }
                    }.runTaskLater((Plugin)SkySimEngine.getPlugin(), 8L);
                    SUtil.delay(() -> {
                        final Object val$e4 = e;
                        e.getWorld().playSound(e.getLocation(), Sound.EXPLODE, 3.0f, 0.0f);
                        return;
                    }, 5L);
                    SUtil.delay(() -> {
                        final Object val$e5 = e;
                        e.getWorld().playSound(e.getLocation(), Sound.EXPLODE, 3.0f, 0.0f);
                        return;
                    }, 10L);
                    for (final Entity entities : e.getNearbyEntities(22.0, 10.0, 22.0)) {
                        final Vector vec = new Vector(0, 0, 0);
                        if (entities.hasMetadata("NPC")) {
                            continue;
                        }
                        if (entities instanceof ArmorStand) {
                            continue;
                        }
                        if (entities instanceof org.bukkit.entity.Giant) {
                            continue;
                        }
                        if (entities.getLocation().distance(e.getLocation()) > 8.0) {
                            vec.setY(2.25);
                            vec.setX(0.25);
                            entities.setVelocity(vec);
                        }
                        else {
                            vec.setY(2.5);
                            vec.setX(0.5);
                            entities.setVelocity(vec);
                        }
                        if (!(entities instanceof Player)) {
                            continue;
                        }
                        if (entities.getLocation().distance(e.getLocation()) <= 5.0) {
                            final Player p = (Player)entities;
                            final double damage = SUtil.random(100, 300) + p.getMaxHealth() * 90.0 / 100.0;
                            User.getUser(p.getUniqueId()).damage(damage, EntityDamageEvent.DamageCause.ENTITY_ATTACK, (Entity)e);
                            ((LivingEntity)p).damage(1.0E-6, (Entity)null);
                            p.sendMessage(Sputnik.trans("&7Terrorant's Shockwave Stomp have hit you for &c" + SUtil.commaify(Math.round(damage)) + " &7true damage."));
                        }
                        else {
                            final Player p = (Player)entities;
                            final double damage = SUtil.random(100, 300) + p.getMaxHealth() * 10.0 / 100.0;
                            User.getUser(p.getUniqueId()).damage(damage, EntityDamageEvent.DamageCause.ENTITY_ATTACK, (Entity)e);
                            ((LivingEntity)p).damage(1.0E-6, (Entity)null);
                            p.sendMessage(Sputnik.trans("&7Terrorant's Shockwave Stomp have hit you for &c" + SUtil.commaify(Math.round(damage)) + " &7true damage."));
                        }
                    }
                }
            }
        }.runTaskTimer((Plugin)SkySimEngine.getPlugin(), 0L, 1L);
    }
    
    public void laserAni(final LivingEntity e) {
        new BukkitRunnable() {
            public void run() {
                if (e.isDead()) {
                    this.cancel();
                    return;
                }
                if (!Giant.this.laserActive) {
                    this.cancel();
                    return;
                }
                for (final Entity p : e.getNearbyEntities(20.0, 20.0, 20.0)) {
                    if (!(p instanceof Player)) {
                        continue;
                    }
                    final Player p2 = (Player)p;
                    p2.playSound(e.getLocation(), "mob.guardian.elder.idle", 0.3f, 2.0f);
                    p2.playSound(e.getLocation(), "mob.guardian.elder.idle", 0.3f, 0.0f);
                }
            }
        }.runTaskTimer((Plugin)SkySimEngine.getPlugin(), 0L, 2L);
        new BukkitRunnable() {
            public void run() {
                if (e.isDead()) {
                    this.cancel();
                    return;
                }
                if (!Giant.this.laserActive) {
                    this.cancel();
                    return;
                }
                final LivingEntity target = (LivingEntity)((CraftZombie)e).getTarget();
                final float angle_1 = e.getEyeLocation().getYaw() / 60.0f;
                final Location loc1 = e.getEyeLocation().add(Math.cos(angle_1), 0.0, Math.sin(angle_1));
                final Location loc2 = e.getEyeLocation().subtract(Math.cos(angle_1), 0.0, Math.sin(angle_1));
                loc1.add(0.0, 9.5, 0.0);
                loc2.add(0.0, 9.5, 0.0);
                if (target != null) {
                    if (target.getLocation().distance(e.getLocation()) < 5.0 || target.getLocation().distance(e.getLocation()) > 30.0) {
                        return;
                    }
                    final Location loc1_ = target.getLocation();
                    final Location loc2_ = target.getLocation();
                    final Location en1 = loc1_.add(0.0, 0.5, 0.0);
                    final Location en2 = loc2_.add(0.0, 0.5, 0.0);
                    Giant.drawLine(loc1, en1, 0.0);
                    Giant.drawLine(loc2, en2, 0.0);
                }
            }
        }.runTaskTimer((Plugin)SkySimEngine.getPlugin(), 0L, 5L);
        new BukkitRunnable() {
            public void run() {
                if (e.isDead()) {
                    this.cancel();
                    return;
                }
                if (!Giant.this.laserActive) {
                    this.cancel();
                    return;
                }
                for (final Entity entity : e.getNearbyEntities(4.0, 10.0, 4.0)) {
                    if (entity instanceof Player) {
                        final double damage = SUtil.random(100, 150) + ((LivingEntity)entity).getMaxHealth() * 5.0 / 100.0;
                        User.getUser(entity.getUniqueId()).damage(damage, EntityDamageEvent.DamageCause.ENTITY_ATTACK, (Entity)e);
                        ((Player)entity).sendMessage(Sputnik.trans("&7Terrorant's Laser Heat have hit you for &c" + SUtil.commaify(Math.round(damage)) + " &7true damage! Move away from it!"));
                        ((LivingEntity)entity).damage(1.0E-6, (Entity)null);
                    }
                }
                final LivingEntity target = (LivingEntity)((CraftZombie)e).getTarget();
                final float angle_1 = e.getEyeLocation().getYaw() / 60.0f;
                final Location loc1 = e.getEyeLocation().add(Math.cos(angle_1), 0.0, Math.sin(angle_1));
                final Location loc2 = e.getEyeLocation().subtract(Math.cos(angle_1), 0.0, Math.sin(angle_1));
                loc1.add(0.0, 9.5, 0.0);
                loc2.add(0.0, 9.5, 0.0);
                if (target != null) {
                    if (target.getLocation().distance(e.getLocation()) < 5.0 || target.getLocation().distance(e.getLocation()) > 30.0) {
                        return;
                    }
                    final Location loc1_ = target.getLocation();
                    final Location loc2_ = target.getLocation();
                    final Location en1 = loc1_.add(0.0, 0.5, 0.0);
                    final Location en2 = loc2_.add(0.0, 0.5, 0.0);
                    Giant.getEntity(loc1, en1, e);
                    Giant.getEntity(loc2, en2, e);
                }
            }
        }.runTaskTimer((Plugin)SkySimEngine.getPlugin(), 0L, 20L);
    }
    
    public void launchTerrain(final LivingEntity e) {
        new BukkitRunnable() {
            public void run() {
                if (e.isDead()) {
                    this.cancel();
                    return;
                }
                if (!Giant.this.terToss) {
                    SUtil.delay(() -> Giant.this.terTossCD = false, 550L);
                    this.cancel();
                    return;
                }
                final LivingEntity t = (LivingEntity)((CraftZombie)e).getTarget();
                if (t != null) {
                    Giant.this.throwTerrain(e, (Entity)t);
                }
            }
        }.runTaskTimer((Plugin)SkySimEngine.getPlugin(), 0L, 30L);
    }
    
    public static ItemStack buildColorStack(final int hexcolor) {
        final ItemStack stack = SUtil.applyColorToLeatherArmor(new ItemStack(Material.LEATHER_HELMET), Color.fromRGB(hexcolor));
        final ItemMeta itemMeta = stack.getItemMeta();
        itemMeta.spigot().setUnbreakable(true);
        stack.setItemMeta(itemMeta);
        return stack;
    }
    
    public static ItemStack b(final int hexcolor, final Material m) {
        final ItemStack stack = SUtil.applyColorToLeatherArmor(new ItemStack(m), Color.fromRGB(hexcolor));
        final ItemMeta itemMeta = stack.getItemMeta();
        itemMeta.spigot().setUnbreakable(true);
        stack.setItemMeta(itemMeta);
        return stack;
    }
    
    public static ItemStack c(final Material m) {
        final ItemStack stack = new ItemStack(m);
        final ItemMeta itemMeta = stack.getItemMeta();
        itemMeta.spigot().setUnbreakable(true);
        stack.setItemMeta(itemMeta);
        return stack;
    }
    
    public static void drawLine(final Location point1, final Location point2, final double space) {
        final Location blockLocation = point1;
        final Location crystalLocation = point2;
        final Vector vector = blockLocation.clone().add(0.1, 0.0, 0.1).toVector().subtract(crystalLocation.clone().toVector());
        final double count = 90.0;
        for (int i = 1; i <= (int)count; ++i) {
            point1.getWorld().spigot().playEffect(crystalLocation.clone().add(vector.clone().multiply(i / count)), Effect.COLOURED_DUST, 0, 1, 0.8627451f, 0.03529412f, 0.007843138f, 1.0f, 0, 64);
            point1.getWorld().spigot().playEffect(crystalLocation.clone().add(vector.clone().multiply(i / count)), Effect.COLOURED_DUST, 0, 1, 1.0196079f, 0.03529412f, 0.007843138f, 1.0f, 0, 64);
        }
    }
    
    public static void getEntity(final Location finaldestination, final Location ended, final LivingEntity e) {
        final Location blockLocation = finaldestination;
        final Location crystalLocation = ended;
        final Vector vector = blockLocation.clone().add(0.1, 0.1, 0.1).toVector().subtract(crystalLocation.clone().toVector());
        final double count = 90.0;
        for (int i = 1; i <= (int)count; ++i) {
            for (final Entity entity : ended.getWorld().getNearbyEntities(crystalLocation.clone().add(vector.clone().multiply(i / count)), 0.17, 0.0, 0.17)) {
                if (entity instanceof Player) {
                    final Player p = (Player)entity;
                    final double damage = SUtil.random(200, 700) + p.getMaxHealth() * 1.0 / 100.0;
                    User.getUser(p.getUniqueId()).damage(damage, EntityDamageEvent.DamageCause.ENTITY_ATTACK, (Entity)e);
                    ((LivingEntity)p).damage(1.0E-6, (Entity)null);
                    p.sendMessage(Sputnik.trans("&7Terrorant's Laser Eye have hit you for &c" + SUtil.commaify(Math.round(damage)) + " &7true damage."));
                    return;
                }
            }
        }
    }
    
    public static void applyEffect(final PotionEffectType e, final Entity en, final int ticks, final int amp) {
        ((LivingEntity)en).addPotionEffect(new PotionEffect(e, ticks, amp));
    }
    
    public static void createBlockTornado(final Entity e, final Material mat, final byte id) {
        for (int i = 0; i <= 30; ++i) {
            final int random = SUtil.random(0, 3);
            double range = 0.0;
            final Location loc = e.getLocation().clone();
            loc.setYaw((float)SUtil.random(0, 360));
            if (random == 1) {
                range = 0.6;
            }
            if (random == 2) {
                range = 0.7;
            }
            if (random == 3) {
                range = 0.8;
            }
            final Vector vec = loc.getDirection().normalize().multiply(range);
            vec.setY(1.1);
            BlockFallAPI.sendVelocityBlock(e.getLocation(), mat, id, e.getWorld(), 70, vec);
        }
    }
    
    public static void damagePlayer(final Player p) {
        final double damage = SUtil.random(200, 700) + p.getMaxHealth() * 25.0 / 100.0;
        User.getUser(p.getUniqueId()).damage(damage, EntityDamageEvent.DamageCause.ENTITY_ATTACK, (Entity)Giant.e);
        ((LivingEntity)p).damage(1.0E-6, (Entity)null);
        p.sendMessage(Sputnik.trans("&7Terrorant's Terrain Toss have hit you for &c" + SUtil.commaify(Math.round(damage)) + " &7true damage."));
    }
    
    public void throwTerrain(final LivingEntity e, final Entity target) {
        final Block b = target.getLocation().getBlock();
        final World world = e.getWorld();
        final Location startBlock = e.getLocation().add(e.getLocation().getDirection().multiply(2));
        final List<Location> locationList = new ArrayList<Location>();
        final List<Location> endList = new ArrayList<Location>();
        final List<Material> blockTypes = new ArrayList<Material>();
        final List<Material> launchTypes = new ArrayList<Material>();
        for (int length = -1; length < 2; ++length) {
            for (int height = -1; height < 2; ++height) {
                final Location loc = startBlock.clone().add((double)length, 0.0, (double)height);
                final Location end = b.getLocation().clone().add((double)length, 0.0, (double)height);
                locationList.add(loc);
                endList.add(end);
            }
        }
        locationList.add(startBlock.clone().add(0.0, 0.0, 2.0));
        locationList.add(startBlock.clone().add(0.0, 0.0, -2.0));
        locationList.add(startBlock.clone().add(2.0, 0.0, 0.0));
        locationList.add(startBlock.clone().add(-2.0, 0.0, 0.0));
        endList.add(b.getLocation().clone().add(0.0, 0.0, 2.0));
        endList.add(b.getLocation().clone().add(0.0, 0.0, -2.0));
        endList.add(b.getLocation().clone().add(2.0, 0.0, 0.0));
        endList.add(b.getLocation().clone().add(-2.0, 0.0, 0.0));
        locationList.add(startBlock.clone().add(0.0, -1.0, 0.0));
        locationList.add(startBlock.clone().add(1.0, -1.0, 0.0));
        locationList.add(startBlock.clone().add(-1.0, -1.0, 0.0));
        locationList.add(startBlock.clone().add(0.0, -1.0, 1.0));
        locationList.add(startBlock.clone().add(0.0, -1.0, -1.0));
        endList.add(b.getLocation().clone().add(0.0, -1.0, 0.0));
        endList.add(b.getLocation().clone().add(1.0, -1.0, 0.0));
        endList.add(b.getLocation().clone().add(-1.0, -1.0, 0.0));
        endList.add(b.getLocation().clone().add(0.0, -1.0, 1.0));
        endList.add(b.getLocation().clone().add(0.0, -1.0, -1.0));
        final Byte blockData = 0;
        locationList.forEach(block -> {
            final Location loc2 = block.getBlock().getLocation().clone().subtract(0.0, 1.0, 0.0);
            Material mat = loc2.getBlock().getType();
            if (mat == Material.AIR) {
                mat = Material.STONE;
            }
            launchTypes.add(mat);
            blockTypes.add(block.getBlock().getType());
            return;
        });
        locationList.forEach(location -> {
            final Material material = launchTypes.get(locationList.indexOf(location));
            final Location origin = location.clone().add(0.0, 7.0, 0.0);
            final int pos = locationList.indexOf(location);
            final Location endPos = endList.get(pos);
            final FallingBlock block2 = world.spawnFallingBlock(origin, material, (byte)blockData);
            block2.setDropItem(false);
            block2.setMetadata("t", (MetadataValue)new FixedMetadataValue((Plugin)SkySimEngine.getPlugin(), (Object)true));
            block2.setVelocity(Sputnik.calculateVelocityBlock(origin.toVector(), endPos.toVector(), 3));
        });
    }
    
    public static void playLaserSound(final Player p, final Entity e) {
        new BukkitRunnable() {
            public void run() {
                if (e.isDead()) {
                    this.cancel();
                    return;
                }
                p.playSound(p.getLocation(), "mob.guardian.elder.idle", 0.3f, 2.0f);
                p.playSound(p.getLocation(), "mob.guardian.elder.idle", 0.3f, 0.0f);
            }
        }.runTaskTimer((Plugin)SkySimEngine.getPlugin(), 0L, 1L);
    }
    
    public void swordSlamAC(final LivingEntity e, final LivingEntity tar) {
        applyEffect(PotionEffectType.SLOW, (Entity)e, 60, 4);
        SUtil.delay(() -> this.swordSlamF(e, tar), 60L);
    }
    
    public void swordSlamF(final LivingEntity e, final LivingEntity tar) {
        final Vector vec = new Vector(0, 0, 0);
        vec.setY(2);
        e.setVelocity(vec);
        SUtil.delay(() -> this.swordSlam(e, tar), 30L);
    }
    
    public void swordSlam(final LivingEntity e, final LivingEntity player) {
        e.getEquipment().setItemInHand((ItemStack)null);
        final org.bukkit.entity.Giant armorStand = (org.bukkit.entity.Giant)player.getWorld().spawn(e.getLocation().add(0.0, 5.0, 0.0), (Class)org.bukkit.entity.Giant.class);
        armorStand.getEquipment().setItemInHand(SUtil.enchant(new ItemStack(Material.DIAMOND_SWORD)));
        Sputnik.applyPacketGiant((Entity)armorStand);
        armorStand.setCustomName("Dinnerbone");
        armorStand.setMetadata("GiantSword", (MetadataValue)new FixedMetadataValue((Plugin)SkySimEngine.getPlugin(), (Object)true));
        armorStand.setMetadata("NoAffect", (MetadataValue)new FixedMetadataValue((Plugin)SkySimEngine.getPlugin(), (Object)true));
        EntityManager.Woosh((LivingEntity)armorStand);
        EntityManager.noHit((Entity)armorStand);
        EntityManager.shutTheFuckUp((Entity)armorStand);
        final Location firstLocation = e.getLocation().add(0.0, 5.0, 0.0);
        final Location secondLocation = player.getLocation();
        final Vector from = firstLocation.toVector();
        final Vector to = secondLocation.toVector();
        final Vector direction = to.subtract(from);
        direction.normalize();
        direction.multiply(3);
        armorStand.setVelocity(direction);
        new BukkitRunnable() {
            public void run() {
                if (!Giant.this.swordActiv) {
                    this.cancel();
                    return;
                }
                if (armorStand.isOnGround()) {
                    Giant.this.swordActiv = false;
                    SUtil.delay(() -> Giant.this.swordSlamCD = false, 450L);
                    armorStand.remove();
                    final org.bukkit.entity.Giant sword = (org.bukkit.entity.Giant)e.getWorld().spawnEntity(armorStand.getLocation(), EntityType.GIANT);
                    Sputnik.applyPacketGiant((Entity)sword);
                    sword.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 100000, 1));
                    EntityManager.noAI((Entity)sword);
                    EntityManager.noHit((Entity)sword);
                    EntityManager.shutTheFuckUp((Entity)sword);
                    sword.setCustomName("Dinnerbone");
                    sword.setMetadata("GiantSword", (MetadataValue)new FixedMetadataValue((Plugin)SkySimEngine.getPlugin(), (Object)true));
                    sword.setMetadata("NoAffect", (MetadataValue)new FixedMetadataValue((Plugin)SkySimEngine.getPlugin(), (Object)true));
                    final ArmorStand stand = (ArmorStand)player.getWorld().spawnEntity(armorStand.getLocation(), EntityType.ARMOR_STAND);
                    stand.setVisible(false);
                    stand.setGravity(true);
                    stand.setPassenger((Entity)sword);
                    sword.getEquipment().setItemInHand(SUtil.enchant(new ItemStack(Material.DIAMOND_SWORD)));
                    e.getWorld().playSound(player.getLocation(), Sound.ANVIL_LAND, 1.0f, 0.0f);
                    e.getWorld().playSound(player.getLocation(), Sound.AMBIENCE_THUNDER, 1.0f, 0.35f);
                    for (final Entity entities : sword.getWorld().getNearbyEntities(sword.getLocation().add(sword.getLocation().getDirection().multiply(3)), 4.0, 4.0, 4.0)) {
                        if (entities.hasMetadata("NPC")) {
                            continue;
                        }
                        if (entities instanceof ArmorStand) {
                            continue;
                        }
                        if (entities instanceof org.bukkit.entity.Giant) {
                            continue;
                        }
                        if (!(entities instanceof Player)) {
                            continue;
                        }
                        if (entities.getLocation().add(sword.getLocation().getDirection().multiply(3)).distance(sword.getLocation()) > 1.0) {
                            final Player p = (Player)entities;
                            final double damage = SUtil.random(100, 300) + p.getMaxHealth() * 35.0 / 100.0;
                            User.getUser(p.getUniqueId()).damage(damage, EntityDamageEvent.DamageCause.ENTITY_ATTACK, (Entity)e);
                            ((LivingEntity)p).damage(1.0E-6, (Entity)null);
                            p.sendMessage(Sputnik.trans("&7Terrorant's &d&lSword Slam&7 have hit you for &c" + SUtil.commaify(Math.round(damage)) + " &7true damage."));
                        }
                        else {
                            final Player p = (Player)entities;
                            final double damage = SUtil.random(100, 300) + p.getMaxHealth() * 90.0 / 100.0;
                            User.getUser(p.getUniqueId()).damage(damage, EntityDamageEvent.DamageCause.ENTITY_ATTACK, (Entity)e);
                            ((LivingEntity)p).damage(1.0E-6, (Entity)null);
                            p.sendMessage(Sputnik.trans("&7Terrorant's &d&lSword Slam&7 have hit you for &c" + SUtil.commaify(Math.round(damage)) + " &7true damage."));
                        }
                    }
                    SUtil.delay(() -> sword.remove(), 65L);
                    SUtil.delay(() -> stand.remove(), 65L);
                    SUtil.delay(() -> {
                        final Object val$e = e;
                        e.getEquipment().setItemInHand(SUtil.enchant(new ItemStack(Material.DIAMOND_SWORD)));
                    }, 60L);
                }
            }
        }.runTaskTimer((Plugin)SkySimEngine.getPlugin(), 0L, 1L);
    }
}
