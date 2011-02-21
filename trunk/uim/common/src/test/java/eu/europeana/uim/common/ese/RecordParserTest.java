package eu.europeana.uim.common.ese;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.InputStream;
import java.util.List;

import org.junit.Test;

import eu.europeana.uim.common.parse.RecordField;
import eu.europeana.uim.common.parse.RecordMap;
import eu.europeana.uim.common.parse.RecordParser;
import eu.europeana.uim.common.parse.XMLStreamParserException;


/**
 * 
 * 
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @date Feb 16, 2011
 */
public class RecordParserTest {


	/** method testing against a standard ese file
	 * @throws XMLStreamParserException
	 */
	@Test
	public void testParseReadingEurope() throws XMLStreamParserException {
		InputStream stream = RecordParserTest.class.getResourceAsStream("/readingeurope.xml");
		RecordParser parser = new RecordParser();
		List<RecordMap> xml = parser.parse(stream, "europeana:record");
		assertEquals(999, xml.size());
	}

	
	/** method testing against an ese file within an oai context
	 * @throws XMLStreamParserException
	 */
	@Test
	public void testParseReadingEuropeOAI() throws XMLStreamParserException {
		InputStream stream = RecordParserTest.class.getResourceAsStream("/readingeurope.oai.xml");
		RecordParser parser = new RecordParser();
		List<RecordMap> xml = parser.parse(stream, "europeana:record");
		assertEquals(250, xml.size());
	}

	
	/** method testing the path resolution of the record name
	 * @throws XMLStreamParserException
	 */
	@Test
	public void testParseReadingEuropePathESE() throws XMLStreamParserException {
		InputStream stream = RecordParserTest.class.getResourceAsStream("/readingeurope.oai.xml");
		RecordParser parser = new RecordParser();
		List<RecordMap> xml = parser.parse(stream, "OAI-PMH|ListRecords|record|metadata|europeana:record");
		assertEquals(250, xml.size());
	}

	/** method testing the record name with TEL data
	 * @throws XMLStreamParserException
	 */
	@Test
	public void testParseReadingEuropeTEL() throws XMLStreamParserException {
		InputStream stream = RecordParserTest.class.getResourceAsStream("/readingeurope.tel.xml");
		RecordParser parser = new RecordParser();
		List<RecordMap> xml = parser.parse(stream, "tel:dcx");
		assertEquals(250, xml.size());
	}

	
	/** method testing the path resolution of the record name
	 * @throws XMLStreamParserException
	 */
	@Test
	public void testParseReadingEuropePathTEL() throws XMLStreamParserException {
		InputStream stream = RecordParserTest.class.getResourceAsStream("/readingeurope.tel.xml");
		RecordParser parser = new RecordParser();
		List<RecordMap> xml = parser.parse(stream, "OAI-PMH|ListRecords|record|metadata|record|tel:dcx");
		assertEquals(250, xml.size());
	}

	

	/** method testing the existence of specific detailed info
	 * @throws XMLStreamParserException
	 */
	@Test
	public void testParseReadingEuropeDetail() throws XMLStreamParserException {
		InputStream stream = RecordParserTest.class.getResourceAsStream("/readingeurope.oai.xml");
		RecordParser parser = new RecordParser();
		List<RecordMap> xml = parser.parse(stream, "europeana:record");
		for (RecordMap record : xml) {
			String title = record.getFirst(new RecordField("dc", "title", "eng"));
			if (title != null) {
				List<String> local = record.getValueByLocal("title");
				boolean matched = false;
				for (String ltitle : local) {
					if (ltitle.equals(title)) {
						matched = true;
					}
				}
				assertTrue(matched);
			}
		}
	}
	
	/** method testing the real data from europeana travel
	 * @throws XMLStreamParserException
	 */
	@Test
	public void testParseEuropeanaTravel1() throws XMLStreamParserException {
		InputStream stream = RecordParserTest.class.getResourceAsStream("/MS10805_09_tn.xml");
		RecordParser parser = new RecordParser();
		List<RecordMap> xml = parser.parse(stream, "record");
		assertEquals(1, xml.size());
	}

	/** method testing the real data from europeana travel
	 * @throws XMLStreamParserException
	 */
	@Test
	public void testParseEuropeanaTravel2() throws XMLStreamParserException {
		InputStream stream = RecordParserTest.class.getResourceAsStream("/MS10805_37_tn.xml");
		RecordParser parser = new RecordParser();
		List<RecordMap> xml = parser.parse(stream, "record");
		assertEquals(1, xml.size());
	}

	

}
