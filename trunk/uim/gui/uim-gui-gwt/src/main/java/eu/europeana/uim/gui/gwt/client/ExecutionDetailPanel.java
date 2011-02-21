package eu.europeana.uim.gui.gwt.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import eu.europeana.uim.gui.gwt.shared.Execution;
import eu.europeana.uim.gui.gwt.shared.StepStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Manuel Bernhardt <bernhardt.manuel@gmail.com>
 */
public class ExecutionDetailPanel extends VerticalPanel {

    private OrchestrationServiceAsync orchestrationServiceAsync;

    private SimplePanel statusPanel = new SimplePanel();

    private Execution currentExecution = null;

    public ExecutionDetailPanel(OrchestrationServiceAsync orchestrationServiceAsync) {
        this.orchestrationServiceAsync = orchestrationServiceAsync;
    }

    public Execution getCurrentExecution() {
        return currentExecution;
    }

    public void display(final Execution execution) {

        clear();

        Label l = new Label("Execution detail");
        l.setStyleName("title");
        add(l);
        Grid g = buildDetail(execution);
        add(g);
        add(new HTML("<br />"));
        add(new HTML("<br />"));

        Label l1 = new Label("Workflow processor detail");
        l1.setStyleName("title");
        Button refresh = new Button("Refresh", new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                updateStatus(execution);
            }
        });
        refresh.setStyleName("uimButton");

        HorizontalPanel p = new HorizontalPanel();
        p.add(l1);
        p.add(refresh);
        add(p);

        add(statusPanel);

        updateStatus(execution);
    }

    private void updateStatus(Execution execution) {
        orchestrationServiceAsync.getStatus(execution.getWorkflow(), new AsyncCallback<List<StepStatus>>() {
            @Override
            public void onFailure(Throwable caught) {
                caught.printStackTrace();
            }

            @Override
            public void onSuccess(List<StepStatus> result) {
                displayStatus(result);
            }
        });
    }

    private void displayStatus(final List<StepStatus> statuses) {
        statusPanel.clear();

        Grid s = new Grid(statuses.size() + 1, 5);
        String[] labels = new String[]{"Parallel execution group", "Step/Plugin", "Successes", "Failures", "Queue size"};

        for (int i = 0; i < labels.length; i++) {
            Label l = new Label(labels[i]);
            l.setStyleName("uimLabel");
            s.setWidget(0, i, l);
        }

        for (int i = 0; i < statuses.size(); i++) {
            Integer group = group(statuses.get(i).getParentName());
            s.setText(i + 1, 0, group == -1 ? "" : group.toString());
            s.setText(i + 1, 1, statuses.get(i).getStep());
            s.setText(i + 1, 2, Integer.toString(statuses.get(i).successes()));
            s.setText(i + 1, 3, Integer.toString(statuses.get(i).failures()));
            s.setText(i + 1, 4, Integer.toString(statuses.get(i).queueSize()));
        }

        statusPanel.add(s);
    }

    private Map<String, Integer> containers = new HashMap<String, Integer>();

    private Integer group(String parentName) {
        if (parentName == null) return -1;
        Integer i = containers.get(parentName);
        if (i == null) {
            i = containers.size();
            containers.put(parentName, i);
        }
        return i;
    }

    private Grid buildDetail(Execution execution) {
        this.currentExecution = execution;
        Grid g = new Grid(4, 2);
        String[] labels = new String[]{"Id", "Name", "Start time", "Total records"};
        DateTimeFormat format = DateTimeFormat.getFormat("dd.MM.yyyy 'at' HH:mm:ss");
        String[] data = new String[]{execution.getId().toString(), execution.getName(), format.format(execution.getStartTime()), "" + execution.getScheduled()};
        for (int i = 0; i < labels.length; i++) {
            Label label = new Label(labels[i]);
            label.setStyleName("uimLabel");
            g.setWidget(i, 0, label);
            g.setText(i, 1, data[i]);
        }
        return g;
    }
}
