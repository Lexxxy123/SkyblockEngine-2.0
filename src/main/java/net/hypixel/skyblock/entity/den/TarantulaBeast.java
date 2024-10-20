/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.md_5.bungee.api.ChatColor
 */
package net.hypixel.skyblock.entity.den;

import java.util.Collections;
import java.util.List;
import net.hypixel.skyblock.entity.EntityDrop;
import net.hypixel.skyblock.entity.EntityDropType;
import net.hypixel.skyblock.entity.den.BaseSpider;
import net.hypixel.skyblock.item.SItem;
import net.hypixel.skyblock.item.SMaterial;
import net.hypixel.skyblock.util.SUtil;
import net.md_5.bungee.api.ChatColor;

public class TarantulaBeast
extends BaseSpider {
    @Override
    public String getEntityName() {
        return ChatColor.DARK_RED + "Tarantula Beast";
    }

    @Override
    public double getEntityMaxHealth() {
        return 144000.0;
    }

    @Override
    public double getDamageDealt() {
        return 2500.0;
    }

    @Override
    public double getXPDropped() {
        return 300.0;
    }

    @Override
    public List<EntityDrop> drops() {
        return Collections.singletonList(new EntityDrop(SUtil.setStackAmount(SItem.of(SMaterial.TARANTULA_WEB).getStack(), 2), EntityDropType.GUARANTEED, 1.0));
    }
}

