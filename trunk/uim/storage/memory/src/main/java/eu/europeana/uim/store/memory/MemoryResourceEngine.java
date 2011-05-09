/* MemoryResourceEngine.java - created on May 9, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.store.memory;

import java.util.LinkedHashMap;
import java.util.List;

import eu.europeana.uim.api.ResourceEngine;
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



}
