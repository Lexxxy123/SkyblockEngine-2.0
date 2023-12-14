package vn.giakhanhvn.skysim.auction;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

public class AuctionBid implements ConfigurationSerializable
{
    private UUID bidder;
    private long amount;
    private long timestamp;
    
    public AuctionBid(final UUID bidder, final long amount, final long timestamp) {
        this.bidder = bidder;
        this.amount = amount;
        this.timestamp = timestamp;
    }
    
    public Map<String, Object> serialize() {
        final Map<String, Object> map = new HashMap<String, Object>();
        map.put("bidder", this.bidder.toString());
        map.put("amount", this.amount);
        map.put("timestamp", this.timestamp);
        return map;
    }
    
    public static AuctionBid deserialize( Map<String, Object> map) {
        return new AuctionBid(UUID.fromString((String) map.get("bidder")), (Long) ((map.get("amount") instanceof Long) ? map.get("amount") : map.get("amount")), (Long) ((map.get("timestamp") instanceof Long) ? map.get("timestamp") : map.get("timestamp")));
    }
    
    public UUID getBidder() {
        return this.bidder;
    }
    
    public void setBidder(final UUID bidder) {
        this.bidder = bidder;
    }
    
    public long getAmount() {
        return this.amount;
    }
    
    public void setAmount(final long amount) {
        this.amount = amount;
    }
    
    public long getTimestamp() {
        return this.timestamp;
    }
    
    public void setTimestamp(final long timestamp) {
        this.timestamp = timestamp;
    }
    
    public long timeSinceBid() {
        return System.currentTimeMillis() - this.timestamp;
    }
}
