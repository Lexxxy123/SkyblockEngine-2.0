package in.godspunky.skyblock.entity.zombie;

import me.libraryaddict.disguise.disguisetypes.PlayerDisguise;
import net.minecraft.server.v1_8_R3.AttributeInstance;
import net.minecraft.server.v1_8_R3.GenericAttributes;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftZombie;
import org.bukkit.entity.LivingEntity;
import org.bukkit.metadata.FixedMetadataValue;
import in.godspunky.skyblock.SkyBlock;
import in.godspunky.skyblock.util.Sputnik;

public interface NPCMobs {
    default PlayerDisguise onEntitySpawn(final LivingEntity entity, final String url1, final String url2, final boolean apl1) {
        ((CraftZombie) entity).setBaby(false);
        final AttributeInstance followRange = ((CraftLivingEntity) entity).getHandle().getAttributeInstance(GenericAttributes.FOLLOW_RANGE);
        followRange.setValue(40.0);
        final PlayerDisguise pl = Sputnik.applyPacketNPC(entity, url1, url2, apl1);
        entity.setMetadata("SlayerBoss", new FixedMetadataValue(SkyBlock.getPlugin(), true));
        entity.setMetadata("LD", new FixedMetadataValue(SkyBlock.getPlugin(), true));
        return pl;
    }
}
