/*
 * Copyright 2007 EDL FOUNDATION
 *
 * Licensed under the EUPL, Version 1.1 or - as soon they
 * will be approved by the European Commission - subsequent
 * versions of the EUPL (the "Licence");
 * you may not use this work except in compliance with the
 * Licence.
 * You may obtain a copy of the Licence at:
 *
 * http://ec.europa.eu/idabc/eupl
 *
 * Unless required by applicable law or agreed to in
 * writing, software distributed under the Licence is
 * distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied.
 * See the Licence for the specific language governing
 * permissions and limitations under the Licence.
 */
package eu.europeana.uim.repox.rest.client.basic;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.io.IOUtils;

import eu.europeana.uim.repox.RepoxException;
import eu.europeana.uim.repox.rest.client.xml.Response;

/**
 * Abstract base class for creating actual rest calls.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since 10.02.2013
 */
public abstract class AbstractRepoxRestClient {
    private final static Logger log = Logger.getLogger(AbstractRepoxRestClient.class.getName());

    /**
     * Base uri specifying the repox installation
     */
    private String              uri;

    /**
     * Context for JAXB
     */
    private static JAXBContext  jaxbContext;

    /**
     * Initializes jaxb context with root class of MACS records
     */
    static {
        try {
            jaxbContext = JAXBContext.newInstance(Response.class);
        } catch (JAXBException e) {
            throw new RuntimeException("Failed to initialize jaxb.", e);
        }
    }

    /**
     * Creates a new instance of this class.
     */
    public AbstractRepoxRestClient() {
        this(null);
    }

    /**
     * Creates a new instance of this class.
     * 
     * @param uri
     *            base uri specifying the repox installation
     */
    public AbstractRepoxRestClient(String uri) {
        this.uri = uri;
    }

    /**
     * Auxiliary method for invoking a REST operation with parameters
     * 
     * @param <S>
     *            the return type
     * @param wsOperation
     * @return casted rest response
     * @throws RepoxException
     */
    /**
     * Auxiliary method for invoking a REST operation with parameters
     * 
     * @param <S>
     *            the return type
     * 
     * @param restOperation
     *            operation
     * @param responseClass
     *            class for response
     * @param params
     *            optional parameters
     * @return casted rest response
     * @throws RepoxException
     */
    protected <S> S invokeRestCall(String restOperation, Class<S> responseClass, String... params)
            throws RepoxException {
        StringBuffer operation = new StringBuffer();
        operation.append(uri);
        operation.append(restOperation);
        if (params.length > 0) {
            operation.append("?");
        }

        for (int i = 0; i < params.length; i++) {
            if (i != 0 && params[i] != null && params[i].length() > 0) {
                operation.append("&");
            }
            operation.append(params[i].replaceAll(" ", "%20"));
        }

        String urlStr = operation.toString();
        URL url;
        try {
            url = new URL(urlStr);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RepoxException("Could not creat url of '" + urlStr + "'!", e);
        }
        log.info("Invoking rest call for '" + urlStr + "'!");

        HttpURLConnection conn;
        try {
            conn = (HttpURLConnection)url.openConnection();
        } catch (IOException e) {
            throw new RepoxException("Could not open url connection!", e);
        }

        conn.disconnect();

        Unmarshaller u;
        try {
            u = jaxbContext.createUnmarshaller();
        } catch (JAXBException e) {
            throw new RepoxException("Could not create unmarshaller!", e);
        }
        S response;
        try {
            response = responseClass.cast(u.unmarshal(new InputStreamReader(conn.getInputStream())));
        } catch (Exception e) {
            // TODO: REMOVE
            StringWriter writer = new StringWriter();
            try {
                IOUtils.copy(conn.getInputStream(), writer, "UTF-8");
            } catch (Exception e1) {
                throw new RepoxException("Could not read stream!", e1);
            }
            String out = writer.toString();

            throw new RepoxException("Could not unmarshall rest response '" + out + "' for url '" +
                                     urlStr + "'!", e);
        }
        return response;
    }
}
