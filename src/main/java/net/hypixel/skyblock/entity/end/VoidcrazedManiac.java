package net.hypixel.skyblock.entity.end;

import net.md_5.bungee.api.ChatColor;
import net.hypixel.skyblock.entity.EntityDrop;
import net.hypixel.skyblock.entity.EntityDropType;
import net.hypixel.skyblock.item.SMaterial;

import java.util.Collections;
import java.util.List;

public class VoidcrazedManiac extends BaseEnderman {
    @Override
    public String getEntityName() {
        return ChatColor.DARK_RED + "Voidcrazed Maniac";
    }

    @Override
    public double getEntityMaxHealth() {
        return 7.5E7;
    }

    @Override
    public double getDamageDealt() {
        return 15000.0;
    }
    
    @Override
    public int mobLevel() {
        return 4330;
    }

    @Override
    public double getXPDropped() {
        return 5000.0;
    }

    @Override
    public List<EntityDrop> drops() {
        return Collections.singletonList(new EntityDrop(SMaterial.NULL_SPHERE, EntityDropType.GUARANTEED, 1.0));
    }
}
