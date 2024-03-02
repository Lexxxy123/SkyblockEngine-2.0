package net.hypixel.skyblock.entity.dimoon;

import net.hypixel.skyblock.item.SItem;

public class DimoonLootItem {
    private final SItem item;
    private final int chance;
    private final int minimumWeight;
    private int amount;
    private boolean isRandomizedAmount;

    public DimoonLootItem(SItem item, int chancePer, int minimumWeight) {
        this(item, 1, chancePer, minimumWeight, false);
    }

    public DimoonLootItem(SItem item, int amount, int chancePer, int minimumWeight) {
        this(item, amount, chancePer, minimumWeight, false);
    }

    public DimoonLootItem(SItem item, int amount, int chancePer, int minimumWeight, boolean randomAmount) {
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

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public boolean isRandomizedAmount() {
        return this.isRandomizedAmount;
    }
}
