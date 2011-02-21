package eu.europeana.uim.store;



/*
    provider    // ref to Provider obj
    name_code   // ingestion internal shorthand for Aggregator
    name        // official name of Aggreagator (for nice reports etc)
    home_page   // homepage of Aggreagator (for nice reports etc)
    language    // Primary language for collection (used as default for all fields if not given)

 */
public interface Collection  extends DataSet {
	
	public Provider getProvider();

	public String getMnemonic();
	public void setMnemonic(String mnemonic);

	public String getName();
	public void setName(String name);
	
	public String getLanguage();
	public void setLanguage(String name);
	
	public String getOaiBaseUrl();
	public void setOaiBaseUrl(String baseUrl);

	public String getOaiSet();
	public void setOaiSet(String set);

	public String getOaiMetadataPrefix();
	public void setOaiMetadataPrefix(String prefix);


}
