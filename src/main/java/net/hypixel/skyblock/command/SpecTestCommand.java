package net.hypixel.skyblock.command;

import net.hypixel.skyblock.api.disguise.PlayerDisguise;
import net.hypixel.skyblock.api.hologram.Hologram;
import net.hypixel.skyblock.util.PacketEntity;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

@CommandParameters(description = "Spec test command.", aliases = "spectest")
public class SpecTestCommand extends SCommand {
    @Override
    public void run(final CommandSource sender, final String[] args) {
        Player player = sender.getPlayer();
        Entity zombie = player.getWorld().spawnEntity(player.getLocation() , EntityType.ZOMBIE);
        PlayerDisguise playerDisguise = new PlayerDisguise((LivingEntity) zombie, null);
    }
}
