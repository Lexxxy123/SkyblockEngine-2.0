/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  me.libraryaddict.disguise.disguisetypes.PlayerDisguise
 *  me.libraryaddict.disguise.disguisetypes.watchers.PlayerWatcher
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 */
package net.hypixel.skyblock.entity.dungeons;

import me.libraryaddict.disguise.disguisetypes.PlayerDisguise;
import me.libraryaddict.disguise.disguisetypes.watchers.PlayerWatcher;
import net.hypixel.skyblock.SkyBlock;
import net.hypixel.skyblock.entity.SEntity;
import net.hypixel.skyblock.entity.zombie.BaseZombie;
import net.hypixel.skyblock.util.EntityManager;
import net.hypixel.skyblock.util.Sputnik;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

public class NPCMobsAI
extends BaseZombie {
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
    public void onSpawn(LivingEntity entity, SEntity sEntity) {
        PlayerDisguise pl = Sputnik.applyPacketNPC((Entity)entity, "adventuure", null, false);
        this.watcher = pl.getWatcher();
        EntityManager.DEFENSE_PERCENTAGE.put((Entity)entity, 80);
        entity.setMetadata("SlayerBoss", (MetadataValue)new FixedMetadataValue((Plugin)SkyBlock.getPlugin(), (Object)true));
        entity.setMetadata("LD", (MetadataValue)new FixedMetadataValue((Plugin)SkyBlock.getPlugin(), (Object)true));
        this.activeEvent(entity, sEntity);
    }

    public void activeEvent(LivingEntity entity, SEntity sEntity) {
    }

    public String getSkinURL() {
        return "";
    }
}

