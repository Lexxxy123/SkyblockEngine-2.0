package in.godspunky.skyblock.npc.hub.Auction;

import in.godspunky.skyblock.gui.GUIType;
import in.godspunky.skyblock.npc.NPCParameters;
import in.godspunky.skyblock.npc.SkyblockNPC;
import in.godspunky.skyblock.user.User;
import org.bukkit.entity.Player;

public class AuctionMasterNPC extends SkyblockNPC {
    public AuctionMasterNPC() {
        super(new NPCParameters() {

            @Override
            public String name() {
                return "Auction Master";
            }

            @Override
            public String[] messages() {
                return new String[]{
                        "Hey there, I'm the Auction Master.",
                        "At the auction house, you can put your valuable items up for auction!",
                        "You may also want to check back here to see what items other players are selling to see if you can get a good deal.",
                        "Talk to me if you would like to start your first auction or if you want to see the items currently being sold."
                };
            }

            @Override
            public String[] holograms() {
                return new String[]{
                        "&fAuction Master",
                        "&e&lCLICK",
                };
            }

            @Override
            public String texture() {
                return "ewogICJ0aW1lc3RhbXAiIDogMTYyMzAwMDYzNDI2NCwKICAicHJvZmlsZUlkIiA6ICI5MWYwNGZlOTBmMzY0M2I1OGYyMGUzMzc1Zjg2ZDM5ZSIsCiAgInByb2ZpbGVOYW1lIiA6ICJTdG9ybVN0b3JteSIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS84ZjIxNGRlNDUxNDE1MzliNmI3M2NlNWQzZjUwNWYyZjg1YzAwNTRkNGMxZGJjZTkxNzU1ODkzNmViNzc4MmE5IgogICAgfQogIH0KfQ=="; // Replace with the actual texture
            }

            @Override
            public String signature() {
                return "VIAY/TPfL+zjeyk1bzxaiV/nzB0g0MnfPrYa54+BNXgrsaQeR6ucqyKUf02G/zH4ZkplhGNR0bQIcWcw8ntQUVUTpYw+qGTmKMLQ95TxQbdXMSqCepLGbME1fNAGFMAw0zMd4oU1VC4Bo+STuMK5ee92X64Hp1SCWV8ap+elH9d3uVOybfECi0Qn+UM1lV+sDaC225p2TXP8iL3X2OmVpvux0FnID48+qLXas4stCi65WzMGk+i7HhrSsvyKDOe7U9xX46Q8zbuHVDVUxFE/AYpfQTUfEgmm2mmvHvu7OmzIQHKCAr+mDshVDAaiX2Tt1ojMGk+lG5MG8U2/3yFlgk0qmex4D5HACGAlS611hImX3oXq8449hArbuDA5KITdNjGb3oF8tZaKNkvqGMpliR6Axt3SDu9CLRsebfu80oHREEzAIKMsc6GZ3cDw49YDA+J/I7UiUQe4PzSRrzyIGHD8Qgn7Yv2LcTSmL3o/svqQM8mRz8S8upi71gXjeSPCkZN4hIg8g9TiMW+ipegN0DuryHx6vxcqc6DfB/3ktrHJi7QMbr/KPRd7cr32S3RMCQJwHxmQwfanauw1vcH+sDKNg6+/47Jzf4hN7MB77RlV7ZLb2oTmlFtQU6aexp3b9sz98QFduUbHaUYgfscm+eHkcjoupZw9g39XgQymSLE="; // Replace with the actual signature
            }

            @Override
            public String world() {
                return "world";
            }

            @Override
            public double x() {
                return -46.0;
            }

            @Override
            public double y() {
                return 73.0;
            }

            @Override
            public double z() {
                return -90.0;
            }

            @Override
            public float yaw() {
                return 97.8751f;
            }

            @Override
            public float pitch() {
                return 0.0f;
            }

            @Override
            public boolean looking() {
                return true;
            }

            @Override
            public void onInteract(Player player, SkyblockNPC npc) {
                GUIType.AUCTION_HOUSE.getGUI().open(player);
            }
        });
    }
}
