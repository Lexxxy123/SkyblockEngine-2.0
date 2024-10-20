/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.hypixel.skyblock.event;

import net.hypixel.skyblock.entity.SEntity;
import net.hypixel.skyblock.event.SkyblockEvent;
import net.hypixel.skyblock.user.User;

public class SkyblockEntityDeathEvent
extends SkyblockEvent {
    private SEntity entity;
    private User killer;

    public SEntity getEntity() {
        return this.entity;
    }

    public User getKiller() {
        return this.killer;
    }

    public SkyblockEntityDeathEvent(SEntity entity, User killer) {
        this.entity = entity;
        this.killer = killer;
    }
}

