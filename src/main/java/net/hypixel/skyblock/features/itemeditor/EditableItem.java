/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 */
package net.hypixel.skyblock.features.itemeditor;

import net.hypixel.skyblock.features.dungeons.stats.ItemSerial;
import net.hypixel.skyblock.features.enchantment.EnchantmentType;
import net.hypixel.skyblock.features.reforge.Reforge;
import net.hypixel.skyblock.item.SItem;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class EditableItem {
    SItem sItem;

    public EditableItem(SItem item) {
        this.sItem = item;
    }

    public void addStar(int amount) {
        if (amount == 0) {
            return;
        }
        if (amount > 5) {
            return;
        }
        if (!this.sItem.isStarrable()) {
            return;
        }
        ItemSerial is = ItemSerial.createBlank();
        is.saveTo(this.sItem);
        this.sItem.setStarAmount(this.getStars() + amount);
    }

    public int getStars() {
        return this.sItem.getStar();
    }

    public void recom(boolean setrecom) {
        this.sItem.setRecombobulated(setrecom);
    }

    public void clone(Player player) {
        player.getInventory().addItem(new ItemStack[]{this.sItem.getStack()});
    }

    public void toDungeonItem() {
        this.sItem.setDungeonsItem(true);
    }

    public void reforge(Reforge reforge) {
        this.sItem.setReforge(reforge);
    }

    public void enchant(EnchantmentType type, int level) {
        this.sItem.addEnchantment(type, level);
    }

    public void addhpb() {
        int oldAmount = this.sItem.getHPBs();
        if (oldAmount == 20) {
            return;
        }
        this.sItem.setHPBs(oldAmount + 1);
    }

    public SItem getHandle() {
        return this.sItem;
    }
}

