/* IngestionCockpitShell.java - created on Apr 27, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.gui.cp.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.SimpleLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.TreeViewModel;

/**
 * Main composite of ingestion cockpit, setting up menu bar etc.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Apr 27, 2011
 */
public class IngestionControlPanelShell extends ResizeComposite {
    interface IngestionCockpitUiBinder extends UiBinder<Widget, IngestionControlPanelShell> {
    }

    /**
     * Dynamic information used in the shell.
     */
    public static interface ShellCustomization {
        /**
         * @return value in main title
         */
        String mainTitle();

        /**
         * @return value in sub title
         */
        String subTitle();

        /**
         * @return optional logo
         */
        ImageResource logo();

        /**
         * @return dynamic links specific to customization
         */
        Anchor[] links();
    }

    private static IngestionCockpitUiBinder uiBinder = GWT.create(IngestionCockpitUiBinder.class);

    /**
     * main title of the application
     */
    @UiField
    Label                                   mainTitle;

    /**
     * sub title of the application
     */
    @UiField
    Label                                   subTitle;

    /**
     * optional logo
     */
    @UiField
    Image                                   logo;

    /**
     * The container around the links at the top of the app.
     */
    @UiField
    Grid                                    linkTable;

    /**
     * The main menu used to navigate to examples.
     */
    @UiField(provided = true)
    CellTree                                mainMenu;

    /**
     * The panel that holds the content.
     */
    @UiField
    SimpleLayoutPanel                       contentPanel;

    /**
     * The current {@link IngestionWidget} being displayed.
     */
    private IngestionWidget                 content;

    /**
     * Construct the {@link IngestionControlPanelShell}.
     * 
     * @param customs
     *            filling dynamic content with this values
     * @param treeModel
     *            the treeModel that backs the main menu
     */
    public IngestionControlPanelShell(ShellCustomization customs, TreeViewModel treeModel) {
        mainMenu = new CellTree(treeModel, null);
        mainMenu.setAnimationEnabled(true);
        mainMenu.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.DISABLED);

        initWidget(uiBinder.createAndBindUi(this));

        linkTable.setCellPadding(0);
        linkTable.setCellSpacing(0);
        linkTable.resize(1, 2 * customs.links().length - 1);
        int columnCounter = 0;
        for (int i = 0; i < customs.links().length; i++) {
            linkTable.setWidget(0, columnCounter++, customs.links()[i]);
            if (i != customs.links().length - 1) {
                linkTable.setHTML(0, columnCounter++, "&nbsp;|&nbsp;");
            }
        }

        mainTitle.setText(customs.mainTitle());
        subTitle.setText(customs.subTitle());
        if (customs.logo() != null) {
            logo.setResource(customs.logo());
        }
    }

    /**
     * Get the main menu used to select ingestion control views.
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
    public void setContent(final IngestionWidget content) {
        this.content = content;
        
        if (content == null) {
            contentPanel.setWidget(null);
            return;
        }

        showContent();
    }

    /**
     * Show a content widget.
     */
    private void showContent() {
        if (content == null) { return; }
//        content.ensureWidget();
//        contentPanel.setWidget(content.getWidget());
        contentPanel.setWidget(content);
    }
}
