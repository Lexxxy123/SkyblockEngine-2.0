package vn.giakhanhvn.skysim.entity.nms;

import vn.giakhanhvn.skysim.enchantment.EnchantmentType;
import vn.giakhanhvn.skysim.entity.EntityDropType;
import java.util.ArrayList;
import vn.giakhanhvn.skysim.entity.EntityDrop;
import java.util.List;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.Location;
import vn.giakhanhvn.skysim.entity.SEntityEquipment;
import org.bukkit.entity.ArmorStand;
import vn.giakhanhvn.skysim.entity.SEntityType;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.metadata.FixedMetadataValue;
import net.minecraft.server.v1_8_R3.Entity;
import org.bukkit.entity.Player;
import vn.giakhanhvn.skysim.item.SItem;
import vn.giakhanhvn.skysim.item.SMaterial;
import org.bukkit.Color;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.bukkit.plugin.Plugin;
import vn.giakhanhvn.skysim.SkySimEngine;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.bukkit.Sound;
import net.minecraft.server.v1_8_R3.GenericAttributes;
import org.bukkit.ChatColor;
import vn.giakhanhvn.skysim.util.Sputnik;
import org.bukkit.entity.LivingEntity;
import vn.giakhanhvn.skysim.user.User;
import vn.giakhanhvn.skysim.util.SUtil;
import org.bukkit.entity.Zombie;
import net.minecraft.server.v1_8_R3.World;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import java.util.UUID;
import vn.giakhanhvn.skysim.entity.SEntity;
import vn.giakhanhvn.skysim.entity.ZombieStatistics;
import vn.giakhanhvn.skysim.entity.EntityFunction;
import net.minecraft.server.v1_8_R3.EntityZombie;

public class RevenantHorror extends EntityZombie implements SNMSEntity, EntityFunction, ZombieStatistics, SlayerBoss
{
    private static final TieredValue<Double> MAX_HEALTH_VALUES;
    private static final TieredValue<Double> DAMAGE_VALUES;
    private static final TieredValue<Double> SPEED_VALUES;
    private final int tier;
    private boolean enraged;
    private boolean raged;
    private boolean Cooldown;
    private final long end;
    private SEntity hologram;
    private SEntity hologram_name;
    private final UUID spawnerUUID;
    
    public RevenantHorror(final Integer tier, final UUID spawnerUUID) {
        super((World)((CraftWorld)Bukkit.getPlayer(spawnerUUID).getWorld()).getHandle());
        this.tier = tier;
        this.enraged = false;
        this.end = System.currentTimeMillis() + 180000L;
        this.spawnerUUID = spawnerUUID;
        this.Cooldown = true;
    }
    
    public RevenantHorror(final World world) {
        super(world);
        this.tier = 1;
        this.enraged = false;
        this.end = System.currentTimeMillis() + 180000L;
        this.spawnerUUID = UUID.randomUUID();
        this.Cooldown = true;
    }
    
    public void t_() {
        super.t_();
        final Player player = Bukkit.getPlayer(this.spawnerUUID);
        if (player == null) {
            return;
        }
        if (((Zombie)this.bukkitEntity).getWorld() == player.getWorld() && this.getBukkitEntity().getLocation().distance(player.getLocation()) >= 20.0 && SUtil.random(0, 10) == 0) {
            this.getBukkitEntity().teleport(player.getLocation());
        }
        final LivingEntity e = (LivingEntity)this.getBukkitEntity();
        if (System.currentTimeMillis() > this.end) {
            User.getUser(player.getUniqueId()).failSlayerQuest();
            ((Zombie)this.bukkitEntity).remove();
            this.hologram.remove();
            return;
        }
        final Entity entity = this.getBukkitEntity().getHandle();
        final double height = entity.getBoundingBox().e - entity.getBoundingBox().b;
        this.hologram_name.getEntity().teleport(this.getBukkitEntity().getLocation().clone().add(0.0, height, 0.0));
        this.hologram_name.getEntity().setCustomName(Sputnik.trans(Sputnik.entityNameTag((LivingEntity)this.getBukkitEntity(), Sputnik.buildcustomString(this.getEntityName(), 0, true))));
        this.hologram.getEntity().teleport(this.getBukkitEntity().getLocation().clone().add(0.0, 2.3, 0.0));
        if (!this.raged) {
            this.hologram.getEntity().setCustomName(ChatColor.RED + SUtil.getFormattedTime(this.end - System.currentTimeMillis(), 1000));
        }
        else {
            this.hologram.getEntity().setCustomName(ChatColor.DARK_RED + "ENRAGED " + ChatColor.RED + SUtil.getFormattedTime(this.end - System.currentTimeMillis(), 1000));
        }
        ((Zombie)this.bukkitEntity).setTarget((LivingEntity)player);
        if (this.tier >= 3 && !this.enraged && SUtil.random(0, 20) == 0 && !this.Cooldown) {
            this.enraged = true;
            this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(this.getMovementSpeed());
            this.hologram.getEntity().setCustomName(ChatColor.DARK_RED + "" + ChatColor.RED + SUtil.getFormattedTime(this.end - System.currentTimeMillis(), 1000));
            player.playSound(player.getLocation(), Sound.ZOMBIE_WOODBREAK, 1.0f, 1.0f);
            player.setVelocity(new Vector(SUtil.random(-1.0, 1.0), SUtil.random(0.0, 0.5), SUtil.random(-1.0, 1.0)));
            new BukkitRunnable() {
                public void run() {
                    RevenantHorror.this.enraged = false;
                    RevenantHorror.this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(RevenantHorror.this.getMovementSpeed());
                    RevenantHorror.this.hologram.getEntity().setCustomName(ChatColor.RED + SUtil.getFormattedTime(RevenantHorror.this.end - System.currentTimeMillis(), 1000));
                }
            }.runTaskLater((Plugin)SkySimEngine.getPlugin(), 200L);
        }
        if (this.tier >= 3 && !this.raged && SUtil.random(0, 200) == 0 && !this.Cooldown) {
            this.raged = true;
            this.Cooldown = true;
            e.getEquipment().setChestplate(SUtil.enchant(SUtil.applyColorToLeatherArmor(new ItemStack(Material.LEATHER_CHESTPLATE), Color.fromRGB(12451840))));
            e.getEquipment().setHelmet(SItem.of(SMaterial.REV_HORROR_2).getStack());
            this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(this.getMovementSpeed() + 0.02);
            this.hologram.getEntity().setCustomName(ChatColor.DARK_RED + "ENRAGED " + ChatColor.RED + SUtil.getFormattedTime(this.end - System.currentTimeMillis(), 1000));
            new BukkitRunnable() {
                public void run() {
                    e.getEquipment().setChestplate(SUtil.enchant(new ItemStack(Material.DIAMOND_CHESTPLATE)));
                    e.getEquipment().setHelmet(SItem.of(SMaterial.REVENANT_HORROR_HEAD).getStack());
                    RevenantHorror.this.raged = false;
                    RevenantHorror.this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(RevenantHorror.this.getMovementSpeed());
                    RevenantHorror.this.hologram.getEntity().setCustomName(ChatColor.RED + SUtil.getFormattedTime(RevenantHorror.this.end - System.currentTimeMillis(), 1000));
                    SUtil.delay(() -> RevenantHorror.this.Cooldown = false, 550L);
                }
            }.runTaskLater((Plugin)SkySimEngine.getPlugin(), 250L);
        }
    }
    
    public void onSpawn(final LivingEntity entity, final SEntity sEntity) {
        SUtil.delay(() -> this.Cooldown = false, 400L);
        entity.setMetadata("BOSS_OWNER_" + Bukkit.getPlayer(this.getSpawnerUUID()).getUniqueId().toString(), (MetadataValue)new FixedMetadataValue((Plugin)SkySimEngine.getPlugin(), (Object)true));
        entity.setMetadata("SlayerBoss", (MetadataValue)new FixedMetadataValue((Plugin)SkySimEngine.getPlugin(), (Object)true));
        this.hologram = new SEntity(entity.getLocation().add(0.0, 2.3, 0.0), SEntityType.UNCOLLIDABLE_ARMOR_STAND, new Object[0]);
        ((ArmorStand)this.hologram.getEntity()).setVisible(false);
        ((ArmorStand)this.hologram.getEntity()).setGravity(false);
        this.hologram.getEntity().setCustomNameVisible(true);
        entity.setMetadata("notDisplay", (MetadataValue)new FixedMetadataValue((Plugin)SkySimEngine.getPlugin(), (Object)true));
        this.hologram_name = new SEntity(entity.getLocation().add(0.0, 2.0, 0.0), SEntityType.UNCOLLIDABLE_ARMOR_STAND, new Object[0]);
        ((ArmorStand)this.hologram_name.getEntity()).setVisible(false);
        final Entity e = this.getBukkitEntity().getHandle();
        final double height = e.getBoundingBox().e - e.getBoundingBox().b;
        ((ArmorStand)this.hologram_name.getEntity()).setGravity(false);
        this.hologram_name.getEntity().setCustomNameVisible(true);
        new BukkitRunnable() {
            public void run() {
                if (entity.isDead()) {
                    this.cancel();
                    return;
                }
                final Player player = Bukkit.getPlayer(RevenantHorror.this.spawnerUUID);
                if (player == null) {
                    return;
                }
                player.damage(RevenantHorror.this.getDamageDealt() * 0.5, (org.bukkit.entity.Entity)entity);
            }
        }.runTaskTimer((Plugin)SkySimEngine.getPlugin(), 60L, 60L);
        if (this.tier >= 2) {
            new BukkitRunnable() {
                public void run() {
                    if (entity.isDead()) {
                        this.cancel();
                        return;
                    }
                    final Player player = Bukkit.getPlayer(RevenantHorror.this.spawnerUUID);
                    if (player == null) {
                        return;
                    }
                    player.damage(RevenantHorror.this.getDamageDealt(), (org.bukkit.entity.Entity)entity);
                }
            }.runTaskTimer((Plugin)SkySimEngine.getPlugin(), 20L, 20L);
        }
        new BukkitRunnable() {
            public void run() {
                if (entity.isDead()) {
                    SUtil.delay(() -> RevenantHorror.this.hologram_name.remove(), 20L);
                    RevenantHorror.this.hologram.remove();
                    this.cancel();
                }
            }
        }.runTaskTimer((Plugin)SkySimEngine.getPlugin(), 0L, 1L);
    }
    
    public void onDeath(final SEntity sEntity, final org.bukkit.entity.Entity killed, final org.bukkit.entity.Entity damager) {
        this.hologram.remove();
        SUtil.delay(() -> this.hologram_name.remove(), 20L);
    }
    
    public String getEntityName() {
        return ChatColor.RED + "☠ " + ChatColor.AQUA + "Revenant Horror";
    }
    
    public double getEntityMaxHealth() {
        return RevenantHorror.MAX_HEALTH_VALUES.getByNumber(this.tier);
    }
    
    public double getDamageDealt() {
        return RevenantHorror.DAMAGE_VALUES.getByNumber(this.tier) * (this.enraged ? 1.8 : 1.0) * (this.raged ? 2 : 2);
    }
    
    public double getMovementSpeed() {
        return RevenantHorror.SPEED_VALUES.getByNumber(this.tier) * (this.enraged ? 1.05 : 1.0);
    }
    
    public SEntityEquipment getEntityEquipment() {
        return new SEntityEquipment(new ItemStack(Material.DIAMOND_HOE), SItem.of(SMaterial.REVENANT_HORROR_HEAD).getStack(), SUtil.enchant(new ItemStack(Material.DIAMOND_CHESTPLATE)), new ItemStack(Material.CHAINMAIL_LEGGINGS), new ItemStack(Material.DIAMOND_BOOTS));
    }
    
    public LivingEntity spawn(final Location location) {
        this.world = (World)((CraftWorld)location.getWorld()).getHandle();
        this.setPosition(location.getX(), location.getY(), location.getZ());
        this.world.addEntity((Entity)this, CreatureSpawnEvent.SpawnReason.CUSTOM);
        return (LivingEntity)this.getBukkitEntity();
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
        drops.add(new EntityDrop(SUtil.setStackAmount(SItem.of(SMaterial.REVENANT_FLESH).getStack(), revFlesh), EntityDropType.GUARANTEED, 1.0));
        if (this.tier >= 2) {
            int foulFlesh = 1;
            if (this.tier == 3) {
                foulFlesh = SUtil.random(1, 2);
            }
            if (this.tier == 4) {
                foulFlesh = SUtil.random(2, 3);
            }
            drops.add(new EntityDrop(SUtil.setStackAmount(SItem.of(SMaterial.FOUL_FLESH).getStack(), foulFlesh), EntityDropType.OCCASIONAL, 0.2));
            drops.add(new EntityDrop(SMaterial.PESTILENCE_RUNE, EntityDropType.RARE, 0.05));
            drops.add(new EntityDrop(SMaterial.UNDEAD_CATALYST, EntityDropType.EXTRAORDINARILY_RARE, 0.01));
            drops.add(new EntityDrop(SMaterial.REVENANT_CATALYST, EntityDropType.EXTRAORDINARILY_RARE, 0.01));
        }
        if (this.tier >= 3) {
            final SItem smiteBook = SItem.of(SMaterial.ENCHANTED_BOOK);
            smiteBook.addEnchantment(EnchantmentType.SMITE, 6);
            drops.add(new EntityDrop(smiteBook.getStack(), EntityDropType.EXTRAORDINARILY_RARE, 0.01));
            drops.add(new EntityDrop(SMaterial.BEHEADED_HORROR, EntityDropType.CRAZY_RARE, 0.005));
        }
        if (this.tier >= 4) {
            drops.add(new EntityDrop(SMaterial.SNAKE_RUNE, EntityDropType.CRAZY_RARE, 0.005));
            drops.add(new EntityDrop(SMaterial.SCYTHE_BLADE, EntityDropType.CRAZY_RARE, 5.384615384615384E-4));
        }
        return drops;
    }
    
    public double getXPDropped() {
        return 0.0;
    }
    
    public boolean isBaby() {
        return false;
    }
    
    public boolean isVillager() {
        return false;
    }
    
    public UUID getSpawnerUUID() {
        return this.spawnerUUID;
    }
    
    public int getTier() {
        return this.tier;
    }
    
    static {
        MAX_HEALTH_VALUES = new TieredValue<Double>(500.0, 20000.0, 400000.0, 1500000.0);
        DAMAGE_VALUES = new TieredValue<Double>(15.0, 50.0, 300.0, 1000.0);
        SPEED_VALUES = new TieredValue<Double>(0.35, 0.4, 0.45, 0.55);
    }
}
