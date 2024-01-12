package in.godspunky.skyblock.util;

import java.util.LinkedList;

public class SaveQueue<T> {
    private final LinkedList<T> queue;

    public SaveQueue() {
        queue = new LinkedList<>();
    }

    public void enqueue(T element) {
        queue.add(element);
    }

    public void add(T element) {
        queue.add(element);
    }

    public T dequeue() {
        if (isEmpty()) {
            throw new IllegalStateException("Queue is empty");
        }
        return queue.remove();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }

    public int size() {
        return queue.size();
    }
}
