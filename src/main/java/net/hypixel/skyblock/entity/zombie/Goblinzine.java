package net.hypixel.skyblock.entity.zombie;

import me.libraryaddict.disguise.disguisetypes.PlayerDisguise;
import me.libraryaddict.disguise.disguisetypes.watchers.PlayerWatcher;
import net.minecraft.server.v1_8_R3.AttributeInstance;
import net.minecraft.server.v1_8_R3.GenericAttributes;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftZombie;
import org.bukkit.entity.LivingEntity;
import org.bukkit.metadata.FixedMetadataValue;
import net.hypixel.skyblock.SkyBlock;
import net.hypixel.skyblock.entity.EntityDrop;
import net.hypixel.skyblock.entity.EntityDropType;
import net.hypixel.skyblock.entity.SEntity;
import net.hypixel.skyblock.item.SMaterial;
import net.hypixel.skyblock.util.EntityManager;
import net.hypixel.skyblock.util.Sputnik;

import java.util.Arrays;
import java.util.List;

public class Goblinzine extends BaseZombie {
    @Override
    public String getEntityName() {
        return "Goblinzine";
    }

    @Override
    public double getEntityMaxHealth() {
        return 1.0E8;
    }

    @Override
    public double getDamageDealt() {
        return 2000000.0;
    }

    @Override
    public void onSpawn(final LivingEntity entity, final SEntity sEntity) {
        ((CraftZombie) entity).setBaby(false);
        final AttributeInstance followRange = ((CraftLivingEntity) entity).getHandle().getAttributeInstance(GenericAttributes.FOLLOW_RANGE);
        followRange.setValue(40.0);
        entity.setMetadata("LD", new FixedMetadataValue(SkyBlock.getPlugin(), true));
        final PlayerDisguise pl = Sputnik.applyPacketNPC(entity, "automota", null, false);
        final PlayerWatcher skywatch = pl.getWatcher();
        final LivingEntity target = ((CraftZombie) entity).getTarget();
        EntityManager.DEFENSE_PERCENTAGE.put(entity, 15);
    }

    @Override
    public List<EntityDrop> drops() {
        return Arrays.asList(new EntityDrop(SMaterial.ROTTEN_FLESH, EntityDropType.GUARANTEED, 1.0), new EntityDrop(SMaterial.POISONOUS_POTATO, EntityDropType.OCCASIONAL, 0.05), new EntityDrop(SMaterial.POTATO_ITEM, EntityDropType.OCCASIONAL, 0.05), new EntityDrop(SMaterial.CARROT_ITEM, EntityDropType.OCCASIONAL, 0.05), new EntityDrop(SMaterial.HIDDEN_DIMOON_GEM, EntityDropType.EXTRAORDINARILY_RARE, 0.006666666828095913));
    }

    @Override
    public boolean isBaby() {
        return false;
    }

    @Override
    public boolean isVillager() {
        return false;
    }

    @Override
    public double getXPDropped() {
        return 124.0;
    }

    @Override
    public int mobLevel() {
        return 250;
    }
}
