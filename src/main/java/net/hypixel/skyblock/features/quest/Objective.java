/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Sound
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Listener
 *  org.bukkit.plugin.Plugin
 */
package net.hypixel.skyblock.features.quest;

import net.hypixel.skyblock.SkyBlock;
import net.hypixel.skyblock.features.quest.QuestLine;
import net.hypixel.skyblock.user.User;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public class Objective
implements Listener {
    private final String id;
    private final String display;

    public Objective(String id, String display) {
        this.id = id;
        this.display = display;
        Bukkit.getPluginManager().registerEvents((Listener)this, (Plugin)SkyBlock.getPlugin());
    }

    private QuestLine getQuest() {
        return SkyBlock.getPlugin().getQuestLineHandler().getQuest(this);
    }

    public Objective getNext() {
        return this.getQuest().getNext(this);
    }

    public void complete(Player p) {
        User player = User.getUser(p.getUniqueId());
        Objective next = this.getNext();
        player.addCompletedObjectives(this.getId());
        if (this.getNext() == null) {
            this.getQuest().complete(player.toBukkitPlayer());
            return;
        }
        String message = " \n " + ChatColor.GOLD + ChatColor.BOLD + " NEW OBJECTIVE\n" + ChatColor.WHITE + "  " + next.getDisplay() + "\n";
        player.toBukkitPlayer().playSound(player.toBukkitPlayer().getLocation(), Sound.NOTE_PLING, 10.0f, 0.0f);
        player.toBukkitPlayer().sendMessage(message);
        player.toBukkitPlayer().sendMessage(" ");
    }

    public String getSuffix(User player) {
        return "";
    }

    protected boolean isThisObjective(Player player) {
        User skyblockPlayer = User.getUser(player.getUniqueId());
        if (skyblockPlayer.getQuestLine() == null) {
            return false;
        }
        if (skyblockPlayer.getQuestLine().getObjective(skyblockPlayer) == null) {
            return false;
        }
        return skyblockPlayer.getQuestLine().getObjective(skyblockPlayer).getId().equals(this.getId());
    }

    public boolean hasSuffix(User skyblockPlayer) {
        return !this.getSuffix(skyblockPlayer).equals("");
    }

    public String getId() {
        return this.id;
    }

    public String getDisplay() {
        return this.display;
    }
}

