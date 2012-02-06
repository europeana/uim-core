package eu.europeana.uim.repox.rest.utils;

import org.theeuropeanlibrary.model.common.qualifier.Country;

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
        String countryCode = provider.getValue(StandardControlledVocabulary.COUNTRY);
        String countryName = null;
        if (countryCode == null) {
            countryCode = "eu";
            countryName = "European Union";
        } else {
            countryCode = countryCode.toLowerCase();
            if (countryCode.equals("xxx")) {
                countryCode = "eu";
                countryName = "European Union";
            } else {
                Country country = Country.lookupCountry(countryCode, false);
                if (country != null) {
                    countryCode = country.getIso2();
                    countryName = country.getName();
                } else {
                    countryName = countryCode;
                }
            }
        }

        Aggregator aggr = DummyXmlObjectCreator.createAggregator(countryName);
        aggr.setName(countryName);
        aggr.setNameCode(countryCode);

        return aggr;
    }

    @Override
    public eu.europeana.uim.repox.rest.client.xml.Provider createProvider(Provider<?> provider) {
        eu.europeana.uim.repox.rest.client.xml.Provider jaxbProv = DummyXmlObjectCreator.createProvider(provider.getName());

        jaxbProv.setName(provider.getName());
        jaxbProv.setNameCode(provider.getMnemonic());

        String countrystr = provider.getValue(StandardControlledVocabulary.COUNTRY);
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
