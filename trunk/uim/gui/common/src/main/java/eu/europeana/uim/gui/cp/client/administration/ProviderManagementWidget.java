package eu.europeana.uim.gui.cp.client.administration;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

import eu.europeana.uim.gui.cp.client.IngestionWidget;
import eu.europeana.uim.gui.cp.client.services.RepositoryServiceAsync;
import eu.europeana.uim.gui.cp.shared.CollectionDTO;
import eu.europeana.uim.gui.cp.shared.DataSourceDTO;
import eu.europeana.uim.gui.cp.shared.ProviderDTO;

/**
 * Table view showing current exectuions.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Apr 27, 2011
 */
public class ProviderManagementWidget extends IngestionWidget {
    private static final String NEW_PROVIDER   = "New Provider";
    private static final String NEW_COLLECTION = "New Collection";

    /**
     * The UiBinder interface used by this example.
     */
    interface Binder extends UiBinder<Widget, ProviderManagementWidget> {
    }

    private final RepositoryServiceAsync repositoryService;

    private final List<ProviderDTO>      providers   = new ArrayList<ProviderDTO>();
    private final List<CollectionDTO>    collections = new ArrayList<CollectionDTO>();

    /**
     * Box with providers for selection
     */
    @UiField
    ListBox                              providerBox;
    /**
     * Box with collections for selection
     */
    @UiField
    ListBox                              collectionBox;

    @UiField(provided = true)
    ProviderForm                         providerForm;

    @UiField(provided = true)
    CollectionForm                       collectionForm;

    /**
     * Creates a new instance of this class.
     * 
     * @param repositoryService
     */
    public ProviderManagementWidget(RepositoryServiceAsync repositoryService) {
        super("Provider/Collection",
                "This view allows the administration of providers and collections!");
        this.repositoryService = repositoryService;
    }

    /**
     * Initialize this example.
     */
    @Override
    public Widget onInitialize() {
        providerForm = new ProviderForm(repositoryService, new UpdateListener() {
            @Override
            public void updated(DataSourceDTO dataSource) {
                updateProviders();
            }
        });
        providerForm.setProvider(new ProviderDTO());

        collectionForm = new CollectionForm(repositoryService, new UpdateListener() {
            @Override
            public void updated(DataSourceDTO dataSource) {
                updateCollections(providers.get(providerBox.getSelectedIndex() - 1));
            }
        });

        Binder uiBinder = GWT.create(Binder.class);
        Widget widget = uiBinder.createAndBindUi(this);

        updateProviders();

        providerBox.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                int selectedIndex = providerBox.getSelectedIndex();
                ProviderDTO provider;
                if (selectedIndex == 0) {
                    provider = new ProviderDTO();
                    providerForm.setProvider(provider);
                } else {
                    provider = providers.get(selectedIndex - 1);
                    providerForm.setProvider(provider);
                    updateCollections(provider);
                }
                CollectionDTO collection = new CollectionDTO();
                collection.setProvider(provider);
                collectionForm.setCollection(collection);
            }
        });

        collectionBox.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                int selectedIndex = collectionBox.getSelectedIndex();
                if (selectedIndex == 0) {
                    CollectionDTO collection = new CollectionDTO();
                    collection.setProvider(providers.get(providerBox.getSelectedIndex() - 1));
                    collectionForm.setCollection(collection);
                } else {
                    collectionForm.setCollection(collections.get(selectedIndex - 1));
                }
            }
        });

        return widget;
    }

    private void updateProviders() {
        repositoryService.getProviders(new AsyncCallback<List<ProviderDTO>>() {
            @Override
            public void onFailure(Throwable caught) {
                caught.printStackTrace();
            }

            @Override
            public void onSuccess(List<ProviderDTO> result) {
                providers.clear();
                providerBox.clear();
                providers.addAll(result);
                providerBox.addItem(NEW_PROVIDER);
                for (ProviderDTO provider : providers) {
                    providerBox.addItem(provider.getName());
                }
            }
        });
    }

    private void updateCollections(ProviderDTO provider) {
        repositoryService.getCollections(provider.getId(),
                new AsyncCallback<List<CollectionDTO>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        caught.printStackTrace();
                    }

                    @Override
                    public void onSuccess(List<CollectionDTO> result) {
                        collections.clear();
                        collectionBox.clear();
                        collections.addAll(result);
                        collectionBox.addItem(NEW_COLLECTION);
                        for (CollectionDTO collection : collections) {
                            collectionBox.addItem(collection.getName());
                        }
                    }
                });
    }

    @Override
    protected void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(ProviderManagementWidget.class, new RunAsyncCallback() {

            @Override
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            @Override
            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }
}
