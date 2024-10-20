/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.hypixel.skyblock.command;

import net.hypixel.skyblock.command.CommandParameters;
import net.hypixel.skyblock.command.CommandSource;
import net.hypixel.skyblock.command.SCommand;
import net.hypixel.skyblock.features.ranks.PlayerRank;
import net.hypixel.skyblock.gui.menu.Items.HexGUI;

@CommandParameters(permission=PlayerRank.DEFAULT, aliases="hex")
public class HexCommand
extends SCommand {
    @Override
    public void run(CommandSource sender, String[] args) {
        new HexGUI(sender.getPlayer()).open(sender.getPlayer());
    }
}

