package org.theeuropeanlibrary.uim.gui.gwt.client.content;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.theeuropeanlibrary.uim.gui.gwt.client.IngestionCockpitWidget;
import org.theeuropeanlibrary.uim.gui.gwt.client.OrchestrationServiceAsync;
import org.theeuropeanlibrary.uim.gui.gwt.client.content.DataTreeViewModel.BrowserObject;
import org.theeuropeanlibrary.uim.gui.gwt.shared.CollectionDTO;
import org.theeuropeanlibrary.uim.gui.gwt.shared.ParameterDTO;
import org.theeuropeanlibrary.uim.gui.gwt.shared.ProviderDTO;
import org.theeuropeanlibrary.uim.gui.gwt.shared.WorkflowDTO;

import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellBrowser;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.view.client.SimpleKeyProvider;

/**
 * Triggers execution.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Apr 27, 2011
 */
public class ExecutionTriggerWidget extends IngestionCockpitWidget {
    /**
     * The UiBinder interface used by this example.
     */
    interface Binder extends UiBinder<Widget, ExecutionTriggerWidget> {
    }

    private final OrchestrationServiceAsync orchestrationService;

    @UiField
    DecoratorPanel                          decPanel;

    @UiField
    SplitLayoutPanel                        splitPanel;

    @UiField
    LayoutPanel                             leftPanel;

    @UiField
    LayoutPanel                             rightPanel;

// @UiField(provided = true)
    CellBrowser                             cellBrowser;

// @UiField(provided = true)
    CellTable<ParameterDTO>                 cellTable;

    @UiField(provided = true)
    ExecutionForm                           executionForm;

    private final List<ParameterDTO>        activeParameters = new ArrayList<ParameterDTO>();

    private ProviderDTO                     provider;
    private CollectionDTO                   collection;
    private WorkflowDTO                     workflow;

    /**
     * Creates a new instance of this class.
     * 
     * @param orchestrationService
     */
    public ExecutionTriggerWidget(OrchestrationServiceAsync orchestrationService) {
        super(
                "Start Execution",
                "This view allows to select provider, collection and workflow and optional the resources to start a new execution!");
        this.orchestrationService = orchestrationService;
    }

    /**
     * Initialize this example.
     */
    @Override
    public Widget onInitialize() {
        executionForm = new ExecutionForm(orchestrationService, new ClickHandler() {
            @Override
            public void onClick(ClickEvent arg0) {
                for (int i = 0; i < cellBrowser.getRootTreeNode().getChildCount(); i++) {
                    cellBrowser.getRootTreeNode().setChildOpen(i, false);
                }
            }
        });

        Binder uiBinder = GWT.create(Binder.class);
        Widget widget = uiBinder.createAndBindUi(this);

        final MultiSelectionModel<BrowserObject> selectionModelBrowser = new MultiSelectionModel<BrowserObject>(
                DataTreeViewModel.KEY_PROVIDER);
        selectionModelBrowser.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                Set<BrowserObject> vals = selectionModelBrowser.getSelectedSet();
                for (BrowserObject val : vals) {
                    if (val.getWrappedObject() instanceof ProviderDTO) {
                        provider = (ProviderDTO)val.getWrappedObject();

                        executionForm.setProvider((ProviderDTO)val.getWrappedObject());
                        executionForm.setCollection(null);
                        executionForm.setWorkflow(null);
                    } else if (val.getWrappedObject() instanceof CollectionDTO) {
                        collection = (CollectionDTO)val.getWrappedObject();

                        executionForm.setCollection((CollectionDTO)val.getWrappedObject());
                        executionForm.setWorkflow(null);
                    } else if (val.getWrappedObject() instanceof WorkflowDTO) {
                        workflow = (WorkflowDTO)val.getWrappedObject();

                        executionForm.setWorkflow((WorkflowDTO)val.getWrappedObject());
                    }
                    updateParameters();
                }
            }
        });

        DataTreeViewModel browserTreeViewModel = new DataTreeViewModel(orchestrationService,
                selectionModelBrowser, false);
        cellBrowser = new CellBrowser(browserTreeViewModel, null);
        cellBrowser.setAnimationEnabled(true);
        cellBrowser.setSize("100%", "100%");

        leftPanel.add(cellBrowser);

        cellTable = new CellTable<ParameterDTO>(new SimpleKeyProvider<ParameterDTO>());
        cellTable.setWidth("100%", true);
        cellTable.setHeight("30px");
// cellTable.setPageSize(15);
// cellTable.setRowCount(10);

        final ListDataProvider<ParameterDTO> dataProvider = new ListDataProvider<ParameterDTO>();
        dataProvider.setList(activeParameters);
        dataProvider.addDataDisplay(cellTable);

        ListHandler<ParameterDTO> sortHandler = new ListHandler<ParameterDTO>(
                new ListDataProvider<ParameterDTO>().getList());
        cellTable.addColumnSortHandler(sortHandler);

        final MultiSelectionModel<ParameterDTO> selectionModelTable = new MultiSelectionModel<ParameterDTO>(
                new SimpleKeyProvider<ParameterDTO>());
        cellTable.setSelectionModel(selectionModelTable,
                DefaultSelectionEventManager.<ParameterDTO> createCheckboxManager());

        initTableColumns(selectionModelTable, sortHandler);

        rightPanel.add(cellTable);

        return widget;
    }

    @Override
    protected void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(ExecutionTriggerWidget.class, new RunAsyncCallback() {
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
     * Retrieve parameters for given settings.
     */
    public void updateParameters() {
        orchestrationService.getParameters(provider.getId(), collection.getId(),
                workflow.getName(), new AsyncCallback<List<ParameterDTO>>() {
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
                        
                        // uim:exec -o start workflowname -c collectionmnemoic | -p providermnemonic key=value&key=value&...
                        StringBuilder b = new StringBuilder();
                        b.append("uim:exec -o start ");
                        b.append(workflow.getName());
                        if (collection != null && !collection.getName().equals(DataTreeViewModel.ALL_COLLECTIONS)) {
                            b.append(" -c ");
                            b.append(collection.getMnemonic());
                        } else {
                            b.append(" -p ");
                            b.append(provider.getMnemonic());
                        }
                        b.append(" ");
                        for (int i = 0; i < parameters.size(); i++) {
                            ParameterDTO param = parameters.get(i);
                            if (param.getValues() != null && param.getValues().length > 0) {
                                b.append(param.getKey());
                                b.append("=");

                                for (int j = 0; j < param.getValues().length; j++) {
                                    b.append(param.getValues()[j]);
                                    if (j < param.getValues().length - 1) {
                                        b.append("|");
                                    }
                                }
                                if (i < parameters.size() - 1) {
                                    b.append("&");
                                }
                            }
                        }
                        executionForm.setCommandline(b.toString());
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
//                for (String val : object.getValues()) {
//                    builder.append(val);
//                    builder.append("\n");
//                }
                return builder.toString();
            }
        };
        valueColumn.setSortable(true);
        cellTable.addColumn(valueColumn, "Resource Value");
        cellTable.setColumnWidth(valueColumn, 40, Unit.PCT);

        // Update Button
        Column<ParameterDTO, String> updateColumn = new Column<ParameterDTO, String>(
                new ActionCell<String>("Update", new ActionCell.Delegate<String>() {
                    @Override
                    public void execute(String contact) {
                        Window.alert("You clicked " + contact);
                    }
                })) {
            @Override
            public String getValue(ParameterDTO object) {
                return "Update...";
            }
        };
        cellTable.addColumn(updateColumn, "Update");
        cellTable.setColumnWidth(updateColumn, 20, Unit.PCT);
    }
}
