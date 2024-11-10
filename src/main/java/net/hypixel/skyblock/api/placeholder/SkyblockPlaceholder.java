/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  me.clip.placeholderapi.expansion.PlaceholderExpansion
 *  net.md_5.bungee.api.ChatColor
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.inventory.ItemStack
 */
package net.hypixel.skyblock.api.placeholder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.hypixel.skyblock.Repeater;
import net.hypixel.skyblock.SkyBlock;
import net.hypixel.skyblock.features.potion.ActivePotionEffect;
import net.hypixel.skyblock.features.skill.CombatSkill;
import net.hypixel.skyblock.features.skill.Skill;
import net.hypixel.skyblock.item.SItem;
import net.hypixel.skyblock.item.pet.Pet;
import net.hypixel.skyblock.user.PlayerStatistics;
import net.hypixel.skyblock.user.PlayerUtils;
import net.hypixel.skyblock.user.User;
import net.hypixel.skyblock.util.SUtil;
import net.hypixel.skyblock.util.Sputnik;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.codec.binary.Base64;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;

public class SkyblockPlaceholder
extends PlaceholderExpansion {
    public static final Map<UUID, String> PTE_CACHE = new HashMap<UUID, String>();

    public boolean canRegister() {
        return true;
    }

    public String getAuthor() {
        return "Lexxxy";
    }

    public String getIdentifier() {
        return "skyblock";
    }

    public String getVersion() {
        return "0.1.6";
    }

    public String onRequest(OfflinePlayer player, String identifier) {
        UUID uuid = player.getUniqueId();
        User user = User.getUser(player.getUniqueId());
        PlayerStatistics statistics = PlayerUtils.STATISTICS_CACHE.get(uuid);
        double visualcap = statistics.getCritChance().addAll() * 100.0;
        if (visualcap > 100.0) {
            visualcap = 100.0;
        }
        if (player.isOnline()) {
            if (identifier.equals("defense")) {
                return SUtil.commaify(statistics.getDefense().addAll().intValue());
            }
            if (identifier.equals("strength")) {
                return SUtil.commaify(statistics.getStrength().addAll().intValue());
            }
            if (identifier.equals("speed")) {
                return String.valueOf(statistics.getSpeed().addAll() * 100.0);
            }
            if (identifier.equals("critchance")) {
                return String.valueOf((int)visualcap);
            }
            if (identifier.equals("critdamage")) {
                return String.valueOf(statistics.getCritDamage().addAll() * 100.0);
            }
            if (identifier.equals("int")) {
                return SUtil.commaify(statistics.getIntelligence().addAll().intValue());
            }
            if (identifier.equals("coins")) {
                return String.valueOf(user.getCoins());
            }
            if (identifier.equals("ferocity")) {
                return SUtil.commaify(statistics.getFerocity().addAll().intValue());
            }
            if (identifier.equals("atkSpeed")) {
                return String.valueOf(SUtil.commaify(Math.min(100.0, statistics.getAttackSpeed().addAll())));
            }
            if (identifier.equals("abilityDamage")) {
                return SUtil.commaify(statistics.getAbilityDamage().addAll().intValue());
            }
            if (identifier.equals("pet")) {
                return this.findPet(player);
            }
            if (identifier.equals("pet_lore")) {
                StringBuilder sb = new StringBuilder();
                Pet.PetItem pet = this.findPetClass(player);
                if (pet == null) {
                    return Sputnik.trans("&cNone");
                }
                SItem item = SItem.of(pet.getType());
                item.setRarity(pet.getRarity());
                item.setDataDouble("xp", pet.getXp());
                item.getData().setBoolean("equipped", true);
                item.update();
                ItemStack stacc = item.getStack();
                for (String s : stacc.getItemMeta().getLore()) {
                    sb.append(s + "\n");
                }
                sb.append(item.getRarity().getBoldedColor() + item.getRarity().getDisplay());
                return sb.toString();
            }
            if (identifier.equals("pet_texture")) {
                Pet pet2 = this.findPetClassA(player);
                if (pet2 == null) {
                    return "Steve";
                }
                String URL2 = "{\"textures\":{\"SKIN\":{\"url\":\"http://textures.minecraft.net/texture/" + pet2.getURL() + "\"}}}";
                String encodedString = Base64.encodeBase64String(URL2.getBytes());
                return encodedString;
            }
            if (identifier.equals("potion")) {
                return this.getEffectLoop(player);
            }
            if (identifier.equals("hitshield")) {
                return Repeater.get(player.getPlayer());
            }
            if (identifier.equals("cookie")) {
                return PlayerUtils.getCookieDurationDisplay(player.getPlayer());
            }
            if (identifier.equals("server_name")) {
                return SkyBlock.getPlugin().getServerName();
            }
            if (identifier.equals("server_date")) {
                return SUtil.getDate();
            }
            if (identifier.equals("badge")) {
                if (PlayerUtils.getCookieDurationTicks(player.getPlayer()) > 0L) {
                    return ChatColor.YELLOW + " \u272a";
                }
                return "";
            }
            if (identifier.equals("cb_status")) {
                if (PlayerUtils.getCookieDurationTicks(player.getPlayer()) > 0L) {
                    return SUtil.getFormattedTimeToDay(PlayerUtils.getCookieDurationTicks(player.getPlayer()));
                }
                return ChatColor.RED + "Not actived!";
            }
            if (identifier.equals("combatlevel")) {
                CombatSkill skill = CombatSkill.INSTANCE;
                double xp = skill != null ? user.getSkillXP(skill) : 0.0;
                int level = skill != null ? Skill.getLevel(xp, ((Skill)skill).hasSixtyLevels()) : 0;
                return String.valueOf(level);
            }
            if (identifier.equals("theendlvl")) {
                int level;
                CombatSkill skill = CombatSkill.INSTANCE;
                double xp = skill != null ? user.getSkillXP(skill) : 0.0;
                int n = level = skill != null ? Skill.getLevel(xp, ((Skill)skill).hasSixtyLevels()) : 0;
                if (level >= 5) {
                    return "true";
                }
                return "false";
            }
            if (identifier.equals("dragonlvl")) {
                int level;
                CombatSkill skill = CombatSkill.INSTANCE;
                double xp = skill != null ? user.getSkillXP(skill) : 0.0;
                int n = level = skill != null ? Skill.getLevel(xp, ((Skill)skill).hasSixtyLevels()) : 0;
                if (level >= 6) {
                    return "true";
                }
                return "false";
            }
        }
        if (identifier.equals("info")) {
            return ChatColor.RED + "SKY" + ChatColor.GOLD + "SIM" + ChatColor.GREEN + " PLACEHOLDER v0.1.3 - POWERED BY PLACEHOLDERAPI";
        }
        return null;
    }

    public String findPet(OfflinePlayer player) {
        Pet.PetItem active = User.getUser(player.getUniqueId()).getActivePet();
        Pet petclass = User.getUser(player.getUniqueId()).getActivePetClass();
        String displayname = Sputnik.trans("&cNone");
        if (active != null && petclass != null) {
            int level = Pet.getLevel(active.getXp(), active.getRarity());
            displayname = Sputnik.trans("&7[Lvl " + level + "&7] " + active.toItem().getRarity().getColor() + petclass.getDisplayName());
        }
        return displayname;
    }

    public Pet.PetItem findPetClass(OfflinePlayer player) {
        Pet.PetItem active = User.getUser(player.getUniqueId()).getActivePet();
        Pet petclass = User.getUser(player.getUniqueId()).getActivePetClass();
        String displayname = Sputnik.trans("&cNone");
        if (active != null && petclass != null) {
            return active;
        }
        return null;
    }

    public Pet findPetClassA(OfflinePlayer player) {
        Pet.PetItem active = User.getUser(player.getUniqueId()).getActivePet();
        Pet petclass = User.getUser(player.getUniqueId()).getActivePetClass();
        String displayname = Sputnik.trans("&cNone");
        if (active != null && petclass != null) {
            return petclass;
        }
        return null;
    }

    public String getEffectLoop(OfflinePlayer player) {
        String returnString = Sputnik.trans(" &7No active effects. Drink Potions or splash \nthem to the ground to buff yourself.");
        User user = User.getUser(player.getUniqueId());
        ArrayList<ActivePotionEffect> pte = new ArrayList();
        if (user != null) {
            pte = user.getEffects();
            if (user.getEffects().size() > 0) {
                returnString = Sputnik.trans(" &7You have &6" + user.getEffects().size() + " &7effects. Use \"&6/potions&7\" to see them ") + "\n" + Sputnik.trans(this.a(user, pte));
            }
        }
        return returnString;
    }

    public String a(User user, List<ActivePotionEffect> pte) {
        ActivePotionEffect effect = pte.get(Math.min(pte.size(), Repeater.PTN_CACHE.get(user.getUuid())));
        PTE_CACHE.put(user.getUuid(), effect.getEffect().getType().getName() + " " + SUtil.toRomanNumeral(effect.getEffect().getLevel()) + " " + ChatColor.WHITE + effect.getRemainingDisplay());
        if (PTE_CACHE.containsKey(user.getUuid())) {
            return PTE_CACHE.get(user.getUuid());
        }
        return "";
    }
}

