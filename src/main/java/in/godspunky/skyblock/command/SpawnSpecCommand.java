package in.godspunky.skyblock.command;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import in.godspunky.skyblock.entity.SEntity;
import in.godspunky.skyblock.entity.SEntityType;
import in.godspunky.skyblock.util.Sputnik;

@CommandParameters(description = "Spawn a mob from Spec.", aliases = "scm", permission = "spt.spawn")
public class SpawnSpecCommand extends SCommand {
    @Override
    public void run(final CommandSource sender, final String[] args) {
        if (args.length == 0) {
            throw new CommandArgumentException();
        }
        if (sender instanceof ConsoleCommandSender) {
            throw new CommandFailException("Console senders cannot use this command!");
        }
        final Player player = sender.getPlayer();
        if (SEntityType.getEntityType(args[0]) == null) {
            player.sendMessage(Sputnik.trans("&cMob not found or it get banned!"));
            return;
        }
        final SEntityType type = SEntityType.getEntityType(args[0]);
        if (type.toString().toLowerCase().contains("banned") || type.toString().toLowerCase().contains("test")) {
            player.sendMessage(Sputnik.trans("&cMob not found or it get banned!"));
            return;
        }
        SEntity entity = null;
        switch (type) {
            case REVENANT_HORROR:
            case SVEN_PACKMASTER:
            case TARANTULA_BROODFATHER:
            case VOIDGLOOM_SERAPH:
            case ATONED_HORROR:
            case CRIMSON_SATHANAS: {
                if (args.length != 2) {
                    throw new CommandArgumentException();
                }
                final int tier = Integer.parseInt(args[1]);
                entity = new SEntity(player, type, tier, player.getUniqueId());
                break;
            }
            default:
                entity = new SEntity(player, type);
                break;
        }
        this.send(ChatColor.GREEN + "Success! You have spawned a(n) " + ChatColor.GOLD + entity.getStatistics().getEntityName());
    }
}
