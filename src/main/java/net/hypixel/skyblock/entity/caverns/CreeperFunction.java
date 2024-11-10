/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.hypixel.skyblock.entity.caverns;

import net.hypixel.skyblock.entity.EntityFunction;
import net.hypixel.skyblock.entity.SEntity;
import net.hypixel.skyblock.event.CreeperIgniteEvent;

public interface CreeperFunction
extends EntityFunction {
    default public void onCreeperIgnite(CreeperIgniteEvent e, SEntity sEntity) {
    }
}

