package net.hypixel.skyblock.features.quest;





import lombok.Getter;
import net.hypixel.skyblock.features.quest.dungeon.Dungeon;
import net.hypixel.skyblock.features.region.RegionType;
import net.hypixel.skyblock.user.User;

import java.util.*;

@Getter
public class QuestLineHandler {

    private final HashMap<RegionType, List<QuestLine>> quests = new HashMap<>();

    public QuestLineHandler() {
        register(new RegionType[] { RegionType.MOUNTAIN, RegionType.F6, RegionType.F6 }, new Dungeon());


        for (List<QuestLine> quest : quests.values()) {
            quest.forEach(QuestLine::onEnable);
        }
    }

    public void disable() {
        for (List<QuestLine> quest : quests.values()) {
            quest.forEach(QuestLine::onDisable);
        }
    }

    public void register(RegionType[] locations, QuestLine line) {
        for (RegionType loc : locations) {
            register(loc, line);
        }
    }

    public void register(RegionType location, QuestLine line) {
        if (quests.containsKey(location)) {
            quests.get(location).add(line);
        } else {
            ArrayList<QuestLine> list = new ArrayList<>();
            list.add(line);

            quests.put(location, list);
        }
    }

    public QuestLine getFromPlayer(User player) {
        RegionType loc = player.getRegion(player.toBukkitPlayer()).getType();

        List<QuestLine> lines = quests.get(loc);

        if (lines == null || lines.isEmpty()) {
            return null;
        }

        List<String> completed = player.getCompletedQuests();

        if (lines == null) return null;

        for (QuestLine quest : lines) {
            if (completed.contains(quest.getName())) continue;

            return quest;
        }

        return null;
    }

    public QuestLine getQuest(Objective objective) {
        for (List<QuestLine> lines : quests.values()) {
            for (QuestLine line : lines) {
                for (Objective obj : line.getLine()) {
                    if (obj.getId().equals(objective.getId())) return line;
                }
            }
        }

        return null;
    }

    public List<QuestLine> getQuests() {
        List<QuestLine> quests = new ArrayList<>();

        for (List<QuestLine> questLines : this.quests.values()) {
            for (QuestLine quest : questLines) {
                if (quests.contains(quest)) continue;
                quests.add(quest);
            }
        }

        return quests;
    }
}