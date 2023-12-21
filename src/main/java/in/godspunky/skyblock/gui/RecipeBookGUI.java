package in.godspunky.skyblock.gui;

import in.godspunky.skyblock.item.SItem;
import in.godspunky.skyblock.item.ShapedRecipe;
import in.godspunky.skyblock.util.SUtil;
import in.godspunky.skyblock.util.Sputnik;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import in.godspunky.skyblock.item.MaterialQuantifiable;
import in.godspunky.skyblock.item.SMaterial;

import java.util.Arrays;

public class RecipeBookGUI extends GUI {
    private static final int[] CRAFT_SLOTS;
    private final SItem targetItem;

    public RecipeBookGUI(final SItem sitem) {
        super(ChatColor.stripColor(sitem.getFullName() + " Recipe"), 54);
        this.targetItem = sitem;
    }

    @Override
    public void onOpen(final GUIOpenEvent e) {
        final Player player = e.getPlayer();
        this.fill(BLACK_STAINED_GLASS_PANE);
        this.set(GUIClickableItem.getCloseItem(49));
        this.set(23, SUtil.getStack(Sputnik.trans("&aCrafting Table"), Material.WORKBENCH, (short) 0, 1, Arrays.asList(Sputnik.trans("&7Craft this recipe by using a"), Sputnik.trans("&7Crafting Table"))));
        this.set(25, this.targetItem.getStack());
        this.set(GUIClickableItem.createGUIOpenerItem(GUIType.RECIPE_BOOK, player, ChatColor.GREEN + "Go Back", 48, Material.ARROW, ChatColor.GRAY + "To Recipe Book"));
        int z = 0;
        for (final ShapedRecipe sr : ShapedRecipe.CACHED_RECIPES) {
            if (sr.getResult().getType() == this.targetItem.getType()) {
                final MaterialQuantifiable[][] lr = sr.toMQ2DArray();
                for (int i = 0; i < 3; ++i) {
                    for (int j = 0; j < 3; ++j) {
                        if (lr[i][j] == null) {
                            this.set(RecipeBookGUI.CRAFT_SLOTS[z], null);
                        } else if (lr[i][j].getMaterial() != SMaterial.AIR) {
                            final ItemStack is = SItem.of(lr[i][j].getMaterial()).getStack();
                            is.setAmount(lr[i][j].getAmount());
                            this.set(RecipeBookGUI.CRAFT_SLOTS[z], is);
                        } else {
                            this.set(RecipeBookGUI.CRAFT_SLOTS[z], null);
                        }
                        ++z;
                    }
                }
                break;
            }
        }
    }

    static {
        CRAFT_SLOTS = new int[]{10, 11, 12, 19, 20, 21, 28, 29, 30};
    }
}
