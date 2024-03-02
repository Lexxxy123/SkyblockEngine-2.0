package net.hypixel.skyblock.util;

import java.util.ArrayList;

public class StackArrayList<T> extends ArrayList<T> {
    public int push(final T element) {
        this.add(element);
        return this.size() - 1;
    }

    public T shift() {
        if (this.isEmpty()) {
            throw new ArrayIndexOutOfBoundsException("Could not shift because the Collection is empty");
        }
        final T el = this.get(0);
        this.remove(0);
        return el;
    }

    public T pop() {
        if (this.isEmpty()) {
            throw new ArrayIndexOutOfBoundsException("Could not pop off last element because the Collection is empty");
        }
        final T el = this.get(this.size() - 1);
        this.remove(this.size() - 1);
        return el;
    }

    public T first() {
        if (this.isEmpty()) {
            throw new ArrayIndexOutOfBoundsException("Could not get the first element because the Collection is empty");
        }
        return this.get(0);
    }

    public T last() {
        if (this.isEmpty()) {
            throw new ArrayIndexOutOfBoundsException("Could not get the last element because the Collection is empty");
        }
        return this.get(this.size() - 1);
    }
}
