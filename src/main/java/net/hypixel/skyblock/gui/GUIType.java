package net.hypixel.skyblock.gui;

import net.hypixel.skyblock.features.merchant.FarmMerchantGUI;
import net.hypixel.skyblock.util.SUtil;

public enum GUIType {
    CRAFTING_TABLE(CraftingTableGUI.class),
    ITEM_BROWSE(ItemBrowserGUI.class),
    ANVIL(AnvilGUI.class),
    TRASH(TrashGUI.class),
    COOKIE_GUI(CookieGUI.class),
    COOKIE_CONSUME_CONFIRM(CookieConfirmGUI.class),
    RECIPE_BOOK(RecipeBookListGUI.class),
    REFORGE_ANVIL(ReforgeAnvilGUI.class),
    DUNGEON_CRAFTING(DungeonsItemConverting.class),
    DUNGEON_SKILL(DungeonsLevelGUI.class),
    BANKER(BankerGUI.class),
    BANKER_DEPOSIT(DepositGUI.class),
    BANKER_WITHDRAWAL(WithdrawalGUI.class),
    SKYBLOCK_MENU(SkyBlockMenuGUI.class),
    CATACOMBS_BOSS(BossMenu.class),
    BOSS_COLLECTION(CollectionBoss.class),
    SKYBLOCK_PROFILE(SkyBlockProfileGUI.class),
    QUIVER(QuiverGUI.class),
    LIFT(LiftGUI.class),
    SLAYER(SlayerGUI.class),
    REVENANT_HORROR(RevenantHorrorGUI.class),
    TARANTULA_BROODFATHER(TarantulaBroodfatherGUI.class),
    SVEN_PACKMASTER(SvenPackmasterGUI.class),
    COLLECTION_MENU(CollectionMenuGUI.class),
    SKILL_MENU(SkillMenuGUI.class),
    PETS(PetsGUI.class),
    FARM_MERCHANT(FarmMerchantGUI.class),
    ACTIVE_EFFECTS(ActiveEffectsGUI.class),
    AUCTION_HOUSE(AuctionHouseGUI.class),
    AUCTIONS_BROWSER(AuctionsBrowserGUI.class),
    CREATE_AUCTION(CreateAuctionGUI.class),
    AUCTION_CONFIRM(AuctionConfirmGUI.class),
    MANAGE_AUCTIONS(ManageAuctionsGUI.class),
    YOUR_BIDS(YourBidsGUI.class),
    WARP(WarpGUI.class),
    VOIDGLOOM_SERAPH(VoidgloomSeraph.class);

    private final Class<? extends GUI> gui;

    GUIType(final Class<? extends GUI> gui) {
        this.gui = gui;
    }

    public GUI getGUI() {
        try {
            return this.gui.newInstance();
        } catch (final IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
            return null;
        }
    }

    public GUI getGUI(final Object... params) {
        return SUtil.instance(GUI.class, params);
    }

    public static GUI getGUI(final String title) {
        for (final GUIType type : values()) {
            if (type.getGUI().getTitle().contains(title)) {
                return type.getGUI();
            }
        }
        return null;
    }
}
