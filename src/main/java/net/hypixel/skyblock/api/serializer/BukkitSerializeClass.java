/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.PlayerInventory
 *  org.bukkit.util.io.BukkitObjectInputStream
 *  org.bukkit.util.io.BukkitObjectOutputStream
 *  org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder
 */
package net.hypixel.skyblock.api.serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

public class BukkitSerializeClass {
    public static String[] playerInventoryToBase64(PlayerInventory playerInventory) throws IllegalStateException {
        String content = BukkitSerializeClass.toBase64((Inventory)playerInventory);
        String armor = BukkitSerializeClass.itemStackArrayToBase64(playerInventory.getArmorContents());
        return new String[]{content, armor};
    }

    public static String itemStackArrayToBase64(ItemStack[] items) throws IllegalStateException {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream((OutputStream)outputStream);
            dataOutput.writeInt(items.length);
            for (int i2 = 0; i2 < items.length; ++i2) {
                dataOutput.writeObject((Object)items[i2]);
            }
            dataOutput.close();
            return Base64Coder.encodeLines((byte[])outputStream.toByteArray());
        } catch (Exception e2) {
            throw new IllegalStateException("Unable to save item stacks.", e2);
        }
    }

    public static String toBase64(Inventory inventory) throws IllegalStateException {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream((OutputStream)outputStream);
            dataOutput.writeInt(inventory.getSize());
            for (int i2 = 0; i2 < inventory.getSize(); ++i2) {
                dataOutput.writeObject((Object)inventory.getItem(i2));
            }
            dataOutput.close();
            return Base64Coder.encodeLines((byte[])outputStream.toByteArray());
        } catch (Exception e2) {
            throw new IllegalStateException("Unable to save item stacks.", e2);
        }
    }

    public static Inventory fromBase64(String data) throws IOException {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines((String)data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream((InputStream)inputStream);
            Inventory inventory = Bukkit.getServer().createInventory(null, dataInput.readInt());
            for (int i2 = 0; i2 < inventory.getSize(); ++i2) {
                inventory.setItem(i2, (ItemStack)dataInput.readObject());
            }
            dataInput.close();
            return inventory;
        } catch (ClassNotFoundException e2) {
            throw new IOException("Unable to decode class type.", e2);
        }
    }

    public static ItemStack[] itemStackArrayFromBase64(String data) throws IOException {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines((String)data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream((InputStream)inputStream);
            ItemStack[] items = new ItemStack[dataInput.readInt()];
            for (int i2 = 0; i2 < items.length; ++i2) {
                items[i2] = (ItemStack)dataInput.readObject();
            }
            dataInput.close();
            return items;
        } catch (ClassNotFoundException e2) {
            throw new IOException("Unable to decode class type.", e2);
        }
    }
}

