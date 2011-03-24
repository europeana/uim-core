package eu.europeana.uim.store.memory;

import eu.europeana.uim.MetaDataRecord;
import eu.europeana.uim.store.Request;
import eu.europeana.uim.store.Uri;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
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
 *
 * User: jaclu
 * Date: 21-03-11
 * Time: 12:34
 * To change this template use File | Settings | File Templates.
 */


public class MemoryUri extends AbstractMemoryEntity implements Uri {
    private String url; // the url in question
    private String uriSource; // the webserver part of the url, we use this to limit the hits on webservers

    private Uri.ItemType itemType;
    private Uri.Status status = Uri.Status.CREATED;
    private Uri.ErrCode errCode = Uri.ErrCode.NO_ERROR;
    private String errMsg = ""; // Only set if errCode != NO_ERROR, contains details of failure

    private Date timeCreated = new Date();
    private Date timeLastCheck = new Date(0);  // use 1970 as unchecked default

    // TODO check witch pointer lists we should use for performance in this environment
    private List<Request> requests = new ArrayList<Request>(); // all requests using this url for the moment
    private List<MetaDataRecord> mdrs = new ArrayList<MetaDataRecord>(); // all mdr using this url


    // Only for thumbnails
    private String urlHash = ""; // this is the key used by europeana portal to find the image, alseo the file name on img_servers
    private String contentHash = ""; // to make sure the item on filesystem is what we belive

    private String mimeType = "";
    private String fileType = "";
    private Integer orgW = -1;
    private Integer orgH = -1;



    public MemoryUri() {

    }

    public MemoryUri(long id, String url, ItemType itemType){
        super(id);
        this.url = url;
        this.itemType = itemType;
    }

    public MemoryUri(long id, String url, ItemType itemType, Request request, MetaDataRecord mdr){
        super(id);
        this.url = url;
        this.itemType = itemType;
        this.addRequest(request);
        this.addMetaDataRecord(mdr);
    }

    public void setStatus(Uri.Status newStatus) {
        status = newStatus;
        if (newStatus == Uri.Status.COMPLETED) {
            timeLastCheck = new Date();
        }
    }

    public void SetError(Uri.ErrCode newErrCode, String newErrMsg) {
        errCode = newErrCode;
        if (errCode != Uri.ErrCode.NO_ERROR) {
            errMsg = newErrMsg;
            setStatus(Uri.Status.FAILED);
        } else {
            clearError();
        }
    }

    public void clearError() {
        errCode = Uri.ErrCode.NO_ERROR;
        errMsg = "";
        setStatus(Uri.Status.CREATED); // Indicate that this record should be picked up again for processing as if it was new
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


    public String getUrl() {
        return url;
    }

    public String getUriSource() {
        return uriSource;
    }

    public void setUriSource(String uriSource) {
        this.uriSource = uriSource;
    }

    public ItemType getItemType() {
        return itemType;
    }

    public Status getStatus() {
        return status;
    }

    public ErrCode getErrCode() {
        return errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public Date getTimeCreated() {
        return timeCreated;
    }

    public Date getTimeLastCheck() {
        return timeLastCheck;
    }

    public List<Request> getRequests() {
        return requests;
    }

    public void addRequest(Request request) {
        this.requests.add(request);
    }

    public void removeRequest(Request request) {
        this.requests.remove(request);
    }

    public List<MetaDataRecord> getMetaDataRecord() {
        return mdrs;
    }

    public void addMetaDataRecord(MetaDataRecord mdr) {
        this.mdrs.add(mdr);
    }

    public void removeMetaDtataRecord(MetaDataRecord mdr) {
        this.mdrs.remove(mdr);
    }

    public String getUrlHash() {
        return urlHash;
    }

    public void setUrlHash(String urlHash) {
        this.urlHash = urlHash;
    }

    public String getContentHash() {
        return contentHash;
    }

    public void setContentHash(String contentHash) {
        this.contentHash = contentHash;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

}
