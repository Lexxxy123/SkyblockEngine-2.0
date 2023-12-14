package vn.giakhanhvn.skysim.collection;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import vn.giakhanhvn.skysim.item.SMaterial;

public class ItemCollectionRecipeReward extends ItemCollectionReward
{
    private final SMaterial material;
    
    public ItemCollectionRecipeReward(final SMaterial material) {
        super(Type.RECIPE);
        this.material = material;
    }
    
    @Override
    public void onAchieve(final Player player) {
    }
    
    @Override
    public String toRewardString() {
        return ChatColor.GRAY + this.material.getDisplayName(this.material.getData()) + ChatColor.GRAY + " Recipe";
    }
}
