package eu.europeana.uim.gui.cp.server;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import eu.europeana.uim.gui.cp.client.services.ResourceService;
import eu.europeana.uim.gui.cp.shared.ParameterDTO;
import eu.europeana.uim.plugin.ingestion.IngestionPlugin;
import eu.europeana.uim.plugin.source.WorkflowStart;
import eu.europeana.uim.resource.ResourceEngine;
import eu.europeana.uim.storage.StorageEngine;
import eu.europeana.uim.store.Collection;
import eu.europeana.uim.store.Provider;
import eu.europeana.uim.workflow.Workflow;

/**
 * Orchestration service implementation.
 * 
 * @author Manuel Bernhardt <bernhardt.manuel@gmail.com>
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since May 11, 2011
 */
@SuppressWarnings({ "unchecked" })
public class ResourceServiceImpl extends AbstractOSGIRemoteServiceServlet implements
        ResourceService {
    private final static Logger log = Logger.getLogger(ResourceServiceImpl.class.getName());

    /**
     * Creates a new instance of this class.
     */
    public ResourceServiceImpl() {
        super();
    }

    @Override
    public List<ParameterDTO> getParameters(Serializable provider, Serializable collection,
            String workflow) {
        List<ParameterDTO> res = new ArrayList<ParameterDTO>();

        if (workflow != null) {
            try {
                getEngine().getRegistry().getWorkflow(workflow);
            } catch (Throwable t) {
                res.add(new ParameterDTO("no workflow lookup", new String[0]));
                return res;
            }

            Workflow<?, ?> w = getEngine().getRegistry().getWorkflow(workflow);
            if (w == null) {
                log.log(Level.WARNING, "Workflows are null!");
                res.add(new ParameterDTO("no workflows", new String[0]));
                return res;
            }

            List<String> params = new ArrayList<String>();
            try {
                WorkflowStart<?, ?> start = w.getStart();
                params.addAll(start.getParameters());
                for (IngestionPlugin<?, ?> i : w.getSteps()) {
                    params.addAll(i.getParameters());
                }
            } catch (Throwable t) {
                res.add(new ParameterDTO("no parameter retrieving", new String[0]));
                return res;
            }

            ResourceEngine resource = getEngine().getRegistry().getResourceEngine();
            if (resource == null) {
                log.log(Level.SEVERE, "Resource engine is null!");
                res.add(new ParameterDTO("no resource", new String[0]));
                return res;
            }

            try {
                resource.getGlobalResources(params);
            } catch (Throwable t) {
                res.add(new ParameterDTO("no globals", new String[0]));
                return res;
            }
            LinkedHashMap<String, List<String>> globalResources = resource.getGlobalResources(params);

            LinkedHashMap<String, List<String>> workflowResources = resource.getWorkflowResources(
                    w, params);
            try {
                resource.getWorkflowResources(w, params);
            } catch (Throwable t) {
                res.add(new ParameterDTO("no workflows", new String[0]));
                return res;
            }
            if (workflowResources != null && workflowResources.size() > 0) {
                for (Entry<String, List<String>> entry : workflowResources.entrySet()) {
                    if (entry.getValue() != null) {
                        globalResources.put(entry.getKey(), entry.getValue());
                    }
                }
            }

            if (provider != null) {
                Provider<Serializable> prov = null;
                try {
                    prov = ((StorageEngine<Serializable>)getEngine().getRegistry().getStorageEngine()).getProvider(provider);
                } catch (Throwable t) {
                    log.log(Level.WARNING, "Could not retrieve provider '" + provider + "'!", t);
                }

                if (prov != null) {
                    LinkedHashMap<String, List<String>> providerResources = resource.getProviderResources(
                            prov, params);
                    if (providerResources != null && providerResources.size() > 0) {
                        for (Entry<String, List<String>> entry : providerResources.entrySet()) {
                            if (entry.getValue() != null) {
                                globalResources.put(entry.getKey(), entry.getValue());
                            }
                        }
                    }
                }
            }

            if (collection != null) {
                Collection<Serializable> coll = null;
                try {
                    coll = ((StorageEngine<Serializable>)getEngine().getRegistry().getStorageEngine()).getCollection(collection);
                } catch (Throwable t) {
                    log.log(Level.WARNING, "Could not retrieve collection '" + collection + "'!", t);
                }

                if (coll != null) {
                    LinkedHashMap<String, List<String>> collectionResources = resource.getCollectionResources(
                            coll, params);
                    if (collectionResources != null && collectionResources.size() > 0) {
                        for (Entry<String, List<String>> entry : collectionResources.entrySet()) {
                            if (entry.getValue() != null) {
                                globalResources.put(entry.getKey(), entry.getValue());
                            }
                        }
                    }
                }
            }

            for (Entry<String, List<String>> entry : globalResources.entrySet()) {
                if (entry.getValue() != null) {
                    res.add(new ParameterDTO(entry.getKey(), entry.getValue().toArray(
                            new String[entry.getValue().size()])));
                } else {
                    res.add(new ParameterDTO(entry.getKey(), null));
                }
            }
        }
        return res;
    }

    @Override
    public Boolean setParameters(ParameterDTO parameter, Serializable provider,
            Serializable collection, String workflow) {
        Boolean res = true;

        LinkedHashMap<String, List<String>> values = new LinkedHashMap<String, List<String>>();
        values.put(parameter.getKey(),
                parameter.getValues() != null ? Arrays.asList(parameter.getValues()) : null);

        ResourceEngine resource = getEngine().getRegistry().getResourceEngine();
        if (resource == null) {
            log.log(Level.SEVERE, "Resource engine is null!");
            return res;
        }

        if (collection == null && provider == null && workflow != null) {
            Workflow<?, ?> wf = getEngine().getRegistry().getWorkflow(workflow);
            if (wf == null) {
                log.log(Level.WARNING, "Workflows are null!");
            } else {
                resource.setWorkflowResources(wf, values);
            }
        } else if (collection == null && provider != null && workflow != null) {
            Provider<Serializable> prov = null;
            try {
                prov = ((StorageEngine<Serializable>)getEngine().getRegistry().getStorageEngine()).getProvider(provider);
            } catch (Throwable t) {
                log.log(Level.WARNING, "Could not retrieve provider '" + provider + "'!", t);
            }

            if (prov != null) {
                resource.setProviderResources(prov, values);
            }
        } else if (collection != null && provider != null && workflow != null) {
            Collection<Serializable> coll = null;
            try {
                coll = ((StorageEngine<Serializable>)getEngine().getRegistry().getStorageEngine()).getCollection(collection);
            } catch (Throwable t) {
                log.log(Level.WARNING, "Could not retrieve collection '" + collection + "'!", t);
            }

            if (coll != null) {
                resource.setCollectionResources(coll, values);
            }
        } else {
            res = false;
        }

        return res;
    }

    @Override
    public List<String> getResourceFileNames() {
        List<String> fileNames = new ArrayList<String>();
        ResourceEngine resource = getEngine().getRegistry().getResourceEngine();
        File rootDirectory = resource.getResourceDirectory();
        if (rootDirectory != null && rootDirectory.exists() && rootDirectory.isDirectory()) {
            listFiles(rootDirectory, "", fileNames);
        }

        Collections.sort(fileNames);
        return fileNames;
    }

    private void listFiles(File directory, String prefix, List<String> filenames) {
        for (File file : directory.listFiles()) {
            if (!file.getName().startsWith(".")) {
                if (file.isDirectory()) {
                    listFiles(file, prefix + file.getName() + "/", filenames);
                } else {
                    filenames.add(prefix + file.getName());
                }
            }
        }
    }
}
