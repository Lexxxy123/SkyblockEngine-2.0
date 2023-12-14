package vn.giakhanhvn.skysim.entity.nms;

import net.minecraft.server.v1_8_R3.EntityLiving;
import org.bukkit.entity.LivingEntity;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;

public class KP56 extends CraftPlayer implements SNMSEntity
{
    private int iq;
    
    public KP56(final CraftServer server, final EntityPlayer entity, final int iq) {
        super(server, entity);
        this.iq = 0;
        this.iq = iq;
    }
    
    public int _INVALID_getLastDamage() {
        return this.iq;
    }
    
    public void _INVALID_setLastDamage(final int damage) {
    }
    
    public void _INVALID_damage(final int amount) {
    }
    
    public void _INVALID_damage(final int amount, final Entity source) {
    }
    
    public int _INVALID_getHealth() {
        return 0;
    }
    
    public void _INVALID_setHealth(final int health) {
    }
    
    public int _INVALID_getMaxHealth() {
        return 0;
    }
    
    public void _INVALID_setMaxHealth(final int health) {
    }
    
    public LivingEntity spawn(final Location location) {
        return null;
    }
}
