package in.godspunky.skyblock.item.weapon;

import in.godspunky.skyblock.SkyBlock;
import in.godspunky.skyblock.item.*;
import in.godspunky.skyblock.util.FerocityCalculation;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTTagInt;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import in.godspunky.skyblock.util.Sputnik;

public class VoidedgeKatana implements ToolStatistics, MaterialFunction, Ability {
    @Override
    public int getBaseDamage() {
        return 125;
    }

    @Override
    public double getBaseStrength() {
        return 60.0;
    }

    @Override
    public double getBaseCritDamage() {
        return 0.2;
    }

    @Override
    public double getBaseIntelligence() {
        return 50.0;
    }

    @Override
    public String getDisplayName() {
        return "Voidedge Katana";
    }

    @Override
    public String getLore() {
        return "Deal " + ChatColor.GREEN + "+150% " + ChatColor.GRAY + "damage to Endermen.";
    }

    @Override
    public Rarity getRarity() {
        return Rarity.RARE;
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
    public void onAbilityUse(final Player player, final SItem sItem) {
        final ItemStack item = player.getInventory().getItemInHand();
        final Integer itemslot = player.getInventory().getHeldItemSlot();
        this.start(player, item, itemslot);
        Sputnik.playActivateSound(player);
        FerocityCalculation.ONE_TYPE_FEROCITY_BONUS_ENDERMAN.put(player.getUniqueId(), 200);
        new BukkitRunnable() {
            public void run() {
                FerocityCalculation.ONE_TYPE_FEROCITY_BONUS_ENDERMAN.put(player.getUniqueId(), 0);
                VoidedgeKatana.this.done(player, item, itemslot);
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
        return "Gain " + ChatColor.RED + "+200⫽ Ferocity " + ChatColor.GRAY + "against Endermen for " + ChatColor.GREEN + "4s.";
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
        return 100;
    }

    public void start(final Player player, final ItemStack stack, final Integer slot) {
        final net.minecraft.server.v1_8_R3.ItemStack tagStack = CraftItemStack.asNMSCopy(stack);
        final NBTTagCompound tagCompound = tagStack.hasTag() ? tagStack.getTag() : new NBTTagCompound();
        tagCompound.set("isGoldSword", new NBTTagInt(1));
        tagStack.setTag(tagCompound);
        final ItemStack itemStack = CraftItemStack.asBukkitCopy(tagStack);
        if (tagStack.getTag().getInt("isGoldSword") == 1) {
            itemStack.setType(Material.GOLD_SWORD);
            player.getInventory().setItem(slot, itemStack);
        }
    }

    public void done(final Player player, final ItemStack stack, final Integer slot) {
        final net.minecraft.server.v1_8_R3.ItemStack tagStack = CraftItemStack.asNMSCopy(stack);
        final NBTTagCompound tagCompound = tagStack.hasTag() ? tagStack.getTag() : new NBTTagCompound();
        tagCompound.set("isGoldSword", new NBTTagInt(0));
        tagStack.setTag(tagCompound);
        final SItem sitem = SItem.find(player.getInventory().getItem(slot));
        if (sitem != null && sitem.getType() == SMaterial.VOIDEDGE_KATANA) {
            final ItemStack itemStack = CraftItemStack.asBukkitCopy(tagStack);
            if (tagStack.getTag().getInt("isGoldSword") == 0) {
                itemStack.setType(Material.DIAMOND_SWORD);
                player.getInventory().setItem(slot, itemStack);
            }
        }
    }
}
