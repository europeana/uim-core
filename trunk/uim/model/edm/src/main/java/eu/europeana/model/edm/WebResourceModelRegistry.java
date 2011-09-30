package eu.europeana.model.edm;

import org.theeuropeanlibrary.model.Link;

import eu.europeana.uim.common.TKey;

/**
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 */
@SuppressWarnings("all")
public final class WebResourceModelRegistry {

    
    public static final TKey<WebResourceModelRegistry, Link>                       LINK                = TKey.register(
                                                                                                          WebResourceModelRegistry.class,
                                                                                                          "link",
                                                                                                          Link.class);

}
