package vn.giakhanhvn.skysim.gui;

import vn.giakhanhvn.skysim.util.SUtil;
import org.bukkit.Material;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.event.inventory.InventoryClickEvent;
import vn.giakhanhvn.skysim.slayer.SlayerBossType;

public class SlayerConfirmGUI extends GUI
{
    public SlayerConfirmGUI(final SlayerBossType type, final Runnable onConfirm) {
        super("Confirm", 27);
        this.set(new GUIClickableItem() {
            @Override
            public void run(final InventoryClickEvent e) {
                onConfirm.run();
                e.getWhoClicked().closeInventory();
            }
            
            @Override
            public ItemStack getItem() {
                return SUtil.getStack(ChatColor.GREEN + "Confirm", Material.STAINED_CLAY, (short)13, 1, ChatColor.GRAY + "Kill " + type.getType().getPluralName() + " to spawn the", ChatColor.GRAY + "boss!", "", ChatColor.YELLOW + "Click to start quest!");
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
                return SUtil.getStack(ChatColor.RED + "Cancel", Material.STAINED_CLAY, (short)14, 1, new String[0]);
            }
            
            @Override
            public int getSlot() {
                return 15;
            }
        });
    }
}
