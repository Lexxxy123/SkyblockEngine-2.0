package vn.giakhanhvn.skysim.entity;

import vn.giakhanhvn.skysim.entity.nether.LargeMagmaCube;
import vn.giakhanhvn.skysim.entity.nether.MediumMagmaCube;
import vn.giakhanhvn.skysim.entity.nether.SmallMagmaCube;
import vn.giakhanhvn.skysim.entity.nether.WitherSkeleton;
import vn.giakhanhvn.skysim.entity.nms.SneakyCreeper;
import vn.giakhanhvn.skysim.entity.den.SpidersDenSlime;
import vn.giakhanhvn.skysim.entity.skeleton.HighLevelSkeleton;
import vn.giakhanhvn.skysim.entity.den.SpidersDenSkeleton;
import vn.giakhanhvn.skysim.entity.wolf.SoulOfTheAlpha;
import vn.giakhanhvn.skysim.entity.wolf.HowlingSpirit;
import vn.giakhanhvn.skysim.entity.wolf.PackSpirit;
import vn.giakhanhvn.skysim.entity.wolf.OldWolf;
import vn.giakhanhvn.skysim.entity.wolf.Wolf;
import vn.giakhanhvn.skysim.entity.zombie.GoldenGhoul;
import vn.giakhanhvn.skysim.entity.zombie.CryptGhoul;
import vn.giakhanhvn.skysim.entity.zombie.ZombieVillager;
import vn.giakhanhvn.skysim.entity.den.VoraciousSpider;
import vn.giakhanhvn.skysim.entity.den.WeaverSpider;
import vn.giakhanhvn.skysim.entity.den.DasherSpider;
import vn.giakhanhvn.skysim.entity.den.BroodMother;
import vn.giakhanhvn.skysim.entity.den.CaveSpider;
import vn.giakhanhvn.skysim.entity.den.SpiderJockey;
import vn.giakhanhvn.skysim.entity.den.Silverfish;
import vn.giakhanhvn.skysim.entity.den.SplitterSpider;
import vn.giakhanhvn.skysim.entity.end.StrongEnderman;
import vn.giakhanhvn.skysim.entity.end.Enderman;
import vn.giakhanhvn.skysim.entity.end.WeakEnderman;
import vn.giakhanhvn.skysim.entity.caverns.EnchantedDiamondSkeleton;
import vn.giakhanhvn.skysim.entity.caverns.EnchantedDiamondZombie;
import vn.giakhanhvn.skysim.entity.caverns.DiamondSkeleton;
import vn.giakhanhvn.skysim.entity.caverns.DiamondZombie;
import vn.giakhanhvn.skysim.entity.caverns.LargeSlime;
import vn.giakhanhvn.skysim.entity.caverns.MediumSlime;
import vn.giakhanhvn.skysim.entity.caverns.SmallSlime;
import vn.giakhanhvn.skysim.entity.caverns.Pigman;
import vn.giakhanhvn.skysim.entity.caverns.LapisZombie;
import vn.giakhanhvn.skysim.entity.insentient.WheatCrystal;
import vn.giakhanhvn.skysim.entity.nms.UncollidableArmorStand;
import vn.giakhanhvn.skysim.entity.nms.VelocityArmorStand;
import vn.giakhanhvn.skysim.entity.end.ObsidianDefender;
import vn.giakhanhvn.skysim.entity.end.Watcher;
import vn.giakhanhvn.skysim.entity.den.MutantTarantula;
import vn.giakhanhvn.skysim.entity.den.TarantulaBeast;
import vn.giakhanhvn.skysim.entity.den.TarantulaVermin;
import vn.giakhanhvn.skysim.entity.nms.TarantulaBroodfather;
import vn.giakhanhvn.skysim.entity.wolf.SvenAlpha;
import vn.giakhanhvn.skysim.entity.wolf.PackEnforcer;
import vn.giakhanhvn.skysim.entity.wolf.SvenFollower;
import vn.giakhanhvn.skysim.entity.wolf.SvenPup;
import vn.giakhanhvn.skysim.entity.nms.SvenPackmaster;
import vn.giakhanhvn.skysim.entity.zombie.AtonedChampion;
import vn.giakhanhvn.skysim.entity.zombie.AtonedRevenant;
import vn.giakhanhvn.skysim.entity.nms.AtonedHorror;
import vn.giakhanhvn.skysim.entity.zombie.DeformedRevenant;
import vn.giakhanhvn.skysim.entity.zombie.RevenantChampion;
import vn.giakhanhvn.skysim.entity.zombie.RevenantSycophant;
import vn.giakhanhvn.skysim.entity.nms.RevenantHorror;
import vn.giakhanhvn.skysim.entity.nms.SuperiorDragon;
import vn.giakhanhvn.skysim.entity.nms.StrongDragon;
import vn.giakhanhvn.skysim.entity.nms.YoungDragon;
import vn.giakhanhvn.skysim.entity.nms.UnstableDragon;
import vn.giakhanhvn.skysim.entity.nms.WiseDragon;
import vn.giakhanhvn.skysim.entity.nms.OldDragon;
import vn.giakhanhvn.skysim.entity.nms.ProtectorDragon;
import vn.giakhanhvn.skysim.entity.end.VoidlingFanatic;
import vn.giakhanhvn.skysim.entity.end.VoidlingExtremist;
import vn.giakhanhvn.skysim.entity.end.VoidcrazedManiac;
import vn.giakhanhvn.skysim.entity.end.VoidlingRadical;
import vn.giakhanhvn.skysim.entity.end.VoidlingDevotee;
import vn.giakhanhvn.skysim.entity.nms.CrimsonSathanas;
import vn.giakhanhvn.skysim.entity.nms.VoidgloomSeraph;
import vn.giakhanhvn.skysim.entity.end.Zealot;
import vn.giakhanhvn.skysim.entity.dungeons.regularentity.SkeletorPrime;
import vn.giakhanhvn.skysim.entity.dungeons.regularentity.Skeletor;
import vn.giakhanhvn.skysim.entity.dungeons.regularentity.SkeletonSoldier;
import vn.giakhanhvn.skysim.entity.dungeons.regularentity.ZombieSoldier;
import vn.giakhanhvn.skysim.entity.dungeons.regularentity.ZombieKnight;
import vn.giakhanhvn.skysim.entity.dungeons.regularentity.UndeadSkeleton;
import vn.giakhanhvn.skysim.entity.dungeons.regularentity.SuperTankZombie;
import vn.giakhanhvn.skysim.entity.dungeons.regularentity.SuperArcher;
import vn.giakhanhvn.skysim.entity.dungeons.regularentity.Sniper;
import vn.giakhanhvn.skysim.entity.dungeons.regularentity.SkeletonMaster;
import vn.giakhanhvn.skysim.entity.dungeons.regularentity.ScaredSkeleton;
import vn.giakhanhvn.skysim.entity.dungeons.regularentity.CryptLurker;
import vn.giakhanhvn.skysim.entity.dungeons.regularentity.CryptSoulstealer;
import vn.giakhanhvn.skysim.entity.dungeons.regularentity.CryptUndead;
import vn.giakhanhvn.skysim.entity.dungeons.regularentity.CryptDreadlord;
import vn.giakhanhvn.skysim.entity.dungeons.regularentity.TankZombie;
import vn.giakhanhvn.skysim.entity.dungeons.regularentity.Withermancer;
import vn.giakhanhvn.skysim.entity.dungeons.regularentity.Fels;
import vn.giakhanhvn.skysim.entity.dungeons.boss.sadan.TerracottaDummy;
import vn.giakhanhvn.skysim.entity.dungeons.boss.sadan.GiantsDummy;
import vn.giakhanhvn.skysim.entity.dungeons.boss.sadan.SadanDummy;
import vn.giakhanhvn.skysim.entity.dungeons.boss.sadan.SadanDummy_Idle;
import vn.giakhanhvn.skysim.entity.dungeons.boss.sadan.SadanHuman;
import vn.giakhanhvn.skysim.entity.dungeons.boss.sadan.SadanGiant;
import vn.giakhanhvn.skysim.entity.dungeons.boss.sadan.SleepingGolem_S;
import vn.giakhanhvn.skysim.entity.dungeons.boss.sadan.DiamondGiant;
import vn.giakhanhvn.skysim.entity.dungeons.boss.sadan.JollyPinkGiant;
import vn.giakhanhvn.skysim.entity.dungeons.boss.sadan.LASRGiant;
import vn.giakhanhvn.skysim.entity.dungeons.boss.sadan.BigfootGiant;
import vn.giakhanhvn.skysim.entity.dungeons.boss.sadan.TerracottaSadan;
import vn.giakhanhvn.skysim.entity.dungeons.TestingMob;
import vn.giakhanhvn.skysim.entity.end.VoidlingsWardenMob;
import vn.giakhanhvn.skysim.entity.zombie.Zombie;
import vn.giakhanhvn.skysim.entity.nms.Giant;
import vn.giakhanhvn.skysim.entity.dungeons.minibosses.ShadowAssassins;
import vn.giakhanhvn.skysim.entity.caverns.UndeadGiaKhanhvn;
import vn.giakhanhvn.skysim.entity.nms.BorisYeltsin;
import vn.giakhanhvn.skysim.entity.dungeons.minibosses.FrozenAdv;
import vn.giakhanhvn.skysim.entity.dungeons.minibosses.AngryArchaeologist;
import vn.giakhanhvn.skysim.entity.dungeons.minibosses.HolyLostAdv;
import vn.giakhanhvn.skysim.entity.dungeons.minibosses.Unstable;
import vn.giakhanhvn.skysim.entity.dungeons.minibosses.YoungLostAdv;
import vn.giakhanhvn.skysim.entity.dungeons.minibosses.SuperiorLostAdv;
import vn.giakhanhvn.skysim.dimoon.Dimoonizae;
import vn.giakhanhvn.skysim.entity.zombie.DiamondGoblinzine;
import vn.giakhanhvn.skysim.entity.zombie.Goblinzine;
import vn.giakhanhvn.skysim.entity.dungeons.watcher.WatcherBonzo;
import vn.giakhanhvn.skysim.entity.dungeons.watcher.WatcherLivid;
import vn.giakhanhvn.skysim.entity.dungeons.watcher.WatcherWalker;
import vn.giakhanhvn.skysim.entity.dungeons.watcher.WatcherVader;
import vn.giakhanhvn.skysim.entity.dungeons.watcher.WatcherTear;
import vn.giakhanhvn.skysim.entity.dungeons.watcher.WatcherSkull;
import vn.giakhanhvn.skysim.entity.dungeons.watcher.WatcherRevoker;
import vn.giakhanhvn.skysim.entity.dungeons.watcher.WatcherReaper;
import vn.giakhanhvn.skysim.entity.dungeons.watcher.WatcherPutrid;
import vn.giakhanhvn.skysim.entity.dungeons.watcher.WatcherPsycho;
import vn.giakhanhvn.skysim.entity.dungeons.watcher.WatcherParasiteFish;
import vn.giakhanhvn.skysim.entity.dungeons.watcher.WatcherParasite;
import vn.giakhanhvn.skysim.entity.dungeons.watcher.WatcherOoze;
import vn.giakhanhvn.skysim.entity.dungeons.watcher.WatcherMute;
import vn.giakhanhvn.skysim.entity.dungeons.watcher.WatcherMrDead;
import vn.giakhanhvn.skysim.entity.dungeons.watcher.WatcherLeech;
import vn.giakhanhvn.skysim.entity.dungeons.watcher.WatcherFrost;
import vn.giakhanhvn.skysim.entity.dungeons.watcher.WatcherFreak;
import vn.giakhanhvn.skysim.entity.dungeons.watcher.WatcherFlamer;
import vn.giakhanhvn.skysim.entity.dungeons.watcher.WatcherCannibal;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.util.List;
import net.minecraft.server.v1_8_R3.EntityTypes;
import java.util.Map;
import java.util.ArrayList;
import java.lang.reflect.InvocationTargetException;
import net.minecraft.server.v1_8_R3.EntityInsentient;
import org.bukkit.entity.EntityType;

public enum SEntityType
{
    WATCHER_CANNIBAL(EntityType.ZOMBIE, (Class<?>)WatcherCannibal.class), 
    WATCHER_FLAMER(EntityType.ZOMBIE, (Class<?>)WatcherFlamer.class), 
    WATCHER_FREAK(EntityType.ZOMBIE, (Class<?>)WatcherFreak.class), 
    WATCHER_FROST(EntityType.ZOMBIE, (Class<?>)WatcherFrost.class), 
    WATCHER_LEECH(EntityType.ZOMBIE, (Class<?>)WatcherLeech.class), 
    WATCHER_MR_DEAD(EntityType.ZOMBIE, (Class<?>)WatcherMrDead.class), 
    WATCHER_MUTE(EntityType.ZOMBIE, (Class<?>)WatcherMute.class), 
    WATCHER_OOZE(EntityType.ZOMBIE, (Class<?>)WatcherOoze.class), 
    WATCHER_PARASITE(EntityType.ZOMBIE, (Class<?>)WatcherParasite.class), 
    WATCHER_PARASITE_SILVERFISH(EntityType.SILVERFISH, (Class<?>)WatcherParasiteFish.class), 
    WATCHER_PSYCHO(EntityType.ZOMBIE, (Class<?>)WatcherPsycho.class), 
    WATCHER_PUTRID(EntityType.ZOMBIE, (Class<?>)WatcherPutrid.class), 
    WATCHER_REAPER(EntityType.ZOMBIE, (Class<?>)WatcherReaper.class), 
    WATCHER_REVOKER(EntityType.ZOMBIE, (Class<?>)WatcherRevoker.class), 
    WATCHER_SKULL(EntityType.ZOMBIE, (Class<?>)WatcherSkull.class), 
    WATCHER_TEAR(EntityType.ZOMBIE, (Class<?>)WatcherTear.class), 
    WATCHER_VADER(EntityType.ZOMBIE, (Class<?>)WatcherVader.class), 
    WATCHER_WALKER(EntityType.ZOMBIE, (Class<?>)WatcherWalker.class), 
    WATCHER_LIVID(EntityType.ZOMBIE, (Class<?>)WatcherLivid.class), 
    WATCHER_BONZO(EntityType.ZOMBIE, (Class<?>)WatcherBonzo.class), 
    GOBLIN_DIMOON(EntityType.ZOMBIE, (Class<?>)Goblinzine.class), 
    DGOBLIN_DIMOON(EntityType.ZOMBIE, (Class<?>)DiamondGoblinzine.class), 
    DIMOON_MINIBOSS(EntityType.ZOMBIE, (Class<?>)Dimoonizae.class), 
    LOST_ADV_SUP(EntityType.ZOMBIE, (Class<?>)SuperiorLostAdv.class), 
    LOST_ADV_YOUNG(EntityType.ZOMBIE, (Class<?>)YoungLostAdv.class), 
    LOST_ADV_UNSTABLE(EntityType.ZOMBIE, (Class<?>)Unstable.class), 
    LOST_ADV_HOLY(EntityType.ZOMBIE, (Class<?>)HolyLostAdv.class), 
    ANGRY_ARCH(EntityType.ZOMBIE, (Class<?>)AngryArchaeologist.class), 
    FROZEN_ADV(EntityType.ZOMBIE, (Class<?>)FrozenAdv.class), 
    BOSS_BORIS_YELTSIN(EntityType.ZOMBIE, (Class<?>)BorisYeltsin.class), 
    SCARY_CAVE_UNDEADBOSS_1(EntityType.ZOMBIE, (Class<?>)UndeadGiaKhanhvn.class), 
    SHADOW_ASSASSINS(EntityType.ZOMBIE, (Class<?>)ShadowAssassins.class), 
    TERRORANT(EntityType.ZOMBIE, (Class<?>)Giant.class), 
    TEST_OBJECT(EntityType.ZOMBIE, (Class<?>)Zombie.class), 
    VOIDLINGS_WARDEN(EntityType.ZOMBIE, (Class<?>)VoidlingsWardenMob.class), 
    TEST_CHIMMY_OBJECT_T34(EntityType.ZOMBIE, (Class<?>)TestingMob.class), 
    TERRACOTTA_SADAN(EntityType.ZOMBIE, (Class<?>)TerracottaSadan.class), 
    BIGFOOT_SADAN(EntityType.ZOMBIE, (Class<?>)BigfootGiant.class), 
    LASR_SADAN(EntityType.ZOMBIE, (Class<?>)LASRGiant.class), 
    JOLLY_PINK_SADAN(EntityType.ZOMBIE, (Class<?>)JollyPinkGiant.class), 
    DIAMOND_SADAN(EntityType.ZOMBIE, (Class<?>)DiamondGiant.class), 
    SLEEPING_GOLEM(EntityType.ZOMBIE, (Class<?>)SleepingGolem_S.class), 
    WOKE_GOLEM(EntityType.ZOMBIE, (Class<?>)Stronker.class), 
    GIANT_SADAN(EntityType.ZOMBIE, (Class<?>)SadanGiant.class), 
    SADAN(EntityType.ZOMBIE, (Class<?>)SadanHuman.class), 
    DUMMY_SADAN_1(EntityType.ZOMBIE, (Class<?>)SadanDummy_Idle.class), 
    DUMMY_FUNCTION_2(EntityType.ZOMBIE, (Class<?>)SadanDummy.class), 
    GIANT_DUMMY(EntityType.ZOMBIE, (Class<?>)GiantsDummy.class), 
    TERRACOTTA_DUMMY(EntityType.ZOMBIE, (Class<?>)TerracottaDummy.class), 
    FELS(EntityType.ENDERMAN, (Class<?>)Fels.class), 
    WITHERMANCER(EntityType.SKELETON, (Class<?>)Withermancer.class), 
    TANK_ZOMBIE(EntityType.ZOMBIE, (Class<?>)TankZombie.class), 
    CRYPT_DREADLORD(EntityType.ZOMBIE, (Class<?>)CryptDreadlord.class), 
    CRYPT_UNDEAD(EntityType.ZOMBIE, (Class<?>)CryptUndead.class), 
    CRYPT_SOULSTEALER(EntityType.ZOMBIE, (Class<?>)CryptSoulstealer.class), 
    CRYPT_LURKER(EntityType.ZOMBIE, (Class<?>)CryptLurker.class), 
    SCARED_SKELETON(EntityType.SKELETON, (Class<?>)ScaredSkeleton.class), 
    SKELETON_MASTER(EntityType.SKELETON, (Class<?>)SkeletonMaster.class), 
    SNIPER(EntityType.SKELETON, (Class<?>)Sniper.class), 
    SUPER_ARCHER(EntityType.SKELETON, (Class<?>)SuperArcher.class), 
    SUPER_TANK_ZOMBIE(EntityType.ZOMBIE, (Class<?>)SuperTankZombie.class), 
    UNDEAD_SKELETON(EntityType.SKELETON, (Class<?>)UndeadSkeleton.class), 
    ZOMBIE_KNIGHT(EntityType.ZOMBIE, (Class<?>)ZombieKnight.class), 
    ZOMBIE_SOLDIER(EntityType.ZOMBIE, (Class<?>)ZombieSoldier.class), 
    SKELETON_SOLDIER(EntityType.SKELETON, (Class<?>)SkeletonSoldier.class), 
    SKELETOR(EntityType.ZOMBIE, (Class<?>)Skeletor.class), 
    SKELETOR_PRIME(EntityType.ZOMBIE, (Class<?>)SkeletorPrime.class), 
    ZEALOT(EntityType.ENDERMAN, (Class<?>)Zealot.class), 
    ENDER_CHEST_ZEALOT(EntityType.ENDERMAN, (Class<?>)Zealot.EnderChestZealot.class), 
    VOIDGLOOM_SERAPH(EntityType.ENDERMAN, (Class<?>)VoidgloomSeraph.class, true), 
    CRIMSON_SATHANAS(EntityType.SKELETON, (Class<?>)CrimsonSathanas.class, true), 
    VOIDLING_DEVOTEE(EntityType.ENDERMAN, (Class<?>)VoidlingDevotee.class), 
    VOIDLING_RADICAL(EntityType.ENDERMAN, (Class<?>)VoidlingRadical.class), 
    VOIDCRAZED_MANIAC(EntityType.ENDERMAN, (Class<?>)VoidcrazedManiac.class), 
    VOIDLING_EXTREMIST(EntityType.ENDERMAN, (Class<?>)VoidlingExtremist.class), 
    VOIDLING_FANATIC(EntityType.ENDERMAN, (Class<?>)VoidlingFanatic.class), 
    SPECIAL_ZEALOT(EntityType.ENDERMAN, (Class<?>)Zealot.SpecialZealot.class), 
    PROTECTOR_DRAGON(EntityType.ENDER_DRAGON, (Class<?>)ProtectorDragon.class), 
    OLD_DRAGON(EntityType.ENDER_DRAGON, (Class<?>)OldDragon.class), 
    WISE_DRAGON(EntityType.ENDER_DRAGON, (Class<?>)WiseDragon.class), 
    UNSTABLE_DRAGON(EntityType.ENDER_DRAGON, (Class<?>)UnstableDragon.class), 
    YOUNG_DRAGON(EntityType.ENDER_DRAGON, (Class<?>)YoungDragon.class), 
    STRONG_DRAGON(EntityType.ENDER_DRAGON, (Class<?>)StrongDragon.class), 
    SUPERIOR_DRAGON(EntityType.ENDER_DRAGON, (Class<?>)SuperiorDragon.class), 
    REVENANT_HORROR(EntityType.ZOMBIE, (Class<?>)RevenantHorror.class, true), 
    REVENANT_SYCOPHANT(EntityType.ZOMBIE, (Class<?>)RevenantSycophant.class), 
    REVENANT_CHAMPION(EntityType.ZOMBIE, (Class<?>)RevenantChampion.class), 
    DEFORMED_REVENANT(EntityType.ZOMBIE, (Class<?>)DeformedRevenant.class), 
    ATONED_HORROR(EntityType.ZOMBIE, (Class<?>)AtonedHorror.class), 
    ATONED_REVENANT(EntityType.ZOMBIE, (Class<?>)AtonedRevenant.class), 
    ATONED_CHAMPION(EntityType.ZOMBIE, (Class<?>)AtonedChampion.class), 
    SVEN_PACKMASTER(EntityType.WOLF, (Class<?>)SvenPackmaster.class, true), 
    SVEN_PUP(EntityType.WOLF, (Class<?>)SvenPup.class, true), 
    SVEN_FOLLOWER(EntityType.WOLF, (Class<?>)SvenFollower.class), 
    PACK_ENFORCER(EntityType.WOLF, (Class<?>)PackEnforcer.class), 
    SVEN_ALPHA(EntityType.WOLF, (Class<?>)SvenAlpha.class), 
    TARANTULA_BROODFATHER(EntityType.SPIDER, (Class<?>)TarantulaBroodfather.class), 
    TOP_CAVE_SPIDER(EntityType.CAVE_SPIDER, (Class<?>)TarantulaBroodfather.TopCaveSpider.class), 
    TARANTULA_VERMIN(EntityType.SPIDER, (Class<?>)TarantulaVermin.class), 
    TARANTULA_BEAST(EntityType.SPIDER, (Class<?>)TarantulaBeast.class), 
    MUTANT_TARANTULA(EntityType.SPIDER, (Class<?>)MutantTarantula.class), 
    WATCHER(EntityType.SKELETON, (Class<?>)Watcher.class), 
    OBSIDIAN_DEFENDER(EntityType.SKELETON, (Class<?>)ObsidianDefender.class), 
    VELOCITY_ARMOR_STAND(EntityType.ARMOR_STAND, (Class<?>)VelocityArmorStand.class), 
    UNCOLLIDABLE_ARMOR_STAND(EntityType.ARMOR_STAND, (Class<?>)UncollidableArmorStand.class), 
    WHEAT_CRYSTAL(EntityType.ARMOR_STAND, (Class<?>)WheatCrystal.class), 
    LAPIS_ZOMBIE(EntityType.ZOMBIE, (Class<?>)LapisZombie.class), 
    PIGMAN(EntityType.PIG_ZOMBIE, (Class<?>)Pigman.class), 
    SMALL_SLIME(EntityType.SLIME, (Class<?>)SmallSlime.class), 
    MEDIUM_SLIME(EntityType.SLIME, (Class<?>)MediumSlime.class), 
    LARGE_SLIME(EntityType.SLIME, (Class<?>)LargeSlime.class), 
    DIAMOND_ZOMBIE(EntityType.ZOMBIE, (Class<?>)DiamondZombie.class), 
    DIAMOND_SKELETON(EntityType.SKELETON, (Class<?>)DiamondSkeleton.class), 
    ENCHANTED_DIAMOND_ZOMBIE(EntityType.ZOMBIE, (Class<?>)EnchantedDiamondZombie.class), 
    ENCHANTED_DIAMOND_SKELETON(EntityType.SKELETON, (Class<?>)EnchantedDiamondSkeleton.class), 
    WEAK_ENDERMAN(EntityType.ENDERMAN, (Class<?>)WeakEnderman.class), 
    ENDERMAN(EntityType.ENDERMAN, (Class<?>)Enderman.class), 
    STRONG_ENDERMAN(EntityType.ENDERMAN, (Class<?>)StrongEnderman.class), 
    SPLITTER_SPIDER(EntityType.SPIDER, (Class<?>)SplitterSpider.class), 
    SILVERFISH(EntityType.SILVERFISH, (Class<?>)Silverfish.class), 
    SPIDER_JOCKEY(EntityType.SPIDER, (Class<?>)SpiderJockey.class), 
    JOCKEY_SKELETON(EntityType.SKELETON, (Class<?>)SpiderJockey.JockeySkeleton.class), 
    CAVE_SPIDER(EntityType.CAVE_SPIDER, (Class<?>)CaveSpider.class), 
    BROOD_MOTHER(EntityType.SPIDER, (Class<?>)BroodMother.class), 
    DASHER_SPIDER(EntityType.SPIDER, (Class<?>)DasherSpider.class), 
    WEAVER_SPIDER(EntityType.SPIDER, (Class<?>)WeaverSpider.class), 
    VORACIOUS_SPIDER(EntityType.SPIDER, (Class<?>)VoraciousSpider.class), 
    ZOMBIE(EntityType.ZOMBIE, (Class<?>)Zombie.class), 
    ZOMBIE_VILLAGER(EntityType.ZOMBIE, (Class<?>)ZombieVillager.class), 
    CRYPT_GHOUL(EntityType.ZOMBIE, (Class<?>)CryptGhoul.class), 
    GOLDEN_GHOUL(EntityType.ZOMBIE, (Class<?>)GoldenGhoul.class), 
    WOLF(EntityType.WOLF, (Class<?>)Wolf.class), 
    OLD_WOLF(EntityType.WOLF, (Class<?>)OldWolf.class), 
    PACK_SPIRIT(EntityType.WOLF, (Class<?>)PackSpirit.class), 
    HOWLING_SPIRIT(EntityType.WOLF, (Class<?>)HowlingSpirit.class), 
    SOUL_OF_THE_ALPHA(EntityType.WOLF, (Class<?>)SoulOfTheAlpha.class), 
    SPIDERS_DEN_SKELETON(EntityType.SKELETON, (Class<?>)SpidersDenSkeleton.class), 
    HIGH_LEVEL_SKELETON(EntityType.SKELETON, (Class<?>)HighLevelSkeleton.class), 
    SPIDERS_DEN_SLIME(EntityType.SLIME, (Class<?>)SpidersDenSlime.class), 
    SNEAKY_CREEPER(EntityType.CREEPER, (Class<?>)SneakyCreeper.class), 
    WITHER_SKELETON(EntityType.SKELETON, (Class<?>)WitherSkeleton.class), 
    SMALL_MAGMA_CUBE(EntityType.MAGMA_CUBE, (Class<?>)SmallMagmaCube.class), 
    MEDIUM_MAGMA_CUBE(EntityType.MAGMA_CUBE, (Class<?>)MediumMagmaCube.class), 
    LARGE_MAGMA_CUBE(EntityType.MAGMA_CUBE, (Class<?>)LargeMagmaCube.class);
    
    private final EntityType craftType;
    private final Class<?> clazz;
    private final boolean specific;
    
    private SEntityType(final EntityType craftType, final Class<?> clazz, final boolean specific) {
        this.craftType = craftType;
        this.clazz = clazz;
        this.specific = specific;
        if (EntityInsentient.class.isAssignableFrom(clazz)) {
            registerEntity(this.name(), craftType.getTypeId(), (Class<? extends EntityInsentient>)clazz);
        }
    }
    
    private SEntityType(final EntityType craftType, final Class<?> clazz) {
        this(craftType, clazz, false);
    }
    
    public EntityStatistics getStatistics() {
        final Object generic = this.getGenericInstance();
        if (generic instanceof EntityStatistics) {
            return (EntityStatistics)generic;
        }
        return null;
    }
    
    public EntityFunction getFunction() {
        final Object generic = this.getGenericInstance();
        if (generic instanceof EntityFunction) {
            return (EntityFunction)generic;
        }
        return null;
    }
    
    public Object instance(final Object... params) {
        try {
            final Class<?>[] paramTypes = new Class[params.length];
            for (int i = 0; i < paramTypes.length; ++i) {
                paramTypes[i] = params[i].getClass();
            }
            return this.clazz.getConstructor(paramTypes).newInstance(params);
        }
        catch (final InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    public Object getGenericInstance() {
        try {
            return this.clazz.newInstance();
        }
        catch (final InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    private static void registerEntity(final String name, final int id, final Class<? extends EntityInsentient> clazz) {
        try {
            final List<Map<?, ?>> dataMap = new ArrayList<Map<?, ?>>();
            for (final Field f : EntityTypes.class.getDeclaredFields()) {
                if (f.getType().getSimpleName().equals(Map.class.getSimpleName())) {
                    f.setAccessible(true);
                    dataMap.add((Map)f.get(null));
                }
            }
            if (dataMap.get(2).containsKey(id)) {
                dataMap.get(0).remove(name);
                dataMap.get(2).remove(id);
            }
            final Method method = EntityTypes.class.getDeclaredMethod("a", Class.class, String.class, Integer.TYPE);
            method.setAccessible(true);
            method.invoke(null, clazz, name, id);
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
    }
    
    public static SEntityType getEntityType(final String name) {
        return valueOf(name.toUpperCase());
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
