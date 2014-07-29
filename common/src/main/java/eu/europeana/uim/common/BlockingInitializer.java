/* BlockingInitializer.java - created on Mar 2, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.common;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Blocks while intialization of generic things.
 *
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @since Mar 2, 2011
 */
public abstract class BlockingInitializer implements Runnable {

    private static final Logger log = Logger.getLogger(BlockingInitializer.class.getName());

    public static final int STATUS_NEW = 0;
    public static final int STATUS_BOOTING = 1;
    public static final int STATUS_FAILED = 97;
    public static final int STATUS_INITIALIZED = 98;
    public static final int STATUS_SHUTDOWN = 99;

    protected int status = STATUS_NEW;

    /**
     * BlockingInitializer exception which is caught from the internal run
     * method
     */
    protected RuntimeException exception = null;

    /**
     * Creates a new instance of this class.
     */
    public BlockingInitializer() {
    }

    /**
     * Start the guarded initialization in a new thread and wait till the result
     * for that is clear.
     *
     * @param cl the class loader relevant for this context
     */
    public final synchronized void initialize(ClassLoader cl) {
        if (status >= STATUS_FAILED) {
            return;
        }

        // if status is NEW or BOOTING
        // we start a new initialization process.
        Thread thread = new Thread(this);
        thread.setContextClassLoader(cl);
        thread.start();

        while (status < STATUS_FAILED) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                //ignore interruption, just go to next check
            }
        }

        if (exception != null) {
            throw exception;
        }
    }

    /**
     * @return status information
     */
    public synchronized int getStatus() {
        return status;
    }

    @Override
    public final void run() {
        try {
            status = STATUS_BOOTING;
            initializeInternal();
            status = STATUS_INITIALIZED;
        } catch (Throwable t) {
            log.log(Level.SEVERE, "Failed to initialize with classloader:"
                    + Thread.currentThread().getContextClassLoader(), t);
            status = STATUS_FAILED;
            exception = new RuntimeException("Exception while running initialization.", t);
        }
    }

    /**
     * This is the extension point for implementations. Everything that needs to
     * be setup during initialization should be in here
     */
    protected abstract void initializeInternal();
}
