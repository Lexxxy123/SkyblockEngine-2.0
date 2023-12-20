package in.godspunky.skyblock.gui;

import in.godspunky.skyblock.user.User;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import in.godspunky.skyblock.collection.ItemCollection;
import in.godspunky.skyblock.collection.ItemCollectionReward;
import in.godspunky.skyblock.collection.ItemCollectionRewards;
import in.godspunky.skyblock.util.SUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemCollectionGUI extends GUI {
    private final ItemCollection collection;

    public ItemCollectionGUI(final ItemCollection collection) {
        super(collection.getName() + " Collection", 54);
        this.collection = collection;
    }

    @Override
    public void onOpen(final GUIOpenEvent e) {
        this.fill(ItemCollectionGUI.BLACK_STAINED_GLASS_PANE);
        final Player player = e.getPlayer();
        final User user = User.getUser(player.getUniqueId());
        final int amount = user.getCollection(this.collection);
        final int tier = this.collection.getTier(amount);
        this.set(4, SUtil.getStack(ChatColor.YELLOW + this.collection.getName() + " " + SUtil.toRomanNumeral(tier), this.collection.getMaterial().getCraftMaterial(), this.collection.getData(), 1, ChatColor.GRAY + "View all your " + this.collection.getName() + " Collection", ChatColor.GRAY + "progress and rewards!", " ", ChatColor.GRAY + "Total Collected: " + ChatColor.YELLOW + SUtil.commaify(amount)));
        this.set(GUIClickableItem.getCloseItem(49));
        this.set(GUIClickableItem.createGUIOpenerItem(new CategoryCollectionGUI(this.collection.getCategory()), player, ChatColor.GREEN + "Go Back", 48, Material.ARROW, (short) 0, ChatColor.GRAY + "To " + this.collection.getCategory().getName() + " Collection"));
        for (int i = 0, slot = 18; i < this.collection.getRewards().size(); ++i, ++slot) {
            final int t = i + 1;
            if (t == 28) {
                break;
            }
            final ItemCollectionRewards rewards = this.collection.getRewards().get(i);
            if (rewards != null) {
                final int finalSlot = slot;
                ChatColor color = ChatColor.RED;
                short data = 14;
                if (amount >= rewards.getRequirement()) {
                    color = ChatColor.GREEN;
                    data = 5;
                }
                if (tier + 1 == t) {
                    color = ChatColor.YELLOW;
                    data = 4;
                }
                final ChatColor finalColor = color;
                final short finalData = data;
                final List<String> lore = new ArrayList<String>(Arrays.asList(" ", SUtil.createProgressText("Progress", amount, rewards.getRequirement()), SUtil.createLineProgressBar(20, ChatColor.DARK_GREEN, amount, rewards.getRequirement()), " "));
                if (rewards.size() != 0) {
                    lore.add(ChatColor.GRAY + "Reward" + ((rewards.size() != 1) ? "s" : "") + ":");
                    for (final ItemCollectionReward reward : rewards) {
                        lore.add(ChatColor.GRAY + " " + reward.toRewardString());
                    }
                    lore.add(" ");
                }
                lore.add(ChatColor.YELLOW + "Click to view rewards!");
                this.set(new GUIClickableItem() {
                    @Override
                    public void run(final InventoryClickEvent e) {
                    }

                    @Override
                    public int getSlot() {
                        return finalSlot;
                    }

                    @Override
                    public ItemStack getItem() {
                        return SUtil.getStack(finalColor + ItemCollectionGUI.this.collection.getName() + " " + SUtil.toRomanNumeral(t), Material.STAINED_GLASS_PANE, finalData, t, lore);
                    }
                });
            }
        }
    }
}
