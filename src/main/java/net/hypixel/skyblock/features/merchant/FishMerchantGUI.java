/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.hypixel.skyblock.features.merchant;

import net.hypixel.skyblock.features.merchant.MerchantItemHandler;
import net.hypixel.skyblock.features.merchant.ShopGUI;
import net.hypixel.skyblock.item.SItem;
import net.hypixel.skyblock.item.SMaterial;

public class FishMerchantGUI
extends ShopGUI {
    private static final SItem[] ITEMS = new SItem[]{MerchantItemHandler.getItem(SMaterial.FISHING_ROD), MerchantItemHandler.getItem(SMaterial.RAW_FISH), MerchantItemHandler.getItem(SMaterial.RAW_SALMON), MerchantItemHandler.getItem(SMaterial.PUFFERFISH)};

    public FishMerchantGUI(int page) {
        super("Fish Merchant", page, ITEMS);
    }

    public FishMerchantGUI() {
        this(1);
    }
}

