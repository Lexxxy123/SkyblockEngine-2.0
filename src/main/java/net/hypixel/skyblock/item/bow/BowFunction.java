/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Arrow
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.entity.EntityShootBowEvent
 */
package net.hypixel.skyblock.item.bow;

import com.google.common.util.concurrent.AtomicDouble;
import net.hypixel.skyblock.item.MaterialFunction;
import net.hypixel.skyblock.item.SItem;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityShootBowEvent;

public interface BowFunction
extends MaterialFunction {
    default public void onBowShoot(SItem bow, EntityShootBowEvent e) {
    }

    default public void onBowHit(Entity hit, Player shooter, Arrow arrow, SItem weapon, AtomicDouble damage) {
    }
}

