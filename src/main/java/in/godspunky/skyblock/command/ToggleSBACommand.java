package in.godspunky.skyblock.command;

import org.bukkit.entity.Player;
import in.godspunky.skyblock.Repeater;
import in.godspunky.skyblock.util.Sputnik;

import java.util.UUID;

@CommandParameters(description = "Spec test command.", aliases = "tsba")
public class ToggleSBACommand extends SCommand {
    @Override
    public void run(CommandSource sender, String[] args) {
        Player player = sender.getPlayer();
        if (null != player) {
            UUID uuid = player.getUniqueId();
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
