/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraft.server.v1_8_R3.EnumParticle
 *  net.minecraft.server.v1_8_R3.Packet
 *  net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.Sound
 *  org.bukkit.World
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer
 *  org.bukkit.entity.ArmorStand
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 *  org.bukkit.util.Vector
 */
package net.hypixel.skyblock.features.launchpads;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.hypixel.skyblock.SkyBlock;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class LaunchPadHandler {
    public static final String LAUNCHPAD_FILE_NAME = "launchpads.yml";
    private final File file = new File(((SkyBlock)SkyBlock.getPlugin(SkyBlock.class)).getDataFolder() + File.separator + "launchpads.yml");
    private final List<Player> onLaunchpad = new ArrayList<Player>();

    public LaunchPadHandler() {
        this.init();
    }

    public String closeTo(Player player) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration((File)this.file);
        ArrayList pads = new ArrayList(config.getConfigurationSection("launchpads").getKeys(false));
        for (String pad : pads) {
            Location from = this.deserializeLocation((String)this.getField(pad, "from"));
            if (!player.getWorld().equals(from.getWorld()) || !(player.getLocation().distance(from) < 2.0)) continue;
            return pad;
        }
        return "NONE";
    }

    public void savePad(String start, String end, Location from, Location to, Location teleport) {
        Location infront = from.clone();
        infront.setY(from.getY() + 4.0);
        String id = "launchpads." + start + "_to_" + end;
        YamlConfiguration config = YamlConfiguration.loadConfiguration((File)this.file);
        config.set(id + ".start", (Object)start);
        config.set(id + ".end", (Object)end);
        config.set(id + ".from", (Object)this.serializeLocation(from));
        config.set(id + ".to", (Object)this.serializeLocation(to));
        config.set(id + ".infront", (Object)this.serializeLocation(infront));
        config.set(id + ".teleport", (Object)this.serializeLocation(teleport));
        try {
            config.save(this.file);
        } catch (IOException iOException) {
            // empty catch block
        }
    }

    private String serializeLocation(Location location) {
        return location.getWorld().getName() + "," + location.getX() + "," + location.getY() + "," + location.getZ() + "," + location.getPitch() + "," + location.getYaw();
    }

    private Location deserializeLocation(String input) {
        String[] parts = input.split(",");
        World world = Bukkit.getWorld((String)parts[0]);
        double x = Double.parseDouble(parts[1]);
        double y = Double.parseDouble(parts[2]);
        double z = Double.parseDouble(parts[3]);
        float pitch = Float.parseFloat(parts[4]);
        float yaw = Float.parseFloat(parts[5]);
        return new Location(world, x, y, z, yaw, pitch);
    }

    public void launch(final Player player, String padName) {
        if (this.onLaunchpad.contains(player)) {
            return;
        }
        final Location to = this.deserializeLocation((String)this.getField(padName, "to"));
        Location front = this.deserializeLocation((String)this.getField(padName, "infront"));
        final Location teleport = this.deserializeLocation((String)this.getField(padName, "teleport"));
        player.teleport(front);
        this.onLaunchpad.add(player);
        final ArmorStand am = (ArmorStand)player.getWorld().spawnEntity(player.getLocation(), EntityType.ARMOR_STAND);
        am.setVisible(false);
        am.setPassenger((Entity)player);
        PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(EnumParticle.EXPLOSION_HUGE, true, (float)player.getLocation().getBlockX(), (float)player.getLocation().getBlockY(), (float)player.getLocation().getBlockZ(), 0.0f, 0.0f, 0.0f, 0.0f, 2, new int[0]);
        for (Player p : player.getLocation().getWorld().getPlayers()) {
            ((CraftPlayer)p).getHandle().playerConnection.sendPacket((Packet)packet);
            p.playSound(player.getLocation(), Sound.EXPLODE, 10.0f, 2.0f);
        }
        new BukkitRunnable(){
            final double x1 = 0.0;
            final double x3;
            final double x2;
            final double y1 = 0.0;
            final double y3;
            final double A3;
            final double D3;
            final double a;
            final double b;
            final double c;
            double xC;
            {
                double d = to.distance(player.getLocation());
                ((Object)((Object)this)).getClass();
                this.x3 = d - 0.0;
                this.x2 = this.x3 / 3.0;
                this.y1 = 0.0;
                this.y3 = Math.abs(to.getBlockY() - player.getLocation().getBlockY()) % 10;
                double d2 = -this.x2 + this.x3;
                ((Object)((Object)this)).getClass();
                double d3 = -(d2 / (-0.0 + this.x2));
                ((Object)((Object)this)).getClass();
                ((Object)((Object)this)).getClass();
                this.A3 = d3 * (-(0.0 * 0.0) + this.x2 * this.x2) - this.x2 * this.x2 + this.x3 * this.x3;
                double d4 = -this.x2 + this.x3;
                ((Object)((Object)this)).getClass();
                double d5 = -(d4 / (-0.0 + this.x2));
                ((Object)((Object)this)).getClass();
                this.D3 = d5 * (-0.0 + this.x2) - this.x2 + this.y3;
                this.a = this.D3 / this.A3;
                ((Object)((Object)this)).getClass();
                double d6 = -0.0 + this.x2;
                ((Object)((Object)this)).getClass();
                ((Object)((Object)this)).getClass();
                double d7 = d6 - (-(0.0 * 0.0) + this.x2 * this.x2) * this.a;
                ((Object)((Object)this)).getClass();
                this.b = d7 / (-0.0 + this.x2);
                ((Object)((Object)this)).getClass();
                ((Object)((Object)this)).getClass();
                ((Object)((Object)this)).getClass();
                ((Object)((Object)this)).getClass();
                this.c = 0.0 - this.a * 0.0 * 0.0 - this.b * 0.0;
                this.xC = 0.0;
            }

            public void run() {
                if (to.distance(am.getLocation()) < 2.0 || this.xC > 200.0 || !LaunchPadHandler.this.onLaunchpad.contains(player)) {
                    new BukkitRunnable(){

                        public void run() {
                            player.teleport(teleport);
                            player.playSound(player.getLocation(), Sound.HORSE_ARMOR, 10.0f, 2.0f);
                        }
                    }.runTask((Plugin)SkyBlock.getPlugin());
                    am.remove();
                    LaunchPadHandler.this.onLaunchpad.remove(player);
                    this.cancel();
                }
                LaunchPadHandler.this.moveToward((Entity)am, LaunchPadHandler.this.yCalculate(this.a, this.b, this.c, this.xC), to);
                this.xC += 0.84;
            }
        }.runTaskTimerAsynchronously((Plugin)SkyBlock.getPlugin(SkyBlock.class), 1L, 1L);
    }

    private void moveToward(Entity player, double yC, Location to) {
        Location loc = player.getLocation();
        double x = loc.getX() - to.getX();
        double y = loc.getY() - to.getY() - Math.max(yC, 0.0);
        double z = loc.getZ() - to.getZ();
        Vector velocity = new Vector(x, y, z).normalize().multiply(-0.8);
        player.setVelocity(velocity);
    }

    private double yCalculate(double a, double b, double c, double x) {
        return a * x * x + x * b + c;
    }

    public void init() {
        if (this.file.exists()) {
            return;
        }
        try {
            boolean success = this.file.createNewFile();
            if (!success) {
                return;
            }
            YamlConfiguration.loadConfiguration((File)this.file).save(this.file);
        } catch (Exception exception) {
            // empty catch block
        }
    }

    public Object getField(String name, String field) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration((File)this.file);
        return config.get("launchpads." + name + "." + field);
    }
}

