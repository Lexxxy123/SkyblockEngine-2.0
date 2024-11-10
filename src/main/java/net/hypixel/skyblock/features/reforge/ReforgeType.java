/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.hypixel.skyblock.features.reforge;

import net.hypixel.skyblock.features.reforge.Ancient;
import net.hypixel.skyblock.features.reforge.EpicReforge;
import net.hypixel.skyblock.features.reforge.Fabled;
import net.hypixel.skyblock.features.reforge.FastReforge;
import net.hypixel.skyblock.features.reforge.FierceReforge;
import net.hypixel.skyblock.features.reforge.GeniusReforge;
import net.hypixel.skyblock.features.reforge.Hasty;
import net.hypixel.skyblock.features.reforge.HeroicReforge;
import net.hypixel.skyblock.features.reforge.LegendaryReforge;
import net.hypixel.skyblock.features.reforge.Necrotic;
import net.hypixel.skyblock.features.reforge.OddReforge;
import net.hypixel.skyblock.features.reforge.OverpoweredReforge;
import net.hypixel.skyblock.features.reforge.RapidReforge;
import net.hypixel.skyblock.features.reforge.Reforge;
import net.hypixel.skyblock.features.reforge.Renowned;
import net.hypixel.skyblock.features.reforge.SharpReforge;
import net.hypixel.skyblock.features.reforge.SpicyReforge;
import net.hypixel.skyblock.features.reforge.Spiritual;
import net.hypixel.skyblock.features.reforge.StronkReforge;
import net.hypixel.skyblock.features.reforge.SupergeniusReforge;
import net.hypixel.skyblock.features.reforge.Unreal;
import net.hypixel.skyblock.features.reforge.Wise;
import net.hypixel.skyblock.features.reforge.WitheredReforge;

public enum ReforgeType {
    OVERPOWERED(OverpoweredReforge.class, false),
    GENIUS(GeniusReforge.class),
    STRONK(StronkReforge.class),
    SUPERGENIUS(SupergeniusReforge.class, false),
    HASTY(Hasty.class),
    FAST(FastReforge.class),
    SPICY(SpicyReforge.class),
    FIERCE(FierceReforge.class),
    HEROIC(HeroicReforge.class),
    ODD(OddReforge.class),
    RAPID(RapidReforge.class),
    ANCIENT(Ancient.class),
    WITHERED(WitheredReforge.class),
    LEGENDARY(LegendaryReforge.class),
    SHARP(SharpReforge.class),
    EPIC(EpicReforge.class),
    FABLED(Fabled.class),
    RENOWNED(Renowned.class),
    SPIRITUAL(Spiritual.class),
    UNREAL(Unreal.class),
    WISE(Wise.class),
    NECROTIC(Necrotic.class);

    private final Class<? extends Reforge> clazz;
    private final boolean accessible;

    private ReforgeType(Class<? extends Reforge> clazz, boolean accessible) {
        this.clazz = clazz;
        this.accessible = accessible;
    }

    private ReforgeType(Class<? extends Reforge> clazz) {
        this(clazz, true);
    }

    public Reforge getReforge() {
        try {
            return this.clazz.newInstance();
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ReforgeType getReforgeType(String name) {
        return ReforgeType.valueOf(name.toUpperCase());
    }

    public static ReforgeType getByClass(Class<? extends Reforge> clazz) {
        for (ReforgeType type : ReforgeType.values()) {
            if (type.clazz != clazz) continue;
            return type;
        }
        return null;
    }

    public boolean isAccessible() {
        return this.accessible;
    }
}

