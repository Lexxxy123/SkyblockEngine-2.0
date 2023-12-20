package in.godspunky.skyblock.reforge;

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

    ReforgeType(final Class<? extends Reforge> clazz, final boolean accessible) {
        this.clazz = clazz;
        this.accessible = accessible;
    }

    ReforgeType(final Class<? extends Reforge> clazz) {
        this(clazz, true);
    }

    public Reforge getReforge() {
        try {
            return this.clazz.newInstance();
        } catch (final InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ReforgeType getReforgeType(final String name) {
        return valueOf(name.toUpperCase());
    }

    public static ReforgeType getByClass(final Class<? extends Reforge> clazz) {
        for (final ReforgeType type : values()) {
            if (type.clazz == clazz) {
                return type;
            }
        }
        return null;
    }

    public boolean isAccessible() {
        return this.accessible;
    }
}
