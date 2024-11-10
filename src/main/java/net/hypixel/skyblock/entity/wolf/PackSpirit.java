/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.md_5.bungee.api.ChatColor
 */
package net.hypixel.skyblock.entity.wolf;

import java.util.Arrays;
import java.util.List;
import net.hypixel.skyblock.entity.EntityDrop;
import net.hypixel.skyblock.entity.EntityDropType;
import net.hypixel.skyblock.entity.wolf.BaseWolf;
import net.hypixel.skyblock.item.SMaterial;
import net.md_5.bungee.api.ChatColor;

public class PackSpirit
extends BaseWolf {
    @Override
    public String getEntityName() {
        return ChatColor.AQUA + "Pack Spirit";
    }

    @Override
    public double getEntityMaxHealth() {
        return 6000.0;
    }

    @Override
    public double getDamageDealt() {
        return 270.0;
    }

    @Override
    public int mobLevel() {
        return 30;
    }

    @Override
    public List<EntityDrop> drops() {
        return Arrays.asList(new EntityDrop(SMaterial.BONE, EntityDropType.COMMON, 0.5), new EntityDrop(SMaterial.OAK_WOOD, EntityDropType.COMMON, 0.1), new EntityDrop(SMaterial.BIRCH_WOOD, EntityDropType.COMMON, 0.1));
    }

    @Override
    public double getXPDropped() {
        return 15.0;
    }

    @Override
    public boolean isAngry() {
        return true;
    }
}

