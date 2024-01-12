package in.godspunky.skyblock.gui;

import in.godspunky.skyblock.collection.ItemCollection;
import in.godspunky.skyblock.collection.ItemCollectionCategory;
import in.godspunky.skyblock.user.User;
import in.godspunky.skyblock.util.SUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

public class CollectionMenuGUI extends GUI {
    public CollectionMenuGUI() {
        super("Collection", 54);
    }

    private static GUIClickableItem createCollectionClickable(final GUI gui, final ItemCollectionCategory category, final Material icon, final short data, final int slot, final Player player) {
        final String[] progress = ItemCollection.getProgress(player, category);
        return GUIClickableItem.createGUIOpenerItem(gui, player, ChatColor.GREEN + category.getName() + " Collection", slot, icon, data, ChatColor.GRAY + "View your " + category.getName() + " Collection!", " ", progress[0], progress[1], " ", ChatColor.YELLOW + "Click to view!");
    }

    private static GUIClickableItem createCollectionClickable(final GUI gui, final ItemCollectionCategory category, final Material icon, final int slot, final Player player) {
        return createCollectionClickable(gui, category, icon, (short) 0, slot, player);
    }

    @Override
    public void onOpen(final GUIOpenEvent e) {
        this.fill(BLACK_STAINED_GLASS_PANE);
        final Player player = e.getPlayer();
        final User user = User.getUser(player.getUniqueId());
        this.set(GUIClickableItem.getCloseItem(49));
        final AtomicInteger found = new AtomicInteger();
        final Collection<ItemCollection> collections = ItemCollection.getCollections();
        for (final ItemCollection collection : collections) {
            if (user.getCollection(collection) > 0) {
                found.incrementAndGet();
            }
        }
        this.set(GUIClickableItem.createGUIOpenerItem(GUIType.SKYBLOCK_MENU, player, ChatColor.GREEN + "Go Back", 48, Material.ARROW, ChatColor.GRAY + "To SkyBlock Menu"));
        final String[] progress = ItemCollection.getProgress(player, null);
        this.set(4, SUtil.getStack(ChatColor.GREEN + "Collection", Material.PAINTING, (short) 0, 1, ChatColor.GRAY + "View all of the items available", ChatColor.GRAY + "in SkyBlock. Collect more of an", ChatColor.GRAY + "item to unlock rewards on your", ChatColor.GRAY + "way to becoming a master of", ChatColor.GRAY + "SkyBlock!", " ", progress[0], progress[1]));
        this.set(createCollectionClickable(new CategoryCollectionGUI(ItemCollectionCategory.FARMING), ItemCollectionCategory.FARMING, Material.GOLD_HOE, 20, player));
        this.set(createCollectionClickable(new CategoryCollectionGUI(ItemCollectionCategory.MINING), ItemCollectionCategory.MINING, Material.STONE_PICKAXE, 21, player));
        this.set(createCollectionClickable(new CategoryCollectionGUI(ItemCollectionCategory.COMBAT), ItemCollectionCategory.COMBAT, Material.STONE_SWORD, 22, player));
        this.set(createCollectionClickable(new CategoryCollectionGUI(ItemCollectionCategory.FORAGING), ItemCollectionCategory.FORAGING, Material.SAPLING, (short) 3, 23, player));
        this.set(createCollectionClickable(new CategoryCollectionGUI(ItemCollectionCategory.FISHING), ItemCollectionCategory.FISHING, Material.FISHING_ROD, 24, player));
    }
}
