package in.godspunky.skyblock;

import in.godspunky.skyblock.item.SItem;
import in.godspunky.skyblock.item.pet.Pet;
import in.godspunky.skyblock.potion.ActivePotionEffect;
import in.godspunky.skyblock.skill.CombatSkill;
import in.godspunky.skyblock.skill.Skill;
import in.godspunky.skyblock.user.PlayerStatistics;
import in.godspunky.skyblock.user.PlayerUtils;
import in.godspunky.skyblock.user.User;
import in.godspunky.skyblock.util.SUtil;
import in.godspunky.skyblock.util.Sputnik;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.codec.binary.Base64;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class placeholding extends PlaceholderExpansion {
    public static final Map<UUID, String> PTE_CACHE;

    public boolean canRegister() {
        return true;
    }

    public String getAuthor() {
        return "GiaKhanhVN";
    }

    public String getIdentifier() {
        return "skysim";
    }

    public String getVersion() {
        return "0.1.6";
    }

    public String onRequest(final OfflinePlayer player, final String identifier) {
        final UUID uuid = player.getUniqueId();
        final User user = User.getUser(player.getUniqueId());
        final PlayerStatistics statistics = PlayerUtils.STATISTICS_CACHE.get(uuid);
        Double visualcap = statistics.getCritChance().addAll() * 100.0;
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
                return String.valueOf((statistics.getSpeed().addAll() * 100.0));
            }
            if (identifier.equals("critchance")) {
                return String.valueOf(visualcap.intValue());
            }
            if (identifier.equals("critdamage")) {
                return String.valueOf((statistics.getCritDamage().addAll() * 100.0));
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
                final StringBuilder sb = new StringBuilder();
                final Pet.PetItem pet = this.findPetClass(player);
                if (pet == null) {
                    return Sputnik.trans("&cNone");
                }
                final SItem item = SItem.of(pet.getType());
                item.setRarity(pet.getRarity());
                item.setDataDouble("xp", pet.getXp());
                item.getData().setBoolean("equipped", true);
                item.update();
                final ItemStack stacc = item.getStack();
                for (final String s : stacc.getItemMeta().getLore()) {
                    sb.append(s + "\n");
                }
                sb.append(item.getRarity().getBoldedColor() + item.getRarity().getDisplay());
                return sb.toString();
            } else if (identifier.equals("pet_texture")) {
                final Pet pet2 = this.findPetClassA(player);
                if (pet2 == null) {
                    return "Steve";
                }
                final String URL = "{\"textures\":{\"SKIN\":{\"url\":\"http://textures.minecraft.net/texture/" + pet2.getURL() + "\"}}}";
                final String encodedString = Base64.encodeBase64String(URL.getBytes());
                return encodedString;
            } else {
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
                    return SkySimEngine.getPlugin().getServerName();
                }
                if (identifier.equals("server_date")) {
                    return SUtil.getDate();
                }
                if (identifier.equals("badge")) {
                    if (PlayerUtils.getCookieDurationTicks(player.getPlayer()) > 0L) {
                        return ChatColor.YELLOW + " ✪";
                    }
                    return "";
                } else if (identifier.equals("cb_status")) {
                    if (PlayerUtils.getCookieDurationTicks(player.getPlayer()) > 0L) {
                        return SUtil.getFormattedTimeToDay(PlayerUtils.getCookieDurationTicks(player.getPlayer()));
                    }
                    return ChatColor.RED + "Not actived!";
                } else {
                    if (identifier.equals("combatlevel")) {
                        final Skill skill = CombatSkill.INSTANCE;
                        final double xp = (skill != null) ? user.getSkillXP(skill) : 0.0;
                        final int level = (skill != null) ? Skill.getLevel(xp, skill.hasSixtyLevels()) : 0;
                        return String.valueOf(level);
                    }
                    if (identifier.equals("theendlvl")) {
                        final Skill skill = CombatSkill.INSTANCE;
                        final double xp = (skill != null) ? user.getSkillXP(skill) : 0.0;
                        final int level = (skill != null) ? Skill.getLevel(xp, skill.hasSixtyLevels()) : 0;
                        if (level >= 5) {
                            return "true";
                        }
                        return "false";
                    } else if (identifier.equals("dragonlvl")) {
                        final Skill skill = CombatSkill.INSTANCE;
                        final double xp = (skill != null) ? user.getSkillXP(skill) : 0.0;
                        final int level = (skill != null) ? Skill.getLevel(xp, skill.hasSixtyLevels()) : 0;
                        if (level >= 6) {
                            return "true";
                        }
                        return "false";
                    }
                }
            }
        }
        if (identifier.equals("info")) {
            return ChatColor.RED + "SKY" + ChatColor.GOLD + "SIM" + ChatColor.GREEN + " PLACEHOLDER v0.1.3 - POWERED BY PLACEHOLDERAPI";
        }
        return null;
    }

    public String findPet(final OfflinePlayer player) {
        final Pet.PetItem active = User.getUser(player.getUniqueId()).getActivePet();
        final Pet petclass = User.getUser(player.getUniqueId()).getActivePetClass();
        String displayname = Sputnik.trans("&cNone");
        if (active != null && petclass != null) {
            final int level = Pet.getLevel(active.getXp(), active.getRarity());
            displayname = Sputnik.trans("&7[Lvl " + level + "&7] " + active.toItem().getRarity().getColor() + petclass.getDisplayName());
        }
        return displayname;
    }

    public Pet.PetItem findPetClass(final OfflinePlayer player) {
        final Pet.PetItem active = User.getUser(player.getUniqueId()).getActivePet();
        final Pet petclass = User.getUser(player.getUniqueId()).getActivePetClass();
        final String displayname = Sputnik.trans("&cNone");
        if (active != null && petclass != null) {
            return active;
        }
        return null;
    }

    public Pet findPetClassA(final OfflinePlayer player) {
        final Pet.PetItem active = User.getUser(player.getUniqueId()).getActivePet();
        final Pet petclass = User.getUser(player.getUniqueId()).getActivePetClass();
        final String displayname = Sputnik.trans("&cNone");
        if (active != null && petclass != null) {
            return petclass;
        }
        return null;
    }

    public String getEffectLoop(final OfflinePlayer player) {
        String returnString = Sputnik.trans(" &7No active effects. Drink Potions or splash \nthem to the ground to buff yourself.");
        final User user = User.getUser(player.getUniqueId());
        List<ActivePotionEffect> pte = new ArrayList<ActivePotionEffect>();
        if (user != null) {
            pte = user.getEffects();
            if (user.getEffects().size() > 0) {
                returnString = Sputnik.trans(" &7You have &6" + user.getEffects().size() + " &7effects. Use \"&6/potions&7\" to see them ") + "\n" + Sputnik.trans(this.a(user, pte));
            }
        }
        return returnString;
    }

    public String a(final User user, final List<ActivePotionEffect> pte) {
        final ActivePotionEffect effect = pte.get(Math.min(pte.size(), Repeater.PTN_CACHE.get(user.getUuid())));
        placeholding.PTE_CACHE.put(user.getUuid(), effect.getEffect().getType().getName() + " " + SUtil.toRomanNumeral(effect.getEffect().getLevel()) + " " + ChatColor.WHITE + effect.getRemainingDisplay());
        if (placeholding.PTE_CACHE.containsKey(user.getUuid())) {
            return placeholding.PTE_CACHE.get(user.getUuid());
        }
        return "";
    }

    static {
        PTE_CACHE = new HashMap<UUID, String>();
    }
}
