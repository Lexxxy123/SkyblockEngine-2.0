package net.hypixel.skyblock.item.oddities;

import net.hypixel.skyblock.item.*;

public class ShardOftheVoidOrb implements MaterialStatistics, MaterialFunction, SkullStatistics {
    @Override
    public String getDisplayName() {
        return "Shard of The Void Orb";
    }

    @Override
    public Rarity getRarity() {
        return Rarity.LEGENDARY;
    }

    @Override
    public GenericItemType getType() {
        return GenericItemType.ITEM;
    }

    @Override
    public boolean isEnchanted() {
        return true;
    }

    @Override
    public String getURL() {
        return "b46d4e880181e6f5ad8c0e6ab084e6b1c81be4dad5eb2d4bc20c016eb03bb475";
    }

    @Override
    public boolean isStackable() {
        return true;
    }

    @Override
    public String getLore() {
        return "A Powerful shard from Voidlings's Remnant, dropped by the Voidlings Warden";
    }
}
