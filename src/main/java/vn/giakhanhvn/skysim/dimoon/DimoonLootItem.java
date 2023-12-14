package vn.giakhanhvn.skysim.dimoon;

import vn.giakhanhvn.skysim.item.SItem;

public class DimoonLootItem
{
    private SItem item;
    private int chance;
    private int minimumWeight;
    private int amount;
    private boolean isRandomizedAmount;
    
    public DimoonLootItem(final SItem item, final int chancePer, final int minimumWeight) {
        this(item, 1, chancePer, minimumWeight, false);
    }
    
    public DimoonLootItem(final SItem item, final int amount, final int chancePer, final int minimumWeight) {
        this(item, amount, chancePer, minimumWeight, false);
    }
    
    public DimoonLootItem(final SItem item, final int amount, final int chancePer, final int minimumWeight, final boolean randomAmount) {
        this.amount = 1;
        this.isRandomizedAmount = false;
        this.item = item;
        this.chance = chancePer;
        this.minimumWeight = minimumWeight;
        this.amount = amount;
        this.isRandomizedAmount = randomAmount;
    }
    
    public SItem getItem() {
        return this.item;
    }
    
    public int getChance() {
        return this.chance;
    }
    
    public int getMinimumWeight() {
        return this.minimumWeight;
    }
    
    public int getAmount() {
        return this.amount;
    }
    
    public void setAmount(final int amount) {
        this.amount = amount;
    }
    
    public boolean isRandomizedAmount() {
        return this.isRandomizedAmount;
    }
}
