/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.player.AsyncPlayerChatEvent
 */
package net.hypixel.skyblock.listener;

import net.hypixel.skyblock.features.ranks.PlayerRank;
import net.hypixel.skyblock.listener.PListener;
import net.hypixel.skyblock.user.User;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PlayerChatListener
extends PListener {
    @EventHandler(priority=EventPriority.LOW)
    public void Event(AsyncPlayerChatEvent e) {
        User data = User.getUser(e.getPlayer());
        e.setFormat("%1$s" + (data.rank == PlayerRank.DEFAULT ? ChatColor.GRAY : ChatColor.WHITE) + ": %2$s");
        e.getPlayer().setDisplayName(e.getPlayer().getName());
        PlayerRank rank = data.rank;
        String userTag = rank == PlayerRank.DEFAULT ? rank.getPrefix() + e.getPlayer().getName() : rank.getPrefix() + " " + e.getPlayer().getName();
        if (!e.getPlayer().getDisplayName().equals(ChatColor.translateAlternateColorCodes((char)'&', (String)userTag))) {
            e.getPlayer().setDisplayName(ChatColor.translateAlternateColorCodes((char)'&', (String)userTag));
        }
        if (rank.isAboveOrEqual(PlayerRank.MVPPLUSPLUS)) {
            e.setMessage(e.getMessage().replace("<3", "\u00a7c\u2764").replace("\u2b50", "\u00a76\u272d").replace(":owo:", "\u00a7dO\u00a75w\u00a7dO").replace("o/", "\u00a7d(/\u25d5\u30ee\u25d5)/").replace(":OOF:", "\u00a7c\u00a7lOOF").replace(":123:", "\u00a7a1\u00a7e2\u00a7c3").replace(":shrug:", "\u00a7e\u00af\\(\u30c4)/\u00af").replace(":yes:", "\u00a7a\u2714").replace(":no:", "\u00a7c\u2716").replace(":java:", "\u00a7b\u2668").replace(":arrow:", "\u00a7e\u27a1").replace(":typing:", "\u00a7e\u270e\u00a76..."));
        }
    }
}

