/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 */
package net.hypixel.skyblock.event;

import net.hypixel.skyblock.event.SkyblockEvent;
import net.hypixel.skyblock.item.Recipe;
import org.bukkit.entity.Player;

public class SkyBlockCraftEvent
extends SkyblockEvent {
    private final Recipe recipe;
    private final Player player;

    public Recipe getRecipe() {
        return this.recipe;
    }

    public Player getPlayer() {
        return this.player;
    }

    public SkyBlockCraftEvent(Recipe recipe, Player player) {
        this.recipe = recipe;
        this.player = player;
    }
}

