/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 */
package net.hypixel.skyblock.gui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;
import net.hypixel.skyblock.features.collection.ItemCollection;
import net.hypixel.skyblock.features.skill.CombatSkill;
import net.hypixel.skyblock.features.skill.EnchantingSkill;
import net.hypixel.skyblock.features.skill.FarmingSkill;
import net.hypixel.skyblock.features.skill.ForagingSkill;
import net.hypixel.skyblock.features.skill.MiningSkill;
import net.hypixel.skyblock.features.skill.Skill;
import net.hypixel.skyblock.gui.GUI;
import net.hypixel.skyblock.gui.GUIClickableItem;
import net.hypixel.skyblock.gui.GUIOpenEvent;
import net.hypixel.skyblock.gui.GUIType;
import net.hypixel.skyblock.gui.SkillDetails;
import net.hypixel.skyblock.user.User;
import net.hypixel.skyblock.util.SUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class SkillMenuGUI
extends GUI {
    public SkillMenuGUI() {
        super("Your Skills", 54);
    }

    @Override
    public void onOpen(GUIOpenEvent e) {
        this.fill(BLACK_STAINED_GLASS_PANE);
        Player player = e.getPlayer();
        User user = User.getUser(player.getUniqueId());
        this.set(GUIClickableItem.getCloseItem(49));
        AtomicInteger found = new AtomicInteger();
        Collection<ItemCollection> collections = ItemCollection.getCollections();
        for (ItemCollection collection : collections) {
            if (user.getCollection(collection) <= 0) continue;
            found.incrementAndGet();
        }
        this.set(GUIClickableItem.createGUIOpenerItem(GUIType.SKYBLOCK_MENU, player, ChatColor.GREEN + "Go Back", 48, Material.ARROW, ChatColor.GRAY + "To SkyBlock Menu"));
        this.set(4, SUtil.getStack(ChatColor.GREEN + "Your Skills", Material.DIAMOND_SWORD, (short)0, 1, ChatColor.GRAY + "View your Skill progression and", ChatColor.GRAY + "rewards."));
        this.set(SkillMenuGUI.createCollectionClickable(new SkillDetails(FarmingSkill.INSTANCE, player, 1), FarmingSkill.INSTANCE, Material.GOLD_HOE, 19, player));
        this.set(SkillMenuGUI.createCollectionClickable(new SkillDetails(MiningSkill.INSTANCE, player, 1), MiningSkill.INSTANCE, Material.STONE_PICKAXE, 20, player));
        this.set(SkillMenuGUI.createCollectionClickable(new SkillDetails(CombatSkill.INSTANCE, player, 1), CombatSkill.INSTANCE, Material.STONE_SWORD, 21, player));
        this.set(SkillMenuGUI.createCollectionClickable(new SkillDetails(ForagingSkill.INSTANCE, player, 1), ForagingSkill.INSTANCE, Material.SAPLING, (short)3, 22, player));
        this.set(SkillMenuGUI.createCollectionClickable(null, null, Material.FISHING_ROD, 23, player));
        this.set(SkillMenuGUI.createCollectionClickable(new SkillDetails(EnchantingSkill.INSTANCE, player, 1), EnchantingSkill.INSTANCE, Material.ENCHANTMENT_TABLE, 24, player));
        this.set(SkillMenuGUI.createCollectionClickable(null, null, Material.BREWING_STAND_ITEM, 25, player));
    }

    private static GUIClickableItem createCollectionClickable(GUI gui, Skill skill, Material icon, short data, int slot, Player player) {
        User user = User.getUser(player.getUniqueId());
        ArrayList<String> l = new ArrayList<String>();
        if (skill != null) {
            for (String line : skill.getDescription()) {
                l.add(ChatColor.GRAY + line);
            }
        }
        if (l.size() == 0) {
            l.add(ChatColor.GRAY + "Coming at a later date!");
        } else {
            l.add(" ");
        }
        double xp = skill != null ? user.getSkillXP(skill) : 0.0;
        int level = skill != null ? Skill.getLevel(xp, skill.hasSixtyLevels()) : 0;
        String name = ChatColor.RED + "Coming Soon!";
        if (skill != null && (level < 50 && !skill.hasSixtyLevels() || level < 60 && skill.hasSixtyLevels())) {
            name = skill.getName();
            int nextLevel = level + 1;
            String numeral = SUtil.toRomanNumeral(nextLevel);
            double nextXP = Skill.getNextOverallXPGoal(xp, skill.hasSixtyLevels());
            l.add(SUtil.createProgressText("Progress to Level " + numeral, xp, nextXP));
            l.add(SUtil.createSLineProgressBar(20, ChatColor.DARK_GREEN, xp, nextXP));
            l.add(" ");
            l.add(ChatColor.GRAY + "Level " + numeral + " Rewards:");
            for (String line2 : skill.getRewardLore(nextLevel, nextLevel, false)) {
                l.add("  " + line2);
            }
            l.add(" ");
        } else if (skill != null) {
            name = skill.getName();
        }
        if (skill != null) {
            l.add(ChatColor.YELLOW + "Click to view!");
        }
        return GUIClickableItem.createGUIOpenerItem(gui, player, ChatColor.GREEN + name + (level != 0 ? " " + SUtil.toRomanNumeral(level) : ""), slot, icon, data, SUtil.toArray(l, String.class));
    }

    private static GUIClickableItem createCollectionClickable(GUI gui, Skill skill, Material icon, int slot, Player player) {
        return SkillMenuGUI.createCollectionClickable(gui, skill, icon, (short)0, slot, player);
    }
}

