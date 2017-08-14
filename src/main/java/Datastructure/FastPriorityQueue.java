package Datastructure;

import java.util.*;

/**
 * A wrapped priority queue with a O(1) cost for contains()
 * while maintaining O(1) for deque operations as expense of memory
 * This class is NOT threadsafe
 */
public class FastPriorityQueue<T> implements Queue<T> {
    /*
     * Wrapped data structure, mutable data structure has to be used
     * due to memory constraints
     */
    private final PriorityQueue<T> queue;
    private final HashSet<T> set;

    public FastPriorityQueue() {
        this.queue = new PriorityQueue<>();
        this.set = new HashSet<>();
    }

    @Override
    public int size() {
        return set.size();
    }

    @Override
    public boolean isEmpty() {
        return set.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return set.contains(o);
    }

    @Override
    public Iterator<T> iterator() {
        return set.iterator();
    }

    @Override
    public Object[] toArray() {
        return set.toArray();
    }

    @Override
    public <T1> T1[] toArray(T1[] t1s) {
        return set.toArray(t1s);
    }

    @Override
    public boolean add(T t) {
        set.add(t);
        queue.add(t);
        return true;
    }

    @Override
    public boolean remove(Object o) {
        set.remove(o);
        queue.remove(o);
        return true;
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        return set.containsAll(collection);
    }

    @Override
    public boolean addAll(Collection<? extends T> collection) {
        set.addAll(collection);
        queue.addAll(collection);
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        set.removeAll(collection);
        queue.removeAll(collection);
        return true;
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        set.retainAll(collection);
        queue.retainAll(collection);
        return true;
    }

    @Override
    public void clear() {
        set.clear();
        queue.clear();
    }

    @Override
    public boolean offer(T t) {
        if (queue.offer(t)) {
            set.add(t);
            return true;
        }
        return false;
    }

    @Override
    public T remove() {
        T o = queue.remove();
        set.remove(o);
        return o;
    }

    @Override
    public T poll() {
        return queue.poll();
    }

    @Override
    public T element() {
        return queue.element();
    }

    @Override
    public T peek() {
        return queue.peek();
    }
}
