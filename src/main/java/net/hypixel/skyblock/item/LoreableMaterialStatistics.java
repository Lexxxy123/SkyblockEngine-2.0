/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.hypixel.skyblock.item;

import java.util.List;
import net.hypixel.skyblock.item.MaterialStatistics;
import net.hypixel.skyblock.item.SItem;

public interface LoreableMaterialStatistics
extends MaterialStatistics {
    public List<String> getCustomLore(SItem var1);
}

