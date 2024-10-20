/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.md_5.bungee.api.ChatColor
 *  net.md_5.bungee.api.chat.ClickEvent
 *  net.md_5.bungee.api.chat.ClickEvent$Action
 *  net.md_5.bungee.api.chat.ComponentBuilder
 *  net.md_5.bungee.api.chat.HoverEvent
 *  net.md_5.bungee.api.chat.HoverEvent$Action
 *  net.md_5.bungee.api.chat.TextComponent
 *  org.bukkit.entity.Player
 */
package net.hypixel.skyblock.command;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.UUID;
import javax.xml.bind.DatatypeConverter;
import net.hypixel.skyblock.command.CommandParameters;
import net.hypixel.skyblock.command.CommandSource;
import net.hypixel.skyblock.command.SCommand;
import net.hypixel.skyblock.features.ranks.PlayerRank;
import net.hypixel.skyblock.user.User;
import net.hypixel.skyblock.util.SUtil;
import net.hypixel.skyblock.util.Sputnik;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

@CommandParameters(description="", aliases="api", permission=PlayerRank.DEFAULT)
public class APICommand
extends SCommand {
    @Override
    public void run(CommandSource sender, String[] args) {
        Player player = sender.getPlayer();
        if (player == null) {
            this.send("&cConsole sender cannot execute this command!");
            return;
        }
        User user = User.getUser(player.getUniqueId());
        if (user.isCooldownAPI()) {
            this.send("&cPlease wait 30 seconds before requesting the API key!");
            return;
        }
        user.setCooldownAPI(true);
        SUtil.delay(() -> user.setCooldownAPI(false), 600L);
        ComponentBuilder message = new ComponentBuilder("");
        String APIKey = "An Error Occured!";
        try {
            APIKey = APICommand.generateAPIKey(player.getUniqueId());
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        }
        String usage = APIKey;
        message.append(Sputnik.trans("&aYour &aAPI &akey &ais &b" + APIKey)).event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, usage)).event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText((String)(ChatColor.YELLOW + "Click to put key into chat so you can copy!"))));
        player.spigot().sendMessage(message.create());
    }

    public static String generateAPIKey(UUID uuid) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(APICommand.xorString(uuid.toString(), "D7qjI59cOcuoEDEQd4Cs"));
        byte[] digest = md5.digest();
        return uuid + ":" + Base64.getEncoder().encodeToString(APICommand.xorString(DatatypeConverter.printHexBinary(digest).toUpperCase().substring(0, 10), "AWF6GGDnWJ54TErdQdsw"));
    }

    private static byte[] xorString(String s2, String key) {
        byte[] bytes = new byte[s2.length()];
        for (int i2 = 0; i2 < s2.length(); ++i2) {
            bytes[i2] = (byte)(s2.charAt(i2) ^ key.charAt(i2 % key.length()));
        }
        return bytes;
    }
}

