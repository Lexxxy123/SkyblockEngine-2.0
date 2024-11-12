/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.hypixel.skyblock.user;

import java.util.ArrayList;
import java.util.Arrays;
import net.hypixel.skyblock.user.PlayerStatistic;

public class IntegerPlayerStatistic
implements PlayerStatistic<Integer> {
    private final int defaultValue;
    private final ArrayList<Integer> values;

    public IntegerPlayerStatistic(int defaultValue) {
        this.defaultValue = defaultValue;
        this.values = new ArrayList<Integer>(6);
        this.values.addAll(Arrays.asList(0, 0, 0, 0, 0, 0));
    }

    public IntegerPlayerStatistic() {
        this(0);
    }

    @Override
    public Integer addAll() {
        int result = this.defaultValue;
        for (Integer value : new ArrayList<Integer>(this.values)) {
            result += value.intValue();
        }
        return result;
    }

    @Override
    public void add(int slot, Integer i2) {
        this.set(slot, this.safeGet(slot) + i2);
    }

    @Override
    public void sub(int slot, Integer i2) {
        this.set(slot, this.safeGet(slot) - i2);
    }

    @Override
    public void zero(int slot) {
        this.set(slot, 0);
    }

    @Override
    public boolean contains(int slot) {
        return slot >= 0 && slot < this.values.size();
    }

    @Override
    public Integer safeGet(int index) {
        if (index < 0 || index > this.values.size() - 1) {
            this.set(index, 0);
        }
        return this.values.get(index);
    }

    @Override
    public void set(int slot, Integer i2) {
        this.values.ensureCapacity(slot + 1);
        while (this.values.size() < slot + 1) {
            this.values.add(0);
        }
        this.values.set(slot, i2);
    }

    @Override
    public int next() {
        return this.values.size();
    }

    @Override
    public Integer getFor(int slot) {
        return this.safeGet(slot);
    }

    public ArrayList<Integer> forInventory() {
        ArrayList<Integer> list = new ArrayList<Integer>();
        for (int i2 = 6; i2 < this.values.size(); ++i2) {
            list.add(this.safeGet(i2));
        }
        return list;
    }

    public int getDefaultValue() {
        return this.defaultValue;
    }
}

