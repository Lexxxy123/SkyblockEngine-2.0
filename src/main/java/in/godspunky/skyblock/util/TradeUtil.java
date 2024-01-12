package in.godspunky.skyblock.util;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class TradeUtil {
    public static final HashMap<UUID, Boolean> trading;
    private static HashMap<UUID, UUID> tradeReq;

    static {
        TradeUtil.tradeReq = new HashMap<UUID, UUID>();
        trading = new HashMap<UUID, Boolean>();
    }

    public static boolean hasRequest(final Player p, final Player requester) {
        return TradeUtil.tradeReq.containsKey(requester.getUniqueId()) && TradeUtil.tradeReq.get(requester.getUniqueId()) == p.getUniqueId();
    }

    public static void requestTrade(final Player requester, final Player p) {
        TradeUtil.tradeReq.put(requester.getUniqueId(), p.getUniqueId());
    }

    public static void resetTrade(final Player requester) {
        TradeUtil.tradeReq.remove(requester.getUniqueId());
    }

    public static boolean isTrading(final Player req) {
        if (!TradeUtil.trading.containsKey(req.getUniqueId())) {
            TradeUtil.trading.put(req.getUniqueId(), false);
        }
        return TradeUtil.trading.get(req.getUniqueId());
    }
}
