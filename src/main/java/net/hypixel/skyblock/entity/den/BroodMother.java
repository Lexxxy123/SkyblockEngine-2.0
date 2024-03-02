package net.hypixel.skyblock.entity.den;

import org.bukkit.entity.Entity;
import net.hypixel.skyblock.entity.SEntity;
import net.hypixel.skyblock.entity.SEntityType;
import net.hypixel.skyblock.util.SUtil;

public class BroodMother extends BaseSpider {
    @Override
    public String getEntityName() {
        return "Brood Mother";
    }

    @Override
    public double getEntityMaxHealth() {
        return 6000.0;
    }

    @Override
    public double getDamageDealt() {
        return 100.0;
    }

    @Override
    public void onDeath(final SEntity sEntity, final Entity killed, final Entity damager) {
        for (int am = SUtil.random(4, 5), i = 0; i < am; ++i) {
            new SEntity(sEntity.getEntity(), SEntityType.CAVE_SPIDER);
        }
    }

    @Override
    public double getXPDropped() {
        return 17.0;
    }
}
