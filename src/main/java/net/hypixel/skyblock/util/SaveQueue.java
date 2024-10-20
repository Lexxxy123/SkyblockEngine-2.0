/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.hypixel.skyblock.util;

import java.util.LinkedList;

public class SaveQueue<T> {
    private final LinkedList<T> queue = new LinkedList();

    public void enqueue(T element) {
        this.queue.add(element);
    }

    public void add(T element) {
        this.queue.add(element);
    }

    public T dequeue() {
        if (this.isEmpty()) {
            throw new IllegalStateException("Queue is empty");
        }
        return this.queue.remove();
    }

    public boolean isEmpty() {
        return this.queue.isEmpty();
    }

    public int size() {
        return this.queue.size();
    }
}

