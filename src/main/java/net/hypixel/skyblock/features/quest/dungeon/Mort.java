/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.hypixel.skyblock.features.quest.dungeon;

import net.hypixel.skyblock.features.quest.QuestLine;
import net.hypixel.skyblock.features.quest.dungeon.TalkToMort;

public class Mort
extends QuestLine {
    public Mort() {
        super("talk_to_mort", "Talk To Mort", new TalkToMort());
    }

    @Override
    protected boolean hasCompletionMessage() {
        return true;
    }
}

