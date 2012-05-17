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

import java.util.HashMap;
import java.util.Map;

import eu.europeana.uim.common.TKey;

/**
 *
 * @author Georgios Markakis <gwarkx@hotmail.com>
 * @since 7 May 2012
 */
public class QAdapterFactory {

	public static Map<TKey<?, ?>,QualiFiedValuesAdapterStrategy<?,?>> adapterMap;
	
	//Static initializer block, you can register your adapters here
	static{
		adapterMap = new HashMap<TKey<?, ?>,QualiFiedValuesAdapterStrategy<?,?>>();
		
	}
	
	
	/**
	 * @param key
	 * @return
	 */
	public static <NS,T,N> QualiFiedValuesAdapterStrategy<T,N> getAdapterbyKey(TKey<NS, T> key){
		
		
		return null;
	}
}
