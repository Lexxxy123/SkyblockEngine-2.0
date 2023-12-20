package in.godspunky.skyblock.entity.end;

import in.godspunky.skyblock.entity.EntityDrop;
import in.godspunky.skyblock.entity.EntityDropType;
import in.godspunky.skyblock.item.SMaterial;

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
    public double getXPDropped() {
        return 400.0;
    }

    @Override
    public List<EntityDrop> drops() {
        return Collections.singletonList(new EntityDrop(SMaterial.NULL_SPHERE, EntityDropType.GUARANTEED, 1.0));
    }
}
