/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.configuration.serialization.ConfigurationSerializable
 */
package net.hypixel.skyblock.features.auction;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

public class AuctionBid
implements ConfigurationSerializable {
    private UUID bidder;
    private long amount;
    private long timestamp;

    public AuctionBid(UUID bidder, long amount, long timestamp) {
        this.bidder = bidder;
        this.amount = amount;
        this.timestamp = timestamp;
    }

    public Map<String, Object> serialize() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("bidder", this.bidder.toString());
        map.put("amount", this.amount);
        map.put("timestamp", this.timestamp);
        return map;
    }

    public static AuctionBid deserialize(Map<String, Object> map) {
        return new AuctionBid(UUID.fromString((String)map.get("bidder")), (Long)map.get("amount"), (Long)map.get("timestamp"));
    }

    public UUID getBidder() {
        return this.bidder;
    }

    public void setBidder(UUID bidder) {
        this.bidder = bidder;
    }

    public long getAmount() {
        return this.amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long timeSinceBid() {
        return System.currentTimeMillis() - this.timestamp;
    }
}

