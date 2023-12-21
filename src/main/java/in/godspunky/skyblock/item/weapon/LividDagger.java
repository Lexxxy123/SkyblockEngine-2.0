package in.godspunky.skyblock.item.weapon;

import in.godspunky.skyblock.item.*;
import in.godspunky.skyblock.util.FerocityCalculation;
import in.godspunky.skyblock.util.Groups;
import net.minecraft.server.v1_8_R3.PacketPlayOutAnimation;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.*;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;
import in.godspunky.skyblock.SkySimEngine;
import in.godspunky.skyblock.item.*;
import in.godspunky.skyblock.listener.PlayerListener;
import in.godspunky.skyblock.user.User;
import in.godspunky.skyblock.util.SUtil;
import in.godspunky.skyblock.util.Sputnik;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class LividDagger implements ToolStatistics, MaterialFunction, Ability {
    String ACT3;
    private boolean active;

    public LividDagger() {
        this.ACT3 = "true";
    }

    @Override
    public int getBaseDamage() {
        return 210;
    }

    @Override
    public double getBaseStrength() {
        return 60.0;
    }

    @Override
    public double getBaseCritChance() {
        return 1.0;
    }

    @Override
    public double getBaseAttackSpeed() {
        return 100.0;
    }

    @Override
    public double getBaseCritDamage() {
        return 0.5;
    }

    @Override
    public String getDisplayName() {
        return "Livid Dagger";
    }

    @Override
    public Rarity getRarity() {
        return Rarity.LEGENDARY;
    }

    @Override
    public GenericItemType getType() {
        return GenericItemType.WEAPON;
    }

    @Override
    public SpecificItemType getSpecificType() {
        return SpecificItemType.SWORD;
    }

    @Override
    public String getLore() {
        return null;
    }

    @Override
    public String getAbilityName() {
        return "Throw";
    }

    @Override
    public String getAbilityDescription() {
        return "Throw your dagger at your enemies! Your Critical Hits deal " + ChatColor.BLUE + "100% " + ChatColor.GRAY + "more damage if you are behind your target.";
    }

    @Override
    public void onAbilityUse(final Player player1, final SItem sItem) {
        final Location throwLoc = player1.getLocation().add(0.0, 1.2, 0.0);
        final Vector throwVec = player1.getLocation().add(player1.getLocation().getDirection().multiply(10)).toVector().subtract(player1.getLocation().toVector()).normalize().multiply(1.2);
        for (final Player p : player1.getWorld().getPlayers()) {
            ((CraftPlayer) p).getHandle().playerConnection.sendPacket(new PacketPlayOutAnimation(((CraftLivingEntity) player1).getHandle(), 0));
        }
        final ArmorStand armorStand1 = (ArmorStand) player1.getWorld().spawnEntity(throwLoc, EntityType.ARMOR_STAND);
        armorStand1.getEquipment().setItemInHand(SItem.of(SMaterial.IRON_SWORD).getStack());
        armorStand1.setGravity(false);
        armorStand1.setVisible(false);
        final Player bukkitPlayer = player1.getPlayer();
        final Vector teleportTo = bukkitPlayer.getLocation().getDirection().normalize().multiply(1);
        final Vector[] previousVector = {throwVec};
        new BukkitRunnable() {
            private int run = -1;

            public void run() {
                final int i;
                final int ran = i = 0;
                final int num = 90;
                final Location loc = null;
                ++this.run;
                if (this.run > 100) {
                    this.cancel();
                    return;
                }
                for (int j = 0; j < 7; ++j) {
                    armorStand1.getWorld().spigot().playEffect(armorStand1.getLocation().clone().add(0.0, 1.75, 0.0), Effect.CRIT, 0, 1, (float) SUtil.random(-0.5, 0.5), (float) SUtil.random(0.0, 0.5), (float) SUtil.random(-0.5, 0.5), 0.0f, 1, 100);
                }
                if (!armorStand1.getLocation().getBlock().getType().isTransparent() || armorStand1.isOnGround()) {
                    armorStand1.remove();
                    this.cancel();
                    return;
                }
                final double xPos = armorStand1.getRightArmPose().getX();
                armorStand1.setRightArmPose(new EulerAngle(xPos + 0.35, 0.0, 0.0));
                final Vector newVector = new Vector(throwVec.getX(), previousVector[0].getY() - 0.03, throwVec.getZ());
                previousVector[0] = newVector;
                armorStand1.setVelocity(newVector);
                if (i < 13) {
                    final int angle = i * 20 + num;
                    final boolean back = false;
                } else {
                    final int angle = i * 20 - num;
                    final boolean back = true;
                }
                if (!armorStand1.getLocation().getBlock().getType().isTransparent()) {
                    armorStand1.remove();
                    this.cancel();
                    return;
                }
                if (i % 2 == 0 && i < 13) {
                    armorStand1.teleport(armorStand1.getLocation().add(teleportTo).multiply(1.0));
                } else if (i % 2 == 0) {
                    armorStand1.teleport(armorStand1.getLocation().subtract(loc.getDirection().normalize().multiply(1)));
                }
                for (final org.bukkit.entity.Entity e : armorStand1.getNearbyEntities(0.7, 0.7, 0.7)) {
                    if (e instanceof LivingEntity && e != player1.getPlayer()) {
                        final Damageable entity = (Damageable) e;
                        if (entity.isDead()) {
                            continue;
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
                        if (Groups.ENDERMAN.contains(entity.getType())) {
                            continue;
                        }
                        final User user = User.getUser(player1.getUniqueId());
                        final Object[] atp = Sputnik.calculateDamage(player1, player1, sItem.getStack(), (LivingEntity) entity, false);
                        final double finalDamage1 = (float) atp[0];
                        PlayerListener.spawnDamageInd(entity, (float) atp[2], (boolean) atp[1]);
                        FerocityCalculation.activeFerocityTimes(player1, (LivingEntity) entity, (int) finalDamage1, (boolean) atp[1]);
                        user.damageEntity(entity, finalDamage1);
                        player1.playSound(player1.getLocation(), Sound.ITEM_BREAK, 1.0f, 1.0f);
                        player1.sendMessage(Sputnik.trans("&7Your Livid Dagger hit &c1 &7enemy for &c" + SUtil.commaify(new BigDecimal(finalDamage1).setScale(1, RoundingMode.HALF_EVEN).doubleValue()) + " &7damage."));
                        this.cancel();
                        armorStand1.remove();
                        break;
                    }
                }
            }
        }.runTaskTimer(SkySimEngine.getPlugin(), 1L, 1L);
        new BukkitRunnable() {
            public void run() {
                armorStand1.remove();
                this.cancel();
            }
        }.runTaskLater(SkySimEngine.getPlugin(), 100L);
    }

    @Override
    public int getAbilityCooldownTicks() {
        return 100;
    }

    @Override
    public int getManaCost() {
        return 150;
    }
}
