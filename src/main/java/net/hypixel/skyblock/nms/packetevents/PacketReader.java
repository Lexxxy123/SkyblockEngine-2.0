package net.hypixel.skyblock.nms.packetevents;


import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import net.hypixel.skyblock.SkyBlock;
import net.hypixel.skyblock.event.SkyblockPlayerNPCClickEvent;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInUseEntity;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import net.hypixel.skyblock.npc.impl.SkyblockNPC;
import net.hypixel.skyblock.npc.impl.SkyblockNPCManager;
import net.hypixel.skyblock.user.User;

import java.lang.reflect.Field;

public class PacketReader {

    private Player player;


    public void injectPlayer(Player player) {
        this.player = player;

        ChannelDuplexHandler channelDuplexHandler = new ChannelDuplexHandler() {

            @Override
            public void channelRead(ChannelHandlerContext channelHandlerContext, Object packet) {
                if(packet instanceof PacketPlayInUseEntity) {
                    try {
                        PacketPlayInUseEntity packetPlayInUseEntity = (PacketPlayInUseEntity) packet;
                        readPacket(packetPlayInUseEntity,player);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                try {
                    super.channelRead(channelHandlerContext, packet);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        };
        ChannelPipeline pipeline = ((CraftPlayer)player).getHandle().playerConnection.networkManager.channel.pipeline();
        pipeline.addBefore("packet_handler", player.getName(), channelDuplexHandler);

    }

    public void readPacket(Packet<?> packet, Player p)
    {
        if(packet.getClass().getSimpleName().equalsIgnoreCase("PacketPlayInUseEntity"))
        {
            int id = (int) getValue(packet, "a");
            if(getValue(packet, "action").toString().equalsIgnoreCase("interact"))
            {
                for (SkyblockNPC npc : SkyblockNPCManager.getNPCS()){
                    if (npc.getEntityID() == id) {

                        SkyblockPlayerNPCClickEvent npcClickEvent = new SkyblockPlayerNPCClickEvent(player , npc);
                        Bukkit.getPluginManager().callEvent(npcClickEvent);

                        if (npc.canSpeak(User.getUser(player))){
                            npc.speak(player);
                        }else{
                            npc.getParameters().onInteract(player , npc);
                        }
                    }
                }
            }
        }
    }

    private Object getValue(Object instance, String name)
    {
        Object result = null;
        try
        {
            Field field = instance.getClass().getDeclaredField(name);
            field.setAccessible(true);
            result = field.get(instance);
            field.setAccessible(false);
        } catch(Exception exception)
        {
            exception.printStackTrace();
        }

        return result;
    }

}
