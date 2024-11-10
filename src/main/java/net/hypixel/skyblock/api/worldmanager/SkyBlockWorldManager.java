/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.World
 */
package net.hypixel.skyblock.api.worldmanager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import net.hypixel.skyblock.api.worldmanager.BlankWorldCreator;
import org.bukkit.Bukkit;
import org.bukkit.World;

public class SkyBlockWorldManager {
    private final World bukkitWorld;

    public SkyBlockWorldManager(World world) {
        this.bukkitWorld = world;
    }

    public static void loadWorlds() {
        new BlankWorldCreator("f6").createWorld();
    }

    public boolean unload(boolean save) {
        try {
            Bukkit.unloadWorld((World)this.bukkitWorld, (boolean)save);
            return true;
        } catch (Exception ignored) {
            return false;
        }
    }

    public void delete() {
        World hub = Bukkit.getWorld((String)"world");
        this.bukkitWorld.getPlayers().forEach(player -> player.teleport(hub.getSpawnLocation()));
        this.deleteWorldFolder(this.bukkitWorld.getWorldFolder());
    }

    private void deleteWorldFolder(File folder) {
        File[] files;
        this.unload(false);
        if (folder.isDirectory() && null != (files = folder.listFiles())) {
            for (File file : files) {
                this.deleteWorldFolder(file);
            }
        }
        if (!folder.delete()) {
            System.out.println("Failed to delete file or directory: " + folder);
        }
    }

    public void cloneWorld(String newWorldName) {
        File copiedFile = new File(Bukkit.getWorldContainer(), newWorldName);
        this.copyFileStructure(this.bukkitWorld.getWorldFolder(), copiedFile);
        new BlankWorldCreator(newWorldName).createWorld();
    }

    private void copyFileStructure(File source, File target) {
        try {
            ArrayList<String> ignore = new ArrayList<String>(Arrays.asList("uid.dat", "session.lock"));
            if (!ignore.contains(source.getName())) {
                if (source.isDirectory()) {
                    String[] files;
                    if (!target.exists() && !target.mkdirs()) {
                        throw new IOException("Couldn't create world directory!");
                    }
                    for (String file : files = source.list()) {
                        File srcFile = new File(source, file);
                        File destFile = new File(target, file);
                        this.copyFileStructure(srcFile, destFile);
                    }
                } else {
                    int length;
                    InputStream in = Files.newInputStream(source.toPath(), new OpenOption[0]);
                    OutputStream out = Files.newOutputStream(target.toPath(), new OpenOption[0]);
                    byte[] buffer = new byte[1024];
                    while (0 < (length = in.read(buffer))) {
                        out.write(buffer, 0, length);
                    }
                    in.close();
                    out.close();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

