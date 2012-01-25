/* RepoxTestUtils.java - created on Jan 25, 2012, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.repox.rest;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Properties;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import eu.europeana.uim.repox.rest.client.xml.Response;

/**
 * Utility functions common to all tests like uri of repox used for testing and logging output.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Jan 25, 2012
 */
public class RepoxTestUtils {
    /**
     * @param clazz
     * @param resource
     * @return the user depending basedirectory read from the resource property
     * @throws IOException
     */
    public static String getUri(Class<?> clazz, String resource) throws IOException {
        Properties properties = new Properties();
        properties.load(clazz.getResourceAsStream(resource));

        String userName = System.getProperty("user.name");
        if (userName == null || userName.length() == 0) { throw new IllegalArgumentException(
                "Property 'user.name' is null, so it is not possible to setup the infrastructure for integration tests!"); }

        String uri = properties.getProperty(userName);
        if (uri == null) {
            uri = properties.getProperty("default");
        }
        if (uri == null) { throw new IllegalArgumentException(
                "Uri for user '" +
                        userName +
                        "' as well as the 'default' setting is null, so it is not possible to setup the infrastructure for integration tests!"); }

        return uri;
    }

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
     * This method marshals the contents of a JIBX Element and outputs the results to the Logger.
     * 
     * @param jaxbObject
     *            A JAXB representation of a REPOX xml.
     * @param logger
     *            logging variable
     * @throws JAXBException
     */
    public static void logMarshalledObject(Object jaxbObject, Logger logger) throws JAXBException {
        StringWriter writer = new StringWriter();

        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.marshal(jaxbObject, writer);

        logger.info("===========================================");
        logger.info("XML representation for '" + jaxbObject.getClass().getName() + "':");
        logger.info(writer.toString());
        logger.info("===========================================");
    }
}
