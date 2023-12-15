package vn.giakhanhvn.skysim.entity.nms;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import net.minecraft.server.v1_8_R3.World;

public class SuperiorDragon extends Dragon {
    public SuperiorDragon(final World world) {
        super(world, 1.6, Dragon.DEFAULT_DAMAGE_DEGREE_RANGE, 300L);
    }

    public SuperiorDragon() {
        this(((CraftWorld) Bukkit.getWorlds().get(0)).getHandle());
    }

    public String getEntityName() {
        return "Superior Dragon";
    }

    public double getEntityMaxHealth() {
        return 1.2E7;
    }

    public double getDamageDealt() {
        return 1600.0;
    }
}
