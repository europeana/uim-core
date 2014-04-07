/* ScriptForDebuggingEdmValidation.java - created on 30/09/2013, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.uim.check.edm;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.util.Properties;

import org.junit.Assert;
import org.theeuropeanlibrary.commons.data.TelRepositoryService;
import org.theeuropeanlibrary.commons.export.edm.EdmXmlSerializer;
import org.theeuropeanlibrary.commons.export.edm.ObjectModelToEdmConverter;
import org.theeuropeanlibrary.commons.export.edm.model.ResourceMap;
import org.theeuropeanlibrary.uim.check.edm.AbstractEdmIngestionPlugin.ContextRunningData;
import org.theeuropeanlibrary.util.XmlUtil;

import eu.europeana.uim.common.TKey;
import eu.europeana.uim.logging.LoggingEngine;
import eu.europeana.uim.logging.LoggingEngineAdapter;
import eu.europeana.uim.orchestration.ActiveExecution;
import eu.europeana.uim.store.MetaDataRecord;
import eu.europeana.uim.store.bean.CollectionBean;
import eu.europeana.uim.store.bean.ExecutionBean;
import eu.europeana.uim.store.bean.MetaDataRecordBean;

/**
 * 
 * 
 * @author Nuno Freire (nfreire@gmail.com)
 * @since 30/09/2013
 */
public class ScriptForDebuggingEdmValidation {
    /**
     * @param args
     * @throws Exception
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static void main(String[] args) throws Exception {
        TelRepositoryService repositoryService = TelRepositoryService.newInstanceOfProductionRepository();
        MetaDataRecordBean<Long> mdr = repositoryService.getMetadataRecord(3000031463925L);
        ObjectModelToEdmConverter edmConverter = new ObjectModelToEdmConverter();
        ResourceMap convertedEdm = edmConverter.convert(mdr);
        EdmXmlSerializer edmSerializer = new EdmXmlSerializer();
        String edmXmlString = XmlUtil.writeDomToString(edmSerializer.toDom(convertedEdm));
        System.out.println(XmlUtil.prettyXml(edmXmlString));

        EdmCheckIngestionPlugin plugin = new EdmCheckIngestionPlugin();
        plugin.initialize();

        CollectionBean collection = new CollectionBean();
        collection.setName("test");
        ExecutionBean execution = new ExecutionBean(1L);
        execution.setDataSet(collection);

        Properties properties = new Properties();

        LoggingEngine logging = LoggingEngineAdapter.LONG;

        ActiveExecution<MetaDataRecord<Long>, Long> context = mock(ActiveExecution.class);
        when(context.getProperties()).thenReturn(properties);
        when(context.getExecution()).thenReturn(execution);
        when(context.getLoggingEngine()).thenReturn(logging);
        ContextRunningData data = new AbstractEdmIngestionPlugin.ContextRunningData(new File(
                "src/test/resources/xsd/EDM_tel.xsd"));
        data.maxErrors = 10;

        when(context.getValue((TKey<?, ContextRunningData>)any())).thenReturn(data);
        when(context.getFileResource((String)any())).thenReturn(
                new File("src/test/resources/xsd/EDM_tel.xsd"));

        plugin.initialize(context);

        plugin.process(mdr, context);

        plugin.completed(context);

        System.out.println("Submited: " + data.submitted);
        System.out.println("Ignored: " + data.ignored);
        System.out.println("Invalid: " + data.report.getInvalidRecords());
        System.out.println("Valid: " + data.report.getValidRecords());

        for (String s : data.report.getErrorMessagesCounts().keySet()) {
            System.out.println(s);
        }

        // TODO: assert validation errors
        Assert.assertTrue(data.submitted > 0);

        plugin.shutdown();
    }
}
