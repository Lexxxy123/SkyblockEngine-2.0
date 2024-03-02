package net.hypixel.skyblock.command;

import net.hypixel.skyblock.api.hologram.Hologram;
import net.hypixel.skyblock.util.PacketEntity;
import org.bukkit.Location;
import org.bukkit.entity.Player;

@CommandParameters(description = "Spec test command.", aliases = "spectest")
public class SpecTestCommand extends SCommand {
    @Override
    public void run(final CommandSource sender, final String[] args) {
        Location location = sender.getPlayer().getLocation();

        Hologram hologram = new Hologram(location);
        hologram.setInvisible(false);
        if (args[0].equals("show")){
            hologram.show(sender.getPlayer());
        }
        if (args[0].equals("spawn")){
            PacketEntity entity = new PacketEntity(hologram);
        }
    }
}
