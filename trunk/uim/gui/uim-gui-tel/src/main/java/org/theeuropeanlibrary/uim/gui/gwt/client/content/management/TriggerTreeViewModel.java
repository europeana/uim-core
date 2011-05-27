/* BrowserTreeViewModel.java - created on Apr 29, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.uim.gui.gwt.client.content.management;

import java.util.List;

import org.theeuropeanlibrary.uim.gui.gwt.client.OrchestrationServiceAsync;
import org.theeuropeanlibrary.uim.gui.gwt.shared.CollectionDTO;
import org.theeuropeanlibrary.uim.gui.gwt.shared.ProviderDTO;
import org.theeuropeanlibrary.uim.gui.gwt.shared.WorkflowDTO;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.TreeViewModel;

/**
 * {@link TreeViewModel} implementation for {@link IngestionTriggerWidget}.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Apr 29, 2011
 */
public class TriggerTreeViewModel implements TreeViewModel {
    /**
     * all collections used for a specific provider
     */
    public static final String                       ALL_COLLECTIONS = "All Collections";

    private final OrchestrationServiceAsync          orchestrationServ;
    private final MultiSelectionModel<BrowserObject> selectionModel;
    private final BrowserObjectCell                  browserObjectCell;

    /**
     * provides keys for browser objects
     */
    public static final ProvidesKey<BrowserObject>   KEY_PROVIDER    = new ProvidesKey<BrowserObject>() {
                                                                         @Override
                                                                         public Object getKey(
                                                                                 BrowserObject item) {
                                                                             return item == null
                                                                                     ? null
                                                                                     : item.getName();
                                                                         }
                                                                     };

    /**
     * Creates a new instance of this class.
     * 
     * @param orchestrationServ
     * @param selectionModel
     */
    public TriggerTreeViewModel(final OrchestrationServiceAsync orchestrationServ,
                                final MultiSelectionModel<BrowserObject> selectionModel) {
        this.orchestrationServ = orchestrationServ;
        this.selectionModel = selectionModel;
        browserObjectCell = new BrowserObjectCell();
    }

    @Override
    public <T> NodeInfo<?> getNodeInfo(T value) {
        if (value == null) {
            ListDataProvider<BrowserObject> providersDataProvider = new ListDataProvider<BrowserObject>();
            loadProviders(providersDataProvider);
            return new DefaultNodeInfo<BrowserObject>(providersDataProvider, browserObjectCell,
                    selectionModel, null);
        } else if (((BrowserObject)value).getWrappedObject() instanceof ProviderDTO) {
            ProviderDTO provider = (ProviderDTO)((BrowserObject)value).getWrappedObject();
            ListDataProvider<BrowserObject> collectionsDataProvider = new ListDataProvider<BrowserObject>();
            loadCollections(provider.getId(), collectionsDataProvider);
            return new DefaultNodeInfo<BrowserObject>(collectionsDataProvider, browserObjectCell,
                    selectionModel, null);
        } else if (((BrowserObject)value).getWrappedObject() instanceof CollectionDTO) {
            ListDataProvider<BrowserObject> workflowsDataProvider = new ListDataProvider<BrowserObject>();
            loadWorkflows(workflowsDataProvider);
            return new DefaultNodeInfo<BrowserObject>(workflowsDataProvider, browserObjectCell,
                    selectionModel, null);
        }

        // Unhandled type.
        String type = value.getClass().getName();
        throw new IllegalArgumentException("Unsupported object type: " + type);
    }

    @Override
    public boolean isLeaf(Object value) {
        return ((BrowserObject)value).getWrappedObject() instanceof WorkflowDTO;
    }

    private void loadProviders(final ListDataProvider<BrowserObject> providersDataProvider) {
        orchestrationServ.getProviders(new AsyncCallback<List<ProviderDTO>>() {
            @Override
            public void onFailure(Throwable throwable) {
                throwable.printStackTrace();
                // TODO: panic
            }

            @Override
            public void onSuccess(List<ProviderDTO> providers) {
                List<BrowserObject> providerList = providersDataProvider.getList();
                providerList.clear();
                for (ProviderDTO p : providers) {
                    providerList.add(new BrowserObject(p.getName(), p));
                }
            }
        });
    }

    private void loadCollections(Long providerId,
            final ListDataProvider<BrowserObject> collectionsDataProvider) {
        orchestrationServ.getCollections(providerId, new AsyncCallback<List<CollectionDTO>>() {
            @Override
            public void onFailure(Throwable throwable) {
                throwable.printStackTrace();
                // TODO: panic
            }

            @Override
            public void onSuccess(List<CollectionDTO> collections) {
                List<BrowserObject> collectionList = collectionsDataProvider.getList();
                collectionList.clear();
                if (collections.size() > 0) {
                    CollectionDTO fakeCollection = new CollectionDTO();
                    fakeCollection.setName(ALL_COLLECTIONS);
                    collectionList.add(new BrowserObject(ALL_COLLECTIONS, fakeCollection));
                }
                for (CollectionDTO collection : collections) {
                    collectionList.add(new BrowserObject(collection.getName(), collection));
                }
            }
        });
    }

    private void loadWorkflows(final ListDataProvider<BrowserObject> workflowDataProvider) {
        orchestrationServ.getWorkflows(new AsyncCallback<List<WorkflowDTO>>() {
            @Override
            public void onFailure(Throwable throwable) {
                throwable.printStackTrace();
                // TODO: panic
            }

            @Override
            public void onSuccess(List<WorkflowDTO> workflows) {
                List<BrowserObject> workflowList = workflowDataProvider.getList();
                workflowList.clear();
                for (WorkflowDTO w : workflows) {
                    workflowList.add(new BrowserObject(w.getName(), w));
                }
            }
        });
    }
}