package eu.europeana.uim.gui.gwt.client;

import java.util.List;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;

import eu.europeana.uim.gui.gwt.shared.Collection;
import eu.europeana.uim.gui.gwt.shared.Execution;
import eu.europeana.uim.gui.gwt.shared.Provider;
import eu.europeana.uim.gui.gwt.shared.Workflow;

/**
 * The panel making it possible to run new executions
 *
 * @author Manuel Bernhardt <bernhardt.manuel@gmail.com>
 */
public class ExecutionPanel extends FlowPanel {

    private static final String ALL_COLLECTIONS = "ALL";

    private OrchestrationServiceAsync orchestrationService = null;
    private final Application application;

    public ExecutionPanel(OrchestrationServiceAsync orchestrationServiceAsync, final Application application) {
        this.orchestrationService = orchestrationServiceAsync;
        this.application = application;

        // pick a workflow
        final Label workflowLabel = new Label("Step 1: Select a Workflow");
        final ListBox workflowList = new ListBox(false);
        add(workflowLabel);
        add(workflowList);
        loadWorkflows(workflowList);

        // pick a provider and collection
        final Label providerLabel = new Label("Step 2: Select a Provider");
        final ListBox providerList = new ListBox(false);
        final Label collectionLabel = new Label("Step 3: Select a Collection");
        final ListBox collectionList = new ListBox(false);
        final Button start = new Button("Go!");

        collectionList.setEnabled(false);
        start.setEnabled(false);

        add(providerLabel);
        add(providerList);
        add(collectionLabel);
        add(collectionList);
        add(start);
        loadProviders(providerList);

        // add change listener to provision collections on provider change
        providerList.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent changeEvent) {
                if (!collectionList.isEnabled()) {
                    collectionList.setEnabled(true);
                    start.setEnabled(true);
                }
                Long providerId = Long.parseLong(providerList.getValue(providerList.getSelectedIndex()));
                loadCollections(providerId, collectionList);
            }
        });

        start.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent clickEvent) {
                String selectedWorkflow = workflowList.getValue(workflowList.getSelectedIndex());
                String selectedDataSource = collectionList.getValue(collectionList.getSelectedIndex());
                if (selectedDataSource.equals(ALL_COLLECTIONS)) {
                    executeProvider(selectedWorkflow, providerList, application);
                } else {
                    executeCollection(selectedWorkflow, selectedDataSource, application);
                }

                // jump to the overview panel and clear this one
                application.selectOverviewTab();
                collectionList.clear();
                collectionList.setEnabled(false);
                start.setEnabled(false);
                providerList.setSelectedIndex(0);
                workflowList.setSelectedIndex(0);
            }
        });


    }

    private void executeCollection(String workflowId, String selectedDataSource, final Application application) {
        // start on collection
        Long collectionId = Long.parseLong(selectedDataSource);
        orchestrationService.startCollection(workflowId, collectionId, new AsyncCallback<Execution>() {
            @Override
            public void onFailure(Throwable throwable) {
                // TODO panic
                throwable.printStackTrace();
            }

            @Override
            public void onSuccess(Execution execution) {
                application.getOverview().addExecution(execution);
            }
        });
    }

    private void executeProvider(String workflowId, ListBox providerList, final Application application) {
        // start on provider
        Long providerId = Long.parseLong(providerList.getValue(providerList.getSelectedIndex()));
        orchestrationService.startProvider(workflowId, providerId, new AsyncCallback<Execution>() {
            @Override
            public void onFailure(Throwable throwable) {
                // TODO panic
                throwable.printStackTrace();
            }

            @Override
            public void onSuccess(Execution execution) {
                application.getOverview().addExecution(execution);
            }
        });
    }

    private void loadCollections(Long providerId, final ListBox collectionList) {
        orchestrationService.getCollections(providerId, new AsyncCallback<List<Collection>>() {
            @Override
            public void onFailure(Throwable throwable) {
                throwable.printStackTrace();
                // TODO: panic
            }

            @Override
            public void onSuccess(List<Collection> collections) {
                collectionList.clear();
                if (collections.size() > 0) {
                    // "all" option
                    collectionList.addItem("All collections", ALL_COLLECTIONS);
                }
                for (Collection collection : collections) {
                    collectionList.addItem(collection.getName(), collection.getId().toString());
                }
            }
        });
    }

    private void loadProviders(final ListBox providerList) {
        // load providers
        orchestrationService.getProviders(new AsyncCallback<List<Provider>>() {
            @Override
            public void onFailure(Throwable throwable) {
                throwable.printStackTrace();
                // TODO: panic
            }

            @Override
            public void onSuccess(List<Provider> providers) {
                providerList.clear();
                for (Provider p : providers) {
                    providerList.addItem(p.getName(), p.getId().toString());
                }
            }
        });
    }

    private void loadWorkflows(final ListBox workflowList) {
        orchestrationService.getWorkflows(new AsyncCallback<List<Workflow>>() {
            @Override
            public void onFailure(Throwable throwable) {
                throwable.printStackTrace();
                // TODO: panic
            }

            @Override
            public void onSuccess(List<Workflow> workflows) {
                for (Workflow w : workflows) {
                    workflowList.addItem(w.getName(), w.getName());
                }
            }
        });
    }

}
