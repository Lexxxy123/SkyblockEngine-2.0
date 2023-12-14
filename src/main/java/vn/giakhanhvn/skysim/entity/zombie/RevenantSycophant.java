package vn.giakhanhvn.skysim.entity.zombie;

import java.util.Collections;
import vn.giakhanhvn.skysim.entity.EntityDropType;
import vn.giakhanhvn.skysim.item.SMaterial;
import vn.giakhanhvn.skysim.entity.EntityDrop;
import java.util.List;
import vn.giakhanhvn.skysim.util.SUtil;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import vn.giakhanhvn.skysim.entity.SEntityEquipment;

public class RevenantSycophant extends BaseZombie
{
    @Override
    public String getEntityName() {
        return "Revenant Sycophant";
    }
    
    @Override
    public double getEntityMaxHealth() {
        return 24000.0;
    }
    
    @Override
    public double getDamageDealt() {
        return 850.0;
    }
    
    @Override
    public double getXPDropped() {
        return 300.0;
    }
    
    @Override
    public SEntityEquipment getEntityEquipment() {
        return new SEntityEquipment(SUtil.enchant(new ItemStack(Material.DIAMOND_SWORD)), null, SUtil.enchant(new ItemStack(Material.DIAMOND_CHESTPLATE)), SUtil.enchant(new ItemStack(Material.CHAINMAIL_LEGGINGS)), new ItemStack(Material.IRON_BOOTS));
    }
    
    @Override
    public List<EntityDrop> drops() {
        return Collections.<EntityDrop>singletonList(new EntityDrop(SMaterial.REVENANT_FLESH, EntityDropType.GUARANTEED, 1.0));
    }
}
