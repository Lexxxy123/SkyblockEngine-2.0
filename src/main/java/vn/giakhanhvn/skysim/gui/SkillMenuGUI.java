package vn.giakhanhvn.skysim.gui;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Collection;

import org.bukkit.entity.Player;
import vn.giakhanhvn.skysim.skill.EnchantingSkill;
import vn.giakhanhvn.skysim.skill.ForagingSkill;
import vn.giakhanhvn.skysim.skill.CombatSkill;
import vn.giakhanhvn.skysim.skill.MiningSkill;
import vn.giakhanhvn.skysim.skill.Skill;
import vn.giakhanhvn.skysim.skill.FarmingSkill;
import vn.giakhanhvn.skysim.util.SUtil;
import org.bukkit.Material;
import org.bukkit.ChatColor;
import vn.giakhanhvn.skysim.collection.ItemCollection;

import java.util.concurrent.atomic.AtomicInteger;

import vn.giakhanhvn.skysim.user.User;

public class SkillMenuGUI extends GUI {
    public SkillMenuGUI() {
        super("Your Skills", 54);
    }

    @Override
    public void onOpen(final GUIOpenEvent e) {
        this.fill(SkillMenuGUI.BLACK_STAINED_GLASS_PANE);
        final Player player = e.getPlayer();
        final User user = User.getUser(player.getUniqueId());
        this.set(GUIClickableItem.getCloseItem(49));
        final AtomicInteger found = new AtomicInteger();
        final Collection<ItemCollection> collections = ItemCollection.getCollections();
        for (final ItemCollection collection : collections) {
            if (user.getCollection(collection) > 0) {
                found.incrementAndGet();
            }
        }
        this.set(GUIClickableItem.createGUIOpenerItem(GUIType.SKYBLOCK_MENU, player, ChatColor.GREEN + "Go Back", 48, Material.ARROW, ChatColor.GRAY + "To SkySim Menu"));
        this.set(4, SUtil.getStack(ChatColor.GREEN + "Your Skills", Material.DIAMOND_SWORD, (short) 0, 1, ChatColor.GRAY + "View your Skill progression and", ChatColor.GRAY + "rewards."));
        this.set(createCollectionClickable(new SkillDetails(FarmingSkill.INSTANCE, player, 1), FarmingSkill.INSTANCE, Material.GOLD_HOE, 19, player));
        this.set(createCollectionClickable(new SkillDetails(MiningSkill.INSTANCE, player, 1), MiningSkill.INSTANCE, Material.STONE_PICKAXE, 20, player));
        this.set(createCollectionClickable(new SkillDetails(CombatSkill.INSTANCE, player, 1), CombatSkill.INSTANCE, Material.STONE_SWORD, 21, player));
        this.set(createCollectionClickable(new SkillDetails(ForagingSkill.INSTANCE, player, 1), ForagingSkill.INSTANCE, Material.SAPLING, (short) 3, 22, player));
        this.set(createCollectionClickable(null, null, Material.FISHING_ROD, 23, player));
        this.set(createCollectionClickable(new SkillDetails(EnchantingSkill.INSTANCE, player, 1), EnchantingSkill.INSTANCE, Material.ENCHANTMENT_TABLE, 24, player));
        this.set(createCollectionClickable(null, null, Material.BREWING_STAND_ITEM, 25, player));
    }

    private static GUIClickableItem createCollectionClickable(final GUI gui, final Skill skill, final Material icon, final short data, final int slot, final Player player) {
        final User user = User.getUser(player.getUniqueId());
        final List<String> l = new ArrayList<String>();
        if (skill != null) {
            for (final String line : skill.getDescription()) {
                l.add(ChatColor.GRAY + line);
            }
        }
        if (l.size() == 0) {
            l.add(ChatColor.GRAY + "Coming at a later date!");
        } else {
            l.add(" ");
        }
        final double xp = (skill != null) ? user.getSkillXP(skill) : 0.0;
        final int level = (skill != null) ? Skill.getLevel(xp, skill.hasSixtyLevels()) : 0;
        String name = ChatColor.RED + "Coming Soon!";
        if (skill != null && ((level < 50 && !skill.hasSixtyLevels()) || (level < 60 && skill.hasSixtyLevels()))) {
            name = skill.getName();
            final int nextLevel = level + 1;
            final String numeral = SUtil.toRomanNumeral(nextLevel);
            final double nextXP = Skill.getNextOverallXPGoal(xp, skill.hasSixtyLevels());
            l.add(SUtil.createProgressText("Progress to Level " + numeral, xp, nextXP));
            l.add(SUtil.createSLineProgressBar(20, ChatColor.DARK_GREEN, xp, nextXP));
            l.add(" ");
            l.add(ChatColor.GRAY + "Level " + numeral + " Rewards:");
            for (final String line2 : skill.getRewardLore(nextLevel, nextLevel, false)) {
                l.add("  " + line2);
            }
            l.add(" ");
        } else if (skill != null) {
            name = skill.getName();
        }
        if (skill != null) {
            l.add(ChatColor.YELLOW + "Click to view!");
        }
        return GUIClickableItem.createGUIOpenerItem(gui, player, ChatColor.GREEN + name + ((level != 0) ? (" " + SUtil.toRomanNumeral(level)) : ""), slot, icon, data, SUtil.toArray(l, String.class));
    }

    private static GUIClickableItem createCollectionClickable(final GUI gui, final Skill skill, final Material icon, final int slot, final Player player) {
        return createCollectionClickable(gui, skill, icon, (short) 0, slot, player);
    }
}
