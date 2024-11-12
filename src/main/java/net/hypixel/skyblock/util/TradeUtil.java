/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 */
package net.hypixel.skyblock.util;

import java.util.HashMap;
import java.util.UUID;
import org.bukkit.entity.Player;

public class TradeUtil {
    private static HashMap<UUID, UUID> tradeReq = new HashMap();
    public static final HashMap<UUID, Boolean> trading = new HashMap();

    public static boolean hasRequest(Player p2, Player requester) {
        return tradeReq.containsKey(requester.getUniqueId()) && tradeReq.get(requester.getUniqueId()) == p2.getUniqueId();
    }

    public static void requestTrade(Player requester, Player p2) {
        tradeReq.put(requester.getUniqueId(), p2.getUniqueId());
    }

    public static void resetTrade(Player requester) {
        tradeReq.remove(requester.getUniqueId());
    }

    public static boolean isTrading(Player req) {
        if (!trading.containsKey(req.getUniqueId())) {
            trading.put(req.getUniqueId(), false);
        }
        return trading.get(req.getUniqueId());
    }
}

