package in.godspunky.skyblock.extra.beam;

import com.google.common.base.Preconditions;
import in.godspunky.skyblock.Skyblock;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

public class Beam {
    private final String worldname;
    private final double viewingRadiusSquared;
    private final long updateDelay;
    private boolean isActive;
    private final LocationTargetBeam beam;
    private Location startingPosition;
    private Location endingPosition;
    private final Set<UUID> viewers;
    private BukkitRunnable runnable;

    public Beam(final Location startingPosition, final Location endingPosition) {
        this(startingPosition, endingPosition, 100.0, 5L);
    }

    public Beam(final Location startingPosition, final Location endingPosition, final double viewingRadius, final long updateDelay) {
        Preconditions.checkNotNull((Object) startingPosition, "startingPosition cannot be null");
        Preconditions.checkNotNull((Object) endingPosition, "endingPosition cannot be null");
        Preconditions.checkState(startingPosition.getWorld().equals(endingPosition.getWorld()), "startingPosition and endingPosition must be in the same world");
        Preconditions.checkArgument(viewingRadius > 0.0, "viewingRadius must be positive");
        Preconditions.checkArgument(updateDelay >= 1L, "viewingRadius must be a natural number");
        this.worldname = startingPosition.getWorld().getName();
        this.viewingRadiusSquared = viewingRadius * viewingRadius;
        this.updateDelay = updateDelay;
        this.isActive = false;
        this.beam = new LocationTargetBeam(startingPosition, endingPosition);
        this.startingPosition = startingPosition;
        this.endingPosition = endingPosition;
        this.viewers = new HashSet<UUID>();
    }

    public void start() {
        Preconditions.checkState(!this.isActive, "The beam must be disabled in order to start it");
        this.isActive = true;
        (this.runnable = new BeamUpdater()).runTaskTimer(Skyblock.getPlugin(), 0L, this.updateDelay);
    }

    public void stop() {
        Preconditions.checkState(this.isActive, "The beam must be enabled in order to stop it");
        this.isActive = false;
        for (final UUID uuid : this.viewers) {
            final Player player = Bukkit.getPlayer(uuid);
            if (player != null && player.getWorld().getName().equalsIgnoreCase(this.worldname) && this.isCloseEnough(player.getLocation())) {
                this.beam.cleanup(player);
            }
        }
        this.viewers.clear();
        this.runnable.cancel();
        this.runnable = null;
    }

    public void setStartingPosition(final Location location) {
        Preconditions.checkArgument(location.getWorld().getName().equalsIgnoreCase(this.worldname), "location must be in the same world as this beam");
        this.startingPosition = location;
        final Iterator<UUID> iterator = this.viewers.iterator();
        while (iterator.hasNext()) {
            final UUID uuid = iterator.next();
            final Player player = Bukkit.getPlayer(uuid);
            if (player == null || !player.isOnline() || !player.getWorld().getName().equalsIgnoreCase(this.worldname) || !this.isCloseEnough(player.getLocation())) {
                iterator.remove();
            } else {
                this.beam.setStartingPosition(player, location);
            }
        }
    }

    public void setEndingPosition(final Location location) {
        Preconditions.checkArgument(location.getWorld().getName().equalsIgnoreCase(this.worldname), "location must be in the same world as this beam");
        this.endingPosition = location;
        final Iterator<UUID> iterator = this.viewers.iterator();
        while (iterator.hasNext()) {
            final UUID uuid = iterator.next();
            final Player player = Bukkit.getPlayer(uuid);
            if (!player.isOnline() || !player.getWorld().getName().equalsIgnoreCase(this.worldname) || !this.isCloseEnough(player.getLocation())) {
                iterator.remove();
            } else {
                this.beam.setEndingPosition(player, location);
            }
        }
    }

    public void update() {
        if (this.isActive) {
            for (final Player player : Bukkit.getOnlinePlayers()) {
                final UUID uuid = player.getUniqueId();
                if (!player.getWorld().getName().equalsIgnoreCase(this.worldname)) {
                    this.viewers.remove(uuid);
                } else if (this.isCloseEnough(player.getLocation())) {
                    if (this.viewers.contains(uuid)) {
                        continue;
                    }
                    this.beam.start(player);
                    this.viewers.add(uuid);
                } else {
                    if (!this.viewers.contains(uuid)) {
                        continue;
                    }
                    this.beam.cleanup(player);
                    this.viewers.remove(uuid);
                }
            }
        }
    }

    public boolean isActive() {
        return this.isActive;
    }

    public boolean isViewing(final Player player) {
        return this.viewers.contains(player.getUniqueId());
    }

    private boolean isCloseEnough(final Location location) {
        return this.startingPosition.distanceSquared(location) <= this.viewingRadiusSquared || this.endingPosition.distanceSquared(location) <= this.viewingRadiusSquared;
    }

    public boolean isShitAss() {
        return true;
    }

    private class BeamUpdater extends BukkitRunnable {
        public void run() {
            Beam.this.update();
        }
    }
}
