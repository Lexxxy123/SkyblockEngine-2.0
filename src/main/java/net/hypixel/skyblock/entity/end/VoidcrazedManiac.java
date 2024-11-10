/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.md_5.bungee.api.ChatColor
 */
package net.hypixel.skyblock.entity.end;

import java.util.Collections;
import java.util.List;
import net.hypixel.skyblock.entity.EntityDrop;
import net.hypixel.skyblock.entity.EntityDropType;
import net.hypixel.skyblock.entity.end.BaseEnderman;
import net.hypixel.skyblock.item.SMaterial;
import net.md_5.bungee.api.ChatColor;

public class VoidcrazedManiac
extends BaseEnderman {
    @Override
    public String getEntityName() {
        return ChatColor.DARK_RED + "Voidcrazed Maniac";
    }

    @Override
    public double getEntityMaxHealth() {
        return 7.5E7;
    }

    @Override
    public double getDamageDealt() {
        return 15000.0;
    }

    @Override
    public int mobLevel() {
        return 4330;
    }

    @Override
    public double getXPDropped() {
        return 5000.0;
    }

    @Override
    public List<EntityDrop> drops() {
        return Collections.singletonList(new EntityDrop(SMaterial.NULL_SPHERE, EntityDropType.GUARANTEED, 1.0));
    }
}

