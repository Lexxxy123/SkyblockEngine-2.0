package vn.giakhanhvn.skysim.entity.nms;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import net.minecraft.server.v1_8_R3.World;

public class ProtectorDragon extends Dragon {
    public ProtectorDragon(final World world) {
        super(world, 1.4, Dragon.DEFAULT_DAMAGE_DEGREE_RANGE, 300L);
    }

    public ProtectorDragon() {
        this(((CraftWorld) Bukkit.getWorlds().get(0)).getHandle());
    }

    public String getEntityName() {
        return "Protector Dragon";
    }

    public double getEntityMaxHealth() {
        return 9000000.0;
    }

    public double getDamageDealt() {
        return 1300.0;
    }
}
