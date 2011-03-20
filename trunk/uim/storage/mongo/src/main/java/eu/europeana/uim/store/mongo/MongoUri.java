/*
 * Copyright 2007 EDL FOUNDATION
 *
 * Licensed under the EUPL, Version 1.0 or - as soon they
 * will be approved by the European Commission - subsequent
 * versions of the EUPL (the "Licence");
 * you may not use this work except in compliance with the
 * Licence.
 * You may obtain a copy of the Licence at:
 *
 * http://ec.europa.eu/idabc/eupl
 *
 * Unless required by applicable law or agreed to in
 * writing, software distributed under the Licence is
 * distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied.
 * See the Licence for the specific language governing
 * permissions and limitations under the Licence.
 */
package eu.europeana.uim.store.mongo;

import eu.europeana.uim.MetaDataRecord;
import eu.europeana.uim.store.Request;
import eu.europeana.uim.store.Uri;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Storage for uri item
 *
 * @author Jacob Lundqvist <jacob.lundqvist@kb.nl>
 */
public class MongoUri extends AbstractMongoEntity implements Uri {


    private String url; // the url in question
    private String uri_source; // the webserver part of the url, we use this to limit the hits on webservers

    private ItemType itemType;
    private Status status = Status.CREATED;
    private ErrCode errCode = ErrCode.NO_ERROR;
    private String errMsg = ""; // Only set if errCode != NO_ERROR, contains details of failure

    private Date timeCreated = new Date();
    private Date timeLastCheck = new Date(0);  // use 1970 as unchecked default

    // TODO: check witch pointer lists we should use for performance in this environment
    private List<Request> requests = new ArrayList<Request>(); // all requests using this url for the moment
    private List<MetaDataRecord> mdrs = new ArrayList<MetaDataRecord>(); // all mdr using this url


    // Only for thumbnails
    private String urlHash = ""; // this is the key used by europeana portal to find the image, alseo the file name on img_servers
    private String contentHash = ""; // to make sure the item on filesystem is what we belive

    private String mimeType = "";
    private String fileType = "";
    private Integer orgW = -1;
    private Integer orgH = -1;

    public MongoUri(long id, String url, ItemType itemType){
        super(id);
        this.url = url;
        this.itemType = itemType;
    }

    public Status getStatus() {
        return status;
    }
    void setStatus(Status newStatus) {
        status = newStatus;
        if (newStatus == Status.COMPLETED) {
            timeLastCheck = new Date();
        }
    }

    public ErrCode getErrCode() {
        return errCode;
    }
    void SetError(ErrCode newErrCode, String newErrMsg) {
        errCode = newErrCode;
        if (errCode != ErrCode.NO_ERROR) {
            errMsg = newErrMsg;
            setStatus(Status.FAILED);
        } else {
            clearError();
        }
    }
    void clearError() {
        errCode = ErrCode.NO_ERROR;
        errMsg = "";
        setStatus(Status.CREATED); // Indicate that this record should be picked up again for processing as if it was new
    }

    public Date getCreated() {
        return timeCreated;
    }
    public Date getTimeLastCheck() {
        return timeLastCheck;
    }
    public void setTimeLastCheck(Date date) {
        timeLastCheck = date;
    }

    public List<Request> getRequests() {
        return requests;
    }
    public boolean isPartOfRequest(Request request) {
        return requests.contains(request);
    }
    public void addRequest(Request request) {
        requests.add(request);
    }
    public void removeRequest(Request request) {
        requests.remove(request);
    }

    public String getUrlHash() {
        return urlHash;
    }
    public void setUrlHash(String newUrlHash) {
        urlHash = newUrlHash;
    }
    public String getContentHash() {
        return contentHash;
    }
    public void setContentHash(String newContentHash) {
        urlHash = newContentHash;
    }

    public String getMimeType() {
        return mimeType;
    }
    public void setMimeType(String newMimeType) {
        mimeType = newMimeType;
    }

    public Integer[] getImgDimentions() {
        Integer[] dimensions = {orgH, orgW};
        return dimensions;
    }
    public void setImgDimentions(Integer width, Integer height) {
        orgW = width;
        orgH = height;
    }

    public String getIdentifier() {
        return "Uri:" + url;
    }

}
