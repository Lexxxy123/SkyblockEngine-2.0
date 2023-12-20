package in.godspunky.skyblock.entity.end;

import in.godspunky.skyblock.entity.EntityDrop;
import in.godspunky.skyblock.entity.EntityDropType;
import in.godspunky.skyblock.item.SMaterial;
import in.godspunky.skyblock.util.SUtil;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class VoidlingExtremist extends BaseEnderman {
    @Override
    public String getEntityName() {
        return ChatColor.LIGHT_PURPLE + "Voidling Extremist";
    }

    @Override
    public double getEntityMaxHealth() {
        return 8000000.0;
    }

    @Override
    public double getDamageDealt() {
        return 13500.0;
    }

    @Override
    public double getXPDropped() {
        return 500.0;
    }

    @Override
    public List<EntityDrop> drops() {
        return Arrays.asList(new EntityDrop(new ItemStack(Material.ENDER_PEARL, SUtil.random(32, 64)), EntityDropType.GUARANTEED, 1.0), new EntityDrop(SMaterial.ENCHANTED_ENDER_PEARL, EntityDropType.RARE, 0.05), new EntityDrop(SMaterial.SUMMONING_EYE, EntityDropType.EXTRAORDINARILY_RARE, 0.01));
    }

    @Override
    public int mobLevel() {
        return 100;
    }
}
