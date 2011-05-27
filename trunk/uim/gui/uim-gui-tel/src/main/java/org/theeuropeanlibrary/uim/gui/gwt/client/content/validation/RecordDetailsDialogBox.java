/* SimpleResourcejava - created on May 10, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.uim.gui.gwt.client.content.validation;

import org.theeuropeanlibrary.uim.gui.gwt.client.OrchestrationServiceAsync;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Shows raw, xml and search index representation of a record.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since May 10, 2011
 */
public class RecordDetailsDialogBox extends DialogBox {
    /**
     * Creates a new instance of this class.
     * 
     * @param recordId
     * @param orchestrationServiceAsync
     */
    public RecordDetailsDialogBox(long recordId,
                                  final OrchestrationServiceAsync orchestrationServiceAsync) {
        setText("Metadata Record Details");
        setWidth("400px");
        setGlassEnabled(true);
        setAnimationEnabled(true);
        center();

        VerticalPanel dialogContents = new VerticalPanel();
        dialogContents.setSpacing(4);
        dialogContents.setSize("100%", "100%");
        setWidget(dialogContents);

        TabPanel tabPanel = new TabPanel();
        tabPanel.setSize("100%", "100%");
        setWidget(tabPanel);

        final TextBox raw = new TextBox();
        final TextBox xml = new TextBox();
        final TextBox search = new TextBox();

        tabPanel.add(raw, "Raw");
        tabPanel.add(xml, "XML");
        tabPanel.add(search, "Search");

        Button closeButton = new Button("Close", new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                hide();
            }
        });
        dialogContents.setCellHorizontalAlignment(closeButton, HasHorizontalAlignment.ALIGN_RIGHT);
        dialogContents.add(closeButton);

        orchestrationServiceAsync.getRawRecord(recordId, new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable caught) {
                caught.printStackTrace();
            }

            @Override
            public void onSuccess(String result) {
                raw.setText(result);
            }
        });

        orchestrationServiceAsync.getXmlRecord(recordId, new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable caught) {
                caught.printStackTrace();
            }

            @Override
            public void onSuccess(String result) {
                xml.setText(result);
            }
        });

        orchestrationServiceAsync.getSearchRecord(recordId, new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable caught) {
                caught.printStackTrace();
            }

            @Override
            public void onSuccess(String result) {
                search.setText(result);
            }
        });
    }
}
