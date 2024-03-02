package net.hypixel.skyblock.command;

import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import net.hypixel.skyblock.entity.EntitySpawner;
import net.hypixel.skyblock.entity.SEntityType;
import net.hypixel.skyblock.util.SUtil;

import java.util.List;

@CommandParameters(description = "Manage entity spawners.", usage = "/<command> [create <type> | delete <index>]", aliases = "entityspawner,es,spawner,spawners", permission = "spt.entity")
public class EntitySpawnersCommand extends SCommand {
    @Override
    public void run(CommandSource sender, String[] args) {
        if (sender instanceof ConsoleCommandSender) {
            throw new CommandFailException("Console senders cannot use this command!");
        }
        Player player = sender.getPlayer();
        if (0 == args.length) {
            StringBuilder builder = new StringBuilder("Spawners:");
            List<EntitySpawner> spawners = EntitySpawner.getSpawners();
            for (int i = 0; i < spawners.size(); ++i) {
                EntitySpawner spawner = spawners.get(i);
                builder.append("\n ").append(i + 1).append(": ").append(SUtil.prettify(spawner.getLocation())).append(" (").append(spawner.getType().name()).append(")");
            }
            this.send(builder.toString());
            return;
        }
        if (2 != args.length) {
            throw new CommandArgumentException();
        }
        String lowerCase = args[0].toLowerCase();
        switch (lowerCase) {
            case "create": {
                SEntityType type = SEntityType.getEntityType(args[1]);
                if (null == type) {
                    throw new CommandFailException(ChatColor.RED + "That is not a valid entity type!");
                }
                EntitySpawner spawner = new EntitySpawner(type, player.getLocation());
                this.send(ChatColor.GREEN + "New entity spawner has been created at " + ChatColor.YELLOW + SUtil.prettify(spawner.getLocation()) + " with the type " + spawner.getType().getGenericInstance());
                break;
            }
            case "delete": {
                int index = Integer.parseInt(args[1]) - 1;
                List<EntitySpawner> spawners2 = EntitySpawner.getSpawners();
                if (0 > index || index > spawners2.size() - 1) {
                    throw new CommandFailException(ChatColor.RED + "There is no spawner at that location!");
                }
                spawners2.remove(index).delete();
                this.send(ChatColor.GREEN + "Entity spawner deleted.");
                break;
            }
        }
    }
}
