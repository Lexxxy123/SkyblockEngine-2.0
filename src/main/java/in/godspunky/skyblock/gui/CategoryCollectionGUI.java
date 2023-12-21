package in.godspunky.skyblock.gui;

import in.godspunky.skyblock.collection.ItemCollection;
import in.godspunky.skyblock.collection.ItemCollectionCategory;
import in.godspunky.skyblock.collection.ItemCollectionReward;
import in.godspunky.skyblock.collection.ItemCollectionRewards;
import in.godspunky.skyblock.user.User;
import in.godspunky.skyblock.util.PaginationList;
import in.godspunky.skyblock.util.SUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CategoryCollectionGUI extends GUI {
    private static final int[] INTERIOR;
    private final ItemCollectionCategory category;
    private int page;

    public CategoryCollectionGUI(final ItemCollectionCategory category, final int page) {
        super(category.getName() + " Collection", 54);
        this.category = category;
        this.page = page;
    }

    public CategoryCollectionGUI(final ItemCollectionCategory category) {
        this(category, 1);
    }

    @Override
    public void onOpen(final GUIOpenEvent e) {
        final Player player = e.getPlayer();
        final User user = User.getUser(player.getUniqueId());
        this.border(CategoryCollectionGUI.BLACK_STAINED_GLASS_PANE);
        final PaginationList<ItemCollection> paged = new PaginationList<ItemCollection>(28);
        paged.addAll(ItemCollection.getCategoryCollections(this.category));
        if (paged.size() == 0) {
            this.page = 0;
        }
        final int finalPage = this.page;
        if (this.page > 1) {
            this.set(new GUIClickableItem() {
                @Override
                public void run(final InventoryClickEvent e) {
                    new CategoryCollectionGUI(CategoryCollectionGUI.this.category, finalPage - 1).open((Player) e.getWhoClicked());
                }

                @Override
                public int getSlot() {
                    return 45;
                }

                @Override
                public ItemStack getItem() {
                    return SUtil.createNamedItemStack(Material.ARROW, ChatColor.GRAY + "<-");
                }
            });
        }
        if (this.page != paged.getPageCount()) {
            this.set(new GUIClickableItem() {
                @Override
                public void run(final InventoryClickEvent e) {
                    new CategoryCollectionGUI(CategoryCollectionGUI.this.category, finalPage + 1).open((Player) e.getWhoClicked());
                }

                @Override
                public int getSlot() {
                    return 53;
                }

                @Override
                public ItemStack getItem() {
                    return SUtil.createNamedItemStack(Material.ARROW, ChatColor.GRAY + "->");
                }
            });
        }
        this.set(GUIClickableItem.createGUIOpenerItem(GUIType.COLLECTION_MENU, player, ChatColor.GREEN + "Go Back", 48, Material.ARROW, ChatColor.GRAY + "To Collection"));
        this.set(GUIClickableItem.getCloseItem(49));
        final List<ItemCollection> p = paged.getPage(this.page);
        if (p == null) {
            return;
        }
        for (int i = 0; i < p.size(); ++i) {
            final int slot = CategoryCollectionGUI.INTERIOR[i];
            final ItemCollection collection = p.get(i);
            final int amount = user.getCollection(collection);
            if (amount != 0) {
                final List<String> lore = new ArrayList<String>(Arrays.asList(ChatColor.GRAY + "View all your " + collection.getName() + " Collection", ChatColor.GRAY + "progress and rewards!", " "));
                final int tier = collection.getTier(amount);
                final int next = tier + 1;
                final int nextReq = collection.getRequirementForTier(next);
                if (nextReq != -1) {
                    final String numeral = SUtil.toRomanNumeral(next);
                    lore.add(SUtil.createProgressText("Progress to " + collection.getName() + " " + numeral, amount, nextReq));
                    lore.add(SUtil.createLineProgressBar(20, ChatColor.DARK_GREEN, amount, nextReq));
                    lore.add(" ");
                    final ItemCollectionRewards rewards = collection.getRewardsFor(next);
                    if (rewards != null && rewards.size() != 0) {
                        lore.add(ChatColor.GRAY + collection.getName() + " " + numeral + " Reward" + ((rewards.size() != 1) ? "s" : ""));
                        for (final ItemCollectionReward reward : rewards) {
                            lore.add(ChatColor.GRAY + " " + reward.toRewardString());
                        }
                        lore.add(" ");
                    }
                }
                lore.add(ChatColor.YELLOW + "Click to view!");
                this.set(new GUIClickableItem() {
                    @Override
                    public void run(final InventoryClickEvent e) {
                        new ItemCollectionGUI(collection).open(player);
                    }

                    @Override
                    public int getSlot() {
                        return slot;
                    }

                    @Override
                    public ItemStack getItem() {
                        return SUtil.getStack(ChatColor.YELLOW + collection.getName() + ((tier != 0) ? (" " + SUtil.toRomanNumeral(tier)) : ""), collection.getMaterial().getCraftMaterial(), collection.getData(), 1, lore);
                    }
                });
            } else {
                this.set(slot, SUtil.getStack(ChatColor.RED + collection.getName(), Material.INK_SACK, (short) 8, 1, ChatColor.GRAY + "Find this item to add it to your", ChatColor.GRAY + "collection and unlock collection", ChatColor.GRAY + "rewards!"));
            }
        }
    }

    static {
        INTERIOR = new int[]{10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34, 37, 38, 39, 40, 41, 42, 43};
    }
}
