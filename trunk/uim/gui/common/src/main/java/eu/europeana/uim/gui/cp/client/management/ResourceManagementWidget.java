package eu.europeana.uim.gui.cp.client.management;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellBrowser;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.view.client.SimpleKeyProvider;

import eu.europeana.uim.gui.cp.client.IngestionWidget;
import eu.europeana.uim.gui.cp.client.services.RepositoryServiceAsync;
import eu.europeana.uim.gui.cp.client.services.ResourceServiceAsync;
import eu.europeana.uim.gui.cp.shared.CollectionDTO;
import eu.europeana.uim.gui.cp.shared.ParameterDTO;
import eu.europeana.uim.gui.cp.shared.ProviderDTO;
import eu.europeana.uim.gui.cp.shared.WorkflowDTO;

/**
 * Triggers execution.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Apr 27, 2011
 */
public class ResourceManagementWidget extends IngestionWidget {
    /**
     * The UiBinder interface used by this example.
     */
    interface Binder extends UiBinder<Widget, ResourceManagementWidget> {
    }

    private final RepositoryServiceAsync repositoryService;
    private final ResourceServiceAsync   resourceService;

    /** CellBrowser cellBrowser */
    @UiField(provided = true)
    public CellBrowser                   cellBrowser;

    /** CellTable<ParameterDTO> cellTable */
    @UiField(provided = true)
    public CellTable<ParameterDTO>       cellTable;

    private ProviderDTO                  provider;
    private CollectionDTO                collection;
    private WorkflowDTO                  workflow;

    private final List<ParameterDTO>     activeParameters = new ArrayList<ParameterDTO>();

    /**
     * Creates a new instance of this class.
     * 
     * @param repositoryService
     * @param resourceService
     */
    public ResourceManagementWidget(RepositoryServiceAsync repositoryService,
                                    ResourceServiceAsync resourceService) {
        super("Manage Resources", "This view allows to manage resources for known plugins!");
        this.repositoryService = repositoryService;
        this.resourceService = resourceService;
    }

    /**
     * Initialize this example.
     */
    @Override
    public Widget onInitialize() {
        final MultiSelectionModel<BrowserObject> selectionModelBrowser = new MultiSelectionModel<BrowserObject>(
                ResourceTreeViewModel.KEY_PROVIDER);
        selectionModelBrowser.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                Set<BrowserObject> vals = selectionModelBrowser.getSelectedSet();
                for (BrowserObject val : vals) {
                    if (val.getWrappedObject() instanceof WorkflowDTO) {
                        workflow = (WorkflowDTO)val.getWrappedObject();
                        provider = null;
                        collection = null;
                    } else if (val.getWrappedObject() instanceof ProviderDTO) {
                        provider = (ProviderDTO)val.getWrappedObject();
                        collection = null;
                    } else if (val.getWrappedObject() instanceof CollectionDTO) {
                        collection = (CollectionDTO)val.getWrappedObject();
                    }
                    updateParameters();
                }
            }
        });

        ResourceTreeViewModel browserTreeViewModel = new ResourceTreeViewModel(repositoryService,
                selectionModelBrowser);
        cellBrowser = new CellBrowser(browserTreeViewModel, null);
        cellBrowser.setAnimationEnabled(true);
        // cellBrowser.setSize("300px", "350px");
        cellBrowser.setSize("60%", "100%");


        cellTable = new CellTable<ParameterDTO>(Integer.MAX_VALUE, new SimpleKeyProvider<ParameterDTO>());
        cellTable.setWidth("40%", true);
        cellTable.setHeight("30px");

        final ListDataProvider<ParameterDTO> dataProvider = new ListDataProvider<ParameterDTO>();
        dataProvider.setList(activeParameters);
        dataProvider.addDataDisplay(cellTable);

        ListHandler<ParameterDTO> sortHandler = new ListHandler<ParameterDTO>(
                new ListDataProvider<ParameterDTO>().getList());
        cellTable.addColumnSortHandler(sortHandler);

        final SelectionModel<ParameterDTO> selectionModelTable = new MultiSelectionModel<ParameterDTO>(
                new SimpleKeyProvider<ParameterDTO>());
        cellTable.setSelectionModel(selectionModelTable,
                DefaultSelectionEventManager.<ParameterDTO> createCheckboxManager());

        initTableColumns(selectionModelTable, sortHandler);

        Widget widget = postInitialize();

        return widget;
    }

    /**
     * This method provides extra functionality for components that are meant to extend this one
     * 
     * @return widget
     */
    public Widget postInitialize() {
        Binder uiBinder = GWT.create(Binder.class);
        Widget widget = uiBinder.createAndBindUi(this);
        return widget;
    }

    /**
     * Retrieve parameters for given settings.
     */
    public void updateParameters() {
        resourceService.getParameters(provider != null ? provider.getId() : null,
                collection != null ? collection.getId() : null,
                workflow != null ? workflow.getIdentifier() : null,
                new AsyncCallback<List<ParameterDTO>>() {
                    @Override
                    public void onFailure(Throwable throwable) {
                        throwable.printStackTrace();
                    }

                    @Override
                    public void onSuccess(List<ParameterDTO> parameters) {
                        activeParameters.clear();
                        activeParameters.addAll(parameters);
                        cellTable.setRowData(0, activeParameters);
                        cellTable.setRowCount(activeParameters.size());
                        cellTable.setHeight((30 + 20 * parameters.size()) + "px");
                    }
                });
    }

    @Override
    protected void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(ResourceManagementWidget.class, new RunAsyncCallback() {
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

    /**
     * Add the columns to the table.
     */
    private void initTableColumns(final SelectionModel<ParameterDTO> selectionModel,
            ListHandler<ParameterDTO> sortHandler) {
        // Key
        Column<ParameterDTO, String> keyColumn = new Column<ParameterDTO, String>(new TextCell()) {
            @Override
            public String getValue(ParameterDTO object) {
                return object.getKey();
            }
        };
        keyColumn.setSortable(true);
        sortHandler.setComparator(keyColumn, new Comparator<ParameterDTO>() {
            @Override
            public int compare(ParameterDTO o1, ParameterDTO o2) {
                return o1.getKey().compareTo(o2.getKey());
            }
        });
        cellTable.addColumn(keyColumn, "Resource Name");
        cellTable.setColumnWidth(keyColumn, 40, Unit.PCT);

        // Value
        Column<ParameterDTO, String> valueColumn = new Column<ParameterDTO, String>(new TextCell()) {
            @Override
            public String getValue(ParameterDTO object) {
                StringBuilder builder = new StringBuilder();
                if (object.getValues() != null) {
                    for (int i = 0; i < object.getValues().length; i++) {
                        builder.append(object.getValues()[i]);
                        if (i < object.getValues().length - 1) {
                            builder.append("|");
                        }
                    }
                }
                return builder.toString();
            }
        };
        valueColumn.setSortable(true);
        cellTable.addColumn(valueColumn, "Resource Value");
        cellTable.setColumnWidth(valueColumn, 40, Unit.PCT);

        // Update Button
        Column<ParameterDTO, ParameterDTO> updateColumn = new Column<ParameterDTO, ParameterDTO>(
                new ActionCell<ParameterDTO>("Value...", new ActionCell.Delegate<ParameterDTO>() {
                    @Override
                    public void execute(ParameterDTO parameter) {
                        ResourceSettingCallback callback = new ResourceSettingCallbackImplementation();
                        final DialogBox updateBox = new SimpleResourceDialogBox(parameter, callback);
                        updateBox.show();
                    }
                })) {
            @Override
            public ParameterDTO getValue(ParameterDTO object) {
                return object;
            }
        };
        cellTable.addColumn(updateColumn, "Update");
        cellTable.setColumnWidth(updateColumn, 80, Unit.PX);

        // File update Button
        Column<ParameterDTO, ParameterDTO> fileColumn = new Column<ParameterDTO, ParameterDTO>(
                new ActionCell<ParameterDTO>("File...", new ActionCell.Delegate<ParameterDTO>() {
                    @Override
                    public void execute(ParameterDTO parameter) {
                        ResourceSettingCallback callback = new ResourceSettingCallbackImplementation();
                        final DialogBox updateBox = new FileResourceDialogBox(resourceService,
                                parameter, callback);
                        updateBox.show();
                    }
                })) {
            @Override
            public ParameterDTO getValue(ParameterDTO object) {
                return object;
            }
        };
        cellTable.addColumn(fileColumn, "Update");
        cellTable.setColumnWidth(fileColumn, 80, Unit.PX);
    }

    private final class ResourceSettingCallbackImplementation implements ResourceSettingCallback {
        @Override
        public void changed(ParameterDTO parameter) {
            resourceService.setParameters(parameter, provider != null ? provider.getId() : null,
                    collection != null ? collection.getId() : null,
                    workflow != null ? workflow.getIdentifier() : null,
                    new AsyncCallback<Boolean>() {
                        @Override
                        public void onFailure(Throwable throwable) {
                            throwable.printStackTrace();
                        }

                        @Override
                        public void onSuccess(Boolean res) {
                            if (!res) {
                                Window.alert("Could not write resource!");
                            } else {
                                updateParameters();
                            }
                        }
                    });
        }
    }

    /*
     * Getters & Setters
     */

    /**
     * @return provider
     */
    public ProviderDTO getProvider() {
        return this.provider;
    }

    /**
     * @param provider
     */
    public void setProvider(ProviderDTO provider) {
        this.provider = provider;
    }

    /**
     * @return collection
     */
    public CollectionDTO getCollection() {
        return this.collection;
    }

    /**
     * @param collection
     */
    public void setCollection(CollectionDTO collection) {
        this.collection = collection;
    }

    /**
     * @return workflow
     */
    public WorkflowDTO getWorkflow() {
        return this.workflow;
    }

    /**
     * @param workflow
     */
    public void setWorkflow(WorkflowDTO workflow) {
        this.workflow = workflow;
    }

    /**
     * @return the cellBrowser
     */
    public CellBrowser getCellBrowser() {
        return cellBrowser;
    }

    /**
     * @param cellBrowser
     *            the cellBrowser to set
     */
    public void setCellBrowser(CellBrowser cellBrowser) {
        this.cellBrowser = cellBrowser;
    }

    /**
     * @return the cellTable
     */
    public CellTable<ParameterDTO> getCellTable() {
        return cellTable;
    }

    /**
     * @param cellTable
     *            the cellTable to set
     */
    public void setCellTable(CellTable<ParameterDTO> cellTable) {
        this.cellTable = cellTable;
    }

    /**
     * @return the repositoryService
     */
    public RepositoryServiceAsync getRepositoryService() {
        return repositoryService;
    }

    /**
     * @return the resourceService
     */
    public ResourceServiceAsync getResourceService() {
        return resourceService;
    }

    /**
     * @return the activeParameters
     */
    public List<ParameterDTO> getActiveParameters() {
        return activeParameters;
    }

}
