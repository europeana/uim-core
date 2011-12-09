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
import java.util.concurrent.atomic.AtomicLong;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.mongodb.Mongo;
import com.mongodb.MongoException;

import eu.europeana.uim.api.AbstractResourceEngineTest;
import eu.europeana.uim.api.ResourceEngine;
import eu.europeana.uim.store.Collection;
import eu.europeana.uim.store.Provider;
import eu.europeana.uim.workflow.Workflow;

/**
 * 
 * 
 * @author Georgios Markakis
 */
public class MongoResourceEngineTest extends AbstractResourceEngineTest<Long>{

	private MongoResourceEngine mongoEngine = null;

	private MongoStorageEngine mongostorageEngine = null;
	
    private Mongo m = null;	
	
    private static AtomicLong id = new AtomicLong();
    
    
	/**
	 * Run before each test
	 */
	@Before
    public void setupTest(){
		
		try {
			m = new Mongo();
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
    public void cleanup(){
		m.dropDatabase("UIMTEST");
	}
    
    
	@Override
	protected ResourceEngine getResourceEngine() {
		   if (mongoEngine == null) {
			      try {
			    	m = new Mongo();
			        MongoResourceEngine engine = new MongoResourceEngine("UIMTEST");
			        
			        MongoStorageEngine storageEngine = new MongoStorageEngine("UIMTEST");
			        
			        m.dropDatabase("UIMTEST");
			        engine.initialize();
			        storageEngine.initialize();
			        mongoEngine = engine;
			        mongostorageEngine = storageEngine;
			      }
			      catch(Exception e) {
			          e.printStackTrace();
			      }
			    }
			    else {
			      return mongoEngine;
			    }
		   return mongoEngine;
	}

	@Override
	protected Long nextID() {
        return id.incrementAndGet();
	}

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

	@Override
	protected Provider<Long> testGenerateProvider() {
		
		return mongostorageEngine.createProvider();
	}

	@Override
	protected Collection<Long> testGenerateCollection(Provider<Long> provider) {

		return mongostorageEngine.createCollection(provider);
	}
	
	

    
    
	

}
