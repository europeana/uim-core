/* RegistryUtil.java - created on Aug 11, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.util;

import eu.europeana.uim.api.Registry;
import eu.europeana.uim.api.StorageEngine;
import eu.europeana.uim.workflow.Workflow;

/**
 * 
 * 
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @since Aug 11, 2011
 */
public class RegistryUtil {

    public static Workflow getWorkflow(Registry registry, String identifier, long maxwait) {
        Workflow workflow = registry.getWorkflow(identifier);
        
        long waited = 0;
        while (workflow == null && maxwait > waited) {
            try {
                Thread.sleep(100);
                waited += 100;
            } catch (InterruptedException e) {
            }

            workflow = registry.getWorkflow(identifier);
        }
        return workflow;
    }

    
    public static <I> StorageEngine<I> getStorage (Registry registry, String identifier, long maxwait) {
        StorageEngine<I> storage = (StorageEngine<I>)registry.getStorageEngine(identifier);
        long waited = 0;
        while (storage == null && maxwait > waited) {
            try {
                Thread.sleep(100);
                waited += 100;
            } catch (InterruptedException e) {
            }
            
            storage = (StorageEngine<I>)registry.getStorageEngine(identifier);
        }
        return storage;
    }
    

}
