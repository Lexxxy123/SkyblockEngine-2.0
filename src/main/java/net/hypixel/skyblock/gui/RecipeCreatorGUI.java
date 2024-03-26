package net.hypixel.skyblock.gui;

import net.hypixel.skyblock.database.RecipeDatabase;
import net.hypixel.skyblock.item.SItem;
import net.hypixel.skyblock.item.ShapelessRecipe;
import net.hypixel.skyblock.user.User;
import net.hypixel.skyblock.util.Groups;
import net.hypixel.skyblock.util.ItemBuilder;
import net.hypixel.skyblock.util.SUtil;
import net.hypixel.skyblock.util.Sputnik;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class RecipeCreatorGUI extends GUI{

    private static final int[] GRID = new int[]{10, 11, 12, 19, 20, 21, 28, 29, 30};

    public RecipeCreatorGUI() {
        super("Admin Recipe Creator" , 54);

    }

    @Override
    public void onOpen(GUIOpenEvent e) {
        this.fill(BLACK_STAINED_GLASS_PANE, 13, 34);
        this.border(RED_STAINED_GLASS_PANE);
        this.border(BLACK_STAINED_GLASS_PANE, 0, 44);
        set(24 , null);

        Player player = e.getPlayer();

        set(new GUIClickableItem() {
            @Override
            public void run(InventoryClickEvent event) {
              player.closeInventory();

            }

            @Override
            public int getSlot() {
                return 48;
            }

            @Override
            public ItemStack getItem() {
                return new ItemBuilder(Material.BARRIER)
                        .setDisplayName(ChatColor.RED + "close")
                        .toItemStack();
            }
        });

        set(new GUIClickableItem() {
            @Override
            public void run(InventoryClickEvent event) {
                ItemStack result = event.getInventory().getItem(24);
                if (result == null){
                    player.sendMessage(ChatColor.RED + "You should specify result item");
                    return;
                }

                ShapelessRecipe recipe = new ShapelessRecipe(SItem.find(result) , Groups.EXCHANGEABLE_RECIPE_RESULTS.contains(result.getType()));
                recipe.getResult().setAmount(result.getAmount());

                for (ItemStack stack : getGridItems(e.getInventory())){
                    recipe.add(SItem.find(stack).getType() , stack.getAmount());
                }
                // save recipe to database asynchronously, so it doesn't affect performance
                SUtil.runAsync(()-> RecipeDatabase.save(recipe));

                player.sendMessage(ChatColor.GREEN + "Successfully Saved " + recipe.getResult().getDisplayName() + " Recipe");
                User.getUser(player.getUniqueId()).getUnlockedRecipes().add(recipe.getResult().getDisplayName());


            }

            @Override
            public int getSlot() {
                return 50;
            }

            @Override
            public ItemStack getItem() {
                return new ItemBuilder(Material.EMERALD)
                        .setDisplayName(ChatColor.GREEN + "Save")
                        .toItemStack();
            }
        });

    }

    @Override
    public void onClose(InventoryCloseEvent e) {
        if (e.getInventory().getItem(24) != null){
            Sputnik.smartGiveItem(e.getInventory().getItem(24) , (Player) e.getPlayer());
        }
        for (ItemStack stack : getGridItems(e.getInventory())){
            Sputnik.smartGiveItem(stack , (Player) e.getPlayer());
        }
    }

    @Override
    public void onTopClick(InventoryClickEvent e) {
        for (int slot : GRID){
            if (slot == e.getSlot() || e.getSlot() == 24){
                e.setCancelled(false);
                break;
            }
        }
    }


    public List<ItemStack> getGridItems(Inventory inventory){
        ArrayList<ItemStack> stacks = new ArrayList<>();
        for (int slot : GRID){
            if (inventory.getItem(slot) != null){
                stacks.add(inventory.getItem(slot));
            }
        }
        return stacks;
    }

}
