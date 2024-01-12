package in.godspunky.skyblock.command;

import in.godspunky.skyblock.gui.GUI;
import in.godspunky.skyblock.gui.GUIType;
import in.godspunky.skyblock.item.oddities.MaddoxBatphone;
import in.godspunky.skyblock.ranks.PlayerRank;
import in.godspunky.skyblock.util.SUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@CommandParameters(description = "Hidden command for Maddox Batphone.", permission = PlayerRank.DEFAULT)
public class BatphoneCommand extends SCommand {
    public static final UUID ACCESS_KEY;
    public static final List<String> KEYS;

    static {
        ACCESS_KEY = UUID.randomUUID();
        KEYS = new ArrayList<String>();
    }

    @Override
    public void run(final CommandSource sender, final String[] args) {
        if (sender instanceof ConsoleCommandSender) {
            throw new CommandFailException("Console senders cannot use this command!");
        }
        if (!args[0].equals(BatphoneCommand.ACCESS_KEY.toString())) {
            return;
        }
        if (!BatphoneCommand.KEYS.contains(args[1])) {
            throw new CommandFailException(ChatColor.RED + "✆ It's too late now, the phone line is off! Call again!");
        }
        final Player player = sender.getPlayer();
        MaddoxBatphone.CALL_COOLDOWN.add(player.getUniqueId());
        SUtil.delay(() -> MaddoxBatphone.CALL_COOLDOWN.remove(player.getUniqueId()), 400L);
        final GUI gui = GUIType.SLAYER.getGUI();
        gui.open(player);
    }
}
