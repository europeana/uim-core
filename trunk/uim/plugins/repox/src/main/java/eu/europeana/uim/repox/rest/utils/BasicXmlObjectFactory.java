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
        Aggregator aggr = new Aggregator();
        aggr.setName(aggrName);
        aggr.setNameCode(aggrName);

        return aggr;
    }

    @Override
    public eu.europeana.uim.repox.rest.client.xml.Provider createProvider(Provider<?> provider) {
        eu.europeana.uim.repox.rest.client.xml.Provider jaxbProv = new eu.europeana.uim.repox.rest.client.xml.Provider();

        jaxbProv.setName(provider.getName());
        jaxbProv.setNameCode(provider.getMnemonic());

        String countrystr = provider.getValue(StandardControlledVocabulary.COUNTRY).toLowerCase();
        if (countrystr == null) {
            countrystr = "eu";
        }
        jaxbProv.setCountry(countrystr);

        return jaxbProv;
    }

    @Override
    public Source createDataSource(Collection<?> collection) {
        Source ds = new Source();
        String id = (collection.getMnemonic());
        ds.setId(id);
        ds.setNameCode(collection.getMnemonic());
        ds.setName(collection.getName());
        ds.setExportPath("");
        return ds;
    }
}
