package net.hypixel.skyblock.item.oddities;

import net.hypixel.skyblock.command.BatphoneCommand;
import net.hypixel.skyblock.item.*;
import net.hypixel.skyblock.features.sequence.SoundSequenceType;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import net.hypixel.skyblock.util.SUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class MaddoxBatphone implements SkullStatistics, MaterialFunction, Ability {
    private static final List<String> SUCCESSFUL_RESPONSES;
    private static final List<String> FAILED_RESPONSES;
    public static final List<UUID> RING_COOLDOWN;
    public static final List<UUID> CALL_COOLDOWN;

    @Override
    public String getURL() {
        return "9336d7cc95cbf6689f5e8c954294ec8d1efc494a4031325bb427bc81d56a484d";
    }

    @Override
    public String getDisplayName() {
        return "Maddox Batphone";
    }

    @Override
    public Rarity getRarity() {
        return Rarity.UNCOMMON;
    }

    @Override
    public GenericItemType getType() {
        return GenericItemType.ITEM;
    }

    @Override
    public String getAbilityName() {
        return "Whassup?";
    }

    @Override
    public String getAbilityDescription() {
        return "Lets you call Maddox, when he's not busy.";
    }

    @Override
    public int getAbilityCooldownTicks() {
        return 0;
    }

    @Override
    public int getManaCost() {
        return 0;
    }

    @Override
    public boolean displayUsage() {
        return false;
    }

    @Override
    public boolean isStackable() {
        return false;
    }


    @Override
    public void onAbilityUse(Player player, SItem sItem) {
        if (player.getWorld().getName().contains("f6")) {
            player.playSound(player.getLocation(), Sound.WOOD_CLICK, 1.0f, 1.0f);
            player.sendMessage(ChatColor.RED + "No service here!");
            return;
        }
        if (RING_COOLDOWN.contains(player.getUniqueId())) {
            return;
        }
        RING_COOLDOWN.add(player.getUniqueId());
        SUtil.delay(() -> RING_COOLDOWN.remove(player.getUniqueId()), 52L);
        SoundSequenceType.MADDOX_BATPHONE.play(player);
        player.sendMessage(ChatColor.YELLOW + "\u2706 Ring...");
        SUtil.delay(() -> player.sendMessage(ChatColor.YELLOW + "\u2706 Ring... Ring..."), 18L);
        SUtil.delay(() -> player.sendMessage(ChatColor.YELLOW + "\u2706 Ring... Ring... Ring..."), 35L);
        if (CALL_COOLDOWN.contains(player.getUniqueId())) {
            SUtil.delay(() -> player.sendMessage(ChatColor.RED + "\u2706 " + SUtil.getRandom(FAILED_RESPONSES)), 52L);
            return;
        }
        TextComponent message = new TextComponent(ChatColor.DARK_GREEN + " " + ChatColor.BOLD + "[OPEN MENU]");
        message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent[]{new TextComponent(ChatColor.YELLOW + "Click!")}));
        String key = UUID.randomUUID().toString();
        BatphoneCommand.KEYS.add(key);
        SUtil.delay(() -> BatphoneCommand.KEYS.remove(key), 460L);
        message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/batphone " + BatphoneCommand.ACCESS_KEY + " " + key));
        SUtil.delay(() -> player.spigot().sendMessage(new TextComponent(new TextComponent(ChatColor.GREEN + "\u2706 " + SUtil.getRandom(SUCCESSFUL_RESPONSES)), message)), 52L);
    }

    static {
        SUCCESSFUL_RESPONSES = Arrays.asList("Hello?", "Someone answers!", "How does a lobster answer? Shello!", "Hey what do you need?", "You hear the line pick up...", "You again? What do you want this time?");
        FAILED_RESPONSES = Arrays.asList("Please leave your message after the beep.", "How can you tell if a bee is on the phone? You get a buzzy signal!", "The phone keeps ringing, is it broken?", "The phone picks up but it immediately hands up!", "What did the cat say on the phone? Can you hear meow?", "No answer.", "Seems like it's not picking up!", "\"Your call is important to us, please stay on the line\", so you hang up.");
        RING_COOLDOWN = new ArrayList<UUID>();
        CALL_COOLDOWN = new ArrayList<UUID>();
    }
}
