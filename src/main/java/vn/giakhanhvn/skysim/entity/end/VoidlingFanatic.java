package vn.giakhanhvn.skysim.entity.end;

import java.util.Arrays;
import vn.giakhanhvn.skysim.item.SMaterial;
import vn.giakhanhvn.skysim.entity.EntityDropType;
import org.bukkit.inventory.ItemStack;
import vn.giakhanhvn.skysim.util.SUtil;
import org.bukkit.Material;
import vn.giakhanhvn.skysim.entity.EntityDrop;
import java.util.List;

public class VoidlingFanatic extends BaseEnderman
{
    @Override
    public String getEntityName() {
        return "Voidling Fanatic";
    }
    
    @Override
    public double getEntityMaxHealth() {
        return 750000.0;
    }
    
    @Override
    public double getDamageDealt() {
        return 3500.0;
    }
    
    @Override
    public double getXPDropped() {
        return 110.0;
    }
    
    @Override
    public List<EntityDrop> drops() {
        return Arrays.<EntityDrop>asList(new EntityDrop(new ItemStack(Material.ENDER_PEARL, SUtil.random(2, 4)), EntityDropType.GUARANTEED, 1.0), new EntityDrop(SMaterial.ENCHANTED_ENDER_PEARL, EntityDropType.RARE, 0.05));
    }
    
    @Override
    public int mobLevel() {
        return 85;
    }
}
