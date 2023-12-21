package in.godspunky.skyblock.command;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import in.godspunky.skyblock.user.User;
import in.godspunky.skyblock.util.SUtil;
import in.godspunky.skyblock.util.Sputnik;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.UUID;

@CommandParameters(description = "", aliases = "api", permission = "")
public class APICommand extends SCommand {
    @Override
    public void run(final CommandSource sender, final String[] args) {
        final Player player = sender.getPlayer();
        if (player == null) {
            this.send("&cConsole sender cannot execute this command!");
            return;
        }
        final User user = User.getUser(player.getUniqueId());
        if (user.isCooldownAPI()) {
            this.send("&cPlease wait 30 seconds before requesting the API key!");
            return;
        }
        user.setCooldownAPI(true);
        SUtil.delay(() -> user.setCooldownAPI(false), 600L);
        final ComponentBuilder message = new ComponentBuilder("");
        String APIKey = "An Error Occured!";
        try {
            APIKey = generateAPIKey(player.getUniqueId());
        } catch (final NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        }
        final String usage = APIKey;
        message.append(Sputnik.trans("&aYour &aAPI &akey &ais &b" + APIKey)).event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, usage)).event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(ChatColor.YELLOW + "Click to put key into chat so you can copy!")));
        player.spigot().sendMessage(message.create());
    }

    public static String generateAPIKey(final UUID uuid) throws NoSuchAlgorithmException {
        final MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(xorString(uuid.toString(), "D7qjI59cOcuoEDEQd4Cs"));
        final byte[] digest = md5.digest();
        return uuid + ":" + Base64.getEncoder().encodeToString(xorString(DatatypeConverter.printHexBinary(digest).toUpperCase().substring(0, 10), "AWF6GGDnWJ54TErdQdsw"));
    }

    private static byte[] xorString(final String s, final String key) {
        final byte[] bytes = new byte[s.length()];
        for (int i = 0; i < s.length(); ++i) {
            bytes[i] = (byte) (s.charAt(i) ^ key.charAt(i % key.length()));
        }
        return bytes;
    }
}
