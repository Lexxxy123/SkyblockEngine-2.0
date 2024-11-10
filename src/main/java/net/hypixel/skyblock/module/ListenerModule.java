/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.hypixel.skyblock.module;

import net.hypixel.skyblock.gui.GUIListener;
import net.hypixel.skyblock.item.ItemListener;
import net.hypixel.skyblock.listener.BlockListener;
import net.hypixel.skyblock.listener.PacketListener;
import net.hypixel.skyblock.listener.PlayerChatListener;
import net.hypixel.skyblock.listener.PlayerListener;
import net.hypixel.skyblock.listener.ServerPingListener;
import net.hypixel.skyblock.listener.WorldListener;
import net.hypixel.skyblock.module.impl.ModuleImpl;

public class ListenerModule
implements ModuleImpl {
    @Override
    public String name() {
        return "Listener";
    }

    @Override
    public void onStart() {
        new BlockListener();
        new PlayerListener();
        new ServerPingListener();
        new ItemListener();
        new GUIListener();
        new PacketListener();
        new WorldListener();
        new PlayerChatListener();
    }

    @Override
    public void onStop() {
    }

    @Override
    public boolean shouldStart() {
        return true;
    }
}

