/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 */
package net.hypixel.skyblock.gui;

import java.util.Arrays;
import net.hypixel.skyblock.gui.GUI;
import net.hypixel.skyblock.gui.GUIClickableItem;
import net.hypixel.skyblock.gui.GUIOpenEvent;
import net.hypixel.skyblock.gui.GUIType;
import net.hypixel.skyblock.item.MaterialQuantifiable;
import net.hypixel.skyblock.item.SItem;
import net.hypixel.skyblock.item.SMaterial;
import net.hypixel.skyblock.item.ShapedRecipe;
import net.hypixel.skyblock.util.SUtil;
import net.hypixel.skyblock.util.Sputnik;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class RecipeBookGUI
extends GUI {
    private static final int[] CRAFT_SLOTS = new int[]{10, 11, 12, 19, 20, 21, 28, 29, 30};
    private final SItem targetItem;

    public RecipeBookGUI(SItem sitem) {
        super(ChatColor.stripColor((String)(sitem.getFullName() + " Recipe")), 54);
        this.targetItem = sitem;
    }

    @Override
    public void onOpen(GUIOpenEvent e2) {
        Player player = e2.getPlayer();
        this.fill(BLACK_STAINED_GLASS_PANE);
        this.set(GUIClickableItem.getCloseItem(49));
        this.set(23, SUtil.getStack(Sputnik.trans("&aCrafting Table"), Material.WORKBENCH, (short)0, 1, Arrays.asList(Sputnik.trans("&7Craft this recipe by using a"), Sputnik.trans("&7Crafting Table"))));
        this.set(25, this.targetItem.getStack());
        this.set(GUIClickableItem.createGUIOpenerItem(GUIType.RECIPE_BOOK, player, ChatColor.GREEN + "Go Back", 48, Material.ARROW, ChatColor.GRAY + "To Recipe Book"));
        int z2 = 0;
        for (ShapedRecipe sr : ShapedRecipe.CACHED_RECIPES) {
            if (sr.getResult().getType() != this.targetItem.getType()) continue;
            MaterialQuantifiable[][] lr = sr.toMQ2DArray();
            for (int i2 = 0; i2 < 3; ++i2) {
                for (int j2 = 0; j2 < 3; ++j2) {
                    if (lr[i2][j2] == null) {
                        this.set(CRAFT_SLOTS[z2], null);
                    } else if (lr[i2][j2].getMaterial() != SMaterial.AIR) {
                        ItemStack is = SItem.of(lr[i2][j2].getMaterial()).getStack();
                        is.setAmount(lr[i2][j2].getAmount());
                        this.set(CRAFT_SLOTS[z2], is);
                    } else {
                        this.set(CRAFT_SLOTS[z2], null);
                    }
                    ++z2;
                }
            }
        }
    }
}

