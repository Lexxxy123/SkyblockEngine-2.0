package vn.giakhanhvn.skysim.collection;

import java.util.ArrayList;
import java.util.Arrays;

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
