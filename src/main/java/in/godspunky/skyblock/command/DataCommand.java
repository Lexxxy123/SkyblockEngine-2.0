package in.godspunky.skyblock.command;

import in.godspunky.skyblock.item.SItem;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

@CommandParameters(description = "Sets data for a Spec item.", permission = "spt.item")
public class DataCommand extends SCommand {
    @Override
    public void run(final CommandSource sender, final String[] args) {
        if (args.length < 3) {
            throw new CommandArgumentException();
        }
        if (sender instanceof ConsoleCommandSender) {
            throw new CommandFailException("Console senders cannot use this command!");
        }
        final Player player = sender.getPlayer();
        final PlayerInventory inv = player.getInventory();
        if (inv.getItemInHand() == null) {
            throw new CommandFailException(ChatColor.RED + "Error! Hold an item in your hand!");
        }
        final SItem sItem = SItem.find(inv.getItemInHand());
        final String key = args[0];
        if (!sItem.hasDataFor(key)) {
            throw new CommandFailException(ChatColor.RED + "Error! This item does not have data for '" + key + "'");
        }
        final String joined = StringUtils.join(args, " ", 1, args.length - 1);
        final String lowerCase = args[args.length - 1].toLowerCase();
        switch (lowerCase) {
            case "string":
                sItem.setDataString(key, joined);
                break;
            case "integer":
            case "int":
                sItem.setDataInt(key, Integer.parseInt(joined));
                break;
            case "long":
                sItem.setDataLong(key, Long.parseLong(joined));
                break;
            case "boolean":
            case "bool":
                sItem.setDataBoolean(key, Boolean.parseBoolean(joined));
                break;
            case "double":
            case "d":
                sItem.setDataDouble(key, Double.parseDouble(joined));
                break;
            case "float":
            case "f":
                sItem.setDataFloat(key, Float.parseFloat(joined));
                break;
        }
        sItem.update();
        this.send(ChatColor.GREEN + "'" + key + "' for this item has been set to '" + joined + "' as type '" + args[args.length - 1].toLowerCase() + "'");
    }
}
