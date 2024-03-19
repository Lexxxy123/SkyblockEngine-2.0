package net.hypixel.skyblock.features.quest.dungeon;

import net.hypixel.skyblock.features.quest.Objective;
import net.hypixel.skyblock.features.quest.QuestLine;

public class Mort extends QuestLine {
    public Mort() {
        super("talk_to_mort","Talk To Mort", new TalkToMort());
    }
    @Override
    protected boolean hasCompletionMessage() {
        return true;
    }
}
