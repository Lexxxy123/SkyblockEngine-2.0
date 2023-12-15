package vn.giakhanhvn.skysim.extra.beam;

import org.bukkit.plugin.Plugin;
import vn.giakhanhvn.skysim.SkySimEngine;
import com.google.common.base.Preconditions;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.entity.Player;
import org.bukkit.Location;

import java.util.UUID;

public class ClientBeam {
    private final UUID worldUID;
    private final double viewingRadiusSquared;
    private final long updateDelay;
    private boolean isActive;
    private final LocationTargetBeam beam;
    private Location startingPosition;
    private Location endingPosition;
    private Player player;
    private boolean isViewing;
    private BukkitRunnable runnable;

    public ClientBeam(final Player player, final Location startingPosition, final Location endingPosition) {
        this(player, startingPosition, endingPosition, 100.0, 5L);
    }

    public ClientBeam(final Player player, final Location startingPosition, final Location endingPosition, final double viewingRadius, final long updateDelay) {
        Preconditions.checkNotNull((Object) player, "player cannot be null");
        Preconditions.checkArgument(player.isOnline(), "The player must be online");
        Preconditions.checkNotNull((Object) startingPosition, "startingPosition cannot be null");
        Preconditions.checkNotNull((Object) endingPosition, "endingPosition cannot be null");
        Preconditions.checkState(startingPosition.getWorld().equals(endingPosition.getWorld()), "startingPosition and endingPosition must be in the same world");
        Preconditions.checkArgument(viewingRadius > 0.0, "viewingRadius must be positive");
        Preconditions.checkArgument(updateDelay >= 1L, "viewingRadius must be a natural number");
        this.worldUID = startingPosition.getWorld().getUID();
        this.viewingRadiusSquared = viewingRadius * viewingRadius;
        this.updateDelay = updateDelay;
        this.isActive = false;
        this.beam = new LocationTargetBeam(startingPosition, endingPosition);
        this.startingPosition = startingPosition;
        this.endingPosition = endingPosition;
        this.player = player;
        this.isViewing = false;
    }

    public void start() {
        Preconditions.checkState(!this.isActive, "The beam must be disabled in order to start it");
        Preconditions.checkState(this.player != null && !this.player.isOnline(), "The player must be online");
        this.isActive = true;
        (this.runnable = new ClientBeamUpdater()).runTaskTimer(SkySimEngine.getPlugin(), 0L, this.updateDelay);
    }

    public void stop() {
        Preconditions.checkState(this.isActive, "The beam must be enabled in order to stop it");
        this.isActive = false;
        this.isViewing = false;
        if (this.player != null && !this.player.isOnline()) {
            this.player = null;
        }
        this.runnable.cancel();
        this.runnable = null;
    }

    public void setStartingPosition(final Location location) {
        Preconditions.checkArgument(location.getWorld().getUID().equals(this.worldUID), "location must be in the same world as this beam");
        Preconditions.checkState(this.player != null && !this.player.isOnline(), "The player must be online");
        this.startingPosition = location;
        this.beam.setStartingPosition(this.player, location);
    }

    public void setEndingPosition(final Location location) {
        Preconditions.checkArgument(location.getWorld().getUID().equals(this.worldUID), "location must be in the same world as this beam");
        Preconditions.checkState(this.player != null && !this.player.isOnline(), "The player must be online");
        this.endingPosition = location;
        this.beam.setEndingPosition(this.player, location);
    }

    public void update() {
        if (this.player == null || !this.player.isOnline()) {
            this.stop();
        }
        if (this.isActive) {
            if (!this.player.getWorld().getUID().equals(this.worldUID)) {
                this.stop();
            }
            if (this.isCloseEnough(this.player.getLocation())) {
                if (!this.isViewing) {
                    this.beam.start(this.player);
                    this.isViewing = true;
                }
            } else if (this.isViewing) {
                this.beam.cleanup(this.player);
                this.isViewing = false;
            }
        }
    }

    public boolean isActive() {
        return this.isActive;
    }

    public boolean isViewing() {
        return this.isViewing;
    }

    private boolean isCloseEnough(final Location location) {
        return this.startingPosition.distanceSquared(location) <= this.viewingRadiusSquared || this.endingPosition.distanceSquared(location) <= this.viewingRadiusSquared;
    }

    private class ClientBeamUpdater extends BukkitRunnable {
        public void run() {
            ClientBeam.this.update();
        }
    }
}
