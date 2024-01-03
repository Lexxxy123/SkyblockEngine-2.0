package in.godspunky.skyblock.item.minions;

import in.godspunky.skyblock.item.SMinion;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;

public class CobbleStoneMinion implements  SMinion {

    @Override
    public String getDisplayName() {
        return "Cobblestone minion";
    }

    @Override
    public ItemStack getHand(int level) {
        return new ItemStack(Material.WOOD_PICKAXE);
    }

    @Override
    public String getHead(int level) {
        return "null";
    }

    @Override
    public int getActionDelay(int level) {
        return 10;
    }

    @Override
    public int getMaxStorage(int level) {
        return 1;
    }

    @Override
    public ArrayList<ItemStack> calculateDrops(int level) {
         return new ArrayList<ItemStack>(Arrays.asList(new ItemStack(Material.COBBLESTONE)));
    }


    @Override
    public int getSlotLevelRequirement(int slot) {
        switch (slot) {
            case 0:
                return 1;
            case 1:
            case 2:
                return 2;
            case 3:
            case 4:
            case 5:
                return 4;
            case 6:
            case 7:
            case 8:
                return 6;
            case 9:
            case 10:
            case 11:
                return 8;
            case 12:
            case 13:
            case 14:
                return 10;
            default:
                return 0;
        }
    }
}
