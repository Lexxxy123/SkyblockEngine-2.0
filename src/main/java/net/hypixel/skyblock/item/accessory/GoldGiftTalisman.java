/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.hypixel.skyblock.item.accessory;

import java.util.Arrays;
import java.util.List;
import net.hypixel.skyblock.item.Rarity;
import net.hypixel.skyblock.item.accessory.AccessoryStatistics;
import net.hypixel.skyblock.util.Sputnik;

public class GoldGiftTalisman
implements AccessoryStatistics {
    @Override
    public String getDisplayName() {
        return "Gold Gift";
    }

    @Override
    public Rarity getRarity() {
        return Rarity.LEGENDARY;
    }

    @Override
    public List<String> getListLore() {
        return Arrays.asList(Sputnik.trans("&7Grants &a+25% &6Coin &7and", "&7&aEXP &7rewards from gifts."));
    }

    @Override
    public String getURL() {
        return "abd98792dd92d9719894341ac9012a584c4428558fd2c712f78e5f0d4da85470";
    }
}

