/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.World
 */
package net.hypixel.skyblock.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import net.hypixel.skyblock.SkyBlock;
import org.bukkit.Bukkit;
import org.bukkit.World;

public class SQLWorldData {
    private static final SkyBlock plugin = SkyBlock.getPlugin();
    private final String SELECT = "SELECT * FROM `worlds` WHERE name=?";
    private final String SELECT_ID = "SELECT * FROM `worlds` WHERE id=?";
    private final String INSERT = "INSERT INTO `worlds` (`id`, `name`) VALUES (?, ?);";
    private final String COUNT = "SELECT COUNT(*) AS rows FROM `worlds`";

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public boolean exists(World world) {
        try (Connection connection = SQLWorldData.plugin.sql.getConnection();){
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM `worlds` WHERE name=?");
            statement.setString(1, world.getName());
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
    public boolean existsID(int id) {
        try (Connection connection = SQLWorldData.plugin.sql.getConnection();){
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM `worlds` WHERE id=?");
            statement.setInt(1, id);
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
    public int getWorldID(World world) {
        try (Connection connection = SQLWorldData.plugin.sql.getConnection();){
            PreparedStatement statement;
            if (!this.exists(world)) {
                statement = connection.prepareStatement("INSERT INTO `worlds` (`id`, `name`) VALUES (?, ?);");
                statement.setInt(1, this.getWorldCount() + 1);
                statement.setString(2, world.getName());
                statement.execute();
            }
            statement = connection.prepareStatement("SELECT * FROM `worlds` WHERE name=?");
            statement.setString(1, world.getName());
            ResultSet set = statement.executeQuery();
            set.next();
            int id = set.getInt("id");
            set.close();
            int n2 = id;
            return n2;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public World getWorld(int id) {
        try (Connection connection = SQLWorldData.plugin.sql.getConnection();){
            if (!this.existsID(id)) {
                World world3 = null;
                if (connection != null) {
                    connection.close();
                }
                World world2 = world3;
                return world2;
            }
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM `worlds` WHERE id=?");
            statement.setInt(1, id);
            ResultSet set = statement.executeQuery();
            set.next();
            String name = set.getString("name");
            set.close();
            World world = Bukkit.getWorld((String)name);
            return world;
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
    public int getWorldCount() {
        try (Connection connection = SQLWorldData.plugin.sql.getConnection();){
            PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) AS rows FROM `worlds`");
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

