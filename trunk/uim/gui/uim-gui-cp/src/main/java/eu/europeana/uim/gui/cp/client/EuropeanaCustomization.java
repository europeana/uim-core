/* EuropeanaIngestionControlPanelDynamics.java - created on Jun 3, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.gui.cp.client;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Anchor;

/**
 * Europeana specific implementation of dynamics.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Jun 3, 2011
 */
public class EuropeanaCustomization implements IngestionCustomization {
    @Override
    public String mainTitle() {
        return "The European Library";
    }

    @Override
    public String subTitle() {
        return "Ingestion Control Panel";
    }

    @Override
    public ImageResource logo() {
        return null;
    }

    @Override
    public Anchor[] links() {
        return new Anchor[] {
                new Anchor("The European Library",
                        "http://search.theeuropeanlibrary.org/portal/en/index.html"),
                new Anchor("TRAC", "http://trac.tel.ulcc.ac.uk/team01") };
    }
}
