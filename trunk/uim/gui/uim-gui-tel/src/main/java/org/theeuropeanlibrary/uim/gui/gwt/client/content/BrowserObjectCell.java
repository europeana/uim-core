/* BrowserObjectCell.java - created on May 20, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.uim.gui.gwt.client.content;

import org.theeuropeanlibrary.uim.gui.gwt.shared.WorkflowDTO;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PopupPanel;

/**
 * Customized {@link AbstractCell} used for {@link BrowserObject}s.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since May 20, 2011
 */
public class BrowserObjectCell extends AbstractCell<BrowserObject> {
    private final PopupPanel panel;
    private final Timer      showTimer;

    /**
     * Creates a new instance of this class.
     */
    public BrowserObjectCell() {
        super("mouseover", "mouseout");
        panel = new PopupPanel();
        panel.hide();
        showTimer = new Timer() {
            @Override
            public void run() {
                panel.show();
            }
        };
    }

    @Override
    public void render(Context context, BrowserObject value, SafeHtmlBuilder sb) {
        if (value != null) {
            SafeHtml html = SafeHtmlUtils.fromString(value.getName());
            sb.append(html);
        }
    }

    @Override
    public void onBrowserEvent(Context context, Element parent, BrowserObject value,
            NativeEvent event, ValueUpdater<BrowserObject> valueUpdater) {
        super.onBrowserEvent(context, parent, value, event, valueUpdater);

        if (value.getWrappedObject() != null && value.getWrappedObject() instanceof WorkflowDTO) {
            if ("mouseover".equals(event.getType())) {
                panel.clear();
                panel.hide();
                HTML contents = new HTML(((WorkflowDTO)value.getWrappedObject()).getDescription());
                panel.add(contents);
                panel.setPopupPosition(event.getClientX(), event.getClientY());
                showTimer.schedule(2000);
            } else if ("mouseout".equals(event.getType())) {
                panel.hide();
                showTimer.cancel();
            }
        }
    }
}
