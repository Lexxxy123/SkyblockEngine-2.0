/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 */
package net.hypixel.skyblock.gui;

import java.util.UUID;
import net.hypixel.skyblock.gui.GUI;
import net.hypixel.skyblock.gui.GUIClickableItem;
import org.bukkit.entity.Player;

public interface GUISignItem
extends GUIClickableItem {
    public GUI onSignClose(String var1, Player var2);

    public UUID inti();
}

