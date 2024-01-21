package in.godspunky.skyblock.objectives.starting;


import in.godspunky.skyblock.objectives.QuestLine;
import in.godspunky.skyblock.user.User;
import org.bukkit.ChatColor;

import java.util.Collections;
import java.util.List;

public class GettingStartedQuest extends QuestLine {
    public GettingStartedQuest() {
        super("getting_started", "Getting Started", new BreakLogObjective(), new WorkbenchObjective(), new PickaxeObjective());
    }

    @Override
    protected boolean hasCompletionMessage() {
        return true;
    }

    @Override
    protected List<String> getRewards() {
        return Collections.singletonList(ChatColor.DARK_GRAY + "+" + ChatColor.GOLD + 50 + " &7Coins");
    }

    @Override
    protected void reward(User player) {
        player.addCoins(50);
    }
}
