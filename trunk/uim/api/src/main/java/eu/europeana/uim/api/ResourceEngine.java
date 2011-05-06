/* ResourceEngine.java - created on May 3, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.api;

import java.io.File;
import java.io.InputStream;

import org.apache.commons.configuration.Configuration;

/**
 * Interface for resource storage engine type to an identifier
 * 
 * @author Rene Wiermer (rene.wiermer@kb.nl)
 * @date May 3, 2011
 */
public interface ResourceEngine<I> {
    
    public String getIdentifier();
    
    public void addGlobalResource(String key,String value);
    public void updateGlobalResource(String key,String value);
    public void cleanGlobalResource(String key);
    public Configuration getGlobalResources();
    
    public void addProviderResource(I id,String key,String value);
    public void updateProviderResource(I id,String key,String value);
    public void truncateProviderResource(I id,String key);
    public Configuration getProviderSpecificResources(I providerId);
    
    public void addCollectionResource(I id,String key,String value);
    public void updateCollectionResource(I id,String key,String value);
    public void truncateCollectionResource(I id,String key);
    public Configuration getCollectionSpecificResources(I collectionId);
    
    public void addExecutionResource(I id,String key,String value);
    public void updateExecutionResource(I id,String key,String value);
    public void truncateExecutionResource(I id,String key); 
    public Configuration getExecutionSpecificResources(I executionId);
    
    /**
     * Returns a combined configuration in which the properties are overwritten in the following order:
     * 
     * 1. Global configuration
     * 2. Provider specific configuration
     * 3. Collection specific configuration
     * 4. Execution specific configuration
     * 
     * @param executionId
     * @param collectionId
     * @param providerId
     * @return
     */
    public Configuration getExecutionEffectiveResources(I executionId,I collectionId,I providerId);
    
    /**
     * @return
     */
    
    public File asFile(String value);
    
    public InputStream asInputStream(String value);
    
    //TODO: extend to support file up/load download, if necessary.
    

        
}
