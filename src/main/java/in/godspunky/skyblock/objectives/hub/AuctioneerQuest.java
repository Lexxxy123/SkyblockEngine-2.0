package in.godspunky.skyblock.objectives.hub;


import in.godspunky.skyblock.event.SkyblockPlayerNPCClickEvent;
import in.godspunky.skyblock.objectives.Objective;
import in.godspunky.skyblock.objectives.QuestLine;
import org.bukkit.event.EventHandler;

public class AuctioneerQuest extends QuestLine {
    public AuctioneerQuest() {
        super("auctioneer", "Auctioneer", new AuctioneerObjective());
    }

    @Override
    protected boolean hasCompletionMessage() {
        return true;
    }

    private static class AuctioneerObjective extends Objective {
        public AuctioneerObjective() {
            super("auctioneer_objective", "Talk to the Auction Master");
        }

        @EventHandler
        public void onClick(SkyblockPlayerNPCClickEvent e) {
            if (!isThisObjective(e.getPlayer())) return;

            if (e.getNpc().getName().startsWith("Auction Master")) complete(e.getPlayer());
        }
    }
}
