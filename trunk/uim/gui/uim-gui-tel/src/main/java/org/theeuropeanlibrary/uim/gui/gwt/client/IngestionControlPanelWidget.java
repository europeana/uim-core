package org.theeuropeanlibrary.uim.gui.gwt.client;

import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.LazyPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Base class for all panels shown in the ingestion cockpit.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Apr 27, 2011
 */
public abstract class IngestionControlPanelWidget extends LazyPanel implements
        HasValueChangeHandlers<String> {
    /**
     * Generic callback used for asynchronously loaded data.
     * 
     * @param <T>
     *            the data type
     */
    public static interface Callback<T> {
        /**
         * callback on errors
         */
        void onError();

        /**
         * @param value
         *            resulting object
         */
        void onSuccess(T value);
    }

    /**
     * Get the simple filename of a class.
     * 
     * @param c
     *            the class
     * @return simple class name
     */
    protected static String getSimpleName(Class<?> c) {
        String name = c.getName();
        return name.substring(name.lastIndexOf(".") + 1);
    }

    /**
     * The name of the example.
     */
    private final String               name;

    /**
     * A description of the example.
     */
    private final String               description;

    /**
     * The view that holds the name, description, and example.
     */
    private IngestionControlPanelWidgetView view;

    /**
     * Whether the demo widget has been initialized.
     */
    private boolean                    widgetInitialized;

    /**
     * Whether the demo widget is (asynchronously) initializing.
     */
    private boolean                    widgetInitializing;

    /**
     * Construct a {@link IngestionControlPanelWidget}.
     * 
     * @param name
     *            the name of the example
     * @param description
     *            a description of the example
     */
    public IngestionControlPanelWidget(String name, String description) {
        this.name = name;
        this.description = description;
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }

    @Override
    public void ensureWidget() {
        super.ensureWidget();
        ensureWidgetInitialized();
    }

    /**
     * Get the description of this example.
     * 
     * @return a description for this example
     */
    public final String getDescription() {
        return description;
    }

    /**
     * Get the name of this example to use as a title.
     * 
     * @return a name for this example
     */
    public final String getName() {
        return name;
    }

    /**
     * Check if the widget should have margins.
     * 
     * @return true to use margins, false to flush against edges
     */
    public boolean hasMargins() {
        return true;
    }

    /**
     * When the widget is first initialized, this method is called. If it returns a Widget, the
     * widget will be added as the first tab. Return null to disable the first tab.
     * 
     * @return the widget to add to the first tab
     */
    public abstract Widget onInitialize();

    /**
     * Called when initialization has completed and the widget has been added to the page.
     */
    public void onInitializeComplete() {
    }

    /**
     * @param callback
     */
    protected abstract void asyncOnInitialize(final AsyncCallback<Widget> callback);

    /**
     * Initialize this widget by creating the elements that should be added to the page.
     */
    @Override
    protected final Widget createWidget() {
        view = new IngestionControlPanelWidgetView(hasMargins());
        view.setName(getName());
        view.setDescription(getDescription());
        return view;
    }

    @Override
    protected void onLoad() {
        ensureWidget();
    }

    /**
     * Ensure that the demo widget has been initialized. Note that initialization can fail if there
     * is a network failure.
     */
    private void ensureWidgetInitialized() {
        if (widgetInitializing || widgetInitialized) { return; }

        widgetInitializing = true;

        asyncOnInitialize(new AsyncCallback<Widget>() {
            @Override
            public void onFailure(Throwable reason) {
                widgetInitializing = false;
                Window.alert("Failed to download code for this widget (" + reason + ")");
            }

            @Override
            public void onSuccess(Widget result) {
                widgetInitializing = false;
                widgetInitialized = true;

                Widget widget = result;
                if (widget != null) {
                    view.setExample(widget);
                }
                onInitializeComplete();
            }
        });
    }
}
