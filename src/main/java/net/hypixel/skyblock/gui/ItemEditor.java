package net.hypixel.skyblock.gui;


import net.hypixel.skyblock.features.itemeditor.EditableItem;
import net.hypixel.skyblock.item.SItem;
import net.hypixel.skyblock.item.SMaterial;
import net.hypixel.skyblock.user.User;
import net.hypixel.skyblock.util.SUtil;
import net.hypixel.skyblock.util.Sputnik;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class ItemEditor extends GUI{
    public ItemEditor() {
        super("Item Editor (incomplete)", 45);
    }

    @Override
    public void onOpen(GUIOpenEvent e) {


        if (e.getPlayer().getItemInHand() == null){
            e.getPlayer().sendMessage(ChatColor.RED + "Please hold a item!");
            e.getPlayer().closeInventory();
            return;
        }
        this.border(BLACK_STAINED_GLASS_PANE);
        final Player player = e.getPlayer();
        this.set(GUIClickableItem.getCloseItem(40));
        final User user = User.getUser(player.getUniqueId());

        EditableItem editableItem = new EditableItem(SItem.find(player.getItemInHand()));

        this.set(new GUIClickableItem() {
            @Override
            public void run(InventoryClickEvent e) {
                GUIType.REFORGE_ANVIL.getGUI().open(player);
            }

            @Override
            public int getSlot() {
                return 13;
            }

            @Override
            public ItemStack getItem() {
                return SUtil.getStack(ChatColor.AQUA+"Reforge", Material.ANVIL, (short) 0, 1, ChatColor.GRAY+"Reforge your items.");
            }
        });

        this.set(new GUIClickableItem() {
            @Override
            public void run(InventoryClickEvent e) {
                //GUIType.ENCHANT.getGUI().open(player);
                player.sendMessage(ChatColor.RED + "/enc <enchant type> <level>");
            }

            @Override
            public int getSlot() {
                return 28;
            }

            @Override
            public ItemStack getItem() {
                return SUtil.getStack(ChatColor.YELLOW+"Enchant", Material.ENCHANTMENT_TABLE, (short) 0, 1, ChatColor.GRAY+"Enchant your items.");
            }
        });


        this.set(new GUIClickableItem() {
            @Override
            public void run(InventoryClickEvent e) {
                editableItem.recom(true);
            }

            @Override
            public int getSlot() {
                return 34;
            }

            @Override
            public ItemStack getItem() {
                return SItem.of(SMaterial.RECOMBOBULATOR_3000).getStack();
            }
        });

        this.set(new GUIClickableItem() {
            @Override
            public void run(InventoryClickEvent e) {

            }

            @Override
            public int getSlot() {
                return 31;
            }

            @Override
            public ItemStack getItem() {
                return SUtil.getSkullURLStack(ChatColor.LIGHT_PURPLE+"Change Rarity", "1035c528036b384c53c9c8a1a125685e16bfb369c197cc9f03dfa3b835b1aa55",1, ChatColor.GRAY+"Change rarity of your items.");
            }
        });

        this.set(new GUIClickableItem() {
            @Override
            public void run(InventoryClickEvent e) {
                if(editableItem.getHandle().getHPBs() == 20){
                    player.sendMessage(Sputnik.trans("&cYou have already reached the Maximum limit!"));
                }
                editableItem.addhpb();
            }

            @Override
            public int getSlot() {
                return 10;
            }

            @Override
            public ItemStack getItem() {
                return SUtil.getStack(ChatColor.GOLD+"Add Hot Potato Book", Material.BOOK, (short) 0,1, ChatColor.GRAY+"Add hot potato book to item.");
            }
        });



    }
}
