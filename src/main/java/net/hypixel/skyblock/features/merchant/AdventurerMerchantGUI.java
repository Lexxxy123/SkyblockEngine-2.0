/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.hypixel.skyblock.features.merchant;

import net.hypixel.skyblock.features.merchant.MerchantItemHandler;
import net.hypixel.skyblock.features.merchant.ShopGUI;
import net.hypixel.skyblock.item.SItem;
import net.hypixel.skyblock.item.SMaterial;

public class AdventurerMerchantGUI
extends ShopGUI {
    private static final SItem[] ITEMS = new SItem[]{MerchantItemHandler.getItem(SMaterial.ROTTEN_FLESH), MerchantItemHandler.getItem(SMaterial.BONE), MerchantItemHandler.getItem(SMaterial.STRING), MerchantItemHandler.getItem(SMaterial.SLIME_BALL), MerchantItemHandler.getItem(SMaterial.GUNPOWDER)};

    public AdventurerMerchantGUI(int page) {
        super("Adventurer", page, ITEMS);
    }

    public AdventurerMerchantGUI() {
        this(1);
    }
}

