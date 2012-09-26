/* RepositoryInitializer.java - created on Feb 23, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.collections.guarded;

/**
 * Interface representing a guarded element
 * 
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @since Aug 22, 2011
 */
public interface Guarded {

    /**
     * @return the key for this guarded
     */
    @SuppressWarnings("rawtypes")
    public GuardedKey getGuardKey();

    /**
     * @param status
     * @param message
     */
    public void processed(int status, String message);

}
