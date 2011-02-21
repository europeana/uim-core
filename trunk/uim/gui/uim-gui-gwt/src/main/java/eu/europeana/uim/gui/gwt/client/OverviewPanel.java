package eu.europeana.uim.gui.gwt.client;

import com.google.gwt.cell.client.DateCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.gwt.widgetideas.client.ProgressBar;
import eu.europeana.uim.gui.gwt.shared.Execution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The overview panel with current and past executions
 *
 * @author Manuel Bernhardt <bernhardt.manuel@gmail.com>
 */
public class OverviewPanel extends ScrollPanel {

    private final Application application;
    private OrchestrationServiceAsync orchestrationService = null;

    private VerticalPanel currentExecutionsPanel = null;
    private ExecutionDetailPanel currentExecutionsDetailPanel = null;
    private Map<Long, HorizontalPanel> progressBars = new HashMap<Long, HorizontalPanel>();

    private final List<Execution> pastExecutions = new ArrayList<Execution>();
    private final CellTable<Execution> pastExecutionsCellTable = new CellTable<Execution>();

    public OverviewPanel(OrchestrationServiceAsync orchestrationServiceAsync, final Application application) {
        this.orchestrationService = orchestrationServiceAsync;
        this.application = application;

        VerticalPanel executionList = new VerticalPanel();

        Label l = new Label("Current executions");
        l.setStyleName("title");
        executionList.add(l);

        currentExecutionsPanel = new VerticalPanel();
        currentExecutionsPanel.setWidth("800px");
        loadActiveExecutions();
        executionList.add(currentExecutionsPanel);

        executionList.add(new HTML("<br />"));
        executionList.add(new HTML("<br />"));

        Label l1 = new Label("Past executions");
        l1.setStyleName("title");
        executionList.add(l1);
        executionList.add(pastExecutionsCellTable);
        executionList.add(new HTML("<br />"));
        executionList.add(new Button("See all", new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                application.selectExecutionHistoryTab();
            }
        }));
        buildPastExecutionsCellTable(pastExecutionsCellTable, pastExecutions);
        updatePastExecutions();

        currentExecutionsDetailPanel = new ExecutionDetailPanel(orchestrationServiceAsync);
        currentExecutionsDetailPanel.setWidth("350px");

        HorizontalPanel h = new HorizontalPanel();
        h.add(executionList);
        h.add(currentExecutionsDetailPanel);
        add(h);


    }

    private void loadActiveExecutions() {
        // load active executions
        orchestrationService.getActiveExecutions(new AsyncCallback<List<Execution>>() {
            @Override
            public void onFailure(Throwable throwable) {
                throwable.printStackTrace();
            }

            @Override
            public void onSuccess(List<Execution> executions) {
                for (Execution e : executions) {
                    addExecution(e);
                }
            }
        });
    }

    public void addExecution(final Execution execution) {
        final ProgressBar bar = new ProgressBar(0, execution.getScheduled());
        bar.setTitle(execution.getName());
        bar.setTextVisible(true);
        HorizontalPanel p = new HorizontalPanel();
        p.setWidth("800px");
        p.add(new HTML(execution.getName()));
        p.add(bar);
        p.setCellWidth(bar, "400px");
        p.add(new Button("Details", new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                displayExecutionDetail(execution);
            }
        }));
        currentExecutionsPanel.add(p);
        progressBars.put(execution.getId(), p);
        displayExecutionDetail(execution);

        // poll the execution status every second
        Timer t = new Timer() {
            @Override
            public void run() {
                orchestrationService.getExecution(execution.getId(), new AsyncCallback<Execution>() {
                    @Override
                    public void onFailure(Throwable throwable) {
                        // TODO panic
                        throwable.printStackTrace();
                    }

                    @Override
                    public void onSuccess(Execution execution) {
                        bar.setProgress(execution.getProgress().getWorked());
                        bar.redraw();
                        if (execution.isDone()) {
                            cancel();
                            executionDone(execution);
                        }
                    }
                });

            }
        };
        t.scheduleRepeating(1000);
    }

    private void displayExecutionDetail(Execution execution) {
        currentExecutionsDetailPanel.display(execution);
    }

    private void executionDone(Execution e) {
        HorizontalPanel widget = progressBars.get(e.getId());
        currentExecutionsPanel.remove(widget);
        if (currentExecutionsDetailPanel.getCurrentExecution().getId().equals(e.getId())) {
            currentExecutionsDetailPanel.clear();
        }
        updatePastExecutions();
        application.getExecutionHistory().updatePastExecutions();
    }


    private void updatePastExecutions() {
        // load past executions
        orchestrationService.getPastExecutions(new AsyncCallback<List<Execution>>() {
            @Override
            public void onFailure(Throwable throwable) {
                throwable.printStackTrace();
            }

            @Override
            public void onSuccess(List<Execution> executions) {
                pastExecutions.clear();
                int i = 0;
                Collections.sort(executions);
                while (i < 10 && i < executions.size()) {
                    pastExecutions.add(executions.get(i));
                    i++;
                }
                CellTableUtils.updateCellTableData(pastExecutionsCellTable, pastExecutions);
            }
        });
    }


    public static void buildPastExecutionsCellTable(CellTable pastExecutionsCellTable, List<Execution> pastExecutions) {
        // cell table
        final ListDataProvider<Execution> dataProvider = new ListDataProvider<Execution>();
        dataProvider.setList(pastExecutions);
        dataProvider.addDataDisplay(pastExecutionsCellTable);

        final SingleSelectionModel<Execution> selectionModel = new SingleSelectionModel<Execution>();
        pastExecutionsCellTable.setSelectionModel(selectionModel);

        CellTableUtils.addColumn(pastExecutionsCellTable, new TextCell(), "Workflow", new CellTableUtils.GetValue<String, Execution>() {
            public String getValue(Execution execution) {
                return execution.getWorkflow();
            }
        });
        CellTableUtils.addColumn(pastExecutionsCellTable, new TextCell(), "Dataset", new CellTableUtils.GetValue<String, Execution>() {
            public String getValue(Execution execution) {
                return execution.getDataSet();
            }
        });
        DateTimeFormat dtf = DateTimeFormat.getFormat("dd.MM.yyyy 'at' HH:mm:ss");
        CellTableUtils.addColumn(pastExecutionsCellTable, new DateCell(dtf), "Start time", new CellTableUtils.GetValue<Date, Execution>() {
            public Date getValue(Execution execution) {
                return execution.getStartTime();
            }
        });
        CellTableUtils.addColumn(pastExecutionsCellTable, new DateCell(dtf), "End time", new CellTableUtils.GetValue<Date, Execution>() {
            public Date getValue(Execution execution) {
                return execution.getEndTime();
            }
        });

        /*
        addColumn(new ActionCell<Collection>(
                "Remove", new ActionCell.Delegate<Collection>() {
                    public void execute(Collection collection) {
                        collections.remove(collection);
                        updateCellTableData();
                    }
                }), "Action", new GetValue<Collection>() {

            public Collection getValue(Collection contact) {
                return contact;
            }
        });
        */
    }

}
