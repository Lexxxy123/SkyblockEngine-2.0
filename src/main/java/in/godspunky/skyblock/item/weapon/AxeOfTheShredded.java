package in.godspunky.skyblock.item.weapon;

import in.godspunky.skyblock.Repeater;
import in.godspunky.skyblock.Skyblock;
import in.godspunky.skyblock.item.*;
import in.godspunky.skyblock.listener.PlayerListener;
import in.godspunky.skyblock.user.PlayerUtils;
import in.godspunky.skyblock.user.User;
import in.godspunky.skyblock.util.*;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AxeOfTheShredded implements ToolStatistics, MaterialFunction, Ability {
    private static final Map<Player, Integer> axeThrows;

    static {
        axeThrows = new HashMap<Player, Integer>();
    }

    int currentAxeThrows;

    @Override
    public int getBaseDamage() {
        return 145;
    }

    @Override
    public double getBaseStrength() {
        return 115.0;
    }

    @Override
    public String getDisplayName() {
        return "Axe of the Shredded";
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
    public String getAbilityName() {
        return "Throw";
    }

    @Override
    public String getAbilityDescription() {
        return "Throw your axe damaging all enemies in its path dealing " + ChatColor.RED + "10%" + ChatColor.GRAY + " melee damage. Consecutive throws stack " + ChatColor.RED + "2x " + ChatColor.GRAY + "damage but cost " + ChatColor.BLUE + "2x " + ChatColor.GRAY + "mana up to 16x";
    }

    @Override
    public void onAbilityUse(final Player player1, final SItem sItem) {
        final List<LivingEntity> le = new ArrayList<LivingEntity>();
        if (!AxeOfTheShredded.axeThrows.containsKey(player1)) {
            AxeOfTheShredded.axeThrows.put(player1, 0);
        }
        AxeOfTheShredded.axeThrows.put(player1, AxeOfTheShredded.axeThrows.get(player1) + 1);
        final int currentAxeThrows1 = AxeOfTheShredded.axeThrows.get(player1);
        double damageBoost = 0.0;
        int counter = 0;
        int manaCost = 0;
        this.currentAxeThrows = AxeOfTheShredded.axeThrows.get(player1);
        switch (this.currentAxeThrows - 1) {
            case 0:
                manaCost = 20;
                damageBoost = 0.1;
                counter = 0;
                break;
            case 1:
                manaCost = 40;
                damageBoost = 0.2;
                counter = 1;
                break;
            case 2:
                manaCost = 80;
                damageBoost = 0.4;
                counter = 2;
                break;
            case 3:
                manaCost = 160;
                damageBoost = 0.8;
                counter = 3;
                break;
            default:
                manaCost = 320;
                damageBoost = 1.6;
                counter = 4;
                break;
        }
        final double damageBoost2 = damageBoost;
        final int manaPool = SUtil.blackMagic(PlayerUtils.STATISTICS_CACHE.get(player1.getUniqueId()).getIntelligence().addAll() + 100.0);
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
                return ChatColor.AQUA + "-" + cost + " Mana (" + ChatColor.GOLD + AxeOfTheShredded.this.getAbilityName() + ChatColor.AQUA + ")";
            }

            @Override
            public long getEnd() {
                return c + 2000L;
            }
        });
        final Location throwLoc = player1.getLocation().add(0.0, 0.5, 0.0);
        final Vector throwVec = player1.getLocation().add(player1.getLocation().getDirection().multiply(10)).toVector().subtract(player1.getLocation().toVector()).normalize().multiply(1.2);
        final ArmorStand armorStand1 = (ArmorStand) player1.getWorld().spawnEntity(throwLoc, EntityType.ARMOR_STAND);
        armorStand1.getEquipment().setItemInHand(SItem.of(SMaterial.AXE_OF_THE_SHREDDED).getStack());
        armorStand1.setGravity(false);
        armorStand1.setVisible(false);
        armorStand1.setMarker(true);
        final Player bukkitPlayer = player1.getPlayer();
        final Vector teleportTo = bukkitPlayer.getLocation().getDirection().normalize().multiply(1);
        final Vector[] previousVector = {throwVec};
        if (PlayerUtils.Debugmsg.debugmsg) {
            SLog.info("[AOTS-DEBUG] " + player1.getName() + "'s AOTS Log. Axe Throws Count: " + currentAxeThrows1 + ". Mana Cost: " + cost + ". Data: " + counter);
        }
        new BukkitRunnable() {
            private int run = -1;

            public void run() {
                int j = 0;
                final int i;
                final int ran = i = 0;
                final int num = 90;
                final Location loc = null;
                ++this.run;
                ++j;
                if (this.run > 100) {
                    this.cancel();
                    return;
                }
                if (armorStand1.isDead()) {
                    this.cancel();
                    return;
                }
                final Location locof = armorStand1.getLocation();
                locof.setY(locof.getY() + 1.0);
                if (locof.getBlock().getType() != Material.AIR) {
                    armorStand1.remove();
                    this.cancel();
                    return;
                }
                if (j >= 25) {
                    armorStand1.remove();
                    this.cancel();
                    return;
                }
                final double xPos = armorStand1.getRightArmPose().getX();
                armorStand1.setRightArmPose(new EulerAngle(xPos + 0.5, 0.0, 0.0));
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
                if (locof.getBlock().getType() != Material.AIR && locof.getBlock().getType() != Material.WATER) {
                    armorStand1.remove();
                    this.cancel();
                    return;
                }
                if (i % 2 == 0 && i < 13) {
                    armorStand1.teleport(armorStand1.getLocation().add(teleportTo).multiply(1.0));
                } else if (i % 2 == 0) {
                    armorStand1.teleport(armorStand1.getLocation().subtract(loc.getDirection().normalize().multiply(1)));
                }
                for (final Entity e : armorStand1.getNearbyEntities(1.0, 1.0, 1.0)) {
                    if (e instanceof LivingEntity && e != player1.getPlayer()) {
                        final Damageable entity = (Damageable) e;
                        if (le.contains(e)) {
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
                        if (entity.hasMetadata("VWE")) {
                            continue;
                        }
                        if (Groups.ENDERMAN.contains(entity.getType())) {
                            continue;
                        }
                        final User user = User.getUser(player1.getUniqueId());
                        final Object[] atp = Sputnik.calculateDamage(player1, player1, sItem.getStack(), (LivingEntity) entity, false);
                        final double finalDamage1 = (float) atp[0] * damageBoost2;
                        le.add((LivingEntity) e);
                        PlayerListener.spawnDamageInd(entity, (float) atp[2] * damageBoost2, (boolean) atp[1]);
                        FerocityCalculation.activeFerocityTimes(player1, (LivingEntity) entity, (int) finalDamage1, (boolean) atp[1]);
                        user.damageEntity(entity, finalDamage1);
                        if (damageBoost2 == 1.6) {
                            AxeOfTheShredded.axeThrows.replace(player1, 0);
                        }
                        player1.setHealth(Math.min(player1.getMaxHealth(), player1.getHealth() + 50.0));
                    }
                }
            }
        }.runTaskTimer(Skyblock.getPlugin(), 1L, 1L);
        new BukkitRunnable() {
            public void run() {
                armorStand1.remove();
                this.cancel();
            }
        }.runTaskLater(Skyblock.getPlugin(), 40L);
    }

    @Override
    public int getAbilityCooldownTicks() {
        return 5;
    }

    @Override
    public boolean displayCooldown() {
        return false;
    }

    @Override
    public int getManaCost() {
        return 0;
    }

    @Override
    public String getLore() {
        return "Heal " + ChatColor.RED + "50" + ChatColor.RED + "❤" + ChatColor.GRAY + " per hit. Deal " + ChatColor.GREEN + "+250% " + ChatColor.GRAY + "damage to Zombies. Receive " + ChatColor.GREEN + "25% " + ChatColor.GRAY + "less damage from Zombies when held.";
    }
}
