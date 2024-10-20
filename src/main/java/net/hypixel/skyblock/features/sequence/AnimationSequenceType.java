/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.entity.Entity
 */
package net.hypixel.skyblock.features.sequence;

import java.util.ArrayList;
import java.util.List;
import net.hypixel.skyblock.features.sequence.AnimationSequence;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

public class AnimationSequenceType {
    private static final List<AnimationSequenceType> TYPES = new ArrayList<AnimationSequenceType>();
    private final String namespace;
    private final AnimationSequence sequence;

    public AnimationSequenceType(String namespace, AnimationSequence sequence) {
        this.namespace = namespace;
        this.sequence = sequence;
        TYPES.add(this);
    }

    public String getNamespace() {
        return this.namespace;
    }

    public AnimationSequence getSequence() {
        return this.sequence;
    }

    public void play(Location location) {
        this.sequence.play(location);
    }

    public void play(Entity entity) {
        this.sequence.play(entity);
    }

    public static AnimationSequenceType getByNamespace(String namespace) {
        for (AnimationSequenceType type : TYPES) {
            if (!namespace.equalsIgnoreCase(type.namespace)) continue;
            return type;
        }
        return null;
    }
}

