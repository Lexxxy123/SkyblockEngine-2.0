/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.comphenix.protocol.ProtocolLibrary
 *  com.comphenix.protocol.events.PacketContainer
 *  org.bukkit.entity.Player
 */
package net.hypixel.skyblock.api.protocol;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import java.lang.reflect.InvocationTargetException;
import org.bukkit.entity.Player;

public class WrappedVillagerPacket {
    private final PacketContainer handle;

    public WrappedVillagerPacket(PacketContainer container) {
        this.handle = container;
    }

    public void send(Player receiver) {
        try {
            ProtocolLibrary.getProtocolManager().sendServerPacket(receiver, this.handle);
        } catch (InvocationTargetException ex) {
            throw new RuntimeException("something went wrong!", ex);
        }
    }

    public PacketContainer getHandle() {
        return this.handle;
    }
}

