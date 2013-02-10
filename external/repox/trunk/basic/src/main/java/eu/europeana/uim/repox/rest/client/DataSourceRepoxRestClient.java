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
package eu.europeana.uim.repox.rest.client;

import eu.europeana.uim.repox.RepoxException;
import eu.europeana.uim.repox.rest.client.xml.DataSources;
import eu.europeana.uim.repox.rest.client.xml.Provider;
import eu.europeana.uim.repox.rest.client.xml.Source;

/**
 * Interface declaration of the Repox REST client OSGI service for data source specifics.
 * 
 * @author Georgios Markakis
 * @author Yorgos Mamakis
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since 10.02.2013
 */
public interface DataSourceRepoxRestClient {
    /**
     * Retrieve all available Repox DataSources
     * 
     * @return a DataSources object
     * @throws RepoxException
     */
    DataSources retrieveDataSources() throws RepoxException;

    /**
     * Retrieve all available Repox DataSources for a specific provider
     * 
     * @param providerId
     *            identifier of provider
     * @return a DataSources object
     * @throws RepoxException
     */
    DataSources retrieveProviderDataSources(String providerId) throws RepoxException;

    /**
     * @param sourceId
     * @return specific data source
     * @throws RepoxException
     */
    Source retrieveDataSource(String sourceId) throws RepoxException;

    /**
     * @param mnemonic
     * @return specific data source
     * @throws RepoxException
     */
    Source retrieveDataSourceByMetadata(String mnemonic) throws RepoxException;

    /**
     * Creates an OAI DataSource. It accesses the following REST Interface:
     * 
     * <code>
     *    /rest/dataSources/createOai?
     *    dataProviderId=DPRestr0&
     *    id=bdaSet&
     *    description=Biblioteca Digital Do Alentejo&
     *    nameCode=00123&
     *    name=Alentejo&
     *    exportPath=D:/Projectos/repoxdata_new&
     *    schema=http://www.europeana.eu/schemas/ese/ESE-V3.3.xsd&
     *    namespace=http://www.europeana.eu/schemas/ese/&
     *    metadataFormat=ese&
     *    oaiURL=http://bd1.inesc-id.pt:8080/repoxel/OAIHandler&
     *    oaiSet=bda
     *  </code>
     * 
     * @param ds
     *            a DataSource object
     * @param prov
     *            provider to the source
     * @return a Source object
     * @throws RepoxException
     */
    Source createDatasourceOAI(Source ds, Provider prov) throws RepoxException;

    /**
     * Creates a Z3950Timestamp DataSource. It accesses the following REST Interface:
     * 
     * <code>
     *  /rest/dataSources/createZ3950Timestamp?
     *   dataProviderId=DPRestr0&
     *   id=z3950TimeTest&
     *   description=test Z39.50 with time stamp&nameCode=00130&
     *   name=Z3950-TimeStamp&
     *   exportPath=D:/Projectos/repoxdata_new&
     *   schema=info:lc/xmlns/marcxchange-v1.xsd&
     *   namespace=info:lc/xmlns/marcxchange-v1&
     *   address=193.6.201.205&
     *   port=1616&
     *   database=B1&
     *   user=&
     *   password=&
     *   recordSyntax=usmarc&
     *   charset=UTF-8&
     *   earliestTimestamp=20110301&
     *   recordIdPolicy=IdGenerated&
     *   idXpath=&
     *   namespacePrefix=&
     *   namespaceUri=
     *  </code>
     * 
     * @param ds
     *            a DataSource object
     * @param prov
     *            provider to the source
     * @return a Source object
     * @throws RepoxException
     */
    Source createDatasourceZ3950Timestamp(Source ds, Provider prov) throws RepoxException;

    /**
     * Creates a Z3950IdFile DataSource. It accesses the following REST Interface:
     * 
     * <code>
     *    /rest/dataSources/createZ3950IdList?
     *    dataProviderId=DPRestr0&
     *    id=z3950IdFile&
     *    description=test Z39.50 with id list&
     *    nameCode=00124&
     *    name=Z3950-IdFile&
     *    exportPath=D:/Projectos/repoxdata_new&
     *    schema=info:lc/xmlns/marcxchange-v1.xsd&
     *    namespace=info:lc/xmlns/marcxchange-v1&
     *    address=aleph.lbfl.li&
     *    port=9909&
     *    database=LLB_IDS&
     *    user=&
     *    password=&
     *    recordSyntax=usmarc&
     *    charset=UTF-8&
     *    filePath=C:\folderZ3950\1900028192z3960idList.txt&
     *    recordIdPolicy=IdGenerated&
     *    idXpath=&
     *    namespacePrefix=&
     *    namespaceUri=
     *  </code>
     * 
     * @param ds
     *            a DataSource object
     * @param prov
     *            provider to the source
     * @return a Source object
     * @throws RepoxException
     */
    Source createDatasourceZ3950IdFile(Source ds, Provider prov) throws RepoxException;

    /**
     * Creates a Z3950IdSequence DataSource. It accesses the following REST Interface:
     * 
     * <code>
     *   /rest/dataSources/createZ3950IdSequence?
     *   dataProviderId=DPRestr0&
     *   id=z3950IdSeqTest&
     *   description=test%20Z39.50%20with%20id%20sequence&
     *   nameCode=00129&
     *   name=Z3950-IdSeq&
     *   exportPath=D:/Projectos/repoxdata_new&
     *   schema=info:lc/xmlns/marcxchange-v1.xsd&
     *   namespace=info:lc/xmlns/marcxchange-v1&
     *   address=aleph.lbfl.li&
     *   port=9909&
     *   database=LLB_IDS&
     *   user=&
     *   password=&
     *   recordSyntax=usmarc&
     *   charset=UTF-8&
     *   maximumId=6000&
     *   recordIdPolicy=IdGenerated&
     *   idXpath=&
     *   namespacePrefix=&
     *   namespaceUri=
     *  </code>
     * 
     * @param ds
     *            a DataSource object
     * @param prov
     *            provider to the source
     * @return a Source object
     * @throws RepoxException
     */
    Source createDatasourceZ3950IdSequence(Source ds, Provider prov) throws RepoxException;

    /**
     * Creates an Ftp DataSource. It accesses the following REST Interface:
     * 
     * <code>
     *   /rest/dataSources/createFtp?
     *   dataProviderId=DPRestr0&
     *   id=ftpTest&
     *   description=test FTP data source&
     *   nameCode=00124&
     *   name=FTP&
     *   exportPath=D:/Projectos/repoxdata_new&
     *   schema=http://www.europeana.eu/schemas/ese/ESE-V3.3.xsd&
     *   namespace=http://www.europeana.eu/schemas/ese/&
     *   metadataFormat=ese&
     *   isoFormat=&
     *   charset=&
     *   recordIdPolicy=IdGenerated&
     *   idXpath=&
     *   namespacePrefix=&
     *   namespaceUri=&
     *   recordXPath=record&
     *   server=bd1.inesc-id.pt&
     *   user=ftp&
     *   password=pmath2010.&
     *   ftpPath=/Lizbeth
     *  </code>
     * 
     * @param ds
     *            a DataSource object
     * @param prov
     *            provider to the source
     * @return a Source object
     * @throws RepoxException
     */
    Source createDatasourceFtp(Source ds, Provider prov) throws RepoxException;

    /**
     * Creates an Http DataSource. It accesses the following REST Interface:
     * 
     * <code>
     *     /rest/dataSources/createHttp?
     *     dataProviderId=DPRestr0&
     *     id=httpTest&
     *     description=test HTTP data source&
     *     nameCode=00124&
     *     name=HTTP&
     *     exportPath=D:/Projectos/repoxdata_new&
     *     schema=http://www.europeana.eu/schemas/ese/ESE-V3.3.xsd&
     *     namespace=http://www.europeana.eu/schemas/ese/&
     *     metadataFormat=ese&
     *     isoFormat=&
     *     charset=&
     *     recordIdPolicy=IdGenerated&
     *     idXpath=&
     *     namespacePrefix=&
     *     namespaceUri=&
     *     recordXPath=record&
     *     url=http://digmap2.ist.utl.pt:8080/index_digital/contente/09428_Ag_DE_ELocal.zip
     *  </code>
     * 
     * @param ds
     *            a DataSource object
     * @param prov
     *            provider to the source
     * @return a Source object
     * @throws RepoxException
     */
    Source createDatasourceHttp(Source ds, Provider prov) throws RepoxException;

    /**
     * Creates an Folder DataSource. It accesses the following REST Interface:
     * 
     * <code>
     *     /rest/dataSources/createFolder?
     *     id=folderTest&
     *     description=test%20Folder%20data%20source3333333&
     *     nameCode=4444444444400124&
     *     name=Folder&
     *     exportPath=D:/Projectos/repoxdata_new&
     *     schema=info:lc/xmlns/marcxchange-v1.xsd&
     *     namespace=info:lc/xmlns/marcxchange-v1&
     *     metadataFormat=ISO2709&
     *     isoFormat=pt.utl.ist.marc.iso2709.IteratorIso2709&
     *     charset=UTF-8&
     *     recordIdPolicy=IdExtracted&
     *     idXpath=/mx:record/mx:controlfield[@tag=%22001%22]&
     *     namespacePrefix=mx&
     *     namespaceUri=info:lc/xmlns/marcxchange-v1&
     *     recordXPath=&
     *     folder=C:\folderNew
     *  </code>
     * 
     * @param ds
     *            a DataSource object
     * @param prov
     *            provider to the source
     * @return a Source object
     * @throws RepoxException
     */
    Source createDatasourceFolder(Source ds, Provider prov) throws RepoxException;

    /**
     * Updates an OAI DataSource. It accesses the following REST Interface:
     * 
     * <code>
     *     /rest/dataSources/updateOai?
     *     id=bdaSet&
     *     description=222Biblioteca Digital Do Alentejo&
     *     nameCode=333300123&
     *     name=4444Alentejo&
     *     exportPath=D:/Projectos/repoxdata_new2&
     *     schema=http://www.europeana.eu/schemas/ese/ESE-V3.3.xsd&
     *     namespace=http://www.europeana.eu/schemas/ese/&
     *     metadataFormat=ese&
     *     oaiURL=http://bd1.inesc-id.pt:8080/repoxel/OAIHandler&
     *     oaiSet=bda
     *  </code>
     * 
     * @param ds
     *            a Source object
     * @param prov
     *            provider to the source
     * @return a Source object
     * @throws RepoxException
     */
    Source updateDatasourceOAI(Source ds) throws RepoxException;

    /**
     * Updates a Z3950Timestamp DataSource. It accesses the following REST Interface:
     * 
     * <code>
     *     /rest/dataSources/updateZ3950Timestamp?
     *     id=z3950TimeTest&
     *     description=new test Z39.50 with time stamp&
     *     nameCode=99900130&
     *     name=Z3950-TimeStampWorking&
     *     exportPath=D:/Projectos/repoxdata_new&
     *     schema=info:lc/xmlns/marcxchange-v1.xsd&
     *     namespace=info:lc/xmlns/marcxchange-v1&
     *     address=193.6.201.205&
     *     port=1616&
     *     database=B1&
     *     user=&
     *     password=&
     *     recordSyntax=usmarc&
     *     charset=UTF-8&
     *     earliestTimestamp=20110301&
     *     recordIdPolicy=IdGenerated&
     *     idXpath=&
     *     namespacePrefix=&
     *     namespaceUri=
     *  </code>
     * 
     * @param ds
     *            a Source object
     * @param prov
     *            provider to the source
     * @return a Source object
     * @throws RepoxException
     */
    Source updateDatasourceZ3950Timestamp(Source ds) throws RepoxException;

    /**
     * Updates a Z3950IdFile DataSource. It accesses the following REST Interface:
     * 
     * <code>
     *      /rest/dataSources/updateZ3950IdList?
     *      id=z3950IdFile&
     *      description=new test Z39.50 with id list&
     *      nameCode=001245555&
     *      name=Z3950-IdFilenew&
     *      exportPath=D:/Projectos/repoxdata_new1&
     *      schema=info:lc/xmlns/marcxchange-v1.xsd&
     *      namespace=info:lc/xmlns/marcxchange-v1&
     *      address=aleph.lbfl.li&port=9909&
     *      database=LLB_IDS&
     *      user=&
     *      password=&
     *      recordSyntax=usmarc&
     *      charset=UTF-8&
     *      filePath=C:\folderZ3950\newFile.txt&
     *      recordIdPolicy=IdGenerated&
     *      idXpath=&
     *      namespacePrefix=&
     *      namespaceUri=
     *  </code>
     * 
     * @param ds
     *            a Source object
     * @param prov
     *            provider to the source
     * @return a Source object
     * @throws RepoxException
     */
    Source updateDatasourceZ3950IdFile(Source ds) throws RepoxException;

    /**
     * Updates a Z3950IdSequence DataSource. It accesses the following REST Interface:
     * 
     * <code>
     *    /rest/dataSources/updateZ3950IdSequence?
     *    id=z3950IdSeqTest&
     *    description=newtest Z39.50 with id sequence&
     *    nameCode=222200129&
     *    name=NEWZ3950-IdSeq&
     *    exportPath=D:/Projectos/repoxdata_new21&
     *    schema=info:lc/xmlns/marcxchange-v1.xsd&
     *    namespace=info:lc/xmlns/marcxchange-v1&
     *    address=aleph.lbfl.li&port=9909&
     *    database=LLB_IDS&
     *    user=&
     *    password=&
     *    recordSyntax=usmarc&
     *    charset=UTF-8&
     *    maximumId=300&
     *    recordIdPolicy=IdGenerated&
     *    idXpath=&
     *    namespacePrefix=&
     *    namespaceUri=
     *  </code>
     * 
     * @param ds
     *            a Source object
     * @param prov
     *            provider to the source
     * @return a Source object
     * @throws RepoxException
     */
    Source updateDatasourceZ3950IdSequence(Source ds) throws RepoxException;

    /**
     * Updates a Ftp DataSource. It accesses the following REST Interface:
     * 
     * <code>
     *     /rest/dataSources/updateFtp?
     *     id=ftpTest&
     *     description=newtest FTP data source&
     *     nameCode=555555500124&
     *     name=FTP&
     *     exportPath=D:/Projectos/repoxdata_new21212121&
     *     schema=http://www.europeana.eu/schemas/ese/ESE-V3.3.xsd&
     *     namespace=http://www.europeana.eu/schemas/ese/&
     *     metadataFormat=ese&
     *     isoFormat=&
     *     charset=&
     *     recordIdPolicy=IdGenerated&
     *     idXpath=&
     *     namespacePrefix=&
     *     namespaceUri=&
     *     recordXPath=record&
     *     server=bd1.inesc-id.pt&
     *     user=ftp&
     *     password=pmath2010.&
     *     ftpPath=/Lizbeth
     *  </code>
     * 
     * @param ds
     *            a Source object
     * @param prov
     *            provider to the source
     * @return a Source object
     * @throws RepoxException
     */
    Source updateDatasourceFtp(Source ds) throws RepoxException;

    /**
     * Updates an Http DataSource. It accesses the following REST Interface:
     * 
     * <code>
     *     /rest/dataSources/updateHttp?
     *     id=ftpTest&
     *     description=newtest FTP data source&
     *     nameCode=555555500124&
     *     name=FTP&
     *     exportPath=D:/Projectos/repoxdata_new21212121&
     *     schema=http://www.europeana.eu/schemas/ese/ESE-V3.3.xsd&
     *     namespace=http://www.europeana.eu/schemas/ese/&
     *     metadataFormat=ese&
     *     isoFormat=&
     *     charset=&
     *     recordIdPolicy=IdGenerated&
     *     idXpath=&
     *     namespacePrefix=&
     *     namespaceUri=&
     *     recordXPath=record&
     *     server=bd1.inesc-id.pt&
     *     user=ftp&
     *     password=pmath2010.&
     *     ftpPath=/Lizbeth
     *  </code>
     * 
     * @param ds
     *            a Source object
     * @param prov
     *            provider to the source
     * @return a Source object
     * @throws RepoxException
     */
    Source updateDatasourceHttp(Source ds) throws RepoxException;

    /**
     * Updates a Folder DataSource. It accesses the following REST Interface:
     * 
     * <code>
     *     /rest/dataSources/updateFolder?
     *     id=folderTest&
     *     description=test%20Folder%20data%20source3333333&
     *     nameCode=4444444444400124&
     *     name=Folder&
     *     exportPath=D:/Projectos/repoxdata_new&
     *     schema=info:lc/xmlns/marcxchange-v1.xsd&
     *     namespace=info:lc/xmlns/marcxchange-v1&
     *     metadataFormat=ISO2709&
     *     isoFormat=pt.utl.ist.marc.iso2709.IteratorIso2709&
     *     charset=UTF-8&
     *     recordIdPolicy=IdExtracted&
     *     idXpath=/mx:record/mx:controlfield[@tag=%22001%22]&
     *     namespacePrefix=mx&
     *     namespaceUri=info:lc/xmlns/marcxchange-v1&
     *     recordXPath=&
     *     folder=C:\folderNew
     *  </code>
     * 
     * @param ds
     *            a Source object
     * @param prov
     *            provider to the source
     * @return a Source object
     * @throws RepoxException
     */
    Source updateDatasourceFolder(Source ds) throws RepoxException;

    /**
     * Deletes a DataSource of any Type. It accesses the following REST Interface:
     * 
     * <code>
     *    /rest/dataSources/delete?id=ftpTest
     *  </code>
     * 
     * @param dsID
     *            a Source object
     * @return successfull?
     * @throws RepoxException
     */
    String deleteDatasource(String dsID) throws RepoxException;
}
