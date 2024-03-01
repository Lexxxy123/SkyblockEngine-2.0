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


    public SkyBlockWorldManager(final World world){
        this.bukkitWorld = world;
    }
    public static void loadWorlds(){
        new BlankWorldCreator("f6").createWorld();
    }

    public boolean unload(final boolean save){
        try{
            Bukkit.unloadWorld(bukkitWorld , save);
            return true;
        }catch (final Exception ignored){
            return false;
        }
    }

    public void delete(){
        final World hub = Bukkit.getWorld("world");
        bukkitWorld.getPlayers().forEach(player -> player.teleport(hub.getSpawnLocation()));
        deleteWorldFolder(bukkitWorld.getWorldFolder());
    }

    private void deleteWorldFolder(final File folder) {
        unload(false);
        if (folder.isDirectory()) {
            final File[] files = folder.listFiles();
            if (null != files) {
                for (final File file : files) {
                    deleteWorldFolder(file);
                }
            }
        }
        if (!folder.delete()) {
            System.out.println("Failed to delete file or directory: " + folder);
        }
    }

    public void cloneWorld(final String newWorldName) {
        final File copiedFile = new File(Bukkit.getWorldContainer(), newWorldName);
        copyFileStructure(bukkitWorld.getWorldFolder(), copiedFile);
        new BlankWorldCreator(newWorldName).createWorld();
    }

    private void copyFileStructure(final File source, final File target) {
        try {
            final ArrayList<String> ignore = new ArrayList<>(Arrays.asList("uid.dat", "session.lock"));
            if (!ignore.contains(source.getName())) {
                if (source.isDirectory()) {
                    if (!target.exists())
                        if (!target.mkdirs())
                            throw new IOException("Couldn't create world directory!");
                    final String[] files = source.list();
                    for (final String file : files) {
                        final File srcFile = new File(source, file);
                        final File destFile = new File(target, file);
                        copyFileStructure(srcFile, destFile);
                    }
                } else {
                    final InputStream in = Files.newInputStream(source.toPath());
                    final OutputStream out = Files.newOutputStream(target.toPath());
                    final byte[] buffer = new byte[1024];
                    int length;
                    while (0 < (length = in.read(buffer)))
                        out.write(buffer, 0, length);
                    in.close();
                    out.close();
                }
            }
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

}
