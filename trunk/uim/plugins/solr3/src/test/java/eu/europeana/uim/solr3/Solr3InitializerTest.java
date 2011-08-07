/* Solr14InitializerTest.java - created on Aug 7, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.solr3;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

/**
 * 
 * 
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @since Aug 7, 2011
 */
public class Solr3InitializerTest {

    
    /**
     * @throws SolrServerException
     * @throws IOException
     */
    @Test
    public void testSetupAndStart() throws SolrServerException, IOException {
        Solr3Initializer initializer = new Solr3Initializer("file://./target/solr", "test");
        initializer.initialize(Solr3Initializer.class.getClassLoader());
        
        SolrInputDocument doc = new SolrInputDocument();
        doc.setField("uuid", "1");
        doc.setField("content", "sample text for field");
        initializer.getServer().add(doc);
        initializer.getServer().commit();
        
        QueryResponse response = initializer.getServer().query(new SolrQuery("*:*"));
        assertEquals(1L, response.getResults().getNumFound());
    }
}
