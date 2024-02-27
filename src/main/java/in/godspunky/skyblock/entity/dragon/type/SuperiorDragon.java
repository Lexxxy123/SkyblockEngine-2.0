package in.godspunky.skyblock.entity.dragon.type;

import in.godspunky.skyblock.entity.dragon.Dragon;
import net.minecraft.server.v1_8_R3.World;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;

public class SuperiorDragon extends Dragon {
    public SuperiorDragon(World world) {
        super(world, 1.6, DEFAULT_DAMAGE_DEGREE_RANGE, 300L);
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
