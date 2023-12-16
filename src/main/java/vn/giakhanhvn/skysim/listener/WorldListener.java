package vn.giakhanhvn.skysim.listener;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import net.minecraft.server.v1_8_R3.PacketPlayOutAnimation;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import vn.giakhanhvn.skysim.SkySimEngine;
import vn.giakhanhvn.skysim.entity.SEntity;
import vn.giakhanhvn.skysim.entity.SlimeStatistics;
import vn.giakhanhvn.skysim.entity.caverns.CreeperFunction;
import vn.giakhanhvn.skysim.entity.nms.Dragon;
import vn.giakhanhvn.skysim.event.CreeperIgniteEvent;
import vn.giakhanhvn.skysim.item.*;
import vn.giakhanhvn.skysim.region.Region;
import vn.giakhanhvn.skysim.region.RegionType;
import vn.giakhanhvn.skysim.skill.FarmingSkill;
import vn.giakhanhvn.skysim.skill.ForagingSkill;
import vn.giakhanhvn.skysim.skill.MiningSkill;
import vn.giakhanhvn.skysim.skill.Skill;
import vn.giakhanhvn.skysim.user.PlayerUtils;
import vn.giakhanhvn.skysim.user.User;
import vn.giakhanhvn.skysim.util.Groups;
import vn.giakhanhvn.skysim.util.SUtil;

import java.util.*;

public class WorldListener extends PListener {
    private static final Map<UUID, List<BlockState>> RESTORER;
    private static final List<UUID> ALREADY_TELEPORTING;
    public static final Map<UUID, EnumWrappers.PlayerDigType> isCM;
    public static final Map<UUID, Boolean> isSWI;
    public static final Map<UUID, Integer> miningSpeed;
    public static final Map<UUID, Integer> breakingPower;
    public static final Map<Block, Integer> CACHED_BLOCK_ID;
    public static final Map<Block, Byte> CACHED_BLOCK_BYTE;
    public static ArrayList<Material> blb;
    public static ArrayList<Block> changed_blocks;

    @EventHandler
    public void onCreatureSpawn(final CreatureSpawnEvent e) {
        if (e.getSpawnReason() != CreatureSpawnEvent.SpawnReason.NATURAL) {
            return;
        }
        if (e.getEntity() instanceof FallingBlock) {
            return;
        }
        e.setCancelled(true);
    }

    @EventHandler
    public void onEntityChangeBlock(final EntityChangeBlockEvent e) {
        if (e.getEntity().getType() == EntityType.ENDERMAN) {
            e.setCancelled(true);
        }
        if (e.getBlock().getType() == Material.SOIL && e.getTo() == Material.DIRT) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityExplode(final EntityExplodeEvent e) {
        final Entity entity = e.getEntity();
        if (entity instanceof EnderDragonPart || entity instanceof EnderDragon || entity instanceof Creeper) {
            e.blockList().clear();
        }
    }

    @EventHandler
    public void onBlockIgnite(final BlockIgniteEvent e) {
        if (e.getIgnitingEntity() instanceof Fireball) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockFade(final BlockFadeEvent e) {
        if (e.getNewState().getType() == Material.DIRT || e.getNewState().getType() == Material.GRASS) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDeath(final EntityDeathEvent e) {
        final Entity entity = e.getEntity();
        if (!entity.hasMetadata("specEntityObject")) {
            return;
        }
        e.getDrops().clear();
    }

    @EventHandler
    public void onCreeperIgnite(final CreeperIgniteEvent e) {
        final Creeper creeper = e.getEntity();
        final SEntity sEntity = SEntity.findSEntity(creeper);
        if (sEntity == null) {
            return;
        }
        if (sEntity.getFunction() instanceof CreeperFunction) {
            ((CreeperFunction) sEntity.getFunction()).onCreeperIgnite(e, sEntity);
        }
    }

    @EventHandler
    public void onLeafDecay(final LeavesDecayEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onBlockBreak(final BlockBreakEvent e) {
        final Block block = e.getBlock();
        final Player player = e.getPlayer();
        final User user = User.getUser(player.getUniqueId());
        final SMaterial equiv = SMaterial.getSpecEquivalent(block.getType(), block.getData());
        final Region region = Region.getRegionOfBlock(block);
        final Collection<ItemStack> drops = block.getDrops(e.getPlayer().getItemInHand());
        if (player.getGameMode() != GameMode.CREATIVE) {
            boolean allowBreak = false;
            if (region != null) {
                if (Groups.FORAGING_REGIONS.contains(region.getType()) && (block.getType() == Material.LOG || block.getType() == Material.LOG_2 || block.getType() == Material.LEAVES || block.getType() == Material.LEAVES_2)) {
                    allowBreak = true;
                    final int level = Skill.getLevel(user.getSkillXP(ForagingSkill.INSTANCE), ForagingSkill.INSTANCE.hasSixtyLevels());
                    final double d = ForagingSkill.INSTANCE.getDoubleDropChance(level);
                    final double t = ForagingSkill.INSTANCE.getTripleDropChance(level);
                    extraDrops(drops, d, t, block);
                    addToRestorer(block, player);
                }
                if (Groups.FARMING_REGIONS.contains(region.getType()) && Groups.FARMING_MATERIALS.contains(block.getType())) {
                    allowBreak = true;
                    final int level = Skill.getLevel(user.getSkillXP(FarmingSkill.INSTANCE), FarmingSkill.INSTANCE.hasSixtyLevels());
                    final double d = FarmingSkill.INSTANCE.getDoubleDropChance(level);
                    extraDrops(drops, d, 0.0, block);
                }
                if (Groups.MINING_REGIONS.contains(region.getType())) {
                    final Material type = block.getType();
                    switch (type) {
                        case COAL_ORE:
                        case DIAMOND_BLOCK:
                        case DIAMOND_ORE:
                        case EMERALD_ORE:
                        case GOLD_ORE:
                        case IRON_ORE:
                        case LAPIS_ORE:
                        case REDSTONE_ORE:
                            block.setType(Material.STONE);
                            break;
                        case STONE:
                            if (block.getData() != 0) {
                                break;
                            }
                            block.setType(Material.COBBLESTONE);
                            break;
                        case OBSIDIAN:
                        case ENDER_STONE:
                        case NETHERRACK:
                        case COBBLESTONE:
                            block.setType(Material.BEDROCK);
                            regenerateLater(block, 60L, region.getType());
                            break;
                    }
                    if (type != block.getType()) {
                        e.setCancelled(true);
                        if (equiv.getStatistics() instanceof ExperienceRewardStatistics) {
                            Skill.reward(((ExperienceRewardStatistics) equiv.getStatistics()).getRewardedSkill(), ((ExperienceRewardStatistics) equiv.getStatistics()).getRewardXP(), player);
                        }
                        final int level2 = Skill.getLevel(user.getSkillXP(MiningSkill.INSTANCE), MiningSkill.INSTANCE.hasSixtyLevels());
                        final double d2 = MiningSkill.INSTANCE.getDoubleDropChance(level2);
                        final double t2 = MiningSkill.INSTANCE.getTripleDropChance(level2);
                        for (final ItemStack drop : drops) {
                            final SItem conv = SItem.convert(drop);
                            conv.setOrigin(ItemOrigin.NATURAL_BLOCK);
                            block.getWorld().dropItemNaturally(block.getLocation().clone().add(0.5, 0.5, 0.5), conv.getStack());
                        }
                        extraDrops(drops, d2, t2, block);
                    }
                    if (block.getType() == Material.GLOWSTONE) {
                        allowBreak = true;
                        addToRestorer(block, player);
                    }
                }
            }
            if (user.isOnIsland(block)) {
                allowBreak = true;
            }
            if (!allowBreak) {
                e.setCancelled(true);
            }
        }
        if (equiv.getStatistics() instanceof ExperienceRewardStatistics && !e.isCancelled()) {
            Skill.reward(((ExperienceRewardStatistics) equiv.getStatistics()).getRewardedSkill(), ((ExperienceRewardStatistics) equiv.getStatistics()).getRewardXP(), player);
        }
        final SBlock sBlock = SBlock.getBlock(e.getBlock().getLocation());
        if (sBlock != null && !e.isCancelled()) {
            sBlock.delete();
        }
        if (e.isCancelled() || player.getGameMode() == GameMode.CREATIVE) {
            return;
        }
        e.setCancelled(true);
        for (final ItemStack drop2 : drops) {
            final SItem conv2 = SItem.convert(drop2);
            conv2.setOrigin(ItemOrigin.NATURAL_BLOCK);
            block.getWorld().dropItemNaturally(block.getLocation().clone().add(0.5, 0.5, 0.5), conv2.getStack());
        }
        block.setType(Material.AIR);
    }

    @EventHandler
    public void onFarmlandDecay(final BlockPhysicsEvent e) {
        if (e.getChangedType() == Material.SOIL) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityTarget(final EntityTargetLivingEntityEvent e) {
        final Entity entity = e.getEntity();
        final SEntity sEntity = SEntity.findSEntity(entity);
        if (sEntity == null) {
            return;
        }
        sEntity.getFunction().onTarget(sEntity, e);
        if (!(sEntity.getGenericInstance() instanceof Dragon)) {
            return;
        }
        e.setCancelled(true);
    }

    public void onPortalEnter(final EntityPortalEnterEvent e) {
        final Material portalType = e.getLocation().getBlock().getType();
        final Entity entity = e.getEntity();
        if (WorldListener.ALREADY_TELEPORTING.contains(entity.getUniqueId())) {
            return;
        }
        if (portalType == Material.PORTAL) {
            final World hub = Bukkit.getWorld(this.plugin.config.getString("hub_world").isEmpty() ? "hub" : this.plugin.config.getString("hub_world"));
            if (hub == null) {
                entity.sendMessage(ChatColor.RED + "Could not find a hub world to teleport you to!");
                return;
            }
            WorldListener.ALREADY_TELEPORTING.add(entity.getUniqueId());
            SUtil.delay(() -> WorldListener.ALREADY_TELEPORTING.remove(entity.getUniqueId()), 15L);
            entity.sendMessage(ChatColor.GRAY + "Sending you to the hub...");
            entity.teleport(hub.getSpawnLocation());
        } else {
            if (!(entity instanceof Player)) {
                return;
            }
            WorldListener.ALREADY_TELEPORTING.add(entity.getUniqueId());
            SUtil.delay(() -> WorldListener.ALREADY_TELEPORTING.remove(entity.getUniqueId()), 15L);
            entity.sendMessage(ChatColor.GRAY + "Sending you to your island...");
            PlayerUtils.sendToIsland((Player) entity);
        }
    }

    @EventHandler
    public void onPortal(final PlayerPortalEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onPortalCreate(final EntityCreatePortalEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onWeatherChange(final WeatherChangeEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onSlimeSplit(final SlimeSplitEvent e) {
        final Slime slime = e.getEntity();
        final SEntity sEntity = SEntity.findSEntity(slime);
        if (sEntity != null && sEntity.getStatistics() instanceof SlimeStatistics && !((SlimeStatistics) sEntity.getStatistics()).split()) {
            e.setCancelled(true);
        }
    }

    private static void addToRestorer(final Block block, final Player player) {
        if (WorldListener.RESTORER.containsKey(player.getUniqueId())) {
            WorldListener.RESTORER.get(player.getUniqueId()).add(block.getState());
        } else {
            WorldListener.RESTORER.put(player.getUniqueId(), new ArrayList<BlockState>());
            WorldListener.RESTORER.get(player.getUniqueId()).add(block.getState());
            new BukkitRunnable() {
                public void run() {
                    for (final BlockState state : WorldListener.RESTORER.get(player.getUniqueId())) {
                        state.getBlock().setType(state.getType());
                        state.setRawData(state.getRawData());
                        state.update();
                    }
                    WorldListener.RESTORER.remove(player.getUniqueId());
                }
            }.runTaskLater(SkySimEngine.getPlugin(), 1200L);
        }
    }

    private static void extraDrops(final Collection<ItemStack> drops, final double d, final double t, final Block block) {
        for (final ItemStack drop : drops) {
            int amount = 0;
            if (SUtil.random(0.0, 1.0) < t) {
                amount = 2;
            } else if (SUtil.random(0.0, 1.0) < d) {
                amount = 1;
            }
            if (amount == 0) {
                continue;
            }
            block.getWorld().dropItemNaturally(block.getLocation().clone().add(0.5, 0.5, 0.5), SUtil.setStackAmount(drop, amount));
        }
    }

    private static BukkitTask regenerateLater(final Block block, final long ticks, final RegionType type) {
        return new BukkitRunnable() {
            public void run() {
                if (block.getType() != Material.BEDROCK) {
                    return;
                }
                final int r5 = SUtil.random(1, 5);
                switch (type) {
                    case COAL_MINE:
                        if (SUtil.random(1, 15) == 1) {
                            block.setType(Material.COAL_ORE);
                            break;
                        }
                        block.setType(Material.STONE);
                        break;
                    case GOLD_MINE:
                    case GUNPOWDER_MINES:
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
                    case LAPIS_QUARRY:
                        if (r5 == 1) {
                            block.setType(Material.LAPIS_ORE);
                            break;
                        }
                        block.setType(Material.STONE);
                        break;
                    case PIGMENS_DEN:
                        if (r5 == 1) {
                            block.setType(Material.REDSTONE_ORE);
                            break;
                        }
                        block.setType(Material.STONE);
                        break;
                    case SLIMEHILL:
                        if (r5 == 1) {
                            block.setType(Material.EMERALD_ORE);
                            break;
                        }
                        block.setType(Material.STONE);
                        break;
                    case DIAMOND_RESERVE:
                        if (r5 == 1) {
                            block.setType(Material.DIAMOND_ORE);
                            break;
                        }
                        block.setType(Material.STONE);
                        break;
                    case OBSIDIAN_SANCTUARY:
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
                    case THE_END:
                    case DRAGONS_NEST:
                        block.setType(Material.ENDER_STONE);
                        break;
                    case BLAZING_FORTRESS:
                        block.setType(Material.NETHERRACK);
                        break;
                    default:
                        block.setType(Material.STONE);
                        break;
                }
            }
        }.runTaskLater(SkySimEngine.getPlugin(), ticks);
    }

    @EventHandler
    public void bpe(final BlockPlaceEvent e) {
        if (e.getBlock().getType() != Material.PRISMARINE) {
            return;
        }
        e.getBlock().setMetadata("block_hardness", new FixedMetadataValue(SkySimEngine.getPlugin(), 1200));
        e.getBlock().setMetadata("block_power", new FixedMetadataValue(SkySimEngine.getPlugin(), 4));
    }

    public static void c() {
        SkySimEngine.getPTC().addPacketListener(new PacketAdapter(SkySimEngine.getPlugin(), ListenerPriority.HIGHEST, new PacketType[]{PacketType.Play.Client.BLOCK_DIG}) {
            public void onPacketReceiving(final PacketEvent event) {
                final PacketContainer packet = event.getPacket();
                final EnumWrappers.PlayerDigType digType = packet.getPlayerDigTypes().getValues().get(0);
                if (event.getPlayer() != null) {
                    WorldListener.isCM.put(event.getPlayer().getUniqueId(), digType);
                }
            }
        });
    }

    public double findDivFor(final double a) {
        final double cumB = a / 10.0 / a;
        final double cumTongHopA = a * cumB;
        return 10.0 / cumTongHopA;
    }

    public int getPlayerMiningSpeed(final Player p) {
        if (WorldListener.miningSpeed.containsKey(p.getUniqueId())) {
            return WorldListener.miningSpeed.get(p.getUniqueId());
        }
        WorldListener.miningSpeed.put(p.getUniqueId(), 100);
        return 600;
    }

    public int getPlayerBreakingPower(final Player p) {
        if (WorldListener.breakingPower.containsKey(p.getUniqueId())) {
            return WorldListener.breakingPower.get(p.getUniqueId());
        }
        WorldListener.breakingPower.put(p.getUniqueId(), 8);
        return 8;
    }

    public double findMiningSpeedFor(final Player p, final Block b) {
        double finalResult = 0.0;
        double blockHardness = 15.0;
        double blockPower = 0.0;
        final List<MetadataValue> a = b.getMetadata("block_hardness");
        final Iterator<MetadataValue> iterator = a.iterator();
        if (iterator.hasNext()) {
            final MetadataValue mv = iterator.next();
            blockHardness = mv.asInt();
            p.sendMessage("" + blockHardness);
        }
        final List<MetadataValue> aA = b.getMetadata("block_power");
        final Iterator<MetadataValue> iterator2 = aA.iterator();
        if (iterator2.hasNext()) {
            final MetadataValue mv2 = iterator2.next();
            blockPower = mv2.asInt();
            p.sendMessage("" + blockPower);
        }
        if (blockHardness == 0.0) {
            blockHardness = 15.0;
        }
        if (WorldListener.blb.contains(b.getType())) {
            blockHardness = 2.0E10;
        }
        finalResult = blockHardness * 30.0 / this.getPlayerMiningSpeed(p);
        if (blockPower > this.getPlayerBreakingPower(p)) {
            finalResult = 1.0E8;
        }
        return finalResult;
    }

    public double findMSIS(final Player p, final Block b) {
        double finalResult = 0.0;
        double blockHardness = 100.0;
        double blockPower = 4.0;
        if (b.getType() == Material.WOOL) {
            if (b.getData() == 3) {
                blockPower = 4.0;
                blockHardness = 1500.0;
            } else {
                if (b.getData() != 7) {
                    return -1.0;
                }
                blockPower = 4.0;
                blockHardness = 500.0;
            }
        } else if (b.getType() == Material.PRISMARINE) {
            blockPower = 4.0;
            blockHardness = 800.0;
        } else if (b.getType() == Material.STAINED_CLAY) {
            if (b.getData() != 9) {
                return -1.0;
            }
            blockPower = 4.0;
            blockHardness = 500.0;
        } else if (b.getType() == Material.STONE) {
            if (b.getData() == 4) {
                blockPower = 5.0;
                blockHardness = 2000.0;
            } else if (b.getData() == 0) {
                blockHardness = this.miningValueForMaterial(b.getType());
                blockPower = 4.0;
            }
        } else {
            blockHardness = this.miningValueForMaterial(b.getType());
        }
        if (WorldListener.blb.contains(b.getType())) {
            blockHardness = 2.0E10;
        }
        finalResult = blockHardness * 30.0 / this.getPlayerMiningSpeed(p);
        if (blockPower > this.getPlayerBreakingPower(p)) {
            finalResult = 1.0E8;
        }
        return finalResult;
    }

    public int miningValueForMaterial(final Material m) {
        switch (m) {
            case STONE:
                return 15;
            case COBBLESTONE:
                return 20;
            case COAL_ORE:
            case DIAMOND_ORE:
            case EMERALD_ORE:
            case GOLD_ORE:
            case IRON_ORE:
            case LAPIS_ORE:
            case REDSTONE_ORE:
            case ENDER_STONE:
            case QUARTZ_ORE:
                return 60;
            case DIAMOND_BLOCK:
            case EMERALD_BLOCK:
                return 100;
            case OBSIDIAN:
                return 1000;
            default:
                return -1;
        }
    }

    public int breakingPowerForMaterial(final Material m) {
        switch (m) {
            case STONE:
                return 15;
            case COBBLESTONE:
                return 20;
            case COAL_ORE:
            case DIAMOND_ORE:
            case EMERALD_ORE:
            case GOLD_ORE:
            case IRON_ORE:
            case LAPIS_ORE:
            case REDSTONE_ORE:
            case ENDER_STONE:
            case QUARTZ_ORE:
                return 60;
            case DIAMOND_BLOCK:
            case EMERALD_BLOCK:
                return 100;
            case OBSIDIAN:
                return 1000;
            default:
                return 0;
        }
    }

    @EventHandler
    public void placeBlockEvent(final BlockPlaceEvent e) {
        final Player player = e.getPlayer();
        for (final Player p : Bukkit.getOnlinePlayers()) {
            if (p.getWorld() == player.getWorld() && p.getLocation().distance(player.getLocation()) <= 50.0 && p != player) {
                ((CraftPlayer) p).getHandle().playerConnection.sendPacket(new PacketPlayOutAnimation(((CraftLivingEntity) player).getHandle(), 0));
            }
        }
    }

    static {
        RESTORER = new HashMap<UUID, List<BlockState>>();
        ALREADY_TELEPORTING = new ArrayList<UUID>();
        isCM = new HashMap<UUID, EnumWrappers.PlayerDigType>();
        isSWI = new HashMap<UUID, Boolean>();
        miningSpeed = new HashMap<UUID, Integer>();
        breakingPower = new HashMap<UUID, Integer>();
        CACHED_BLOCK_ID = new HashMap<Block, Integer>();
        CACHED_BLOCK_BYTE = new HashMap<Block, Byte>();
        WorldListener.blb = new ArrayList<Material>();
        WorldListener.changed_blocks = new ArrayList<Block>();
    }
}
