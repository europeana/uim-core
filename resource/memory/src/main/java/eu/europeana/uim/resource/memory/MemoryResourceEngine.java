/* MemoryResourceEngine.java - created on May 9, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.resource.memory;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import eu.europeana.uim.EngineStatus;
import eu.europeana.uim.resource.ResourceEngine;
import eu.europeana.uim.store.Collection;
import eu.europeana.uim.store.Provider;
import eu.europeana.uim.workflow.Workflow;

/**
 * In-memory implementation of the resource engine. This does not persist data. NOT FOR PRODUCTION
 * USE !
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @author Rene Wiermer (rene.wiermer@kb.nl)
 * @since May 9, 2011
 */
public class MemoryResourceEngine implements ResourceEngine {
    private static final String                                              IDENTIFIER          = MemoryResourceEngine.class.getSimpleName();

    private static final String                                              DEFAULT_DATA_DIR    = System.getProperty("java.io.tmpdir") +
                                                                                                   File.separator +
                                                                                                   "uim-memorystorage";

    private final LinkedHashMap<String, List<String>>                        globalResources     = new LinkedHashMap<String, List<String>>();
    private final LinkedHashMap<String, LinkedHashMap<String, List<String>>> workflowResources   = new LinkedHashMap<String, LinkedHashMap<String, List<String>>>();
    private final LinkedHashMap<Object, LinkedHashMap<String, List<String>>> providerResources   = new LinkedHashMap<Object, LinkedHashMap<String, List<String>>>();
    private final LinkedHashMap<Object, LinkedHashMap<String, List<String>>> collectionResources = new LinkedHashMap<Object, LinkedHashMap<String, List<String>>>();

    private File                                                             rootResourceDir;
    private File                                                             rootWorkingDir;
    private File                                                             rootTmpDir;

    /**
     * Creates a new instance of this class.
     */
    public MemoryResourceEngine() {
        try {
            setRootPath(DEFAULT_DATA_DIR);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Could not setup data directory for resource engine " +
                                       DEFAULT_DATA_DIR, e);
        }
    }

    @Override
    public String getIdentifier() {
        return IDENTIFIER;
    }

    @Override
    public void setGlobalResources(LinkedHashMap<String, List<String>> resources) {
        if (resources == null) {
            globalResources.clear();
            return;
        }
        for (String key : resources.keySet()) {
            if (resources.get(key) == null) {
                // clean up. if the value is null, explicitely remove the key from the stored set.
                globalResources.remove(key);
            } else {
                globalResources.put(key, resources.get(key));
            }
        }
    }

    @Override
    public LinkedHashMap<String, List<String>> getGlobalResources(List<String> keys) {
        LinkedHashMap<String, List<String>> results = new LinkedHashMap<String, List<String>>();
        for (String key : keys) {
            List<String> values = globalResources.get(key);
            results.put(key, values);

        }
        return results;
    }

    @Override
    public void setWorkflowResources(Workflow<?, ?> workflow,
            LinkedHashMap<String, List<String>> resources) {
        if (resources == null) {
            // clean up and remove id entry from resources
            workflowResources.remove(workflow.getIdentifier());
            return;
        }

        LinkedHashMap<String, List<String>> workResources = workflowResources.get(workflow.getIdentifier());
        if (workResources == null) {
            workResources = new LinkedHashMap<String, List<String>>();
            workflowResources.put(workflow.getIdentifier(), workResources);
        }

        for (Entry<String, List<String>> entry : resources.entrySet()) {
            if (entry.getValue() == null) {
                // clean up. if the value is null, explicitely remove the key from the stored set.
                workResources.remove(entry.getKey());
            } else {
                workResources.put(entry.getKey(), entry.getValue());
            }
        }
    }

    @Override
    public LinkedHashMap<String, List<String>> getWorkflowResources(Workflow<?, ?> workflow,
            List<String> keys) {
        LinkedHashMap<String, List<String>> results = new LinkedHashMap<String, List<String>>();
        LinkedHashMap<String, List<String>> workflowMap = workflowResources.get(workflow.getIdentifier());

        for (String key : keys) {
            List<String> values = null;
            if (workflowMap != null) {
                values = workflowMap.get(key);
            }

            results.put(key, values);
        }
        return results;
    }

    @Override
    public void setProviderResources(Provider<?> id, LinkedHashMap<String, List<String>> resources) {
        if (resources == null) {
            // clean up and remove id entry from resources
            providerResources.remove(id.getId());
            return;
        }

        if (providerResources.get(id.getId()) == null) {
            providerResources.put(id.getId(), new LinkedHashMap<String, List<String>>());
        }

        LinkedHashMap<String, List<String>> provResources = providerResources.get(id.getId());

        // collectionResources.put(id.getId(), resources);
        for (String key : resources.keySet()) {
            if (resources.get(key) == null) {
                // clean up. if the value is null, explicitely remove the key from the stored set.
                provResources.remove(key);
            } else {
                provResources.put(key, resources.get(key));
            }
        }
    }

    @Override
    public LinkedHashMap<String, List<String>> getProviderResources(Provider<?> id,
            List<String> keys) {
        LinkedHashMap<String, List<String>> results = new LinkedHashMap<String, List<String>>();
        LinkedHashMap<String, List<String>> providerMap = providerResources.get(id.getId());

        for (String key : keys) {
            List<String> values = null;
            if (providerMap != null) {
                values = providerMap.get(key);
            }

            results.put(key, values);

        }
        return results;
    }

    @Override
    public void setCollectionResources(Collection<?> id,
            LinkedHashMap<String, List<String>> resources) {
        if (resources == null) {
            // clean up and remove id entry from resources
            collectionResources.remove(id.getId());
            return;
        }

        if (collectionResources.get(id.getId()) == null) {
            collectionResources.put(id.getId(), new LinkedHashMap<String, List<String>>());
        }

        LinkedHashMap<String, List<String>> collResources = collectionResources.get(id.getId());

        // collectionResources.put(id.getId(), resources);
        for (String key : resources.keySet()) {
            if (resources.get(key) == null) {
                // clean up. if the value is null, explicitely remove the key from the stored set.
                collResources.remove(key);
            } else {
                collResources.put(key, resources.get(key));
            }
        }
    }

    @Override
    public LinkedHashMap<String, List<String>> getCollectionResources(Collection<?> id,
            List<String> keys) {
        LinkedHashMap<String, List<String>> results = new LinkedHashMap<String, List<String>>();
        LinkedHashMap<String, List<String>> collectionMap = collectionResources.get(id.getId());

        for (String key : keys) {
            List<String> values = null;
            if (collectionMap != null) {
                values = collectionMap.get(key);
            }
            results.put(key, values);

        }
        return results;
    }

    @Override
    public void setConfiguration(Map<String, String> config) {
        // don't expect anything, just ignore;
    }

    @Override
    public Map<String, String> getConfiguration() {
        // not needed, return empty
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
    public File getResourceDirectory() {
        return rootResourceDir;

    }

    private File createDir(String rootPath, String suffix) throws FileNotFoundException {
        File directory = new File(rootPath + File.separator + suffix);

        if (!directory.exists() && !directory.mkdirs()) { throw new FileNotFoundException(
                "Directory " + directory.getAbsolutePath() + " not found and could not be created"); }

        if (!directory.isDirectory()) { throw new IllegalArgumentException(rootPath +
                                                                           " is not a directory"); }
        return directory;
    }

    /**
     * Sets the rootPath to the given value.
     * 
     * @param rootPath
     *            the rootPath to set
     * @throws FileNotFoundException
     */
    public void setRootPath(String rootPath) throws FileNotFoundException {
        rootResourceDir = createDir(rootPath, "resources");
        rootWorkingDir = createDir(rootPath, "work");
        rootTmpDir = createDir(rootPath, "rootPath, tmp");
    }

    @Override
    public File getWorkingDirectory() {
        return rootWorkingDir;
    }

    @Override
    public File getTemporaryDirectory() {
        return rootTmpDir;
    }
}
