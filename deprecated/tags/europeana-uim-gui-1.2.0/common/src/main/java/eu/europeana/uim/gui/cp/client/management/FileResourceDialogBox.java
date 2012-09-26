/* SimpleResourcejava - created on May 10, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.gui.cp.client.management;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.HasKeyboardPagingPolicy.KeyboardPagingPolicy;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SimpleKeyProvider;

import eu.europeana.uim.gui.cp.client.services.ResourceServiceAsync;
import eu.europeana.uim.gui.cp.shared.ParameterDTO;

/**
 * File based resources are managed with this dialog.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since May 10, 2011
 */
public class FileResourceDialogBox extends DialogBox {
    private final ResourceServiceAsync resourceService;
    private final List<String>         activeFileNames = new ArrayList<String>();

    private CellList<String>           cellList;

    /**
     * Creates a new instance of this class.
     * 
     * @param orchestrationService
     * @param parameter
     * @param callback
     */
    public FileResourceDialogBox(ResourceServiceAsync orchestrationService,
                                 final ParameterDTO parameter,
                                 final ResourceSettingCallback callback) {
        this.resourceService = orchestrationService;

        setText("Update File Resource");
        setWidth("400px");
        setGlassEnabled(true);
        setAnimationEnabled(true);
        center();

        VerticalPanel dialogContents = new VerticalPanel();
        dialogContents.setSpacing(4);
        dialogContents.setSize("100%", "100%");
        setWidget(dialogContents);
        dialogContents.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

        HorizontalPanel key = new HorizontalPanel();
        key.setSpacing(4);
        key.setWidth("100%");
        Label keyLabel = new Label("Resource Key");
        keyLabel.setWidth("100%");
        key.add(keyLabel);
        key.setCellHorizontalAlignment(keyLabel, HasHorizontalAlignment.ALIGN_LEFT);
        Label keyValue = new Label(parameter.getKey());
        keyValue.setWidth("100%");
        key.add(keyValue);
        key.setCellHorizontalAlignment(keyValue, HasHorizontalAlignment.ALIGN_LEFT);
        dialogContents.add(key);

        DecoratorPanel decPanel = new DecoratorPanel();
        ScrollPanel scrollPanel = new ScrollPanel();
        scrollPanel.setSize("400px", "400px");

        cellList = new CellList<String>(new TextCell(), new SimpleKeyProvider<String>());
        cellList.setSize("100%", "100%");
        cellList.setPageSize(30);
        cellList.setKeyboardPagingPolicy(KeyboardPagingPolicy.INCREASE_RANGE);
        cellList.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
        cellList.setTitle("File Resource List");

        final ListDataProvider<String> workflowProvider = new ListDataProvider<String>();
        workflowProvider.setList(activeFileNames);
        workflowProvider.addDataDisplay(cellList);

        final MultiSelectionModel<String> selectionModel = new MultiSelectionModel<String>(
                new SimpleKeyProvider<String>());
        if (parameter.getValues() != null && parameter.getValues().length > 0) {
            for (String str : parameter.getValues()) {
                selectionModel.setSelected(str, true);
            }
        }
        cellList.setSelectionModel(selectionModel);

        scrollPanel.add(cellList);
        decPanel.add(scrollPanel);
        dialogContents.add(decPanel);

        final FormPanel form = new FormPanel();
        form.setWidth("100%");
        form.setAction(GWT.getModuleBaseURL() + "fileupload");
        form.setEncoding(FormPanel.ENCODING_MULTIPART);
        form.setMethod(FormPanel.METHOD_POST);

        HorizontalPanel panel = new HorizontalPanel();
        panel.setWidth("100%");
        form.setWidget(panel);

        final FileUpload upload = new FileUpload();
        upload.setName("uploadFormElement");
// Element ee = upload.getElement();
// DOM.setAttribute(ee, "size", "40");
        panel.add(upload);

        panel.add(new Button("Upload", new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                form.submit();
            }
        }));

        form.addSubmitHandler(new FormPanel.SubmitHandler() {
            @Override
            public void onSubmit(SubmitEvent event) {
                if (upload.getFilename().length() == 0) {
                    Window.alert("The text box must not be empty");
                    event.cancel();
                }
            }
        });

        form.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {
            @Override
            public void onSubmitComplete(SubmitCompleteEvent event) {
                Window.alert(event.getResults());
                updateFileNames();
            }
        });

        dialogContents.add(form);

        HorizontalPanel buttons = new HorizontalPanel();
        buttons.setSpacing(4);
        buttons.setWidth("100%");
        Button okButton = new Button("Ok", new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                Set<String> vals = selectionModel.getSelectedSet();
                if (vals.size() == 0) {
                    parameter.setValues(null);
                } else {
                    parameter.setValues(vals.toArray(new String[vals.size()]));
                }

                callback.changed(parameter);

                hide();
            }
        });
        buttons.add(okButton);
        buttons.setCellHorizontalAlignment(okButton, HasHorizontalAlignment.ALIGN_RIGHT);

        Button cancelButton = new Button("Cancel", new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                hide();
            }
        });
        buttons.add(cancelButton);
        buttons.setCellHorizontalAlignment(cancelButton, HasHorizontalAlignment.ALIGN_LEFT);
        dialogContents.add(buttons);

        updateFileNames();
    }

    /**
     * Retrieve parameters for given settings.
     */
    public void updateFileNames() {
        resourceService.getResourceFileNames(new AsyncCallback<List<String>>() {
            @Override
            public void onFailure(Throwable throwable) {
                throwable.printStackTrace();
            }

            @Override
            public void onSuccess(List<String> fileNames) {
                activeFileNames.clear();
                activeFileNames.addAll(fileNames);
                cellList.setRowData(0, activeFileNames);
                cellList.setRowCount(activeFileNames.size());
            }
        });
    }
}
