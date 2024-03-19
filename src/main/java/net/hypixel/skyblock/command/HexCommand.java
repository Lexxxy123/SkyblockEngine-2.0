package net.hypixel.skyblock.command;

import net.hypixel.skyblock.features.ranks.PlayerRank;
import net.hypixel.skyblock.gui.menu.Items.HexGUI;
import net.hypixel.skyblock.item.GenericItemType;
import net.hypixel.skyblock.item.SItem;

import net.hypixel.skyblock.item.SpecificItemType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@CommandParameters(permission = PlayerRank.DEFAULT, aliases = "hex")
public class HexCommand extends SCommand {
    @Override
    public void run(CommandSource sender, String[] args) {
       sender.getPlayer().sendMessage("Coming soon");

    }
}
