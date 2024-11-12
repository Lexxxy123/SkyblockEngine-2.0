/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 */
package net.hypixel.skyblock.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import net.hypixel.skyblock.SkyBlock;
import net.hypixel.skyblock.features.region.Region;
import net.hypixel.skyblock.features.region.RegionType;
import org.bukkit.Location;

public class SQLRegionData {
    private static final SkyBlock plugin = SkyBlock.getPlugin();
    private final String SELECT = "SELECT * FROM `regions` WHERE name=?";
    private final String SELECT_TYPE = "SELECT * FROM `regions` WHERE type=?";
    private final String INSERT = "INSERT INTO `regions` (`name`, `x1`, `y1`, `z1`, `x2`, `y2`, `z2`, `world`, `type`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
    private final String UPDATE = "UPDATE `regions` SET x1=?, y1=?, z1=?, x2=?, y2=?, z2=?, world=?, type=? WHERE name=?";
    private final String COUNT = "SELECT COUNT(*) AS rows FROM `regions`";
    private final String DELETE = "DELETE FROM `regions` WHERE name=?";

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public boolean exists(String regionName) {
        try (Connection connection = SQLRegionData.plugin.sql.getConnection();){
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM `regions` WHERE name=?");
            statement.setString(1, regionName);
            ResultSet set = statement.executeQuery();
            boolean bl2 = set.next();
            return bl2;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public Region get(String name) {
        if (!this.exists(name)) {
            return null;
        }
        try (Connection connection = SQLRegionData.plugin.sql.getConnection();){
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM `regions` WHERE name=?");
            statement.setString(1, name);
            ResultSet set = statement.executeQuery();
            set.next();
            Location firstLocation = new Location(SQLRegionData.plugin.worldData.getWorld(set.getInt("world")), (double)set.getInt("x1"), (double)set.getInt("y1"), (double)set.getInt("z1"));
            Location secondLocation = new Location(SQLRegionData.plugin.worldData.getWorld(set.getInt("world")), (double)set.getInt("x2"), (double)set.getInt("y2"), (double)set.getInt("z2"));
            RegionType type = RegionType.getType(set.getString("type"));
            if (type == null) {
                Region region3 = null;
                if (connection != null) {
                    connection.close();
                }
                Region region2 = region3;
                return region2;
            }
            set.close();
            Region region = new Region(name, firstLocation, secondLocation, type);
            return region;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public List<Region> getAllOfType(RegionType type) {
        try (Connection connection = SQLRegionData.plugin.sql.getConnection();){
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM `regions` WHERE type=?");
            statement.setInt(1, type.ordinal());
            ResultSet set = statement.executeQuery();
            ArrayList<Region> regions = new ArrayList<Region>();
            while (set.next()) {
                String name = set.getString("name");
                Location firstLocation = new Location(SQLRegionData.plugin.worldData.getWorld(set.getInt("world")), (double)set.getInt("x1"), (double)set.getInt("y1"), (double)set.getInt("z1"));
                Location secondLocation = new Location(SQLRegionData.plugin.worldData.getWorld(set.getInt("world")), (double)set.getInt("x2"), (double)set.getInt("y2"), (double)set.getInt("z2"));
                regions.add(new Region(name, firstLocation, secondLocation, type));
            }
            set.close();
            ArrayList<Region> arrayList = regions;
            return arrayList;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public List<Region> getAll() {
        try (Connection connection = SQLRegionData.plugin.sql.getConnection();){
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM `regions`");
            ResultSet set = statement.executeQuery();
            ArrayList<Region> regions = new ArrayList<Region>();
            while (set.next()) {
                regions.add(this.get(set.getString("name")));
            }
            set.close();
            ArrayList<Region> arrayList = regions;
            return arrayList;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public Region create(String name, Location firstLocation, Location secondLocation, RegionType type) {
        if (this.exists(name = name.toLowerCase())) {
            return this.get(name);
        }
        try (Connection connection = SQLRegionData.plugin.sql.getConnection();){
            PreparedStatement statement = connection.prepareStatement("INSERT INTO `regions` (`name`, `x1`, `y1`, `z1`, `x2`, `y2`, `z2`, `world`, `type`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);");
            statement.setString(1, name);
            statement.setInt(2, (int)firstLocation.getX());
            statement.setInt(3, (int)firstLocation.getY());
            statement.setInt(4, (int)firstLocation.getZ());
            statement.setInt(5, (int)secondLocation.getX());
            statement.setInt(6, (int)secondLocation.getY());
            statement.setInt(7, (int)secondLocation.getZ());
            statement.setInt(8, SQLRegionData.plugin.worldData.getWorldID(firstLocation.getWorld()));
            statement.setString(9, type.name());
            statement.execute();
            Region region = new Region(name, firstLocation, secondLocation, type);
            return region;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public void save(Region region) {
        try (Connection connection = SQLRegionData.plugin.sql.getConnection();){
            PreparedStatement statement = connection.prepareStatement("UPDATE `regions` SET x1=?, y1=?, z1=?, x2=?, y2=?, z2=?, world=?, type=? WHERE name=?");
            statement.setInt(1, (int)region.getFirstLocation().getX());
            statement.setInt(2, (int)region.getFirstLocation().getY());
            statement.setInt(3, (int)region.getFirstLocation().getZ());
            statement.setInt(4, (int)region.getSecondLocation().getX());
            statement.setInt(5, (int)region.getSecondLocation().getY());
            statement.setInt(6, (int)region.getSecondLocation().getZ());
            statement.setInt(7, SQLRegionData.plugin.worldData.getWorldID(region.getFirstLocation().getWorld()));
            statement.setString(8, region.getType().name());
            statement.setString(9, region.getName());
            statement.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void delete(Region region) {
        try (Connection connection = SQLRegionData.plugin.sql.getConnection();){
            PreparedStatement statement = connection.prepareStatement("DELETE FROM `regions` WHERE name=?");
            statement.setString(1, region.getName());
            statement.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public int getRegionCount() {
        try (Connection connection = SQLRegionData.plugin.sql.getConnection();){
            PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) AS rows FROM `regions`");
            ResultSet set = statement.executeQuery();
            set.next();
            int count = set.getInt("rows");
            set.close();
            int n2 = count;
            return n2;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return 0;
        }
    }
}

