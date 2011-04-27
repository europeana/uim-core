/* IngestionShell.java - created on Apr 27, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.uim.gui.gwt.client;

import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.view.client.TreeViewModel;

/**
 * Application shell for Ingestion Cockpit.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Apr 27, 2011
 */
public class CockpitShell extends Composite {
    /**
     * Construct the {@link CockpitShell}.
     * 
     * @param treeModel
     *            the treeModel that backs the main menu
     */
    public CockpitShell(TreeViewModel treeModel) {
        // Create the cell tree.
        CellTree mainMenu = new CellTree(treeModel, null);
        mainMenu.setAnimationEnabled(true);
        mainMenu.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.DISABLED);
    }
}
