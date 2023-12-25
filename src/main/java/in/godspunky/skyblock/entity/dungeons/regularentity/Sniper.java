package in.godspunky.skyblock.entity.dungeons.regularentity;

import in.godspunky.skyblock.Skyblock;
import in.godspunky.skyblock.entity.*;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import in.godspunky.skyblock.item.SMaterial;
import in.godspunky.skyblock.util.EntityManager;
import in.godspunky.skyblock.util.SUtil;

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
    public void onSpawn(final LivingEntity entity, final SEntity sEntity) {
        entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 1000000, 100));
        entity.setMetadata("SlayerBoss", new FixedMetadataValue(Skyblock.getPlugin(), true));
        entity.setMetadata("DungeonMobs", new FixedMetadataValue(Skyblock.getPlugin(), true));
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
