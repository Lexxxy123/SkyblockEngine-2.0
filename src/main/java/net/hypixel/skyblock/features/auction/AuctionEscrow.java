// 
// Decompiled by Procyon v0.5.36
// 

package net.hypixel.skyblock.features.auction;

import net.hypixel.skyblock.item.SItem;
import org.bson.Document;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.HashMap;
import java.util.Map;

public class AuctionEscrow implements ConfigurationSerializable {
    private SItem item;
    private long starter;
    private long duration;

    public AuctionEscrow(SItem item, long starter, long duration) {
        this.item = item;
        this.starter = starter;
        this.duration = duration;
    }

    public AuctionEscrow() {
        this(null, 50L, 21600000L);
    }

    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("item", this.item);
        map.put("starter", this.starter);
        map.put("duration", this.duration);
        return map;
    }

    // todo : fix it
    public static AuctionEscrow deserialize(Map<String, Object> map) {
        return new AuctionEscrow((SItem) map.get("item"), map.get("starter") instanceof Long ? ((Long) map.get("starter")).longValue() : ((Integer) map.get("starter")).longValue(), map.get("duration") instanceof Long ? ((Long) map.get("duration")).longValue() : ((Integer) map.get("duration")).longValue());
    }

    public SItem getItem() {
        return this.item;
    }

    public void setItem(SItem item) {
        this.item = item;
    }

    public long getStarter() {
        return this.starter;
    }

    public void setStarter(long starter) {
        this.starter = starter;
    }

    public long getDuration() {
        return this.duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getCreationFee(boolean bin) {
        return Math.round(this.starter * (bin ? 0.01 : 0.05));
    }
}