package in.godspunky.skyblock.minion;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import in.godspunky.skyblock.item.SMaterial;

public interface MinionParameters {
    String name();
    default ItemStack itemInHand(){
        return new ItemStack(Material.WOOD_PICKAXE);
    }

    SMaterial material();

}
