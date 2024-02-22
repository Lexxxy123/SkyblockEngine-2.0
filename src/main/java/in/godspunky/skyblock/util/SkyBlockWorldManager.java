package in.godspunky.skyblock.util;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;

public class SkyBlockWorldManager {

    private final World bukkitWorld;


    public SkyBlockWorldManager(World world){
        this.bukkitWorld = world;
    }
    public static void loadWorlds(){
        new BlankWorldCreator("f6").createWorld();
    }

    public boolean unload(boolean save){
        try{
            Bukkit.unloadWorld(bukkitWorld , save);
            return true;
        }catch (Exception ignored){
            return false;
        }
    }

    public void delete(){
        World hub = Bukkit.getWorld("world");
        bukkitWorld.getPlayers().forEach(player -> player.teleport(hub.getSpawnLocation()));
        deleteWorldFolder(bukkitWorld.getWorldFolder());
    }

    private void deleteWorldFolder(File folder) {
        if (folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    deleteWorldFolder(file);
                }
            }
        }
        if (!folder.delete()) {
            System.out.println("Failed to delete file or directory: " + folder);
        }
    }

    public void cloneWorld(String newWorldName) {
        File copiedFile = new File(Bukkit.getWorldContainer(), newWorldName);
        copyFileStructure(bukkitWorld.getWorldFolder(), copiedFile);
        new BlankWorldCreator(newWorldName).createWorld();
    }

    private void copyFileStructure(File source, File target) {
        try {
            ArrayList<String> ignore = new ArrayList<>(Arrays.asList("uid.dat", "session.lock"));
            if (!ignore.contains(source.getName())) {
                if (source.isDirectory()) {
                    if (!target.exists())
                        if (!target.mkdirs())
                            throw new IOException("Couldn't create world directory!");
                    String[] files = source.list();
                    for (String file : files) {
                        File srcFile = new File(source, file);
                        File destFile = new File(target, file);
                        copyFileStructure(srcFile, destFile);
                    }
                } else {
                    InputStream in = Files.newInputStream(source.toPath());
                    OutputStream out = Files.newOutputStream(target.toPath());
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = in.read(buffer)) > 0)
                        out.write(buffer, 0, length);
                    in.close();
                    out.close();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
