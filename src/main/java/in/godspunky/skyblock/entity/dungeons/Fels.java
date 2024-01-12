package in.godspunky.skyblock.entity.dungeons;

import in.godspunky.skyblock.Skyblock;
import in.godspunky.skyblock.entity.SEntity;
import in.godspunky.skyblock.entity.end.BaseEnderman;
import in.godspunky.skyblock.util.EntityManager;
import org.bukkit.entity.LivingEntity;
import org.bukkit.metadata.FixedMetadataValue;

public class Fels extends BaseEnderman {
    @Override
    public String getEntityName() {
        return "Fels";
    }

    @Override
    public double getEntityMaxHealth() {
        return 1.0E8;
    }

    @Override
    public double getDamageDealt() {
        return 1400000.0;
    }

    @Override
    public void onSpawn(final LivingEntity entity, final SEntity sEntity) {
        entity.setMetadata("upsidedown", new FixedMetadataValue(Skyblock.getPlugin(), true));
        entity.setMetadata("SlayerBoss", new FixedMetadataValue(Skyblock.getPlugin(), true));
        EntityManager.DEFENSE_PERCENTAGE.put(entity, 60);
    }

    @Override
    public double getXPDropped() {
        return 320.0;
    }

    @Override
    public int mobLevel() {
        return 0;
    }
}
