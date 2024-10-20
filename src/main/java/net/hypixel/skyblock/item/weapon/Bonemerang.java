/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraft.server.v1_8_R3.ItemStack
 *  net.minecraft.server.v1_8_R3.NBTBase
 *  net.minecraft.server.v1_8_R3.NBTTagCompound
 *  net.minecraft.server.v1_8_R3.NBTTagInt
 *  org.bukkit.ChatColor
 *  org.bukkit.Effect
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.Sound
 *  org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack
 *  org.bukkit.entity.ArmorStand
 *  org.bukkit.entity.Damageable
 *  org.bukkit.entity.EnderDragonPart
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.Item
 *  org.bukkit.entity.ItemFrame
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.entity.Villager
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 *  org.bukkit.util.EulerAngle
 *  org.bukkit.util.Vector
 */
package net.hypixel.skyblock.item.weapon;

import de.tr7zw.nbtapi.NBTItem;
import java.util.ArrayList;
import net.hypixel.skyblock.SkyBlock;
import net.hypixel.skyblock.item.Ability;
import net.hypixel.skyblock.item.GenericItemType;
import net.hypixel.skyblock.item.MaterialFunction;
import net.hypixel.skyblock.item.Rarity;
import net.hypixel.skyblock.item.SItem;
import net.hypixel.skyblock.item.SMaterial;
import net.hypixel.skyblock.item.SpecificItemType;
import net.hypixel.skyblock.item.ToolStatistics;
import net.hypixel.skyblock.listener.PlayerListener;
import net.hypixel.skyblock.user.User;
import net.hypixel.skyblock.util.FerocityCalculation;
import net.hypixel.skyblock.util.SUtil;
import net.hypixel.skyblock.util.Sputnik;
import net.minecraft.server.v1_8_R3.NBTBase;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTTagInt;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.EnderDragonPart;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

public class Bonemerang
implements ToolStatistics,
MaterialFunction,
Ability {
    String Activable = "true";
    private int slotThrew = 0;

    @Override
    public int getBaseDamage() {
        return 270;
    }

    @Override
    public double getBaseStrength() {
        return 130.0;
    }

    @Override
    public String getDisplayName() {
        return "Bonemerang";
    }

    @Override
    public Rarity getRarity() {
        return Rarity.LEGENDARY;
    }

    @Override
    public GenericItemType getType() {
        return GenericItemType.RANGED_WEAPON;
    }

    @Override
    public SpecificItemType getSpecificType() {
        return SpecificItemType.BOW;
    }

    @Override
    public String getLore() {
        return null;
    }

    @Override
    public String getAbilityName() {
        return "Swing";
    }

    @Override
    public String getAbilityDescription() {
        return "Throw bone a short distance, dealing the damage an arrow would.                      Deals " + ChatColor.RED + "double" + ChatColor.GRAY + " damage when coming back. Pierces up to " + ChatColor.YELLOW + "10" + ChatColor.GRAY + " foes.";
    }

    @Override
    public void onAbilityUse(final Player player, final SItem sItem) {
        if (player.getInventory().getItemInHand().toString().contains("GHAST_TEAR")) {
            return;
        }
        final int slot = player.getInventory().getHeldItemSlot();
        this.slotThrew = player.getInventory().getHeldItemSlot();
        final ItemStack item = player.getInventory().getItemInHand();
        net.minecraft.server.v1_8_R3.ItemStack tagStack = CraftItemStack.asNMSCopy((ItemStack)item);
        NBTTagCompound tagCompound = tagStack.hasTag() ? tagStack.getTag() : new NBTTagCompound();
        tagCompound.set("ejectedBonemerang", (NBTBase)new NBTTagInt(0));
        tagStack.setTag(tagCompound);
        this.releaseBone(player, item, slot);
        ItemStack bone = player.getInventory().getItemInHand();
        boolean activate = true;
        player.playSound(player.getLocation(), Sound.SKELETON_HURT, 3.0f, 2.0f);
        if ("true" == this.Activable) {
            Player bukkitPlayer = player.getPlayer();
            final ArmorStand stand = (ArmorStand)bukkitPlayer.getWorld().spawnEntity(bukkitPlayer.getLocation().add(0.0, 0.7, 0.0), EntityType.ARMOR_STAND);
            final Vector teleportTo = bukkitPlayer.getLocation().getDirection().normalize().multiply(1);
            stand.setVisible(false);
            stand.setGravity(false);
            stand.setArms(false);
            stand.setMarker(true);
            stand.getEquipment().setItemInHand(SItem.of(SMaterial.BONE).getStack());
            stand.setRightArmPose(new EulerAngle(Math.toRadians(350.0), Math.toRadians(120.0), Math.toRadians(0.0)));
            stand.setBasePlate(false);
            final ArrayList goBone = new ArrayList();
            final ArrayList backBone = new ArrayList();
            new BukkitRunnable(){
                public int ran = 0;
                final int slot1 = player.getInventory().getHeldItemSlot();
                final NBTItem nbtItem = new NBTItem(player.getInventory().getItem(this.slot1));

                public void run() {
                    boolean back;
                    int angle;
                    ++this.ran;
                    int slot1 = player.getInventory().getHeldItemSlot();
                    if (26 == this.ran) {
                        Bonemerang.this.returnBone(player, item, slot);
                        User.getUser(player.getUniqueId()).setBoneToZeroDamage(false);
                        stand.remove();
                        this.cancel();
                        return;
                    }
                    int i2 = this.ran;
                    int num = 120;
                    Location loc = null;
                    if (13 > i2) {
                        angle = i2 * 20 + 120;
                        back = false;
                    } else {
                        angle = i2 * 20 - 120;
                        back = true;
                        loc = player.getLocation();
                        loc.setDirection(teleportTo);
                    }
                    Location locof = stand.getLocation();
                    locof.setY(locof.getY() + 1.0);
                    if (Material.AIR != locof.getBlock().getType() && Material.WATER != locof.getBlock().getType()) {
                        stand.getWorld().playEffect(stand.getLocation(), Effect.EXPLOSION, 1);
                        stand.getWorld().playEffect(stand.getLocation(), Effect.EXPLOSION, 1);
                        stand.getWorld().playEffect(stand.getLocation(), Effect.EXPLOSION, 1);
                        stand.getWorld().playEffect(stand.getLocation(), Effect.EXPLOSION, 1);
                        stand.getWorld().playEffect(stand.getLocation(), Effect.EXPLOSION, 1);
                        stand.remove();
                        new BukkitRunnable(){

                            public void run() {
                                if (nbtItem.hasKey("ejectedBonemerang").booleanValue()) {
                                    Bonemerang.this.returnBone(player, item, slot);
                                }
                                this.cancel();
                            }
                        }.runTaskLater((Plugin)SkyBlock.getPlugin(), 30L);
                        this.cancel();
                        return;
                    }
                    stand.setRightArmPose(new EulerAngle(Math.toRadians(0.0), Math.toRadians(angle), Math.toRadians(350.0)));
                    if (0 == i2 % 2 && 13 > i2) {
                        stand.teleport(stand.getLocation().add(teleportTo).multiply(1.0));
                        stand.teleport(stand.getLocation().add(teleportTo).multiply(1.0));
                    } else if (0 == i2 % 2) {
                        stand.getWorld().spigot().playEffect(stand.getEyeLocation().add(0.0, 1.0, 0.0).clone().add(SUtil.random(-0.5, 0.5), 0.0, SUtil.random(-0.5, 0.5)), Effect.FLYING_GLYPH, 24, 1, 0.0f, 0.0f, 0.0f, 1.0f, 0, 64);
                        stand.teleport(stand.getLocation().subtract(loc.getDirection().normalize().multiply(1)));
                        stand.teleport(stand.getLocation().subtract(loc.getDirection().normalize().multiply(1)));
                    }
                    for (Entity e2 : stand.getNearbyEntities(1.0, 1.0, 1.0)) {
                        if (!(e2 instanceof LivingEntity) || e2 == player.getPlayer()) continue;
                        Damageable entity = (Damageable)e2;
                        if (!back && goBone.contains(e2) || back && backBone.contains(e2) || entity.isDead()) continue;
                        if (entity.hasMetadata("VWE")) {
                            stand.remove();
                            stand.getWorld().playEffect(stand.getLocation(), Effect.EXPLOSION, 1);
                            stand.getWorld().playEffect(stand.getLocation(), Effect.EXPLOSION, 1);
                            stand.getWorld().playEffect(stand.getLocation(), Effect.EXPLOSION, 1);
                            stand.getWorld().playEffect(stand.getLocation(), Effect.EXPLOSION, 1);
                            stand.getWorld().playEffect(stand.getLocation(), Effect.EXPLOSION, 1);
                            return;
                        }
                        if (!(entity instanceof LivingEntity) || entity instanceof Player || entity instanceof EnderDragonPart || entity instanceof Villager || entity instanceof ArmorStand || entity instanceof Item || entity instanceof ItemFrame || entity.hasMetadata("GiantSword")) continue;
                        double decr = 1.0;
                        User user = User.getUser(player.getUniqueId());
                        if (user.toBukkitPlayer().getInventory().getHeldItemSlot() != Bonemerang.this.slotThrew) {
                            user.setBoneToZeroDamage(true);
                        }
                        Object[] atp = Sputnik.calculateDamage(player, player, sItem.getStack(), (LivingEntity)entity, true);
                        double finalDamage1 = (double)(((Float)atp[0]).floatValue() * (float)(back ? 2 : 1)) * 1.0;
                        double rm = 100.0;
                        if (User.getUser(player.getUniqueId()).isBoneToZeroDamage()) {
                            rm = 5.0;
                        }
                        finalDamage1 = finalDamage1 * rm / 100.0;
                        if (!back) {
                            goBone.add(e2);
                        } else {
                            backBone.add(e2);
                        }
                        PlayerListener.spawnDamageInd((Entity)entity, (double)(((Float)atp[2]).floatValue() * (float)(back ? 2 : 1)) * 1.0 * rm / 100.0, (Boolean)atp[1]);
                        FerocityCalculation.activeFerocityTimes(player, (LivingEntity)entity, (int)finalDamage1, (Boolean)atp[1]);
                        user.damageEntity(entity, finalDamage1);
                        if (back) {
                            User.getUser(player.getUniqueId()).setBoneToZeroDamage(true);
                        }
                        new BukkitRunnable(){

                            public void run() {
                                this.cancel();
                            }
                        }.runTaskLater((Plugin)SkyBlock.getPlugin(), 30L);
                    }
                }
            }.runTaskTimer((Plugin)SkyBlock.getPlugin(), 1L, 1L);
        }
    }

    public void releaseBone(Player player, ItemStack stack, Integer slot) {
        net.minecraft.server.v1_8_R3.ItemStack tagStack = CraftItemStack.asNMSCopy((ItemStack)stack);
        NBTTagCompound tagCompound = tagStack.hasTag() ? tagStack.getTag() : new NBTTagCompound();
        tagCompound.set("ejectedBonemerang", (NBTBase)new NBTTagInt(1));
        tagStack.setTag(tagCompound);
        ItemStack itemStack = CraftItemStack.asBukkitCopy((net.minecraft.server.v1_8_R3.ItemStack)tagStack);
        if (1 == tagStack.getTag().getInt("ejectedBonemerang")) {
            itemStack.setType(Material.GHAST_TEAR);
            player.getInventory().setItem(slot.intValue(), itemStack);
        }
    }

    public void returnBone(Player player, ItemStack stack, Integer slot) {
        net.minecraft.server.v1_8_R3.ItemStack tagStack = CraftItemStack.asNMSCopy((ItemStack)stack);
        NBTTagCompound tagCompound = tagStack.hasTag() ? tagStack.getTag() : new NBTTagCompound();
        tagCompound.set("ejectedBonemerang", (NBTBase)new NBTTagInt(0));
        tagStack.setTag(tagCompound);
        SItem sitem = SItem.find(player.getInventory().getItem(slot.intValue()));
        if (null != sitem && SMaterial.BONEMERANG == sitem.getType() && sitem.getDataString("uuid").equals(SItem.find(stack).getDataString("uuid"))) {
            ItemStack itemStack = CraftItemStack.asBukkitCopy((net.minecraft.server.v1_8_R3.ItemStack)tagStack);
            if (0 == tagStack.getTag().getInt("ejectedBonemerang")) {
                itemStack.setType(Material.BONE);
                player.getInventory().setItem(slot.intValue(), itemStack);
            }
        }
    }

    @Override
    public int getAbilityCooldownTicks() {
        return 0;
    }

    @Override
    public boolean isStackable() {
        return false;
    }

    @Override
    public int getManaCost() {
        return 0;
    }
}

