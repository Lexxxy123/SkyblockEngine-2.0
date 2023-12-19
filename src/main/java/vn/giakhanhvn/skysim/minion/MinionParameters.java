package vn.giakhanhvn.skysim.minion;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import vn.giakhanhvn.skysim.item.SMaterial;

public interface MinionParameters {
    String name();
    default ItemStack itemInHand(){
        return new ItemStack(Material.WOOD_PICKAXE);
    }

    SMaterial material();

}
