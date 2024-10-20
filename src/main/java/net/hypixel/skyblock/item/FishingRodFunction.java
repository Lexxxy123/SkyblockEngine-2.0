/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.event.player.PlayerFishEvent
 */
package net.hypixel.skyblock.item;

import net.hypixel.skyblock.item.MaterialFunction;
import net.hypixel.skyblock.item.SItem;
import org.bukkit.event.player.PlayerFishEvent;

public interface FishingRodFunction
extends MaterialFunction {
    public void onFish(SItem var1, PlayerFishEvent var2);
}

