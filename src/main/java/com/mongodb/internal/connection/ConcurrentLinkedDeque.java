/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.connection;

import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicReference;

public class ConcurrentLinkedDeque<E>
extends AbstractCollection<E>
implements Deque<E>,
Serializable {
    private static final long serialVersionUID = 876323262645176354L;
    private final Node<E> header;
    private final Node<E> trailer;

    private static boolean usable(Node<?> n2) {
        return n2 != null && !n2.isSpecial();
    }

    private static void checkNotNull(Object v2) {
        if (v2 == null) {
            throw new NullPointerException();
        }
    }

    private E screenNullResult(E v2) {
        if (v2 == null) {
            throw new NoSuchElementException();
        }
        return v2;
    }

    private ArrayList<E> toArrayList() {
        ArrayList c2 = new ArrayList();
        for (Node<E> n2 = this.header.forward(); n2 != null; n2 = n2.forward()) {
            c2.add(n2.element);
        }
        return c2;
    }

    public ConcurrentLinkedDeque() {
        Node<Object> h2 = new Node<Object>(null, null, null);
        Node<Object> t2 = new Node<Object>(null, null, h2);
        h2.setNext(t2);
        this.header = h2;
        this.trailer = t2;
    }

    public ConcurrentLinkedDeque(Collection<? extends E> c2) {
        this();
        this.addAll(c2);
    }

    @Override
    public void addFirst(E e2) {
        ConcurrentLinkedDeque.checkNotNull(e2);
        while (this.header.append(e2) == null) {
        }
    }

    @Override
    public void addLast(E e2) {
        ConcurrentLinkedDeque.checkNotNull(e2);
        while (this.trailer.prepend(e2) == null) {
        }
    }

    @Override
    public boolean offerFirst(E e2) {
        this.addFirst(e2);
        return true;
    }

    @Override
    public boolean offerLast(E e2) {
        this.addLast(e2);
        return true;
    }

    @Override
    public E peekFirst() {
        Node<E> n2 = this.header.successor();
        return n2 == null ? null : (E)n2.element;
    }

    @Override
    public E peekLast() {
        Node<E> n2 = this.trailer.predecessor();
        return n2 == null ? null : (E)n2.element;
    }

    @Override
    public E getFirst() {
        return this.screenNullResult(this.peekFirst());
    }

    @Override
    public E getLast() {
        return this.screenNullResult(this.peekLast());
    }

    @Override
    public E pollFirst() {
        Node<E> n2;
        do {
            if (ConcurrentLinkedDeque.usable(n2 = this.header.successor())) continue;
            return null;
        } while (!n2.delete());
        return n2.element;
    }

    @Override
    public E pollLast() {
        Node<E> n2;
        do {
            if (ConcurrentLinkedDeque.usable(n2 = this.trailer.predecessor())) continue;
            return null;
        } while (!n2.delete());
        return n2.element;
    }

    @Override
    public E removeFirst() {
        return this.screenNullResult(this.pollFirst());
    }

    @Override
    public E removeLast() {
        return this.screenNullResult(this.pollLast());
    }

    @Override
    public boolean offer(E e2) {
        return this.offerLast(e2);
    }

    @Override
    public boolean add(E e2) {
        return this.offerLast(e2);
    }

    @Override
    public E poll() {
        return this.pollFirst();
    }

    @Override
    public E remove() {
        return this.removeFirst();
    }

    @Override
    public E peek() {
        return this.peekFirst();
    }

    @Override
    public E element() {
        return this.getFirst();
    }

    @Override
    public void push(E e2) {
        this.addFirst(e2);
    }

    @Override
    public E pop() {
        return this.removeFirst();
    }

    @Override
    public boolean removeFirstOccurrence(Object o2) {
        ConcurrentLinkedDeque.checkNotNull(o2);
        block0: while (true) {
            Node<E> n2 = this.header.forward();
            while (true) {
                if (n2 == null) {
                    return false;
                }
                if (o2.equals(n2.element)) {
                    if (!n2.delete()) continue block0;
                    return true;
                }
                n2 = n2.forward();
            }
            break;
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public boolean removeLastOccurrence(Object o2) {
        ConcurrentLinkedDeque.checkNotNull(o2);
        block0: while (true) {
            Node<E> s2 = this.trailer;
            while (true) {
                Node<E> n2 = s2.back();
                if (s2.isDeleted() || n2 != null && n2.successor() != s2) continue block0;
                if (n2 == null) {
                    return false;
                }
                if (o2.equals(n2.element)) {
                    if (n2.delete()) return true;
                    continue block0;
                }
                s2 = n2;
            }
            break;
        }
    }

    @Override
    public boolean contains(Object o2) {
        if (o2 == null) {
            return false;
        }
        for (Node<E> n2 = this.header.forward(); n2 != null; n2 = n2.forward()) {
            if (!o2.equals(n2.element)) continue;
            return true;
        }
        return false;
    }

    @Override
    public boolean isEmpty() {
        return !ConcurrentLinkedDeque.usable(this.header.successor());
    }

    @Override
    public int size() {
        long count = 0L;
        for (Node<E> n2 = this.header.forward(); n2 != null; n2 = n2.forward()) {
            ++count;
        }
        return count >= Integer.MAX_VALUE ? Integer.MAX_VALUE : (int)count;
    }

    @Override
    public boolean remove(Object o2) {
        return this.removeFirstOccurrence(o2);
    }

    @Override
    public boolean addAll(Collection<? extends E> c2) {
        Iterator<E> it = c2.iterator();
        if (!it.hasNext()) {
            return false;
        }
        do {
            this.addLast(it.next());
        } while (it.hasNext());
        return true;
    }

    @Override
    public void clear() {
        while (this.pollFirst() != null) {
        }
    }

    @Override
    public Object[] toArray() {
        return this.toArrayList().toArray();
    }

    @Override
    public <T> T[] toArray(T[] a2) {
        return this.toArrayList().toArray(a2);
    }

    @Override
    public RemovalReportingIterator<E> iterator() {
        return new CLDIterator();
    }

    @Override
    public Iterator<E> descendingIterator() {
        throw new UnsupportedOperationException();
    }

    public static interface RemovalReportingIterator<E>
    extends Iterator<E> {
        public boolean reportingRemove();
    }

    final class CLDIterator
    implements RemovalReportingIterator<E> {
        Node<E> last;
        Node<E> next;

        CLDIterator() {
            this.next = ConcurrentLinkedDeque.this.header.forward();
        }

        @Override
        public boolean hasNext() {
            return this.next != null;
        }

        @Override
        public E next() {
            this.last = this.next;
            Node l2 = this.last;
            if (l2 == null) {
                throw new NoSuchElementException();
            }
            this.next = this.next.forward();
            return l2.element;
        }

        @Override
        public void remove() {
            this.reportingRemove();
        }

        @Override
        public boolean reportingRemove() {
            Node l2 = this.last;
            if (l2 == null) {
                throw new IllegalStateException();
            }
            boolean successfullyRemoved = l2.delete();
            while (!successfullyRemoved && !l2.isDeleted()) {
                successfullyRemoved = l2.delete();
            }
            return successfullyRemoved;
        }
    }

    static final class Node<E>
    extends AtomicReference<Node<E>> {
        private volatile Node<E> prev;
        final E element;
        private static final long serialVersionUID = 876323262645176354L;

        Node(E element, Node<E> next, Node<E> prev) {
            super(next);
            this.prev = prev;
            this.element = element;
        }

        Node(Node<E> next) {
            super(next);
            this.prev = this;
            this.element = null;
        }

        private Node<E> getNext() {
            return (Node)this.get();
        }

        void setNext(Node<E> n2) {
            this.set(n2);
        }

        private boolean casNext(Node<E> cmp, Node<E> val) {
            return this.compareAndSet(cmp, val);
        }

        private Node<E> getPrev() {
            return this.prev;
        }

        void setPrev(Node<E> b2) {
            this.prev = b2;
        }

        boolean isSpecial() {
            return this.element == null;
        }

        boolean isTrailer() {
            return this.getNext() == null;
        }

        boolean isHeader() {
            return this.getPrev() == null;
        }

        boolean isMarker() {
            return this.getPrev() == this;
        }

        boolean isDeleted() {
            Node<E> f2 = this.getNext();
            return f2 != null && f2.isMarker();
        }

        private Node<E> nextNonmarker() {
            Node<E> f2 = this.getNext();
            return f2 == null || !f2.isMarker() ? f2 : super.getNext();
        }

        Node<E> successor() {
            Node<E> f2 = this.nextNonmarker();
            while (f2 != null) {
                if (!f2.isDeleted()) {
                    if (super.getPrev() != this && !this.isDeleted()) {
                        f2.setPrev(this);
                    }
                    return f2;
                }
                Node<E> s2 = super.nextNonmarker();
                if (f2 == this.getNext()) {
                    this.casNext(f2, s2);
                }
                f2 = s2;
            }
            return null;
        }

        private Node<E> findPredecessorOf(Node<E> target) {
            Node<E> n2 = this;
            Node<E> f2;
            while ((f2 = n2.successor()) != target) {
                if (f2 == null) {
                    return null;
                }
                n2 = f2;
            }
            return n2;
        }

        Node<E> predecessor() {
            Node<E> n2 = this;
            Node<E> b2;
            while ((b2 = n2.getPrev()) != null) {
                Node<E> p2;
                Node<E> s2 = super.getNext();
                if (s2 == this) {
                    return b2;
                }
                if (!(s2 != null && s2.isMarker() || (p2 = super.findPredecessorOf(this)) == null)) {
                    return p2;
                }
                n2 = b2;
            }
            return n2.findPredecessorOf(this);
        }

        Node<E> forward() {
            Node<E> f2 = this.successor();
            return f2 == null || f2.isSpecial() ? null : f2;
        }

        Node<E> back() {
            Node<E> f2 = this.predecessor();
            return f2 == null || f2.isSpecial() ? null : f2;
        }

        Node<E> append(E element) {
            Node<E> x2;
            Node<E> f2;
            do {
                if ((f2 = this.getNext()) != null && !f2.isMarker()) continue;
                return null;
            } while (!this.casNext(f2, x2 = new Node<E>(element, f2, this)));
            f2.setPrev(x2);
            return x2;
        }

        Node<E> prepend(E element) {
            Node<E> x2;
            Node<E> b2;
            do {
                if ((b2 = this.predecessor()) != null) continue;
                return null;
            } while (!super.casNext(this, x2 = new Node<E>(element, this, b2)));
            this.setPrev(x2);
            return x2;
        }

        boolean delete() {
            Node<E> b2 = this.getPrev();
            Node<E> f2 = this.getNext();
            if (b2 != null && f2 != null && !f2.isMarker() && this.casNext(f2, new Node<E>(f2))) {
                if (super.casNext(this, f2)) {
                    f2.setPrev(b2);
                }
                return true;
            }
            return false;
        }

        Node<E> replace(E newElement) {
            Node<E> b2;
            Node<E> x2;
            Node<E> f2;
            do {
                b2 = this.getPrev();
                f2 = this.getNext();
                if (b2 != null && f2 != null && !f2.isMarker()) continue;
                return null;
            } while (!this.casNext(f2, new Node<E>(x2 = new Node<E>(newElement, f2, b2))));
            b2.successor();
            x2.successor();
            return x2;
        }
    }
}

