/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.event.Listener
 *  org.bukkit.plugin.Plugin
 */
package net.hypixel.skyblock.listener;

import net.hypixel.skyblock.SkyBlock;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public class PListener
implements Listener {
    private static int amount = 0;
    protected SkyBlock plugin = SkyBlock.getPlugin();

    protected PListener() {
        this.plugin.getServer().getPluginManager().registerEvents((Listener)this, (Plugin)this.plugin);
        ++amount;
    }

    public static int getAmount() {
        return amount;
    }
}

