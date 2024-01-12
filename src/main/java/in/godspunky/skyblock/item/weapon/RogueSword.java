package in.godspunky.skyblock.item.weapon;

import in.godspunky.skyblock.item.*;
import in.godspunky.skyblock.user.PlayerUtils;
import org.bukkit.entity.Player;

public class RogueSword implements ToolStatistics, MaterialFunction, Ability {
    @Override
    public String getAbilityName() {
        return "Speed Boost";
    }

    @Override
    public String getAbilityDescription() {
        return "Increases your Speed by +20 for 30 seconds.";
    }

    @Override
    public void onAbilityUse(final Player player, final SItem sItem) {
        PlayerUtils.boostPlayer(PlayerUtils.STATISTICS_CACHE.get(player.getUniqueId()), new PlayerBoostStatistics() {
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
                return 0.2;
            }
        }, 600L);
    }

    @Override
    public int getAbilityCooldownTicks() {
        return 0;
    }

    @Override
    public int getManaCost() {
        return 50;
    }

    @Override
    public String getDisplayName() {
        return "Rogue Sword";
    }

    @Override
    public Rarity getRarity() {
        return Rarity.COMMON;
    }

    @Override
    public GenericItemType getType() {
        return GenericItemType.WEAPON;
    }

    @Override
    public SpecificItemType getSpecificType() {
        return SpecificItemType.SWORD;
    }

    @Override
    public int getBaseDamage() {
        return 20;
    }
}
