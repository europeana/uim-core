package org.theeuropeanlibrary.uim.gui.gwt.client.content;

import java.util.Comparator;

import org.theeuropeanlibrary.uim.gui.gwt.client.IngestionCockpitWidget;
import org.theeuropeanlibrary.uim.gui.gwt.shared.ExecutionDTO;

import com.google.gwt.cell.client.EditTextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionModel;

/**
 * Table view showing current exectuions.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Apr 27, 2011
 */
public class ExecutionHistoryWidget extends IngestionCockpitWidget {
    /**
     * The UiBinder interface used by this example.
     */
    interface Binder extends UiBinder<Widget, ExecutionHistoryWidget> {
    }

    /**
     * The main CellTable.
     */
    @UiField(provided = true)
    CellTable<ExecutionDTO> cellTable;

    /**
     * The pager used to change the range of data.
     */
    @UiField(provided = true)
    SimplePager             pager;

    /**
     * Creates a new instance of this class.
     * 
     * @param name
     * @param description
     */
    public ExecutionHistoryWidget(String name, String description) {
        super(name, description);
    }

    /**
     * Initialize this example.
     */
    @Override
    public Widget onInitialize() {
        // Create a CellTable.

        // Set a key provider that provides a unique key for each contact. If key is
        // used to identify contacts when fields (such as the name and address)
        // change.
        cellTable = new CellTable<ExecutionDTO>(ExecutionDTO.KEY_PROVIDER);
        cellTable.setWidth("100%", true);

        // Attach a column sort handler to the ListDataProvider to sort the list.
        ListHandler<ExecutionDTO> sortHandler = new ListHandler<ExecutionDTO>(
                new ListDataProvider<ExecutionDTO>().getList());
        cellTable.addColumnSortHandler(sortHandler);

        // Create a Pager to control the table.
        SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
        pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
        pager.setDisplay(cellTable);

        // Add a selection model so we can select cells.
        final SelectionModel<ExecutionDTO> selectionModel = new MultiSelectionModel<ExecutionDTO>(
                ExecutionDTO.KEY_PROVIDER);
        cellTable.setSelectionModel(selectionModel,
                DefaultSelectionEventManager.<ExecutionDTO> createCheckboxManager());

        // Initialize the columns.
        initTableColumns(selectionModel, sortHandler);

// // Add the CellList to the adapter in the database.
// ContactDatabase.get().addDataDisplay(cellTable);

        // Create the UiBinder.
        Binder uiBinder = GWT.create(Binder.class);
        Widget widget = uiBinder.createAndBindUi(this);

        return widget;
    }

    @Override
    protected void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(ExecutionHistoryWidget.class, new RunAsyncCallback() {

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
    private void initTableColumns(final SelectionModel<ExecutionDTO> selectionModel,
            ListHandler<ExecutionDTO> sortHandler) {
        // Workflow
        Column<ExecutionDTO, String> firstNameColumn = new Column<ExecutionDTO, String>(
                new EditTextCell()) {
            @Override
            public String getValue(ExecutionDTO object) {
                return object.getWorkflow();
            }
        };
        firstNameColumn.setSortable(true);
        sortHandler.setComparator(firstNameColumn, new Comparator<ExecutionDTO>() {
            @Override
            public int compare(ExecutionDTO o1, ExecutionDTO o2) {
                return o1.getWorkflow().compareTo(o2.getWorkflow());
            }
        });
        cellTable.addColumn(firstNameColumn, "Workflow");
        cellTable.setColumnWidth(firstNameColumn, 20, Unit.PCT);

        // Data set
        Column<ExecutionDTO, String> lastNameColumn = new Column<ExecutionDTO, String>(
                new EditTextCell()) {
            @Override
            public String getValue(ExecutionDTO object) {
                return object.getDataSet();
            }
        };
        lastNameColumn.setSortable(true);
        sortHandler.setComparator(lastNameColumn, new Comparator<ExecutionDTO>() {
            @Override
            public int compare(ExecutionDTO o1, ExecutionDTO o2) {
                return o1.getDataSet().compareTo(o2.getDataSet());
            }
        });
        cellTable.addColumn(lastNameColumn, "Dataset");
        cellTable.setColumnWidth(lastNameColumn, 20, Unit.PCT);

        // Data set
        lastNameColumn = new Column<ExecutionDTO, String>(new EditTextCell()) {
            @Override
            public String getValue(ExecutionDTO object) {
                return object.getDataSet();
            }
        };
        lastNameColumn.setSortable(true);
        sortHandler.setComparator(lastNameColumn, new Comparator<ExecutionDTO>() {
            @Override
            public int compare(ExecutionDTO o1, ExecutionDTO o2) {
                return o1.getDataSet().compareTo(o2.getDataSet());
            }
        });
        cellTable.addColumn(lastNameColumn, "Dataset");
        cellTable.setColumnWidth(lastNameColumn, 20, Unit.PCT);
    }
}
