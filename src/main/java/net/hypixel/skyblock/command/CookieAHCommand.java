/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Sound
 *  org.bukkit.entity.Player
 */
package net.hypixel.skyblock.command;

import net.hypixel.skyblock.command.CommandParameters;
import net.hypixel.skyblock.command.CommandSource;
import net.hypixel.skyblock.command.SCommand;
import net.hypixel.skyblock.features.ranks.PlayerRank;
import net.hypixel.skyblock.gui.GUIType;
import net.hypixel.skyblock.user.PlayerUtils;
import net.hypixel.skyblock.util.Sputnik;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

@CommandParameters(description="Gets the NBT of your current item.", aliases="ah", permission=PlayerRank.DEFAULT)
public class CookieAHCommand
extends SCommand {
    @Override
    public void run(CommandSource sender, String[] args) {
        Player player = sender.getPlayer();
        if (PlayerUtils.getCookieDurationTicks(player) <= 0L) {
            player.playSound(player.getLocation(), Sound.VILLAGER_YES, 0.0f, 2.0f);
            this.send(Sputnik.trans("&eYou need the &dCookie Buff &eto use this command!"));
            this.send(Sputnik.trans("&eObtain a &6Booster Cookie &efrom the community shop in the hub!"));
            player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1.0f, 1.0f);
            return;
        }
        GUIType.AUCTION_HOUSE.getGUI().open(player);
    }
}

