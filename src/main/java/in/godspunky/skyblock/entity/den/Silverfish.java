package in.godspunky.skyblock.entity.den;

import in.godspunky.skyblock.entity.EntityFunction;
import in.godspunky.skyblock.entity.EntityStatistics;

public class Silverfish implements EntityStatistics, EntityFunction {
    @Override
    public String getEntityName() {
        return "Silverfish";
    }

    @Override
    public double getEntityMaxHealth() {
        return 50.0;
    }

    @Override
    public double getDamageDealt() {
        return 20.0;
    }

    @Override
    public double getXPDropped() {
        return 5.4;
    }
}
