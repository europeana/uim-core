package eu.europeana.uim.repox.rest.utils;

import eu.europeana.uim.repox.rest.client.xml.Aggregator;
import eu.europeana.uim.repox.rest.client.xml.Source;
import eu.europeana.uim.store.Collection;
import eu.europeana.uim.store.Provider;
import eu.europeana.uim.store.StandardControlledVocabulary;

/**
 * Utility functions to create xml generated objects.
 * 
 * @author Georgios Markakis
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Jan 24, 2012
 */
public class BasicXmlObjectFactory implements XmlObjectFactory {
    @Override
    public Aggregator createAggregator(Provider<?> provider) {
        String countrystr = provider.getValue(StandardControlledVocabulary.COUNTRY).toLowerCase();
        if (countrystr == null) {
            countrystr = "eu";
        }

        String aggrName = countrystr + "-aggregator";

        Aggregator aggr = DummyXmlObjectCreator.createAggregator(aggrName);
        aggr.setName(aggrName);
        aggr.setNameCode(aggrName);

        return aggr;
    }

    @Override
    public eu.europeana.uim.repox.rest.client.xml.Provider createProvider(Provider<?> provider) {
        eu.europeana.uim.repox.rest.client.xml.Provider jaxbProv = DummyXmlObjectCreator.createProvider(provider.getName());

        jaxbProv.setName(provider.getName());
        jaxbProv.setNameCode(provider.getMnemonic());

        String countrystr = provider.getValue(StandardControlledVocabulary.COUNTRY);
        if (countrystr == null) {
            countrystr = "eu";
        } else {
            countrystr = countrystr.toLowerCase();
        }
        jaxbProv.setCountry(countrystr);

        return jaxbProv;
    }

    @Override
    public Source createDataSource(Collection<?> collection) {
        Source ds = DummyXmlObjectCreator.createOAIDataSource(collection.getMnemonic());
        ds.setId(collection.getMnemonic());
        ds.setNameCode(collection.getMnemonic());
        ds.setName(collection.getName());
        return ds;
    }
}
