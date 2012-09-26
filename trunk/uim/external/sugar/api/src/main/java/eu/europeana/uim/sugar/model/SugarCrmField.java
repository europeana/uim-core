
 /**
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
package eu.europeana.uim.sugar.model;

import eu.europeana.uim.store.ControlledVocabularyKeyValue;

/**
 * Interface declaration of a SugarCRM field
 * 
 * @author Georgios Markakis
 */
public interface SugarCrmField {

    
    /**
     * @return the field id in the SugarCRM
     */
    public String getFieldId();
    
    /**
     * @return the qualified field id in the SugarCRM
     */
    public String getQualifiedFieldId();

    /**
     * @return the matching target controlled field in UIM
     */
    public ControlledVocabularyKeyValue getMappingField();
    
    
	/**
	 * @return the semantic meaning of the field
	 */
	public String getDescription();
}
