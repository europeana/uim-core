package eu.europeana.uim.gui.cp.client.monitoring;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import eu.europeana.uim.gui.cp.client.services.ExecutionServiceAsync;
import eu.europeana.uim.gui.cp.shared.ExecutionDTO;

/**
 * A form used for editing contacts.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Apr 27, 2011
 */
public class IngestionStatus extends Composite {
    private static Binder uiBinder = GWT.create(Binder.class);

    interface Binder extends UiBinder<Widget, IngestionStatus> {
    }

    @UiField
    TextBox              nameBox;
    @UiField
    TextBox              datasetBox;
    @UiField
    TextBox              workflowBox;
    @UiField
    LayoutPanel          leftPanel;
    ProgressBar          progressBar;
    @UiField
    TextBox              startTimeBox;
    @UiField
    TextBox              scheduledBox;
    @UiField
    TextBox              completedBox;
    @UiField
    TextBox              failedBox;
    @UiField
    TextBox              totalBox;
    @UiField
    Button               pauseButton;
    @UiField
    Button               cancelButton;

    private ExecutionDTO execution;

    /**
     * Creates a new instance of this class.
     * 
     * @param executionService
     */
    public IngestionStatus(final ExecutionServiceAsync executionService) {
        initWidget(uiBinder.createAndBindUi(this));

        nameBox.setReadOnly(true);
        datasetBox.setReadOnly(true);
        workflowBox.setReadOnly(true);
        startTimeBox.setReadOnly(true);
        scheduledBox.setReadOnly(true);
        completedBox.setReadOnly(true);
        failedBox.setReadOnly(true);
        totalBox.setReadOnly(true);

        progressBar = new ProgressBar(10, ProgressBar.SHOW_TEXT);
        progressBar.setTitle("Ingestion Progress");
        progressBar.setHeight("20px");
        progressBar.setWidth("100%");
        progressBar.setVisible(true);

        leftPanel.add(progressBar);
        leftPanel.setVisible(true);

        pauseButton.setText("Pause");
        pauseButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                if (execution != null) {
                    if (execution.isPaused()) {
                        executionService.resumeExecution(execution.getId(),
                                new AsyncCallback<Boolean>() {
                                    @Override
                                    public void onFailure(Throwable throwable) {
                                        throwable.printStackTrace();
                                    }

                                    @Override
                                    public void onSuccess(Boolean success) {
                                        pauseButton.setText("Resume");
                                    }
                                });
                    } else {
                        executionService.pauseExecution(execution.getId(),
                                new AsyncCallback<Boolean>() {
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
                }
            }
        });

        cancelButton.setText("Cancel");
        cancelButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                if (execution != null) {
                    executionService.cancelExecution(execution.getId(),
                            new AsyncCallback<Boolean>() {
                                @Override
                                public void onFailure(Throwable throwable) {
                                    throwable.printStackTrace();
                                }

                                @Override
                                public void onSuccess(Boolean success) {
// clearForm();
                                }
                            });
                }
            }
        });

        clearForm();
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

        progressBar.setProgress(execution.getProgress().getWorked() / execution.getProgress().getWork());

        if (execution.isPaused()) {
            pauseButton.setText("Resume");
        } else {
            pauseButton.setText("Pause");
        }
    }

    /**
     * @return current execution
     */
    public ExecutionDTO getExecution() {
        return execution;
    }

    /**
     * Clears the execution status widget.
     */
    public void clearForm() {
        nameBox.setText("");
        nameBox.setText("");
        datasetBox.setText("");
        workflowBox.setText("");
        startTimeBox.setText("");
        scheduledBox.setText("");
        completedBox.setText("");
        failedBox.setText("");
        totalBox.setText("");
        progressBar.setProgress(0);
    }
}
