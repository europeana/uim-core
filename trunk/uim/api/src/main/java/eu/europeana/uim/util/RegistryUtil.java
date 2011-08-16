
 /**
 * Licensed under the EUPL, Version 1.1 or - as soon they
 * will be approved by the European Commission - subsequent
 * versions of the EUPL (the "Licence");
 * you may not use this work except in compliance with the
 * Licence.
 * You may obtain a copy of the Licence at:
 *
 * http://ec.europa.eu/idabc/eupl
 *
 * Unless required by applicable law or agreed to in
 * writing, software distributed under the Licence is
 * distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied.
 * See the Licence for the specific language governing
 * permissions and limitations under the Licence.
 */

package eu.europeana.uim.util;

import eu.europeana.uim.api.Registry;
import eu.europeana.uim.api.StorageEngine;
import eu.europeana.uim.workflow.Workflow;

/**
 * Util methods for getting objects from the registry
 * 
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @since Aug 11, 2011
 */
public class RegistryUtil {

    /**
     * Tries to get a workflow from the registry
     * 
     * @param registry
     * @param identifier
     * @param maxwait
     * @return the workflow, null if not found
     */
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

    
    /**
     * Tries to get the storage engine
     * @param <I>
     * @param registry
     * @param identifier
     * @param maxwait
     * @return the storage engine, null if not found
     */
    @SuppressWarnings("unchecked")
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
