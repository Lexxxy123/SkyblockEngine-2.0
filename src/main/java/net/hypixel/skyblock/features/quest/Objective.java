package net.hypixel.skyblock.features.quest;


import lombok.Getter;
import net.hypixel.skyblock.SkyBlock;
import net.hypixel.skyblock.user.User;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

@Getter
public class Objective implements Listener {

    private final String id;
    private final String display;

    public Objective(String id, String display) {
        this.id = id;
        this.display = display;

        Bukkit.getPluginManager().registerEvents(this, SkyBlock.getPlugin());
    }

    private QuestLine getQuest() {
        return SkyBlock.getPlugin().getQuestLineHandler().getQuest(this);
    }

    public Objective getNext() {
        return getQuest().getNext(this);
    }

    public void complete(Player p) {
        User player = User.getUser(p.getUniqueId());
        Objective next = getNext();


        player.addCompletedObjectives(this.getId());

        if (getNext() == null) {
            getQuest().complete(player.toBukkitPlayer());
            return;
        }

        String message = " \n " + ChatColor.GOLD + ChatColor.BOLD + " NEW OBJECTIVE" + "\n" +
                ChatColor.WHITE + "  " + next.getDisplay() + "\n";

        player.toBukkitPlayer().playSound(player.toBukkitPlayer().getLocation(), Sound.NOTE_PLING, 10, 0);
        player.toBukkitPlayer().sendMessage(message);
        player.toBukkitPlayer().sendMessage(" ");
    }

    public String getSuffix(User player) {
        return "";
    }

    protected boolean isThisObjective(Player player) {
        User skyblockPlayer = User.getUser(player.getUniqueId());

        if (skyblockPlayer.getQuestLine() == null) return false;
        if (skyblockPlayer.getQuestLine().getObjective(skyblockPlayer) == null) return false;

        return skyblockPlayer.getQuestLine().getObjective(skyblockPlayer).getId().equals(getId());
    }

    public boolean hasSuffix(User skyblockPlayer) {
        return !getSuffix(skyblockPlayer).equals("");
    }
}