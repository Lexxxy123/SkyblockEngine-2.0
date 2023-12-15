package vn.giakhanhvn.skysim.entity.den;

import java.util.Collections;

import vn.giakhanhvn.skysim.entity.EntityDropType;
import vn.giakhanhvn.skysim.util.SUtil;
import vn.giakhanhvn.skysim.item.SItem;
import vn.giakhanhvn.skysim.item.SMaterial;
import vn.giakhanhvn.skysim.entity.EntityDrop;

import java.util.List;

import org.bukkit.plugin.Plugin;
import vn.giakhanhvn.skysim.SkySimEngine;

import java.util.Iterator;

import org.bukkit.entity.Player;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;
import vn.giakhanhvn.skysim.entity.SEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.ChatColor;

public class MutantTarantula extends BaseSpider {
    @Override
    public String getEntityName() {
        return ChatColor.RED + "Mutant Tarantula";
    }

    @Override
    public double getEntityMaxHealth() {
        return 576000.0;
    }

    @Override
    public double getDamageDealt() {
        return 5000.0;
    }

    @Override
    public double getXPDropped() {
        return 500.0;
    }

    @Override
    public void onSpawn(final LivingEntity entity, final SEntity sEntity) {
        new BukkitRunnable() {
            public void run() {
                if (entity.isDead()) {
                    this.cancel();
                    return;
                }
                for (final Entity e : entity.getNearbyEntities(1.0, 1.0, 1.0)) {
                    if (!(e instanceof Player)) {
                        return;
                    }
                    ((Player) e).damage(MutantTarantula.this.getDamageDealt() * 0.5, entity);
                }
            }
        }.runTaskTimer(SkySimEngine.getPlugin(), 20L, 20L);
    }

    @Override
    public List<EntityDrop> drops() {
        return Collections.singletonList(new EntityDrop(SUtil.setStackAmount(SItem.of(SMaterial.TARANTULA_WEB).getStack(), 5), EntityDropType.GUARANTEED, 1.0));
    }
}
