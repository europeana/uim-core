/*
 * Copyright 2007 EDL FOUNDATION
 *
 * Licensed under the EUPL, Version 1.1 or - as soon they
 * will be approved by the European Commission - subsequent
 * versions of the EUPL (the "Licence");
 * you may not use this work except in compliance with the
 * Licence.
 * You may obtain a copy of the Licence at:
 *
 * http://ec.europa.eu/idabc/eupl
 *
 * Unless required by applicable law or agreed to in
 * writing, software distributed under the Licence is
 * distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied.
 * See the Licence for the specific language governing
 * permissions and limitations under the Licence.
 */
package eu.europeana.uim.store;

/**
 * This interface is meant to provide support for storing information more efficiently in the values
 * Map as defined in the Collection, Provider and Execution interfaces. If the user wants to keep
 * track of the keys values used in this map, then may use a specific Enumeration that implements
 * this interface and use the following methods:
 * 
 * <code>void putValue(ControlledVocabularyKeyValue key, String value)</code>
 * <code>String getValue(ControlledVocabularyKeyValue key)</code>
 * 
 * @author Georgios Markakis
 */
public interface ControlledVocabularyKeyValue {
    /**
     * @return field identifier
     */
    public String getFieldId();
}
