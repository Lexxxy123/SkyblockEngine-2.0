package net.hypixel.skyblock.entity.wolf;

import net.md_5.bungee.api.ChatColor;
import net.hypixel.skyblock.entity.EntityDrop;
import net.hypixel.skyblock.entity.EntityDropType;
import net.hypixel.skyblock.item.SItem;
import net.hypixel.skyblock.item.SMaterial;
import net.hypixel.skyblock.util.SUtil;

import java.util.Collections;
import java.util.List;

public class SvenAlpha extends BaseWolf {
    @Override
    public String getEntityName() {
        return ChatColor.DARK_RED + "Sven Alpha";
    }

    @Override
    public double getEntityMaxHealth() {
        return 480000.0;
    }

    @Override
    public double getDamageDealt() {
        return 1300.0;
    }
    
    @Override
    public int mobLevel() {
        return 340;
    }

    @Override
    public List<EntityDrop> drops() {
        return Collections.singletonList(new EntityDrop(SUtil.setStackAmount(SItem.of(SMaterial.WOLF_TOOTH).getStack(), 5), EntityDropType.GUARANTEED, 1.0));
    }

    @Override
    public double getXPDropped() {
        return 500.0;
    }

    @Override
    public boolean isAngry() {
        return true;
    }
}
