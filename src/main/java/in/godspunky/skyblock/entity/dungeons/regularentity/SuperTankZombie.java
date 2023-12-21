package in.godspunky.skyblock.entity.dungeons.regularentity;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import in.godspunky.skyblock.SkySimEngine;
import in.godspunky.skyblock.entity.EntityFunction;
import in.godspunky.skyblock.entity.EntityStatistics;
import in.godspunky.skyblock.entity.SEntity;
import in.godspunky.skyblock.entity.SEntityEquipment;
import in.godspunky.skyblock.util.EntityManager;
import in.godspunky.skyblock.util.SUtil;

public class SuperTankZombie implements EntityFunction, EntityStatistics {
    @Override
    public String getEntityName() {
        return "Super Tank Zombie";
    }

    @Override
    public double getEntityMaxHealth() {
        return 2.0E7;
    }

    @Override
    public double getDamageDealt() {
        return 1600500.0;
    }

    @Override
    public SEntityEquipment getEntityEquipment() {
        return new SEntityEquipment(new ItemStack(Material.STONE_SWORD), SUtil.applyColorToLeatherArmor(new ItemStack(Material.LEATHER_HELMET), Color.fromRGB(15132390)), SUtil.applyColorToLeatherArmor(new ItemStack(Material.LEATHER_CHESTPLATE), Color.fromRGB(8553090)), SUtil.applyColorToLeatherArmor(new ItemStack(Material.LEATHER_LEGGINGS), Color.fromRGB(8553090)), SUtil.applyColorToLeatherArmor(new ItemStack(Material.LEATHER_BOOTS), Color.fromRGB(15132390)));
    }

    @Override
    public void onSpawn(final LivingEntity entity, final SEntity sEntity) {
        entity.setMetadata("SlayerBoss", new FixedMetadataValue(SkySimEngine.getPlugin(), true));
        EntityManager.DEFENSE_PERCENTAGE.put(entity, 98);
        entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 1000000, 1));
    }

    @Override
    public double getXPDropped() {
        return 100.0;
    }
}
