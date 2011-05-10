package org.theeuropeanlibrary.uim.gui.gwt.client.content;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.theeuropeanlibrary.uim.gui.gwt.client.IngestionCockpitWidget;
import org.theeuropeanlibrary.uim.gui.gwt.client.OrchestrationServiceAsync;
import org.theeuropeanlibrary.uim.gui.gwt.shared.ExecutionDTO;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.DateCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.widgetideas.client.ProgressBar;

/**
 * Table view showing current exectuions.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Apr 27, 2011
 */
public class ExecutionDetailWidget extends IngestionCockpitWidget {
    /**
     * The UiBinder interface used by this example.
     */
    interface Binder extends UiBinder<Widget, ExecutionDetailWidget> {
    }

    private final OrchestrationServiceAsync orchestrationService;
    private final List<ExecutionDTO>        activeExecutions = new ArrayList<ExecutionDTO>();

    /**
     * The main CellTable.
     */
    @UiField(provided = true)
    CellTable<ExecutionDTO>                 cellTable;

    /**
     * The contact form used to update contacts.
     */
    @UiField
    ExecutionStatus                         executionStatus;

    /**
     * The pager used to change the range of data.
     */
    @UiField(provided = true)
    SimplePager                             pager;

    /**
     * Creates a new instance of this class.
     * 
     * @param orchestrationService
     */
    public ExecutionDetailWidget(OrchestrationServiceAsync orchestrationService) {
        super(
                "Active Executions",
                "This view shows the current running executions together with their progress and a termination button!");
        this.orchestrationService = orchestrationService;
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
        dataProvider.setList(activeExecutions);
        dataProvider.addDataDisplay(cellTable);

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

    /**
     * Retrieve current executions.
     */
    public void updateActiveExecutions() {
        // load active executions
        orchestrationService.getActiveExecutions(new AsyncCallback<List<ExecutionDTO>>() {
            @Override
            public void onFailure(Throwable throwable) {
                throwable.printStackTrace();
            }

            @Override
            public void onSuccess(List<ExecutionDTO> executions) {
                activeExecutions.clear();
                activeExecutions.addAll(executions);
                cellTable.setRowData(0, activeExecutions);
                cellTable.setRowCount(activeExecutions.size());
            }
        });
    }

    @Override
    protected void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(ExecutionDetailWidget.class, new RunAsyncCallback() {

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

        // Progress Bar
        Column<ExecutionDTO, ProgressBar> progressColumn = new Column<ExecutionDTO, ProgressBar>(
                new ProgressBarCell()) {
            @Override
            public ProgressBar getValue(ExecutionDTO execution) {
                final ProgressBar bar = new ProgressBar(0, execution.getScheduled());
                bar.setTitle(execution.getName());
                bar.setTextVisible(true);
                return bar;
            }
        };
        progressColumn.setSortable(true);
        sortHandler.setComparator(progressColumn, new Comparator<ExecutionDTO>() {
            @Override
            public int compare(ExecutionDTO o1, ExecutionDTO o2) {
                return o1.getEndTime().compareTo(o2.getEndTime());
            }
        });
        cellTable.addColumn(progressColumn, "Progress");
        cellTable.setColumnWidth(progressColumn, 20, Unit.PCT);

        updateActiveExecutions();

        Timer t = new Timer() {
            @Override
            public void run() {
                updateActiveExecutions();
            }
        };
        t.scheduleRepeating(5000);
    }

    private static class ProgressBarCell extends AbstractCell<ProgressBar> {
        @Override
        public void render(Context context, ProgressBar value, SafeHtmlBuilder sb) {
            value.redraw();
            sb.append(SafeHtmlUtils.fromString(value.getElement().getInnerHTML()));
        }
    }

}
