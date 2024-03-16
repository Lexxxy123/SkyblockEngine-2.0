package net.hypixel.skyblock.features.quest;



import lombok.Getter;
import net.hypixel.skyblock.features.quest.Objective;
import net.hypixel.skyblock.user.User;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.*;

@Getter
public abstract class QuestLine {

    protected final List<Objective> line;
    private final String name;
    private final String display;

    public QuestLine(String name, String display, Objective... objectives) {
        this.line = Arrays.asList(objectives);
        this.display = display;
        this.name = name;
    }

    public Objective getObjective(User skyblockPlayer) {
        List<String> completed = skyblockPlayer.getCompletedObjectives();

        for (Objective obj : line) {
            if (completed.contains(obj.getId())) continue;

            return obj;
        }

        return new Objective("", "");
    }

    public Objective getNext(Objective obj) {
        try {
            return line.get(line.indexOf(obj) + 1);
        } catch (ArrayIndexOutOfBoundsException ignored) { }

        return null;
    }

    public void complete(Player player) {
        User skyblockPlayer = User.getUser(player.getUniqueId());;


        skyblockPlayer.addCompletedQuest(getName());

        if (!hasCompletionMessage()) return;

        String message = " \n " + ChatColor.GOLD + ChatColor.BOLD + " QUEST COMPLETE" + "\n" +
                ChatColor.WHITE + "  " + display + "\n";

        player.playSound(player.getLocation(), Sound.NOTE_PLING, 10, 0);
        player.sendMessage(message);
        player.sendMessage(" ");

        for (Objective objective : line) {
            player.sendMessage(ChatColor.GREEN + "   ✓ " + ChatColor.WHITE + objective.getDisplay());
        }

        if (getRewards().size() > 0) {
            player.sendMessage(" ");
            player.sendMessage(ChatColor.GREEN + "  " + ChatColor.BOLD + "REWARD");

            for (String reward : getRewards()) {
                player.sendMessage("   " + ChatColor.translateAlternateColorCodes('&', reward));
            }
        }

        player.sendMessage("  ");

        reward(User.getUser(player.getUniqueId()));
    }

    protected List<String> getRewards() {
        return Collections.emptyList();
    }

    protected void reward(User player) { }

    protected boolean hasCompletionMessage() {
        return false;
    }

    public void onDisable() {}
    public void onEnable() {}

}