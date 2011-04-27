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
    private static final Logger  log                = Logger.getLogger(BlockingInitializer.class.getName());

    public static final int      STATUS_NEW         = 0;
    public static final int      STATUS_BOOTING     = 1;

    public static final int      STATUS_FAILED      = 97;
    public static final int      STATUS_INITIALIZED = 98;
    public static final int      STATUS_SHUTDOWN    = 99;

    protected int                  status             = 0;

    public BlockingInitializer() {
    }
    
    public final synchronized void initialize(ClassLoader cl) {
        if (status >= STATUS_FAILED) return;

        // if status is REGISTERED, STOPPED, or FAILED
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
    }
    
    /**
     * @return status information
     */
    public synchronized int getStatus() {
        return status;
    }


    public final void run() {
        try {
            status = STATUS_BOOTING;
            initialize();
            status = STATUS_INITIALIZED;
        } catch (Throwable t) {
            log.log(Level.SEVERE, "Failed to initialize with classloader:" + Thread.currentThread().getContextClassLoader(), t);
            status = STATUS_FAILED;
        }
    }
    
    protected abstract void initialize();

}
