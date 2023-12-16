package vn.giakhanhvn.skysim.entity.nms;

import net.md_5.bungee.api.ChatColor;
import vn.giakhanhvn.skysim.entity.EntityDrop;
import vn.giakhanhvn.skysim.entity.EntityFunction;
import vn.giakhanhvn.skysim.entity.SkeletonStatistics;

import java.util.List;

public class Test implements SkeletonStatistics, EntityFunction {
    @Override
    public String getEntityName() {
        return ChatColor.LIGHT_PURPLE + "☠ " + ChatColor.DARK_RED + ChatColor.BOLD + "Terminator Golem";
    }

    @Override
    public double getEntityMaxHealth() {
        return 9.5E8;
    }

    @Override
    public double getDamageDealt() {
        return 50000.0;
    }

    @Override
    public List<EntityDrop> drops() {
        return null;
    }

    @Override
    public double getXPDropped() {
        return 100000.0;
    }
}
