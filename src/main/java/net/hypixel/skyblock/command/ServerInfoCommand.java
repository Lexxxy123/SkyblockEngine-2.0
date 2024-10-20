/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.hypixel.skyblock.command;

import net.hypixel.skyblock.command.CommandParameters;
import net.hypixel.skyblock.command.CommandSource;
import net.hypixel.skyblock.command.SCommand;
import net.hypixel.skyblock.features.ranks.PlayerRank;

@CommandParameters(description="", aliases="ssi", permission=PlayerRank.ADMIN)
public class ServerInfoCommand
extends SCommand {
    @Override
    public void run(CommandSource sender, String[] args) {
        this.send("&aYou're playing on &cSkyBlock Network &bBeta 2");
        try {
            this.send("&7Server Version: &a1.8.9");
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        this.send("&7Developed by: &cEpicportal &7and &cHamza&7.");
        this.send("&7Server API information: &aMC1_8_R3 &7by &eMojang");
    }
}

