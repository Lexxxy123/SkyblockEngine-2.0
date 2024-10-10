package net.hypixel.skyblock.npc.impl.type.impl;

import com.mojang.authlib.GameProfile;
import net.hypixel.skyblock.npc.impl.type.NPCBase;
import net.hypixel.skyblock.util.SUtil;
import net.hypixel.skyblock.util.TaskUtility;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.scoreboard.CraftScoreboard;
import org.bukkit.craftbukkit.v1_8_R3.scoreboard.CraftScoreboardManager;
import org.bukkit.entity.Player;

import java.util.Collections;

public class NPCPlayerImpl extends EntityPlayer implements NPCBase {

    private final Location location;
    private Player player;

    private final String name;


    public NPCPlayerImpl(Location location , GameProfile gameProfile) {
        super(
                ((CraftServer) Bukkit.getServer()).getServer(),
                ((CraftWorld) location.getWorld()).getHandle(),
                gameProfile,
                new PlayerInteractManager(((CraftWorld) location.getWorld()).getHandle())
        );
        this.name = gameProfile.getName();
        this.location = location;
    }
    @Override
    public void show(Player player){
        PacketPlayOutPlayerInfo infoAddPacket = new PacketPlayOutPlayerInfo(
                PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER,
                this
        );
        PacketPlayOutNamedEntitySpawn spawnPacket = new PacketPlayOutNamedEntitySpawn(
                this
        );
        PacketPlayOutPlayerInfo infoRemovePacket = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER
        , this);
        sendPacket(player , infoAddPacket);
        sendPacket(player , spawnPacket);
        TaskUtility.delaySync(()->{
            sendPacket(player , infoRemovePacket);
        }, 20 * 5);
    }
    @Override
    public void hide(Player player){
        PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(getId());
        sendPacket(player, packet);
    }

    @Override
    public void hideNameTag(Player player){
        CraftScoreboardManager scoreboardManager = ((CraftServer) Bukkit.getServer()).getScoreboardManager();
        CraftScoreboard craftScoreboard = scoreboardManager.getMainScoreboard();
        Scoreboard scoreboard = craftScoreboard.getHandle();

        ScoreboardTeam scoreboardTeam = scoreboard.getTeam(name);
        if (scoreboardTeam == null){
            scoreboardTeam = new ScoreboardTeam(scoreboard , name);
        }
        scoreboardTeam.setNameTagVisibility(ScoreboardTeamBase.EnumNameTagVisibility.NEVER);
        scoreboardTeam.setPrefix("[NPC] ");

        sendPacket(player, new PacketPlayOutScoreboardTeam(scoreboardTeam, 1));
        sendPacket(player, new PacketPlayOutScoreboardTeam(scoreboardTeam, 0));
        sendPacket(player, new PacketPlayOutScoreboardTeam(scoreboardTeam, Collections.singletonList(name), 3));
    }

    @Override
    public void sendRotation(Player player){
        Location original = location;
        Location location = original.clone().setDirection(player.getLocation().subtract(original.clone()).toVector());

        byte yaw = (byte) (location.getYaw() * 256 / 360);
        byte pitch = (byte) (location.getPitch() * 256 / 360);

        PacketPlayOutEntityHeadRotation headRotationPacket = new PacketPlayOutEntityHeadRotation(
                this,
                yaw
        );
        sendPacket(player, headRotationPacket);

        PacketPlayOutEntity.PacketPlayOutEntityLook lookPacket = new PacketPlayOutEntity.PacketPlayOutEntityLook(
                getId(),
                yaw,
                pitch,
                false
        );
        sendPacket(player, lookPacket);
    }
    @Override
    public void setLocation(Location location){
        this.setLocation(
                location.getX(),
                location.getY(),
                location.getZ(),
                location.getYaw(),
                location.getPitch()
        );
    }
    @Override
    public int entityId(){
       return getBukkitEntity().getEntityId();
    }
}
