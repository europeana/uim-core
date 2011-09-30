package org.theeuropeanlibrary.qualifier;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;
import org.theeuropeanlibrary.model.common.qualifier.Language;

/**
 * Tests the Language enumareation
 * 
 * @author Nuno Freire (nfreire@gmail.com)
 * @date 4 de Abr de 2011
 */
public class LanguageTest {

	/**
	 * tests getting a language by ISO code
	 */
	@Test
	public void testGetByISO() {
		assertNotNull(Language.getByIso2("en"));
		assertNotNull(Language.getByIso3("eng"));
		assertNull(Language.getByIso2("eng"));
		assertNull(Language.getByIso3("en"));

		assertSame(Language.ENG, Language.getByIso2("en"));
		assertSame(Language.ENG, Language.getByIso3("eng"));
	}

	/**
	 * Test non-language codes
	 */
	@Test
	public void testNonLanguages() {
		assertNull(Language.getByIso2("xx"));
		assertNull(Language.getByIso2("un"));
		assertNotNull(Language.getByIso3("und"));
		assertNull(Language.getByIso3("und").getLocale());
		assertNotNull(Language.getByIso3("mul"));
		assertNull(Language.getByIso3("mul").getLocale());
	}

	/**
	 * Prints out the languages in the enum
	 */
	@Test
	public void testLanguageNumber() {
		StringBuilder builder = new StringBuilder();
		for (Language lang : Language.values()) {
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
