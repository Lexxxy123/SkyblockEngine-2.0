package vn.giakhanhvn.skysim.entity.dungeons.boss.sadan;

import org.bukkit.inventory.meta.ItemMeta;
import vn.giakhanhvn.skysim.util.SUtil;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.metadata.FixedMetadataValue;
import vn.giakhanhvn.skysim.SkySimEngine;
import vn.giakhanhvn.skysim.util.EntityManager;
import org.bukkit.entity.Entity;
import vn.giakhanhvn.skysim.entity.SEntity;
import org.bukkit.entity.LivingEntity;
import vn.giakhanhvn.skysim.util.Sputnik;
import vn.giakhanhvn.skysim.entity.zombie.BaseZombie;

public class GiantsDummy extends BaseZombie {
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
        Sputnik.applyPacketGiant(entity);
        EntityManager.noAI(entity);
        EntityManager.DEFENSE_PERCENTAGE.put(entity, 100);
        EntityManager.noHit(entity);
        entity.setMetadata("GiantSword", new FixedMetadataValue(SkySimEngine.getPlugin(), true));
        entity.setMetadata("NoAffect", new FixedMetadataValue(SkySimEngine.getPlugin(), true));
        EntityManager.shutTheFuckUp(entity);
        entity.setMetadata("dummy_a", new FixedMetadataValue(SkySimEngine.getPlugin(), true));
        entity.setMetadata("notDisplay", new FixedMetadataValue(SkySimEngine.getPlugin(), true));
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
