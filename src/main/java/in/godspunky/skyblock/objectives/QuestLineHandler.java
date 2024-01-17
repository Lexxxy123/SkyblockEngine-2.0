package in.godspunky.skyblock.objectives;




import in.godspunky.skyblock.objectives.starting.GettingStartedQuest;
import in.godspunky.skyblock.region.Region;
import in.godspunky.skyblock.region.RegionType;
import in.godspunky.skyblock.user.Profile;
import in.godspunky.skyblock.user.ProfileDatabase;
import in.godspunky.skyblock.user.User;
import lombok.Getter;

import java.util.*;

@Getter
public class QuestLineHandler {

    private final HashMap<RegionType, List<QuestLine>> quests = new HashMap<>();

    public QuestLineHandler() {
        register(RegionType.PRIVATE_ISLAND, new GettingStartedQuest());

        for (List<QuestLine> quest : quests.values()) {
            quest.forEach(QuestLine::onEnable);
        }
    }

    public void disable() {
        for (List<QuestLine> quest : quests.values()) {
            quest.forEach(QuestLine::onDisable);
        }
    }

    /*SUtil.runAsync(() -> {
            Object completedObjectivesObj = get(base, "completedObjectives", new HashMap<>());
            if (completedObjectivesObj instanceof List<?>) {
                profile.setCompletedObjectives((List<String>) completedObjectivesObj);
                owner.setCompletedObjectives((List<String>) completedObjectivesObj);
            } else {
                SLog.info("Quest aint working Redo!");
            }
        });

        SUtil.runAsync(() -> {
            Object completedQuestsObj = get(base, "completedQuests", new HashMap<>());
            if (completedQuestsObj instanceof List<?>) {
                profile.setCompletedQuests((List<String>) completedQuestsObj);
                owner.setCompletedQuests((List<String>) completedQuestsObj);
            } else {
                SLog.info("Quest aint working Redo!");
            }
        });*/

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
        RegionType loc = player.getRegion().getType();
        Profile profile = player.selectedProfile;

        List<QuestLine> lines = quests.get(loc);

        if (lines == null || lines.isEmpty()) {
            return null;
        }

        List<String> completed = player.getCompletedQuests();

        for (QuestLine quest : lines) {
            if (completed.contains(quest.getName())) {
                continue;
            }

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
