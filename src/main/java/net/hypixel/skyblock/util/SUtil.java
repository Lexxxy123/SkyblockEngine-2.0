/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParser
 *  com.mojang.authlib.GameProfile
 *  com.mojang.authlib.properties.Property
 *  com.sk89q.worldedit.EditSession
 *  com.sk89q.worldedit.Vector
 *  com.sk89q.worldedit.WorldEdit
 *  com.sk89q.worldedit.WorldEditException
 *  com.sk89q.worldedit.bukkit.BukkitWorld
 *  com.sk89q.worldedit.extent.Extent
 *  com.sk89q.worldedit.extent.clipboard.Clipboard
 *  com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat
 *  com.sk89q.worldedit.function.operation.Operation
 *  com.sk89q.worldedit.function.operation.Operations
 *  com.sk89q.worldedit.session.ClipboardHolder
 *  com.sk89q.worldedit.world.World
 *  com.sk89q.worldedit.world.registry.WorldData
 *  net.minecraft.server.v1_8_R3.IChatBaseComponent
 *  net.minecraft.server.v1_8_R3.IChatBaseComponent$ChatSerializer
 *  net.minecraft.server.v1_8_R3.ItemStack
 *  net.minecraft.server.v1_8_R3.NBTBase
 *  net.minecraft.server.v1_8_R3.NBTCompressedStreamTools
 *  net.minecraft.server.v1_8_R3.NBTTagByte
 *  net.minecraft.server.v1_8_R3.NBTTagCompound
 *  net.minecraft.server.v1_8_R3.NBTTagDouble
 *  net.minecraft.server.v1_8_R3.NBTTagFloat
 *  net.minecraft.server.v1_8_R3.NBTTagInt
 *  net.minecraft.server.v1_8_R3.NBTTagLong
 *  net.minecraft.server.v1_8_R3.NBTTagShort
 *  net.minecraft.server.v1_8_R3.NBTTagString
 *  net.minecraft.server.v1_8_R3.Packet
 *  net.minecraft.server.v1_8_R3.PacketPlayOutChat
 *  net.minecraft.server.v1_8_R3.PacketPlayOutTitle
 *  net.minecraft.server.v1_8_R3.PacketPlayOutTitle$EnumTitleAction
 *  net.minecraft.server.v1_8_R3.PathfinderGoalSelector
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Color
 *  org.bukkit.GameMode
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.World
 *  org.bukkit.block.Block
 *  org.bukkit.configuration.ConfigurationSection
 *  org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer
 *  org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack
 *  org.bukkit.craftbukkit.v1_8_R3.util.UnsafeList
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.entity.ArmorStand
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Item
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.NPC
 *  org.bukkit.entity.Player
 *  org.bukkit.entity.Projectile
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.ItemFlag
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.inventory.meta.LeatherArmorMeta
 *  org.bukkit.inventory.meta.SkullMeta
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 *  org.bukkit.scheduler.BukkitTask
 *  org.bukkit.util.Vector
 */
package net.hypixel.skyblock.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.registry.WorldData;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import net.hypixel.skyblock.SkyBlock;
import net.hypixel.skyblock.features.merchant.MerchantItemHandler;
import net.hypixel.skyblock.features.potion.PotionColor;
import net.hypixel.skyblock.features.potion.PotionEffect;
import net.hypixel.skyblock.gui.GUI;
import net.hypixel.skyblock.item.GenericItemType;
import net.hypixel.skyblock.item.MaterialStatistics;
import net.hypixel.skyblock.item.Rarity;
import net.hypixel.skyblock.item.SItem;
import net.hypixel.skyblock.item.SMaterial;
import net.hypixel.skyblock.util.Groups;
import net.hypixel.skyblock.util.SLog;
import net.hypixel.skyblock.util.Sputnik;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.NBTBase;
import net.minecraft.server.v1_8_R3.NBTCompressedStreamTools;
import net.minecraft.server.v1_8_R3.NBTTagByte;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTTagDouble;
import net.minecraft.server.v1_8_R3.NBTTagFloat;
import net.minecraft.server.v1_8_R3.NBTTagInt;
import net.minecraft.server.v1_8_R3.NBTTagLong;
import net.minecraft.server.v1_8_R3.NBTTagShort;
import net.minecraft.server.v1_8_R3.NBTTagString;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import net.minecraft.server.v1_8_R3.PathfinderGoalSelector;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_8_R3.util.UnsafeList;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.NPC;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

public class SUtil {
    public static final SimpleDateFormat DATE_FORMAT;
    private static final NumberFormat COMMA_FORMAT;
    private static final List<ChatColor> CRIT_SPECTRUM;
    private static final List<ChatColor> VISIBLE_COLOR_SPECTRUM;
    public static final String[] numToStr;

    public static String commaify(int i2) {
        return COMMA_FORMAT.format(i2);
    }

    public static String commaify(double d2) {
        return COMMA_FORMAT.format(d2);
    }

    public static String commaify(long l2) {
        return COMMA_FORMAT.format(l2);
    }

    public static List<String> getPlayerNameList() {
        ArrayList<String> names = new ArrayList<String>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            names.add(player.getName());
        }
        return names;
    }

    @NotNull
    public static GenericItemType getItemType(@NotNull SMaterial material) {
        MaterialStatistics statistics = material.getStatistics();
        if (statistics != null) {
            return statistics.getType();
        }
        return GenericItemType.NONE;
    }

    public static String getTimeDifferenceAndColor(long start, long end) {
        return SUtil.getColorBasedOnSize(end - start, 20, 5000, 10000) + "" + (end - start) + "ms";
    }

    public static ChatColor getColorBasedOnSize(long num, int low, int med, int high) {
        if (num <= (long)low) {
            return ChatColor.GREEN;
        }
        if (num <= (long)med) {
            return ChatColor.YELLOW;
        }
        if (num <= (long)high) {
            return ChatColor.RED;
        }
        return ChatColor.DARK_RED;
    }

    public static int random(int min, int max) {
        if (min < 0) {
            min = 0;
        }
        if (max < 0) {
            max = 0;
        }
        return new Random().nextInt(max - min + 1) + min;
    }

    public static double random(double min, double max) {
        return Math.random() * (max - min) + min;
    }

    public static ItemStack getSkull(String texture, ItemStack stack, SMaterial material) {
        String stringUUID;
        SkullMeta meta = (SkullMeta)stack.getItemMeta();
        SkyBlock plugin = SkyBlock.getPlugin();
        if (material != null) {
            if (!plugin.heads.contains(material.name().toLowerCase())) {
                plugin.heads.set(material.name().toLowerCase(), UUID.randomUUID().toString());
                plugin.heads.save();
            }
            stringUUID = plugin.heads.getString(material.name().toLowerCase());
        } else {
            stringUUID = UUID.randomUUID().toString();
        }
        GameProfile profile = new GameProfile(UUID.fromString(stringUUID), null);
        byte[] ed = java.util.Base64.getEncoder().encode(String.format("{textures:{SKIN:{url:\"http://textures.minecraft.net/texture/%s\"}}}", texture).getBytes());
        profile.getProperties().put((Object)"textures", (Object)new Property("textures", new String(ed)));
        try {
            Field f2 = meta.getClass().getDeclaredField("profile");
            f2.setAccessible(true);
            f2.set(meta, profile);
        } catch (IllegalAccessException | IllegalArgumentException | NoSuchFieldException exception) {
            // empty catch block
        }
        stack.setItemMeta((ItemMeta)meta);
        return stack;
    }

    public static ItemStack getSkullURL(String url) {
        ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, 3);
        if (url.isEmpty()) {
            return head;
        }
        SkullMeta headMeta = (SkullMeta)head.getItemMeta();
        headMeta.setDisplayName(ChatColor.translateAlternateColorCodes((char)'&', (String)"Bonzo"));
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        byte[] encodedData = java.util.Base64.getEncoder().encode(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());
        profile.getProperties().put((Object)"textures", (Object)new Property("textures", new String(encodedData)));
        Field profileField = null;
        try {
            profileField = headMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(headMeta, profile);
        } catch (IllegalAccessException | IllegalArgumentException | NoSuchFieldException e1) {
            e1.printStackTrace();
        }
        head.setItemMeta((ItemMeta)headMeta);
        return head;
    }

    public static ItemStack getSkull(String texture, SMaterial material) {
        return SUtil.getSkull(texture, new ItemStack(Material.SKULL_ITEM, 1, 3), material);
    }

    public static List<String> splitByWordAndLength(String string, int splitLength, String separator) {
        ArrayList<String> result = new ArrayList<String>();
        Pattern pattern = Pattern.compile("\\G" + separator + "*(.{1," + splitLength + "})(?=\\s|$)", 32);
        Matcher matcher = pattern.matcher(string);
        while (matcher.find()) {
            result.add(matcher.group(1));
        }
        return result;
    }

    public static ItemStack applyColorToLeatherArmor(ItemStack stack, Color color) {
        if (!(stack.getItemMeta() instanceof LeatherArmorMeta)) {
            return stack;
        }
        LeatherArmorMeta meta = (LeatherArmorMeta)stack.getItemMeta();
        meta.setColor(color);
        stack.setItemMeta((ItemMeta)meta);
        return stack;
    }

    public static String color(String string) {
        return ChatColor.translateAlternateColorCodes((char)'&', (String)string);
    }

    public static String toRomanNumeral(int num) {
        StringBuilder sb = new StringBuilder();
        String[] romans = new String[]{"I", "IV", "V", "IX", "X", "XL", "L", "XC", "C", "CD", "D", "CM", "M"};
        int[] ints = new int[]{1, 4, 5, 9, 10, 40, 50, 90, 100, 400, 500, 900, 1000};
        for (int i2 = ints.length - 1; i2 >= 0; --i2) {
            int times = num / ints[i2];
            num %= ints[i2];
            while (times > 0) {
                sb.append(romans[i2]);
                --times;
            }
        }
        return sb.toString();
    }

    public static String rainbowize(String string) {
        StringBuilder builder = new StringBuilder();
        int i2 = 0;
        for (String c2 : string.split("")) {
            if (i2 > CRIT_SPECTRUM.size() - 1) {
                i2 = 0;
            }
            builder.append(CRIT_SPECTRUM.get(i2)).append(c2);
            ++i2;
        }
        return builder.toString();
    }

    public static String getMaterialDisplayName(Material material, short variant) {
        if (variant != 0) {
            return SMaterial.getSpecEquivalent(material, variant).getBaseName();
        }
        net.minecraft.server.v1_8_R3.ItemStack nmsStack = CraftItemStack.asNMSCopy((ItemStack)new ItemStack(material));
        if (nmsStack == null) {
            return material.name();
        }
        if (nmsStack.getItem() == null) {
            return material.name();
        }
        return nmsStack.getName();
    }

    public static void sendActionBar(Player player, String message) {
        PacketPlayOutChat packet = new PacketPlayOutChat(IChatBaseComponent.ChatSerializer.a((String)("{\"text\":\"" + message + "\"}")), 2);
        ((CraftPlayer)player).getHandle().playerConnection.sendPacket((Packet)packet);
    }

    public static GenericItemType getItemType(Material material) {
        if (material == Material.BOW) {
            return GenericItemType.RANGED_WEAPON;
        }
        if (Groups.SWORDS.contains(material)) {
            return GenericItemType.WEAPON;
        }
        if (Groups.PICKAXES.contains(material) || Groups.HOES.contains(material) || Groups.AXES.contains(material) || Groups.SHOVELS.contains(material)) {
            return GenericItemType.TOOL;
        }
        if (Groups.LEATHER_ARMOR.contains(material) || Groups.IRON_ARMOR.contains(material) || Groups.GOLD_ARMOR.contains(material) || Groups.DIAMOND_ARMOR.contains(material)) {
            return GenericItemType.ARMOR;
        }
        return material.isBlock() ? GenericItemType.BLOCK : GenericItemType.ITEM;
    }

    public static ItemStack createNamedItemStack(Material material, String name) {
        ItemStack stack = new ItemStack(material);
        if (name != null) {
            ItemMeta meta = stack.getItemMeta();
            meta.setDisplayName(name);
            stack.setItemMeta(meta);
        }
        return stack;
    }

    public static ItemStack createColoredStainedGlassPane(short data, String name) {
        ItemStack stack = SUtil.createNamedItemStack(Material.STAINED_GLASS_PANE, name);
        stack.setDurability(data);
        return stack;
    }

    public static ItemStack getColorStack(short data, String name, List<String> lore, short dat, int amount) {
        ItemStack stack = SUtil.getStack(name, Material.STAINED_GLASS_PANE, dat, amount, lore);
        stack.setDurability(data);
        return stack;
    }

    public static void border(Inventory inventory, GUI gui, ItemStack stack, int cornerSlot, int cornerSlot2, boolean overwrite, boolean pickup) {
        int bottomLeft;
        int topRight;
        if (cornerSlot < 0 || cornerSlot > inventory.getSize()) {
            throw new IllegalArgumentException("Corner 1 of the border described is out of bounds");
        }
        if (cornerSlot2 < 0 || cornerSlot2 > inventory.getSize()) {
            throw new IllegalArgumentException("Corner 2 of the border described is out of bounds");
        }
        int topLeft = Math.min(cornerSlot, cornerSlot2);
        int bottomRight = topRight = Math.max(cornerSlot, cornerSlot2);
        while (topRight > topLeft) {
            topRight -= 9;
        }
        for (bottomLeft = topLeft; bottomLeft < bottomRight; bottomLeft += 9) {
        }
        topRight += 9;
        bottomLeft -= 9;
        for (int y2 = topLeft; y2 <= bottomLeft; y2 += 9) {
            for (int x2 = y2; x2 <= topRight - topLeft + y2; ++x2) {
                int f2 = x2;
                if (gui.getItems().stream().filter(item -> item.getSlot() == f2).toArray().length != 0 && !overwrite) continue;
                if (y2 == topLeft || y2 == bottomLeft) {
                    gui.set(x2, stack, pickup);
                    inventory.setItem(x2, stack);
                }
                if (x2 != y2 && x2 != topRight - topLeft + y2) continue;
                gui.set(x2, stack, pickup);
                inventory.setItem(x2, stack);
            }
        }
    }

    public static void sendTypedTitle(Player player, String message, PacketPlayOutTitle.EnumTitleAction type) {
        IChatBaseComponent chatTitle = IChatBaseComponent.ChatSerializer.a((String)("{\"text\": \"" + message + "\"}"));
        PacketPlayOutTitle title = new PacketPlayOutTitle(type, chatTitle);
        PacketPlayOutTitle length = new PacketPlayOutTitle(5, 60, 5);
        ((CraftPlayer)player).getHandle().playerConnection.sendPacket((Packet)title);
        ((CraftPlayer)player).getHandle().playerConnection.sendPacket((Packet)length);
    }

    public static void sendTitle(Player player, String message) {
        SUtil.sendTypedTitle(player, message, PacketPlayOutTitle.EnumTitleAction.TITLE);
    }

    public static void sendSubtitle(Player player, String message) {
        SUtil.sendTypedTitle(player, message, PacketPlayOutTitle.EnumTitleAction.SUBTITLE);
    }

    public static void generate(Location loc, String filename) {
        try {
            FileInputStream fis = new FileInputStream(new File(SkyBlock.getPlugin().getDataFolder(), filename));
            Object nbtData = NBTCompressedStreamTools.class.getMethod("a", InputStream.class).invoke(null, fis);
            Method getShort = nbtData.getClass().getMethod("getShort", String.class);
            Method getByteArray = nbtData.getClass().getMethod("getByteArray", String.class);
            int width = ((Short)getShort.invoke(nbtData, "Width")).shortValue();
            int height = ((Short)getShort.invoke(nbtData, "Height")).shortValue();
            int length = ((Short)getShort.invoke(nbtData, "Length")).shortValue();
            byte[] blocks = (byte[])getByteArray.invoke(nbtData, "Blocks");
            byte[] data = (byte[])getByteArray.invoke(nbtData, "Data");
            fis.close();
            for (int x2 = 0; x2 < width; ++x2) {
                for (int y2 = 0; y2 < height; ++y2) {
                    for (int z2 = 0; z2 < length; ++z2) {
                        int index = y2 * width * length + z2 * width + x2;
                        int b2 = blocks[index] & 0xFF;
                        Material m2 = Material.getMaterial((int)b2);
                        if (m2 == Material.AIR) continue;
                        Block block = new Location(loc.getWorld(), (double)(loc.getBlockX() - width / 2 + x2), (double)(loc.getBlockY() + y2 - 19), (double)(loc.getBlockZ() - length / 2 + z2 + 14)).getBlock();
                        block.setType(m2, true);
                        block.setData(data[index]);
                    }
                }
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    public static void lightningLater(final Location location, final boolean effect, long delay) {
        new BukkitRunnable(){

            public void run() {
                if (effect) {
                    location.getWorld().strikeLightningEffect(location);
                } else {
                    location.getWorld().strikeLightning(location);
                }
            }
        }.runTaskLater((Plugin)SkyBlock.getPlugin(), delay);
    }

    public static void runIntervalForTicks(final Runnable runnable, long interval, long end) {
        final AtomicBoolean stop = new AtomicBoolean(false);
        new BukkitRunnable(){

            public void run() {
                if (stop.get()) {
                    this.cancel();
                    return;
                }
                runnable.run();
            }
        }.runTaskTimer((Plugin)SkyBlock.getPlugin(), 0L, interval);
        new BukkitRunnable(){

            public void run() {
                stop.set(true);
            }
        }.runTaskLater((Plugin)SkyBlock.getPlugin(), end);
    }

    public static String getDate() {
        return DATE_FORMAT.format(new Date());
    }

    public static Item spawnPersonalItem(ItemStack stack, Location location, Player player) {
        Item item = location.getWorld().dropItem(location, stack);
        item.setMetadata("owner", (MetadataValue)new FixedMetadataValue((Plugin)SkyBlock.getPlugin(), (Object)player.getUniqueId().toString()));
        return item;
    }

    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        ArrayList<Map.Entry<K, V>> list = new ArrayList<Map.Entry<K, V>>(map.entrySet());
        list.sort(Map.Entry.comparingByValue());
        LinkedHashMap result = new LinkedHashMap();
        for (Map.Entry entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    public static <T> boolean addIf(T t2, List<T> list, boolean test) {
        if (test) {
            list.add(t2);
        }
        return test;
    }

    public static ItemStack setStackAmount(ItemStack stack, int amount) {
        stack.setAmount(amount);
        return stack;
    }

    public static SItem setSItemAmount(SItem item, int amount) {
        item.getStack().setAmount(amount);
        return item;
    }

    public static double roundTo(double d2, int decimalPlaces) {
        if (decimalPlaces < 1) {
            throw new IllegalArgumentException();
        }
        StringBuilder builder = new StringBuilder().append("#.");
        for (int i2 = 0; i2 < decimalPlaces; ++i2) {
            builder.append("#");
        }
        DecimalFormat df = new DecimalFormat(builder.toString());
        df.setRoundingMode(RoundingMode.CEILING);
        return Double.parseDouble(df.format(d2));
    }

    public static void toggleAllowFlightNoCreative(UUID uuid, boolean flight) {
        Player player = Bukkit.getPlayer((UUID)uuid);
        if (player == null) {
            return;
        }
        GameMode gameMode = player.getGameMode();
        if (gameMode == GameMode.CREATIVE || gameMode == GameMode.SPECTATOR) {
            return;
        }
        player.setAllowFlight(flight);
    }

    public static List<Block> getNearbyBlocks(Location location, int radius, Material type) {
        ArrayList<Block> blocks = new ArrayList<Block>();
        for (int x2 = location.getBlockX() - radius; x2 <= location.getBlockX() + radius; ++x2) {
            for (int y2 = location.getBlockY() - radius; y2 <= location.getBlockY() + radius; ++y2) {
                for (int z2 = location.getBlockZ() - radius; z2 <= location.getBlockZ() + radius; ++z2) {
                    Block block = location.getWorld().getBlockAt(x2, y2, z2);
                    if (block.getType() != type && type != null) continue;
                    blocks.add(block);
                }
            }
        }
        return blocks;
    }

    public static void markAimingArmorStand(final ArmorStand projectile) {
        final AtomicReference<Object> target = new AtomicReference<Object>(null);
        new BukkitRunnable(){

            public void run() {
                if (projectile.isDead()) {
                    this.cancel();
                    return;
                }
                if (target.get() == null) {
                    ArrayList<LivingEntity> possible = new ArrayList<LivingEntity>();
                    for (Entity entity : projectile.getNearbyEntities(5.0, 5.0, 5.0)) {
                        if (entity instanceof Player || !(entity instanceof LivingEntity) || entity instanceof ArmorStand || entity instanceof NPC || entity.isDead()) continue;
                        possible.add((LivingEntity)entity);
                    }
                    LivingEntity setTarget = (LivingEntity)SUtil.getRandom(possible);
                    if (setTarget == null) {
                        return;
                    }
                    target.set(setTarget);
                }
                Location location = projectile.getLocation().clone();
                org.bukkit.util.Vector vector = location.clone().toVector().subtract(((LivingEntity)target.get()).getLocation().clone().add(0.0, 1.0, 0.0).toVector());
                location.setYaw((float)Math.atan2(vector.getX(), vector.getZ()));
                projectile.teleport(location);
                projectile.setVelocity(vector.clone().multiply(-1.0).multiply(0.2));
            }
        }.runTaskTimer((Plugin)SkyBlock.getPlugin(), 0L, 1L);
    }

    public static void giantsHitboxFix(final Projectile projectile) {
        final AtomicReference<Object> target = new AtomicReference<Object>(null);
        new BukkitRunnable(){

            public void run() {
                if (projectile.isDead()) {
                    this.cancel();
                    return;
                }
                if (target.get() == null) {
                    LivingEntity setTarget = null;
                    for (Entity entity : projectile.getNearbyEntities(2.0, 12.0, 2.0)) {
                        if (!entity.hasMetadata("Giant_")) continue;
                        setTarget = (LivingEntity)entity;
                    }
                    if (setTarget == null) {
                        return;
                    }
                    target.set(setTarget);
                }
                Location location = projectile.getLocation().clone();
                org.bukkit.util.Vector vector = location.clone().toVector().subtract(((LivingEntity)target.get()).getLocation().clone().add(0.0, 1.0, 0.0).toVector());
                location.setYaw((float)Math.atan2(vector.getX(), vector.getZ()));
                projectile.teleport(location);
                projectile.setVelocity(vector.clone().multiply(-1.0).multiply(0.5));
            }
        }.runTaskTimer((Plugin)SkyBlock.getPlugin(), 0L, 1L);
        new BukkitRunnable(){

            public void run() {
                projectile.remove();
            }
        }.runTaskLater((Plugin)SkyBlock.getPlugin(), 140L);
    }

    public static void markAimingArrow(final Projectile projectile, final net.hypixel.skyblock.features.enchantment.Enchantment aiming) {
        if (aiming == null) {
            return;
        }
        final AtomicReference<Object> target = new AtomicReference<Object>(null);
        new BukkitRunnable(){

            public void run() {
                if (projectile.isDead()) {
                    this.cancel();
                    return;
                }
                if (target.get() == null) {
                    ArrayList<LivingEntity> possible = new ArrayList<LivingEntity>();
                    int aiminglvl = aiming.getLevel();
                    if (aiminglvl > 4) {
                        aiminglvl = 4;
                    }
                    for (Entity entity : projectile.getNearbyEntities((double)(2 * aiminglvl), (double)(2 * aiminglvl), (double)(2 * aiminglvl))) {
                        if (entity instanceof Player || !(entity instanceof LivingEntity) || entity instanceof ArmorStand || entity instanceof NPC || entity.isDead() || entity.hasMetadata("GiantSword")) continue;
                        possible.add((LivingEntity)entity);
                    }
                    LivingEntity setTarget = (LivingEntity)SUtil.getRandom(possible);
                    if (setTarget == null) {
                        return;
                    }
                    target.set(setTarget);
                }
                Location location = projectile.getLocation().clone();
                org.bukkit.util.Vector vector = location.clone().toVector().subtract(((LivingEntity)target.get()).getLocation().clone().add(0.0, 1.0, 0.0).toVector());
                location.setYaw((float)Math.atan2(vector.getX(), vector.getZ()));
                projectile.teleport(location);
                projectile.setVelocity(vector.clone().multiply(-1.0).multiply(0.15));
            }
        }.runTaskTimer((Plugin)SkyBlock.getPlugin(), 0L, 1L);
        new BukkitRunnable(){

            public void run() {
                projectile.remove();
            }
        }.runTaskLater((Plugin)SkyBlock.getPlugin(), 80L);
    }

    public static ItemStack getStack(String name, Material material, short data, int amount, List<String> lore) {
        ItemStack stack = new ItemStack(material, (int)data);
        stack.setDurability(data);
        ItemMeta meta = stack.getItemMeta();
        if (name != null) {
            meta.setDisplayName(name);
        }
        stack.setAmount(amount);
        meta.setLore(lore);
        meta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ATTRIBUTES});
        stack.setItemMeta(meta);
        return stack;
    }

    public static ItemStack getStack(String name, Material material, short data, int amount, String ... lore) {
        return SUtil.getStack(name, material, data, amount, Arrays.asList(lore));
    }

    public static ItemStack getSkullStack(String name, String skullName, int amount, String ... lore) {
        ItemStack stack = SUtil.getStack(name, Material.SKULL_ITEM, (short)3, amount, lore);
        SkullMeta meta = (SkullMeta)stack.getItemMeta();
        meta.setOwner(skullName);
        stack.setItemMeta((ItemMeta)meta);
        return stack;
    }

    public static ItemStack getSkullURLStack(String name, String url, int amount, String ... lore) {
        return SUtil.getSkull(url, SUtil.getStack(name, Material.SKULL_ITEM, (short)3, amount, lore), null);
    }

    public static ItemStack getSingleLoreStack(String name, Material material, short data, int amount, String lore) {
        ArrayList<String> l2 = new ArrayList<String>();
        for (String line : SUtil.splitByWordAndLength(lore, 30, "\\s")) {
            l2.add(ChatColor.GRAY + line);
        }
        return SUtil.getStack(name, material, data, amount, l2.toArray(new String[0]));
    }

    public static boolean isEnchantable(SItem sItem) {
        if (sItem.getType() == SMaterial.ENCHANTED_BOOK) {
            return true;
        }
        GenericItemType type = sItem.getType().getStatistics().getType();
        return type == GenericItemType.WEAPON || type == GenericItemType.TOOL || type == GenericItemType.RANGED_WEAPON || type == GenericItemType.ARMOR || type == GenericItemType.WAND;
    }

    public static boolean isHotPotatoAble(SItem item) {
        GenericItemType type = item.getType().getStatistics().getType();
        return (type == GenericItemType.WEAPON || type == GenericItemType.RANGED_WEAPON || type == GenericItemType.ARMOR) && item.getDataInt("hpb") < 10;
    }

    public static boolean isRecomable(SItem sitem) {
        GenericItemType type = sitem.getType().getStatistics().getType();
        return type == GenericItemType.PET && !sitem.isRecombobulated();
    }

    public static boolean isAir(ItemStack is) {
        return is == null || is.getType() == Material.AIR;
    }

    public static List<String> combineElements(List<String> list, String separator, int perElement) {
        ArrayList<String> n2 = new ArrayList<String>();
        for (int i2 = 0; i2 < list.size(); i2 += perElement) {
            StringBuilder builder = new StringBuilder();
            for (int j2 = 0; j2 < perElement && i2 + j2 <= list.size() - 1; ++j2) {
                builder.append(j2 != 0 ? separator : "").append(list.get(i2 + j2));
            }
            n2.add(builder.toString());
        }
        return n2;
    }

    public static boolean pasteSchematic(File schematicFile, Location location, boolean withAir) {
        try {
            Vector pasteLocation = new Vector(location.getX(), location.getY(), location.getZ());
            BukkitWorld pasteWorld = new BukkitWorld(location.getWorld());
            WorldData pasteWorldData = pasteWorld.getWorldData();
            Clipboard clipboard = ClipboardFormat.SCHEMATIC.getReader((InputStream)new FileInputStream(schematicFile)).read(pasteWorldData);
            ClipboardHolder clipboardHolder = new ClipboardHolder(clipboard, pasteWorldData);
            EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession((com.sk89q.worldedit.world.World)pasteWorld, -1);
            Operation operation = clipboardHolder.createPaste((Extent)editSession, pasteWorldData).to(pasteLocation).ignoreAirBlocks(!withAir).build();
            Operations.complete((Operation)operation);
            return true;
        } catch (WorldEditException | IOException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static void setBlocks(Location c1, Location c2, Material material, boolean applyPhysics) {
        if (!c1.getWorld().getName().equals(c1.getWorld().getName())) {
            return;
        }
        int sy = Math.min(c1.getBlockY(), c2.getBlockY());
        int ey = Math.max(c1.getBlockY(), c2.getBlockY());
        int sx = Math.min(c1.getBlockX(), c2.getBlockX());
        int ex = Math.max(c1.getBlockX(), c2.getBlockX());
        int sz = Math.min(c1.getBlockZ(), c2.getBlockZ());
        int ez = Math.max(c1.getBlockZ(), c2.getBlockZ());
        World world = c1.getWorld();
        for (int y2 = sy; y2 <= ey; ++y2) {
            for (int x2 = sx; x2 <= ex; ++x2) {
                for (int z2 = sz; z2 <= ez; ++z2) {
                    world.getBlockAt(x2, y2, z2).setType(material, applyPhysics);
                }
            }
        }
    }

    public static <T> T instance(Class<T> clazz, Object ... params) {
        Class[] paramClasses = new Class[params.length];
        for (int i2 = 0; i2 < paramClasses.length; ++i2) {
            paramClasses[i2] = params[i2].getClass();
        }
        try {
            Constructor<T> constructor = clazz.getConstructor(paramClasses);
            constructor.setAccessible(true);
            return constructor.newInstance(params);
        } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException ex) {
            return null;
        }
    }

    public static <C, T> T getDeclaredField(C instance, String name, Class<T> type) {
        try {
            Field f2 = instance.getClass().getDeclaredField(name);
            f2.setAccessible(true);
            return type.cast(f2.get(instance));
        } catch (IllegalAccessException | NoSuchFieldException ex) {
            return null;
        }
    }

    public static Object getObjectFromCompound(NBTTagCompound compound, String key) {
        Object o2 = null;
        switch (compound.get(key).getTypeId()) {
            case 1: {
                o2 = compound.getByte(key);
                break;
            }
            case 2: {
                o2 = compound.getShort(key);
                break;
            }
            case 3: {
                o2 = compound.getInt(key);
                break;
            }
            case 4: {
                o2 = compound.getLong(key);
                break;
            }
            case 5: {
                o2 = Float.valueOf(compound.getFloat(key));
                break;
            }
            case 6: {
                o2 = compound.getDouble(key);
                break;
            }
            case 7: {
                o2 = compound.getByteArray(key);
                break;
            }
            case 10: {
                o2 = compound.getCompound(key);
                break;
            }
            case 11: {
                o2 = compound.getIntArray(key);
                break;
            }
            default: {
                o2 = compound.getString(key);
            }
        }
        return o2;
    }

    public static NBTBase getBaseFromObject(Object o2) {
        if (o2 instanceof Byte) {
            return new NBTTagByte(((Byte)o2).byteValue());
        }
        if (o2 instanceof Short) {
            return new NBTTagShort(((Short)o2).shortValue());
        }
        if (o2 instanceof Integer) {
            return new NBTTagInt(((Integer)o2).intValue());
        }
        if (o2 instanceof Long) {
            return new NBTTagLong(((Long)o2).longValue());
        }
        if (o2 instanceof Float) {
            return new NBTTagFloat(((Float)o2).floatValue());
        }
        if (o2 instanceof Double) {
            return new NBTTagDouble(((Double)o2).doubleValue());
        }
        if (o2 instanceof String) {
            return new NBTTagString((String)o2);
        }
        return null;
    }

    public static NBTBase getBaseFromObject(ConfigurationSection cs, String key) {
        return SUtil.getBaseFromObject(cs.get(key));
    }

    public static ChatColor getRandomVisibleColor() {
        return VISIBLE_COLOR_SPECTRUM.get(SUtil.random(0, VISIBLE_COLOR_SPECTRUM.size() - 1));
    }

    public static <T> T getRandom(List<T> list) {
        if (list.size() == 0) {
            return null;
        }
        return list.get(SUtil.random(0, list.size() - 1));
    }

    public static void broadcastExcept(String message, Player player) {
        for (Player p2 : player.getWorld().getPlayers()) {
            if (p2.getUniqueId().equals(player.getUniqueId())) continue;
            p2.sendMessage(message);
        }
        SLog.info(message);
    }

    public static void broadcast(String message, Player player) {
        for (Player p2 : player.getWorld().getPlayers()) {
            p2.sendMessage(message);
        }
        SLog.info("[SYSTEM LOG] " + message);
    }

    public static void globalBroadcast(String message) {
        for (Player p2 : Bukkit.getOnlinePlayers()) {
            p2.sendMessage(message);
        }
        SLog.info("[SYSTEM LOG] " + message);
    }

    public static void broadcastWorld(String message, World w2) {
        for (Player p2 : w2.getPlayers()) {
            p2.sendMessage(message);
        }
    }

    public static ItemStack enchant(ItemStack stack) {
        ItemMeta meta = stack.getItemMeta();
        meta.addEnchant(Enchantment.DURABILITY, 1, true);
        stack.setItemMeta(meta);
        return stack;
    }

    public static SItem enchant(SItem item, net.hypixel.skyblock.features.enchantment.Enchantment ... enchantments) {
        for (net.hypixel.skyblock.features.enchantment.Enchantment enchantment : enchantments) {
            item.addEnchantment(enchantment.getType(), enchantment.getLevel());
        }
        return item;
    }

    public static byte[] gzipCompress(byte[] uncompressedData) {
        byte[] result = new byte[]{};
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream(uncompressedData.length);
             GZIPOutputStream gzipOS = new GZIPOutputStream(bos);){
            gzipOS.write(uncompressedData);
            gzipOS.close();
            result = bos.toByteArray();
        } catch (IOException iOException) {
            // empty catch block
        }
        return result;
    }

    public static byte[] gzipUncompress(byte[] compressedData) {
        byte[] result = new byte[]{};
        try (ByteArrayInputStream bis = new ByteArrayInputStream(compressedData);
             ByteArrayOutputStream bos = new ByteArrayOutputStream();
             GZIPInputStream gzipIS = new GZIPInputStream(bis);){
            int len;
            byte[] buffer = new byte[1024];
            while ((len = gzipIS.read(buffer)) != -1) {
                bos.write(buffer, 0, len);
            }
            result = bos.toByteArray();
        } catch (IOException iOException) {
            // empty catch block
        }
        return result;
    }

    public static double midpoint(int x2, int y2) {
        return (double)(x2 + y2) / 2.0;
    }

    public static double midpoint(double x2, double y2) {
        return (x2 + y2) / 2.0;
    }

    public static void clearGoalSelector(PathfinderGoalSelector goalSelector) {
        try {
            Field b2 = PathfinderGoalSelector.class.getDeclaredField("b");
            Field c2 = PathfinderGoalSelector.class.getDeclaredField("c");
            b2.setAccessible(true);
            c2.setAccessible(true);
            ((UnsafeList)b2.get(goalSelector)).clear();
            ((UnsafeList)c2.get(goalSelector)).clear();
        } catch (IllegalAccessException | NoSuchFieldException e2) {
            e2.printStackTrace();
        }
    }

    public static <T> T getOrDefault(List<T> list, int index, T def) {
        if (index < 0 || index >= list.size()) {
            return def;
        }
        return list.get(index);
    }

    public static <T> T getOrDefault(T[] array, int index, T def) {
        if (index < 0 || index >= array.length) {
            return def;
        }
        return array[index];
    }

    public static String zeroed(long l2) {
        return l2 > 9L ? "" + l2 : "0" + l2;
    }

    public static String getFormattedTime(long t2, int div) {
        long seconds = t2 / (long)div;
        long hours = seconds / 3600L;
        long minutes = (seconds -= hours * 3600L) / 60L;
        return (hours != 0L ? hours + ":" : "") + SUtil.zeroed(minutes) + ":" + SUtil.zeroed(seconds -= minutes * 60L);
    }

    public static String getFormattedTimeToDay(long l2) {
        long seconds = Math.round(l2 / 20L);
        int day = (int)TimeUnit.SECONDS.toDays(seconds);
        int hours = (int)(TimeUnit.SECONDS.toHours(seconds) - TimeUnit.DAYS.toHours(day));
        int minute = (int)(TimeUnit.SECONDS.toMinutes(seconds) - TimeUnit.DAYS.toMinutes(day) - TimeUnit.HOURS.toMinutes(hours));
        int second = (int)(TimeUnit.SECONDS.toSeconds(seconds) - TimeUnit.DAYS.toSeconds(day) - TimeUnit.HOURS.toSeconds(hours) - TimeUnit.MINUTES.toSeconds(minute));
        return day + "d " + hours + "h " + minute + "m " + second + "s";
    }

    public static String getFormattedTime(long ticks) {
        return SUtil.getFormattedTime(ticks, 20);
    }

    public static String getSlayerFormattedTime(long millis) {
        long seconds = millis / 1000L;
        long hours = seconds / 3600L;
        long minutes = (seconds -= hours * 3600L) / 60L;
        return (hours != 0L ? SUtil.zeroed(hours) + "h" : "") + SUtil.zeroed(minutes) + "m" + SUtil.zeroed(seconds -= minutes * 60L) + "s";
    }

    public static double quadrt(double d2) {
        return Math.pow(d2, 0.25);
    }

    public static void delay(final Runnable runnable, long delay) {
        new BukkitRunnable(){

            public void run() {
                runnable.run();
            }
        }.runTaskLater((Plugin)SkyBlock.getPlugin(), delay);
    }

    public static double square(double val) {
        return val * val;
    }

    public static SItem toShopItem(SMaterial smaterial, int amount, Long price, Long value) {
        SItem item = SItem.of(smaterial);
        item.getStack().setAmount(amount);
        item.setPrice(price);
        item.setItemValue(value);
        MerchantItemHandler.ITEMS.put(smaterial, item);
        return item;
    }

    public static GameProfile createGameProfile(String url) {
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        byte[] ed = java.util.Base64.getEncoder().encode(String.format("{textures:{SKIN:{url:\"http://textures.minecraft.net/texture/%s\"}}}", url).getBytes());
        profile.getProperties().put((Object)"textures", (Object)new Property("textures", new String(ed)));
        return profile;
    }

    public static float getLookAtYaw(org.bukkit.util.Vector motion) {
        double dx = motion.getX();
        double dz = motion.getZ();
        double yaw = 0.0;
        if (dx != 0.0) {
            yaw = dx < 0.0 ? 4.71238898038469 : 1.5707963267948966;
            yaw -= Math.atan(dz / dx);
        } else if (dz < 0.0) {
            yaw = Math.PI;
        }
        return (float)(-yaw * 180.0 / Math.PI - 90.0);
    }

    public static String ntify(int i2) {
        if (i2 == 11 || i2 == 12 || i2 == 13) {
            return i2 + "th";
        }
        String s2 = String.valueOf(i2);
        char last = s2.charAt(s2.length() - 1);
        switch (last) {
            case '1': {
                return i2 + "st";
            }
            case '2': {
                return i2 + "nd";
            }
            case '3': {
                return i2 + "rd";
            }
        }
        return i2 + "th";
    }

    public static String pad(String s2, int length) {
        return String.format("%-" + length + "s", s2);
    }

    public static <T> List<T> shuffle(List<T> list) {
        ThreadLocalRandom rnd = ThreadLocalRandom.current();
        for (int i2 = list.size() - 1; i2 > 0; --i2) {
            int index = ((Random)rnd).nextInt(i2 + 1);
            T t2 = list.get(index);
            list.set(index, list.get(i2));
            list.set(i2, t2);
        }
        return list;
    }

    public static <T> int deepLength(T[][] array2d) {
        int c2 = 0;
        for (T[] array : array2d) {
            c2 += array.length;
        }
        return c2;
    }

    public static <T> T[] unnest(T[][] array2d, Class<T> clazz) {
        Object[] array = (Object[])Array.newInstance(clazz, SUtil.deepLength(array2d));
        int c2 = 0;
        for (int i2 = 0; i2 < array2d.length; ++i2) {
            int j2 = 0;
            while (j2 < array2d[i2].length) {
                array[c2] = array2d[i2][j2];
                ++j2;
                ++c2;
            }
        }
        return array;
    }

    public static void runAsync(Runnable runnable) {
        new Thread(runnable).start();
    }

    public static void runSync(final Runnable runnable) {
        new BukkitRunnable(){

            public void run() {
                runnable.run();
            }
        }.runTask((Plugin)SkyBlock.getPlugin());
    }

    public static ItemStack idToSkull(ItemStack head, String id) {
        JsonParser parser = new JsonParser();
        JsonObject o2 = parser.parse(new String(Base64.decodeBase64(id))).getAsJsonObject();
        String skinUrl = o2.get("textures").getAsJsonObject().get("SKIN").getAsJsonObject().get("url").getAsString();
        SkullMeta headMeta = (SkullMeta)head.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        byte[] encodedData = Base64.encodeBase64(("{textures:{SKIN:{url:\"" + skinUrl + "\"}}}").getBytes());
        profile.getProperties().put((Object)"textures", (Object)new Property("textures", new String(encodedData)));
        try {
            Field profileField = headMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(headMeta, profile);
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        head.setItemMeta((ItemMeta)headMeta);
        return head;
    }

    public static String createProgressText(String text, int current, int max) {
        double percent = max != 0 ? (double)current / (double)max * 100.0 : 0.0;
        percent = Math.round(percent * 10.0 / 10.0);
        return ChatColor.GRAY + text + ": " + (percent < 100.0 ? ChatColor.YELLOW + SUtil.commaify(percent) + ChatColor.GOLD + "%" : ChatColor.GREEN + "100.0%");
    }

    public static String createProgressText(String text, double current, double max) {
        double percent = max != 0.0 ? current / max * 100.0 : 0.0;
        percent = Math.round(percent * 10.0 / 10.0);
        return ChatColor.GRAY + text + ": " + (percent < 100.0 ? ChatColor.YELLOW + SUtil.commaify(percent) + ChatColor.GOLD + "%" : ChatColor.GREEN + "100.0%");
    }

    public static String createLineProgressBar(int length, ChatColor progressColor, int current, int max) {
        double percent = Math.min((double)current, (double)max) / (double)max;
        long completed = Math.round((double)length * percent);
        StringBuilder builder = new StringBuilder().append(progressColor);
        int i2 = 0;
        while ((long)i2 < completed) {
            builder.append("-");
            ++i2;
        }
        builder.append(ChatColor.WHITE);
        i2 = 0;
        while ((long)i2 < (long)length - completed) {
            builder.append("-");
            ++i2;
        }
        builder.append(" ").append(ChatColor.YELLOW).append(SUtil.commaify(current)).append(ChatColor.GOLD).append("/").append(ChatColor.YELLOW).append(SUtil.commaify(max));
        return builder.toString();
    }

    public static String createSLineProgressBar(int length, ChatColor progressColor, double current, double max) {
        double percent = Math.min(current, max) / max;
        long completed = Math.round((double)length * percent);
        StringBuilder builder = new StringBuilder().append(progressColor);
        int i2 = 0;
        while ((long)i2 < completed) {
            builder.append("-");
            ++i2;
        }
        builder.append(ChatColor.WHITE);
        i2 = 0;
        while ((long)i2 < (long)length - completed) {
            builder.append("-");
            ++i2;
        }
        builder.append(" ").append(ChatColor.YELLOW).append(SUtil.commaify(current)).append(ChatColor.GOLD).append("/").append(ChatColor.YELLOW).append(Sputnik.formatFull((float)max));
        return builder.toString();
    }

    public static String createLineProgressBar(int length, ChatColor progressColor, double current, double max) {
        double percent = Math.min(current, max) / max;
        long completed = Math.round((double)length * percent);
        StringBuilder builder = new StringBuilder().append(progressColor);
        int i2 = 0;
        while ((long)i2 < completed) {
            builder.append("-");
            ++i2;
        }
        builder.append(ChatColor.WHITE);
        i2 = 0;
        while ((long)i2 < (long)length - completed) {
            builder.append("-");
            ++i2;
        }
        builder.append(" ").append(ChatColor.YELLOW).append(SUtil.commaify(current)).append(ChatColor.GOLD).append("/").append(ChatColor.YELLOW).append(SUtil.commaify(max));
        return builder.toString();
    }

    public static <T> T[] toArray(List<T> list, Class<T> clazz) {
        Object[] array = (Object[])Array.newInstance(clazz, list.size());
        for (int i2 = 0; i2 < list.size(); ++i2) {
            array[i2] = list.get(i2);
        }
        return array;
    }

    public static Rarity findPotionRarity(int level) {
        switch (level) {
            case 0: 
            case 1: 
            case 2: {
                return Rarity.COMMON;
            }
            case 3: 
            case 4: {
                return Rarity.UNCOMMON;
            }
            case 5: 
            case 6: {
                return Rarity.RARE;
            }
            case 7: 
            case 8: {
                return Rarity.EPIC;
            }
            case 9: 
            case 10: {
                return Rarity.LEGENDARY;
            }
            case 11: 
            case 12: {
                return Rarity.MYTHIC;
            }
            case 13: 
            case 14: {
                return Rarity.SUPREME;
            }
            case 15: 
            case 16: {
                return Rarity.SPECIAL;
            }
        }
        return Rarity.VERY_SPECIAL;
    }

    public static PotionColor getTopColor(SItem item) {
        if (!item.isPotion()) {
            return null;
        }
        int topLevel = 0;
        PotionColor color = null;
        for (PotionEffect effect : item.getPotionEffects()) {
            if (effect.getLevel() <= topLevel) continue;
            topLevel = effect.getLevel();
            color = effect.getType().getColor();
        }
        return color;
    }

    public static boolean canFitStack(Inventory inventory, ItemStack fit) {
        for (ItemStack stack : inventory) {
            if (stack == null || !fit.equals((Object)stack) || stack.getAmount() + fit.getAmount() > 64) continue;
            return true;
        }
        return false;
    }

    public static BukkitTask repeatingTask(final Runnable runnable, long delay, long interval) {
        return new BukkitRunnable(){

            public void run() {
                runnable.run();
            }
        }.runTaskTimer((Plugin)SkyBlock.getPlugin(), delay, interval);
    }

    public static int blackMagic(double d2) {
        return (int)d2;
    }

    public static String prettify(Object obj) {
        Class<?> clazz = obj.getClass();
        if (clazz == Location.class) {
            Location location = (Location)obj;
            return location.getX() + ", " + location.getY() + ", " + location.getZ() + ", " + location.getWorld().getName() + ", " + location.getYaw() + ", " + location.getPitch();
        }
        return "No pretty!";
    }

    public static String toNormalCase(String string) {
        string = string.replaceAll("_", " ");
        Object[] spl = string.split(" ");
        for (int i2 = 0; i2 < spl.length; ++i2) {
            String s2 = spl[i2];
            if (s2.length() == 0) continue;
            spl[i2] = s2.length() == 1 ? s2.toUpperCase() : s2.substring(0, 1).toUpperCase() + s2.substring(1).toLowerCase();
        }
        return StringUtils.join(spl, " ");
    }

    public static String getAuctionFormattedTime(long millis) {
        if (millis == 0L) {
            return "Ended!";
        }
        if ((double)millis >= 8.64E7) {
            return Math.round((double)millis / 8.64E7) + "d";
        }
        if ((double)millis >= 2.16E7) {
            return Math.round((double)millis / 3600000.0) + "h";
        }
        long seconds = millis / 1000L;
        long hours = seconds / 3600L;
        long minutes = (seconds -= hours * 3600L) / 60L;
        seconds -= minutes * 60L;
        StringBuilder builder = new StringBuilder();
        if (hours > 0L) {
            builder.append(hours).append("h ");
        }
        builder.append(minutes).append("m ").append(seconds).append("s");
        return builder.toString();
    }

    public static String getAuctionSetupFormattedTime(long millis) {
        String dur;
        if ((double)millis >= 8.64E7) {
            long days = Math.round((double)millis / 8.64E7);
            dur = days + " Day";
            if (days != 1L) {
                dur = dur + "s";
            }
        } else if (millis >= 3600000L) {
            long hours = Math.round((double)millis / 3600000.0);
            dur = hours + " Hour";
            if (hours != 1L) {
                dur = dur + "s";
            }
        } else {
            long minutes = Math.round((double)millis / 60000.0);
            dur = minutes + " Minute";
            if (minutes != 1L) {
                dur = dur + "s";
            }
        }
        return dur;
    }

    static {
        numToStr = new String[]{"", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine"};
        DATE_FORMAT = new SimpleDateFormat("MM/dd/yy");
        COMMA_FORMAT = NumberFormat.getInstance();
        CRIT_SPECTRUM = Arrays.asList(ChatColor.WHITE, ChatColor.WHITE, ChatColor.YELLOW, ChatColor.GOLD, ChatColor.RED, ChatColor.RED);
        VISIBLE_COLOR_SPECTRUM = Arrays.asList(ChatColor.DARK_GREEN, ChatColor.DARK_AQUA, ChatColor.DARK_RED, ChatColor.DARK_PURPLE, ChatColor.GOLD, ChatColor.GREEN, ChatColor.AQUA, ChatColor.RED, ChatColor.LIGHT_PURPLE, ChatColor.YELLOW, ChatColor.WHITE);
        COMMA_FORMAT.setGroupingUsed(true);
    }
}

