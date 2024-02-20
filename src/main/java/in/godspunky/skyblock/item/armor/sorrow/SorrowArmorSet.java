package in.godspunky.skyblock.item.armor.sorrow;

import org.bukkit.entity.Player;
import in.godspunky.skyblock.item.MaterialStatistics;
import in.godspunky.skyblock.item.SItem;
import in.godspunky.skyblock.item.armor.TickingSet;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class SorrowArmorSet implements TickingSet {
    @Override
    public String getName() {
        return "Mist Aura";
    }

    @Override
    public String getDescription() {
        return "Hides the wearer in a guise of mist.";
    }

    @Override
    public Class<? extends MaterialStatistics> getHelmet() {
        return SorrowArmorHelmet.class;
    }

    @Override
    public Class<? extends MaterialStatistics> getChestplate() {
        return SorrowArmorChestplate.class;
    }

    @Override
    public Class<? extends MaterialStatistics> getLeggings() {
        return SorrowArmorLeggings.class;
    }

    @Override
    public Class<? extends MaterialStatistics> getBoots() {
        return SorrowArmorBoots.class;
    }

    @Override
    public void tick(final Player owner, final SItem helmet, final SItem chestplate, final SItem leggings, final SItem boots, final List<AtomicInteger> counters) {
    }
}
