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

import static org.junit.Assert.assertEquals;

import java.net.UnknownHostException;
import java.util.Date;

import javax.annotation.PreDestroy;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.mongodb.Mongo;
import com.mongodb.MongoException;

import eu.europeana.uim.common.MDRFieldRegistry;
import eu.europeana.uim.storage.AbstractStorageEngineTest;
import eu.europeana.uim.storage.StorageEngine;
import eu.europeana.uim.storage.StorageEngineException;
import eu.europeana.uim.store.Collection;
import eu.europeana.uim.store.MetaDataRecord;
import eu.europeana.uim.store.Provider;
import eu.europeana.uim.store.Request;

/**
 * Configuration class for MongoDB StorageEngineTests
 * 
 * @author Georgios Markakis (gwarkx@hotmail.com)
 * @since Jan 6 2012
 * @see eu.europeana.uim.store.memory.MemoryStorageEngineTest
 */

@RunWith(JUnit4.class)
public class MongoStorageEngineTest extends AbstractStorageEngineTest<String> {
	private MongoStorageEngine mongoEngine = null;

	private Mongo m = null;


	private final static String HOST = "127.0.0.1";

	private final static int PORT = 27017;
	
	private MongoProvider mongoProvider = new MongoProvider(PORT);

	private enum TestEnum {
		EN;
	}

	@PreDestroy
    public void shutdownMongo(){
    	mongoProvider.stopMongo();
    }
	/**
	 * Run before each test
	 */
	@Before
	public void setupTest() {

		try {
            m = new Mongo(HOST,PORT);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (MongoException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Run after each test
	 */
	@After
	public void cleanup() {
		m.dropDatabase("UIMTEST");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.europeana.uim.store.AbstractStorageEngineTest#getStorageEngine()
	 */
	@Override
	protected StorageEngine<String> getStorageEngine() {
		if (mongoEngine == null) {
			try {
				
				 m = new Mongo(HOST,PORT);
				MongoStorageEngine engine = new MongoStorageEngine("UIMTEST",HOST,PORT);
				m.dropDatabase("UIMTEST");
				engine.initialize();
				mongoEngine = engine;
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			return mongoEngine;
		}
		return mongoEngine;
	}

	/**
	 * @throws StorageEngineException
	 * 
	 */
	@Test
	public void testAggregators() throws StorageEngineException {
		Provider<String> provider0 = engine.createProvider();
		provider0.setMnemonic("TEL");
		provider0.setName("The European Library");
		engine.updateProvider(provider0);

		Collection<String> collection0 = engine.createCollection(provider0);
		collection0.setMnemonic("a0001");
		collection0.setName("TEL's collection 001");
		engine.updateCollection(collection0);

		Request<String> request0 = engine.createRequest(collection0,
				new Date(0));
		engine.updateRequest(request0);

		assertEquals(0, engine.getTotalByRequest(request0));
		assertEquals(0, engine.getTotalByCollection(collection0));

		MetaDataRecord<String> record0 = engine.createMetaDataRecord(
				collection0, "abcd-1");
		record0.addValue(MDRFieldRegistry.rawrecord, "title 01");
		record0.addValue(MDRFieldRegistry.rawrecord, "title 02");
		record0.addValue(MDRFieldRegistry.rawrecord, "title 03", TestEnum.EN);
		record0.addValue(MDRFieldRegistry.rawformat, "MARC21");
		engine.updateMetaDataRecord(record0);
		engine.addRequestRecord(request0, record0);

		assertEquals(1, engine.getTotalByRequest(request0));

		MetaDataRecord<String> record1 = engine.createMetaDataRecord(
				collection0, "abcd-2");
		record1.addValue(MDRFieldRegistry.rawrecord, "title 11");
		record1.addValue(MDRFieldRegistry.rawrecord, "title 12");
		record1.addValue(MDRFieldRegistry.rawrecord, "title 13", TestEnum.EN);
		record1.addValue(MDRFieldRegistry.rawformat, "MARC21");
		engine.updateMetaDataRecord(record1);
		engine.addRequestRecord(request0, record1);

		assertEquals(2, engine.getTotalByRequest(request0));

		MetaDataRecord<String> record3 = engine.getMetaDataRecord(record0
				.getId());
		assertEquals("title 01",
				record3.getFirstValue(MDRFieldRegistry.rawrecord));
		assertEquals(
				"title 03",
				record3.getValues(MDRFieldRegistry.rawrecord, TestEnum.EN).get(
						0));

		assertEquals(3, record3.getQualifiedValues(MDRFieldRegistry.rawrecord)
				.size());
		engine.checkpoint();

		MetaDataRecord<String> record4 = engine.getMetaDataRecord(record0
				.getId());
		assertEquals("title 01",
				record4.getFirstValue(MDRFieldRegistry.rawrecord));
		assertEquals(
				"title 03",
				record4.getValues(MDRFieldRegistry.rawrecord, TestEnum.EN).get(
						0));
		assertEquals(request0.getCollection().getId(), record4.getCollection()
				.getId());

		assertEquals(2, engine.getTotalByRequest(request0));
		assertEquals(2, engine.getTotalByCollection(collection0));

		assertEquals(request0.getCollection().getId(), record1.getCollection()
				.getId());

		mongoEngine.flushCollectionMDRS(collection0.getId());

		mongoEngine.flushRequestMDRS(request0.getId());

	}
}
