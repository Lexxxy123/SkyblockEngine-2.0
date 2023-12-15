package vn.giakhanhvn.skysim.item.oddities;

import java.util.ArrayList;

import vn.giakhanhvn.skysim.item.GenericItemType;
import vn.giakhanhvn.skysim.item.Rarity;
import org.bukkit.plugin.Plugin;
import vn.giakhanhvn.skysim.SkySimEngine;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.GameMode;
import org.bukkit.ChatColor;
import vn.giakhanhvn.skysim.util.Sputnik;
import org.bukkit.event.player.PlayerFishEvent;
import vn.giakhanhvn.skysim.item.SItem;

import java.util.UUID;
import java.util.List;

import vn.giakhanhvn.skysim.item.FishingRodFunction;
import vn.giakhanhvn.skysim.item.MaterialStatistics;

public class GrapplingHook implements MaterialStatistics, FishingRodFunction {
    private static final List<UUID> COOLDOWN;

    @Override
    public void onFish(final SItem instance, final PlayerFishEvent e) {
        final PlayerFishEvent.State state = e.getState();
        if (state != PlayerFishEvent.State.FAILED_ATTEMPT && state != PlayerFishEvent.State.IN_GROUND) {
            return;
        }
        final Player player = e.getPlayer();
        if (!Sputnik.tpAbilUsable(player)) {
            player.sendMessage(ChatColor.RED + "Dimoon destroyed your hook! Yikes!");
            return;
        }
        if (GrapplingHook.COOLDOWN.contains(player.getUniqueId())) {
            player.sendMessage(ChatColor.RED + "Whow! Slow down there!");
            return;
        }
        player.setVelocity(player.getLocation().toVector().subtract(e.getHook().getLocation().toVector()).multiply(-1.0).multiply(0.5).setY(0.9));
        if (player.getGameMode() != GameMode.CREATIVE && player.getGameMode() != GameMode.SPECTATOR) {
            GrapplingHook.COOLDOWN.add(player.getUniqueId());
            new BukkitRunnable() {
                public void run() {
                    GrapplingHook.COOLDOWN.remove(player.getUniqueId());
                }
            }.runTaskLater(SkySimEngine.getPlugin(), 40L);
        }
    }

    @Override
    public String getDisplayName() {
        return "Grappling Hook";
    }

    @Override
    public Rarity getRarity() {
        return Rarity.UNCOMMON;
    }

    @Override
    public GenericItemType getType() {
        return GenericItemType.ITEM;
    }

    @Override
    public String getLore() {
        return "Travel around in style using this Grappling Hook. 2 Second Cooldown";
    }

    static {
        COOLDOWN = new ArrayList<UUID>();
    }
}
