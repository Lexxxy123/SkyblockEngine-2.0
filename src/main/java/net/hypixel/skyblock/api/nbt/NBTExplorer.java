package net.hypixel.skyblock.api.nbt;

import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.json.JSONException;
import org.json.JSONObject;

public class NBTExplorer {
    public static JSONObject NBTSaver(final ItemStack i) {
        final JSONObject jo = new JSONObject();
        final net.minecraft.server.v1_8_R3.ItemStack stack = CraftItemStack.asNMSCopy(i);
        final NBTTagCompound compound = stack.getTag();
        final String finalnbt = "";
        final StringBuilder sb = new StringBuilder();
        final char c = '\"';
        if (compound != null) {
            try {
                for (String key : compound.c()) {
                    jo.put(key, compound.get(key).toString());
                }
            } catch (JSONException e) {

            }
        }
        return jo;
    }
}
