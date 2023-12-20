package in.godspunky.skyblock.entity.dungeons.boss.sadan;

import com.google.common.util.concurrent.AtomicDouble;
import in.godspunky.skyblock.SkySimEngine;
import in.godspunky.skyblock.entity.SEntity;
import in.godspunky.skyblock.entity.SEntityEquipment;
import in.godspunky.skyblock.entity.zombie.BaseZombie;
import in.godspunky.skyblock.util.EntityManager;
import in.godspunky.skyblock.util.SUtil;
import in.godspunky.skyblock.util.Sputnik;
import net.minecraft.server.v1_8_R3.AttributeInstance;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.GenericAttributes;
import net.minecraft.server.v1_8_R3.PacketPlayOutAnimation;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftZombie;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class JollyPinkGiant extends BaseZombie {
    private static LivingEntity e;
    private boolean terToss;
    private boolean terTossCD;

    public JollyPinkGiant() {
        this.terToss = false;
        this.terTossCD = true;
    }

    @Override
    public String getEntityName() {
        return Sputnik.trans("&d&lJolly Pink Giant");
    }

    @Override
    public double getEntityMaxHealth() {
        return 2.5E7;
    }

    @Override
    public double getDamageDealt() {
        return 25000.0;
    }

    @Override
    public void onSpawn(final LivingEntity entity, final SEntity sEntity) {
        if (entity.getWorld().getPlayers().size() == 0) {
            return;
        }
        JollyPinkGiant.e = entity;
        final AttributeInstance followRange = ((CraftLivingEntity) entity).getHandle().getAttributeInstance(GenericAttributes.FOLLOW_RANGE);
        followRange.setValue(500.0);
        ((CraftZombie) entity).setBaby(false);
        final Player p = entity.getWorld().getPlayers().get(SUtil.random(0, entity.getWorld().getPlayers().size() - 1));
        if (p != null && p.getGameMode() != GameMode.SPECTATOR && p.getGameMode() != GameMode.CREATIVE) {
            ((CraftZombie) entity).setTarget(p);
        }
        SUtil.delay(() -> this.terTossCD = false, 60L);
        Sputnik.applyPacketGiant(entity);
        EntityManager.DEFENSE_PERCENTAGE.put(entity, 30);
        entity.setMetadata("SlayerBoss", new FixedMetadataValue(SkySimEngine.getPlugin(), true));
        entity.setMetadata("highername", new FixedMetadataValue(SkySimEngine.getPlugin(), true));
        entity.setMetadata("Giant_", new FixedMetadataValue(SkySimEngine.getPlugin(), true));
        new BukkitRunnable() {
            public void run() {
                final LivingEntity target = ((CraftZombie) entity).getTarget();
                if (entity.isDead()) {
                    this.cancel();
                    return;
                }
                if (!JollyPinkGiant.this.terToss && !JollyPinkGiant.this.terTossCD && SUtil.random(1, 150) <= 4 && target != null) {
                    JollyPinkGiant.this.terTossCD = true;
                    JollyPinkGiant.this.terToss = true;
                    SUtil.delay(() -> JollyPinkGiant.this.terToss = false, 400L);
                    JollyPinkGiant.this.launchTerrain(entity);
                }
            }
        }.runTaskTimer(SkySimEngine.getPlugin(), 0L, 1L);
        new BukkitRunnable() {
            public void run() {
                final EntityLiving nms = ((CraftLivingEntity) entity).getHandle();
                if (entity.isDead()) {
                    this.cancel();
                    return;
                }
                for (final Entity entities : entity.getWorld().getNearbyEntities(entity.getLocation().add(entity.getLocation().getDirection().multiply(1.0)), 1.5, 1.5, 1.5)) {
                    if (!(entities instanceof Player)) {
                        continue;
                    }
                    final Player target = (Player) entities;
                    if (target.getGameMode() == GameMode.CREATIVE) {
                        continue;
                    }
                    if (target.getGameMode() == GameMode.SPECTATOR) {
                        continue;
                    }
                    if (target.hasMetadata("NPC")) {
                        continue;
                    }
                    entity.teleport(entity.getLocation().setDirection(target.getLocation().toVector().subtract(target.getLocation().toVector())));
                    for (final Player players : Bukkit.getOnlinePlayers()) {
                        ((CraftPlayer) players).getHandle().playerConnection.sendPacket(new PacketPlayOutAnimation(((CraftLivingEntity) entity).getHandle(), 0));
                    }
                    nms.r(((CraftPlayer) target).getHandle());
                    break;
                }
            }
        }.runTaskTimer(SkySimEngine.getPlugin(), 0L, 5L);
    }

    @Override
    public SEntityEquipment getEntityEquipment() {
        return new SEntityEquipment(null, b(14751108, Material.LEATHER_HELMET), b(14751108, Material.LEATHER_CHESTPLATE), b(14751108, Material.LEATHER_LEGGINGS), b(14751108, Material.LEATHER_BOOTS));
    }

    @Override
    public void onDeath(final SEntity sEntity, final Entity killed, final Entity damager) {
        Sputnik.zero(killed);
        if (SadanHuman.SadanGiantsCount.containsKey(killed.getWorld().getUID())) {
            SadanHuman.SadanGiantsCount.put(killed.getWorld().getUID(), SadanHuman.SadanGiantsCount.get(killed.getWorld().getUID()) - 1);
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

    public void launchTerrain(final LivingEntity e) {
        new BukkitRunnable() {
            public void run() {
                if (e.isDead()) {
                    this.cancel();
                    return;
                }
                if (!JollyPinkGiant.this.terToss) {
                    SUtil.delay(() -> JollyPinkGiant.this.terTossCD = false, 200L);
                    this.cancel();
                    return;
                }
                final LivingEntity t = ((CraftZombie) e).getTarget();
                if (t != null) {
                    JollyPinkGiant.this.throwTerrain(e, t);
                }
            }
        }.runTaskTimer(SkySimEngine.getPlugin(), 0L, 30L);
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

    public static void applyEffect(final PotionEffectType e, final Entity en, final int ticks, final int amp) {
        ((LivingEntity) en).addPotionEffect(new PotionEffect(e, ticks, amp));
    }

    public static void damagePlayer(final Player p) {
        p.sendMessage(Sputnik.trans("&d&lJolly Pink Giant &chit you with &eBoulder Toss &cfor " + SUtil.commaify(SadanFunction.dmgc(30000, p, JollyPinkGiant.e)) + " &cdamage."));
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
                final Location loc = startBlock.clone().add(length, 0.0, height);
                final Location end = b.getLocation().clone().add(length, 0.0, height);
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
        });
        locationList.forEach(location -> {
            final Material material = launchTypes.get(locationList.indexOf(location));
            final Location origin = location.clone().add(0.0, 7.0, 0.0);
            final int pos = locationList.indexOf(location);
            final Location endPos = endList.get(pos);
            final FallingBlock block2 = world.spawnFallingBlock(origin, material, blockData);
            block2.setDropItem(false);
            block2.setMetadata("t", new FixedMetadataValue(SkySimEngine.getPlugin(), true));
            block2.setVelocity(Sputnik.calculateVelocityBlock(origin.toVector(), endPos.toVector(), 3));
        });
    }

    @Override
    public void onDamage(final SEntity sEntity, final Entity damager, final EntityDamageByEntityEvent e, final AtomicDouble damage) {
        e.getEntity().getWorld().playSound(e.getEntity().getLocation(), Sound.ZOMBIE_HURT, 1.0f, 0.0f);
    }
}
