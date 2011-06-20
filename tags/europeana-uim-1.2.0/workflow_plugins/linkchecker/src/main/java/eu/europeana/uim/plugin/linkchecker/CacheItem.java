package eu.europeana.uim.plugin.linkchecker;


import java.io.*;
import java.util.ArrayList;

import eu.europeana.uim.plugin.linkchecker.errorcodes.LinkStatus;
import eu.europeana.uim.plugin.linkchecker.errorcodes.ThumbnailStatus;
import eu.europeana.uim.plugin.linkchecker.exceptions.FileStorageException;
import eu.europeana.uim.plugin.linkchecker.exceptions.HttpAccessException;

public class CacheItem extends CheckUrl{
    private FileTree fileTree;

    private String hashContent; // storage of org only once...
    private String hashUrl; // the generated imgs needs to be stored perl url refering them
    private String mimeType;
    private String fileType;
    private Integer orgW;
    private Integer orgH;

    ArrayList<File> createdFiles = new ArrayList<File>(); // all files created during processing of this record, remove them if final result is not ok

    public CacheItem(FileTree fileTree, String uri) {
        super(uri);
        initiateParams(fileTree);
    }
    
    public CacheItem(FileTree fileTree, String uri, Integer redirectDepth) {
        super(uri, redirectDepth);
        initiateParams(fileTree);
    }

    
    public boolean createCacheFiles() throws HttpAccessException, FileStorageException {

        if (state == LinkStatus.UNKNOWN) {
            if (!isResponding(true))  // linkcheck hasnt been done, do it now
                return false;
        }

        // ok now uri should be valid, try to use orgFile
        hashGenByContent();
        saveOrigFile();
        generateThumbTiny();
        generateThumbBrief();
        generateThumbFull();

        return true;  // not realy :)
    }



    private boolean generateThumbTiny() throws HttpAccessException{
        return false;
    }

    private boolean generateThumbBrief() throws HttpAccessException{
        return false;
    }

    private boolean generateThumbFull() throws HttpAccessException{
        return false;
    }



    private void hashGenByContent() {

        //r_hash = hashlib.sha256(item).hexdigest().upper()

        hashContent = "123ACE";
    }


    private boolean saveOrigFile() throws FileStorageException{
        File fileOrg = new File(fileTree.getOriginalFileName(hashContent));

        //Yes ok but is this a valid thing to do?
        if (fileOrg.exists()) {
            return true; // same orig already saved
        }

        try {
            FileOutputStream os = new FileOutputStream(fileOrg);
            orgFileConent.writeTo(os);
        } catch (Exception e) {

            if (fileOrg.exists()) {
                fileOrg.delete();
            }
            throw new FileStorageException(ThumbnailStatus.SAVE_ORIG_FAILED);
        }
        createdFiles.add(fileOrg);
        return true;
    }



    private void initiateParams(FileTree fileTree) {
        this.fileTree = fileTree;
    }


    protected boolean setState(LinkStatus state, String msg){
        boolean b = super.setState(state, msg);
        if ((!b) && (!(createdFiles == null))) {
            // an issue occured remove generated files
            for(File f : createdFiles) {
                f.delete();
            }
        }
        return b;
    }

}