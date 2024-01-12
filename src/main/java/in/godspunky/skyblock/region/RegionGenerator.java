package in.godspunky.skyblock.region;

import org.bukkit.Location;

public class RegionGenerator {
    private final String modificationType;
    private String name;
    private Location firstLocation;
    private Location secondLocation;
    private RegionType type;
    private int phase;

    public RegionGenerator(final String modificationType, final String name, final RegionType type) {
        this.modificationType = modificationType;
        this.name = name;
        this.type = type;
        this.phase = 1;
    }

    public String getModificationType() {
        return this.modificationType;
    }

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Location getFirstLocation() {
        return this.firstLocation;
    }

    public void setFirstLocation(final Location firstLocation) {
        this.firstLocation = firstLocation;
    }

    public Location getSecondLocation() {
        return this.secondLocation;
    }

    public void setSecondLocation(final Location secondLocation) {
        this.secondLocation = secondLocation;
    }

    public RegionType getType() {
        return this.type;
    }

    public void setType(final RegionType type) {
        this.type = type;
    }

    public int getPhase() {
        return this.phase;
    }

    public void setPhase(final int phase) {
        this.phase = phase;
    }
}
