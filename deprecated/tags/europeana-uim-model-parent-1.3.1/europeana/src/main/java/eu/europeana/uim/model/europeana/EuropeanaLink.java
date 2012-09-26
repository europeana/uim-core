/*
 * Copyright 2007-2012 The Europeana Foundation
 *
 *  Licenced under the EUPL, Version 1.1 (the "Licence") and subsequent versions as approved
 *  by the European Commission;
 *  You may not use this work except in compliance with the Licence.
 * 
 *  You may obtain a copy of the Licence at:
 *  http://joinup.ec.europa.eu/software/page/eupl
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under
 *  the Licence is distributed on an "AS IS" basis, without warranties or conditions of
 *  any kind, either express or implied.
 *  See the Licence for the specific language governing permissions and limitations under
 *  the Licence.
 */
package eu.europeana.uim.model.europeana;

import java.util.Date;

import org.theeuropeanlibrary.model.common.FieldId;
import org.theeuropeanlibrary.model.common.qualifier.LinkStatus;

/**
 *
 * @author Georgios Markakis <gwarkx@hotmail.com>
 * @since 8 May 2012
 */
public class EuropeanaLink {

	/**
	 * uniform resource location
	 */
	@FieldId(1)
	private String url;

	@FieldId(2)
	private Date lastChecked;
	
	@FieldId(3)
	private LinkStatus linkStatus;
	
    @FieldId(4)
    private String cachedPath;
    
    @FieldId(5)
    private String anchorKey;
	
    @FieldId(6)
    private String mongoId;
    
    @FieldId(7)
    private boolean cacheable;

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the lastChecked
	 */
	public Date getLastChecked() {
		return lastChecked;
	}

	/**
	 * @param lastChecked the lastChecked to set
	 */
	public void setLastChecked(Date lastChecked) {
		this.lastChecked = lastChecked;
	}

	/**
	 * @return the linkStatus
	 */
	public LinkStatus getLinkStatus() {
		return linkStatus;
	}

	/**
	 * @param linkStatus the linkStatus to set
	 */
	public void setLinkStatus(LinkStatus linkStatus) {
		this.linkStatus = linkStatus;
	}

	/**
	 * @return the cachedPath
	 */
	public String getCachedPath() {
		return cachedPath;
	}

	/**
	 * @param cachedPath the cachedPath to set
	 */
	public void setCachedPath(String cachedPath) {
		this.cachedPath = cachedPath;
	}

	/**
	 * @return the anchorKey
	 */
	public String getAnchorKey() {
		return anchorKey;
	}

	/**
	 * @param anchorKey the anchorKey to set
	 */
	public void setAnchorKey(String anchorKey) {
		this.anchorKey = anchorKey;
	}

	/**
	 * @return the mongoId
	 */
	public String getMongoId() {
		return mongoId;
	}

	/**
	 * @param mongoId the mongoId to set
	 */
	public void setMongoId(String mongoId) {
		this.mongoId = mongoId;
	}

	/**
	 * @return the cacheable
	 */
	public boolean isCacheable() {
		return cacheable;
	}

	/**
	 * @param cacheable the cacheable to set
	 */
	public void setCacheable(boolean cacheable) {
		this.cacheable = cacheable;
	}
}
