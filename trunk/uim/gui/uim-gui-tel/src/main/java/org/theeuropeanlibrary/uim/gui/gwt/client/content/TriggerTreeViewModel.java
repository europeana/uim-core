/* BrowserTreeViewModel.java - created on Apr 29, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.uim.gui.gwt.client.content;

import java.util.List;

import org.theeuropeanlibrary.uim.gui.gwt.client.OrchestrationServiceAsync;
import org.theeuropeanlibrary.uim.gui.gwt.shared.CollectionDTO;
import org.theeuropeanlibrary.uim.gui.gwt.shared.ProviderDTO;
import org.theeuropeanlibrary.uim.gui.gwt.shared.WorkflowDTO;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.TreeViewModel;

/**
 * {@link TreeViewModel} implementation for {@link ExecutionTriggerWidget}.
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
    }

    @Override
    public <T> NodeInfo<?> getNodeInfo(T value) {
        if (value == null) {
            ListDataProvider<BrowserObject> providersDataProvider = new ListDataProvider<BrowserObject>();
            loadProviders(providersDataProvider);
            return new DefaultNodeInfo<BrowserObject>(providersDataProvider,
                    new BrowserObjectCell(), selectionModel, null);
        } else if (((BrowserObject)value).getWrappedObject() instanceof ProviderDTO) {
            ProviderDTO provider = (ProviderDTO)((BrowserObject)value).getWrappedObject();
            ListDataProvider<BrowserObject> collectionsDataProvider = new ListDataProvider<BrowserObject>();
            loadCollections(provider.getId(), collectionsDataProvider);
            return new DefaultNodeInfo<BrowserObject>(collectionsDataProvider,
                    new BrowserObjectCell(), selectionModel, null);
        } else if (((BrowserObject)value).getWrappedObject() instanceof CollectionDTO) {
            ListDataProvider<BrowserObject> workflowsDataProvider = new ListDataProvider<BrowserObject>();
            loadWorkflows(workflowsDataProvider);
            return new DefaultNodeInfo<BrowserObject>(workflowsDataProvider,
                    new BrowserObjectCell(), selectionModel, null);
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

    /**
     * Wrapped object.
     * 
     * @author Markus Muhr (markus.muhr@kb.nl)
     * @since May 6, 2011
     */
    public static class BrowserObject {
        private final String name;
        private final Object wrappedObject;

        /**
         * Creates a new instance of this class.
         * 
         * @param name
         * @param wrappedObject
         */
        public BrowserObject(String name, Object wrappedObject) {
            this.name = name;
            this.wrappedObject = wrappedObject;
        }

        /**
         * @return name
         */
        public String getName() {
            return name;
        }

        /**
         * @return wrapped object
         */
        public Object getWrappedObject() {
            return wrappedObject;
        }
    }

    /**
     * provides keys for browser objects
     */
    public static final ProvidesKey<BrowserObject> KEY_PROVIDER = new ProvidesKey<BrowserObject>() {
                                                                    @Override
                                                                    public Object getKey(
                                                                            BrowserObject item) {
                                                                        return item == null ? null
                                                                                : item.getName();
                                                                    }
                                                                };

    /**
     * The cell used to render categories.
     */
    private static class BrowserObjectCell extends AbstractCell<BrowserObject> {
        @Override
        public void render(Context context, BrowserObject value, SafeHtmlBuilder sb) {
            if (value != null) {
                SafeHtml html = SafeHtmlUtils.fromString(value.getName());
                sb.append(html);
            }
        }
    }
}