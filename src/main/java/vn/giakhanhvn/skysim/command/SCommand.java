package vn.giakhanhvn.skysim.command;

import vn.giakhanhvn.skysim.util.SUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import vn.giakhanhvn.skysim.util.Sputnik;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import java.util.Arrays;
import java.util.List;
import vn.giakhanhvn.skysim.SkySimEngine;
import org.bukkit.command.TabCompleter;
import org.bukkit.command.CommandExecutor;

public abstract class SCommand implements CommandExecutor, TabCompleter
{
    public static final String COMMAND_SUFFIX = "Command";
    protected static final SkySimEngine plugin;
    private CommandParameters params;
    private String name;
    private String description;
    private String usage;
    private List<String> aliases;
    private String permission;
    private SECommand command;
    private CommandSource sender;
    
    protected SCommand() {
        this.params = this.getClass().<CommandParameters>getAnnotation(CommandParameters.class);
        this.name = this.getClass().getSimpleName().replace("Command", "").toLowerCase();
        this.description = this.params.description();
        this.usage = this.params.usage();
        this.aliases = Arrays.<String>asList(this.params.aliases().split(","));
        this.permission = this.params.permission();
        this.command = new SECommand(this);
    }
    
    public abstract void run(final CommandSource p0, final String[] p1);
    
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        return false;
    }
    
    public List<String> onTabComplete(final CommandSender sender, final Command command, final String alias, final String[] args) {
        return null;
    }
    
    public void register() {
        SCommand.plugin.commandMap.register("", (Command)this.command);
    }
    
    public void send(final String message, final CommandSource sender) {
        sender.send(ChatColor.GRAY + message);
    }
    
    public void send(final String message) {
        this.send(Sputnik.trans(message), this.sender);
    }
    
    public void send(final String message, final Player player) {
        player.sendMessage(ChatColor.GRAY + message);
    }
    
    public void checkPermission(final String permission) {
        if (!this.sender.getSender().hasPermission(permission)) {
            throw new CommandPermissionException(permission);
        }
    }
    
    public Player getNonNullPlayer(final String name) {
        final Player player = Bukkit.getPlayer(name);
        if (player == null) {
            throw new PlayerNotFoundException();
        }
        return player;
    }
    
    static {
        plugin = SkySimEngine.getPlugin();
    }
    
    private static class SECommand extends Command
    {
        private final SCommand sc;
        
        public SECommand(final SCommand xc) {
            super(xc.name, xc.description, xc.usage, xc.aliases);
            this.setPermission(xc.permission);
            this.setPermissionMessage(ChatColor.RED + "No permission. You need \"" + xc.permission + "\"");
            this.sc = xc;
        }
        
        public boolean execute(final CommandSender sender, final String commandLabel, final String[] args) {
            this.sc.sender = new CommandSource(sender);
            try {
                this.sc.run(this.sc.sender, args);
                return true;
            }
            catch (final CommandFailException | CommandPermissionException | PlayerNotFoundException ex) {
                sender.sendMessage(ex.getMessage());
                return true;
            }
            catch (final CommandArgumentException ex2) {
                return false;
            }
            catch (final Exception ex3) {
                sender.sendMessage(ChatColor.RED + "Error! " + ex3.getMessage());
                ex3.printStackTrace();
                return true;
            }
        }
        
        public List<String> tabComplete(final CommandSender sender, final String alias, final String[] args) {
            final List<String> tc = this.sc.onTabComplete(sender, this, alias, args);
            if (tc != null) {
                return tc;
            }
            return SUtil.getPlayerNameList();
        }
    }
}
