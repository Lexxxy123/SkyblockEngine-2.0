/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.hypixel.skyblock.item.accessory;

import java.util.Arrays;
import java.util.List;
import net.hypixel.skyblock.item.Rarity;
import net.hypixel.skyblock.item.accessory.AccessoryStatistics;
import net.hypixel.skyblock.util.Sputnik;

public class RingOfLove
implements AccessoryStatistics {
    @Override
    public String getDisplayName() {
        return "Legendary Ring of Love";
    }

    @Override
    public Rarity getRarity() {
        return Rarity.LEGENDARY;
    }

    @Override
    public List<String> getListLore() {
        return Arrays.asList(Sputnik.trans5("&7Grants a 1 in 100 chance to", "&7deal &c+100% &7damage.", "&7", "&8True love is tough to find,", "&8but finding it hits you hard!"));
    }

    @Override
    public String getURL() {
        return "177c9c638bf3dcda348edea44e9a3db4abc1e239558661611f80c110472ad";
    }
}

