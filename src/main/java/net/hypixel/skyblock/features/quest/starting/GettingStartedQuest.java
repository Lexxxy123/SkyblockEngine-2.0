/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.hypixel.skyblock.features.quest.starting;

import net.hypixel.skyblock.features.quest.QuestLine;
import net.hypixel.skyblock.features.quest.starting.BreakLogObjective;
import net.hypixel.skyblock.features.quest.starting.PickaxeObjective;
import net.hypixel.skyblock.features.quest.starting.TeleporterObjective;
import net.hypixel.skyblock.features.quest.starting.WorkbenchObjective;

public class GettingStartedQuest
extends QuestLine {
    public GettingStartedQuest() {
        super("getting_started", "Getting Started", new BreakLogObjective(), new WorkbenchObjective(), new PickaxeObjective(), new TeleporterObjective());
    }
}

