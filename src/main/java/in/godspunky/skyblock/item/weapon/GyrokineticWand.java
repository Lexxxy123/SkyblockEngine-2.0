package in.godspunky.skyblock.item.weapon;

import in.godspunky.skyblock.Skyblock;
import in.godspunky.skyblock.item.*;
import in.godspunky.skyblock.slayer.SlayerBossType;
import in.godspunky.skyblock.util.BlockFallAPI;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftZombie;
import org.bukkit.entity.*;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import in.godspunky.skyblock.Repeater;
import in.godspunky.skyblock.user.User;
import in.godspunky.skyblock.util.SUtil;
import in.godspunky.skyblock.util.Sputnik;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GyrokineticWand implements WandStatistics, MaterialFunction, Ability, Ownable {
    @Override
    public String getDisplayName() {
        return "Gyrokinetic Wand";
    }

    @Override
    public Rarity getRarity() {
        return Rarity.EPIC;
    }

    @Override
    public GenericItemType getType() {
        return GenericItemType.WAND;
    }

    @Override
    public SpecificItemType getSpecificType() {
        return SpecificItemType.WAND;
    }

    @Override
    public String getLore() {
        return null;
    }

    @Override
    public String getAbilityName() {
        return "Gravity Storm";
    }

    @Override
    public AbilityActivation getAbilityActivation() {
        return AbilityActivation.LEFT_CLICK;
    }

    @Override
    public String getAbilityDescription() {
        return Sputnik.trans("Create a large &5rift &7at aimed location, pulling all mobs together.");
    }

    @Override
    public boolean requirementsUse(final Player player, final SItem sItem) {
        return SlayerBossType.SlayerMobType.ENDERMAN.getLevelForXP(User.getUser(player.getUniqueId()).getEndermanSlayerXP()) < 6;
    }

    @Override
    public String getAbilityReq() {
        return "&cYou do not have requirement to use this item!\n&cYou need &5Enderman Slayer 6 &cto use it!\n&eTalk to Maddox in the Hub to unlock the requirement!";
    }

    @Override
    public void onAbilityUse(final Player player, final SItem sItem) {
        this.startGyrowandAbility(player);
        Repeater.MANA_REGEN_DEC.put(player.getUniqueId(), true);
        SUtil.delay(() -> Repeater.MANA_REGEN_DEC.put(player.getUniqueId(), false), 60L);
    }

    public void pullingMobsTo(final Location l) {
        for (final Entity entity : l.getWorld().getNearbyEntities(l, 8.0, 8.0, 8.0)) {
            if (entity.isDead()) {
                continue;
            }
            if (!(entity instanceof LivingEntity)) {
                continue;
            }
            if (entity.hasMetadata("GiantSword")) {
                continue;
            }
            if (entity.hasMetadata("NoAffect")) {
                continue;
            }
            if (entity instanceof Player || entity instanceof EnderDragonPart || entity instanceof Villager || entity instanceof ArmorStand) {
                continue;
            }
            if (entity instanceof EnderDragon) {
                continue;
            }
            final Location loc = entity.getLocation().clone();
            new BukkitRunnable() {
                Location look;
                int t;

                public void run() {
                    if (this.t >= 25) {
                        this.cancel();
                        return;
                    }
                    (this.look = entity.getLocation().setDirection(l.toVector().subtract(entity.getLocation().toVector()))).add(this.look.getDirection().normalize().multiply(1));
                    if (entity.getLocation().distance(l) > 0.5) {
                        final Location nl = new Location(this.look.getWorld(), this.look.getX(), this.look.getY(), this.look.getZ(), entity.getLocation().getYaw(), entity.getLocation().getPitch());
                        if (!entity.hasMetadata("LD")) {
                            entity.teleport(nl);
                        } else {
                            ((CraftZombie) entity).getHandle().setPositionRotation(nl.getX(), nl.getY(), nl.getZ(), nl.getYaw(), nl.getPitch());
                        }
                    } else {
                        final Location lc = entity.getLocation();
                        lc.setY(entity.getLocation().getY() + 0.5);
                        if (!entity.hasMetadata("LD")) {
                            entity.teleport(lc);
                        } else {
                            ((CraftZombie) entity).getHandle().setPositionRotation(lc.getX(), lc.getY(), lc.getZ(), lc.getYaw(), lc.getPitch());
                        }
                    }
                    ++this.t;
                }
            }.runTaskTimer(Skyblock.getPlugin(), 0L, 2L);
        }
    }

    public void cylinderReset(final Location loc, final int r) {
        final int cx = loc.getBlockX();
        final int cy = loc.getBlockY();
        final int cz = loc.getBlockZ();
        final World w = loc.getWorld();
        final int rSquared = r * r;
        for (int x = cx - r; x <= cx + r; ++x) {
            for (int z = cz - r; z <= cz + r; ++z) {
                if ((cx - x) * (cx - x) + (cz - z) * (cz - z) <= rSquared) {
                    final Location l = new Location(w, x, cy, z);
                    l.getBlock().getState().update();
                }
            }
        }
    }

    public void startGyrowandAbility(final Player p) {
        final Location loc = p.getLocation();
        final Location sloc = loc.clone().add(loc.getDirection().multiply(10));
        if (sloc.getBlock().getType() == Material.AIR) {
            final Location cacheLocation = sloc.getBlock().getLocation();
            for (int y = cacheLocation.getBlockY(); y > 0; --y) {
                if (cacheLocation.subtract(0.0, 1.0, 0.0).getBlock().getType() != Material.AIR) {
                    for (int i = 0; i < 40; ++i) {
                        this.a(cacheLocation.clone().add(0.0, 1.0, 0.0), (float) (i * 12));
                    }
                    this.pullingMobsTo(cacheLocation.clone().add(0.0, 1.0, 0.0));
                    break;
                }
            }
        } else if (sloc.getBlock().getType() != Material.AIR) {
            final Location cacheLocation = sloc.getBlock().getLocation();
            for (int y = cacheLocation.getBlockY(); y > 0; ++y) {
                if (cacheLocation.add(0.0, 1.0, 0.0).getBlock().getType() == Material.AIR) {
                    for (int i = 0; i < 40; ++i) {
                        this.a(cacheLocation.clone().add(0.0, 0.0, 0.0), (float) (i * 12));
                    }
                    this.pullingMobsTo(cacheLocation.clone().add(0.0, 0.0, 0.0));
                    break;
                }
            }
        }
        this.gyroWandActive(p, loc, 8, 6);
        SUtil.delay(() -> this.gyroWandActive(p, loc, 8, 6), 5L);
        SUtil.delay(() -> this.gyroWandActive(p, loc, 6, 4), 10L);
        SUtil.delay(() -> this.gyroWandActive(p, loc, 4, 2), 15L);
        SUtil.delay(() -> this.gyroWandActive(p, loc, 3, 1), 20L);
        SUtil.delay(() -> this.gyroWandActive(p, loc, 2, 1), 25L);
        SUtil.delay(() -> this.gyroWandActive(p, loc, 1, 0), 30L);
        SUtil.delay(() -> this.cylinderReset(loc, 10), 32L);
    }

    public void gyroWandActive(final Player player, final Location loc, final int arg1, final int arg2) {
        final Location sloc = loc.clone().add(loc.getDirection().multiply(10));
        if (sloc.getBlock().getType() == Material.AIR) {
            final Location cacheLocation = sloc.getBlock().getLocation();
            for (int y = cacheLocation.getBlockY(); y > 0; --y) {
                if (cacheLocation.subtract(0.0, 1.0, 0.0).getBlock().getType() != Material.AIR) {
                    this.gyroWand(player, cacheLocation.add(0.0, 0.0, 0.0), arg1, arg2);
                    this.cylinderReset(cacheLocation.add(0.0, 0.0, 0.0), 10);
                    break;
                }
            }
        } else if (sloc.getBlock().getType() != Material.AIR) {
            final Location cacheLocation = sloc.getBlock().getLocation();
            for (int y = cacheLocation.getBlockY(); y > 0; ++y) {
                if (cacheLocation.add(0.0, 1.0, 0.0).getBlock().getType() == Material.AIR) {
                    this.gyroWand(player, cacheLocation.clone().add(0.0, -1.0, 0.0), arg1, arg2);
                    this.cylinderReset(cacheLocation.add(0.0, -1.0, 0.0), 10);
                    break;
                }
            }
        }
    }

    public void gyroWand(final Player p, final Location l, final int arg0, final int arg1) {
        final Material[] mat = {Material.OBSIDIAN, Material.AIR, Material.STAINED_GLASS, Material.STAINED_CLAY, Material.AIR};
        final Material[] mat_r = {Material.OBSIDIAN, Material.STAINED_GLASS, Material.STAINED_CLAY};
        final List<Block> a = this.cylinder(l, arg0);
        final List<Block> b = this.cylinder(l, arg1);
        for (final Block bl : new ArrayList<Block>(a)) {
            if (b.contains(bl)) {
                a.remove(bl);
            }
        }
        final List<Location> aA = new ArrayList<Location>();
        for (final Block bl2 : a) {
            aA.add(bl2.getLocation().add(0.5, 0.0, 0.5));
        }
        if (arg1 != 0) {
            for (final Location loc : aA) {
                byte data = 0;
                final int r = random(0, 4);
                final Material mats = mat[r];
                if (mats == Material.STAINED_GLASS) {
                    data = 2;
                } else if (mats == Material.STAINED_CLAY) {
                    data = 11;
                }
                BlockFallAPI.sendVelocityBlock(loc, mats, data, loc.getWorld(), 10, new Vector(0.0, 0.225, 0.0));
            }
        } else {
            for (final Location loc : aA) {
                byte data = 0;
                final int r = random(0, 2);
                final Material mats = mat_r[r];
                if (mats == Material.STAINED_GLASS) {
                    data = 2;
                } else if (mats == Material.STAINED_CLAY) {
                    data = 11;
                }
                BlockFallAPI.sendVelocityBlock(loc, mats, data, loc.getWorld(), 10, new Vector(0.0, 0.225, 0.0));
                BlockFallAPI.sendVelocityBlock(l.getBlock().getLocation().add(0.5, 0.0, 0.5), mats, data, loc.getWorld(), 10, new Vector(0.0, 0.225, 0.0));
            }
        }
    }

    public static int random(int min, int max) {
        if (min < 0) {
            min = 0;
        }
        if (max < 0) {
            max = 0;
        }
        return new Random().nextInt(max - min + 1) + min;
    }

    public List<Block> cylinder(final Location loc, final int r) {
        final List<Block> bl = new ArrayList<Block>();
        final int cx = loc.getBlockX();
        final int cy = loc.getBlockY();
        final int cz = loc.getBlockZ();
        final World w = loc.getWorld();
        final int rSquared = r * r;
        for (int x = cx - r; x <= cx + r; ++x) {
            for (int z = cz - r; z <= cz + r; ++z) {
                if ((cx - x) * (cx - x) + (cz - z) * (cz - z) <= rSquared) {
                    final Location l = new Location(w, x, cy, z);
                    bl.add(l.getBlock());
                }
            }
        }
        return bl;
    }

    public void a(final Location location, final float startYaw) {
        new BukkitRunnable() {
            float cout = startYaw;
            int b = 0;
            double c = 8.0;

            public void run() {
                if (this.b >= 22) {
                    this.cancel();
                    return;
                }
                final Location loc = location.clone();
                ++this.b;
                loc.setYaw(this.cout);
                loc.setPitch(0.0f);
                if (this.c > 0.0) {
                    this.c -= 0.3;
                }
                loc.add(loc.getDirection().normalize().multiply(this.c));
                location.getWorld().spigot().playEffect(loc, Effect.WITCH_MAGIC, 0, 1, 1.0f, 1.0f, 1.0f, 0.0f, 0, 64);
                location.getWorld().spigot().playEffect(loc, Effect.WITCH_MAGIC, 0, 1, 1.0f, 1.0f, 1.0f, 0.0f, 0, 64);
                this.cout += 10.0f;
            }
        }.runTaskTimer(Skyblock.getPlugin(), 0L, 1L);
    }

    @Override
    public int getAbilityCooldownTicks() {
        return 300;
    }

    @Override
    public int getManaCost() {
        return 1500;
    }

    @Override
    public void load() {
        final ShapedRecipe recipe = new ShapedRecipe(SMaterial.HIDDEN_GYROKINETIC_WAND);
        recipe.shape("a", "b", "c");
        recipe.set('a', SMaterial.HIDDEN_GYRO_EYE);
        recipe.set('b', SMaterial.HIDDEN_REFINED_POWDER);
        recipe.set('c', SMaterial.HIDDEN_COMPRESSED_BITS, 5);
    }

    @Override
    public NBTTagCompound getData() {
        return Ownable.super.getData();
    }
}
