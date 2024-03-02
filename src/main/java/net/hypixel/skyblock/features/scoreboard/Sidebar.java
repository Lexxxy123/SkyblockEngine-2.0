package net.hypixel.skyblock.features.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.ArrayList;
import java.util.List;

public class Sidebar {
    private static ScoreboardManager manager;
    private final String name;
    private final String identifier;
    private final Scoreboard board;
    private final Objective obj;
    private final List<Score> scores;

    public Sidebar(final String name, final String identifier) {
        this.name = name;
        this.identifier = identifier;
        this.board = Sidebar.manager.getNewScoreboard();
        this.obj = this.board.registerNewObjective(identifier, "");
        this.scores = new ArrayList<Score>();
        this.obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        this.obj.setDisplayName(name);
    }

    public void add(final String s) {
        final Score score = this.obj.getScore(s);
        this.scores.add(0, score);
    }

    public void apply(final Player player) {
        for (int i = 0; i < this.scores.size(); ++i) {
            this.scores.get(i).setScore(i);
        }
        player.setScoreboard(this.board);
    }

    static {
        Sidebar.manager = Bukkit.getScoreboardManager();
    }
}
