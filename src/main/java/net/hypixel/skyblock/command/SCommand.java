package net.hypixel.skyblock.command;

import net.hypixel.skyblock.SkyBlock;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import net.hypixel.skyblock.util.SUtil;
import net.hypixel.skyblock.util.Sputnik;

import java.util.Arrays;
import java.util.List;

public abstract class SCommand implements CommandExecutor, TabCompleter {
    public static final String COMMAND_SUFFIX = "Command";
    protected static final SkyBlock plugin;
    private final CommandParameters params;
    private final String name;
    private final String description;
    private final String usage;
    private final List<String> aliases;
    private final String permission;
    private final SECommand command;
    private CommandSource sender;

    protected SCommand() {
        this.params = this.getClass().getAnnotation(CommandParameters.class);
        this.name = this.getClass().getSimpleName().replace("Command", "").toLowerCase();
        this.description = this.params.description();
        this.usage = this.params.usage();
        this.aliases = Arrays.asList(this.params.aliases().split(","));
        this.permission = this.params.permission();
        this.command = new SECommand(this);
    }

    public abstract void run(CommandSource p0, String[] p1);

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return false;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }

    public void register() {
        plugin.commandMap.register("", this.command);
    }

    public void send(String message, CommandSource sender) {
        sender.send(ChatColor.GRAY + message);
    }

    public void send(String message) {
        this.send(Sputnik.trans(message), this.sender);
    }

    public void send(String message, Player player) {
        player.sendMessage(ChatColor.GRAY + message);
    }

    public void checkPermission(String permission) {
        if (!this.sender.getSender().hasPermission(permission)) {
            throw new CommandPermissionException(permission);
        }
    }

    public Player getNonNullPlayer(String name) {
        Player player = Bukkit.getPlayer(name);
        if (null == player) {
            throw new PlayerNotFoundException();
        }
        return player;
    }

    static {
        plugin = SkyBlock.getPlugin();
    }

    private static class SECommand extends Command {
        private final SCommand sc;

        public SECommand(SCommand xc) {
            super(xc.name, xc.description, xc.usage, xc.aliases);
            this.setPermission(xc.permission);
            this.setPermissionMessage(ChatColor.RED + "No permission. You need \"" + xc.permission + "\"");
            this.sc = xc;
        }

        public boolean execute(CommandSender sender, String commandLabel, String[] args) {
            this.sc.sender = new CommandSource(sender);
            try {
                this.sc.run(this.sc.sender, args);
                return true;
            } catch (CommandFailException | CommandPermissionException | PlayerNotFoundException ex) {
                sender.sendMessage(ex.getMessage());
                return true;
            } catch (CommandArgumentException ex2) {
                return false;
            } catch (Exception ex3) {
                sender.sendMessage(ChatColor.RED + "Error! " + ex3.getMessage());
                ex3.printStackTrace();
                return true;
            }
        }

        public List<String> tabComplete(CommandSender sender, String alias, String[] args) {
            List<String> tc = this.sc.onTabComplete(sender, this, alias, args);
            if (null != tc) {
                return tc;
            }
            return SUtil.getPlayerNameList();
        }
    }
}
