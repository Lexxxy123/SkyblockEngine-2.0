/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.inventory.ItemStack
 */
package net.hypixel.skyblock.item;

import net.hypixel.skyblock.item.SItem;
import net.hypixel.skyblock.item.SMaterial;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class MaterialQuantifiable {
    private SMaterial material;
    private int amount;

    public MaterialQuantifiable(SMaterial material, int amount) {
        this.material = material;
        this.amount = amount;
    }

    public MaterialQuantifiable(SMaterial material) {
        this(material, 1);
    }

    public MaterialQuantifiable setMaterial(SMaterial material) {
        this.material = material;
        return this;
    }

    public MaterialQuantifiable setAmount(int amount) {
        this.amount = amount;
        return this;
    }

    public boolean equals(Object o) {
        if (!(o instanceof MaterialQuantifiable)) {
            return false;
        }
        MaterialQuantifiable material = (MaterialQuantifiable)o;
        return material.material == this.material && material.amount == this.amount;
    }

    public MaterialQuantifiable clone() {
        return new MaterialQuantifiable(this.material, this.amount);
    }

    public String toString() {
        return "MQ{material=" + (null != this.material ? this.material.name() : "?") + ", amount=" + this.amount + "}";
    }

    public static MaterialQuantifiable of(ItemStack stack) {
        if (null == stack || Material.AIR == stack.getType()) {
            return new MaterialQuantifiable(SMaterial.AIR, null != stack ? stack.getAmount() : 1);
        }
        SItem found = SItem.find(stack);
        if (null == found) {
            found = SItem.of(SMaterial.getSpecEquivalent(stack.getType(), stack.getDurability()));
        }
        return new MaterialQuantifiable(found.getType(), stack.getAmount());
    }

    public static MaterialQuantifiable[] of(ItemStack[] stacks) {
        MaterialQuantifiable[] materials = new MaterialQuantifiable[stacks.length];
        for (int i = 0; i < stacks.length; ++i) {
            materials[i] = MaterialQuantifiable.of(stacks[i]);
        }
        return materials;
    }

    public SMaterial getMaterial() {
        return this.material;
    }

    public int getAmount() {
        return this.amount;
    }
}

