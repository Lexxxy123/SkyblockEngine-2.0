/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.Sound
 *  org.bukkit.World
 *  org.bukkit.entity.Player
 *  org.bukkit.event.inventory.InventoryClickEvent
 *  org.bukkit.inventory.ItemStack
 */
package net.hypixel.skyblock.gui;

import net.hypixel.skyblock.SkyBlock;
import net.hypixel.skyblock.gui.GUI;
import net.hypixel.skyblock.gui.GUIClickableItem;
import net.hypixel.skyblock.gui.GUIOpenEvent;
import net.hypixel.skyblock.gui.GUIType;
import net.hypixel.skyblock.user.User;
import net.hypixel.skyblock.util.SUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class WarpGUI
extends GUI {
    private SkyBlock plugin = SkyBlock.getPlugin();

    public WarpGUI() {
        super("Fast Travel", 54);
    }

    @Override
    public void onOpen(GUIOpenEvent e) {
        this.fill(BLACK_STAINED_GLASS_PANE);
        final Player player = e.getPlayer();
        User user = User.getUser(e.getPlayer().getUniqueId());
        this.set(GUIClickableItem.getCloseItem(49));
        this.set(new GUIClickableItem(){

            @Override
            public void run(InventoryClickEvent e) {
                GUIType.SKYBLOCK_MENU.getGUI().open(player);
            }

            @Override
            public int getSlot() {
                return 48;
            }

            @Override
            public ItemStack getItem() {
                return SUtil.getStack(ChatColor.GREEN + "Go Back", Material.ARROW, (short)0, 1, ChatColor.GRAY + "To SkyBlock Menu");
            }
        });
        this.set(new GUIClickableItem(){

            @Override
            public void run(InventoryClickEvent e) {
                player.sendMessage(ChatColor.RED + "Private islands are temporarily unavailable at the moment.");
                if (player.getOpenInventory() != null) {
                    player.closeInventory();
                }
            }

            @Override
            public int getSlot() {
                return 10;
            }

            @Override
            public ItemStack getItem() {
                return SUtil.getSkullURLStack(ChatColor.AQUA + "Private Island", "c9c8881e42915a9d29bb61a16fb26d059913204d265df5b439b3d792acd56", 1, ChatColor.DARK_GRAY + "/is", " ", ChatColor.GRAY + "Your very own chunk of SkyBlock.", ChatColor.GRAY + "Nice housing for your minions.", " ", ChatColor.YELLOW + "Click to warp!");
            }
        });
        this.set(new GUIClickableItem(){

            @Override
            public void run(InventoryClickEvent e) {
                World hub = Bukkit.getWorld((String)"world");
                player.teleport(new Location(Bukkit.getWorld((String)"world"), -2.5, 70.0, -68.5, 180.0f, 0.0f));
            }

            @Override
            public int getSlot() {
                return 11;
            }

            @Override
            public ItemStack getItem() {
                return SUtil.getSkullURLStack(ChatColor.AQUA + "Skyblock Hub", "cf40942f364f6cbceffcf1151796410286a48b1aeba77243e218026c09cd1", 1, ChatColor.DARK_GRAY + "/hub", " ", ChatColor.GRAY + "Where everything happens and", ChatColor.GRAY + "anything is possible.", " ", ChatColor.YELLOW + "Click to warp!");
            }
        });
        this.set(new GUIClickableItem(){

            @Override
            public void run(InventoryClickEvent e) {
                player.sendMessage(ChatColor.YELLOW + "The Dungeon hub has not been added yet. Stay tuned for updates!");
            }

            @Override
            public int getSlot() {
                return 12;
            }

            @Override
            public ItemStack getItem() {
                return SUtil.getSkullURLStack(ChatColor.GREEN + "Dungeon Hub " + ChatColor.DARK_GRAY + "- " + ChatColor.AQUA + "Spawn", "1035c528036b384c53c9c8a1a125685e16bfb369c197cc9f03dfa3b835b1aa55", 1, ChatColor.DARK_GRAY + "/dungeon_hub", " ", ChatColor.GRAY + "Group with friends and take on", ChatColor.GRAY + "challenging Dungeons.");
            }
        });
        this.set(new GUIClickableItem(){

            @Override
            public void run(InventoryClickEvent e) {
                Player player1 = (Player)e.getWhoClicked();
                World the_barn = Bukkit.getWorld((String)"world");
                player1.teleport(new Location(the_barn, 114.0, 71.0, -207.0));
            }

            @Override
            public int getSlot() {
                return 13;
            }

            @Override
            public ItemStack getItem() {
                return SUtil.getSkullURLStack(ChatColor.GREEN + "The Barn" + ChatColor.GRAY + " - " + ChatColor.AQUA + "Spawn", "4d3a6bd98ac1833c664c4909ff8d2dc62ce887bdcf3cc5b3848651ae5af6b", 1, ChatColor.DARK_GRAY + "/warp barn", " ", ChatColor.GRAY + "Collect basic farming resource", ChatColor.GRAY + "from plentiful crops or the local", ChatColor.GRAY + "animal population.", " ", ChatColor.GRAY + "Main Skill: " + ChatColor.AQUA + "Farming", ChatColor.GRAY + "Island Tier" + ChatColor.YELLOW + "I", " ", ChatColor.YELLOW + "Click to warp!");
            }
        });
        this.set(new GUIClickableItem(){

            @Override
            public void run(InventoryClickEvent e) {
                Player player1 = (Player)e.getWhoClicked();
                World park = Bukkit.getWorld((String)"world");
                player1.teleport(new Location(park, -276.0, 82.0, -12.0));
            }

            @Override
            public int getSlot() {
                return 14;
            }

            @Override
            public ItemStack getItem() {
                return SUtil.getSkullURLStack(ChatColor.GREEN + "The Park" + ChatColor.GRAY + " - " + ChatColor.AQUA + "Spawn", "b76c7f96f862243c5a6fe727aec0b8657cd2c65a463fd816c94efe4c622c055a", 1, ChatColor.DARK_GRAY + "/warp park", " ", ChatColor.GRAY + "Chop down trees and explore to", ChatColor.GRAY + "meet various characters across", ChatColor.GRAY + "different biome layers.", " ", ChatColor.GRAY + "Main Skill: " + ChatColor.AQUA + "Foraging", ChatColor.GRAY + "Island Tier" + ChatColor.YELLOW + "I", " ", ChatColor.YELLOW + "Click to warp!");
            }
        });
        this.set(new GUIClickableItem(){

            @Override
            public void run(InventoryClickEvent e) {
                Player player1 = (Player)e.getWhoClicked();
                World gold_mine = Bukkit.getWorld((String)"world");
                player1.teleport(new Location(gold_mine, -4.0, 74.0, -273.0));
            }

            @Override
            public int getSlot() {
                return 15;
            }

            @Override
            public ItemStack getItem() {
                return SUtil.getSkullURLStack(ChatColor.GREEN + "Gold Mine" + ChatColor.GRAY + " - " + ChatColor.AQUA + "Spawn", "73bc965d579c3c6039f0a17eb7c2e6faf538c7a5de8e60ec7a719360d0a857a9", 1, ChatColor.DARK_GRAY + "/warp gold", " ", ChatColor.GRAY + "our first stop for extended", ChatColor.GRAY + "mining related activities and home", ChatColor.GRAY + "to SkyBlock's local janitor Rusty.", " ", ChatColor.GRAY + "Main Skill: " + ChatColor.AQUA + "Mining", ChatColor.GRAY + "Island Tier" + ChatColor.YELLOW + "I", " ", ChatColor.YELLOW + "Click to warp!");
            }
        });
        this.set(new GUIClickableItem(){

            @Override
            public void run(InventoryClickEvent e) {
                Player player1 = (Player)e.getWhoClicked();
                World deep_caverns = Bukkit.getWorld((String)"world");
                player1.teleport(new Location(deep_caverns, -2.0, 178.0, -458.0));
            }

            @Override
            public int getSlot() {
                return 16;
            }

            @Override
            public ItemStack getItem() {
                return SUtil.getSkullURLStack(ChatColor.GREEN + "Deep Caverns" + ChatColor.GRAY + " - " + ChatColor.AQUA + "Spawn", "569a1f114151b4521373f34bc14c2963a5011cdc25a6554c48c708cd96ebfc", 1, ChatColor.DARK_GRAY + "/warp deep", " ", ChatColor.GRAY + "Collect basic farming resource", ChatColor.GRAY + "from plentiful crops or the local", ChatColor.GRAY + "animal population.", " ", ChatColor.GRAY + "Main Skill: " + ChatColor.AQUA + "Farming", ChatColor.GRAY + "Island Tier" + ChatColor.YELLOW + "I", " ", ChatColor.YELLOW + "Click to warp!");
            }
        });
        this.set(new GUIClickableItem(){

            @Override
            public void run(InventoryClickEvent e) {
                Player player1 = (Player)e.getWhoClicked();
                player1.playSound(player1.getLocation(), Sound.ENDERMAN_TELEPORT, 1.0f, 0.0f);
                player1.sendMessage("Comming Soon!");
            }

            @Override
            public int getSlot() {
                return 20;
            }

            @Override
            public ItemStack getItem() {
                return SUtil.getSkullURLStack(ChatColor.GREEN + "Dwarven Mines" + ChatColor.GRAY + " - " + ChatColor.AQUA + "Spawn", "1035c528036b384c53c9c8a1a125685e16bfb369c197cc9f03dfa3b835b1aa55", 1, ChatColor.DARK_GRAY + "/warp dwarves", " ", ChatColor.GRAY + "Discover new ores and minerals and", ChatColor.GRAY + "level up your heart of the", ChatColor.GRAY + "Mountain whilst completing", ChatColor.GRAY + "commissions from the Dwarven King", ChatColor.GRAY + "himself.", " ", ChatColor.GRAY + "Main Skill: " + ChatColor.AQUA + "Mining", ChatColor.GRAY + "Island Tier" + ChatColor.YELLOW + "III", " ", ChatColor.RED + "Comming Soon!");
            }
        });
        this.set(new GUIClickableItem(){

            @Override
            public void run(InventoryClickEvent e) {
                Player player1 = (Player)e.getWhoClicked();
                player1.playSound(player1.getLocation(), Sound.ENDERMAN_TELEPORT, 1.0f, 0.0f);
                player1.sendMessage("Comming Soon!");
            }

            @Override
            public int getSlot() {
                return 21;
            }

            @Override
            public ItemStack getItem() {
                return SUtil.getSkullURLStack(ChatColor.GREEN + "Crystal Hollows" + ChatColor.GRAY + " - " + ChatColor.AQUA + "Entrance", "1035c528036b384c53c9c8a1a125685e16bfb369c197cc9f03dfa3b835b1aa55", 1, ChatColor.DARK_GRAY + "/warp crystals", " ", ChatColor.GRAY + "A vast series of caves and random", ChatColor.GRAY + "structures with tougher Stone and", ChatColor.GRAY + "special gems!", " ", ChatColor.GRAY + "Main Skill: " + ChatColor.AQUA + "Mining", ChatColor.GRAY + "Island Tier" + ChatColor.YELLOW + "IV", " ", ChatColor.RED + "Comming Soon!");
            }
        });
        this.set(new GUIClickableItem(){

            @Override
            public void run(InventoryClickEvent e) {
                Player player1 = (Player)e.getWhoClicked();
                World spider_mine = Bukkit.getWorld((String)"world");
                player1.teleport(new Location(spider_mine, -201.0, 84.0, -232.0));
            }

            @Override
            public int getSlot() {
                return 22;
            }

            @Override
            public ItemStack getItem() {
                return SUtil.getSkullURLStack(ChatColor.GREEN + "Spider's Den" + ChatColor.GRAY + " - " + ChatColor.AQUA + "Spawn", "21c5840736229db9d9645bf9b409e73e706e3dc4fc30d78eb2079d20d929db9e", 1, ChatColor.DARK_GRAY + "/warp spider", " ", ChatColor.GRAY + "Explore a dangerous nest, discover", ChatColor.GRAY + "the Bestiary, hunt for Relics, and", ChatColor.GRAY + "fight all kinds of Spiders!", " ", ChatColor.GRAY + "Main Skill: " + ChatColor.AQUA + "Combat", ChatColor.GRAY + "Island Tier" + ChatColor.YELLOW + "I", " ", ChatColor.YELLOW + "Click to warp!");
            }
        });
        this.set(new GUIClickableItem(){

            @Override
            public void run(InventoryClickEvent e) {
                Player player1 = (Player)e.getWhoClicked();
                World the_end = Bukkit.getWorld((String)"world");
                player1.teleport(new Location(the_end, -499.0, 101.0, -275.0));
            }

            @Override
            public int getSlot() {
                return 23;
            }

            @Override
            public ItemStack getItem() {
                return SUtil.getSkullURLStack(ChatColor.GREEN + "The End" + ChatColor.GRAY + " - " + ChatColor.AQUA + "Spawn", "7840b87d52271d2a755dedc82877e0ed3df67dcc42ea479ec146176b02779a5", 1, ChatColor.DARK_GRAY + "/warp end", " ", ChatColor.GRAY + "Fight Zealots, mine End Stone, and", ChatColor.GRAY + "defeat ancient Dragons!", " ", ChatColor.GRAY + "Main Skill: " + ChatColor.AQUA + "Combat", ChatColor.GRAY + "Island Tier" + ChatColor.YELLOW + "III", " ", ChatColor.YELLOW + "Click to warp!");
            }
        });
        this.set(new GUIClickableItem(){

            @Override
            public void run(InventoryClickEvent e) {
                Player player1 = (Player)e.getWhoClicked();
                World nether = Bukkit.getWorld((String)"world");
                player1.teleport(new Location(nether, -310.0, 83.0, -381.0));
            }

            @Override
            public int getSlot() {
                return 24;
            }

            @Override
            public ItemStack getItem() {
                return SUtil.getSkullURLStack(ChatColor.GREEN + "Crimson Isle" + ChatColor.GRAY + " - " + ChatColor.AQUA + "Spawn", "721d0930bd61fea4cb9027b00e94e13d62029c524ea0b3260c747457ba1bcfa1", 1, ChatColor.DARK_GRAY + "/warp nether", " ", ChatColor.GRAY + "Fight challenging bosses, discover", ChatColor.GRAY + "new Sea Creatures, complete epic", ChatColor.GRAY + "quests, and join your favourite", ChatColor.GRAY + "faction!", " ", ChatColor.GRAY + "Main Skill: " + ChatColor.AQUA + "Combat", ChatColor.GRAY + "Island Tier" + ChatColor.YELLOW + "IV", " ", ChatColor.YELLOW + "Click to warp!");
            }
        });
        this.set(new GUIClickableItem(){

            @Override
            public void run(InventoryClickEvent e) {
                Player player1 = (Player)e.getWhoClicked();
                player1.playSound(player1.getLocation(), Sound.ENDERMAN_TELEPORT, 1.0f, 0.0f);
                player1.sendMessage("Comming Soon!");
            }

            @Override
            public int getSlot() {
                return 30;
            }

            @Override
            public ItemStack getItem() {
                return SUtil.getSkullURLStack(ChatColor.GREEN + "Garden", "1035c528036b384c53c9c8a1a125685e16bfb369c197cc9f03dfa3b835b1aa55", 1, ChatColor.DARK_GRAY + "/warp garden", " ", ChatColor.GRAY + "Spawn on your very own " + ChatColor.GREEN + "Garden.", " ", ChatColor.RED + "Comming Soon!");
            }
        });
        this.set(new GUIClickableItem(){

            @Override
            public void run(InventoryClickEvent e) {
                Player player1 = (Player)e.getWhoClicked();
                player1.playSound(player1.getLocation(), Sound.ENDERMAN_TELEPORT, 1.0f, 0.0f);
                player1.sendMessage("Comming Soon!");
            }

            @Override
            public int getSlot() {
                return 32;
            }

            @Override
            public ItemStack getItem() {
                return SUtil.getSkullURLStack(ChatColor.AQUA + "Warp to: " + ChatColor.RED + "Jerry's Workshop", "1035c528036b384c53c9c8a1a125685e16bfb369c197cc9f03dfa3b835b1aa55", 1, ChatColor.DARK_GRAY + "Teleports you to " + ChatColor.RED + "Jerry's", ChatColor.RED + "Workshop. " + ChatColor.GRAY + "Available for a", ChatColor.GRAY + "limited time!", " ", ChatColor.RED + "Comming Soon!");
            }
        });
        this.set(new GUIClickableItem(){

            @Override
            public void run(InventoryClickEvent e) {
                Player player1 = (Player)e.getWhoClicked();
                player1.playSound(player1.getLocation(), Sound.ENDERMAN_TELEPORT, 1.0f, 0.0f);
                player1.sendMessage("Comming Soon!");
            }

            @Override
            public int getSlot() {
                return 45;
            }

            @Override
            public ItemStack getItem() {
                return SUtil.getStack(ChatColor.GREEN + "Island Browser", Material.BLAZE_POWDER, (short)0, 1, ChatColor.DARK_GRAY + "Check out the most popular", ChatColor.GRAY + "islands in Skyblock! Filter by", ChatColor.GRAY + "category tags to explore various", ChatColor.GRAY + "types of islands.", " ", ChatColor.RED + "Comming Soon!");
            }
        });
        this.set(new GUIClickableItem(){

            @Override
            public void run(InventoryClickEvent e) {
                Player player1 = (Player)e.getWhoClicked();
                player1.playSound(player1.getLocation(), Sound.ENDERMAN_TELEPORT, 1.0f, 0.0f);
                player1.sendMessage("Comming Soon!");
            }

            @Override
            public int getSlot() {
                return 50;
            }

            @Override
            public ItemStack getItem() {
                return SUtil.getStack(ChatColor.GREEN + "Advanced Mode", Material.INK_SACK, (short)10, 1, ChatColor.DARK_GRAY + "Show additional convenient fast", ChatColor.GRAY + "travel options such as quick", ChatColor.GRAY + "Right-Click warping and extra", ChatColor.GRAY + "warps obtained from " + ChatColor.DARK_PURPLE + "EPIC", ChatColor.GRAY + "scrolls", " ", ChatColor.GRAY + "Enabled: " + ChatColor.GREEN + "ON", ChatColor.RED + "Comming Soon!");
            }
        });
        this.set(new GUIClickableItem(){

            @Override
            public void run(InventoryClickEvent e) {
                player.sendMessage("Gui not added yet");
            }

            @Override
            public int getSlot() {
                return 53;
            }

            @Override
            public ItemStack getItem() {
                return SUtil.getStack(ChatColor.GREEN + "Paper Icon", Material.EMPTY_MAP, (short)0, 1, ChatColor.DARK_GRAY + "Use paper icons, which may load this", ChatColor.GRAY + "menu faster on your computer.", " ", ChatColor.GRAY + "Enabled: " + ChatColor.RED + "OFF", ChatColor.YELLOW + "Click to toggle!");
            }
        });
    }
}

