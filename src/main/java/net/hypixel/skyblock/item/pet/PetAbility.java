/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Entity
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 */
package net.hypixel.skyblock.item.pet;

import com.google.common.util.concurrent.AtomicDouble;
import java.util.List;
import net.hypixel.skyblock.item.SItem;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public interface PetAbility {
    public String getName();

    public List<String> getDescription(SItem var1);

    default public void onHurt(EntityDamageByEntityEvent e, Entity damager) {
    }

    default public void onDamage(EntityDamageByEntityEvent e) {
    }

    default public void onZealotAttempt(AtomicDouble chance) {
    }
}

