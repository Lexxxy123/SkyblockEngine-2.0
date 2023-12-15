package vn.giakhanhvn.skysim.collection;

import java.util.Collection;
import java.util.Arrays;
import java.util.ArrayList;

public class ItemCollectionRewards extends ArrayList<ItemCollectionReward> {
    private final int requirement;

    public ItemCollectionRewards(final int requirement, final ItemCollectionReward... rewards) {
        super(Arrays.asList(rewards));
        this.requirement = requirement;
    }

    public int getRequirement() {
        return this.requirement;
    }
}
