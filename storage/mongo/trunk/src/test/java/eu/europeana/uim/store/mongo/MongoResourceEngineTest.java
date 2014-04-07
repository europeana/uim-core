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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.net.UnknownHostException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.junit.After;
import org.junit.Before;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.mongodb.Mongo;
import com.mongodb.MongoException;

import eu.europeana.uim.resource.AbstractResourceEngineTest;
import eu.europeana.uim.resource.ResourceEngine;
import eu.europeana.uim.store.Collection;
import eu.europeana.uim.store.Provider;
import eu.europeana.uim.workflow.Workflow;

/**
 * MongoDB ResourceEngine JUnit Tests.
 * 
 * @author Georgios Markakis (gwarkx@hotmail.com)
 * @since Jan 6 2012
 * @see eu.europeana.uim.store.memory.ResourceStorageEngineTest
 */
public class MongoResourceEngineTest extends AbstractResourceEngineTest<String> {
    private MongoResourceEngine mongoEngine        = null;

    private MongoStorageEngine  mongostorageEngine = null;

    private Mongo               m                  = null;
    
    
    
    private final static String HOST = "127.0.0.1";
    
    private final static int PORT = 27017;
    
    private MongoProvider mongoProvider = new MongoProvider(PORT);

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

    @PreDestroy
    public void stopMongo(){
    	mongoProvider.stopMongo();
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see eu.europeana.uim.api.AbstractResourceEngineTest#getResourceEngine()
     */
    @Override
    protected ResourceEngine getResourceEngine() {
        if (mongoEngine == null) {
            try {
            	 m = new Mongo(HOST,PORT);
                MongoResourceEngine engine = new MongoResourceEngine("UIMTEST",HOST,PORT);
                MongoStorageEngine storageEngine = new MongoStorageEngine("UIMTEST",HOST,PORT);
                m.dropDatabase("UIMTEST");
                engine.initialize();
                storageEngine.initialize();
                mongoEngine = engine;
                mongostorageEngine = storageEngine;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            return mongoEngine;
        }
        return mongoEngine;
    }

    /*
     * (non-Javadoc)
     * 
     * @see eu.europeana.uim.api.AbstractResourceEngineTest#nextID()
     */
    @Override
    protected String nextID() {
        return new String();
    }

    /*
     * (non-Javadoc)
     * 
     * @see eu.europeana.uim.api.AbstractResourceEngineTest#testGenerateWorkflow()
     */
    @Override
    protected Workflow testGenerateWorkflow() {
        Workflow workflow = mock(Workflow.class);

        when(workflow.getIdentifier()).thenAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return "Workflow";
            }
        });

        return workflow;
    }

    /*
     * (non-Javadoc)
     * 
     * @see eu.europeana.uim.api.AbstractResourceEngineTest#testGenerateProvider()
     */
    @Override
    protected Provider<String> testGenerateProvider() {

        return mongostorageEngine.createProvider();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * eu.europeana.uim.api.AbstractResourceEngineTest#testGenerateCollection(eu.europeana.uim.store
     * .Provider)
     */
    @Override
    protected Collection<String> testGenerateCollection(Provider<String> provider) {

        return mongostorageEngine.createCollection(provider);
    }
}
