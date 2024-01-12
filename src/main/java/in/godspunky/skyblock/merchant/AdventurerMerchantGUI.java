package in.godspunky.skyblock.merchant;

import in.godspunky.skyblock.item.SItem;
import in.godspunky.skyblock.item.SMaterial;

public class AdventurerMerchantGUI extends ShopGUI {
    private static final SItem[] ITEMS = new SItem[]{
            MerchantItemHandler.getItem(SMaterial.ROTTEN_FLESH),
            MerchantItemHandler.getItem(SMaterial.BONE),
            MerchantItemHandler.getItem(SMaterial.STRING),
            MerchantItemHandler.getItem(SMaterial.SLIME_BALL),
            MerchantItemHandler.getItem(SMaterial.GUNPOWDER)
    };


    public AdventurerMerchantGUI(int page) {
        super("Adventurer", page, ITEMS);
    }

    public AdventurerMerchantGUI() {
        this(1);
    }

}
