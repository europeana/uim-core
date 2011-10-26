package eu.europeana.uim.gui.cp.client.monitoring;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.ActionCell;
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
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.gwt.widgetideas.client.ProgressBar;

import eu.europeana.uim.gui.cp.client.IngestionWidget;
import eu.europeana.uim.gui.cp.client.services.ExecutionServiceAsync;
import eu.europeana.uim.gui.cp.shared.ExecutionDTO;

/**
 * Table view showing current exectuions.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Apr 27, 2011
 */
public class IngestionDetailWidget extends IngestionWidget {
    /**
     * The UiBinder interface used by this example.
     */
    interface Binder extends UiBinder<Widget, IngestionDetailWidget> {
    }

    private final ExecutionServiceAsync executionService;
    private final List<ExecutionDTO>    activeExecutions = new ArrayList<ExecutionDTO>();

    /**
     * The main CellTable.
     */
    @UiField(provided = true)
    CellTable<ExecutionDTO>             cellTable;

    /**
     * The contact form used to update contacts.
     */
    @UiField(provided = true)
    IngestionStatus                     executionStatus;

    /**
     * Creates a new instance of this class.
     * 
     * @param executionService
     */
    public IngestionDetailWidget(ExecutionServiceAsync executionService) {
        super(
                "Active Ingestions",
                "This view shows the current running ingestion activities together with their progress and a cancel button!");
        this.executionService = executionService;
    }

    /**
     * Initialize this example.
     */
    @Override
    public Widget onInitialize() {
        executionStatus = new IngestionStatus(executionService);

        cellTable = new CellTable<ExecutionDTO>(ExecutionDTO.KEY_PROVIDER);
        cellTable.setWidth("100%", true);
        cellTable.setHeight("30px");
        cellTable.setPageSize(10);

        final ListDataProvider<ExecutionDTO> dataProvider = new ListDataProvider<ExecutionDTO>();
        dataProvider.setList(activeExecutions);
        dataProvider.addDataDisplay(cellTable);

        ListHandler<ExecutionDTO> sortHandler = new ListHandler<ExecutionDTO>(
                new ListDataProvider<ExecutionDTO>().getList());
        cellTable.addColumnSortHandler(sortHandler);

        final SingleSelectionModel<ExecutionDTO> selectionModel = new SingleSelectionModel<ExecutionDTO>(
                ExecutionDTO.KEY_PROVIDER);
        cellTable.setSelectionModel(selectionModel);
        selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                ExecutionDTO execution = selectionModel.getSelectedObject();
                executionStatus.setExecution(execution);
            }
        });

        initTableColumns(selectionModel, sortHandler);

        Binder uiBinder = GWT.create(Binder.class);
        Widget widget = uiBinder.createAndBindUi(this);

        return widget;
    }

    @Override
    protected void onLoad() {
        super.onLoad();
        updateActiveExecutions();
    }

    /**
     * Retrieve current executions.
     */
    public void updateActiveExecutions() {
        executionService.getActiveExecutions(new AsyncCallback<List<ExecutionDTO>>() {
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
                cellTable.setHeight((30 + 20 * activeExecutions.size()) + "px");

                boolean updated = false;
                for (ExecutionDTO exec : executions) {
                    if (executionStatus.getExecution() != null &&
                        executionStatus.getExecution().getId().equals(exec.getId())) {
                        executionStatus.setExecution(exec);
                        updated = true;
                        break;
                    }
                }
                if (!updated) {
                    executionStatus.clearForm();
                }
            }
        });
    }

    @Override
    protected void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(IngestionDetailWidget.class, new RunAsyncCallback() {
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
        // ID
        Column<ExecutionDTO, String> idColumn = new Column<ExecutionDTO, String>(new TextCell()) {
            @Override
            public String getValue(ExecutionDTO object) {
                return object.getId().toString();
            }
        };
        idColumn.setSortable(true);
        sortHandler.setComparator(idColumn, new Comparator<ExecutionDTO>() {
            @Override
            public int compare(ExecutionDTO o1, ExecutionDTO o2) {
                return o1.getId().compareTo(o2.getId());
            }
        });
        cellTable.addColumn(idColumn, "ID");
        cellTable.setColumnWidth(idColumn, 16, Unit.PCT);

        // Name
        Column<ExecutionDTO, String> workflowColumn = new Column<ExecutionDTO, String>(
                new TextCell()) {
            @Override
            public String getValue(ExecutionDTO object) {
                return object.getName();
            }
        };
        workflowColumn.setSortable(true);
        sortHandler.setComparator(workflowColumn, new Comparator<ExecutionDTO>() {
            @Override
            public int compare(ExecutionDTO o1, ExecutionDTO o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        cellTable.addColumn(workflowColumn, "Name");
        cellTable.setColumnWidth(workflowColumn, 30, Unit.PCT);

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
        cellTable.setColumnWidth(startTimeColumn, 25, Unit.PCT);

        // Progress Bar
        Column<ExecutionDTO, FlowPanel> progressColumn = new Column<ExecutionDTO, FlowPanel>(
                new FlowPanelCell()) {
            @Override
            public FlowPanel getValue(ExecutionDTO execution) {
                final ProgressBar progressBar = new ProgressBar(0,
                        execution.getProgress().getWork());
                progressBar.setTitle("Ingestion Progress");
                progressBar.setTextVisible(true);
                progressBar.setHeight("20px");
                progressBar.setWidth("100%");
                progressBar.setVisible(true);
                progressBar.setProgress(execution.getProgress().getWorked());

                FlowPanel panel = new FlowPanel();
                panel.add(progressBar);
                panel.setVisible(true);
                panel.setSize("100%", "100%");
                return panel;
            }
        };
        progressColumn.setSortable(true);
        sortHandler.setComparator(progressColumn, new Comparator<ExecutionDTO>() {
            @Override
            public int compare(ExecutionDTO o1, ExecutionDTO o2) {
                double one = (double)o1.getProgress().getWorked() /
                             (double)o1.getProgress().getWork();
                double two = (double)o2.getProgress().getWorked() /
                             (double)o2.getProgress().getWork();
                return Double.compare(one, two);
            }
        });
        cellTable.addColumn(progressColumn, "Progress");
        cellTable.setColumnWidth(progressColumn, 30, Unit.PCT);

        // Log file
        Column<ExecutionDTO, ExecutionDTO> logfileColumn = new Column<ExecutionDTO, ExecutionDTO>(
                new ActionCell<ExecutionDTO>("Log", new ActionCell.Delegate<ExecutionDTO>() {
                    @Override
                    public void execute(ExecutionDTO parameter) {
                        com.google.gwt.user.client.Window.open(GWT.getModuleBaseURL() +
                                                               "logfile?format=html&execution=" +
                                                               parameter.getId(), "_blank", "");
                    }
                })) {
            @Override
            public ExecutionDTO getValue(ExecutionDTO object) {
                return object;
            }
        };

        cellTable.addColumn(logfileColumn, "Log");
        cellTable.setColumnWidth(logfileColumn, 10, Unit.PCT);

        updateActiveExecutions();

        Timer t = new Timer() {
            @Override
            public void run() {
                updateActiveExecutions();
            }
        };
        t.scheduleRepeating(5000);
    }

    private static class FlowPanelCell extends AbstractCell<FlowPanel> {
        @Override
        public void render(Context context, FlowPanel value, SafeHtmlBuilder sb) {
// value.redraw();
            sb.append(SafeHtmlUtils.fromTrustedString(value.getElement().getInnerHTML()));
// <div
// style="position: relative; height: 20px; width: 100%; left: 0px; top: 0px; right: 0px; bottom: 0px;"
// class="gwt-ProgressBar-shell" title="Ingestion Progress"><div style="height: 100%; width: 0%;"
// class="gwt-ProgressBar-bar"></div><div style="position: absolute; top: 0px; left: 449px;"
// class="gwt-ProgressBar-text gwt-ProgressBar-text-firstHalf">0%</div></div>
// sb.append(SafeHtmlUtils.fromTrustedString("<div style=\"position: relative; height: 20px; width: 100%; left: 0px; top: 0px; right: 0px; bottom: 0px;\" class=\"gwt-ProgressBar-shell\" title=\"Ingestion Progress\">"
// + value.getElement().getInnerHTML() + "</div>"));
        }
    }
}
