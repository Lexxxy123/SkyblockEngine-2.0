package in.godspunky.skyblock.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldedit.world.registry.WorldData;
import in.godspunky.skyblock.enchantment.Enchantment;
import in.godspunky.skyblock.item.GenericItemType;
import in.godspunky.skyblock.item.Rarity;
import in.godspunky.skyblock.item.SItem;
import in.godspunky.skyblock.merchant.MerchantItemHandler;
import in.godspunky.skyblock.potion.PotionColor;
import in.godspunky.skyblock.potion.PotionEffect;
import net.minecraft.server.v1_8_R3.*;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Material;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_8_R3.util.UnsafeList;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.NPC;
import org.bukkit.entity.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;
import in.godspunky.skyblock.SkySimEngine;
import in.godspunky.skyblock.gui.GUI;
import in.godspunky.skyblock.item.SMaterial;

import java.io.*;
import java.lang.reflect.*;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class SUtil {
    public static final SimpleDateFormat DATE_FORMAT;
    private static final NumberFormat COMMA_FORMAT;
    private static final List<ChatColor> CRIT_SPECTRUM;
    private static final List<ChatColor> VISIBLE_COLOR_SPECTRUM;

    public static String commaify(final int i) {
        return SUtil.COMMA_FORMAT.format(i);
    }

    public static String commaify(final double d) {
        return SUtil.COMMA_FORMAT.format(d);
    }

    public static String commaify(final long l) {
        return SUtil.COMMA_FORMAT.format(l);
    }

    public static List<String> getPlayerNameList() {
        final List<String> names = new ArrayList<String>();
        for (final Player player : Bukkit.getOnlinePlayers()) {
            names.add(player.getName());
        }
        return names;
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

    public static boolean isUUID(String input) {
        try {
            // Try parsing the input as a UUID
            UUID.fromString(input);
            return true;
        } catch (IllegalArgumentException e) {
            // If an exception is thrown, it's not a valid UUID
            return false;
        }
    }

    private static final String[] FRUITS = {
            "Apple", "Banana", "Blueberry", "Coconut", "Cucumber", "Grapes", "Kiwi",
            "Lemon", "Lime", "Mango", "Orange", "Papaya", "Peach", "Pear",
            "Pineapple", "Pomegranate", "Raspberry", "Strawberry", "Tomato",
            "Watermelon", "Zucchini"
    };
    public static String generateRandomProfileNameFor() {
        Random random = new Random();
        int index = random.nextInt(FRUITS.length);
        return FRUITS[index];
    }

    public static double random(final double min, final double max) {
        return Math.random() * (max - min) + min;
    }

    public static ItemStack getSkull(final String texture, final ItemStack stack, final SMaterial material) {
        final SkullMeta meta = (SkullMeta) stack.getItemMeta();
        final SkySimEngine plugin = SkySimEngine.getPlugin();
        String stringUUID;
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
        byte[] ed = Base64.getEncoder().encode(String.format("{textures:{SKIN:{url:\"http://textures.minecraft.net/texture/%s\"}}}", texture).getBytes());
        profile.getProperties().put("textures", new Property("textures", new String(ed)));
        try {
            final Field f = meta.getClass().getDeclaredField("profile");
            f.setAccessible(true);
            f.set(meta, profile);
        } catch (final NoSuchFieldException | IllegalArgumentException | IllegalAccessException ex) {
        }
        stack.setItemMeta(meta);
        return stack;
    }

    public static ItemStack getSkullURL(final String url) {
        final ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        if (url.isEmpty()) {
            return head;
        }
        SkullMeta headMeta = (SkullMeta) head.getItemMeta();
        headMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "Bonzo"));
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        byte[] encodedData = Base64.getEncoder().encode(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());
        profile.getProperties().put("textures", new Property("textures", new String(encodedData)));
        Field profileField = null;
        try {
            profileField = headMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(headMeta, profile);
        } catch (final NoSuchFieldException | IllegalArgumentException | IllegalAccessException e1) {
            e1.printStackTrace();
        }
        head.setItemMeta(headMeta);
        return head;
    }

    public static ItemStack getSkull(final String texture, final SMaterial material) {
        return getSkull(texture, new ItemStack(Material.SKULL_ITEM, 1, (short) 3), material);
    }

    public static List<String> splitByWordAndLength(final String string, final int splitLength, final String separator) {
        final List<String> result = new ArrayList<String>();
        final Pattern pattern = Pattern.compile("\\G" + separator + "*(.{1," + splitLength + "})(?=\\s|$)", 32);
        final Matcher matcher = pattern.matcher(string);
        while (matcher.find()) {
            result.add(matcher.group(1));
        }
        return result;
    }

    public static ItemStack applyColorToLeatherArmor(final ItemStack stack, final Color color) {
        if (!(stack.getItemMeta() instanceof LeatherArmorMeta)) {
            return stack;
        }
        final LeatherArmorMeta meta = (LeatherArmorMeta) stack.getItemMeta();
        meta.setColor(color);
        stack.setItemMeta(meta);
        return stack;
    }

    public static String color(final String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static String toRomanNumeral(int num) {
        final StringBuilder sb = new StringBuilder();
        final String[] romans = {"I", "IV", "V", "IX", "X", "XL", "L", "XC", "C", "CD", "D", "CM", "M"};
        final int[] ints = {1, 4, 5, 9, 10, 40, 50, 90, 100, 400, 500, 900, 1000};
        for (int i = ints.length - 1; i >= 0; --i) {
            int times = num / ints[i];
            num %= ints[i];
            while (times > 0) {
                sb.append(romans[i]);
                --times;
            }
        }
        return sb.toString();
    }

    public static String rainbowize(final String string) {
        final StringBuilder builder = new StringBuilder();
        int i = 0;
        for (final String c : string.split("")) {
            if (i > SUtil.CRIT_SPECTRUM.size() - 1) {
                i = 0;
            }
            builder.append(SUtil.CRIT_SPECTRUM.get(i)).append(c);
            ++i;
        }
        return builder.toString();
    }

    public static String getMaterialDisplayName(final Material material, final short variant) {
        if (variant != 0) {
            return SMaterial.getSpecEquivalent(material, variant).getBaseName();
        }
        final net.minecraft.server.v1_8_R3.ItemStack nmsStack = CraftItemStack.asNMSCopy(new ItemStack(material));
        if (nmsStack == null) {
            return material.name();
        }
        if (nmsStack.getItem() == null) {
            return material.name();
        }
        return nmsStack.getName();
    }

    public static void sendActionBar(final Player player, final String message) {
        final PacketPlayOutChat packet = new PacketPlayOutChat(IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + message + "\"}"), (byte) 2);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }

    public static GenericItemType getItemType(final Material material) {
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

    public static ItemStack createNamedItemStack(final Material material, final String name) {
        final ItemStack stack = new ItemStack(material);
        if (name != null) {
            final ItemMeta meta = stack.getItemMeta();
            meta.setDisplayName(name);
            stack.setItemMeta(meta);
        }
        return stack;
    }

    public static ItemStack createColoredStainedGlassPane(final short data, final String name) {
        final ItemStack stack = createNamedItemStack(Material.STAINED_GLASS_PANE, name);
        stack.setDurability(data);
        return stack;
    }

    public static ItemStack getColorStack(final short data, final String name, final List<String> lore, final short dat, final int amount) {
        final ItemStack stack = getStack(name, Material.STAINED_GLASS_PANE, dat, amount, lore);
        stack.setDurability(data);
        return stack;
    }

    public static void border(final Inventory inventory, final GUI gui, final ItemStack stack, final int cornerSlot, final int cornerSlot2, final boolean overwrite, final boolean pickup) {
        if (cornerSlot < 0 || cornerSlot > inventory.getSize()) {
            throw new IllegalArgumentException("Corner 1 of the border described is out of bounds");
        }
        if (cornerSlot2 < 0 || cornerSlot2 > inventory.getSize()) {
            throw new IllegalArgumentException("Corner 2 of the border described is out of bounds");
        }
        int topLeft;
        int topRight;
        int bottomRight;
        for (topLeft = Math.min(cornerSlot, cornerSlot2), bottomRight = (topRight = Math.max(cornerSlot, cornerSlot2)); topRight > topLeft; topRight -= 9) {
        }
        int bottomLeft;
        for (bottomLeft = topLeft; bottomLeft < bottomRight; bottomLeft += 9) {
        }
        topRight += 9;
        bottomLeft -= 9;
        for (int y = topLeft; y <= bottomLeft; y += 9) {
            for (int x = y; x <= topRight - topLeft + y; ++x) {
                final int f = x;
                if (gui.getItems().stream().filter(item -> item.getSlot() == f).toArray().length == 0 || overwrite) {
                    if (y == topLeft || y == bottomLeft) {
                        gui.set(x, stack, pickup);
                        inventory.setItem(x, stack);
                    }
                    if (x == y || x == topRight - topLeft + y) {
                        gui.set(x, stack, pickup);
                        inventory.setItem(x, stack);
                    }
                }
            }
        }
    }

    public static void sendTypedTitle(final Player player, final String message, final PacketPlayOutTitle.EnumTitleAction type) {
        final IChatBaseComponent chatTitle = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + message + "\"}");
        final PacketPlayOutTitle title = new PacketPlayOutTitle(type, chatTitle);
        final PacketPlayOutTitle length = new PacketPlayOutTitle(5, 60, 5);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(title);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(length);
    }

    public static void sendTitle(final Player player, final String message) {
        sendTypedTitle(player, message, PacketPlayOutTitle.EnumTitleAction.TITLE);
    }

    public static void sendSubtitle(final Player player, final String message) {
        sendTypedTitle(player, message, PacketPlayOutTitle.EnumTitleAction.SUBTITLE);
    }

    public static void generate(Location loc, String filename) {
        try {
            FileInputStream fis = new FileInputStream(new File(SkySimEngine.getPlugin().getDataFolder(), filename));
            Object nbtData = NBTCompressedStreamTools.class.getMethod("a", InputStream.class).invoke(null, fis);
            Method getShort  = nbtData.getClass().getMethod("getShort", String.class);
            Method getByteArray = nbtData.getClass().getMethod("getByteArray", String.class);

            short width = ((short) getShort.invoke(nbtData, "Width"));
            short height = ((short) getShort.invoke(nbtData, "Height"));
            short length = ((short) getShort.invoke(nbtData, "Length"));

            byte[] blocks = ((byte[]) getByteArray.invoke(nbtData, "Blocks"));
            byte[] data = ((byte[]) getByteArray.invoke(nbtData, "Data"));

            fis.close();
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    for (int z = 0; z < length; z++) {
                        int index = y * width * length + z * width + x;
                        int b = blocks[index] & 0xFF;
                        Material m = Material.getMaterial(b);
                        if (m != Material.AIR) {

                            Block block = new Location(loc.getWorld(),
                                    loc.getBlockX() - ((int) (width / 2)) + x,
                                    loc.getBlockY()  + y - 19,
                                    loc.getBlockZ() - ((int) (length / 2)) + z + 14).getBlock();
                            block.setType(m, true);
                            block.setData(data[index]);

                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void lightningLater(final Location location, final boolean effect, final long delay) {
        new BukkitRunnable() {
            public void run() {
                if (effect) {
                    location.getWorld().strikeLightningEffect(location);
                } else {
                    location.getWorld().strikeLightning(location);
                }
            }
        }.runTaskLater(SkySimEngine.getPlugin(), delay);
    }

    public static void runIntervalForTicks(final Runnable runnable, final long interval, final long end) {
        final AtomicBoolean stop = new AtomicBoolean(false);
        new BukkitRunnable() {
            public void run() {
                if (stop.get()) {
                    this.cancel();
                    return;
                }
                runnable.run();
            }
        }.runTaskTimer(SkySimEngine.getPlugin(), 0L, interval);
        new BukkitRunnable() {
            public void run() {
                stop.set(true);
            }
        }.runTaskLater(SkySimEngine.getPlugin(), end);
    }

    public static String getDate() {
        return SUtil.DATE_FORMAT.format(new Date());
    }

    public static Item spawnPersonalItem(final ItemStack stack, final Location location, final Player player) {
        final Item item = location.getWorld().dropItem(location, stack);
        item.setMetadata("owner", new FixedMetadataValue(SkySimEngine.getPlugin(), player.getUniqueId().toString()));
        return item;
    }

    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(final Map<K, V> map) {
        final List<Map.Entry<K, V>> list = new ArrayList<Map.Entry<K, V>>(map.entrySet());
        list.sort((Comparator<? super Map.Entry<K, V>>) Map.Entry.<Object, Comparable>comparingByValue());
        final Map<K, V> result = new LinkedHashMap<K, V>();
        for (final Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    public static <T> boolean addIf(final T t, final List<T> list, final boolean test) {
        if (test) {
            list.add(t);
        }
        return test;
    }

    public static ItemStack setStackAmount(final ItemStack stack, final int amount) {
        stack.setAmount(amount);
        return stack;
    }

    public static SItem setSItemAmount(final SItem item, final int amount) {
        item.getStack().setAmount(amount);
        return item;
    }

    public static double roundTo(final double d, final int decimalPlaces) {
        if (decimalPlaces < 1) {
            throw new IllegalArgumentException();
        }
        final StringBuilder builder = new StringBuilder().append("#.");
        for (int i = 0; i < decimalPlaces; ++i) {
            builder.append("#");
        }
        final DecimalFormat df = new DecimalFormat(builder.toString());
        df.setRoundingMode(RoundingMode.CEILING);
        return Double.parseDouble(df.format(d));
    }

    public static void toggleAllowFlightNoCreative(final UUID uuid, final boolean flight) {
        final Player player = Bukkit.getPlayer(uuid);
        if (player == null) {
            return;
        }
        final GameMode gameMode = player.getGameMode();
        if (gameMode == GameMode.CREATIVE || gameMode == GameMode.SPECTATOR) {
            return;
        }
        player.setAllowFlight(flight);
    }

    public static List<Block> getNearbyBlocks(final Location location, final int radius, final Material type) {
        final List<Block> blocks = new ArrayList<Block>();
        for (int x = location.getBlockX() - radius; x <= location.getBlockX() + radius; ++x) {
            for (int y = location.getBlockY() - radius; y <= location.getBlockY() + radius; ++y) {
                for (int z = location.getBlockZ() - radius; z <= location.getBlockZ() + radius; ++z) {
                    final Block block = location.getWorld().getBlockAt(x, y, z);
                    if (block.getType() == type || type == null) {
                        blocks.add(block);
                    }
                }
            }
        }
        return blocks;
    }

    public static void markAimingArmorStand(final ArmorStand projectile) {
        final AtomicReference<LivingEntity> target = new AtomicReference<LivingEntity>(null);
        new BukkitRunnable() {
            public void run() {
                if (projectile.isDead()) {
                    this.cancel();
                    return;
                }
                if (target.get() == null) {
                    final List<LivingEntity> possible = new ArrayList<LivingEntity>();
                    for (final Entity entity : projectile.getNearbyEntities(5.0, 5.0, 5.0)) {
                        if (entity instanceof Player) {
                            continue;
                        }
                        if (!(entity instanceof LivingEntity) || entity instanceof ArmorStand || entity instanceof NPC || entity.isDead()) {
                            continue;
                        }
                        possible.add((LivingEntity) entity);
                    }
                    final LivingEntity setTarget = SUtil.getRandom(possible);
                    if (setTarget == null) {
                        return;
                    }
                    target.set(setTarget);
                }
                final Location location = projectile.getLocation().clone();
                final Vector vector = location.clone().toVector().subtract(target.get().getLocation().clone().add(0.0, 1.0, 0.0).toVector());
                location.setYaw((float) Math.atan2(vector.getX(), vector.getZ()));
                projectile.teleport(location);
                projectile.setVelocity(vector.clone().multiply(-1.0).multiply(0.2));
            }
        }.runTaskTimer(SkySimEngine.getPlugin(), 0L, 1L);
    }

    public static void giantsHitboxFix(final Projectile projectile) {
        final AtomicReference<LivingEntity> target = new AtomicReference<LivingEntity>(null);
        new BukkitRunnable() {
            public void run() {
                if (projectile.isDead()) {
                    this.cancel();
                    return;
                }
                if (target.get() == null) {
                    LivingEntity setTarget = null;
                    for (final Entity entity : projectile.getNearbyEntities(2.0, 12.0, 2.0)) {
                        if (entity.hasMetadata("Giant_")) {
                            setTarget = (LivingEntity) entity;
                        }
                    }
                    if (setTarget == null) {
                        return;
                    }
                    target.set(setTarget);
                }
                final Location location = projectile.getLocation().clone();
                final Vector vector = location.clone().toVector().subtract(target.get().getLocation().clone().add(0.0, 1.0, 0.0).toVector());
                location.setYaw((float) Math.atan2(vector.getX(), vector.getZ()));
                projectile.teleport(location);
                projectile.setVelocity(vector.clone().multiply(-1.0).multiply(0.5));
            }
        }.runTaskTimer(SkySimEngine.getPlugin(), 0L, 1L);
        new BukkitRunnable() {
            public void run() {
                projectile.remove();
            }
        }.runTaskLater(SkySimEngine.getPlugin(), 140L);
    }

    public static void markAimingArrow(final Projectile projectile, final in.godspunky.skyblock.enchantment.Enchantment aiming) {
        if (aiming == null) {
            return;
        }
        final AtomicReference<LivingEntity> target = new AtomicReference<LivingEntity>(null);
        new BukkitRunnable() {
            public void run() {
                if (projectile.isDead()) {
                    this.cancel();
                    return;
                }
                if (target.get() == null) {
                    final List<LivingEntity> possible = new ArrayList<LivingEntity>();
                    int aiminglvl = aiming.getLevel();
                    if (aiminglvl > 4) {
                        aiminglvl = 4;
                    }
                    for (final Entity entity : projectile.getNearbyEntities(2 * aiminglvl, 2 * aiminglvl, 2 * aiminglvl)) {
                        if (entity instanceof Player) {
                            continue;
                        }
                        if (!(entity instanceof LivingEntity) || entity instanceof ArmorStand || entity instanceof NPC || entity.isDead() || entity.hasMetadata("GiantSword")) {
                            continue;
                        }
                        possible.add((LivingEntity) entity);
                    }
                    final LivingEntity setTarget = SUtil.getRandom(possible);
                    if (setTarget == null) {
                        return;
                    }
                    target.set(setTarget);
                }
                final Location location = projectile.getLocation().clone();
                final Vector vector = location.clone().toVector().subtract(target.get().getLocation().clone().add(0.0, 1.0, 0.0).toVector());
                location.setYaw((float) Math.atan2(vector.getX(), vector.getZ()));
                projectile.teleport(location);
                projectile.setVelocity(vector.clone().multiply(-1.0).multiply(0.15));
            }
        }.runTaskTimer(SkySimEngine.getPlugin(), 0L, 1L);
        new BukkitRunnable() {
            public void run() {
                projectile.remove();
            }
        }.runTaskLater(SkySimEngine.getPlugin(), 80L);
    }

    public static ItemStack getStack(final String name, final Material material, final short data, final int amount, final List<String> lore) {
        final ItemStack stack = new ItemStack(material, data);
        stack.setDurability(data);
        final ItemMeta meta = stack.getItemMeta();
        if (name != null) {
            meta.setDisplayName(name);
        }
        stack.setAmount(amount);
        meta.setLore(lore);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ATTRIBUTES);
        stack.setItemMeta(meta);
        return stack;
    }

    public static ItemStack getStack(final String name, final Material material, final short data, final int amount, final String... lore) {
        return getStack(name, material, data, amount, Arrays.asList(lore));
    }

    public static ItemStack getSkullStack(final String name, final String skullName, final int amount, final String... lore) {
        final ItemStack stack = getStack(name, Material.SKULL_ITEM, (short) 3, amount, lore);
        final SkullMeta meta = (SkullMeta) stack.getItemMeta();
        meta.setOwner(skullName);
        stack.setItemMeta(meta);
        return stack;
    }

    public static ItemStack getSkullURLStack(final String name, final String url, final int amount, final String... lore) {
        return getSkull(url, getStack(name, Material.SKULL_ITEM, (short) 3, amount, lore), null);
    }

    public static ItemStack getSingleLoreStack(final String name, final Material material, final short data, final int amount, final String lore) {
        final List<String> l = new ArrayList<String>();
        for (final String line : splitByWordAndLength(lore, 30, "\\s")) {
            l.add(ChatColor.GRAY + line);
        }
        return getStack(name, material, data, amount, l.toArray(new String[0]));
    }

    public static boolean isEnchantable(final SItem sItem) {
        if (sItem.getType() == SMaterial.ENCHANTED_BOOK) {
            return true;
        }
        final GenericItemType type = sItem.getType().getStatistics().getType();
        return type == GenericItemType.WEAPON || type == GenericItemType.TOOL || type == GenericItemType.RANGED_WEAPON || type == GenericItemType.ARMOR || type == GenericItemType.WAND;
    }

    public static boolean isHotPotatoAble(final SItem item) {
        final GenericItemType type = item.getType().getStatistics().getType();
        return (type == GenericItemType.WEAPON || type == GenericItemType.RANGED_WEAPON || type == GenericItemType.ARMOR) && item.getDataInt("hpb") < 10;
    }

    public static boolean isRecomable(final SItem sitem) {
        final GenericItemType type = sitem.getType().getStatistics().getType();
        return type == GenericItemType.PET && !sitem.isRecombobulated();
    }

    public static boolean isAir(final ItemStack is) {
        return is == null || is.getType() == Material.AIR;
    }

    public static List<String> combineElements(final List<String> list, final String separator, final int perElement) {
        final List<String> n = new ArrayList<String>();
        for (int i = 0; i < list.size(); i += perElement) {
            final StringBuilder builder = new StringBuilder();
            for (int j = 0; j < perElement && i + j <= list.size() - 1; ++j) {
                builder.append((j != 0) ? separator : "").append(list.get(i + j));
            }
            n.add(builder.toString());
        }
        return n;
    }

    public static boolean pasteSchematic(final File schematicFile, final Location location, final boolean withAir) {
        try {
            final com.sk89q.worldedit.Vector pasteLocation = new com.sk89q.worldedit.Vector(location.getX(), location.getY(), location.getZ());
            final World pasteWorld = new BukkitWorld(location.getWorld());
            final WorldData pasteWorldData = pasteWorld.getWorldData();
            final Clipboard clipboard = ClipboardFormat.SCHEMATIC.getReader(new FileInputStream(schematicFile)).read(pasteWorldData);
            final ClipboardHolder clipboardHolder = new ClipboardHolder(clipboard, pasteWorldData);
            final EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(pasteWorld, -1);
            final Operation operation = clipboardHolder.createPaste(editSession, pasteWorldData).to(pasteLocation).ignoreAirBlocks(!withAir).build();
            Operations.complete(operation);
            return true;
        } catch (final IOException | WorldEditException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static void setBlocks(final Location c1, final Location c2, final Material material, final boolean applyPhysics) {
        if (!c1.getWorld().getName().equals(c1.getWorld().getName())) {
            return;
        }
        final int sy = Math.min(c1.getBlockY(), c2.getBlockY());
        final int ey = Math.max(c1.getBlockY(), c2.getBlockY());
        final int sx = Math.min(c1.getBlockX(), c2.getBlockX());
        final int ex = Math.max(c1.getBlockX(), c2.getBlockX());
        final int sz = Math.min(c1.getBlockZ(), c2.getBlockZ());
        final int ez = Math.max(c1.getBlockZ(), c2.getBlockZ());
        final org.bukkit.World world = c1.getWorld();
        for (int y = sy; y <= ey; ++y) {
            for (int x = sx; x <= ex; ++x) {
                for (int z = sz; z <= ez; ++z) {
                    world.getBlockAt(x, y, z).setType(material, applyPhysics);
                }
            }
        }
    }

    public static <T> T instance(final Class<T> clazz, final Object... params) {
        final Class<?>[] paramClasses = new Class[params.length];
        for (int i = 0; i < paramClasses.length; ++i) {
            paramClasses[i] = params[i].getClass();
        }
        try {
            final Constructor<T> constructor = clazz.getConstructor(paramClasses);
            constructor.setAccessible(true);
            return constructor.newInstance(params);
        } catch (final IllegalAccessException | InstantiationException | InvocationTargetException |
                       NoSuchMethodException ex) {
            return null;
        }
    }

    public static <C, T> T getDeclaredField(final C instance, final String name, final Class<T> type) {
        try {
            final Field f = instance.getClass().getDeclaredField(name);
            f.setAccessible(true);
            return type.cast(f.get(instance));
        } catch (final NoSuchFieldException | IllegalAccessException ex) {
            return null;
        }
    }

    public static Object getObjectFromCompound(final NBTTagCompound compound, final String key) {
        Object o = null;
        switch (compound.get(key).getTypeId()) {
            case 1:
                o = compound.getByte(key);
                break;
            case 2:
                o = compound.getShort(key);
                break;
            case 3:
                o = compound.getInt(key);
                break;
            case 4:
                o = compound.getLong(key);
                break;
            case 5:
                o = compound.getFloat(key);
                break;
            case 6:
                o = compound.getDouble(key);
                break;
            case 7:
                o = compound.getByteArray(key);
                break;
            case 10:
                o = compound.getCompound(key);
                break;
            case 11:
                o = compound.getIntArray(key);
                break;
            default:
                o = compound.getString(key);
                break;
        }
        return o;
    }

    public static NBTBase getBaseFromObject(final Object o) {
        if (o instanceof Byte) {
            return new NBTTagByte((byte) o);
        }
        if (o instanceof Short) {
            return new NBTTagShort((short) o);
        }
        if (o instanceof Integer) {
            return new NBTTagInt((int) o);
        }
        if (o instanceof Long) {
            return new NBTTagLong((long) o);
        }
        if (o instanceof Float) {
            return new NBTTagFloat((float) o);
        }
        if (o instanceof Double) {
            return new NBTTagDouble((double) o);
        }
        if (o instanceof String) {
            return new NBTTagString((String) o);
        }
        return null;
    }

    public static NBTBase getBaseFromObject(final ConfigurationSection cs, final String key) {
        return getBaseFromObject(cs.get(key));
    }

    public static ChatColor getRandomVisibleColor() {
        return SUtil.VISIBLE_COLOR_SPECTRUM.get(random(0, SUtil.VISIBLE_COLOR_SPECTRUM.size() - 1));
    }

    public static <T> T getRandom(final List<T> list) {
        if (list.size() == 0) {
            return null;
        }
        return list.get(random(0, list.size() - 1));
    }

    public static void broadcastExcept(final String message, final Player player) {
        for (final Player p : player.getWorld().getPlayers()) {
            if (p.getUniqueId().equals(player.getUniqueId())) {
                continue;
            }
            p.sendMessage(message);
        }
        SLog.info(message);
    }

    public static void broadcast(final String message, final Player player) {
        for (final Player p : player.getWorld().getPlayers()) {
            p.sendMessage(message);
        }
        SLog.info("[SYSTEM LOG] " + message);
    }

    public static void globalBroadcast(final String message) {
        for (final Player p : Bukkit.getOnlinePlayers()) {
            p.sendMessage(message);
        }
        SLog.info("[SYSTEM LOG] " + message);
    }

    public static void broadcastWorld(final String message, final org.bukkit.World w) {
        for (final Player p : w.getPlayers()) {
            p.sendMessage(message);
        }
    }

    public static ItemStack enchant(final ItemStack stack) {
        final ItemMeta meta = stack.getItemMeta();
        meta.addEnchant(org.bukkit.enchantments.Enchantment.DURABILITY, 1, true);
        stack.setItemMeta(meta);
        return stack;
    }

    public static SItem enchant(final SItem item, final in.godspunky.skyblock.enchantment.Enchantment... enchantments) {
        for (final Enchantment enchantment : enchantments) {
            item.addEnchantment(enchantment.getType(), enchantment.getLevel());
        }
        return item;
    }

    public static byte[] gzipCompress(final byte[] uncompressedData) {
        byte[] result = new byte[0];
        try (final ByteArrayOutputStream bos = new ByteArrayOutputStream(uncompressedData.length);
             final GZIPOutputStream gzipOS = new GZIPOutputStream(bos)) {
            gzipOS.write(uncompressedData);
            gzipOS.close();
            result = bos.toByteArray();
        } catch (final IOException ex) {
        }
        return result;
    }

    public static byte[] gzipUncompress(final byte[] compressedData) {
        byte[] result = new byte[0];
        try (final ByteArrayInputStream bis = new ByteArrayInputStream(compressedData);
             final ByteArrayOutputStream bos = new ByteArrayOutputStream();
             final GZIPInputStream gzipIS = new GZIPInputStream(bis)) {
            final byte[] buffer = new byte[1024];
            int len;
            while ((len = gzipIS.read(buffer)) != -1) {
                bos.write(buffer, 0, len);
            }
            result = bos.toByteArray();
        } catch (final IOException ex) {
        }
        return result;
    }

    public static double midpoint(final int x, final int y) {
        return (x + y) / 2.0;
    }

    public static double midpoint(final double x, final double y) {
        return (x + y) / 2.0;
    }

    public static void clearGoalSelector(final PathfinderGoalSelector goalSelector) {
        try {
            final Field b = PathfinderGoalSelector.class.getDeclaredField("b");
            final Field c = PathfinderGoalSelector.class.getDeclaredField("c");
            b.setAccessible(true);
            c.setAccessible(true);
            ((UnsafeList) b.get(goalSelector)).clear();
            ((UnsafeList) c.get(goalSelector)).clear();
        } catch (final IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static <T> T getOrDefault(final List<T> list, final int index, final T def) {
        if (index < 0 || index >= list.size()) {
            return def;
        }
        return list.get(index);
    }

    public static <T> T getOrDefault(final T[] array, final int index, final T def) {
        if (index < 0 || index >= array.length) {
            return def;
        }
        return array[index];
    }

    public static String zeroed(final long l) {
        return (l > 9L) ? ("" + l) : ("0" + l);
    }

    public static String getFormattedTime(final long t, final int div) {
        long seconds = t / div;
        final long hours = seconds / 3600L;
        seconds -= hours * 3600L;
        final long minutes = seconds / 60L;
        seconds -= minutes * 60L;
        return ((hours != 0L) ? (hours + ":") : "") + zeroed(minutes) + ":" + zeroed(seconds);
    }

    public static String getFormattedTimeToDay(final long l) {
        final long seconds = Math.round((float) (l / 20L));
        final int day = (int) TimeUnit.SECONDS.toDays(seconds);
        final int hours = (int) (TimeUnit.SECONDS.toHours(seconds) - TimeUnit.DAYS.toHours(day));
        final int minute = (int) (TimeUnit.SECONDS.toMinutes(seconds) - TimeUnit.DAYS.toMinutes(day) - TimeUnit.HOURS.toMinutes(hours));
        final int second = (int) (TimeUnit.SECONDS.toSeconds(seconds) - TimeUnit.DAYS.toSeconds(day) - TimeUnit.HOURS.toSeconds(hours) - TimeUnit.MINUTES.toSeconds(minute));
        return day + "d " + hours + "h " + minute + "m " + second + "s";
    }

    public static String getFormattedTime(final long ticks) {
        return getFormattedTime(ticks, 20);
    }

    public static String getSlayerFormattedTime(final long millis) {
        long seconds = millis / 1000L;
        final long hours = seconds / 3600L;
        seconds -= hours * 3600L;
        final long minutes = seconds / 60L;
        seconds -= minutes * 60L;
        return ((hours != 0L) ? (zeroed(hours) + "h") : "") + zeroed(minutes) + "m" + zeroed(seconds) + "s";
    }

    public static double quadrt(final double d) {
        return Math.pow(d, 0.25);
    }

    public static void delay(final Runnable runnable, final long delay) {
        new BukkitRunnable() {
            public void run() {
                runnable.run();
            }
        }.runTaskLater(SkySimEngine.getPlugin(), delay);
    }
    public static double square(double val) {
        return val * val;
    }
    public static SItem toShopItem(SMaterial smaterial , int amount , Long price , Long value){
        SItem item = SItem.of(smaterial);
        item.getStack().setAmount(amount);
        item.setPrice(price);
        item.setItemValue(value);
        MerchantItemHandler.ITEMS.put(smaterial , item);
        return item;
    }

    public static GameProfile createGameProfile(String url) {
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        byte[] ed = Base64.getEncoder().encode(String.format("{textures:{SKIN:{url:\"http://textures.minecraft.net/texture/%s\"}}}", url).getBytes());
        profile.getProperties().put("textures", new Property("textures", new String(ed)));
        return profile;
    }

    public static float getLookAtYaw(final Vector motion) {
        final double dx = motion.getX();
        final double dz = motion.getZ();
        double yaw = 0.0;
        if (dx != 0.0) {
            if (dx < 0.0) {
                yaw = 4.71238898038469;
            } else {
                yaw = 1.5707963267948966;
            }
            yaw -= Math.atan(dz / dx);
        } else if (dz < 0.0) {
            yaw = 3.141592653589793;
        }
        return (float) (-yaw * 180.0 / 3.141592653589793 - 90.0);
    }

    public static String ntify(final int i) {
        if (i == 11 || i == 12 || i == 13) {
            return i + "th";
        }
        final String s = String.valueOf(i);
        final char last = s.charAt(s.length() - 1);
        switch (last) {
            case '1':
                return i + "st";
            case '2':
                return i + "nd";
            case '3':
                return i + "rd";
            default:
                return i + "th";
        }
    }

    public static String pad(final String s, final int length) {
        return String.format("%-" + length + "s", s);
    }

    public static <T> List<T> shuffle(final List<T> list) {
        final Random rnd = ThreadLocalRandom.current();
        for (int i = list.size() - 1; i > 0; --i) {
            final int index = rnd.nextInt(i + 1);
            final T t = list.get(index);
            list.set(index, list.get(i));
            list.set(i, t);
        }
        return list;
    }

    public static <T> int deepLength(final T[][] array2d) {
        int c = 0;
        for (final T[] array : array2d) {
            c += array.length;
        }
        return c;
    }

    public static <T> T[] unnest(final T[][] array2d, final Class<T> clazz) {
        final T[] array = (T[]) Array.newInstance(clazz, SUtil.deepLength(array2d));
        int i = 0;
        int c = 0;
        while (i < array2d.length) {
            for (int j = 0; j < array2d[i].length; ++j, ++c) {
                array[c] = array2d[i][j];
            }
            ++i;
        }
        return array;
    }

    public static void runAsync(Runnable runnable) {
        new Thread(runnable).start();
    }
    public static void runSync(Runnable runnable){
        new BukkitRunnable(){

            @Override
            public void run() {
                runnable.run();
            }
        }.runTask(SkySimEngine.getPlugin());
    }

    public static ItemStack idToSkull(ItemStack head, String id) {
        JsonParser parser = new JsonParser();
        JsonObject o = parser.parse(new String(org.apache.commons.codec.binary.Base64.decodeBase64(id))).getAsJsonObject();
        String skinUrl = o.get("textures").getAsJsonObject().get("SKIN").getAsJsonObject().get("url").getAsString();
        SkullMeta headMeta = (SkullMeta) head.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        byte[] encodedData = org.apache.commons.codec.binary.Base64.encodeBase64(("{textures:{SKIN:{url:\"" + skinUrl + "\"}}}").getBytes());
        profile.getProperties().put("textures", new Property("textures", new String(encodedData)));
        Field profileField;
        try {
            profileField = headMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(headMeta, profile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        head.setItemMeta(headMeta);
        return head;
    }


    public static String createProgressText(final String text, final int current, final int max) {
        double percent;
        if (max != 0) {
            percent = current / (double) max * 100.0;
        } else {
            percent = 0.0;
        }
        percent = (double) Math.round(percent * 10.0 / 10.0);
        return ChatColor.GRAY + text + ": " + ((percent < 100.0) ? (ChatColor.YELLOW + commaify(percent) + ChatColor.GOLD + "%") : (ChatColor.GREEN + "100.0%"));
    }

    public static String createProgressText(final String text, final double current, final double max) {
        double percent;
        if (max != 0.0) {
            percent = current / max * 100.0;
        } else {
            percent = 0.0;
        }
        percent = (double) Math.round(percent * 10.0 / 10.0);
        return ChatColor.GRAY + text + ": " + ((percent < 100.0) ? (ChatColor.YELLOW + commaify(percent) + ChatColor.GOLD + "%") : (ChatColor.GREEN + "100.0%"));
    }

    public static String createLineProgressBar(final int length, final ChatColor progressColor, final int current, final int max) {
        final double percent = Math.min(current, (double) max) / max;
        final long completed = Math.round(length * percent);
        final StringBuilder builder = new StringBuilder().append(progressColor);
        for (int i = 0; i < completed; ++i) {
            builder.append("-");
        }
        builder.append(ChatColor.WHITE);
        for (int i = 0; i < length - completed; ++i) {
            builder.append("-");
        }
        builder.append(" ").append(ChatColor.YELLOW).append(commaify(current)).append(ChatColor.GOLD).append("/").append(ChatColor.YELLOW).append(commaify(max));
        return builder.toString();
    }

    public static String createSLineProgressBar(final int length, final ChatColor progressColor, final double current, final double max) {
        final double percent = Math.min(current, max) / max;
        final long completed = Math.round(length * percent);
        final StringBuilder builder = new StringBuilder().append(progressColor);
        for (int i = 0; i < completed; ++i) {
            builder.append("-");
        }
        builder.append(ChatColor.WHITE);
        for (int i = 0; i < length - completed; ++i) {
            builder.append("-");
        }
        builder.append(" ").append(ChatColor.YELLOW).append(commaify(current)).append(ChatColor.GOLD).append("/").append(ChatColor.YELLOW).append(Sputnik.formatFull((float) max));
        return builder.toString();
    }

    public static String createLineProgressBar(final int length, final ChatColor progressColor, final double current, final double max) {
        final double percent = Math.min(current, max) / max;
        final long completed = Math.round(length * percent);
        final StringBuilder builder = new StringBuilder().append(progressColor);
        for (int i = 0; i < completed; ++i) {
            builder.append("-");
        }
        builder.append(ChatColor.WHITE);
        for (int i = 0; i < length - completed; ++i) {
            builder.append("-");
        }
        builder.append(" ").append(ChatColor.YELLOW).append(commaify(current)).append(ChatColor.GOLD).append("/").append(ChatColor.YELLOW).append(commaify(max));
        return builder.toString();
    }

    public static <T> T[] toArray(final List<T> list, final Class<T> clazz) {
        final T[] array = (T[]) Array.newInstance(clazz, list.size());
        for (int i = 0; i < list.size(); ++i) {
            array[i] = list.get(i);
        }
        return array;
    }

    public static Rarity findPotionRarity(final int level) {
        switch (level) {
            case 0:
            case 1:
            case 2:
                return Rarity.COMMON;
            case 3:
            case 4:
                return Rarity.UNCOMMON;
            case 5:
            case 6:
                return Rarity.RARE;
            case 7:
            case 8:
                return Rarity.EPIC;
            case 9:
            case 10:
                return Rarity.LEGENDARY;
            case 11:
            case 12:
                return Rarity.MYTHIC;
            case 13:
            case 14:
                return Rarity.SUPREME;
            case 15:
            case 16:
                return Rarity.SPECIAL;
            default:
                return Rarity.VERY_SPECIAL;
        }
    }

    public static PotionColor getTopColor(final SItem item) {
        if (!item.isPotion()) {
            return null;
        }
        int topLevel = 0;
        PotionColor color = null;
        for (final PotionEffect effect : item.getPotionEffects()) {
            if (effect.getLevel() > topLevel) {
                topLevel = effect.getLevel();
                color = effect.getType().getColor();
            }
        }
        return color;
    }

    public static boolean canFitStack(final Inventory inventory, final ItemStack fit) {
        for (final ItemStack stack : inventory) {
            if (stack == null) {
                continue;
            }
            if (!fit.equals(stack)) {
                continue;
            }
            if (stack.getAmount() + fit.getAmount() > 64) {
                continue;
            }
            return true;
        }
        return false;
    }

    public static BukkitTask repeatingTask(final Runnable runnable, final long delay, final long interval) {
        return new BukkitRunnable() {
            public void run() {
                runnable.run();
            }
        }.runTaskTimer(SkySimEngine.getPlugin(), delay, interval);
    }

    public static int blackMagic(final double d) {
        return (int) d;
    }

    public static String prettify(final Object obj) {
        final Class<?> clazz = obj.getClass();
        if (clazz == Location.class) {
            final Location location = (Location) obj;
            return location.getX() + ", " + location.getY() + ", " + location.getZ() + ", " + location.getWorld().getName() + ", " + location.getYaw() + ", " + location.getPitch();
        }
        return "No pretty!";
    }

    public static String toNormalCase(String string) {
        string = string.replaceAll("_", " ");
        final String[] spl = string.split(" ");
        for (int i = 0; i < spl.length; ++i) {
            final String s = spl[i];
            if (s.length() != 0) {
                if (s.length() == 1) {
                    spl[i] = s.toUpperCase();
                } else {
                    spl[i] = s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
                }
            }
        }
        return StringUtils.join(spl, " ");
    }

    public static String getAuctionFormattedTime(final long millis) {
        if (millis == 0L) {
            return "Ended!";
        }
        if (millis >= 8.64E7) {
            return Math.round(millis / 8.64E7) + "d";
        }
        if (millis >= 2.16E7) {
            return Math.round(millis / 3600000.0) + "h";
        }
        long seconds = millis / 1000L;
        final long hours = seconds / 3600L;
        seconds -= hours * 3600L;
        final long minutes = seconds / 60L;
        seconds -= minutes * 60L;
        final StringBuilder builder = new StringBuilder();
        if (hours > 0L) {
            builder.append(hours).append("h ");
        }
        builder.append(minutes).append("m ").append(seconds).append("s");
        return builder.toString();
    }

    public static String getAuctionSetupFormattedTime(final long millis) {
        String dur;
        if (millis >= 8.64E7) {
            final long days = Math.round(millis / 8.64E7);
            dur = days + " Day";
            if (days != 1L) {
                dur += "s";
            }
        } else if (millis >= 3600000L) {
            final long hours = Math.round(millis / 3600000.0);
            dur = hours + " Hour";
            if (hours != 1L) {
                dur += "s";
            }
        } else {
            final long minutes = Math.round(millis / 60000.0);
            dur = minutes + " Minute";
            if (minutes != 1L) {
                dur += "s";
            }
        }
        return dur;
    }

    static {
        DATE_FORMAT = new SimpleDateFormat("MM/dd/yy");
        COMMA_FORMAT = NumberFormat.getInstance();
        CRIT_SPECTRUM = Arrays.asList(ChatColor.WHITE, ChatColor.WHITE, ChatColor.YELLOW, ChatColor.GOLD, ChatColor.RED, ChatColor.RED);
        VISIBLE_COLOR_SPECTRUM = Arrays.asList(ChatColor.DARK_GREEN, ChatColor.DARK_AQUA, ChatColor.DARK_RED, ChatColor.DARK_PURPLE, ChatColor.GOLD, ChatColor.GREEN, ChatColor.AQUA, ChatColor.RED, ChatColor.LIGHT_PURPLE, ChatColor.YELLOW, ChatColor.WHITE);
        SUtil.COMMA_FORMAT.setGroupingUsed(true);
    }
}
