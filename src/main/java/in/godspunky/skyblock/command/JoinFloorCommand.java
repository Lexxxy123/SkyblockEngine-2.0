package in.godspunky.skyblock.command;

import in.godspunky.skyblock.ranks.PlayerRank;

@CommandParameters(description = "Spec test command.", aliases = "joinfloor6", permission = PlayerRank.DEFAULT)
public class JoinFloorCommand extends SCommand {
    @Override
    public void run(final CommandSource sender, final String[] args) {
    }
}
