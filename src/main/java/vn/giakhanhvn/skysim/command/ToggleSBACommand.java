package vn.giakhanhvn.skysim.command;

import java.util.UUID;

import org.bukkit.entity.Player;
import vn.giakhanhvn.skysim.util.Sputnik;
import vn.giakhanhvn.skysim.Repeater;

@CommandParameters(description = "Spec test command.", aliases = "tsba")
public class ToggleSBACommand extends SCommand {
    @Override
    public void run(final CommandSource sender, final String[] args) {
        final Player player = sender.getPlayer();
        if (player != null) {
            final UUID uuid = player.getUniqueId();
            if (Repeater.SBA_MAP.get(uuid)) {
                Repeater.SBA_MAP.put(uuid, false);
                this.send(Sputnik.trans("&cNo longer support SkyblockAddons Mods!"));
            } else if (!Repeater.SBA_MAP.get(uuid)) {
                Repeater.SBA_MAP.put(uuid, true);
                this.send(Sputnik.trans("&aNow supporting SBA Mods! Please rejoin!"));
                this.send(Sputnik.trans("&ePlease note that it &6may not working properly &esince this is SkySim, not Hypixel!"));
            }
        } else {
            this.send(Sputnik.trans("&cYou must be a player to execute this command!"));
        }
    }
}
