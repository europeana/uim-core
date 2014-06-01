/*
 * Copyright 2001-2007 by Andreas Juffinger, comtoo.org       
 *                                                                            
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.theeuropeanlibrary.collections.guarded;

import java.util.AbstractQueue;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <p>
 * </p>
 *
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @since Aug 22, 2011
 */
@SuppressWarnings({"cast", "rawtypes", "unchecked"})
public class GuardedQueue extends AbstractQueue<Guarded> implements BlockingQueue<Guarded> {

    /**
     * long serialVersionUID
     */
    public static final long serialVersionUID = 1L;

    private PriorityQueue<GuardedKey> priority = null;

    private Map<Object, Queue<Guarded>> sublistMap = new HashMap<>();

    private AtomicInteger size = new AtomicInteger(0);
    private GuardCondition condition;

    private int maxParallelSize = 500;
    private int maxQueueSize = 100;

    private ReentrantLock changeLock = new ReentrantLock();

    private final Semaphore emptyBlock;

    private final Semaphore maxKeySizeBlock;
    private final Semaphore maxSizeBlock;

    /**
     * Creates a new instance of this class.
     *
     * @param condition
     * @param maxParallelQueues
     * @param maxQueueSize
     * @param maxSize
     * @param fair
     */
    public GuardedQueue(GuardCondition condition, int maxParallelQueues, int maxQueueSize,
            int maxSize, boolean fair) {
        this.condition = condition;
        this.maxParallelSize = maxParallelQueues;
        this.maxQueueSize = maxQueueSize;

        priority = new PriorityQueue<>(maxQueueSize, new Comparator<GuardedKey>() {
            @Override
            public int compare(GuardedKey o1, GuardedKey o2) {
                return o1.compareTo(o2);
            }
        });

        emptyBlock = new Semaphore(0, fair);
        maxKeySizeBlock = new Semaphore(maxParallelQueues, fair);
        maxSizeBlock = new Semaphore(maxSize, fair);
    }

    /**
     * @param task
     * @return accept?
     */
    @Override
    public boolean offer(Guarded task) {
        if (task == null) {
            throw new NullPointerException();
        }

        GuardedKey key = task.getGuardKey();
        if (key == null) {
            throw new NullPointerException();
        }

        changeLock.lock();
        try {
            Queue<Guarded> subList = (Queue<Guarded>) sublistMap.get(key);
            if (subList == null) {
                subList = new LinkedList<>();
                if (maxKeySizeBlock.tryAcquire()) {
                    addQueue(key, subList);
                } else {
                    changeLock.unlock();
                    return false;
                }
            }

            if (maxSizeBlock.tryAcquire()) {
                if (subList.offer(task)) {
                    size.getAndIncrement();
                }

                emptyBlock.release();
                changeLock.unlock();
                return true;
            }
        } finally {
            changeLock.unlock();
        }
        return false;
    }

    @Override
    public boolean offer(Guarded task, long timeout, TimeUnit unit) throws InterruptedException {
        if (task == null) {
            throw new NullPointerException();
        }

        GuardedKey key = task.getGuardKey();
        if (key == null) {
            throw new NullPointerException();
        }

        changeLock.lock();
        try {
            Queue<Guarded> subList = (Queue<Guarded>) sublistMap.get(key);
            if (subList == null) {
                subList = new LinkedList<>();
                if (maxKeySizeBlock.tryAcquire(timeout, unit)) {
                    addQueue(key, subList);
                } else {
                    changeLock.unlock();
                    return false;
                }
            }

            if (maxSizeBlock.tryAcquire(timeout, unit)) {
                if (subList.offer(task)) {
                    size.getAndIncrement();
                }
                emptyBlock.release();
                changeLock.unlock();
                return true;
            }
        } finally {
            changeLock.unlock();
        }
        return false;
    }

    @Override
    public Guarded poll() {
        LinkedList<GuardedKey> testList = new LinkedList<>();
        GuardedKey key;

        changeLock.lock();
        do {
            key = (GuardedKey) priority.peek();
            if (key != null && condition.allow(key)) {
                testList.add(priority.poll());
            }
        } while (key != null && condition.allow(key));

        if (testList.isEmpty()) {
            changeLock.unlock();
            return null;
        } else {
            GuardedKey key0 = testList.getFirst();
            Queue<Guarded> subList = (Queue<Guarded>) sublistMap.get(key0);

            Guarded gt0 = subList.poll();
            Queue<Guarded> subList0 = subList;

            key0.delivered();
            for (GuardedKey k : testList) {
                priority.offer(k);
            }

            if (subList0.isEmpty()) {
                removeQueue(key0);
            }

            size.getAndDecrement();
            maxSizeBlock.release();

            changeLock.unlock();
            return gt0;
        }

    }

    @Override
    public Guarded poll(long timeout, TimeUnit unit) throws InterruptedException {
        Guarded e = poll();
        if (e == null) {
            long millis = unit.toMillis(timeout);

            do {
                if (emptyBlock.tryAcquire(millis, TimeUnit.MILLISECONDS)) {
                    try {
                        GuardedKey key = (GuardedKey) priority.peek();
                        if (key != null) {
                            if (condition.allow(key)) {
                                e = poll();
                                if (e != null) {
                                    return e;
                                }
                            }
                        }
                    } finally {
                        emptyBlock.release();
                    }
                }
                Thread.sleep(20);
                millis -= 20;
            } while (millis > 0);
        }
        return e;
    }

    @Override
    public Guarded take() throws InterruptedException {
        Guarded e = null;
        do {
            e = poll(20, TimeUnit.MILLISECONDS);
            if (e == null && size.get() == 0) {
                emptyBlock.acquire();
            }
        } while (e == null);
        return e;
    }

    @Override
    public Guarded peek() {
        GuardedKey key = priority.peek();
        if (condition.allow(key)) {
            Queue<Guarded> subList = sublistMap.get(key);
            Guarded gt = subList.peek();
            return gt;
        }
        return null;
    }

    @Override
    public int remainingCapacity() {
        return (maxParallelSize * maxQueueSize) - size.get();
    }

    /**
     * @return size sublist map
     */
    public int sizeMap() {
        return sublistMap.size();
    }

    @Override
    public int size() {
        return size.get();
    }

    @Override
    public boolean isEmpty() {
        return size.get() == 0 && sublistMap.isEmpty();
    }

    @Override
    public synchronized int drainTo(Collection<? super Guarded> c, int maxElements) {
        int cnt = 0;
        for (Queue<Guarded> queue : sublistMap.values()) {
            while (!queue.isEmpty()) {
                c.add(queue.poll());
                size.getAndDecrement();
                cnt++;
                if (cnt > maxElements) {
                    break;
                }
            }
        }
        return cnt;
    }

    @Override
    public synchronized int drainTo(Collection<? super Guarded> c) {
        int cnt = 0;
        for (Queue<Guarded> queue : sublistMap.values()) {
            while (!queue.isEmpty()) {
                c.add(queue.poll());
                size.getAndDecrement();
                cnt++;
            }
        }

        return cnt;
    }

    @Override
    public void put(Guarded o) throws InterruptedException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<Guarded> iterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        String result = priority.toString();
        return result;
    }

    private synchronized void addQueue(GuardedKey key, Queue<Guarded> subList) {
        sublistMap.put(key, subList);
        priority.offer(key);
    }

    private synchronized void removeQueue(Object key) {
        sublistMap.remove(key);
        priority.remove(key);
        maxKeySizeBlock.release();
    }

}
