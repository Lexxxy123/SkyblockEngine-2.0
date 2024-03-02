package net.hypixel.skyblock.item.weapon;

import net.hypixel.skyblock.SkyBlock;
import net.hypixel.skyblock.item.*;
import net.hypixel.skyblock.user.PlayerStatistics;
import net.hypixel.skyblock.util.EntityManager;
import net.hypixel.skyblock.util.Groups;
import net.hypixel.skyblock.util.SLog;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import net.hypixel.skyblock.entity.SEntity;
import net.hypixel.skyblock.entity.SEntityType;
import net.hypixel.skyblock.user.PlayerUtils;
import net.hypixel.skyblock.user.User;
import net.hypixel.skyblock.util.SUtil;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;

public class SpiritSceptre implements ToolStatistics, MaterialFunction, Ability {
    String ACT3;

    public SpiritSceptre() {
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
    public double getBaseCritDamage() {
        return 0.5;
    }

    @Override
    public String getDisplayName() {
        return "Spirit Sceptre";
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
        return "IDK UWU";
    }

    @Override
    public String getAbilityDescription() {
        return "IDK uwu";
    }

    @Override
    public void onAbilityUse(final Player player1, final SItem sItem) {
        final Location throwLoc = player1.getLocation().add(0.0, 1.5, 0.0);
        final Vector throwVec = player1.getLocation().add(player1.getLocation().getDirection().multiply(10)).toVector().subtract(player1.getLocation().toVector()).normalize().multiply(1.2);
        final Bat armorStand1 = (Bat) player1.getWorld().spawnEntity(throwLoc, EntityType.BAT);
        armorStand1.setMetadata("GiantSword", new FixedMetadataValue(SkyBlock.getPlugin(), true));
        EntityManager.noHit(armorStand1);
        EntityManager.shutTheFuckUp(armorStand1);
        final Player bukkitPlayer = player1.getPlayer();
        final Vector teleportTo = bukkitPlayer.getLocation().getDirection().normalize().multiply(1);
        final Vector[] previousVector = {throwVec};
        final Collection<Entity> damaged = new ArrayList<Entity>();
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
                if (!armorStand1.getLocation().getBlock().getType().isTransparent() || armorStand1.isOnGround()) {
                    armorStand1.getWorld().playSound(armorStand1.getLocation(), Sound.EXPLODE, 2.0f, 1.0f);
                    armorStand1.getWorld().playEffect(armorStand1.getLocation(), Effect.EXPLOSION_HUGE, 3);
                    armorStand1.remove();
                    this.cancel();
                    return;
                }
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
                    armorStand1.getWorld().playSound(armorStand1.getLocation(), Sound.EXPLODE, 2.0f, 1.0f);
                    armorStand1.getWorld().playEffect(armorStand1.getLocation(), Effect.EXPLOSION_HUGE, 3);
                    armorStand1.remove();
                    this.cancel();
                    return;
                }
                if (i % 2 == 0 && i < 13) {
                    armorStand1.teleport(armorStand1.getLocation().add(teleportTo).multiply(1.0));
                    armorStand1.teleport(armorStand1.getLocation().add(teleportTo).multiply(1.0));
                } else if (i % 2 == 0) {
                    armorStand1.teleport(armorStand1.getLocation().subtract(loc.getDirection().normalize().multiply(1)));
                    armorStand1.teleport(armorStand1.getLocation().subtract(loc.getDirection().normalize().multiply(1)));
                }
                final PlayerStatistics statistics = PlayerUtils.STATISTICS_CACHE.get(player1.getUniqueId());
                final int manaPool = SUtil.blackMagic(100.0 + statistics.getIntelligence().addAll());
                if (SpiritSceptre.this.ACT3 == "true") {
                    for (final Entity e : armorStand1.getNearbyEntities(5.0, 5.0, 5.0)) {
                        SpiritSceptre.this.ACT3 = "false";
                        if (e instanceof Damageable && e != player1.getPlayer()) {
                            final Damageable entity = (Damageable) e;
                            int baseMagicDmg = 2000;
                            final PlayerInventory inv = player1.getInventory();
                            final SItem helmet = SItem.find(inv.getHelmet());
                            if (helmet != null) {
                                if (helmet.getType() == SMaterial.DARK_GOGGLES) {
                                    baseMagicDmg += baseMagicDmg * 25 / 100;
                                } else if (helmet.getType() == SMaterial.SHADOW_GOGGLES) {
                                    baseMagicDmg += baseMagicDmg * 35 / 100;
                                } else if (helmet.getType() == SMaterial.WITHER_GOGGLES) {
                                    baseMagicDmg += baseMagicDmg * 45 / 100;
                                }
                            }
                            final double finalDamage = baseMagicDmg * (manaPool / 100 * 0.2 + 1.0);
                            if (entity instanceof Player && !entity.hasMetadata("NPC")) {
                                continue;
                            }
                            final SEntity sbEntity = SEntity.findSEntity(entity);
                            for (final Entity entity2 : armorStand1.getWorld().getNearbyEntities(armorStand1.getLocation().add(armorStand1.getLocation().getDirection().multiply(1.0)), 5.0, 5.0, 5.0)) {
                                if (entity2.isDead()) {
                                    continue;
                                }
                                if (!(entity2 instanceof LivingEntity)) {
                                    continue;
                                }
                                if (entity2 instanceof Player || entity2 instanceof EnderDragonPart || entity2 instanceof Villager || entity2 instanceof ArmorStand || entity2 instanceof Item) {
                                    continue;
                                }
                                if (entity2 instanceof ItemFrame) {
                                    continue;
                                }
                                if (Groups.ENDERMAN.contains(entity2.getType())) {
                                    continue;
                                }
                                final User user = User.getUser(player1.getUniqueId());
                                final ArmorStand stand3 = (ArmorStand) new SEntity(entity2.getLocation().clone().add(SUtil.random(-1.5, 1.5), 1.0, SUtil.random(-1.5, 1.5)), SEntityType.UNCOLLIDABLE_ARMOR_STAND).getEntity();
                                stand3.setCustomName("" + ChatColor.GRAY + (int) finalDamage);
                                stand3.setCustomNameVisible(true);
                                stand3.setGravity(false);
                                stand3.setVisible(false);
                                user.damageEntity((Damageable) entity2, finalDamage);
                                if (PlayerUtils.Debugmsg.debugmsg) {
                                    SLog.info("[DEBUG] " + player1.getName() + " have dealt " + finalDamage + " (Bat Wand Ability)");
                                }
                                armorStand1.remove();
                                new BukkitRunnable() {
                                    public void run() {
                                        stand3.remove();
                                        this.cancel();
                                    }
                                }.runTaskLater(SkyBlock.getPlugin(), 30L);
                                break;
                            }
                            break;
                        }
                    }
                }
                int count1 = 0;
                final PlayerStatistics statistics2 = PlayerUtils.STATISTICS_CACHE.get(player1.getUniqueId());
                final int manaPool2 = SUtil.blackMagic(100.0 + statistics.getIntelligence().addAll());
                int baseMagicDmg2 = 2000;
                final PlayerInventory inv = player1.getInventory();
                final SItem helmet = SItem.find(inv.getHelmet());
                if (helmet != null) {
                    if (helmet.getType() == SMaterial.DARK_GOGGLES) {
                        baseMagicDmg2 += baseMagicDmg2 * 25 / 100;
                    } else if (helmet.getType() == SMaterial.SHADOW_GOGGLES) {
                        baseMagicDmg2 += baseMagicDmg2 * 35 / 100;
                    } else if (helmet.getType() == SMaterial.WITHER_GOGGLES) {
                        baseMagicDmg2 += baseMagicDmg2 * 45 / 100;
                    }
                }
                final double baseDamage1 = baseMagicDmg2 * (manaPool / 100 * 0.2 + 1.0);
                for (final Entity entity3 : armorStand1.getWorld().getNearbyEntities(armorStand1.getLocation().add(armorStand1.getLocation().getDirection().multiply(1.0)), 5.0, 4.0, 5.0)) {
                    if (!(entity3 instanceof LivingEntity)) {
                        continue;
                    }
                    if (entity3 instanceof Player || entity3 instanceof EnderDragonPart || entity3 instanceof Villager) {
                        continue;
                    }
                    if (entity3 instanceof ArmorStand) {
                        continue;
                    }
                    if (entity3.hasMetadata("GiantSword")) {
                        continue;
                    }
                    ++count1;
                }
                if (count1 > 0) {
                    final double AllDmg = baseDamage1 * count1;
                    final DecimalFormat df = new DecimalFormat("###,###");
                    final String AllDmgReal = df.format(AllDmg);
                    if (count1 > 1) {
                        player1.sendMessage(ChatColor.GRAY + "Your Spirit Sceptre hit " + ChatColor.RED + count1 + ChatColor.GRAY + " enemies for " + ChatColor.RED + AllDmgReal + ChatColor.GRAY + " damage.");
                    } else if (count1 == 1) {
                        player1.sendMessage(ChatColor.GRAY + "Your Spirit Sceptre hit " + ChatColor.RED + count1 + ChatColor.GRAY + " enemy for " + ChatColor.RED + AllDmgReal + ChatColor.GRAY + " damage.");
                    } else {
                        player1.sendMessage(ChatColor.GRAY + "Your Spirit Sceptre hit " + ChatColor.RED + count1 + ChatColor.GRAY + " enemies for " + ChatColor.RED + AllDmgReal + ChatColor.GRAY + " damage.");
                    }
                    if (PlayerUtils.Debugmsg.debugmsg) {
                        SLog.info("[DEBUG] " + player1.getName() + " have dealt " + (float) baseDamage1 + " for each enemies and hit total of " + count1 + " enemies, for a total of " + AllDmg + " damage! (Spirit Sceptre ability)");
                    }
                    count1 = 0;
                }
            }
        }.runTaskTimer(SkyBlock.getPlugin(), 1L, 2L);
        new BukkitRunnable() {
            public void run() {
                armorStand1.remove();
                this.cancel();
            }
        }.runTaskLater(SkyBlock.getPlugin(), 150L);
    }

    @Override
    public int getAbilityCooldownTicks() {
        return 0;
    }

    @Override
    public int getManaCost() {
        return 150;
    }
}
