package org.theeuropeanlibrary.qualifier;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;
import org.theeuropeanlibrary.model.common.qualifier.Country;

/**
 * Tests the Country enumareation
 * 
 * @author Nuno Freire (nfreire@gmail.com)
 * @date 4 de Abr de 2011
 */
public class CountryTest {

	/**
	 * tests getting a language by ISO code
	 */
	@Test
	public void testGetByISO() {
		assertNotNull(Country.getByIso2("gb"));
		assertNotNull(Country.getByIso3("deu"));
		assertNull(Country.getByIso2("eng"));
		assertNull(Country.getByIso3("en"));

        assertSame(Country.GB, Country.getByIso2("uk"));
        assertSame(Country.GB, Country.getByIso2("gb"));
		assertSame(Country.DE, Country.getByIso3("ger"));
	}

	/**
	 * Test non-language codes
	 */
	@Test
	public void testNonCountrys() {
		assertNull(Country.getByIso2("xy"));
		assertNull(Country.getByIso2("un"));
		assertNotNull(Country.getByIso3("xxx"));
		assertNotNull(Country.getByIso3("xxx").getLocale());
	}

	/**
	 * Prints out the languages in the enum
	 */
	@Test
	public void testCountryNumber() {
		StringBuilder builder = new StringBuilder();
		for (Country lang : Country.values()) { 
			if (builder.length() > 0) {
				builder.append(", ");
			}
			builder.append(lang.getIso3());
			builder.append(":");
			builder.append(lang.getName());
		}
//		System.out.println(builder.toString());
	}
}
