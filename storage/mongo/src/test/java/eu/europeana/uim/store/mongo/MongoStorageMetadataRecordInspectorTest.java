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
package eu.europeana.uim.store.mongo;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.theeuropeanlibrary.model.common.qualifier.Status;

import eu.europeana.uim.model.europeana.EuropeanaModelRegistry;
import eu.europeana.uim.storage.StorageEngineException;
import eu.europeana.uim.storage.modules.criteria.KeyCriterium;
import eu.europeana.uim.store.Collection;
import eu.europeana.uim.store.MetaDataRecord;

/**
 * Configuration class for MongoDB StorageEngineTests
 * 
 * @author Georgios Markakis (gwarkx@hotmail.com)
 * @since Jan 6 2012
 * @see eu.europeana.uim.store.memory.MemoryStorageEngineTest
 */

@RunWith(JUnit4.class)
public class MongoStorageMetadataRecordInspectorTest{
	private MongoStorageEngine mongoEngine = null;


	private final static String HOST = "localhost";

	private final static int PORT = 27017;
	


	/**
	 * Run before each test
	 */
	@Before
	public void setupTest() {
		mongoEngine = new MongoStorageEngine("UIM",HOST,PORT);
		mongoEngine.initialize();
	}
	
	/**
	 * Run before each test
	 */
	@After
	public void shutdownStorageEngineTest() {	
		mongoEngine.shutdown();
	}


	

	/**
	 * @throws StorageEngineException
	 * 
	 */
	@Test
	public void inspect() throws StorageEngineException {
		MetaDataRecord<String> mdr = mongoEngine.getMetaDataRecord("/9200114/BibliographicResource_3000095895046");
		if (mdr!=null) {
			System.out.println("MetadataRecoord: " + mdr.toString());
			assertTrue(true);
		} else {
			assertTrue(false);
		}
	}
	
	@Test
	public void test() throws StorageEngineException {
		Collection<String> collection0 = mongoEngine.findCollection("9200114");
		KeyCriterium<EuropeanaModelRegistry, String> notOfThisSession  = KeyCriterium.buildNotKeyCriterium(EuropeanaModelRegistry.INITIALINGESTIONSESSION, "4322");
		KeyCriterium<EuropeanaModelRegistry, Status> notMarkedAsDeleted  = KeyCriterium.buildNotKeyCriterium(EuropeanaModelRegistry.STATUS, Status.DELETED);

		String[] entries = (String[]) mongoEngine.getByCollectionAndCriteria(collection0, notOfThisSession, notMarkedAsDeleted);
		assertTrue(entries.length>0);
	}
}
