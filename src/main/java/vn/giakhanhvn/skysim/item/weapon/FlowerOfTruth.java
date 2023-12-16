package vn.giakhanhvn.skysim.item.weapon;

import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;
import vn.giakhanhvn.skysim.Repeater;
import vn.giakhanhvn.skysim.SkySimEngine;
import vn.giakhanhvn.skysim.item.*;
import vn.giakhanhvn.skysim.listener.PlayerListener;
import vn.giakhanhvn.skysim.user.PlayerUtils;
import vn.giakhanhvn.skysim.user.User;
import vn.giakhanhvn.skysim.util.*;

import java.util.ArrayList;
import java.util.List;

public class FlowerOfTruth implements ToolStatistics, MaterialFunction, Ability {
    Vector teleportTo;
    String ACT3;

    public FlowerOfTruth() {
        this.ACT3 = "true";
    }

    @Override
    public int getBaseDamage() {
        return 100;
    }

    @Override
    public double getBaseStrength() {
        return 360.0;
    }

    @Override
    public String getDisplayName() {
        return "Flower of Truth";
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
        return "Heat-Seeking Rose";
    }

    @Override
    public String getAbilityDescription() {
        return "Shoots a rose that ricochets between enemies, damaging up to " + ChatColor.GREEN + "3 " + ChatColor.GRAY + "of your foes! Damage multiplies as more enemies are hit. Cost " + ChatColor.GREEN + "10.0% " + ChatColor.GRAY + "of your maximum mana";
    }

    @Override
    public void onAbilityUse(final Player player1, final SItem sItem) {
        final int manaPool = SUtil.blackMagic(PlayerUtils.STATISTICS_CACHE.get(player1.getUniqueId()).getIntelligence().addAll() + 100.0);
        final int manaCost = manaPool * 10 / 100;
        final int cost = PlayerUtils.getFinalManaCost(player1, sItem, manaCost);
        final boolean take = PlayerUtils.takeMana(player1, cost);
        if (!take) {
            player1.playSound(player1.getLocation(), Sound.ENDERMAN_TELEPORT, 1.0f, -4.0f);
            final long c = System.currentTimeMillis();
            Repeater.MANA_REPLACEMENT_MAP.put(player1.getUniqueId(), new ManaReplacement() {
                @Override
                public String getReplacement() {
                    return "" + ChatColor.RED + ChatColor.BOLD + "NOT ENOUGH MANA";
                }

                @Override
                public long getEnd() {
                    return c + 1500L;
                }
            });
            return;
        }
        final long c = System.currentTimeMillis();
        Repeater.DEFENSE_REPLACEMENT_MAP.put(player1.getUniqueId(), new DefenseReplacement() {
            @Override
            public String getReplacement() {
                return ChatColor.AQUA + "-" + cost + " Mana (" + ChatColor.GOLD + FlowerOfTruth.this.getAbilityName() + ChatColor.AQUA + ")";
            }

            @Override
            public long getEnd() {
                return c + 2000L;
            }
        });
        final Location throwLoc = player1.getLocation().add(0.0, 0.2, 0.0);
        player1.playSound(player1.getLocation(), Sound.EAT, 1.0f, 1.0f);
        final ArmorStand armorStand1 = (ArmorStand) player1.getWorld().spawnEntity(throwLoc, EntityType.ARMOR_STAND);
        armorStand1.getEquipment().setHelmet(SItem.of(SMaterial.RED_ROSE).getStack());
        armorStand1.setHeadPose(new EulerAngle(-92.55000305175781, 0.0, 0.0));
        armorStand1.setGravity(false);
        armorStand1.setVisible(false);
        final Player bukkitPlayer = player1.getPlayer();
        this.teleportTo = bukkitPlayer.getLocation().getDirection().normalize().multiply(1);
        final List<LivingEntity> le = new ArrayList<LivingEntity>();
        new BukkitRunnable() {
            private int run = -1;
            int entityhit = 0;

            public void run() {
                final Vector teleportTo = armorStand1.getLocation().getDirection().normalize().multiply(1);
                final int i;
                final int ran = i = 0;
                int j = 0;
                final int num = 90;
                final Location loc = null;
                ++this.run;
                ++j;
                if (this.run > 100) {
                    this.cancel();
                    return;
                }
                if (j >= 40) {
                    armorStand1.remove();
                    this.cancel();
                    return;
                }
                final Location locof = armorStand1.getLocation();
                locof.setY(locof.getY() + 1.0);
                if (locof.getBlock().getType() != Material.AIR) {
                    armorStand1.getWorld().playEffect(locof, Effect.SNOWBALL_BREAK, 3);
                    armorStand1.getWorld().playEffect(locof, Effect.SNOWBALL_BREAK, 10);
                    armorStand1.getWorld().playEffect(locof, Effect.SNOWBALL_BREAK, 10);
                    armorStand1.getWorld().playEffect(locof, Effect.SNOWBALL_BREAK, 10);
                    armorStand1.remove();
                    this.cancel();
                    return;
                }
                if (this.entityhit >= 3 || le.size() >= 3) {
                    armorStand1.getWorld().playEffect(locof, Effect.SNOWBALL_BREAK, 3);
                    armorStand1.getWorld().playEffect(locof, Effect.SNOWBALL_BREAK, 10);
                    armorStand1.getWorld().playEffect(locof, Effect.SNOWBALL_BREAK, 10);
                    armorStand1.getWorld().playEffect(locof, Effect.SNOWBALL_BREAK, 10);
                    armorStand1.remove();
                    this.cancel();
                    return;
                }
                if (this.entityhit < 3) {
                    for (final Entity e2 : armorStand1.getNearbyEntities(8.0, 8.0, 8.0)) {
                        if (e2 instanceof LivingEntity) {
                            if (e2.isDead()) {
                                continue;
                            }
                            if (!(e2 instanceof LivingEntity)) {
                                continue;
                            }
                            if (e2 instanceof Player || e2 instanceof EnderDragonPart || e2 instanceof Villager || e2 instanceof ArmorStand || e2 instanceof Item) {
                                continue;
                            }
                            if (e2 instanceof ItemFrame) {
                                continue;
                            }
                            if (e2.hasMetadata("GiantSword")) {
                                continue;
                            }
                            armorStand1.teleport(armorStand1.getLocation().setDirection(e2.getLocation().toVector().subtract(armorStand1.getLocation().toVector())));
                            break;
                        }
                    }
                }
                if (i % 2 == 0 && i < 13) {
                    armorStand1.teleport(armorStand1.getLocation().add(teleportTo).multiply(1.0));
                } else if (i % 2 == 0) {
                    armorStand1.teleport(armorStand1.getLocation().subtract(loc.getDirection().normalize().multiply(1)));
                }
                for (final Entity e3 : armorStand1.getNearbyEntities(1.0, 1.0, 1.0)) {
                    if (e3 instanceof LivingEntity && e3 != player1.getPlayer()) {
                        final Damageable entity = (Damageable) e3;
                        if (le.contains(e3)) {
                            continue;
                        }
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
                        final User user = User.getUser(player1.getUniqueId());
                        final Object[] atp = Sputnik.calculateDamage(player1, player1, sItem.getStack(), (LivingEntity) entity, false);
                        final double finalDamage1 = (float) atp[0];
                        le.add((LivingEntity) e3);
                        PlayerListener.spawnDamageInd(entity, (float) atp[2], (boolean) atp[1]);
                        FerocityCalculation.activeFerocityTimes(player1, (LivingEntity) entity, (int) finalDamage1, (boolean) atp[1]);
                        user.damageEntity(entity, finalDamage1);
                        ++this.entityhit;
                        int k = 0;
                        for (final Entity e4 : armorStand1.getNearbyEntities(20.0, 20.0, 20.0)) {
                            if (e4 instanceof LivingEntity) {
                                if (e4.isDead()) {
                                    continue;
                                }
                                if (!(e4 instanceof LivingEntity)) {
                                    continue;
                                }
                                if (e4 instanceof Player || e4 instanceof EnderDragonPart || e4 instanceof Villager || e4 instanceof ArmorStand || e4 instanceof Item) {
                                    continue;
                                }
                                if (e4 instanceof ItemFrame) {
                                    continue;
                                }
                                if (e4.hasMetadata("GiantSword")) {
                                    continue;
                                }
                                ++k;
                            }
                        }
                        if (k > 0) {
                            continue;
                        }
                        armorStand1.getWorld().playEffect(armorStand1.getLocation().clone().add(0.0, 1.8, 0.0), Effect.SNOWBALL_BREAK, 10);
                        armorStand1.getWorld().playEffect(armorStand1.getLocation().clone().add(0.0, 1.8, 0.0), Effect.SNOWBALL_BREAK, 10);
                        armorStand1.getWorld().playEffect(armorStand1.getLocation().clone().add(0.0, 1.8, 0.0), Effect.SNOWBALL_BREAK, 10);
                        armorStand1.getWorld().playEffect(armorStand1.getLocation().clone().add(0.0, 1.8, 0.0), Effect.SNOWBALL_BREAK, 10);
                        armorStand1.remove();
                        this.cancel();
                    }
                }
            }
        }.runTaskTimer(SkySimEngine.getPlugin(), 1L, 1L);
        new BukkitRunnable() {
            public void run() {
                if (armorStand1.isDead()) {
                    return;
                }
                armorStand1.getWorld().playEffect(armorStand1.getLocation().clone().add(0.0, 1.8, 0.0), Effect.SNOWBALL_BREAK, 10);
                armorStand1.getWorld().playEffect(armorStand1.getLocation().clone().add(0.0, 1.8, 0.0), Effect.SNOWBALL_BREAK, 10);
                armorStand1.getWorld().playEffect(armorStand1.getLocation().clone().add(0.0, 1.8, 0.0), Effect.SNOWBALL_BREAK, 10);
                armorStand1.getWorld().playEffect(armorStand1.getLocation().clone().add(0.0, 1.8, 0.0), Effect.SNOWBALL_BREAK, 10);
                armorStand1.remove();
                this.cancel();
            }
        }.runTaskLater(SkySimEngine.getPlugin(), 40L);
    }

    @Override
    public int getAbilityCooldownTicks() {
        return 20;
    }

    @Override
    public int getManaCost() {
        return 0;
    }

    @Override
    public boolean isEnchanted() {
        return true;
    }
}
