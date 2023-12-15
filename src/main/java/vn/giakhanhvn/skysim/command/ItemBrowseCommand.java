package vn.giakhanhvn.skysim.command;

import org.bukkit.entity.Player;
import vn.giakhanhvn.skysim.gui.ItemBrowserGUI;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.ConsoleCommandSender;

@CommandParameters(description = "Browse from a catalog of items.", aliases = "browseitem,browseitems,browsei,bi,ib", permission = "spt.item")
public class ItemBrowseCommand extends SCommand {
    @Override
    public void run(final CommandSource sender, final String[] args) {
        if (sender instanceof ConsoleCommandSender) {
            throw new CommandFailException("Console senders cannot use this command!");
        }
        final Player player = sender.getPlayer();
        String query = "";
        if (args.length >= 1) {
            query = StringUtils.join(args);
        }
        new ItemBrowserGUI(query).open(player);
    }
}
