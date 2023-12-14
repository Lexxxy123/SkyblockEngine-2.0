package vn.giakhanhvn.skysim.item.weapon;

import org.bukkit.plugin.Plugin;
import vn.giakhanhvn.skysim.SkySimEngine;
import java.util.Iterator;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutAnimation;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import vn.giakhanhvn.skysim.util.FerocityCalculation;
import vn.giakhanhvn.skysim.listener.PlayerListener;
import vn.giakhanhvn.skysim.util.Sputnik;
import org.bukkit.entity.LivingEntity;
import vn.giakhanhvn.skysim.user.User;
import org.bukkit.Sound;
import java.util.List;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import java.util.ArrayList;
import vn.giakhanhvn.skysim.item.SItem;
import org.bukkit.entity.Player;
import net.md_5.bungee.api.ChatColor;
import vn.giakhanhvn.skysim.item.SpecificItemType;
import vn.giakhanhvn.skysim.item.GenericItemType;
import vn.giakhanhvn.skysim.item.Rarity;
import vn.giakhanhvn.skysim.item.Ability;
import vn.giakhanhvn.skysim.item.MaterialFunction;
import vn.giakhanhvn.skysim.item.ToolStatistics;

public class ShadowFury implements ToolStatistics, MaterialFunction, Ability
{
    @Override
    public int getBaseDamage() {
        return 310;
    }
    
    @Override
    public double getBaseStrength() {
        return 130.0;
    }
    
    @Override
    public double getBaseSpeed() {
        return 0.3;
    }
    
    @Override
    public String getDisplayName() {
        return "Shadow Fury";
    }
    
    @Override
    public Rarity getRarity() {
        return Rarity.LEGENDARY;
    }
    
    @Override
    public GenericItemType getType() {
        return GenericItemType.WEAPON;
    }
    
    @Override
    public SpecificItemType getSpecificType() {
        return SpecificItemType.SWORD;
    }
    
    @Override
    public String getLore() {
        return "";
    }
    
    @Override
    public String getAbilityName() {
        return "Shadow Fury";
    }
    
    @Override
    public String getAbilityDescription() {
        return "Rapidly teleports you to up to " + ChatColor.AQUA + "5 " + ChatColor.GRAY + "enemies within " + ChatColor.YELLOW + "12 " + ChatColor.GRAY + "blocks, rooting each of them and allowing you to hit them.";
    }
    
    @Override
    public void onAbilityUse(final Player player, final SItem sItem) {
        final int count1 = 0;
        final List<Entity> inRange = player.getNearbyEntities(12.0, 12.0, 12.0);
        final List<Entity> filteredList = new ArrayList<Entity>();
        for (final Entity e : inRange) {
            if (e instanceof Damageable && e != player && !(e instanceof ArmorStand) && !(e instanceof Player) && !e.hasMetadata("NPC") && !e.hasMetadata("GiantSword")) {
                if (filteredList.size() >= 5 || filteredList.size() < 0) {
                    break;
                }
                filteredList.add(e);
            }
        }
        if (inRange.size() != 0) {
            new BukkitRunnable() {
                private int run = 0;
                
                public void run() {
                    if (this.run < filteredList.size()) {
                        if (!filteredList.get(this.run).isDead()) {
                            player.teleport(filteredList.get(this.run).getLocation().add(filteredList.get(this.run).getLocation().getDirection().multiply(-1)));
                            player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 3.0f, 1.0f);
                            final User user = User.getUser(player.getUniqueId());
                            final Entity e = filteredList.get(this.run);
                            final Object[] atp = Sputnik.calculateDamage(player, player, sItem.getStack(), (LivingEntity)e, false);
                            final double finalDamage1 = (float)atp[0];
                            PlayerListener.spawnDamageInd(e, (float)atp[2], (boolean)atp[1]);
                            FerocityCalculation.activeFerocityTimes(player, (LivingEntity)e, (int)finalDamage1, (boolean)atp[1]);
                            user.damageEntity((Damageable)e, finalDamage1);
                            for (final Player p : player.getWorld().getPlayers()) {
                                ((CraftPlayer)p).getHandle().playerConnection.sendPacket((Packet)new PacketPlayOutAnimation((net.minecraft.server.v1_8_R3.Entity)((CraftLivingEntity)player).getHandle(), 0));
                            }
                        }
                        ++this.run;
                    }
                    else {
                        this.cancel();
                    }
                }
            }.runTaskTimer((Plugin)SkySimEngine.getPlugin(), 1L, 5L);
        }
        else {
            player.sendMessage(ChatColor.RED + "No nearby target found.");
        }
    }
    
    @Override
    public int getAbilityCooldownTicks() {
        return 50;
    }
    
    @Override
    public int getManaCost() {
        return 0;
    }
}
