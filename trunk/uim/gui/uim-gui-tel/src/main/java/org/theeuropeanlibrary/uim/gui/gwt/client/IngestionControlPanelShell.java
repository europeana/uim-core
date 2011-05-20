/* IngestionCockpitShell.java - created on Apr 27, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.uim.gui.gwt.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.TableElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.TreeViewModel;

/**
 * Main composite of ingestion cockpit, setting up menu bar etc.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Apr 27, 2011
 */
public class IngestionControlPanelShell extends Composite {
    interface IngestionCockpitUiBinder extends UiBinder<Widget, IngestionControlPanelShell> {
    }

    private static IngestionCockpitUiBinder uiBinder      = GWT.create(IngestionCockpitUiBinder.class);

    /**
     * The panel that holds the content.
     */
    @UiField
    SimplePanel                             contentPanel;

    /**
     * The container around the links at the top of the app.
     */
    @UiField
    TableElement                            linkCell;

    /**
     * The main menu used to navigate to examples.
     */
    @UiField(provided = true)
    CellTree                                mainMenu;

    /**
     * The current {@link IngestionControlPanelWidget} being displayed.
     */
    private IngestionControlPanelWidget          content;

    /**
     * The widget that holds CSS or source code for an example.
     */
    private HTML                            contentSource = new HTML();

    /**
     * Construct the {@link IngestionControlPanelShell}.
     * 
     * @param treeModel
     *            the treeModel that backs the main menu
     */
    public IngestionControlPanelShell(TreeViewModel treeModel) {
        // Create the cell tree.
        mainMenu = new CellTree(treeModel, null);
        mainMenu.setAnimationEnabled(true);
        mainMenu.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.DISABLED);

        // Initialize the ui binder.
        initWidget(uiBinder.createAndBindUi(this));
        contentSource.getElement().getStyle().setBackgroundColor("#eee");
        contentSource.getElement().getStyle().setMarginLeft(10.0, Unit.PX);
        contentSource.getElement().getStyle().setMarginRight(10.0, Unit.PX);
        contentSource.getElement().getStyle().setProperty("border", "1px solid #c3c3c3");
        contentSource.getElement().getStyle().setProperty("padding", "10px 2px");
    }

    /**
     * Get the main menu used to select examples.
     * 
     * @return the main menu
     */
    public CellTree getMainMenu() {
        return mainMenu;
    }

    /**
     * Set the content to display.
     * 
     * @param content
     *            the content
     */
    public void setContent(final IngestionControlPanelWidget content) {
        this.content = content;
        if (content == null) {
            contentPanel.setWidget(null);
            return;
        }

        // Show the widget.
        showExample();
    }

    /**
     * Show a example.
     */
    private void showExample() {
        if (content == null) { return; }
        contentPanel.setWidget(content);
    }
}
