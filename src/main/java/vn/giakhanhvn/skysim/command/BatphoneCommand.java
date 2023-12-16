package vn.giakhanhvn.skysim.command;

import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import vn.giakhanhvn.skysim.gui.GUI;
import vn.giakhanhvn.skysim.gui.GUIType;
import vn.giakhanhvn.skysim.item.oddities.MaddoxBatphone;
import vn.giakhanhvn.skysim.util.SUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@CommandParameters(description = "Hidden command for Maddox Batphone.")
public class BatphoneCommand extends SCommand {
    public static final UUID ACCESS_KEY;
    public static final List<String> KEYS;

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

    static {
        ACCESS_KEY = UUID.randomUUID();
        KEYS = new ArrayList<String>();
    }
}
