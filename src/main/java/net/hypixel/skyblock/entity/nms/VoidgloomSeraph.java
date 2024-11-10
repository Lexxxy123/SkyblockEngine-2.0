/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraft.server.v1_8_R3.Entity
 *  net.minecraft.server.v1_8_R3.EntityCreature
 *  net.minecraft.server.v1_8_R3.EntityEnderman
 *  net.minecraft.server.v1_8_R3.EntityInsentient
 *  net.minecraft.server.v1_8_R3.PathfinderGoal
 *  net.minecraft.server.v1_8_R3.PathfinderGoalFloat
 *  net.minecraft.server.v1_8_R3.PathfinderGoalRandomLookaround
 *  net.minecraft.server.v1_8_R3.PathfinderGoalRandomStroll
 *  net.minecraft.server.v1_8_R3.World
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Effect
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.Sound
 *  org.bukkit.World
 *  org.bukkit.block.Block
 *  org.bukkit.craftbukkit.v1_8_R3.CraftWorld
 *  org.bukkit.craftbukkit.v1_8_R3.entity.CraftEnderman
 *  org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity
 *  org.bukkit.entity.ArmorStand
 *  org.bukkit.entity.Creature
 *  org.bukkit.entity.Enderman
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.entity.CreatureSpawnEvent$SpawnReason
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.event.entity.EntityDamageEvent$DamageCause
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 *  org.bukkit.scheduler.BukkitTask
 *  org.bukkit.util.EulerAngle
 *  org.bukkit.util.Vector
 */
package net.hypixel.skyblock.entity.nms;

import com.google.common.util.concurrent.AtomicDouble;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.hypixel.skyblock.Repeater;
import net.hypixel.skyblock.SkyBlock;
import net.hypixel.skyblock.api.block.BlockFallAPI;
import net.hypixel.skyblock.entity.EntityDrop;
import net.hypixel.skyblock.entity.EntityDropType;
import net.hypixel.skyblock.entity.EntityFunction;
import net.hypixel.skyblock.entity.SEntity;
import net.hypixel.skyblock.entity.SEntityType;
import net.hypixel.skyblock.entity.end.EndermanStatistics;
import net.hypixel.skyblock.entity.nms.SNMSEntity;
import net.hypixel.skyblock.entity.nms.SlayerBoss;
import net.hypixel.skyblock.entity.nms.TieredValue;
import net.hypixel.skyblock.features.enchantment.EnchantmentType;
import net.hypixel.skyblock.item.SItem;
import net.hypixel.skyblock.item.SMaterial;
import net.hypixel.skyblock.user.User;
import net.hypixel.skyblock.util.EntityManager;
import net.hypixel.skyblock.util.SUtil;
import net.hypixel.skyblock.util.Sputnik;
import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.EntityCreature;
import net.minecraft.server.v1_8_R3.EntityEnderman;
import net.minecraft.server.v1_8_R3.EntityInsentient;
import net.minecraft.server.v1_8_R3.PathfinderGoal;
import net.minecraft.server.v1_8_R3.PathfinderGoalFloat;
import net.minecraft.server.v1_8_R3.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_8_R3.PathfinderGoalRandomStroll;
import net.minecraft.server.v1_8_R3.World;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEnderman;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

public class VoidgloomSeraph
extends EntityEnderman
implements SNMSEntity,
EntityFunction,
SlayerBoss,
EndermanStatistics {
    private static final TieredValue<Double> MAX_HEALTH_VALUES = new TieredValue<Double>(3.0E7, 1.5E8, 6.66E8, 1.0E9);
    private static final TieredValue<Double> DAMAGE_VALUES = new TieredValue<Double>(120000.0, 1000000.0, 1200000.0, 1500000.0);
    private static final TieredValue<Double> SPEED_VALUES = new TieredValue<Double>(0.2, 0.2, 0.2, 0.2);
    private static final TieredValue<Double> XP_DROPS = new TieredValue<Double>(750.0, 1600.0, 2900.4, 10900.0);
    public static final Map<org.bukkit.entity.Entity, Player> BEACON_THROW = new HashMap<org.bukkit.entity.Entity, Player>();
    public static final Map<org.bukkit.entity.Entity, Integer> HIT_SHIELD = new HashMap<org.bukkit.entity.Entity, Integer>();
    public static final Map<org.bukkit.entity.Entity, Integer> HIT_SHIELD_MAX = new HashMap<org.bukkit.entity.Entity, Integer>();
    public static final Map<UUID, org.bukkit.entity.Entity> OWNER_BOSS = new HashMap<UUID, org.bukkit.entity.Entity>();
    public static final Map<org.bukkit.entity.Entity, Player> NUKEKUBI_TARGET = new HashMap<org.bukkit.entity.Entity, Player>();
    public static final Map<org.bukkit.entity.Entity, Integer> NUKEKUBI_DAMAGE = new HashMap<org.bukkit.entity.Entity, Integer>();
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
    private final ArrayList<org.bukkit.entity.Entity> Ar1 = new ArrayList();
    public static final Map<UUID, List<org.bukkit.entity.Entity>> LivingSkulls = new HashMap<UUID, List<org.bukkit.entity.Entity>>();
    private final ArrayList<org.bukkit.entity.Entity> Ar2 = new ArrayList();
    private final UUID spawnerUUID;
    static UUID spawnerUUID2;
    public static final Map<org.bukkit.entity.Entity, Block> CACHED_BLOCK;
    public static final Map<org.bukkit.entity.Entity, Integer> CACHED_BLOCK_ID;
    public static final Map<org.bukkit.entity.Entity, Byte> CACHED_BLOCK_DATA;

    public VoidgloomSeraph(Integer tier, UUID spawnerUUID) {
        super((World)((CraftWorld)Bukkit.getPlayer((UUID)spawnerUUID).getWorld()).getHandle());
        this.setSize(0.6f, 2.9f);
        this.S = 1.0f;
        this.goalSelector.a(0, (PathfinderGoal)new PathfinderGoalFloat((EntityInsentient)this));
        this.goalSelector.a(7, (PathfinderGoal)new PathfinderGoalRandomStroll((EntityCreature)this, 1.0));
        this.goalSelector.a(8, (PathfinderGoal)new PathfinderGoalRandomLookaround((EntityInsentient)this));
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
        spawnerUUID2 = spawnerUUID;
    }

    public VoidgloomSeraph(World world) {
        super(world);
        this.setSize(0.6f, 2.9f);
        this.S = 1.0f;
        this.goalSelector.a(0, (PathfinderGoal)new PathfinderGoalFloat((EntityInsentient)this));
        this.goalSelector.a(7, (PathfinderGoal)new PathfinderGoalRandomStroll((EntityCreature)this, 1.0));
        this.goalSelector.a(8, (PathfinderGoal)new PathfinderGoalRandomLookaround((EntityInsentient)this));
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
        spawnerUUID2 = this.spawnerUUID;
    }

    public static Player getPlayer() {
        return Bukkit.getPlayer((UUID)spawnerUUID2);
    }

    public void t_() {
        org.bukkit.entity.Entity stand2;
        super.t_();
        ((CraftEnderman)this.bukkitEntity).getHandle();
        final Player player = Bukkit.getPlayer((UUID)this.spawnerUUID);
        if (null == player) {
            return;
        }
        OWNER_BOSS.put(player.getUniqueId(), (org.bukkit.entity.Entity)this.bukkitEntity);
        if (this.bukkitEntity.getWorld() == player.getWorld() && 50.0 <= this.getBukkitEntity().getLocation().distance(player.getLocation()) && 0 == SUtil.random(0, 10) && !this.HeartRadi) {
            this.getBukkitEntity().teleport(player.getLocation());
            this.getBukkitEntity().getWorld().spigot().playEffect(this.getBukkitEntity().getLocation(), Effect.SMOKE, 0, 1, (float)SUtil.random(-1.0, 1.0), 2.0f, (float)SUtil.random(-1.0, 1.0), 0.0f, 1, 12);
        }
        if (!(this.CooldownSkill5 || this.HeartRadi || this.activehs)) {
            this.teleportSkill((org.bukkit.entity.Entity)this.bukkitEntity, player);
            this.CooldownSkill5 = true;
            SUtil.delay(() -> {
                this.CooldownSkill5 = false;
            }, SUtil.random(170, 250));
        }
        Location locPitch = this.bukkitEntity.getLocation();
        locPitch.setPitch(0.0f);
        if (!this.HeartRadi) {
            this.bukkitEntity.teleport(locPitch);
        }
        if (Material.AIR != this.bukkitEntity.getLocation().getBlock().getType() && Material.AIR != this.bukkitEntity.getLocation().add(0.0, 1.0, 0.0).getBlock().getType()) {
            this.bukkitEntity.teleport((org.bukkit.entity.Entity)player);
        }
        if (System.currentTimeMillis() > this.end) {
            org.bukkit.entity.Entity stand = Repeater.BEACON_THROW2.get(player.getUniqueId());
            BlockFallAPI.removeBlock(Repeater.BEACON.get(stand), player.getWorld());
            Sputnik.RemoveEntityArray(this.Ar1);
            Sputnik.RemoveEntityArray(this.Ar2);
            if (LivingSkulls.containsKey(this.spawnerUUID)) {
                List<org.bukkit.entity.Entity> a = LivingSkulls.get(this.spawnerUUID);
                Sputnik.RemoveEntityArray(a);
            }
            if (null != stand) {
                stand.remove();
            }
            User.getUser(player.getUniqueId()).failSlayerQuest();
            ((Enderman)this.bukkitEntity).remove();
            this.hologram.remove();
            return;
        }
        org.bukkit.entity.Entity stand = null;
        CraftEntity entity = this.bukkitEntity;
        if (1 < this.tier && this.activebea && !this.isBroken && null != (stand2 = Repeater.BEACON_THROW2.get(player.getUniqueId()))) {
            if (stand2.isDead()) {
                BlockFallAPI.removeBlock(Repeater.BEACON.get(stand2), entity.getWorld());
                if (CACHED_BLOCK.containsKey(stand2) && CACHED_BLOCK_ID.containsKey(stand2) && CACHED_BLOCK_DATA.containsKey(stand2)) {
                    CACHED_BLOCK.get(stand2).getLocation().getBlock().setTypeIdAndData(CACHED_BLOCK_ID.get(stand2).intValue(), CACHED_BLOCK_DATA.get(stand2).byteValue(), true);
                }
            }
            if (stand2.getNearbyEntities(0.5, 0.5, 0.5).contains(player)) {
                this.isBroken = true;
                SUtil.delay(() -> VoidgloomSeraph.lambda$t_$1(stand, (org.bukkit.entity.Entity)entity), 4L);
                return;
            }
        }
        if (!(1 >= this.tier || this.CooldownSkill || 4 != SUtil.random(0, 100) || this.activebea || this.activehs)) {
            Sputnik.endermanCarryBlock((Enderman)this.bukkitEntity, Material.BEACON);
            this.activebea = true;
            BEACON_THROW.remove(entity);
            Repeater.BEACON_THROW2.remove(player.getUniqueId());
            new BukkitRunnable((org.bukkit.entity.Entity)entity, player){
                final /* synthetic */ org.bukkit.entity.Entity val$entity;
                final /* synthetic */ Player val$player;
                {
                    this.val$entity = entity;
                    this.val$player = player;
                }

                public void run() {
                    if (this.val$entity.isDead()) {
                        org.bukkit.entity.Entity stand = Repeater.BEACON_THROW2.get(this.val$player.getUniqueId());
                        BlockFallAPI.removeBlock(Repeater.BEACON.get(stand), this.val$entity.getWorld());
                        if (null != stand) {
                            stand.remove();
                        }
                        this.cancel();
                        return;
                    }
                    Location l1 = this.val$entity.getLocation();
                    l1.add(this.val$entity.getLocation().getDirection().normalize().multiply(1));
                    l1.setYaw((float)SUtil.random(0, 360));
                    ArmorStand armorStand1 = (ArmorStand)this.val$entity.getWorld().spawnEntity(l1.add(0.0, 0.3, 0.0), EntityType.ARMOR_STAND);
                    armorStand1.getEquipment().setHelmet(SItem.of(SMaterial.BEACON).getStack());
                    armorStand1.setGravity(true);
                    armorStand1.setVisible(false);
                    armorStand1.setCustomName(ChatColor.translateAlternateColorCodes((char)'&', (String)""));
                    Vector vec = armorStand1.getLocation().getDirection().normalize().multiply(1.25);
                    armorStand1.setMetadata("BeaconSkill", (MetadataValue)new FixedMetadataValue((Plugin)SkyBlock.getPlugin(), (Object)true));
                    vec.setY(0.55);
                    armorStand1.setVelocity(vec);
                    Sputnik.endermanCarryBlock((Enderman)this.val$entity, Material.AIR);
                    SUtil.delay(() -> this.val$player.playSound(this.val$player.getLocation(), Sound.PORTAL_TRIGGER, 0.4f, 0.81f), 10L);
                    SUtil.delay(() -> this.val$player.playSound(this.val$player.getLocation(), "mob.guardian.elder.idle", 0.2f, 0.85f), 15L);
                    SUtil.delay(() -> this.val$player.playSound(this.val$player.getLocation(), "mob.guardian.elder.idle", 0.2f, 0.85f), 40L);
                    SUtil.delay(() -> this.val$player.playSound(this.val$player.getLocation(), "mob.guardian.elder.idle", 0.2f, 0.85f), 40L);
                    VoidgloomSeraph.this.Ar1.add(armorStand1);
                    Repeater.BEACON_OWNER.remove(armorStand1);
                    Repeater.BEACON_OWNER.put((org.bukkit.entity.Entity)armorStand1, this.val$player);
                    VoidgloomSeraph.a(armorStand1);
                }
            }.runTaskLater((Plugin)SkyBlock.getPlugin(), 80L);
            new BukkitRunnable((org.bukkit.entity.Entity)entity, player){
                final /* synthetic */ org.bukkit.entity.Entity val$entity;
                final /* synthetic */ Player val$player;
                {
                    this.val$entity = entity;
                    this.val$player = player;
                }

                public void run() {
                    if (this.val$entity.isDead()) {
                        org.bukkit.entity.Entity stand = Repeater.BEACON_THROW2.get(this.val$player.getUniqueId());
                        BlockFallAPI.removeBlock(Repeater.BEACON.get(stand), this.val$entity.getWorld());
                        if (CACHED_BLOCK.containsKey(stand) && CACHED_BLOCK_ID.containsKey(stand) && CACHED_BLOCK_DATA.containsKey(stand)) {
                            CACHED_BLOCK.get(stand).getLocation().getBlock().setTypeIdAndData(CACHED_BLOCK_ID.get(stand).intValue(), CACHED_BLOCK_DATA.get(stand).byteValue(), true);
                        }
                        if (null != stand) {
                            stand.remove();
                        }
                        this.cancel();
                        return;
                    }
                    org.bukkit.entity.Entity stand = Repeater.BEACON_THROW2.get(this.val$player.getUniqueId());
                    VoidgloomSeraph.this.Ar1.add(stand);
                    if (!VoidgloomSeraph.this.isBroken) {
                        User.getUser(this.val$player.getUniqueId()).damage(this.val$player.getMaxHealth() * 3.0, EntityDamageEvent.DamageCause.ENTITY_ATTACK, this.val$entity);
                        this.val$player.damage(0.1, this.val$entity);
                        BlockFallAPI.removeBlock(Repeater.BEACON.get(stand), this.val$entity.getWorld());
                        if (null != stand) {
                            stand.remove();
                        }
                        VoidgloomSeraph.this.activebea = false;
                        this.val$entity.getWorld().playSound(stand.getLocation(), Sound.ITEM_BREAK, 1.0f, 0.5f);
                        this.val$entity.getWorld().playEffect(stand.getLocation(), Effect.EXPLOSION_LARGE, 1);
                        if (CACHED_BLOCK.containsKey(stand) && CACHED_BLOCK_ID.containsKey(stand) && CACHED_BLOCK_DATA.containsKey(stand)) {
                            CACHED_BLOCK.get(stand).getLocation().getBlock().setTypeIdAndData(CACHED_BLOCK_ID.get(stand).intValue(), CACHED_BLOCK_DATA.get(stand).byteValue(), true);
                        }
                    } else {
                        VoidgloomSeraph.this.isBroken = false;
                        BlockFallAPI.removeBlock(Repeater.BEACON.get(stand), this.val$entity.getWorld());
                        if (CACHED_BLOCK.containsKey(stand) && CACHED_BLOCK_ID.containsKey(stand) && CACHED_BLOCK_DATA.containsKey(stand)) {
                            CACHED_BLOCK.get(stand).getLocation().getBlock().setTypeIdAndData(CACHED_BLOCK_ID.get(stand).intValue(), CACHED_BLOCK_DATA.get(stand).byteValue(), true);
                        }
                        stand.remove();
                        VoidgloomSeraph.this.activebea = false;
                    }
                    VoidgloomSeraph.this.activebea = false;
                }
            }.runTaskLater((Plugin)SkyBlock.getPlugin(), 185L);
            new BukkitRunnable(){

                public void run() {
                    if (VoidgloomSeraph.this.CooldownSkill) {
                        VoidgloomSeraph.this.CooldownSkill = false;
                    }
                }
            }.runTaskLater((Plugin)SkyBlock.getPlugin(), 400L);
        }
        int amounths = 30;
        if (2 == this.tier) {
            amounths = 60;
        }
        if (3 == this.tier) {
            amounths = 120;
        }
        if (4 <= this.tier) {
            amounths = 200;
        }
        if (!(1 > this.tier || this.CooldownSkill2 || 2 != SUtil.random(0, 100) || this.activehs || this.HeartRadi || this.activebea)) {
            this.CooldownSkill2 = true;
            this.activehs = true;
            HIT_SHIELD.put((org.bukkit.entity.Entity)entity, amounths);
            HIT_SHIELD_MAX.put((org.bukkit.entity.Entity)entity, amounths);
            EntityManager.DEFENSE_PERCENTAGE.put((org.bukkit.entity.Entity)entity, 100);
        }
        if (!HIT_SHIELD.containsKey(entity) && this.activehs) {
            HIT_SHIELD.put((org.bukkit.entity.Entity)entity, amounths);
            HIT_SHIELD_MAX.put((org.bukkit.entity.Entity)entity, amounths);
        }
        if (this.activehs && 0 >= HIT_SHIELD.get(entity) && !this.HeartRadi && !this.activebea) {
            this.CooldownSkill2 = true;
            HIT_SHIELD.remove(entity);
            HIT_SHIELD_MAX.remove(entity);
            player.playSound(player.getLocation(), Sound.ZOMBIE_REMEDY, 1.0f, 1.0f);
            EntityManager.DEFENSE_PERCENTAGE.put((org.bukkit.entity.Entity)entity, 75);
            this.activehs = false;
            new BukkitRunnable(){

                public void run() {
                    if (VoidgloomSeraph.this.CooldownSkill2) {
                        VoidgloomSeraph.this.CooldownSkill2 = false;
                    }
                }
            }.runTaskLater((Plugin)SkyBlock.getPlugin(), 850L);
        }
        if (this.bukkitEntity.getWorld() == player.getWorld() && 20.0 < this.getBukkitEntity().getLocation().distance(player.getLocation()) && this.HeartRadi) {
            User.getUser(player.getUniqueId()).damage(player.getMaxHealth() * 90.0 / 100.0, EntityDamageEvent.DamageCause.ENTITY_ATTACK, (org.bukkit.entity.Entity)entity);
            player.damage(1.0E-4, (org.bukkit.entity.Entity)entity);
        }
        Entity entity_ = this.getBukkitEntity().getHandle();
        double height = entity_.getBoundingBox().e - entity_.getBoundingBox().b;
        this.hologram_name.getEntity().teleport(this.getBukkitEntity().getLocation().clone().add(0.0, height, 0.0));
        this.hologram_name.getEntity().setCustomName(Sputnik.trans(Sputnik.entityNameTag((LivingEntity)this.getBukkitEntity(), Sputnik.buildcustomString(this.getEntityName(), 0, true))));
        this.hologram.getEntity().teleport(this.getBukkitEntity().getLocation().clone().add(0.0, 3.2, 0.0));
        this.hologram.getEntity().setCustomName(ChatColor.RED + SUtil.getFormattedTime(this.end - System.currentTimeMillis(), 1000));
        ((Enderman)this.getBukkitEntity()).setTarget((LivingEntity)player);
        if (!(4 > this.tier || this.CooldownSkill3 || 10 < SUtil.random(0, 100) || this.HeartRadi || this.activehs)) {
            EntityManager.DEFENSE_PERCENTAGE.put((org.bukkit.entity.Entity)entity, 100);
            this.HeartRadi = true;
            this.CooldownSkill3 = true;
            double x = entity.getLocation().getX();
            double y = entity.getLocation().getY();
            double z = entity.getLocation().getZ();
            Location l = new Location(entity.getWorld(), x, y, z);
            entity.getWorld().playEffect(entity.getLocation(), Effect.EXPLOSION_HUGE, 3);
            entity.getWorld().playEffect(entity.getLocation(), Effect.EXPLOSION_HUGE, 3);
            final ArmorStand stand_sit = (ArmorStand)entity.getWorld().spawn(l, ArmorStand.class);
            stand_sit.setVisible(false);
            stand_sit.setGravity(true);
            stand_sit.setMarker(true);
            stand_sit.setPassenger((org.bukkit.entity.Entity)entity);
            this.Ar1.add((org.bukkit.entity.Entity)stand_sit);
            this.Ar2.add((org.bukkit.entity.Entity)stand_sit);
            new BukkitRunnable((org.bukkit.entity.Entity)entity){
                final /* synthetic */ org.bukkit.entity.Entity val$entity;
                {
                    this.val$entity = entity;
                }

                public void run() {
                    if (stand_sit.isDead()) {
                        this.cancel();
                        return;
                    }
                    stand_sit.setPassenger(this.val$entity);
                }
            }.runTaskTimer((Plugin)SkyBlock.getPlugin(), 0L, 1L);
            ArmorStand stand3 = (ArmorStand)stand_sit.getWorld().spawn(stand_sit.getLocation().add(0.0, 0.0, 0.0), ArmorStand.class);
            stand3.setVisible(false);
            stand3.setGravity(false);
            stand3.setMarker(true);
            stand3.setMetadata("HeartRadiAS", (MetadataValue)new FixedMetadataValue((Plugin)SkyBlock.getPlugin(), (Object)true));
            this.Ar1.add((org.bukkit.entity.Entity)stand3);
            this.Ar2.add((org.bukkit.entity.Entity)stand3);
            ArmorStand stand4 = (ArmorStand)entity.getWorld().spawn(stand3.getLocation().add(0.0, 1.5, 0.0), ArmorStand.class);
            stand4.setVisible(false);
            stand4.setGravity(false);
            stand4.setMarker(true);
            this.Ar1.add((org.bukkit.entity.Entity)stand4);
            this.Ar2.add((org.bukkit.entity.Entity)stand4);
            stand4.setMetadata("HeartRadiAS", (MetadataValue)new FixedMetadataValue((Plugin)SkyBlock.getPlugin(), (Object)true));
            ArmorStand stand5 = (ArmorStand)entity.getWorld().spawn(stand4.getLocation().add(0.0, 1.5, 0.0), ArmorStand.class);
            stand5.setVisible(false);
            stand5.setGravity(false);
            stand5.setMarker(true);
            this.Ar1.add((org.bukkit.entity.Entity)stand5);
            this.Ar2.add((org.bukkit.entity.Entity)stand5);
            stand5.setMetadata("HeartRadiAS", (MetadataValue)new FixedMetadataValue((Plugin)SkyBlock.getPlugin(), (Object)true));
            Location l2 = l;
            l2.setYaw(l.getYaw() + 90.0f);
            ArmorStand stand1_ = (ArmorStand)stand_sit.getWorld().spawn(l2, ArmorStand.class);
            stand1_.setVisible(false);
            stand1_.setGravity(false);
            stand1_.setMarker(true);
            this.Ar1.add((org.bukkit.entity.Entity)stand1_);
            this.Ar2.add((org.bukkit.entity.Entity)stand1_);
            stand4.setMetadata("HeartRadiAS", (MetadataValue)new FixedMetadataValue((Plugin)SkyBlock.getPlugin(), (Object)true));
            ArmorStand stand6 = (ArmorStand)entity.getWorld().spawn(stand1_.getLocation().add(0.0, 1.5, 0.0), ArmorStand.class);
            stand6.setVisible(false);
            stand6.setGravity(false);
            stand6.setMarker(true);
            this.Ar1.add((org.bukkit.entity.Entity)stand6);
            this.Ar2.add((org.bukkit.entity.Entity)stand6);
            stand6.setMetadata("HeartRadiAS", (MetadataValue)new FixedMetadataValue((Plugin)SkyBlock.getPlugin(), (Object)true));
            ArmorStand stand7 = (ArmorStand)entity.getWorld().spawn(stand6.getLocation().add(0.0, 1.5, 0.0), ArmorStand.class);
            stand7.setVisible(false);
            stand7.setGravity(false);
            stand7.setMarker(true);
            this.Ar1.add((org.bukkit.entity.Entity)stand7);
            this.Ar2.add((org.bukkit.entity.Entity)stand7);
            new BukkitRunnable(){

                public void run() {
                    player.playSound(player.getLocation(), Sound.FIREWORK_LARGE_BLAST, 1.0f, 0.5f);
                    Sputnik.playFuckingSoundOfVoidgloomThatTookForeverToMake(player, (org.bukkit.entity.Entity)stand_sit);
                }
            }.runTaskLater((Plugin)SkyBlock.getPlugin(), 15L);
            new BukkitRunnable((org.bukkit.entity.Entity)entity, stand3, player, stand7, stand6, stand1_, stand5, stand4){
                final /* synthetic */ org.bukkit.entity.Entity val$entity;
                final /* synthetic */ ArmorStand val$stand3;
                final /* synthetic */ Player val$player;
                final /* synthetic */ ArmorStand val$stand7;
                final /* synthetic */ ArmorStand val$stand6;
                final /* synthetic */ ArmorStand val$stand1_;
                final /* synthetic */ ArmorStand val$stand5;
                final /* synthetic */ ArmorStand val$stand4;
                {
                    this.val$entity = entity;
                    this.val$stand3 = armorStand;
                    this.val$player = player;
                    this.val$stand7 = armorStand2;
                    this.val$stand6 = armorStand3;
                    this.val$stand1_ = armorStand4;
                    this.val$stand5 = armorStand5;
                    this.val$stand4 = armorStand6;
                }

                public void run() {
                    if (this.val$entity.isDead()) {
                        Sputnik.RemoveEntityArray(VoidgloomSeraph.this.Ar2);
                        this.cancel();
                        return;
                    }
                    Sputnik.entityBeam(this.val$stand3, this.val$stand3.getLocation(), this.val$player, this.val$entity);
                    Sputnik.entityBeam(this.val$stand7, this.val$stand3.getLocation(), this.val$player, this.val$entity);
                    Sputnik.entityBeam(this.val$stand6, this.val$stand3.getLocation(), this.val$player, this.val$entity);
                    Sputnik.entityBeam(this.val$stand1_, this.val$stand3.getLocation(), this.val$player, this.val$entity);
                    Sputnik.entityBeam(this.val$stand5, this.val$stand3.getLocation(), this.val$player, this.val$entity);
                    Sputnik.entityBeam(this.val$stand4, this.val$stand3.getLocation(), this.val$player, this.val$entity);
                    new BukkitRunnable(){

                        public void run() {
                            if (val$stand7.isDead()) {
                                this.cancel();
                                return;
                            }
                            if (val$entity.isDead()) {
                                Sputnik.IsInsideTheBeam.remove(val$player.getUniqueId());
                                this.cancel();
                                return;
                            }
                            if (Sputnik.IsInsideTheBeam.containsKey(val$player.getUniqueId()) && Sputnik.IsInsideTheBeam.get(val$player.getUniqueId()).booleanValue()) {
                                org.bukkit.entity.Entity entity = OWNER_BOSS.get(val$player.getUniqueId());
                                if (null == entity) {
                                    this.cancel();
                                    return;
                                }
                                User.getUser(val$player.getUniqueId()).damage(val$player.getMaxHealth() * 25.0 / 100.0, EntityDamageEvent.DamageCause.ENTITY_ATTACK, entity);
                                Sputnik.IsInsideTheBeam.remove(val$player.getUniqueId());
                            }
                        }
                    }.runTaskTimer((Plugin)SkyBlock.getPlugin(), 0L, 3L);
                }
            }.runTaskLater((Plugin)SkyBlock.getPlugin(), 25L);
            new BukkitRunnable((org.bukkit.entity.Entity)entity){
                final /* synthetic */ org.bukkit.entity.Entity val$entity;
                {
                    this.val$entity = entity;
                }

                public void run() {
                    VoidgloomSeraph.this.HeartRadi = false;
                    Sputnik.RemoveEntityArray(VoidgloomSeraph.this.Ar2);
                    EntityManager.DEFENSE_PERCENTAGE.put(this.val$entity, 75);
                    new BukkitRunnable(){

                        public void run() {
                            if (VoidgloomSeraph.this.CooldownSkill3) {
                                VoidgloomSeraph.this.CooldownSkill3 = false;
                            }
                        }
                    }.runTaskLater((Plugin)SkyBlock.getPlugin(), 700L);
                }
            }.runTaskLater((Plugin)SkyBlock.getPlugin(), 200L);
        }
        if (this.activeskull) {
            int damagefin = 800;
            if (4 <= this.tier) {
                damagefin = 2000;
            }
            if (LivingSkulls.containsKey(player.getUniqueId()) && this.activeskull) {
                VoidgloomSeraph.damageUpdate(damagefin * (2 * (LivingSkulls.get(player.getUniqueId()).size() + 1)));
            }
        }
        if (LivingSkulls.containsKey(player.getUniqueId()) && this.activeskull) {
            boolean bl = this.activeskull = 0 < LivingSkulls.get(player.getUniqueId()).size();
        }
        if (!(3 > this.tier || this.CooldownSkill4 || 2 != SUtil.random(0, 100) || this.activehs || this.activeskull)) {
            LivingSkulls.remove(player.getUniqueId());
            this.CooldownSkill4 = true;
            this.activeskull = true;
            VoidgloomSeraph.spawnNukekubi((org.bukkit.entity.Entity)entity, player, 1, 3);
            new BukkitRunnable((org.bukkit.entity.Entity)entity, player){
                final /* synthetic */ org.bukkit.entity.Entity val$entity;
                final /* synthetic */ Player val$player;
                {
                    this.val$entity = entity;
                    this.val$player = player;
                }

                public void run() {
                    if (this.val$entity.isDead()) {
                        if (LivingSkulls.containsKey(this.val$player.getUniqueId())) {
                            Sputnik.RemoveEntityArray(LivingSkulls.get(this.val$player.getUniqueId()));
                        }
                        this.cancel();
                        return;
                    }
                    if (VoidgloomSeraph.this.activeskull) {
                        VoidgloomSeraph.spawnNukekubi(this.val$entity, this.val$player, 1, 2);
                    } else {
                        VoidgloomSeraph.updateSkill(LivingSkulls.get(this.val$player.getUniqueId()));
                        new BukkitRunnable(){

                            public void run() {
                                if (VoidgloomSeraph.this.CooldownSkill4) {
                                    VoidgloomSeraph.this.CooldownSkill4 = false;
                                }
                            }
                        }.runTaskLater((Plugin)SkyBlock.getPlugin(), 400L);
                    }
                }
            }.runTaskLater((Plugin)SkyBlock.getPlugin(), 300L);
        }
    }

    @Override
    public void onSpawn(final LivingEntity entity, SEntity sEntity) {
        entity.setMetadata("NoAffect", (MetadataValue)new FixedMetadataValue((Plugin)SkyBlock.getPlugin(), (Object)true));
        entity.setMetadata("Voidgloom", (MetadataValue)new FixedMetadataValue((Plugin)SkyBlock.getPlugin(), (Object)true));
        entity.setMetadata("BOSS_OWNER_" + Bukkit.getPlayer((UUID)this.getSpawnerUUID()).getUniqueId().toString(), (MetadataValue)new FixedMetadataValue((Plugin)SkyBlock.getPlugin(), (Object)true));
        entity.setMetadata("GiantSword", (MetadataValue)new FixedMetadataValue((Plugin)SkyBlock.getPlugin(), (Object)true));
        entity.setMetadata("SlayerBoss", (MetadataValue)new FixedMetadataValue((Plugin)SkyBlock.getPlugin(), (Object)true));
        LivingSkulls.put(this.spawnerUUID, new ArrayList());
        SUtil.delay(() -> entity.removeMetadata("GiantSword", (Plugin)SkyBlock.getPlugin()), 20L);
        SUtil.delay(() -> {
            int amounths = 30;
            if (2 == this.tier) {
                amounths = 60;
            }
            if (3 == this.tier) {
                amounths = 120;
            }
            if (4 <= this.tier) {
                amounths = 200;
            }
            this.CooldownSkill2 = true;
            this.activehs = true;
            HIT_SHIELD.put((org.bukkit.entity.Entity)entity, amounths);
            HIT_SHIELD_MAX.put((org.bukkit.entity.Entity)entity, amounths);
            EntityManager.DEFENSE_PERCENTAGE.put((org.bukkit.entity.Entity)entity, 100);
        }, 0L);
        SUtil.delay(() -> {
            this.CooldownSkill5 = false;
        }, 70L);
        this.playBossParticle_1((org.bukkit.entity.Entity)entity);
        this.playBossParticle_2((org.bukkit.entity.Entity)entity);
        this.playShieldParticle((org.bukkit.entity.Entity)entity);
        SUtil.delay(() -> {
            this.CooldownSkill = false;
        }, 150L);
        this.CooldownSkill2 = false;
        SUtil.delay(() -> {
            this.CooldownSkill3 = false;
        }, 100L);
        SUtil.delay(() -> {
            this.CooldownSkill4 = false;
        }, 300L);
        Entity entity_ = this.getBukkitEntity().getHandle();
        this.hologram = new SEntity(entity.getLocation().add(0.0, 3.2, 0.0), SEntityType.UNCOLLIDABLE_ARMOR_STAND, new Object[0]);
        BEACON_THROW.put((org.bukkit.entity.Entity)entity, Bukkit.getPlayer((UUID)this.spawnerUUID));
        ((ArmorStand)this.hologram.getEntity()).setVisible(false);
        ((ArmorStand)this.hologram.getEntity()).setGravity(false);
        this.hologram.getEntity().setCustomNameVisible(true);
        entity.setMetadata("notDisplay", (MetadataValue)new FixedMetadataValue((Plugin)SkyBlock.getPlugin(), (Object)true));
        Entity e = this.getBukkitEntity().getHandle();
        double height_ = e.getBoundingBox().e - e.getBoundingBox().b;
        this.hologram_name = new SEntity(entity.getLocation().add(0.0, height_, 0.0), SEntityType.UNCOLLIDABLE_ARMOR_STAND, new Object[0]);
        ((ArmorStand)this.hologram_name.getEntity()).setVisible(false);
        ((ArmorStand)this.hologram_name.getEntity()).setGravity(false);
        this.hologram_name.getEntity().setCustomNameVisible(true);
        new BukkitRunnable(){

            public void run() {
                if (entity.isDead()) {
                    VoidgloomSeraph.this.hologram.remove();
                    this.cancel();
                    SUtil.delay(() -> VoidgloomSeraph.this.hologram_name.remove(), 20L);
                    Sputnik.RemoveEntityArray(LivingSkulls.get(VoidgloomSeraph.this.getSpawnerUUID()));
                    Repeater.BEACON_THROW2.remove(VoidgloomSeraph.this.getSpawnerUUID());
                    OWNER_BOSS.remove(VoidgloomSeraph.this.getSpawnerUUID());
                    if (null != VoidgloomSeraph.getPlayer()) {
                        VoidgloomSeraph.destroyArmorStandWithUUID(VoidgloomSeraph.this.getSpawnerUUID(), VoidgloomSeraph.getPlayer().getWorld());
                    }
                    BEACON_THROW.remove(entity);
                    HIT_SHIELD.remove(entity);
                    HIT_SHIELD_MAX.remove(entity);
                    Repeater.BEACON_OWNER.remove(entity);
                }
            }
        }.runTaskTimer((Plugin)SkyBlock.getPlugin(), 0L, 1L);
        new BukkitRunnable(){

            public void run() {
                if (entity.isDead()) {
                    this.cancel();
                    Sputnik.RemoveEntityArray(VoidgloomSeraph.this.Ar2);
                    if (LivingSkulls.containsKey(VoidgloomSeraph.this.getSpawnerUUID())) {
                        List<org.bukkit.entity.Entity> a = LivingSkulls.get(VoidgloomSeraph.this.getSpawnerUUID());
                        Sputnik.RemoveEntityArray(a);
                    }
                }
            }
        }.runTaskTimer((Plugin)SkyBlock.getPlugin(), 0L, 1L);
        new BukkitRunnable(){

            public void run() {
                if (entity.isDead()) {
                    this.cancel();
                    return;
                }
                if (null == VoidgloomSeraph.getPlayer()) {
                    this.cancel();
                    entity.remove();
                    return;
                }
                if (!VoidgloomSeraph.this.HeartRadi) {
                    VoidgloomSeraph.getPlayer().damage(((Double)DAMAGE_VALUES.getByNumber(VoidgloomSeraph.this.tier)).doubleValue(), (org.bukkit.entity.Entity)entity);
                }
            }
        }.runTaskTimer((Plugin)SkyBlock.getPlugin(), 0L, 20L);
    }

    @Override
    public void onDamage(SEntity sEntity, org.bukkit.entity.Entity damager, EntityDamageByEntityEvent e, AtomicDouble damage) {
        LivingEntity en = sEntity.getEntity();
        Vector v = new Vector(0, 0, 0);
        SUtil.delay(() -> VoidgloomSeraph.lambda$onDamage$8((org.bukkit.entity.Entity)en, v), 1L);
    }

    @Override
    public void onDeath(SEntity sEntity, org.bukkit.entity.Entity killed, org.bukkit.entity.Entity damager) {
        this.hologram.remove();
        SUtil.delay(() -> this.hologram_name.remove(), 20L);
        org.bukkit.entity.Entity stand = Repeater.BEACON_THROW2.get(damager.getUniqueId());
        BlockFallAPI.removeBlock(Repeater.BEACON.get(stand), killed.getWorld());
        Sputnik.RemoveEntityArray(this.Ar1);
        Sputnik.RemoveEntityArray(this.Ar2);
        if (LivingSkulls.containsKey(this.spawnerUUID)) {
            List<org.bukkit.entity.Entity> a = LivingSkulls.get(this.spawnerUUID);
            Sputnik.RemoveEntityArray(a);
        }
        if (null != stand) {
            stand.remove();
        }
        User user = User.getUser(damager.getUniqueId());
        user.addCoins(100000L);
        user.send(ChatColor.GOLD + "+100000 Coins");
    }

    @Override
    public String getEntityName() {
        return ChatColor.RED + "\u2620 " + ChatColor.AQUA + "Voidgloom Seraph";
    }

    @Override
    public double getEntityMaxHealth() {
        return MAX_HEALTH_VALUES.getByNumber(this.tier);
    }

    @Override
    public double getDamageDealt() {
        return DAMAGE_VALUES.getByNumber(this.tier);
    }

    @Override
    public double getMovementSpeed() {
        return SPEED_VALUES.getByNumber(this.tier);
    }

    @Override
    public LivingEntity spawn(Location location) {
        this.world = ((CraftWorld)location.getWorld()).getHandle();
        this.setPosition(location.getX(), location.getY(), location.getZ());
        this.world.addEntity((Entity)this, CreatureSpawnEvent.SpawnReason.CUSTOM);
        return (LivingEntity)this.getBukkitEntity();
    }

    @Override
    public List<EntityDrop> drops() {
        ArrayList<EntityDrop> drops = new ArrayList<EntityDrop>();
        int revFlesh = SUtil.random(1, 3);
        if (2 == this.tier) {
            revFlesh = SUtil.random(9, 18);
        }
        if (3 == this.tier) {
            revFlesh = SUtil.random(30, 50);
        }
        if (4 == this.tier) {
            revFlesh = SUtil.random(50, 64);
        }
        drops.add(new EntityDrop(SUtil.setStackAmount(SItem.of(SMaterial.NULL_SPHERE).getStack(), revFlesh), EntityDropType.GUARANTEED, 1.0));
        if (2 <= this.tier) {
            drops.add(new EntityDrop(SMaterial.SUMMONING_EYE, EntityDropType.EXTRAORDINARILY_RARE, 0.0033333333333333335, Bukkit.getPlayer((UUID)this.getSpawnerUUID())));
        }
        if (3 <= this.tier) {
            drops.add(new EntityDrop(SMaterial.HIDDEN_ETHERWARP_MERGER, EntityDropType.EXTRAORDINARILY_RARE, 5.0E-4, Bukkit.getPlayer((UUID)this.getSpawnerUUID())));
            drops.add(new EntityDrop(SMaterial.HIDDEN_DEMONS_PEARL, EntityDropType.RARE, 0.5));
        }
        if (4 <= this.tier) {
            drops.add(new EntityDrop(SMaterial.HIDDEN_DEMONS_PEARL, EntityDropType.RARE, 0.5));
            SItem endBook = SItem.of(SMaterial.ENCHANTED_BOOK);
            endBook.addEnchantment(EnchantmentType.ENDER_SLAYER, 15);
            drops.add(new EntityDrop(endBook.getStack(), EntityDropType.CRAZY_RARE, 0.002, Bukkit.getPlayer((UUID)this.getSpawnerUUID())));
            SItem legiBook = SItem.of(SMaterial.ENCHANTED_BOOK);
            legiBook.addEnchantment(EnchantmentType.LEGION, SUtil.random(1, 2));
            drops.add(new EntityDrop(legiBook.getStack(), EntityDropType.INSANE_RARE, 0.00125, Bukkit.getPlayer((UUID)this.getSpawnerUUID())));
            SItem fatalBook = SItem.of(SMaterial.ENCHANTED_BOOK);
            fatalBook.addEnchantment(EnchantmentType.FATAL_TEMPO, 1);
            drops.add(new EntityDrop(fatalBook.getStack(), EntityDropType.CRAZY_RARE, 4.0E-4, Bukkit.getPlayer((UUID)this.getSpawnerUUID())));
            drops.add(new EntityDrop(SMaterial.HIDDEN_ETHERWARP_CONDUIT, EntityDropType.CRAZY_RARE, 0.002, Bukkit.getPlayer((UUID)this.getSpawnerUUID())));
            drops.add(new EntityDrop(SMaterial.JUDGEMENT_CORE, EntityDropType.CRAZY_RARE, 0.001, Bukkit.getPlayer((UUID)this.getSpawnerUUID())));
            drops.add(new EntityDrop(SMaterial.HIDDEN_GYRO_EYE, EntityDropType.CRAZY_RARE, 0.001, Bukkit.getPlayer((UUID)this.getSpawnerUUID())));
            drops.add(new EntityDrop(SMaterial.HIDDEN_REFINED_POWDER, EntityDropType.EXTRAORDINARILY_RARE, 0.001, Bukkit.getPlayer((UUID)this.getSpawnerUUID())));
        }
        return drops;
    }

    @Override
    public double getXPDropped() {
        return XP_DROPS.getByNumber(this.tier);
    }

    public boolean isBaby() {
        return false;
    }

    public boolean isAngry() {
        return true;
    }

    @Override
    public UUID getSpawnerUUID() {
        return this.spawnerUUID;
    }

    public static void spawnNukekubi(org.bukkit.entity.Entity e, Player p, Integer damage, Integer spawnCouples) {
        if (2 >= spawnCouples) {
            Location loc1_ = p.getLocation();
            loc1_.setYaw(loc1_.getYaw() + (float)SUtil.random(0, 360));
            Location loc2_ = p.getLocation();
            loc2_.setYaw(loc1_.getYaw() + (float)SUtil.random(0, 360));
            Location loc1 = loc1_.add(loc1_.getDirection().multiply(5));
            Location loc2 = loc2_.add(loc2_.getDirection().multiply(-5));
            VoidgloomSeraph.moveHeadAround((org.bukkit.entity.Entity)VoidgloomSeraph.spawnHeads(e, loc1, p), p, damage);
            VoidgloomSeraph.moveHeadAround((org.bukkit.entity.Entity)VoidgloomSeraph.spawnHeads(e, loc2, p), p, damage);
        } else {
            for (int i = 0; i < spawnCouples; ++i) {
                Location loc4;
                Location loc3;
                Location loc2_2;
                Location loc1_2;
                if (1 == SUtil.random(1, 2)) {
                    loc1_2 = p.getLocation();
                    loc1_2.setYaw(loc1_2.getYaw() + (float)SUtil.random(0, 360));
                    loc2_2 = p.getLocation();
                    loc2_2.setYaw(loc1_2.getYaw() + (float)SUtil.random(0, 360));
                    loc3 = loc1_2.add(loc1_2.getDirection().multiply(5));
                    loc4 = loc2_2.add(loc2_2.getDirection().multiply(-5));
                    VoidgloomSeraph.moveHeadAround((org.bukkit.entity.Entity)VoidgloomSeraph.spawnHeads(e, loc3, p), p, damage);
                    continue;
                }
                loc1_2 = p.getLocation();
                loc1_2.setYaw(loc1_2.getYaw() + (float)SUtil.random(0, 360));
                loc2_2 = p.getLocation();
                loc2_2.setYaw(loc1_2.getYaw() + (float)SUtil.random(0, 360));
                loc3 = loc1_2.add(loc1_2.getDirection().multiply(5));
                loc4 = loc2_2.add(loc2_2.getDirection().multiply(-5));
                VoidgloomSeraph.moveHeadAround((org.bukkit.entity.Entity)VoidgloomSeraph.spawnHeads(e, loc4, p), p, damage);
            }
        }
    }

    public static void destroyArmorStandWithUUID(UUID uuid, org.bukkit.World w) {
        String uuidString = uuid.toString() + "_NUKEKUBI";
        for (org.bukkit.entity.Entity e : w.getEntities()) {
            if (!e.hasMetadata(uuidString)) continue;
            e.remove();
        }
    }

    public static LivingEntity spawnHeads(org.bukkit.entity.Entity e, Location loc, final Player p) {
        final ArmorStand entity = (ArmorStand)loc.getWorld().spawnEntity(e.getLocation().add(e.getLocation().getDirection().normalize().multiply(-1)), EntityType.ARMOR_STAND);
        loc.setY(loc.getY() + SUtil.random(0.0, 0.6));
        entity.setCustomName(Sputnik.trans("&c\u2620 &bVoidgloom Seraph"));
        entity.setVisible(false);
        entity.setGravity(false);
        entity.getEquipment().setHelmet(SItem.of(SMaterial.NUKEKUBI).getStack());
        entity.getEquipment().setItemInHand(new ItemStack(Material.AIR));
        entity.setMetadata("Nukekubi", (MetadataValue)new FixedMetadataValue((Plugin)SkyBlock.getPlugin(), (Object)true));
        entity.setMetadata(p.getUniqueId().toString() + "_NUKEKUBI", (MetadataValue)new FixedMetadataValue((Plugin)SkyBlock.getPlugin(), (Object)true));
        NUKEKUBI_TARGET.put((org.bukkit.entity.Entity)entity, p);
        if (!LivingSkulls.containsKey(p.getUniqueId())) {
            LivingSkulls.put(p.getUniqueId(), new ArrayList());
        }
        LivingSkulls.get(p.getUniqueId()).add((org.bukkit.entity.Entity)entity);
        VoidgloomSeraph.moveToLoc((org.bukkit.entity.Entity)entity, loc, 3, 0, 1.0);
        new BukkitRunnable(){

            public void run() {
                if (entity.isDead()) {
                    this.cancel();
                    return;
                }
                if (entity.hasMetadata("Nukekubi")) {
                    Location l = entity.getLocation().setDirection(p.getLocation().toVector().subtract(entity.getLocation().toVector()));
                    entity.teleport(l);
                    double x = 0.0;
                    double y = 0.0;
                    double z = 0.0;
                    x = Math.toRadians(l.getPitch());
                    entity.setHeadPose(new EulerAngle(x, 0.0, 0.0));
                    entity.getWorld().playEffect(entity.getLocation().add(0.0, 1.1, 0.0), Effect.WITCH_MAGIC, 1);
                    entity.getWorld().playEffect(entity.getLocation().add(0.0, 1.2, 0.0), Effect.WITCH_MAGIC, 1);
                    entity.getWorld().playEffect(entity.getLocation().add(0.0, 1.2, 0.0), Effect.WITCH_MAGIC, 1);
                    entity.getWorld().playEffect(entity.getLocation().add(0.0, 1.4, 0.0), Effect.WITCH_MAGIC, 1);
                    entity.getWorld().playEffect(entity.getLocation().add(0.0, 1.4, 0.0), Effect.WITCH_MAGIC, 1);
                }
            }
        }.runTaskTimer((Plugin)SkyBlock.getPlugin(), 0L, 1L);
        return entity;
    }

    public static void moveToLoc(final org.bukkit.entity.Entity e, final Location target, int tickingRad, int firstTickRad, final double jump) {
        final Location l = e.getLocation().setDirection(target.toVector().subtract(e.getLocation().toVector()));
        new BukkitRunnable(){

            public void run() {
                Vector teleportTo = l.getDirection().normalize().multiply(jump);
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
        }.runTaskTimer((Plugin)SkyBlock.getPlugin(), (long)firstTickRad, (long)tickingRad);
    }

    public static void moveHeadAround(final org.bukkit.entity.Entity head, final Player p, final Integer damage) {
        new BukkitRunnable(){

            public void run() {
                if (head.isDead()) {
                    this.cancel();
                    return;
                }
                int i1 = 0;
                if (NUKEKUBI_DAMAGE.containsKey(p)) {
                    i1 = NUKEKUBI_DAMAGE.get(p);
                }
                if (head.getWorld().equals(p.getWorld())) {
                    Sputnik.drawLineforMovingPoints(head.getLocation().add(head.getLocation().getDirection().multiply(0.1)).add(0.0, 2.0, 0.0), p.getLocation().add(0.0, 1.8, 0.0), 20.0, p, damage, head);
                    Sputnik.dmgc(i1, p, head);
                } else {
                    head.remove();
                }
            }
        }.runTaskTimer((Plugin)SkyBlock.getPlugin(), 0L, 20L);
    }

    public static void damageUpdate(double formula) {
        NUKEKUBI_DAMAGE.put((org.bukkit.entity.Entity)VoidgloomSeraph.getPlayer(), (int)formula);
    }

    public static void targetSelect(org.bukkit.entity.Entity entity, LivingEntity target) {
        if (!(entity instanceof Creature)) {
            return;
        }
        ((Creature)entity).setTarget(target);
    }

    public static void updateSkill(List<org.bukkit.entity.Entity> list) {
        for (org.bukkit.entity.Entity e : list) {
            NUKEKUBI_TARGET.remove(e);
        }
    }

    public static BukkitTask a(final ArmorStand entity) {
        return new BukkitRunnable(){

            public void run() {
                if (entity.isDead()) {
                    this.cancel();
                    return;
                }
                org.bukkit.World world = entity.getWorld();
                if (entity.hasMetadata("BeaconSkill") && entity.isOnGround()) {
                    entity.remove();
                    if (null != VoidgloomSeraph.getPlayer()) {
                        VoidgloomSeraph.getPlayer().getWorld().playSound(VoidgloomSeraph.getPlayer().getLocation(), Sound.ITEM_BREAK, 0.5f, 0.67f);
                    }
                    Location loc = entity.getLocation().getBlock().getLocation().add(0.5, 0.0, 0.5);
                    ArmorStand armorStand2 = (ArmorStand)entity.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
                    CACHED_BLOCK.put((org.bukkit.entity.Entity)armorStand2, loc.getBlock());
                    CACHED_BLOCK_ID.put((org.bukkit.entity.Entity)armorStand2, loc.getBlock().getTypeId());
                    CACHED_BLOCK_DATA.put((org.bukkit.entity.Entity)armorStand2, loc.getBlock().getData());
                    armorStand2.setGravity(true);
                    armorStand2.setVisible(false);
                    armorStand2.setCustomName(ChatColor.translateAlternateColorCodes((char)'&', (String)"&c\u2620 &fTEST"));
                    armorStand2.setMetadata("BeaconSkill2", (MetadataValue)new FixedMetadataValue((Plugin)SkyBlock.getPlugin(), (Object)true));
                    loc.getBlock().setType(Material.BARRIER);
                    Repeater.BEACON_THROW2.put(Repeater.BEACON_OWNER.get(entity).getUniqueId(), (org.bukkit.entity.Entity)armorStand2);
                    VoidgloomSeraph.b((org.bukkit.entity.Entity)armorStand2);
                    Repeater.BEACON.put((org.bukkit.entity.Entity)armorStand2, BlockFallAPI.sendBlockDestroyWithSignal(loc, Material.BEACON, (byte)0, world));
                }
            }
        }.runTaskTimer((Plugin)SkyBlock.getPlugin(), 0L, 1L);
    }

    public static void b(final org.bukkit.entity.Entity armorStand2) {
        new BukkitRunnable(){

            public void run() {
                if (armorStand2.isDead()) {
                    this.cancel();
                    return;
                }
                org.bukkit.World world = armorStand2.getWorld();
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
        }.runTaskTimer((Plugin)SkyBlock.getPlugin(), 0L, 1L);
    }

    public void teleportSkill(final org.bukkit.entity.Entity e, final Player p) {
        final int LOR = SUtil.random(0, 1);
        new BukkitRunnable(){
            int cout = 0;
            float addedYaw = p.getLocation().getYaw();

            public void run() {
                if (7 <= this.cout) {
                    p.getLocation().getWorld().playSound(p.getLocation(), Sound.ENDERDRAGON_HIT, 1.0f, 1.0f);
                    this.cancel();
                    return;
                }
                Location a = p.getLocation();
                this.addedYaw = 0 == LOR ? (this.addedYaw += 19.0f) : (this.addedYaw -= 19.0f);
                a.setPitch(0.0f);
                a.setYaw(this.addedYaw);
                a.add(a.getDirection().normalize().multiply(1.7));
                a.setY(e.getLocation().getY());
                Location tpl = a.clone();
                tpl.setYaw(e.getLocation().getYaw());
                e.teleport(tpl);
                VoidgloomSeraph.this.dP(a);
                a.getWorld().playSound(a, Sound.ENDERMAN_TELEPORT, 0.2f, 1.0f);
                ++this.cout;
            }
        }.runTaskTimer((Plugin)SkyBlock.getPlugin(), 0L, 1L);
    }

    public void drawPointerAt(Location loc) {
        loc.getWorld().spigot().playEffect(loc, Effect.WITCH_MAGIC, 0, 1, 1.0f, 1.0f, 1.0f, 0.0f, 0, 64);
        loc.getWorld().spigot().playEffect(loc, Effect.LARGE_SMOKE, 21, 0, 0.1f, 0.0f, 0.1f, 0.01f, 1, 30);
        loc.getWorld().spigot().playEffect(loc, Effect.LARGE_SMOKE, 21, 0, 0.1f, 0.0f, 0.1f, 0.01f, 1, 30);
        loc.getWorld().spigot().playEffect(loc, Effect.WITCH_MAGIC, 0, 1, 1.0f, 1.0f, 1.0f, 0.0f, 0, 64);
    }

    public void dP(Location loc) {
        this.drawPointerAt(loc.clone().add(0.0, 0.9, 0.0));
        this.drawPointerAt(loc.clone().add(0.0, 1.5, 0.0));
    }

    public void playShieldParticle(final org.bukkit.entity.Entity e) {
        new BukkitRunnable(){
            float cout;
            {
                this.cout = e.getLocation().getYaw();
            }

            public void run() {
                if (e.isDead()) {
                    this.cancel();
                    return;
                }
                Location loc = e.getLocation();
                loc.setYaw(this.cout);
                loc.setPitch(0.0f);
                loc.add(loc.getDirection().normalize().multiply(0.8));
                if (HIT_SHIELD.containsKey(e)) {
                    int hitshield = HIT_SHIELD.get(e);
                    int hitshieldmax = HIT_SHIELD_MAX.get(e);
                    int stage = 3;
                    if (hitshield <= hitshieldmax / 2 && hitshield > hitshieldmax * 25 / 100) {
                        stage = 2;
                    } else if (hitshield <= hitshieldmax * 25 / 100 && 1 != hitshield) {
                        stage = 1;
                    } else if (1 == hitshield) {
                        stage = 1;
                    }
                    if (0 < HIT_SHIELD.get(e)) {
                        e.getWorld().spigot().playEffect(loc, Effect.WITCH_MAGIC, 0, 1, 1.0f, 1.0f, 1.0f, 0.0f, 0, 64);
                        e.getWorld().spigot().playEffect(loc.clone().add(0.0, 0.6, 0.0), Effect.WITCH_MAGIC, 0, 1, 1.0f, 1.0f, 1.0f, 0.0f, 0, 64);
                        e.getWorld().spigot().playEffect(loc, Effect.WITCH_MAGIC, 0, 1, 1.0f, 1.0f, 1.0f, 0.0f, 0, 64);
                        e.getWorld().spigot().playEffect(loc.clone().add(0.0, 0.6, 0.0), Effect.WITCH_MAGIC, 0, 1, 1.0f, 1.0f, 1.0f, 0.0f, 0, 64);
                        if (2 <= stage) {
                            e.getWorld().spigot().playEffect(loc.clone().add(0.0, 1.2, 0.0), Effect.WITCH_MAGIC, 0, 1, 1.0f, 1.0f, 1.0f, 0.0f, 0, 64);
                            e.getWorld().spigot().playEffect(loc.clone().add(0.0, 1.2, 0.0), Effect.WITCH_MAGIC, 0, 1, 1.0f, 1.0f, 1.0f, 0.0f, 0, 64);
                        }
                        if (3 == stage) {
                            e.getWorld().spigot().playEffect(loc.clone().add(0.0, 2.4, 0.0), Effect.WITCH_MAGIC, 0, 1, 1.0f, 1.0f, 1.0f, 0.0f, 0, 64);
                            e.getWorld().spigot().playEffect(loc.clone().add(0.0, 2.4, 0.0), Effect.WITCH_MAGIC, 0, 1, 1.0f, 1.0f, 1.0f, 0.0f, 0, 64);
                            e.getWorld().spigot().playEffect(loc.clone().add(0.0, 1.8, 0.0), Effect.WITCH_MAGIC, 0, 1, 1.0f, 1.0f, 1.0f, 0.0f, 0, 64);
                            e.getWorld().spigot().playEffect(loc.clone().add(0.0, 1.8, 0.0), Effect.WITCH_MAGIC, 0, 1, 1.0f, 1.0f, 1.0f, 0.0f, 0, 64);
                        }
                    }
                }
                this.cout += 18.0f;
            }
        }.runTaskTimer((Plugin)SkyBlock.getPlugin(), 0L, 1L);
    }

    public void playBossParticle_1(final org.bukkit.entity.Entity e) {
        new BukkitRunnable(){
            float cout;
            {
                this.cout = e.getLocation().getYaw();
            }

            public void run() {
                if (e.isDead()) {
                    this.cancel();
                    return;
                }
                Location loc = e.getLocation().clone().add(0.0, 0.3, 0.0);
                loc.setYaw(this.cout);
                loc.setPitch(0.0f);
                loc.add(loc.getDirection().normalize().multiply(0.4));
                e.getWorld().spigot().playEffect(loc, Effect.CRIT, 0, 1, 1.0f, 1.0f, 1.0f, 0.0f, 0, 64);
                e.getWorld().spigot().playEffect(loc, Effect.CRIT, 0, 1, 1.0f, 1.0f, 1.0f, 0.0f, 0, 64);
                this.cout += 9.0f;
            }
        }.runTaskTimer((Plugin)SkyBlock.getPlugin(), 0L, 1L);
    }

    public void playBossParticle_2(final org.bukkit.entity.Entity e) {
        new BukkitRunnable(){
            float cout;
            {
                this.cout = e.getLocation().getYaw();
            }

            public void run() {
                if (e.isDead()) {
                    this.cancel();
                    return;
                }
                Location loc = e.getLocation().clone().add(0.0, 0.3, 0.0);
                loc.setYaw(this.cout);
                loc.setPitch(0.0f);
                loc.add(loc.getDirection().normalize().multiply(0.4));
                e.getWorld().spigot().playEffect(loc.clone().add(0.0, 0.6, 0.0), Effect.CRIT, 0, 1, 1.0f, 1.0f, 1.0f, 0.0f, 0, 64);
                e.getWorld().spigot().playEffect(loc.clone().add(0.0, 0.6, 0.0), Effect.CRIT, 0, 1, 1.0f, 1.0f, 1.0f, 0.0f, 0, 64);
                this.cout -= 9.0f;
            }
        }.runTaskTimer((Plugin)SkyBlock.getPlugin(), 0L, 1L);
    }

    @Override
    public int getTier() {
        return this.tier;
    }

    private static /* synthetic */ void lambda$onDamage$8(org.bukkit.entity.Entity en, Vector v) {
        en.setVelocity(v);
    }

    private static /* synthetic */ void lambda$t_$1(org.bukkit.entity.Entity stand, org.bukkit.entity.Entity entity) {
        BlockFallAPI.removeBlock(Repeater.BEACON.get(stand), entity.getWorld());
        stand.remove();
        entity.getWorld().playSound(stand.getLocation(), Sound.ITEM_BREAK, 1.0f, 0.5f);
        entity.getWorld().playEffect(stand.getLocation(), Effect.EXPLOSION_LARGE, 1);
        if (CACHED_BLOCK.containsKey(stand) && CACHED_BLOCK_ID.containsKey(stand) && CACHED_BLOCK_DATA.containsKey(stand)) {
            CACHED_BLOCK.get(stand).getLocation().getBlock().setTypeIdAndData(CACHED_BLOCK_ID.get(stand).intValue(), CACHED_BLOCK_DATA.get(stand).byteValue(), true);
        }
    }

    static {
        CACHED_BLOCK = new HashMap<org.bukkit.entity.Entity, Block>();
        CACHED_BLOCK_ID = new HashMap<org.bukkit.entity.Entity, Integer>();
        CACHED_BLOCK_DATA = new HashMap<org.bukkit.entity.Entity, Byte>();
    }
}

