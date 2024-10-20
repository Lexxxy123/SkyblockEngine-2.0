/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.hypixel.skyblock.features.requirement;

import net.hypixel.skyblock.user.User;

public interface AbstractRequirement {
    public boolean hasRequirement(User var1);

    public String getMessage();
}

