package in.godspunky.skyblock.user;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class PetsFollow {
    public static HashMap<UUID, PetsFollow> pets;

    static {
        PetsFollow.pets = new HashMap<UUID, PetsFollow>();
    }

    private Player player;
    private ArmorStand nametag;
    private ArmorStand petitem;
    private ArrayList<Location> locs;
    private Location loc;

    public void setPetAS(final ArmorStand as) {
        this.petitem = as;
    }

    public void addLocation(final Location loc) {
        this.locs.add(loc);
    }

    public void removeLoc(final int index) {
        if (index < this.locs.size() && this.locs.size() != 0) {
            this.locs.remove(index);
        }
    }

    public void removeLoc(final Location loc) {
        this.locs.remove(loc);
    }

    public Location getLoc() {
        return this.loc;
    }

    public void setLoc(final Location loc) {
        this.loc = loc;
    }

    public Player getPlayer() {
        return this.player;
    }

    public void setPlayer(final Player player) {
        this.player = player;
    }

    public ArmorStand getNameTagAS() {
        return this.nametag;
    }

    public void setNameTagAS(final ArmorStand as) {
        this.nametag = as;
    }

    public ArmorStand getPetItemAS() {
        return this.petitem;
    }

    public ArrayList<Location> getLocs() {
        return this.locs;
    }

    public void setLocs(final ArrayList<Location> locs) {
        this.locs = locs;
    }
}
