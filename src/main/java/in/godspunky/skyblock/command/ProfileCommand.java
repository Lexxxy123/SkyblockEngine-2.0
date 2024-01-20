package in.godspunky.skyblock.command;

import in.godspunky.skyblock.island.SkyblockIsland;
import in.godspunky.skyblock.ranks.PlayerRank;
import in.godspunky.skyblock.user.Profile;
import in.godspunky.skyblock.user.User;

import java.util.UUID;

@CommandParameters(description = "Manage profiles", aliases = "profile", permission = PlayerRank.DEFAULT)
public class ProfileCommand extends SCommand {
    @Override
    public void run(CommandSource sender, String[] args) {
        User user = sender.getUser();
        if (args[0].equals("list")) {
            for (Profile profile : user.getProfiles()) {
                send(profile.getName() + " : " + profile.uuid);
            }
        }
        if (args[0].equals("selectedProfile")) {
            send(user.getSelectedProfile().getName());
            send(user.getSelectedProfile().getUuid().toString());
        }
        if (args[0].equals("create")) {
            user.switchProfile(null, User.SwitchReason.CREATE);
            //SkyblockIsland.getIsland(user.getUuid()).send();
        }
        if (args[0].equals("switch")) {
            user.switchProfile(Profile.get(UUID.fromString(args[1]), user.getUuid()), User.SwitchReason.SWITCH);
            SkyblockIsland.getIsland(user.getUuid()).send(user.selectedProfile.getProfileId());
        }
        if (args[0].equals("help")) {
            send("list");
            send("selectedProfile");
            send("create");
            send("switch");
        }
    }
}
