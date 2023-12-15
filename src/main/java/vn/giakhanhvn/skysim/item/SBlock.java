package vn.giakhanhvn.skysim.item;

import java.util.Iterator;

import org.bukkit.configuration.ConfigurationSection;
import vn.giakhanhvn.skysim.util.SUtil;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Location;
import vn.giakhanhvn.skysim.SkySimEngine;

public class SBlock {
    protected static final SkySimEngine plugin;
    private final Location location;
    private SMaterial type;
    private final NBTTagCompound data;

    public SBlock(final Location location, final SMaterial type, final NBTTagCompound data) {
        this.location = location;
        this.type = type;
        this.data = data;
    }

    public float getDataFloat(final String key) {
        return this.data.getFloat(key);
    }

    public long getDataLong(final String key) {
        return this.data.getLong(key);
    }

    public double getDataDouble(final String key) {
        return this.data.getDouble(key);
    }

    public String getDataString(final String key) {
        return this.data.getString(key);
    }

    public static SBlock getBlock(final Location location) {
        final ConfigurationSection cs = SBlock.plugin.blocks.getConfigurationSection(toLocationString(location));
        if (cs == null) {
            return null;
        }
        final NBTTagCompound compound = new NBTTagCompound();
        for (final String key : cs.getKeys(false)) {
            if (key.equals("type")) {
                continue;
            }
            compound.set(key, SUtil.getBaseFromObject(cs, key));
        }
        return new SBlock(location, SMaterial.getMaterial(cs.getString("type")), compound);
    }

    public void save() {
        SBlock.plugin.blocks.set(this.toLocationString() + ".type", this.type.name());
        for (final String key : this.data.c()) {
            final Object o = SUtil.getObjectFromCompound(this.data, key);
            if (o instanceof NBTTagCompound) {
                continue;
            }
            SBlock.plugin.blocks.set(this.toLocationString() + "." + key, o);
        }
        SBlock.plugin.blocks.save();
    }

    public void delete() {
        SBlock.plugin.blocks.set(this.toLocationString(), null);
        SBlock.plugin.blocks.save();
    }

    private String toLocationString() {
        return toLocationString(this.location);
    }

    private static String toLocationString(final Location location) {
        return location.getBlockX() + "," + location.getBlockY() + "," + location.getBlockZ() + "," + location.getWorld().getName();
    }

    public Location getLocation() {
        return this.location;
    }

    public SMaterial getType() {
        return this.type;
    }

    public NBTTagCompound getData() {
        return this.data;
    }

    public void setType(final SMaterial type) {
        this.type = type;
    }

    static {
        plugin = SkySimEngine.getPlugin();
    }
}
