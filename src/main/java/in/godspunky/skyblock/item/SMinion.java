package in.godspunky.skyblock.item;

import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public interface SMinion extends MaterialStatistics {

    ItemStack getHand(int level);

    String getHead(int level);

    int getActionDelay(int level);

    int getMaxStorage(int level);

    ArrayList<ItemStack> calculateDrops(int level);

    int getSlotLevelRequirement(int level);

    @Override
    default String getLore() {
        return "minion ig!";
    }

    @Override
    default Rarity getRarity() {
        return Rarity.RARE;
    }

    @Override
    default GenericItemType getType() {
        return GenericItemType.ITEM;
    }

}
