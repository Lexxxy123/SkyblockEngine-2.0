package in.godspunky.skyblock.gui;

import in.godspunky.skyblock.features.skill.Skill;
import in.godspunky.skyblock.user.PlayerStatistics;
import in.godspunky.skyblock.user.PlayerUtils;
import in.godspunky.skyblock.user.User;
import in.godspunky.skyblock.util.FerocityCalculation;
import in.godspunky.skyblock.util.SUtil;
import in.godspunky.skyblock.util.Sputnik;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Dye;

public class SkyBlockProfileGUI extends GUI {
    public SkyBlockProfileGUI() {
        super("Your SkySim Profile", 54);
    }

    @Override
    public void onOpen(final GUIOpenEvent e) {
        this.fill(BLACK_STAINED_GLASS_PANE);
        this.set(GUIClickableItem.createGUIOpenerItem(GUIType.SKYBLOCK_MENU, e.getPlayer(), ChatColor.GREEN + "Go Back", 48, Material.ARROW, ChatColor.GRAY + "To SkySim Menu"));
        this.set(GUIClickableItem.getCloseItem(49));
        final Player player = e.getPlayer();
        final PlayerStatistics statistics = PlayerUtils.STATISTICS_CACHE.get(player.getUniqueId());
        Double visualcap = statistics.getCritChance().addAll() * 100.0;
        if (visualcap > 100.0) {
            visualcap = 100.0;
        }
        ItemStack itemstack = null;
        final String feroDisplay = "";
        if (statistics.getFerocity().addAll() > 0.0) {
            itemstack = SUtil.getSkullStack(ChatColor.GREEN + "Your SkySim Profile", player.getName(), 1, ChatColor.RED + "  ❤ Health " + ChatColor.WHITE + SUtil.commaify(statistics.getMaxHealth().addAll().intValue()) + " HP", ChatColor.GREEN + "  ❈ Defense " + ChatColor.WHITE + SUtil.commaify(statistics.getDefense().addAll().intValue()), ChatColor.RED + "  ❁ Strength " + ChatColor.WHITE + SUtil.commaify(statistics.getStrength().addAll().intValue()), ChatColor.WHITE + "  ✦ Speed " + SUtil.commaify((statistics.getSpeed().addAll() * 100.0)), ChatColor.BLUE + "  ☣ Crit Chance " + ChatColor.WHITE + SUtil.commaify(visualcap.intValue()) + "%", ChatColor.BLUE + "  ☠ Crit Damage " + ChatColor.WHITE + SUtil.commaify((statistics.getCritDamage().addAll() * 100.0)) + "%", ChatColor.AQUA + "  ✎ Intelligence " + ChatColor.WHITE + SUtil.commaify(statistics.getIntelligence().addAll().intValue()), ChatColor.YELLOW + "  ⚔ Bonus Attack Speed " + ChatColor.WHITE + SUtil.commaify(Math.min(100.0, statistics.getAttackSpeed().addAll())) + "%", ChatColor.DARK_AQUA + "  α Sea Creature Chance " + ChatColor.RED + "✗", ChatColor.LIGHT_PURPLE + "  ♣ Pet Luck " + ChatColor.RED + "✗", ChatColor.AQUA + "  ✯ Magic Find " + ChatColor.WHITE + SUtil.commaify((statistics.getMagicFind().addAll() * 100.0)), ChatColor.RED + "  ⫽ Ferocity " + ChatColor.WHITE + SUtil.commaify(statistics.getFerocity().addAll().intValue()), ChatColor.RED + "  ๑ Ability Damage " + ChatColor.WHITE + SUtil.commaify(statistics.getAbilityDamage().addAll().intValue()) + "%");
        } else {
            itemstack = SUtil.getSkullStack(ChatColor.GREEN + "Your SkySim Profile", player.getName(), 1, ChatColor.RED + "  ❤ Health " + ChatColor.WHITE + SUtil.commaify(statistics.getMaxHealth().addAll().intValue()) + " HP", ChatColor.GREEN + "  ❈ Defense " + ChatColor.WHITE + SUtil.commaify(statistics.getDefense().addAll().intValue()), ChatColor.RED + "  ❁ Strength " + ChatColor.WHITE + SUtil.commaify(statistics.getStrength().addAll().intValue()), ChatColor.WHITE + "  ✦ Speed " + SUtil.commaify((statistics.getSpeed().addAll() * 100.0)), ChatColor.BLUE + "  ☣ Crit Chance " + ChatColor.WHITE + SUtil.commaify(visualcap.intValue()) + "%", ChatColor.BLUE + "  ☠ Crit Damage " + ChatColor.WHITE + SUtil.commaify((statistics.getCritDamage().addAll() * 100.0)) + "%", ChatColor.AQUA + "  ✎ Intelligence " + ChatColor.WHITE + SUtil.commaify(statistics.getIntelligence().addAll().intValue()), ChatColor.YELLOW + "  ⚔ Bonus Attack Speed " + ChatColor.WHITE + SUtil.commaify(Math.min(100.0, statistics.getAttackSpeed().addAll())) + "%", ChatColor.DARK_AQUA + "  α Sea Creature Chance " + ChatColor.RED + "✗", ChatColor.LIGHT_PURPLE + "  ♣ Pet Luck " + ChatColor.RED + "✗", ChatColor.AQUA + "  ✯ Magic Find " + ChatColor.WHITE + SUtil.commaify((statistics.getMagicFind().addAll() * 100.0)), ChatColor.RED + "  ๑ Ability Damage " + ChatColor.WHITE + SUtil.commaify(statistics.getAbilityDamage().addAll().intValue()) + "%");
        }
        this.set(4, itemstack);
        this.set(19, SUtil.getStack(ChatColor.RED + "❤ Health " + ChatColor.WHITE + SUtil.commaify(statistics.getMaxHealth().addAll().intValue()) + " HP", Material.GOLDEN_APPLE, (short) 0, 1, ChatColor.GRAY + "Health is your total maximum", ChatColor.GRAY + "health. Your natural", ChatColor.GRAY + "regeneration gives " + ChatColor.GREEN + SUtil.commaify(Math.round(statistics.getMaxHealth().addAll() * 0.01)) + " HP", ChatColor.GRAY + "every " + ChatColor.GREEN + "2s" + ChatColor.GRAY + "."));
        this.set(20, SUtil.getStack(ChatColor.GREEN + "❈ Defense " + ChatColor.WHITE + SUtil.commaify(statistics.getDefense().addAll().intValue()), Material.IRON_CHESTPLATE, (short) 0, 1, ChatColor.GRAY + "Defense reduces the damage that", ChatColor.GRAY + "you take from enemies.", " ", ChatColor.GRAY + "Damage Reduction: " + ChatColor.GREEN + Math.round(statistics.getDefense().addAll() / (statistics.getDefense().addAll() + 100.0) * 100.0 * 10.0 / 10.0) + "%", ChatColor.GRAY + "Effective Health: " + ChatColor.RED + SUtil.commaify(SUtil.roundTo(statistics.getMaxHealth().addAll() * ((statistics.getDefense().addAll() + 100.0) / 100.0), 1)) + "❤"));
        final Dye dye = new Dye();
        dye.setColor(DyeColor.RED);
        this.set(21, SUtil.getStack(ChatColor.RED + "❁ Strength " + ChatColor.WHITE + SUtil.commaify(statistics.getStrength().addAll().intValue()), Material.BLAZE_POWDER, (short) 0, 1, ChatColor.GRAY + "Strength increases your base", ChatColor.GRAY + "melee damage, including punching", ChatColor.GRAY + "and weapons.", " ", ChatColor.GRAY + "Base Damage: " + ChatColor.GREEN + SUtil.commaify(5.0 + statistics.getStrength().addAll() / 5.0)));
        this.set(22, SUtil.getStack(ChatColor.WHITE + "✦ Speed " + SUtil.commaify((statistics.getSpeed().addAll() * 100.0)), Material.SUGAR, (short) 0, 1, ChatColor.GRAY + "Speed increases your walk speed.", " ", ChatColor.GRAY + "You are " + ChatColor.GREEN + ((statistics.getSpeed().addAll() * 100.0) - 100) + "% " + ChatColor.GRAY + "faster!"));
        this.set(23, SUtil.getSkullURLStack(ChatColor.BLUE + "☣ Crit Chance " + ChatColor.WHITE + SUtil.commaify(visualcap.intValue()) + "%", "3e4f49535a276aacc4dc84133bfe81be5f2a4799a4c04d9a4ddb72d819ec2b2b", 1, ChatColor.GRAY + "Crit Chance is your chance to", ChatColor.GRAY + "deal extra damage on enemies."));
        this.set(24, SUtil.getSkullURLStack(ChatColor.BLUE + "☠ Crit Damage " + ChatColor.WHITE + SUtil.commaify((statistics.getCritDamage().addAll() * 100.0)) + "%", "ddafb23efc57f251878e5328d11cb0eef87b79c87b254a7ec72296f9363ef7c", 1, ChatColor.GRAY + "Crit Damage is the amount of", ChatColor.GRAY + "extra damage you deal when", ChatColor.GRAY + "landing a critical hit."));
        this.set(25, SUtil.getStack(ChatColor.AQUA + "✎ Intelligence " + ChatColor.WHITE + SUtil.commaify(statistics.getIntelligence().addAll().intValue()), Material.ENCHANTED_BOOK, (short) 0, 1, ChatColor.GRAY + "Intelligence increases both your", ChatColor.GRAY + "Mana Pool and the damage of your", ChatColor.GRAY + "magical items.", " ", ChatColor.GRAY + "Magic Damage: +" + ChatColor.AQUA + SUtil.commaify(statistics.getIntelligence().addAll()) + "%", ChatColor.GRAY + "Mana Pool: " + ChatColor.AQUA + SUtil.commaify(statistics.getIntelligence().addAll() + 100.0)));
        this.set(28, SUtil.getStack(ChatColor.RED + "๑ Ability Damage " + ChatColor.WHITE + SUtil.commaify(Math.round(statistics.getAbilityDamage().addAll())) + "%", Material.BEACON, (short) 0, 1, ChatColor.GRAY + "Ability Damage is the percentage", ChatColor.GRAY + "of bonus damage applied to your", ChatColor.GRAY + "spells!", " ", Sputnik.trans("&7Base Ability Damage: &a") + Math.round(0.5 * Skill.getLevel(User.getUser(player.getUniqueId()).getEnchantXP(), false)) + "%", " ", Sputnik.trans("&7Bonus Ability Damage: &8+&e" + (Math.round(statistics.getAbilityDamage().addAll()) - Math.round(0.5 * Skill.getLevel(User.getUser(player.getUniqueId()).getEnchantXP(), false))) + "%")));
        this.set(29, SUtil.getStack(ChatColor.YELLOW + "⚔ Bonus Attack Speed " + ChatColor.WHITE + SUtil.commaify(Math.min(100.0, statistics.getAttackSpeed().addAll())) + "%", Material.GOLD_AXE, (short) 0, 1, ChatColor.GRAY + "Bonus Attack Speed decreases the", ChatColor.GRAY + "time between hits on your", ChatColor.GRAY + "opponent.", " ", Sputnik.trans("&7Base Attack Speed: &a0%"), " ", Sputnik.trans("&7Bonus Attack Speed: &8+&e" + SUtil.commaify(Math.min(100.0, statistics.getAttackSpeed().addAll())) + "%")));
        this.set(30, SUtil.getStack(ChatColor.DARK_AQUA + "α Sea Creature Chance " + ChatColor.RED + "✗", Material.PRISMARINE_CRYSTALS, (short) 0, 1, ChatColor.GRAY + "Sea Creature Chance is your", ChatColor.GRAY + "chance to catch Sea Creatures", ChatColor.GRAY + "while fishing."));
        this.set(32, SUtil.getSkullURLStack(ChatColor.LIGHT_PURPLE + "♣ Pet Luck " + ChatColor.RED + "✗", "93c8aa3fde295fa9f9c27f734bdbab11d33a2e43e855accd7465352377413b", 1, ChatColor.GRAY + "Pet Luck increases how many pets", ChatColor.GRAY + "you find and gives you better", ChatColor.GRAY + "luck when crafting pets."));
        this.set(31, SUtil.getStack(ChatColor.AQUA + "✯ Magic Find " + ChatColor.WHITE + SUtil.commaify(statistics.getMagicFind().addAll() * 100.0), Material.STICK, (short) 0, 1, ChatColor.GRAY + "Magic Find increases how many", ChatColor.GRAY + "rare items you find.", " ", Sputnik.trans("&7Base Magic Find: &a0"), " ", Sputnik.trans("&7Bonus Magic Find: &8+&e" + SUtil.commaify(statistics.getMagicFind().addAll() * 100.0))));
        this.set(33, SUtil.getStack(ChatColor.WHITE + "❂ True Protection " + ChatColor.WHITE + SUtil.commaify(statistics.getTrueDefense().addAll()), Material.INK_SACK, (short) 15, 1, " ", Sputnik.trans("&7Base True Defense: &a0"), " ", Sputnik.trans("&7Bonus True Defense: &8+&e" + SUtil.commaify(statistics.getTrueDefense().addAll()))));
        this.set(34, SUtil.getStack(ChatColor.RED + "⫽ Ferocity " + ChatColor.WHITE + SUtil.commaify(statistics.getFerocity().addAll()), Material.INK_SACK, (short) 1, 1, ChatColor.GRAY + "Ferocity grants percent chance", ChatColor.GRAY + "to double-strike enemies", ChatColor.GRAY + "Increments of 100 increases the", ChatColor.GRAY + "base number of strikes.", " ", Sputnik.trans("&7Base Ferocity: &a0"), " ", Sputnik.trans("&7Bonus Ferocity: &8+&e" + SUtil.commaify(statistics.getFerocity().addAll())), " ", Sputnik.trans("&7Base extra strikes: &c+" + Math.round(FerocityCalculation.ferocityStrikeCurrent((int) Math.round(statistics.getFerocity().addAll())))), Sputnik.trans("&7Chance for 1 more: &c" + Math.round(FerocityCalculation.ferocityPercentNext((int) Math.round(statistics.getFerocity().addAll()))) + "%")));
    }
}
