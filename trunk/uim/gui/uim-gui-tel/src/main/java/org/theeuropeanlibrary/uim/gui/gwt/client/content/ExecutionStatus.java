package org.theeuropeanlibrary.uim.gui.gwt.client.content;

import org.theeuropeanlibrary.uim.gui.gwt.client.OrchestrationServiceAsync;
import org.theeuropeanlibrary.uim.gui.gwt.shared.ExecutionDTO;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.widgetideas.client.ProgressBar;

/**
 * A form used for editing contacts.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Apr 27, 2011
 */
public class ExecutionStatus extends Composite {
    private static Binder uiBinder = GWT.create(Binder.class);

    interface Binder extends UiBinder<Widget, ExecutionStatus> {
    }

    @UiField
    TextBox                                 nameBox;
    @UiField
    TextBox                                 datasetBox;
    @UiField
    TextBox                                 workflowBox;
    @UiField
    LayoutPanel                             leftPanel;
    ProgressBar                             progressBar;
    @UiField
    TextBox                                 startTimeBox;
    @UiField
    TextBox                                 scheduledBox;
    @UiField
    TextBox                                 completedBox;
    @UiField
    TextBox                                 failedBox;
    @UiField
    TextBox                                 totalBox;
    @UiField
    Button                                  pauseButton;
    @UiField
    Button                                  cancelButton;

    private ExecutionDTO                    execution;
    private final OrchestrationServiceAsync orchestrationService;

    /**
     * Creates a new instance of this class.
     * 
     * @param orchestrationService
     */
    public ExecutionStatus(OrchestrationServiceAsync orchestrationService) {
        this.orchestrationService = orchestrationService;
        initWidget(uiBinder.createAndBindUi(this));

        nameBox.setReadOnly(true);
        datasetBox.setReadOnly(true);
        workflowBox.setReadOnly(true);
        startTimeBox.setReadOnly(true);
        scheduledBox.setReadOnly(true);
        completedBox.setReadOnly(true);
        failedBox.setReadOnly(true);
        totalBox.setReadOnly(true);

        progressBar = new ProgressBar();
        progressBar.setTitle("Ingestion Progress");
        progressBar.setTextVisible(true);
        progressBar.setHeight("20px");
        progressBar.setWidth("100%");
        progressBar.setVisible(true);

        leftPanel.add(progressBar);
        leftPanel.setVisible(true);

        pauseButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                if (execution.isPaused()) {
                    pauseExecution();
                } else {
                    resumeExecution();
                }
            }
        });

        cancelButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                cancelExecution();

            }
        });
    }

    private void resumeExecution() {
        orchestrationService.pauseExecution(execution.getId(), new AsyncCallback<Boolean>() {
            @Override
            public void onFailure(Throwable throwable) {
                throwable.printStackTrace();
            }

            @Override
            public void onSuccess(Boolean success) {
                pauseButton.setText("Pause");
            }
        });
    }

    private void pauseExecution() {
        orchestrationService.resumeExecution(execution.getId(), new AsyncCallback<Boolean>() {
            @Override
            public void onFailure(Throwable throwable) {
                throwable.printStackTrace();
            }

            @Override
            public void onSuccess(Boolean success) {
                pauseButton.setText("Resume");
            }
        });

    }

    private void cancelExecution() {
        orchestrationService.cancelExecution(execution.getId(), new AsyncCallback<Boolean>() {
            @Override
            public void onFailure(Throwable throwable) {
                throwable.printStackTrace();
            }

            @Override
            public void onSuccess(Boolean success) {
            }
        });
    }

    /**
     * @param execution
     */
    public void setExecution(final ExecutionDTO execution) {
        this.execution = execution;

        DateTimeFormat dtf = DateTimeFormat.getFormat("dd.MM.yyyy 'at' HH:mm:ss");
        nameBox.setText(execution.getName());
        datasetBox.setText(execution.getDataSet());
        workflowBox.setText(execution.getWorkflow());
        startTimeBox.setText(dtf.format(execution.getStartTime()));
        scheduledBox.setText("" + execution.getScheduled());
        completedBox.setText("" + execution.getCompleted());
        failedBox.setText("" + execution.getFailure());
        totalBox.setText("" + execution.getProgress().getWork());

        progressBar.setMinProgress(0);
        progressBar.setMaxProgress(execution.getProgress().getWork());
        progressBar.setProgress(execution.getProgress().getWorked());
        progressBar.redraw();

        if (execution.isPaused()) {
            pauseButton.setText("Resume");
        } else {
            pauseButton.setText("Pause");
        }

        Timer t = new Timer() {
            @Override
            public void run() {
                orchestrationService.getExecution(execution.getId(),
                        new AsyncCallback<ExecutionDTO>() {
                            @Override
                            public void onFailure(Throwable throwable) {
                                // TODO panic
                                throwable.printStackTrace();
                            }

                            @Override
                            public void onSuccess(ExecutionDTO execution) {
                                DateTimeFormat dtf = DateTimeFormat.getFormat("dd.MM.yyyy 'at' HH:mm:ss");
                                nameBox.setText(execution.getName());
                                datasetBox.setText(execution.getDataSet());
                                workflowBox.setText(execution.getWorkflow());
                                startTimeBox.setText(dtf.format(execution.getStartTime()));
                                scheduledBox.setText("" + execution.getScheduled());
                                completedBox.setText("" + execution.getCompleted());
                                failedBox.setText("" + execution.getFailure());
                                totalBox.setText("" + execution.getProgress().getWork());

                                progressBar.setProgress(execution.getProgress().getWorked());
                                progressBar.redraw();
                            }
                        });
            }
        };
        t.scheduleRepeating(5000);
    }
}
