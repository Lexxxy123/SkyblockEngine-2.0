/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.hypixel.skyblock.command;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import net.hypixel.skyblock.features.ranks.PlayerRank;

@Retention(value=RetentionPolicy.RUNTIME)
public @interface CommandParameters {
    public String description() default "";

    public String usage() default "/<command>";

    public String aliases() default "";

    public PlayerRank permission();
}

