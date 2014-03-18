package eu.europeana.uim.gui.cp.server;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import eu.europeana.uim.api.IngestionPlugin;
import eu.europeana.uim.api.ResourceEngine;
import eu.europeana.uim.gui.cp.client.services.ResourceService;
import eu.europeana.uim.gui.cp.shared.ParameterDTO;
import eu.europeana.uim.store.bean.CollectionBean;
import eu.europeana.uim.store.bean.ProviderBean;
import eu.europeana.uim.workflow.Workflow;
import eu.europeana.uim.workflow.WorkflowStart;

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
    /**
     * Creates a new instance of this class.
     */
    public ResourceServiceImpl() {
        super();
    }

    @Override
    public List<ParameterDTO> getParameters(Long provider, Long collection, String workflow) {
        List<ParameterDTO> res = new ArrayList<ParameterDTO>();
        if (workflow != null) {
            Workflow w = getEngine().getRegistry().getWorkflow(workflow);
            if (w == null) { throw new RuntimeException("Error: cannot find workflow " + workflow); }

            WorkflowStart start = w.getStart();

            List<String> params = new ArrayList<String>();
            params.addAll(start.getParameters());
            for (IngestionPlugin i : w.getSteps()) {
                params.addAll(i.getParameters());
            }

            ResourceEngine<Long> resource = (ResourceEngine<Long>)getEngine().getRegistry().getResourceEngine();
            LinkedHashMap<String, List<String>> globalResources = resource.getGlobalResources(params);

            if (collection != null) {
                LinkedHashMap<String, List<String>> collectionResources = resource.getCollectionResources(
                        new CollectionBean<Long>(collection, new ProviderBean<Long>(provider)),
                        params);
                if (collectionResources != null && collectionResources.size() > 0) {
                    for (Entry<String, List<String>> entry : collectionResources.entrySet()) {
                        if (entry.getValue() != null) {
                            globalResources.put(entry.getKey(), entry.getValue());
                        }
                    }
                }
            }
            if (provider != null) {
                LinkedHashMap<String, List<String>> providerResources = resource.getProviderResources(
                        new ProviderBean<Long>(provider), params);
                if (providerResources != null && providerResources.size() > 0) {
                    for (Entry<String, List<String>> entry : providerResources.entrySet()) {
                        if (entry.getValue() != null) {
                            globalResources.put(entry.getKey(), entry.getValue());
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
    public Boolean setParameters(ParameterDTO parameter, Long provider, Long collection,
            String workflow) {
        Boolean res = true;

        LinkedHashMap<String, List<String>> values = new LinkedHashMap<String, List<String>>();
        values.put(parameter.getKey(),
                parameter.getValues() != null ? Arrays.asList(parameter.getValues()) : null);
        ResourceEngine<Long> resource = (ResourceEngine<Long>)getEngine().getRegistry().getResourceEngine();

        if (provider == null && collection == null && workflow != null) {
            resource.setGlobalResources(values);
        } else if (provider != null && collection != null) {
            resource.setCollectionResources(new CollectionBean<Long>(collection,
                    new ProviderBean<Long>(provider)), values);
        } else if (provider != null && collection == null) {
            resource.setProviderResources(new ProviderBean<Long>(provider), values);
        } else {
            res = false;
        }

        return res;
    }

    @Override
    public List<String> getResourceFileNames() {
        List<String> fileNames = new ArrayList<String>();
        ResourceEngine<Long> resource = (ResourceEngine<Long>)getEngine().getRegistry().getResourceEngine();
        File rootDirectory = resource.getResourceDirectory();
        if (rootDirectory != null && rootDirectory.exists() && rootDirectory.isDirectory()) {
            for (File file : rootDirectory.listFiles()) {
                fileNames.add(file.getName());
            }
        }
        return fileNames;
    }
}
