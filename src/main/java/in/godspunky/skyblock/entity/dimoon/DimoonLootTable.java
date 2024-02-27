package in.godspunky.skyblock.entity.dimoon;

import org.bukkit.entity.Player;
import in.godspunky.skyblock.util.SUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DimoonLootTable {
    public static List<DimoonLootItem> lowQualitylootTable;
    public static List<DimoonLootItem> highQualitylootTable;
    private int weight;
    private Player p;
    private final int cp;
    private final int plm;

    public DimoonLootTable(final Player p, final int placement, final int catalPlaced) {
        this.weight = 0;
        this.cp = catalPlaced;
        this.plm = placement;
        if (placement <= 0 || catalPlaced < 0) {
            return;
        }
        this.p = p;
        this.weight = catalPlaced * 100 + this.calPlacementWeight(placement);
    }

    int calPlacementWeight(final int i) {
        if (i == 1) {
            return 500;
        }
        if (i == 2) {
            return 400;
        }
        if (i == 3) {
            return 300;
        }
        if (i == 4) {
            return 200;
        }
        if (i == 5) {
            return 100;
        }
        if (i > 5 && i <= 400) {
            return 400 / i;
        }
        return 0;
    }

    public List<List<DimoonLootItem>> roll() {
        if (this.cp < 0 || this.plm <= 0) {
            return null;
        }
        List<DimoonLootItem> rolledHiItems = new ArrayList<DimoonLootItem>();
        List<DimoonLootItem> rolledLoItems = new ArrayList<DimoonLootItem>();
        List<DimoonLootItem> highQualityLootable = (List<DimoonLootItem>) ((ArrayList) DimoonLootTable.highQualitylootTable).clone();
        highQualityLootable.removeIf(item -> item.getMinimumWeight() > this.weight);
        List<DimoonLootItem> lowQualityLootable = (List<DimoonLootItem>) ((ArrayList) DimoonLootTable.lowQualitylootTable).clone();
        lowQualityLootable.removeIf(item -> item.getMinimumWeight() > this.weight);
        for (DimoonLootItem item2 : highQualityLootable) {
            int r = SUtil.random(1, item2.getChance());
            if (r == 1) {
                rolledHiItems.add(item2);
                break;
            }
        }
        for (DimoonLootItem item2 : lowQualityLootable) {
            int r = SUtil.random(1, item2.getChance());
            if (r == 1) {
                rolledLoItems.add(item2);
                break;
            }
        }
        return Arrays.asList(rolledHiItems, rolledLoItems);
    }

    public int getWeight() {
        return this.weight;
    }

    static {
        DimoonLootTable.lowQualitylootTable = null;
        DimoonLootTable.highQualitylootTable = null;
    }
}
