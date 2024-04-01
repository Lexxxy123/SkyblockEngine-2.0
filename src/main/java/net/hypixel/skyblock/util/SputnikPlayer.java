package net.hypixel.skyblock.util;

import de.tr7zw.nbtapi.NBTItem;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTTagInt;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SputnikPlayer {
    public static final Map<UUID, Integer> AbsHP;

    public static void sendTranslated(final Player p, final String content) {
        p.sendMessage(ChatColor.translateAlternateColorCodes('&', content));
    }

    public static void BonemerangFix(final Player player) {
        for (int i = 0; i < player.getInventory().getSize(); ++i) {
            final ItemStack stack = player.getInventory().getItem(i);
            if (stack != null) {
                final NBTItem nbti = new NBTItem(stack);
                if (nbti.hasKey("ejectedBonemerang") && nbti.getInteger("ejectedBonemerang") == 1) {
                    final net.minecraft.server.v1_8_R3.ItemStack tagStack = CraftItemStack.asNMSCopy(stack);
                    final NBTTagCompound tagCompound = tagStack.hasTag() ? tagStack.getTag() : new NBTTagCompound();
                    tagCompound.set("ejectedBonemerang", new NBTTagInt(0));
                    tagStack.setTag(tagCompound);
                    final ItemStack itemStack = CraftItemStack.asBukkitCopy(tagStack);
                    if (tagStack.getTag().getInt("ejectedBonemerang") == 0) {
                        itemStack.setType(Material.BONE);
                        player.getInventory().setItem(i, itemStack);
                    }
                }
            }
        }
    }

    public static void KatanasFix(final Player player) {
        for (int i = 0; i < player.getInventory().getSize(); ++i) {
            final ItemStack stack = player.getInventory().getItem(i);
            if (stack != null) {
                final NBTItem nbti = new NBTItem(stack);
                if (nbti.hasKey("isGoldSword") && nbti.getInteger("isGoldSword") == 1) {
                    final net.minecraft.server.v1_8_R3.ItemStack tagStack = CraftItemStack.asNMSCopy(stack);
                    final NBTTagCompound tagCompound = tagStack.hasTag() ? tagStack.getTag() : new NBTTagCompound();
                    tagCompound.set("isGoldSword", new NBTTagInt(0));
                    tagStack.setTag(tagCompound);
                    final ItemStack itemStack = CraftItemStack.asBukkitCopy(tagStack);
                    if (tagStack.getTag().getInt("isGoldSword") == 0) {
                        itemStack.setType(Material.DIAMOND_SWORD);
                        player.getInventory().setItem(i, itemStack);
                    }
                }
            }
        }
    }

    public static void setCustomAbsorptionHP(final Player p, final float amount) {
        if (!SputnikPlayer.AbsHP.containsKey(p.getUniqueId())) {
            SputnikPlayer.AbsHP.put(p.getUniqueId(), 0);
        }
        SputnikPlayer.AbsHP.put(p.getUniqueId(), Math.round(amount));
    }

    public static void minusCustomAbsorptionHP(final Player p, final float amount) {
        if (!SputnikPlayer.AbsHP.containsKey(p.getUniqueId())) {
            SputnikPlayer.AbsHP.put(p.getUniqueId(), 0);
        }
        if (SputnikPlayer.AbsHP.get(p.getUniqueId()) == 0) {
            return;
        }
        SputnikPlayer.AbsHP.put(p.getUniqueId(), SputnikPlayer.AbsHP.get(p.getUniqueId()) - Math.round(amount));
    }

    public static Integer getCustomAbsorptionHP(final Player p) {
        if (!SputnikPlayer.AbsHP.containsKey(p.getUniqueId())) {
            SputnikPlayer.AbsHP.put(p.getUniqueId(), 0);
        }
        return SputnikPlayer.AbsHP.get(p.getUniqueId());
    }

    public static void updateScaledAHP(final Player p) {
        final EntityHuman human = ((CraftHumanEntity) p).getHandle();
        if (!SputnikPlayer.AbsHP.containsKey(p.getUniqueId())) {
            SputnikPlayer.AbsHP.put(p.getUniqueId(), 0);
        }
        if (SputnikPlayer.AbsHP.get(p.getUniqueId()) == 0) {
            human.setAbsorptionHearts(0.0f);
        }
        final Integer absHP = SputnikPlayer.AbsHP.get(p.getUniqueId());
        human.setAbsorptionHearts((float) Math.min(20.0, (int) Math.round(0.05 * absHP)));
        if (SputnikPlayer.AbsHP.get(p.getUniqueId()) == 0) {
            human.setAbsorptionHearts(0.0f);
        }
    }

    static {
        AbsHP = new HashMap<>();
    }
}
