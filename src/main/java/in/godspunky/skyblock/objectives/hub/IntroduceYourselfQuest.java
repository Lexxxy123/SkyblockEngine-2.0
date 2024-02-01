package in.godspunky.skyblock.objectives.hub;


import in.godspunky.skyblock.Skyblock;
import in.godspunky.skyblock.features.npc.NPC;
import in.godspunky.skyblock.features.npc.NPCHandler;
import in.godspunky.skyblock.objectives.Objective;
import in.godspunky.skyblock.objectives.QuestLine;
import in.godspunky.skyblock.user.User;
import in.godspunky.skyblock.util.SUtil;
import lombok.val;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class IntroduceYourselfQuest extends QuestLine {

    public IntroduceYourselfQuest() {
        super("introduce_yourself", "Introduce Yourself", new TalkToVillagersObjective());
    }

    @Override
    public void onEnable() {
        World world = Bukkit.getWorld("world");
        registerVillager("leo", new NPC("Leo", true, false, true, Villager.Profession.BUTCHER,
                new Location(world, -7, 70, -75),
                (p) -> {
                    sendDelayedMessages(p, "Leo",
                            "You can unlock Leaflet Armor by progressing through your Oak Log Collection",
                            "There is a Forest west of the Village where you can gather Oak Logs",
                            "To check your Collection progress and rewards, open the Collection Menu in your Skyblock Menu");
                }, "", ""));

        registerVillager("tom", new NPC("Tom", true, false, true, Villager.Profession.BUTCHER,
                new Location(world, -27, 70, -49),
                (p) -> {
                    sendDelayedMessages(p, "Tom",
                            "I will teach you the Promising Axe Recipe to get you started!",
                            "All Skyblock recipes can be found by opening the Recipe Book in your Skyblock Menu");
                }, "", ""));

        registerVillager("jamie", new NPC("Jamie", true, false, true, Villager.Profession.BUTCHER,
                new Location(world, -35, 70, -36),
                (p) -> {
                    sendDelayedMessages(p, "Jamie",
                            "You might have noticed that you have a Mana bar!",
                            "Some items have mysterious properties called Abilities",
                            "Abilities use your Mana as a resource. Here take this Rogue Sword. I don't need it!");
                }, "", ""));

        registerVillager("andrew", new NPC("Andrew", true, false, true, Villager.Profession.BUTCHER,
                new Location(world, 38, 68, -46),
                (p) -> {
                    sendDelayedMessages(p, "Andrew",
                            "If you find a bug or exploit, remember to report it on the forums!");
                }, "", ""));

        registerVillager("jack", new NPC("Jack", true, false, true, Villager.Profession.BUTCHER,
                new Location(world, -0, 70, -54),
                (p) -> {
                    sendDelayedMessages(p, "Jack",
                            "Your SkyBlock Profile in your SkyBlock Menu shows details about your current stats!",
                            "Equipped armor, weapons, and accessories in your inventory all improve your stats.",
                            "Additionally, leveling your Skills can permanently boost some of your stats!",
                            "The higher your ❤ Health stat, the faster your health will regenerate!");
                }, "", ""));

        registerVillager("vex", new NPC("Vex", true, false, true, Villager.Profession.BUTCHER,
                new Location(world, -16, 70, -81),
                (p) -> {
                    sendDelayedMessages(p, "Vex",
                            "You can shift click any player to trade with them!",
                            "Once both players are ready to trade, click on Accept trade!",
                            "You can trade with me if you want!");
                }, "", ""));

        registerVillager("stella", new NPC("Stella", true, false, true, Villager.Profession.BUTCHER,
                new Location(world, 19, 70, -99),
                (p) -> {
                    sendDelayedMessages(p, "Stella",
                            "At any time you can create a Co-op with your friends",
                            "Simply go to your Skyblock Menu where you can find the Profile Menu",
                            "Enter /coop followed by the name of all the friends you want to invite!");
                }, "", ""));

        registerVillager("duke", new NPC("Duke", true, false, true, Villager.Profession.BUTCHER,
                new Location(world, -6, 70, -89),
                (p) -> {
                    sendDelayedMessages(p, "Duke",
                            "Are you new here? As you can see there is a lot to explore!",
                            "My advice is to start by visiting the Farm, or the Coal Mine both North of here.",
                            "If you do need some wood, the best place to get some is West of the Village!");
                }, "", ""));

        registerVillager("felix", new NPC("Felix", true, false, true, Villager.Profession.BUTCHER,
                new Location(world, -25, 68, -103),
                (p) -> {
                    sendDelayedMessages(p, "Felix",
                            "You can access your Ender Chest in your SkyBlock Menu.",
                            "Store items in this chest and access them at any time!");
                }, "", ""));

        registerVillager("lynn", new NPC("Lynn", true, false, true, Villager.Profession.BUTCHER,
                new Location(world, -21, 68, -124),
                (p) -> {
                    sendDelayedMessages(p, "Lynn",
                            "If you ever get lost during a quest, open your Quest Log in your SkyBlock Menu!");
                }, "", ""));

        registerVillager("liam", new NPC("Liam", true, false, true, Villager.Profession.BUTCHER,
                new Location(world, -75, 70, -107),
                (p) -> {
                    sendDelayedMessages(p, "Liam",
                            "One day those houses in the Village will be rentable for Coins!",
                            "Anyone will be able to rent them, players, co-ops, even Guilds!");
                }, "", ""));

        registerVillager("ryu", new NPC("Ryu", true, false, true, Villager.Profession.BUTCHER,
                new Location(world, 25, 70, -116),
                (p) -> {
                    sendDelayedMessages(p, "Ryu",
                            "There are 7 Skills in Skyblock!",
                            "Farming, Mining, Foraging, Fishing, Combat, Enchanting and Alchemy",
                            "You can access yours skills through your Skyblock Menu");
                }, "", ""));

    }

        private void registerVillager(String id, NPC npc){
            Skyblock skyblock = Skyblock.getPlugin();
            NPCHandler npcHandler = skyblock.getNpcHandler();

            npcHandler.registerNPC(id, npc);
        }

    @Override
    protected List<String> getRewards() {
        return Collections.singletonList(ChatColor.DARK_GRAY + "+" + ChatColor.GOLD + 50 + " &7Coins");
    }

    @Override
    protected boolean hasCompletionMessage() {
        return true;
    }

    @Override
    protected void reward(User player) {
        player.addCoins(50);
    }

    private void sendDelayedMessages(Player player, String npc, String... messages) {
        sendDelayedMessages(player, npc, (p) -> {}, messages);
    }

    private void sendDelayedMessages(Player player, String npc, Consumer<Player> action, String... messages) {
        List<String> talked = (List<String>) User.getUser(player.getUniqueId()).getValue();
        User skyblockPlayer = User.getUser(player.getUniqueId());

        if (skyblockPlayer.isinteracting()) return;

        if (talked.contains(npc)) return;

        skyblockPlayer.setisinteracting(true);

        for (int i = 0; i < messages.length; i++) {
            String message = messages[i];
            sendDelayedMessage(player, npc, message, i);

            if (i == messages.length - 1) {
                SUtil.delay(() -> {
                    action.accept(player);

                    if(!talked.contains(npc)) {
                        User.getUser(player.getUniqueId()).setValue(npc);
                    }

                    if (talked.size() == 12) {
                        complete(player);
                    }
                }, (i + 1) * 30);
            }
        }

        SUtil.delay(() -> skyblockPlayer.setisinteracting(false), messages.length * 20);
    }

    private static void sendDelayedMessage(Player player, String npc, String message, int delay) {
        SUtil.delay(() -> {
            player.sendMessage(ChatColor.YELLOW + "[NPC] " + npc + ChatColor.WHITE + ": " + message);
            player.playSound(player.getLocation(), Sound.VILLAGER_YES, 10, 1);
        }, delay * 20);
    }

    private static class TalkToVillagersObjective extends Objective {

        public TalkToVillagersObjective() {
            super("talk_to_villagers", "Explore Hub");
        }

        @Override
        public String getSuffix(User player) {
            return ChatColor.GRAY + "(" + ChatColor.YELLOW + player.getValue().size()
                    + ChatColor.GRAY + "/" + ChatColor.GREEN + "12" + ChatColor.GRAY + ")";
        }
    }
}
