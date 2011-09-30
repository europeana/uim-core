/* GlobalModelRegistryTest.java - created on Jun 19, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.model.edm;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.theeuropeanlibrary.model.Link;
import org.theeuropeanlibrary.model.Title;
import org.theeuropeanlibrary.qualifier.Language;
import org.theeuropeanlibrary.qualifier.LinkTarget;
import org.theeuropeanlibrary.qualifier.TitleType;

import eu.europeana.uim.store.MetaDataRecord;
import eu.europeana.uim.store.MetaDataRecord.QualifiedValue;
import eu.europeana.uim.store.bean.MetaDataRecordBean;

/**
 * 
 */
public class ProvidedChoModelRegistryTest {

    /**
     */
    @Test
    public void testSimpleConcepts() {
        MetaDataRecord<Long> record = new MetaDataRecordBean<Long>(1L, null);

        Title title = new Title("Clavicorde lie....");
        record.addValue(ProvidedChoModelRegistry.TITLE, title, TitleType.MAIN, Language.LAT);

        record.addValue(WebResourceModelRegistry.LINK, new Link("http://ssome.reousec.eu"),
                LinkTarget.THUMBNAIL);
        
        record.addValue(WebResourceModelRegistry.LINK, new Link("http://cc0/rights"),
                LinkTarget.RIGHTS);
       
        List<QualifiedValue<Link>> list = record.getQualifiedValues(WebResourceModelRegistry.LINK);
        assertEquals(2, list.size());

        list = record.getQualifiedValues(WebResourceModelRegistry.LINK, LinkTarget.RIGHTS);
        assertEquals(1, list.size());
    }

}
