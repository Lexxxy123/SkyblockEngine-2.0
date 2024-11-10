/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraft.server.v1_8_R3.EntityInsentient
 *  net.minecraft.server.v1_8_R3.EntityTypes
 *  org.bukkit.entity.EntityType
 */
package net.hypixel.skyblock.entity;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Map;
import net.hypixel.skyblock.entity.EntityFunction;
import net.hypixel.skyblock.entity.EntityStatistics;
import net.hypixel.skyblock.entity.Stronker;
import net.hypixel.skyblock.entity.caverns.DiamondSkeleton;
import net.hypixel.skyblock.entity.caverns.DiamondZombie;
import net.hypixel.skyblock.entity.caverns.EnchantedDiamondSkeleton;
import net.hypixel.skyblock.entity.caverns.EnchantedDiamondZombie;
import net.hypixel.skyblock.entity.caverns.LapisZombie;
import net.hypixel.skyblock.entity.caverns.LargeSlime;
import net.hypixel.skyblock.entity.caverns.MediumSlime;
import net.hypixel.skyblock.entity.caverns.Pigman;
import net.hypixel.skyblock.entity.caverns.SmallSlime;
import net.hypixel.skyblock.entity.caverns.UndeadGiaKhanhvn;
import net.hypixel.skyblock.entity.den.BroodMother;
import net.hypixel.skyblock.entity.den.CaveSpider;
import net.hypixel.skyblock.entity.den.DasherSpider;
import net.hypixel.skyblock.entity.den.MutantTarantula;
import net.hypixel.skyblock.entity.den.Silverfish;
import net.hypixel.skyblock.entity.den.SpiderJockey;
import net.hypixel.skyblock.entity.den.SpidersDenSkeleton;
import net.hypixel.skyblock.entity.den.SpidersDenSlime;
import net.hypixel.skyblock.entity.den.SplitterSpider;
import net.hypixel.skyblock.entity.den.TarantulaBeast;
import net.hypixel.skyblock.entity.den.TarantulaVermin;
import net.hypixel.skyblock.entity.den.VoraciousSpider;
import net.hypixel.skyblock.entity.den.WeaverSpider;
import net.hypixel.skyblock.entity.dragon.type.OldDragon;
import net.hypixel.skyblock.entity.dragon.type.ProtectorDragon;
import net.hypixel.skyblock.entity.dragon.type.StrongDragon;
import net.hypixel.skyblock.entity.dragon.type.SuperiorDragon;
import net.hypixel.skyblock.entity.dragon.type.UnstableDragon;
import net.hypixel.skyblock.entity.dragon.type.WiseDragon;
import net.hypixel.skyblock.entity.dragon.type.YoungDragon;
import net.hypixel.skyblock.entity.dungeons.TestingMob;
import net.hypixel.skyblock.entity.dungeons.boss.sadan.BigfootGiant;
import net.hypixel.skyblock.entity.dungeons.boss.sadan.DiamondGiant;
import net.hypixel.skyblock.entity.dungeons.boss.sadan.GiantsDummy;
import net.hypixel.skyblock.entity.dungeons.boss.sadan.JollyPinkGiant;
import net.hypixel.skyblock.entity.dungeons.boss.sadan.LASRGiant;
import net.hypixel.skyblock.entity.dungeons.boss.sadan.SadanDummy;
import net.hypixel.skyblock.entity.dungeons.boss.sadan.SadanDummy_Idle;
import net.hypixel.skyblock.entity.dungeons.boss.sadan.SadanGiant;
import net.hypixel.skyblock.entity.dungeons.boss.sadan.SadanHuman;
import net.hypixel.skyblock.entity.dungeons.boss.sadan.SleepingGolem_S;
import net.hypixel.skyblock.entity.dungeons.boss.sadan.TerracottaDummy;
import net.hypixel.skyblock.entity.dungeons.boss.sadan.TerracottaSadan;
import net.hypixel.skyblock.entity.dungeons.minibosses.AngryArchaeologist;
import net.hypixel.skyblock.entity.dungeons.minibosses.FrozenAdv;
import net.hypixel.skyblock.entity.dungeons.minibosses.HolyLostAdv;
import net.hypixel.skyblock.entity.dungeons.minibosses.ShadowAssassins;
import net.hypixel.skyblock.entity.dungeons.minibosses.SuperiorLostAdv;
import net.hypixel.skyblock.entity.dungeons.minibosses.Unstable;
import net.hypixel.skyblock.entity.dungeons.minibosses.YoungLostAdv;
import net.hypixel.skyblock.entity.dungeons.regularentity.CryptDreadlord;
import net.hypixel.skyblock.entity.dungeons.regularentity.CryptLurker;
import net.hypixel.skyblock.entity.dungeons.regularentity.CryptSoulstealer;
import net.hypixel.skyblock.entity.dungeons.regularentity.CryptUndead;
import net.hypixel.skyblock.entity.dungeons.regularentity.Fels;
import net.hypixel.skyblock.entity.dungeons.regularentity.ScaredSkeleton;
import net.hypixel.skyblock.entity.dungeons.regularentity.SkeletonMaster;
import net.hypixel.skyblock.entity.dungeons.regularentity.SkeletonSoldier;
import net.hypixel.skyblock.entity.dungeons.regularentity.Skeletor;
import net.hypixel.skyblock.entity.dungeons.regularentity.SkeletorPrime;
import net.hypixel.skyblock.entity.dungeons.regularentity.Sniper;
import net.hypixel.skyblock.entity.dungeons.regularentity.SuperArcher;
import net.hypixel.skyblock.entity.dungeons.regularentity.SuperTankZombie;
import net.hypixel.skyblock.entity.dungeons.regularentity.TankZombie;
import net.hypixel.skyblock.entity.dungeons.regularentity.UndeadSkeleton;
import net.hypixel.skyblock.entity.dungeons.regularentity.Withermancer;
import net.hypixel.skyblock.entity.dungeons.regularentity.ZombieKnight;
import net.hypixel.skyblock.entity.dungeons.regularentity.ZombieSoldier;
import net.hypixel.skyblock.entity.dungeons.watcher.WatcherBonzo;
import net.hypixel.skyblock.entity.dungeons.watcher.WatcherCannibal;
import net.hypixel.skyblock.entity.dungeons.watcher.WatcherFlamer;
import net.hypixel.skyblock.entity.dungeons.watcher.WatcherFreak;
import net.hypixel.skyblock.entity.dungeons.watcher.WatcherFrost;
import net.hypixel.skyblock.entity.dungeons.watcher.WatcherLeech;
import net.hypixel.skyblock.entity.dungeons.watcher.WatcherLivid;
import net.hypixel.skyblock.entity.dungeons.watcher.WatcherMrDead;
import net.hypixel.skyblock.entity.dungeons.watcher.WatcherMute;
import net.hypixel.skyblock.entity.dungeons.watcher.WatcherOoze;
import net.hypixel.skyblock.entity.dungeons.watcher.WatcherParasite;
import net.hypixel.skyblock.entity.dungeons.watcher.WatcherParasiteFish;
import net.hypixel.skyblock.entity.dungeons.watcher.WatcherPsycho;
import net.hypixel.skyblock.entity.dungeons.watcher.WatcherPutrid;
import net.hypixel.skyblock.entity.dungeons.watcher.WatcherReaper;
import net.hypixel.skyblock.entity.dungeons.watcher.WatcherRevoker;
import net.hypixel.skyblock.entity.dungeons.watcher.WatcherSkull;
import net.hypixel.skyblock.entity.dungeons.watcher.WatcherTear;
import net.hypixel.skyblock.entity.dungeons.watcher.WatcherVader;
import net.hypixel.skyblock.entity.dungeons.watcher.WatcherWalker;
import net.hypixel.skyblock.entity.end.Enderman;
import net.hypixel.skyblock.entity.end.ObsidianDefender;
import net.hypixel.skyblock.entity.end.StrongEnderman;
import net.hypixel.skyblock.entity.end.VoidcrazedManiac;
import net.hypixel.skyblock.entity.end.VoidlingDevotee;
import net.hypixel.skyblock.entity.end.VoidlingExtremist;
import net.hypixel.skyblock.entity.end.VoidlingFanatic;
import net.hypixel.skyblock.entity.end.VoidlingRadical;
import net.hypixel.skyblock.entity.end.VoidlingsWardenMob;
import net.hypixel.skyblock.entity.end.Watcher;
import net.hypixel.skyblock.entity.end.WeakEnderman;
import net.hypixel.skyblock.entity.end.Zealot;
import net.hypixel.skyblock.entity.insentient.WheatCrystal;
import net.hypixel.skyblock.entity.nether.LargeMagmaCube;
import net.hypixel.skyblock.entity.nether.MediumMagmaCube;
import net.hypixel.skyblock.entity.nether.SmallMagmaCube;
import net.hypixel.skyblock.entity.nether.WitherSkeleton;
import net.hypixel.skyblock.entity.nms.AtonedHorror;
import net.hypixel.skyblock.entity.nms.BorisYeltsin;
import net.hypixel.skyblock.entity.nms.CrimsonSathanas;
import net.hypixel.skyblock.entity.nms.Giant;
import net.hypixel.skyblock.entity.nms.RevenantHorror;
import net.hypixel.skyblock.entity.nms.SneakyCreeper;
import net.hypixel.skyblock.entity.nms.SvenPackmaster;
import net.hypixel.skyblock.entity.nms.TarantulaBroodfather;
import net.hypixel.skyblock.entity.nms.UncollidableArmorStand;
import net.hypixel.skyblock.entity.nms.VelocityArmorStand;
import net.hypixel.skyblock.entity.nms.VoidgloomSeraph;
import net.hypixel.skyblock.entity.skeleton.HighLevelSkeleton;
import net.hypixel.skyblock.entity.wolf.HowlingSpirit;
import net.hypixel.skyblock.entity.wolf.OldWolf;
import net.hypixel.skyblock.entity.wolf.PackEnforcer;
import net.hypixel.skyblock.entity.wolf.PackSpirit;
import net.hypixel.skyblock.entity.wolf.SoulOfTheAlpha;
import net.hypixel.skyblock.entity.wolf.SvenAlpha;
import net.hypixel.skyblock.entity.wolf.SvenFollower;
import net.hypixel.skyblock.entity.wolf.SvenPup;
import net.hypixel.skyblock.entity.wolf.Wolf;
import net.hypixel.skyblock.entity.zombie.AtonedChampion;
import net.hypixel.skyblock.entity.zombie.AtonedRevenant;
import net.hypixel.skyblock.entity.zombie.CryptGhoul;
import net.hypixel.skyblock.entity.zombie.DeformedRevenant;
import net.hypixel.skyblock.entity.zombie.DiamondGoblinzine;
import net.hypixel.skyblock.entity.zombie.Goblinzine;
import net.hypixel.skyblock.entity.zombie.GoldenGhoul;
import net.hypixel.skyblock.entity.zombie.RevenantChampion;
import net.hypixel.skyblock.entity.zombie.RevenantSycophant;
import net.hypixel.skyblock.entity.zombie.Zombie;
import net.hypixel.skyblock.entity.zombie.ZombieVillager;
import net.minecraft.server.v1_8_R3.EntityInsentient;
import net.minecraft.server.v1_8_R3.EntityTypes;
import org.bukkit.entity.EntityType;

public enum SEntityType {
    WATCHER_CANNIBAL(EntityType.ZOMBIE, WatcherCannibal.class),
    WATCHER_FLAMER(EntityType.ZOMBIE, WatcherFlamer.class),
    WATCHER_FREAK(EntityType.ZOMBIE, WatcherFreak.class),
    WATCHER_FROST(EntityType.ZOMBIE, WatcherFrost.class),
    WATCHER_LEECH(EntityType.ZOMBIE, WatcherLeech.class),
    WATCHER_MR_DEAD(EntityType.ZOMBIE, WatcherMrDead.class),
    WATCHER_MUTE(EntityType.ZOMBIE, WatcherMute.class),
    WATCHER_OOZE(EntityType.ZOMBIE, WatcherOoze.class),
    WATCHER_PARASITE(EntityType.ZOMBIE, WatcherParasite.class),
    WATCHER_PARASITE_SILVERFISH(EntityType.SILVERFISH, WatcherParasiteFish.class),
    WATCHER_PSYCHO(EntityType.ZOMBIE, WatcherPsycho.class),
    WATCHER_PUTRID(EntityType.ZOMBIE, WatcherPutrid.class),
    WATCHER_REAPER(EntityType.ZOMBIE, WatcherReaper.class),
    WATCHER_REVOKER(EntityType.ZOMBIE, WatcherRevoker.class),
    WATCHER_SKULL(EntityType.ZOMBIE, WatcherSkull.class),
    WATCHER_TEAR(EntityType.ZOMBIE, WatcherTear.class),
    WATCHER_VADER(EntityType.ZOMBIE, WatcherVader.class),
    WATCHER_WALKER(EntityType.ZOMBIE, WatcherWalker.class),
    WATCHER_LIVID(EntityType.ZOMBIE, WatcherLivid.class),
    WATCHER_BONZO(EntityType.ZOMBIE, WatcherBonzo.class),
    GOBLIN_DIMOON(EntityType.ZOMBIE, Goblinzine.class),
    DGOBLIN_DIMOON(EntityType.ZOMBIE, DiamondGoblinzine.class),
    LOST_ADV_SUP(EntityType.ZOMBIE, SuperiorLostAdv.class),
    LOST_ADV_YOUNG(EntityType.ZOMBIE, YoungLostAdv.class),
    LOST_ADV_UNSTABLE(EntityType.ZOMBIE, Unstable.class),
    LOST_ADV_HOLY(EntityType.ZOMBIE, HolyLostAdv.class),
    ANGRY_ARCH(EntityType.ZOMBIE, AngryArchaeologist.class),
    FROZEN_ADV(EntityType.ZOMBIE, FrozenAdv.class),
    BOSS_BORIS_YELTSIN(EntityType.ZOMBIE, BorisYeltsin.class),
    SCARY_CAVE_UNDEADBOSS_1(EntityType.ZOMBIE, UndeadGiaKhanhvn.class),
    SHADOW_ASSASSINS(EntityType.ZOMBIE, ShadowAssassins.class),
    TERRORANT(EntityType.ZOMBIE, Giant.class),
    TEST_OBJECT(EntityType.ZOMBIE, Zombie.class),
    VOIDLINGS_WARDEN(EntityType.ZOMBIE, VoidlingsWardenMob.class),
    TEST_CHIMMY_OBJECT_T34(EntityType.ZOMBIE, TestingMob.class),
    TERRACOTTA_SADAN(EntityType.ZOMBIE, TerracottaSadan.class),
    BIGFOOT_SADAN(EntityType.ZOMBIE, BigfootGiant.class),
    LASR_SADAN(EntityType.ZOMBIE, LASRGiant.class),
    JOLLY_PINK_SADAN(EntityType.ZOMBIE, JollyPinkGiant.class),
    DIAMOND_SADAN(EntityType.ZOMBIE, DiamondGiant.class),
    SLEEPING_GOLEM(EntityType.ZOMBIE, SleepingGolem_S.class),
    WOKE_GOLEM(EntityType.ZOMBIE, Stronker.class),
    GIANT_SADAN(EntityType.ZOMBIE, SadanGiant.class),
    SADAN(EntityType.ZOMBIE, SadanHuman.class),
    DUMMY_SADAN_1(EntityType.ZOMBIE, SadanDummy_Idle.class),
    DUMMY_FUNCTION_2(EntityType.ZOMBIE, SadanDummy.class),
    GIANT_DUMMY(EntityType.ZOMBIE, GiantsDummy.class),
    TERRACOTTA_DUMMY(EntityType.ZOMBIE, TerracottaDummy.class),
    FELS(EntityType.ENDERMAN, Fels.class),
    WITHERMANCER(EntityType.SKELETON, Withermancer.class),
    TANK_ZOMBIE(EntityType.ZOMBIE, TankZombie.class),
    CRYPT_DREADLORD(EntityType.ZOMBIE, CryptDreadlord.class),
    CRYPT_UNDEAD(EntityType.ZOMBIE, CryptUndead.class),
    CRYPT_SOULSTEALER(EntityType.ZOMBIE, CryptSoulstealer.class),
    CRYPT_LURKER(EntityType.ZOMBIE, CryptLurker.class),
    SCARED_SKELETON(EntityType.SKELETON, ScaredSkeleton.class),
    SKELETON_MASTER(EntityType.SKELETON, SkeletonMaster.class),
    SNIPER(EntityType.SKELETON, Sniper.class),
    SUPER_ARCHER(EntityType.SKELETON, SuperArcher.class),
    SUPER_TANK_ZOMBIE(EntityType.ZOMBIE, SuperTankZombie.class),
    UNDEAD_SKELETON(EntityType.SKELETON, UndeadSkeleton.class),
    ZOMBIE_KNIGHT(EntityType.ZOMBIE, ZombieKnight.class),
    ZOMBIE_SOLDIER(EntityType.ZOMBIE, ZombieSoldier.class),
    SKELETON_SOLDIER(EntityType.SKELETON, SkeletonSoldier.class),
    SKELETOR(EntityType.ZOMBIE, Skeletor.class),
    SKELETOR_PRIME(EntityType.ZOMBIE, SkeletorPrime.class),
    ZEALOT(EntityType.ENDERMAN, Zealot.class),
    ENDER_CHEST_ZEALOT(EntityType.ENDERMAN, Zealot.EnderChestZealot.class),
    VOIDGLOOM_SERAPH(EntityType.ENDERMAN, VoidgloomSeraph.class, true),
    CRIMSON_SATHANAS(EntityType.SKELETON, CrimsonSathanas.class, true),
    VOIDLING_DEVOTEE(EntityType.ENDERMAN, VoidlingDevotee.class),
    VOIDLING_RADICAL(EntityType.ENDERMAN, VoidlingRadical.class),
    VOIDCRAZED_MANIAC(EntityType.ENDERMAN, VoidcrazedManiac.class),
    VOIDLING_EXTREMIST(EntityType.ENDERMAN, VoidlingExtremist.class),
    VOIDLING_FANATIC(EntityType.ENDERMAN, VoidlingFanatic.class),
    SPECIAL_ZEALOT(EntityType.ENDERMAN, Zealot.SpecialZealot.class),
    PROTECTOR_DRAGON(EntityType.ENDER_DRAGON, ProtectorDragon.class),
    OLD_DRAGON(EntityType.ENDER_DRAGON, OldDragon.class),
    WISE_DRAGON(EntityType.ENDER_DRAGON, WiseDragon.class),
    UNSTABLE_DRAGON(EntityType.ENDER_DRAGON, UnstableDragon.class),
    YOUNG_DRAGON(EntityType.ENDER_DRAGON, YoungDragon.class),
    STRONG_DRAGON(EntityType.ENDER_DRAGON, StrongDragon.class),
    SUPERIOR_DRAGON(EntityType.ENDER_DRAGON, SuperiorDragon.class),
    REVENANT_HORROR(EntityType.ZOMBIE, RevenantHorror.class, true),
    REVENANT_SYCOPHANT(EntityType.ZOMBIE, RevenantSycophant.class),
    REVENANT_CHAMPION(EntityType.ZOMBIE, RevenantChampion.class),
    DEFORMED_REVENANT(EntityType.ZOMBIE, DeformedRevenant.class),
    ATONED_HORROR(EntityType.ZOMBIE, AtonedHorror.class),
    ATONED_REVENANT(EntityType.ZOMBIE, AtonedRevenant.class),
    ATONED_CHAMPION(EntityType.ZOMBIE, AtonedChampion.class),
    SVEN_PACKMASTER(EntityType.WOLF, SvenPackmaster.class, true),
    SVEN_PUP(EntityType.WOLF, SvenPup.class, true),
    SVEN_FOLLOWER(EntityType.WOLF, SvenFollower.class),
    PACK_ENFORCER(EntityType.WOLF, PackEnforcer.class),
    SVEN_ALPHA(EntityType.WOLF, SvenAlpha.class),
    TARANTULA_BROODFATHER(EntityType.SPIDER, TarantulaBroodfather.class),
    TOP_CAVE_SPIDER(EntityType.CAVE_SPIDER, TarantulaBroodfather.TopCaveSpider.class),
    TARANTULA_VERMIN(EntityType.SPIDER, TarantulaVermin.class),
    TARANTULA_BEAST(EntityType.SPIDER, TarantulaBeast.class),
    MUTANT_TARANTULA(EntityType.SPIDER, MutantTarantula.class),
    WATCHER(EntityType.SKELETON, Watcher.class),
    OBSIDIAN_DEFENDER(EntityType.SKELETON, ObsidianDefender.class),
    VELOCITY_ARMOR_STAND(EntityType.ARMOR_STAND, VelocityArmorStand.class),
    UNCOLLIDABLE_ARMOR_STAND(EntityType.ARMOR_STAND, UncollidableArmorStand.class),
    WHEAT_CRYSTAL(EntityType.ARMOR_STAND, WheatCrystal.class),
    LAPIS_ZOMBIE(EntityType.ZOMBIE, LapisZombie.class),
    PIGMAN(EntityType.PIG_ZOMBIE, Pigman.class),
    SMALL_SLIME(EntityType.SLIME, SmallSlime.class),
    MEDIUM_SLIME(EntityType.SLIME, MediumSlime.class),
    LARGE_SLIME(EntityType.SLIME, LargeSlime.class),
    DIAMOND_ZOMBIE(EntityType.ZOMBIE, DiamondZombie.class),
    DIAMOND_SKELETON(EntityType.SKELETON, DiamondSkeleton.class),
    ENCHANTED_DIAMOND_ZOMBIE(EntityType.ZOMBIE, EnchantedDiamondZombie.class),
    ENCHANTED_DIAMOND_SKELETON(EntityType.SKELETON, EnchantedDiamondSkeleton.class),
    WEAK_ENDERMAN(EntityType.ENDERMAN, WeakEnderman.class),
    ENDERMAN(EntityType.ENDERMAN, Enderman.class),
    STRONG_ENDERMAN(EntityType.ENDERMAN, StrongEnderman.class),
    SPLITTER_SPIDER(EntityType.SPIDER, SplitterSpider.class),
    SILVERFISH(EntityType.SILVERFISH, Silverfish.class),
    SPIDER_JOCKEY(EntityType.SPIDER, SpiderJockey.class),
    JOCKEY_SKELETON(EntityType.SKELETON, SpiderJockey.JockeySkeleton.class),
    CAVE_SPIDER(EntityType.CAVE_SPIDER, CaveSpider.class),
    BROOD_MOTHER(EntityType.SPIDER, BroodMother.class),
    DASHER_SPIDER(EntityType.SPIDER, DasherSpider.class),
    WEAVER_SPIDER(EntityType.SPIDER, WeaverSpider.class),
    VORACIOUS_SPIDER(EntityType.SPIDER, VoraciousSpider.class),
    ZOMBIE(EntityType.ZOMBIE, Zombie.class),
    ZOMBIE_VILLAGER(EntityType.ZOMBIE, ZombieVillager.class),
    CRYPT_GHOUL(EntityType.ZOMBIE, CryptGhoul.class),
    GOLDEN_GHOUL(EntityType.ZOMBIE, GoldenGhoul.class),
    WOLF(EntityType.WOLF, Wolf.class),
    OLD_WOLF(EntityType.WOLF, OldWolf.class),
    PACK_SPIRIT(EntityType.WOLF, PackSpirit.class),
    HOWLING_SPIRIT(EntityType.WOLF, HowlingSpirit.class),
    SOUL_OF_THE_ALPHA(EntityType.WOLF, SoulOfTheAlpha.class),
    SPIDERS_DEN_SKELETON(EntityType.SKELETON, SpidersDenSkeleton.class),
    HIGH_LEVEL_SKELETON(EntityType.SKELETON, HighLevelSkeleton.class),
    SPIDERS_DEN_SLIME(EntityType.SLIME, SpidersDenSlime.class),
    SNEAKY_CREEPER(EntityType.CREEPER, SneakyCreeper.class),
    WITHER_SKELETON(EntityType.SKELETON, WitherSkeleton.class),
    SMALL_MAGMA_CUBE(EntityType.MAGMA_CUBE, SmallMagmaCube.class),
    MEDIUM_MAGMA_CUBE(EntityType.MAGMA_CUBE, MediumMagmaCube.class),
    LARGE_MAGMA_CUBE(EntityType.MAGMA_CUBE, LargeMagmaCube.class);

    private final EntityType craftType;
    private final Class<?> clazz;
    private final boolean specific;

    private SEntityType(EntityType craftType, Class<?> clazz, boolean specific) {
        this.craftType = craftType;
        this.clazz = clazz;
        this.specific = specific;
        if (EntityInsentient.class.isAssignableFrom(clazz)) {
            SEntityType.registerEntity(this.name(), craftType.getTypeId(), clazz);
        }
    }

    private SEntityType(EntityType craftType, Class<?> clazz) {
        this(craftType, clazz, false);
    }

    public EntityStatistics getStatistics() {
        Object generic = this.getGenericInstance();
        if (generic instanceof EntityStatistics) {
            return (EntityStatistics)generic;
        }
        return null;
    }

    public EntityFunction getFunction() {
        Object generic = this.getGenericInstance();
        if (generic instanceof EntityFunction) {
            return (EntityFunction)generic;
        }
        return null;
    }

    public Object instance(Object ... params) {
        try {
            Class[] paramTypes = new Class[params.length];
            for (int i = 0; i < paramTypes.length; ++i) {
                paramTypes[i] = params[i].getClass();
            }
            return this.clazz.getConstructor(paramTypes).newInstance(params);
        } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public Object getGenericInstance() {
        try {
            return this.clazz.newInstance();
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void registerEntity(String name, int id, Class<? extends EntityInsentient> clazz) {
        try {
            ArrayList<Map> dataMap = new ArrayList<Map>();
            for (Field f : EntityTypes.class.getDeclaredFields()) {
                if (!f.getType().getSimpleName().equals(Map.class.getSimpleName())) continue;
                f.setAccessible(true);
                dataMap.add((Map)f.get(null));
            }
            if (((Map)dataMap.get(2)).containsKey(id)) {
                ((Map)dataMap.get(0)).remove(name);
                ((Map)dataMap.get(2)).remove(id);
            }
            Method method = EntityTypes.class.getDeclaredMethod("a", Class.class, String.class, Integer.TYPE);
            method.setAccessible(true);
            method.invoke(null, clazz, name, id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static SEntityType getEntityType(String name) {
        return SEntityType.valueOf(name.toUpperCase());
    }

    public EntityType getCraftType() {
        return this.craftType;
    }

    public Class<?> getClazz() {
        return this.clazz;
    }

    public boolean isSpecific() {
        return this.specific;
    }
}

