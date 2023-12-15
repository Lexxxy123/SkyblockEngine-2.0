package vn.giakhanhvn.skysim.item;

import net.minecraft.server.v1_8_R3.NBTTagCompound;

public interface Reforgable extends ItemData {
    default NBTTagCompound getData() {
        return new NBTTagCompound();
    }
}
