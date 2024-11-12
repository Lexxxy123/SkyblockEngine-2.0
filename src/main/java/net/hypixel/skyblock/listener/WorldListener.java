/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.comphenix.protocol.PacketType
 *  com.comphenix.protocol.PacketType$Play$Client
 *  com.comphenix.protocol.events.ListenerPriority
 *  com.comphenix.protocol.events.PacketAdapter
 *  com.comphenix.protocol.events.PacketContainer
 *  com.comphenix.protocol.events.PacketEvent
 *  com.comphenix.protocol.events.PacketListener
 *  com.comphenix.protocol.wrappers.EnumWrappers$PlayerDigType
 *  net.minecraft.server.v1_8_R3.Entity
 *  net.minecraft.server.v1_8_R3.Packet
 *  net.minecraft.server.v1_8_R3.PacketPlayOutAnimation
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.GameMode
 *  org.bukkit.Material
 *  org.bukkit.World
 *  org.bukkit.block.Block
 *  org.bukkit.block.BlockState
 *  org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity
 *  org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer
 *  org.bukkit.entity.Creeper
 *  org.bukkit.entity.EnderDragon
 *  org.bukkit.entity.EnderDragonPart
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.FallingBlock
 *  org.bukkit.entity.Fireball
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.entity.Slime
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.block.BlockBreakEvent
 *  org.bukkit.event.block.BlockFadeEvent
 *  org.bukkit.event.block.BlockIgniteEvent
 *  org.bukkit.event.block.BlockPhysicsEvent
 *  org.bukkit.event.block.BlockPlaceEvent
 *  org.bukkit.event.block.LeavesDecayEvent
 *  org.bukkit.event.entity.CreatureSpawnEvent
 *  org.bukkit.event.entity.CreatureSpawnEvent$SpawnReason
 *  org.bukkit.event.entity.EntityChangeBlockEvent
 *  org.bukkit.event.entity.EntityCreatePortalEvent
 *  org.bukkit.event.entity.EntityDeathEvent
 *  org.bukkit.event.entity.EntityExplodeEvent
 *  org.bukkit.event.entity.EntityPortalEnterEvent
 *  org.bukkit.event.entity.EntityTargetLivingEntityEvent
 *  org.bukkit.event.entity.SlimeSplitEvent
 *  org.bukkit.event.player.PlayerPortalEvent
 *  org.bukkit.event.weather.WeatherChangeEvent
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.PlayerInventory
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 *  org.bukkit.scheduler.BukkitTask
 */
package net.hypixel.skyblock.listener;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.events.PacketListener;
import com.comphenix.protocol.wrappers.EnumWrappers;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.hypixel.skyblock.SkyBlock;
import net.hypixel.skyblock.entity.SEntity;
import net.hypixel.skyblock.entity.SlimeStatistics;
import net.hypixel.skyblock.entity.caverns.CreeperFunction;
import net.hypixel.skyblock.entity.dragon.Dragon;
import net.hypixel.skyblock.event.CreeperIgniteEvent;
import net.hypixel.skyblock.features.region.Region;
import net.hypixel.skyblock.features.region.RegionType;
import net.hypixel.skyblock.features.skill.FarmingSkill;
import net.hypixel.skyblock.features.skill.ForagingSkill;
import net.hypixel.skyblock.features.skill.MiningSkill;
import net.hypixel.skyblock.features.skill.Skill;
import net.hypixel.skyblock.item.ExperienceRewardStatistics;
import net.hypixel.skyblock.item.ItemOrigin;
import net.hypixel.skyblock.item.SBlock;
import net.hypixel.skyblock.item.SItem;
import net.hypixel.skyblock.item.SMaterial;
import net.hypixel.skyblock.listener.PListener;
import net.hypixel.skyblock.user.PlayerUtils;
import net.hypixel.skyblock.user.User;
import net.hypixel.skyblock.util.Groups;
import net.hypixel.skyblock.util.SUtil;
import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutAnimation;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.EnderDragonPart;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityCreatePortalEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityPortalEnterEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.entity.SlimeSplitEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class WorldListener
extends PListener {
    private static final Map<UUID, List<BlockState>> RESTORER = new HashMap<UUID, List<BlockState>>();
    private static final List<UUID> ALREADY_TELEPORTING = new ArrayList<UUID>();
    public static final Map<UUID, EnumWrappers.PlayerDigType> isCM = new HashMap<UUID, EnumWrappers.PlayerDigType>();
    public static final Map<UUID, Boolean> isSWI = new HashMap<UUID, Boolean>();
    public static final Map<UUID, Integer> miningSpeed = new HashMap<UUID, Integer>();
    public static final Map<UUID, Integer> breakingPower = new HashMap<UUID, Integer>();
    public static final Map<Block, Integer> CACHED_BLOCK_ID = new HashMap<Block, Integer>();
    public static final Map<Block, Byte> CACHED_BLOCK_BYTE = new HashMap<Block, Byte>();
    public static ArrayList<Material> blb = new ArrayList();
    public static ArrayList<Block> changed_blocks = new ArrayList();

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent e2) {
        if (e2.getSpawnReason() != CreatureSpawnEvent.SpawnReason.NATURAL) {
            return;
        }
        if (e2.getEntity() instanceof FallingBlock) {
            return;
        }
        e2.setCancelled(true);
    }

    @EventHandler
    public void onEntityChangeBlock(EntityChangeBlockEvent e2) {
        if (e2.getEntity().getType() == EntityType.ENDERMAN) {
            e2.setCancelled(true);
        }
        if (e2.getBlock().getType() == Material.SOIL && e2.getTo() == Material.DIRT) {
            e2.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent e2) {
        org.bukkit.entity.Entity entity = e2.getEntity();
        if (entity instanceof EnderDragonPart || entity instanceof EnderDragon || entity instanceof Creeper) {
            e2.blockList().clear();
        }
    }

    @EventHandler
    public void onBlockIgnite(BlockIgniteEvent e2) {
        if (e2.getIgnitingEntity() instanceof Fireball) {
            e2.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockFade(BlockFadeEvent e2) {
        if (e2.getNewState().getType() == Material.DIRT || e2.getNewState().getType() == Material.GRASS) {
            e2.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent e2) {
        LivingEntity entity = e2.getEntity();
        if (!entity.hasMetadata("specEntityObject")) {
            return;
        }
        e2.getDrops().clear();
        PlayerInventory inventory = e2.getEntity().getKiller().getInventory();
        SItem helmet = null;
        SItem chestplate = null;
        SItem leggings = null;
        SItem boots = null;
        if (inventory.getHelmet() != null) {
            helmet = SItem.find(inventory.getHelmet());
        }
        if (inventory.getChestplate() != null) {
            chestplate = SItem.find(inventory.getChestplate());
        }
        if (inventory.getLeggings() != null) {
            leggings = SItem.find(inventory.getLeggings());
        }
        if (inventory.getBoots() != null) {
            boots = SItem.find(inventory.getBoots());
        }
        if (e2.getEntity().getType() == EntityType.ENDERMAN && helmet.getType() == SMaterial.VOIDBANE_HELMET && leggings.getType() == SMaterial.VOIDBANE_LEGGINGS && chestplate.getType() == SMaterial.VOIDBANE_CHESTPLATE && boots.getType() == SMaterial.VOIDBANE_BOOTS) {
            helmet.setKills(helmet.getDataInt("kills") + 1);
            chestplate.setKills(chestplate.getDataInt("kills") + 1);
            leggings.setKills(leggings.getDataInt("kills") + 1);
            boots.setKills(boots.getDataInt("kills") + 1);
        }
    }

    @EventHandler
    public void onCreeperIgnite(CreeperIgniteEvent e2) {
        Creeper creeper = e2.getEntity();
        SEntity sEntity = SEntity.findSEntity((org.bukkit.entity.Entity)creeper);
        if (sEntity == null) {
            return;
        }
        if (sEntity.getFunction() instanceof CreeperFunction) {
            ((CreeperFunction)sEntity.getFunction()).onCreeperIgnite(e2, sEntity);
        }
    }

    @EventHandler
    public void onLeafDecay(LeavesDecayEvent e2) {
        e2.setCancelled(true);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e2) {
        SBlock sBlock;
        Block block = e2.getBlock();
        Player player = e2.getPlayer();
        User user = User.getUser(player.getUniqueId());
        SMaterial equiv = SMaterial.getSpecEquivalent(block.getType(), block.getData());
        Region region = Region.getRegionOfBlock(block);
        Collection drops = block.getDrops(e2.getPlayer().getItemInHand());
        if (player.getGameMode() != GameMode.CREATIVE) {
            boolean allowBreak = false;
            if (region != null) {
                double d2;
                int level;
                if (Groups.FORAGING_REGIONS.contains((Object)region.getType()) && (block.getType() == Material.LOG || block.getType() == Material.LOG_2 || block.getType() == Material.LEAVES || block.getType() == Material.LEAVES_2)) {
                    allowBreak = true;
                    level = Skill.getLevel(user.getSkillXP(ForagingSkill.INSTANCE), ForagingSkill.INSTANCE.hasSixtyLevels());
                    d2 = ForagingSkill.INSTANCE.getDoubleDropChance(level);
                    double t2 = ForagingSkill.INSTANCE.getTripleDropChance(level);
                    WorldListener.extraDrops(drops, d2, t2, block);
                    WorldListener.addToRestorer(block, player);
                }
                if (Groups.FARMING_REGIONS.contains((Object)region.getType()) && Groups.FARMING_MATERIALS.contains(block.getType())) {
                    allowBreak = true;
                    level = Skill.getLevel(user.getSkillXP(FarmingSkill.INSTANCE), FarmingSkill.INSTANCE.hasSixtyLevels());
                    d2 = FarmingSkill.INSTANCE.getDoubleDropChance(level);
                    WorldListener.extraDrops(drops, d2, 0.0, block);
                }
                if (Groups.MINING_REGIONS.contains((Object)region.getType())) {
                    Material type = block.getType();
                    switch (type) {
                        case COAL_ORE: 
                        case DIAMOND_BLOCK: 
                        case DIAMOND_ORE: 
                        case EMERALD_ORE: 
                        case GOLD_ORE: 
                        case IRON_ORE: 
                        case LAPIS_ORE: 
                        case REDSTONE_ORE: {
                            block.setType(Material.STONE);
                            break;
                        }
                        case STONE: {
                            if (block.getData() != 0) break;
                            block.setType(Material.COBBLESTONE);
                            break;
                        }
                        case OBSIDIAN: 
                        case ENDER_STONE: 
                        case NETHERRACK: 
                        case COBBLESTONE: {
                            block.setType(Material.BEDROCK);
                            WorldListener.regenerateLater(block, 60L, region.getType());
                        }
                    }
                    if (type != block.getType()) {
                        e2.setCancelled(true);
                        if (equiv.getStatistics() instanceof ExperienceRewardStatistics) {
                            Skill.reward(((ExperienceRewardStatistics)equiv.getStatistics()).getRewardedSkill(), ((ExperienceRewardStatistics)equiv.getStatistics()).getRewardXP(), player);
                        }
                        int level2 = Skill.getLevel(user.getSkillXP(MiningSkill.INSTANCE), MiningSkill.INSTANCE.hasSixtyLevels());
                        double d22 = MiningSkill.INSTANCE.getDoubleDropChance(level2);
                        double t2 = MiningSkill.INSTANCE.getTripleDropChance(level2);
                        for (ItemStack drop : drops) {
                            SItem conv = SItem.convert(drop);
                            conv.setOrigin(ItemOrigin.NATURAL_BLOCK);
                            block.getWorld().dropItemNaturally(block.getLocation().clone().add(0.5, 0.5, 0.5), conv.getStack());
                        }
                        WorldListener.extraDrops(drops, d22, t2, block);
                    }
                    if (block.getType() == Material.GLOWSTONE) {
                        allowBreak = true;
                        WorldListener.addToRestorer(block, player);
                    }
                }
            }
            if (user.isOnIsland(block)) {
                allowBreak = true;
            }
            if (!allowBreak) {
                e2.setCancelled(true);
            }
        }
        if (equiv.getStatistics() instanceof ExperienceRewardStatistics && !e2.isCancelled()) {
            Skill.reward(((ExperienceRewardStatistics)equiv.getStatistics()).getRewardedSkill(), ((ExperienceRewardStatistics)equiv.getStatistics()).getRewardXP(), player);
        }
        if ((sBlock = SBlock.getBlock(e2.getBlock().getLocation())) != null && !e2.isCancelled()) {
            sBlock.delete();
        }
        if (e2.isCancelled() || player.getGameMode() == GameMode.CREATIVE) {
            return;
        }
        e2.setCancelled(true);
        for (ItemStack drop2 : drops) {
            SItem conv2 = SItem.convert(drop2);
            conv2.setOrigin(ItemOrigin.NATURAL_BLOCK);
            block.getWorld().dropItemNaturally(block.getLocation().clone().add(0.5, 0.5, 0.5), conv2.getStack());
        }
        block.setType(Material.AIR);
    }

    @EventHandler
    public void onFarmlandDecay(BlockPhysicsEvent e2) {
        if (e2.getChangedType() == Material.SOIL) {
            e2.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityTarget(EntityTargetLivingEntityEvent e2) {
        org.bukkit.entity.Entity entity = e2.getEntity();
        SEntity sEntity = SEntity.findSEntity(entity);
        if (sEntity == null) {
            return;
        }
        sEntity.getFunction().onTarget(sEntity, e2);
        if (!(sEntity.getGenericInstance() instanceof Dragon)) {
            return;
        }
        e2.setCancelled(true);
    }

    @EventHandler
    public void onPortalEnter(EntityPortalEnterEvent e2) {
        Material portalType = e2.getLocation().getBlock().getType();
        org.bukkit.entity.Entity entity = e2.getEntity();
        if (ALREADY_TELEPORTING.contains(entity.getUniqueId())) {
            return;
        }
        if (portalType == Material.PORTAL) {
            World hub = Bukkit.getWorld((String)"world");
            if (hub == null) {
                entity.sendMessage(ChatColor.RED + "Could not find a hub world to teleport you to!");
                return;
            }
            ALREADY_TELEPORTING.add(entity.getUniqueId());
            SUtil.delay(() -> ALREADY_TELEPORTING.remove(entity.getUniqueId()), 15L);
            entity.sendMessage(ChatColor.GRAY + "Sending you to the hub...");
            entity.teleport(hub.getSpawnLocation());
        } else {
            if (!(entity instanceof Player)) {
                return;
            }
            ALREADY_TELEPORTING.add(entity.getUniqueId());
            SUtil.delay(() -> ALREADY_TELEPORTING.remove(entity.getUniqueId()), 15L);
            entity.sendMessage(ChatColor.GRAY + "Sending you to your island...");
            PlayerUtils.sendToIsland(Bukkit.getPlayer((UUID)e2.getEntity().getUniqueId()));
        }
    }

    @EventHandler
    public void onPortal(PlayerPortalEvent e2) {
        e2.setCancelled(true);
    }

    @EventHandler
    public void onPortalCreate(EntityCreatePortalEvent e2) {
        e2.setCancelled(true);
    }

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent e2) {
        e2.setCancelled(true);
    }

    @EventHandler
    public void onSlimeSplit(SlimeSplitEvent e2) {
        Slime slime = e2.getEntity();
        SEntity sEntity = SEntity.findSEntity((org.bukkit.entity.Entity)slime);
        if (sEntity != null && sEntity.getStatistics() instanceof SlimeStatistics && !((SlimeStatistics)sEntity.getStatistics()).split()) {
            e2.setCancelled(true);
        }
    }

    private static void addToRestorer(Block block, final Player player) {
        if (RESTORER.containsKey(player.getUniqueId())) {
            RESTORER.get(player.getUniqueId()).add(block.getState());
        } else {
            RESTORER.put(player.getUniqueId(), new ArrayList());
            RESTORER.get(player.getUniqueId()).add(block.getState());
            new BukkitRunnable(){

                public void run() {
                    for (BlockState state : (List)RESTORER.get(player.getUniqueId())) {
                        state.getBlock().setType(state.getType());
                        state.setRawData(state.getRawData());
                        state.update();
                    }
                    RESTORER.remove(player.getUniqueId());
                }
            }.runTaskLater((Plugin)SkyBlock.getPlugin(), 1200L);
        }
    }

    private static void extraDrops(Collection<ItemStack> drops, double d2, double t2, Block block) {
        for (ItemStack drop : drops) {
            int amount = 0;
            if (SUtil.random(0.0, 1.0) < t2) {
                amount = 2;
            } else if (SUtil.random(0.0, 1.0) < d2) {
                amount = 1;
            }
            if (amount == 0) continue;
            block.getWorld().dropItemNaturally(block.getLocation().clone().add(0.5, 0.5, 0.5), SUtil.setStackAmount(drop, amount));
        }
    }

    private static BukkitTask regenerateLater(final Block block, long ticks, final RegionType type) {
        return new BukkitRunnable(){

            public void run() {
                if (block.getType() != Material.BEDROCK) {
                    return;
                }
                int r5 = SUtil.random(1, 5);
                switch (type) {
                    case COAL_MINE: {
                        if (SUtil.random(1, 15) == 1) {
                            block.setType(Material.COAL_ORE);
                            break;
                        }
                        block.setType(Material.STONE);
                        break;
                    }
                    case GOLD_MINE: 
                    case GUNPOWDER_MINES: {
                        if (SUtil.random(1, 20) == 1) {
                            block.setType(Material.GOLD_ORE);
                            break;
                        }
                        if (r5 == 1) {
                            block.setType(Material.IRON_ORE);
                            break;
                        }
                        block.setType(Material.STONE);
                        break;
                    }
                    case LAPIS_QUARRY: {
                        if (r5 == 1) {
                            block.setType(Material.LAPIS_ORE);
                            break;
                        }
                        block.setType(Material.STONE);
                        break;
                    }
                    case PIGMENS_DEN: {
                        if (r5 == 1) {
                            block.setType(Material.REDSTONE_ORE);
                            break;
                        }
                        block.setType(Material.STONE);
                        break;
                    }
                    case SLIMEHILL: {
                        if (r5 == 1) {
                            block.setType(Material.EMERALD_ORE);
                            break;
                        }
                        block.setType(Material.STONE);
                        break;
                    }
                    case DIAMOND_RESERVE: {
                        if (r5 == 1) {
                            block.setType(Material.DIAMOND_ORE);
                            break;
                        }
                        block.setType(Material.STONE);
                        break;
                    }
                    case OBSIDIAN_SANCTUARY: {
                        if (SUtil.random(1, 40) == 1) {
                            block.setType(Material.DIAMOND_BLOCK);
                            break;
                        }
                        if (SUtil.random(1, 30) == 1) {
                            block.setType(Material.OBSIDIAN);
                            break;
                        }
                        if (r5 == 1) {
                            block.setType(Material.DIAMOND_ORE);
                            break;
                        }
                        block.setType(Material.STONE);
                        break;
                    }
                    case THE_END: 
                    case DRAGONS_NEST: {
                        block.setType(Material.ENDER_STONE);
                        break;
                    }
                    case BLAZING_FORTRESS: {
                        block.setType(Material.NETHERRACK);
                        break;
                    }
                    default: {
                        block.setType(Material.STONE);
                    }
                }
            }
        }.runTaskLater((Plugin)SkyBlock.getPlugin(), ticks);
    }

    @EventHandler
    public void bpe(BlockPlaceEvent e2) {
        if (e2.getBlock().getType() != Material.PRISMARINE) {
            return;
        }
        e2.getBlock().setMetadata("block_hardness", (MetadataValue)new FixedMetadataValue((Plugin)SkyBlock.getPlugin(), (Object)1200));
        e2.getBlock().setMetadata("block_power", (MetadataValue)new FixedMetadataValue((Plugin)SkyBlock.getPlugin(), (Object)4));
    }

    public static void init() {
        blb.add(Material.BEDROCK);
        blb.add(Material.COMMAND);
        blb.add(Material.BARRIER);
        blb.add(Material.ENDER_PORTAL_FRAME);
        blb.add(Material.ENDER_PORTAL);
        SkyBlock.getProtocolManager().addPacketListener((PacketListener)new PacketAdapter((Plugin)SkyBlock.getPlugin(), ListenerPriority.HIGHEST, new PacketType[]{PacketType.Play.Client.BLOCK_DIG}){

            public void onPacketReceiving(PacketEvent event) {
                PacketContainer packet = event.getPacket();
                EnumWrappers.PlayerDigType digType = (EnumWrappers.PlayerDigType)packet.getPlayerDigTypes().getValues().get(0);
                if (event.getPlayer() != null) {
                    isCM.put(event.getPlayer().getUniqueId(), digType);
                }
            }
        });
    }

    public static void unloadBlocks() {
        if (changed_blocks.isEmpty()) {
            return;
        }
        for (Block block : changed_blocks) {
            if (!CACHED_BLOCK_ID.containsKey(block) || !CACHED_BLOCK_BYTE.containsKey(block)) continue;
            int id = CACHED_BLOCK_ID.get(block);
            byte data = CACHED_BLOCK_BYTE.get(block);
            block.setTypeIdAndData(id, data, true);
            if (!changed_blocks.contains(block)) continue;
            changed_blocks.remove(block);
        }
    }

    public double findDivFor(double a2) {
        double cumB = a2 / 10.0 / a2;
        double cumTongHopA = a2 * cumB;
        return 10.0 / cumTongHopA;
    }

    public int getPlayerMiningSpeed(Player p2) {
        if (miningSpeed.containsKey(p2.getUniqueId())) {
            return miningSpeed.get(p2.getUniqueId());
        }
        miningSpeed.put(p2.getUniqueId(), 100);
        return 600;
    }

    public int getPlayerBreakingPower(Player p2) {
        if (breakingPower.containsKey(p2.getUniqueId())) {
            return breakingPower.get(p2.getUniqueId());
        }
        breakingPower.put(p2.getUniqueId(), 8);
        return 8;
    }

    public double findMiningSpeedFor(Player p2, Block b2) {
        List aA2;
        Iterator iterator2;
        double finalResult = 0.0;
        double blockHardness = 15.0;
        double blockPower = 0.0;
        List a2 = b2.getMetadata("block_hardness");
        Iterator iterator = a2.iterator();
        if (iterator.hasNext()) {
            MetadataValue mv = (MetadataValue)iterator.next();
            blockHardness = mv.asInt();
            p2.sendMessage("" + blockHardness);
        }
        if ((iterator2 = (aA2 = b2.getMetadata("block_power")).iterator()).hasNext()) {
            MetadataValue mv2 = (MetadataValue)iterator2.next();
            blockPower = mv2.asInt();
            p2.sendMessage("" + blockPower);
        }
        if (blockHardness == 0.0) {
            blockHardness = 15.0;
        }
        if (blb.contains(b2.getType())) {
            blockHardness = 2.0E10;
        }
        finalResult = blockHardness * 30.0 / (double)this.getPlayerMiningSpeed(p2);
        if (blockPower > (double)this.getPlayerBreakingPower(p2)) {
            finalResult = 1.0E8;
        }
        return finalResult;
    }

    public double findMSIS(Player p2, Block b2) {
        double finalResult = 0.0;
        double blockHardness = 100.0;
        double blockPower = 4.0;
        if (b2.getType() == Material.WOOL) {
            if (b2.getData() == 3) {
                blockPower = 4.0;
                blockHardness = 1500.0;
            } else {
                if (b2.getData() != 7) {
                    return -1.0;
                }
                blockPower = 4.0;
                blockHardness = 500.0;
            }
        } else if (b2.getType() == Material.PRISMARINE) {
            blockPower = 4.0;
            blockHardness = 800.0;
        } else if (b2.getType() == Material.STAINED_CLAY) {
            if (b2.getData() != 9) {
                return -1.0;
            }
            blockPower = 4.0;
            blockHardness = 500.0;
        } else if (b2.getType() == Material.STONE) {
            if (b2.getData() == 4) {
                blockPower = 5.0;
                blockHardness = 2000.0;
            } else if (b2.getData() == 0) {
                blockHardness = this.miningValueForMaterial(b2.getType());
                blockPower = 4.0;
            }
        } else {
            blockHardness = this.miningValueForMaterial(b2.getType());
        }
        if (blb.contains(b2.getType())) {
            blockHardness = 2.0E10;
        }
        finalResult = blockHardness * 30.0 / (double)this.getPlayerMiningSpeed(p2);
        if (blockPower > (double)this.getPlayerBreakingPower(p2)) {
            finalResult = 1.0E8;
        }
        return finalResult;
    }

    public int miningValueForMaterial(Material m2) {
        switch (m2) {
            case STONE: {
                return 15;
            }
            case COBBLESTONE: {
                return 20;
            }
            case COAL_ORE: 
            case DIAMOND_ORE: 
            case EMERALD_ORE: 
            case GOLD_ORE: 
            case IRON_ORE: 
            case LAPIS_ORE: 
            case REDSTONE_ORE: 
            case ENDER_STONE: 
            case QUARTZ_ORE: {
                return 60;
            }
            case DIAMOND_BLOCK: 
            case EMERALD_BLOCK: {
                return 100;
            }
            case OBSIDIAN: {
                return 1000;
            }
        }
        return -1;
    }

    public int breakingPowerForMaterial(Material m2) {
        switch (m2) {
            case STONE: {
                return 15;
            }
            case COBBLESTONE: {
                return 20;
            }
            case COAL_ORE: 
            case DIAMOND_ORE: 
            case EMERALD_ORE: 
            case GOLD_ORE: 
            case IRON_ORE: 
            case LAPIS_ORE: 
            case REDSTONE_ORE: 
            case ENDER_STONE: 
            case QUARTZ_ORE: {
                return 60;
            }
            case DIAMOND_BLOCK: 
            case EMERALD_BLOCK: {
                return 100;
            }
            case OBSIDIAN: {
                return 1000;
            }
        }
        return 0;
    }

    @EventHandler
    public void placeBlockEvent(BlockPlaceEvent e2) {
        Player player = e2.getPlayer();
        for (Player p2 : Bukkit.getOnlinePlayers()) {
            if (p2.getWorld() != player.getWorld() || !(p2.getLocation().distance(player.getLocation()) <= 50.0) || p2 == player) continue;
            ((CraftPlayer)p2).getHandle().playerConnection.sendPacket((Packet)new PacketPlayOutAnimation((Entity)((CraftLivingEntity)player).getHandle(), 0));
        }
    }
}

