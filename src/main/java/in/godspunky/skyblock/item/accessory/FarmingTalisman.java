package in.godspunky.skyblock.item.accessory;

import in.godspunky.skyblock.item.GenericItemType;
import in.godspunky.skyblock.item.PlayerBoostStatistics;
import in.godspunky.skyblock.item.Rarity;
import in.godspunky.skyblock.item.SItem;
import org.bukkit.entity.Player;
import in.godspunky.skyblock.region.Region;
import in.godspunky.skyblock.region.RegionType;
import in.godspunky.skyblock.user.PlayerUtils;

public class FarmingTalisman implements AccessoryStatistics, AccessoryFunction {
    @Override
    public String getDisplayName() {
        return "Farming Talisman";
    }

    @Override
    public Rarity getRarity() {
        return Rarity.COMMON;
    }

    @Override
    public String getURL() {
        return "9af328c87b068509aca9834eface197705fe5d4f0871731b7b21cd99b9fddc";
    }

    @Override
    public void update(final SItem instance, final Player player, final int accessorySlot) {
        final Region region = Region.getQuickRegionOfEntity(player);
        if (region == null) {
            return;
        }
        if (region.getType() != RegionType.THE_BARN && region.getType() != RegionType.MUSHROOM_DESERT) {
            return;
        }
        PlayerUtils.addBoostStatistics(PlayerUtils.STATISTICS_CACHE.get(player.getUniqueId()), accessorySlot, new PlayerBoostStatistics() {
            @Override
            public String getDisplayName() {
                return null;
            }

            @Override
            public Rarity getRarity() {
                return null;
            }

            @Override
            public GenericItemType getType() {
                return null;
            }

            @Override
            public double getBaseSpeed() {
                return 0.1;
            }
        });
    }
}
