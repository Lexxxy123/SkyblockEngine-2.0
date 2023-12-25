package in.godspunky.skyblock.entity.dungeons;

import in.godspunky.skyblock.Skyblock;
import me.libraryaddict.disguise.disguisetypes.PlayerDisguise;
import me.libraryaddict.disguise.disguisetypes.watchers.PlayerWatcher;
import org.bukkit.entity.LivingEntity;
import org.bukkit.metadata.FixedMetadataValue;
import in.godspunky.skyblock.entity.SEntity;
import in.godspunky.skyblock.entity.zombie.BaseZombie;
import in.godspunky.skyblock.util.EntityManager;
import in.godspunky.skyblock.util.Sputnik;

public class NPCMobsAI extends BaseZombie {
    private PlayerWatcher watcher;
    private String skinURL;
    private String skinURL_P2;
    private boolean useURL;

    @Override
    public String getEntityName() {
        return "Empty NPC Entity";
    }

    @Override
    public double getEntityMaxHealth() {
        return 0.0;
    }

    @Override
    public double getDamageDealt() {
        return 0.0;
    }

    @Override
    public double getXPDropped() {
        return 0.0;
    }

    @Override
    public void onSpawn(final LivingEntity entity, final SEntity sEntity) {
        final PlayerDisguise pl = Sputnik.applyPacketNPC(entity, "adventuure", null, false);
        this.watcher = pl.getWatcher();
        EntityManager.DEFENSE_PERCENTAGE.put(entity, 80);
        entity.setMetadata("SlayerBoss", new FixedMetadataValue(Skyblock.getPlugin(), true));
        entity.setMetadata("LD", new FixedMetadataValue(Skyblock.getPlugin(), true));
        this.activeEvent(entity, sEntity);
    }

    public void activeEvent(final LivingEntity entity, final SEntity sEntity) {
    }

    public String getSkinURL() {
        return "";
    }
}
