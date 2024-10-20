/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 */
package net.hypixel.skyblock.item.armor.sorrow;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import net.hypixel.skyblock.item.MaterialStatistics;
import net.hypixel.skyblock.item.SItem;
import net.hypixel.skyblock.item.armor.TickingSet;
import net.hypixel.skyblock.item.armor.sorrow.SorrowArmorBoots;
import net.hypixel.skyblock.item.armor.sorrow.SorrowArmorChestplate;
import net.hypixel.skyblock.item.armor.sorrow.SorrowArmorHelmet;
import net.hypixel.skyblock.item.armor.sorrow.SorrowArmorLeggings;
import org.bukkit.entity.Player;

public class SorrowArmorSet
implements TickingSet {
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
    public void tick(Player owner, SItem helmet, SItem chestplate, SItem leggings, SItem boots, List<AtomicInteger> counters) {
    }
}

