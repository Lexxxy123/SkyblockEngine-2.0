package in.godspunky.skyblock.util;

import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.Messenger;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.net.InetSocketAddress;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;

public class BungeeChannel {
    private static WeakHashMap<Plugin, BungeeChannel> registeredInstances;

    static {
        BungeeChannel.registeredInstances = new WeakHashMap<Plugin, BungeeChannel>();
    }

    private final PluginMessageListener messageListener;
    private final Plugin plugin;
    private final Map<String, Queue<CompletableFuture<?>>> callbackMap;
    private Map<String, ForwardConsumer> forwardListeners;
    private ForwardConsumer globalForwardListener;

    public BungeeChannel(final Plugin plugin) {
        this.plugin = Objects.requireNonNull(plugin, "plugin cannot be null");
        this.callbackMap = new HashMap<String, Queue<CompletableFuture<?>>>();
        synchronized (BungeeChannel.registeredInstances) {
            BungeeChannel.registeredInstances.compute(plugin, (k, oldInstance) -> {
                if (oldInstance != null) {
                    oldInstance.unregister();
                }
                return this;
            });
        }
        this.messageListener = this::onPluginMessageReceived;
        final Messenger messenger = Bukkit.getServer().getMessenger();
        messenger.registerOutgoingPluginChannel(plugin, "BungeeCord");
        messenger.registerIncomingPluginChannel(plugin, "BungeeCord", this.messageListener);
    }

    public static synchronized BungeeChannel of(final Plugin plugin) {
        return BungeeChannel.registeredInstances.compute(plugin, (k, v) -> {
            if (v == null) {
                v = new BungeeChannel(plugin);
            }
            return v;
        });
    }

    public void registerForwardListener(final ForwardConsumer globalListener) {
        this.globalForwardListener = globalListener;
    }

    public void registerForwardListener(final String channelName, final ForwardConsumer listener) {
        if (this.forwardListeners == null) {
            this.forwardListeners = new HashMap<String, ForwardConsumer>();
        }
        synchronized (this.forwardListeners) {
            this.forwardListeners.put(channelName, listener);
        }
    }

    public CompletableFuture<Integer> getPlayerCount(final String serverName) {
        final Player player = this.getFirstPlayer();
        final CompletableFuture<Integer> future = new CompletableFuture<Integer>();
        synchronized (this.callbackMap) {
            this.callbackMap.compute("PlayerCount-" + serverName, this.computeQueueValue(future));
        }
        final ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeUTF("PlayerCount");
        output.writeUTF(serverName);
        player.sendPluginMessage(this.plugin, "BungeeCord", output.toByteArray());
        return future;
    }

    public CompletableFuture<List<String>> getPlayerList(final String serverName) {
        final Player player = this.getFirstPlayer();
        final CompletableFuture<List<String>> future = new CompletableFuture<List<String>>();
        synchronized (this.callbackMap) {
            this.callbackMap.compute("PlayerList-" + serverName, this.computeQueueValue(future));
        }
        final ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeUTF("PlayerList");
        output.writeUTF(serverName);
        player.sendPluginMessage(this.plugin, "BungeeCord", output.toByteArray());
        return future;
    }

    public CompletableFuture<List<String>> getServers() {
        final Player player = this.getFirstPlayer();
        final CompletableFuture<List<String>> future = new CompletableFuture<List<String>>();
        synchronized (this.callbackMap) {
            this.callbackMap.compute("GetServers", this.computeQueueValue(future));
        }
        final ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeUTF("GetServers");
        player.sendPluginMessage(this.plugin, "BungeeCord", output.toByteArray());
        return future;
    }

    public void connect(final Player player, final String serverName) {
        final ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeUTF("Connect");
        output.writeUTF(serverName);
        player.sendPluginMessage(this.plugin, "BungeeCord", output.toByteArray());
    }

    public void connectOther(final String playerName, final String server) {
        final Player player = this.getFirstPlayer();
        final ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeUTF("ConnectOther");
        output.writeUTF(playerName);
        output.writeUTF(server);
        player.sendPluginMessage(this.plugin, "BungeeCord", output.toByteArray());
    }

    public CompletableFuture<InetSocketAddress> getIp(final Player player) {
        final CompletableFuture<InetSocketAddress> future = new CompletableFuture<InetSocketAddress>();
        synchronized (this.callbackMap) {
            this.callbackMap.compute("IP", this.computeQueueValue(future));
        }
        final ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeUTF("IP");
        player.sendPluginMessage(this.plugin, "BungeeCord", output.toByteArray());
        return future;
    }

    public void sendMessage(final String playerName, final String message) {
        final Player player = this.getFirstPlayer();
        final ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeUTF("Message");
        output.writeUTF(playerName);
        output.writeUTF(message);
        player.sendPluginMessage(this.plugin, "BungeeCord", output.toByteArray());
    }

    public CompletableFuture<String> getServer() {
        final Player player = this.getFirstPlayer();
        final CompletableFuture<String> future = new CompletableFuture<String>();
        synchronized (this.callbackMap) {
            this.callbackMap.compute("GetServer", this.computeQueueValue(future));
        }
        final ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeUTF("GetServer");
        player.sendPluginMessage(this.plugin, "BungeeCord", output.toByteArray());
        return future;
    }

    public CompletableFuture<String> getUUID(final Player player) {
        final CompletableFuture<String> future = new CompletableFuture<String>();
        synchronized (this.callbackMap) {
            this.callbackMap.compute("UUID", this.computeQueueValue(future));
        }
        final ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeUTF("UUID");
        player.sendPluginMessage(this.plugin, "BungeeCord", output.toByteArray());
        return future;
    }

    public CompletableFuture<String> getUUID(final String playerName) {
        final Player player = this.getFirstPlayer();
        final CompletableFuture<String> future = new CompletableFuture<String>();
        synchronized (this.callbackMap) {
            this.callbackMap.compute("UUIDOther-" + playerName, this.computeQueueValue(future));
        }
        final ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeUTF("UUIDOther");
        output.writeUTF(playerName);
        player.sendPluginMessage(this.plugin, "BungeeCord", output.toByteArray());
        return future;
    }

    public CompletableFuture<InetSocketAddress> getServerIp(final String serverName) {
        final Player player = this.getFirstPlayer();
        final CompletableFuture<InetSocketAddress> future = new CompletableFuture<InetSocketAddress>();
        synchronized (this.callbackMap) {
            this.callbackMap.compute("ServerIP-" + serverName, this.computeQueueValue(future));
        }
        final ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeUTF("ServerIP");
        output.writeUTF(serverName);
        player.sendPluginMessage(this.plugin, "BungeeCord", output.toByteArray());
        return future;
    }

    public void kickPlayer(final String playerName, final String kickMessage) {
        final Player player = this.getFirstPlayer();
        final CompletableFuture<InetSocketAddress> future = new CompletableFuture<InetSocketAddress>();
        synchronized (this.callbackMap) {
            this.callbackMap.compute("KickPlayer", this.computeQueueValue(future));
        }
        final ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeUTF("KickPlayer");
        output.writeUTF(playerName);
        output.writeUTF(kickMessage);
        player.sendPluginMessage(this.plugin, "BungeeCord", output.toByteArray());
    }

    public void forward(final String server, final String channelName, final byte[] data) {
        final Player player = this.getFirstPlayer();
        final ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeUTF("Forward");
        output.writeUTF(server);
        output.writeUTF(channelName);
        output.writeShort(data.length);
        output.write(data);
        player.sendPluginMessage(this.plugin, "BungeeCord", output.toByteArray());
    }

    public void forwardToPlayer(final String playerName, final String channelName, final byte[] data) {
        final Player player = this.getFirstPlayer();
        final ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeUTF("ForwardToPlayer");
        output.writeUTF(playerName);
        output.writeUTF(channelName);
        output.writeShort(data.length);
        output.write(data);
        player.sendPluginMessage(this.plugin, "BungeeCord", output.toByteArray());
    }

    private void onPluginMessageReceived(final String channel, final Player player, final byte[] message) {
        if (!channel.equalsIgnoreCase("BungeeCord")) {
            return;
        }
        final ByteArrayDataInput input = ByteStreams.newDataInput(message);
        final String subchannel = input.readUTF();
        synchronized (this.callbackMap) {
            if (subchannel.equals("PlayerCount") || subchannel.equals("PlayerList") || subchannel.equals("UUIDOther") || subchannel.equals("ServerIP")) {
                final String identifier = input.readUTF();
                final Queue<CompletableFuture<?>> callbacks = this.callbackMap.get(subchannel + "-" + identifier);
                if (callbacks == null || callbacks.isEmpty()) {
                    return;
                }
                CompletableFuture<Object> callback = (CompletableFuture<Object>) callbacks.poll();
                try {
                    final String s = subchannel;
                    switch (s) {
                        case "PlayerCount":
                            callback.complete(input.readInt());
                            break;
                        case "PlayerList":
                            callback.complete(Arrays.asList(input.readUTF().split(", ")));
                            break;
                        case "UUIDOther":
                            callback.complete(input.readUTF());
                            break;
                        case "ServerIP": {
                            final String ip = input.readUTF();
                            final int port = input.readUnsignedShort();
                            callback.complete(new InetSocketAddress(ip, port));
                            break;
                        }
                    }
                } catch (final Exception ex) {
                    callback.completeExceptionally(ex);
                }
            } else {
                final Queue<CompletableFuture<?>> callbacks = this.callbackMap.get(subchannel);
                if (callbacks == null) {
                    final short dataLength = input.readShort();
                    final byte[] data = new byte[dataLength];
                    input.readFully(data);
                    if (this.globalForwardListener != null) {
                        this.globalForwardListener.accept(subchannel, player, data);
                    }
                    if (this.forwardListeners != null) {
                        synchronized (this.forwardListeners) {
                            final ForwardConsumer listener = this.forwardListeners.get(subchannel);
                            if (listener != null) {
                                listener.accept(subchannel, player, data);
                            }
                        }
                    }
                    return;
                }
                if (callbacks.isEmpty()) {
                    return;
                }
                CompletableFuture<Object> callback2 = (CompletableFuture<Object>) callbacks.poll();
                try {
                    final String s2 = subchannel;
                    switch (s2) {
                        case "GetServers":
                            callback2.complete(Arrays.asList(input.readUTF().split(", ")));
                            break;
                        case "GetServer":
                        case "UUID":
                            callback2.complete(input.readUTF());
                            break;
                        case "IP": {
                            final String ip2 = input.readUTF();
                            final int port2 = input.readInt();
                            callback2.complete(new InetSocketAddress(ip2, port2));
                            break;
                        }
                    }
                } catch (final Exception ex2) {
                    callback2.completeExceptionally(ex2);
                }
            }
        }
    }

    public void unregister() {
        final Messenger messenger = Bukkit.getServer().getMessenger();
        messenger.unregisterIncomingPluginChannel(this.plugin, "BungeeCord", this.messageListener);
        messenger.unregisterOutgoingPluginChannel(this.plugin);
        this.callbackMap.clear();
    }

    private BiFunction<String, Queue<CompletableFuture<?>>, Queue<CompletableFuture<?>>> computeQueueValue(final CompletableFuture<?> queueValue) {
        return (key, value) -> {
            if (value == null) {
                value = new ArrayDeque();
            }
            value.add(queueValue);
            return value;
        };
    }

    private Player getFirstPlayer() {
        final Player firstPlayer = this.getFirstPlayer0(Bukkit.getOnlinePlayers());
        if (firstPlayer == null) {
            throw new IllegalArgumentException("Bungee Messaging Api requires at least one player to be online.");
        }
        return firstPlayer;
    }

    private Player getFirstPlayer0(final Player[] playerArray) {
        return (playerArray.length > 0) ? playerArray[0] : null;
    }

    private Player getFirstPlayer0(final Collection<? extends Player> playerCollection) {
        return (Player) Iterables.getFirst(playerCollection, (Object) null);
    }

    @FunctionalInterface
    public interface ForwardConsumer {
        void accept(final String p0, final Player p1, final byte[] p2);
    }
}
