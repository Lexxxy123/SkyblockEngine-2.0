package in.godspunky.skyblock.item;

import in.godspunky.skyblock.Skyblock;
import in.godspunky.skyblock.util.SUtil;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

public class SBlock {
    protected static final Skyblock plugin;

    static {
        plugin = Skyblock.getPlugin();
    }

    private final Location location;
    private final NBTTagCompound data;
    private SMaterial type;

    public SBlock(final Location location, final SMaterial type, final NBTTagCompound data) {
        this.location = location;
        this.type = type;
        this.data = data;
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

    private static String toLocationString(final Location location) {
        return location.getBlockX() + "," + location.getBlockY() + "," + location.getBlockZ() + "," + location.getWorld().getName();
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

    public Location getLocation() {
        return this.location;
    }

    public SMaterial getType() {
        return this.type;
    }

    public void setType(final SMaterial type) {
        this.type = type;
    }

    public NBTTagCompound getData() {
        return this.data;
    }
}
