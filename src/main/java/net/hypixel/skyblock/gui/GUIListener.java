package net.hypixel.skyblock.gui;

import net.hypixel.skyblock.item.SItem;
import net.hypixel.skyblock.listener.PListener;
import net.hypixel.skyblock.user.User;
import net.hypixel.skyblock.util.SignInput;
import net.hypixel.skyblock.util.Sputnik;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GUIListener extends PListener {
    public static final Map<UUID, GUIQueryItem> QUERY_MAP;
    public static final Map<UUID, Boolean> QUERY_MAPPING;


    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e) {
        final GUI gui = GUI.GUI_MAP.get(e.getWhoClicked().getUniqueId());
        if (gui == null) {
            return;
        }
        if (e.getClickedInventory() == e.getView().getTopInventory()) {
            final int slot = e.getSlot();
            gui.onTopClick(e);
            final GUIItem item = gui.get(slot);
            if (item != null) {
                if (!item.canPickup()) {
                    e.setCancelled(true);
                }
                if (item instanceof GUIClickableItem) {
                    final GUIClickableItem clickable = (GUIClickableItem) item;
                    clickable.run(e);
                }
                if (item instanceof GUIQueryItem) {
                    final GUIQueryItem query = (GUIQueryItem) item;
                    final Player player = (Player) e.getWhoClicked();
                    GUIListener.QUERY_MAPPING.put(player.getUniqueId(), true);
                    player.closeInventory();
                    player.sendMessage(ChatColor.GREEN + "Enter your query:");
                    GUIListener.QUERY_MAP.put(player.getUniqueId(), query);
                }
                if (item instanceof GUISignItem) {
                    final GUISignItem query2 = (GUISignItem) item;
                    final Player player = (Player) e.getWhoClicked();
                    SignInput.SIGN_INPUT_QUERY.put(player.getUniqueId(), query2);
                    new SignInput(player, new String[]{"", "^^^^^^^", "Enter amount", "of Bits"}, 15, query2.inti());
                }
            }
        } else {
            gui.onBottomClick(e);
        }
        gui.update(e.getView().getTopInventory());
    }

    @EventHandler
    public void onInventoryClickEven(final InventoryClickEvent event) {
        final GUI gui = GUI.GUI_MAP.get(event.getWhoClicked().getUniqueId());
        if (gui != null && !gui.getClass().equals(CraftingTableGUI.class) && !gui.getClass().equals(ReforgeAnvilGUI.class) && !gui.getClass().equals(AnvilGUI.class) && !gui.getClass().equals(QuiverGUI.class) && !gui.getClass().equals(TrashGUI.class) && !gui.getClass().equals(DungeonsItemConverting.class) && !gui.getClass().equals(DungeonsLootGUI.class) && !gui.getClass().equals(RecipeCreatorGUI.class)) {
            event.setCancelled(true);
        }
        if (gui != null && gui.getClass().equals(QuiverGUI.class)) {
            final ItemStack stack = event.getCurrentItem();
            if (stack != null && stack.getType() != Material.ARROW) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onGUIOpen(final GUIOpenEvent e) {
        final GUI gui = e.getOpened();
        e.getOpened().onOpen(e);
    }


    @EventHandler
    public void onBlockInteract(final PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        final Block block = e.getClickedBlock();
        GUI toOpen = null;

        switch (block.getType()){
            case WORKBENCH:
               toOpen = GUIType.CRAFTING_TABLE.getGUI();
               break;
            case ANVIL:
                toOpen = GUIType.ANVIL.getGUI();
                break;
        }
        if (toOpen != null){
            e.setCancelled(true);
            toOpen.open(e.getPlayer());
        }

    }

    @EventHandler
    public void onPlayerChat(final AsyncPlayerChatEvent e) {
        final Player player = e.getPlayer();
        if (!GUIListener.QUERY_MAP.containsKey(player.getUniqueId())) {
            return;
        }
        e.setCancelled(true);
        final GUIQueryItem item = GUIListener.QUERY_MAP.get(player.getUniqueId());
        player.sendMessage(ChatColor.GOLD + "Querying for: " + e.getMessage());
        final GUI next = item.onQueryFinish(e.getMessage());
        if (next != null) {
            next.open(player);
        }
        GUIListener.QUERY_MAP.remove(player.getUniqueId());
        GUIListener.QUERY_MAPPING.remove(player.getUniqueId());
    }

    @EventHandler
    public void onInventoryClose(final InventoryCloseEvent e) {
        final Inventory inventory = e.getInventory();
        if (!(e.getPlayer() instanceof Player)) {
            return;
        }
        final Player player = (Player) e.getPlayer();
        final GUI gui = GUI.GUI_MAP.get(player.getUniqueId());
        if (gui == null) {
            return;
        }
        if (gui.getClass().equals(CraftingTableGUI.class) || gui.getClass().equals(AnvilGUI.class) || gui.getClass().equals(ReforgeAnvilGUI.class) || gui.getClass().equals(DungeonsItemConverting.class)) {
            if (gui.getClass().equals(AnvilGUI.class)) {
                if (inventory.getItem(29) != null) {
                    if (player.getInventory().firstEmpty() != -1) {
                        player.getInventory().addItem(inventory.getItem(29));
                    } else {
                        player.getWorld().dropItem(player.getLocation(), inventory.getItem(29));
                    }
                }
                if (inventory.getItem(33) != null) {
                    if (player.getInventory().firstEmpty() != -1) {
                        player.getInventory().addItem(inventory.getItem(33));
                    } else {
                        player.getWorld().dropItem(player.getLocation(), inventory.getItem(33));
                    }
                }
                if (inventory.getItem(13).getType().toString() != "BARRIER" && inventory.getItem(29) == null && inventory.getItem(33) == null) {
                    if (player.getInventory().firstEmpty() != -1) {
                        player.getInventory().addItem(inventory.getItem(13));
                    } else {
                        player.getWorld().dropItem(player.getLocation(), inventory.getItem(13));
                    }
                }
            }
            if (gui.getClass().equals(ReforgeAnvilGUI.class)) {
                if (player.getInventory().firstEmpty() != -1) {
                    if (inventory.getItem(13) != null) {
                        player.getInventory().addItem(inventory.getItem(13));
                    }
                } else if (inventory.getItem(13) != null) {
                    player.getWorld().dropItem(player.getLocation(), inventory.getItem(13));
                }
            }
            if (gui.getClass().equals(DungeonsItemConverting.class)) {
                if (inventory.getItem(13) != null) {
                    Sputnik.smartGiveItem(inventory.getItem(13), player);
                } else if (inventory.getItem(31) != null && inventory.getItem(13) == null && inventory.getItem(31).getType() != Material.BARRIER && SItem.find(inventory.getItem(31)) != null && !SItem.find(inventory.getItem(31)).getDataBoolean("dummyItem")) {
                    Sputnik.smartGiveItem(inventory.getItem(31), player);
                }
            }
            if (gui.getClass().equals(CraftingTableGUI.class)) {
                if (inventory.getItem(10) != null) {
                    if (player.getInventory().firstEmpty() != -1) {
                        player.getInventory().addItem(inventory.getItem(10));
                    } else {
                        player.getWorld().dropItem(player.getLocation(), inventory.getItem(10));
                    }
                }
                if (inventory.getItem(11) != null) {
                    if (player.getInventory().firstEmpty() != -1) {
                        player.getInventory().addItem(inventory.getItem(11));
                    } else {
                        player.getWorld().dropItem(player.getLocation(), inventory.getItem(11));
                    }
                }
                if (inventory.getItem(12) != null) {
                    if (player.getInventory().firstEmpty() != -1) {
                        player.getInventory().addItem(inventory.getItem(12));
                    } else {
                        player.getWorld().dropItem(player.getLocation(), inventory.getItem(12));
                    }
                }
                if (inventory.getItem(19) != null) {
                    if (player.getInventory().firstEmpty() != -1) {
                        player.getInventory().addItem(inventory.getItem(19));
                    } else {
                        player.getWorld().dropItem(player.getLocation(), inventory.getItem(19));
                    }
                }
                if (inventory.getItem(20) != null) {
                    if (player.getInventory().firstEmpty() != -1) {
                        player.getInventory().addItem(inventory.getItem(20));
                    } else {
                        player.getWorld().dropItem(player.getLocation(), inventory.getItem(20));
                    }
                }
                if (inventory.getItem(21) != null) {
                    if (player.getInventory().firstEmpty() != -1) {
                        player.getInventory().addItem(inventory.getItem(21));
                    } else {
                        player.getWorld().dropItem(player.getLocation(), inventory.getItem(21));
                    }
                }
                if (inventory.getItem(28) != null) {
                    if (player.getInventory().firstEmpty() != -1) {
                        player.getInventory().addItem(inventory.getItem(28));
                    } else {
                        player.getWorld().dropItem(player.getLocation(), inventory.getItem(28));
                    }
                }
                if (inventory.getItem(29) != null) {
                    if (player.getInventory().firstEmpty() != -1) {
                        player.getInventory().addItem(inventory.getItem(29));
                    } else {
                        player.getWorld().dropItem(player.getLocation(), inventory.getItem(29));
                    }
                }
                if (inventory.getItem(30) != null) {
                    if (player.getInventory().firstEmpty() != -1) {
                        player.getInventory().addItem(inventory.getItem(30));
                    } else {
                        player.getWorld().dropItem(player.getLocation(), inventory.getItem(30));
                    }
                }
                if (inventory.getItem(24).getType().toString() != "BARRIER" && inventory.getItem(10) == null && inventory.getItem(11) == null && inventory.getItem(12) == null && inventory.getItem(19) == null && inventory.getItem(20) == null && inventory.getItem(21) == null && inventory.getItem(28) == null && inventory.getItem(29) == null && inventory.getItem(30) == null) {
                    if (player.getInventory().firstEmpty() != -1) {
                        player.getInventory().addItem(inventory.getItem(24));
                    } else {
                        player.getWorld().dropItem(player.getLocation(), inventory.getItem(24));
                    }
                }
            }
        }
        GUI.GUI_MAP.remove(player.getUniqueId());
        if (GUIListener.QUERY_MAPPING.containsKey(player.getUniqueId()) && GUIListener.QUERY_MAPPING.get(player.getUniqueId())) {
            return;
        }
        if (User.getUser(player.getUniqueId()) != null && User.getUser(player.getUniqueId()).isWaitingForSign()) {
            return;
        }
        gui.onClose(e);
    }

    static {
        QUERY_MAP = new HashMap<UUID, GUIQueryItem>();
        QUERY_MAPPING = new HashMap<UUID, Boolean>();
    }
}
