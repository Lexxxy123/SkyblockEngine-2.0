/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.hypixel.skyblock.gui;

import net.hypixel.skyblock.features.merchant.FarmMerchantGUI;
import net.hypixel.skyblock.gui.ActiveEffectsGUI;
import net.hypixel.skyblock.gui.AdminItemBrowser;
import net.hypixel.skyblock.gui.AnvilGUI;
import net.hypixel.skyblock.gui.AuctionConfirmGUI;
import net.hypixel.skyblock.gui.AuctionHouseGUI;
import net.hypixel.skyblock.gui.AuctionsBrowserGUI;
import net.hypixel.skyblock.gui.BankerGUI;
import net.hypixel.skyblock.gui.BoosterCookieShop;
import net.hypixel.skyblock.gui.BossMenu;
import net.hypixel.skyblock.gui.CollectionBoss;
import net.hypixel.skyblock.gui.CollectionMenuGUI;
import net.hypixel.skyblock.gui.CookieConfirmGUI;
import net.hypixel.skyblock.gui.CookieGUI;
import net.hypixel.skyblock.gui.CraftingTableGUI;
import net.hypixel.skyblock.gui.CreateAuctionGUI;
import net.hypixel.skyblock.gui.DepositGUI;
import net.hypixel.skyblock.gui.DungeonsItemConverting;
import net.hypixel.skyblock.gui.DungeonsLevelGUI;
import net.hypixel.skyblock.gui.GUI;
import net.hypixel.skyblock.gui.ItemBrowserGUI;
import net.hypixel.skyblock.gui.ItemEditor;
import net.hypixel.skyblock.gui.LiftGUI;
import net.hypixel.skyblock.gui.ManageAuctionsGUI;
import net.hypixel.skyblock.gui.MobSummonGUI;
import net.hypixel.skyblock.gui.PetsGUI;
import net.hypixel.skyblock.gui.QuiverGUI;
import net.hypixel.skyblock.gui.RecipeBookListGUI;
import net.hypixel.skyblock.gui.RecipeCreatorGUI;
import net.hypixel.skyblock.gui.ReforgeAnvilGUI;
import net.hypixel.skyblock.gui.RevenantHorrorGUI;
import net.hypixel.skyblock.gui.SkillMenuGUI;
import net.hypixel.skyblock.gui.SkyBlockMenuGUI;
import net.hypixel.skyblock.gui.SkyBlockProfileGUI;
import net.hypixel.skyblock.gui.SlayerGUI;
import net.hypixel.skyblock.gui.SvenPackmasterGUI;
import net.hypixel.skyblock.gui.TarantulaBroodfatherGUI;
import net.hypixel.skyblock.gui.TrashGUI;
import net.hypixel.skyblock.gui.VoidgloomSeraph;
import net.hypixel.skyblock.gui.WarpGUI;
import net.hypixel.skyblock.gui.WithdrawalGUI;
import net.hypixel.skyblock.gui.YourBidsGUI;
import net.hypixel.skyblock.util.SUtil;

public enum GUIType {
    CRAFTING_TABLE(CraftingTableGUI.class),
    ITEM_BROWSE(ItemBrowserGUI.class),
    MOB_GUI(MobSummonGUI.class),
    ANVIL(AnvilGUI.class),
    TRASH(TrashGUI.class),
    COOKIE_GUI(CookieGUI.class),
    ITEM_EDITOR(ItemEditor.class),
    COOKIE_CONSUME_CONFIRM(CookieConfirmGUI.class),
    RECIPE_BOOK(RecipeBookListGUI.class),
    REFORGE_ANVIL(ReforgeAnvilGUI.class),
    DUNGEON_CRAFTING(DungeonsItemConverting.class),
    DUNGEON_SKILL(DungeonsLevelGUI.class),
    BOOSTER_COOKIE_SHOP(BoosterCookieShop.class),
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
    ADMIN_ITEM_BROWSER(AdminItemBrowser.class),
    RECIPE_CREATOR(RecipeCreatorGUI.class),
    VOIDGLOOM_SERAPH(VoidgloomSeraph.class);

    private final Class<? extends GUI> gui;

    private GUIType(Class<? extends GUI> gui) {
        this.gui = gui;
    }

    public GUI getGUI() {
        try {
            return this.gui.newInstance();
        } catch (IllegalAccessException | InstantiationException e2) {
            e2.printStackTrace();
            return null;
        }
    }

    public GUI getGUI(Object ... params) {
        return SUtil.instance(GUI.class, params);
    }

    public static GUI getGUI(String title) {
        for (GUIType type : GUIType.values()) {
            if (!type.getGUI().getTitle().contains(title)) continue;
            return type.getGUI();
        }
        return null;
    }
}

