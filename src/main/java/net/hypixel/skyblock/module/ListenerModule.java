package net.hypixel.skyblock.module;

import net.hypixel.skyblock.gui.GUIListener;
import net.hypixel.skyblock.item.ItemListener;
import net.hypixel.skyblock.listener.*;
import net.hypixel.skyblock.module.impl.ModuleImpl;

public class ListenerModule implements ModuleImpl {
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
