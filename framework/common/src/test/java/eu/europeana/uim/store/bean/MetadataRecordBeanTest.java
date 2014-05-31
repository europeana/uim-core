/* MetadataRecordBeanTest.java - created on Aug 13, 2012, Copyright (c) 2012 The European Library, all rights reserved */
package eu.europeana.uim.store.bean;

import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import eu.europeana.uim.common.TKey;
import eu.europeana.uim.store.MetaDataRecord.QualifiedValue;
import eu.europeana.uim.store.Provider;
import eu.europeana.uim.store.Request;

/**
 * Tests basic operations on implementation {@link RequestBean} for
 * {@link Request}.
 *
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Aug 13, 2012
 */
public class MetadataRecordBeanTest {

    private static final TKey<MetadataRecordBeanTest, String> PERSON = TKey.register(
            MetadataRecordBeanTest.class,
            "person", String.class);

    private enum PersonQualifier {

        AUTHOR, PUBLISHER
    }

    private static final TKey<MetadataRecordBeanTest, String> YEAR = TKey.register(
            MetadataRecordBeanTest.class,
            "year", String.class);

    private enum YearQualifier {

        BIRTH, DEATH
    }

    /**
     * Tests creating of bean and general getter and setter methods on it (id,
     * collection)
     */
    @Test
    public void testMetadataRecordCreation() {
        Provider<Long> provider = new ProviderBean<>(1L);
        provider.setMnemonic("pmnemonic");
        provider.setName("pname");
        provider.setOaiBaseUrl("Pbase");
        provider.setOaiMetadataPrefix("Pmeta");

        CollectionBean<Long> collection = new CollectionBean<>();
        collection.setProvider(provider);
        collection.setId(2L);
        collection.setLanguage("a1");
        collection.setMnemonic("mnemonic");
        collection.setName("name");
        collection.setOaiSet("cset");

        MetaDataRecordBean<Long> bean = new MetaDataRecordBean<>(1L, collection);

        Assert.assertTrue(1 == bean.getId());
        Assert.assertEquals(collection, bean.getCollection());
    }

    /**
     * Tests putting values onto the metadata record and retrieving them.
     */
    @Test
    public void testMetadataRecordValues() {
        Provider<Long> provider = new ProviderBean<>(1L);
        provider.setMnemonic("pmnemonic");
        provider.setName("pname");
        provider.setOaiBaseUrl("Pbase");
        provider.setOaiMetadataPrefix("Pmeta");

        CollectionBean<Long> collection = new CollectionBean<>();
        collection.setProvider(provider);
        collection.setId(2L);
        collection.setLanguage("a1");
        collection.setMnemonic("mnemonic");
        collection.setName("name");
        collection.setOaiSet("cset");

        MetaDataRecordBean<Long> bean = new MetaDataRecordBean<>(1L, collection);

        bean.addValue(PERSON, "Markus Muhr", PersonQualifier.AUTHOR);

        List<String> values = bean.getValues(PERSON);
        Assert.assertEquals(1, values.size());

        List<QualifiedValue<String>> qualifiedValues = bean.getQualifiedValues(PERSON);
        Assert.assertEquals(1, qualifiedValues.size());
        Assert.assertEquals(PersonQualifier.AUTHOR,
                qualifiedValues.get(0).getQualifier(PersonQualifier.class));

        Assert.assertEquals(values.get(0), qualifiedValues.get(0).getValue());

        String value = bean.getFirstValue(PERSON);
        QualifiedValue<String> qvalue = bean.getFirstQualifiedValue(PERSON, PersonQualifier.AUTHOR);
        Assert.assertEquals(value, qvalue.getValue());

        qvalue = bean.getFirstQualifiedValue(PERSON, PersonQualifier.PUBLISHER);
        Assert.assertNull(qvalue);

        List<QualifiedValue<String>> rems = bean.deleteValues(PERSON, PersonQualifier.PUBLISHER);
        Assert.assertEquals(0, rems.size());

        rems = bean.deleteValues(PERSON, PersonQualifier.AUTHOR);
        Assert.assertEquals(1, rems.size());

        qvalue = bean.getFirstQualifiedValue(PERSON, PersonQualifier.AUTHOR);
        Assert.assertNull(qvalue);
    }

    /**
     * Tests putting values onto the metadata record and retrieving them.
     */
    @Test
    public void testMetadataRecordRelations() {
        Provider<Long> provider = new ProviderBean<>(1L);
        provider.setMnemonic("pmnemonic");
        provider.setName("pname");
        provider.setOaiBaseUrl("Pbase");
        provider.setOaiMetadataPrefix("Pmeta");

        CollectionBean<Long> collection = new CollectionBean<>();
        collection.setProvider(provider);
        collection.setId(2L);
        collection.setLanguage("a1");
        collection.setMnemonic("mnemonic");
        collection.setName("name");
        collection.setOaiSet("cset");

        MetaDataRecordBean<Long> bean = new MetaDataRecordBean<>(1L, collection);

        bean.addValue(PERSON, "Markus Muhr", PersonQualifier.AUTHOR);
        bean.addValue(YEAR, "1981");
        bean.addValue(YEAR, "2060");

        QualifiedValue<String> person = bean.getFirstQualifiedValue(PERSON);
        List<QualifiedValue<String>> years = bean.getQualifiedValues(YEAR);

        bean.addRelation(person, years.get(0), YearQualifier.BIRTH);
        bean.addRelation(person, years.get(1), YearQualifier.DEATH);

        Set<QualifiedValue<String>> targets = bean.getTargetQualifiedValues(person, YEAR);
        Assert.assertEquals(2, targets.size());

        targets = bean.getTargetQualifiedValues(person, YEAR, YearQualifier.BIRTH);
        Assert.assertEquals(1, targets.size());

        Set<QualifiedValue<String>> sources = bean.getSourceQualifiedValues(years.get(0), PERSON);
        Assert.assertEquals(1, sources.size());

        bean.deleteRelations(years.get(1));
        targets = bean.getTargetQualifiedValues(person, YEAR);
        Assert.assertEquals(1, targets.size());

        bean.deleteValues(PERSON);
        targets = bean.getTargetQualifiedValues(person, YEAR);
        Assert.assertEquals(0, targets.size());
    }
}
