package net.hypixel.skyblock.item.oddities;

import net.hypixel.skyblock.item.*;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.hypixel.skyblock.item.*;

public class Launcher implements MaterialStatistics, MaterialFunction, ItemData {
    @Override
    public String getDisplayName() {
        return "Launcher";
    }

    @Override
    public Rarity getRarity() {
        return Rarity.LEGENDARY;
    }

    @Override
    public GenericItemType getType() {
        return GenericItemType.BLOCK;
    }

    @Override
    public NBTTagCompound getData() {
        NBTTagCompound compound = new NBTTagCompound();
        compound.setFloat("velX", 1.0f);
        compound.setFloat("velY", 1.0f);
        compound.setFloat("velZ", 1.0f);
        return compound;
    }
}
