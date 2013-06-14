/* ConverterTest.java - created on Mar 22, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.model.tel;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.theeuropeanlibrary.model.common.Link;
import org.theeuropeanlibrary.model.common.Title;
import org.theeuropeanlibrary.model.common.qualifier.Language;
import org.theeuropeanlibrary.model.common.qualifier.LinkTarget;
import org.theeuropeanlibrary.model.common.qualifier.TitleType;

import eu.europeana.uim.store.MetaDataRecord;
import eu.europeana.uim.store.MetaDataRecord.QualifiedValue;
import eu.europeana.uim.store.bean.MetaDataRecordBean;

/**
 * Tests the conversion of a compreensive metadata record
 * 
 * @author Nuno Freire <nfreire@gmail.com>
 * @since Apr 18, 2011
 */
public class ObjectModelRegistryTest {
    /**
     * 
     */
    @Test
    public void testSimpleMetadata() {
        MetaDataRecord<Long> record = new MetaDataRecordBean<Long>(1L, null);

        Title title = new Title("Clavicorde lie....");
        record.addValue(ObjectModelRegistry.TITLE, title, TitleType.MAIN, Language.LAT);

        record.addValue(ObjectModelRegistry.LINK, new Link("http://ssome.reousec.eu"),
                LinkTarget.THUMBNAIL);

        record.addValue(ObjectModelRegistry.LINK, new Link("http://cc0/rights"), LinkTarget.RIGHTS);

        List<QualifiedValue<Link>> list = record.getQualifiedValues(ObjectModelRegistry.LINK);
        assertEquals(2, list.size());

        list = record.getQualifiedValues(ObjectModelRegistry.LINK, LinkTarget.RIGHTS);
        assertEquals(1, list.size());
    }
}
