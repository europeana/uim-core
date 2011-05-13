/* MemoryResourceEngine.java - created on May 9, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.store.memory;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import eu.europeana.uim.api.ResourceEngine;
import eu.europeana.uim.api.StorageEngine.EngineStatus;
import eu.europeana.uim.store.Collection;
import eu.europeana.uim.store.Execution;
import eu.europeana.uim.store.Provider;

/**
 * 
 * 
 * @author Rene Wiermer (rene.wiermer@kb.nl)
 * @date May 9, 2011
 */
public class MemoryResourceEngine implements ResourceEngine<Long> {
    LinkedHashMap<String, List<String>> globalResources=new LinkedHashMap<String, List<String>>();
    LinkedHashMap<Long,LinkedHashMap<String, List<String>>> providerResources=new  LinkedHashMap<Long,LinkedHashMap<String, List<String>>>();
    LinkedHashMap<Long,LinkedHashMap<String, List<String>>> collectionResources=new  LinkedHashMap<Long,LinkedHashMap<String, List<String>>>();
    LinkedHashMap<Long,LinkedHashMap<String, List<String>>> executionResources=new  LinkedHashMap<Long,LinkedHashMap<String, List<String>>>();
    
    private static final String DEFAULT_DATA_DIR=System.getProperty("java.io.tmpdir");
    private String rootPath=DEFAULT_DATA_DIR;
    private File rootDir=new File(DEFAULT_DATA_DIR);
    
    /**
     * Creates a new instance of this class.
     */
    public MemoryResourceEngine() {
        
    }
    
    @Override
    public String getIdentifier() {
      return MemoryResourceEngine.class.getSimpleName();
    }

    @Override
    public void setGlobalResources(LinkedHashMap<String, List<String>> resources) {
      globalResources=resources;
    }

    @Override
    public LinkedHashMap<String, List<String>> getGlobalResources(List<String> keys) {
      LinkedHashMap<String, List<String>>  results=new LinkedHashMap<String, List<String>>() ;
      for (String key:keys) {
          List<String> values=globalResources.get(key);
          if (values !=null && values.size()>0) {
              results.put(key, values);
          } 
      }
      return results;
    }

    @Override
    public void setProviderResources(Provider<Long> id,
            LinkedHashMap<String, List<String>> resources) {
        providerResources.put(id.getId(), resources);
    }

    @Override
    public LinkedHashMap<String, List<String>> getProviderResources(Provider<Long> id,
            List<String> keys) {
        LinkedHashMap<String, List<String>>  results=new LinkedHashMap<String, List<String>>() ;
        LinkedHashMap<String, List<String>>  providerMap=providerResources.get(id.getId());
        if (providerMap==null) {
            return null;
        }
        
        for (String key:keys) {
            List<String> values=providerMap.get(key);
            if (values !=null && values.size()>0) {
                results.put(key, values);
            } 
        }
        return results;
      
    }

    @Override
    public void setCollectionResources(Collection<Long> id,
            LinkedHashMap<String, List<String>> resources) {
        collectionResources.put(id.getId(), resources);
    }

    @Override
    public LinkedHashMap<String, List<String>> getCollectionResources(Collection<Long> id,
            List<String> keys) {
        LinkedHashMap<String, List<String>>  results=new LinkedHashMap<String, List<String>>() ;
        LinkedHashMap<String, List<String>>  collectionMap=collectionResources.get(id.getId());
        if (collectionMap==null) {
            return null;
        }
        
        for (String key:keys) {
            List<String> values=collectionMap.get(key);
            if (values !=null && values.size()>0) {
                results.put(key, values);
            }
        }
        return results;
    }

    @Override
    public void setExecutionResources(Execution<Long> id,
            LinkedHashMap<String, List<String>> resources) {
           executionResources.put(id.getId(), resources);
    }

    @Override
    public LinkedHashMap<String, List<String>> getExecutionResources(Execution<Long> id,
            List<String> keys) {
        LinkedHashMap<String, List<String>>  results=new LinkedHashMap<String, List<String>>() ;
        LinkedHashMap<String, List<String>>  executionMap=executionResources.get(id.getId());
        if (executionMap==null) {
            return null;
        }
        
        for (String key:keys) {
            List<String> values=executionMap.get(key);
            if (values !=null && values.size()>0) {
                results.put(key, values);
            }
        }
        return results;
    }

    @Override
    public void setConfiguration(Map<String, String> config) {
        //don't expect anything, just ignore; 
    }

    @Override
    public Map<String, String> getConfiguration() {
        //not needed, return empty
        return new HashMap<String, String>();
    }

    @Override
    public void initialize() {
    }

    @Override
    public void shutdown() {
    }

    @Override
    public EngineStatus getStatus() {
        return EngineStatus.RUNNING;
    }

    @Override
    public void checkpoint() {
    }

    @Override
    public File getRootDirectory() {
     return rootDir;
        
    }

    /**
     * Sets the rootPath to the given value.
     * @param rootPath the rootPath to set
     * @throws FileNotFoundException 
     */
    public void setRootPath(String rootPath) throws FileNotFoundException {
        this.rootPath = rootPath;
     rootDir =new File(getRootPath());
        if (!rootDir.exists()&&!rootDir.mkdirs()) {
            throw new FileNotFoundException("Directory "+rootDir.getAbsolutePath()+" not found and could not be created");
        };
        if (!rootDir.isDirectory()) {
            throw new IllegalArgumentException(rootPath+" is not a directory");
        }
    }

    /**
     * Returns the rootPath.
     * @return the rootPath
     */
    public String getRootPath() {
        return rootPath;
    }
    
    



}
