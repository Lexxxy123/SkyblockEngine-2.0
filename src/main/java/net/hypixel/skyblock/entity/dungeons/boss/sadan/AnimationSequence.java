/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.sk89q.worldedit.BlockVector
 *  com.sk89q.worldedit.EditSession
 *  com.sk89q.worldedit.LocalWorld
 *  com.sk89q.worldedit.MaxChangedBlocksException
 *  com.sk89q.worldedit.Vector
 *  com.sk89q.worldedit.WorldEdit
 *  com.sk89q.worldedit.blocks.BaseBlock
 *  com.sk89q.worldedit.bukkit.BukkitUtil
 *  com.sk89q.worldedit.regions.CuboidRegion
 *  com.sk89q.worldedit.regions.Region
 *  com.sk89q.worldedit.world.World
 *  org.bukkit.Location
 *  org.bukkit.Sound
 *  org.bukkit.World
 *  org.bukkit.entity.Entity
 */
package net.hypixel.skyblock.entity.dungeons.boss.sadan;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.LocalWorld;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.world.World;
import net.hypixel.skyblock.entity.SEntity;
import net.hypixel.skyblock.entity.SEntityType;
import net.hypixel.skyblock.util.SUtil;
import net.hypixel.skyblock.util.Sputnik;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;

public class AnimationSequence {
    public static void chainAnimation(org.bukkit.World w2) {
        boolean chaining = false;
        if (w2.getName().contains("f6")) {
            AnimationSequence.beginRenderDown(w2);
            SUtil.delay(() -> AnimationSequence.beginRenderUp(w2), 330L);
        }
    }

    public static void pasteBase(org.bukkit.World w2, int delay) {
        SUtil.delay(() -> Sputnik.pasteSchematic("chain_base", true, 186.0f, 105.0f, 271.0f, w2), delay - 1);
        SUtil.delay(() -> Sputnik.pasteSchematic("chain_base", true, 186.0f, 105.0f, 271.0f, w2), delay);
        SUtil.delay(() -> Sputnik.pasteSchematic("chain_base", true, 186.0f, 105.0f, 271.0f, w2), delay + 1);
    }

    public static void pasteGlass(org.bukkit.World w2, int delay, int phase) {
        if (phase == 0) {
            SUtil.delay(() -> Sputnik.pasteSchematic("f6_h1", true, 189.0f, 71.0f, 266.0f, w2), delay);
        } else if (phase == 1) {
            SUtil.delay(() -> Sputnik.pasteSchematic("f6_h2", true, 190.0f, 71.0f, 269.0f, w2), delay);
        } else if (phase == 2) {
            SUtil.delay(() -> Sputnik.pasteSchematic("f6_h3", true, 191.0f, 73.0f, 266.0f, w2), delay);
        }
    }

    public static void pasteChain(org.bukkit.World w2, float y2, int delay, boolean up) {
        SUtil.delay(() -> Sputnik.pasteSchematic("chain_main", true, 195.0f, y2, 261.0f, w2), delay);
        SUtil.delay(() -> w2.playSound(new Location(w2, 195.0, (double)y2, 261.0), Sound.HORSE_ARMOR, 100.0f, 0.0f), delay);
    }

    public static void pasteAir(org.bukkit.World w2, float y2, int delay) {
        SUtil.delay(() -> AnimationSequence.edit(new Location(w2, 191.0, (double)(y2 - 2.0f), 266.0), new Location(w2, 191.0, 69.0, 266.0), w2), delay + 1);
    }

    public static void beginRenderUp(org.bukkit.World w2) {
        AnimationSequence.pasteChain(w2, 69.0f, 1, false);
        AnimationSequence.pasteBase(w2, 1);
        AnimationSequence.pasteChain(w2, 71.0f, 20, false);
        AnimationSequence.pasteBase(w2, 20);
        AnimationSequence.pasteAir(w2, 71.0f, 20);
        AnimationSequence.pasteChain(w2, 73.0f, 40, false);
        AnimationSequence.pasteBase(w2, 40);
        AnimationSequence.pasteAir(w2, 73.0f, 40);
        AnimationSequence.pasteChain(w2, 75.0f, 60, false);
        AnimationSequence.pasteBase(w2, 60);
        AnimationSequence.pasteAir(w2, 75.0f, 60);
        AnimationSequence.pasteChain(w2, 77.0f, 80, false);
        AnimationSequence.pasteBase(w2, 80);
        AnimationSequence.pasteAir(w2, 77.0f, 80);
        AnimationSequence.pasteChain(w2, 79.0f, 100, false);
        AnimationSequence.pasteBase(w2, 100);
        AnimationSequence.pasteAir(w2, 79.0f, 100);
        AnimationSequence.pasteChain(w2, 81.0f, 120, false);
        AnimationSequence.pasteBase(w2, 120);
        AnimationSequence.pasteAir(w2, 81.0f, 120);
        AnimationSequence.pasteChain(w2, 83.0f, 140, false);
        AnimationSequence.pasteBase(w2, 140);
        AnimationSequence.pasteGlass(w2, 140, 0);
        AnimationSequence.pasteAir(w2, 83.0f, 140);
        AnimationSequence.pasteChain(w2, 85.0f, 160, false);
        AnimationSequence.pasteBase(w2, 160);
        AnimationSequence.pasteAir(w2, 85.0f, 160);
        AnimationSequence.pasteChain(w2, 87.0f, 180, false);
        AnimationSequence.pasteBase(w2, 180);
        AnimationSequence.pasteAir(w2, 87.0f, 180);
        AnimationSequence.pasteChain(w2, 89.0f, 200, false);
        AnimationSequence.pasteBase(w2, 200);
        AnimationSequence.pasteAir(w2, 89.0f, 200);
        AnimationSequence.pasteChain(w2, 91.0f, 220, false);
        AnimationSequence.pasteBase(w2, 220);
        AnimationSequence.pasteAir(w2, 91.0f, 220);
        AnimationSequence.pasteChain(w2, 93.0f, 240, false);
        AnimationSequence.pasteBase(w2, 240);
        AnimationSequence.pasteAir(w2, 93.0f, 240);
        AnimationSequence.pasteChain(w2, 95.0f, 260, false);
        AnimationSequence.pasteBase(w2, 260);
        AnimationSequence.pasteAir(w2, 95.0f, 260);
        AnimationSequence.pasteChain(w2, 97.0f, 280, false);
        AnimationSequence.pasteBase(w2, 280);
        AnimationSequence.pasteAir(w2, 97.0f, 280);
        AnimationSequence.pasteChain(w2, 99.0f, 300, false);
        AnimationSequence.pasteBase(w2, 300);
        AnimationSequence.pasteAir(w2, 99.0f, 300);
        AnimationSequence.pasteBase(w2, 301);
    }

    public static void beginRenderDown(org.bukkit.World w2) {
        AnimationSequence.pasteChain(w2, 99.0f, 1, false);
        AnimationSequence.pasteBase(w2, 1);
        AnimationSequence.pasteChain(w2, 97.0f, 20, false);
        AnimationSequence.pasteBase(w2, 20);
        AnimationSequence.pasteChain(w2, 95.0f, 40, false);
        AnimationSequence.pasteBase(w2, 40);
        AnimationSequence.pasteChain(w2, 93.0f, 60, false);
        AnimationSequence.pasteBase(w2, 60);
        AnimationSequence.pasteChain(w2, 91.0f, 80, false);
        AnimationSequence.pasteBase(w2, 80);
        AnimationSequence.pasteChain(w2, 89.0f, 110, false);
        AnimationSequence.pasteBase(w2, 110);
        AnimationSequence.pasteChain(w2, 87.0f, 130, false);
        AnimationSequence.pasteBase(w2, 130);
        AnimationSequence.pasteChain(w2, 85.0f, 150, false);
        AnimationSequence.pasteBase(w2, 150);
        AnimationSequence.pasteChain(w2, 83.0f, 170, false);
        AnimationSequence.pasteBase(w2, 170);
        AnimationSequence.pasteChain(w2, 81.0f, 180, false);
        AnimationSequence.pasteBase(w2, 180);
        AnimationSequence.pasteChain(w2, 79.0f, 200, false);
        AnimationSequence.pasteBase(w2, 200);
        AnimationSequence.pasteChain(w2, 77.0f, 220, false);
        AnimationSequence.pasteBase(w2, 220);
        AnimationSequence.pasteChain(w2, 75.0f, 240, false);
        AnimationSequence.pasteBase(w2, 240);
        AnimationSequence.pasteChain(w2, 73.0f, 260, false);
        AnimationSequence.pasteBase(w2, 260);
        AnimationSequence.pasteChain(w2, 71.0f, 280, false);
        AnimationSequence.pasteGlass(w2, 280, 1);
        AnimationSequence.pasteBase(w2, 280);
        AnimationSequence.pasteChain(w2, 69.0f, 300, false);
        AnimationSequence.pasteBase(w2, 300);
        AnimationSequence.pasteGlass(w2, 300, 2);
        AnimationSequence.pasteBase(w2, 301);
        SUtil.delay(() -> AnimationSequence.r(w2), 301L);
        SUtil.delay(() -> new SEntity(new Location(w2, 191.5, 54.0, 266.5, 180.0f, 0.0f), SEntityType.DUMMY_FUNCTION_2, new Object[0]), 300L);
    }

    public static void r(org.bukkit.World w2) {
        for (Entity e1 : w2.getEntities()) {
            if (!e1.hasMetadata("dummy_r")) continue;
            e1.remove();
        }
    }

    public static void edit(Location pos1, Location pos2, org.bukkit.World w2) {
        LocalWorld world = BukkitUtil.getLocalWorld((org.bukkit.World)w2);
        CuboidRegion selection = new CuboidRegion((World)world, (Vector)BlockVector.toBlockPoint((double)pos1.getX(), (double)pos1.getY(), (double)pos1.getZ()), (Vector)BlockVector.toBlockPoint((double)pos2.getX(), (double)pos2.getY(), (double)pos2.getZ()));
        EditSession e2 = WorldEdit.getInstance().getEditSessionFactory().getEditSession((World)world, -1);
        try {
            e2.setBlocks((Region)selection, new BaseBlock(0));
        } catch (MaxChangedBlocksException e22) {
            e22.printStackTrace();
        }
    }
}

