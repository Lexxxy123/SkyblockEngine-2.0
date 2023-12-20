package in.godspunky.skyblock.item;

import in.godspunky.skyblock.user.User;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import in.godspunky.skyblock.dungeons.ItemSerial;
import in.godspunky.skyblock.enchantment.Enchantment;
import in.godspunky.skyblock.enchantment.EnchantmentType;
import in.godspunky.skyblock.item.armor.ArmorSet;
import in.godspunky.skyblock.item.orb.OrbBuff;
import in.godspunky.skyblock.reforge.Reforge;
import in.godspunky.skyblock.slayer.SlayerBossType;
import in.godspunky.skyblock.util.SUtil;
import in.godspunky.skyblock.util.Sputnik;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ItemLore {
    private static final String SSE_ID;
    private final SItem parent;
    private Player player;
    private User user;

    public ItemLore(final SItem parent) {
        this.player = null;
        this.user = null;
        this.parent = parent;
    }

    public List<String> asBukkitLore() {
        final String baseMgD = "0";
        final List<String> lore = new ArrayList<String>();
        final List<Enchantment> enchantments = this.parent.getEnchantments();
        final SMaterial material = this.parent.getType();
        final MaterialStatistics statistics = material.getStatistics();
        final Reforge reforge = (this.parent.getReforge() == null) ? Reforge.blank() : this.parent.getReforge();
        try {
            this.player = Bukkit.getPlayer(UUID.fromString(this.parent.getDataString("owner")));
        } catch (final IllegalArgumentException ex) {
        }
        if (this.player != null) {
            this.user = User.getUser(this.player.getUniqueId());
        }
        if (statistics instanceof PlayerBoostStatistics) {
            final PlayerBoostStatistics playerBoostStatistics = (PlayerBoostStatistics) material.getStatistics();
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
                final Enchantment e = this.parent.getEnchantment(EnchantmentType.ONE_FOR_ALL);
                grantedATKDmg = playerBoostStatistics.getBaseDamage() * (e.getLevel() * 210L) / 100;
            }
            if (this.parent.getType().getStatistics().getType() == GenericItemType.ARMOR) {
                final Enchantment growth = this.parent.getEnchantment(EnchantmentType.GROWTH);
                final Enchantment defense = this.parent.getEnchantment(EnchantmentType.PROTECTION);
                if (growth != null) {
                    grantedHP = Math.round(growth.getLevel() * 15.0);
                }
                if (defense != null) {
                    grantedDEF = Math.round(defense.getLevel() * 3.0);
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
            boolean damage;
            if (this.parent.getType() == SMaterial.EMERALD_BLADE) {
                if (this.user != null) {
                    final long cap = 35000000000L;
                    final double d1 = Math.pow((double) Math.min(cap, this.user.getCoins()), 0.25);
                    final double finald = 2.5 * d1;
                    if (finald != 0.0) {
                        damage = this.addPossiblePropertyInt("Damage", playerBoostStatistics.getBaseDamage() + finald + finalhpb1 + grantedATKDmg, hpb1, false, lore);
                    } else {
                        damage = this.addPossiblePropertyInt("Damage", (double) (playerBoostStatistics.getBaseDamage() + finalhpb1 + grantedATKDmg), hpb1, false, lore);
                    }
                } else {
                    damage = this.addPossiblePropertyInt("Damage", (double) (playerBoostStatistics.getBaseDamage() + finalhpb1 + grantedATKDmg), hpb1, false, lore);
                }
            } else {
                damage = this.addPossiblePropertyInt("Damage", (double) (playerBoostStatistics.getBaseDamage() + finalhpb1 + grantedATKDmg), hpb1, false, lore);
            }
            final boolean strength = this.addPossiblePropertyInt("Strength", playerBoostStatistics.getBaseStrength() + finalhpb1, SUtil.blackMagic(reforge.getStrength().getForRarity(this.parent.getRarity())), hpb1, false, lore);
            final boolean critChance = this.addPossiblePropertyInt("Crit Chance", (int) (playerBoostStatistics.getBaseCritChance() * 100.0), (int) (reforge.getCritChance().getForRarity(this.parent.getRarity()) * 100.0), "%", false, lore);
            final boolean critDamage = this.addPossiblePropertyInt("Crit Damage", (int) (playerBoostStatistics.getBaseCritDamage() * 100.0), (int) (reforge.getCritDamage().getForRarity(this.parent.getRarity()) * 100.0), "%", false, lore);
            final boolean atkSpeed = this.addPossiblePropertyIntAtkSpeed("Bonus Attack Speed", (int) Math.min(100.0, playerBoostStatistics.getBaseAttackSpeed()), (int) Math.round(reforge.getAttackSpeed().getForRarity(this.parent.getRarity())), "%", false, lore);
            if (damage || strength || critChance || critDamage || atkSpeed) {
                lore.add("");
            }
            final boolean health = this.addPossiblePropertyInt("Health", playerBoostStatistics.getBaseHealth() + finalhpb3 + grantedHP, " HP" + hpb3, true, lore);
            final boolean defense2 = this.addPossiblePropertyInt("Defense", playerBoostStatistics.getBaseDefense() + finalhpb2 + grantedDEF, hpb2, true, lore);
            final boolean speed = this.addPossiblePropertyInt("Speed", (int) (playerBoostStatistics.getBaseSpeed() * 100.0), "", true, lore);
            final boolean intelligence = this.addPossiblePropertyInt("Intelligence", playerBoostStatistics.getBaseIntelligence(), SUtil.blackMagic(reforge.getIntelligence().getForRarity(this.parent.getRarity())), "", true, lore);
            final boolean magicFind = this.addPossiblePropertyInt("Magic Find", (int) (playerBoostStatistics.getBaseMagicFind() * 100.0), "", true, lore);
            final boolean ferocity = this.addPossiblePropertyInt("Ferocity", playerBoostStatistics.getBaseFerocity(), SUtil.blackMagic(reforge.getFerocity().getForRarity(this.parent.getRarity())), "", true, lore);
            if (health || defense2 || speed || intelligence || magicFind || ferocity) {
                lore.add("");
            }
        }
        if (enchantments != null && enchantments.size() != 0) {
            final int amount = enchantments.size();
            final List<String> stringEnchantments = new ArrayList<String>();
            final List<Enchantment> filteredList_ultimate_a = new ArrayList<Enchantment>();
            final List<Enchantment> filteredList_normal_a = new ArrayList<Enchantment>();
            for (final Enchantment enchantment : enchantments) {
                if (enchantment.getDisplayName().contains(ChatColor.LIGHT_PURPLE.toString())) {
                    filteredList_ultimate_a.add(enchantment);
                } else {
                    filteredList_normal_a.add(enchantment);
                }
            }
            filteredList_ultimate_a.addAll(filteredList_normal_a);
            for (final Enchantment enchantment : filteredList_ultimate_a) {
                stringEnchantments.add(enchantment.getDisplayName());
            }
            if (amount <= 5) {
                final List<Enchantment> filteredList_ultimate = new ArrayList<Enchantment>();
                final List<Enchantment> filteredList_normal = new ArrayList<Enchantment>();
                for (final Enchantment enchantment2 : enchantments) {
                    if (enchantment2.getDisplayName().contains(ChatColor.LIGHT_PURPLE.toString())) {
                        filteredList_ultimate.add(enchantment2);
                    } else {
                        filteredList_normal.add(enchantment2);
                    }
                }
                for (final Enchantment enchantment2 : filteredList_ultimate) {
                    lore.add(enchantment2.getDisplayName());
                    for (final String line : SUtil.splitByWordAndLength(enchantment2.getDescription(), 30, "\\s")) {
                        lore.add(ChatColor.GRAY + line);
                    }
                }
                for (final Enchantment enchantment2 : filteredList_normal) {
                    lore.add(enchantment2.getDisplayName());
                    for (final String line : SUtil.splitByWordAndLength(enchantment2.getDescription(), 30, "\\s")) {
                        lore.add(ChatColor.GRAY + line);
                    }
                }
            } else if (amount <= 10) {
                final List<Enchantment> filteredList_ultimate = new ArrayList<Enchantment>();
                final List<Enchantment> filteredList_normal = new ArrayList<Enchantment>();
                for (final Enchantment enchantment2 : enchantments) {
                    if (enchantment2.getDisplayName().contains(ChatColor.LIGHT_PURPLE.toString())) {
                        filteredList_ultimate.add(enchantment2);
                    } else {
                        filteredList_normal.add(enchantment2);
                    }
                }
                filteredList_ultimate.addAll(filteredList_normal);
                for (final Enchantment enchantment2 : filteredList_ultimate) {
                    lore.add(enchantment2.getDisplayName());
                }
            } else if (amount <= 25) {
                lore.addAll(SUtil.combineElements(stringEnchantments, ", ", 2));
            } else {
                lore.addAll(SUtil.combineElements(stringEnchantments, ", ", 3));
            }
            lore.add("");
        }
        final ArmorSet set = SMaterial.findArmorSet(material);
        if (set != null) {
            lore.add(ChatColor.GOLD + "Full Set Bonus: " + set.getName());
            for (final String line2 : SUtil.splitByWordAndLength(set.getDescription(), 30, "\\s")) {
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
            lore.add(Sputnik.trans("&cDivides your &9☣ Crit Chance &cby 4!"));
            lore.add("");
        }
        final Ability ability = material.getAbility();
        if (ability != null) {
            final StringBuilder abilityTitle = new StringBuilder().append(ChatColor.GOLD).append("Ability: ").append(ability.getAbilityName());
            switch (ability.getAbilityActivation()) {
                case RIGHT_CLICK:
                    abilityTitle.append(" ").append(ChatColor.YELLOW).append(ChatColor.BOLD).append("RIGHT CLICK");
                    break;
                case LEFT_CLICK:
                    abilityTitle.append(" ").append(ChatColor.YELLOW).append(ChatColor.BOLD).append("LEFT CLICK");
                    break;
                case SNEAK:
                    abilityTitle.append(" ").append(ChatColor.YELLOW).append(ChatColor.BOLD).append("SNEAK");
                    break;
            }
            if (this.parent.getType() != SMaterial.GOD_POT && this.parent.getType() != SMaterial.HIDDEN_BOOSTER_COOKIE) {
                lore.add(abilityTitle.toString());
            }
            for (final String line3 : SUtil.splitByWordAndLength(ability.getAbilityDescription(), 35, "\\s")) {
                lore.add(ChatColor.GRAY + line3);
            }
            if (this.parent.getType() == SMaterial.HIDDEN_BOOSTER_COOKIE) {
                lore.add(" ");
                lore.add(Sputnik.trans("&8▶ &b+35% &7Bonus Combat XP"));
                lore.add(Sputnik.trans("&8▶ &b+30✯ &7Bonus Magic Find"));
                lore.add(Sputnik.trans("&8▶ &c+100❁ &7Bonus Strength"));
                lore.add(Sputnik.trans("&8▶ &a+200❈ &7Bonus Defense"));
                lore.add(Sputnik.trans("&8▶ &9+25☠ &7Bonus Crit Damage"));
                lore.add(Sputnik.trans("&8▶ &c+35⫽ &7Bonus Ferocity"));
                lore.add(Sputnik.trans("&8▶ &b+2000✎ &7Bonus Intelligence"));
                lore.add(Sputnik.trans("&8▶ &7Keep &6coins &7and &deffects &7on death"));
                lore.add(Sputnik.trans("&8▶ &7Access to &6/auh &7and &6/fm"));
                lore.add(Sputnik.trans("&8▶ &7Access to &6/av &7and &6/bin &7(Trash Bin)"));
                lore.add(Sputnik.trans("&8▶ &7A shiny &e✪ &6Badge &7on your &aname tag."));
            }
            if (this.parent.getType() == SMaterial.WEIRD_TUBA) {
                lore.add(Sputnik.trans("&c+30❁ Strength"));
                lore.add(Sputnik.trans("&f+30✦ Speed"));
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
                lore.add(ChatColor.translateAlternateColorCodes('&', "&8Charges: &e5 &8/ &a15s"));
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
                lore.add(ChatColor.translateAlternateColorCodes('&', "&8Charges: &e5 &8/ &a15s"));
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
        final OrbBuff buff = material.getOrbBuff();
        if (buff != null) {
            lore.add(this.parent.getRarity().getColor() + "Orb Buff: " + buff.getBuffName());
            for (final String line3 : SUtil.splitByWordAndLength(buff.getBuffDescription(), 30, "\\s")) {
                lore.add(ChatColor.GRAY + line3);
            }
            lore.add("");
        }
        final int kills = this.parent.getDataInt("kills");
        final double coins = this.parent.getDataInt("coins");
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
        final String l = this.parent.getType().getStatistics().getLore();
        if (this.parent.getType() == SMaterial.HIDDEN_VOIDLINGS_WARDEN_HELMET) {
            lore.add(Sputnik.trans("&6Ability: Extra Brute Force"));
        }
        if (l != null) {
            for (final String string : SUtil.splitByWordAndLength(l, 30, "\\s")) {
                lore.add(ChatColor.GRAY + string);
            }
            if (l.length() != 0) {
                lore.add("");
            }
        }
        if (this.parent.getType() == SMaterial.HIDDEN_DONATOR_HELMET) {
            final String p = this.parent.getDataString("p_given");
            if (Bukkit.getPlayer(p) != null) {
                lore.add(Sputnik.trans("&7From: ") + Bukkit.getPlayer(p).getDisplayName());
            }
        }
        if (this.parent.getType() == SMaterial.HIDDEN_DONATOR_HELMET) {
            final String p = this.parent.getDataString("p_rcv");
            if (Bukkit.getPlayer(p) != null) {
                lore.add(Sputnik.trans("&7To: ") + Bukkit.getPlayer(p).getDisplayName());
            }
        }
        if (this.parent.getType() == SMaterial.HIDDEN_DONATOR_HELMET) {
            final String bf = this.parent.getDataString("lore_d");
            final String a = bf.replaceAll("<>", " ");
            if (a != null && a != "null") {
                lore.add("");
                for (final String string2 : SUtil.splitByWordAndLength(a, 25, "\\s")) {
                    lore.add(Sputnik.trans("&7" + string2));
                }
                if (l.length() != 0) {
                    lore.add("");
                }
            }
        }
        final List<String> ll = this.parent.getType().getStatistics().getListLore();
        if (ll != null) {
            for (final String line : ll) {
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
            final NBTTagCompound compound = this.parent.getData();
            for (final String key : compound.c()) {
                final List<String> dl = material.getItemData().getDataLore(key, SUtil.getObjectFromCompound(this.parent.getData(), key));
                if (dl != null) {
                    lore.addAll(dl);
                    lore.add("");
                }
            }
        }
        if (this.parent.isReforgable() && !this.parent.isReforged()) {
            lore.add(Sputnik.trans("&8This item can be reforged!"));
        }
        if (this.user != null) {
            if (this.user.getBCollection() < 100L && this.parent.getType() == SMaterial.GOLDEN_TROPHY_SADAN) {
                lore.add(Sputnik.trans("&c❣ &aRequires &6100 Sadan Kills"));
            } else if (this.user.getBCollection() < 1000L && this.parent.getType() == SMaterial.DIAMOND_TROPHY_SADAN) {
                lore.add(Sputnik.trans("&c❣ &aRequires &61,000 Sadan Kills"));
            } else if (SlayerBossType.SlayerMobType.ENDERMAN.getLevelForXP(this.user.getEndermanSlayerXP()) < 6 && this.parent.getType() == SMaterial.HIDDEN_GYROKINETIC_WAND) {
                lore.add(Sputnik.trans("&4☠ &cRequires &5Enderman Slayer 6."));
            } else if (this.user.getBCollection() < 25L && this.parent.getType() == SMaterial.HIDDEN_SOUL_WHIP) {
                lore.add(Sputnik.trans("&c❣ &aRequires &625 Sadan Kills"));
            }
        }
        final SpecificItemType type = statistics.getSpecificType();
        if (statistics.displayRarity()) {
            String s = "";
            if (this.parent.getDataBoolean("dungeons_item")) {
                s = "DUNGEON ";
            }
            lore.add((this.parent.isRecombobulated() ? (this.parent.getRarity().getBoldedColor() + ChatColor.MAGIC + "D" + ChatColor.RESET + " ") : "") + this.parent.getRarity().getDisplay() + ((type != SpecificItemType.NONE) ? (" " + s + type.getName()) : "") + (this.parent.isRecombobulated() ? (this.parent.getRarity().getBoldedColor() + " " + ChatColor.MAGIC + "D" + ChatColor.RESET) : ""));
        }
        return lore;
    }

    private boolean addPossiblePropertyInt(final String name, double i, final int r, final String succeeding, final boolean green, final List<String> list) {
        if (this.player == null) {
            i += r;
            i += this.getBoostStats(this.parent, name);
            if (i == 0.0) {
                return false;
            }
            final StringBuilder builder = new StringBuilder();
            builder.append(ChatColor.GRAY).append(name).append(": ").append(green ? ChatColor.GREEN : ChatColor.RED).append((i < 0.0) ? "" : "+").append(SUtil.commaify(Math.round(i))).append(succeeding);
            if (r != 0) {
                builder.append(ChatColor.BLUE).append(" (").append((r < 0) ? "" : "+").append(r).append(")");
            }
            list.add(builder.toString());
            return true;
        } else if (this.player.getWorld().getName().contains("f6") || this.player.getWorld().getName().contains("dungeon")) {
            i += r;
            i += this.getBoostStats(this.parent, name);
            if (i == 0.0) {
                return false;
            }
            final StringBuilder builder = new StringBuilder();
            builder.append(ChatColor.GRAY).append(name).append(": ").append(green ? ChatColor.GREEN : ChatColor.RED).append((i < 0.0) ? "" : "+").append(SUtil.commaify(Math.round(i))).append(succeeding);
            if (r != 0) {
                builder.append(ChatColor.BLUE).append(" (").append((r < 0) ? "" : "+").append(r).append(")");
            }
            list.add(builder.toString());
            return true;
        } else {
            i += r;
            if (i == 0.0) {
                return false;
            }
            final StringBuilder builder = new StringBuilder();
            builder.append(ChatColor.GRAY).append(name).append(": ").append(green ? ChatColor.GREEN : ChatColor.RED).append((i < 0.0) ? "" : "+").append(SUtil.commaify(Math.round(i))).append(succeeding);
            if (r != 0) {
                builder.append(ChatColor.BLUE).append(" (").append((r < 0) ? "" : "+").append(r).append(")");
            }
            builder.append(" " + this.getBoostLore(this.parent, i, name));
            list.add(builder.toString());
            return true;
        }
    }

    private String getBoostLore(final SItem sitem, final double baseDamagem, final String stats) {
        if (sitem != null) {
            final ItemSerial is = ItemSerial.getItemBoostStatistics(sitem);
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
                if (amount - Math.round(amount) > 0.1) {
                    return ChatColor.DARK_GRAY + "(+" + SUtil.commaify(amount) + suffix + ")";
                }
                return ChatColor.DARK_GRAY + "(+" + SUtil.commaify(Math.round(amount)) + suffix + ")";
            }
        }
        return "";
    }

    private double getBoostStats(final SItem sitem, final String stats) {
        if (sitem != null) {
            final ItemSerial is = ItemSerial.getItemBoostStatistics(sitem);
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

    private boolean addPossiblePropertyIntAtkSpeed(final String name, double i, final int r, final String succeeding, final boolean green, final List<String> list) {
        if (this.player == null) {
            i += r;
            if (i == 0.0) {
                return false;
            }
            final StringBuilder builder = new StringBuilder();
            builder.append(ChatColor.GRAY).append(name).append(": ").append(green ? ChatColor.GREEN : ChatColor.RED).append((i < 0.0) ? "" : "+").append(SUtil.commaify(Math.round(Math.min(100.0, i)))).append(succeeding);
            if (r != 0) {
                builder.append(ChatColor.BLUE).append(" (").append((r < 0) ? "" : "+").append(r).append(succeeding).append(")");
            }
            builder.append(" " + this.getBoostLore(this.parent, i, name));
            list.add(builder.toString());
            return true;
        } else if (this.player.getWorld().getName().contains("f6") || this.player.getWorld().getName().contains("dungeon")) {
            i += r;
            i += this.getBoostStats(this.parent, name);
            if (i == 0.0) {
                return false;
            }
            final StringBuilder builder = new StringBuilder();
            builder.append(ChatColor.GRAY).append(name).append(": ").append(green ? ChatColor.GREEN : ChatColor.RED).append((i < 0.0) ? "" : "+").append(SUtil.commaify(Math.round(Math.min(100.0, i)))).append(succeeding);
            if (r != 0) {
                builder.append(ChatColor.BLUE).append(" (").append((r < 0) ? "" : "+").append(r).append(succeeding).append(")");
            }
            list.add(builder.toString());
            return true;
        } else {
            i += r;
            if (i == 0.0) {
                return false;
            }
            final StringBuilder builder = new StringBuilder();
            builder.append(ChatColor.GRAY).append(name).append(": ").append(green ? ChatColor.GREEN : ChatColor.RED).append((i < 0.0) ? "" : "+").append(SUtil.commaify(Math.round(Math.min(100.0, i)))).append(succeeding);
            if (r != 0) {
                builder.append(ChatColor.BLUE).append(" (").append((r < 0) ? "" : "+").append(r).append(succeeding).append(")");
            }
            builder.append(" " + this.getBoostLore(this.parent, i, name));
            list.add(builder.toString());
            return true;
        }
    }

    private boolean addPossiblePropertyInt(final String name, final double i, final String succeeding, final boolean green, final List<String> list) {
        return this.addPossiblePropertyInt(name, i, 0, succeeding, green, list);
    }

    private boolean addPossiblePropertyDouble(final String name, double d, final int r, final String succeeding, final boolean green, final List<String> list) {
        d += r;
        if (d == 0.0) {
            return false;
        }
        final StringBuilder builder = new StringBuilder();
        builder.append(ChatColor.GRAY).append(name).append(": ").append(green ? ChatColor.GREEN : ChatColor.RED).append((d < 0.0) ? "" : "+").append(d).append(succeeding);
        if (r != 0) {
            builder.append(ChatColor.BLUE).append(" (").append(this.parent.getReforge().getName()).append(" ").append((r < 0) ? "" : "+").append(r).append(succeeding).append(")");
        }
        list.add(builder.toString());
        return true;
    }

    static {
        SSE_ID = ChatColor.DARK_GRAY + "SKYSIM_ID: %s";
    }
}
