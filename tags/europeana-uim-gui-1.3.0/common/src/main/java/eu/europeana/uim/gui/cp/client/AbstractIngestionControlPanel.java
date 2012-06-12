package eu.europeana.uim.gui.cp.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.prefetch.Prefetcher;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.cellview.client.TreeNode;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

import eu.europeana.uim.gui.cp.client.SidebarMenu.SidebarMenuEntry;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Apr 26, 2011
 */
public abstract class AbstractIngestionControlPanel {
    private IngestionControlPanelShell shell;

    /**
     * @return implementation of dynamic valueso
     */
    protected abstract IngestionCustomization getDynamics();

    /**
     * Adds menu entries to the sidebar menu.
     * 
     * @param treeModel
     */
    protected abstract void addMenuEntries(SidebarMenu treeModel);

    /**
     * Initializes menu and so forth.
     */
    protected void initialize() {
        final SingleSelectionModel<IngestionWidget> selectionModel = new SingleSelectionModel<IngestionWidget>();
        final SidebarMenu treeModel = new SidebarMenu(selectionModel);
        addMenuEntries(treeModel);
        IngestionCustomization dynamics = getDynamics();

        shell = new IngestionControlPanelShell(dynamics, treeModel);
        RootLayoutPanel.get().add(shell);

        // Prefetch examples when opening the Category tree nodes.
        final List<SidebarMenuEntry> prefetched = new ArrayList<SidebarMenuEntry>();
        final CellTree mainMenu = shell.getMainMenu();
        mainMenu.addOpenHandler(new OpenHandler<TreeNode>() {
            @Override
            public void onOpen(OpenEvent<TreeNode> event) {
                Object value = event.getTarget().getValue();
                if (!(value instanceof SidebarMenuEntry)) { return; }

                SidebarMenuEntry category = (SidebarMenuEntry)value;
                if (!prefetched.contains(category)) {
                    prefetched.add(category);
                    Prefetcher.prefetch(category.getSplitPoints());
                }
            }
        });

        // Always prefetch.
        Prefetcher.start();

        // Change the history token when a main menu item is selected.
        selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                IngestionWidget selected = selectionModel.getSelectedObject();
                if (selected != null) {
                    History.newItem(getContentWidgetToken(selected), true);
                }
            }
        });

        // Setup a history handler to reselect the associate menu item.
        final ValueChangeHandler<String> historyHandler = new ValueChangeHandler<String>() {
            @Override
            public void onValueChange(ValueChangeEvent<String> event) {
                // Get the content widget associated with the history token.
                IngestionWidget ingestionCockpitWidget = treeModel.getContentWidgetForToken(event.getValue());
                if (ingestionCockpitWidget == null) { return; }

                // Expand the tree node associated with the content.
                SidebarMenuEntry category = treeModel.getCategoryForContentWidget(ingestionCockpitWidget);
                TreeNode node = mainMenu.getRootTreeNode();
                int childCount = node.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    if (node.getChildValue(i) == category) {
                        node.setChildOpen(i, true, true);
                        break;
                    }
                }

                // Select the node in the tree.
                selectionModel.setSelected(ingestionCockpitWidget, true);

                // Display the content widget.
                displayIngestionCockpitWidget(ingestionCockpitWidget);
            }
        };
        History.addValueChangeHandler(historyHandler);

        // Show the initial example.
        if (History.getToken().length() > 0) {
            History.fireCurrentHistoryState();
        } else {
            // Use the first token available.
            TreeNode root = mainMenu.getRootTreeNode();
            TreeNode category = root.setChildOpen(0, true);
            IngestionWidget content = (IngestionWidget)category.getChildValue(0);
            selectionModel.setSelected(content, true);
        }
    }

    /**
     * Set the content to the {@link IngestionWidget}.
     * 
     * @param content
     *            the {@link IngestionWidget} to display
     */
    private void displayIngestionCockpitWidget(IngestionWidget ingestionCockpitWidget) {
        if (ingestionCockpitWidget == null) { return; }

        shell.setContent(ingestionCockpitWidget);
        Window.setTitle(ingestionCockpitWidget.getName());
    }

    /**
     * Get the token for a given content widget.
     * 
     * @param content
     * @return the content widget token.
     */
    public static String getContentWidgetToken(IngestionWidget content) {
        return getContentWidgetToken(content.getClass());
    }

    /**
     * Get the token for a given content widget.
     * 
     * @param <C>
     * @param cwClass
     * @return the content widget token.
     */
    public static <C extends IngestionWidget> String getContentWidgetToken(Class<C> cwClass) {
        String className = cwClass.getName();
        className = className.substring(className.lastIndexOf('.') + 1);
        return "!" + className;
    }
}
