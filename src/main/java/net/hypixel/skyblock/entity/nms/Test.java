/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.md_5.bungee.api.ChatColor
 */
package net.hypixel.skyblock.entity.nms;

import java.util.List;
import net.hypixel.skyblock.entity.EntityDrop;
import net.hypixel.skyblock.entity.EntityFunction;
import net.hypixel.skyblock.entity.SkeletonStatistics;
import net.md_5.bungee.api.ChatColor;

public class Test
implements SkeletonStatistics,
EntityFunction {
    @Override
    public String getEntityName() {
        return ChatColor.LIGHT_PURPLE + "\u2620 " + ChatColor.DARK_RED + ChatColor.BOLD + "Terminator Golem";
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

