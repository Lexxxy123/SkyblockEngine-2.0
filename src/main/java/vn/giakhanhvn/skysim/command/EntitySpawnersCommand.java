package vn.giakhanhvn.skysim.command;

import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import vn.giakhanhvn.skysim.entity.EntitySpawner;
import vn.giakhanhvn.skysim.entity.SEntityType;
import vn.giakhanhvn.skysim.util.SUtil;

import java.util.List;

@CommandParameters(description = "Manage entity spawners.", usage = "/<command> [create <type> | delete <index>]", aliases = "entityspawner,es,spawner,spawners", permission = "spt.entity")
public class EntitySpawnersCommand extends SCommand {
    @Override
    public void run(final CommandSource sender, final String[] args) {
        if (sender instanceof ConsoleCommandSender) {
            throw new CommandFailException("Console senders cannot use this command!");
        }
        final Player player = sender.getPlayer();
        if (args.length == 0) {
            final StringBuilder builder = new StringBuilder("Spawners:");
            final List<EntitySpawner> spawners = EntitySpawner.getSpawners();
            for (int i = 0; i < spawners.size(); ++i) {
                final EntitySpawner spawner = spawners.get(i);
                builder.append("\n ").append(i + 1).append(": ").append(SUtil.prettify(spawner.getLocation())).append(" (").append(spawner.getType().name()).append(")");
            }
            this.send(builder.toString());
            return;
        }
        if (args.length != 2) {
            throw new CommandArgumentException();
        }
        final String lowerCase = args[0].toLowerCase();
        switch (lowerCase) {
            case "create": {
                final SEntityType type = SEntityType.getEntityType(args[1]);
                if (type == null) {
                    throw new CommandFailException(ChatColor.RED + "That is not a valid entity type!");
                }
                final EntitySpawner spawner = new EntitySpawner(type, player.getLocation());
                this.send(ChatColor.GREEN + "New entity spawner has been created at " + ChatColor.YELLOW + SUtil.prettify(spawner.getLocation()) + " with the type " + spawner.getType().getGenericInstance());
                break;
            }
            case "delete": {
                final int index = Integer.parseInt(args[1]) - 1;
                final List<EntitySpawner> spawners2 = EntitySpawner.getSpawners();
                if (index < 0 || index > spawners2.size() - 1) {
                    throw new CommandFailException(ChatColor.RED + "There is no spawner at that location!");
                }
                spawners2.remove(index).delete();
                this.send(ChatColor.GREEN + "Entity spawner deleted.");
                break;
            }
        }
    }
}
