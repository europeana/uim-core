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
package eu.europeana.uim.repoxclient.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.ops4j.pax.exam.CoreOptions.felix;
import static org.ops4j.pax.exam.CoreOptions.maven;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.CoreOptions.systemPackage;
import static org.ops4j.pax.exam.CoreOptions.wrappedBundle;

import java.io.StringWriter;


import static org.ops4j.pax.exam.CoreOptions.systemProperty;
import static org.ops4j.pax.exam.CoreOptions.waitForFrameworkStartup;
import static org.ops4j.pax.exam.OptionUtils.combine;
import static org.ops4j.pax.exam.container.def.PaxRunnerOptions.scanFeatures;

import org.apache.karaf.testing.Helper;
import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.IMarshallingContext;
import org.jibx.runtime.JiBXException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Inject;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.container.def.PaxRunnerOptions;
import org.ops4j.pax.exam.junit.Configuration;
import org.ops4j.pax.exam.junit.JUnit4TestRunner;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;

import eu.europeana.uim.api.Registry;
import eu.europeana.uim.api.StorageEngine;
import eu.europeana.uim.store.Collection;
import eu.europeana.uim.store.Provider;


import eu.europeana.uim.repoxclient.jibxbindings.DataSources;
import eu.europeana.uim.repoxclient.plugin.RepoxRestClient;
import eu.europeana.uim.repoxclient.plugin.RepoxUIMService;
import eu.europeana.uim.repoxclient.rest.exceptions.RepoxException;
import eu.europeana.uim.clientbindings.utils.Utils;
import org.apache.karaf.testing.AbstractIntegrationTest;
import org.apache.log4j.Logger;

/**
 * Class implementing Unit Tests for UIM Repox Service
 * This is a PAX-EXAM OSGI based test. This practically means that tests are executed within
 * an OSGI Karaf container created by PAX-EXAM. Make sure that no other instance of Karaf is
 * running locally during the execution of the tests (otherwise the tests will fail)
 * 
 * @author Georgios Markakis
 */

@RunWith(JUnit4TestRunner.class)
public class RepoxUIMServiceTest extends AbstractIntegrationTest{
	
	
	private static org.apache.log4j.Logger LOGGER = Logger.getLogger(RepoxUIMServiceTest.class);
	
    /**
     * This is the configuration section of the "virtual" Karaf container during the tests execution. It sets 
     * all the dependencies required for the installation of the UIM Core & Repox Plugin modules 
     * @return
     * @throws Exception
     */
    @Configuration
    public static Option[] configuration() throws Exception {
         return combine(
 				Helper.getDefaultOptions(
						systemProperty("karaf.name").value("junit"),
						systemProperty("org.ops4j.pax.logging.DefaultServiceLog.level").value("INFO")),

				
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
                mavenBundle().groupId("eu.europeana").artifactId("europeana-uim-plugin-repox").versionAsInProject(),
                
                felix(),

                waitForFrameworkStartup()
        );
    }
	
    
    
	@Test
	public void testRetrieveDataSourcesService() throws Exception{
		    RepoxUIMService repoxservice = getOsgiService(RepoxUIMService.class);
	
			assertNotNull(repoxservice);
	
	}
	
	
	
	/**
	 * This method marshals the contents of a  JIBX Element and outputs the results to the
	 * Logger.  
	 * @param jaxbObject A JIBX representation of a SugarCRM SOAP Element. 
	 */
	private  void logMarshalledObject(Object jibxObject){		
		IBindingFactory context;

		try {
			context = BindingDirectory.getFactory(jibxObject.getClass());

			IMarshallingContext mctx = context.createMarshallingContext();
			mctx.setIndent(2);
			StringWriter stringWriter = new StringWriter();
			mctx.setOutput(stringWriter);
			mctx.marshalDocument(jibxObject);
			LOGGER.info("===========================================");
			StringBuffer sb = new StringBuffer("Soap Ouput for Class: ");
			sb.append(jibxObject.getClass().getSimpleName());
			LOGGER.info(sb.toString());
			LOGGER.info(stringWriter.toString());
			LOGGER.info("===========================================");
		} catch (JiBXException e) {

			e.printStackTrace();
		}

		
	}

}
