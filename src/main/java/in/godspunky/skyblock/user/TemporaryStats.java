package in.godspunky.skyblock.user;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TemporaryStats {
    public static Map<UUID, TemporaryStats> TEMP_CACHE;

    static {
        TemporaryStats.TEMP_CACHE = new HashMap<UUID, TemporaryStats>();
    }

    private final User targettedUser;
    public float health;
    public float defense;
    public float strength;
    public float speed;
    public float critChance;
    public float critDamage;
    public float intelligence;
    public float ferocity;
    public float attackSpeed;
    public float abilityDamage;
    public float trueDefense;
    public float magicFind;
    public float damageBonus;
    public UUID uuid;

    public TemporaryStats(final User targettedUser) {
        this.targettedUser = targettedUser;
        this.uuid = targettedUser.getUuid();
        this.health = 0.0f;
        this.defense = 0.0f;
        this.strength = 0.0f;
        this.critChance = 0.0f;
        this.critDamage = 0.0f;
        this.speed = 0.0f;
        this.intelligence = 0.0f;
        this.abilityDamage = 0.0f;
        this.trueDefense = 0.0f;
        this.attackSpeed = 0.0f;
        this.magicFind = 0.0f;
        this.ferocity = 0.0f;
        TemporaryStats.TEMP_CACHE.put(this.targettedUser.getUuid(), this);
    }

    public static void cleanStats(final UUID uuid) {
        TemporaryStats.TEMP_CACHE.remove(uuid);
        if (Bukkit.getPlayer(uuid) != null) {
            final PlayerStatistics statistics = PlayerUtils.STATISTICS_CACHE.get(uuid);
            statistics.zeroAll(152);
        }
    }

    public static TemporaryStats getFromPlayer(final Player p) {
        if (TemporaryStats.TEMP_CACHE.containsKey(p.getUniqueId())) {
            return TemporaryStats.TEMP_CACHE.get(p.getUniqueId());
        }
        return null;
    }

    public static TemporaryStats getFromPlayer(final UUID u) {
        if (TemporaryStats.TEMP_CACHE.containsKey(u)) {
            return TemporaryStats.TEMP_CACHE.get(u);
        }
        return null;
    }

    public void cleanUp() {
        TemporaryStats.TEMP_CACHE.remove(this.uuid);
        if (Bukkit.getPlayer(this.targettedUser.getUuid()) != null) {
            final PlayerStatistics statistics = PlayerUtils.STATISTICS_CACHE.get(this.targettedUser.getUuid());
            statistics.zeroAll(152);
        }
    }

    public void cleanStats() {
        if (Bukkit.getPlayer(this.uuid) != null && PlayerUtils.STATISTICS_CACHE.get(this.uuid) != null) {
            final PlayerStatistics statistics = PlayerUtils.STATISTICS_CACHE.get(this.uuid);
            this.health = 0.0f;
            this.defense = 0.0f;
            this.strength = 0.0f;
            this.critChance = 0.0f;
            this.critDamage = 0.0f;
            this.speed = 0.0f;
            this.intelligence = 0.0f;
            this.abilityDamage = 0.0f;
            this.trueDefense = 0.0f;
            this.attackSpeed = 0.0f;
            this.magicFind = 0.0f;
            this.ferocity = 0.0f;
            statistics.zeroAll(152);
        }
    }

    public void update() {
        if (Bukkit.getPlayer(this.targettedUser.getUuid()) == null) {
            this.cleanUp();
            return;
        }
        final PlayerStatistics statistics = PlayerUtils.STATISTICS_CACHE.get(this.targettedUser.getUuid());
        statistics.zeroAll(152);
        statistics.getMaxHealth().set(152, Double.valueOf(this.health));
        statistics.getDefense().set(152, Double.valueOf(this.defense));
        statistics.getStrength().set(152, Double.valueOf(this.strength));
        statistics.getCritChance().set(152, Double.valueOf(this.critChance));
        statistics.getCritDamage().set(152, Double.valueOf(this.critDamage));
        statistics.getIntelligence().set(152, Double.valueOf(this.intelligence));
        statistics.getFerocity().set(152, Double.valueOf(this.ferocity));
        statistics.getMagicFind().set(152, Double.valueOf(this.magicFind));
        statistics.getSpeed().set(152, Double.valueOf(this.speed));
        statistics.getTrueDefense().set(152, Double.valueOf(this.trueDefense));
        statistics.getAttackSpeed().set(152, Double.valueOf(this.attackSpeed));
        statistics.getAbilityDamage().set(152, Double.valueOf(this.abilityDamage));
    }

    public float getHealth() {
        return this.health;
    }

    public void setHealth(final float health) {
        this.health = health;
    }

    public float getDefense() {
        return this.defense;
    }

    public void setDefense(final float defense) {
        this.defense = defense;
    }

    public float getStrength() {
        return this.strength;
    }

    public void setStrength(final float strength) {
        this.strength = strength;
    }

    public float getSpeed() {
        return this.speed;
    }

    public void setSpeed(final float speed) {
        this.speed = speed;
    }

    public float getCritChance() {
        return this.critChance;
    }

    public void setCritChance(final float critChance) {
        this.critChance = critChance;
    }

    public float getCritDamage() {
        return this.critDamage;
    }

    public void setCritDamage(final float critDamage) {
        this.critDamage = critDamage;
    }

    public float getIntelligence() {
        return this.intelligence;
    }

    public void setIntelligence(final float intelligence) {
        this.intelligence = intelligence;
    }

    public float getFerocity() {
        return this.ferocity;
    }

    public void setFerocity(final float ferocity) {
        this.ferocity = ferocity;
    }

    public float getAttackSpeed() {
        return this.attackSpeed;
    }

    public void setAttackSpeed(final float attackSpeed) {
        this.attackSpeed = attackSpeed;
    }

    public float getAbilityDamage() {
        return this.abilityDamage;
    }

    public void setAbilityDamage(final float abilityDamage) {
        this.abilityDamage = abilityDamage;
    }

    public float getTrueDefense() {
        return this.trueDefense;
    }

    public void setTrueDefense(final float trueDefense) {
        this.trueDefense = trueDefense;
    }

    public float getMagicFind() {
        return this.magicFind;
    }

    public void setMagicFind(final float magicFind) {
        this.magicFind = magicFind;
    }

    public float getDamageBonus() {
        return this.damageBonus;
    }

    public void setDamageBonus(final float damageBonus) {
        this.damageBonus = damageBonus;
    }
}
