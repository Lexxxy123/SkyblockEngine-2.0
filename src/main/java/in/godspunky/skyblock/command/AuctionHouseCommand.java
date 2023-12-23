package in.godspunky.skyblock.command;

import in.godspunky.skyblock.ranks.PlayerRank;

@CommandParameters(description = "Modify your coin amount.", usage = "/<command> <auction uuid/player name>", aliases = "su", permission = PlayerRank.DEFAULT)
public class AuctionHouseCommand extends SCommand {
    @Override
    public void run(final CommandSource sender, final String[] args) {
    }
}
