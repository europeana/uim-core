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

import java.net.UnknownHostException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.junit.After;
import org.junit.Before;

import com.mongodb.Mongo;
import com.mongodb.MongoException;

import eu.europeana.uim.AbstractMetaDataRecordTest;
import eu.europeana.uim.storage.StorageEngine;

/**
 * Configuration class for MongoDB StorageEngineTests
 *
 * @author Georgios Markakis
 */
public class MongoStorageMetaDataRecordTest extends AbstractMetaDataRecordTest<String> {

    private MongoStorageEngine mongoEngine = null;

    private Mongo m = null;

    private final static String HOST = "127.0.0.1";

    private final static int PORT = 10000;

    private MongoProvider mongoProvider = new MongoProvider(PORT);

    @PreDestroy
    public void shutdownMongo() {
        mongoProvider.stopMongo();
    }

    /**
     * Run before each test
     */
    @Before
    public void setupTest() {
        try {
            m = new Mongo(HOST, PORT);
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
                m = new Mongo(HOST, PORT);
                MongoStorageEngine engine = new MongoStorageEngine("UIMTEST", HOST, PORT);
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
}
