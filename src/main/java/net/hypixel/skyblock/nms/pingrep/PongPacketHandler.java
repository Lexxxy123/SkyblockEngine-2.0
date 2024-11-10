/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraft.server.v1_8_R3.PacketStatusOutPong
 */
package net.hypixel.skyblock.nms.pingrep;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import net.hypixel.skyblock.nms.pingrep.PingEvent;
import net.hypixel.skyblock.nms.pingrep.PingReply;
import net.hypixel.skyblock.nms.pingrep.PongPacket;
import net.minecraft.server.v1_8_R3.PacketStatusOutPong;

public class PongPacketHandler
extends PongPacket {
    public PongPacketHandler(PingEvent reply) {
        super(reply);
    }

    @Override
    public void send() {
        try {
            PingReply reply = this.getEvent().getReply();
            PacketStatusOutPong packet = new PacketStatusOutPong();
            Field field = this.getEvent().getReply().getClass().getDeclaredField("ctx");
            field.setAccessible(true);
            Object ctx = field.get(reply);
            Method writeAndFlush = ctx.getClass().getMethod("writeAndFlush", Object.class);
            writeAndFlush.setAccessible(true);
            writeAndFlush.invoke(ctx, packet);
        } catch (IllegalAccessException | IllegalArgumentException | NoSuchFieldException | NoSuchMethodException | SecurityException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}

