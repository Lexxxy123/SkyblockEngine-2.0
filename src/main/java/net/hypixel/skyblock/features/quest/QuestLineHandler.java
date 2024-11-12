/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.hypixel.skyblock.features.quest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.hypixel.skyblock.features.quest.Objective;
import net.hypixel.skyblock.features.quest.QuestLine;
import net.hypixel.skyblock.features.quest.hub.IntroduceYourselfQuest;
import net.hypixel.skyblock.features.quest.starting.GettingStartedQuest;
import net.hypixel.skyblock.features.region.RegionType;
import net.hypixel.skyblock.user.User;

public class QuestLineHandler {
    private final HashMap<RegionType, List<QuestLine>> quests = new HashMap();

    public QuestLineHandler() {
        this.register(RegionType.PRIVATE_ISLAND, (QuestLine)new GettingStartedQuest());
        this.register(RegionType.VILLAGE, (QuestLine)new IntroduceYourselfQuest());
        for (List<QuestLine> quest : this.quests.values()) {
            quest.forEach(QuestLine::onEnable);
        }
    }

    public void disable() {
        for (List<QuestLine> quest : this.quests.values()) {
            quest.forEach(QuestLine::onDisable);
        }
    }

    public void register(RegionType[] locations, QuestLine line) {
        for (RegionType loc : locations) {
            this.register(loc, line);
        }
    }

    public void register(RegionType location, QuestLine line) {
        if (this.quests.containsKey((Object)location)) {
            this.quests.get((Object)location).add(line);
        } else {
            ArrayList<QuestLine> list = new ArrayList<QuestLine>();
            list.add(line);
            this.quests.put(location, list);
        }
    }

    public QuestLine getFromPlayer(User player) {
        if (player.getRegion() == null) {
            return null;
        }
        RegionType loc = player.getRegion().getType();
        List<QuestLine> lines = this.quests.get((Object)loc);
        if (lines == null || lines.isEmpty()) {
            return null;
        }
        List<String> completed = player.getCompletedQuests();
        if (lines == null) {
            return null;
        }
        for (QuestLine quest : lines) {
            if (completed.contains(quest.getName())) continue;
            return quest;
        }
        return null;
    }

    public QuestLine getQuest(Objective objective) {
        for (List<QuestLine> lines : this.quests.values()) {
            for (QuestLine line : lines) {
                for (Objective obj : line.getLine()) {
                    if (!obj.getId().equals(objective.getId())) continue;
                    return line;
                }
            }
        }
        return null;
    }

    public List<QuestLine> getQuests() {
        ArrayList<QuestLine> quests = new ArrayList<QuestLine>();
        for (List<QuestLine> questLines : this.quests.values()) {
            for (QuestLine quest : questLines) {
                if (quests.contains(quest)) continue;
                quests.add(quest);
            }
        }
        return quests;
    }
}

