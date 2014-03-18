/* RepoxTestUtils.java - created on Jan 25, 2012, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.repox.rest.utils;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import eu.europeana.uim.repox.rest.client.xml.Response;

/**
 * Utility functions common to all tests like uri of repox used for testing and logging output.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Jan 25, 2012
 */
public class RepoxXmlUtils {
    /**
     * context for JAXB
     */
    private static JAXBContext jaxbContext;

    /**
     * initializes jaxb context with root class of MACS records
     */
    static {
        try {
            jaxbContext = JAXBContext.newInstance(Response.class);
        } catch (JAXBException e) {
            throw new RuntimeException("Failed to initialize jaxb.", e);
        }
    }

    /**
     * @param jaxbObject
     * @return marshalled xml of given object
     * @throws JAXBException
     */
    public static String marshall(Object jaxbObject) throws JAXBException {
        StringWriter writer = new StringWriter();
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.marshal(jaxbObject, writer);
        return writer.toString();
    }

    /**
     * @param xmlObject
     * @return unmarshalled object from xml
     * @throws JAXBException
     */
    public static Object unmarshall(String xmlObject) throws JAXBException {
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        Object jaxbObj = unmarshaller.unmarshal(new StringReader(xmlObject));
        return jaxbObj;
    }
}
