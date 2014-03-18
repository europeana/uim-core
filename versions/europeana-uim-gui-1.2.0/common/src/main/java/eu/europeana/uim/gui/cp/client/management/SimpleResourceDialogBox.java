/* SimpleResourcejava - created on May 10, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.gui.cp.client.management;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import eu.europeana.uim.gui.cp.shared.ParameterDTO;

/**
 * Simple value based resources are managed with this dialog.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since May 10, 2011
 */
public class SimpleResourceDialogBox extends DialogBox {
    /**
     * Creates a new instance of this class.
     * 
     * @param parameter
     * @param callback
     */
    public SimpleResourceDialogBox(final ParameterDTO parameter,
                                   final ResourceSettingCallback callback) {
        setText("Update Resource");
        setWidth("400px");
        setGlassEnabled(true);
        setAnimationEnabled(true);
        center();

        VerticalPanel dialogContents = new VerticalPanel();
        dialogContents.setSpacing(4);
        dialogContents.setSize("100%", "100%");
        setWidget(dialogContents);

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

        HorizontalPanel values = new HorizontalPanel();
        values.setSpacing(4);
        values.setWidth("100%");
        Label valueLabel = new Label("Resource Values");
        valueLabel.setWidth("100%");
        values.add(valueLabel);
        values.setCellHorizontalAlignment(valueLabel, HasHorizontalAlignment.ALIGN_LEFT);

        final List<TextBox> boxes = new ArrayList<TextBox>();
        VerticalPanel valuesPanel = new VerticalPanel();
        valuesPanel.setWidth("100%");
        if (parameter.getValues() != null) {
            for (String value : parameter.getValues()) {
                TextBox box = new TextBox();
                box.setWidth("100%");
                box.setText(value);
                box.setReadOnly(false);
                valuesPanel.add(box);
                boxes.add(box);
            }
        }
        TextBox box = new TextBox();
        box.setReadOnly(false);
        box.setWidth("100%");
        box.setFocus(true);
        boxes.add(box);
        valuesPanel.add(box);
        values.add(valuesPanel);
        values.setCellHorizontalAlignment(valuesPanel, HasHorizontalAlignment.ALIGN_LEFT);

        dialogContents.add(values);

        HorizontalPanel buttons = new HorizontalPanel();
        buttons.setSpacing(4);
        buttons.setWidth("100%");
        Button okButton = new Button("Ok", new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                List<String> vals = new ArrayList<String>();
                for (TextBox box : boxes) {
                    if (box.getText().length() > 0) {
                        vals.add(box.getText());
                    }
                }
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
    }
}
