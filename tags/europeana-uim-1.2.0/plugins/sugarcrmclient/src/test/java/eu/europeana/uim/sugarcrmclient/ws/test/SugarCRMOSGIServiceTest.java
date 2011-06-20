package eu.europeana.uim.sugarcrmclient.ws.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.ops4j.pax.exam.CoreOptions.felix;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.CoreOptions.systemProperty;
import static org.ops4j.pax.exam.CoreOptions.waitForFrameworkStartup;
import static org.ops4j.pax.exam.CoreOptions.wrappedBundle;
import static org.ops4j.pax.exam.OptionUtils.combine;
import static org.ops4j.pax.exam.container.def.PaxRunnerOptions.scanFeatures;

import java.util.HashMap;
import java.util.List;

import org.apache.karaf.testing.AbstractIntegrationTest;
import org.apache.karaf.testing.Helper;
import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.Configuration;
import org.ops4j.pax.exam.junit.JUnit4TestRunner;


import eu.europeana.uim.store.Collection;
import eu.europeana.uim.store.Provider;
import eu.europeana.uim.sugarcrmclient.plugin.SugarCRMService;
import eu.europeana.uim.sugarcrmclient.plugin.objects.ConnectionStatus;
import eu.europeana.uim.sugarcrmclient.plugin.objects.SugarCrmRecord;
import eu.europeana.uim.sugarcrmclient.plugin.objects.data.DatasetStates;
import eu.europeana.uim.sugarcrmclient.plugin.objects.data.RetrievableField;
import eu.europeana.uim.sugarcrmclient.plugin.objects.data.SugarCrmField;
import eu.europeana.uim.sugarcrmclient.plugin.objects.data.UpdatableField;
import eu.europeana.uim.sugarcrmclient.plugin.objects.queries.ComplexSugarCrmQuery;
import eu.europeana.uim.sugarcrmclient.plugin.objects.queries.CustomSugarCrmQuery;
import eu.europeana.uim.sugarcrmclient.plugin.objects.queries.EqOp;
import eu.europeana.uim.sugarcrmclient.plugin.objects.queries.SimpleSugarCrmQuery;
import eu.europeana.uim.workflow.Workflow;

@RunWith(JUnit4TestRunner.class)
public class SugarCRMOSGIServiceTest  extends AbstractIntegrationTest{

	private static org.apache.log4j.Logger LOGGER = Logger.getLogger(SugarCRMOSGIServiceTest.class);
	
	
    /**
     * This is the configuration section of the "virtual" Karaf container during the tests execution. It sets 
     * all the dependencies required for the installation of the UIM Core & SugarCRM Plugin modules 
     * @return
     * @throws Exception
     */
    @Configuration
    public static Option[] configuration() throws Exception {
         return combine(
 				Helper.getDefaultOptions(
						//systemProperty("karaf.name").value("junit"),
						systemProperty("org.ops4j.pax.logging.DefaultServiceLog.level").value("OFF")),

				
				//This section corresponds to the "spring-ws" & "spring-dm" features in spring-features.xml		
		        mavenBundle().groupId("javax.activation").artifactId("com.springsource.javax.activation").version("1.1.1"),
		        mavenBundle().groupId("javax.servlet").artifactId("com.springsource.javax.servlet").version("2.5.0"),
		        mavenBundle().groupId("javax.xml.stream").artifactId("com.springsource.javax.xml.stream").version("1.0.1"),
		        mavenBundle().groupId("org.apache.commons").artifactId("com.springsource.org.apache.commons.logging").version("1.1.1"),
		        mavenBundle().groupId("org.aopalliance").artifactId("com.springsource.org.aopalliance").version("1.0.0"),
	            scanFeatures("mvn:org.apache.karaf.assemblies.features/standard/2.2.0/xml/features","spring","spring-dm"),
		        mavenBundle().groupId("org.springframework").artifactId("org.springframework.oxm").version("3.0.5.RELEASE"),
		        mavenBundle().groupId("org.springframework").artifactId("org.springframework.web").version("3.0.5.RELEASE"),
		        mavenBundle().groupId("org.springframework").artifactId("org.springframework.web.servlet").version("3.0.5.RELEASE"),
		        mavenBundle().groupId("org.springframework.ws").artifactId("org.springframework.xml").version("2.0.0.RELEASE"),
		        mavenBundle().groupId("javax.xml.bind").artifactId("com.springsource.javax.xml.bind").version("2.2.0"),
		        mavenBundle().groupId("javax.xml.soap").artifactId("com.springsource.javax.xml.soap").version("1.3.0"),
		        mavenBundle().groupId("org.apache.commons").artifactId("com.springsource.org.apache.commons.codec").version("1.4.0"),
		        mavenBundle().groupId("org.apache.commons").artifactId("com.springsource.org.apache.commons.httpclient").version("3.1.0"),
		        mavenBundle().groupId("org.springframework.ws").artifactId("org.springframework.ws").version("2.0.0.RELEASE"),
		        mavenBundle().groupId("org.springframework").artifactId("org.springframework.jms").version("3.0.5.RELEASE"),
		        mavenBundle().groupId("javax.mail").artifactId("com.springsource.javax.mail").version("1.4.1"),
		        mavenBundle().groupId("javax.jms").artifactId("com.springsource.javax.jms").version("1.1.0"),
		        mavenBundle().groupId("javax.xml.rpc").artifactId("com.springsource.javax.xml.rpc").version("1.1.0"),
		        mavenBundle().groupId("javax.ejb").artifactId("com.springsource.javax.ejb").version("3.0.0"),
		        mavenBundle().groupId("javax.mail").artifactId("com.springsource.javax.mail").version("1.4.1"),
		        mavenBundle().groupId("org.springframework").artifactId("org.springframework.jms").version("3.0.5.RELEASE"),
		        mavenBundle().groupId("javax.xml.rpc").artifactId("com.springsource.javax.xml.rpc").version("1.1.0"),
		        mavenBundle().groupId("com.sun.xml").artifactId("com.springsource.com.sun.xml.messaging.saaj").version("1.3.2"),
		        mavenBundle().groupId("org.joda").artifactId("com.springsource.org.joda.time").version("1.6.0"),
		        mavenBundle().groupId("org.xmlpull").artifactId("com.springsource.org.xmlpull").version("1.1.4"),
		        mavenBundle().groupId("org.jibx").artifactId("jibx-run").version("1.2.3"),
		        mavenBundle().groupId("org.jibx").artifactId("jibx-extras").version("1.2.3"),
		        mavenBundle().groupId("org.apache.commons").artifactId("com.springsource.org.apache.commons.collections").version("3.2.1"),
		        mavenBundle().groupId("org.springframework").artifactId("org.springframework.transaction").version("3.0.5.RELEASE"),
		        mavenBundle().groupId("com.opensymphony.quartz").artifactId("com.springsource.org.quartz").version("1.6.2"),
				wrappedBundle(mavenBundle().groupId("stax").artifactId("stax").version("1.2.0")),	
		        
				//This feature corresponds to the "uim-core" feature in uim-features.xml
                mavenBundle().groupId("eu.europeana").artifactId("europeana-uim-common").versionAsInProject(),
                mavenBundle().groupId("eu.europeana").artifactId("europeana-uim-api").versionAsInProject(),
                mavenBundle().groupId("eu.europeana").artifactId("europeana-uim-storage-memory").versionAsInProject(),
                mavenBundle().groupId("eu.europeana").artifactId("europeana-uim-plugin-basic").versionAsInProject(),
                mavenBundle().groupId("eu.europeana").artifactId("europeana-uim-plugin-sugarcrmclient").versionAsInProject(),
                
                felix(),

                waitForFrameworkStartup()
        );
    }
    
    
    
        
    
    
    /**
     * Tests the GetConnectionInfo functionality
     * @throws Exception
     */
    @Test
    public void testGetConnectionInfo() throws Exception{
    	SugarCRMService service = getOsgiService(SugarCRMService.class);
    	
		assertNotNull(service);
		
		ConnectionStatus status = service.showConnectionStatus();
		
		assertNotNull(status.getDefaultURI());
		assertNotNull(status.getSessionID());
		
		System.out.println(status);
    	
    }
	


    /**
     * Tests the Update Session functionality
     * 
     * @throws Exception
     */
    @Test
    public void testUpdateSession() throws Exception{
    	SugarCRMService service = getOsgiService(SugarCRMService.class);
    	ConnectionStatus statusbefore = service.showConnectionStatus();
    	String sessionIDbefore = statusbefore.getSessionID();
    	
    	String username = "test";
		String password = "test";
		
		String sessionIDafter = service.updateSession(username, password);
		
		assertNotNull(sessionIDbefore);
		assertNotNull(sessionIDafter);
		assertTrue(!sessionIDbefore.equals(sessionIDafter));
		
    }
    
    

    /**
     * Tests the execution of a Simple Query
     * 
     * @throws Exception
     */
    @Test
    public void testRetrieveRecordsSimpleQuery() throws Exception{
    	SugarCRMService service = getOsgiService(SugarCRMService.class);
    	
		DatasetStates status = DatasetStates.INGESTION_COMPLETE;
		SimpleSugarCrmQuery query =  new SimpleSugarCrmQuery(status);
		query.setMaxResults(1000);
		query.setOffset(0);
		query.setOrderBy(RetrievableField.DATE_ENTERED);

		List<SugarCrmRecord> records = service.retrieveRecords(query);
		System.out.println("Number of Records retrieved: " + records.size());
		System.out.println("NO | RECORD ID                          | COLLECTION NAME");

		for(int i=0; i< records.size(); i++){
			System.out.println( (i+1) + " : " + records.get(i).getItemValue(RetrievableField.ID) + " | " +
					records.get(i).getItemValue(RetrievableField.NAME)	) ;
		}
    }
    
    
    
    /**
     * Tests the execution of a Complex Query
     * 
     * @throws Exception
     */
    @Test
    public void testRetrieveRecordsComplexQuery() throws Exception{
    
    	SugarCRMService service = getOsgiService(SugarCRMService.class);
    	
		ComplexSugarCrmQuery query =  new ComplexSugarCrmQuery(RetrievableField.NAME ,EqOp.LIKE,"00101_M_PT_Gulbenkian_biblioteca_digital" );
		//query = query.and(field, op, value)
		
		List<SugarCrmRecord> records = service.retrieveRecords(query);
		System.out.println("Number of Records retrieved: " + records.size());
		System.out.println("NO | RECORD ID                          | COLLECTION NAME");

		for(int i=0; i< records.size(); i++){
			System.out.println( (i+1) + " : " + records.get(i).getItemValue(RetrievableField.ID) + " | " +
					records.get(i).getItemValue(RetrievableField.NAME)	) ;
		}
	}

    
    /**
     * Tests the execution of a Custom Query
     * 
     * @throws Exception
     */
    @Test
    public void testRetrieveRecordsCustomQuery() throws Exception{
    
    	SugarCRMService service = getOsgiService(SugarCRMService.class);
    	
		CustomSugarCrmQuery query =  new CustomSugarCrmQuery("opportunities.sales_stage LIKE 'Needs%Analysis'");
		
		List<SugarCrmRecord> records = service.retrieveRecords(query);
		System.out.println("Number of Records retrieved: " + records.size());
		System.out.println("NO | RECORD ID                          | COLLECTION NAME");

		for(int i=0; i< records.size(); i++){
			System.out.println( (i+1) + " : " + records.get(i).getItemValue(RetrievableField.ID) + " | " +
					records.get(i).getItemValue(RetrievableField.NAME)	) ;
		}
	}
    
    

    /**
     * Tests the fetch Record functionality 
     * 
     * @throws Exception
     */
    @Test
    public void testfetchRecord() throws Exception{
    	String recId = "a2098f49-37db-2362-3e4b-4c5861d23639";
    	SugarCRMService service = getOsgiService(SugarCRMService.class);
    	
    	SugarCrmRecord rec = service.retrieveRecord(recId);
		assertNotNull(rec);
    }
    
    

    /**
     * Tests the Update Record functionality 
     * @throws Exception
     */
    @Test
    public void testupdateRecord() throws Exception{
    	SugarCRMService service = getOsgiService(SugarCRMService.class);
    	
		String recordID = "a2098f49-37db-2362-3e4b-4c5861d23639";
		String threcords = "100";
		String himages = "50";
		String htetx = "30";
		String hvideo = "10";
		String hsound = "10";
		
		HashMap<UpdatableField, String> values  = new HashMap<UpdatableField, String>();
		values.put(UpdatableField.TOTAL_INGESTED, threcords);
		values.put(UpdatableField.INGESTED_IMAGE, himages);
		values.put(UpdatableField.INGESTED_TEXT, htetx);			
		values.put(UpdatableField.INGESTED_VIDEO, hvideo);
		values.put(UpdatableField.INGESTED_SOUND, hsound);
		
		service.updateRecordData(recordID, values);
    }
    
    
    

    /**
     * Tests the Change Record Status functionality 
     * @throws Exception
     */
    @Test
    public void testChangeRecordStatus() throws Exception{
    	SugarCRMService service = getOsgiService(SugarCRMService.class);
    	
		String recordID = "a2098f49-37db-2362-3e4b-4c5861d23639";
		DatasetStates chstate = DatasetStates.INGESTION_COMPLETE; 
		service.changeEntryStatus(recordID, chstate);
    }
    

    
    
    /**
     * Tests the Populate UIM from Record functionality (automatically infer 
     * Providers and Collections from a record)
     * @throws Exception
     */
    @Test
    public void testPopulateUIMfromRecord() throws Exception{
    	SugarCRMService service = getOsgiService(SugarCRMService.class);
		SugarCrmRecord re = service.retrieveRecord("a2098f49-37db-2362-3e4b-4c5861d23639");
		
		Provider prov = service.createProviderFromRecord(re);
		Collection coll = service.createCollectionFromRecord(re, prov);
		
    }
    
    
    
    /**
     * Tests the Initialize Workflow from a SugarCRM Record with a specific ID functionality.
     * 
     * @throws Exception
     */
    @Test
    public void testInitWorkflowByID() throws Exception{
    	
    	SugarCRMService service = getOsgiService(SugarCRMService.class);
    	
		String recordID = "a2098f49-37db-2362-3e4b-4c5861d23639";
		String worklfowName = "SysoutWorkflow";
		SugarCrmRecord record = service.retrieveRecord(recordID);
		DatasetStates endstate = DatasetStates.HARVESTING_PENDING; 
		Workflow wf = service.initWorkflowFromRecord(worklfowName, record, endstate);
		
		assertNotNull(wf);
    }
    
    
    
    /**
     * Tests the Initialize multiple Workflows from many SugarCRM Records having the same state functionality.
     * @throws Exception
     */
    @Test
    public void testInitWorkflowsByState() throws Exception{
    	SugarCRMService service = getOsgiService(SugarCRMService.class);
    	
		String wfname = "SysoutWorkflow";
		DatasetStates currentstate = DatasetStates.HARVESTING_PENDING;
		DatasetStates ndstate = DatasetStates.INGESTION_COMPLETE; 
		List<Workflow> wfs = service.initWorkflowsFromRecords(wfname, currentstate, ndstate);

		assertTrue(!wfs.isEmpty());
    }
    
    
    
    /**
     * Tests the Add Note Attachment to record functionality
     * @throws Exception
     */
    @Test
    public void testaddNoteAttachmentToRecord() throws Exception{
    	SugarCRMService service = getOsgiService(SugarCRMService.class);
		String recordID = "a2098f49-37db-2362-3e4b-4c5861d23639";
		String message = "Exception Stacktrace....";
    	service.addNoteAttachmentToRecord(recordID, message);
    }

    

    
    
}
