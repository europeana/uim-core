/* TestDateUtils.java - created on Feb 16, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.common;

import static org.junit.Assert.assertEquals;

import java.text.ParseException;
import java.util.Date;

import org.junit.Test;

import eu.europeana.uim.common.DateUtils;

/**
 * 
 * 
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @date Feb 16, 2011
 */
public class TestDateUtils {

	
	/** test if parsing of 4 and 8 digit dates works correct.
	 * 
	 * @throws ParseException
	 */
	@Test
	public void testParseDate() throws ParseException {
		Date date0 = DateUtils.parse("1234");
		Date comp0 = org.apache.commons.lang.time.DateUtils.parseDate("1234", new String[]{"yyyy"});
		assertEquals(date0, comp0);
		
		Date date1 = DateUtils.parse("1234-11-28");
		Date comp1 = org.apache.commons.lang.time.DateUtils.parseDate("28111234", new String[]{"ddMMyyyy"});
		assertEquals(date1, comp1);
		
	}
}
