package vn.giakhanhvn.skysim.dimoon;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

public class Altar {
    public static double[] pAe;
    public static double[] pAd;
    public static double[] pAr;
    public static double[] pAg;
    public static World w;
    public static Block em1;
    public static Block em2;
    public static Block di1;
    public static Block di2;
    public static Block rb1;
    public static Block rb2;
    public static Block gl1;
    public static Block gl2;
    public static Block[] altarList;

    static {
        Altar.pAe = new double[]{234668.5, 159.0, 236545.5, 234665.5, 159.0, 236545.5};
        Altar.pAd = new double[]{234731.5, 159.0, 236481.5, 234731.5, 159.0, 236484.5};
        Altar.pAr = new double[]{234670.5, 159.0, 236418.5, 234673.5, 159.0, 236418.5};
        Altar.pAg = new double[]{234604.5, 159.0, 236482.5, 234604.5, 159.0, 236479.5};
        Altar.w = Bukkit.getWorld("arena");
        Altar.em1 = Altar.w.getBlockAt(new Location(Altar.w, Altar.pAe[0], Altar.pAe[1], Altar.pAe[2]));
        Altar.em2 = Altar.w.getBlockAt(new Location(Altar.w, Altar.pAe[3], Altar.pAe[4], Altar.pAe[5]));
        Altar.di1 = Altar.w.getBlockAt(new Location(Altar.w, Altar.pAd[0], Altar.pAd[1], Altar.pAd[2]));
        Altar.di2 = Altar.w.getBlockAt(new Location(Altar.w, Altar.pAd[3], Altar.pAd[4], Altar.pAd[5]));
        Altar.rb1 = Altar.w.getBlockAt(new Location(Altar.w, Altar.pAr[0], Altar.pAr[1], Altar.pAr[2]));
        Altar.rb2 = Altar.w.getBlockAt(new Location(Altar.w, Altar.pAr[3], Altar.pAr[4], Altar.pAr[5]));
        Altar.gl1 = Altar.w.getBlockAt(new Location(Altar.w, Altar.pAg[0], Altar.pAg[1], Altar.pAg[2]));
        Altar.gl2 = Altar.w.getBlockAt(new Location(Altar.w, Altar.pAg[3], Altar.pAg[4], Altar.pAg[5]));
        Altar.altarList = new Block[]{Altar.di1, Altar.di2, Altar.em1, Altar.em2, Altar.gl1, Altar.gl2, Altar.rb1, Altar.rb2};
    }
}
