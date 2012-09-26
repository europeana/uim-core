/* IngestionControlPanel.java - created on Jun 3, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.gui.cp.basic.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.prefetch.RunAsyncCode;

import eu.europeana.uim.gui.cp.client.AbstractIngestionControlPanel;
import eu.europeana.uim.gui.cp.client.IngestionCustomization;
import eu.europeana.uim.gui.cp.client.SidebarMenu;
import eu.europeana.uim.gui.cp.client.administration.ProviderManagementWidget;
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
 * The European Library specific control panel for ingestion workflows.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Jun 3, 2011
 */
public class UimIngestionControlPanel extends AbstractIngestionControlPanel implements EntryPoint {
    @Override
    public void onModuleLoad() {
        initialize();
    }

    @Override
    protected IngestionCustomization getDynamics() {
        return new UimCustomization();
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

        treeModel.addMenuEntry("Managing", new ProviderManagementWidget(repositoryService),
                RunAsyncCode.runAsyncCode(ProviderManagementWidget.class));
        treeModel.addMenuEntry("Managing", new ResourceManagementWidget(repositoryService,
                resourceService), RunAsyncCode.runAsyncCode(ResourceManagementWidget.class));
        treeModel.addMenuEntry("Managing", new IngestionTriggerWidget(repositoryService,
                resourceService, executionService),
                RunAsyncCode.runAsyncCode(IngestionTriggerWidget.class));
    }
}
