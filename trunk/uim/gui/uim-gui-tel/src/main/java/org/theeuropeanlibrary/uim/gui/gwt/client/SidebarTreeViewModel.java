/* CockpitTreeViewModel.java - created on Apr 27, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.uim.gui.gwt.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.theeuropeanlibrary.uim.gui.gwt.client.content.management.IngestionTriggerWidget;
import org.theeuropeanlibrary.uim.gui.gwt.client.content.management.ResourceManagementWidget;
import org.theeuropeanlibrary.uim.gui.gwt.client.content.monitoring.IngestionDetailWidget;
import org.theeuropeanlibrary.uim.gui.gwt.client.content.monitoring.IngestionHistoryWidget;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
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
public class SidebarTreeViewModel implements TreeViewModel {
    private final OrchestrationServiceAsync orchestrationService = (OrchestrationServiceAsync)GWT.create(OrchestrationService.class);

    /**
     * The cell used to render categories.
     */
    private static class CategoryCell extends AbstractCell<Category> {
        @Override
        public void render(Context context, Category value, SafeHtmlBuilder sb) {
            if (value != null) {
                sb.appendEscaped(value.getName());
            }
        }
    }

    /**
     * The cell used to render examples.
     */
    private static class IngestionCockpitWidgetCell extends
            AbstractCell<IngestionControlPanelWidget> {
        @Override
        public void render(Context context, IngestionControlPanelWidget value, SafeHtmlBuilder sb) {
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
    public class Category {
        private final ListDataProvider<IngestionControlPanelWidget> examples    = new ListDataProvider<IngestionControlPanelWidget>();
        private final String                                        name;
        private NodeInfo<IngestionControlPanelWidget>               nodeInfo;
        private final List<RunAsyncCode>                            splitPoints = new ArrayList<RunAsyncCode>();

        /**
         * Creates a new instance of this class.
         * 
         * @param name
         */
        public Category(String name) {
            this.name = name;
        }

        /**
         * @param example
         * @param splitPoint
         */
        public void addExample(IngestionControlPanelWidget example, RunAsyncCode splitPoint) {
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
        public NodeInfo<IngestionControlPanelWidget> getNodeInfo() {
            if (nodeInfo == null) {
                nodeInfo = new DefaultNodeInfo<IngestionControlPanelWidget>(examples,
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

    /**
     * The top level categories.
     */
    private final ListDataProvider<Category>                  categories                 = new ListDataProvider<Category>();

    /**
     * A mapping of {@link IngestionControlPanelWidget}s to their associated categories.
     */
    private final Map<IngestionControlPanelWidget, Category>  contentCategory            = new HashMap<IngestionControlPanelWidget, Category>();

    /**
     * The cell used to render examples.
     */
    private final IngestionCockpitWidgetCell                  ingestionCockpitWidgetCell = new IngestionCockpitWidgetCell();

    /**
     * A mapping of history tokens to their associated {@link IngestionControlPanelWidget}.
     */
    private final Map<String, IngestionControlPanelWidget>    contentToken               = new HashMap<String, IngestionControlPanelWidget>();

    /**
     * The selection model used to select examples.
     */
    private final SelectionModel<IngestionControlPanelWidget> selectionModel;

    /**
     * Creates a new instance of this class.
     * 
     * @param selectionModel
     */
    public SidebarTreeViewModel(SingleSelectionModel<IngestionControlPanelWidget> selectionModel) {
        this.selectionModel = selectionModel;

        List<Category> catList = categories.getList();

        {
            Category category = new Category("Monitoring");
            catList.add(category);
            category.addExample(new IngestionDetailWidget(orchestrationService),
                    RunAsyncCode.runAsyncCode(IngestionDetailWidget.class));
            category.addExample(new IngestionHistoryWidget(orchestrationService),
                    RunAsyncCode.runAsyncCode(IngestionHistoryWidget.class));
        }

        {
            Category category = new Category("Managing");
            catList.add(category);
            category.addExample(new IngestionTriggerWidget(orchestrationService),
                    RunAsyncCode.runAsyncCode(IngestionTriggerWidget.class));
            category.addExample(new ResourceManagementWidget(orchestrationService),
                    RunAsyncCode.runAsyncCode(ResourceManagementWidget.class));
        }

        {
            Category category = new Category("Validation");
            catList.add(category);
//            category.addExample(new RepositoryValidationWidget(orchestO
        }

        {
            Category category = new Category("Reporting");
            catList.add(category);
        }
    }

    /**
     * Get the {@link Category} associated with a widget.
     * 
     * @param widget
     *            the {@link IngestionControlPanelWidget}
     * @return the associated {@link Category}
     */
    public Category getCategoryForContentWidget(IngestionControlPanelWidget widget) {
        return contentCategory.get(widget);
    }

    /**
     * Get the content widget associated with the specified history token.
     * 
     * @param token
     *            the history token
     * @return the associated {@link IngestionControlPanelWidget}
     */
    public IngestionControlPanelWidget getContentWidgetForToken(String token) {
        return contentToken.get(token);
    }

    @Override
    public <T> NodeInfo<?> getNodeInfo(T value) {
        if (value == null) {
            // Return the top level categories.
            return new DefaultNodeInfo<Category>(categories, new CategoryCell());
        } else if (value instanceof Category) {
            // Return the examples within the category.
            Category category = (Category)value;
            return category.getNodeInfo();
        }
        return null;
    }

    @Override
    public boolean isLeaf(Object value) {
        return value != null && !(value instanceof Category);
    }
}
