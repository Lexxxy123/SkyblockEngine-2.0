package vn.giakhanhvn.skysim.entity.wolf;

import net.md_5.bungee.api.ChatColor;
import vn.giakhanhvn.skysim.entity.EntityDrop;
import vn.giakhanhvn.skysim.entity.EntityDropType;
import vn.giakhanhvn.skysim.item.SItem;
import vn.giakhanhvn.skysim.item.SMaterial;
import vn.giakhanhvn.skysim.util.SUtil;

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
