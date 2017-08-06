package Datastructure;

import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by e on 6/08/17.
 */
public class FastPriorityBlockingQueue<E> implements Queue<E> {

    private final PriorityQueue<E> queue;
    private final Set<E> set;
    private final ReentrantLock lock = new ReentrantLock(true);
    private final Condition notEmpty = lock.newCondition();

    public FastPriorityBlockingQueue() {
        this.queue = new PriorityQueue<>();
        this.set = new HashSet<>();
    }

    private class Itor implements Iterator<E>{
        final Object[] array;
        int caret, lastRet;

        Itor(Object[] array) {
            lastRet = -1;
            this.array = array;
        }

        public boolean hasNext() {
            return caret < array.length;
        }

        public E next() {
            if (caret >= array.length)
                throw new NoSuchElementException();
            lastRet = caret;
            return (E)array[caret++];
        }

        public void remove() {
            if (lastRet < 0)
                throw new IllegalStateException();
            Object x = array[lastRet];
            lastRet = -1;
            lock.lock();
            try {
                for (Iterator it = queue.iterator(); it.hasNext(); ) {
                    if (it.next() == x) {
                        it.remove();
                        return;
                    }
                }
            } finally {
                lock.unlock();
            }
        }
    }

    @Override
    public Iterator<E> iterator() {
        return new Itor(toArray());
    }

    @Override
    public Object[] toArray() {
        return this.queue.toArray();
    }

    @Override
    public <T> T[] toArray(T[] ts) {
        return this.queue.toArray(ts);
    }

    @Override
    public boolean add(final E e) {
        return offer(e);
    }

    @Override
    public boolean remove(Object o) {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            this.queue.remove(o);
            this.set.remove(o);
            return true;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            return set.containsAll(collection);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean addAll(Collection<? extends E> collection) {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            queue.addAll(collection);
            return set.addAll(collection);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            queue.removeAll(collection);
            return set.removeAll(collection);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            this.queue.retainAll(collection);
            return set.retainAll(collection);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void clear() {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            this.queue.clear();
            this.set.clear();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public int size() {
        return queue.size();
    }

    @Override
    public boolean isEmpty() {
        return set.isEmpty();
    }

    @Override
    public boolean offer(final E e) {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            boolean ok = this.queue.offer(e);
            assert ok;
            this.set.add(e);
            notEmpty.signal();
            return true;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public E remove() {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            E obj = this.queue.remove();
            this.set.remove(obj);
            return obj;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public E poll() {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            return this.queue.poll();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public E element() {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            return this.queue.element();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean contains(final Object o) {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            return this.set.contains(o);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public E peek() {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            return queue.peek();
        } finally {
            lock.unlock();
        }
    }

}
