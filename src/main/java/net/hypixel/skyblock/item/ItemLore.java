/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraft.server.v1_8_R3.NBTTagCompound
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.entity.Player
 */
package net.hypixel.skyblock.item;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import net.hypixel.skyblock.features.dungeons.stats.ItemSerial;
import net.hypixel.skyblock.features.enchantment.Enchantment;
import net.hypixel.skyblock.features.enchantment.EnchantmentType;
import net.hypixel.skyblock.features.reforge.Reforge;
import net.hypixel.skyblock.features.slayer.SlayerBossType;
import net.hypixel.skyblock.item.Ability;
import net.hypixel.skyblock.item.GenericItemType;
import net.hypixel.skyblock.item.MaterialStatistics;
import net.hypixel.skyblock.item.PlayerBoostStatistics;
import net.hypixel.skyblock.item.SItem;
import net.hypixel.skyblock.item.SMaterial;
import net.hypixel.skyblock.item.SpecificItemType;
import net.hypixel.skyblock.item.armor.ArmorSet;
import net.hypixel.skyblock.item.orb.OrbBuff;
import net.hypixel.skyblock.user.User;
import net.hypixel.skyblock.util.SUtil;
import net.hypixel.skyblock.util.Sputnik;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ItemLore {
    private static final String SSE_ID = ChatColor.DARK_GRAY + "SKYSIM_ID: %s";
    private final SItem parent;
    private Player player = null;
    private User user = null;

    public ItemLore(SItem parent) {
        this.parent = parent;
    }

    public List<String> asBukkitLore() {
        List<String> ll;
        String bf;
        Object a;
        String p;
        OrbBuff buff;
        Ability ability;
        ArmorSet set;
        String baseMgD = "0";
        ArrayList<String> lore = new ArrayList<String>();
        List<Enchantment> enchantments = this.parent.getEnchantments();
        SMaterial material = this.parent.getType();
        MaterialStatistics statistics = material.getStatistics();
        Reforge reforge = this.parent.getReforge() == null ? Reforge.blank() : this.parent.getReforge();
        try {
            this.player = Bukkit.getPlayer((UUID)UUID.fromString(this.parent.getDataString("owner")));
        } catch (IllegalArgumentException illegalArgumentException) {
            // empty catch block
        }
        if (this.player != null) {
            this.user = User.getUser(this.player.getUniqueId());
        }
        if (statistics instanceof PlayerBoostStatistics) {
            boolean damage;
            List<String> KillBonusLore;
            PlayerBoostStatistics playerBoostStatistics = (PlayerBoostStatistics)material.getStatistics();
            String hpb1 = "";
            String hpb2 = "";
            String hpb3 = "";
            int finalhpb1 = 0;
            int finalhpb2 = 0;
            int finalhpb3 = 0;
            long grantedHP = 0L;
            long grantedDEF = 0L;
            long grantedATKDmg = 0L;
            if (this.parent.getDataInt("hpb") > 0) {
                if (this.parent.getType().getStatistics().getType() == GenericItemType.WEAPON || this.parent.getType().getStatistics().getType() == GenericItemType.RANGED_WEAPON) {
                    finalhpb1 = this.parent.getDataInt("hpb") * 2;
                    hpb1 = Sputnik.trans(" &e(+" + SUtil.commaify(finalhpb1) + ")");
                } else if (this.parent.getType().getStatistics().getType() == GenericItemType.ARMOR) {
                    finalhpb2 = this.parent.getDataInt("hpb") * 2;
                    finalhpb3 = this.parent.getDataInt("hpb") * 4;
                    hpb2 = Sputnik.trans(" &e(+" + SUtil.commaify(finalhpb2) + ")");
                    hpb3 = Sputnik.trans(" &e(+" + SUtil.commaify(finalhpb3) + " HP)");
                }
            }
            if (this.parent.getType().getStatistics().getType() == GenericItemType.WEAPON && this.parent.getEnchantment(EnchantmentType.ONE_FOR_ALL) != null) {
                Enchantment e = this.parent.getEnchantment(EnchantmentType.ONE_FOR_ALL);
                grantedATKDmg = (long)playerBoostStatistics.getBaseDamage() * ((long)e.getLevel() * 210L) / 100L;
            }
            if (this.parent.getType().getStatistics().getType() == GenericItemType.ARMOR) {
                Enchantment growth = this.parent.getEnchantment(EnchantmentType.GROWTH);
                Enchantment defense = this.parent.getEnchantment(EnchantmentType.PROTECTION);
                if (growth != null) {
                    grantedHP = Math.round((double)growth.getLevel() * 15.0);
                }
                if (defense != null) {
                    grantedDEF = Math.round((double)defense.getLevel() * 3.0);
                }
            }
            if ((KillBonusLore = this.parent.getType().getStatistics().killReplacementLore()) != null) {
                for (String line : KillBonusLore) {
                    String line1 = line.replaceAll("<SKYBLOCK_BONUS_DEFENSE>", String.valueOf(this.parent.getBonusDefense())).replaceAll("<SKYBLOCK_NEXT_DEFENSE>", String.valueOf(this.parent.getNextDefense())).replaceAll("<SKYBLOCK_CURRENT_KILLS>", String.valueOf(this.parent.getProgressKills())).replaceAll("<SKYBLOCK_REQUIRED_KILLS>", String.valueOf(this.parent.getRequiredKills()));
                    lore.add(SUtil.color(ChatColor.GRAY + line1));
                }
                if (KillBonusLore.size() != 0) {
                    lore.add("");
                }
            }
            if (this.parent.getType() == SMaterial.DARK_GOGGLES) {
                lore.add(ChatColor.GRAY + "Ability Damage: " + ChatColor.RED + "+25%");
                lore.add("");
            }
            if (this.parent.getType() == SMaterial.SHADOW_GOGGLES) {
                lore.add(ChatColor.GRAY + "Ability Damage: " + ChatColor.RED + "+35%");
                lore.add("");
            }
            if (this.parent.getType() == SMaterial.WITHER_GOGGLES) {
                lore.add(ChatColor.GRAY + "Ability Damage: " + ChatColor.RED + "+45%");
                lore.add("");
            }
            if (this.parent.getType() == SMaterial.EMERALD_BLADE) {
                if (this.user != null) {
                    long cap = 35000000000L;
                    double d1 = Math.pow(Math.min(35000000000L, this.user.getCoins()), 0.25);
                    double finald = 2.5 * d1;
                    damage = finald != 0.0 ? this.addPossiblePropertyInt("Damage", (double)playerBoostStatistics.getBaseDamage() + finald + (double)finalhpb1 + (double)grantedATKDmg, hpb1, false, lore) : this.addPossiblePropertyInt("Damage", (long)(playerBoostStatistics.getBaseDamage() + finalhpb1) + grantedATKDmg, hpb1, false, lore);
                } else {
                    damage = this.addPossiblePropertyInt("Damage", (long)(playerBoostStatistics.getBaseDamage() + finalhpb1) + grantedATKDmg, hpb1, false, lore);
                }
            } else {
                damage = this.addPossiblePropertyInt("Damage", (long)(playerBoostStatistics.getBaseDamage() + finalhpb1) + grantedATKDmg, hpb1, false, lore);
            }
            boolean strength = this.addPossiblePropertyInt("Strength", playerBoostStatistics.getBaseStrength() + (double)finalhpb1, SUtil.blackMagic(reforge.getStrength().getForRarity(this.parent.getRarity())), hpb1, false, lore);
            boolean critChance = this.addPossiblePropertyInt("Crit Chance", (int)(playerBoostStatistics.getBaseCritChance() * 100.0), (int)(reforge.getCritChance().getForRarity(this.parent.getRarity()) * 100.0), "%", false, lore);
            boolean critDamage = this.addPossiblePropertyInt("Crit Damage", (int)(playerBoostStatistics.getBaseCritDamage() * 100.0), (int)(reforge.getCritDamage().getForRarity(this.parent.getRarity()) * 100.0), "%", false, lore);
            boolean atkSpeed = this.addPossiblePropertyIntAtkSpeed("Bonus Attack Speed", (int)Math.min(100.0, playerBoostStatistics.getBaseAttackSpeed()), (int)Math.round(reforge.getAttackSpeed().getForRarity(this.parent.getRarity())), "%", false, lore);
            if (damage || strength || critChance || critDamage || atkSpeed) {
                lore.add("");
            }
            boolean health = this.addPossiblePropertyInt("Health", playerBoostStatistics.getBaseHealth() + (double)finalhpb3 + (double)grantedHP, " HP" + hpb3, true, lore);
            boolean defense2 = this.addPossiblePropertyInt("Defense", playerBoostStatistics.getBaseDefense() + (double)finalhpb2 + (double)grantedDEF, hpb2, true, lore);
            boolean speed = this.addPossiblePropertyInt("Speed", (int)(playerBoostStatistics.getBaseSpeed() * 100.0), "", true, lore);
            boolean intelligence = this.addPossiblePropertyInt("Intelligence", playerBoostStatistics.getBaseIntelligence(), SUtil.blackMagic(reforge.getIntelligence().getForRarity(this.parent.getRarity())), "", true, lore);
            boolean magicFind = this.addPossiblePropertyInt("Magic Find", (int)(playerBoostStatistics.getBaseMagicFind() * 100.0), "", true, lore);
            boolean ferocity = this.addPossiblePropertyInt("Ferocity", playerBoostStatistics.getBaseFerocity(), SUtil.blackMagic(reforge.getFerocity().getForRarity(this.parent.getRarity())), "", true, lore);
            if (health || defense2 || speed || intelligence || magicFind || ferocity) {
                lore.add("");
            }
        }
        if (enchantments != null && enchantments.size() != 0) {
            ArrayList<Enchantment> filteredList_ultimate;
            int amount = enchantments.size();
            ArrayList stringEnchantments = new ArrayList();
            ArrayList<Enchantment> filteredList_ultimate_a = new ArrayList<Enchantment>();
            Iterator<String> filteredList_normal_a = new ArrayList<Enchantment>();
            for (Enchantment enchantment : enchantments) {
                if (enchantment.getDisplayName().contains(ChatColor.LIGHT_PURPLE.toString())) {
                    filteredList_ultimate_a.add(enchantment);
                    continue;
                }
                filteredList_normal_a.add((String)((Object)enchantment));
            }
            filteredList_ultimate_a.addAll((Collection<Enchantment>)((Object)filteredList_normal_a));
            for (Enchantment enchantment : filteredList_ultimate_a) {
                stringEnchantments.add(enchantment.getDisplayName());
            }
            if (amount <= 5) {
                filteredList_ultimate = new ArrayList<Enchantment>();
                ArrayList<Enchantment> filteredList_normal = new ArrayList<Enchantment>();
                for (Enchantment enchantment2 : enchantments) {
                    if (enchantment2.getDisplayName().contains(ChatColor.LIGHT_PURPLE.toString())) {
                        filteredList_ultimate.add(enchantment2);
                        continue;
                    }
                    filteredList_normal.add(enchantment2);
                }
                for (Enchantment enchantment2 : filteredList_ultimate) {
                    lore.add(enchantment2.getDisplayName());
                    for (String line : SUtil.splitByWordAndLength(enchantment2.getDescription(), 30, "\\s")) {
                        lore.add(ChatColor.GRAY + line);
                    }
                }
                for (Enchantment enchantment2 : filteredList_normal) {
                    lore.add(enchantment2.getDisplayName());
                    for (String line : SUtil.splitByWordAndLength(enchantment2.getDescription(), 30, "\\s")) {
                        lore.add(ChatColor.GRAY + line);
                    }
                }
            } else if (amount <= 10) {
                filteredList_ultimate = new ArrayList();
                ArrayList<Enchantment> filteredList_normal = new ArrayList<Enchantment>();
                for (Enchantment enchantment2 : enchantments) {
                    if (enchantment2.getDisplayName().contains(ChatColor.LIGHT_PURPLE.toString())) {
                        filteredList_ultimate.add(enchantment2);
                        continue;
                    }
                    filteredList_normal.add(enchantment2);
                }
                filteredList_ultimate.addAll(filteredList_normal);
                for (Enchantment enchantment2 : filteredList_ultimate) {
                    lore.add(enchantment2.getDisplayName());
                }
            } else if (amount <= 25) {
                lore.addAll(SUtil.combineElements(stringEnchantments, ", ", 2));
            } else {
                lore.addAll(SUtil.combineElements(stringEnchantments, ", ", 3));
            }
            lore.add("");
        }
        if ((set = SMaterial.findArmorSet(material)) != null) {
            lore.add(ChatColor.GOLD + "Full Set Bonus: " + set.getName());
            for (String line2 : SUtil.splitByWordAndLength(set.getDescription(), 30, "\\s")) {
                lore.add(ChatColor.GRAY + line2);
            }
            lore.add("");
        }
        if (this.parent.getType() == SMaterial.JUJU_SHORTBOW) {
            lore.add(ChatColor.DARK_PURPLE + "Shortbow: Instantly Shoots");
            lore.add(ChatColor.GRAY + "Hits " + ChatColor.RED + "3 " + ChatColor.GRAY + "mobs on impact.");
            lore.add(ChatColor.GRAY + "Can damage Endermen.");
            lore.add("");
        }
        if (this.parent.getType() == SMaterial.TERMINATOR) {
            lore.add(ChatColor.GOLD + "Shortbow: Instantly Shoots");
            lore.add(Sputnik.trans("&7Shoots &b3 &7arrows at once."));
            lore.add(ChatColor.GRAY + "Can damage Endermen.");
            lore.add("");
            lore.add(Sputnik.trans("&cDivides your &9\u2623 Crit Chance &cby 4!"));
            lore.add("");
        }
        if ((ability = material.getAbility()) != null) {
            StringBuilder abilityTitle = new StringBuilder().append(ChatColor.GOLD).append("Ability: ").append(ability.getAbilityName());
            switch (ability.getAbilityActivation()) {
                case RIGHT_CLICK: {
                    abilityTitle.append(" ").append(ChatColor.YELLOW).append(ChatColor.BOLD).append("RIGHT CLICK");
                    break;
                }
                case LEFT_CLICK: {
                    abilityTitle.append(" ").append(ChatColor.YELLOW).append(ChatColor.BOLD).append("LEFT CLICK");
                    break;
                }
                case SNEAK: {
                    abilityTitle.append(" ").append(ChatColor.YELLOW).append(ChatColor.BOLD).append("SNEAK");
                }
            }
            if (this.parent.getType() != SMaterial.GOD_POT && this.parent.getType() != SMaterial.HIDDEN_BOOSTER_COOKIE) {
                lore.add(abilityTitle.toString());
            }
            for (String line3 : SUtil.splitByWordAndLength(ability.getAbilityDescription(), 35, "\\s")) {
                lore.add(ChatColor.GRAY + line3);
            }
            if (this.parent.getType() == SMaterial.HIDDEN_BOOSTER_COOKIE) {
                lore.add(" ");
                lore.add(Sputnik.trans("&8\u25b6 &b+35% &7Bonus Combat XP"));
                lore.add(Sputnik.trans("&8\u25b6 &b+30\u272f &7Bonus Magic Find"));
                lore.add(Sputnik.trans("&8\u25b6 &c+100\u2741 &7Bonus Strength"));
                lore.add(Sputnik.trans("&8\u25b6 &a+200\u2748 &7Bonus Defense"));
                lore.add(Sputnik.trans("&8\u25b6 &9+25\u2620 &7Bonus Crit Damage"));
                lore.add(Sputnik.trans("&8\u25b6 &c+35\u2afd &7Bonus Ferocity"));
                lore.add(Sputnik.trans("&8\u25b6 &b+2000\u270e &7Bonus Intelligence"));
                lore.add(Sputnik.trans("&8\u25b6 &7Keep &6coins &7and &deffects &7on death"));
                lore.add(Sputnik.trans("&8\u25b6 &7Access to &6/auh &7and &6/fm"));
                lore.add(Sputnik.trans("&8\u25b6 &7Access to &6/av &7and &6/bin &7(Trash Bin)"));
                lore.add(Sputnik.trans("&8\u25b6 &7A shiny &e\u272a &6Badge &7on your &aname tag."));
            }
            if (this.parent.getType() == SMaterial.WEIRD_TUBA) {
                lore.add(Sputnik.trans("&c+30\u2741 Strength"));
                lore.add(Sputnik.trans("&f+30\u2726 Speed"));
                lore.add(Sputnik.trans("&7for &a20 &7seconds."));
                lore.add(ChatColor.DARK_GRAY + "Buff doesn't stack.");
            }
            if (this.parent.getType() == SMaterial.EDIBLE_MACE) {
                lore.add(ChatColor.DARK_GRAY + "Debuff doesn't stack.");
                lore.add(ChatColor.DARK_GRAY + "Mana Cost: " + ChatColor.DARK_AQUA + "100");
            }
            if (this.parent.getType() == SMaterial.HIDDEN_GYROKINETIC_WAND) {
                lore.add(ChatColor.DARK_GRAY + "Regen mana 10x slower for 3s after cast.");
            }
            if (this.parent.getType() == SMaterial.HIDDEN_VOIDLINGS_WARDEN_HELMET) {
                lore.add(ChatColor.DARK_GRAY + "Mana Cost: " + ChatColor.DARK_AQUA + "All");
                lore.add(ChatColor.DARK_GRAY + "Cooldown: " + ChatColor.GREEN + "30s");
            }
            if (this.parent.getType() == SMaterial.ZOMBIE_SWORD_T2) {
                lore.add(ChatColor.DARK_GRAY + "Mana Cost: " + ChatColor.DARK_AQUA + "70");
                lore.add(ChatColor.translateAlternateColorCodes((char)'&', (String)"&8Charges: &e5 &8/ &a15s"));
            }
            if (this.parent.getType() == SMaterial.GOD_POT) {
                lore.add(" ");
                lore.add(Sputnik.trans("&eRight-click to consume!"));
            }
            if (this.parent.getType() == SMaterial.TERMINATOR) {
                lore.add(ChatColor.DARK_GRAY + "Mana Cost: " + ChatColor.DARK_AQUA + "100");
                lore.add(ChatColor.DARK_GRAY + "Cooldown: " + ChatColor.GREEN + "1s");
            }
            if (this.parent.getType() == SMaterial.ZOMBIE_SWORD_T3) {
                lore.add(ChatColor.DARK_GRAY + "Mana Cost: " + ChatColor.DARK_AQUA + "70");
                lore.add(ChatColor.translateAlternateColorCodes((char)'&', (String)"&8Charges: &e5 &8/ &a15s"));
            }
            if (this.parent.getType() == SMaterial.AXE_OF_THE_SHREDDED) {
                lore.add(ChatColor.DARK_GRAY + "Mana Cost: " + ChatColor.DARK_AQUA + "20");
            }
            if (this.parent.getType() == SMaterial.HIDDEN_DIMOONIZARY_DAGGER) {
                lore.add(ChatColor.DARK_GRAY + "Mana Cost: " + ChatColor.DARK_AQUA + "160");
            }
            if (ability.getManaCost() > 0) {
                lore.add(ChatColor.DARK_GRAY + "Mana Cost: " + ChatColor.DARK_AQUA + ability.getManaCost());
            }
            if (ability.getManaCost() == -1) {
                lore.add(ChatColor.DARK_GRAY + "Mana Cost: " + ChatColor.DARK_AQUA + "All");
            }
            if (ability.getAbilityCooldownTicks() >= 20 && ability.getAbilityCooldownTicks() <= 1200) {
                lore.add(ChatColor.DARK_GRAY + "Cooldown: " + ChatColor.GREEN + ability.getAbilityCooldownTicks() / 20 + "s");
            } else if (ability.getAbilityCooldownTicks() > 1200) {
                lore.add(ChatColor.DARK_GRAY + "Cooldown: " + ChatColor.GREEN + ability.getAbilityCooldownTicks() / 20 / 60 + "m");
            }
            lore.add("");
        }
        if (this.parent.getDataString("etherwarp_trans").equals("true")) {
            lore.add(Sputnik.trans("&6Ability: Ether Transmission &e&lSNEAK RIGHT CLICK"));
            lore.add(Sputnik.trans("&7Teleport to your targetted block"));
            lore.add(Sputnik.trans("&7up to &a57 blocks &7away."));
            lore.add(ChatColor.DARK_GRAY + "Mana Cost: " + ChatColor.DARK_AQUA + "250");
            lore.add("");
        }
        if ((buff = material.getOrbBuff()) != null) {
            lore.add(this.parent.getRarity().getColor() + "Orb Buff: " + buff.getBuffName());
            for (String line3 : SUtil.splitByWordAndLength(buff.getBuffDescription(), 30, "\\s")) {
                lore.add(ChatColor.GRAY + line3);
            }
            lore.add("");
        }
        int kills = this.parent.getDataInt("kills");
        double coins = this.parent.getDataInt("coins");
        if (statistics.displayKills()) {
            lore.add(ChatColor.GOLD + "Kills: " + ChatColor.AQUA + kills);
            lore.add("");
        }
        if (statistics.displayCoins()) {
            lore.add(ChatColor.GRAY + "Price paid: " + ChatColor.GOLD + SUtil.commaify(coins));
            lore.add("");
        }
        if (this.parent.getType() == SMaterial.MIDAS_STAFF) {
            lore.add(Sputnik.trans("&6Ability: Greed"));
        }
        if (this.parent.getType() == SMaterial.ENCHANTED_BOOK && this.parent.getEnchantments() != null && Enchantment.ultimateEnchantsListFromList(this.parent.getEnchantments()).size() > 0) {
            lore.add(Sputnik.trans("&cYou can only have 1 Ultimate"));
            lore.add(Sputnik.trans("&cEnchantment on an item!"));
            lore.add(" ");
        }
        String l = this.parent.getType().getStatistics().getLore();
        if (this.parent.getType() == SMaterial.HIDDEN_VOIDLINGS_WARDEN_HELMET) {
            lore.add(Sputnik.trans("&6Ability: Extra Brute Force"));
        }
        if (l != null) {
            for (String string : SUtil.splitByWordAndLength(l, 30, "\\s")) {
                lore.add(ChatColor.GRAY + string);
            }
            if (l.length() != 0) {
                lore.add("");
            }
        }
        if (this.parent.getType() == SMaterial.HIDDEN_DONATOR_HELMET && Bukkit.getPlayer((String)(p = this.parent.getDataString("p_given"))) != null) {
            lore.add(Sputnik.trans("&7From: ") + Bukkit.getPlayer((String)p).getDisplayName());
        }
        if (this.parent.getType() == SMaterial.HIDDEN_DONATOR_HELMET && Bukkit.getPlayer((String)(p = this.parent.getDataString("p_rcv"))) != null) {
            lore.add(Sputnik.trans("&7To: ") + Bukkit.getPlayer((String)p).getDisplayName());
        }
        if (this.parent.getType() == SMaterial.HIDDEN_DONATOR_HELMET && (a = (bf = this.parent.getDataString("lore_d")).replaceAll("<>", " ")) != null && a != "null") {
            lore.add("");
            for (String string2 : SUtil.splitByWordAndLength((String)a, 25, "\\s")) {
                lore.add(Sputnik.trans("&7" + string2));
            }
            if (l.length() != 0) {
                lore.add("");
            }
        }
        if ((ll = this.parent.getType().getStatistics().getListLore()) != null) {
            for (String line : ll) {
                lore.add(ChatColor.GRAY + line);
            }
            if (ll.size() != 0) {
                lore.add("");
            }
        }
        if (statistics.displayCoins()) {
            lore.add(ChatColor.GRAY + "Price paid: " + ChatColor.GOLD + SUtil.commaify(coins));
            lore.add("");
        }
        if (this.parent.getType() == SMaterial.MIDAS_STAFF) {
            lore.add(Sputnik.trans("&7Price paid: &6100,000,000"));
            lore.add(Sputnik.trans("&7Base Ability Damage Bonus: &326000"));
            lore.add("");
        }
        if (this.parent.getDataInt("anvil") != 0) {
            lore.add(ChatColor.GRAY + "Anvil Uses: " + ChatColor.RED + this.parent.getDataInt("anvil"));
        }
        if (material.getItemData() != null) {
            NBTTagCompound compound = this.parent.getData();
            for (String key : compound.c()) {
                List<String> dl = material.getItemData().getDataLore(key, SUtil.getObjectFromCompound(this.parent.getData(), key));
                if (dl == null) continue;
                lore.addAll(dl);
                lore.add("");
            }
        }
        if (this.parent.isReforgable() && !this.parent.isReforged()) {
            lore.add(Sputnik.trans("&8This item can be reforged!"));
        }
        if (this.user != null) {
            if (this.user.getBCollection() < 100L && this.parent.getType() == SMaterial.GOLDEN_TROPHY_SADAN) {
                lore.add(Sputnik.trans("&c\u2763 &aRequires &6100 Sadan Kills"));
            } else if (this.user.getBCollection() < 1000L && this.parent.getType() == SMaterial.DIAMOND_TROPHY_SADAN) {
                lore.add(Sputnik.trans("&c\u2763 &aRequires &61,000 Sadan Kills"));
            } else if (SlayerBossType.SlayerMobType.ENDERMAN.getLevelForXP(this.user.getEndermanSlayerXP()) < 6 && this.parent.getType() == SMaterial.HIDDEN_GYROKINETIC_WAND) {
                lore.add(Sputnik.trans("&4\u2620 &cRequires &5Enderman Slayer 6."));
            } else if (this.user.getBCollection() < 25L && this.parent.getType() == SMaterial.HIDDEN_SOUL_WHIP) {
                lore.add(Sputnik.trans("&c\u2763 &aRequires &625 Sadan Kills"));
            }
        }
        SpecificItemType type = statistics.getSpecificType();
        if (statistics.displayRarity()) {
            String s = "";
            if (this.parent.getDataBoolean("dungeons_item")) {
                s = "DUNGEON ";
            }
            lore.add((this.parent.isRecombobulated() ? this.parent.getRarity().getBoldedColor() + ChatColor.MAGIC + "D" + ChatColor.RESET + " " : "") + this.parent.getRarity().getDisplay() + (type != SpecificItemType.NONE ? " " + s + type.getName() : "") + (this.parent.isRecombobulated() ? this.parent.getRarity().getBoldedColor() + " " + ChatColor.MAGIC + "D" + ChatColor.RESET : ""));
        }
        return lore;
    }

    private boolean addPossiblePropertyInt(String name, double i, int r, String succeeding, boolean green, List<String> list) {
        if (this.player == null) {
            i += (double)r;
            if ((i += this.getBoostStats(this.parent, name)) == 0.0) {
                return false;
            }
            StringBuilder builder = new StringBuilder();
            builder.append(ChatColor.GRAY).append(name).append(": ").append(green ? ChatColor.GREEN : ChatColor.RED).append(i < 0.0 ? "" : "+").append(SUtil.commaify(Math.round(i))).append(succeeding);
            if (r != 0) {
                builder.append(ChatColor.BLUE).append(" (").append(r < 0 ? "" : "+").append(r).append(")");
            }
            list.add(builder.toString());
            return true;
        }
        if (this.player.getWorld().getName().contains("f6") || this.player.getWorld().getName().contains("dungeon")) {
            i += (double)r;
            if ((i += this.getBoostStats(this.parent, name)) == 0.0) {
                return false;
            }
            StringBuilder builder = new StringBuilder();
            builder.append(ChatColor.GRAY).append(name).append(": ").append(green ? ChatColor.GREEN : ChatColor.RED).append(i < 0.0 ? "" : "+").append(SUtil.commaify(Math.round(i))).append(succeeding);
            if (r != 0) {
                builder.append(ChatColor.BLUE).append(" (").append(r < 0 ? "" : "+").append(r).append(")");
            }
            list.add(builder.toString());
            return true;
        }
        if ((i += (double)r) == 0.0) {
            return false;
        }
        StringBuilder builder = new StringBuilder();
        builder.append(ChatColor.GRAY).append(name).append(": ").append(green ? ChatColor.GREEN : ChatColor.RED).append(i < 0.0 ? "" : "+").append(SUtil.commaify(Math.round(i))).append(succeeding);
        if (r != 0) {
            builder.append(ChatColor.BLUE).append(" (").append(r < 0 ? "" : "+").append(r).append(")");
        }
        builder.append(" " + this.getBoostLore(this.parent, i, name));
        list.add(builder.toString());
        return true;
    }

    private String getBoostLore(SItem sitem, double baseDamagem, String stats) {
        if (sitem != null) {
            ItemSerial is = ItemSerial.getItemBoostStatistics(sitem);
            double amount = 0.0;
            String suffix = "";
            if (stats.contains("Damage") && !stats.contains("Crit")) {
                amount = is.getDamage();
            } else if (stats.contains("Strength")) {
                amount = is.getStrength();
            } else if (stats.contains("Chance")) {
                amount = is.getCritchance() * 100.0;
                suffix = "%";
            } else if (stats.contains("Crit") && stats.contains("Damage")) {
                amount = is.getCritdamage() * 100.0;
                suffix = "%";
            } else if (stats.contains("Ferocity")) {
                amount = is.getFerocity();
            } else if (stats.contains("Intelligence")) {
                amount = is.getIntelligence();
            } else if (stats.contains("Health")) {
                amount = is.getHealth();
            } else if (stats.contains("Defense")) {
                amount = is.getDefense();
            } else if (stats.contains("Magic")) {
                amount = is.getMagicFind() * 100.0;
            } else if (stats.contains("Bonus")) {
                amount = is.getAtkSpeed();
                suffix = "%";
            } else if (stats.contains("Speed")) {
                amount = is.getSpeed();
            }
            if (sitem.getDataBoolean("dungeons_item")) {
                amount += baseDamagem;
            }
            if (amount > 0.0) {
                if (amount - (double)Math.round(amount) > 0.1) {
                    return ChatColor.DARK_GRAY + "(+" + SUtil.commaify(amount) + suffix + ")";
                }
                return ChatColor.DARK_GRAY + "(+" + SUtil.commaify(Math.round(amount)) + suffix + ")";
            }
        }
        return "";
    }

    private double getBoostStats(SItem sitem, String stats) {
        if (sitem != null) {
            ItemSerial is = ItemSerial.getItemBoostStatistics(sitem);
            double amount = 0.0;
            String suffix = "";
            if (stats.contains("Damage") && !stats.contains("Crit")) {
                amount = is.getDamage();
            } else if (stats.contains("Strength")) {
                amount = is.getStrength();
            } else if (stats.contains("Chance")) {
                amount = is.getCritchance() * 100.0;
                suffix = "%";
            } else if (stats.contains("Crit") && stats.contains("Damage")) {
                amount = is.getCritdamage() * 100.0;
                suffix = "%";
            } else if (stats.contains("Ferocity")) {
                amount = is.getFerocity();
            } else if (stats.contains("Intelligence")) {
                amount = is.getIntelligence();
            } else if (stats.contains("Health")) {
                amount = is.getHealth();
            } else if (stats.contains("Defense")) {
                amount = is.getDefense();
            } else if (stats.contains("Magic")) {
                amount = is.getMagicFind() * 100.0;
            } else if (stats.contains("Bonus")) {
                amount = is.getAtkSpeed();
                suffix = "%";
            } else if (stats.contains("Speed")) {
                amount = is.getSpeed();
            }
            return amount;
        }
        return 0.0;
    }

    private boolean addPossiblePropertyIntAtkSpeed(String name, double i, int r, String succeeding, boolean green, List<String> list) {
        if (this.player == null) {
            if ((i += (double)r) == 0.0) {
                return false;
            }
            StringBuilder builder = new StringBuilder();
            builder.append(ChatColor.GRAY).append(name).append(": ").append(green ? ChatColor.GREEN : ChatColor.RED).append(i < 0.0 ? "" : "+").append(SUtil.commaify(Math.round(Math.min(100.0, i)))).append(succeeding);
            if (r != 0) {
                builder.append(ChatColor.BLUE).append(" (").append(r < 0 ? "" : "+").append(r).append(succeeding).append(")");
            }
            builder.append(" " + this.getBoostLore(this.parent, i, name));
            list.add(builder.toString());
            return true;
        }
        if (this.player.getWorld().getName().contains("f6") || this.player.getWorld().getName().contains("dungeon")) {
            i += (double)r;
            if ((i += this.getBoostStats(this.parent, name)) == 0.0) {
                return false;
            }
            StringBuilder builder = new StringBuilder();
            builder.append(ChatColor.GRAY).append(name).append(": ").append(green ? ChatColor.GREEN : ChatColor.RED).append(i < 0.0 ? "" : "+").append(SUtil.commaify(Math.round(Math.min(100.0, i)))).append(succeeding);
            if (r != 0) {
                builder.append(ChatColor.BLUE).append(" (").append(r < 0 ? "" : "+").append(r).append(succeeding).append(")");
            }
            list.add(builder.toString());
            return true;
        }
        if ((i += (double)r) == 0.0) {
            return false;
        }
        StringBuilder builder = new StringBuilder();
        builder.append(ChatColor.GRAY).append(name).append(": ").append(green ? ChatColor.GREEN : ChatColor.RED).append(i < 0.0 ? "" : "+").append(SUtil.commaify(Math.round(Math.min(100.0, i)))).append(succeeding);
        if (r != 0) {
            builder.append(ChatColor.BLUE).append(" (").append(r < 0 ? "" : "+").append(r).append(succeeding).append(")");
        }
        builder.append(" " + this.getBoostLore(this.parent, i, name));
        list.add(builder.toString());
        return true;
    }

    private boolean addPossiblePropertyInt(String name, double i, String succeeding, boolean green, List<String> list) {
        return this.addPossiblePropertyInt(name, i, 0, succeeding, green, list);
    }

    private boolean addPossiblePropertyDouble(String name, double d, int r, String succeeding, boolean green, List<String> list) {
        if ((d += (double)r) == 0.0) {
            return false;
        }
        StringBuilder builder = new StringBuilder();
        builder.append(ChatColor.GRAY).append(name).append(": ").append(green ? ChatColor.GREEN : ChatColor.RED).append(d < 0.0 ? "" : "+").append(d).append(succeeding);
        if (r != 0) {
            builder.append(ChatColor.BLUE).append(" (").append(this.parent.getReforge().getName()).append(" ").append(r < 0 ? "" : "+").append(r).append(succeeding).append(")");
        }
        list.add(builder.toString());
        return true;
    }
}

