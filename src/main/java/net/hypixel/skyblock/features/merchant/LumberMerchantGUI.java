/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.hypixel.skyblock.features.merchant;

import net.hypixel.skyblock.features.merchant.MerchantItemHandler;
import net.hypixel.skyblock.features.merchant.ShopGUI;
import net.hypixel.skyblock.item.SItem;
import net.hypixel.skyblock.item.SMaterial;

public class LumberMerchantGUI
extends ShopGUI {
    private static final SItem[] ITEMS = new SItem[]{MerchantItemHandler.getItem(SMaterial.OAK_WOOD), MerchantItemHandler.getItem(SMaterial.BIRCH_WOOD), MerchantItemHandler.getItem(SMaterial.SPRUCE_WOOD), MerchantItemHandler.getItem(SMaterial.DARK_OAK_WOOD), MerchantItemHandler.getItem(SMaterial.ACACIA_WOOD), MerchantItemHandler.getItem(SMaterial.JUNGLE_WOOD), MerchantItemHandler.getItem(SMaterial.STICK), MerchantItemHandler.getItem(SMaterial.PODZOL), MerchantItemHandler.getItem(SMaterial.WOOD_SWORD), MerchantItemHandler.getItem(SMaterial.WOOD_PICKAXE), MerchantItemHandler.getItem(SMaterial.WOOD_SHOVEL), MerchantItemHandler.getItem(SMaterial.WOOD_HOE), MerchantItemHandler.getItem(SMaterial.WOOD_AXE)};

    public LumberMerchantGUI(int page) {
        super("Lumber Merchant", page, ITEMS);
    }

    public LumberMerchantGUI() {
        this(1);
    }
}

