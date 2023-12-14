package vn.giakhanhvn.skysim.auction;

import java.util.HashMap;
import java.util.Map;
import vn.giakhanhvn.skysim.item.SItem;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

public class AuctionEscrow implements ConfigurationSerializable
{
    private SItem item;
    private long starter;
    private long duration;
    
    public AuctionEscrow(final SItem item, final long starter, final long duration) {
        this.item = item;
        this.starter = starter;
        this.duration = duration;
    }
    
    public AuctionEscrow() {
        this(null, 50L, 21600000L);
    }
    
    public Map<String, Object> serialize() {
        final Map<String, Object> map = new HashMap<String, Object>();
        map.put("item", this.item);
        map.put("starter", this.starter);
        map.put("duration", this.duration);
        return map;
    }


    // todo : fix it
    public static AuctionEscrow deserialize(Map<String, Object> map) {
        return new AuctionEscrow((SItem)map.get("item"), map.get("starter") instanceof Long ? ((Long)map.get("starter")).longValue() : ((Integer)map.get("starter")).longValue(), map.get("duration") instanceof Long ? ((Long)map.get("duration")).longValue() : ((Integer)map.get("duration")).longValue());
    }
    
    public SItem getItem() {
        return this.item;
    }
    
    public void setItem(final SItem item) {
        this.item = item;
    }
    
    public long getStarter() {
        return this.starter;
    }
    
    public void setStarter(final long starter) {
        this.starter = starter;
    }
    
    public long getDuration() {
        return this.duration;
    }
    
    public void setDuration(final long duration) {
        this.duration = duration;
    }
    
    public long getCreationFee(final boolean bin) {
        return Math.round(this.starter * (bin ? 0.01 : 0.05));
    }
}
