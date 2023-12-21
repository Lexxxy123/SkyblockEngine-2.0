package in.godspunky.skyblock.entity.nms;

import com.google.common.util.concurrent.AtomicDouble;
import in.godspunky.skyblock.enchantment.EnchantmentType;
import in.godspunky.skyblock.entity.*;
import net.minecraft.server.v1_8_R3.World;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Material;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEnderman;
import org.bukkit.entity.Entity;
import org.bukkit.entity.*;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;
import in.godspunky.skyblock.Repeater;
import in.godspunky.skyblock.SkySimEngine;
import in.godspunky.skyblock.entity.*;
import in.godspunky.skyblock.entity.end.EndermanStatistics;
import in.godspunky.skyblock.item.SItem;
import in.godspunky.skyblock.item.SMaterial;
import in.godspunky.skyblock.user.User;
import in.godspunky.skyblock.util.BlockFallAPI;
import in.godspunky.skyblock.util.EntityManager;
import in.godspunky.skyblock.util.SUtil;
import in.godspunky.skyblock.util.Sputnik;

import java.util.*;

public class VoidgloomSeraph extends EntityEnderman implements SNMSEntity, EntityFunction, SlayerBoss, EndermanStatistics {
    private static final TieredValue<Double> MAX_HEALTH_VALUES;
    private static final TieredValue<Double> DAMAGE_VALUES;
    private static final TieredValue<Double> SPEED_VALUES;
    private static final TieredValue<Double> XP_DROPS;
    public static final Map<Entity, Player> BEACON_THROW;
    public static final Map<Entity, Integer> HIT_SHIELD;
    public static final Map<Entity, Integer> HIT_SHIELD_MAX;
    public static final Map<UUID, Entity> OWNER_BOSS;
    public static final Map<Entity, Player> NUKEKUBI_TARGET;
    public static final Map<Entity, Integer> NUKEKUBI_DAMAGE;
    private final int tier;
    private final long end;
    private boolean CooldownSkill;
    private boolean CooldownSkill2;
    private boolean CooldownSkill3;
    private boolean CooldownSkill4;
    private boolean activeskull;
    private boolean activebea;
    private boolean activehs;
    private boolean CooldownSkill5;
    private boolean HeartRadi;
    private boolean isBroken;
    private SEntity hologram;
    private SEntity hologram_name;
    private final ArrayList<Entity> Ar1;
    public static final Map<UUID, List<Entity>> LivingSkulls;
    private final ArrayList<Entity> Ar2;
    private final UUID spawnerUUID;
    static UUID spawnerUUID2;
    public static final Map<Entity, Block> CACHED_BLOCK;
    public static final Map<Entity, Integer> CACHED_BLOCK_ID;
    public static final Map<Entity, Byte> CACHED_BLOCK_DATA;

    public VoidgloomSeraph(final Integer tier, final UUID spawnerUUID) {
        super(((CraftWorld) Bukkit.getPlayer(spawnerUUID).getWorld()).getHandle());
        this.Ar1 = new ArrayList<Entity>();
        this.Ar2 = new ArrayList<Entity>();
        this.setSize(0.6f, 2.9f);
        this.S = 1.0f;
        this.goalSelector.a(0, new PathfinderGoalFloat(this));
        this.goalSelector.a(7, new PathfinderGoalRandomStroll(this, 1.0));
        this.goalSelector.a(8, new PathfinderGoalRandomLookaround(this));
        this.tier = tier;
        this.end = System.currentTimeMillis() + 240000L;
        this.spawnerUUID = spawnerUUID;
        this.activehs = false;
        this.activebea = false;
        this.HeartRadi = false;
        this.isBroken = false;
        this.CooldownSkill = true;
        this.CooldownSkill2 = true;
        this.CooldownSkill3 = true;
        this.CooldownSkill4 = true;
        this.CooldownSkill5 = true;
        this.activeskull = false;
        VoidgloomSeraph.spawnerUUID2 = spawnerUUID;
    }

    public VoidgloomSeraph(final World world) {
        super(world);
        this.Ar1 = new ArrayList<Entity>();
        this.Ar2 = new ArrayList<Entity>();
        this.setSize(0.6f, 2.9f);
        this.S = 1.0f;
        this.goalSelector.a(0, new PathfinderGoalFloat(this));
        this.goalSelector.a(7, new PathfinderGoalRandomStroll(this, 1.0));
        this.goalSelector.a(8, new PathfinderGoalRandomLookaround(this));
        this.tier = 1;
        this.activebea = false;
        this.activehs = false;
        this.HeartRadi = false;
        this.isBroken = false;
        this.end = System.currentTimeMillis() + 240000L;
        this.spawnerUUID = UUID.randomUUID();
        this.CooldownSkill = true;
        this.CooldownSkill2 = true;
        this.CooldownSkill3 = true;
        this.CooldownSkill4 = true;
        this.CooldownSkill5 = true;
        this.activeskull = false;
        VoidgloomSeraph.spawnerUUID2 = this.spawnerUUID;
    }

    public static Player getPlayer() {
        return Bukkit.getPlayer(VoidgloomSeraph.spawnerUUID2);
    }

    public void t_() {
        super.t_();
        ((CraftEnderman) this.bukkitEntity).getHandle();
        final Player player = Bukkit.getPlayer(this.spawnerUUID);
        if (player == null) {
            return;
        }
        VoidgloomSeraph.OWNER_BOSS.put(player.getUniqueId(), this.bukkitEntity);
        if (this.bukkitEntity.getWorld() == player.getWorld() && this.getBukkitEntity().getLocation().distance(player.getLocation()) >= 50.0 && SUtil.random(0, 10) == 0 && !this.HeartRadi) {
            this.getBukkitEntity().teleport(player.getLocation());
            this.getBukkitEntity().getWorld().spigot().playEffect(this.getBukkitEntity().getLocation(), Effect.SMOKE, 0, 1, (float) SUtil.random(-1.0, 1.0), 2.0f, (float) SUtil.random(-1.0, 1.0), 0.0f, 1, 12);
        }
        if (!this.CooldownSkill5 && !this.HeartRadi && !this.activehs) {
            this.teleportSkill(this.bukkitEntity, player);
            this.CooldownSkill5 = true;
            SUtil.delay(() -> this.CooldownSkill5 = false, SUtil.random(170, 250));
        }
        final Location locPitch = this.bukkitEntity.getLocation();
        locPitch.setPitch(0.0f);
        if (!this.HeartRadi) {
            this.bukkitEntity.teleport(locPitch);
        }
        if (this.bukkitEntity.getLocation().getBlock().getType() != Material.AIR && this.bukkitEntity.getLocation().add(0.0, 1.0, 0.0).getBlock().getType() != Material.AIR) {
            this.bukkitEntity.teleport(player);
        }
        Entity stand;
        if (System.currentTimeMillis() > this.end) {
            stand = Repeater.BEACON_THROW2.get(player.getUniqueId());
            BlockFallAPI.removeBlock(Repeater.BEACON.get(stand), player.getWorld());
            Sputnik.RemoveEntityArray(this.Ar1);
            Sputnik.RemoveEntityArray(this.Ar2);
            if (VoidgloomSeraph.LivingSkulls.containsKey(this.spawnerUUID)) {
                final List<Entity> a = VoidgloomSeraph.LivingSkulls.get(this.spawnerUUID);
                Sputnik.RemoveEntityArray(a);
            }
            if (stand != null) {
                stand.remove();
            }
            User.getUser(player.getUniqueId()).failSlayerQuest();
            ((Enderman) this.bukkitEntity).remove();
            this.hologram.remove();
            return;
        } else {
            stand = null;
        }
        final Entity entity = this.bukkitEntity;
        if (this.tier > 1 && this.activebea && !this.isBroken) {
            final Entity stand2 = Repeater.BEACON_THROW2.get(player.getUniqueId());
            if (stand2 != null) {
                if (stand2.isDead()) {
                    BlockFallAPI.removeBlock(Repeater.BEACON.get(stand2), entity.getWorld());
                    if (VoidgloomSeraph.CACHED_BLOCK.containsKey(stand2) && VoidgloomSeraph.CACHED_BLOCK_ID.containsKey(stand2) && VoidgloomSeraph.CACHED_BLOCK_DATA.containsKey(stand2)) {
                        VoidgloomSeraph.CACHED_BLOCK.get(stand2).getLocation().getBlock().setTypeIdAndData(VoidgloomSeraph.CACHED_BLOCK_ID.get(stand2), VoidgloomSeraph.CACHED_BLOCK_DATA.get(stand2), true);
                    }
                }
                if (stand2.getNearbyEntities(0.5, 0.5, 0.5).contains(player)) {
                    this.isBroken = true;
                    SUtil.delay(() -> {
                        BlockFallAPI.removeBlock(Repeater.BEACON.get(stand), entity.getWorld());
                        stand.remove();
                        entity.getWorld().playSound(stand.getLocation(), Sound.ITEM_BREAK, 1.0f, 0.5f);
                        entity.getWorld().playEffect(stand.getLocation(), Effect.EXPLOSION_LARGE, 1);
                        if (VoidgloomSeraph.CACHED_BLOCK.containsKey(stand) && VoidgloomSeraph.CACHED_BLOCK_ID.containsKey(stand) && VoidgloomSeraph.CACHED_BLOCK_DATA.containsKey(stand)) {
                            VoidgloomSeraph.CACHED_BLOCK.get(stand).getLocation().getBlock().setTypeIdAndData(VoidgloomSeraph.CACHED_BLOCK_ID.get(stand), VoidgloomSeraph.CACHED_BLOCK_DATA.get(stand), true);
                        }
                    }, 4L);
                    return;
                }
            }
        }
        if (this.tier > 1 && !this.CooldownSkill && SUtil.random(0, 100) == 4 && !this.activebea && !this.activehs) {
            Sputnik.endermanCarryBlock((Enderman) this.bukkitEntity, Material.BEACON);
            this.activebea = true;
            VoidgloomSeraph.BEACON_THROW.remove(entity);
            Repeater.BEACON_THROW2.remove(player.getUniqueId());
            new BukkitRunnable() {
                public void run() {
                    if (entity.isDead()) {
                        final Entity stand = Repeater.BEACON_THROW2.get(player.getUniqueId());
                        BlockFallAPI.removeBlock(Repeater.BEACON.get(stand), entity.getWorld());
                        if (stand != null) {
                            stand.remove();
                        }
                        this.cancel();
                        return;
                    }
                    final Location l1 = entity.getLocation();
                    l1.add(entity.getLocation().getDirection().normalize().multiply(1));
                    l1.setYaw((float) SUtil.random(0, 360));
                    final ArmorStand armorStand1 = (ArmorStand) entity.getWorld().spawnEntity(l1.add(0.0, 0.3, 0.0), EntityType.ARMOR_STAND);
                    armorStand1.getEquipment().setHelmet(SItem.of(SMaterial.BEACON).getStack());
                    armorStand1.setGravity(true);
                    armorStand1.setVisible(false);
                    armorStand1.setCustomName(ChatColor.translateAlternateColorCodes('&', ""));
                    final Vector vec = armorStand1.getLocation().getDirection().normalize().multiply(1.25);
                    armorStand1.setMetadata("BeaconSkill", new FixedMetadataValue(SkySimEngine.getPlugin(), true));
                    vec.setY(0.55);
                    armorStand1.setVelocity(vec);
                    Sputnik.endermanCarryBlock((Enderman) entity, Material.AIR);
                    SUtil.delay(() -> {
                        final Object val$player = player;
                        player.playSound(player.getLocation(), Sound.PORTAL_TRIGGER, 0.4f, 0.81f);
                    }, 10L);
                    SUtil.delay(() -> {
                        final Object val$player2 = player;
                        player.playSound(player.getLocation(), "mob.guardian.elder.idle", 0.2f, 0.85f);
                    }, 15L);
                    SUtil.delay(() -> {
                        final Object val$player3 = player;
                        player.playSound(player.getLocation(), "mob.guardian.elder.idle", 0.2f, 0.85f);
                    }, 40L);
                    SUtil.delay(() -> {
                        final Object val$player4 = player;
                        player.playSound(player.getLocation(), "mob.guardian.elder.idle", 0.2f, 0.85f);
                    }, 40L);
                    VoidgloomSeraph.this.Ar1.add(armorStand1);
                    Repeater.BEACON_OWNER.remove(armorStand1);
                    Repeater.BEACON_OWNER.put(armorStand1, player);
                    VoidgloomSeraph.a(armorStand1);
                }
            }.runTaskLater(SkySimEngine.getPlugin(), 80L);
            new BukkitRunnable() {
                public void run() {
                    if (entity.isDead()) {
                        final Entity stand = Repeater.BEACON_THROW2.get(player.getUniqueId());
                        BlockFallAPI.removeBlock(Repeater.BEACON.get(stand), entity.getWorld());
                        if (VoidgloomSeraph.CACHED_BLOCK.containsKey(stand) && VoidgloomSeraph.CACHED_BLOCK_ID.containsKey(stand) && VoidgloomSeraph.CACHED_BLOCK_DATA.containsKey(stand)) {
                            VoidgloomSeraph.CACHED_BLOCK.get(stand).getLocation().getBlock().setTypeIdAndData(VoidgloomSeraph.CACHED_BLOCK_ID.get(stand), VoidgloomSeraph.CACHED_BLOCK_DATA.get(stand), true);
                        }
                        if (stand != null) {
                            stand.remove();
                        }
                        this.cancel();
                        return;
                    }
                    final Entity stand = Repeater.BEACON_THROW2.get(player.getUniqueId());
                    VoidgloomSeraph.this.Ar1.add(stand);
                    if (!VoidgloomSeraph.this.isBroken) {
                        User.getUser(player.getUniqueId()).damage(player.getMaxHealth() * 3.0, EntityDamageEvent.DamageCause.ENTITY_ATTACK, entity);
                        player.damage(0.1, entity);
                        BlockFallAPI.removeBlock(Repeater.BEACON.get(stand), entity.getWorld());
                        if (stand != null) {
                            stand.remove();
                        }
                        VoidgloomSeraph.this.activebea = false;
                        entity.getWorld().playSound(stand.getLocation(), Sound.ITEM_BREAK, 1.0f, 0.5f);
                        entity.getWorld().playEffect(stand.getLocation(), Effect.EXPLOSION_LARGE, 1);
                        if (VoidgloomSeraph.CACHED_BLOCK.containsKey(stand) && VoidgloomSeraph.CACHED_BLOCK_ID.containsKey(stand) && VoidgloomSeraph.CACHED_BLOCK_DATA.containsKey(stand)) {
                            VoidgloomSeraph.CACHED_BLOCK.get(stand).getLocation().getBlock().setTypeIdAndData(VoidgloomSeraph.CACHED_BLOCK_ID.get(stand), VoidgloomSeraph.CACHED_BLOCK_DATA.get(stand), true);
                        }
                    } else {
                        VoidgloomSeraph.this.isBroken = false;
                        BlockFallAPI.removeBlock(Repeater.BEACON.get(stand), entity.getWorld());
                        if (VoidgloomSeraph.CACHED_BLOCK.containsKey(stand) && VoidgloomSeraph.CACHED_BLOCK_ID.containsKey(stand) && VoidgloomSeraph.CACHED_BLOCK_DATA.containsKey(stand)) {
                            VoidgloomSeraph.CACHED_BLOCK.get(stand).getLocation().getBlock().setTypeIdAndData(VoidgloomSeraph.CACHED_BLOCK_ID.get(stand), VoidgloomSeraph.CACHED_BLOCK_DATA.get(stand), true);
                        }
                        stand.remove();
                        VoidgloomSeraph.this.activebea = false;
                    }
                    VoidgloomSeraph.this.activebea = false;
                }
            }.runTaskLater(SkySimEngine.getPlugin(), 185L);
            new BukkitRunnable() {
                public void run() {
                    if (VoidgloomSeraph.this.CooldownSkill) {
                        VoidgloomSeraph.this.CooldownSkill = false;
                    }
                }
            }.runTaskLater(SkySimEngine.getPlugin(), 400L);
        }
        int amounths = 30;
        if (this.tier == 2) {
            amounths = 60;
        }
        if (this.tier == 3) {
            amounths = 120;
        }
        if (this.tier >= 4) {
            amounths = 200;
        }
        if (this.tier >= 1 && !this.CooldownSkill2 && SUtil.random(0, 100) == 2 && !this.activehs && !this.HeartRadi && !this.activebea) {
            this.CooldownSkill2 = true;
            this.activehs = true;
            VoidgloomSeraph.HIT_SHIELD.put(entity, amounths);
            VoidgloomSeraph.HIT_SHIELD_MAX.put(entity, amounths);
            EntityManager.DEFENSE_PERCENTAGE.put(entity, 100);
        }
        if (!VoidgloomSeraph.HIT_SHIELD.containsKey(entity) && this.activehs) {
            VoidgloomSeraph.HIT_SHIELD.put(entity, amounths);
            VoidgloomSeraph.HIT_SHIELD_MAX.put(entity, amounths);
        }
        if (this.activehs && VoidgloomSeraph.HIT_SHIELD.get(entity) <= 0 && !this.HeartRadi && !this.activebea) {
            this.CooldownSkill2 = true;
            VoidgloomSeraph.HIT_SHIELD.remove(entity);
            VoidgloomSeraph.HIT_SHIELD_MAX.remove(entity);
            player.playSound(player.getLocation(), Sound.ZOMBIE_REMEDY, 1.0f, 1.0f);
            EntityManager.DEFENSE_PERCENTAGE.put(entity, 75);
            this.activehs = false;
            new BukkitRunnable() {
                public void run() {
                    if (VoidgloomSeraph.this.CooldownSkill2) {
                        VoidgloomSeraph.this.CooldownSkill2 = false;
                    }
                }
            }.runTaskLater(SkySimEngine.getPlugin(), 850L);
        }
        if (this.bukkitEntity.getWorld() == player.getWorld() && this.getBukkitEntity().getLocation().distance(player.getLocation()) > 20.0 && this.HeartRadi) {
            User.getUser(player.getUniqueId()).damage(player.getMaxHealth() * 90.0 / 100.0, EntityDamageEvent.DamageCause.ENTITY_ATTACK, entity);
            player.damage(1.0E-4, entity);
        }
        final net.minecraft.server.v1_8_R3.Entity entity_ = this.getBukkitEntity().getHandle();
        final double height = entity_.getBoundingBox().e - entity_.getBoundingBox().b;
        this.hologram_name.getEntity().teleport(this.getBukkitEntity().getLocation().clone().add(0.0, height, 0.0));
        this.hologram_name.getEntity().setCustomName(Sputnik.trans(Sputnik.entityNameTag((LivingEntity) this.getBukkitEntity(), Sputnik.buildcustomString(this.getEntityName(), 0, true))));
        this.hologram.getEntity().teleport(this.getBukkitEntity().getLocation().clone().add(0.0, 3.2, 0.0));
        this.hologram.getEntity().setCustomName(ChatColor.RED + SUtil.getFormattedTime(this.end - System.currentTimeMillis(), 1000));
        ((Enderman) this.getBukkitEntity()).setTarget(player);
        if (this.tier >= 4 && !this.CooldownSkill3 && SUtil.random(0, 100) <= 10 && !this.HeartRadi && !this.activehs) {
            EntityManager.DEFENSE_PERCENTAGE.put(entity, 100);
            this.HeartRadi = true;
            this.CooldownSkill3 = true;
            final double x = entity.getLocation().getX();
            final double y = entity.getLocation().getY();
            final double z = entity.getLocation().getZ();
            final Location l = new Location(entity.getWorld(), x, y, z);
            entity.getWorld().playEffect(entity.getLocation(), Effect.EXPLOSION_HUGE, 3);
            entity.getWorld().playEffect(entity.getLocation(), Effect.EXPLOSION_HUGE, 3);
            final ArmorStand stand_sit = (ArmorStand) entity.getWorld().spawn(l, (Class) ArmorStand.class);
            stand_sit.setVisible(false);
            stand_sit.setGravity(true);
            stand_sit.setMarker(true);
            stand_sit.setPassenger(entity);
            this.Ar1.add(stand_sit);
            this.Ar2.add(stand_sit);
            new BukkitRunnable() {
                public void run() {
                    if (stand_sit.isDead()) {
                        this.cancel();
                        return;
                    }
                    stand_sit.setPassenger(entity);
                }
            }.runTaskTimer(SkySimEngine.getPlugin(), 0L, 1L);
            final ArmorStand stand3 = (ArmorStand) stand_sit.getWorld().spawn(stand_sit.getLocation().add(0.0, 0.0, 0.0), (Class) ArmorStand.class);
            stand3.setVisible(false);
            stand3.setGravity(false);
            stand3.setMarker(true);
            stand3.setMetadata("HeartRadiAS", new FixedMetadataValue(SkySimEngine.getPlugin(), true));
            this.Ar1.add(stand3);
            this.Ar2.add(stand3);
            final ArmorStand stand4 = (ArmorStand) entity.getWorld().spawn(stand3.getLocation().add(0.0, 1.5, 0.0), (Class) ArmorStand.class);
            stand4.setVisible(false);
            stand4.setGravity(false);
            stand4.setMarker(true);
            this.Ar1.add(stand4);
            this.Ar2.add(stand4);
            stand4.setMetadata("HeartRadiAS", new FixedMetadataValue(SkySimEngine.getPlugin(), true));
            final ArmorStand stand5 = (ArmorStand) entity.getWorld().spawn(stand4.getLocation().add(0.0, 1.5, 0.0), (Class) ArmorStand.class);
            stand5.setVisible(false);
            stand5.setGravity(false);
            stand5.setMarker(true);
            this.Ar1.add(stand5);
            this.Ar2.add(stand5);
            stand5.setMetadata("HeartRadiAS", new FixedMetadataValue(SkySimEngine.getPlugin(), true));
            final Location l2 = l;
            l2.setYaw(l.getYaw() + 90.0f);
            final ArmorStand stand1_ = (ArmorStand) stand_sit.getWorld().spawn(l2, (Class) ArmorStand.class);
            stand1_.setVisible(false);
            stand1_.setGravity(false);
            stand1_.setMarker(true);
            this.Ar1.add(stand1_);
            this.Ar2.add(stand1_);
            stand4.setMetadata("HeartRadiAS", new FixedMetadataValue(SkySimEngine.getPlugin(), true));
            final ArmorStand stand6 = (ArmorStand) entity.getWorld().spawn(stand1_.getLocation().add(0.0, 1.5, 0.0), (Class) ArmorStand.class);
            stand6.setVisible(false);
            stand6.setGravity(false);
            stand6.setMarker(true);
            this.Ar1.add(stand6);
            this.Ar2.add(stand6);
            stand6.setMetadata("HeartRadiAS", new FixedMetadataValue(SkySimEngine.getPlugin(), true));
            final ArmorStand stand7 = (ArmorStand) entity.getWorld().spawn(stand6.getLocation().add(0.0, 1.5, 0.0), (Class) ArmorStand.class);
            stand7.setVisible(false);
            stand7.setGravity(false);
            stand7.setMarker(true);
            this.Ar1.add(stand7);
            this.Ar2.add(stand7);
            new BukkitRunnable() {
                public void run() {
                    player.playSound(player.getLocation(), Sound.FIREWORK_LARGE_BLAST, 1.0f, 0.5f);
                    Sputnik.playFuckingSoundOfVoidgloomThatTookForeverToMake(player, stand_sit);
                }
            }.runTaskLater(SkySimEngine.getPlugin(), 15L);
            new BukkitRunnable() {
                public void run() {
                    if (entity.isDead()) {
                        Sputnik.RemoveEntityArray(VoidgloomSeraph.this.Ar2);
                        this.cancel();
                        return;
                    }
                    Sputnik.entityBeam(stand3, stand3.getLocation(), player, entity);
                    Sputnik.entityBeam(stand7, stand3.getLocation(), player, entity);
                    Sputnik.entityBeam(stand6, stand3.getLocation(), player, entity);
                    Sputnik.entityBeam(stand1_, stand3.getLocation(), player, entity);
                    Sputnik.entityBeam(stand5, stand3.getLocation(), player, entity);
                    Sputnik.entityBeam(stand4, stand3.getLocation(), player, entity);
                    new BukkitRunnable() {
                        public void run() {
                            if (stand7.isDead()) {
                                this.cancel();
                                return;
                            }
                            if (entity.isDead()) {
                                Sputnik.IsInsideTheBeam.remove(player.getUniqueId());
                                this.cancel();
                                return;
                            }
                            if (Sputnik.IsInsideTheBeam.containsKey(player.getUniqueId()) && Sputnik.IsInsideTheBeam.get(player.getUniqueId())) {
                                final Entity entity = VoidgloomSeraph.OWNER_BOSS.get(player.getUniqueId());
                                if (entity == null) {
                                    this.cancel();
                                    return;
                                }
                                User.getUser(player.getUniqueId()).damage(player.getMaxHealth() * 25.0 / 100.0, EntityDamageEvent.DamageCause.ENTITY_ATTACK, entity);
                                Sputnik.IsInsideTheBeam.remove(player.getUniqueId());
                            }
                        }
                    }.runTaskTimer(SkySimEngine.getPlugin(), 0L, 3L);
                }
            }.runTaskLater(SkySimEngine.getPlugin(), 25L);
            new BukkitRunnable() {
                public void run() {
                    VoidgloomSeraph.this.HeartRadi = false;
                    Sputnik.RemoveEntityArray(VoidgloomSeraph.this.Ar2);
                    EntityManager.DEFENSE_PERCENTAGE.put(entity, 75);
                    new BukkitRunnable() {
                        public void run() {
                            if (VoidgloomSeraph.this.CooldownSkill3) {
                                VoidgloomSeraph.this.CooldownSkill3 = false;
                            }
                        }
                    }.runTaskLater(SkySimEngine.getPlugin(), 700L);
                }
            }.runTaskLater(SkySimEngine.getPlugin(), 200L);
        }
        if (this.activeskull) {
            int damagefin = 800;
            if (this.tier >= 4) {
                damagefin = 2000;
            }
            if (VoidgloomSeraph.LivingSkulls.containsKey(player.getUniqueId()) && this.activeskull) {
                damageUpdate(damagefin * (2 * (VoidgloomSeraph.LivingSkulls.get(player.getUniqueId()).size() + 1)));
            }
        }
        if (VoidgloomSeraph.LivingSkulls.containsKey(player.getUniqueId()) && this.activeskull) {
            this.activeskull = VoidgloomSeraph.LivingSkulls.get(player.getUniqueId()).size() > 0;
        }
        if (this.tier >= 3 && !this.CooldownSkill4 && SUtil.random(0, 100) == 2 && !this.activehs && !this.activeskull) {
            VoidgloomSeraph.LivingSkulls.remove(player.getUniqueId());
            this.CooldownSkill4 = true;
            this.activeskull = true;
            spawnNukekubi(entity, player, 1, 3);
            new BukkitRunnable() {
                public void run() {
                    if (entity.isDead()) {
                        if (VoidgloomSeraph.LivingSkulls.containsKey(player.getUniqueId())) {
                            Sputnik.RemoveEntityArray(VoidgloomSeraph.LivingSkulls.get(player.getUniqueId()));
                        }
                        this.cancel();
                        return;
                    }
                    if (VoidgloomSeraph.this.activeskull) {
                        VoidgloomSeraph.spawnNukekubi(entity, player, 1, 2);
                    } else {
                        VoidgloomSeraph.updateSkill(VoidgloomSeraph.LivingSkulls.get(player.getUniqueId()));
                        new BukkitRunnable() {
                            public void run() {
                                if (VoidgloomSeraph.this.CooldownSkill4) {
                                    VoidgloomSeraph.this.CooldownSkill4 = false;
                                }
                            }
                        }.runTaskLater(SkySimEngine.getPlugin(), 400L);
                    }
                }
            }.runTaskLater(SkySimEngine.getPlugin(), 300L);
        }
    }

    public void onSpawn(final LivingEntity entity, final SEntity sEntity) {
        entity.setMetadata("NoAffect", new FixedMetadataValue(SkySimEngine.getPlugin(), true));
        entity.setMetadata("Voidgloom", new FixedMetadataValue(SkySimEngine.getPlugin(), true));
        entity.setMetadata("BOSS_OWNER_" + Bukkit.getPlayer(this.getSpawnerUUID()).getUniqueId().toString(), new FixedMetadataValue(SkySimEngine.getPlugin(), true));
        entity.setMetadata("GiantSword", new FixedMetadataValue(SkySimEngine.getPlugin(), true));
        entity.setMetadata("SlayerBoss", new FixedMetadataValue(SkySimEngine.getPlugin(), true));
        VoidgloomSeraph.LivingSkulls.put(this.spawnerUUID, new ArrayList<Entity>());
        SUtil.delay(() -> entity.removeMetadata("GiantSword", SkySimEngine.getPlugin()), 20L);
        SUtil.delay(() -> {
            int amounths = 30;
            if (this.tier == 2) {
                amounths = 60;
            }
            if (this.tier == 3) {
                amounths = 120;
            }
            if (this.tier >= 4) {
                amounths = 200;
            }
            this.CooldownSkill2 = true;
            this.activehs = true;
            VoidgloomSeraph.HIT_SHIELD.put(entity, amounths);
            VoidgloomSeraph.HIT_SHIELD_MAX.put(entity, amounths);
            EntityManager.DEFENSE_PERCENTAGE.put(entity, 100);
        }, 0L);
        SUtil.delay(() -> this.CooldownSkill5 = false, 70L);
        this.playBossParticle_1(entity);
        this.playBossParticle_2(entity);
        this.playShieldParticle(entity);
        SUtil.delay(() -> this.CooldownSkill = false, 150L);
        this.CooldownSkill2 = false;
        SUtil.delay(() -> this.CooldownSkill3 = false, 100L);
        SUtil.delay(() -> this.CooldownSkill4 = false, 300L);
        final net.minecraft.server.v1_8_R3.Entity entity_ = this.getBukkitEntity().getHandle();
        this.hologram = new SEntity(entity.getLocation().add(0.0, 3.2, 0.0), SEntityType.UNCOLLIDABLE_ARMOR_STAND);
        VoidgloomSeraph.BEACON_THROW.put(entity, Bukkit.getPlayer(this.spawnerUUID));
        ((ArmorStand) this.hologram.getEntity()).setVisible(false);
        ((ArmorStand) this.hologram.getEntity()).setGravity(false);
        this.hologram.getEntity().setCustomNameVisible(true);
        entity.setMetadata("notDisplay", new FixedMetadataValue(SkySimEngine.getPlugin(), true));
        final net.minecraft.server.v1_8_R3.Entity e = this.getBukkitEntity().getHandle();
        final double height_ = e.getBoundingBox().e - e.getBoundingBox().b;
        this.hologram_name = new SEntity(entity.getLocation().add(0.0, height_, 0.0), SEntityType.UNCOLLIDABLE_ARMOR_STAND);
        ((ArmorStand) this.hologram_name.getEntity()).setVisible(false);
        ((ArmorStand) this.hologram_name.getEntity()).setGravity(false);
        this.hologram_name.getEntity().setCustomNameVisible(true);
        new BukkitRunnable() {
            public void run() {
                if (entity.isDead()) {
                    VoidgloomSeraph.this.hologram.remove();
                    this.cancel();
                    SUtil.delay(() -> VoidgloomSeraph.this.hologram_name.remove(), 20L);
                    Sputnik.RemoveEntityArray(VoidgloomSeraph.LivingSkulls.get(VoidgloomSeraph.this.getSpawnerUUID()));
                    Repeater.BEACON_THROW2.remove(VoidgloomSeraph.this.getSpawnerUUID());
                    VoidgloomSeraph.OWNER_BOSS.remove(VoidgloomSeraph.this.getSpawnerUUID());
                    if (VoidgloomSeraph.getPlayer() != null) {
                        VoidgloomSeraph.destroyArmorStandWithUUID(VoidgloomSeraph.this.getSpawnerUUID(), VoidgloomSeraph.getPlayer().getWorld());
                    }
                    VoidgloomSeraph.BEACON_THROW.remove(entity);
                    VoidgloomSeraph.HIT_SHIELD.remove(entity);
                    VoidgloomSeraph.HIT_SHIELD_MAX.remove(entity);
                    Repeater.BEACON_OWNER.remove(entity);
                }
            }
        }.runTaskTimer(SkySimEngine.getPlugin(), 0L, 1L);
        new BukkitRunnable() {
            public void run() {
                if (entity.isDead()) {
                    this.cancel();
                    Sputnik.RemoveEntityArray(VoidgloomSeraph.this.Ar2);
                    if (VoidgloomSeraph.LivingSkulls.containsKey(VoidgloomSeraph.this.getSpawnerUUID())) {
                        final List<Entity> a = VoidgloomSeraph.LivingSkulls.get(VoidgloomSeraph.this.getSpawnerUUID());
                        Sputnik.RemoveEntityArray(a);
                    }
                }
            }
        }.runTaskTimer(SkySimEngine.getPlugin(), 0L, 1L);
        new BukkitRunnable() {
            public void run() {
                if (entity.isDead()) {
                    this.cancel();
                    return;
                }
                if (VoidgloomSeraph.getPlayer() == null) {
                    this.cancel();
                    entity.remove();
                    return;
                }
                if (!VoidgloomSeraph.this.HeartRadi) {
                    VoidgloomSeraph.getPlayer().damage(VoidgloomSeraph.DAMAGE_VALUES.getByNumber(VoidgloomSeraph.this.tier), entity);
                }
            }
        }.runTaskTimer(SkySimEngine.getPlugin(), 0L, 20L);
    }

    public void onDamage(final SEntity sEntity, final Entity damager, final EntityDamageByEntityEvent e, final AtomicDouble damage) {
        final Entity en = sEntity.getEntity();
        final Vector v = new Vector(0, 0, 0);
        SUtil.delay(() -> en.setVelocity(v), 1L);
    }

    public void onDeath(final SEntity sEntity, final Entity killed, final Entity damager) {
        this.hologram.remove();
        SUtil.delay(() -> this.hologram_name.remove(), 20L);
        final Entity stand = Repeater.BEACON_THROW2.get(damager.getUniqueId());
        BlockFallAPI.removeBlock(Repeater.BEACON.get(stand), killed.getWorld());
        Sputnik.RemoveEntityArray(this.Ar1);
        Sputnik.RemoveEntityArray(this.Ar2);
        if (VoidgloomSeraph.LivingSkulls.containsKey(this.spawnerUUID)) {
            final List<Entity> a = VoidgloomSeraph.LivingSkulls.get(this.spawnerUUID);
            Sputnik.RemoveEntityArray(a);
        }
        if (stand != null) {
            stand.remove();
        }
    }

    public String getEntityName() {
        return ChatColor.RED + "☠ " + ChatColor.AQUA + "Voidgloom Seraph";
    }

    public double getEntityMaxHealth() {
        return VoidgloomSeraph.MAX_HEALTH_VALUES.getByNumber(this.tier);
    }

    public double getDamageDealt() {
        return VoidgloomSeraph.DAMAGE_VALUES.getByNumber(this.tier);
    }

    public double getMovementSpeed() {
        return VoidgloomSeraph.SPEED_VALUES.getByNumber(this.tier);
    }

    public LivingEntity spawn(final Location location) {
        this.world = ((CraftWorld) location.getWorld()).getHandle();
        this.setPosition(location.getX(), location.getY(), location.getZ());
        this.world.addEntity(this, CreatureSpawnEvent.SpawnReason.CUSTOM);
        return (LivingEntity) this.getBukkitEntity();
    }

    public List<EntityDrop> drops() {
        final List<EntityDrop> drops = new ArrayList<EntityDrop>();
        int revFlesh = SUtil.random(1, 3);
        if (this.tier == 2) {
            revFlesh = SUtil.random(9, 18);
        }
        if (this.tier == 3) {
            revFlesh = SUtil.random(30, 50);
        }
        if (this.tier == 4) {
            revFlesh = SUtil.random(50, 64);
        }
        drops.add(new EntityDrop(SUtil.setStackAmount(SItem.of(SMaterial.NULL_SPHERE).getStack(), revFlesh), EntityDropType.GUARANTEED, 1.0));
        if (this.tier >= 2) {
            drops.add(new EntityDrop(SMaterial.SUMMONING_EYE, EntityDropType.EXTRAORDINARILY_RARE, 0.0033333333333333335, Bukkit.getPlayer(this.getSpawnerUUID())));
        }
        if (this.tier >= 3) {
            drops.add(new EntityDrop(SMaterial.HIDDEN_ETHERWARP_MERGER, EntityDropType.EXTRAORDINARILY_RARE, 5.0E-4, Bukkit.getPlayer(this.getSpawnerUUID())));
        }
        if (this.tier >= 4) {
            final SItem endBook = SItem.of(SMaterial.ENCHANTED_BOOK);
            endBook.addEnchantment(EnchantmentType.ENDER_SLAYER, 15);
            drops.add(new EntityDrop(endBook.getStack(), EntityDropType.CRAZY_RARE, 0.002, Bukkit.getPlayer(this.getSpawnerUUID())));
            final SItem legiBook = SItem.of(SMaterial.ENCHANTED_BOOK);
            legiBook.addEnchantment(EnchantmentType.LEGION, SUtil.random(1, 2));
            drops.add(new EntityDrop(legiBook.getStack(), EntityDropType.INSANE_RARE, 0.00125, Bukkit.getPlayer(this.getSpawnerUUID())));
            final SItem fatalBook = SItem.of(SMaterial.ENCHANTED_BOOK);
            fatalBook.addEnchantment(EnchantmentType.FATAL_TEMPO, 1);
            drops.add(new EntityDrop(fatalBook.getStack(), EntityDropType.CRAZY_RARE, 4.0E-4, Bukkit.getPlayer(this.getSpawnerUUID())));
            drops.add(new EntityDrop(SMaterial.HIDDEN_DEMONS_PEARL, EntityDropType.INSANE_RARE, 2.2222222222222223E-4, Bukkit.getPlayer(this.getSpawnerUUID())));
            drops.add(new EntityDrop(SMaterial.HIDDEN_ETHERWARP_CONDUIT, EntityDropType.CRAZY_RARE, 0.002, Bukkit.getPlayer(this.getSpawnerUUID())));
            drops.add(new EntityDrop(SMaterial.JUDGEMENT_CORE, EntityDropType.CRAZY_RARE, 0.001, Bukkit.getPlayer(this.getSpawnerUUID())));
            drops.add(new EntityDrop(SMaterial.HIDDEN_GYRO_EYE, EntityDropType.CRAZY_RARE, 0.001, Bukkit.getPlayer(this.getSpawnerUUID())));
            drops.add(new EntityDrop(SMaterial.HIDDEN_REFINED_POWDER, EntityDropType.EXTRAORDINARILY_RARE, 0.001, Bukkit.getPlayer(this.getSpawnerUUID())));
        }
        return drops;
    }

    public double getXPDropped() {
        return VoidgloomSeraph.XP_DROPS.getByNumber(this.tier);
    }

    public boolean isBaby() {
        return false;
    }

    public boolean isAngry() {
        return true;
    }

    public UUID getSpawnerUUID() {
        return this.spawnerUUID;
    }

    public static void spawnNukekubi(final Entity e, final Player p, final Integer damage, final Integer spawnCouples) {
        if (spawnCouples <= 2) {
            final Location loc1_ = p.getLocation();
            loc1_.setYaw(loc1_.getYaw() + SUtil.random(0, 360));
            final Location loc2_ = p.getLocation();
            loc2_.setYaw(loc1_.getYaw() + SUtil.random(0, 360));
            final Location loc1 = loc1_.add(loc1_.getDirection().multiply(5));
            final Location loc2 = loc2_.add(loc2_.getDirection().multiply(-5));
            moveHeadAround(spawnHeads(e, loc1, p), p, damage);
            moveHeadAround(spawnHeads(e, loc2, p), p, damage);
        } else {
            for (int i = 0; i < spawnCouples; ++i) {
                if (SUtil.random(1, 2) == 1) {
                    final Location loc1_2 = p.getLocation();
                    loc1_2.setYaw(loc1_2.getYaw() + SUtil.random(0, 360));
                    final Location loc2_2 = p.getLocation();
                    loc2_2.setYaw(loc1_2.getYaw() + SUtil.random(0, 360));
                    final Location loc3 = loc1_2.add(loc1_2.getDirection().multiply(5));
                    final Location loc4 = loc2_2.add(loc2_2.getDirection().multiply(-5));
                    moveHeadAround(spawnHeads(e, loc3, p), p, damage);
                } else {
                    final Location loc1_2 = p.getLocation();
                    loc1_2.setYaw(loc1_2.getYaw() + SUtil.random(0, 360));
                    final Location loc2_2 = p.getLocation();
                    loc2_2.setYaw(loc1_2.getYaw() + SUtil.random(0, 360));
                    final Location loc3 = loc1_2.add(loc1_2.getDirection().multiply(5));
                    final Location loc4 = loc2_2.add(loc2_2.getDirection().multiply(-5));
                    moveHeadAround(spawnHeads(e, loc4, p), p, damage);
                }
            }
        }
    }

    public static void destroyArmorStandWithUUID(final UUID uuid, final org.bukkit.World w) {
        final String uuidString = uuid.toString() + "_NUKEKUBI";
        for (final Entity e : w.getEntities()) {
            if (e.hasMetadata(uuidString)) {
                e.remove();
            }
        }
    }

    public static LivingEntity spawnHeads(final Entity e, final Location loc, final Player p) {
        final ArmorStand entity = (ArmorStand) loc.getWorld().spawnEntity(e.getLocation().add(e.getLocation().getDirection().normalize().multiply(-1)), EntityType.ARMOR_STAND);
        loc.setY(loc.getY() + SUtil.random(0.0, 0.6));
        entity.setCustomName(Sputnik.trans("&c☠ &bVoidgloom Seraph"));
        entity.setVisible(false);
        entity.setGravity(false);
        entity.getEquipment().setHelmet(SItem.of(SMaterial.NUKEKUBI).getStack());
        entity.getEquipment().setItemInHand(new ItemStack(Material.AIR));
        entity.setMetadata("Nukekubi", new FixedMetadataValue(SkySimEngine.getPlugin(), true));
        entity.setMetadata(p.getUniqueId().toString() + "_NUKEKUBI", new FixedMetadataValue(SkySimEngine.getPlugin(), true));
        VoidgloomSeraph.NUKEKUBI_TARGET.put(entity, p);
        if (!VoidgloomSeraph.LivingSkulls.containsKey(p.getUniqueId())) {
            VoidgloomSeraph.LivingSkulls.put(p.getUniqueId(), new ArrayList<Entity>());
        }
        VoidgloomSeraph.LivingSkulls.get(p.getUniqueId()).add(entity);
        moveToLoc(entity, loc, 3, 0, 1.0);
        new BukkitRunnable() {
            public void run() {
                if (entity.isDead()) {
                    this.cancel();
                    return;
                }
                if (entity.hasMetadata("Nukekubi")) {
                    final Location l = entity.getLocation().setDirection(p.getLocation().toVector().subtract(entity.getLocation().toVector()));
                    entity.teleport(l);
                    double x = 0.0;
                    final double y = 0.0;
                    final double z = 0.0;
                    x = Math.toRadians(l.getPitch());
                    entity.setHeadPose(new EulerAngle(x, y, z));
                    entity.getWorld().playEffect(entity.getLocation().add(0.0, 1.1, 0.0), Effect.WITCH_MAGIC, 1);
                    entity.getWorld().playEffect(entity.getLocation().add(0.0, 1.2, 0.0), Effect.WITCH_MAGIC, 1);
                    entity.getWorld().playEffect(entity.getLocation().add(0.0, 1.2, 0.0), Effect.WITCH_MAGIC, 1);
                    entity.getWorld().playEffect(entity.getLocation().add(0.0, 1.4, 0.0), Effect.WITCH_MAGIC, 1);
                    entity.getWorld().playEffect(entity.getLocation().add(0.0, 1.4, 0.0), Effect.WITCH_MAGIC, 1);
                }
            }
        }.runTaskTimer(SkySimEngine.getPlugin(), 0L, 1L);
        return entity;
    }

    public static void moveToLoc(final Entity e, final Location target, final int tickingRad, final int firstTickRad, final double jump) {
        final Location l = e.getLocation().setDirection(target.toVector().subtract(e.getLocation().toVector()));
        new BukkitRunnable() {
            public void run() {
                final Vector teleportTo = l.getDirection().normalize().multiply(jump);
                if (e.isDead()) {
                    this.cancel();
                    return;
                }
                if (e.getWorld().getNearbyEntities(target, 1.5, 1.5, 1.5).contains(e)) {
                    this.cancel();
                    return;
                }
                e.teleport(e.getLocation().add(teleportTo).multiply(jump));
                e.getWorld().spigot().playEffect(e.getLocation().add(0.0, 1.1, 0.0), Effect.FLAME, 0, 1, 1.0f, 1.0f, 1.0f, 0.0f, 0, 64);
                e.getWorld().spigot().playEffect(e.getLocation().add(0.0, 1.0, 0.0), Effect.FLAME, 0, 1, 1.0f, 1.0f, 1.0f, 0.0f, 0, 64);
                e.getWorld().spigot().playEffect(e.getLocation().add(0.0, 1.0, 0.0), Effect.FLAME, 0, 1, 1.0f, 1.0f, 1.0f, 0.0f, 0, 64);
            }
        }.runTaskTimer(SkySimEngine.getPlugin(), firstTickRad, tickingRad);
    }

    public static void moveHeadAround(final Entity head, final Player p, final Integer damage) {
        new BukkitRunnable() {
            public void run() {
                if (head.isDead()) {
                    this.cancel();
                    return;
                }
                int i1 = 0;
                if (VoidgloomSeraph.NUKEKUBI_DAMAGE.containsKey(p)) {
                    i1 = VoidgloomSeraph.NUKEKUBI_DAMAGE.get(p);
                }
                if (head.getWorld().equals(p.getWorld())) {
                    Sputnik.drawLineforMovingPoints(head.getLocation().add(head.getLocation().getDirection().multiply(0.1)).add(0.0, 2.0, 0.0), p.getLocation().add(0.0, 1.8, 0.0), 20.0, p, damage, head);
                    Sputnik.dmgc(i1, p, head);
                } else {
                    head.remove();
                }
            }
        }.runTaskTimer(SkySimEngine.getPlugin(), 0L, 20L);
    }

    public static void damageUpdate(final double formula) {
        VoidgloomSeraph.NUKEKUBI_DAMAGE.put(getPlayer(), (int) formula);
    }

    public static void targetSelect(final Entity entity, final LivingEntity target) {
        if (!(entity instanceof Creature)) {
            return;
        }
        ((Creature) entity).setTarget(target);
    }

    public static void updateSkill(final List<Entity> list) {
        for (final Entity e : list) {
            VoidgloomSeraph.NUKEKUBI_TARGET.remove(e);
        }
    }

    public static BukkitTask a(final ArmorStand entity) {
        return new BukkitRunnable() {
            public void run() {
                if (entity.isDead()) {
                    this.cancel();
                    return;
                }
                final org.bukkit.World world = entity.getWorld();
                if (entity.hasMetadata("BeaconSkill") && entity.isOnGround()) {
                    entity.remove();
                    if (VoidgloomSeraph.getPlayer() != null) {
                        VoidgloomSeraph.getPlayer().getWorld().playSound(VoidgloomSeraph.getPlayer().getLocation(), Sound.ITEM_BREAK, 0.5f, 0.67f);
                    }
                    final Location loc = entity.getLocation().getBlock().getLocation().add(0.5, 0.0, 0.5);
                    final ArmorStand armorStand2 = (ArmorStand) entity.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
                    VoidgloomSeraph.CACHED_BLOCK.put(armorStand2, loc.getBlock());
                    VoidgloomSeraph.CACHED_BLOCK_ID.put(armorStand2, loc.getBlock().getTypeId());
                    VoidgloomSeraph.CACHED_BLOCK_DATA.put(armorStand2, loc.getBlock().getData());
                    armorStand2.setGravity(true);
                    armorStand2.setVisible(false);
                    armorStand2.setCustomName(ChatColor.translateAlternateColorCodes('&', "&c☠ &fTEST"));
                    armorStand2.setMetadata("BeaconSkill2", new FixedMetadataValue(SkySimEngine.getPlugin(), true));
                    loc.getBlock().setType(Material.BARRIER);
                    Repeater.BEACON_THROW2.put(Repeater.BEACON_OWNER.get(entity).getUniqueId(), armorStand2);
                    VoidgloomSeraph.b(armorStand2);
                    Repeater.BEACON.put(armorStand2, BlockFallAPI.sendBlockDestroyWithSignal(loc, Material.BEACON, (byte) 0, world));
                }
            }
        }.runTaskTimer(SkySimEngine.getPlugin(), 0L, 1L);
    }

    public static void b(final Entity armorStand2) {
        new BukkitRunnable() {
            public void run() {
                if (armorStand2.isDead()) {
                    this.cancel();
                    return;
                }
                final org.bukkit.World world = armorStand2.getWorld();
                world.playEffect(armorStand2.getLocation().add(0.0, 1.0, 0.0), Effect.FLYING_GLYPH, 3);
                world.playEffect(armorStand2.getLocation().add(0.0, 1.0, 0.0), Effect.FLYING_GLYPH, 3);
                world.playEffect(armorStand2.getLocation().add(0.0, 1.0, 0.0), Effect.FLYING_GLYPH, 3);
                world.playEffect(armorStand2.getLocation().add(0.0, 1.0, 0.0), Effect.FLYING_GLYPH, 3);
                world.playEffect(armorStand2.getLocation().add(0.0, 1.0, 0.0), Effect.FLYING_GLYPH, 3);
                world.playEffect(armorStand2.getLocation().add(0.0, 1.0, 0.0), Effect.FLYING_GLYPH, 3);
                world.playEffect(armorStand2.getLocation().add(0.0, 1.0, 0.0), Effect.FLYING_GLYPH, 3);
                world.playEffect(armorStand2.getLocation().add(0.0, 1.0, 0.0), Effect.FLYING_GLYPH, 3);
                world.playEffect(armorStand2.getLocation().add(0.0, 1.0, 0.0), Effect.FLYING_GLYPH, 3);
                world.playEffect(armorStand2.getLocation().add(0.0, 1.0, 0.0), Effect.FLYING_GLYPH, 3);
                world.playEffect(armorStand2.getLocation().add(0.0, 1.0, 0.0), Effect.FLYING_GLYPH, 3);
                world.playEffect(armorStand2.getLocation().add(0.0, 1.0, 0.0), Effect.FLYING_GLYPH, 3);
            }
        }.runTaskTimer(SkySimEngine.getPlugin(), 0L, 1L);
    }

    public void teleportSkill(final Entity e, final Player p) {
        final int LOR = SUtil.random(0, 1);
        new BukkitRunnable() {
            int cout = 0;
            float addedYaw = p.getLocation().getYaw();

            public void run() {
                if (this.cout >= 7) {
                    p.getLocation().getWorld().playSound(p.getLocation(), Sound.ENDERDRAGON_HIT, 1.0f, 1.0f);
                    this.cancel();
                    return;
                }
                final Location a = p.getLocation();
                if (LOR == 0) {
                    this.addedYaw += 19.0f;
                } else {
                    this.addedYaw -= 19.0f;
                }
                a.setPitch(0.0f);
                a.setYaw(this.addedYaw);
                a.add(a.getDirection().normalize().multiply(1.7));
                a.setY(e.getLocation().getY());
                final Location tpl = a.clone();
                tpl.setYaw(e.getLocation().getYaw());
                e.teleport(tpl);
                VoidgloomSeraph.this.dP(a);
                a.getWorld().playSound(a, Sound.ENDERMAN_TELEPORT, 0.2f, 1.0f);
                ++this.cout;
            }
        }.runTaskTimer(SkySimEngine.getPlugin(), 0L, 1L);
    }

    public void drawPointerAt(final Location loc) {
        loc.getWorld().spigot().playEffect(loc, Effect.WITCH_MAGIC, 0, 1, 1.0f, 1.0f, 1.0f, 0.0f, 0, 64);
        loc.getWorld().spigot().playEffect(loc, Effect.LARGE_SMOKE, 21, 0, 0.1f, 0.0f, 0.1f, 0.01f, 1, 30);
        loc.getWorld().spigot().playEffect(loc, Effect.LARGE_SMOKE, 21, 0, 0.1f, 0.0f, 0.1f, 0.01f, 1, 30);
        loc.getWorld().spigot().playEffect(loc, Effect.WITCH_MAGIC, 0, 1, 1.0f, 1.0f, 1.0f, 0.0f, 0, 64);
    }

    public void dP(final Location loc) {
        this.drawPointerAt(loc.clone().add(0.0, 0.9, 0.0));
        this.drawPointerAt(loc.clone().add(0.0, 1.5, 0.0));
    }

    public void playShieldParticle(final Entity e) {
        new BukkitRunnable() {
            float cout = e.getLocation().getYaw();

            public void run() {
                if (e.isDead()) {
                    this.cancel();
                    return;
                }
                final Location loc = e.getLocation();
                loc.setYaw(this.cout);
                loc.setPitch(0.0f);
                loc.add(loc.getDirection().normalize().multiply(0.8));
                if (VoidgloomSeraph.HIT_SHIELD.containsKey(e)) {
                    final int hitshield = VoidgloomSeraph.HIT_SHIELD.get(e);
                    final int hitshieldmax = VoidgloomSeraph.HIT_SHIELD_MAX.get(e);
                    int stage = 3;
                    if (hitshield <= hitshieldmax / 2 && hitshield > hitshieldmax * 25 / 100) {
                        stage = 2;
                    } else if (hitshield <= hitshieldmax * 25 / 100 && hitshield != 1) {
                        stage = 1;
                    } else if (hitshield == 1) {
                        stage = 1;
                    }
                    if (VoidgloomSeraph.HIT_SHIELD.get(e) > 0) {
                        e.getWorld().spigot().playEffect(loc, Effect.WITCH_MAGIC, 0, 1, 1.0f, 1.0f, 1.0f, 0.0f, 0, 64);
                        e.getWorld().spigot().playEffect(loc.clone().add(0.0, 0.6, 0.0), Effect.WITCH_MAGIC, 0, 1, 1.0f, 1.0f, 1.0f, 0.0f, 0, 64);
                        e.getWorld().spigot().playEffect(loc, Effect.WITCH_MAGIC, 0, 1, 1.0f, 1.0f, 1.0f, 0.0f, 0, 64);
                        e.getWorld().spigot().playEffect(loc.clone().add(0.0, 0.6, 0.0), Effect.WITCH_MAGIC, 0, 1, 1.0f, 1.0f, 1.0f, 0.0f, 0, 64);
                        if (stage >= 2) {
                            e.getWorld().spigot().playEffect(loc.clone().add(0.0, 1.2, 0.0), Effect.WITCH_MAGIC, 0, 1, 1.0f, 1.0f, 1.0f, 0.0f, 0, 64);
                            e.getWorld().spigot().playEffect(loc.clone().add(0.0, 1.2, 0.0), Effect.WITCH_MAGIC, 0, 1, 1.0f, 1.0f, 1.0f, 0.0f, 0, 64);
                        }
                        if (stage == 3) {
                            e.getWorld().spigot().playEffect(loc.clone().add(0.0, 2.4, 0.0), Effect.WITCH_MAGIC, 0, 1, 1.0f, 1.0f, 1.0f, 0.0f, 0, 64);
                            e.getWorld().spigot().playEffect(loc.clone().add(0.0, 2.4, 0.0), Effect.WITCH_MAGIC, 0, 1, 1.0f, 1.0f, 1.0f, 0.0f, 0, 64);
                            e.getWorld().spigot().playEffect(loc.clone().add(0.0, 1.8, 0.0), Effect.WITCH_MAGIC, 0, 1, 1.0f, 1.0f, 1.0f, 0.0f, 0, 64);
                            e.getWorld().spigot().playEffect(loc.clone().add(0.0, 1.8, 0.0), Effect.WITCH_MAGIC, 0, 1, 1.0f, 1.0f, 1.0f, 0.0f, 0, 64);
                        }
                    }
                }
                this.cout += 18.0f;
            }
        }.runTaskTimer(SkySimEngine.getPlugin(), 0L, 1L);
    }

    public void playBossParticle_1(final Entity e) {
        new BukkitRunnable() {
            float cout = e.getLocation().getYaw();

            public void run() {
                if (e.isDead()) {
                    this.cancel();
                    return;
                }
                final Location loc = e.getLocation().clone().add(0.0, 0.3, 0.0);
                loc.setYaw(this.cout);
                loc.setPitch(0.0f);
                loc.add(loc.getDirection().normalize().multiply(0.4));
                e.getWorld().spigot().playEffect(loc, Effect.CRIT, 0, 1, 1.0f, 1.0f, 1.0f, 0.0f, 0, 64);
                e.getWorld().spigot().playEffect(loc, Effect.CRIT, 0, 1, 1.0f, 1.0f, 1.0f, 0.0f, 0, 64);
                this.cout += 9.0f;
            }
        }.runTaskTimer(SkySimEngine.getPlugin(), 0L, 1L);
    }

    public void playBossParticle_2(final Entity e) {
        new BukkitRunnable() {
            float cout = e.getLocation().getYaw();

            public void run() {
                if (e.isDead()) {
                    this.cancel();
                    return;
                }
                final Location loc = e.getLocation().clone().add(0.0, 0.3, 0.0);
                loc.setYaw(this.cout);
                loc.setPitch(0.0f);
                loc.add(loc.getDirection().normalize().multiply(0.4));
                e.getWorld().spigot().playEffect(loc.clone().add(0.0, 0.6, 0.0), Effect.CRIT, 0, 1, 1.0f, 1.0f, 1.0f, 0.0f, 0, 64);
                e.getWorld().spigot().playEffect(loc.clone().add(0.0, 0.6, 0.0), Effect.CRIT, 0, 1, 1.0f, 1.0f, 1.0f, 0.0f, 0, 64);
                this.cout -= 9.0f;
            }
        }.runTaskTimer(SkySimEngine.getPlugin(), 0L, 1L);
    }

    public int getTier() {
        return this.tier;
    }

    static {
        MAX_HEALTH_VALUES = new TieredValue<Double>(3.0E7, 1.5E8, 6.66E8, 1.0E9);
        DAMAGE_VALUES = new TieredValue<Double>(120000.0, 1000000.0, 1200000.0, 1500000.0);
        SPEED_VALUES = new TieredValue<Double>(0.2, 0.2, 0.2, 0.2);
        XP_DROPS = new TieredValue<Double>(750.0, 1600.0, 2900.4, 10900.0);
        BEACON_THROW = new HashMap<Entity, Player>();
        HIT_SHIELD = new HashMap<Entity, Integer>();
        HIT_SHIELD_MAX = new HashMap<Entity, Integer>();
        OWNER_BOSS = new HashMap<UUID, Entity>();
        NUKEKUBI_TARGET = new HashMap<Entity, Player>();
        NUKEKUBI_DAMAGE = new HashMap<Entity, Integer>();
        LivingSkulls = new HashMap<UUID, List<Entity>>();
        CACHED_BLOCK = new HashMap<Entity, Block>();
        CACHED_BLOCK_ID = new HashMap<Entity, Integer>();
        CACHED_BLOCK_DATA = new HashMap<Entity, Byte>();
    }
}
