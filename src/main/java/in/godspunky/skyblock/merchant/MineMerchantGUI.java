package in.godspunky.skyblock.merchant;

import in.godspunky.skyblock.item.SItem;
import in.godspunky.skyblock.item.SMaterial;

public class MineMerchantGUI extends ShopGUI{

    private static final SItem[] ITEMS = new SItem[]
            {
                    MerchantItemHandler.getItem(SMaterial.COAL),
                    MerchantItemHandler.getItem(SMaterial.IRON_INGOT),
                    MerchantItemHandler.getItem(SMaterial.GOLD_INGOT),
                    MerchantItemHandler.getItem(SMaterial.GOLD_PICKAXE),
                    MerchantItemHandler.getItem(SMaterial.TORCH),
                    MerchantItemHandler.getItem(SMaterial.GRAVEL),
                    MerchantItemHandler.getItem(SMaterial.COBBLESTONE),
                    MerchantItemHandler.getItem(SMaterial.STONE),
            };
    public MineMerchantGUI(int page) {
        super("Mine Merchant", page, ITEMS);
    }

    public MineMerchantGUI() {
        this(1);
    }
}
