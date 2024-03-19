
package net.hypixel.skyblock.gui.menu.Items;

import net.hypixel.skyblock.SkyBlock;
import net.hypixel.skyblock.features.enchantment.Enchantment;
import net.hypixel.skyblock.features.enchantment.EnchantmentType;
import net.hypixel.skyblock.gui.GUI;
import net.hypixel.skyblock.gui.GUIClickableItem;
import net.hypixel.skyblock.gui.GUIItem;
import net.hypixel.skyblock.item.SItem;
import net.hypixel.skyblock.item.SMaterial;
import net.hypixel.skyblock.item.SpecificItemType;
import net.hypixel.skyblock.util.PaginationList;
import net.hypixel.skyblock.util.SUtil;
import net.hypixel.skyblock.util.Sputnik;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class HexModifiersGUI extends GUI {
    SItem upgradeableItem;

    public HexModifiersGUI(SItem item) {
        super("The Hex -> Modifiers", 54);
        fill(BLACK_STAINED_GLASS_PANE);

        //Recombobulator
        set(new GUIClickableItem() {
            @Override
            public void run(InventoryClickEvent e) {
                upgradeableItem = item;
                item.setRecombobulated(true);

                Player player = (Player) e.getWhoClicked();
                player.playSound(player.getLocation(), Sound.ORB_PICKUP, 10, 2);

                e.getWhoClicked().sendMessage(ChatColor.translateAlternateColorCodes('&', "&aYou recombobulated your " + item.getFullName() + "!"));
            }

            @Override
            public int getSlot() {
                return 23;
            }

            @Override
            public ItemStack getItem() {
                return SItem.of(SMaterial.RECOMBOBULATOR_3000).getStack();
            }
        });

        //Close ( with rizz )
        set(new GUIClickableItem() {
            @Override
            public void run(InventoryClickEvent e) {
                e.getWhoClicked().sendMessage(ChatColor.translateAlternateColorCodes('&', "&d&lSUCCESS! &r&dYou modified your " + item.getFullName() + "!"));

                Player p = (Player) e.getWhoClicked();
                p.playSound(p.getLocation(), Sound.ANVIL_USE, 10, 1);

                new HexGUI(p.getPlayer(), item).open(p);
            }

            @Override
            public int getSlot() {
                return 28;
            }

            @Override
            public ItemStack getItem() {
                return SUtil.getStack("&aApply Modifiers", Material.ANVIL, (short) 0, 1,
                        Sputnik.trans5("&7Apply miscellaneous item",
                                "&7modifiers like the",
                                "&6Recombobulator 3000&7,",
                                "&5Wither Scrolls&7, and &cMaster",
                                "&cStars&7!")
                );
            }
        });

        set(GUIClickableItem.getCloseItem(49));
        set(19, item.getStack());

    }

}