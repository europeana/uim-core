package org.theeuropeanlibrary.uim.gui.gwt.client.content;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.theeuropeanlibrary.uim.gui.gwt.client.IngestionCockpitWidget;
import org.theeuropeanlibrary.uim.gui.gwt.client.OrchestrationServiceAsync;
import org.theeuropeanlibrary.uim.gui.gwt.shared.ExecutionDTO;

import com.google.gwt.cell.client.DateCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.i18n.client.DateTimeFormat;
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

    private final OrchestrationServiceAsync orchestrationServiceAsync;
    private final List<ExecutionDTO>        pastExecutions = new ArrayList<ExecutionDTO>();

    /**
     * The main CellTable.
     */
    @UiField(provided = true)
    CellTable<ExecutionDTO>                 cellTable;

    /**
     * The pager used to change the range of data.
     */
    @UiField(provided = true)
    SimplePager                             pager;

    /**
     * Creates a new instance of this class.
     * 
     * @param orchestrationServiceAsync
     */
    public ExecutionHistoryWidget(OrchestrationServiceAsync orchestrationServiceAsync) {
        super("Finished Executions", "This view shows all finished executions!");
        this.orchestrationServiceAsync = orchestrationServiceAsync;
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

        final ListDataProvider<ExecutionDTO> dataProvider = new ListDataProvider<ExecutionDTO>();
        dataProvider.setList(pastExecutions);
        dataProvider.addDataDisplay(cellTable);

        // Attach a column sort handler to the ListDataProvider to sort the list.
        ListHandler<ExecutionDTO> sortHandler = new ListHandler<ExecutionDTO>(
                dataProvider.getList());
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

    /**
     * Retrieve current past executions.
     */
    public void updatePastExecutions() {
        orchestrationServiceAsync.getPastExecutions(new AsyncCallback<List<ExecutionDTO>>() {
            @Override
            public void onFailure(Throwable caught) {
                caught.printStackTrace();
            }

            @Override
            public void onSuccess(List<ExecutionDTO> result) {
                pastExecutions.clear();
                pastExecutions.addAll(result);
                cellTable.setRowData(0, pastExecutions);
                cellTable.setRowCount(pastExecutions.size());
            }
        });
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
        Column<ExecutionDTO, String> workflowColumn = new Column<ExecutionDTO, String>(
                new TextCell()) {
            @Override
            public String getValue(ExecutionDTO object) {
                return object.getWorkflow();
            }
        };
        workflowColumn.setSortable(true);
        sortHandler.setComparator(workflowColumn, new Comparator<ExecutionDTO>() {
            @Override
            public int compare(ExecutionDTO o1, ExecutionDTO o2) {
                return o1.getWorkflow().compareTo(o2.getWorkflow());
            }
        });
        cellTable.addColumn(workflowColumn, "Workflow");
        cellTable.setColumnWidth(workflowColumn, 20, Unit.PCT);

        // Data set
        Column<ExecutionDTO, String> datasetColumn = new Column<ExecutionDTO, String>(
                new TextCell()) {
            @Override
            public String getValue(ExecutionDTO object) {
                return object.getDataSet();
            }
        };
        datasetColumn.setSortable(true);
        sortHandler.setComparator(datasetColumn, new Comparator<ExecutionDTO>() {
            @Override
            public int compare(ExecutionDTO o1, ExecutionDTO o2) {
                return o1.getDataSet().compareTo(o2.getDataSet());
            }
        });
        cellTable.addColumn(datasetColumn, "Dataset");
        cellTable.setColumnWidth(datasetColumn, 20, Unit.PCT);

        DateTimeFormat dtf = DateTimeFormat.getFormat("dd.MM.yyyy 'at' HH:mm:ss");
        // Start Time
        Column<ExecutionDTO, Date> startTimeColumn = new Column<ExecutionDTO, Date>(new DateCell(
                dtf)) {
            @Override
            public Date getValue(ExecutionDTO object) {
                return object.getStartTime();
            }
        };
        startTimeColumn.setSortable(true);
        sortHandler.setComparator(startTimeColumn, new Comparator<ExecutionDTO>() {
            @Override
            public int compare(ExecutionDTO o1, ExecutionDTO o2) {
                return o1.getStartTime().compareTo(o2.getStartTime());
            }
        });
        cellTable.addColumn(startTimeColumn, "Start Time");
        cellTable.setColumnWidth(startTimeColumn, 20, Unit.PCT);

        // End Time
        Column<ExecutionDTO, Date> endTimeColumn = new Column<ExecutionDTO, Date>(new DateCell(dtf)) {
            @Override
            public Date getValue(ExecutionDTO object) {
                return object.getEndTime();
            }
        };
        endTimeColumn.setSortable(true);
        sortHandler.setComparator(endTimeColumn, new Comparator<ExecutionDTO>() {
            @Override
            public int compare(ExecutionDTO o1, ExecutionDTO o2) {
                return o1.getEndTime().compareTo(o2.getEndTime());
            }
        });
        cellTable.addColumn(endTimeColumn, "End Time");
        cellTable.setColumnWidth(endTimeColumn, 20, Unit.PCT);

        updatePastExecutions();
    }
}
