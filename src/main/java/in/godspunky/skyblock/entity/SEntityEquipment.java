package in.godspunky.skyblock.entity;

import org.bukkit.inventory.ItemStack;

public class SEntityEquipment {
    private ItemStack itemInHand;
    private ItemStack helmet;
    private ItemStack chestplate;
    private ItemStack leggings;
    private ItemStack boots;

    public SEntityEquipment(final ItemStack itemInHand, final ItemStack helmet, final ItemStack chestplate, final ItemStack leggings, final ItemStack boots) {
        this.itemInHand = itemInHand;
        this.helmet = helmet;
        this.chestplate = chestplate;
        this.leggings = leggings;
        this.boots = boots;
    }

    public ItemStack getItemInHand() {
        return this.itemInHand;
    }

    public void setItemInHand(final ItemStack itemInHand) {
        this.itemInHand = itemInHand;
    }

    public ItemStack getHelmet() {
        return this.helmet;
    }

    public void setHelmet(final ItemStack helmet) {
        this.helmet = helmet;
    }

    public ItemStack getChestplate() {
        return this.chestplate;
    }

    public void setChestplate(final ItemStack chestplate) {
        this.chestplate = chestplate;
    }

    public ItemStack getLeggings() {
        return this.leggings;
    }

    public void setLeggings(final ItemStack leggings) {
        this.leggings = leggings;
    }

    public ItemStack getBoots() {
        return this.boots;
    }

    public void setBoots(final ItemStack boots) {
        this.boots = boots;
    }
}
