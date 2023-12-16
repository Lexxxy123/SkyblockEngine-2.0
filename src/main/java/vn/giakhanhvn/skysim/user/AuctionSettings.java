package vn.giakhanhvn.skysim.user;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import vn.giakhanhvn.skysim.item.ItemCategory;
import vn.giakhanhvn.skysim.item.Rarity;

import java.util.HashMap;
import java.util.Map;

public class AuctionSettings implements Cloneable, ConfigurationSerializable {
    private ItemCategory category;
    private String query;
    private Sort sort;
    private Rarity tier;
    private Type type;

    public AuctionSettings(final ItemCategory category, final String query, final Sort sort, final Rarity tier, final Type type) {
        this.category = category;
        this.query = query;
        this.sort = sort;
        this.tier = tier;
        this.type = type;
    }

    public AuctionSettings() {
        this(ItemCategory.WEAPONS, null, Sort.HIGHEST_BID, null, Type.SHOW_ALL);
    }

    public Map<String, Object> serialize() {
        final Map<String, Object> map = new HashMap<String, Object>();
        map.put("category", this.category.name());
        map.put("query", this.query);
        map.put("sort", this.sort.name());
        if (this.tier != null) {
            map.put("tier", this.tier.name());
        }
        map.put("type", this.type.name());
        return map;
    }

    @Override
    public String toString() {
        return "AuctionSettings{category=" + this.category.name() + ", query=" + this.query + ", sort=" + this.sort.name() + ", tier=" + this.tier.name() + ", type=" + this.type.name() + "}";
    }

    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof AuctionSettings)) {
            return false;
        }
        final AuctionSettings settings = (AuctionSettings) o;
        return this.category == settings.category && this.query.equals(settings.query) && this.sort == settings.sort && this.tier == settings.tier && this.type == settings.type;
    }

    public AuctionSettings clone() {
        return new AuctionSettings(this.category, this.query, this.sort, this.tier, this.type);
    }

    public ItemCategory getCategory() {
        return this.category;
    }

    public void setCategory(final ItemCategory category) {
        this.category = category;
    }

    public String getQuery() {
        return this.query;
    }

    public void setQuery(final String query) {
        this.query = query;
    }

    public Sort getSort() {
        return this.sort;
    }

    public void setSort(final Sort sort) {
        this.sort = sort;
    }

    public Rarity getTier() {
        return this.tier;
    }

    public void setTier(final Rarity tier) {
        this.tier = tier;
    }

    public Type getType() {
        return this.type;
    }

    public void setType(final Type type) {
        this.type = type;
    }

    public static AuctionSettings deserialize(Map<String, Object> map) {
        return new AuctionSettings(ItemCategory.valueOf((String) map.get("category")), (String) map.getOrDefault("query", null), Sort.valueOf((String) map.get("sort")), map.containsKey("tier") ? Rarity.getRarity((String) map.get("tier")) : null, Type.valueOf((String) map.get("type")));
    }

    public enum Sort {
        HIGHEST_BID("Highest Bid"),
        LOWEST_BID("Lowest Bid"),
        ENDING_SOON("Ending soon"),
        MOST_BIDS("Most Bids");

        private final String display;

        Sort(final String display) {
            this.display = display;
        }

        public String getDisplay() {
            return this.display;
        }

        public Sort previous() {
            final int prev = this.ordinal() - 1;
            if (prev < 0) {
                return values()[values().length - 1];
            }
            return values()[prev];
        }

        public Sort next() {
            final int nex = this.ordinal() + 1;
            if (nex > values().length - 1) {
                return values()[0];
            }
            return values()[nex];
        }
    }

    public enum Type {
        SHOW_ALL("Show All"),
        BIN_ONLY("BIN Only"),
        AUCTIONS_ONLY("Auctions Only");

        private final String display;

        Type(final String display) {
            this.display = display;
        }

        public String getDisplay() {
            return this.display;
        }

        public Type previous() {
            final int prev = this.ordinal() - 1;
            if (prev < 0) {
                return values()[values().length - 1];
            }
            return values()[prev];
        }

        public Type next() {
            final int nex = this.ordinal() + 1;
            if (nex > values().length - 1) {
                return values()[0];
            }
            return values()[nex];
        }
    }
}
