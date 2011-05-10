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

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellBrowser;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.HasKeyboardPagingPolicy.KeyboardPagingPolicy;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.view.client.SimpleKeyProvider;
import com.google.gwt.view.client.SingleSelectionModel;

/**
 * Triggers execution.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Apr 27, 2011
 */
public class ResourceAdministrationWidget extends IngestionCockpitWidget {
    /**
     * The UiBinder interface used by this example.
     */
    interface Binder extends UiBinder<Widget, ResourceAdministrationWidget> {
    }

    private final OrchestrationServiceAsync orchestrationService;

    @UiField
    DecoratorPanel                          decPanel;

    @UiField
    SplitLayoutPanel                        splitPanel;

    @UiField
    LayoutPanel                             leftPanel;

    @UiField
    LayoutPanel                             centerPanel;

    @UiField
    LayoutPanel                             rightPanel;

    CellBrowser                             cellBrowser;

    CellList<WorkflowDTO>                   cellList;

    CellTable<ParameterDTO>                 cellTable;

    private ProviderDTO                     provider;
    private CollectionDTO                   collection;
    private WorkflowDTO                     workflow;

    private final List<ParameterDTO>        activeParameters = new ArrayList<ParameterDTO>();
    private final List<WorkflowDTO>         activeWorkflows  = new ArrayList<WorkflowDTO>();

    /**
     * Creates a new instance of this class.
     * 
     * @param orchestrationService
     */
    public ResourceAdministrationWidget(OrchestrationServiceAsync orchestrationService) {
        super("Manage Resources", "This view allows to manage resources for known plugins!");
        this.orchestrationService = orchestrationService;
    }

    /**
     * Initialize this example.
     */
    @Override
    public Widget onInitialize() {
        // Create the UiBinder.
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
                    } else if (val.getWrappedObject() instanceof CollectionDTO) {
                        collection = (CollectionDTO)val.getWrappedObject();
                    }
                    updateParameters();
                }
            }
        });

        DataTreeViewModel browserTreeViewModel = new DataTreeViewModel(orchestrationService,
                selectionModelBrowser, true);
        cellBrowser = new CellBrowser(browserTreeViewModel, null);
        cellBrowser.setAnimationEnabled(true);
        // cellBrowser.setSize("300px", "350px");
        cellBrowser.setSize("100%", "100%");

        centerPanel.add(cellBrowser);

        cellList = new CellList<WorkflowDTO>(new WorkflowDTOCell(),
                new SimpleKeyProvider<WorkflowDTO>());
        cellList.setSize("100%", "100%");
        cellList.setPageSize(30);
        cellList.setKeyboardPagingPolicy(KeyboardPagingPolicy.INCREASE_RANGE);
        cellList.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.BOUND_TO_SELECTION);

        final ListDataProvider<WorkflowDTO> workflowProvider = new ListDataProvider<WorkflowDTO>();
        workflowProvider.setList(activeWorkflows);
        workflowProvider.addDataDisplay(cellList);

        // Add a selection model so we can select cells.
        final SingleSelectionModel<WorkflowDTO> selectionModel = new SingleSelectionModel<WorkflowDTO>(
                new SimpleKeyProvider<WorkflowDTO>());
        cellList.setSelectionModel(selectionModel);
        selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                workflow = selectionModel.getSelectedObject();
                provider = null;
                collection = null;
                for (int i = 0; i < cellBrowser.getRootTreeNode().getChildCount(); i++) {
                    cellBrowser.getRootTreeNode().setChildOpen(i, false);
                }
                updateParameters();
            }
        });

        leftPanel.add(cellList);

        updateWorkflows();

        cellTable = new CellTable<ParameterDTO>(new SimpleKeyProvider<ParameterDTO>());
        cellTable.setWidth("100%", true);
        cellTable.setHeight("30px");

        final ListDataProvider<ParameterDTO> dataProvider = new ListDataProvider<ParameterDTO>();
        dataProvider.setList(activeParameters);
        dataProvider.addDataDisplay(cellTable);

        // Attach a column sort handler to the ListDataProvider to sort the list.
        ListHandler<ParameterDTO> sortHandler = new ListHandler<ParameterDTO>(
                new ListDataProvider<ParameterDTO>().getList());
        cellTable.addColumnSortHandler(sortHandler);

        // Add a selection model so we can select cells.
        final SelectionModel<ParameterDTO> selectionModelTable = new MultiSelectionModel<ParameterDTO>(
                new SimpleKeyProvider<ParameterDTO>());
        cellTable.setSelectionModel(selectionModelTable,
                DefaultSelectionEventManager.<ParameterDTO> createCheckboxManager());

        // Initialize the columns.
        initTableColumns(selectionModelTable, sortHandler);

        rightPanel.add(cellTable);

        return widget;
    }

    /**
     * Retrieve parameters for given settings.
     */
    public void updateParameters() {
        orchestrationService.getParameters(provider != null ? provider.getId() : null,
                collection != null ? collection.getId() : null,
                workflow != null ? workflow.getName() : null,
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

    /**
     * Retrieve parameters for given settings.
     */
    public void updateWorkflows() {
        orchestrationService.getWorkflows(new AsyncCallback<List<WorkflowDTO>>() {
            @Override
            public void onFailure(Throwable throwable) {
                throwable.printStackTrace();
            }

            @Override
            public void onSuccess(List<WorkflowDTO> workflows) {
                activeWorkflows.clear();
                activeWorkflows.addAll(workflows);
                cellList.setRowData(0, activeWorkflows);
                cellList.setRowCount(activeWorkflows.size());
            }
        });
    }

    /**
     * The Cell used to render a {@link WorkflowDTO}.
     */
    private static class WorkflowDTOCell extends AbstractCell<WorkflowDTO> {
        @Override
        public void render(Context context, WorkflowDTO value, SafeHtmlBuilder sb) {
            if (value == null) { return; }
            sb.appendHtmlConstant(value.getName());
        }
    }

    @Override
    protected void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(ResourceAdministrationWidget.class, new RunAsyncCallback() {
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
                for (int i = 0; i < object.getValues().length; i++) {
                    builder.append(object.getValues()[i]);
                    if (i < object.getValues().length - 1) {
                        builder.append("|");
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
                new ActionCell<ParameterDTO>("Update...", new ActionCell.Delegate<ParameterDTO>() {
                    @Override
                    public void execute(ParameterDTO parameter) {
                        final DialogBox updateBox = createDialogBox(parameter);
                        updateBox.show();
                    }
                })) {
            @Override
            public ParameterDTO getValue(ParameterDTO object) {
                return object;
            }
        };
        cellTable.addColumn(updateColumn, "Update");
        cellTable.setColumnWidth(updateColumn, 20, Unit.PCT);
    }
    
    /**
     * Create the dialog box for this example.
     * 
     * @param parameter
     * @return the new dialog box
     */
    public DialogBox createDialogBox(final ParameterDTO parameter) {
        final DialogBox dialogBox = new DialogBox();
        dialogBox.setText("Update Resource");
// dialogBox.setSize("400px", "400px");
        dialogBox.setGlassEnabled(true);
        dialogBox.setAnimationEnabled(true);
        dialogBox.center();

        VerticalPanel dialogContents = new VerticalPanel();
        dialogContents.setSpacing(4);
        dialogContents.setSize("100%", "100%");
        dialogBox.setWidget(dialogContents);

        HorizontalPanel key = new HorizontalPanel();
        key.setSpacing(4);
        key.setWidth("100%");
        Label keyLabel = new Label("Resource Key");
        keyLabel.setWidth("30%");
        key.add(keyLabel);
        key.setCellHorizontalAlignment(keyLabel, HasHorizontalAlignment.ALIGN_LEFT);
        Label keyValue = new Label(parameter.getKey());
        keyValue.setWidth("70%");
        key.add(keyValue);
        key.setCellHorizontalAlignment(keyValue, HasHorizontalAlignment.ALIGN_LEFT);
        dialogContents.add(key);

        HorizontalPanel values = new HorizontalPanel();
        values.setSpacing(4);
        values.setWidth("100%");
        Label valueLabel = new Label("Resource Values");
        valueLabel.setWidth("30%");
        values.add(valueLabel);
        values.setCellHorizontalAlignment(valueLabel, HasHorizontalAlignment.ALIGN_LEFT);

        final List<TextBox> boxes = new ArrayList<TextBox>();
        VerticalPanel valuesPanel = new VerticalPanel();
        valuesPanel.setWidth("70%");
        for (String value : parameter.getValues()) {
            TextBox box = new TextBox();
            box.setWidth("100%");
            box.setText(value);
            box.setReadOnly(false);
            valuesPanel.add(box);
            boxes.add(box);
        }
        TextBox box = new TextBox();
        box.setReadOnly(false);
        box.setWidth("100%");
        boxes.add(box);
        valuesPanel.add(box);
        values.add(valuesPanel);
        values.setCellHorizontalAlignment(valuesPanel, HasHorizontalAlignment.ALIGN_LEFT);

        dialogContents.add(values);

        HorizontalPanel buttons = new HorizontalPanel();
        buttons.setSpacing(4);
        buttons.setWidth("100%");
        Button okButton = new Button("Ok", new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                List<String> vals = new ArrayList<String>();
                for (TextBox box : boxes) {
                    if (box.getText().length() > 0) {
                        vals.add(box.getText());
                    }
                }
                if (vals.size() > 0) {
                    parameter.setValues(vals.toArray(new String[vals.size()]));
                    orchestrationService.setParameters(parameter,
                            provider != null ? provider.getId() : null, collection != null
                                    ? collection.getId() : null,
                            workflow != null ? workflow.getName() : null,
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

                dialogBox.hide();
            }
        });
        buttons.add(okButton);
        buttons.setCellHorizontalAlignment(okButton, HasHorizontalAlignment.ALIGN_RIGHT);

        Button cancelButton = new Button("Cancel", new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                dialogBox.hide();
            }
        });
        buttons.add(cancelButton);
        buttons.setCellHorizontalAlignment(cancelButton, HasHorizontalAlignment.ALIGN_LEFT);
        dialogContents.add(buttons);

        return dialogBox;
    }
}
