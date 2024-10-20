/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 */
package net.hypixel.skyblock.command;

import org.bukkit.ChatColor;

public class PlayerNotFoundException
extends RuntimeException {
    public PlayerNotFoundException() {
        super(ChatColor.GRAY + "Player not found!");
    }
}

