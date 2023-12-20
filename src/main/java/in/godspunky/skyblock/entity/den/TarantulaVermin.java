package in.godspunky.skyblock.entity.den;

import in.godspunky.skyblock.entity.EntityDrop;
import in.godspunky.skyblock.entity.EntityDropType;
import in.godspunky.skyblock.item.SMaterial;

import java.util.Collections;
import java.util.List;

public class TarantulaVermin extends BaseSpider {
    @Override
    public String getEntityName() {
        return "Tarantula Vermin";
    }

    @Override
    public double getEntityMaxHealth() {
        return 54000.0;
    }

    @Override
    public double getDamageDealt() {
        return 900.0;
    }

    @Override
    public double getXPDropped() {
        return 150.0;
    }

    @Override
    public List<EntityDrop> drops() {
        return Collections.singletonList(new EntityDrop(SMaterial.TARANTULA_WEB, EntityDropType.GUARANTEED, 1.0));
    }
}
