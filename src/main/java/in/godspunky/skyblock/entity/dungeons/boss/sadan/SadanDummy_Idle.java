package in.godspunky.skyblock.entity.dungeons.boss.sadan;

import in.godspunky.skyblock.Skyblock;
import in.godspunky.skyblock.entity.SEntity;
import in.godspunky.skyblock.entity.SEntityEquipment;
import in.godspunky.skyblock.entity.zombie.BaseZombie;
import in.godspunky.skyblock.util.EntityManager;
import in.godspunky.skyblock.util.SUtil;
import in.godspunky.skyblock.util.Sputnik;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;

public class SadanDummy_Idle extends BaseZombie {
    public static void t(final LivingEntity e) {
        SUtil.delay(() -> e.teleport(new Location(e.getWorld(), 191.0, 54.0, 266.0)), 1L);
        SUtil.delay(() -> e.teleport(new Location(e.getWorld(), 191.0, 56.0, 266.0)), 20L);
        SUtil.delay(() -> e.teleport(new Location(e.getWorld(), 191.0, 58.0, 266.0)), 40L);
        SUtil.delay(() -> e.teleport(new Location(e.getWorld(), 191.0, 60.0, 266.0)), 60L);
        SUtil.delay(() -> e.teleport(new Location(e.getWorld(), 191.0, 62.0, 266.0)), 80L);
        SUtil.delay(() -> e.teleport(new Location(e.getWorld(), 191.0, 64.0, 266.0)), 100L);
        SUtil.delay(() -> e.teleport(new Location(e.getWorld(), 191.0, 66.0, 266.0)), 120L);
        SUtil.delay(() -> e.teleport(new Location(e.getWorld(), 191.0, 68.0, 266.0)), 140L);
        SUtil.delay(() -> e.teleport(new Location(e.getWorld(), 191.0, 69.0, 266.0)), 160L);
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
        final Location l = entity.getLocation();
        Sputnik.applyPacketGiant(entity);
        EntityManager.noAI(entity);
        EntityManager.noHit(entity);
        entity.setMetadata("GiantSword", new FixedMetadataValue(Skyblock.getPlugin(), true));
        entity.setMetadata("NoAffect", new FixedMetadataValue(Skyblock.getPlugin(), true));
        EntityManager.shutTheFuckUp(entity);
        EntityManager.DEFENSE_PERCENTAGE.put(entity, 100);
        l.setYaw(180.0f);
        entity.teleport(l);
        entity.setMetadata("notDisplay", new FixedMetadataValue(Skyblock.getPlugin(), true));
        entity.setMetadata("dummy_r", new FixedMetadataValue(Skyblock.getPlugin(), true));
    }

    @Override
    public SEntityEquipment getEntityEquipment() {
        return new SEntityEquipment(SUtil.enchant(new ItemStack(Material.DIAMOND_SWORD)), b(5385260, Material.LEATHER_HELMET), b(14751108, Material.LEATHER_CHESTPLATE), c(Material.DIAMOND_LEGGINGS), b(8991025, Material.LEATHER_BOOTS));
    }

    public void laser(final LivingEntity e) {
        final int[] array_colors = {12100772, 12089721, 12080726, 12204602, 12194322};
        SUtil.delay(() -> e.getEquipment().setHelmet(buildColorStack(array_colors[4])), 270L);
        SUtil.delay(() -> e.getEquipment().setHelmet(buildColorStack(array_colors[3])), 290L);
        SUtil.delay(() -> e.getEquipment().setHelmet(buildColorStack(array_colors[2])), 310L);
        SUtil.delay(() -> e.getEquipment().setHelmet(buildColorStack(array_colors[1])), 330L);
        SUtil.delay(() -> e.getEquipment().setHelmet(buildColorStack(array_colors[0])), 350L);
        SUtil.delay(() -> e.getEquipment().setHelmet(b(15249075, Material.LEATHER_HELMET)), 370L);
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
}
