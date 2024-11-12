/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.command.TabCompleter
 *  org.bukkit.entity.Player
 */
package net.hypixel.skyblock.command;

import java.util.Arrays;
import java.util.List;
import net.hypixel.skyblock.SkyBlock;
import net.hypixel.skyblock.command.CommandArgumentException;
import net.hypixel.skyblock.command.CommandFailException;
import net.hypixel.skyblock.command.CommandParameters;
import net.hypixel.skyblock.command.CommandPermissionException;
import net.hypixel.skyblock.command.CommandSource;
import net.hypixel.skyblock.command.PlayerNotFoundException;
import net.hypixel.skyblock.features.ranks.PlayerRank;
import net.hypixel.skyblock.user.User;
import net.hypixel.skyblock.util.SUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public abstract class SCommand
implements CommandExecutor,
TabCompleter {
    public static final String COMMAND_SUFFIX = "Command";
    protected static final SkyBlock plugin = SkyBlock.getPlugin();
    private CommandParameters params = this.getClass().getAnnotation(CommandParameters.class);
    private String name = this.getClass().getSimpleName().replace("Command", "").toLowerCase();
    private String description = this.params.description();
    private String usage = this.params.usage();
    private List<String> aliases = Arrays.asList(this.params.aliases().split(","));
    private PlayerRank permission = this.params.permission();
    private SECommand command = new SECommand(this);
    private CommandSource sender;

    protected SCommand() {
    }

    public abstract void run(CommandSource var1, String[] var2);

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return false;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }

    public void register() {
        SCommand.plugin.commandMap.register("", (Command)this.command);
    }

    public void send(String message, CommandSource sender) {
        sender.send(ChatColor.GRAY + message);
    }

    public void send(String message) {
        this.send(message, this.sender);
    }

    public void send(String message, Player player) {
        player.sendMessage(ChatColor.GRAY + message);
    }

    public void checkPermission(PlayerRank requiredRank) {
        PlayerRank playerRank = this.getPlayerRank(this.sender.getPlayer());
        if (!playerRank.isAboveOrEqual(requiredRank)) {
            throw new CommandPermissionException(requiredRank);
        }
    }

    public Player getNonNullPlayer(String name) {
        Player player = Bukkit.getPlayer((String)name);
        if (player == null) {
            throw new PlayerNotFoundException();
        }
        return player;
    }

    public PlayerRank getPlayerRank(Player player) {
        User godspunkyPlayer = User.getUser(player);
        if (godspunkyPlayer != null) {
            return godspunkyPlayer.rank;
        }
        return PlayerRank.DEFAULT;
    }

    private static class SECommand
    extends Command {
        private final SCommand sc;

        public SECommand(SCommand xc) {
            super(xc.name, xc.description, xc.usage, xc.aliases);
            this.setPermissionMessage(ChatColor.RED + "No permission. You need \"" + (Object)((Object)xc.permission) + "\"");
            this.sc = xc;
        }

        public boolean execute(CommandSender sender, String commandLabel, String[] args) {
            this.sc.sender = new CommandSource(sender);
            try {
                this.sc.checkPermission(this.sc.permission);
                this.sc.run(this.sc.sender, args);
                return true;
            } catch (CommandFailException | CommandPermissionException | PlayerNotFoundException ex) {
                sender.sendMessage(ex.getMessage());
                return true;
            } catch (CommandArgumentException ex) {
                return false;
            } catch (Exception ex) {
                sender.sendMessage(ChatColor.RED + "Error: " + ex.getMessage());
                ex.printStackTrace();
                return true;
            }
        }

        public List<String> tabComplete(CommandSender sender, String alias, String[] args) {
            List<String> tc = this.sc.onTabComplete(sender, this, alias, args);
            if (tc != null) {
                return tc;
            }
            return SUtil.getPlayerNameList();
        }
    }
}

