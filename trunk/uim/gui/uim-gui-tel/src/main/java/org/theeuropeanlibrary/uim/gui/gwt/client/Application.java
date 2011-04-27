package org.theeuropeanlibrary.uim.gui.gwt.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootLayoutPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Apr 26, 2011
 */
public class Application implements EntryPoint {
    @Override
    public void onModuleLoad() {
        CockpitMainMenuTreeViewModel model = new CockpitMainMenuTreeViewModel();
        CockpitShell shell = new CockpitShell(model);
        RootLayoutPanel.get().add(shell);
    }
}
