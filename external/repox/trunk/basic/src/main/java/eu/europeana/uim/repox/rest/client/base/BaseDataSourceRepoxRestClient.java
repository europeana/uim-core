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
package eu.europeana.uim.repox.rest.client.base;

import java.util.logging.Logger;

import eu.europeana.uim.repox.RepoxException;
import eu.europeana.uim.repox.rest.client.DataSourceRepoxRestClient;
import eu.europeana.uim.repox.rest.client.xml.DataSources;
import eu.europeana.uim.repox.rest.client.xml.Provider;
import eu.europeana.uim.repox.rest.client.xml.Response;
import eu.europeana.uim.repox.rest.client.xml.Source;

/**
 * Class implementing REST functionality for accessing data source functionality
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since 10.02.2013
 */
public class BaseDataSourceRepoxRestClient extends AbstractRepoxRestClient implements
        DataSourceRepoxRestClient {
    private final static Logger log = Logger.getLogger(BaseDataSourceRepoxRestClient.class.getName());

    /**
     * Creates a new instance of this class.
     */
    public BaseDataSourceRepoxRestClient() {
        this(null);
    }

    /**
     * Creates a new instance of this class.
     * 
     * @param uri
     *            base uri specifying the repox installation
     */
    public BaseDataSourceRepoxRestClient(String uri) {
        super(uri);
    }

    @Override
    public DataSources retrieveDataSources() throws RepoxException {
        Response resp = invokeRestCall("/dataSources/list", Response.class);

        if (resp.getDataSources() == null) {
            if (resp.getError() != null) {
                throw new RepoxException("Could not retrieving sources! Reason: " +
                                         resp.getError().getCause());
            } else {
                throw new RepoxException("Unidentified repox error during retrieving of sources!");
            }
        } else {
            return resp.getDataSources();
        }
    }

    @Override
    public DataSources retrieveProviderDataSources(String providerId) throws RepoxException {
        StringBuffer provId = new StringBuffer();
        provId.append("dataProviderId=");
        provId.append(providerId);

        Response resp = invokeRestCall("/dataSources/list", Response.class, provId.toString());

        if (resp.getDataSources() == null) {
            if (resp.getError() != null) {
                throw new RepoxException("Could not retrieving sources! Reason: " +
                                         resp.getError().getCause());
            } else {
                throw new RepoxException("Unidentified repox error during retrieving of sources!");
            }
        } else {
            return resp.getDataSources();
        }
    }

    @Override
    public Source retrieveDataSource(String dsid) throws RepoxException {
        StringBuffer id = new StringBuffer();
        id.append("id=");
        id.append(dsid);

        Response resp = invokeRestCall("/dataSources/getDataSource", Response.class, id.toString());
        return resp.getSource();
    }

    @Override
    public Source retrieveDataSourceByMetadata(String mnemonic) throws RepoxException {
        log.info("Search mnemonic is " + mnemonic);
        Source source = null;
        DataSources sources = retrieveDataSources();
        for (Source src : sources.getSource()) {
            log.info(src.getNameCode() + "|" + src.getName());

            if (src.getNameCode() != null && src.getNameCode().length() > 0 &&
                src.getNameCode().equals(mnemonic)) {
                source = src;
                break;
            }

            if (src.getName() != null && src.getName().length() > 0 &&
                src.getName().equals(mnemonic)) {
                source = src;
                break;
            }

            if (src.getDescription() != null && src.getDescription().length() > 0 &&
                src.getDescription().equals(mnemonic)) {
                source = src;
                break;
            }
        }
        return source;
    }

    @Override
    public Source createDatasourceOAI(Source ds, Provider prov) throws RepoxException {
        Response resp = createUpdateDSOAI("create", ds, prov);

        if (resp.getSource() == null) {
            if (resp.getError() != null) {
                throw new RepoxException("Could not create OAI source! Reason: " +
                                         resp.getError().getCause());
            } else {
                throw new RepoxException("Unidentified repox error during creating of OAI source!");
            }
        } else {
            return resp.getSource();
        }
    }

    @Override
    public Source createDatasourceZ3950Timestamp(Source ds, Provider prov) throws RepoxException {
        Response resp = createUpdateZ3950Timestamp("create", ds, prov);

        if (resp.getSource() == null) {
            if (resp.getError() != null) {
                throw new RepoxException("Could not create Z3950 source! Reason: " +
                                         resp.getError().getCause());
            } else {
                throw new RepoxException(
                        "Unidentified repox error during creating of Z3950 source!");
            }
        } else {
            return resp.getSource();
        }
    }

    @Override
    public Source createDatasourceZ3950IdFile(Source ds, Provider prov) throws RepoxException {
        Response resp = createUpdateDSZ3950IdFile("create", ds, prov);

        if (resp.getSource() == null) {
            if (resp.getError() != null) {
                throw new RepoxException("Could not create Z3950 source! Reason: " +
                                         resp.getError().getCause());
            } else {
                throw new RepoxException(
                        "Unidentified repox error during creating of Z3950 source!");
            }
        } else {
            return resp.getSource();
        }
    }

    @Override
    public Source createDatasourceZ3950IdSequence(Source ds, Provider prov) throws RepoxException {
        Response resp = createUpdateDSZ3950IdSequence("create", ds, prov);

        if (resp.getSource() == null) {
            if (resp.getError() != null) {
                throw new RepoxException("Could not create Z3950 source! Reason: " +
                                         resp.getError().getCause());
            } else {
                throw new RepoxException(
                        "Unidentified repox error during creating of Z3950 source!");
            }
        } else {
            return resp.getSource();
        }
    }

    @Override
    public Source createDatasourceFtp(Source ds, Provider prov) throws RepoxException {
        Response resp = createUpdateDSFtp("create", ds, prov);

        if (resp.getSource() == null) {
            if (resp.getError() != null) {
                throw new RepoxException("Could not create ftp source! Reason: " +
                                         resp.getError().getCause());
            } else {
                throw new RepoxException("Unidentified repox error during creating of ftp source!");
            }
        } else {
            return resp.getSource();
        }
    }

    @Override
    public Source createDatasourceHttp(Source ds, Provider prov) throws RepoxException {
        Response resp = createUpdateDSHttp("create", ds, prov);

        if (resp.getSource() == null) {
            if (resp.getError() != null) {
                throw new RepoxException("Could not create http source! Reason: " +
                                         resp.getError().getCause());
            } else {
                throw new RepoxException("Unidentified repox error during creating of http source!");
            }
        } else {
            return resp.getSource();
        }
    }

    @Override
    public Source createDatasourceFolder(Source ds, Provider prov) throws RepoxException {
        Response resp = createUpdateDSFolder("create", ds, prov);

        if (resp.getSource() == null) {
            if (resp.getError() != null) {
                throw new RepoxException("Could not create folder source! Reason: " +
                                         resp.getError().getCause());
            } else {
                throw new RepoxException(
                        "Unidentified repox error during creating of folder source!");
            }
        } else {
            return resp.getSource();
        }
    }

    @Override
    public Source updateDatasourceOAI(Source ds) throws RepoxException {
        Response resp = createUpdateDSOAI("update", ds, null);

        if (resp.getSource() == null) {
            if (resp.getError() != null) {
                throw new RepoxException("Could not update OAI source! Reason: " +
                                         resp.getError().getCause());
            } else {
                throw new RepoxException("Unidentified repox error during updating of OAI source!");
            }
        } else {
            return resp.getSource();
        }
    }

    @Override
    public Source updateDatasourceZ3950Timestamp(Source ds) throws RepoxException {
        Response resp = createUpdateZ3950Timestamp("update", ds, null);

        if (resp.getSource() == null) {
            if (resp.getError() != null) {
                throw new RepoxException("Could not update Z3950 source! Reason: " +
                                         resp.getError().getCause());
            } else {
                throw new RepoxException(
                        "Unidentified repox error during updating of Z3950 source!");
            }
        } else {
            return resp.getSource();
        }
    }

    @Override
    public Source updateDatasourceZ3950IdFile(Source ds) throws RepoxException {
        Response resp = createUpdateDSZ3950IdFile("update", ds, null);

        if (resp.getSource() == null) {
            if (resp.getError() != null) {
                throw new RepoxException("Could not update Z3950 source! Reason: " +
                                         resp.getError().getCause());
            } else {
                throw new RepoxException(
                        "Unidentified repox error during updating of Z3950 source!");
            }
        } else {
            return resp.getSource();
        }
    }

    @Override
    public Source updateDatasourceZ3950IdSequence(Source ds) throws RepoxException {
        Response resp = createUpdateDSZ3950IdSequence("update", ds, null);

        if (resp.getSource() == null) {
            if (resp.getError() != null) {
                throw new RepoxException("Could not update Z3950 source! Reason: " +
                                         resp.getError().getCause());
            } else {
                throw new RepoxException(
                        "Unidentified repox error during updating of Z3950 source!");
            }
        } else {
            return resp.getSource();
        }
    }

    @Override
    public Source updateDatasourceFtp(Source ds) throws RepoxException {
        Response resp = createUpdateDSFtp("update", ds, null);

        if (resp.getSource() == null) {
            if (resp.getError() != null) {
                throw new RepoxException("Could not update ftp source! Reason: " +
                                         resp.getError().getCause());
            } else {
                throw new RepoxException("Unidentified repox error during updating of ftp source!");
            }
        } else {
            return resp.getSource();
        }
    }

    @Override
    public Source updateDatasourceHttp(Source ds) throws RepoxException {
        Response resp = createUpdateDSHttp("update", ds, null);

        if (resp.getSource() == null) {
            if (resp.getError() != null) {
                throw new RepoxException("Could not update http source! Reason: " +
                                         resp.getError().getCause());
            } else {
                throw new RepoxException("Unidentified repox error during updating of http source!");
            }
        } else {
            return resp.getSource();
        }
    }

    @Override
    public Source updateDatasourceFolder(Source ds) throws RepoxException {
        Response resp = createUpdateDSFolder("update", ds, null);

        if (resp.getSource() == null) {
            if (resp.getError() != null) {
                throw new RepoxException("Could not update source! Reason: " +
                                         resp.getError().getCause());
            } else {
                throw new RepoxException("Unidentified repox error during updating of source!");
            }
        } else {
            return resp.getSource();
        }
    }

    @Override
    public String deleteDatasource(String dsID) throws RepoxException {
        StringBuffer id = new StringBuffer();
        id.append("id=");
        id.append(dsID);

        Response resp = invokeRestCall("/dataSources/delete", Response.class, id.toString());

        if (resp.getSuccess() == null) {
            if (resp.getError() != null) {
                throw new RepoxException("Could not delete source! Reason: " +
                                         resp.getError().getCause());
            } else {
                throw new RepoxException("Unidentified repox error during deletion of source!");
            }
        } else {
            return resp.getSuccess();
        }
    }

    private Response createUpdateDSOAI(String action, Source ds, Provider prov)
            throws RepoxException {
        StringBuffer dataProviderId = new StringBuffer();
        StringBuffer id = new StringBuffer();
        StringBuffer description = new StringBuffer();
        StringBuffer nameCode = new StringBuffer();
        StringBuffer name = new StringBuffer();
        StringBuffer exportPath = new StringBuffer();
        StringBuffer schema = new StringBuffer();
        StringBuffer namespace = new StringBuffer();

        // Method specific
        StringBuffer metadataFormat = new StringBuffer();
        StringBuffer oaiURL = new StringBuffer();
        StringBuffer oaiSet = new StringBuffer();

        if (action.equals("create")) {
            dataProviderId.append("dataProviderId=");
            dataProviderId.append(prov.getId());
        }
        id.append("id=");
        id.append(ds.getId());
        description.append("description=");
        // description.append(ds.getDescription());
        description.append("NONE");
        nameCode.append("nameCode=");
        nameCode.append(ds.getNameCode());
        name.append("name=");
        name.append(ds.getName());
        exportPath.append("exportPath=");
        exportPath.append(ds.getExportPath());
        schema.append("schema=");
        schema.append(ds.getSchema());
        namespace.append("namespace=");
        namespace.append(ds.getNamespace());
        metadataFormat.append("metadataFormat=");
        metadataFormat.append(ds.getMetadataFormat());
        oaiURL.append("oaiURL=");
        oaiURL.append(ds.getOaiSource());
        oaiSet.append("oaiSet=");
        oaiSet.append(ds.getOaiSet());
        if (action.equals("create")) {
            return invokeRestCall("/dataSources/createOai", Response.class,
                    dataProviderId.toString(), id.toString(), description.toString(),
                    nameCode.toString(), name.toString(), exportPath.toString(), schema.toString(),
                    namespace.toString(), metadataFormat.toString(), oaiURL.toString(),
                    oaiSet.toString());
        } else {
            return invokeRestCall("/dataSources/updateOai", Response.class, id.toString(),
                    description.toString(), nameCode.toString(), name.toString(),
                    exportPath.toString(), schema.toString(), namespace.toString(),
                    metadataFormat.toString(), oaiURL.toString(), oaiSet.toString());
        }
    }

    private Response createUpdateZ3950Timestamp(String action, Source ds, Provider prov)
            throws RepoxException {
        StringBuffer dataProviderId = new StringBuffer();
        StringBuffer id = new StringBuffer();
        StringBuffer description = new StringBuffer();
        StringBuffer nameCode = new StringBuffer();
        StringBuffer name = new StringBuffer();
        StringBuffer exportPath = new StringBuffer();
        StringBuffer schema = new StringBuffer();
        StringBuffer namespace = new StringBuffer();

        // Method specific
        StringBuffer address = new StringBuffer();
        StringBuffer port = new StringBuffer();
        StringBuffer database = new StringBuffer();
        StringBuffer user = new StringBuffer();
        StringBuffer password = new StringBuffer();
        StringBuffer recordSyntax = new StringBuffer();
        StringBuffer charset = new StringBuffer();
        StringBuffer earliestTimestamp = new StringBuffer();
        StringBuffer recordIdPolicy = new StringBuffer();

        StringBuffer idXpath = new StringBuffer();
        StringBuffer namespacePrefix = new StringBuffer();
        StringBuffer namespaceUri = new StringBuffer();

        if (action.equals("create")) {
            dataProviderId.append("dataProviderId=");
            dataProviderId.append(prov.getId());
        }
        id.append("id=");
        id.append(ds.getId());
        description.append("description=");
        description.append(ds.getDescription());
        nameCode.append("nameCode=");
        nameCode.append(ds.getNameCode());
        name.append("name=");
        name.append(ds.getName());
        exportPath.append("exportPath=");
        exportPath.append(ds.getExportPath());
        schema.append("schema=");
        schema.append(ds.getSchema());

        namespace.append("namespace=");
        namespace.append(ds.getNamespace());

        if (ds.getTarget() != null) {
            address.append("address=");
            address.append(ds.getTarget().getAddress());
            port.append("port=");
            port.append(ds.getTarget().getPort());
            database.append("database=");
            database.append(ds.getTarget().getDatabase());

            user.append("user=");
            user.append(ds.getTarget().getUser());
            password.append("password=");
            password.append(ds.getTarget().getPassword());
            recordSyntax.append("recordSyntax=");
            recordSyntax.append(ds.getTarget().getRecordSyntax());

            charset.append("charset=");
            charset.append(ds.getTarget().getCharset());
        }

        earliestTimestamp.append("earliestTimestamp=");
        earliestTimestamp.append(ds.getEarliestTimestamp());

        if (ds.getRecordIdPolicy() != null) {
            recordIdPolicy.append("recordIdPolicy=");
            recordIdPolicy.append(ds.getRecordIdPolicy().getType());
            if (ds.getRecordIdPolicy().getType().equals("idExported")) {
                idXpath.append("idXpath=");
                idXpath.append(ds.getRecordIdPolicy().getIdXpath());

                namespacePrefix.append("namespacePrefix=");
                namespacePrefix.append(ds.getRecordIdPolicy().getNamespaces().getNamespace().getNamespacePrefix());
                namespaceUri.append("namespaceUri=");
                namespaceUri.append(ds.getRecordIdPolicy().getNamespaces().getNamespace().getNamespaceUri());
            }
        }

        if (action.equals("create")) {
            Response resp = invokeRestCall("/dataSources/createZ3950Timestamp", Response.class,
                    dataProviderId.toString(), id.toString(), description.toString(),
                    nameCode.toString(), name.toString(), exportPath.toString(), schema.toString(),
                    namespace.toString(), address.toString(), port.toString(), database.toString(),
                    user.toString(), password.toString(), recordSyntax.toString(),
                    charset.toString(), earliestTimestamp.toString(), recordIdPolicy.toString(),
                    idXpath.toString(), namespacePrefix.toString(), namespaceUri.toString());
            return resp;
        } else {
            Response resp = invokeRestCall("/dataSources/updateZ3950Timestamp", Response.class,
                    id.toString(), description.toString(), nameCode.toString(), name.toString(),
                    exportPath.toString(), schema.toString(), namespace.toString(),
                    address.toString(), port.toString(), database.toString(), user.toString(),
                    password.toString(), recordSyntax.toString(), charset.toString(),
                    earliestTimestamp.toString(), recordIdPolicy.toString(), idXpath.toString(),
                    namespacePrefix.toString(), namespaceUri.toString());
            return resp;
        }
    }

    private Response createUpdateDSZ3950IdFile(String action, Source ds, Provider prov)
            throws RepoxException {
        StringBuffer dataProviderId = new StringBuffer();
        StringBuffer id = new StringBuffer();
        StringBuffer description = new StringBuffer();
        StringBuffer nameCode = new StringBuffer();
        StringBuffer name = new StringBuffer();
        StringBuffer exportPath = new StringBuffer();
        StringBuffer schema = new StringBuffer();
        StringBuffer namespace = new StringBuffer();

        // Method specific
        StringBuffer address = new StringBuffer();
        StringBuffer port = new StringBuffer();
        StringBuffer database = new StringBuffer();
        StringBuffer user = new StringBuffer();
        StringBuffer password = new StringBuffer();
        StringBuffer recordSyntax = new StringBuffer();
        StringBuffer charset = new StringBuffer();

        StringBuffer filePath = new StringBuffer();
        StringBuffer recordIdPolicy = new StringBuffer();
        StringBuffer idXpath = new StringBuffer();
        StringBuffer namespacePrefix = new StringBuffer();
        StringBuffer namespaceUri = new StringBuffer();
        if (action.equals("create")) {
            dataProviderId.append("dataProviderId=");
            dataProviderId.append(prov.getId());
        }
        id.append("id=");
        id.append(ds.getId());
        description.append("description=");
        description.append(ds.getDescription());
        nameCode.append("nameCode=");
        nameCode.append(ds.getNameCode());
        name.append("name=");
        name.append(ds.getName());
        exportPath.append("exportPath=");
        exportPath.append(ds.getExportPath());
        schema.append("schema=");
        schema.append(ds.getSchema());
        namespace.append("namespace=");
        namespace.append(ds.getNamespace());
        address.append("address=");
        address.append(ds.getTarget().getAddress());
        port.append("port=");
        port.append(ds.getTarget().getPort());
        database.append("database=");
        database.append(ds.getTarget().getDatabase());

        user.append("user=");
        user.append(ds.getTarget().getUser());
        password.append("password=");
        password.append(ds.getTarget().getPassword());
        recordSyntax.append("recordSyntax=");
        recordSyntax.append(ds.getTarget().getRecordSyntax());

        charset.append("charset=");
        charset.append(ds.getTarget().getCharset());
        filePath.append("filePath=");
        filePath.append(ds.getFilePath());
        recordIdPolicy.append("recordIdPolicy=");
        recordIdPolicy.append(ds.getRecordIdPolicy().getType());
        if (ds.getRecordIdPolicy().getType().equals("idExported")) {
            idXpath.append("idXpath=");
            idXpath.append(ds.getRecordIdPolicy().getIdXpath());

            namespacePrefix.append("namespacePrefix=");
            namespacePrefix.append(ds.getRecordIdPolicy().getNamespaces().getNamespace().getNamespacePrefix());
            namespaceUri.append("namespaceUri=");
            namespaceUri.append(ds.getRecordIdPolicy().getNamespaces().getNamespace().getNamespaceUri());
        }

        if (action.equals("create")) {
            Response resp = invokeRestCall("/dataSources/createZ3950IdList", Response.class,
                    dataProviderId.toString(), id.toString(), description.toString(),
                    nameCode.toString(), name.toString(), exportPath.toString(), schema.toString(),
                    namespace.toString(), address.toString(), port.toString(), database.toString(),
                    user.toString(), password.toString(), recordSyntax.toString(),
                    charset.toString(), filePath.toString(), recordIdPolicy.toString(),
                    idXpath.toString(), namespacePrefix.toString(), namespaceUri.toString());
            return resp;
        } else {
            Response resp = invokeRestCall("/dataSources/updateZ3950IdList", Response.class,
                    id.toString(), description.toString(), nameCode.toString(), name.toString(),
                    exportPath.toString(), schema.toString(), namespace.toString(),
                    address.toString(), port.toString(), database.toString(), user.toString(),
                    password.toString(), recordSyntax.toString(), charset.toString(),
                    filePath.toString(), recordIdPolicy.toString(), idXpath.toString(),
                    namespacePrefix.toString(), namespaceUri.toString());
            return resp;
        }
    }

    private Response createUpdateDSZ3950IdSequence(String action, Source ds, Provider prov)
            throws RepoxException {
        StringBuffer dataProviderId = new StringBuffer();
        StringBuffer id = new StringBuffer();
        StringBuffer description = new StringBuffer();
        StringBuffer nameCode = new StringBuffer();
        StringBuffer name = new StringBuffer();
        StringBuffer exportPath = new StringBuffer();
        StringBuffer schema = new StringBuffer();
        StringBuffer namespace = new StringBuffer();

        // Method specific
        StringBuffer address = new StringBuffer();
        StringBuffer port = new StringBuffer();
        StringBuffer database = new StringBuffer();
        StringBuffer user = new StringBuffer();
        StringBuffer password = new StringBuffer();
        StringBuffer recordSyntax = new StringBuffer();
        StringBuffer charset = new StringBuffer();

        StringBuffer maximumId = new StringBuffer();

        StringBuffer recordIdPolicy = new StringBuffer();
        StringBuffer idXpath = new StringBuffer();
        StringBuffer namespacePrefix = new StringBuffer();
        StringBuffer namespaceUri = new StringBuffer();
        if (action.equals("create")) {
            dataProviderId.append("dataProviderId=");
            dataProviderId.append(prov.getId());
        }
        id.append("id=");
        id.append(ds.getId());
        description.append("description=");
        description.append(ds.getDescription());
        nameCode.append("nameCode=");
        nameCode.append(ds.getNameCode());
        name.append("name=");
        name.append(ds.getName());
        exportPath.append("exportPath=");
        exportPath.append(ds.getExportPath());
        schema.append("schema=");
        schema.append(ds.getSchema());
        namespace.append("namespace=");
        namespace.append(ds.getNamespace());
        address.append("address=");
        address.append(ds.getTarget().getAddress());
        port.append("port=");
        port.append(ds.getTarget().getPort());
        database.append("database=");
        database.append(ds.getTarget().getDatabase());

        user.append("user=");
        user.append(ds.getTarget().getUser());
        password.append("password=");
        password.append(ds.getTarget().getPassword());
        recordSyntax.append("recordSyntax=");
        recordSyntax.append(ds.getTarget().getRecordSyntax());

        charset.append("charset=");
        charset.append(ds.getTarget().getCharset());

        maximumId.append("maximumId=");
        maximumId.append(ds.getMaximumId());

        recordIdPolicy.append("recordIdPolicy=");
        recordIdPolicy.append(ds.getRecordIdPolicy().getType());
        if (ds.getRecordIdPolicy().getType().equals("idExported")) {
            idXpath.append("idXpath=");
            idXpath.append(ds.getRecordIdPolicy().getIdXpath());

            namespacePrefix.append("namespacePrefix=");
            namespacePrefix.append(ds.getRecordIdPolicy().getNamespaces().getNamespace().getNamespacePrefix());
            namespaceUri.append("namespaceUri=");
            namespaceUri.append(ds.getRecordIdPolicy().getNamespaces().getNamespace().getNamespaceUri());
        }

        if (action.equals("create")) {
            return invokeRestCall("/dataSources/createZ3950IdSequence", Response.class,
                    dataProviderId.toString(), id.toString(), description.toString(),
                    nameCode.toString(), name.toString(), exportPath.toString(), schema.toString(),
                    namespace.toString(), address.toString(), port.toString(), database.toString(),
                    user.toString(), password.toString(), recordSyntax.toString(),
                    charset.toString(), maximumId.toString(), recordIdPolicy.toString(),
                    idXpath.toString(), namespacePrefix.toString(), namespaceUri.toString());
        } else {
            return invokeRestCall("/dataSources/updateZ3950IdSequence", Response.class,
                    id.toString(), description.toString(), nameCode.toString(), name.toString(),
                    exportPath.toString(), schema.toString(), namespace.toString(),
                    address.toString(), port.toString(), database.toString(), user.toString(),
                    password.toString(), recordSyntax.toString(), charset.toString(),
                    maximumId.toString(), recordIdPolicy.toString(), idXpath.toString(),
                    namespacePrefix.toString(), namespaceUri.toString());
        }
    }

    private Response createUpdateDSFtp(String action, Source ds, Provider prov)
            throws RepoxException {
        StringBuffer dataProviderId = new StringBuffer();
        StringBuffer id = new StringBuffer();
        StringBuffer description = new StringBuffer();
        StringBuffer nameCode = new StringBuffer();
        StringBuffer name = new StringBuffer();
        StringBuffer exportPath = new StringBuffer();
        StringBuffer schema = new StringBuffer();
        StringBuffer namespace = new StringBuffer();

        // Method specific
        StringBuffer metadataFormat = new StringBuffer();
        StringBuffer isoFormat = new StringBuffer();
        StringBuffer charset = new StringBuffer();
        StringBuffer recordIdPolicy = new StringBuffer();
        StringBuffer idXpath = new StringBuffer();
        StringBuffer namespacePrefix = new StringBuffer();
        StringBuffer namespaceUri = new StringBuffer();
        StringBuffer recordXPath = new StringBuffer();
        StringBuffer server = new StringBuffer();
        StringBuffer user = new StringBuffer();
        StringBuffer password = new StringBuffer();
        StringBuffer ftpPath = new StringBuffer();

        if (action.equals("create")) {
            dataProviderId.append("dataProviderId=");
            dataProviderId.append(prov.getId());
        }
        id.append("id=");
        id.append(ds.getId());
        description.append("description=");
        description.append(ds.getDescription());
        nameCode.append("nameCode=");
        nameCode.append(ds.getNameCode());
        name.append("name=");
        name.append(ds.getName());
        exportPath.append("exportPath=");
        exportPath.append(ds.getExportPath());
        schema.append("schema=");
        schema.append(ds.getSchema());
        namespace.append("namespace=");
        namespace.append(ds.getNamespace());
        metadataFormat.append("metadataFormat=");
        metadataFormat.append(ds.getMetadataFormat());
        // TODO: isoFormat and charset and ftpPath are missing
        isoFormat.append("isoFormat=");
        isoFormat.append(ds.getIsoFormat());
        // isoFormat.append("test");

        if (ds.getTarget() != null) {
            charset.append("charset=");
            charset.append(ds.getTarget().getCharset());
        }

        recordIdPolicy.append("recordIdPolicy=");
        recordIdPolicy.append(ds.getRecordIdPolicy().getType());
        if (ds.getRecordIdPolicy().getType().equals("idExported")) {
            idXpath.append("idXpath=");
            idXpath.append(ds.getRecordIdPolicy().getIdXpath());
            namespacePrefix.append("namespacePrefix=");
            namespacePrefix.append(ds.getRecordIdPolicy().getNamespaces().getNamespace().getNamespacePrefix());
            namespaceUri.append("namespaceUri=");
            namespaceUri.append(ds.getRecordIdPolicy().getNamespaces().getNamespace().getNamespaceUri());
        }
        recordXPath.append("recordXPath=");
        recordXPath.append(ds.getSplitRecords().getRecordXPath());

        server.append("server=");
        server.append(ds.getRetrieveStrategy().getServer());
        user.append("user=");
        user.append(ds.getRetrieveStrategy().getUser());
        password.append("password=");
        password.append(ds.getRetrieveStrategy().getPassword());

        ftpPath.append("ftpPath=");
        // ftpPath.append("/test");
        ftpPath.append(ds.getFtpPath());

        if (action.equals("create")) {
            return invokeRestCall("/dataSources/createFtp", Response.class,
                    dataProviderId.toString(), id.toString(), description.toString(),
                    nameCode.toString(), name.toString(), exportPath.toString(), schema.toString(),
                    namespace.toString(), metadataFormat.toString(), isoFormat.toString(),
                    charset.toString(), recordIdPolicy.toString(), idXpath.toString(),
                    namespacePrefix.toString(), namespaceUri.toString(), recordXPath.toString(),
                    server.toString(), user.toString(), password.toString(), ftpPath.toString());
        } else {
            return invokeRestCall("/dataSources/updateFtp", Response.class, id.toString(),
                    description.toString(), nameCode.toString(), name.toString(),
                    exportPath.toString(), schema.toString(), namespace.toString(),
                    metadataFormat.toString(), isoFormat.toString(), charset.toString(),
                    recordIdPolicy.toString(), idXpath.toString(), namespacePrefix.toString(),
                    namespaceUri.toString(), recordXPath.toString(), server.toString(),
                    user.toString(), password.toString(), ftpPath.toString());
        }
    }

    private Response createUpdateDSHttp(String action, Source ds, Provider prov)
            throws RepoxException {
        StringBuffer dataProviderId = new StringBuffer();
        StringBuffer id = new StringBuffer();
        StringBuffer description = new StringBuffer();
        StringBuffer nameCode = new StringBuffer();
        StringBuffer name = new StringBuffer();
        StringBuffer exportPath = new StringBuffer();
        StringBuffer schema = new StringBuffer();
        StringBuffer namespace = new StringBuffer();

        // Method specific
        StringBuffer metadataFormat = new StringBuffer();
        StringBuffer isoFormat = new StringBuffer();
        StringBuffer charset = new StringBuffer();
        StringBuffer recordIdPolicy = new StringBuffer();
        StringBuffer idXpath = new StringBuffer();
        StringBuffer namespacePrefix = new StringBuffer();
        StringBuffer namespaceUri = new StringBuffer();
        StringBuffer recordXPath = new StringBuffer();

        StringBuffer url = new StringBuffer();
        if (action.equals("create")) {
            dataProviderId.append("dataProviderId=");
            dataProviderId.append(prov.getId());
        }
        id.append("id=");
        id.append(ds.getId());
        description.append("description=");
        description.append(ds.getDescription());
        nameCode.append("nameCode=");
        nameCode.append(ds.getNameCode());
        name.append("name=");
        name.append(ds.getName());
        exportPath.append("exportPath=");
        exportPath.append(ds.getExportPath());
        schema.append("schema=");
        schema.append(ds.getSchema());
        namespace.append("namespace=");
        namespace.append(ds.getNamespace());
        metadataFormat.append("metadataFormat=");
        metadataFormat.append(ds.getMetadataFormat());
        // TODO: not supported
        isoFormat.append("isoFormat=");
        isoFormat.append(ds.getIsoFormat());
        charset.append("charset=");
        charset.append(ds.getTarget().getCharset());
        recordIdPolicy.append("recordIdPolicy=");
        recordIdPolicy.append(ds.getRecordIdPolicy().getType());
        if (ds.getRecordIdPolicy().getType().equals("idExported")) {
            idXpath.append("idXpath=");
            idXpath.append(ds.getRecordIdPolicy().getIdXpath());
            namespacePrefix.append("namespacePrefix=");
            namespacePrefix.append(ds.getRecordIdPolicy().getNamespaces().getNamespace().getNamespacePrefix());
            namespaceUri.append("namespaceUri=");
            namespaceUri.append(ds.getRecordIdPolicy().getNamespaces().getNamespace().getNamespaceUri());

            recordXPath.append("recordXPath=");
            recordXPath.append(ds.getSplitRecords().getRecordXPath());
        }
        url.append("url=");
        url.append(ds.getRetrieveStrategy().getUrl());

        if (action.equals("create")) {
            return invokeRestCall("/dataSources/createHttp", Response.class,
                    dataProviderId.toString(), id.toString(), description.toString(),
                    nameCode.toString(), name.toString(), exportPath.toString(), schema.toString(),
                    namespace.toString(), metadataFormat.toString(), isoFormat.toString(),
                    charset.toString(), recordIdPolicy.toString(), idXpath.toString(),
                    namespacePrefix.toString(), namespaceUri.toString(), recordXPath.toString(),
                    url.toString());
        } else {
            return invokeRestCall("/dataSources/updateHttp", Response.class, id.toString(),
                    description.toString(), nameCode.toString(), name.toString(),
                    exportPath.toString(), schema.toString(), namespace.toString(),
                    metadataFormat.toString(), isoFormat.toString(), charset.toString(),
                    recordIdPolicy.toString(), idXpath.toString(), namespacePrefix.toString(),
                    namespaceUri.toString(), recordXPath.toString(), url.toString());
        }
    }

    private Response createUpdateDSFolder(String action, Source ds, Provider prov)
            throws RepoxException {
        StringBuffer dataProviderId = new StringBuffer();
        StringBuffer id = new StringBuffer();
        StringBuffer description = new StringBuffer();
        StringBuffer nameCode = new StringBuffer();
        StringBuffer name = new StringBuffer();
        StringBuffer exportPath = new StringBuffer();
        StringBuffer schema = new StringBuffer();
        StringBuffer namespace = new StringBuffer();

        // Method specific
        StringBuffer metadataFormat = new StringBuffer();
        StringBuffer isoFormat = new StringBuffer();
        StringBuffer charset = new StringBuffer();
        StringBuffer recordIdPolicy = new StringBuffer();
        StringBuffer idXpath = new StringBuffer();
        StringBuffer namespacePrefix = new StringBuffer();
        StringBuffer namespaceUri = new StringBuffer();
        StringBuffer recordXPath = new StringBuffer();

        StringBuffer folder = new StringBuffer();
        if (action.equals("create")) {
            dataProviderId.append("dataProviderId=");
            dataProviderId.append(prov.getId());
        }
        id.append("id=");
        id.append(ds.getId());
        description.append("description=");
        description.append(ds.getDescription());
        nameCode.append("nameCode=");
        nameCode.append(ds.getNameCode());
        name.append("name=");
        name.append(ds.getName());
        exportPath.append("exportPath=");
        exportPath.append(ds.getExportPath());
        schema.append("schema=");
        schema.append(ds.getSchema());
        namespace.append("namespace=");
        namespace.append(ds.getNamespace());
        metadataFormat.append("metadataFormat=");
        metadataFormat.append(ds.getMetadataFormat());
        // todo: isoFormat charset folder are missing
        isoFormat.append("isoFormat=");
        isoFormat.append(ds.getIsoFormat());
        // isoFormat.append("test");
        if (ds.getTarget() != null) {
            charset.append("charset=");
            charset.append(ds.getTarget().getCharset());
        }
        recordIdPolicy.append("recordIdPolicy=");
        recordIdPolicy.append(ds.getRecordIdPolicy().getType());
        if (ds.getRecordIdPolicy().getType().equals("idExported")) {
            idXpath.append("idXpath=");

            idXpath.append(ds.getRecordIdPolicy().getIdXpath());
            namespacePrefix.append("namespacePrefix=");
            namespacePrefix.append(ds.getRecordIdPolicy().getNamespaces().getNamespace().getNamespacePrefix());
            namespaceUri.append("namespaceUri=");
            namespaceUri.append(ds.getRecordIdPolicy().getNamespaces().getNamespace().getNamespaceUri());

            recordXPath.append("recordXPath=");
            recordXPath.append(ds.getSplitRecords().getRecordXPath());
        }
        folder.append("folder=");
        folder.append("/tmp");
        // folder.append(ds.getFolder());

        if (action.equals("create")) {
            return invokeRestCall("/dataSources/createFolder", Response.class,
                    dataProviderId.toString(), id.toString(), description.toString(),
                    nameCode.toString(), name.toString(), exportPath.toString(), schema.toString(),
                    namespace.toString(), metadataFormat.toString(), isoFormat.toString(),
                    charset.toString(), recordIdPolicy.toString(), idXpath.toString(),
                    namespacePrefix.toString(), namespaceUri.toString(), recordXPath.toString(),
                    folder.toString());
        } else {
            return invokeRestCall("/dataSources/updateFolder", Response.class, id.toString(),
                    description.toString(), nameCode.toString(), name.toString(),
                    exportPath.toString(), schema.toString(), namespace.toString(),
                    metadataFormat.toString(), isoFormat.toString(), charset.toString(),
                    recordIdPolicy.toString(), idXpath.toString(), namespacePrefix.toString(),
                    namespaceUri.toString(), recordXPath.toString(), folder.toString());
        }
    }
}
