/* ResourceEngine.java - created on May 3, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.api;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import eu.europeana.uim.api.StorageEngine.EngineStatus;
import eu.europeana.uim.store.Collection;
import eu.europeana.uim.store.Execution;
import eu.europeana.uim.store.Provider;

/**
 * Interface for resource storage engine type to an identifier
 * 
 * @author Rene Wiermer (rene.wiermer@kb.nl)
 * @date May 3, 2011
 */
public interface ResourceEngine<I> {
    
    public String getIdentifier();
    
    /**
     * @param config
     */
    public void setConfiguration(Map<String, String> config);

    /**
     * @return configuration
     */
    public Map<String, String> getConfiguration();

    /**
     * Initializes engine by for example opening database connection.
     */
    public void initialize();

    /**
     * Shutdown the engine and its connected components like connection to database.
     */
    public void shutdown();
    
    /**
     * Saves all entries 
     */
    public void checkpoint();
    
    /**
     * @return the current running status of the engine
     */
    public EngineStatus getStatus();
    
//    public Set<String> getAvailableResourceKeys();
    
    public void setGlobalResources(LinkedHashMap<String, List<String>> resources);
    public LinkedHashMap<String, List<String>> getGlobalResources(List<String> keys);

    public void setProviderResources(Provider<I> id, LinkedHashMap<String, List<String>> resources);
    public LinkedHashMap<String,List<String>> getProviderResources(Provider<I> id, List<String> keys);
    
    public void setCollectionResources(Collection<I> id, LinkedHashMap<String, List<String>> resources);
    public LinkedHashMap<String, List<String>> getCollectionResources(Collection<I> id, List<String> keys);
    
    public void setExecutionResources(Execution<I> id, LinkedHashMap<String, List<String>> resources);
    public LinkedHashMap<String, List<String>> getExecutionResources(Execution<I> id, List<String> keys);
//
//    public Map<String, List<String>> getGlobalResouresForKeys(Set<String> keys);
//    public Map<String, List<String>> getLocalResoures(UimEntity<I> id,Set<String> keys);



 
    
//    
//    public void addGlobalResource(String key,String value);
//    public void clearGlobalResource(String key);
//    public Configuration getGlobalResources(Set<String> keys);
//    
//    public void addProviderResource(I id,String key,String value);
//    public void clearProviderResource(I id,String key);
//    public Configuration getProviderSpecificResources(I providerId,Set<String> keys);
//    
//    public void addCollectionResource(I id,String key,String value);
//    public void clearCollectionResource(I id,String key);
//    public Configuration getCollectionSpecificResources(I collectionId,Set<String> keys);
//    
//    public void addExecutionResource(I id,String key,String value);
//    public void clearExecutionResource(I id,String key); 
//    public Configuration getExecutionSpecificResources(I executionId,Set<String> keys);
    
//    /**
//     * Returns a combined configuration in which the properties are overwritten in the following order:
//     * 
//     * 1. Global configuration
//     * 2. Provider specific configuration
//     * 3. Collection specific configuration
//     * 4. Execution specific configuration
//     * 
//     * @param executionId
//     * @return
//     */
//    public Configuration getExecutionEffectiveResources(Execution<I> executionId);
//    
//    /**
//     * @return
//     */
    
//    public File asFile(String value);
//    
//    public InputStream asInputStream(String value);
    
    //TODO: extend to support file up/load download, if necessary.
    

        
}
