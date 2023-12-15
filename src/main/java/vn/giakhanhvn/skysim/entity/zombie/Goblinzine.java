package vn.giakhanhvn.skysim.entity.zombie;

import java.util.Arrays;

import vn.giakhanhvn.skysim.entity.EntityDropType;
import vn.giakhanhvn.skysim.item.SMaterial;
import vn.giakhanhvn.skysim.entity.EntityDrop;

import java.util.List;

import me.libraryaddict.disguise.disguisetypes.watchers.PlayerWatcher;
import me.libraryaddict.disguise.disguisetypes.PlayerDisguise;
import net.minecraft.server.v1_8_R3.AttributeInstance;
import vn.giakhanhvn.skysim.util.EntityManager;
import org.bukkit.entity.Entity;
import vn.giakhanhvn.skysim.util.Sputnik;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.metadata.FixedMetadataValue;
import vn.giakhanhvn.skysim.SkySimEngine;
import net.minecraft.server.v1_8_R3.GenericAttributes;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftZombie;
import vn.giakhanhvn.skysim.entity.SEntity;
import org.bukkit.entity.LivingEntity;

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
        entity.setMetadata("LD", new FixedMetadataValue(SkySimEngine.getPlugin(), true));
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
