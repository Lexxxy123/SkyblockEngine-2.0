/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.hypixel.skyblock.command;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.hypixel.skyblock.command.CommandParameters;
import net.hypixel.skyblock.command.CommandSource;
import net.hypixel.skyblock.command.SCommand;
import net.hypixel.skyblock.features.ranks.PlayerRank;

@CommandParameters(description="Modify your coin amount.", usage="", aliases="ss", permission=PlayerRank.ADMIN)
public class ServerCommand
extends SCommand {
    public Map<UUID, List<String>> servers = new HashMap<UUID, List<String>>();

    @Override
    public void run(CommandSource sender, String[] args) {
    }
}

