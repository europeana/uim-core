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
            countryCode = "EU";
            countryName = "Europe";
        } else {
            countryCode = countryCode.toLowerCase();
            if (countryCode.equals("xxx")) {
                countryCode = "EU";
                countryName = "Europe";
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
        eu.europeana.uim.repox.rest.client.xml.Provider jaxbProvider = DummyXmlObjectCreator.createProvider(provider.getMnemonic());

        jaxbProvider.setName(provider.getName());
        jaxbProvider.setNameCode(provider.getMnemonic());

        String countrystr = provider.getValue(StandardControlledVocabulary.COUNTRY);
        if (countrystr != null) {
            Country country = Country.lookupCountry(countrystr.toLowerCase(), false);
            if (country != null && !country.getIso2().toLowerCase().equals(jaxbProvider.getCountry())) {
                jaxbProvider.setCountry(country.getIso2().toLowerCase());
            }
        }

        return jaxbProvider;
    }

    @Override
    public boolean updateProvider(Provider<?> provider,
            eu.europeana.uim.repox.rest.client.xml.Provider jaxbProvider) {
        boolean changed = false;

        if (!provider.getMnemonic().equals(jaxbProvider.getNameCode())) {
            jaxbProvider.setNameCode(provider.getMnemonic());
            changed = true;
        }

        if (provider.getName() != null && !provider.getName().equals(jaxbProvider.getName())) {
            jaxbProvider.setName(provider.getName());
            changed = true;
        }

        String countrystr = provider.getValue(StandardControlledVocabulary.COUNTRY);
        if (countrystr != null) {
            Country country = Country.lookupCountry(countrystr.toLowerCase(), false);
            if (country != null && !country.getIso2().toLowerCase().equals(jaxbProvider.getCountry())) {
                jaxbProvider.setCountry(country.getIso2().toLowerCase());
                changed = true;
            }
        }

        return changed;
    }

    @Override
    public Source createDataSource(Collection<?> collection) {
        Source jaxbSource = DummyXmlObjectCreator.createOAIDataSource(collection.getMnemonic());
        jaxbSource.setId(collection.getMnemonic());
        jaxbSource.setNameCode(collection.getMnemonic());
        jaxbSource.setName(collection.getName());
        return jaxbSource;
    }

    @Override
    public boolean updateDataSource(Collection<?> collection, Source jaxbSource) {
        boolean changed = false;

        if (!collection.getMnemonic().equals(jaxbSource.getId())) {
            jaxbSource.setId(collection.getMnemonic());
            changed = true;
        }

        if (!collection.getMnemonic().equals(jaxbSource.getNameCode())) {
            jaxbSource.setNameCode(collection.getMnemonic());
            changed = true;
        }

        if (collection.getName() != null && !collection.getName().equals(jaxbSource.getName())) {
            jaxbSource.setName(collection.getName());
            changed = true;
        }

        return changed;
    }
}
