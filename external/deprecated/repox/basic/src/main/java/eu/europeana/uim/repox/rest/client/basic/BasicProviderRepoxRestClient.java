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

import eu.europeana.uim.repox.RepoxException;
import eu.europeana.uim.repox.rest.client.ProviderRepoxRestClient;
import eu.europeana.uim.repox.rest.client.xml.Aggregator;
import eu.europeana.uim.repox.rest.client.xml.DataProviders;
import eu.europeana.uim.repox.rest.client.xml.Provider;
import eu.europeana.uim.repox.rest.client.xml.Response;

/**
 * Class implementing REST functionality for accessing record functionality
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since 10.02.2013
 */
public class BasicProviderRepoxRestClient extends AbstractRepoxRestClient implements
        ProviderRepoxRestClient {
    /**
     * Creates a new instance of this class.
     */
    public BasicProviderRepoxRestClient() {
        this(null);
    }

    /**
     * Creates a new instance of this class.
     * 
     * @param uri
     *            base uri specifying the repox installation
     */
    public BasicProviderRepoxRestClient(String uri) {
        super(uri);
    }

    @Override
    public Provider createProvider(Provider prov, Aggregator agr) throws RepoxException {
        StringBuffer aggregatorId = new StringBuffer();
        StringBuffer providerId = new StringBuffer();
        StringBuffer name = new StringBuffer();
        StringBuffer description = new StringBuffer();
        StringBuffer country = new StringBuffer();
        StringBuffer nameCode = new StringBuffer();
        StringBuffer homepage = new StringBuffer();
        StringBuffer datasetType = new StringBuffer();

        aggregatorId.append("aggregatorId=");
        aggregatorId.append(agr.getId());

        providerId.append("dataProviderId=");
        providerId.append(prov.getId());

        name.append("name=");
        name.append(prov.getName());

        description.append("description=");
        if (prov.getDescription() != null) {
            description.append(prov.getDescription());
        } else {
            description.append("NONE");
        }

        country.append("country=");
        if (prov.getCountry() != null) {
            country.append(prov.getCountry());
        } else {
            country.append("eu");
        }

        nameCode.append("nameCode=");
        nameCode.append(prov.getNameCode());

        homepage.append("url=");
        if (prov.getUrl() != null) {
            homepage.append(prov.getUrl());
        } else {
            homepage.append("http://europeana.eu");
        }

        datasetType.append("dataSetType=");
        if (prov.getType() != null) {
            datasetType.append(prov.getType());
        } else {
            datasetType.append("UNKNOWN");
        }

        Response resp = invokeRestCall("/dataProviders/create", Response.class,
                aggregatorId.toString(), providerId.toString(), name.toString(),
                description.toString(), country.toString(), nameCode.toString(),
                homepage.toString(), datasetType.toString());

        if (resp.getProvider() == null) {
            if (resp.getError() != null) {
                throw new RepoxException("Could not create provider! Reason: " +
                                         resp.getError().getCause());
            } else {
                throw new RepoxException("Unidentified repox error during creation of provider!");
            }
        } else {
            return resp.getProvider();
        }
    }

    @Override
    public String deleteProvider(String provID) throws RepoxException {
        StringBuffer providerId = new StringBuffer();
        providerId.append("id=");
        providerId.append(provID);

        Response resp = invokeRestCall("/dataProviders/delete", Response.class,
                providerId.toString());

        if (resp.getSuccess() == null) {
            if (resp.getError() != null) {
                throw new RepoxException("Could not delete provider! Reason: " +
                                         resp.getError().getCause());
            } else {
                throw new RepoxException("Unidentified repox error during deletion of provider!");
            }
        } else {
            return resp.getSuccess();
        }
    }

    @Override
    public Provider updateProvider(Provider prov) throws RepoxException {
        StringBuffer provId = new StringBuffer();
        StringBuffer name = new StringBuffer();
        StringBuffer description = new StringBuffer();
        StringBuffer country = new StringBuffer();
        StringBuffer nameCode = new StringBuffer();
        StringBuffer homepage = new StringBuffer();
        StringBuffer datasetType = new StringBuffer();

        provId.append("id=");
        provId.append(prov.getId());

        name.append("name=");
        name.append(prov.getName());

        description.append("description=");

        if (prov.getDescription() != null) {
            description.append(prov.getDescription());
        } else {
            description.append("NONE");
        }

        country.append("country=");
        if (prov.getCountry() != null) {
            country.append(prov.getCountry());
        } else {
            country.append("eu");
        }

        nameCode.append("nameCode=");
        nameCode.append(prov.getNameCode());

        homepage.append("url=");
        if (prov.getUrl() != null) {
            homepage.append(prov.getUrl());
        } else {
            homepage.append("http://europeana.eu");
        }

        datasetType.append("dataSetType=");
        if (prov.getType() != null) {
            datasetType.append(prov.getType());
        } else {
            datasetType.append("UNKNOWN");
        }

        Response resp = invokeRestCall("/dataProviders/update", Response.class, provId.toString(),
                name.toString(), description.toString(), country.toString(), nameCode.toString(),
                homepage.toString(), datasetType.toString());

        if (resp.getProvider() == null) {
            if (resp.getError() != null) {
                throw new RepoxException("Could not update provider! Reason: " +
                                         resp.getError().getCause());
            } else {
                throw new RepoxException("Unidentified repox error during updating of provider!");
            }
        } else {
            return resp.getProvider();
        }
    }

    @Override
    public Provider moveProvider(String providerId, String aggregatorId) throws RepoxException {
        StringBuffer provID = new StringBuffer();
        provID.append("idDataProvider=");
        provID.append(providerId);

        StringBuffer agrID = new StringBuffer();
        agrID.append("idNewAggr=");
        agrID.append(aggregatorId);

        Response resp = invokeRestCall("/dataProviders/move", Response.class, provID.toString(),
                agrID.toString());

        if (resp.getProvider() == null) {
            if (resp.getError() != null) {
                throw new RepoxException("Could not move provider! Reason: " +
                                         resp.getError().getCause());
            } else {
                throw new RepoxException("Unidentified repox error during updating of provider!");
            }
        } else {
            return resp.getProvider();
        }
    }

    @Override
    public DataProviders retrieveProviders() throws RepoxException {
        Response resp = invokeRestCall("/dataProviders/list", Response.class);

        if (resp.getDataProviders() == null) {
            if (resp.getError() != null) {
                throw new RepoxException("Could not retrieving providers! Reason: " +
                                         resp.getError().getCause());
            } else {
                throw new RepoxException("Unidentified repox error during retrieving of providers!");
            }
        } else {
            return resp.getDataProviders();
        }
    }

    @Override
    public DataProviders retrieveAggregatorProviders(Aggregator agr) throws RepoxException {
        StringBuffer agrID = new StringBuffer();
        agrID.append("aggregatorId=");
        agrID.append(agr.getId());

        Response resp = invokeRestCall("/dataProviders/list", Response.class, agrID.toString());

        if (resp.getDataProviders() == null) {
            if (resp.getError() != null) {
                throw new RepoxException("Could not retrieving providers! Reason: " +
                                         resp.getError().getCause());
            } else {
                throw new RepoxException("Unidentified repox error during retrieving of providers!");
            }
        } else {
            return resp.getDataProviders();
        }
    }

    @Override
    public Provider retrieveProvider(String provId) throws RepoxException {
        StringBuffer id = new StringBuffer();
        id.append("id=");
        id.append(provId);

        Response resp = invokeRestCall("/dataProviders/getDataProvider", Response.class,
                id.toString());
        return resp.getProvider();
    }

    @Override
    public Provider retrieveProviderByMetadata(String mnemonic) throws RepoxException {
        Provider provider = null;
        DataProviders providers = retrieveProviders();
        for (Provider prov : providers.getProvider()) {
            if (prov.getNameCode() != null && prov.getNameCode().length() > 0 &&
                prov.getNameCode().equals(mnemonic)) {
                provider = prov;
                break;
            }

            if (prov.getName() != null && prov.getName().length() > 0 &&
                prov.getName().equals(mnemonic)) {
                provider = prov;
                break;
            }

            if (prov.getDescription() != null && prov.getDescription().length() > 0 &&
                prov.getDescription().equals(mnemonic)) {
                provider = prov;
                break;
            }
        }
        return provider;
    }
}
