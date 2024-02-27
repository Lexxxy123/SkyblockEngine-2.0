package in.godspunky.skyblock.features.sequence;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.List;

public class AnimationSequenceType {
    private static final List<AnimationSequenceType> TYPES;
    private final String namespace;
    private final AnimationSequence sequence;

    public AnimationSequenceType(final String namespace, final AnimationSequence sequence) {
        this.namespace = namespace;
        this.sequence = sequence;
        AnimationSequenceType.TYPES.add(this);
    }

    public String getNamespace() {
        return this.namespace;
    }

    public AnimationSequence getSequence() {
        return this.sequence;
    }

    public void play(final Location location) {
        this.sequence.play(location);
    }

    public void play(final Entity entity) {
        this.sequence.play(entity);
    }

    public static AnimationSequenceType getByNamespace(final String namespace) {
        for (final AnimationSequenceType type : AnimationSequenceType.TYPES) {
            if (namespace.equalsIgnoreCase(type.namespace)) {
                return type;
            }
        }
        return null;
    }

    static {
        TYPES = new ArrayList<AnimationSequenceType>();
    }
}
