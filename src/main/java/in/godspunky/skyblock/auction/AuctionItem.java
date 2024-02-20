package in.godspunky.skyblock.auction;

import in.godspunky.skyblock.SkyBlock;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import in.godspunky.skyblock.config.Config;
import in.godspunky.skyblock.item.SItem;
import in.godspunky.skyblock.user.AuctionSettings;
import in.godspunky.skyblock.user.User;
import in.godspunky.skyblock.util.SUtil;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AuctionItem {
    private static final Map<UUID, AuctionItem> AUCTION_ITEM_CACHE;
    private static final SkyBlock plugin;
    private static final File AUCTION_ITEM_FOLDER;
    private Config config;
    private UUID uuid;
    private SItem item;
    private long starter;
    private List<AuctionBid> bids;
    private long end;
    private UUID owner;
    private final List<UUID> participants;
    private boolean bin;

    private AuctionItem(final UUID uuid, final SItem item, final long starter, final long end, final UUID owner, final List<UUID> participants, final boolean bin) {
        this.uuid = uuid;
        this.item = item;
        this.starter = starter;
        this.bids = new ArrayList<AuctionBid>();
        this.end = end;
        this.owner = owner;
        this.participants = participants;
        this.bin = bin;
        if (!AuctionItem.AUCTION_ITEM_FOLDER.exists()) {
            AuctionItem.AUCTION_ITEM_FOLDER.mkdirs();
        }
        final String path = uuid.toString() + ".yml";
        final File configFile = new File(AuctionItem.AUCTION_ITEM_FOLDER, path);
        boolean save = false;
        try {
            if (!configFile.exists()) {
                save = true;
                configFile.createNewFile();
            }
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
        this.config = new Config(AuctionItem.AUCTION_ITEM_FOLDER, path);
        AuctionItem.AUCTION_ITEM_CACHE.put(uuid, this);
        if (save) {
            this.save();
        }
        this.load();
    }

    public void load() {
        this.uuid = UUID.fromString(this.config.getString("uuid"));
        this.item = (SItem) this.config.get("item");
        this.starter = this.config.getLong("starter");
        this.bids = (List) this.config.get("bids");
        this.owner = UUID.fromString(this.config.getString("owner"));
        final List<String> strings = this.config.getStringList("participants");
        for (final String string : strings) {
            this.participants.add(UUID.fromString(string));
        }
        this.end = this.config.getLong("end");
        this.bin = this.config.getBoolean("bin");
    }

    public void save() {
        this.config.set("uuid", this.uuid.toString());
        this.config.set("item", this.item);
        this.config.set("starter", this.starter);
        this.config.set("bids", this.bids);
        this.config.set("owner", this.owner.toString());
        final List<String> strings = new ArrayList<String>();
        for (final UUID uuid : this.participants) {
            strings.add(uuid.toString());
        }
        this.config.set("participants", strings);
        this.config.set("end", this.end);
        this.config.set("bin", this.bin);
        this.config.save();
    }

    public boolean delete() {
        this.config = null;
        AuctionItem.AUCTION_ITEM_CACHE.remove(this.uuid);
        return new File(AuctionItem.AUCTION_ITEM_FOLDER, this.uuid.toString() + ".yml").delete();
    }

    public User getOwner() {
        return User.getUser(this.owner);
    }

    public boolean isExpired() {
        return System.currentTimeMillis() >= this.end;
    }

    public User getTopBidder() {
        UUID uuid = null;
        long b = 0L;
        for (final AuctionBid bid : this.bids) {
            if (bid.getAmount() > b) {
                uuid = bid.getBidder();
                b = bid.getAmount();
            }
        }
        return User.getUser(uuid);
    }

    public AuctionBid getTopBid() {
        AuctionBid b = null;
        long l = 0L;
        for (final AuctionBid bid : this.bids) {
            if (bid.getAmount() > l) {
                l = bid.getAmount();
                b = bid;
            }
        }
        return b;
    }

    public long getTopBidAmount() {
        long b = 0L;
        for (final AuctionBid bid : this.bids) {
            if (bid.getAmount() > b) {
                b = bid.getAmount();
            }
        }
        return b;
    }

    public AuctionBid getRecentBid() {
        return (this.bids.size() == 0) ? null : this.bids.get(this.bids.size() - 1);
    }

    public long nextBid() {
        final long top = this.getTopBidAmount();
        if (top == 0L) {
            return this.starter;
        }
        return Math.round(top * 1.15);
    }

    public AuctionBid getBid(final UUID uuid) {
        for (int i = this.bids.size() - 1; i >= 0; --i) {
            final AuctionBid bid = this.bids.get(i);
            if (bid.getBidder().equals(uuid)) {
                return bid;
            }
        }
        return null;
    }

    public AuctionBid getBid(final User user) {
        return this.getBid(user.getUuid());
    }

    public void claim(final Player player) {
        if (!this.participants.contains(player.getUniqueId())) {
            return;
        }
        final User user = User.getUser(player.getUniqueId());
        if (player.getUniqueId().equals(this.owner)) {
            if (this.bids.size() == 0) {
                player.getInventory().addItem(this.item.getStack());
            } else {
                user.addCoins(this.getTopBidAmount());
            }
            this.removeParticipant(user.getUuid());
            return;
        }
        final AuctionBid top = this.getTopBid();
        if (top == null) {
            return;
        }
        final AuctionBid bid = this.getBid(user);
        if (bid == null) {
            return;
        }
        if (top.getBidder().equals(player.getUniqueId())) {
            player.getInventory().addItem(this.item.getStack());
        } else {
            user.addCoins(bid.getAmount());
        }
        this.removeParticipant(player.getUniqueId());
    }

    public void removeParticipant(final UUID uuid) {
        final List<UUID> participants = this.participants;
        Objects.requireNonNull(uuid);
        participants.removeIf(uuid::equals);
        if (this.participants.size() == 0) {
            this.delete();
        }
    }

    public void bid(final User user, final long amount) {
        if (!this.participants.contains(user.getUuid())) {
            this.participants.add(user.getUuid());
        }
        final AuctionBid prev = this.getBid(user);
        user.subCoins(amount - ((prev != null) ? prev.getAmount() : 0L));
        this.bids.add(new AuctionBid(user.getUuid(), amount, System.currentTimeMillis()));
        final String bidder = Bukkit.getOfflinePlayer(user.getUuid()).getName();
        if (this.bin) {
            this.messageOwner(ChatColor.GOLD + "[Auction] " + ChatColor.GREEN + bidder + ChatColor.YELLOW + " bought " + this.item.getFullName() + ChatColor.YELLOW + " for " + ChatColor.GOLD + SUtil.commaify(this.starter) + " coin" + ((this.starter != 1L) ? "s" : ""));
            this.end();
            final Player player = Bukkit.getPlayer(user.getUuid());
            if (player != null) {
                this.claim(player);
            }
            return;
        }
        this.messageOwner(ChatColor.GOLD + "[Auction] " + ChatColor.GREEN + bidder + ChatColor.YELLOW + " bid " + ChatColor.GOLD + SUtil.commaify(this.starter) + " coin" + ((this.starter != 1L) ? "s" : "") + ChatColor.YELLOW + " on " + this.item.getFullName());
        for (final UUID participant : this.participants) {
            if (participant.equals(user.getUuid())) {
                continue;
            }
            final Player player2 = Bukkit.getPlayer(participant);
            if (player2 == null) {
                continue;
            }
            final AuctionBid bid = this.getBid(participant);
            if (bid == null) {
                continue;
            }
            final long diff = amount - bid.getAmount();
            player2.sendMessage(ChatColor.GOLD + "[Auction] " + ChatColor.GREEN + bidder + ChatColor.YELLOW + " outbid you by " + ChatColor.GOLD + diff + " coin" + ((diff != 1L) ? "s" : "") + ChatColor.YELLOW + " for " + this.item.getFullName());
        }
    }

    public void messageOwner(final String message) {
        final User ou = this.getOwner();
        final Player owner = Bukkit.getPlayer(ou.getUuid());
        if (owner != null) {
            owner.sendMessage(message);
        }
    }

    public void end() {
        this.end = System.currentTimeMillis() - 1L;
    }

    public long getRemaining() {
        return Math.max(0L, this.end - System.currentTimeMillis());
    }

    public ItemStack getDisplayItem(final boolean inspect, final boolean yourAuction) {
        final ItemStack stack = this.item.getStack().clone();
        final ItemMeta meta = stack.getItemMeta();
        final List<String> lore = meta.getLore();
        lore.add(ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH + "-----------------");
        lore.add(ChatColor.GRAY + "Seller: " + ChatColor.GREEN + Bukkit.getOfflinePlayer(this.owner).getName());
        final User top = this.getTopBidder();
        if (this.isBin()) {
            lore.add(ChatColor.GRAY + "Buy it now: " + ChatColor.GOLD + SUtil.commaify(this.starter) + " coins");
        } else if (top == null) {
            lore.add(ChatColor.GRAY + "Starting bid: " + ChatColor.GOLD + SUtil.commaify(this.starter) + " coins");
        } else {
            lore.add(ChatColor.GRAY + "Bids: " + ChatColor.GREEN + this.bids.size() + " bids");
            lore.add(" ");
            lore.add(ChatColor.GRAY + "Top bid: " + ChatColor.GOLD + SUtil.commaify(this.getTopBidAmount()) + " coins");
            lore.add(ChatColor.GRAY + "Bidder: " + ChatColor.GREEN + Bukkit.getOfflinePlayer(top.getUuid()).getName());
        }
        if (yourAuction) {
            lore.add(" ");
            lore.add(ChatColor.GREEN + "This is your own auction!");
        }
        lore.add(" ");
        lore.add(ChatColor.GRAY + "Ends in: " + ChatColor.YELLOW + SUtil.getAuctionFormattedTime(this.getRemaining()));
        if (inspect) {
            lore.add(" ");
            lore.add(ChatColor.YELLOW + "Click to inspect!");
        }
        meta.setLore(lore);
        stack.setItemMeta(meta);
        return stack;
    }

    @Override
    public String toString() {
        return "AuctionItem{uuid=" + this.uuid.toString() + ", item=" + this.item.toString() + ", bids=" + this.bids.toString() + ", end=" + this.end + "}";
    }

    public static AuctionItem createAuction(final SItem item, final long starter, final long end, final UUID owner, final boolean bin) {
        return new AuctionItem(UUID.randomUUID(), item, starter, end, owner, new ArrayList<UUID>(Collections.singletonList(owner)), bin);
    }

    public static AuctionItem getAuction(final UUID uuid) {
        if (AuctionItem.AUCTION_ITEM_CACHE.containsKey(uuid)) {
            return AuctionItem.AUCTION_ITEM_CACHE.get(uuid);
        }
        if (!new File(AuctionItem.AUCTION_ITEM_FOLDER, uuid.toString() + ".yml").exists()) {
            return null;
        }
        return new AuctionItem(uuid, null, 0L, 0L, null, new ArrayList<UUID>(), false);
    }

    public static Collection<AuctionItem> getAuctions() {
        if (AuctionItem.AUCTION_ITEM_FOLDER == null || !AuctionItem.AUCTION_ITEM_FOLDER.exists()) {
            return new ArrayList<AuctionItem>();
        }
        return AuctionItem.AUCTION_ITEM_CACHE.values();
    }

    public static List<AuctionItem> getOwnedAuctions(final String name) {
        final OfflinePlayer player = Bukkit.getOfflinePlayer(name);
        if (player == null) {
            return null;
        }
        final User user = User.getUser(player.getUniqueId());
        if (user == null) {
            return null;
        }
        return user.getAuctions();
    }

    public static CompletableFuture<List<AuctionItem>> search(final AuctionSettings settings) {
        return CompletableFuture.supplyAsync(() -> {
            final Stream<AuctionItem> items = getAuctions().stream();
            final Stream<AuctionItem> items2 = items.filter(item -> !item.isExpired());
            Stream<AuctionItem> items3 = items2.filter(item -> item.getItem().getType().getStatistics().getCategory() == settings.getCategory());
            if (settings.getQuery() != null) {
                items3 = items3.filter(item -> {
                    final String query = settings.getQuery().toLowerCase();
                    final String name = item.getItem().getType().getDisplayName(item.getItem().getType().getData()).toLowerCase();
                    final String lore = item.getItem().getLore().asBukkitLore().toString().toLowerCase();
                    return query.contains(name) || query.contains(lore) || query.contains(item.getItem().getType().name().toLowerCase());
                });
            }
            Stream<AuctionItem> items4 = items3.sorted((i1, i2) -> {
                switch (settings.getSort()) {
                    case HIGHEST_BID:
                        return Long.compare(i1.getTopBidAmount(), i2.getTopBidAmount());
                    case LOWEST_BID:
                        if (i1.getTopBidAmount() < i2.getTopBidAmount()) {
                            return 1;
                        } else if (i2.getTopBidAmount() > i1.getTopBidAmount()) {
                            return -1;
                        } else {
                            return 0;
                        }
                    case MOST_BIDS:
                        return Long.compare(i1.getBids().size(), i2.getBids().size());
                    case ENDING_SOON:
                        return Long.compare(i2.end - System.currentTimeMillis(), i2.end - System.currentTimeMillis());
                    default:
                        return 0;
                }
            });
            if (settings.getTier() != null) {
                items4 = items4.filter(item -> item.getItem().getRarity() == settings.getTier());
            }
            switch (settings.getType()) {
                case AUCTIONS_ONLY:
                    items4 = items4.filter(item -> !item.isBin());
                    break;
                case BIN_ONLY:
                    items4 = items4.filter(AuctionItem::isBin);
                    break;
            }
            return items4.collect(Collectors.toList());
        });
    }

    public static void loadAuctionsFromDisk() {
        if (!AuctionItem.AUCTION_ITEM_FOLDER.exists()) {
            return;
        }
        for (final File f : Objects.requireNonNull(AuctionItem.AUCTION_ITEM_FOLDER.listFiles())) {
            String name = f.getName();
            Label_0092:
            {
                if (name.endsWith(".yml")) {
                    name = name.substring(0, name.length() - 4);
                    UUID uuid;
                    try {
                        uuid = UUID.fromString(name);
                    } catch (final IllegalArgumentException ex) {
                        break Label_0092;
                    }
                    getAuction(uuid);
                }
            }
        }
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public SItem getItem() {
        return this.item;
    }

    public long getStarter() {
        return this.starter;
    }

    public List<AuctionBid> getBids() {
        return this.bids;
    }

    public long getEnd() {
        return this.end;
    }

    public List<UUID> getParticipants() {
        return this.participants;
    }

    public boolean isBin() {
        return this.bin;
    }

    static {
        AUCTION_ITEM_CACHE = new HashMap<UUID, AuctionItem>();
        plugin = SkyBlock.getPlugin();
        AUCTION_ITEM_FOLDER = new File(AuctionItem.plugin.getDataFolder(), "./auctions");
    }
}
