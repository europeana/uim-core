/*
 * Copyright 2007-2012 The Europeana Foundation
 *
 *  Licenced under the EUPL, Version 1.1 (the "Licence") and subsequent versions as approved
 *  by the European Commission;
 *  You may not use this work except in compliance with the Licence.
 * 
 *  You may obtain a copy of the Licence at:
 *  http://joinup.ec.europa.eu/software/page/eupl
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under
 *  the Licence is distributed on an "AS IS" basis, without warranties or conditions of
 *  any kind, either express or implied.
 *  See the Licence for the specific language governing permissions and limitations under
 *  the Licence.
 */
package eu.europeana.uim.model.adapters.europeana;

import java.util.HashSet;
import java.util.Set;
import org.theeuropeanlibrary.model.common.Link;
import eu.europeana.uim.common.TKey;
import eu.europeana.uim.model.adapters.QValueAdapterStrategy;
import eu.europeana.uim.model.europeana.EuropeanaLink;
import eu.europeana.uim.model.europeana.EuropeanaModelRegistry;
import org.theeuropeanlibrary.model.tel.ObjectModelRegistry;

/**
 * 
 * @author Georgios Markakis <gwarkx@hotmail.com>
 * @since 8 May 2012
 */
public class EuropeanaLinkAdapterStrategy extends
        QValueAdapterStrategy<ObjectModelRegistry, Link, EuropeanaModelRegistry, EuropeanaLink> {
    @Override
    public AdaptedInput adaptinput(TKey<ObjectModelRegistry, Link> key, Enum<?>... qualifiers) {
        AdaptedInput adinput = new AdaptedInput();

        TKey<EuropeanaModelRegistry, EuropeanaLink> tkey = EuropeanaModelRegistry.EUROPEANALINK;
        adinput.setKey(tkey);
        Set<Enum<?>> qs = new HashSet<Enum<?>>();

        if (qualifiers != null) {
            for (int i = 0; i < qualifiers.length; i++) {
                qs.add(qualifiers[i]);
            }
        }

        adinput.setQualifiers(qs);

        return adinput;
    }

    @Override
    public AdaptedOutput adaptoutput(EuropeanaLink adaptedResult, Set<Enum<?>> set) {
        AdaptedOutput adoutput = new AdaptedOutput();

        Link link = new Link();

        link.setAnchorKey(adaptedResult.getAnchorKey());
        link.setCachedPath(adaptedResult.getCachedPath());
        link.setLastChecked(adaptedResult.getLastChecked());
        // link.getLinkStatus(adaptedResult.getLinkStatus());
        link.setUrl(adaptedResult.getUrl());

        adoutput.setOutputObject(link);

        return adoutput;
    }
}
