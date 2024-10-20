/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraft.server.v1_8_R3.NBTTagCompound
 */
package net.hypixel.skyblock.item;

import net.hypixel.skyblock.item.ItemData;
import net.hypixel.skyblock.item.MaterialFunction;
import net.hypixel.skyblock.item.SItem;
import net.hypixel.skyblock.item.SkullStatistics;
import net.hypixel.skyblock.util.SUtil;
import net.minecraft.server.v1_8_R3.NBTTagCompound;

public interface Rune
extends SkullStatistics,
MaterialFunction,
ItemData {
    @Override
    default public NBTTagCompound getData() {
        NBTTagCompound compound = new NBTTagCompound();
        compound.setByte("level", (byte)1);
        return compound;
    }

    @Override
    default public void onInstanceUpdate(SItem instance) {
        byte level = instance.getData().getByte("level");
        instance.setDisplayName(this.getDisplayName() + " " + SUtil.toRomanNumeral(level));
    }
}

