/* EuropeanaIngestionControlPanelDynamics.java - created on Jun 3, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.gui.cp.basic.client;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Anchor;

import eu.europeana.uim.gui.cp.client.IngestionCustomization;

/**
 * Europeana specific implementation of dynamics.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Jun 4, 2011
 */
public class UimCustomization implements IngestionCustomization {
    @Override
    public String mainTitle() {
        return "Unified Ingestion Manager";

    }

    @Override
    public String subTitle() {
        return "Control Panel";
    }

    @Override
    public ImageResource logo() {
        return null;
    }

    @Override
    public Anchor[] links() {
        return new Anchor[] { new Anchor("The European Library",
                "http://search.theeuropeanlibrary.org/portal/en/index.html") };
    }
}
