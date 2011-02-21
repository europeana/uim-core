package eu.europeana.uim.gui.gwt.client;

import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ScrollPanel;
import eu.europeana.uim.gui.gwt.shared.Execution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Manuel Bernhardt <bernhardt.manuel@gmail.com>
 */
public class ExecutionHistoryPanel extends ScrollPanel {

    private OrchestrationServiceAsync orchestrationServiceAsync;

    private final List<Execution> pastExecutions = new ArrayList<Execution>();

    private final CellTable<Execution> pastExecutionsCellTable = new CellTable<Execution>();

    public ExecutionHistoryPanel(OrchestrationServiceAsync orchestrationServiceAsync) {
        this.orchestrationServiceAsync = orchestrationServiceAsync;
        setWidth("1000px");
        setHeight("450px");
        OverviewPanel.buildPastExecutionsCellTable(pastExecutionsCellTable, pastExecutions);
        add(pastExecutionsCellTable);
        updatePastExecutions();
    }

    public void updatePastExecutions() {
        orchestrationServiceAsync.getPastExecutions(new AsyncCallback<List<Execution>>() {
            @Override
            public void onFailure(Throwable caught) {
                caught.printStackTrace();
            }

            @Override
            public void onSuccess(List<Execution> result) {
                pastExecutions.clear();
                pastExecutions.addAll(result);
                Collections.sort(result);
                CellTableUtils.updateCellTableData(pastExecutionsCellTable, pastExecutions);
            }
        });
    }
}
