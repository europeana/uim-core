package org.theeuropeanlibrary.uim.gui.gwt.client.content;

import java.util.Comparator;

import org.theeuropeanlibrary.uim.gui.gwt.client.IngestionCockpitWidget;
import org.theeuropeanlibrary.uim.gui.gwt.client.OrchestrationServiceAsync;
import org.theeuropeanlibrary.uim.gui.gwt.client.content.BrowserTreeViewModel.BrowserObject;

import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Style.Unit;
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

    CellList<String>                        cellList;

    CellTable<String>                       cellTable;

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
                BrowserTreeViewModel.KEY_PROVIDER);
        selectionModelBrowser.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
//                Set<BrowserObject> vals = selectionModelBrowser.getSelectedSet();
//                for (BrowserObject val : vals) {
//                    if (val.getWrappedObject() instanceof ProviderDTO) {
//                        
//                    } else if (val.getWrappedObject() instanceof CollectionDTO) {
//                        
//                    } else if (val.getWrappedObject() instanceof WorkflowDTO) {
//                        
//                    } 
//                }
            }
        });
        
        BrowserTreeViewModel browserTreeViewModel = new BrowserTreeViewModel(orchestrationService, selectionModelBrowser);
        cellBrowser = new CellBrowser(browserTreeViewModel, null);
        cellBrowser.setAnimationEnabled(true);
        // cellBrowser.setSize("300px", "350px");
        cellBrowser.setSize("100%", "100%");

        leftPanel.add(cellBrowser);

        // Create a CellList.
        TextCell valCell = new TextCell();

        // Set a key provider that provides a unique key for each contact. If key is
        // used to identify contacts when fields (such as the name and address)
        // change.
        cellList = new CellList<String>(valCell, new SimpleKeyProvider<String>());
        cellList.setSize("100%", "100%");
        cellList.setPageSize(30);
        cellList.setKeyboardPagingPolicy(KeyboardPagingPolicy.INCREASE_RANGE);
        cellList.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.BOUND_TO_SELECTION);

        // Add a selection model so we can select cells.
        final SingleSelectionModel<String> selectionModel = new SingleSelectionModel<String>(
                new SimpleKeyProvider<String>());
        cellList.setSelectionModel(selectionModel);

        centerPanel.add(cellList);

        cellTable = new CellTable<String>(new SimpleKeyProvider<String>());
        cellTable.setWidth("100%", true);
        cellTable.setHeight("30px");

        final ListDataProvider<String> dataProvider = new ListDataProvider<String>();
        dataProvider.addDataDisplay(cellTable);

        // Attach a column sort handler to the ListDataProvider to sort the list.
        ListHandler<String> sortHandler = new ListHandler<String>(
                new ListDataProvider<String>().getList());
        cellTable.addColumnSortHandler(sortHandler);

        // Add a selection model so we can select cells.
        final SelectionModel<String> selectionModelTable = new MultiSelectionModel<String>(
                new SimpleKeyProvider<String>());
        cellTable.setSelectionModel(selectionModelTable,
                DefaultSelectionEventManager.<String> createCheckboxManager());

        // Initialize the columns.
        initTableColumns(selectionModelTable, sortHandler);

        rightPanel.add(cellTable);

        return widget;
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
    private void initTableColumns(final SelectionModel<String> selectionModel,
            ListHandler<String> sortHandler) {
        // Key
        Column<String, String> keyColumn = new Column<String, String>(new TextCell()) {
            @Override
            public String getValue(String object) {
                return object;
            }
        };
        keyColumn.setSortable(true);
        sortHandler.setComparator(keyColumn, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
        cellTable.addColumn(keyColumn, "Resource Name");
        cellTable.setColumnWidth(keyColumn, 20, Unit.PCT);

        // Value
        Column<String, String> valueColumn = new Column<String, String>(new TextCell()) {
            @Override
            public String getValue(String object) {
                return object;
            }
        };
        valueColumn.setSortable(true);
        sortHandler.setComparator(valueColumn, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
        cellTable.addColumn(valueColumn, "Resource Value");
        cellTable.setColumnWidth(valueColumn, 20, Unit.PCT);

        // Update Button
        Column<String, String> updateColumn = new Column<String, String>(new ActionCell<String>(
                "Update", new ActionCell.Delegate<String>() {
                    @Override
                    public void execute(String contact) {
                        Window.alert("You clicked " + contact);
                    }
                })) {
            @Override
            public String getValue(String object) {
                return object;
            }
        };
        cellTable.addColumn(updateColumn, "Update");
        cellTable.setColumnWidth(updateColumn, 10, Unit.PCT);
    }
}
