package in.godspunky.skyblock.entity.dungeons.regularentity;

import in.godspunky.skyblock.SkyBlock;
import in.godspunky.skyblock.entity.*;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import in.godspunky.skyblock.item.SMaterial;
import in.godspunky.skyblock.util.EntityManager;
import in.godspunky.skyblock.util.SUtil;

import java.util.Arrays;
import java.util.List;

public class SuperArcher implements EntityFunction, EntityStatistics {
    @Override
    public String getEntityName() {
        return "Super Archer";
    }

    @Override
    public double getEntityMaxHealth() {
        return 1.0E8;
    }

    @Override
    public double getDamageDealt() {
        return 3000000.0;
    }

    @Override
    public double getXPDropped() {
        return 60.0;
    }

    @Override
    public void onSpawn(final LivingEntity entity, final SEntity sEntity) {
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
        return new SEntityEquipment(new ItemStack(Material.BOW), null, null, null, null);
    }
}
