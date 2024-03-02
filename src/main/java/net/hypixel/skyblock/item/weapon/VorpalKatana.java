package net.hypixel.skyblock.item.weapon;

import net.hypixel.skyblock.SkyBlock;
import net.hypixel.skyblock.item.*;
import net.hypixel.skyblock.util.FerocityCalculation;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTTagInt;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import net.hypixel.skyblock.util.Sputnik;

public class VorpalKatana implements ToolStatistics, MaterialFunction, Ability {
    @Override
    public int getBaseDamage() {
        return 155;
    }

    @Override
    public double getBaseStrength() {
        return 80.0;
    }

    @Override
    public double getBaseCritDamage() {
        return 0.25;
    }

    @Override
    public double getBaseIntelligence() {
        return 200.0;
    }

    @Override
    public String getDisplayName() {
        return "Vorpal Katana";
    }

    @Override
    public String getLore() {
        return "Deal " + ChatColor.GREEN + "+200% " + ChatColor.GRAY + "damage to Endermen.";
    }

    @Override
    public Rarity getRarity() {
        return Rarity.EPIC;
    }

    @Override
    public GenericItemType getType() {
        return GenericItemType.WEAPON;
    }

    @Override
    public SpecificItemType getSpecificType() {
        return SpecificItemType.SWORD;
    }

    @Override
    public void onAbilityUse(Player player, SItem sItem) {
        ItemStack item = player.getInventory().getItemInHand();
        Integer itemslot = player.getInventory().getHeldItemSlot();
        this.start(player, item, itemslot);
        Sputnik.playActivateSound(player);
        FerocityCalculation.ONE_TYPE_FEROCITY_BONUS_ENDERMAN.put(player.getUniqueId(), 300);
        new BukkitRunnable() {
            public void run() {
                FerocityCalculation.ONE_TYPE_FEROCITY_BONUS_ENDERMAN.put(player.getUniqueId(), 0);
                VorpalKatana.this.done(player, item, itemslot);
                Sputnik.playDeActivateSound(player);
            }
        }.runTaskLater(SkyBlock.getPlugin(), 75L);
    }

    @Override
    public String getAbilityName() {
        return "Soulcry";
    }

    @Override
    public String getAbilityDescription() {
        return "Gain " + ChatColor.RED + "+300⫽ Ferocity " + ChatColor.GRAY + "against Endermen for " + ChatColor.GREEN + "4s.";
    }

    @Override
    public int getAbilityCooldownTicks() {
        return 80;
    }

    @Override
    public AbilityActivation getAbilityActivation() {
        return AbilityActivation.RIGHT_CLICK;
    }

    @Override
    public int getManaCost() {
        return 200;
    }

    public void start(Player player, ItemStack stack, Integer slot) {
        net.minecraft.server.v1_8_R3.ItemStack tagStack = CraftItemStack.asNMSCopy(stack);
        NBTTagCompound tagCompound = tagStack.hasTag() ? tagStack.getTag() : new NBTTagCompound();
        tagCompound.set("isGoldSword", new NBTTagInt(1));
        tagStack.setTag(tagCompound);
        ItemStack itemStack = CraftItemStack.asBukkitCopy(tagStack);
        if (1 == tagStack.getTag().getInt("isGoldSword")) {
            itemStack.setType(Material.GOLD_SWORD);
            player.getInventory().setItem(slot, itemStack);
        }
    }

    public void done(Player player, ItemStack stack, Integer slot) {
        net.minecraft.server.v1_8_R3.ItemStack tagStack = CraftItemStack.asNMSCopy(stack);
        NBTTagCompound tagCompound = tagStack.hasTag() ? tagStack.getTag() : new NBTTagCompound();
        tagCompound.set("isGoldSword", new NBTTagInt(0));
        tagStack.setTag(tagCompound);
        SItem sitem = SItem.find(player.getInventory().getItem(slot));
        if (null != sitem && SMaterial.VORPAL_KATANA == sitem.getType()) {
            ItemStack itemStack = CraftItemStack.asBukkitCopy(tagStack);
            if (0 == tagStack.getTag().getInt("isGoldSword")) {
                itemStack.setType(Material.DIAMOND_SWORD);
                player.getInventory().setItem(slot, itemStack);
            }
        }
    }
}
