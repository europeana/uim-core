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
import eu.europeana.uim.repox.rest.client.AggregatorRepoxRestClient;
import eu.europeana.uim.repox.rest.client.xml.Aggregator;
import eu.europeana.uim.repox.rest.client.xml.Aggregators;
import eu.europeana.uim.repox.rest.client.xml.Response;

/**
 * Class implementing REST functionality for administrating aggregators.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since 10.02.2013
 */
public class BasicAggregatorRepoxRestClient extends AbstractRepoxRestClient implements
        AggregatorRepoxRestClient {
    /**
     * Creates a new instance of this class.
     */
    public BasicAggregatorRepoxRestClient() {
        this(null);
    }

    /**
     * Creates a new instance of this class.
     * 
     * @param uri
     *            base uri specifying the repox installation
     */
    public BasicAggregatorRepoxRestClient(String uri) {
        super(uri);
    }

    /*
     * Aggregator related operations
     */
    @Override
    public Aggregator createAggregator(Aggregator aggregator) throws RepoxException {
        StringBuffer name = new StringBuffer();
        StringBuffer nameCode = new StringBuffer();
        StringBuffer homepage = new StringBuffer();

        name.append("name=");
        name.append(aggregator.getName());
        nameCode.append("nameCode=");
        nameCode.append(aggregator.getNameCode());
        homepage.append("homepage=");
        homepage.append(aggregator.getUrl());

        Response resp = invokeRestCall("/aggregators/create", Response.class, name.toString(),
                nameCode.toString(), homepage.toString());

        if (resp.getAggregator() == null) {
            if (resp.getError() != null) {
                throw new RepoxException("Could not create aggregator! Reason: " +
                                         resp.getError().getCause());
            } else {
                throw new RepoxException("Unidentified repox error during creation of aggregator!");
            }
        } else {
            return resp.getAggregator();
        }
    }

    @Override
    public String deleteAggregator(String aggregatorId) throws RepoxException {
        StringBuffer id = new StringBuffer();
        id.append("id=");
        id.append(aggregatorId);

        Response resp = invokeRestCall("/aggregators/delete", Response.class, id.toString());

        if (resp.getSuccess() == null) {
            if (resp.getError() != null) {
                throw new RepoxException("Could not delete aggregator! Reason: " +
                                         resp.getError().getCause());
            } else {
                throw new RepoxException("Unidentified repox error during deletion of aggregator!");
            }
        } else {
            return resp.getSuccess();
        }
    }

    @Override
    public Aggregator updateAggregator(Aggregator aggregator) throws RepoxException {
        StringBuffer id = new StringBuffer();
        StringBuffer name = new StringBuffer();
        StringBuffer nameCode = new StringBuffer();
        StringBuffer homepage = new StringBuffer();

        id.append("id=");
        id.append(aggregator.getId());
        name.append("name=");
        name.append(aggregator.getName());
        nameCode.append("nameCode=");
        nameCode.append(aggregator.getNameCode());
        homepage.append("homepage=");
        homepage.append(aggregator.getUrl());

        Response resp = invokeRestCall("/aggregators/update", Response.class, id.toString(),
                name.toString(), nameCode.toString(), homepage.toString());

        if (resp.getAggregator() == null) {
            if (resp.getError() != null) {
                throw new RepoxException("Could not update aggregator! Reason: " +
                                         resp.getError().getCause());
            } else {
                throw new RepoxException("Unidentified repox error during updating of aggregator!");
            }
        } else {
            return resp.getAggregator();
        }
    }

    @Override
    public Aggregators retrieveAggregators() throws RepoxException {
        Response resp = invokeRestCall("/aggregators/list", Response.class);

        if (resp.getAggregators() == null) {
            if (resp.getError() != null) {
                throw new RepoxException("Could not retrieving aggregators! Reason: " +
                                         resp.getError().getCause());
            } else {
                throw new RepoxException(
                        "Unidentified repox error during retrieving of aggregators!");
            }
        } else {
            return resp.getAggregators();
        }
    }

    @Override
    public Aggregator retrieveAggregator(String aggrID) throws RepoxException {
        StringBuffer id = new StringBuffer();
        id.append("id=");
        id.append(aggrID);

        Response resp = invokeRestCall("/aggregators/getAggregator", Response.class, id.toString());
        return resp.getAggregator();
    }

    @Override
    public Aggregator retrieveAggregatorByMetadata(String mnemonic) throws RepoxException {
        Aggregator aggregator = null;
        Aggregators aggregators = retrieveAggregators();
        for (Aggregator aggr : aggregators.getAggregator()) {
            if (aggr.getNameCode() != null && aggr.getNameCode().length() > 0 &&
                aggr.getNameCode().equals(mnemonic)) {
                aggregator = aggr;
                break;
            }

            if (aggr.getName() != null && aggr.getName().length() > 0 &&
                aggr.getName().equals(mnemonic)) {
                aggregator = aggr;
                break;
            }
        }
        return aggregator;
    }
}
