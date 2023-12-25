package in.godspunky.skyblock.entity.dungeons.regularentity;

import in.godspunky.skyblock.Skyblock;
import in.godspunky.skyblock.entity.*;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import in.godspunky.skyblock.item.SMaterial;
import in.godspunky.skyblock.util.EntityManager;
import in.godspunky.skyblock.util.SUtil;

import java.util.Arrays;
import java.util.List;

public class SkeletonMaster implements EntityFunction, EntityStatistics {
    @Override
    public String getEntityName() {
        return "Skeleton Master";
    }

    @Override
    public double getEntityMaxHealth() {
        return 6.0E7;
    }

    @Override
    public double getDamageDealt() {
        return 5000000.0;
    }

    @Override
    public double getXPDropped() {
        return 40.0;
    }

    @Override
    public void onSpawn(final LivingEntity entity, final SEntity sEntity) {
        entity.setMetadata("DungeonMobs", new FixedMetadataValue(Skyblock.getPlugin(), true));
        entity.setMetadata("SlayerBoss", new FixedMetadataValue(Skyblock.getPlugin(), true));
        entity.setMetadata("SkeletonMaster", new FixedMetadataValue(Skyblock.getPlugin(), true));
        EntityManager.DEFENSE_PERCENTAGE.put(entity, 45);
    }

    @Override
    public List<EntityDrop> drops() {
        return Arrays.asList(new EntityDrop(new ItemStack(Material.ARROW, SUtil.random(2, 4)), EntityDropType.GUARANTEED, 1.0), new EntityDrop(SMaterial.ENCHANTED_BONE, EntityDropType.RARE, 0.05));
    }

    @Override
    public SEntityEquipment getEntityEquipment() {
        return new SEntityEquipment(new ItemStack(Material.BOW), SUtil.applyColorToLeatherArmor(new ItemStack(Material.LEATHER_HELMET), Color.fromRGB(16739083)), SUtil.applyColorToLeatherArmor(new ItemStack(Material.LEATHER_CHESTPLATE), Color.fromRGB(16739083)), SUtil.applyColorToLeatherArmor(new ItemStack(Material.LEATHER_LEGGINGS), Color.fromRGB(16739083)), SUtil.applyColorToLeatherArmor(new ItemStack(Material.LEATHER_BOOTS), Color.fromRGB(16739083)));
    }
}
