package in.godspunky.skyblock.item.armor.lapis;

import org.bukkit.entity.Player;
import in.godspunky.skyblock.item.GenericItemType;
import in.godspunky.skyblock.item.MaterialStatistics;
import in.godspunky.skyblock.item.PlayerBoostStatistics;
import in.godspunky.skyblock.item.Rarity;
import in.godspunky.skyblock.item.armor.ArmorSet;

public class LapisArmorSet implements ArmorSet {
    @Override
    public String getName() {
        return "Health";
    }

    @Override
    public String getDescription() {
        return "Increases the wearer's maximum Health by 60.";
    }

    @Override
    public Class<? extends MaterialStatistics> getHelmet() {
        return LapisArmorHelmet.class;
    }

    @Override
    public Class<? extends MaterialStatistics> getChestplate() {
        return LapisArmorChestplate.class;
    }

    @Override
    public Class<? extends MaterialStatistics> getLeggings() {
        return LapisArmorLeggings.class;
    }

    @Override
    public Class<? extends MaterialStatistics> getBoots() {
        return LapisArmorBoots.class;
    }

    @Override
    public PlayerBoostStatistics whileHasFullSet(final Player player) {
        return new PlayerBoostStatistics() {
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
            public double getBaseHealth() {
                return 60.0;
            }
        };
    }
}
