package vn.giakhanhvn.skysim.item.orb;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import vn.giakhanhvn.skysim.item.Rarity;
import org.bukkit.ChatColor;

public class RadiantPowerOrb extends PowerOrb {
    @Override
    public String getAbilityDescription() {
        return "Place an orb for " + ChatColor.GREEN + "30s " + ChatColor.GRAY + "buffing up to " + ChatColor.AQUA + "5" + ChatColor.GRAY + " players within " + ChatColor.GREEN + "18 " + ChatColor.GRAY + "blocks. " + ChatColor.DARK_GRAY + "Costs " + ChatColor.DARK_GRAY + "50% of max mana. " + ChatColor.DARK_GRAY + "Only " + ChatColor.DARK_GRAY + "one orb applies per player.";
    }

    @Override
    public String getURL() {
        return "7ab4c4d6ee69bc24bba2b8faf67b9f704a06b01aa93f3efa6aef7a9696c4feef";
    }

    @Override
    public String getDisplayName() {
        return "Radiant Power Orb";
    }

    @Override
    public Rarity getRarity() {
        return Rarity.UNCOMMON;
    }

    @Override
    public String getBuffName() {
        return "Radiant";
    }

    @Override
    public String getBuffDescription() {
        return "Heals " + ChatColor.RED + "1% " + ChatColor.GRAY + "of max " + ChatColor.RED + "❤ " + ChatColor.GRAY + "per second.";
    }

    @Override
    protected void buff(final Player player) {
        player.setHealth(Math.min(player.getMaxHealth(), player.getHealth() + player.getMaxHealth() * 0.01));
    }

    @Override
    protected long getOrbLifeTicks() {
        return 600L;
    }

    @Override
    public boolean isStackable() {
        return false;
    }

    @Override
    protected void playEffect(final Location location) {
        location.getWorld().playEffect(location, Effect.HAPPY_VILLAGER, (Object) Effect.HAPPY_VILLAGER.getData());
    }
}
