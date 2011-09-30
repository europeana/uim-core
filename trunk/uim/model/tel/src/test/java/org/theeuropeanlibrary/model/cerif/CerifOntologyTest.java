/* CerifOntology.java - created on 14 de Set de 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.model.cerif;

import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests the CerifOntology class - in memory representation of the CERIF subject scheme
 * 
 * @author Nuno Freire (nfreire@gmail.com)
 * @date 14 de Set de 2011
 */
public class CerifOntologyTest {
	
	
	/**
	 * Tests loading the ontology from the csv in the classpath, and tests some basic operations on the ontology
	 */
	@Test
	public void testLoadFromCsv() {
		CerifOntology cerif=new CerifOntology();
		
		CerifConcept top = cerif.getConcept("H000");
		Assert.assertEquals(1, top.getHierarchyLevel());
		Assert.assertNull(top.getBroader());
		Assert.assertTrue(top.getNarrower().size() > 2);
		
		CerifConcept level2 = cerif.getConcept("H001");
		Assert.assertEquals(2, level2.getHierarchyLevel());
		Assert.assertEquals(level2.getBroader(), top);
		Assert.assertTrue(level2.getNarrower().size() > 2);

		CerifConcept level3 = cerif.getConcept("H120");
		Assert.assertEquals(3, level3.getHierarchyLevel());
		Assert.assertEquals(level3.getBroader(), level2);
		Assert.assertTrue(level3.getNarrower().size() == 0);
		
		Collection<CerifConcept> topConcepts = cerif.getTopConcepts();
		Assert.assertEquals(5, topConcepts.size());
		Assert.assertTrue(topConcepts.contains(top));
		Assert.assertFalse(topConcepts.contains(level2));
		Assert.assertFalse(topConcepts.contains(level3));
	}
	
	
}

