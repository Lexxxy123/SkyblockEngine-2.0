package vn.giakhanhvn.skysim.merchant;

import vn.giakhanhvn.skysim.util.SUtil;
import vn.giakhanhvn.skysim.item.SMaterial;
import vn.giakhanhvn.skysim.item.SItem;

public class FarmMerchantGUI extends ShopGUI {
    private static final SItem[] ITEMS = new SItem[]{
            MerchantItemHandler.getItem(SMaterial.WHEAT),
            MerchantItemHandler.getItem(SMaterial.CARROT_ITEM),
            MerchantItemHandler.getItem(SMaterial.POTATO_ITEM),
            MerchantItemHandler.getItem(SMaterial.MELON),
            MerchantItemHandler.getItem(SMaterial.SUGAR_CANE),
            MerchantItemHandler.getItem(SMaterial.PUMPKIN),
            MerchantItemHandler.getItem(SMaterial.COCOA_BEANS),
            MerchantItemHandler.getItem(SMaterial.RED_MUSHROOM),
            MerchantItemHandler.getItem(SMaterial.BROWN_MUSHROOM),
            MerchantItemHandler.getItem(SMaterial.SAND),
            MerchantItemHandler.getItem(SMaterial.CACTUS)
    };


    public FarmMerchantGUI(int page) {
        super("Farm Merchant", page, ITEMS);
    }

    public FarmMerchantGUI() {
        this(1);
    }
}
