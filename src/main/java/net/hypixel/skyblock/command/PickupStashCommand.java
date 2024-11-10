/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.hypixel.skyblock.command;

import net.hypixel.skyblock.command.CommandParameters;
import net.hypixel.skyblock.command.CommandSource;
import net.hypixel.skyblock.command.SCommand;
import net.hypixel.skyblock.features.ranks.PlayerRank;
import net.hypixel.skyblock.user.User;
import net.hypixel.skyblock.user.UserStash;

@CommandParameters(description="Adds an enchantment from Spec to the specified item.", aliases="pickupstash", permission=PlayerRank.DEFAULT)
public class PickupStashCommand
extends SCommand {
    @Override
    public void run(CommandSource sender, String[] args) {
        if (sender.getPlayer() == null) {
            this.send("&cPoor you, but console can't stash items away, do they even... exist?");
            return;
        }
        User u = User.getUser(sender.getPlayer().getUniqueId());
        if (u != null) {
            UserStash us = UserStash.getStash(u.getUuid());
            us.pickUpStash();
        }
    }
}

