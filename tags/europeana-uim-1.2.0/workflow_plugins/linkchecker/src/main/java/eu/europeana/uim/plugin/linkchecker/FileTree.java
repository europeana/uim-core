package eu.europeana.uim.plugin.linkchecker;

import java.io.File;


/*

You are expected to call ensureFileTreeIsOk() before any other usage!

 */

public class FileTree {
    String basePath;

    private String DIR_ORG = "orginal";
    private String DIR_BRIEF = "BRIEF_DOC";
    private String DIR_FULL = "FULL_DOC";
    private String DIR_TINY = "TINY";

    public FileTree(String basePath) {
        this.basePath = basePath;
    }

    public String getOriginalFileName(String hashFileName) {
        return basePath + "/" + DIR_ORG + "/" + getHashRelName(hashFileName);
    }

    public String getBriefDocFileName(String hashFileName) {
        return basePath + "/" + DIR_BRIEF + "/" + getHashRelName(hashFileName);
    }

    public String getFullDocFileName(String hashFileName) {
        return basePath + "/" + DIR_FULL + "/" + getHashRelName(hashFileName);
    }

    public String getTinyFileName(String hashFileName) {
        return basePath + "/" + DIR_TINY + "/" + getHashRelName(hashFileName);
    }

    public String getHashRelName(String  hashFileName) {
        return hashFileName.substring(0,2) + "/" + hashFileName.substring(2,4) + "/" + hashFileName;
    }



    public boolean ensureFileTreeIsOk() {
        return ensureFileTreeIsOk(true);
    }
    /*
       If fullScan is false, the hexdirs wont be checked, only use this for testing purposes...
     */
    public boolean ensureFileTreeIsOk(boolean fullScan) {
        String[] subTrees = {DIR_ORG, DIR_BRIEF, DIR_FULL, DIR_TINY};
        File relBase;

        if (!ensureBasePathIsReasonable()) {
            System.out.println("basepath is invalid: " + basePath);
            return false;
        }

        for (String subTree : subTrees) {
            relBase = new File(basePath + "/" + subTree);
            if (!relBase.exists()) {
                if (!relBase.mkdir()) {
                    System.out.println("Failed to create: " + relBase.toString());
                    return false;
                }
            }
            if (fullScan) {
                if (!ensureHexDirs(relBase.toString(), true)) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean ensureHexDirs(String relBase, boolean recursion) {
        String[] hex = {"0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F"};
        File examDir;

        for (String h1 : hex) {
            for (String h2 : hex) {
                examDir = new File(relBase + "/" + h1 + h2);
                if (!examDir.exists()) {
                    if (!examDir.mkdir()) {
                        System.out.println("Failed to create: " + examDir.toString());
                        return false;
                    }
                }
                if (recursion) {
                    if (!ensureHexDirs(examDir.toString(), false)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }


    private boolean ensureBasePathIsReasonable() {
        String[] illegalPaths = {"", "/", "/etc", "/root"};

        if (!(new File(basePath)).exists()) {
            return false;
        }

        for (String p : illegalPaths) {
            if (basePath.equals(p)) {
                return false;
            }
        }
       return true;
    }
}
