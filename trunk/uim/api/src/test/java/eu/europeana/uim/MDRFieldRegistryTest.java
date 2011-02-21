/* MDRFieldRegistryTest.java - created on Feb 16, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * 
 * 
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @date Feb 16, 2011
 */
public class MDRFieldRegistryTest {

	@Test
	public void testUntouched() {
		assertEquals(0, TKey.size());
	}
	
	@Test
	public void testFieldRegistry() {
		TKey<MDRFieldRegistry,String> title = MDRFieldRegistry.title;
		assertEquals(3, TKey.size());
	}
	
	
}
