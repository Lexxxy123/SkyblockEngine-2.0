package vn.giakhanhvn.skysim.entity.zombie;

import java.util.Collections;
import vn.giakhanhvn.skysim.entity.EntityDropType;
import vn.giakhanhvn.skysim.item.SItem;
import vn.giakhanhvn.skysim.item.SMaterial;
import vn.giakhanhvn.skysim.entity.EntityDrop;
import java.util.List;
import org.bukkit.Color;
import vn.giakhanhvn.skysim.util.SUtil;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import vn.giakhanhvn.skysim.entity.SEntityEquipment;
import net.md_5.bungee.api.ChatColor;

public class AtonedRevenant extends BaseZombie
{
    @Override
    public String getEntityName() {
        return ChatColor.DARK_RED + "Atoned Revenant";
    }
    
    @Override
    public double getEntityMaxHealth() {
        return 2400000.0;
    }
    
    @Override
    public double getDamageDealt() {
        return 4800.0;
    }
    
    @Override
    public double getXPDropped() {
        return 1600.0;
    }
    
    @Override
    public SEntityEquipment getEntityEquipment() {
        return new SEntityEquipment(SUtil.enchant(new ItemStack(Material.IRON_SWORD)), null, SUtil.enchant(new ItemStack(Material.DIAMOND_CHESTPLATE)), SUtil.enchant(SUtil.applyColorToLeatherArmor(new ItemStack(Material.LEATHER_LEGGINGS), Color.fromRGB(16777215))), SUtil.enchant(SUtil.applyColorToLeatherArmor(new ItemStack(Material.LEATHER_BOOTS), Color.fromRGB(16777215))));
    }
    
    @Override
    public List<EntityDrop> drops() {
        return Collections.<EntityDrop>singletonList(new EntityDrop(SUtil.setStackAmount(SItem.of(SMaterial.REVENANT_FLESH).getStack(), 5), EntityDropType.GUARANTEED, 1.0));
    }
}
