package vn.giakhanhvn.skysim.item;

import java.util.List;

public interface LoreableMaterialStatistics extends MaterialStatistics {
    List<String> getCustomLore(final SItem p0);
}
