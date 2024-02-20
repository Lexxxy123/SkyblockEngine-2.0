package in.godspunky.skyblock.entity.dungeons.regularentity;

import com.google.common.util.concurrent.AtomicDouble;
import me.libraryaddict.disguise.disguisetypes.PlayerDisguise;
import me.libraryaddict.disguise.disguisetypes.watchers.PlayerWatcher;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Material;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftZombie;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.WitherSkull;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import in.godspunky.skyblock.SkyBlock;
import in.godspunky.skyblock.entity.SEntity;
import in.godspunky.skyblock.entity.SEntityEquipment;
import in.godspunky.skyblock.entity.zombie.BaseZombie;
import in.godspunky.skyblock.entity.zombie.NPCMobs;
import in.godspunky.skyblock.item.SItem;
import in.godspunky.skyblock.item.SMaterial;
import in.godspunky.skyblock.util.EntityManager;
import in.godspunky.skyblock.util.SUtil;
import in.godspunky.skyblock.util.Sputnik;

public class CryptSoulstealer extends BaseZombie implements NPCMobs {
    private boolean isBowing;

    public CryptSoulstealer() {
        this.isBowing = false;
    }

    @Override
    public String getEntityName() {
        return "Crypt Soulstealer";
    }

    @Override
    public double getEntityMaxHealth() {
        return 1.0E8;
    }

    @Override
    public double getDamageDealt() {
        return 1000000.0;
    }

    public static ItemStack b(final int hexcolor, final Material m) {
        final ItemStack stack = SUtil.applyColorToLeatherArmor(new ItemStack(m), Color.fromRGB(hexcolor));
        final ItemMeta itemMeta = stack.getItemMeta();
        itemMeta.spigot().setUnbreakable(true);
        stack.setItemMeta(itemMeta);
        return stack;
    }

    @Override
    public void onSpawn(final LivingEntity entity, final SEntity sEntity) {
        ((CraftZombie) entity).setBaby(false);
        final AttributeInstance followRange = ((CraftLivingEntity) entity).getHandle().getAttributeInstance(GenericAttributes.FOLLOW_RANGE);
        followRange.setValue(40.0);
        final PlayerDisguise pl = Sputnik.applyPacketNPC(entity, "ewogICJ0aW1lc3RhbXAiIDogMTYyNjMyMjczMDkzMSwKICAicHJvZmlsZUlkIiA6ICIzZmM3ZmRmOTM5NjM0YzQxOTExOTliYTNmN2NjM2ZlZCIsCiAgInByb2ZpbGVOYW1lIiA6ICJZZWxlaGEiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDA5M2Q2NTJjMWI5ZjIyZmUwZTgxZWIxYTAyOGZhNGIwYjY5MDRjZWQ1YzdlZTlkNjI5YTgxOTU2MDc2NDk5YyIKICAgIH0KICB9Cn0=", "NybQzjteIreKG8mUVlpy4+gVYEloMFxsdAQyfRk5+WS2braPgTWfwxVvv8sukxJLpgxQjrOzSOVhwW5k4cO9j2n8ugWUOzUWnrxGzKmvegZ5UTmDVanLhg2ESFce0oFadJ7RrrQeYYgfqFFjKsA/9Q+Aky0KfdV38pt8U2UsGq68IVSjyickXD3QiwHR9u4FINT98th6m4/9iwhm80Oz1wd9C3O4kdpqGwNWrxLJx8MlcTfzmqSnuuw8bpSNXjXeD1yuScqAXkr8CYg78vg106YFQMNMuwNyIJX65HtTnjJD01xjoKVDw+jKZkFy9v/9ejtQyUjv1cumzrD+lQDejbKyFDNq5cuS0FGza3cfZrqXDXLRr4ujxARNQGxDsbRaXHVbGhuVnHfKy2Z5SjjPOgAzk+ZLzt3nINsp0lRj9xxYilOnKLi+6ExC38+1xUwcU2jtqvkqqCHYDe35WtVIj6nir/sBSbOu93z2anM7/eFH2cboGP/JVwrAJ4o5gH2u644DTxfB9zd6uUqs2mKGwSDd6N/S8IYJmjjQbk87mj9NpnMvWbPVpAs7pmROzuLJ12w+wJtUz6LqU1Nr5YgZyT2NgGiG9xZl560RAAXtNDexM29Zy+gNfIL6aYuLoy6Jz0OhPcKmDfsVWsSsUO7AQDRSLcc5cgGO17m/P0E0l6o=", true);
        pl.getWatcher().setRightClicking(false);
        final PlayerWatcher skywatch = pl.getWatcher();
        EntityManager.DEFENSE_PERCENTAGE.put(entity, 70);
        entity.setMetadata("LD", new FixedMetadataValue(SkyBlock.getPlugin(), true));
        entity.setMetadata("DungeonMobs", new FixedMetadataValue(SkyBlock.getPlugin(), true));
        entity.setMetadata("SlayerBoss", new FixedMetadataValue(SkyBlock.getPlugin(), true));
        new BukkitRunnable() {
            public void run() {
                if (entity.isDead()) {
                    Sputnik.zero(entity);
                    this.cancel();
                    return;
                }
                final LivingEntity target1 = ((CraftZombie) entity).getTarget();
                if (target1 != null) {
                    if (target1.getLocation().distance(entity.getLocation()) >= 2.0 && target1.getLocation().distance(entity.getLocation()) < 16.0) {
                        entity.teleport(entity.getLocation().setDirection(target1.getLocation().toVector().subtract(entity.getLocation().toVector())));
                        CryptSoulstealer.sendHeadRotation(entity, entity.getLocation().getYaw(), entity.getLocation().getPitch());
                    }
                    if (target1.getLocation().distance(entity.getLocation()) < 2.0 || target1.getLocation().distance(entity.getLocation()) > 16.0) {
                        SUtil.delay(() -> {
                            final Object val$entity = entity;
                            entity.getEquipment().setItemInHand(SItem.of(SMaterial.BOW).getStack());
                        }, 0L);
                        CryptSoulstealer.this.isBowing = false;
                    }
                    if (target1.getLocation().distance(entity.getLocation()) >= 2.0 && target1.getLocation().distance(entity.getLocation()) < 16.0 && !CryptSoulstealer.this.isBowing) {
                        CryptSoulstealer.this.isBowing = true;
                        skywatch.setRightClicking(false);
                        entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 10000, 4));
                        entity.getEquipment().setItemInHand(SItem.of(SMaterial.BOW).getStack());
                        new BukkitRunnable() {
                            int t = 0;
                            final int atkCharge = 20;

                            public void run() {
                                ++this.t;
                                if (!CryptSoulstealer.this.isBowing) {
                                    skywatch.setRightClicking(false);
                                    entity.removePotionEffect(PotionEffectType.SLOW);
                                    this.cancel();
                                    return;
                                }
                                if (this.t == 5) {
                                    if (!CryptSoulstealer.this.isBowing) {
                                        return;
                                    }
                                    skywatch.setRightClicking(true);
                                }
                                if (this.t == this.atkCharge) {
                                    if (!CryptSoulstealer.this.isBowing) {
                                        return;
                                    }
                                    skywatch.setRightClicking(false);
                                }
                                if (this.t >= this.atkCharge + 1) {
                                    if (!CryptSoulstealer.this.isBowing) {
                                        return;
                                    }
                                    CryptSoulstealer.this.throwSkull(entity, pl);
                                    skywatch.setRightClicking(false);
                                    entity.removePotionEffect(PotionEffectType.SLOW);
                                    CryptSoulstealer.this.isBowing = false;
                                }
                            }
                        }.runTaskTimer(SkyBlock.getPlugin(), 0L, 1L);
                    }
                }
            }
        }.runTaskTimer(SkyBlock.getPlugin(), 0L, 2L);
        new BukkitRunnable() {
            public void run() {
                final EntityLiving nms = ((CraftLivingEntity) entity).getHandle();
                if (entity.isDead()) {
                    this.cancel();
                    return;
                }
                final LivingEntity target1 = ((CraftZombie) entity).getTarget();
                for (final Entity entities : entity.getWorld().getNearbyEntities(entity.getLocation().add(entity.getLocation().getDirection().multiply(1.0)), 1.5, 1.5, 1.5)) {
                    if (CryptSoulstealer.this.isBowing) {
                        continue;
                    }
                    if (!(entities instanceof Player)) {
                        continue;
                    }
                    final Player target2 = (Player) entities;
                    if (target2.getGameMode() == GameMode.CREATIVE) {
                        continue;
                    }
                    if (target2.getGameMode() == GameMode.SPECTATOR) {
                        continue;
                    }
                    if (target2.hasMetadata("NPC")) {
                        continue;
                    }
                    if (target2.getNoDamageTicks() == 7) {
                        continue;
                    }
                    if (SUtil.random(0, 10) > 8) {
                        continue;
                    }
                    entity.teleport(entity.getLocation().setDirection(target2.getLocation().subtract(entities.getLocation()).toVector()));
                    for (final Player players : Bukkit.getOnlinePlayers()) {
                        ((CraftPlayer) players).getHandle().playerConnection.sendPacket(new PacketPlayOutAnimation(((CraftLivingEntity) entity).getHandle(), 0));
                    }
                    nms.r(((CraftPlayer) target2).getHandle());
                    break;
                }
            }
        }.runTaskTimer(SkyBlock.getPlugin(), 0L, 2L);
    }

    @Override
    public SEntityEquipment getEntityEquipment() {
        return new SEntityEquipment(new ItemStack(Material.BOW), null, null, null, null);
    }

    @Override
    public void onDeath(final SEntity sEntity, final Entity killed, final Entity damager) {
    }

    @Override
    public boolean isBaby() {
        return false;
    }

    @Override
    public boolean hasNameTag() {
        return false;
    }

    @Override
    public boolean isVillager() {
        return false;
    }

    @Override
    public void onDamage(final SEntity sEntity, final Entity damager, final EntityDamageByEntityEvent e, final AtomicDouble damage) {
    }

    @Override
    public double getXPDropped() {
        return 56.0;
    }

    @Override
    public double getMovementSpeed() {
        return 0.2;
    }

    public void throwSkull(final LivingEntity e, final PlayerDisguise pl) {
        final WitherSkull skull = (WitherSkull) e.launchProjectile((Class) WitherSkull.class);
        skull.setShooter(e);
        e.getWorld().playSound(e.getLocation(), Sound.WITHER_SHOOT, 1.0f, 1.0f);
    }

    public static void sendHeadRotation(final Entity e, final float yaw, final float pitch) {
        final net.minecraft.server.v1_8_R3.Entity pl = ((CraftZombie) e).getHandle();
        pl.setLocation(e.getLocation().getX(), e.getLocation().getY(), e.getLocation().getZ(), yaw, pitch);
        final PacketPlayOutEntityTeleport packet = new PacketPlayOutEntityTeleport(pl);
        Sputnik.sendPacket(e.getWorld(), packet);
    }
}
