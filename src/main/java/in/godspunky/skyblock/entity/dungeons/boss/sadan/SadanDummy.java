package in.godspunky.skyblock.entity.dungeons.boss.sadan;

import in.godspunky.skyblock.Skyblock;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import in.godspunky.skyblock.entity.SEntity;
import in.godspunky.skyblock.entity.SEntityEquipment;
import in.godspunky.skyblock.entity.zombie.BaseZombie;
import in.godspunky.skyblock.util.EntityManager;
import in.godspunky.skyblock.util.SUtil;
import in.godspunky.skyblock.util.Sputnik;

public class SadanDummy extends BaseZombie {
    @Override
    public String getEntityName() {
        return Sputnik.trans("");
    }

    @Override
    public double getEntityMaxHealth() {
        return 4.0E7;
    }

    @Override
    public double getDamageDealt() {
        return 120000.0;
    }

    @Override
    public void onSpawn(final LivingEntity entity, final SEntity sEntity) {
        entity.teleport(new Location(entity.getWorld(), 191.5, 54.0, 266.5, 180.0f, 0.0f));
        final Location l = entity.getLocation().clone();
        Sputnik.applyPacketGiant(entity);
        EntityManager.noAI(entity);
        EntityManager.noHit(entity);
        entity.setMetadata("GiantSword", new FixedMetadataValue(Skyblock.getPlugin(), true));
        entity.setMetadata("NoAffect", new FixedMetadataValue(Skyblock.getPlugin(), true));
        EntityManager.DEFENSE_PERCENTAGE.put(entity, 100);
        EntityManager.shutTheFuckUp(entity);
        entity.setMetadata("dummyforphase3", new FixedMetadataValue(Skyblock.getPlugin(), true));
        entity.setMetadata("notDisplay", new FixedMetadataValue(Skyblock.getPlugin(), true));
        SUtil.delay(() -> this.t(entity), 30L);
        SUtil.delay(() -> this.helmetcolor(entity), 220L);
    }

    @Override
    public SEntityEquipment getEntityEquipment() {
        return new SEntityEquipment(SUtil.enchant(new ItemStack(Material.DIAMOND_SWORD)), b(5385260, Material.LEATHER_HELMET), b(14751108, Material.LEATHER_CHESTPLATE), c(Material.DIAMOND_LEGGINGS), b(8991025, Material.LEATHER_BOOTS));
    }

    public void helmetcolor(final LivingEntity e) {
        final int[] array_colors = {12228503, 8739418, 6897985, 6042419, 5385260};
        SUtil.delay(() -> e.getEquipment().setHelmet(buildColorStack(array_colors[4])), 1L);
        SUtil.delay(() -> e.getEquipment().setHelmet(buildColorStack(array_colors[3])), 20L);
        SUtil.delay(() -> e.getEquipment().setHelmet(buildColorStack(array_colors[2])), 30L);
        SUtil.delay(() -> e.getEquipment().setHelmet(buildColorStack(array_colors[1])), 40L);
        SUtil.delay(() -> e.getEquipment().setHelmet(buildColorStack(array_colors[0])), 60L);
        SUtil.delay(() -> e.getEquipment().setHelmet(b(15249075, Material.LEATHER_HELMET)), 60L);
    }

    public void t(final LivingEntity e) {
        SUtil.delay(() -> e.teleport(new Location(e.getWorld(), 191.5, 55.5, 266.5, 180.0f, 0.0f)), 1L);
        SUtil.delay(() -> e.teleport(new Location(e.getWorld(), 191.5, 57.5, 266.5, 180.0f, 0.0f)), 20L);
        SUtil.delay(() -> e.teleport(new Location(e.getWorld(), 191.5, 59.5, 266.5, 180.0f, 0.0f)), 40L);
        SUtil.delay(() -> e.teleport(new Location(e.getWorld(), 191.5, 61.5, 266.5, 180.0f, 0.0f)), 60L);
        SUtil.delay(() -> e.teleport(new Location(e.getWorld(), 191.5, 63.5, 266.5, 180.0f, 0.0f)), 80L);
        SUtil.delay(() -> e.teleport(new Location(e.getWorld(), 191.5, 65.5, 266.5, 180.0f, 0.0f)), 100L);
        SUtil.delay(() -> e.teleport(new Location(e.getWorld(), 191.5, 67.5, 266.5, 180.0f, 0.0f)), 120L);
        SUtil.delay(() -> e.teleport(new Location(e.getWorld(), 191.5, 69.5, 266.5, 180.0f, 0.0f)), 140L);
        SUtil.delay(() -> e.teleport(new Location(e.getWorld(), 191.5, 70.5, 266.5, 180.0f, 0.0f)), 160L);
        SUtil.delay(() -> e.teleport(new Location(e.getWorld(), 191.5, 70.75, 266.5, 180.0f, 0.0f)), 180L);
        SUtil.delay(() -> e.teleport(new Location(e.getWorld(), 191.5, 70.5, 266.5, 180.0f, 0.0f)), 181L);
        SUtil.delay(() -> e.teleport(new Location(e.getWorld(), 191.5, 70.25, 266.5, 180.0f, 0.0f)), 182L);
        SUtil.delay(() -> e.teleport(new Location(e.getWorld(), 191.5, 70.0, 266.5, 180.0f, 0.0f)), 183L);
        SUtil.delay(() -> e.teleport(new Location(e.getWorld(), 191.5, 69.75, 266.5, 180.0f, 0.0f)), 185L);
        SUtil.delay(() -> e.teleport(new Location(e.getWorld(), 191.5, 69.5, 266.5, 180.0f, 0.0f)), 186L);
        SUtil.delay(() -> e.teleport(new Location(e.getWorld(), 191.5, 69.25, 266.5, 180.0f, 0.0f)), 187L);
        SUtil.delay(() -> e.teleport(new Location(e.getWorld(), 191.5, 69.0, 266.5, 180.0f, 0.0f)), 188L);
        SUtil.delay(() -> this.e(e), 200L);
        SUtil.delay(() -> this.r(e), 205L);
    }

    public void e(final LivingEntity e) {
        final Location l = e.getLocation();
        l.setYaw(60.0f);
        e.teleport(l);
    }

    public void r(final LivingEntity e) {
        final Location l = e.getLocation();
        l.setYaw(0.0f);
        new BukkitRunnable() {
            public void run() {
                final Vector teleportTo = l.getDirection().normalize().multiply(1);
                if (e.isDead()) {
                    this.cancel();
                    return;
                }
                if (String.valueOf(e.getLocation().getX()).contains("191") && String.valueOf(e.getLocation().getZ()).contains("285")) {
                    SadanHuman.SadanReach.put(e.getWorld().getUID(), true);
                    this.cancel();
                    return;
                }
                e.teleport(e.getLocation().add(teleportTo).multiply(1.0));
            }
        }.runTaskTimer(Skyblock.getPlugin(), 0L, 3L);
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
        return 0.0;
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
}
