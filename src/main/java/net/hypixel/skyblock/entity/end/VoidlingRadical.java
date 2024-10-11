package net.hypixel.skyblock.entity.end;

import net.hypixel.skyblock.entity.EntityDrop;
import net.hypixel.skyblock.entity.EntityDropType;
import net.hypixel.skyblock.item.SMaterial;

import java.util.Collections;
import java.util.List;

public class VoidlingRadical extends BaseEnderman {
    @Override
    public String getEntityName() {
        return "Voidling Radical";
    }

    @Override
    public double getEntityMaxHealth() {
        return 1.2E7;
    }

    @Override
    public double getDamageDealt() {
        return 6000.0;
    }
    
    @Override
    public int mobLevel() {
        return 2500;
    }

    @Override
    public double getXPDropped() {
        return 400.0;
    }

    @Override
    public List<EntityDrop> drops() {
        return Collections.singletonList(new EntityDrop(SMaterial.NULL_SPHERE, EntityDropType.GUARANTEED, 1.0));
    }
}
