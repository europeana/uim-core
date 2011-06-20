/**
 * 
 */
package eu.europeana.uim.plugin.solr.test;

import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

import eu.europeana.uim.plugin.solr.client.SolrClient;

/**
 * @author Georgios Markakis
 *
 */
public class SolrPluginTest {

	SolrClient client;
	
	public SolrPluginTest(){
		this.client = new SolrClient();
	}
	
	
	@Test
	public void insertTest() throws Exception{
		SolrInputDocument solrDocument = new SolrInputDocument();
		
		
		solrDocument.addField("europeana_uri", "uri2");
		solrDocument.addField("europeana_collectionName", "collection2");
		solrDocument.addField("creator", "test2");
		solrDocument.addField("identifier", "xxxxxxxxxx1");
		solrDocument.addField("europeana_edm_class", "class");
		
		client.importRecord(solrDocument);
		
	}
	
	
}
