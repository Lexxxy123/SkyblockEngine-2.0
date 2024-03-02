package net.hypixel.skyblock.entity.dungeons.regularentity;

import net.hypixel.skyblock.SkyBlock;
import net.hypixel.skyblock.entity.*;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import net.hypixel.skyblock.item.SMaterial;
import net.hypixel.skyblock.util.EntityManager;
import net.hypixel.skyblock.util.SUtil;

import java.util.Arrays;
import java.util.List;

public class Sniper implements EntityFunction, EntityStatistics {
    @Override
    public String getEntityName() {
        return "Sniper";
    }

    @Override
    public double getEntityMaxHealth() {
        return 2.0E7;
    }

    @Override
    public double getDamageDealt() {
        return 8000000.0;
    }

    @Override
    public double getXPDropped() {
        return 60.0;
    }

    @Override
    public void onSpawn(LivingEntity entity, SEntity sEntity) {
        entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 1000000, 100));
        entity.setMetadata("SlayerBoss", new FixedMetadataValue(SkyBlock.getPlugin(), true));
        entity.setMetadata("DungeonMobs", new FixedMetadataValue(SkyBlock.getPlugin(), true));
        EntityManager.DEFENSE_PERCENTAGE.put(entity, 55);
    }

    @Override
    public List<EntityDrop> drops() {
        return Arrays.asList(new EntityDrop(new ItemStack(Material.ARROW, SUtil.random(2, 4)), EntityDropType.GUARANTEED, 1.0), new EntityDrop(SMaterial.ENCHANTED_BONE, EntityDropType.RARE, 0.05));
    }

    @Override
    public SEntityEquipment getEntityEquipment() {
        return new SEntityEquipment(new ItemStack(Material.BOW), SUtil.getSkullURLStack("asas", "98949a424802498a1f1d6b30dfd4556379831b4f6e9d59c9a880f192e61da765", 1, ""), null, null, null);
    }
}
