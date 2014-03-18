package eu.europeana.uim.gui.cp.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * A view of a {@link IngestionWidget}.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Apr 27, 2011
 */
public class IngestionWidgetView extends Composite {
    interface IngestionCockpitWidgetViewUiBinder extends
            UiBinder<Widget, IngestionWidgetView> {
    }

    private static IngestionCockpitWidgetViewUiBinder uiBinder = GWT.create(IngestionCockpitWidgetViewUiBinder.class);

    @UiField
    Element                                           descElem;

    @UiField
    SimplePanel                                       examplePanel;

    @UiField
    Element                                           nameElem;

    /**
     * Creates a new instance of this class.
     * 
     * @param useMargins
     */
    public IngestionWidgetView(boolean useMargins) {
        initWidget(uiBinder.createAndBindUi(this));
        if (useMargins) {
            examplePanel.getElement().getStyle().setMarginLeft(10.0, Unit.PX);
            examplePanel.getElement().getStyle().setMarginRight(10.0, Unit.PX);
        }
    }

    /**
     * @param html
     */
    public void setDescription(String html) {
        descElem.setInnerHTML(html);
    }

    /**
     * @param widget
     */
    public void setExample(Widget widget) {
        examplePanel.setWidget(widget);
    }

    /**
     * @param text
     */
    public void setName(String text) {
        nameElem.setInnerText(text);
    }
}
