/* LazySimpleLayoutPanel.java - created on Sep 15, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.gui.cp.client;

import com.google.gwt.user.client.ui.SimpleLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Convenience class to help lazy loading of {@link SimpleLayoutPanel}.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Sep 15, 2011
 */
public abstract class LazySimpleLayoutPanel extends SimpleLayoutPanel {
    /**
     * Creates a new instance of this class.
     */
    public LazySimpleLayoutPanel() {
        // nothing to do
    }

    /**
     * Create the widget contained within the {@link LazySimpleLayoutPanel}.
     * 
     * @return the lazy widget
     */
    protected abstract Widget createWidget();

    /**
     * Ensures that the widget has been created by calling {@link #createWidget} if
     * {@link #getWidget} returns <code>null</code>. Typically it is not necessary to call this
     * directly, as it is called as a side effect of a <code>setVisible(true)</code> call.
     */
    public void ensureWidget() {
        Widget widget = getWidget();
        if (widget == null) {
            widget = createWidget();
            setWidget(widget);
        }
    }

    @Override
    public void setVisible(boolean visible) {
        if (visible) {
            ensureWidget();
        }
        super.setVisible(visible);
    }
}
