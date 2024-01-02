package in.godspunky.skyblock.merchant;

import in.godspunky.skyblock.item.SItem;
import in.godspunky.skyblock.item.SMaterial;
import in.godspunky.skyblock.util.SUtil;

import java.util.HashMap;

public class MerchantItemHandler {
    public final static HashMap<SMaterial , SItem> ITEMS = new HashMap<>();

    public static void init(){
        initFarmMerchant();
        initMineMerchant();
        initLumberMerchant();
        initAdventurerMerchant();
        initFishMerchant();
        initLibrarianMerchant();

    }

    private static void initFarmMerchant(){
        SUtil.toShopItem(SMaterial.WHEAT, 3, 10L, 6L);
        SUtil.toShopItem(SMaterial.CARROT_ITEM, 3, 10L, 3L);
        SUtil.toShopItem(SMaterial.POTATO_ITEM, 3, 10L, 3L);
        SUtil.toShopItem(SMaterial.MELON, 10, 4L, 2L);
        SUtil.toShopItem(SMaterial.SUGAR_CANE, 3, 10L, 4L);
        SUtil.toShopItem(SMaterial.PUMPKIN, 1, 25L, 10L);
        SUtil.toShopItem(SMaterial.COCOA_BEANS, 1, 5L, 3L);
        SUtil.toShopItem(SMaterial.RED_MUSHROOM, 1, 12L, 10L);
        SUtil.toShopItem(SMaterial.BROWN_MUSHROOM, 1, 12L, 10L);
        SUtil.toShopItem(SMaterial.SAND, 2, 4L, 2L);
        SUtil.toShopItem(SMaterial.CACTUS, 1, 10L, 3L);
    }
    private static void initMineMerchant(){
        SUtil.toShopItem(SMaterial.COAL, 2 , 4L , 2L);
        SUtil.toShopItem(SMaterial.IRON_INGOT, 4 , 5L , 3L);
        SUtil.toShopItem(SMaterial.GOLD_INGOT, 4 , 5L , 4L);
        SUtil.toShopItem(SMaterial.GOLD_PICKAXE, 1 , 20L , 10L);
        SUtil.toShopItem(SMaterial.TORCH, 32 , 1L , 1L);
        SUtil.toShopItem(SMaterial.GRAVEL, 2 , 8L , 4L);
        SUtil.toShopItem(SMaterial.COBBLESTONE, 1 , 3L , 1L);
        SUtil.toShopItem(SMaterial.STONE, 2 , 2L , 1L);
    }
    private static void initLumberMerchant(){

        SUtil.toShopItem(SMaterial.OAK_WOOD, 5 , 25L , 5L);
        SUtil.toShopItem(SMaterial.BIRCH_WOOD, 5 , 25L , 5L);
        SUtil.toShopItem(SMaterial.SPRUCE_WOOD, 5 , 25L , 5L);
        SUtil.toShopItem(SMaterial.DARK_OAK_WOOD, 5 , 25L , 5L);
        SUtil.toShopItem(SMaterial.ACACIA_WOOD, 5 , 25L , 5L);
        SUtil.toShopItem(SMaterial.JUNGLE_WOOD, 5 , 25L , 5L);
        SUtil.toShopItem(SMaterial.STICK, 32 , 2L , 1L);
        SUtil.toShopItem(SMaterial.PODZOL, 1 , 20L , 10L);
        SUtil.toShopItem(SMaterial.WOOD_SWORD, 1 , 5L , 1L);
        SUtil.toShopItem(SMaterial.WOOD_PICKAXE, 1 , 5L , 1L);
        SUtil.toShopItem(SMaterial.WOOD_SHOVEL, 1 , 5L , 1L);
        SUtil.toShopItem(SMaterial.WOOD_HOE, 1 , 5L , 1L);
        SUtil.toShopItem(SMaterial.WOOD_AXE, 1 , 5L , 1L);
    }
    private static void initAdventurerMerchant(){
        SUtil.toShopItem(SMaterial.ROTTEN_FLESH, 1 , 8L , 2L);
        SUtil.toShopItem(SMaterial.BONE, 1 , 8L , 2L);
        SUtil.toShopItem(SMaterial.STRING, 1 , 10L , 3L);
        SUtil.toShopItem(SMaterial.SLIME_BALL, 1 , 14L , 5L);
        SUtil.toShopItem(SMaterial.GUNPOWDER, 1 , 10L , 4L);
    }
    private static void initFishMerchant(){
        SUtil.toShopItem(SMaterial.FISHING_ROD, 1 , 100L , 40L);
        SUtil.toShopItem(SMaterial.RAW_FISH, 1 , 20L , 6L);
        SUtil.toShopItem(SMaterial.RAW_SALMON, 1 , 30L , 10L);
        SUtil.toShopItem(SMaterial.PUFFERFISH, 1 , 40L , 15L);
    }

    private static void initLibrarianMerchant(){
        SUtil.toShopItem(SMaterial.EXP_BOTTLE, 1 , 30L , 5L);
        SUtil.toShopItem(SMaterial.BOOK, 1 , 20L , 2L);
    }

    public static SItem getItem(SMaterial material){
        if (ITEMS.containsKey(material)) return ITEMS.get(material);
        return null;
    }
}
