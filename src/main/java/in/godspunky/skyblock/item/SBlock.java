package in.godspunky.skyblock.item;

import in.godspunky.skyblock.SkyBlock;
import in.godspunky.skyblock.util.SUtil;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

public class SBlock {
    protected static final SkyBlock plugin;
    private final Location location;
    private SMaterial type;
    private final NBTTagCompound data;

    public SBlock(Location location, SMaterial type, NBTTagCompound data) {
        this.location = location;
        this.type = type;
        this.data = data;
    }

    public float getDataFloat(String key) {
        return this.data.getFloat(key);
    }

    public long getDataLong(String key) {
        return this.data.getLong(key);
    }

    public double getDataDouble(String key) {
        return this.data.getDouble(key);
    }

    public String getDataString(String key) {
        return this.data.getString(key);
    }

    public static SBlock getBlock(Location location) {
        ConfigurationSection cs = plugin.blocks.getConfigurationSection(toLocationString(location));
        if (null == cs) {
            return null;
        }
        NBTTagCompound compound = new NBTTagCompound();
        for (String key : cs.getKeys(false)) {
            if (key.equals("type")) {
                continue;
            }
            compound.set(key, SUtil.getBaseFromObject(cs, key));
        }
        return new SBlock(location, SMaterial.getMaterial(cs.getString("type")), compound);
    }

    public void save() {
        plugin.blocks.set(this.toLocationString() + ".type", this.type.name());
        for (String key : this.data.c()) {
            Object o = SUtil.getObjectFromCompound(this.data, key);
            if (o instanceof NBTTagCompound) {
                continue;
            }
            plugin.blocks.set(this.toLocationString() + "." + key, o);
        }
        plugin.blocks.save();
    }

    public void delete() {
        plugin.blocks.set(this.toLocationString(), null);
        plugin.blocks.save();
    }

    private String toLocationString() {
        return toLocationString(this.location);
    }

    private static String toLocationString(Location location) {
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

    public void setType(SMaterial type) {
        this.type = type;
    }

    static {
        plugin = SkyBlock.getPlugin();
    }
}
