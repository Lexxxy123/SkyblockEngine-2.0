package vn.giakhanhvn.skysim.command;

import vn.giakhanhvn.skysim.user.UserStash;
import vn.giakhanhvn.skysim.user.User;

@CommandParameters(description = "Adds an enchantment from Spec to the specified item.", aliases = "pickupstash", permission = "spt.item")
public class PickupStashCommand extends SCommand
{
    @Override
    public void run(final CommandSource sender, final String[] args) {
        if (sender.getPlayer() == null) {
            this.send("&cPoor you, but console can't stash items away, do they even... exist?");
            return;
        }
        final User u = User.getUser(sender.getPlayer().getUniqueId());
        if (u != null) {
            final UserStash us = UserStash.getStash(u.getUuid());
            us.pickUpStash();
        }
    }
}
