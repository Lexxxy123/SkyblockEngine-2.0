/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraft.server.v1_8_R3.Block
 *  net.minecraft.server.v1_8_R3.Material
 *  org.bukkit.Color
 */
package net.hypixel.skyblock.features.minion;

import java.util.List;
import net.minecraft.server.v1_8_R3.Block;
import net.minecraft.server.v1_8_R3.Material;
import org.bukkit.Color;

public abstract class SkyBlockMinion {
    public abstract List<MinionTier> getTiers();

    public abstract Color getBootColour();

    public abstract Color getLeggingsColour();

    public abstract Color getChestplateColour();

    public abstract List<MinionExpectations> getExpectations();

    public class MinionExpectations {
        private int yLevel;
        private Block[] materials;

        public MinionExpectations(int yLevel, Block ... materials) {
            this.yLevel = yLevel;
            this.materials = materials;
        }

        public int getYLevel() {
            return this.yLevel;
        }

        public Block[] getMaterials() {
            return this.materials;
        }
    }

    public class MinionTier {
        private int tier;
        private int timeBetweenActions;
        private int storage;
        private String texture;
        private Material heldItem;
        private boolean craftable;

        public MinionTier(int tier, int timeBetweenActions, int storage, String texture, Material heldItem, boolean craftable) {
            this.tier = tier;
            this.timeBetweenActions = timeBetweenActions;
            this.storage = storage;
            this.texture = texture;
            this.heldItem = heldItem;
            this.craftable = craftable;
        }

        public int getTier() {
            return this.tier;
        }

        public int getTimeBetweenActions() {
            return this.timeBetweenActions;
        }

        public int getStorage() {
            return this.storage;
        }

        public String getTexture() {
            return this.texture;
        }

        public Material getHeldItem() {
            return this.heldItem;
        }

        public boolean isCraftable() {
            return this.craftable;
        }

        public int getSlots() {
            return this.storage / 64;
        }
    }
}

