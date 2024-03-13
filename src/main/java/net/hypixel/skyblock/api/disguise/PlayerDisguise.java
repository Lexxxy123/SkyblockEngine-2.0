package net.hypixel.skyblock.api.disguise;

import com.mojang.authlib.GameProfile;
import lombok.Getter;
import net.hypixel.skyblock.SkyBlock;
import net.hypixel.skyblock.api.disguise.utils.ReflectionUtils;
import net.hypixel.skyblock.user.User;
import net.hypixel.skyblock.util.SUtil;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_8_R3.scoreboard.CraftScoreboard;
import org.bukkit.craftbukkit.v1_8_R3.scoreboard.CraftScoreboardManager;
import org.bukkit.entity.LivingEntity;
import com.mojang.authlib.properties.Property;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;

public class PlayerDisguise {
    public static final HashMap<Integer, PlayerDisguise> nonFake = new HashMap<>();
    public static final HashMap<Integer, PlayerDisguise> fake = new HashMap<>();
    private final HashSet<User> shown = new HashSet<>();
    private final LivingEntity entity;
    @Getter
    private final EntityPlayer fakePlayer;

    private Location l;
    private final BukkitRunnable runnable;


    public PlayerDisguise(LivingEntity entity, String texture, String value) {
        this(entity, new Property("textures", texture, value));
    }

    public PlayerDisguise(LivingEntity entity, Property skin) {
        l = entity.getLocation();
        this.entity = entity;
        GameProfile profile = new GameProfile(UUID.randomUUID(), "§§§§§9§9§9§9");

        new BukkitRunnable() {
            double maxH = 0;

            public void run() {
                if (entity.isDead()) {
                    cancel();
                    return;
                }
                if (maxH != getIAttribute(entity , GenericAttributes.maxHealth).getValue()){
                    getIAttribute(fakePlayer.getBukkitEntity() , GenericAttributes.maxHealth).setValue(getIAttribute(entity , GenericAttributes.maxHealth).getValue());
                }

                maxH = getIAttribute(entity , GenericAttributes.maxHealth).getValue();

                fakePlayer.setHealth((float) entity.getHealth());
            }
        }.runTaskTimer(SkyBlock.getPlugin(), 1, 1);

        if (skin != null) {
            profile.getProperties().put("textures", skin);
        }
        fakePlayer = new EntityPlayer(
                ((CraftServer) Bukkit.getServer()).getServer(),
                ((CraftWorld) entity.getWorld()).getHandle(),
                profile ,
                new PlayerInteractManager(
                        ((CraftWorld)entity.getWorld()
                        ).getHandle())
        );
        fakePlayer.setPosition(
                entity.getLocation().getX(),
                entity.getLocation().getY(),
                entity.getLocation().getZ());
        for (Player player : Bukkit.getOnlinePlayers()) {
            check(User.getUser(player));
        }


        nonFake.put(entity.getEntityId(), this);
        fake.put(fakePlayer.getId(), this);

        runnable = new BukkitRunnable() {
            int i = 0;

            @Override
            public void run() {
                i++;
                if (l.equals(entity.getLocation())) return;
                Location location = entity.getLocation();
                Packet<?> packet = null;
                if (entity.getLocation().distance(l) > 5 || i >= 20 * 5) {
                    System.out.println("sending entity teleport packet!");

                    packet = new PacketPlayOutEntityTeleport(
                            fakePlayer.getId() ,
                            MathHelper.floor(location.getX() * 32.0) ,
                            MathHelper.floor(location.getY() * 32.0),
                            MathHelper.floor(location.getZ() * 32.0),
                            (byte)((int)(location.getYaw() * 256.0F / 360.0F)),
                            (byte)((int)(location.getPitch() * 256.0F / 360.0F)),
                            entity.isOnGround()
                    );
                }
                if (packet != null) {
                    sendPacket(packet);
                }
                l = entity.getLocation();
            }
        };
        runnable.runTaskTimer(SkyBlock.getPlugin(), 1, 1);

    }

    private static final HashMap<Class<? extends Packet<?>>, String> idFieldsOut = new HashMap<>();
    private static final HashMap<Class<? extends Packet<?>>, String> idFieldsIn = new HashMap<>();

    static {
        idFieldsOut.put(PacketPlayOutEntity.PacketPlayOutEntityLook.class, "a");
        idFieldsOut.put(PacketPlayOutEntity.PacketPlayOutRelEntityMove.class, "a");
        idFieldsOut.put(PacketPlayOutEntity.PacketPlayOutRelEntityMoveLook.class, "a");
        idFieldsOut.put(PacketPlayOutEntityStatus.class, "a");
        idFieldsOut.put(PacketPlayOutEntityHeadRotation.class, "a");
        idFieldsOut.put(PacketPlayOutAttachEntity.class, "a");
        idFieldsOut.put(PacketPlayOutEntityVelocity.class, "a");
        idFieldsOut.put(PacketPlayOutEntityEffect.class, "a");
        idFieldsOut.put(PacketPlayOutEntityEquipment.class, "a");
        idFieldsOut.put(PacketPlayOutAnimation.class, "a");
        idFieldsOut.put(PacketPlayOutEntityMetadata.class, "a");
        idFieldsOut.put(PacketPlayOutNamedEntitySpawn.class, "a");
        idFieldsOut.put(PacketPlayOutSpawnEntityLiving.class, "a");
        idFieldsOut.put(PacketPlayOutSpawnEntity.class, "a");
        idFieldsIn.put(PacketPlayInEntityAction.class, "a");
        idFieldsIn.put(PacketPlayInUseEntity.class, "a");

    }


    public boolean onPacketOut(Packet<?> packet) {
        if (idFieldsOut.containsKey(packet.getClass()))
            ReflectionUtils.setField(idFieldsOut.get(packet.getClass()), packet, fakePlayer.getId());
        if (packet instanceof PacketPlayOutEntity.PacketPlayOutEntityLook || packet instanceof PacketPlayOutEntity.PacketPlayOutRelEntityMove || packet instanceof PacketPlayOutEntity.PacketPlayOutRelEntityMoveLook)
            return false;

        if (packet instanceof PacketPlayOutEntityStatus || packet instanceof PacketPlayOutEntityMetadata) {
            getIAttribute(fakePlayer.getBukkitEntity() , GenericAttributes.maxHealth).setValue(getIAttribute(entity , GenericAttributes.maxHealth).getValue());
            fakePlayer.setHealth((float) entity.getHealth());
        }
        // todo - fix it
//        if(packet instanceof PacketPlayOutEntityMetadata p) {
//            List<DataWatcher.Item<?>> items = new ArrayList<>(p.b());
//            for (DataWatcher.Item<?> it : p.b())
//                if(it.a().a() == 15 && (entity.getHealth() <= 0 || entity.isDead()))
//                    items.remove(it);
//            ReflectionUtils.setField("b", p, items);
//        }

        return true;
    }

    public void onPacketIn(Packet<?> packet) {
        if (idFieldsIn.containsKey(packet.getClass()))
            ReflectionUtils.setField(idFieldsIn.get(packet.getClass()), packet, entity.getEntityId());
    }
    @SuppressWarnings("deprecation")
    public void check(User player) {
        if (player == null) return;
        if (!shown.contains(player)) {
            if (inRangeOf(player.toBukkitPlayer() , entity.getLocation())) {
                shown.add(player);
                player.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, fakePlayer));
                player.sendPacket(new PacketPlayOutNamedEntitySpawn(fakePlayer));
                Location location = entity.getLocation();

                PacketPlayOutEntityTeleport entityTeleportPacket = new PacketPlayOutEntityTeleport(
                        fakePlayer.getId(),
                        MathHelper.floor(location.getX() * 32.0) ,
                        MathHelper.floor(location.getY() * 32.0),
                        MathHelper.floor(location.getZ() * 32.0),
                        (byte)((int)(location.getYaw() * 256.0F / 360.0F)),
                        (byte)((int)(location.getPitch() * 256.0F / 360.0F)),
                        entity.isOnGround()
                );

                player.sendPacket(entityTeleportPacket);
                player.sendPacket(new PacketPlayOutEntityDestroy(entity.getEntityId()));
                String s = 99 + "n" + fakePlayer.getUniqueID().toString().substring(1, 5);

                CraftScoreboardManager scoreboardManager = ((CraftServer) Bukkit.getServer()).getScoreboardManager();
                CraftScoreboard craftScoreboard = scoreboardManager.getMainScoreboard();
                Scoreboard scoreboard = craftScoreboard.getHandle();

                ScoreboardTeam scoreboardTeam = scoreboard.getTeam(s);
                if (scoreboardTeam == null){
                    scoreboardTeam = new ScoreboardTeam(scoreboard , s);
                }
                scoreboardTeam.setNameTagVisibility(ScoreboardTeamBase.EnumNameTagVisibility.NEVER);
                scoreboardTeam.setPrefix("[NPC] ");

                player.sendPacket(new PacketPlayOutScoreboardTeam(scoreboardTeam, 1));
                player.sendPacket(new PacketPlayOutScoreboardTeam(scoreboardTeam, 0));
                player.sendPacket(new PacketPlayOutScoreboardTeam(scoreboardTeam, Collections.singletonList(s), 3));


                // todo - implement equipments mapping
//                List<com.mojang.datafixers.util.Pair<EnumItemSlot, ItemStack>> items = new ArrayList<>();
//                for (EnumItemSlot slot : EnumItemSlot.values())
//                    items.add(new com.mojang.datafixers.util.Pair<>(slot, CraftItemStack.asNMSCopy(entity.getEquipment().getItem(transform(slot)))));
//                player.getHandle().b.sendPacket(new PacketPlayOutEntityEquipment(fakePlayer.getId(), items));
            }
        } else if (!inRangeOf(player.toBukkitPlayer() , entity.getLocation())) {
            shown.remove(player);
        }
    }


    private void sendPacket(Packet<?> packet) {
        if (shown.isEmpty()) return;
        for (User player : shown) {
            if (player == null) continue;
            player.sendPacket(packet);
        }
    }

    public static void packetInManager(Packet<?> packet) {
        if (!idFieldsIn.containsKey(packet.getClass())) return;
        int id = (int) ReflectionUtils.getField(ReflectionUtils.findField(packet.getClass(), idFieldsIn.get(packet.getClass())), packet);
        if (fake.containsKey(id))
            fake.get(id).onPacketIn(packet);
    }


    public boolean inRangeOf(Player player , Location location) {
        if (player == null) return false;
        if (!player.getWorld().equals(location.getWorld())) {
            // No need to continue our checks, they are in different worlds.
            return false;
        }

        // If Bukkit doesn't track the NPC entity anymore, bypass the hiding distance variable.
        // This will cause issues otherwise (e.g. custom skin disappearing).
        double hideDistance = 40;
        double distanceSquared = player.getLocation().distanceSquared(location);
        double bukkitRange = Bukkit.getViewDistance() << 4;

        return distanceSquared <= SUtil.square(hideDistance) && distanceSquared <= SUtil.square(bukkitRange);
    }

    public static boolean packetOutManager(Packet<?> packet) {
        if ((packet instanceof PacketPlayOutSpawnEntityLiving || packet instanceof PacketPlayOutSpawnEntity || packet instanceof PacketPlayOutNamedEntitySpawn) && nonFake.containsKey((int) ReflectionUtils.getField(ReflectionUtils.findField(packet.getClass(), idFieldsOut.get(packet.getClass())), packet)))
            return false;
        if (!idFieldsOut.containsKey(packet.getClass())) return true;
        int id = (int) ReflectionUtils.getField(ReflectionUtils.findField(packet.getClass(), idFieldsOut.get(packet.getClass())), packet);
        if (nonFake.containsKey(id))
            return nonFake.get(id).onPacketOut(packet);
        return true;
    }

    public void kill(User player) {
        if (player != null) {
            Vector vec = entity.getLocation().toVector().subtract(player.toBukkitPlayer().getLocation().toVector()).normalize().setY(0.3);
            PacketPlayOutEntityVelocity p = new PacketPlayOutEntityVelocity(fakePlayer.getId(), vec.getX(), vec.getY(), vec.getZ());
            sendPacket(p);
        }
        Bukkit.getScheduler().runTaskLater(SkyBlock.getPlugin(), () -> {
            entity.setHealth(0);
            fakePlayer.setHealth(0);
            sendPacket(new PacketPlayOutEntityStatus(fakePlayer, (byte) 60));
        }, 1);

        runnable.cancel();
        Bukkit.getScheduler().runTaskLater(SkyBlock.getPlugin(), () -> {
            sendPacket(new PacketPlayOutEntityDestroy(fakePlayer.getId()));
            nonFake.remove(entity.getEntityId());
            fake.remove(fakePlayer.getId());
            entity.remove();
        }, 20);

    }


    // utils method

    public AttributeInstance getIAttribute(LivingEntity entity , IAttribute iAttribute){
        return  ((CraftLivingEntity)entity).getHandle().getAttributeInstance(iAttribute);
    }


    public void status(byte b) {
        sendPacket(new PacketPlayOutEntityStatus(fakePlayer, b));
    }
}