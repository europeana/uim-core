/* StorageUpdateListener.java - created on Feb 7, 2012, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.api;

import eu.europeana.uim.store.Collection;
import eu.europeana.uim.store.Provider;

/**
 * 
 * 
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @param <I> 
 * @since Feb 7, 2012
 */
public interface StorageUpdateListener<I> {
    
    /**
     * @return the identifier of the representing module
     */
    public String getIdentifier();

    /** Method which should be called by modules which change the collections
     * in this system.
     * 
     * @param modul the module which issued the call
     * @param collection the collection which has been updated in UIM
     */
    public void updateCollection(String modul, Collection<I> collection);

    /** Method which should be called by modules which change the providers
     * in this system.
     * 
     * @param modul the module which issued the call
     * @param provider the provider which has been updated in UIM
     */
    public void updateProvider(String modul, Provider<I> provider);
}
