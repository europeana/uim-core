package eu.europeana.uim.gui.gwt.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ScrollPanel;

import eu.europeana.uim.gui.gwt.shared.ExecutionDTO;

/**
 * @author Manuel Bernhardt <bernhardt.manuel@gmail.com>
 */
public class ExecutionHistoryPanel extends ScrollPanel {

    private OrchestrationServiceAsync orchestrationServiceAsync;

    private final List<ExecutionDTO> pastExecutions = new ArrayList<ExecutionDTO>();

    private final CellTable<ExecutionDTO> pastExecutionsCellTable = new CellTable<ExecutionDTO>();

    public ExecutionHistoryPanel(OrchestrationServiceAsync orchestrationServiceAsync) {
        this.orchestrationServiceAsync = orchestrationServiceAsync;
        setWidth("1000px");
        setHeight("450px");
        OverviewPanel.buildPastExecutionsCellTable(pastExecutionsCellTable, pastExecutions);
        add(pastExecutionsCellTable);
        updatePastExecutions();
    }

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
                Collections.sort(result);
                CellTableUtils.updateCellTableData(pastExecutionsCellTable, pastExecutions);
            }
        });
    }
}
