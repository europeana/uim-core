package eu.europeana.uim.gui.cp.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.prefetch.RunAsyncCode;

import eu.europeana.uim.gui.cp.client.management.IngestionTriggerWidget;
import eu.europeana.uim.gui.cp.client.management.ResourceManagementWidget;
import eu.europeana.uim.gui.cp.client.monitoring.IngestionDetailWidget;
import eu.europeana.uim.gui.cp.client.monitoring.IngestionHistoryWidget;
import eu.europeana.uim.gui.cp.client.services.ExecutionService;
import eu.europeana.uim.gui.cp.client.services.ExecutionServiceAsync;
import eu.europeana.uim.gui.cp.client.services.RepositoryService;
import eu.europeana.uim.gui.cp.client.services.RepositoryServiceAsync;
import eu.europeana.uim.gui.cp.client.services.ResourceService;
import eu.europeana.uim.gui.cp.client.services.ResourceServiceAsync;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Apr 26, 2011
 */
public class IngestionControlPanel extends AbstractIngestionControlPanel implements EntryPoint {
    @Override
    public void onModuleLoad() {
        initialize();
    }

    @Override
    protected IngestionCustomization getDynamics() {
        return new EuropeanaCustomization();
    }

    @Override
    protected void addMenuEntries(SidebarMenu treeModel) {
        final RepositoryServiceAsync repositoryService = (RepositoryServiceAsync)GWT.create(RepositoryService.class);
        final ResourceServiceAsync resourceService = (ResourceServiceAsync)GWT.create(ResourceService.class);
        final ExecutionServiceAsync executionService = (ExecutionServiceAsync)GWT.create(ExecutionService.class);

        treeModel.addMenuEntry("Monitoring", new IngestionDetailWidget(executionService),
                RunAsyncCode.runAsyncCode(IngestionDetailWidget.class));
        treeModel.addMenuEntry("Monitoring", new IngestionHistoryWidget(executionService),
                RunAsyncCode.runAsyncCode(IngestionHistoryWidget.class));

        treeModel.addMenuEntry("Managing", new IngestionTriggerWidget(repositoryService,
                resourceService, executionService),
                RunAsyncCode.runAsyncCode(IngestionTriggerWidget.class));
        treeModel.addMenuEntry("Managing", new ResourceManagementWidget(repositoryService,
                resourceService), RunAsyncCode.runAsyncCode(ResourceManagementWidget.class));
    }
}