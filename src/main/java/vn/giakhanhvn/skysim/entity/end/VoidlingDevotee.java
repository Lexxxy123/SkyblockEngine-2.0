package vn.giakhanhvn.skysim.entity.end;

import java.util.Arrays;
import vn.giakhanhvn.skysim.entity.EntityDropType;
import vn.giakhanhvn.skysim.item.SMaterial;
import vn.giakhanhvn.skysim.entity.EntityDrop;
import java.util.List;

public class VoidlingDevotee extends BaseEnderman
{
    @Override
    public String getEntityName() {
        return "Voidling Devotee";
    }
    
    @Override
    public double getEntityMaxHealth() {
        return 2.5E7;
    }
    
    @Override
    public double getDamageDealt() {
        return 5000.0;
    }
    
    @Override
    public double getXPDropped() {
        return 1500.0;
    }
    
    @Override
    public List<EntityDrop> drops() {
        return Arrays.<EntityDrop>asList(new EntityDrop(SMaterial.NULL_SPHERE, EntityDropType.GUARANTEED, 1.0));
    }
}
