/* BrowserTreeViewModel.java - created on Apr 29, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.gui.cp.client.management;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.TreeViewModel;

import eu.europeana.uim.gui.cp.client.services.RepositoryServiceAsync;
import eu.europeana.uim.gui.cp.shared.CollectionDTO;
import eu.europeana.uim.gui.cp.shared.ProviderDTO;
import eu.europeana.uim.gui.cp.shared.WorkflowDTO;

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

    private final RepositoryServiceAsync             repositoryService;
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
     * @param repositoryService
     * @param selectionModel
     */
    public TriggerTreeViewModel(final RepositoryServiceAsync repositoryService,
                                final MultiSelectionModel<BrowserObject> selectionModel) {
        this.repositoryService = repositoryService;
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
        repositoryService.getProviders(new AsyncCallback<List<ProviderDTO>>() {
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
                    String name = p.getCountry() == null ? "XX: " + p.getName() : p.getCountry() + ": " + p.getName();
                    providerList.add(new BrowserObject(name, p));
                }
                Collections.sort(providerList, new Comparator<BrowserObject>() {
                    @Override
                    public int compare(BrowserObject o1, BrowserObject o2) {
                        return o1.getName().compareTo(o2.getName());
                    }
                });

            }
        });
    }

    private void loadCollections(Serializable providerId,
            final ListDataProvider<BrowserObject> collectionsDataProvider) {
        repositoryService.getCollections(providerId, new AsyncCallback<List<CollectionDTO>>() {
            @Override
            public void onFailure(Throwable throwable) {
                throwable.printStackTrace();
                // TODO: panic
            }

            @Override
            public void onSuccess(List<CollectionDTO> collections) {
                List<BrowserObject> collectionList = collectionsDataProvider.getList();
                collectionList.clear();
//                if (collections.size() > 0) {
//                    CollectionDTO fakeCollection = new CollectionDTO();
//                    fakeCollection.setName(ALL_COLLECTIONS);
//                    collectionList.add(new BrowserObject(ALL_COLLECTIONS, fakeCollection));
//                }
                for (CollectionDTO collection : collections) {
                    collectionList.add(new BrowserObject(collection.getMnemonic() + ": " + collection.getName(), collection));
                }
                Collections.sort(collectionList, new Comparator<BrowserObject>() {
                    @Override
                    public int compare(BrowserObject o1, BrowserObject o2) {
                        return o1.getName().compareTo(o2.getName());
                    }
                });

            }
        });
    }

    private void loadWorkflows(final ListDataProvider<BrowserObject> workflowDataProvider) {
        repositoryService.getWorkflows(new AsyncCallback<List<WorkflowDTO>>() {
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
                Collections.sort(workflowList, new Comparator<BrowserObject>() {
                    @Override
                    public int compare(BrowserObject o1, BrowserObject o2) {
                        return o1.getName().compareTo(o2.getName());
                    }
                });

            }
        });
    }
}