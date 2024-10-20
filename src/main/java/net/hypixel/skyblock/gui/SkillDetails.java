/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.event.inventory.InventoryClickEvent
 *  org.bukkit.inventory.ItemStack
 */
package net.hypixel.skyblock.gui;

import java.util.ArrayList;
import java.util.UUID;
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
import net.hypixel.skyblock.user.User;
import net.hypixel.skyblock.util.SUtil;
import net.hypixel.skyblock.util.Sputnik;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class SkillDetails
extends GUI {
    private final Skill skill;
    private final UUID uuid;
    private Material skillInstanceMat;
    private Material skillLvlMat;
    private short data = 0;
    private final Player player;
    private final int page;
    static final int[] slots = new int[]{9, 18, 27, 28, 29, 20, 11, 2, 3, 4, 13, 22, 31, 32, 33, 24, 15, 6, 7, 8, 17, 26, 35, 44, 53};

    public SkillDetails(Skill skill, Player player, int index) {
        super(skill.getName() + " Skill", 54);
        this.skill = skill;
        this.page = index;
        this.player = player;
        this.uuid = player.getUniqueId();
    }

    @Override
    public void onOpen(GUIOpenEvent e2) {
        block40: {
            int level;
            double xp;
            block39: {
                this.fill(BLACK_STAINED_GLASS_PANE);
                this.set(GUIClickableItem.getCloseItem(49));
                if (this.skill instanceof CombatSkill) {
                    this.skillInstanceMat = Material.STONE_SWORD;
                    this.skillLvlMat = Material.DIAMOND_HELMET;
                } else if (this.skill instanceof MiningSkill) {
                    this.skillInstanceMat = Material.STONE_PICKAXE;
                    this.skillLvlMat = Material.IRON_BLOCK;
                } else if (this.skill instanceof ForagingSkill) {
                    this.skillInstanceMat = Material.SAPLING;
                    this.data = (short)3;
                    this.skillLvlMat = Material.LOG;
                } else if (this.skill instanceof FarmingSkill) {
                    this.skillInstanceMat = Material.GOLD_HOE;
                    this.skillLvlMat = Material.HAY_BLOCK;
                } else if (this.skill instanceof EnchantingSkill) {
                    this.skillInstanceMat = Material.ENCHANTMENT_TABLE;
                    this.skillLvlMat = Material.ENCHANTED_BOOK;
                }
                xp = this.skill != null ? User.getUser(this.uuid).getSkillXP(this.skill) : 0.0;
                level = this.skill != null ? Skill.getLevel(xp, this.skill.hasSixtyLevels()) : 0;
                this.set(new GUIClickableItem(){

                    @Override
                    public void run(InventoryClickEvent e2) {
                        GUIType.SKILL_MENU.getGUI().open(SkillDetails.this.player);
                    }

                    @Override
                    public int getSlot() {
                        return 48;
                    }

                    @Override
                    public ItemStack getItem() {
                        return SUtil.getStack(ChatColor.GREEN + "Go Back", Material.ARROW, (short)0, 1, ChatColor.GRAY + "To Skill Menu");
                    }
                });
                String name = "";
                ArrayList<String> l2 = new ArrayList<String>();
                if (this.skill != null) {
                    for (String line : this.skill.getDescription()) {
                        l2.add(ChatColor.GRAY + line);
                    }
                }
                if (this.skill != null && (level < 50 && !this.skill.hasSixtyLevels() || level < 60 && this.skill.hasSixtyLevels())) {
                    name = this.skill.getName();
                    int nextLevel = level + 1;
                    String numeral = SUtil.toRomanNumeral(nextLevel);
                    double nextXP = Skill.getNextOverallXPGoal(xp, this.skill.hasSixtyLevels());
                    l2.add(" ");
                    l2.add(SUtil.createProgressText("Progress to Level " + numeral, xp, nextXP));
                    l2.add(SUtil.createSLineProgressBar(20, ChatColor.DARK_GREEN, xp, nextXP));
                    l2.add(" ");
                    for (String line2 : this.skill.getRewardLore(nextLevel, nextLevel, false)) {
                        l2.add("  " + line2);
                    }
                    l2.add(" ");
                } else if (this.skill != null) {
                    name = this.skill.getName();
                    l2.add(" ");
                }
                if (this.skill != null) {
                    l2.add(Sputnik.trans("&8Increase your " + this.skill.getName() + " Level to"));
                    l2.add(Sputnik.trans("&8unlock Perks, statistic bonuses,"));
                    l2.add(Sputnik.trans("&8and more!"));
                }
                this.set(0, SUtil.getStack(ChatColor.GREEN + this.skill.getName() + " Skill", this.skillInstanceMat, this.data, 1, l2));
                if (this.page == 1 && level >= 25) {
                    this.set(new GUIClickableItem(){

                        @Override
                        public void run(InventoryClickEvent e2) {
                            new SkillDetails(SkillDetails.this.skill, (Player)e2.getWhoClicked(), SkillDetails.this.page + 1).open(SkillDetails.this.player);
                        }

                        @Override
                        public int getSlot() {
                            return 50;
                        }

                        @Override
                        public ItemStack getItem() {
                            return SUtil.getStack(ChatColor.GREEN + "Levels 26-50", Material.ARROW, (short)0, 1, ChatColor.YELLOW + "Click to view!");
                        }
                    });
                } else if (this.page == 2 && level >= 25) {
                    this.set(new GUIClickableItem(){

                        @Override
                        public void run(InventoryClickEvent e2) {
                            new SkillDetails(SkillDetails.this.skill, (Player)e2.getWhoClicked(), SkillDetails.this.page - 1).open(SkillDetails.this.player);
                        }

                        @Override
                        public int getSlot() {
                            return 50;
                        }

                        @Override
                        public ItemStack getItem() {
                            return SUtil.getStack(ChatColor.GREEN + "Levels 1-25", Material.ARROW, (short)0, 1, ChatColor.YELLOW + "Click to view!");
                        }
                    });
                } else if (level < 25 && this.page > 1) {
                    this.player.closeInventory();
                }
                if (this.page != 1) break block39;
                int i2 = 1;
                short data = 0;
                ChatColor c2 = ChatColor.GRAY;
                double nextXP2 = Skill.getNextOverallXPGoal(xp, this.skill.hasSixtyLevels());
                for (int slot : slots) {
                    ArrayList<String> lore = new ArrayList<String>();
                    lore.add(Sputnik.trans("&7Rewards:"));
                    for (String str : this.skill.getRewardLore(i2, i2, false)) {
                        lore.add("  " + str);
                    }
                    if (i2 > level && i2 != level + 1) {
                        data = 14;
                        c2 = ChatColor.RED;
                    } else if (i2 == level + 1) {
                        data = 4;
                        c2 = ChatColor.YELLOW;
                        lore.add(" ");
                        lore.add(SUtil.createProgressText(ChatColor.GRAY + "Progress" + ChatColor.YELLOW, xp, nextXP2));
                        lore.add(SUtil.createSLineProgressBar(20, ChatColor.DARK_GREEN, xp, nextXP2));
                    } else if (i2 <= level) {
                        data = 5;
                        c2 = ChatColor.GREEN;
                        lore.add(" ");
                        lore.add(c2 + "UNLOCKED");
                    }
                    if (i2 <= level && i2 % 5 == 0) {
                        this.set(slot, SUtil.getStack(c2 + this.skill.getName() + " Level " + SUtil.toRomanNumeral(i2), this.skillLvlMat, this.data, i2, lore));
                    } else {
                        this.set(slot, SUtil.getColorStack(data, c2 + this.skill.getName() + " Level " + SUtil.toRomanNumeral(i2), lore, (short)0, i2));
                    }
                    ++i2;
                }
                break block40;
            }
            if (this.page != 2) break block40;
            int i3 = 26;
            short data = 0;
            ChatColor c3 = ChatColor.GRAY;
            double nextXP2 = Skill.getNextOverallXPGoal(xp, this.skill.hasSixtyLevels());
            for (int slot : slots) {
                ArrayList<String> lore = new ArrayList<String>();
                lore.add(Sputnik.trans("&7Rewards:"));
                for (String str : this.skill.getRewardLore(i3, i3, false)) {
                    lore.add("  " + str);
                }
                if (i3 > level && i3 != level + 1) {
                    data = 14;
                    c3 = ChatColor.RED;
                } else if (i3 == level + 1) {
                    data = 4;
                    c3 = ChatColor.YELLOW;
                    lore.add(" ");
                    lore.add(SUtil.createProgressText(ChatColor.GRAY + "Progress" + ChatColor.YELLOW, xp, nextXP2));
                    lore.add(SUtil.createSLineProgressBar(20, ChatColor.DARK_GREEN, xp, nextXP2));
                } else if (i3 <= level) {
                    data = 5;
                    c3 = ChatColor.GREEN;
                    lore.add(" ");
                    lore.add(c3 + "UNLOCKED");
                }
                if (i3 <= level && i3 % 5 == 0) {
                    this.set(slot, SUtil.getStack(c3 + this.skill.getName() + " Level " + SUtil.toRomanNumeral(i3), this.skillInstanceMat, this.data, i3, lore));
                } else {
                    this.set(slot, SUtil.getColorStack(data, c3 + this.skill.getName() + " Level " + SUtil.toRomanNumeral(i3), lore, (short)0, i3));
                }
                ++i3;
            }
        }
    }
}

