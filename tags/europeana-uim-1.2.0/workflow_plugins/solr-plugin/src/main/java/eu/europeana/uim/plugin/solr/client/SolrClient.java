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
package eu.europeana.uim.plugin.solr.client;

import java.io.IOException;
import java.net.MalformedURLException;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.apache.solr.common.SolrInputDocument;

/**
 * 
 * @author Georgios Markakis
 */
public class SolrClient {

	private SolrServer server;
	
	
	/**
	 * Default Constructor
	 */
	public SolrClient(){

	}
	
	
	private void init(){
		String url = "http://localhost:8983/solr";
			  CommonsHttpSolrServer server;
			try {
				server = new CommonsHttpSolrServer(url);
				  server.setSoTimeout(1000);  // socket read timeout
				  server.setConnectionTimeout(100);
				  server.setDefaultMaxConnectionsPerHost(100);
				  server.setMaxTotalConnections(100);
				  server.setFollowRedirects(false);  // defaults to false
				  // allowCompression defaults to false.
				  // Server side must support gzip or deflate for this to have any effect.
				  server.setAllowCompression(true);
				  server.setMaxRetries(1); // defaults to 0.  > 1 not recommended.
				  server.setParser(new XMLResponseParser()); // binary parser is used by default
				  
				  this.server =  server;
				  
			} catch (MalformedURLException e) {
				e.printStackTrace();
				
				this.server = null;
			}

	}
	
	
	
	public void importRecord(SolrInputDocument solrDocument) throws SolrServerException, IOException{

		init();
		server.add(solrDocument);
		server.commit();
	}
	
	
	
	
	
	
	
	
	
}
