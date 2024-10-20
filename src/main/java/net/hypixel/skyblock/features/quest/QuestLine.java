/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.Sound
 *  org.bukkit.entity.Player
 */
package net.hypixel.skyblock.features.quest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import net.hypixel.skyblock.features.quest.Objective;
import net.hypixel.skyblock.user.User;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public abstract class QuestLine {
    protected final List<Objective> line;
    private final String name;
    private final String display;

    public QuestLine(String name, String display, Objective ... objectives) {
        this.line = Arrays.asList(objectives);
        this.display = display;
        this.name = name;
    }

    public Objective getObjective(User skyblockPlayer) {
        List<String> completed = skyblockPlayer.getCompletedObjectives();
        for (Objective obj : this.line) {
            if (completed.contains(obj.getId())) continue;
            return obj;
        }
        return new Objective("", "");
    }

    public Objective getNext(Objective obj) {
        try {
            return this.line.get(this.line.indexOf(obj) + 1);
        } catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException) {
            return null;
        }
    }

    public void complete(Player player) {
        User skyblockPlayer = User.getUser(player.getUniqueId());
        skyblockPlayer.addCompletedQuest(this.getName());
        if (!this.hasCompletionMessage()) {
            return;
        }
        String message = " \n " + ChatColor.GOLD + ChatColor.BOLD + " QUEST COMPLETE\n" + ChatColor.WHITE + "  " + this.display + "\n";
        player.playSound(player.getLocation(), Sound.NOTE_PLING, 10.0f, 0.0f);
        player.sendMessage(message);
        player.sendMessage(" ");
        for (Objective objective : this.line) {
            player.sendMessage(ChatColor.GREEN + "   \u2713 " + ChatColor.WHITE + objective.getDisplay());
        }
        if (this.getRewards().size() > 0) {
            player.sendMessage(" ");
            player.sendMessage(ChatColor.GREEN + "  " + ChatColor.BOLD + "REWARD");
            for (String reward : this.getRewards()) {
                player.sendMessage("   " + ChatColor.translateAlternateColorCodes((char)'&', (String)reward));
            }
        }
        player.sendMessage("  ");
        this.reward(User.getUser(player.getUniqueId()));
    }

    protected List<String> getRewards() {
        return Collections.emptyList();
    }

    protected void reward(User player) {
    }

    protected boolean hasCompletionMessage() {
        return false;
    }

    public void onDisable() {
    }

    public void onEnable() {
    }

    public List<Objective> getLine() {
        return this.line;
    }

    public String getName() {
        return this.name;
    }

    public String getDisplay() {
        return this.display;
    }
}

