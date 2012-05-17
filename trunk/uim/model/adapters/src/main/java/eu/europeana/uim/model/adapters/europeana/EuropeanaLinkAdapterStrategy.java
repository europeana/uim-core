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
public class EuropeanaLinkAdapterStrategy extends QValueAdapterStrategy<ObjectModelRegistry,Link,EuropeanaModelRegistry,EuropeanaLink>{

	@Override
	public AdaptedInput adaptinput(TKey<ObjectModelRegistry, Link> key, Enum<?>... qualifiers) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AdaptedOutput adaptoutput(EuropeanaLink adaptedResult, Enum<?>... qualifiers) {
		// TODO Auto-generated method stub
		return null;
	}

}
