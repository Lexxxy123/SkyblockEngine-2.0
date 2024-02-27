package in.godspunky.skyblock.item.weapon;

import de.tr7zw.nbtapi.NBTItem;
import in.godspunky.skyblock.SkyBlock;
import in.godspunky.skyblock.item.*;
import in.godspunky.skyblock.util.FerocityCalculation;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTTagInt;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;
import in.godspunky.skyblock.listener.PlayerListener;
import in.godspunky.skyblock.user.User;
import in.godspunky.skyblock.util.SUtil;
import in.godspunky.skyblock.util.Sputnik;

import java.util.ArrayList;
import java.util.List;

public class Bonemerang implements ToolStatistics, MaterialFunction, Ability {
    String Activable;
    private int slotThrew;

    public Bonemerang() {
        this.Activable = "true";
        this.slotThrew = 0;
    }

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
    public void onAbilityUse(Player player, SItem sItem) {
        if (player.getInventory().getItemInHand().toString().contains("GHAST_TEAR")) {
            return;
        }
        int slot = player.getInventory().getHeldItemSlot();
        this.slotThrew = player.getInventory().getHeldItemSlot();
        ItemStack item = player.getInventory().getItemInHand();
        net.minecraft.server.v1_8_R3.ItemStack tagStack = CraftItemStack.asNMSCopy(item);
        NBTTagCompound tagCompound = tagStack.hasTag() ? tagStack.getTag() : new NBTTagCompound();
        tagCompound.set("ejectedBonemerang", new NBTTagInt(0));
        tagStack.setTag(tagCompound);
        this.releaseBone(player, item, slot);
        ItemStack bone = player.getInventory().getItemInHand();
        final boolean activate = true;
        player.playSound(player.getLocation(), Sound.SKELETON_HURT, 3.0f, 2.0f);
        if ("true" == this.Activable) {
            Player bukkitPlayer = player.getPlayer();
            ArmorStand stand = (ArmorStand) bukkitPlayer.getWorld().spawnEntity(bukkitPlayer.getLocation().add(0.0, 0.7, 0.0), EntityType.ARMOR_STAND);
            Vector teleportTo = bukkitPlayer.getLocation().getDirection().normalize().multiply(1);
            stand.setVisible(false);
            stand.setGravity(false);
            stand.setArms(false);
            stand.setMarker(true);
            stand.getEquipment().setItemInHand(SItem.of(SMaterial.BONE).getStack());
            stand.setRightArmPose(new EulerAngle(Math.toRadians(350.0), Math.toRadians(120.0), Math.toRadians(0.0)));
            stand.setBasePlate(false);
            List<Entity> goBone = new ArrayList<Entity>();
            List<Entity> backBone = new ArrayList<Entity>();
            new BukkitRunnable() {
                public int ran = 0;
                final int slot1 = player.getInventory().getHeldItemSlot();
                final NBTItem nbtItem = new NBTItem(player.getInventory().getItem(this.slot1));

                public void run() {
                    ++this.ran;
                    int slot1 = player.getInventory().getHeldItemSlot();
                    if (26 == this.ran) {
                        Bonemerang.this.returnBone(player, item, slot);
                        User.getUser(player.getUniqueId()).setBoneToZeroDamage(false);
                        stand.remove();
                        this.cancel();
                        return;
                    }
                    int i = this.ran;
                    final int num = 120;
                    Location loc = null;
                    final int angle;
                    final boolean back;
                    if (13 > i) {
                        angle = i * 20 + num;
                        back = false;
                    } else {
                        angle = i * 20 - num;
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
                        new BukkitRunnable() {

                            public void run() {
                                if (nbtItem.hasKey("ejectedBonemerang").booleanValue()) {
                                    Bonemerang.this.returnBone(player, item, slot);
                                }
                                this.cancel();
                            }
                        }.runTaskLater(SkyBlock.getPlugin(), 30L);
                        this.cancel();
                        return;
                    }
                    stand.setRightArmPose(new EulerAngle(Math.toRadians(0.0), Math.toRadians(angle), Math.toRadians(350.0)));
                    if (0 == i % 2 && 13 > i) {
                        stand.teleport(stand.getLocation().add(teleportTo).multiply(1.0));
                        stand.teleport(stand.getLocation().add(teleportTo).multiply(1.0));
                    } else if (0 == i % 2) {
                        stand.getWorld().spigot().playEffect(stand.getEyeLocation().add(0.0, 1.0, 0.0).clone().add(SUtil.random(-0.5, 0.5), 0.0, SUtil.random(-0.5, 0.5)), Effect.FLYING_GLYPH, 24, 1, 0.0f, 0.0f, 0.0f, 1.0f, 0, 64);
                        stand.teleport(stand.getLocation().subtract(loc.getDirection().normalize().multiply(1)));
                        stand.teleport(stand.getLocation().subtract(loc.getDirection().normalize().multiply(1)));
                    }
                    for (Entity e : stand.getNearbyEntities(1.0, 1.0, 1.0)) {
                        if (e instanceof LivingEntity && e != player.getPlayer()) {
                            Damageable entity = (Damageable) e;
                            if (!back && goBone.contains(e)) {
                                continue;
                            }
                            if (back && backBone.contains(e)) {
                                continue;
                            }
                            if (entity.isDead()) {
                                continue;
                            }
                            if (entity.hasMetadata("VWE")) {
                                stand.remove();
                                stand.getWorld().playEffect(stand.getLocation(), Effect.EXPLOSION, 1);
                                stand.getWorld().playEffect(stand.getLocation(), Effect.EXPLOSION, 1);
                                stand.getWorld().playEffect(stand.getLocation(), Effect.EXPLOSION, 1);
                                stand.getWorld().playEffect(stand.getLocation(), Effect.EXPLOSION, 1);
                                stand.getWorld().playEffect(stand.getLocation(), Effect.EXPLOSION, 1);
                                return;
                            }
                            if (!(entity instanceof LivingEntity)) {
                                continue;
                            }
                            if (entity instanceof Player || entity instanceof EnderDragonPart || entity instanceof Villager || entity instanceof ArmorStand || entity instanceof Item) {
                                continue;
                            }
                            if (entity instanceof ItemFrame) {
                                continue;
                            }
                            if (entity.hasMetadata("GiantSword")) {
                                continue;
                            }
                            final double decr = 1.0;
                            User user = User.getUser(player.getUniqueId());
                            if (user.toBukkitPlayer().getInventory().getHeldItemSlot() != Bonemerang.this.slotThrew) {
                                user.setBoneToZeroDamage(true);
                            }
                            Object[] atp = Sputnik.calculateDamage(player, player, sItem.getStack(), (LivingEntity) entity, true);
                            double finalDamage1 = (float) atp[0] * (back ? 2 : 1) * decr;
                            double rm = 100.0;
                            if (User.getUser(player.getUniqueId()).isBoneToZeroDamage()) {
                                rm = 5.0;
                            }
                            finalDamage1 = finalDamage1 * rm / 100.0;
                            if (!back) {
                                goBone.add(e);
                            } else {
                                backBone.add(e);
                            }
                            PlayerListener.spawnDamageInd(entity, (float) atp[2] * (back ? 2 : 1) * decr * rm / 100.0, (boolean) atp[1]);
                            FerocityCalculation.activeFerocityTimes(player, (LivingEntity) entity, (int) finalDamage1, (boolean) atp[1]);
                            user.damageEntity(entity, finalDamage1);
                            if (back) {
                                User.getUser(player.getUniqueId()).setBoneToZeroDamage(true);
                            }
                            new BukkitRunnable() {
                                public void run() {
                                    this.cancel();
                                }
                            }.runTaskLater(SkyBlock.getPlugin(), 30L);
                        }
                    }
                }
            }.runTaskTimer(SkyBlock.getPlugin(), 1L, 1L);
        }
    }

    public void releaseBone(Player player, ItemStack stack, Integer slot) {
        net.minecraft.server.v1_8_R3.ItemStack tagStack = CraftItemStack.asNMSCopy(stack);
        NBTTagCompound tagCompound = tagStack.hasTag() ? tagStack.getTag() : new NBTTagCompound();
        tagCompound.set("ejectedBonemerang", new NBTTagInt(1));
        tagStack.setTag(tagCompound);
        ItemStack itemStack = CraftItemStack.asBukkitCopy(tagStack);
        if (1 == tagStack.getTag().getInt("ejectedBonemerang")) {
            itemStack.setType(Material.GHAST_TEAR);
            player.getInventory().setItem(slot, itemStack);
        }
    }

    public void returnBone(Player player, ItemStack stack, Integer slot) {
        net.minecraft.server.v1_8_R3.ItemStack tagStack = CraftItemStack.asNMSCopy(stack);
        NBTTagCompound tagCompound = tagStack.hasTag() ? tagStack.getTag() : new NBTTagCompound();
        tagCompound.set("ejectedBonemerang", new NBTTagInt(0));
        tagStack.setTag(tagCompound);
        SItem sitem = SItem.find(player.getInventory().getItem(slot));
        if (null != sitem && SMaterial.BONEMERANG == sitem.getType() && sitem.getDataString("uuid").equals(SItem.find(stack).getDataString("uuid"))) {
            ItemStack itemStack = CraftItemStack.asBukkitCopy(tagStack);
            if (0 == tagStack.getTag().getInt("ejectedBonemerang")) {
                itemStack.setType(Material.BONE);
                player.getInventory().setItem(slot, itemStack);
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
