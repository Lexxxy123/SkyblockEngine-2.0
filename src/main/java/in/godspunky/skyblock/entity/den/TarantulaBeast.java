package in.godspunky.skyblock.entity.den;

import net.md_5.bungee.api.ChatColor;
import in.godspunky.skyblock.entity.EntityDrop;
import in.godspunky.skyblock.entity.EntityDropType;
import in.godspunky.skyblock.item.SItem;
import in.godspunky.skyblock.item.SMaterial;
import in.godspunky.skyblock.util.SUtil;

import java.util.Collections;
import java.util.List;

public class TarantulaBeast extends BaseSpider {
    @Override
    public String getEntityName() {
        return ChatColor.DARK_RED + "Tarantula Beast";
    }

    @Override
    public double getEntityMaxHealth() {
        return 144000.0;
    }

    @Override
    public double getDamageDealt() {
        return 2500.0;
    }

    @Override
    public double getXPDropped() {
        return 300.0;
    }

    @Override
    public List<EntityDrop> drops() {
        return Collections.singletonList(new EntityDrop(SUtil.setStackAmount(SItem.of(SMaterial.TARANTULA_WEB).getStack(), 2), EntityDropType.GUARANTEED, 1.0));
    }
}
