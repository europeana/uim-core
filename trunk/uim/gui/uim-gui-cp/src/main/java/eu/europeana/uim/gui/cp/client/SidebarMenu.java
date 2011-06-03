/* CockpitTreeViewModel.java - created on Apr 27, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.gui.cp.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.prefetch.RunAsyncCode;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.gwt.view.client.TreeViewModel;

/**
 * The {@link TreeViewModel} used by the main menu.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Apr 27, 2011
 */
public class SidebarMenu implements TreeViewModel {
// private final OrchestrationServiceAsync orchestrationService =
// (OrchestrationServiceAsync)GWT.create(OrchestrationService.class);

    /**
     * The top level categories.
     */
    private final ListDataProvider<SidebarMenuEntry>                 categories                 = new ListDataProvider<SidebarMenuEntry>();

    /**
     * A mapping of {@link IngestionWidget}s to their associated categories.
     */
    private final Map<IngestionWidget, SidebarMenuEntry> contentCategory            = new HashMap<IngestionWidget, SidebarMenuEntry>();

    /**
     * The cell used to render examples.
     */
    private final IngestionCockpitWidgetCell                         ingestionCockpitWidgetCell = new IngestionCockpitWidgetCell();

    /**
     * A mapping of history tokens to their associated {@link IngestionWidget}.
     */
    private final Map<String, IngestionWidget>           contentToken               = new HashMap<String, IngestionWidget>();

    /**
     * The selection model used to select examples.
     */
    private final SelectionModel<IngestionWidget>        selectionModel;

    /**
     * Creates a new instance of this class.
     * 
     * @param selectionModel
     */
    public SidebarMenu(SingleSelectionModel<IngestionWidget> selectionModel) {
        this.selectionModel = selectionModel;
    }

    /**
     * Adds a new menu entry under the given main menu (creates it if it is not there yet).
     * 
     * @param menu
     *            category of main selection
     * @param widget
     *            to be added widget
     * @param runAsyncCode
     *            asynchronous code
     */
    public void addMenuEntry(String menu, IngestionWidget widget,
            RunAsyncCode runAsyncCode) {
        SidebarMenuEntry main = null;
        List<SidebarMenuEntry> catList = categories.getList();
        for (SidebarMenuEntry cat : catList) {
            if (cat.getName().equals(menu)) {
                main = cat;
            }
        }
        if (main == null) {
            main = new SidebarMenuEntry(menu);
            catList.add(main);
        }
        main.addExample(widget, runAsyncCode);
    }

    /**
     * Get the {@link SidebarMenuEntry} associated with a widget.
     * 
     * @param widget
     *            the {@link IngestionWidget}
     * @return the associated {@link SidebarMenuEntry}
     */
    public SidebarMenuEntry getCategoryForContentWidget(IngestionWidget widget) {
        return contentCategory.get(widget);
    }

    /**
     * Get the content widget associated with the specified history token.
     * 
     * @param token
     *            the history token
     * @return the associated {@link IngestionWidget}
     */
    public IngestionWidget getContentWidgetForToken(String token) {
        return contentToken.get(token);
    }

    @Override
    public <T> NodeInfo<?> getNodeInfo(T value) {
        if (value == null) {
            return new DefaultNodeInfo<SidebarMenuEntry>(categories, new CategoryCell());
        } else if (value instanceof SidebarMenuEntry) {
            SidebarMenuEntry category = (SidebarMenuEntry)value;
            return category.getNodeInfo();
        }
        return null;
    }

    @Override
    public boolean isLeaf(Object value) {
        return value != null && !(value instanceof SidebarMenuEntry);
    }

    private static class CategoryCell extends AbstractCell<SidebarMenuEntry> {
        @Override
        public void render(Context context, SidebarMenuEntry value, SafeHtmlBuilder sb) {
            if (value != null) {
                sb.appendEscaped(value.getName());
            }
        }
    }

    private static class IngestionCockpitWidgetCell extends
            AbstractCell<IngestionWidget> {
        @Override
        public void render(Context context, IngestionWidget value, SafeHtmlBuilder sb) {
            if (value != null) {
                sb.appendEscaped(value.getName());
            }
        }
    }

    /**
     * A top level category in the tree.
     * 
     * @author Markus Muhr (markus.muhr@kb.nl)
     * @since Apr 27, 2011
     */
    public class SidebarMenuEntry {
        private final ListDataProvider<IngestionWidget> examples    = new ListDataProvider<IngestionWidget>();
        private final String                                        name;
        private NodeInfo<IngestionWidget>               nodeInfo;
        private final List<RunAsyncCode>                            splitPoints = new ArrayList<RunAsyncCode>();

        /**
         * Creates a new instance of this class.
         * 
         * @param name
         */
        private SidebarMenuEntry(String name) {
            this.name = name;
        }

        /**
         * @param example
         * @param splitPoint
         */
        public void addExample(IngestionWidget example, RunAsyncCode splitPoint) {
            examples.getList().add(example);
            if (splitPoint != null) {
                splitPoints.add(splitPoint);
            }
            contentCategory.put(example, this);
            contentToken.put(IngestionControlPanel.getContentWidgetToken(example), example);
        }

        /**
         * @return name
         */
        public String getName() {
            return name;
        }

        /**
         * Get the node info for the examples under this category.
         * 
         * @return the node info
         */
        public NodeInfo<IngestionWidget> getNodeInfo() {
            if (nodeInfo == null) {
                nodeInfo = new DefaultNodeInfo<IngestionWidget>(examples,
                        ingestionCockpitWidgetCell, selectionModel, null);
            }
            return nodeInfo;
        }

        /**
         * Get the list of split points to prefetch for this category.
         * 
         * @return the list of classes in this category
         */
        public Iterable<RunAsyncCode> getSplitPoints() {
            return splitPoints;
        }
    }
}
