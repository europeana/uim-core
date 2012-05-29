/*
 * Copyright 2007-2012 The Europeana Foundation
 *
 *  Licenced under the EUPL, Version 1.1 (the "Licence") and subsequent versions as approved
 *  by the European Commission;
 *  You may not use this work except in compliance with the Licence.
 * 
 *  You may obtain a copy of the Licence at:
 *  http://joinup.ec.europa.eu/software/page/eupl
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under
 *  the Licence is distributed on an "AS IS" basis, without warranties or conditions of
 *  any kind, either express or implied.
 *  See the Licence for the specific language governing permissions and limitations under
 *  the Licence.
 */
package eu.europeana.uim.model.adapters.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;
import org.theeuropeanlibrary.model.common.Link;
import org.theeuropeanlibrary.model.common.qualifier.LinkStatus;
import org.theeuropeanlibrary.model.tel.ObjectModelRegistry;

import eu.europeana.uim.common.TKey;
import eu.europeana.uim.model.adapters.AdapterFactory;
import eu.europeana.uim.model.adapters.MetadataRecordAdapter;
import eu.europeana.uim.model.adapters.QValueAdapterStrategy;
import eu.europeana.uim.model.adapters.europeana.EuropeanaLinkAdapterStrategy;
import eu.europeana.uim.model.europeana.EuropeanaLink;
import eu.europeana.uim.model.europeana.EuropeanaModelRegistry;
import eu.europeana.uim.store.MetaDataRecord;
import eu.europeana.uim.store.MetaDataRecord.QualifiedValue;
import eu.europeana.uim.store.bean.MetaDataRecordBean;


/**
 * Unit test for the EuropeanaLinkAdapterStrategy class
 *
 * @author Georgios Markakis <gwarkx@hotmail.com>
 * @since 21 May 2012
 */
public class EuropeanaLinkAdapterTest {

	private static MetaDataRecord<String> mdr;
	private static MetadataRecordAdapter<String,?> mdrad;
	private static final String ANCHORKEY = "anchorKey";
	private static final String CACHEDPATH = "message";
	private static final Date LASTCECKED = new Date();
	private static final LinkStatus LINKSTATUS = LinkStatus.NOT_CHECKED;
	private static final String URL = "url";
	
	/**
	 * Initialize the test
	 */
	@BeforeClass
	public static void init(){
		//This is where the initialization takes place, it is assumed that the 
		//
		mdr = new MetaDataRecordBean<String>();
		EuropeanaLink link = new EuropeanaLink();

		link.setAnchorKey(ANCHORKEY);
		link.setCachedPath(CACHEDPATH);
		link.setLastChecked(LASTCECKED);
		link.setLinkStatus(LINKSTATUS);
		link.setUrl(URL);
		mdr.addValue(EuropeanaModelRegistry.EUROPEANALINK, link);
		
		Map<TKey<?, ?>, QValueAdapterStrategy<?, ?, ?, ?>> strategies =  new HashMap<TKey<?, ?>, QValueAdapterStrategy<?, ?, ?, ?>>();
		
		strategies.put(ObjectModelRegistry.LINK, new EuropeanaLinkAdapterStrategy());
		
		 mdrad = AdapterFactory.getAdapter(mdr, strategies);
	}
	
	
	/**
	 * 
	 */
	@Test
	public void getFirstQualifiedValueTest(){
		
		QualifiedValue<Link> qval = mdrad.getFirstQualifiedValue(ObjectModelRegistry.LINK);
		
		assertNotNull(qval.getValue());
		assertEquals(ANCHORKEY,qval.getValue().getAnchorKey());
		assertEquals(CACHEDPATH,qval.getValue().getCachedPath());
		assertEquals(LASTCECKED,qval.getValue().getLastChecked());
		//assertEquals(LINKSTATUS,qval.getValue().getLinkStatus());
		assertEquals(URL,qval.getValue().getUrl());
	}
	
	/**
	 * 
	 */
	@Test
	public void getFirstValueTest(){
		Link val = mdrad.getFirstValue(ObjectModelRegistry.LINK);
		
		assertNotNull(val);
		assertEquals(ANCHORKEY,val.getAnchorKey());
		assertEquals(CACHEDPATH,val.getCachedPath());
		assertEquals(LASTCECKED,val.getLastChecked());
		//assertEquals(LINKSTATUS,val.getLinkStatus());
		assertEquals(URL,val.getUrl());	
	}
	
	
	/**
	 * 
	 */
	@Test
	public void putValue(){
		//mdrad.addValue(ObjectModelRegistry.LINK, value, qualifiers)

	}
	
	
	/**
	 * 
	 */
	@Test
	public void getQualifiedValues(){
		List<QualifiedValue<Link>> qvalues = mdrad.getQualifiedValues(ObjectModelRegistry.LINK);
	}
	
	/**
	 * 
	 */
	@Test
	public void getValues(){
		//mdrad.getValues(ObjectModelRegistry.LINK);
	}
	
	
	/**
	 * 
	 */
	@Test
	public void deleteValues(){
		mdrad.deleteValues(ObjectModelRegistry.LINK);
	}
	
	
}
