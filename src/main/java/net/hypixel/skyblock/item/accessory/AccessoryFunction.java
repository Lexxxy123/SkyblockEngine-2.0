/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 */
package net.hypixel.skyblock.item.accessory;

import com.google.common.util.concurrent.AtomicDouble;
import net.hypixel.skyblock.item.MaterialFunction;
import net.hypixel.skyblock.item.SItem;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public interface AccessoryFunction
extends MaterialFunction {
    default public void onDamageInInventory(SItem weapon, Player damager, Entity damaged, SItem accessory, AtomicDouble damage) {
    }

    default public void update(SItem instance, Player player, int accessorySlot) {
    }
}

