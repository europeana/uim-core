package eu.europeana.uim.plugin.linkchecker.test;

import org.junit.Test;
import org.junit.runner.RunWith;


import eu.europeana.uim.plugin.linkchecker.CacheItem;
import eu.europeana.uim.plugin.linkchecker.CheckUrl;
import eu.europeana.uim.plugin.linkchecker.FileTree;
import eu.europeana.uim.plugin.linkchecker.exceptions.HttpAccessException;

import static org.junit.Assert.assertEquals;



public class LinkCheckerTest {
	
	@Test
	public void checklinkSuccessTest() throws Exception{

        CheckUrl cu = new CheckUrl("http://www.google.com");
        cu.isResponding(); 
    }
	
	
	
	@Test(expected = HttpAccessException.class)
	public void checklinkFailureTest() throws Exception{

        CheckUrl cu = new CheckUrl("http://www.googleeee.com/");
        cu.isResponding();
     
    }
	
	
	@Test
	public void cacheTest() throws Exception{
		
        String msg;
        String[] imgs = {"http://ogimages.bl.uk/images/019/019ADDOR0000002U00000000[SVC1].jpg",};

        FileTree fileTree = new FileTree("/tmp/thumbler_files");
        if (!fileTree.ensureFileTreeIsOk(false)) {    // param false = quick scan
            System.out.println("Filetree not in an acceptable state");
        }

        // test handling images for caching
        for (String s : imgs) {
            CacheItem ci = new CacheItem(fileTree, s);
            if (ci.createCacheFiles()) {
                msg = "ok";
            }
            else {
                msg = ci.getState().toString();
            }
            System.out.println(s + "\t" + msg);
        }
		
	}
	
	
	
	}
	


