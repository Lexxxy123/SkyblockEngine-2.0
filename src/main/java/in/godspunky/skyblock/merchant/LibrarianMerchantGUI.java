package in.godspunky.skyblock.merchant;

import in.godspunky.skyblock.item.SItem;
import in.godspunky.skyblock.item.SMaterial;

public class LibrarianMerchantGUI extends ShopGUI{


    private static final SItem[] ITEMS = new SItem[] {
            MerchantItemHandler.getItem(SMaterial.EXP_BOTTLE),
            MerchantItemHandler.getItem(SMaterial.BOOK)
    };


    public LibrarianMerchantGUI(int page) {
        super("Librarian", page, ITEMS);
    }


    public LibrarianMerchantGUI() {
        this(1);
    }
}
