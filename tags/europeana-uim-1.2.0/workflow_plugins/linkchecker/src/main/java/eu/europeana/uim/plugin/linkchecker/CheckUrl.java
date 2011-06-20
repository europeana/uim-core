package eu.europeana.uim.plugin.linkchecker;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import eu.europeana.uim.plugin.linkchecker.errorcodes.LinkStatus;
import eu.europeana.uim.plugin.linkchecker.errorcodes.ThumbnailStatus;
import eu.europeana.uim.plugin.linkchecker.exceptions.FileStorageException;
import eu.europeana.uim.plugin.linkchecker.exceptions.HttpAccessException;


public class CheckUrl {
    protected String uri;
    private Integer redirectDepth;
    protected LinkStatus state;
    private String ErrorMsg;
    protected ByteArrayOutputStream orgFileConent = null; // storage for downloaded item, not used when just linkchecking


    /**
     * @param uri
     */
    public CheckUrl(String uri) {
        this.uri = uri;
        initiateParams(5);
    }

    /**
     * @param uri
     * @param redirectDepth
     */
    public CheckUrl(String uri, Integer redirectDepth) {
        this.uri = uri;
        initiateParams(redirectDepth);
    }


    

    public boolean isResponding() throws HttpAccessException, FileStorageException {
        return doConnection(this.redirectDepth, false);
    }
    
    
    public boolean isResponding(boolean saveItem) throws HttpAccessException, FileStorageException {
        return doConnection(this.redirectDepth, saveItem);
    }
    
    
    public boolean isResponding(Integer redirectDepth) throws HttpAccessException, FileStorageException {
        return doConnection(redirectDepth, false);
    }



    private boolean doConnection(Integer redirectDepth, boolean saveItem) throws HttpAccessException, FileStorageException{
        HttpURLConnection urlConnection;
        URL url;
        BufferedInputStream in;

        //What exactly are you trying to do here?
        if (redirectDepth < 0) {
        	throw new HttpAccessException(LinkStatus.REDIRECT_DEPTH_EXCEEDED);
        }

        try {
            url = new URL(uri);
        } catch (MalformedURLException e) {
        	throw new HttpAccessException(LinkStatus.BAD_URL);
        }

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
        	throw new HttpAccessException(LinkStatus.FAILED_TO_OPEN_CONNECTION);
        }
        //urlConnection.setRequestMethod("HEAD");
        
        //This should not be hardwired
        urlConnection.setConnectTimeout(5000); /* timeout after 5s if can't connect */

        try {
            urlConnection.connect();
        } catch (IOException e) {
        	throw new HttpAccessException(LinkStatus.FAILED_TO_CONNECT);
        }

        String redirectLink = urlConnection.getHeaderField("Location");
        if (redirectLink != null && !uri.equals(redirectLink)) {
            return isResponding(redirectDepth - 1);
        }

        Integer respCode;
        try {
            respCode = urlConnection.getResponseCode();
        } catch (IOException e) {
        	throw new HttpAccessException(LinkStatus.FAILED_TO_OPEN_CONNECTION);	
        }
        if (respCode != HttpURLConnection.HTTP_OK) {
        	throw new HttpAccessException(LinkStatus.HTTP_ERROR);
        }


        if (saveItem) {
            // since the link is open read it now and save the item into orgFile to avoid additional connects
            try {
                in = new BufferedInputStream(urlConnection.getInputStream());
            } catch (IOException e) {
            	throw new FileStorageException(ThumbnailStatus.FAILED_TO_BIND_TO_INPUT_STREAM);
            }
            orgFileConent = new ByteArrayOutputStream();
            int c;
            try {
                while ((c = in.read()) != -1) {
                    orgFileConent.write(c);
                }
                orgFileConent.close();
            } catch (IOException e) {
                orgFileConent = null;
            	throw new FileStorageException(ThumbnailStatus.FAILED_TO_READ_ORIG);

            }
        }
        urlConnection.disconnect();
        return true; // we just checked the link
    }


    public LinkStatus getState() {
        return state;
    }

    public String getErrorMessage() {
        return ErrorMsg; // might be empty if no specific info was available
    }



    private void initiateParams(Integer redirectDepth) {
        this.redirectDepth = redirectDepth;
        setState(LinkStatus.UNKNOWN);
    }

    protected boolean setState(LinkStatus state){  // param lazy shorthand
        return setState(state,"");
    }
    protected boolean setState(LinkStatus state, String msg){
        boolean b =  true;///(state == LinkStatus.CACHE_OK || state == LinkStatus.LINK_OK);
        this.state = state;
        ErrorMsg = msg;
        return b;
    }
}
