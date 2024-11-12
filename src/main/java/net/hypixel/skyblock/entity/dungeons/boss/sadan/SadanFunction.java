/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Color
 *  org.bukkit.Effect
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.World
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.entity.EntityDamageEvent$DamageCause
 *  org.bukkit.inventory.EntityEquipment
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 */
package net.hypixel.skyblock.entity.dungeons.boss.sadan;

import java.util.Random;
import net.hypixel.skyblock.Repeater;
import net.hypixel.skyblock.SkyBlock;
import net.hypixel.skyblock.entity.SEntity;
import net.hypixel.skyblock.entity.SEntityType;
import net.hypixel.skyblock.entity.dungeons.boss.sadan.AnimationSequence;
import net.hypixel.skyblock.entity.dungeons.boss.sadan.SadanBossManager;
import net.hypixel.skyblock.user.PlayerStatistics;
import net.hypixel.skyblock.user.PlayerUtils;
import net.hypixel.skyblock.user.User;
import net.hypixel.skyblock.util.SUtil;
import net.hypixel.skyblock.util.Sputnik;
import org.bukkit.Color;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

public class SadanFunction {
    public static String generateRandom() {
        int leftLimit = 97;
        int rightLimit = 122;
        int targetStringLength = SUtil.random(5, 6);
        Random random = new Random();
        String generatedString = random.ints(97, 123).limit(targetStringLength).collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();
        return generatedString;
    }

    public static void jlp(World w2) {
        SEntity se_jollypink = new SEntity(new Location(w2, 183.5, 84.0, 253.5, -45.0f, 0.0f), SEntityType.GIANT_DUMMY, new Object[0]);
        LivingEntity e2 = se_jollypink.getEntity();
        EntityEquipment eq = e2.getEquipment();
        eq.setItemInHand(null);
        eq.setHelmet(SadanFunction.b(14751108, Material.LEATHER_HELMET));
        eq.setChestplate(SadanFunction.b(14751108, Material.LEATHER_CHESTPLATE));
        eq.setLeggings(SadanFunction.b(14751108, Material.LEATHER_LEGGINGS));
        eq.setBoots(SadanFunction.b(14751108, Material.LEATHER_BOOTS));
        e2.setMetadata("JollyPink", (MetadataValue)new FixedMetadataValue((Plugin)SkyBlock.getPlugin(), (Object)true));
    }

    public static void lasrg(World w2) {
        SEntity se_jollypink = new SEntity(new Location(w2, 199.5, 84.0, 253.5, 45.0f, 0.0f), SEntityType.GIANT_DUMMY, new Object[0]);
        LivingEntity e2 = se_jollypink.getEntity();
        EntityEquipment eq = e2.getEquipment();
        eq.setItemInHand(null);
        eq.setHelmet(SadanFunction.b(12228503, Material.LEATHER_HELMET));
        eq.setChestplate(SadanFunction.b(12228503, Material.LEATHER_CHESTPLATE));
        eq.setLeggings(SadanFunction.b(12228503, Material.LEATHER_LEGGINGS));
        eq.setBoots(SadanFunction.b(12228503, Material.LEATHER_BOOTS));
        e2.setMetadata("LASR", (MetadataValue)new FixedMetadataValue((Plugin)SkyBlock.getPlugin(), (Object)true));
    }

    public static void diag(World w2) {
        SEntity se_jollypink = new SEntity(new Location(w2, 183.5, 84.0, 279.5, -135.0f, 0.0f), SEntityType.GIANT_DUMMY, new Object[0]);
        LivingEntity e2 = se_jollypink.getEntity();
        EntityEquipment eq = e2.getEquipment();
        eq.setItemInHand(SUtil.enchant(SadanFunction.c(Material.DIAMOND_SWORD)));
        eq.setHelmet(SadanFunction.c(Material.DIAMOND_HELMET));
        eq.setChestplate(SadanFunction.c(Material.DIAMOND_CHESTPLATE));
        eq.setLeggings(SadanFunction.c(Material.DIAMOND_LEGGINGS));
        eq.setBoots(SadanFunction.c(Material.DIAMOND_BOOTS));
        e2.setMetadata("Diamond", (MetadataValue)new FixedMetadataValue((Plugin)SkyBlock.getPlugin(), (Object)true));
    }

    public static void bfg(World w2) {
        SEntity se_jollypink = new SEntity(new Location(w2, 199.5, 84.0, 279.5, 135.0f, 0.0f), SEntityType.GIANT_DUMMY, new Object[0]);
        LivingEntity e2 = se_jollypink.getEntity();
        EntityEquipment eq = e2.getEquipment();
        eq.setItemInHand(null);
        eq.setHelmet(null);
        eq.setChestplate(null);
        eq.setLeggings(null);
        eq.setBoots(SadanFunction.b(8991025, Material.LEATHER_BOOTS));
        e2.setMetadata("Bigfoot", (MetadataValue)new FixedMetadataValue((Plugin)SkyBlock.getPlugin(), (Object)true));
    }

    public static ItemStack buildColorStack(int hexcolor) {
        ItemStack stack = SUtil.applyColorToLeatherArmor(new ItemStack(Material.LEATHER_HELMET), Color.fromRGB((int)hexcolor));
        ItemMeta itemMeta = stack.getItemMeta();
        itemMeta.spigot().setUnbreakable(true);
        stack.setItemMeta(itemMeta);
        return stack;
    }

    public static ItemStack b(int hexcolor, Material m2) {
        ItemStack stack = SUtil.applyColorToLeatherArmor(new ItemStack(m2), Color.fromRGB((int)hexcolor));
        ItemMeta itemMeta = stack.getItemMeta();
        itemMeta.spigot().setUnbreakable(true);
        stack.setItemMeta(itemMeta);
        return stack;
    }

    public static ItemStack c(Material m2) {
        ItemStack stack = new ItemStack(m2);
        ItemMeta itemMeta = stack.getItemMeta();
        itemMeta.spigot().setUnbreakable(true);
        stack.setItemMeta(itemMeta);
        return stack;
    }

    public static void endPhase1(World w2) {
        for (Entity e2 : w2.getEntities()) {
            if (e2.hasMetadata("ftd")) {
                e2.remove();
            }
            if (!e2.hasMetadata("t_sadan")) continue;
            e2.remove();
            e2.getWorld().playEffect(e2.getLocation(), Effect.EXPLOSION, 10);
            e2.getWorld().playEffect(e2.getLocation(), Effect.EXPLOSION, 10);
            e2.getWorld().playEffect(e2.getLocation(), Effect.EXPLOSION, 10);
            e2.getWorld().playEffect(e2.getLocation(), Effect.EXPLOSION, 10);
            e2.getWorld().playEffect(e2.getLocation(), Effect.EXPLOSION, 10);
            e2.getWorld().playEffect(e2.getLocation(), Effect.EXPLOSION, 10);
            e2.getWorld().playEffect(e2.getLocation(), Effect.EXPLOSION, 10);
            e2.getWorld().playEffect(e2.getLocation(), Effect.EXPLOSION, 10);
            e2.getWorld().playEffect(e2.getLocation(), Effect.EXPLOSION, 10);
            e2.getWorld().playEffect(e2.getLocation(), Effect.EXPLOSION, 10);
            e2.getWorld().playEffect(e2.getLocation(), Effect.EXPLOSION, 10);
        }
    }

    public static void release(World w2) {
        SUtil.delay(() -> SadanFunction.rjp(w2), 10L);
        SUtil.delay(() -> SadanFunction.rdia(w2), 30L);
        SUtil.delay(() -> SadanFunction.rbf(w2), 50L);
        SUtil.delay(() -> SadanFunction.rlasr(w2), 70L);
    }

    public static void rjp(World w2) {
        for (Entity e2 : w2.getEntities()) {
            if (!e2.hasMetadata("JollyPink")) continue;
            e2.remove();
            new SEntity(new Location(e2.getWorld(), e2.getLocation().getX(), e2.getLocation().getY(), e2.getLocation().getZ(), e2.getLocation().getYaw(), 0.0f), SEntityType.JOLLY_PINK_SADAN, new Object[0]);
            w2.strikeLightningEffect(e2.getLocation());
            break;
        }
        AnimationSequence.edit(new Location(w2, 187.0, 81.0, 257.0), new Location(w2, 178.0, 101.0, 248.0), w2);
    }

    public static void rdia(World w2) {
        for (Entity e2 : w2.getEntities()) {
            if (!e2.hasMetadata("Diamond")) continue;
            e2.remove();
            new SEntity(new Location(e2.getWorld(), e2.getLocation().getX(), e2.getLocation().getY(), e2.getLocation().getZ(), e2.getLocation().getYaw(), 0.0f), SEntityType.DIAMOND_SADAN, new Object[0]);
            w2.strikeLightningEffect(e2.getLocation());
            break;
        }
        AnimationSequence.edit(new Location(w2, 187.0, 82.0, 275.0), new Location(w2, 178.0, 101.0, 284.0), w2);
    }

    public static void rbf(World w2) {
        for (Entity e2 : w2.getEntities()) {
            if (!e2.hasMetadata("Bigfoot")) continue;
            e2.remove();
            new SEntity(new Location(e2.getWorld(), e2.getLocation().getX(), e2.getLocation().getY(), e2.getLocation().getZ(), e2.getLocation().getYaw(), 0.0f), SEntityType.BIGFOOT_SADAN, new Object[0]);
            w2.strikeLightningEffect(e2.getLocation());
            break;
        }
        AnimationSequence.edit(new Location(w2, 194.0, 82.0, 283.0), new Location(w2, 203.0, 101.0, 275.0), w2);
    }

    public static void rlasr(World w2) {
        for (Entity e2 : w2.getEntities()) {
            if (!e2.hasMetadata("LASR")) continue;
            e2.remove();
            new SEntity(new Location(e2.getWorld(), e2.getLocation().getX(), e2.getLocation().getY(), e2.getLocation().getZ(), e2.getLocation().getYaw(), 0.0f), SEntityType.LASR_SADAN, new Object[0]);
            w2.strikeLightningEffect(e2.getLocation());
            break;
        }
        AnimationSequence.edit(new Location(w2, 203.0, 101.0, 248.0), new Location(w2, 195.0, 81.0, 257.0), w2);
    }

    public static void a(World w2, double x2, double z2, float yaw) {
        new SEntity(new Location(w2, x2 += 0.5, 69.0, z2 += 0.5, yaw, 0.0f), SEntityType.TERRACOTTA_DUMMY, new Object[0]);
    }

    public static void aA(World w2, double x2, double z2, float yaw) {
        new SEntity(new Location(w2, x2 += 0.5, 69.0, z2 += 0.5, yaw, 0.0f), SEntityType.SLEEPING_GOLEM, new Object[0]);
    }

    public static void b(World w2) {
        new SEntity(new Location(w2, 191.5, 54.0, 266.5, 180.0f, 0.0f), SEntityType.DUMMY_SADAN_1, new Object[0]);
    }

    public static void s_(Entity e2) {
        World w2 = e2.getWorld();
        SadanFunction.lasrg(w2);
        SadanFunction.diag(w2);
        SadanFunction.bfg(w2);
        SadanFunction.jlp(w2);
        SadanFunction.aA(w2, 184.0, 252.0, -45.0f);
        SadanFunction.aA(w2, 198.0, 252.0, 45.0f);
        SadanFunction.aA(w2, 204.0, 266.0, 90.0f);
        SadanFunction.aA(w2, 184.0, 280.0, -135.0f);
        SadanFunction.aA(w2, 178.0, 266.0, -90.0f);
        SadanFunction.aA(w2, 198.0, 280.0, 135.0f);
        SadanFunction.a(w2, 194.0, 295.0, 90.0f);
        SadanFunction.a(w2, 188.0, 295.0, -90.0f);
        SadanFunction.a(w2, 194.0, 290.0, 90.0f);
        SadanFunction.a(w2, 188.0, 290.0, -90.0f);
        SadanFunction.a(w2, 194.0, 285.0, 90.0f);
        SadanFunction.a(w2, 188.0, 285.0, -90.0f);
        SadanFunction.a(w2, 194.0, 280.0, 90.0f);
        SadanFunction.a(w2, 188.0, 280.0, -90.0f);
        SadanFunction.a(w2, 194.0, 275.0, 90.0f);
        SadanFunction.a(w2, 188.0, 275.0, -90.0f);
        SadanFunction.b(w2);
        SadanFunction.a(w2, 194.0, 257.0, 90.0f);
        SadanFunction.a(w2, 188.0, 257.0, -90.0f);
        SadanFunction.a(w2, 194.0, 252.0, 90.0f);
        SadanFunction.a(w2, 188.0, 252.0, -90.0f);
        SadanFunction.a(w2, 194.0, 248.0, 90.0f);
        SadanFunction.a(w2, 188.0, 248.0, -90.0f);
        SadanFunction.a(w2, 194.0, 243.0, 90.0f);
        SadanFunction.a(w2, 188.0, 243.0, -90.0f);
        SadanFunction.a(w2, 194.0, 238.0, 90.0f);
        SadanFunction.a(w2, 188.0, 238.0, -90.0f);
    }

    public static Integer dmgc(int damage, Player p2, Entity e2) {
        PlayerStatistics statistics = PlayerUtils.STATISTICS_CACHE.get(p2.getUniqueId());
        if (statistics == null) {
            return 0;
        }
        double defense = statistics.getDefense().addAll();
        int dmglater = (int)Math.round((double)damage - (double)damage * (defense / (defense + 100.0)));
        User.getUser(p2.getUniqueId()).damage(dmglater, EntityDamageEvent.DamageCause.ENTITY_ATTACK, e2);
        p2.damage(1.0E-6, null);
        return dmglater;
    }

    public static void roomLoop(World w2) {
    }

    public static void endRoom1(World w2) {
        if (w2.getName().contains("f6")) {
            SUtil.broadcastWorld(Sputnik.trans("&c&lSKYSIM MC >> &e&lThis demo floor currently in development so you can't respawn or get reward, sorry! We will update later on, thanks for playing, leave rating on #server-rating"), w2);
            SUtil.delay(() -> SUtil.broadcastWorld(Sputnik.trans("&c[Warning] &eThis dungeon will close in &c30s"), w2), 600L);
            SUtil.delay(() -> SUtil.broadcastWorld(Sputnik.trans("&c[Warning] &eThis dungeon will close in &c20s"), w2), 1000L);
            SUtil.delay(() -> SUtil.broadcastWorld(Sputnik.trans("&c[Warning] &eThis dungeon will close in &c10s"), w2), 1200L);
            SUtil.delay(() -> SUtil.broadcastWorld(Sputnik.trans("&c[Warning] &eThis dungeon will close in &c5s"), w2), 1300L);
            SUtil.delay(() -> SUtil.broadcastWorld(Sputnik.trans("&c[Warning] &eThis dungeon will close in &c4s"), w2), 1320L);
            SUtil.delay(() -> SUtil.broadcastWorld(Sputnik.trans("&c[Warning] &eThis dungeon will close in &c3s"), w2), 1340L);
            SUtil.delay(() -> SUtil.broadcastWorld(Sputnik.trans("&c[Warning] &eThis dungeon will close in &c2s"), w2), 1360L);
            SUtil.delay(() -> SUtil.broadcastWorld(Sputnik.trans("&c[Warning] &eThis dungeon will close in &c1s"), w2), 1380L);
            SUtil.delay(() -> SadanBossManager.endFloor(w2), 1400L);
        }
    }

    public static void sendReMsg(boolean finishornot, World w2) {
        if (w2.getName().contains("f6") && Repeater.FloorLivingSec.containsKey(w2.getUID())) {
            if (finishornot) {
                int bitsReward = Math.round((100000 - Math.min(100000, Repeater.FloorLivingSec.get(w2.getUID()))) * 150 / 255);
                String rew = "&6+" + SUtil.commaify(bitsReward) + " Coins &7(Completion Reward)";
                if (bitsReward <= 0) {
                    rew = "&cYou have no rewards!";
                } else {
                    w2.getPlayers().forEach(p2 -> User.getUser(p2.getUniqueId()).addCoins(bitsReward));
                }
                SUtil.broadcastWorld(Sputnik.trans("&a\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac"), w2);
                SUtil.broadcastWorld(Sputnik.trans("        &cThe Catacombs Demo &8- &eFloor VI"), w2);
                SUtil.broadcastWorld(Sputnik.trans("&c"), w2);
                SUtil.broadcastWorld(Sputnik.trans("        &c\u2620&e Defeated &cSadan &ein &a" + Sputnik.formatTime(Repeater.FloorLivingSec.get(w2.getUID()))), w2);
                SUtil.broadcastWorld(Sputnik.trans("&c"), w2);
                SUtil.broadcastWorld(Sputnik.trans("            " + rew), w2);
                SUtil.broadcastWorld(Sputnik.trans("&c"), w2);
                SUtil.broadcastWorld(Sputnik.trans("&a\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac"), w2);
            } else {
                SUtil.broadcastWorld(Sputnik.trans("&a\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac"), w2);
                SUtil.broadcastWorld(Sputnik.trans("        &cThe Catacombs Demo &8- &eFloor VI"), w2);
                SUtil.broadcastWorld(Sputnik.trans("&c"), w2);
                SUtil.broadcastWorld(Sputnik.trans("        &c\u2620&e You died, but you can try again!"), w2);
                SUtil.broadcastWorld(Sputnik.trans("&c"), w2);
                SUtil.broadcastWorld(Sputnik.trans("           &cYou have no rewards cause you died."), w2);
                SUtil.broadcastWorld(Sputnik.trans("&c"), w2);
                SUtil.broadcastWorld(Sputnik.trans("&a\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac"), w2);
            }
        }
    }

    public static void endRoom2(World w2) {
        if (w2.getName().contains("f6")) {
            SUtil.delay(() -> SUtil.broadcastWorld(Sputnik.trans("&c[Warning] &eThis dungeon will close in &c5s"), w2), 200L);
            SUtil.delay(() -> SUtil.broadcastWorld(Sputnik.trans("&c[Warning] &eThis dungeon will close in &c4s"), w2), 220L);
            SUtil.delay(() -> SUtil.broadcastWorld(Sputnik.trans("&c[Warning] &eThis dungeon will close in &c3s"), w2), 240L);
            SUtil.delay(() -> SUtil.broadcastWorld(Sputnik.trans("&c[Warning] &eThis dungeon will close in &c2s"), w2), 260L);
            SUtil.delay(() -> SUtil.broadcastWorld(Sputnik.trans("&c[Warning] &eThis dungeon will close in &c1s"), w2), 280L);
            SUtil.delay(() -> SUtil.broadcastWorld(Sputnik.trans("&c[Warning] &eWarping you back to the Hub"), w2), 300L);
            SUtil.delay(() -> SadanBossManager.endFloor(w2), 300L);
        }
    }
}

