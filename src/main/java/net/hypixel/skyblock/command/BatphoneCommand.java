/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.command.ConsoleCommandSender
 *  org.bukkit.entity.Player
 */
package net.hypixel.skyblock.command;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.hypixel.skyblock.command.CommandFailException;
import net.hypixel.skyblock.command.CommandParameters;
import net.hypixel.skyblock.command.CommandSource;
import net.hypixel.skyblock.command.SCommand;
import net.hypixel.skyblock.features.ranks.PlayerRank;
import net.hypixel.skyblock.gui.GUI;
import net.hypixel.skyblock.gui.GUIType;
import net.hypixel.skyblock.item.oddities.MaddoxBatphone;
import net.hypixel.skyblock.util.SUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

@CommandParameters(description="Hidden command for Maddox Batphone.", permission=PlayerRank.DEFAULT)
public class BatphoneCommand
extends SCommand {
    public static final UUID ACCESS_KEY = UUID.randomUUID();
    public static final List<String> KEYS = new ArrayList<String>();

    @Override
    public void run(CommandSource sender, String[] args) {
        if (sender instanceof ConsoleCommandSender) {
            throw new CommandFailException("Console senders cannot use this command!");
        }
        if (!args[0].equals(ACCESS_KEY.toString())) {
            return;
        }
        if (!KEYS.contains(args[1])) {
            throw new CommandFailException(ChatColor.RED + "\u2706 It's too late now, the phone line is off! Call again!");
        }
        Player player = sender.getPlayer();
        MaddoxBatphone.CALL_COOLDOWN.add(player.getUniqueId());
        SUtil.delay(() -> MaddoxBatphone.CALL_COOLDOWN.remove(player.getUniqueId()), 400L);
        GUI gui = GUIType.SLAYER.getGUI();
        gui.open(player);
    }
}

