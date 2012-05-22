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
package eu.europeana.uim.model.adapters;

import java.util.Map;
import eu.europeana.uim.common.TKey;
import eu.europeana.uim.store.MetaDataRecord;

/**
 * Factory class for creating adapters
 *
 * @author Georgios Markakis <gwarkx@hotmail.com>
 * @since 20 May 2012
 */
public class AdapterFactory {

	
	/**
	 * Factory method
	 * 
	 * @param adaptedRecord the metadata record to be adapted
	 * @param strategies a Map containing conversion strategies
	 * @return an adapter for the given input
	 */
	public static <T,S extends QValueAdapterStrategy<?,?,?,?>> MetadataRecordAdapter<T,S> 
	getAdapter(MetaDataRecord<T> adaptedRecord,Map<TKey<?, ?>,S> strategies){
		return new MetadataRecordAdapterImpl<T,S>(adaptedRecord,strategies);
	}
	
}
