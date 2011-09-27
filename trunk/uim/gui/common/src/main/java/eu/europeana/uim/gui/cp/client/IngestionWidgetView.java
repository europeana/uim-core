package eu.europeana.uim.gui.cp.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.SimpleLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * A view of a {@link IngestionWidget}.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Apr 27, 2011
 */
public class IngestionWidgetView extends ResizeComposite {
    interface IngestionCockpitWidgetViewUiBinder extends UiBinder<Widget, IngestionWidgetView> {
    }

    private static IngestionCockpitWidgetViewUiBinder uiBinder = GWT.create(IngestionCockpitWidgetViewUiBinder.class);
    
    @UiField
    Element                                           nameElem;
    
    @UiField
    Element                                           descElem;

    @UiField
    SimpleLayoutPanel                                 contentPanel;

    /**
     * Creates a new instance of this class.
     * 
     * @param useMargins
     */
    public IngestionWidgetView(boolean useMargins) {
        initWidget(uiBinder.createAndBindUi(this));
        if (useMargins) {
            contentPanel.getElement().getStyle().setMarginLeft(10.0, Unit.PX);
            contentPanel.getElement().getStyle().setMarginRight(10.0, Unit.PX);
        }
    }
    
    /**
     * @param text
     */
    public void setName(String text) {
        nameElem.setInnerText(text);
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
    public void setContent(Widget widget) {
        contentPanel.setWidget(widget);
    }
}
