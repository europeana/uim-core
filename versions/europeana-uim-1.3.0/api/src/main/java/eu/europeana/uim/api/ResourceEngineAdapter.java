/* ResourceEngineAdapter.java - created on May 3, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.api;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import eu.europeana.uim.store.Collection;
import eu.europeana.uim.store.Provider;
import eu.europeana.uim.workflow.Workflow;

/**
 * Dummy implementation of the ResourceEngine
 * 
 * @author Rene Wiermer (rene.wiermer@kb.nl)
 * @date May 3, 2011
 */
public class ResourceEngineAdapter implements ResourceEngine {
    @Override
    public String getIdentifier() {
        return ResourceEngineAdapter.class.getSimpleName();
    }

    @Override
    public void setGlobalResources(LinkedHashMap<String, List<String>> resources) {
    }

    @Override
    public void setWorkflowResources(Workflow workflow,
            LinkedHashMap<String, List<String>> resources) {
    }

    @Override
    public void setProviderResources(Provider<?> id, LinkedHashMap<String, List<String>> resources) {
    }

    @Override
    public void setCollectionResources(Collection<?> id,
            LinkedHashMap<String, List<String>> resources) {
    }

    @Override
    public LinkedHashMap<String, List<String>> getGlobalResources(List<String> keys) {
        return new LinkedHashMap<String, List<String>>();
    }

    @Override
    public LinkedHashMap<String, List<String>> getWorkflowResources(Workflow workflow,
            List<String> keys) {
        return new LinkedHashMap<String, List<String>>();
    }

    @Override
    public LinkedHashMap<String, List<String>> getProviderResources(Provider<?> id,
            List<String> keys) {
        return new LinkedHashMap<String, List<String>>();
    }

    @Override
    public LinkedHashMap<String, List<String>> getCollectionResources(Collection<?> id,
            List<String> keys) {
        return new LinkedHashMap<String, List<String>>();
    }

    @Override
    public void setConfiguration(Map<String, String> config) {

    }

    @Override
    public Map<String, String> getConfiguration() {
        return new HashMap<String, String>();
    }

    @Override
    public void initialize() {

    }

    @Override
    public void shutdown() {

    }

    @Override
    public void checkpoint() {

    }

    @Override
    public EngineStatus getStatus() {
        return EngineStatus.RUNNING;
    }

    @Override
    public File getResourceDirectory() {
        return null;
    }

    @Override
    public File getWorkingDirectory() {
        return null;
    }

    @Override
    public File getTemporaryDirectory() {
        return null;
    }
}
