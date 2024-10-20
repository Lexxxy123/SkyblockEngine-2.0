/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.command.ConsoleCommandSender
 */
package net.hypixel.skyblock.command;

import net.hypixel.skyblock.command.CommandArgumentException;
import net.hypixel.skyblock.command.CommandFailException;
import net.hypixel.skyblock.command.CommandParameters;
import net.hypixel.skyblock.command.CommandSource;
import net.hypixel.skyblock.command.SCommand;
import net.hypixel.skyblock.features.collection.ItemCollection;
import net.hypixel.skyblock.features.ranks.PlayerRank;
import net.hypixel.skyblock.user.User;
import net.hypixel.skyblock.util.SUtil;
import org.bukkit.command.ConsoleCommandSender;

@CommandParameters(description="Modify your collections.", permission=PlayerRank.ADMIN)
public class CollectionsCommand
extends SCommand {
    @Override
    public void run(CommandSource sender, String[] args) {
        String lowerCase;
        if (args.length != 3) {
            throw new CommandArgumentException();
        }
        if (sender instanceof ConsoleCommandSender) {
            throw new CommandFailException("Console senders cannot use this command!");
        }
        ItemCollection collection = ItemCollection.getByIdentifier(args[0]);
        if (collection == null) {
            throw new CommandFailException("Could not find the specified collection!");
        }
        User user = sender.getUser();
        int amount = Integer.parseInt(args[2]);
        switch (lowerCase = args[1].toLowerCase()) {
            case "add": {
                user.addToCollection(collection, amount);
                this.send("You have added " + SUtil.commaify(amount) + " to your " + collection.getName() + " collection.");
                return;
            }
            case "subtract": 
            case "sub": {
                user.setCollection(collection, user.getCollection(collection) - amount);
                this.send("You have subtracted " + SUtil.commaify(amount) + " from your " + collection.getName() + " collection.");
                return;
            }
            case "set": {
                user.setCollection(collection, amount);
                this.send("You have set your " + collection.getName() + " collection to " + SUtil.commaify(amount) + ".");
                return;
            }
        }
        throw new CommandArgumentException();
    }
}

