/* BlockingInitializer.java - created on Mar 2, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.common;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * 
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @since Mar 2, 2011
 */
public abstract class BlockingInitializer implements Runnable {
    private static final Logger log                = Logger.getLogger(BlockingInitializer.class.getName());

    /**
     * BlockingInitializer STATUS_NEW object created
     */
    public static final int     STATUS_NEW         = 0;

    /**
     * BlockingInitializer STATUS_BOOTING object is being initialized
     */
    public static final int     STATUS_BOOTING     = 1;

    /**
     * BlockingInitializer STATUS_FAILED Initialization failed
     */
    public static final int     STATUS_FAILED      = 97;
    /**
     * BlockingInitializer STATUS_INITIALIZED Initialization successful
     * */
    public static final int     STATUS_INITIALIZED = 98;
    /** BlockingInitializer STATUS_SHUTDOWN Initializer has ended */
    public static final int     STATUS_SHUTDOWN    = 99;

    /**
     * BlockingInitializer status current status of the initializer
     */
    protected int               status             = STATUS_NEW;

    protected RuntimeException  exception          = null;

    /**
     * Creates a new instance of this class.
     */
    public BlockingInitializer() {
    }

    /**
     * Start the guarded initialization in a new thread and wait till the result for that is clear.
     * 
     * @param cl
     *            the class loader relevant for this context
     */
    public final synchronized void initialize(ClassLoader cl) {
        if (status >= STATUS_FAILED) return;

        // if status is NEW or BOOTING
        // we start a new initialization process.
        Thread thread = new Thread(this);
        thread.setContextClassLoader(cl);
        thread.start();

        while (status < STATUS_FAILED) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
        }

        if (exception != null) { throw exception; }
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
        } catch (RuntimeException t) {
            log.log(Level.SEVERE, "Failed to initialize with classloader:" +
                                  Thread.currentThread().getContextClassLoader(), t);
            status = STATUS_FAILED;
            exception = t;
        }
    }

    /**
     * This is the extension point for implementations. Everything that needs to be setup during
     * initialization should be in here
     * 
     */
    protected abstract void initializeInternal();

}
