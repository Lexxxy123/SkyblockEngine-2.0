/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 */
package net.hypixel.skyblock.command;

import net.hypixel.skyblock.features.ranks.PlayerRank;
import org.bukkit.ChatColor;

public class CommandPermissionException
extends RuntimeException {
    public CommandPermissionException(PlayerRank permission) {
        super(ChatColor.RED + "You need " + (Object)((Object)permission) + " rank to use this command");
    }
}

