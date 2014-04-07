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

import eu.europeana.uim.store.MetaDataRecord;

/**
 * General interface for a metadata record adapter.
 * 
 * @param <I>
 * @param <Q>
 * 
 * @author Georgios Markakis <gwarkx@hotmail.com>
 * @since 9 May 2012
 */
public interface MetadataRecordAdapter<I, Q extends QValueAdapterStrategy<?, ?, ?, ?>> extends
        MetaDataRecord<I> {
    /**
     * Returns the original metadata record contained within the adapter
     * 
     * @return the adapted record
     */
    MetaDataRecord<I> getAdaptedRecord();
}
