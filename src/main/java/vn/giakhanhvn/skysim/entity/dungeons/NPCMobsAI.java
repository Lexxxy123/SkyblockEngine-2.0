package vn.giakhanhvn.skysim.entity.dungeons;

import me.libraryaddict.disguise.disguisetypes.PlayerDisguise;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.metadata.FixedMetadataValue;
import vn.giakhanhvn.skysim.SkySimEngine;
import vn.giakhanhvn.skysim.util.EntityManager;
import org.bukkit.entity.Entity;
import vn.giakhanhvn.skysim.util.Sputnik;
import vn.giakhanhvn.skysim.entity.SEntity;
import org.bukkit.entity.LivingEntity;
import me.libraryaddict.disguise.disguisetypes.watchers.PlayerWatcher;
import vn.giakhanhvn.skysim.entity.zombie.BaseZombie;

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
        entity.setMetadata("SlayerBoss", new FixedMetadataValue(SkySimEngine.getPlugin(), true));
        entity.setMetadata("LD", new FixedMetadataValue(SkySimEngine.getPlugin(), true));
        this.activeEvent(entity, sEntity);
    }

    public void activeEvent(final LivingEntity entity, final SEntity sEntity) {
    }

    public String getSkinURL() {
        return "";
    }
}
