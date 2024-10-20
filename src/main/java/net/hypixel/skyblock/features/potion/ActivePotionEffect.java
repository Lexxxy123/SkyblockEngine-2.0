/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.hypixel.skyblock.features.potion;

import net.hypixel.skyblock.features.potion.PotionEffect;
import net.hypixel.skyblock.util.SUtil;

public class ActivePotionEffect {
    private final PotionEffect effect;
    private long remaining;

    public ActivePotionEffect(PotionEffect effect, long remaining) {
        this.effect = effect;
        this.remaining = remaining;
    }

    public String getRemainingDisplay() {
        return SUtil.getFormattedTime(this.remaining);
    }

    public PotionEffect getEffect() {
        return this.effect;
    }

    public long getRemaining() {
        return this.remaining;
    }

    public void setRemaining(long remaining) {
        this.remaining = remaining;
    }
}

