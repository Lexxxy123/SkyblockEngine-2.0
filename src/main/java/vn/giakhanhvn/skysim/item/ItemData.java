package vn.giakhanhvn.skysim.item;

import net.minecraft.server.v1_8_R3.NBTTagCompound;

import java.util.List;

public interface ItemData {
    NBTTagCompound getData();

    default List<String> getDataLore(final String key, final Object value) {
        return null;
    }
}
