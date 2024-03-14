package net.hypixel.skyblock.features.itemeditor;


import net.hypixel.skyblock.features.dungeons.stats.ItemSerial;
import net.hypixel.skyblock.features.enchantment.EnchantmentType;
import net.hypixel.skyblock.features.reforge.Reforge;
import net.hypixel.skyblock.item.SItem;
import org.bukkit.entity.Player;

public class EditableItem {

    SItem sItem;

    public EditableItem(SItem item) {
            this.sItem = item;
        }


    public void addStar(int amount)
    {
        if (amount == 0) return;
        if (amount > 5) return;
        if (!sItem.isStarrable()) return;
        ItemSerial is = ItemSerial.createBlank();
        is.saveTo(sItem);
        sItem.setStarAmount(getStars() + amount);
    }
    public int getStars(){
        return sItem.getStar();
    }


    public void recom(boolean setrecom){
        sItem.setRecombobulated(setrecom);
    }

    public void clone(Player player) {
        player.getInventory().addItem(sItem.getStack());
    }


    public void toDungeonItem(){
        sItem.setDungeonsItem(true);
    }


    public void reforge(Reforge reforge){
        sItem.setReforge(reforge);
    }

    public void enchant(EnchantmentType type, int level){
        sItem.addEnchantment(type , level);
    }

    public void addhpb() {
        int oldAmount = sItem.getHPBs();
        if (oldAmount == 20) return;
        sItem.setHPBs(oldAmount + 1);
    }


    public SItem getHandle(){
        return sItem;
    }

}
