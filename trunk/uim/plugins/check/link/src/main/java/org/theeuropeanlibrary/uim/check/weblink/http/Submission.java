/* Submission.java - created on Jul 15, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.uim.check.weblink.http;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.theeuropeanlibrary.collections.guarded.GuardedQueue;

import eu.europeana.uim.api.StorageEngine;

/**
 * 
 * 
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @since Jul 15, 2011
 */
public class Submission {
    private static final Logger              log        = Logger.getLogger(Submission.class.getName());

    private Set<GuardedMetaDataRecordUrl<?>> remaining  = new HashSet<GuardedMetaDataRecordUrl<?>>();
    private Map<Integer, Integer>            statusmap  = new HashMap<Integer, Integer>();

    private long                             start      = System.currentTimeMillis();

    private int                              processed  = 0;
    private int                              exceptions = 0;

    private GuardedQueue                     queue;
    private StorageEngine<?>                 storage;

    /**
     * Creates a new instance of this class.
     * 
     * @param storage
     * @param queue 
     */
    public Submission(StorageEngine<?> storage, GuardedQueue queue) {
        this.storage = storage;
        this.queue = queue;
    }

    /**
     * Returns the storage.
     * 
     * @return the storage
     */
    public StorageEngine<?> getStorageEngine() {
        return storage;
    }

    /**
     * @param guarded
     * @return adding successfull?
     */
    public boolean addRemaining(GuardedMetaDataRecordUrl<?> guarded) {
        synchronized (remaining) {
            return remaining.add(guarded);
        }
    }

    /**
     * @param guarded
     * @return removing successfull?
     */
    public boolean removeRemaining(GuardedMetaDataRecordUrl<?> guarded) {
        synchronized (remaining) {
            return remaining.remove(guarded);
        }
    }

    /**
     * @return processed
     */
    public int getProcessed() {
        return processed;
    }

    /**
     * increments processed
     */
    public void incrProcessed() {
        this.processed++;
    }

    /**
     * @return exceptions
     */
    public int getExceptions() {
        return exceptions;
    }

    /**
     * Increases the exceptions count by one
     */
    public void incrExceptions() {
        this.exceptions++;
    }

    /**
     * Increases the status count by one
     * 
     * @param status
     */
    public void incrStatus(int status) {
        if (!statusmap.containsKey(status)) {
            statusmap.put(status, 0);
        }
        statusmap.put(status, statusmap.get(status) + 1);
    }

    /**
     * @return statusmap
     */
    public Map<Integer, Integer> getStatus() {
        return statusmap;
    }

    /**
     * Wait until no remaining urls are in the queue.
     */
    public void waitUntilFinished() {
        try {
            if (queue.isEmpty()) {
                Thread.sleep(500);
            }
            
            while (!remaining.isEmpty()) {
                Thread.sleep(100);
//                if (remaining.size() == 1) {
//                    System.out.println("Remaining:" + remaining.iterator().next() + ", Queue:" +
//                                       queue.size() + ", Processed:" + getProcessed() +
//                                       ", Exceptions:" + getExceptions() + ", Status:" + getStatus());
//                }
            }
        } catch (Throwable t) {
        }

        long time = System.currentTimeMillis() - start;
        log.info(String.format("Done %d records in %.0f secs. Average: %.3f/sec", getProcessed(),
                time / 1000.0, getProcessed() * 1000.0 / time));
    }
}
