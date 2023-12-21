package in.godspunky.skyblock.gui;

import in.godspunky.skyblock.user.User;
import in.godspunky.skyblock.util.SUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class SlayerCancellationConfirmGUI extends GUI {
    public SlayerCancellationConfirmGUI(final User user) {
        super("Confirm", 27);
        this.set(new GUIClickableItem() {
            @Override
            public void run(final InventoryClickEvent e) {
                user.setSlayerQuest(null);
                e.getWhoClicked().sendMessage(ChatColor.GREEN + "Your Slayer Quest has been cancelled!");
                GUIType.SLAYER.getGUI().open((Player) e.getWhoClicked());
                user.removeAllSlayerBosses();
            }

            @Override
            public ItemStack getItem() {
                return SUtil.getStack(ChatColor.GREEN + "Confirm", Material.STAINED_CLAY, (short) 13, 1, ChatColor.RED + "Clears " + ChatColor.GRAY + "progress towards", ChatColor.GRAY + "the current Slayer Quest to", ChatColor.GRAY + "let you pick a different", ChatColor.GRAY + "one.", "", ChatColor.RED + "" + ChatColor.BOLD + "CANCELLING THE QUEST!", ChatColor.YELLOW + "Click to cancel the quest!");
            }

            @Override
            public int getSlot() {
                return 11;
            }
        });
        this.set(new GUIClickableItem() {
            @Override
            public void run(final InventoryClickEvent e) {
                e.getWhoClicked().closeInventory();
            }

            @Override
            public ItemStack getItem() {
                return SUtil.getStack(ChatColor.RED + "Cancel", Material.STAINED_CLAY, (short) 14, 1);
            }

            @Override
            public int getSlot() {
                return 15;
            }
        });
    }
}
