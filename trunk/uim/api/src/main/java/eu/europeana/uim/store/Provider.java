package eu.europeana.uim.store;

import java.util.List;


/*
aggregator // ref to Aggregator obj
name_code       // ingestion internal shorthand for Aggregator
name            // official name of Aggreagator (for nice reports etc)
home_page       // homepage of Aggreagator (for nice reports etc)
country         // Country for Provider
content_type    // enum PROVT_MUSEUM, PROVT_ARCHIVE, PROVT_LIBRARY, PROVT_AUDIO_VIS_ARCH, PROVT_AGGREGATOR

 */
public interface Provider  extends DataSet {

	public List<Provider> getRelatedOut();
	public List<Provider> getRelatedIn();

	public String getMnemonic();
	public void setMnemonic(String mnemonic);

	public String getName();
	public void setName(String name);

	public void setAggregator(boolean aggregator);
	public boolean isAggregator();

	public String getOaiBaseUrl();
	public void setOaiBaseUrl(String baseUrl);

	public String getOaiMetadataPrefix();
	public void setOaiMetadataPrefix(String prefix);


}
